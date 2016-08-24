package io.element.netty.handlers.impl;

import io.element.netty.handlers.DefaultHandlerInterface;
import io.element.room.impl.GM_BaseRoom;
import io.element.server.GM_Mangers;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class HeartBeatHandler extends ChannelDuplexHandler implements DefaultHandlerInterface{

	protected int m_iFailCount = 0;
	
	protected int m_iMaxCount  = 30000;
	
	protected long m_lRoomid;
		
	public HeartBeatHandler(long roomid)
	{
		m_lRoomid = roomid;
	}
	
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state() == IdleState.READER_IDLE) {
            	
            	m_iFailCount++;
            	if(m_iFailCount < m_iMaxCount)
            		return;
            	
            	// close session by room id
            	GM_BaseRoom room = (GM_BaseRoom)GM_Mangers.getRoomManager().getRoomByID(m_lRoomid);
            	if( room != null )
            		room.Active(false);
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
    	
    	m_iFailCount = 0;
    	super.channelRead(ctx, msg);
    }
    
    public void resetHeartBeatCount()
    {
    	m_iFailCount = 0;
    }
    
	public static String toHandlerString() {
		return "heartbeathandler";
	}
    
}
