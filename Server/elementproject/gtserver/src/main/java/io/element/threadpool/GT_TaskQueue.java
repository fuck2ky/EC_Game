package io.element.threadpool;

import io.element.gtserver.App;
import io.element.task.BaseTask;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;


public class GT_TaskQueue implements Runnable{
	
	protected GT_TaskQueueConfig 				m_config;
	
	private LinkedBlockingQueue<BaseTask<?,?>> 	m_tasksInsert;
	
	private NotifyingBlockingThreadPoolExecutor m_executor = null;
	
	private boolean								m_bQuit = false; 
	
	public GT_TaskQueue(GT_TaskQueueConfig config)
	{
		m_config = config;
	}
	
	public void init()
	{
		m_tasksInsert = new LinkedBlockingQueue<BaseTask<?,?>>(m_config.getQueueCapacity());
		
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
			
			BaseTask<?,?> task = null;
			try {
				 task = this.get_task();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			FutureTask<Boolean> task_future = new FutureTask<Boolean>(task);			
			m_executor.submit(task_future);
		}
	}
		
	//-----------------------------------------------------------------
	// Add task to the NotifyingBlockingThreadPoolExecutor & return future -- producer
	//-----------------------------------------------------------------	
	public void push_task( BaseTask<?,?> task ) throws RejectedExecutionException
	{
		boolean result = m_tasksInsert.offer((BaseTask<?,?>)task);
		if(!result)
			throw new RejectedExecutionException("the work thread preinsert is full!!!");
			
		return;
	}
	
	//-----------------------------------------------------------------
	//  Get task to the NotifyingBlockingThreadPoolExecutor & with future -- consumer
	//-----------------------------------------------------------------	
	protected BaseTask<?,?> get_task()  throws Exception
	{
		BaseTask<?,?> task = null;	
		task = m_tasksInsert.take();		//blocking if empty
		return task; 
	}
}
