package io.element.communication;

import java.util.List;

import io.element.reflect.Handler;
import io.element.time.Counter;
import io.netty.channel.socket.SocketChannel;

public abstract class Session {
	
	
	protected SocketChannel m_channel = null;
	
	//protected Counter m_counter = new Counter();
	protected Counter m_counter;
	
	
	public abstract Object sendMessage(Object message);
	
	public abstract Object sendMessage(Object message, final Handler completeCallback);
	
	public abstract boolean isActive();
	
	public abstract void close()throws InterruptedException;
	
	public abstract void close(final Handler completeCallback);
	
	public Session(){}
	
	public interface Deliver{
		
		public boolean connect() throws InterruptedException;
	
		public boolean connect(Handler completeCallback);

		public boolean connect(int port, Handler completeCallback);
	}
	
	public interface BroadCast<T>
	{
		public void 	broadcast();
		
		public void		broadcast(int location);
		
		public void 	broadcast(List<T> lists);
	}
	
}
