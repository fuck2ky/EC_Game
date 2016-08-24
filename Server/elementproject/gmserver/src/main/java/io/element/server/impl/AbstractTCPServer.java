package io.element.server.impl;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.element.server.Server;
import io.element.server.ServerConfig;

import io.nadron.service.GameAdminService;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public abstract class AbstractTCPServer implements Server {
	
	protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractTCPServer.class);

	// all the channel save in this group
	// when the loginhandler active --
	// we push a channel in it
	public static final ChannelGroup ALL_CHANNELS = new DefaultChannelGroup("WXT-CHANNELS", GlobalEventExecutor.INSTANCE);

	protected ServerConfig m_config;
	
	protected GameAdminService m_adminService;
	
	protected ChannelInitializer<? extends Channel> m_channelInitializer;
	
	public AbstractTCPServer( ServerConfig nettyConfig,
							  ChannelInitializer<? extends Channel> channelInitializer )
	{
		m_config = nettyConfig;
		m_channelInitializer = channelInitializer;
	}
	
	public TransmissionProtocol getTransmissionProtocol() {
		// TODO Auto-generated method stub
		return TRANSMISSION_PROTOCOL.TCP;
	}

	public void startServer() throws Exception {
		
	}

	public void startServer(int port) throws Exception {
		// TODO Auto-generated method stub
		m_config.setPortNumber(port);
		m_config.setSocketAddress(new InetSocketAddress(port));
		startServer();
	}

	public void startServer(InetSocketAddress socketAddress) throws Exception {
		// TODO Auto-generated method stub
		m_config.setSocketAddress(socketAddress);
		startServer();
	}

	public void stopServer() throws Exception {
		
		LOGGER.debug("In stopServer method of class: {}", this.getClass()
				.getName());
		ChannelGroupFuture future = ALL_CHANNELS.close();
		try 
		{
			future.await();
		} 
		catch (InterruptedException e) 
		{
			LOGGER.error( "Execption occurred while waiting for channels to close: {}", e);
		} 
		finally 
		{
			// TODO move this part to spring.
			if (null != m_config.getBossGroup()) 
			{
				m_config.getBossGroup().shutdownGracefully();
			}
			if (null != m_config.getWorkerGroup()) 
			{
				m_config.getWorkerGroup().shutdownGracefully();
			}
			m_adminService.shutdown();
		}
	}
	
	public ChannelInitializer<? extends Channel> getChannelInitializer()
	{
		return m_channelInitializer;
	}

	public ServerConfig getNettyConfig() {
		return m_config;
	}

	public InetSocketAddress getSocketAddress() {
		// TODO Auto-generated method stub
		return m_config.getSocketAddress();
	}
	
	protected EventLoopGroup getBossGroup(){
		return m_config.getBossGroup();
	}
	
	protected EventLoopGroup getWorkerGroup(){
		return m_config.getWorkerGroup();
	}
	
	public GameAdminService getGameAdminService()
	{
		return m_adminService;
	}

	public void setGameAdminService(GameAdminService gameAdminService)
	{
		this.m_adminService = gameAdminService;
	}
	
	
	
}
