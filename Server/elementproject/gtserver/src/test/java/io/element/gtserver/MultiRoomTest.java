package io.element.gtserver;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import io.element.player.BasePlayer;
import io.element.protobuf.LoginProto;
import io.element.protobuf.LoginProto.ROOM_TYPE;
import io.element.reflect.Handler;
import io.element.room.impl.SessionRoom;
import io.element.util.HandlerUtil;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import junit.framework.TestCase;

public class MultiRoomTest extends TestCase{

	public MultiRoomTest(String testMethod) {  
        super(testMethod);  
    } 
	
    protected void setUp() throws Exception {

    }
    
    public void closeCallBack(Object obj)
    {
    	System.out.println("hahahahhahahahah");
    }
	
	public void testMultiRoom() throws InterruptedException
	{
    	PropertyConfigurator.configure("log4j.properties");
    	
        App.context = 
        		new AnnotationConfigApplicationContext(GT_ServerSpringConfig.class);
        
        GT_Managers.getInstance().init();
  
        Integer index_begin = 10000;
        Integer roomindex_begin = 20000;
        Integer playersnum = 4;

        int roomnum = 3;
        
        SessionRoom preClosedRoom = null;
        
        for(int i=0;i<roomnum;++i)
        {
        	SessionRoom room = createRoom(index_begin, roomindex_begin, playersnum);
            Handler handler = room.steering().ReflectHandler( LoginProto.G2S_MSGTYPE.G2S_REQUEST_CREATE_NEWROOM);
            room.steering().connect(handler);
            
            index_begin = index_begin + playersnum;
            roomindex_begin ++;
            
            preClosedRoom = room;
        }
     
        for (int i=0; i<100000; i++)
        {	
        	if( i == 30 )
        	{
        		Method method = null;
        		try {
					 method = HandlerUtil.getMethod(this.getClass().getName(), "closeCallBack");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		
        		Handler handler = new Handler(method);
        		handler.SetObject(this);
        		handler.SetParam(null);
        		
        		preClosedRoom.steering().close();
        		
        	}
        	
        	Thread.sleep(1000);
        }
        
        assertTrue( true );
	}
	
	private SessionRoom createRoom(Integer pIndex, Integer rIndex, Integer num ) {
		
        List<BasePlayer> players = new ArrayList<BasePlayer>();
        for (int i = 0; i < num; i++) {
        	BasePlayer player = new BasePlayer(null);
        	player.SetPlayerGuid(pIndex++);
        	player.SetLocation(pIndex % num);
        	player.SetPlayerName("player:" + new Integer(pIndex).toString());
        	players.add(player);
		}
        
        SessionRoom room = new SessionRoom(rIndex++,"room:"+new Integer(rIndex).toString(),ROOM_TYPE.LOGIN_ROOMTYPE_SIMPLE_2V2);
        for (int i = 0; i < players.size(); i++) {
			room.addPlayer(players.get(i));
		}
               
		return room;
	}
}
