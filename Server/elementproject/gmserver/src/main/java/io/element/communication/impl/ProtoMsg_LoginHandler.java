package io.element.communication.impl;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import io.element.app.App;
import io.element.communication.ProtoMsgHandler;
import io.element.event.impl.G2S_CreateRoomEvent;
import io.element.event.impl.G2S_LoginEvent;
import io.element.protobuf.LoginProto;
import io.element.protobuf.GlobalProto.MESSAGE;
import io.netty.channel.ChannelHandlerContext;

public class ProtoMsg_LoginHandler extends ProtoMsgHandler<LoginProto.G2S_MSGTYPE,LoginProto.G2SByteStream >{

	 private static final ProtoMsg_LoginHandler single = new ProtoMsg_LoginHandler();  
	 
	 private ProtoMsg_LoginHandler() { m_globalType = MESSAGE.MESSAGE_LOGIN_G2S; RegisterProtoMsgCmd();}  
	    
	 public static ProtoMsg_LoginHandler getInstance() {  
	        return single;  
	 }  
	
	@Override
	public void RegisterProtoMsgCmd() {
		super.RegisterProtoMsgCmd();		
		
		PROTOMSG_REGISTER_HANDLER( LoginProto.G2S_MSGTYPE.G2S_REQUEST_CREATE_NEWROOM , 	G2S_CreateRoomEvent.class.getName() );
		PROTOMSG_REGISTER_HANDLER( LoginProto.G2S_MSGTYPE.G2S_REQUEST_BEGIN_GAME, 		G2S_CreateRoomEvent.class.getName() );
	}

	@Override
	public boolean HandlerMsg(ChannelHandlerContext ctx, ByteString param) {
		
		LoginProto.G2SByteStream byteStream = null;
		try {
			byteStream = LoginProto.G2SByteStream.parseFrom(param);
		} catch (InvalidProtocolBufferException e1) {
			return false;
		}
						
		LoginProto.G2S_MSGTYPE type = byteStream.getType();
		String eventName = m_handlers.get(type);
		G2S_LoginEvent<?> event = null;
		
		if(eventName == null)
			return false;
		
		try {
			Class<?> clazz;
			clazz = Class.forName(eventName);
			event = (G2S_LoginEvent<?>) clazz.newInstance();			
			event.SetChannel( ctx.channel() );
			event.SetBuffer( byteStream.getRequestData() );		
			event.apply();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvalidProtocolBufferException e) {
			App.LOGGER.info( "receive wrong loginevent params from gate server, type value is {}", event.type().toString());
			e.printStackTrace();
		} 
		
		return true;
	}
}
