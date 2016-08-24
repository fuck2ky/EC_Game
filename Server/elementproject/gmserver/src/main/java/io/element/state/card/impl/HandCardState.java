package io.element.state.card.impl;

import java.util.Vector;

import io.element.app.App;
import io.element.card.ChooseStrategy;
import io.element.card.PerfectStrategy;
import io.element.dataman.ElementDataMan.CardExecuteInfo;
import io.element.dataman.ElementDataMan.HandCardEssence;
import io.element.room.Room;
import io.element.room.impl.GM_BaseRoom;

// 仅仅考虑选择一次的情况 碰上第一个需要执行choose的执行
// 1. check perfect
// 2. 选人
public class HandCardState extends CardState{

	protected boolean m_prefectCheck = false;
		
	public HandCardState( int cid, long pid) {
		super( cid, pid);
	}

	protected void FillUpPerformInfo(long pid)
	{
		HandCardEssence hEssence = getEssence();
		
		Vector<InteractiveInfo> infos = new Vector<InteractiveInfo>();
		for (int i = 0; i < hEssence.executes.size(); i++) {
			CardExecuteInfo info = hEssence.executes.get(i);
			infos.add( new InteractiveInfo(info) );
		}
		
		m_perform = new PerformInfo(pid , this.m_id ,infos);
	}
	
	public final HandCardEssence getEssence()
	{
		return (HandCardEssence) m_essence;
	}
	
	public PerformInfo getPerformInfo()
	{
		return m_perform;
	}
	
	@Override
	public boolean Enter(Room entity) {
		
		if(m_essence == null || ( m_essence instanceof HandCardEssence == false ) )
			App.LOGGER.warn(" the state machine essence is wrong!!! ");
		
		GM_BaseRoom room = (GM_BaseRoom) entity;
		HandCardEssence essence = getEssence();
		
		if( essence.perfectVaild && m_prefectCheck == false )
		{
			m_prefectCheck = true;
			
			// 使用无懈可击牌的时候
			// 1。我们会让当前的delay work cancel掉
			// 2。设置this state为begin
			// 3。直接执行一个task，调用execute
			// 4。card 任务进来的时候 检查 stack 顶的state 不是刚才的state（意味着card来晚了  这个操作已经被做了 ） 那么丢弃它
			new PerfectStrategy().doOperation(m_perform, room, 0);
		}
		
		// 查找是否需要choose 多个choose的话this state only response the first one
		for (InteractiveInfo info : m_perform.recvs) {
			if(info.einfo.chooseType != CHOOSE_TYPE.NONE && info.performed() == false )
			{	
				info.setPerformed(true);
				new ChooseStrategy().doOperation(m_perform, room, m_perform.recvs.indexOf(info) );	
				// 下一次update 直接调用 execute
				m_bBegin = true;
				break;
			}
		}
				
		return true;
	}

	@Override
	public boolean Excute(Room entity) {

		GM_BaseRoom room = (GM_BaseRoom) entity;
		
		if(  m_perform.perFlag == true )
		{	
			room.getStateMachine().ChangeState();
			return true;
		}
		
		m_card.perform(this, room);
		room.getStateMachine().ChangeState();
		return true;
	}
}



















