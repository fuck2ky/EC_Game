package io.element.task.impl;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import io.element.communication.impl.C2GProto_SimpleHandler;
import io.element.protobuf.SimpleProto;
import io.element.protobuf.SimpleProto.C2GByteStream;
import io.element.task.BaseTask;
import io.netty.channel.ChannelHandlerContext;

public class C2G_SimpleTask extends BaseTask<SimpleProto.C2G_MSGTYPE, SimpleProto.C2GByteStream>  {

	public C2G_SimpleTask(ChannelHandlerContext channel) {
		super(channel);
		// TODO Auto-generated constructor stub
	}
	
	public C2G_SimpleTask(ChannelHandlerContext channel, ByteString buffer)
	{
		super(channel,buffer);
	}
	
	public SimpleProto.C2G_MSGTYPE type() {
		
		SimpleProto.C2GByteStream stream = AntiSerialization();
		
		return stream != null ? stream.getType() : null;
	}
	

	@Override
	public C2GByteStream AntiSerialization() {
		SimpleProto.C2GByteStream byteStream;
		try {
			byteStream = SimpleProto.C2GByteStream.parseFrom(m_buffer);
		} catch (InvalidProtocolBufferException e) {
			return null;
		}
		
		return byteStream;
	}
	
	public boolean apply() {
		
		SimpleProto.C2GByteStream buffer = AntiSerialization();
		if(buffer == null || !(buffer instanceof SimpleProto.C2GByteStream))
			return false;
		
		C2GProto_SimpleHandler handler = C2GProto_SimpleHandler.getInstance();
		return handler.HandlerMsg( m_channelContext , buffer);
	}

}
