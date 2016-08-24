package io.element.card.pool;

import io.element.card.impl.EventCard;
import io.element.card.impl.HandCard;
import io.element.player.BasePlayer;
import io.element.room.Room;
import io.element.room.impl.GM_BaseRoom;

import java.util.Vector;

public abstract class CardPool {

	protected Vector<HandCard> m_handCards = null; 			// 牌堆     	手牌
	
	protected Vector<HandCard> m_discardCards = null;		// 弃牌堆	手牌
	
	protected Vector<EventCard> m_eventCards = null;		// 牌堆		事件牌
	
	protected GM_BaseRoom m_owner;
	
	public CardPool(GM_BaseRoom room)
	{
		m_owner = room;
		
		m_handCards = new Vector<HandCard>();
		m_discardCards = new Vector<HandCard>();
		m_eventCards = new Vector<EventCard>();
				
		refreshHandCard(room);
	}
	
	// 给当前玩家抓value张牌
	public void drawHandCards(int value, BasePlayer player)
	{
		if(value <= 0)
			return ;
		
		for(int i=0; i<value; ++i)
			drawHandCard(player);

	}
	
	// 给当前玩家抓1张牌
	public boolean drawHandCard(BasePlayer player)
	{
		return player.drawHandCard(this);
	}
	
	// 
	public HandCard popHandCard()
	{
		if(m_handCards.size() == 0)
			return null;
		
		HandCard card = m_handCards.remove(0);
		m_discardCards.add(card);
		return card;
	}
	
	//
	public abstract void refreshHandCard(Room entity);
}
