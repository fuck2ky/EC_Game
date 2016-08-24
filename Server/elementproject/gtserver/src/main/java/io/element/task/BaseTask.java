package io.element.task;

import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.Callable;

import com.google.protobuf.ByteString;

public abstract class BaseTask<T,E> implements Task<T>,Callable<Boolean> {

	protected T m_type;
	
	protected ByteString m_buffer;
	
	protected ChannelHandlerContext  m_channelContext;
	
	public BaseTask(ChannelHandlerContext channel)
	{
		m_channelContext = channel;
	}
	
	public BaseTask(ChannelHandlerContext channel, ByteString buffer)
	{
		this(channel);
		SetBuffer(buffer);
	}
	
	public Boolean call() throws Exception {
		return apply();
	}

	public T type() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean apply() {
		// TODO Auto-generated method stub
		return false;
	}

	public void init() {
		// TODO Auto-generated method stub
		
	}

	public ByteString GetBuffer() { return m_buffer; }
	
	public void SetBuffer(ByteString params){ m_buffer = params; }
	
	public abstract E AntiSerialization();
}
