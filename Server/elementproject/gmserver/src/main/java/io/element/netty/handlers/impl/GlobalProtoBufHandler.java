package io.element.netty.handlers.impl;

import java.io.IOException;

import com.google.protobuf.ByteString;

import io.element.app.App;
import io.element.communication.impl.ProtoMsg_LoginHandler;
import io.element.communication.impl.ProtoMsg_SimpleHandler;
import io.element.protobuf.GlobalProto.MESSAGE;
import io.element.protobuf.GlobalProto.MessageStream;
import io.element.server.impl.AbstractTCPServer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class GlobalProtoBufHandler extends ChannelInboundHandlerAdapter{

	protected ProtoMsg_LoginHandler m_loginHandler;
	
	protected ProtoMsg_SimpleHandler m_simpleHandler;
	
	public GlobalProtoBufHandler()
	{
		super();
		
		m_loginHandler  = ProtoMsg_LoginHandler.getInstance();
		m_simpleHandler = ProtoMsg_SimpleHandler.getInstance(); 
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception 
	{
		AbstractTCPServer.ALL_CHANNELS.add(ctx.channel());
		super.channelActive(ctx);
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx,  Object e) throws Exception
	{
		MessageStream msgStream = (MessageStream) e;
		MESSAGE msgType 		= msgStream.getType();
		ByteString msgBuffer 	= msgStream.getRequestData();
		
		if( msgType == MESSAGE.MESSAGE_LOGIN_G2S )
			m_loginHandler.HandlerMsg(ctx, msgBuffer);
		if( msgType == MESSAGE.MESSAGE_SIMPLE_G2S )
			m_simpleHandler.HandlerMsg(ctx, msgBuffer);
	}
	
	@Override  
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {  
        
		if( cause instanceof NullPointerException )
		{
			App.LOGGER.warn("a NullPointerException excute, detail msg is {}", cause.getMessage());
			return;
		}
		
		if( cause instanceof IOException )
		{
			App.LOGGER.warn( "a remote session has been shut down, release realted buffer, detail msg is {}", cause.getMessage() );
			return;
		}
		
    } 

}
