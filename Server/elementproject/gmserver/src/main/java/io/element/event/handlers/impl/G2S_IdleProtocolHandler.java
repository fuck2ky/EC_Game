package io.element.event.handlers.impl;

import java.util.concurrent.TimeUnit;

import io.element.event.EventHandler;
import io.element.event.impl.BaseEvent;
import io.element.event.impl.G2S_CreateRoomEvent;
import io.element.netty.handlers.DefaultHandlerInterface;
import io.element.netty.handlers.impl.HeartBeatHandler;
import io.element.room.impl.GM_BaseRoom;
import io.element.server.GM_Mangers;
import io.netty.channel.Channel;
import io.netty.handler.timeout.IdleStateHandler;

public class G2S_IdleProtocolHandler extends EventHandler{
	
	@Override
	public <T> boolean applyHandler(BaseEvent<T> event) {
		
		if( !(event instanceof G2S_CreateRoomEvent) )
			return false;
		
		G2S_CreateRoomEvent vevent = (G2S_CreateRoomEvent) event;
		long 				roomid =  vevent.GetConvertBuffer().getRoom().getId();
		GM_BaseRoom 		room   = (GM_BaseRoom) GM_Mangers.getRoomManager().getRoomByID(roomid);
		
		if(room == null || !room.session().getChannel().isActive())
			return false;
		
		Channel ch = room.session().getChannel();
		ch.pipeline().addLast(DefaultHandlerInterface.strIdleStateHandler, 
				new IdleStateHandler(10000, 0, 0,TimeUnit.MILLISECONDS));
		ch.pipeline().addLast( DefaultHandlerInterface.strHeartBeatHandler, 
				new HeartBeatHandler(roomid));
		return true;
	}
	
	
	
}
