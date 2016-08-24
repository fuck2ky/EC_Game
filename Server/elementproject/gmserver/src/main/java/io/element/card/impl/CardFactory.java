package io.element.card.impl;

import java.util.Vector;

import io.element.card.Card;
import io.element.card.Card.HANDCARD;
import io.element.state.State.CATEGORY;
import io.element.state.State.INTERACTIVE_TYPE;
import io.element.state.State.InteractiveInfo;
import io.element.state.State.PerformInfo;
import io.element.state.interactive.impl.InterCureState;
import io.element.state.interactive.impl.InterDamageState;
import io.element.state.interactive.impl.InterDiscardState;
import io.element.state.interactive.impl.InterDrawCardState;
import io.element.state.interactive.impl.InteractiveState;

public class CardFactory {

	public static Card createCard(int cid)
	{
		if( BaseCard.categoryType(cid) == CATEGORY.HANDCARD )
		{
			return createHandCard( cid);
		}
		
		return null;
	}
	
	public static HandCard createHandCard( int cid)
	{		
		if(BaseCard.categoryType(cid) != CATEGORY.HANDCARD)
			return null;
		
		HANDCARD type = HANDCARD.valueOf( BaseCard.innerType(cid) );
		if(type == null)
			return null;
		
		// example 
		if(type == HANDCARD.SP_ICE_HEART)
			return create_Hand_SP_ICE_Heart(cid);
		
		return new HandCard(cid);
	}
	
	protected static HandCard create_Hand_SP_ICE_Heart(int cid)
	{
		return new HandCard(cid);
	}

	/////////////////////////////////////////////////////////////
	// inter active state 
	/////////////////////////////////////////////////////////////
	public static Vector<InteractiveState> createInteractiveState( 	InteractiveInfo info, 	
														   			PerformInfo pInfo)
	{
		if( pInfo.recvs.indexOf(pInfo) == -1 )
			return null;
		
		if(info.einfo.inavType == INTERACTIVE_TYPE.DEAL_DAMAGE)
		{	
			return createInterDamageState(info, pInfo);
		}
		
		if(info.einfo.inavType == INTERACTIVE_TYPE.DEAL_DRAWCARD)
		{
			return createInterDrawCardState(info, pInfo);
		}
		
		if(info.einfo.inavType == INTERACTIVE_TYPE.DEAL_CURE)
		{
			return createInterCureState(info, pInfo);
		}
		
		if(info.einfo.inavType == INTERACTIVE_TYPE.DEAL_DISCARD)
		{
			return createInterDiscardState(info, pInfo);		
		}
		
		return null;
	}
	
	protected static Vector<InteractiveState> createInterDamageState( 	InteractiveInfo info, 	
			   															PerformInfo pInfo )
	{
		Vector<InteractiveState> states = new Vector<InteractiveState>();
		
		for(int i=0; i <info.pids.size(); ++i)
		{		
			InteractiveState state = new InterDamageState(pInfo, pInfo.recvs.indexOf(info) , i);
			states.add(state);
		}
		
		return states;
	}
	
	protected static Vector<InteractiveState> createInterCureState( InteractiveInfo info, 	
																	PerformInfo pInfo )
	{
		Vector<InteractiveState> states = new Vector<InteractiveState>();
		
		for (int i = 0; i < info.pids.size(); i++) {
			InteractiveState state = new InterCureState(pInfo, pInfo.recvs.indexOf(info) , i);
			states.add(state);
		}
		
		return states;
	}
	
	protected static Vector<InteractiveState> createInterDiscardState(  InteractiveInfo info, 	
																		PerformInfo pInfo )
	{
		Vector<InteractiveState> states = new Vector<InteractiveState>();
		
		for (int i = 0; i < info.pids.size(); i++) {
			InteractiveState state = new InterDiscardState(pInfo, pInfo.recvs.indexOf(info) , i);
			states.add(state);
		}
		
		return states;
	}
	
	protected static Vector<InteractiveState> createInterDrawCardState(  InteractiveInfo info, 	
																			PerformInfo pInfo )
	{
		Vector<InteractiveState> states = new Vector<InteractiveState>();
		
		for(int i=0; i <info.pids.size(); ++i)
		{	
			InteractiveState state = new InterDrawCardState(pInfo, pInfo.recvs.indexOf(info) , i);
			states.add(state);
		}
		
		return states;
	}
	
	
}
