package io.element.state.global.impl;

import io.element.dataman.ElementDataMan;
import io.element.dataman.ElementDataMan.DATA_TYPE;
import io.element.dataman.ElementDataMan.ID_SPACE;
import io.element.dataman.ElementDataMan.StateEssence;
import io.element.protobuf.SimpleProto.PHASE_TYPE;
import io.element.protobuf.SimpleProto.S2G_MSGTYPE;
import io.element.room.Room;

public class SG_GameBeginState extends SimplePhaseState{
	
	public SG_GameBeginState()
	{
		super();
		m_sMsgType = S2G_MSGTYPE.S2G_NOTIFY_GLOBAL_GAME_BEGIN;
		m_sPhase   = PHASE_TYPE.PHASE_GAME_BEGIN;
		m_essence  = (StateEssence) ElementDataMan.getInstance().get_data_obj(ID_SPACE.ID_SPACE_ESSENCE, DATA_TYPE.DT_GAME_BGEIN);
	}
	
	@Override
	public boolean Enter(Room entity) 
	{
		// 初始玩家的active信息 在创建room的时候就已经决定好了  enter的时候将active信息传给client，
		// client 跟进 msg type 进行具体的处理
		return super.Enter(entity);
	}
		
	public String toString()
	{
		return "simple game global begin!!!!";
	}


	
}
