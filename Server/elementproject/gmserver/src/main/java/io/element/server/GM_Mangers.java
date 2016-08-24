package io.element.server;

import io.element.app.App;
import io.element.room.impl.GM_RoomManager;
import io.element.threadpool.*;

public class GM_Mangers {

	private static class LazyHolder {    
		private static final GM_Mangers INSTANCE = new GM_Mangers();    
	}    
	
	private GM_Mangers (){
	
	} 
	
	public static final GM_Mangers getInstance() {    
		return LazyHolder.INSTANCE;    
	}

	public void init()
	{
		m_queue = (GM_TaskQueue) App.context.getBean("taskqueue");
		m_queue.init();
		
		m_rooms = (GM_RoomManager) App.context.getBean("roomManagers");
		m_rooms.init();
		
		startTaskPool();
	}
	    
	private GM_TaskQueue m_queue = null;

	/**
	 * @return Returns the unique task queue in single instance
	 */
	public static final GM_TaskQueue getTaskQueue()
	{
		return LazyHolder.INSTANCE.m_queue;
	}
	
	public void startTaskPool()
	{
		Thread thread = new Thread(m_queue);
		thread.setName("task pool");
		thread.start();
	}
	
	private GM_RoomManager m_rooms = null;
	
	/**
	 * @return Returns the unique task queue in single instance
	 */
	public static final GM_RoomManager getRoomManager()
	{
		return LazyHolder.INSTANCE.m_rooms;
	}

	
}
