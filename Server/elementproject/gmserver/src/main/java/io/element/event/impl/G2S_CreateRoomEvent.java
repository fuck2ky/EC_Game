package io.element.event.impl;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import io.element.event.handlers.impl.G2S_CreateRoomHandler;
import io.element.event.handlers.impl.G2S_LoginTaskHandler;
import io.element.event.handlers.impl.G2S_IdleProtocolHandler;
import io.element.protobuf.LoginProto;
import io.element.protobuf.LoginProto.G2S_MSGTYPE;
import io.element.protobuf.LoginProto.CREATEROOM_STATUS;

public class G2S_CreateRoomEvent extends G2S_LoginEvent<LoginProto.G2S_CreateNewRoom>{
    
	protected LoginProto.CREATEROOM_STATUS m_status = CREATEROOM_STATUS.CREATEROOM_FAILED_UNKNOWN;
	
	public G2S_CreateRoomEvent(G2S_MSGTYPE type) {
		super();
		m_type = type;
	}
	
	public G2S_CreateRoomEvent() {
		super();
		m_type = G2S_MSGTYPE.G2S_REQUEST_CREATE_NEWROOM;
	}
	
	public void init() {
		m_lstHandlers.add(new G2S_CreateRoomHandler());
		m_lstHandlers.add(new G2S_LoginTaskHandler());
		m_lstHandlers.add(new G2S_IdleProtocolHandler());
	}
	
	@Override
	public boolean SetBuffer(Object params) throws InvalidProtocolBufferException {
		
		ByteString byte_str = (ByteString) params;
		
		m_params = LoginProto.G2S_CreateNewRoom.parseFrom(byte_str);

		if(m_params == null)
			return false;		
		return true;
	}
		
	public void SetStatus( CREATEROOM_STATUS status ) { m_status = status; }
	
	public CREATEROOM_STATUS GetStatus() { return m_status; }
		
}
