package io.element.state.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import io.element.protobuf.SimpleProto;
import io.element.protobuf.SimpleProto.PHASE_TYPE;
import io.element.room.Room;
import io.element.state.global.impl.SG_GameBeginState;
import io.element.state.global.impl.SP_BattleAskState;
import io.element.state.global.impl.SP_BattleAssistState;
import io.element.state.global.impl.SP_BeginState;
import io.element.state.global.impl.SP_DiscardState;
import io.element.state.global.impl.SP_SkillCardState;
import io.element.state.global.impl.SimplePhaseState;

public class SimpleStateMachine extends BaseStateMachine{

	@SuppressWarnings("serial")
	protected static HashMap<SimpleProto.PHASE_TYPE, String> m_nPhases = new HashMap<SimpleProto.PHASE_TYPE, String>()
	{
		{
			put(PHASE_TYPE.PHASE_NONE, 				SG_GameBeginState.class.getName());
			put(PHASE_TYPE.PHASE_GAME_BEGIN,		SP_BeginState.class.getName());
			put(PHASE_TYPE.PHASE_BEGIN, 			SP_SkillCardState.class.getName());
			put(PHASE_TYPE.PHASE_SKILLCARD,			SP_BattleAskState.class.getName());
			put(PHASE_TYPE.PHASE_BATTLE_ASK,		SP_BattleAssistState.class.getName());
			put(PHASE_TYPE.PHASE_BATTLE_ASSIT,		SP_DiscardState.class.getName());
			put(PHASE_TYPE.PHASE_DISCARD,			SP_BeginState.class.getName());
		}
	};
	
	public SimpleStateMachine(Room owner) {
		super(owner);
	}

	public final SimplePhaseState getGlobalPhaseState(){ return (SimplePhaseState)m_sGlobal; }
	
	public static BaseGameState getNextGlobalState(PHASE_TYPE phase)
	{
		String strClass = m_nPhases.get(phase);
        
		SimplePhaseState nState = null;
		try {
			Class<?> clazz = Class.forName(strClass);
			Constructor<?> c1 = clazz.getDeclaredConstructor(new Class[]{});
			nState = (SimplePhaseState) c1.newInstance();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return nState;
	}
	
	public BaseGameState nextPhaseState()
	{
		SimplePhaseState gState = (SimplePhaseState) m_sGlobal;
		
		// 针对特定状态进行检查 某些多种状态转换的
		if(!gState.checkTranslaction(m_owner))
			return gState.translaction(m_owner);
		
		return SimpleStateMachine.getNextGlobalState(gState.GetPhaseState());
	}
	
}
