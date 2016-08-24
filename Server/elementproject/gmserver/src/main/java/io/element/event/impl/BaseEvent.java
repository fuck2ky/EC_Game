package io.element.event.impl;

import java.util.Vector;

import com.google.protobuf.InvalidProtocolBufferException;

import io.element.event.Event;
import io.element.event.EventHandler;
import io.netty.channel.Channel;


public abstract class BaseEvent<T> implements Event<T> {
	
	protected T m_type;
	
	protected Vector<EventHandler> m_lstHandlers;
	
	protected Channel m_channel;
	
	public boolean continuation;
	
	public BaseEvent(T type)
	{
		this();
		this.m_type = type;
	}
	
	public BaseEvent()
	{
		this.m_lstHandlers = new Vector<EventHandler>();
		continuation = true;
		this.init();
	}
	
	public boolean apply() {
		// TODO Auto-generated method stub
		
		for (EventHandler iter : m_lstHandlers) {
			if (!iter.applyHandler(this))
				return false;
		}
		
		return true;
	}

	public abstract T type();
	
	public abstract void init();

	public abstract Object GetBuffer();
	
	public abstract boolean SetBuffer(Object params) throws InvalidProtocolBufferException;
	
	public void SetChannel(Channel channel) { m_channel = channel; }
	
	public Channel GetChannel() { return m_channel; }
	
}
