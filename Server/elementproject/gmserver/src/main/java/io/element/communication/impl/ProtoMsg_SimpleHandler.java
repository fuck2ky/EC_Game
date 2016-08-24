package io.element.communication.impl;

import com.google.protobuf.ByteString;

import io.element.communication.ProtoMsgHandler;
import io.element.protobuf.SimpleProto;
import io.element.protobuf.GlobalProto.MESSAGE;
import io.netty.channel.ChannelHandlerContext;

public class ProtoMsg_SimpleHandler extends ProtoMsgHandler<SimpleProto.G2S_MSGTYPE,SimpleProto.G2SByteStream> {
	
	private static final ProtoMsg_SimpleHandler single = new ProtoMsg_SimpleHandler();  
	 
	private ProtoMsg_SimpleHandler() { m_globalType = MESSAGE.MESSAGE_SIMPLE_G2S; RegisterProtoMsgCmd(); }  
	    
	public static ProtoMsg_SimpleHandler getInstance() {  
		return single;  
	}
	
	@Override
	public void RegisterProtoMsgCmd() {
		super.RegisterProtoMsgCmd();
		
		
	}

	@Override
	public boolean HandlerMsg(ChannelHandlerContext channel, ByteString param) {
		// TODO Auto-generated method stub
		return false;
	}
	
	

}
