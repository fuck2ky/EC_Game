package io.element.state.impl;

import java.util.Stack;
import java.util.Vector;

import io.element.event.Message;
import io.element.event.impl.BaseMessage;
import io.element.room.Room;
import io.element.room.impl.BaseRoom;
import io.element.state.StateMachine;
import io.element.state.card.impl.HandCardState;
import io.element.state.global.impl.GlobalGameState;
import io.element.state.impl.AskingResponseState.AlterInfo;
import io.element.state.interactive.impl.InteractiveState;
import io.element.util.StateUtil;

public class BaseStateMachine implements StateMachine<Room, BaseGameState> {

	protected Stack<BaseGameState> 	m_sStack 	= new Stack<BaseGameState>();
	
	protected BaseGameState 		m_sGlobal 	= null;
	
	protected Room 					m_owner   	= null;
			
	public BaseStateMachine(Room owner)
	{
		this.m_owner = owner;
	}
	
	public void Update() {
		// if stack is empty, pop global state
		BaseGameState state = this.PeekCurrentState();
		if(state == null)
			state = m_sGlobal;
		
		// some special state which could check begin for lot of times
		if(state.IsBegan())
			state.Excute(m_owner);
		else 
			state.Enter(m_owner);
	}
	
	public void ClearStates()
	{
		if( m_sStack.size() != 0 )
		{
			StateUtil.PrintStateStack(m_sStack);
			m_sStack.clear();
		}
	}
	
	public final BaseGameState PhaseState()
	{
		return m_sGlobal;
	}
	
	public void PushCurrentState(BaseGameState state)
	{
		// TODO Auto-generated method stub
		m_sStack.push((BaseGameState) state);
		
		// important for state stacks
		state.Enter(m_owner);
	}
	
	public void PushInteractiveState( Vector<InteractiveState> states)
	{
		for (int i = 0; i < states.size(); i++) {
			m_sStack.push( states.get(i) );	
		}
		
		BaseGameState state = this.PeekCurrentState();
		state.Enter(m_owner);
	}

	public BaseGameState PeekCurrentState() {
		// TODO Auto-generated method stub		
		if(!m_sStack.isEmpty())	
			return m_sStack.peek();
		
		return null;
	}
	
	public BaseGameState PopCurrentState() {
		// TODO Auto-generated method stub
		if(!m_sStack.isEmpty())
			return m_sStack.pop();
		
		return null;
	}
	
	public void ChangeGlobalState(BaseGameState newState)
	{		
		if( !(newState instanceof io.element.state.global.impl.SimplePhaseState) )
			return;
		
		if(m_sGlobal != null)	
			m_sGlobal.Exit(m_owner);
		
		BaseRoom baseRoom = (BaseRoom) m_owner;
		baseRoom.HandleMsg(new BaseMessage(Message.MESSGAE_TYPE.SERVER_CLEAR_DELAYED_INFO,null));
		m_sGlobal = newState;
		m_sGlobal.Enter(m_owner);
	}
		
	public void ChangeState(BaseGameState newState)
	{
		// TODO Auto-generated method stub		
		BaseGameState sCurBaseGameState =  this.PeekCurrentState();
		if(sCurBaseGameState != null)	
			sCurBaseGameState.Exit(m_owner);

		// important for new state enter game
		m_sStack.push(newState);
		newState.Enter(m_owner);
	}
	
	public void ChangeState(){
		
		BaseGameState sCurBaseGameState = this.PopCurrentState();
		sCurBaseGameState.Exit(m_owner);
		BaseGameState sPreBaseGameState = this.PeekCurrentState();
				
		//check the state is begin ?
		if( sPreBaseGameState != null && sPreBaseGameState.IsBegan() )
			sPreBaseGameState.Excute(m_owner);
		else if(sPreBaseGameState != null)
		// special state that state push and never use
			sPreBaseGameState.Enter(m_owner);
		else {
		// stack is empty, so we refresh the global state instead
			GlobalGameState<?, ?> gState = (GlobalGameState<?, ?>) m_sGlobal;
			gState.delayRefresh( m_owner);
		}	
	}
	
	public void SetOwner(Room owner) {
		// TODO Auto-generated method stub
		this.m_owner = owner;
	}

	public Room GetOwner() {
		// TODO Auto-generated method stub
		return this.m_owner;
	}
	
	public BaseGameState GetPhaseState()
	{
		return m_sGlobal;
	}
	
	public HandCardState GetLastHandCardState()
	{
		HandCardState state = null;
		
		// 从栈底向栈顶半遍历
		for(BaseGameState bs : m_sStack)
		{
			if(bs instanceof HandCardState)
				state = (HandCardState) bs;
		}
		
		return state;
	}
	
	// 
	public boolean recursionDealPerfect(Stack<AlterInfo> infos)
	{
		if( !( this.PeekCurrentState() instanceof PerfectAlterState) )
			return false;
		infos.clear();
		
		PerfectAlterState state = (PerfectAlterState) this.PopCurrentState();
		AlterInfo info = state.getPerfectPlayerId();
		if(info != null)
			infos.push(info);
			
		return perfectStateCheck(infos);
	}
	
	private boolean perfectStateCheck(Stack<AlterInfo> infos)
	{
		// 不应该出现这种情况
		if( this.PeekCurrentState() == null )
			return false;
		
		if( ( this.PeekCurrentState() instanceof HandCardState) )
			return true;
		
		if( ( this.PeekCurrentState() instanceof PerfectAlterState) )
		{
			PerfectAlterState state = (PerfectAlterState) this.PopCurrentState();
			AlterInfo info = state.getPerfectPlayerId();
			if(info != null)	
				infos.push(info);
		}
		
		// 不应该出现其他的state 全部弹出
		this.PopCurrentState();
		return perfectStateCheck(infos);
	}


}
