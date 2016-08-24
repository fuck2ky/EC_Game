package io.element.room;

public interface Dispatcher<M,T> {

	/**
	 * object 1 send msg to object 2
	 */
	public boolean DispatchTask(T task);
	
	/**
	 * object 1 send msg to object 2
	 */
	public boolean DispatchDelayedTask(T task, long delayedTime);
	
	/**
	 * object 1 send msg to object 2
	 */
	public void OnTick(long deltaTime);
	
	/**
	 * object 1 send msg to object 2
	 */
	public void DisCharge(M msg);
}
