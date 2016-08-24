package io.element.netty.handlers.impl;

import java.lang.reflect.Method;

import io.element.gtserver.App;
import io.element.gtserver.GT_Managers;
import io.element.gtserver.GT_PlayerManager;
import io.element.netty.handlers.DefaultHandlerInterface;
import io.element.player.BasePlayer;
import io.element.reflect.Handler;
import io.element.util.HandlerUtil;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class CGHeartBeatHandler  extends ChannelDuplexHandler implements DefaultHandlerInterface{
	
	protected int m_iFailCount = 0;
	
	protected int m_iMaxCount  = 300;
	
	protected Handler m_closeHandler = null;
	
	public CGHeartBeatHandler()
	{
		super();	
		
		Method method = null;
		try {
			method = HandlerUtil.getMethod(CGHeartBeatHandler.class.getName(), "closeSessionHandle", Object.class );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		m_closeHandler = new Handler(method);
	}

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)  {
    	
    	// 达到某一阈值， 关闭链路，释放资源    	
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            
            // no receive || no send in a while time
            if (e.state() == IdleState.READER_IDLE) {
            	    	
            	if( m_iFailCount++ < m_iMaxCount )
            		return;
            	
            	// close session when the count full    
        		GT_PlayerManager playerManager = GT_Managers.getPlayerManager();
        		BasePlayer player = playerManager.GetPlayer( ctx.channel().toString() );
        		if( player != null )
        		{	
        			m_closeHandler.SetObject(this);
					m_closeHandler.SetParam(player);
					player.GetSession().close( m_closeHandler );
        		}
        		else
        		{
        			// 登陆时间过长
        			ChannelFuture closefuture = ctx.channel().close();
        			closefuture.addListener(new ChannelFutureListener() {    
        				public void operationComplete(ChannelFuture arg0) throws Exception { 					
        					App.LOGGER.info("a visitor with out log has been shut down for deactive");
        				}  
        	        });
        		}
            } 
            else if( e.state() == IdleState.ALL_IDLE )
            {
            	
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
    
	public void closeSessionHandle(Object arg)
	{
		BasePlayer p = (BasePlayer) arg;
		p.SetActive(false);
		App.LOGGER.info(String.format("gate -- a player has lose connection, name %s, id %d ", p.GetPlayerName(), p.GetPlayerGuid()));
	}
    
}
