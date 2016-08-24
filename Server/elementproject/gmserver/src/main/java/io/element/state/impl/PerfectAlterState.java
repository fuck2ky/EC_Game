package io.element.state.impl;

import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;

import com.google.protobuf.ByteString;

import io.element.app.App;
import io.element.player.Player;
import io.element.protobuf.GlobalProto;
import io.element.protobuf.SimpleProto;
import io.element.protobuf.SimpleProto.ReactorInfo;
import io.element.room.Room;
import io.element.room.impl.BaseDispatcher;
import io.element.room.impl.BaseRoom;
import io.element.room.impl.EntityManager;
import io.element.room.impl.GM_BaseRoom;
import io.element.room.impl.GM_SimpleRoom;
import io.element.state.impl.AskingResponseState.ASK_RESPONSE;
import io.element.state.impl.AskingResponseState.AlterInfo;
import io.element.task.impl.Task_StateRun;
import io.element.util.HandlerUtil;

public class PerfectAlterState extends BaseGameState{

	// ask player for perfect!!!!! this is location 
	protected Vector<AlterInfo> m_flags = new Vector<AlterInfo>();
		
	// the flag used to init m_flags
	protected boolean m_iFirstTimeEnter = true;
	
	// the current ask index 
	protected int m_indexAsker;
				
	public boolean Enter(Room entity) {
		
		GM_BaseRoom room = (GM_BaseRoom) entity;
		BaseDispatcher dispatcher = room.getMessageDispatcher();
		EntityManager entitys = room.getEntityManager();
		
		if(m_iFirstTimeEnter)
		{
			// 初始化 flags by room players
			Set<Entry<Integer,Player>> sets = entitys.getPlayers().entrySet();  
			m_flags.clear();
		    for(Entry<Integer,Player> entry : sets) {  
		    	m_flags.add( new AlterInfo( entry.getValue().GetPlayerGuid()) );
		    } 
		    this.m_indexAsker = 0;
		    
			m_iFirstTimeEnter = true;
		}
		
		// 都检查了一遍
		if( IsBegan() )
			return true;
		
		// 一次广播一个人 去play perfect card
		if( !this.sendPerfectAskMsg(entity) )
			return false;
			
		// set delayed task to run next ask
		dispatcher.DispatchDelayedTask( new Task_StateRun(room.ID()) , room.config().getResponseGapTime());
		return true;
	}
	
	public boolean Excute(Room entity) {
		
		GM_BaseRoom room = (GM_BaseRoom) entity;
		
		// recursion state stack until we meet a state 
		// which is not perfect alter, excute it or discard it, over!!!
		Stack<AlterInfo> infos = new Stack<AlterInfo>();
		boolean result = room.getStateMachine().recursionDealPerfect(infos);
		if(!result)
		{
			room.getStateMachine().ChangeState( );
			return false;
		}
		
		PerformInfo pInfo = room.getStateMachine().GetLastHandCardState().getPerformInfo();
		if(infos.size() % 2 == 1)
			pInfo.perFlag = true;
		
		room.getStateMachine().ChangeState();
		return true;
	}
	
	public boolean IsBegan()
	{
		boolean result = true;
		for (int i = 0; i < m_flags.size(); i++) {
			if( !m_flags.get(i).check() )
				result = false;
		}
		return result;
	}
	
	public boolean sendPerfectAskMsg(Room entity)
	{
		if (entity instanceof GM_SimpleRoom) {
			
			GM_SimpleRoom room = (GM_SimpleRoom) entity;
			PerformInfo info   = room.getStateMachine().GetLastHandCardState().getPerformInfo();
			
			if(info == null)
				return false;
			
			// so the perfect asker source must be the card
			try {
				SimpleProto.S2G_PerfectPlayer.Builder builder = SimpleProto.S2G_PerfectPlayer.newBuilder();
				
				ReactorInfo sendRInfo = HandlerUtil.getReactorInfo( info );
				SimpleProto.S2G_PerfectPlayer msg = builder.setRoomid( room.ID() )
														   .setActor( sendRInfo )
														   .build();
				ByteString msgByte = HandlerUtil.CREATE_S2G_SIMPLEMESSAGE( SimpleProto.S2G_MSGTYPE.S2G_NOTIFY_ASK_PERFECT , msg.toByteString()).toByteString();
				room.session().sendMessage( HandlerUtil.CREATE_S2G_GLOBALMESSAGE( GlobalProto.MESSAGE.MESSAGE_SIMPLE_S2G, msgByte) );
				
				return m_flags.get(m_indexAsker++).check(true);
				
			} catch (Exception e) {
				App.LOGGER.warn(e.toString());
				return false;
			}
		}
	
		return false;
	}
	
	public AlterInfo getPerfectPlayerId()
	{
		for (AlterInfo info : m_flags) {
			if(info.use())
				return info;
		}
		return null;
	}
	
	public void HandlerMessage(STATE_MESSAGE message,Object param)
	{
		if( message == STATE_MESSAGE.STATE_PLAY_PERFECT_CARD )
			Message_PlayPerfect(param);
		
		
	}
	
	// when eventhandler create task and task run, we will call this method
	// control the state stack in specify task
	public void Message_PlayPerfect(Object param)
	{
		// receive some one play a perfect card
		
		// current state stack pop is perfect alter state
		
		// push asking state into state machine
		BaseRoom room = (BaseRoom)param; 
		BaseStateMachine machine = room.getStateMachine();
		machine.PushCurrentState( new AskingResponseState(ASK_RESPONSE.ASK_ICE_HEART) );
	
	}

}
