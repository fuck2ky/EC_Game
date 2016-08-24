package io.element.communication.impl;

import io.element.communication.ProtoMsgHandler;
import io.element.protobuf.SimpleProto;
import io.element.protobuf.GlobalProto.MESSAGE;
import io.element.protobuf.SimpleProto.C2GByteStream;
import io.netty.channel.ChannelHandlerContext;

public class C2GProto_SimpleHandler extends ProtoMsgHandler<SimpleProto.C2G_MSGTYPE, SimpleProto.C2GByteStream>{

	private static final C2GProto_SimpleHandler single = new C2GProto_SimpleHandler();  
	 
	private C2GProto_SimpleHandler() { m_globalType = MESSAGE.MESSAGE_SIMPLE_C2G; }  
	    
	public static C2GProto_SimpleHandler getInstance() {  
        return single;  
	}  
	
	@Override
	public boolean HandlerMsg(ChannelHandlerContext channel, C2GByteStream param) {
		// TODO Auto-generated method stub
		return false;
	}
	
	

}
