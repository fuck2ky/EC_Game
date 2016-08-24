package io.element.event.impl;

import io.element.event.Message;

public class BaseMessage implements Message{

	protected MESSGAE_TYPE m_type;
	
	protected Object m_param;
	
	public BaseMessage()
	{
		
	}
	
	public BaseMessage(MESSGAE_TYPE type)
	{
		this.m_type = type;
	}
	
	public BaseMessage(MESSGAE_TYPE type,Object param)
	{
		this.m_type = type;
		this.m_param = param;
	}
	
	public long GetSender() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void SetSender(long id) {
		// TODO Auto-generated method stub
		
	}

	public long GetReceiver() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void SetReceiver(long id) {
		// TODO Auto-generated method stub
		
	}

	public MESSGAE_TYPE GetType() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void SetType(MESSGAE_TYPE type)
	{
		this.m_type = type;
	}
	
	public Object GetParam()
	{
		return m_param;
	}
	
	public void SetParam(Object obj)
	{
		m_param = obj;
	}
	
	@Override
	public boolean equals(Object obj) {
		if ( !obj.getClass().getName().equals(BaseMessage.class.getName()) ||
			 !obj.getClass().isAssignableFrom(BaseMessage.class) )
			return false;
		
		BaseMessage task = (BaseMessage)obj;
	    return (this.m_type == task.m_type);
	}
	
	@Override
	public int hashCode()
	{
		return (int)this.m_type.value();
	}

	
}
