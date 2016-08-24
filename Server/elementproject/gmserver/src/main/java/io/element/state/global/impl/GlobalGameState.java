package io.element.state.global.impl;

import java.util.concurrent.TimeUnit;


import io.element.dataman.ElementDataMan.StateEssence;
import io.element.event.impl.BaseTask;
import io.element.room.Room;
import io.element.room.impl.GM_BaseRoom;
import io.element.state.impl.BaseGameState;
import io.element.task.impl.Task_StateRun;

public class GlobalGameState<P,M> extends BaseGameState{
	
	protected TimeUnit 					m_tUnit = TimeUnit.MILLISECONDS;
	
	protected P 						m_sPhase;
	
	protected M							m_sMsgType;
	
	protected StateEssence 				m_essence = null;
	
	public P GetPhaseState()
	{
		return m_sPhase;
	}
		
	public GlobalGameState()
	{
		
	}
	
	public String toString()
	{
		return "global game state";
	}
	
	public void delayRefresh(Room room)
	{
		GM_BaseRoom bRoom = (GM_BaseRoom) room;
		bRoom.getMessageDispatcher().DispatchDelayedTask(getEnterDelayedTask(bRoom), m_essence.lastTime);	
	}
	
	public BaseTask getEnterDelayedTask(Room room)
	{
		return new Task_StateRun(room.ID());
	}
	
	// 针对特定状态进行检查 某些多种状态转换的  ( 抽怪物牌结束的情况 & 一方玩家hp皆为0的情况 )
	public boolean checkTranslaction(Room entity){ return true; }
	
	// 针对特定状态进行检查 某些多种状态转换的
	public BaseGameState translaction(Room entity){ return null; }
	
	public long delayed_period()
	{
		return m_essence.lastTime;
	}
}
