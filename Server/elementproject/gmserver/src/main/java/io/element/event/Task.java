package io.element.event;


public interface Task {

	enum TaskType
	{
		NOT_CONNECTED, CONNECTING, CONNECTED, CLOSED
	}
	
	enum RunType
	{
		IMMEDIATELY,DELAY
	}
	
	public TaskType type();

	public void cancelTask();
	
	public long roomid();

}
