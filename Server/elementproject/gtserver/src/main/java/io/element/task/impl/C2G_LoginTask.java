package io.element.task.impl;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import io.element.communication.impl.C2GProto_LoginHandler;
import io.element.protobuf.LoginProto;
import io.element.protobuf.LoginProto.C2GByteStream;
import io.element.task.BaseTask;
import io.netty.channel.ChannelHandlerContext;

public class C2G_LoginTask extends BaseTask<LoginProto.C2G_MSGTYPE, LoginProto.C2GByteStream> {

	public C2G_LoginTask(ChannelHandlerContext channel) {
		super(channel);
		// TODO Auto-generated constructor stub
	}
	
	public C2G_LoginTask(ChannelHandlerContext channel, ByteString buffer)
	{
		super(channel,buffer);
	}
	
	public LoginProto.C2G_MSGTYPE type() {
		
		LoginProto.C2GByteStream stream = AntiSerialization();
		
		return stream != null ? stream.getType() : null;
	}
	
	@Override
	public C2GByteStream AntiSerialization() {
		LoginProto.C2GByteStream byteStream;
		try {
			byteStream = LoginProto.C2GByteStream.parseFrom(m_buffer);
		} catch (InvalidProtocolBufferException e) {
			return null;
		}
		
		return byteStream;
	}
	
	public boolean apply() {		
		LoginProto.C2GByteStream buffer = AntiSerialization();
		if(buffer == null || !(buffer instanceof LoginProto.C2GByteStream))
			return false;
		
		C2GProto_LoginHandler handler = C2GProto_LoginHandler.getInstance();
		return handler.HandlerMsg( m_channelContext , buffer);
	}

}
