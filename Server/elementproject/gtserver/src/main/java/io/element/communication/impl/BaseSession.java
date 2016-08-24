package io.element.communication.impl;

import io.element.communication.Session;
import io.element.reflect.Handler;
import io.element.time.Counter;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.socket.SocketChannel;

public class BaseSession    extends Session{
	
	public BaseSession(SocketChannel channel)
	{
		m_channel = channel;
		m_counter = new Counter();
	}
	
	public BaseSession()
	{
		m_counter = new Counter();
	}
	
	@Override
	public Object sendMessage(Object message) {
		m_channel.writeAndFlush( message );
		m_counter.Recount();
		return message;
	}
	
	@Override
	public Object sendMessage(Object message, final Handler completeCallback) {
		
		ChannelFuture sendfuture = m_channel.writeAndFlush( message );
		sendfuture.addListener(new ChannelFutureListener() {    
			public void operationComplete(ChannelFuture arg0) throws Exception {
				completeCallback.invoke();
			}  
        });
	
		m_counter.Recount();
		return message;
	}

	@Override
	public void close() throws InterruptedException {
		if(m_channel == null || !m_channel.isActive())
			return;		
		m_channel.close().sync();
	}
	
	@Override
	public void close(final Handler completeCallback) {
		if(m_channel == null && !m_channel.isActive())
			return;
		
		ChannelFuture closefuture = m_channel.close();
		closefuture.addListener(new ChannelFutureListener() {    
			public void operationComplete(ChannelFuture arg0) throws Exception {
				completeCallback.invoke();
			}  
        });
	}

	@Override
	public boolean isActive() {
		return m_channel == null ? false : m_channel.isActive();
	}
	
	@Override
	public String toString()
	{
		return m_channel != null ? m_channel.toString() : "";
	}
	
}
