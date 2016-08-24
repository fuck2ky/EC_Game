package io.element.netty.handlers;

import io.element.netty.handlers.impl.GlobalProtoBufHandler;
import io.element.netty.handlers.impl.HeartBeatHandler;
import io.element.protobuf.GlobalProto;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;

public class GlobalChannelInitializer extends ChannelInitializer<SocketChannel> {

	protected IdleStateHandler m_idleHandler;
	
	protected HeartBeatHandler m_heartHandler;
	
	protected int m_readIdleTime = 2000;
	
	protected int m_writeIdleTime = 0;
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
				 
		 //decode login
		 ch.pipeline().addLast(DefaultHandlerInterface.strProtobufVarint32FrameDecoder, new ProtobufVarint32FrameDecoder() );
		 ch.pipeline().addLast(DefaultHandlerInterface.strProtobufDecoderGlobal, 		new ProtobufDecoder( 
				 				GlobalProto.MessageStream.getDefaultInstance()  ));
		 
		 //encode
		 ch.pipeline().addLast(DefaultHandlerInterface.strProtobufVarint32LengthFieldPrepender, new ProtobufVarint32LengthFieldPrepender() );
		 ch.pipeline().addLast(DefaultHandlerInterface.strProtobufEncoder, 						new ProtobufEncoder() );
		 
		 //global logic handler
		 ch.pipeline().addLast(DefaultHandlerInterface.strGlobalProtoBufHandler, 		new GlobalProtoBufHandler() );
	}
	
}
