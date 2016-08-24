package io.element.netty.handlers.impl;

import com.google.protobuf.ByteString;

import io.element.communication.impl.S2GProto_LoginHandler;
import io.element.communication.impl.S2GProto_SimpleHandler;
import io.element.gtserver.GT_Managers;
import io.element.protobuf.GlobalProto.MESSAGE;
import io.element.protobuf.GlobalProto.MessageStream;
import io.element.server.impl.AbstractTCPServer;
import io.element.task.BaseTask;
import io.element.task.impl.S2G_LoginTask;
import io.element.task.impl.S2G_SimpleTask;
import io.element.threadpool.GT_TaskQueue;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class GSProtoBufHandler extends ChannelInboundHandlerAdapter{

	protected S2GProto_LoginHandler m_loginHandler = S2GProto_LoginHandler.getInstance();
	
	protected S2GProto_SimpleHandler m_simpleHandler = S2GProto_SimpleHandler.getInstance();
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception 
	{
		AbstractTCPServer.ALL_CHANNELS.add(ctx.channel());
		super.channelActive(ctx);
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
		MessageStream msgStream = (MessageStream) msg;
		MESSAGE msgType 		= msgStream.getType();
		ByteString msgBuffer 	= msgStream.getRequestData();
		
		BaseTask<?, ?> task = null;
		if( msgType == MESSAGE.MESSAGE_LOGIN_S2G )
			task = new S2G_LoginTask(ctx,msgBuffer);
		if( msgType == MESSAGE.MESSAGE_SIMPLE_S2G )
			task = new S2G_SimpleTask(ctx,msgBuffer);
		
		if(task == null)
			return;
		
		GT_TaskQueue pool = GT_Managers.getTaskQueue();
		pool.push_task(task);
		
    }
	
}
