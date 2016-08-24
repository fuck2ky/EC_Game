package io.element.app;

import io.element.dataman.ElementDataMan;
import io.element.server.GM_Mangers;
import io.element.server.impl.NettyTCPServer;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.slf4j.Logger;

public class App 
{
	public static final Logger LOGGER =  LoggerFactory.getLogger(App.class);
	
	public static  ApplicationContext context = null;
	
    public static void main( String[] args )
    {        	
    	PropertyConfigurator.configure("log4j.properties");
    	
        App.context = 
        		new AnnotationConfigApplicationContext(GM_ServerSpringConfig.class);
        LOGGER.info("Loading java bean resources completed!!!");
        
        if( !ElementDataMan.getInstance().Load() ){
        	LOGGER.info("Loading script resources failed");
        	return;
        }
        LOGGER.info("Loading script resources completed!!!");
        
        GM_Mangers.getInstance().init();
        LOGGER.info("Loading Game Managers for initialize completed!!!!");
        
		NettyTCPServer server = (NettyTCPServer) App.context.getBean("tcpServer");
        try {
			server.startServer();
			LOGGER.info("TCP Server start success!");
		} catch (Exception e1) {
			LOGGER.error("TCP Server start error {}, going to shut down", e1);
			return;
		}
        
        AppRunThread ticker = AppRunThread.getInstance();
        ticker.start();
        LOGGER.info("Game server main tick thread start!!");
        
        try {
			server.sync();
			server.stopServer();
			ticker.stop();
		} catch (InterruptedException e1) {
			LOGGER.error("TCP Server run error with InterruptedException: {}, going to shut down", e1.getMessage());
		} catch (Exception e) {
			LOGGER.error("TCP Server stop error in stopserver method: {}, going to shut down", e.getMessage());
		}
    }
}
