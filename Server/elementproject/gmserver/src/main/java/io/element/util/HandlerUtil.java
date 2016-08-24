package io.element.util;

import io.element.player.Player;
import io.element.protobuf.GlobalProto;
import io.element.protobuf.LoginProto;
import io.element.protobuf.SimpleProto;
import io.element.protobuf.SimpleProto.Reactor;
import io.element.room.*;
import io.element.room.impl.EntityManager;
import io.element.room.impl.GM_BaseRoom;
import io.element.room.impl.EntityManager.TEAM_FLAG;
import io.element.room.impl.GM_SimpleRoom;
import io.element.state.State.InteractiveInfo;
import io.element.state.State.OperateValue;
import io.element.state.State.PerformInfo;
import io.element.state.global.impl.SimplePhaseState;
import io.element.state.impl.BaseStateMachine;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.util.Map.Entry;

import com.google.protobuf.ByteString;

public class HandlerUtil {
	
	public enum PROTOBUF_GERENAL{ LOGIN,SIMPLE, }
	
	public static Method getMethod(String str_class, String str_method ) throws Exception
	{
		Class<?> c = Class.forName(str_class);   
		Method method = c.getMethod(str_method,Object.class);  
		
		return method;
	}
	
	public static GlobalProto.MessageStream CREATE_S2G_GLOBALMESSAGE(GlobalProto.MESSAGE type, ByteString buffer)
	{
		GlobalProto.MessageStream.Builder mBuilder = GlobalProto.MessageStream.newBuilder();
		return mBuilder.setType(type).setRequestData(buffer).build();
	}
	
	public static LoginProto.S2GByteStream CREATE_S2G_LOGINMESSAGE(LoginProto.S2G_MSGTYPE type, ByteString buffer)
	{
		LoginProto.S2GByteStream.Builder lBuilder = LoginProto.S2GByteStream.newBuilder();
		return lBuilder.setType(type).setRequestData(buffer).build();
	}
	
	public static SimpleProto.S2GByteStream CREATE_S2G_SIMPLEMESSAGE(SimpleProto.S2G_MSGTYPE type, ByteString buffer)
	{
		SimpleProto.S2GByteStream.Builder sBuilder = SimpleProto.S2GByteStream.newBuilder();
		return sBuilder.setType(type).setRequestData(buffer).build();
	}
	
	public static ByteString getRoomProtoBuf(Room entity, PROTOBUF_GERENAL gerenal)
	{
		if(	entity.Type() == LoginProto.ROOM_TYPE.LOGIN_ROOMTYPE_SIMPLE_2V2 ||
			entity.Type() == LoginProto.ROOM_TYPE.LOGIN_ROOMTYPE_SIMPLE_3V3	)
		{	
			if( gerenal == PROTOBUF_GERENAL.SIMPLE )
				return new HandlerUtil().getSimpleRoom(entity).toByteString();
			else
				return new HandlerUtil().getLoginRoom(entity).toByteString();
		}
		
		return null;
	}
		
	public static ByteString getTeamProtoBuf(Room entity)
	{
		if(	entity.Type() == LoginProto.ROOM_TYPE.LOGIN_ROOMTYPE_SIMPLE_2V2 ||
			entity.Type() == LoginProto.ROOM_TYPE.LOGIN_ROOMTYPE_SIMPLE_3V3	)
			return new HandlerUtil().getSimpleTeam(entity).toByteString();
		
		return null;
	}
	
	public LoginProto.Room getLoginRoom(Room entity)
	{
		LoginProto.Room.Builder rBuilder = LoginProto.Room.newBuilder();
		
		GM_BaseRoom   room 	= (GM_BaseRoom) entity;
		EntityManager mgr 	= (EntityManager) room.getEntityManager(); 
		
		// create player list
		ArrayList<LoginProto.Player> list = new ArrayList<LoginProto.Player>();
		final HashMap<Integer,Player> players = mgr.getPlayers();
		for (Entry<Integer, Player> entry : players.entrySet()) { 
			LoginProto.Player player = this.getLoginPlayer(entry.getValue(), room);
			list.add(player);
		}
		
		LoginProto.Room lRoom = rBuilder.setId(room.ID())
										.setType(room.Type())
										.setName(room.getRoomName())
										.addAllPlayers(list).build();
		return lRoom;
	}
	
	public SimpleProto.Room getSimpleRoom(Room entity)
	{
		SimpleProto.Room.Builder rBuilder = SimpleProto.Room.newBuilder();
		
		GM_SimpleRoom room 				= (GM_SimpleRoom) entity;
		
		SimpleProto.Active 	active 	= this.getSimpleActive(entity);
		SimpleProto.Team	team 	= this.getSimpleTeam(entity);
		
		SimpleProto.Room 	sRoom 	= rBuilder.setId(room.ID())
										 	  .setType(this.transType(room.Type()))
										 	  .setName(room.getRoomName())
										 	  .setPlayers(team)
										 	  .setActive(active).build();
		return sRoom;
	}
	
	protected SimpleProto.Team getSimpleTeam(Room entity)
	{
		SimpleProto.Team.Builder tBuilder 	= SimpleProto.Team.newBuilder();
		
		GM_SimpleRoom room 					= (GM_SimpleRoom) entity;
		EntityManager mgr 					= (EntityManager) room.getEntityManager();
		
		ArrayList<SimpleProto.Player> redList  = new ArrayList<SimpleProto.Player>();
		ArrayList<SimpleProto.Player> blueList = new ArrayList<SimpleProto.Player>();
		
		//Create team info
		final HashMap<Integer,Player> players = mgr.getPlayers();
		for (Entry<Integer, Player> entry : players.entrySet()) { 
			Player elementPlayer = entry.getValue();
			TEAM_FLAG campFlag = EntityManager.getTeamFlag( room.Type(),elementPlayer.GetLocation());
			if(campFlag == TEAM_FLAG.TEAM_RED )
				redList.add( new HandlerUtil().getSimplePlayer(elementPlayer) );
			else 
				blueList.add( new HandlerUtil().getSimplePlayer(elementPlayer));
		}
			
		return tBuilder.addAllTeamRed(redList).addAllTeamBlue(blueList).build();
	}
	
	protected SimpleProto.Active getSimpleActive(Room entity)
	{
		SimpleProto.Active.Builder aBuilder = SimpleProto.Active.newBuilder();
		
		GM_SimpleRoom room 				= (GM_SimpleRoom) entity;
		EntityManager mgr 				= (EntityManager) room.getEntityManager();
		BaseStateMachine stateMachine	= (BaseStateMachine) room.getStateMachine();
		SimplePhaseState sState 		= (SimplePhaseState)stateMachine.GetPhaseState();
		
		SimpleProto.Active active = aBuilder.setPlayerid(mgr.getActivePlayer().GetPlayerGuid())
											.setRoomid(room.ID())
											.setPhase(sState.GetPhaseState()).build();
		return active;
	}
	
	protected LoginProto.Player getLoginPlayer(Player p,Room room)
	{
		LoginProto.Player.Builder pBuilder = LoginProto.Player.newBuilder();
		LoginProto.Player sPlayer = pBuilder.setId(p.GetPlayerGuid())
											 .setName(p.GetPlayerName())
											 .setRoleid(0)
											 .setLocation(p.GetLocation())
											 .setRoomid(room.ID()).build();
		return sPlayer;
	}
	
	protected SimpleProto.Player getSimplePlayer(Player p)
	{
		SimpleProto.Player.Builder pBuilder = SimpleProto.Player.newBuilder();
		SimpleProto.Player sPlayer = pBuilder.setId(p.GetPlayerGuid())
											 .setName(p.GetPlayerName())
											 .setRoleid(0)							// !!!!!!  no has now
											 .setLocation(p.GetLocation()).build();
		return sPlayer;
	}

	protected SimpleProto.ROOM_TYPE transType(LoginProto.ROOM_TYPE type)
	{
		if( type == LoginProto.ROOM_TYPE.LOGIN_ROOMTYPE_SIMPLE_2V2 )
			return SimpleProto.ROOM_TYPE.SIMPLE_ROOMTYPE_SIMPLE_2V2;
		else if( type == LoginProto.ROOM_TYPE.LOGIN_ROOMTYPE_SIMPLE_3V3 )
			return SimpleProto.ROOM_TYPE.SIMPLE_ROOMTYPE_SIMPLE_3V3;
		
		return null;
	}
	
	// card interactive apis
	public static SimpleProto.Reactor getReactor(InteractiveInfo info)
	{
		SimpleProto.Reactor.Builder builder = SimpleProto.Reactor.newBuilder();
		SimpleProto.Reactor reactor = builder.setPlayerid( info.pids.get(0).pid )
											 .setType(info.einfo.inavType.toValue())
											 .build();
		return reactor;		
	}
	
	public static SimpleProto.Reactor getReactor(InteractiveInfo info, int playerIndex,
										Vector<OperateValue> values)
	{
		SimpleProto.Reactor.Builder builder = SimpleProto.Reactor.newBuilder();
		builder.setPlayerid( info.pids.get(0).pid )
		 	   .setType(info.einfo.inavType.toValue());
		
		for (OperateValue value : values) {
			
			SimpleProto.OperateValue.Builder oBuilder = SimpleProto.OperateValue.newBuilder();
			oBuilder.setValue(value.value);
			if(value.preValue != null)
				oBuilder.setPrevalue(value.preValue);
			
			builder.addValues(oBuilder.build());
		}
		
		return builder.build();
	}
	
	public static SimpleProto.Reactor getReactor(InteractiveInfo info, int playerIndex,
										OperateValue value)
	{
		SimpleProto.Reactor.Builder builder = SimpleProto.Reactor.newBuilder();
		builder.setPlayerid( info.pids.get(0).pid )
		 	   .setType(info.einfo.inavType.toValue());
		
		SimpleProto.OperateValue.Builder oBuilder = SimpleProto.OperateValue.newBuilder();
		oBuilder.setValue(value.value);
		if(value.preValue != null)
			oBuilder.setPrevalue(value.preValue);
		
		builder.addValues(oBuilder.build());
		
		return builder.build();
	}
	
	public static SimpleProto.ReactorInfo getReactorInfo(PerformInfo info ) throws Exception
	{
		SimpleProto.ReactorInfo.Builder builder = SimpleProto.ReactorInfo.newBuilder();
		
		InteractiveInfo temp = null;
		for (int i = 0; i < info.recvs.size(); i++) {
			
			InteractiveInfo iInfo = info.recvs.get(i);
			if( iInfo.performed() == false )
			{
				temp = iInfo;
				continue;
			}
		}
		
		if( temp == null )
			throw new Exception("null interactive performed!!!!");

		return builder.setType( info.categoryType().reactor())
					  .setId( info.cid )
					  .setHostid( info.pid )
					  .setActor( getReactor(temp) )
					  .build();
	}
	
	public static SimpleProto.ReactorInfo getReactorInfo(PerformInfo info , int index ) throws Exception 
	{
		SimpleProto.ReactorInfo.Builder builder = SimpleProto.ReactorInfo.newBuilder();
		if(index >= info.recvs.size())
			throw new Exception("null interactive performed!!!!");
		
		InteractiveInfo temp = info.recvs.get(index);
		return builder.setType( info.categoryType().reactor())
		   	   		  .setId( info.cid )
		   	   		  .setHostid( info.pid )
		   	   		  .setActor( getReactor(temp) )
		   	   		  .build();
	}
	
	public static SimpleProto.ReactorInfo getReactorInfo(PerformInfo info , Reactor actor )
	{
		SimpleProto.ReactorInfo.Builder builder = SimpleProto.ReactorInfo.newBuilder();
		
		return builder.setType( info.categoryType().reactor())
	   	   		  .setId( info.cid )
	   	   		  .setHostid( info.pid )
	   	   		  .setActor( actor )
	   	   		  .build();
	}
	
}





















