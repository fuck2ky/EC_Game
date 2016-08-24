package io.element.room.impl;

import io.element.card.pool.SimpleCardPool;
import io.element.communication.RoomSession;
import io.element.event.Message;
import io.element.state.impl.SimpleStateMachine;

public class GM_SimpleRoom extends GM_BaseRoom{
	
	// 创建房间外部队数据作验证
	public GM_SimpleRoom(long roomid, RoomSession session,String name)
	{
		super(roomid, session,name);
		m_stateMachine = new SimpleStateMachine(this);
		m_cardPool	   = new SimpleCardPool(this);
	}
	
	// we temply call in room manager 
	public static void RegisterRoomMsgCmd()
	{
		
	}
	
	public static void MESSAGE_ROOM_REGISTER_HANDLER(Message.MESSGAE_TYPE type, String str_method)
	{
		
	}
		
}
