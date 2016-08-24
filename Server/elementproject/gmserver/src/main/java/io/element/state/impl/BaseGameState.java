package io.element.state.impl;
import io.element.event.Message;
import io.element.room.Room;
import io.element.room.impl.BaseRoom;
import io.element.state.*;

public class BaseGameState implements State<Room,Message> {
	
	protected boolean m_bBegin = false;
	
	public boolean Enter(Room entity) {
		// TODO Auto-generated method stub
		m_bBegin = true;
		return false;
	}

	public boolean Excute(Room entity) {
		BaseRoom room = (BaseRoom) entity;
		room.getStateMachine().ChangeState();
		return false;
	}

	public boolean Exit(Room entity) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean HandlerMsg(Message msg) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean IsBegan()
	{
		return m_bBegin;
	}
	
	public void SetBegan(boolean flag)
	{
		m_bBegin = flag;
	}
	
	public void HandlerMessage(STATE_MESSAGE message,Object param)
	{
		
	}
	
	public long delayed_period()
	{
		return 0;
	}
}
