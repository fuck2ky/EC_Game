package io.element.event.impl;

import com.google.protobuf.InvalidProtocolBufferException;

import io.element.protobuf.LoginProto.G2S_MSGTYPE;
import io.netty.channel.Channel;

public class G2S_LoginEvent<T> extends BaseEvent<G2S_MSGTYPE> {

	protected Channel m_inChannel = null;
	
	protected Object m_params = null;
	
	public G2S_LoginEvent() {
		super();
	}
	
	public G2S_LoginEvent(G2S_MSGTYPE type)
	{
		super(type);
	}
		
	@Override
	public boolean equals(Object obj) {
		if ( !obj.getClass().getName().equals(G2S_LoginEvent.class.getName()) ||
			 !obj.getClass().isAssignableFrom(G2S_LoginEvent.class) )
			return false;
		
		G2S_LoginEvent<?> task = (G2S_LoginEvent<?>)obj;
	    return (this.m_type == task.m_type);
	}
	
	@Override
	public int hashCode()
	{
		return (int)this.m_type.getNumber();
	}
	
	@Override
	public G2S_MSGTYPE type() {
		// TODO Auto-generated method stub
		return m_type;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
	}

	@Override
	public Object GetBuffer() {
		// TODO Auto-generated method stub
		return m_params;
	}
	
	public T GetConvertBuffer()
	{
		@SuppressWarnings("unchecked")
		T getBuffer = (T) this.GetBuffer();
		return getBuffer;
	}

	@Override
	public boolean SetBuffer(Object params) throws InvalidProtocolBufferException {
		// TODO Auto-generated method stub
		return false;
	}

}
