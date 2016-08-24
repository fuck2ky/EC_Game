package io.element.gtserver;

import java.util.Calendar;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;



import io.element.server.impl.NettyTCPServer;


public class App 
{	
	public static  ApplicationContext context = null;
	
	public static final Logger LOGGER =  LoggerFactory.getLogger(App.class);
	
    public static void main( String[] args ) throws Exception
    {    	    	
    	PropertyConfigurator.configure("log4j.properties");
    	
        App.context = 
        		new AnnotationConfigApplicationContext(GT_ServerSpringConfig.class);
        LOGGER.info("GATE -- Loading java bean resources completed!!!");
        
        
        GT_Managers.getInstance().init();
        LOGGER.info("GATE -- Loading Game Managers for initialize completed!!!!");
        
		NettyTCPServer server = (NettyTCPServer) App.context.getBean("tcpServer");
        try {
			server.startServer();
			LOGGER.info("GATE -- TCP Server start success!");
		} catch (Exception e1) {
			LOGGER.error("GATE -- TCP Server start error {}, going to shut down", e1);
			return;
		}
        
        long tCurrentTime = Calendar.getInstance().getTimeInMillis();
        while (true)
        {
        	if( server.isClosed() )
        		break;
        	        	
        	// next delta time
        	long tDeltaTime = Calendar.getInstance().getTimeInMillis() - tCurrentTime;
        	tCurrentTime = Calendar.getInstance().getTimeInMillis();
        	
        	// 模拟长时间操作
			Thread.sleep(1000);
        	
        	// room tick
        	GT_RoomManager roomMgr = GT_Managers.getRoomManager();
        	if( roomMgr != null ) 	roomMgr.OnTick(tDeltaTime);
        	
        	// lobby players tick  
        	GT_PlayerManager playerMgr = GT_Managers.getPlayerManager();
        	if( playerMgr != null )	playerMgr.OnTick(tDeltaTime);
        	
        	// fitness mgr tick 匹配器定时匹配
        	GT_MatchManager matchMgr = GT_Managers.getMatchManager();
        	if( matchMgr != null ) matchMgr.OnTick(tDeltaTime);
        	
        }
        
        try {
			server.sync();
			server.stopServer();
		} catch (InterruptedException e1) {
			LOGGER.error("TCP Server run error with InterruptedException: {}, going to shut down", e1.getMessage());
		} catch (Exception e) {
			LOGGER.error("TCP Server stop error in stopserver method: {}, going to shut down", e.getMessage());
		}
        
    }
}
