package io.element.state.card.impl;

import io.element.room.Room;
import io.element.room.impl.GM_BaseRoom;
import io.element.state.impl.AskingResponseState.AlterInfo;


public class SP_StealthState extends HandCardState{

	protected AlterInfo m_info;
	
	public SP_StealthState(int cid, long pid, AlterInfo info) {
		super(cid, pid);
		// TODO Auto-generated constructor stub
		m_info = info;
	}
	
	// enter 会请求perfect
	@Override
	public boolean Excute(Room entity) {
		
		GM_BaseRoom room = (GM_BaseRoom) entity;
			
		// check the pre state is damage state

		
		// 将操作数据保存 由于是引用保存 damage state info 也会直接修改
		m_info.info( m_perform.cardinfo() );
		room.getStateMachine().ChangeState();		// 进入 damage state
		return true;
	}
		
}
