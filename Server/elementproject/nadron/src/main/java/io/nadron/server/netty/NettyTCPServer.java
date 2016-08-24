package io.nadron.server.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ServerChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used for TCP IP communications with client. It uses Netty tcp
 * server bootstrap for this.
 * 
 * @author Abraham Menacherry
 * 
 */
public class NettyTCPServer extends AbstractNettyServer {
	private static final Logger LOG = LoggerFactory
			.getLogger(NettyTCPServer.class);

	protected ServerBootstrap m_serverBootstrap;
	
	protected ServerChannel	m_serverChannel;	

	public NettyTCPServer(NettyConfig nettyConfig,
			ChannelInitializer<? extends Channel> channelInitializer) 
	{
		super(nettyConfig, channelInitializer);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void startServer() throws Exception {
		try {
			m_serverBootstrap = new ServerBootstrap();
			Map<ChannelOption<?>, Object> channelOptions = nettyConfig.getChannelOptions();
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
			Channel serverChannel = m_serverBootstrap.bind(nettyConfig.getSocketAddress()).sync()
					.channel();
			ALL_CHANNELS.add(serverChannel);
		} catch(Exception e) {
			LOG.error("TCP Server start error {}, going to shut down", e);
			super.stopServer();
			throw e;
		}
	}

	@Override
	public void setChannelInitializer(ChannelInitializer<? extends Channel> initializer) {
		this.channelInitializer = initializer;
		m_serverBootstrap.childHandler(initializer);
	}
	
	@Override
	public String toString() {
		return "NettyTCPServer [socketAddress=" + nettyConfig.getSocketAddress()
				+ ", portNumber=" + nettyConfig.getPortNumber() + "]";
	}

	@Override
	public TransmissionProtocol getTransmissionProtocol() {
		// TODO Auto-generated method stub
		return null;
	}

}
