package io.element.state.global.impl;

import com.google.protobuf.ByteString;

import io.element.app.App;
import io.element.cmd.S2G;
import io.element.protobuf.GlobalProto;
import io.element.protobuf.SimpleProto;
import io.element.protobuf.SimpleProto.PHASE_TYPE;
import io.element.room.Room;
import io.element.room.impl.BaseDispatcher;
import io.element.room.impl.GM_BaseRoom;
import io.element.room.impl.GM_SimpleRoom;
import io.element.state.impl.BaseGameState;
import io.element.state.impl.SimpleStateMachine;
import io.element.util.HandlerUtil;

// 他的子类有两种 	1. SP_XX SIMPLE PHASE 游戏开始以后特定某人的某个阶段
//				2. SG_XX SIMPLE GAME  游戏开始前的整体阶段 例如 选择角色 整体发牌啦
public class SimplePhaseState extends GlobalGameState<SimpleProto.PHASE_TYPE, SimpleProto.S2G_MSGTYPE> {
				
	public SimplePhaseState()
	{
		
	}
		
	public ByteString getEnterBroadBuffer(Room entity)
	{
		SimpleProto.S2G_NotifyPhaseBegin.Builder builder = SimpleProto.S2G_NotifyPhaseBegin.newBuilder();
		GM_SimpleRoom room = (GM_SimpleRoom) entity;
		BaseDispatcher dispatcher = room.getMessageDispatcher();
		
		SimpleProto.Room sRoom = new HandlerUtil().getSimpleRoom(entity);
		SimpleProto.S2G_NotifyPhaseBegin msg = builder.setPhase(m_sPhase)
					  								  .setLast( (int) dispatcher.DelayedTime())
					  								  .setRoom(sRoom)
					  								  .build();
		
		return HandlerUtil.CREATE_S2G_SIMPLEMESSAGE(m_sMsgType, msg.toByteString()).toByteString();
	}
	
	// 根据当前state  创建下一个phase
	protected BaseGameState CreateNextPhase(Room entity)
	{
		GM_SimpleRoom room = (GM_SimpleRoom) entity;
		SimpleStateMachine sMachine = (SimpleStateMachine) room.getStateMachine();
		return sMachine.nextPhaseState();
	}
	
	// 所有的 global state 都会通过 enter发表消息 进入某种状态
	@Override
	public boolean Enter(Room entity) 
	{
		GM_BaseRoom room = (GM_BaseRoom) entity;
		
		// 广播给所有玩家 进入下一个global state example!!!!!!!!!!!!!!!!!!
		// room.session().sendMessage( HandlerUtil.CREATE_S2G_GLOBALMESSAGE( GlobalProto.MESSAGE.MESSAGE_SIMPLE_S2G, getEnterBroadBuffer(entity)) );
		S2G.Cmd_Notify_GlobalState_Simple((GM_SimpleRoom)room, this.m_sPhase,  this.m_sMsgType);
		
		// set a delayed work to run it & next execute will set next phase and give a delay
		room.getMessageDispatcher().DispatchDelayedTask(getEnterDelayedTask(room), m_essence.lastTime);	
		
		m_bBegin = true;
		return m_bBegin;	
	}
	
	// next phase 执行函数  主要负责状态机的跳转
	@Override
	public boolean Excute(Room entity)
	{
		GM_SimpleRoom room = (GM_SimpleRoom) entity;
		
		if( !this.checkStateCorrectness(room) )
		{
			// exit current game and release the resources
			App.LOGGER.info("the phase state is not matching!!!");
			return false;
		}
		
		// 清除  state machine 中的所有state
		room.getStateMachine().ClearStates();
		// 创建 一个 global state next phase
		BaseGameState next = CreateNextPhase(entity); 	
		room.getStateMachine().ChangeGlobalState(next);
		
		return true;
	}
	
	public boolean checkStateCorrectness(GM_SimpleRoom entity)
	{		
		// 得到当前的整体状态
		SimplePhaseState sState = (SimplePhaseState) entity.getStateMachine().GetPhaseState();
		PHASE_TYPE phase = sState.GetPhaseState();
		
		// 检查状态的正确性
		if(phase != m_sPhase)
			return false;

		return true;
	}
	
	public SimpleProto.S2G_MSGTYPE getNotifyMsgType(PHASE_TYPE phase)
	{
		if(phase == PHASE_TYPE.PHASE_GAME_BEGIN)
			return SimpleProto.S2G_MSGTYPE.S2G_NOTIFY_GLOBAL_GAME_BEGIN;
		if(phase == PHASE_TYPE.PHASE_BEGIN)
			return SimpleProto.S2G_MSGTYPE.S2G_NOTIFY_GLOBAL_PHASE_BEGIN;
		if(phase == PHASE_TYPE.PHASE_SKILLCARD)
			return SimpleProto.S2G_MSGTYPE.S2G_NOTIFY_GLOBAL_PHASE_SKILLCARD;
		if(phase == PHASE_TYPE.PHASE_BATTLE_ASK)
			return SimpleProto.S2G_MSGTYPE.S2G_NOTIFY_GLOBAL_PHASE_BATTLE_ASK;
		if(phase == PHASE_TYPE.PHASE_BATTLE_ASSIT)
			return SimpleProto.S2G_MSGTYPE.S2G_NOTIFY_GLOBAL_PHASE_BATTLE_ASSIT;
		if(phase == PHASE_TYPE.PHASE_BATTLE_SKILLCARD)
			return SimpleProto.S2G_MSGTYPE.S2G_NOTIFY_GLOBAL_PHASE_BATTLE_SKILLCARD;
		if(phase == PHASE_TYPE.PHASE_DISCARD)
			return SimpleProto.S2G_MSGTYPE.S2G_NOTIFY_GLOBAL_PHASE_DISCARD;
	
		return SimpleProto.S2G_MSGTYPE.S2G_NOTIFY_GLOBAL_GAME_END;
	}
	
	public String toString()
	{
		return "simple game's father state";
	}

}
