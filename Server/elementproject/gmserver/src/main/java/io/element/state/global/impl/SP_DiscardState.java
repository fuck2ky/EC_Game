package io.element.state.global.impl;

import io.element.dataman.ElementDataMan;
import io.element.dataman.ElementDataMan.DATA_TYPE;
import io.element.dataman.ElementDataMan.ID_SPACE;
import io.element.dataman.ElementDataMan.StateEssence;
import io.element.protobuf.SimpleProto.PHASE_TYPE;
import io.element.protobuf.SimpleProto.S2G_MSGTYPE;
import io.element.room.Room;
import io.element.room.impl.EntityManager;
import io.element.room.impl.GM_SimpleRoom;

public class SP_DiscardState extends SimplePhaseState{

	public SP_DiscardState()
	{
		super();
		m_sMsgType = S2G_MSGTYPE.S2G_NOTIFY_GLOBAL_PHASE_DISCARD;
		m_sPhase   = PHASE_TYPE.PHASE_DISCARD;
		m_essence  = (StateEssence) ElementDataMan.getInstance().get_data_obj(ID_SPACE.ID_SPACE_ESSENCE, DATA_TYPE.DT_PHASE_DISCARD);
	}
	
	@Override
	public boolean Exit(Room entity) {

		// we change active player & next state begin will then send refreshed info 
		GM_SimpleRoom room = (GM_SimpleRoom) entity;
		EntityManager mgr  = room.getEntityManager();
		mgr.nextActivePlayer();
		
		return super.Exit(entity);
	}
	
	public String toString()
	{
		return "simple skill card state";
	}
	
}
