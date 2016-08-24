package io.element.app;

import io.element.room.impl.GM_RoomManager;
import io.element.server.GM_Mangers;

import java.util.Calendar;

public class AppRunThread implements Runnable {

	private boolean m_bStart = false;		// 线程开始标记，防止重复开启
		
	private boolean m_bExit = false;		// 线程结束标记，主动通知线程退出
	 
	private static final AppRunThread single = new AppRunThread();  
	 
	private AppRunThread() { }  
	
	public void stop(){ m_bExit = true; }
	    
	public static AppRunThread getInstance() {  
		return single;  
	}  

	public void run() {
		
        GM_RoomManager room_mgr = GM_Mangers.getRoomManager();
        
        long tCurrentTime = Calendar.getInstance().getTimeInMillis();
        while(true)
        {  
        	if(m_bExit)
        		break;
        	
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        	// 片段化时间
        	long tDeltaTime = Calendar.getInstance().getTimeInMillis() - tCurrentTime;
        	tCurrentTime = Calendar.getInstance().getTimeInMillis();
        	
        	// 房间管理器
        	room_mgr.OnTick(tDeltaTime);

        }
	}

	public void start()
	{
		if(m_bStart)
			return;
		m_bStart = true;
		
		Thread runner = new Thread(getInstance());
		runner.setName("game manager");
		runner.start();
	}
	
}
