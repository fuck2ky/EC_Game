package io.element.event.impl;

import java.util.concurrent.Callable;
import io.element.event.Task;

public class BaseTask implements Task ,Callable<Integer>{

	protected long m_roomid;
	
	public BaseTask()
	{
		m_roomid = -1;
	}
	
	public Integer call() throws Exception {
		// TODO Auto-generated method stub
		
		return null;
	}

	public TaskType type() {
		// TODO Auto-generated method stub
		return null;
	}

	public void cancelTask() {
		// TODO Auto-generated method stub
		
	}
	
	public long roomid()
	{
		return m_roomid;
	}
	
	public void set_roomid(long id)
	{
		this.m_roomid = id;
	}
	

	
}
