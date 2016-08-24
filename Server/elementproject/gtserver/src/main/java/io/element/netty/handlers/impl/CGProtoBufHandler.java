package io.element.netty.handlers.impl;


import java.io.IOException;

import com.google.protobuf.ByteString;


import io.element.communication.impl.C2GProto_LoginHandler;
import io.element.communication.impl.C2GProto_SimpleHandler;
import io.element.gtserver.App;
import io.element.gtserver.GT_Managers;


import io.element.protobuf.GlobalProto.MESSAGE;
import io.element.protobuf.GlobalProto.MessageStream;


import io.element.server.impl.AbstractTCPServer;
import io.element.task.BaseTask;
import io.element.task.impl.C2G_LoginTask;
import io.element.task.impl.C2G_SimpleTask;
import io.element.threadpool.GT_TaskQueue;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class CGProtoBufHandler extends ChannelInboundHandlerAdapter{
	
	protected C2GProto_LoginHandler m_loginHandler;
	
	protected C2GProto_SimpleHandler m_simpleHandler;
	
	public CGProtoBufHandler()
	{
		super();
		
		m_loginHandler  = C2GProto_LoginHandler.getInstance();
		m_simpleHandler = C2GProto_SimpleHandler.getInstance(); 
	}
	
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
		if( msgType == MESSAGE.MESSAGE_LOGIN_C2G )
			task = new C2G_LoginTask(ctx,msgBuffer);
		if( msgType == MESSAGE.MESSAGE_SIMPLE_C2G )
			task = new C2G_SimpleTask(ctx,msgBuffer);
		
		if(task == null)
			return;
		
		GT_TaskQueue pool = GT_Managers.getTaskQueue();
		pool.push_task(task);
		
		// notify heart beat handler
		super.channelRead(ctx, msg);
    }
	
	@Override  
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {  
	
		if( cause instanceof IOException )
		{
			App.LOGGER.warn( "a remote session has been shut down, release realted buffer" );
		}
	}
	
}



//if( msgType == MESSAGE.MESSAGE_LOGIN_C2G  )
//{
//	LoginProto.C2GByteStream byteStream = null;
//	try {
//		byteStream = LoginProto.C2GByteStream.parseFrom(msgBuffer);
//	} catch (InvalidProtocolBufferException e) {
//
//	}
//	
//	LoginProto.G2C_TEST.Builder builder = LoginProto.G2C_TEST.newBuilder();
// 	LoginProto.G2C_TEST  sendMsg =   builder.setValue( 1 ).build();
//
// 	
// 	GlobalProto.MessageStream sendOb = (GlobalProto.MessageStream) HandlerUtil.CREATE_G2C_GLOBALMESSAGE(LoginProto.G2C_MSGTYPE.G2C_RESP_LOGIN_ATTEMPT, sendMsg.toByteString());
//	
// 	ctx.channel().writeAndFlush(sendOb);
//}
