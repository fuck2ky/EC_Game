package io.element.server.impl;

import java.util.Map;
import java.util.Set;

import io.element.server.ServerConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyTCPServer extends AbstractTCPServer {

	protected Channel	m_serverChannel;
	
	protected ServerBootstrap m_serverBootstrap;
	
	public NettyTCPServer(ServerConfig nettyConfig,
			ChannelInitializer<? extends Channel> channelInitializer) {
		super(nettyConfig, channelInitializer);
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void startServer() throws Exception {
			
		m_serverBootstrap = new ServerBootstrap();
		Map<ChannelOption<?>, Object> channelOptions = m_config.getChannelOptions();
		if(null != channelOptions){
			Set<ChannelOption<?>> keySet = channelOptions.keySet();
			for(@SuppressWarnings("rawtypes") ChannelOption option : keySet)
			{
				m_serverBootstrap.option(option, channelOptions.get(option));
			}
		}
			
		m_serverBootstrap.group(getBossGroup(),getWorkerGroup())
				.channel(NioServerSocketChannel.class)
				.childHandler(getChannelInitializer());
		
		m_serverChannel = m_serverBootstrap.bind(m_config.getSocketAddress()).sync().channel();
		ALL_CHANNELS.add(m_serverChannel);
	}
	
	public void setChannelInitializer(ChannelInitializer<? extends Channel> initializer) {
		this.m_channelInitializer  = initializer;
		m_serverBootstrap.childHandler(initializer);
	}
	
	@Override
	public String toString() {
		return "NettyTCPServer [socketAddress=" + m_config.getSocketAddress()
				+ ", portNumber=" + m_config.getPortNumber() + "]";
	}
	
	public void sync() throws InterruptedException
	{
		m_serverChannel.closeFuture().sync();
	}
	
	
}
