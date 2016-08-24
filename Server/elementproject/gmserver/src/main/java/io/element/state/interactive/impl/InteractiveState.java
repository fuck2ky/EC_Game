package io.element.state.interactive.impl;

import io.element.room.Room;
import io.element.room.impl.GM_BaseRoom;
import io.element.state.impl.BaseGameState;
import io.element.state.impl.AskingResponseState.AlterInfo;


// 只有保证数据整齐的前提下 我们才会创建 操作状态类
public class InteractiveState extends BaseGameState {

	protected int m_gapTime ;
	
	protected PerformInfo m_perInfo;
	
	protected int m_interIndex;			// interactive info index in  perform
	
	protected AlterInfo m_alter;		// for which player in a single interactive action
	
	public InteractiveState(PerformInfo info, int index)
	{
		m_perInfo = info;
		m_interIndex = index;
		m_alter = new AlterInfo( info.pid );
	}

	@Override
	public boolean Enter(Room entity) {
		
		GM_BaseRoom room = (GM_BaseRoom) entity;
		m_gapTime = room.config().getResponseGapTime();
		m_bBegin  = true;
		
		room.getStateMachine().Update();
		return true;
	}
	
	public long delayed_period()
	{
		return m_gapTime;
	}
	
	public INTERACTIVE_TYPE interType()
	{
		return INTERACTIVE_TYPE.DEAL_DEFAULT;
	}
}
