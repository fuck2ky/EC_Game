package io.element.task.impl;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import io.element.communication.impl.S2GProto_SimpleHandler;
import io.element.protobuf.SimpleProto;
import io.element.task.BaseTask;
import io.netty.channel.ChannelHandlerContext;

public class S2G_SimpleTask extends BaseTask<SimpleProto.S2G_MSGTYPE, SimpleProto.S2GByteStream>{

	public S2G_SimpleTask(ChannelHandlerContext channel) {
		super(channel);
		// TODO Auto-generated constructor stub
	}
	
	public S2G_SimpleTask(ChannelHandlerContext channel, ByteString buffer)
	{
		super(channel,buffer);
	}

	public void SetBuffer(ByteString params)
	{ 
		m_buffer = params; 
	}
	
	public SimpleProto.S2G_MSGTYPE type() {
		
		SimpleProto.S2GByteStream stream = AntiSerialization();
		return stream != null ? stream.getType() : null;
	}
	
	public SimpleProto.S2GByteStream AntiSerialization()
	{
		SimpleProto.S2GByteStream  byteStream;
		try {
			byteStream = SimpleProto.S2GByteStream.parseFrom(m_buffer);
		} catch (InvalidProtocolBufferException e) {
			return null;
		}
		
		return byteStream;
	}
	
	public boolean apply() {
		
		SimpleProto.S2GByteStream buffer = AntiSerialization();
		if(buffer == null || !(buffer instanceof SimpleProto.S2GByteStream))
			return false;
		
		S2GProto_SimpleHandler handler = S2GProto_SimpleHandler.getInstance();
		return handler.HandlerMsg( m_channelContext , buffer);
	}
}
