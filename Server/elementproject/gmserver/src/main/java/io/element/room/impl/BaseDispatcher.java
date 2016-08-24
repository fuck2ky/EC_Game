package io.element.room.impl;

import java.util.Stack;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.RejectedExecutionException;


import io.element.app.App;
import io.element.event.Message;
import io.element.event.Task;
import io.element.event.impl.BaseTask;
import io.element.room.Dispatcher;
import io.element.server.GM_Mangers;
import io.element.task.impl.Task_StateRun;
import io.element.threadpool.GM_TaskQueue;
import io.element.time.Counter;


public class BaseDispatcher implements Dispatcher<Message, Task>
{
	protected BaseRoom 						m_owner;
	
	protected LinkedBlockingDeque<Task> 	m_queueTasks;			// task ready for insert to pool
		
	protected Task 							m_delayTask;			// delayed task
	
	protected Counter 						m_cDelayTimer;
	
	protected FutureTask<Integer> 			m_tExecuting;			// spec task working in pool!!!
																	// yes, it should be save in room dispatch 
	protected boolean 						m_bTaskPushedNoExcu;	
	
	protected Stack<Float>                  m_delayTimes;
	
	public BaseDispatcher(BaseRoom owner)
	{
		m_owner = owner;
		m_bTaskPushedNoExcu = false;
		
		m_tExecuting = null;
		
		m_queueTasks  = new LinkedBlockingDeque<Task>();
		m_cDelayTimer = new Counter(); 
		m_delayTimes  = new Stack<Float>();
	}
		
	public boolean DispatchTask(Task task)  {
		
		boolean result = false;
		try {
			result = m_queueTasks.offerFirst(task,100,TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public boolean DispatchTask_Tail(Task task)
	{
		boolean result = false;
		try {
			result = m_queueTasks.offerLast(task,100,TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public boolean DispatchDelayedTask(Task task, long delayedTime) {
		// cancel delayed task
		if(m_delayTask != null)
			m_delayTask.cancelTask();
		m_delayTask = task;
				
		m_cDelayTimer.Reset();
		m_cDelayTimer.SetPeriod(delayedTime);
		
		// record each state delayed time
		m_delayTimes.push(new Float(delayedTime));
		
		return false;
	}
	
	public void OnTick(long deltaTime) {
		// TODO Auto-generated method stub
		m_cDelayTimer.IncCounter(deltaTime);
		if(m_cDelayTimer.IsFull())
		{
			// push delay task into pool if future work done
			if( !PushDelayedTaskIntoPool() )
				return;
				
			// if there only one global state time in m_delayTimes, 
			// we do not need to set delay task to prepare the next state 
			// which current pushed task will call the delayed work
			if(m_delayTimes.size() == 1)
				return;
			
			// run next phase state after some delayed work run
			// not once delayed work put into task pool 
			Float delayTime = m_delayTimes.pop();
			this.DispatchDelayedTask(new Task_StateRun(m_owner.ID()),(long) delayTime.floatValue() );
			return;
		}
		
		if (PushTaskIntoPool())
			// reset current timer count if some one reaction!!!!! 
			m_cDelayTimer.Recount();
	}
	
	public boolean isDone()
	{
		return !m_bTaskPushedNoExcu && ( m_tExecuting == null || m_tExecuting.isDone() );
	}
	
	// this method will check queues 
	protected boolean PushTaskIntoPool()
	{
	 	Task task = m_queueTasks.peek();
	 	
	 	if( task!= null && PushTaskIntoPool(task))
	 	{	
	 		m_queueTasks.poll();
	 		return true;
	 	}
		return false;
	}
		
	// this method will not check queues, just judge if the current task run over???
	protected boolean PushTaskIntoPool(Task task)
	{
		// m_tExecuting is null and m_bTaskPushedNoExcu == true means  
		// the task has pushed but not execute with wait
		if( m_bTaskPushedNoExcu == true )
			return false;
		
		// m_tExecuting is null and m_bTaskPushedNoExcu == false means
		// the init state that first push task
		if( m_tExecuting != null && !m_tExecuting.isDone() )
			return false;
		
		// this flag tell the task which into pool 还没有执行 但是已经准备完成了
		m_bTaskPushedNoExcu = true;
		
		// send task into task pool
		GM_TaskQueue queue = GM_Mangers.getTaskQueue();
		try {
			queue.push_task((BaseTask)task);
		} catch (RejectedExecutionException e) {
			
		}
			
		return true;
	}
		
	protected boolean PushDelayedTaskIntoPool()
	{	
		if(m_delayTask == null)
			return false;
		
		boolean result =  PushTaskIntoPool(m_delayTask);
		if(!result)
			return false;
				
		// do logic for clear the last task in room
		// m_queueTasks saves the operation states created by delayed task
		// !!!!!!!!!!!!!!!!!!!!
		if( m_queueTasks.peek() != null )
		{	
			m_queueTasks.clear();
			App.LOGGER.info(" the room id: {} has left task, but into another phase!!! error!! ", m_owner.ID());
		}
		
		return true;
	}
	
	public void SetExecutingTask(FutureTask<Integer> task_future)
	{
		m_tExecuting = task_future;
		m_bTaskPushedNoExcu = false;
	}

	public long DelayedTime(){ return m_cDelayTimer.lastTime(); }
	
	public void ClearDelayedTimeVec() { m_delayTimes.clear(); }
	
	public void DisCharge(Message msg) {
		// TODO Auto-generated method stub
		
	}
	
}