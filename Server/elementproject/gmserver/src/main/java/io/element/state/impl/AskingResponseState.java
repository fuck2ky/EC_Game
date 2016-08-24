package io.element.state.impl;


import io.element.room.Room;
import io.element.room.impl.BaseDispatcher;
import io.element.room.impl.BaseRoom;
import io.element.room.impl.GM_BaseRoom;
import io.element.task.impl.Task_StateRun;

public class AskingResponseState extends BaseGameState {
	
	public enum ASK_RESPONSE
	{
		ASK_ICE_HEART,
		ASK_STEALTH,
	}
	
	public static class CardInfo
	{
		public int cid;					// 牌的id
		public long pid;				// 谁打出的
		public boolean pflag = false;	// 无懈可击了么?
		
		public CardInfo( int cid, long pid, Boolean pflag )
		{
			this.cid = cid;
			this.pid = pid;
			this.pflag = pflag;
		}
	}
	
	public static class AlterInfo
	{
		protected long id;			// 玩家id
		protected boolean check;	// 检查了没
		protected boolean use;		// 使用了没
		protected CardInfo cInfo;	// 使用的数据
		
		public AlterInfo(long id) { this.id= id; check = false; use = false; }
		
		public long id() { return id; }
		
		public boolean check() { return this.check; }
		
		public boolean check(boolean flag) { this.check = flag ; return flag; }
		
		public boolean use() { return this.use; }
		
		public boolean use(boolean flag) { this.use = flag; return use; }
		
		public CardInfo info(){ return cInfo; }
		
		public CardInfo info( CardInfo info ){ this.cInfo = info; return this.cInfo; }
	}
	
	protected ASK_RESPONSE m_ask = ASK_RESPONSE.ASK_ICE_HEART;
	
	public AskingResponseState(ASK_RESPONSE asker)
	{
		m_ask = asker;
	}	
	
	public boolean Enter(Room entity) {
		// push task into room task queue
		BaseRoom room = (BaseRoom)entity;  
		long roomid = room.ID(); 
		
		BaseDispatcher dispatcher = room.getMessageDispatcher();
		dispatcher.DispatchTask( new Task_StateRun(roomid) );
		m_bBegin = true;
		return false;
	}
	
	public boolean Excute(Room entity) {
		// 没有perfect
		GM_BaseRoom room = (GM_BaseRoom)entity;  
		
		if(m_ask == ASK_RESPONSE.ASK_ICE_HEART)
		{
			if(!room.Check_Perfect())
				room.getStateMachine().ChangeState();
			else 
				// change to perfect state
				room.getStateMachine().ChangeState( new PerfectAlterState() );
			return true;
		}
		
		if( m_ask == ASK_RESPONSE.ASK_STEALTH )
		{
			if(!room.Check_Stealth())
				room.getStateMachine().ChangeState();
			//else 
				
			return false;
		}

		return false;
	}
	
}
