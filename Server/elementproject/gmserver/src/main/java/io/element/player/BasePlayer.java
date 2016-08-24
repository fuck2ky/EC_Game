package io.element.player;

import io.element.Character.BaseCharacter;
import io.element.Character.Character.CHARACTER;
import io.element.card.impl.ArmorCard;
import io.element.card.impl.HandCard;
import io.element.card.impl.WeaponCard;
import io.element.card.pool.CardPool;
import io.element.room.impl.BaseRoom;

import java.util.Vector;

public class BasePlayer implements Player {

	protected long									m_id;				// 当前角色的唯一id
	protected io.element.Character.Character		m_role;				// 最好保存的是大量静态数据 关于角色的
	protected String								m_sName;
	
	// game information
	protected int									m_iCurHP;
	protected Vector<HandCard> 						m_cards = new Vector<HandCard>();	// 手牌
	protected WeaponCard							m_weapon;			// 武器
	protected ArmorCard								m_armor;			// 防具			
	
	// Room information 
	protected boolean								m_turn;				// is my turn?
	protected int 									m_iLocation;		// my location in cur room
	protected BaseRoom								m_owner;			// my room
	
	public BasePlayer()
	{
		m_turn = false;
	}
	
	public boolean InitWithCharacter(CHARACTER cRole)
	{
		m_role = null;
		m_role = BaseCharacter.createCharacter(cRole);
		
		if(m_role == null)
			return false;
		
		return true;
	}
	
	public long GetPlayerGuid() {
		// TODO Auto-generated method stub
		return m_id;
	}

	public void SetPlayerGuid(long id) {
		// TODO Auto-generated method stub
		m_id = id;
	}

	public Character GetCharacter() {
		// TODO Auto-generated method stub
		return null;
	}

	public void SetCharacter(CHARACTER role) {
		// TODO Auto-generated method stub
		
	}

	public int GetCurrentHP() {
		// TODO Auto-generated method stub
		return m_iCurHP;
	}

	public int GetMaxHP() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public boolean IsDying()
	{
		return m_iCurHP > 0 ? false : true;
	}

	public Vector<Integer> GetHandCard() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean IsMyTurn() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean HandleState_Enter(Object obj) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean HandleState_Execute(Object obj) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public int GetLocation() {
		// TODO Auto-generated method stub
		return m_iLocation;
	}
	
	public String GetPlayerName() {
		// TODO Auto-generated method stub
		return m_sName;
	}
	
	////////////////////////////////////////////////////////////////////
	// hand card info
	////////////////////////////////////////////////////////////////////
	public Vector<Integer> getNewDrawCardIds()
	{
		Vector<Integer> news = new Vector<Integer>();
		
		for (HandCard card : m_cards) {
			if(card.newDraw())
				news.add(card.getId());
		}
		return news;
	}
	
	public void refreshNewDrawCard()
	{
		for (HandCard card : m_cards) {
			card.newDraw(false);
		}
	}
	

	
	public final Vector<HandCard> seekHandCards()
	{
		return this.m_cards;
	}
	
	////////////////////////////////////////////////////////////////////
	// hand card info
	////////////////////////////////////////////////////////////////////
	public final WeaponCard getWeapon()
	{
		return this.m_weapon;
	}
	
	public final ArmorCard getArmor()
	{
		return this.m_armor;
	}
	
	public int getEquipmentCount()
	{
		int count = 0 ;
		if(m_weapon != null)
			count++;
		if(m_armor != null)
			count++;
		return count;
	}
	
	////////////////////////////////////////////////////////////////////
	// player game logic info
	////////////////////////////////////////////////////////////////////
	public int damaged(int value , BasePlayer trigger)
	{
		this.m_iCurHP = Math.max( m_iCurHP - value , 0);
		return this.m_iCurHP;
	}
	
	public int cured(int value , BasePlayer trigger)
	{
		this.m_iCurHP = Math.min( m_iCurHP +value, GetMaxHP() );
		return this.m_iCurHP;
	}
	
	public boolean drawHandCard(CardPool pool)
	{	
		HandCard card = pool.popHandCard();
		if(card == null )
		{
			pool.refreshHandCard(m_owner);
			card = pool.popHandCard();
		}
		
		this.m_cards.add(card);
		return true;
	}
	
	public Vector<Integer> discardCards(Vector<Integer> ids)
	{
		
		
		return null;
	}
	
	public Vector<Integer> discardCards(Vector<Integer> ids, BasePlayer p)
	{
		return null;
	}
	
	public static class Builder
	{
		protected int 			m_iLocation;		// my location in cur room
		protected BaseRoom		m_owner;			// my room
		protected long			m_id;				// 当前角色的唯一id
		protected CHARACTER		m_role;				// 最好保存的是大量静态数据 关于角色的
		protected String		m_name;
		
		public Builder location(int location)			{ m_iLocation = location; return this; }
		public Builder room(BaseRoom room)				{ m_owner = room; return this; }
		public Builder id(long id)						{ m_id = id; return this; }
		public Builder Character(CHARACTER character)	{ m_role = character; return this; }
		public Builder Name(String name)				{ m_name = name; return this;}
		
		public BasePlayer builder()
		{
			BasePlayer player = new BasePlayer();
			
			player.m_iLocation = m_iLocation;
			player.m_owner     = m_owner;
			player.m_id        = m_id;
			player.m_sName	   = m_name;
			player.InitWithCharacter(m_role);
			
			return player;
		}
		
	}

}
