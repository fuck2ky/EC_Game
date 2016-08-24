package io.element.util;

import io.element.gtserver.App;
import io.element.player.BasePlayer;
import io.element.player.Player;
import io.element.protobuf.GlobalProto;
import io.element.protobuf.LoginProto;
import io.element.protobuf.SimpleProto;
import io.element.room.BaseRoom;
import io.element.room.Room;
import io.element.room.impl.SessionRoom;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.google.protobuf.ByteString;

public class HandlerUtil {
	
	public enum PROTOBUF_GERENAL{ LOGIN,SIMPLE, }
	
	// test use -- state info record in txt
	public static void recordStateInfo( long roomid, String info)
	{
		File file = new File("logs\\roomid_"+roomid+".txt");
		if(!file.exists()){//判断文件是否存在  
            try {  
                file.createNewFile();  //创建文件 
            } catch (IOException e) {  
                e.printStackTrace();  
            }
        }
		
		try {
			FileOutputStream out = new FileOutputStream(file,true);
			out.write( new String(info+"\r\n").getBytes() );
			out.close();
		}catch (IOException e) {  
            e.printStackTrace();  
        }
		
		App.LOGGER.info(info);
	}
	
	public static Method getMethod(String str_class, String str_method ) throws Exception
	{
		Class<?> c = Class.forName(str_class);   
		Method method = c.getMethod(str_method,Object.class);  
		
		return method;
	}
	
	public static Method getMethod(String str_class, String str_method, Class<?> para1_class ) throws Exception
	{
		Class<?> c = Class.forName(str_class);
		Method method = c.getMethod(str_method, para1_class);
		
		return method;
	}
	
	public static Method getMethod(String str_class, String str_method, Class<?> para1_class, Class<?> para2_class  ) throws Exception
	{
		Class<?> c = Class.forName(str_class);
		Method method = c.getMethod(str_method, para1_class, para2_class);
		
		return method;
	}
	
	public static Method getDeclaredMethod(String str_class, String str_method ) throws Exception
	{
		Class<?> c = Class.forName(str_class);   
		Method method = c.getDeclaredMethod(str_method,Object.class);  
		
		return method;
	}
	
	//***********************************************
	//  
	// gate -- server, handler util for protobuf
	// 
	//***********************************************
	
	public static Object CREATE_G2S_GLOBALMESSAGE(LoginProto.G2S_MSGTYPE type, ByteString buffer)
	{
		LoginProto.G2SByteStream object = (LoginProto.G2SByteStream) CREATE_G2S_LOGINMESSAGE(type,buffer);
		return CREATE_G2S_GLOBALMESSAGE(GlobalProto.MESSAGE.MESSAGE_LOGIN_G2S, object.toByteString());
	}
	
	public static Object CREATE_G2S_GLOBALMESSAGE(GlobalProto.MESSAGE type, ByteString buffer)
	{
		GlobalProto.MessageStream.Builder mBuilder = GlobalProto.MessageStream.newBuilder();
		return mBuilder.setType(type).setRequestData(buffer).build();
	}
	
	public static Object CREATE_G2S_LOGINMESSAGE(LoginProto.G2S_MSGTYPE type, ByteString buffer)
	{
		LoginProto.G2SByteStream.Builder lBuilder = LoginProto.G2SByteStream.newBuilder();
		return lBuilder.setType(type).setRequestData(buffer).build();
	}
	
	public static Object CREATE_G2S_SIMPLEMESSAGE(SimpleProto.G2S_MSGTYPE type, ByteString buffer)
	{
		SimpleProto.G2SByteStream.Builder sBuilder = SimpleProto.G2SByteStream.newBuilder();
		return sBuilder.setType(type).setRequestData(buffer).build();
	}
	
	public static ByteString getRoomProtoBuf(Room<?,?> entity, PROTOBUF_GERENAL gerenal)
	{
		if(	entity.type() == LoginProto.ROOM_TYPE.LOGIN_ROOMTYPE_SIMPLE_2V2 ||
			entity.type() == LoginProto.ROOM_TYPE.LOGIN_ROOMTYPE_SIMPLE_3V3	)
		{	
			if( gerenal == PROTOBUF_GERENAL.SIMPLE )
				return new HandlerUtil().getSimpleRoom(entity).toByteString();
			else
				return new HandlerUtil().getLoginRoom(entity).toByteString();
		}
		
		return null;
	}
	
	protected LoginProto.Player getLoginPlayer(Player p,BaseRoom room)
	{
		LoginProto.Player.Builder pBuilder = LoginProto.Player.newBuilder();
		LoginProto.Player sPlayer = pBuilder.setId(p.GetPlayerGuid())
											 .setName(p.GetPlayerName())
											 .setRoleid(0)
											 .setLocation(p.GetLocation())
											 .setRoomid(room.getID()).build();
		return sPlayer;
	}
	
	public LoginProto.Room getLoginRoom(Room<?, ?> entity)
	{
		LoginProto.Room.Builder rBuilder = LoginProto.Room.newBuilder();
		
		SessionRoom   room 	= (SessionRoom) entity;
		
		// create player list
		ArrayList<LoginProto.Player> list = new ArrayList<LoginProto.Player>();
		final List<BasePlayer> players = room.getPlayers();
		for ( BasePlayer player : players ) { 
			LoginProto.Player lPlayer = this.getLoginPlayer(player, room);
			list.add(lPlayer);
		}
		
		LoginProto.Room lRoom = rBuilder.setId(room.getID())
										.setType(room.type())
										.setName(room.getName())
										.addAllPlayers(list).build();
		return lRoom;
	}
	
	public SimpleProto.Room getSimpleRoom(Room<?,?> entity)
	{
		return null;
	}
	
	//***********************************************
	//  
	// client -- gate, handler util for protobuf
	// 
	//***********************************************
	
	public static Object CREATE_G2C_LOGINMESSAGE(LoginProto.G2C_MSGTYPE type, ByteString buffer)
	{
		LoginProto.G2CByteStream.Builder lBuilder = LoginProto.G2CByteStream.newBuilder();
		return lBuilder.setType(type).setRequestData(buffer).build();
	}
	
	public static Object CREATE_G2C_SIMPLEMESSAGE(SimpleProto.G2C_MSGTYPE type, ByteString buffer)
	{
		return null;
	}
	
	public static Object CREATE_G2C_GLOBALMESSAGE(LoginProto.G2C_MSGTYPE type, ByteString buffer)
	{
		LoginProto.G2CByteStream object = (LoginProto.G2CByteStream) CREATE_G2C_LOGINMESSAGE(type,buffer);
		return GlobalProto.MessageStream.newBuilder()
				.setType( GlobalProto.MESSAGE.MESSAGE_LOGIN_G2C ).setRequestData(object.toByteString()).build();
	}
	

}
