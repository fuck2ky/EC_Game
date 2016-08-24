package io.element.state.card.impl;

import io.element.card.Card.HANDCARD;
import io.element.card.impl.BaseCard;
import io.element.state.State.CATEGORY;
import io.element.state.impl.AskingResponseState.AlterInfo;


public class CardStateFactory {

	
	public static CardState createCardState(int cid, long pid, Object param )
	{
		CATEGORY cg = BaseCard.categoryType(cid);
		if(cg == CATEGORY.HANDCARD)
			return createHandCardState(cid,pid,param);
		
		return null;
	}
	
	public static HandCardState createHandCardState(int cid, long pid, Object param )
	{
		HANDCARD hc = HANDCARD.valueOf( BaseCard.innerType(cid) );
		if(hc == HANDCARD.SP_STEALTH)
			return createStealthState(cid, pid, param);
		
		return null;
	}
	
	public static HandCardState createStealthState(int cid, long pid, Object param)
	{
		return new SP_StealthState(cid, pid,(AlterInfo) param);
	}
	
}
