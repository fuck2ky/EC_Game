package io.element.card.impl;

import io.element.card.Card;
import io.element.dataman.ElementDataMan.Essence;
import io.element.room.impl.GM_BaseRoom;
import io.element.state.State.CATEGORY;
import io.element.state.card.impl.CardState;

public class BaseCard implements Card{

	protected Essence m_essence = null;
	
	protected int m_id;
	
	// essence 是  1.没有参数  自己构造  2.使用传参构造
	// 保证每个card创造的时候都有自己的 essence
	public BaseCard(int id)
	{
		m_id = id;
	}
	
	public BaseCard( Essence essence )
	{
		m_essence = essence;
	}
	
	// perform info create after the perform function run
	public void perform(CardState state, GM_BaseRoom room) {
		// TODO Auto-generated method stub
		
	}

	public void disciplines(CardState state, GM_BaseRoom room) {
		// TODO Auto-generated method stub
		
	}

	public CATEGORY getOuter() {
		// TODO Auto-generated method stub
		return CATEGORY.NONE;
	}

	public Essence getEssence() {
		// TODO Auto-generated method stub
		return m_essence;
	}

	public int getId() {
		// TODO Auto-generated method stub
		return m_id;
	}
	
	public static CATEGORY categoryType(int cid)
	{
		int h8 = cid >> 24;
		return CATEGORY.valueOf(h8);	
	}
	
	public static int innerType(int cid)
	{
		return (cid & 0x0F00) >> 16;
	}
}
