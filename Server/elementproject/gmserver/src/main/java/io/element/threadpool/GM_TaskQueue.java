package io.element.threadpool;

import io.element.app.App;
import io.element.event.Message;
import io.element.event.impl.BaseMessage;
import io.element.event.impl.BaseTask;
import io.element.room.impl.BaseRoom;
import io.element.server.GM_Mangers;


import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;


public class GM_TaskQueue implements Runnable{
	
	protected GM_TaskQueueConfig 				m_config;
	
	private LinkedBlockingQueue<BaseTask> 		m_tasksInsert;
	
	private NotifyingBlockingThreadPoolExecutor m_executor = null;
	
	private boolean								m_bQuit = false; 
	
	public GM_TaskQueue(GM_TaskQueueConfig config)
	{
		m_config = config;
	}
	
	public void init()
	{
		m_tasksInsert = new LinkedBlockingQueue<BaseTask>(m_config.getQueueCapacity());
		
		int queueSize = 50; // recommended - twice the size of the poolSize
		int threadKeepAliveTime = 15;
		TimeUnit threadKeepAliveTimeUnit = TimeUnit.SECONDS;
		int maxBlockingTime = 10;
		TimeUnit maxBlockingTimeUnit = TimeUnit.MILLISECONDS;
		Callable<Boolean> blockingTimeoutCallback = new Callable<Boolean>() {
			
			public Boolean call() throws Exception {				
				App.LOGGER.info("GM_TaskQueue is full! we just want give up a task!!");
				return true; // keep waiting return true  or return false stop waiting
			}
		};
		
		m_executor =
				new NotifyingBlockingThreadPoolExecutor( m_config.getThreadCapacity(), 
														 queueSize,
														 threadKeepAliveTime, 
														 threadKeepAliveTimeUnit,
														 maxBlockingTime, 
														 maxBlockingTimeUnit, 
														 blockingTimeoutCallback);
	}

	public void run() {
		// TODO Auto-generated method stub
		while(true)
		{
			if(m_bQuit)
				break;
			
			BaseTask task = null;
			
			try {
				 task = this.get_task();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			FutureTask<Integer> task_future = new FutureTask<Integer>(task);
			
			// add into spec room for tick 
			// like roommanager instance get room id by task and set future into room
			BaseRoom room = GM_Mangers.getRoomManager().getRoomByID(task.roomid());
			BaseMessage msg = new BaseMessage(Message.MESSGAE_TYPE.SERVER_SET_FUTURE_TASK,task_future);
			room.HandleMsg(msg);
			
			m_executor.submit(task_future);
		}
	}
		
	//-----------------------------------------------------------------
	// Add task to the NotifyingBlockingThreadPoolExecutor & return future -- producer
	//-----------------------------------------------------------------	
	public void push_task( BaseTask task ) throws RejectedExecutionException
	{
		boolean result = m_tasksInsert.offer((BaseTask)task);
		if(!result)
			throw new RejectedExecutionException("the work thread preinsert is full!!!");
			
		return;
	}
	
	//-----------------------------------------------------------------
	//  Get task to the NotifyingBlockingThreadPoolExecutor & with future -- consumer
	//-----------------------------------------------------------------	
	protected BaseTask get_task()  throws Exception
	{
		BaseTask task = null;	
		task = m_tasksInsert.take();		//blocking if empty
		return task; 
	}
}
