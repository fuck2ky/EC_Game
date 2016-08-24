package io.element.task.impl;

import io.element.room.Room;
import io.element.room.impl.GM_BaseRoom;
import io.element.server.GM_Mangers;
import io.element.state.impl.BaseGameState;

public abstract class Task_NextPhase<T> extends Task_StateRun{

	T m_phase;
		
	public Task_NextPhase(long roomid,T phase) {
		super(roomid);
		m_phase = phase;
	}

	public Integer call() throws Exception {
		long roomid = this.roomid();
		
		// 获得room
		GM_BaseRoom room = (GM_BaseRoom) GM_Mangers.getRoomManager().getRoomByID(roomid);
		BaseGameState state = getGlobalState(room);
				
		room.getStateMachine().ClearStates();
		// 创建 一个global state next phase
		room.getStateMachine().ChangeGlobalState(state);						// next begin

		return 1;
	}
	
	protected abstract BaseGameState getGlobalState(Room room);
	
}
