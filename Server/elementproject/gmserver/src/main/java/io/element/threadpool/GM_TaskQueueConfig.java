package io.element.threadpool;

public class GM_TaskQueueConfig {

	private int queueCapacity;
	
	private int threadCapacity;
	
	public void setQueueCapacity(int queueCapacity) {
		this.queueCapacity = queueCapacity;
	}
	
	public int getQueueCapacity() {
		return queueCapacity;
	}
	
	public void setThreadCapacity(int threadCapacity) {
		this.threadCapacity = threadCapacity;
	}
	
	public int getThreadCapacity() {
		return threadCapacity;
	}

}
