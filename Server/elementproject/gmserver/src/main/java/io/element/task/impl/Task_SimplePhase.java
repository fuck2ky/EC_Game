package io.element.task.impl;

import io.element.protobuf.SimpleProto;
import io.element.room.Room;
import io.element.state.impl.BaseGameState;
import io.element.state.impl.SimpleStateMachine;

// 用于起始的状态跳转 以及 强制的状态跳转   正常情况下 task run  足矣
public class Task_SimplePhase extends Task_NextPhase<SimpleProto.PHASE_TYPE> {

	public Task_SimplePhase(long roomid,SimpleProto.PHASE_TYPE phase) {
		super(roomid, phase);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected BaseGameState getGlobalState(Room room) {				
		return SimpleStateMachine.getNextGlobalState(m_phase);
	}

}
