package io.element.gtserver;

import io.element.player.BasePlayer;
import io.element.protobuf.LoginProto;
import io.element.protobuf.LoginProto.ROOM_TYPE;
import io.element.reflect.Handler;
import io.element.room.impl.SessionRoom;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import junit.framework.TestCase;

public class SingleRoomTest extends TestCase{

	public SingleRoomTest(String testMethod) {  
        super(testMethod);  
    } 
	
    protected void setUp() throws Exception {

    }
	
	public void testSingleRoom() throws InterruptedException
	{
    	PropertyConfigurator.configure("log4j.properties");
    	
        App.context = 
        		new AnnotationConfigApplicationContext(GT_ServerSpringConfig.class);
        
        GT_Managers.getInstance().init();
                
        int index = 10000;
        int playersnum = 4;
        int roomindex = 20000;
             
        List<BasePlayer> players = new ArrayList<BasePlayer>();
        for (int i = 0; i < playersnum; i++) {
        	BasePlayer player = new BasePlayer(null);
        	player.SetPlayerGuid(index);
        	player.SetLocation(index % playersnum);
        	
        	index++;
        	
        	player.SetPlayerName("player:" + new Integer(index).toString());
        	players.add(player);
		}
        
        SessionRoom room = new SessionRoom(roomindex,"room:"+new Integer(roomindex).toString(),ROOM_TYPE.LOGIN_ROOMTYPE_SIMPLE_2V2);
        for (int i = 0; i < players.size(); i++) {
			room.addPlayer(players.get(i));
		}

        Handler handler = room.steering().ReflectHandler( LoginProto.G2S_MSGTYPE.G2S_REQUEST_CREATE_NEWROOM);
        handler.SetParam( new Object() );
        GT_Managers.getRoomManager().addRoom(room);
        
        room.steering().connect(handler);
        
      
        for (int i=0; i<100000; i++)
        	Thread.sleep(1000);
        assertTrue( true );
	}
	
}
