package io.element.communication;

import java.util.Map;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;

public class SSConfig {

	private String 				inetAddress;
	
	private int					inetPort;
	
	private NioEventLoopGroup 	gmServerGroup;
	
	private Map<ChannelOption<?>, Object> channelOptions;
	
	private ChannelInitializer<? extends Channel> channelInitializer;
	
	public String 	getInetAddress(){ return inetAddress; }
	
	public void 	setInetAddress(String inetAddress){ this.inetAddress = inetAddress; }
	
	public int 		getInetPort(){ return inetPort; }
	
	public void 	setInetPort(int inetPort){ this.inetPort = inetPort; }
	
	public NioEventLoopGroup getGmServerGroup(){
		return gmServerGroup;
	}
	
	public void setGmServerGroup(NioEventLoopGroup gmServerGroup) {
		this.gmServerGroup = gmServerGroup;
	}
	
	public Map<ChannelOption<?>, Object> getChannelOptions() {
		return channelOptions;
	}

	public void setChannelOptions(
			Map<ChannelOption<?>, Object> channelOptions) {
		this.channelOptions = channelOptions;
	}
	
	public ChannelInitializer<? extends Channel> getChannelInitializer()
	{
		return channelInitializer;
	}
	
	public void setChannelInitializer(ChannelInitializer<? extends Channel> channelInitializer)
	{
		this.channelInitializer = channelInitializer;
	}
	
}
