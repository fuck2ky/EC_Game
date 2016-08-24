package io.element.dataman;

import io.element.app.App;
import io.element.card.Card.HANDCARD;
import io.element.card.impl.CardFactory;
import io.element.card.impl.HandCard;
import io.element.script.Luaf;
import io.element.state.State.CAMP_TYPE;
import io.element.state.State.CATEGORY;
import io.element.state.State.CHOOSE_TYPE;
import io.element.state.State.INTERACTIVE_TYPE;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import javax.script.Bindings;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

public class ElementDataMan {
	
	protected String m_root =  (String) App.context.getBean("scriptroot");
		
	private static class LazyHolder {    
		private static final ElementDataMan INSTANCE = new ElementDataMan();    
	}    
	
	private ElementDataMan (){
	
	} 
	
	public static final ElementDataMan getInstance() {    
		return LazyHolder.INSTANCE;    
	}
	
	public enum ID_SPACE			// 大类
	{
		ID_SPACE_ESSENCE,
		ID_CARD_ESSENCE,
	}
	
	public enum DATA_TYPE			// 小类
	{
		DT_GAME_BGEIN,
		DT_PHASE_BEGIN,
		DT_PHASE_SKILLCARD,
		DT_PHASE_BATTLE_ASK,
		DT_PHASE_BATTLE_ASSIT,
		DT_PHASE_BATTLE_SKILLCARD,
		DT_PHASE_BATTLE_CALCULATE,
		DT_PHASE_RECHARGE,
		DT_PHASE_DISCARD ,
	}
	
	public boolean Load()
	{		
		return Load_StateEssences() && Load_HandCardEssences();
	}
	
	public static class Essence				// dead info
	{
		public int id;
	}
		
	// type is returned value
	public Object get_data_obj(int id , ID_SPACE space, DATA_TYPE type)
	{
		if( space == ID_SPACE.ID_SPACE_ESSENCE )
		{
			for (Entry<DATA_TYPE, Essence> entry : m_essences.entrySet()) {
				StateEssence essence = (StateEssence) entry.getValue();
				if(essence.id == id){
					type = entry.getKey();
					return essence;
				}
			}
		}
		return null;
	}
	
	public Object get_data_obj(ID_SPACE space, DATA_TYPE type)
	{
		if( space == ID_SPACE.ID_SPACE_ESSENCE )
		{
			return m_essences.get(type);
		}
		
		return null;
	}
	
	public Object get_data_obj(ID_SPACE space, HANDCARD type)
	{
		if(space == ID_SPACE.ID_CARD_ESSENCE)
		{
			return m_essences_hand.get(type);
		}
		
		return null;
	}
		
   /*
	*	state essence load function & class define 
	*
	*	begin
	*/
	protected HashMap<DATA_TYPE, Essence> m_essences = new HashMap<DATA_TYPE, Essence>();
	
	public static class StateEssence extends Essence
	{
		public int lastTime;
		public boolean positiveEnd;
	}
	
	public boolean Load_StateEssences()
	{
		Vector<String> filespath = new Vector<String>();
		filespath.add( m_root + "StateEssence.lua");
		filespath.add( m_root + "SimpleStateEssence.lua");
		String strTable = new String("StateEssence");
		
	 	Bindings bindings    = Luaf.createBindings();
	 	LuaTable stateTable  = Luaf.getTableInFile(bindings, filespath, strTable);
	 	if( stateTable == null)
	 		return false;
	 	
	 	strTable = "Simple";
		LuaTable simpleTable = Luaf.getTableInTable(stateTable, strTable);
	 	
		HashMap<String,DATA_TYPE> labels = new HashMap<String,DATA_TYPE>();
		labels.put("Game_Begin",        DATA_TYPE.DT_GAME_BGEIN);
		labels.put("Phase_Begin", 		DATA_TYPE.DT_PHASE_BEGIN);
		labels.put("Phase_SkillCard",	DATA_TYPE.DT_PHASE_SKILLCARD);
		labels.put("Phase_BattleAsk",	DATA_TYPE.DT_PHASE_BATTLE_ASK);
		labels.put("Phase_BattleAssit", DATA_TYPE.DT_PHASE_BATTLE_ASSIT);
		labels.put("Phase_BattleSkillCard", DATA_TYPE.DT_PHASE_BATTLE_SKILLCARD);
		labels.put("Phase_BattleCalculate", DATA_TYPE.DT_PHASE_BATTLE_CALCULATE);
		labels.put("Phase_Recharge",		DATA_TYPE.DT_PHASE_RECHARGE);
		labels.put("Phase_Discard",		DATA_TYPE.DT_PHASE_DISCARD);
		
		for (Entry<String, DATA_TYPE> entry : labels.entrySet()) {
			StateEssence essence = getStateEssences(simpleTable,entry.getKey());
			if(essence != null)
				m_essences.put(entry.getValue(), essence);
		}
		
		return true;
	}
	
	protected StateEssence getStateEssences(LuaTable lv, String label)
	{
		StateEssence essence = new StateEssence();
		LuaValue table = (LuaValue) lv.get(label);
		if(table== null)
			return null;
		
		essence.id       = table.get("id").toint();
		essence.lastTime = table.get("lastTime").toint();
		essence.positiveEnd = table.get("positiveEnd").toboolean();
		
		return essence;
	}
	
   /*
    *	card event character essence load function & class define
	*
	*	begin
	*/
	protected HashMap<HANDCARD, Essence> m_essences_hand = new HashMap<HANDCARD, Essence>();	
	
	public static class CardExecuteInfo
	{
		public INTERACTIVE_TYPE inavType;
		public int 				inavValue;
		
		public CAMP_TYPE		campType;
		public CHOOSE_TYPE		chooseType;
	}
	
	public static class HandCardEssence extends Essence
	{
		public final Vector<CardExecuteInfo> executes;
		
		public final int simpleCount;
		
		public final boolean perfectVaild;
				
		public HandCardEssence( HANDCARD card  ,int simpleCount , boolean perfectVaild , Vector<CardExecuteInfo> vec){
			this.executes = vec;
			this.simpleCount = simpleCount;
			this.perfectVaild = perfectVaild;
			this.id = card.toValue();
		}
	}
	
	public boolean Load_HandCardEssences()
	{
		Vector<String> filespath = new Vector<String>();
		filespath.add( m_root + "CardEssence.lua");
		filespath.add( m_root + "HandCardEssence.lua");
		String strTable = new String("CardEssence");
		
	 	Bindings bindings    = Luaf.createBindings();
	 	LuaTable cardTable   = Luaf.getTableInFile(bindings, filespath, strTable);
	 	if( cardTable == null)
	 		return false;
	 	
	 	strTable = "HandCard";
		LuaTable hcardTable  = Luaf.getTableInTable(cardTable, strTable);
		
		// get sp card table
		strTable = "SpecialCard";
		LuaTable spcardTable = Luaf.getTableInTable(hcardTable, strTable);
		this.Load_HandCardTable(spcardTable);
		
		// get skill card table
		strTable = "SkillCard";
		LuaTable skillTable  = Luaf.getTableInTable(hcardTable, strTable);
		this.Load_HandCardTable(skillTable);
		
		// get battle card table
		strTable = "BattleCard";
		LuaTable battleTable  = Luaf.getTableInTable(hcardTable, strTable);
		this.Load_HandCardTable(battleTable);
		
		return true;
	}
	
	protected void Load_HandCardTable(LuaTable t)
	{
		Varargs iter = t.next( LuaValue.NIL );
		while (iter.arg(1) != LuaValue.NIL)
		{
			LuaValue key = iter.arg(1);
			HANDCARD ekey = HANDCARD.valueOf(key.toint());
			if(ekey == null)
				continue;
			
			LuaTable table = (LuaTable) iter.arg(2);
			LuaTable table_inav = (LuaTable) table.get("InteractiveType");
			LuaTable table_camp = (LuaTable) table.get("CampType");
			LuaTable table_choose = (LuaTable) table.get("ChooseType");
			LuaTable table_count = (LuaTable) table.get("Count");
			
			boolean perfectVaild = table.get("PerfectVaild").toboolean();
			int simpleCount = table_count.get("Simple").toint();
			Vector<CardExecuteInfo> executes = new Vector<CardExecuteInfo>();
			
			int inner_key = 1;
			while(table_inav.get(inner_key) != LuaValue.NIL )
			{
				CardExecuteInfo ceInfo = new CardExecuteInfo();
				
				LuaTable table_inav_inner = (LuaTable) table_inav.get(inner_key);
				ceInfo.inavType  = INTERACTIVE_TYPE.valueOf( table_inav_inner.get("Interactive").toint() );
				ceInfo.inavValue = table_inav_inner.get("Count").toint(); 
				ceInfo.campType  = CAMP_TYPE.valueOf(table_camp.get(inner_key).toint());
				ceInfo.chooseType= CHOOSE_TYPE.valueOf( table_choose.get(inner_key).toint() );
				
				executes.add(ceInfo);
				inner_key++;
			}
			
			HandCardEssence essence = new HandCardEssence(ekey,simpleCount, perfectVaild, executes);
			m_essences_hand.put(ekey, essence);
			
			iter = t.next(key);
		}
	}
		
	public Vector<HandCard> createSimpleHandCards()
	{	  
		Vector<HandCard> cards = new Vector<HandCard>();
				
		for (Map.Entry<HANDCARD, Essence> entry : m_essences_hand.entrySet()) {  
			
			HandCardEssence essence = (HandCardEssence) entry.getValue();
			HANDCARD hc = entry.getKey();

			int cardIndex = 0 ;
			int cateIndex = CATEGORY.HANDCARD.toValue();
			int cartId    = hc.toValue();
			for( int i=0; i < essence.simpleCount; ++i )
			{
				int finalid = cateIndex << 24 + cartId << 16 + (cardIndex++) << 8;
				HandCard card = CardFactory.createHandCard(finalid);
				if(card == null)
				{
					App.LOGGER.warn("create hand card which id can not found: {}", cartId );
					continue;
				}
				cards.add(card);
			}	
		}  
				
		return cards;
	}
	
}
















