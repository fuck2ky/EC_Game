package io.element.netty.handlers;

import java.util.concurrent.TimeUnit;

import io.element.netty.handlers.impl.CGHeartBeatHandler;
import io.element.netty.handlers.impl.CGProtoBufHandler;
import io.element.protobuf.GlobalProto;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;

public class CGChannelInitializer extends ChannelInitializer<SocketChannel>{

	protected long m_lIdleRead = 0;
	
	protected long m_lIdleWrite = 0;
	
	protected long m_lIdleAll = 0;

	public CGChannelInitializer(long idleRead, long idleWrite, long idleAll)
	{
		super();
		m_lIdleRead = idleRead;
		m_lIdleWrite = idleWrite;
		m_lIdleAll = idleAll;
	}
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		
		// decode global proto bufs
		ch.pipeline().addLast(DefaultHandlerInterface.strProtobufVarint32FrameDecoder, new ProtobufVarint32FrameDecoder() );
		ch.pipeline().addLast(DefaultHandlerInterface.strProtobufDecoderGlobal, new ProtobufDecoder( 
				 				GlobalProto.MessageStream.getDefaultInstance()  )); 
		
		// encode
		ch.pipeline().addLast(DefaultHandlerInterface.strProtobufVarint32LengthFieldPrepender, new ProtobufVarint32LengthFieldPrepender() );
		ch.pipeline().addLast(DefaultHandlerInterface.strProtobufEncoder, 		new ProtobufEncoder() );

		// custom handler
		ch.pipeline().addLast(DefaultHandlerInterface.strGlobalProtoBufHandler, new CGProtoBufHandler());
		
		// heart beat handler
		ch.pipeline().addLast(DefaultHandlerInterface.strIdleStateHandler, 
				new IdleStateHandler(m_lIdleRead, m_lIdleWrite, m_lIdleAll, TimeUnit.MILLISECONDS));
		ch.pipeline().addLast( DefaultHandlerInterface.strHeartBeatHandler, 
				new CGHeartBeatHandler());
	}
	
	
}
