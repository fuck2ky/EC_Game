package io.element.state.card.impl;

import io.element.card.Card;
import io.element.card.Card.HANDCARD;
import io.element.card.impl.BaseCard;
import io.element.card.impl.CardFactory;
import io.element.dataman.ElementDataMan;
import io.element.dataman.ElementDataMan.Essence;
import io.element.dataman.ElementDataMan.ID_SPACE;
import io.element.state.impl.BaseGameState;

public class CardState extends BaseGameState {
	
	protected PerformInfo m_perform;	// card dynamic info
	
	protected Essence m_essence;		// card static info
	
	protected int m_id; 				// every card has a unique id even they are same effect
	
	protected Card m_card;				// the created card in card state
		
	public CardState(int cid)
	{
		m_id = cid;
		CATEGORY cg = BaseCard.categoryType(cid);
		
		if(cg == CATEGORY.HANDCARD)
		{
			HANDCARD ct = HANDCARD.valueOf( BaseCard.innerType(cid) );
			m_essence = (Essence) ElementDataMan.getInstance().get_data_obj(ID_SPACE.ID_CARD_ESSENCE, ct);
		}
		
		m_card = CardFactory.createCard(cid);
	}
	
	public CardState( int cid, long pid)
	{
		this(cid);
		FillUpPerformInfo(pid);
	}
	
	protected void FillUpPerformInfo(long pid)
	{
		
	}
		
}
