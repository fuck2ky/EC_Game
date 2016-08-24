package io.element.card.impl;

import java.util.Vector;

import io.element.dataman.ElementDataMan;
import io.element.dataman.ElementDataMan.Essence;
import io.element.dataman.ElementDataMan.ID_SPACE;
import io.element.room.impl.GM_BaseRoom;
import io.element.state.State.CATEGORY;
import io.element.state.State.CHOOSE_TYPE;
import io.element.state.State.InteractiveInfo;
import io.element.state.State.PerformInfo;
import io.element.state.card.impl.HandCardState;
import io.element.state.interactive.impl.InteractiveState;

public  class HandCard extends BaseCard{
	
	protected boolean m_newDraw = true;
	
	// 他的子类自动获取 essence
	public HandCard(int id){ 
		super(id); 
	
		HANDCARD ct = HANDCARD.valueOf( BaseCard.innerType(id) );
		m_essence = (Essence) ElementDataMan.getInstance().get_data_obj(ID_SPACE.ID_CARD_ESSENCE, ct);
	}
	
	public HandCard( Essence essence )
	{
		super(-1);
		m_essence = essence;
	}
	
	public CATEGORY getOuter() {
		// TODO Auto-generated method stub
		return CATEGORY.HANDCARD;
	}
	
	public HANDCARD getInnerType() 
	{  
		return HANDCARD.valueOf( m_essence.id );
	}
	
	public boolean newDraw()
	{
		return m_newDraw;
	}
	
	public boolean newDraw(boolean flag)
	{
		m_newDraw = flag;
		return m_newDraw;
	}
	
	// perform info create after the perform function run
	public void perform(HandCardState state, GM_BaseRoom room) {
		
		PerformInfo pInfo = state.getPerformInfo();
	
		Vector<InteractiveState> states = new Vector<InteractiveState>();
		for (int i = 0; i < pInfo.recvs.size() ; i++) {	
			InteractiveInfo iInfo = pInfo.recvs.get(i);
			
			if(iInfo.einfo.chooseType != CHOOSE_TYPE.NONE && iInfo.performed() )
			{				
				// 一个interactive info 可以创造多个InteractiveState
				// 比如造成伤害什么的
				Vector<InteractiveState> iStates = CardFactory.createInteractiveState(iInfo, pInfo);
				states.addAll(iStates);
			}
			
			// 不需要选择的 直接添加
			if(iInfo.einfo.chooseType == CHOOSE_TYPE.NONE)
			{
				Vector<InteractiveState> iStates = CardFactory.createInteractiveState(iInfo, pInfo);
				states.addAll(iStates);
			}
		}
		
		// 将states push 到 state machine中
		room.getStateMachine().PushInteractiveState(states);
	}
	
}
