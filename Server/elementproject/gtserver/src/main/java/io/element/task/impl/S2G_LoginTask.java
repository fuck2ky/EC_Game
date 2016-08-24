package io.element.task.impl;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import io.element.communication.impl.S2GProto_LoginHandler;
import io.element.protobuf.LoginProto;
import io.element.task.BaseTask;
import io.netty.channel.ChannelHandlerContext;

public class S2G_LoginTask extends BaseTask<LoginProto.S2G_MSGTYPE,LoginProto.S2GByteStream>{

	public S2G_LoginTask(ChannelHandlerContext channel) {
		super(channel);
	}
	
	public S2G_LoginTask(ChannelHandlerContext channel, ByteString buffer)
	{
		super(channel,buffer);
	}
	
	public LoginProto.S2G_MSGTYPE type() {
		
		LoginProto.S2GByteStream stream = AntiSerialization();
		
		return stream != null ? stream.getType() : null;
	}
	
	public LoginProto.S2GByteStream AntiSerialization()
	{
		LoginProto.S2GByteStream byteStream;
		try {
			byteStream = LoginProto.S2GByteStream.parseFrom(m_buffer);
		} catch (InvalidProtocolBufferException e) {
			return null;
		}
		
		return byteStream;
	}
		
	public boolean apply() {
		
		LoginProto.S2GByteStream buffer = AntiSerialization();
		if(buffer == null || !(buffer instanceof LoginProto.S2GByteStream))
			return false;
		
		S2GProto_LoginHandler handler = S2GProto_LoginHandler.getInstance();
		return handler.HandlerMsg( m_channelContext , buffer);
	}
	
}
