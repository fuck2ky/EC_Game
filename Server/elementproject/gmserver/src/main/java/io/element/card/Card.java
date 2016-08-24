package io.element.card;

import io.element.dataman.ElementDataMan.Essence;
import io.element.room.impl.GM_BaseRoom;
import io.element.state.State.CATEGORY;
import io.element.state.card.impl.CardState;


public interface Card {
	
	public static class Config				// dynamic info
	{
		
	}
	
	// 手牌中的一个小类别分得50个标记
	// 战牌	0 	-	49
	// 技牌   50 	-	99
	// 以此类推
	public enum HANDCARD
	{
		HAND_NONE(-1,"HAND_NONE"),
		
		// special card
		SP_ICE_HEART(0,"SP_ICE_HEART"),				// 冰心诀
		SP_BOX_ELIXIR(1,"SP_BOX_ELIXIR"),			// 灵葫仙丹
		SP_STEALTH(2,"SP_STEALTH"),					// 隐蛊
		
		// weapon
		WP_EVIL_KNIFE(50,"WP_EVIL_KNIFE"),			// 魔刀天吒
		WP_CLEAN_SWORD(51,"WP_CLEAN_SWORD"),		// 无尘剑 
		WP_SNAKE_ROD(52,"WP_SNAKE_ROD"),			// 天蛇杖
		WP_GREAT_SWORD(53,"WP_GREAT_SWORD"),		// 魔剑
		
		// armor
		AMR_COLORFUL_DRESS(100,"AMR_COLORFUL_DRESS"),		// 五彩霞衣
		AMR_UNIVERSE_CLOTHES(101,"AMR_UNIVERSE_CLOTHES"),	// 乾坤道袍
		AMR_SOUL_CLOTHES(102,"AMR_SOUL_CLOTHES"),			// 龙魂战铠
		AMR_SACRIFICE_CLOTHES(103,"AMR_SACRIFICE_CLOTHES"),	// 天帝祭服
		
		// skill card
		SK_MOUSE_FRUIT(150,"SK_MOUSE_FRUIT"),		// 鼠儿果
		SK_STEAL(151,"SK_STEAL"),					// 偷盗
		SK_COOPER_DART(152,"SK_COOPER_DART"),		// 铜钱镖
		SK_CURE_GREATLY(153,"SK_CURE_GREATLY"),		// 五气朝元
		
		// battle card
		BT_SKY_VOICE(200,"BT_SKY_VOICE"),			// 天玄五音
		BT_WORM_KING(201,"BT_WORM_KING"),			// 金蚕王
		BT_FIGHT_VOLITION(202,"BT_FIGHT_VOLITION");	// 天罡战气
		
		private int value = 0;
		private String str = new String();
		
		private HANDCARD(int value, String str) {    //必须是private的，否则编译错误
	        this.value = value;
	        this.str = str;
	    }
		
	    public static HANDCARD valueOf(int value) {    //手写的从int到enum的转换函数
	        switch (value) {
	        case -1:
	        	return HAND_NONE;
	        
	        case 0:
	            return SP_ICE_HEART;
	        case 1:
	            return SP_BOX_ELIXIR;
	        case 2:
	            return SP_STEALTH;
	            
	        case 50:
	            return WP_EVIL_KNIFE;
	        case 51:
	            return WP_CLEAN_SWORD;
	        case 52:
	            return WP_SNAKE_ROD;
	        case 53:
	            return WP_GREAT_SWORD;
	            
	        case 100:
	            return AMR_COLORFUL_DRESS;
	        case 101:
	            return AMR_UNIVERSE_CLOTHES;
	        case 102:
	            return AMR_SOUL_CLOTHES;
	        case 103:
	            return AMR_SACRIFICE_CLOTHES;
	            
	        case 150:
	            return SK_MOUSE_FRUIT;
	        case 151:
	            return SK_STEAL;
	        case 152:
	            return SK_COOPER_DART;
	        case 153:
	            return SK_CURE_GREATLY;
	            
	        case 200:
	            return BT_SKY_VOICE;
	        case 201:
	            return BT_WORM_KING;
	        case 202:
	            return BT_FIGHT_VOLITION;
	            
	        default:
	            return null;
	        }
	    }

	    public int toValue() {
	        return this.value;
	    }
	    
	    public String toString(){
	    	return this.str;
	    }
	}
	
	public enum EVENT
	{
		
	}
	
	public enum CHARACTER
	{
		
	}
	
	public enum MONSTER
	{
		
	}
		
	// normal play current card
	public void perform(CardState state, GM_BaseRoom room);
	
	// skill play current card
	public void disciplines(CardState state, GM_BaseRoom room);
		
	// 牌的大类
	public CATEGORY getOuter();
	
	// get card essence
	public Essence 	getEssence();
	
	// 32 byte id
	public int 		getId();
		
	
}
