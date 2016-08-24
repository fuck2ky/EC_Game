package io.element.netty.handlers.impl;

import io.element.netty.handlers.DefaultHandlerInterface;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class GSHeartBeatHandler extends ChannelDuplexHandler implements DefaultHandlerInterface{

	protected int m_iFailCount = 0;
	
	protected int m_iMaxCount  = 30000;
	
	protected long m_lRoomid;
		
	public GSHeartBeatHandler(long roomid)
	{
		m_lRoomid = roomid;
	}
	
	public GSHeartBeatHandler(){}
	
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            
            // no receive || no send in a while time
            if (e.state() == IdleState.ALL_IDLE) {
            	
            	m_iFailCount++;
            	if(m_iFailCount < m_iMaxCount)
            	{	
                	// send a ping message
            		return;
            	}
            	
            	// close session when the count full
            	
            } 
        }
    }
    
    @Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
    	// we create a specify msg called heart beat(pong)
    	// to notify client that we are active
    	
    	// if one ever message enter this channel 
    	// we reset count 
    	
    	resetHeartBeatCount();
    	super.channelRead(ctx, msg);
    }
    
    public void resetHeartBeatCount()
    {
    	m_iFailCount = 0;
    }
        
}