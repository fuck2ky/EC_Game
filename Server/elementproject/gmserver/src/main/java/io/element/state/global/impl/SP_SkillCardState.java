package io.element.state.global.impl;

import io.element.dataman.ElementDataMan;
import io.element.dataman.ElementDataMan.DATA_TYPE;
import io.element.dataman.ElementDataMan.ID_SPACE;
import io.element.dataman.ElementDataMan.StateEssence;
import io.element.protobuf.SimpleProto.PHASE_TYPE;
import io.element.protobuf.SimpleProto.S2G_MSGTYPE;

public class SP_SkillCardState extends SimplePhaseState{
	
	public SP_SkillCardState()
	{
		super();
		m_sMsgType = S2G_MSGTYPE.S2G_NOTIFY_GLOBAL_PHASE_SKILLCARD;
		m_sPhase   = PHASE_TYPE.PHASE_SKILLCARD;
		m_essence  = (StateEssence) ElementDataMan.getInstance().get_data_obj(ID_SPACE.ID_SPACE_ESSENCE, DATA_TYPE.DT_PHASE_SKILLCARD);
	}
	
	public String toString()
	{
		return "simple skill card state";
	}

}
