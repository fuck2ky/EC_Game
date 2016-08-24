package io.element.gtserver;

import io.element.threadpool.GT_TaskQueue;

public class GT_Managers {
	
	public interface Manager{
		
		public void init();
		
		public void OnTick(long deltaTime);
		
	}

	private static class LazyHolder {    
		private static final GT_Managers INSTANCE = new GT_Managers();    
	}    
	
	private GT_Managers (){
	} 
	
	public static final GT_Managers getInstance() {    
		return LazyHolder.INSTANCE;    
	}
	
	private GT_TaskQueue m_queue = null;
	
	private GT_RoomManager m_roommgr = null;
	
	private GT_MatchManager m_marchmgr = null;
	
	private GT_PlayerManager m_playermgr = null;
	
	public void init()
	{
		m_queue = (GT_TaskQueue) App.context.getBean("taskqueue");
		m_queue.init();
		
		m_roommgr = (GT_RoomManager) App.context.getBean("roommgr");
		m_roommgr.init();
		
		m_marchmgr = (GT_MatchManager) App.context.getBean("marchmgr");
		m_marchmgr.init();
		
		m_playermgr = (GT_PlayerManager) App.context.getBean("playermgr");
		m_playermgr.init();
		
		startTaskPool();
	}
	
	public static final GT_TaskQueue getTaskQueue()
	{
		return LazyHolder.INSTANCE.m_queue;
	}
	
	public static final GT_RoomManager getRoomManager()
	{
		return LazyHolder.INSTANCE.m_roommgr;
	}
	
	public static final GT_MatchManager getMatchManager()
	{
		return LazyHolder.INSTANCE.m_marchmgr;
	}
	
	public static final GT_PlayerManager getPlayerManager()
	{
		return LazyHolder.INSTANCE.m_playermgr;
	}
	
	public void startTaskPool()
	{
		Thread thread = new Thread(m_queue);
		thread.setName("task pool");
		thread.start();
	}
	
}
