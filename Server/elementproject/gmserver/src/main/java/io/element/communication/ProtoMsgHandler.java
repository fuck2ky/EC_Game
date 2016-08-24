package io.element.communication;

import io.element.protobuf.GlobalProto.MESSAGE;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;

import com.google.protobuf.ByteString;


public abstract class ProtoMsgHandler<T,E> {
	
	protected HashMap<T, String> m_handlers = new HashMap<T, String>();
		
	protected MESSAGE m_globalType;
	
	public void RegisterProtoMsgCmd() { m_handlers.clear(); }

	public void PROTOMSG_REGISTER_HANDLER(T type, String str_classname){ m_handlers.put(type, str_classname); }
	
	public abstract boolean HandlerMsg(ChannelHandlerContext channel, ByteString param);

}
