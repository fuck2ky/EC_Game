package io.element.state;


import io.element.card.impl.BaseCard;
import io.element.dataman.ElementDataMan.CardExecuteInfo;
import io.element.protobuf.SimpleProto;
import io.element.state.impl.AskingResponseState.CardInfo;

import java.util.Vector;

//	牌池以及相应的id根据 函数算法生成  client server 保持一致就好
//	32位保存一个特定的id
//	xxxx xxxx 		|| xxxx xxxx 		|| xxxx xxxx 		|| xxxx xxxx
//	大类--CATEGORY		小类--与大类相关		第几张				保留
//
//	对于大类是牌来说   小类就有可能是 handcard 。。。。。
//	对于技能来说  没有第几张的概念

public interface State<T,E> {

	enum STATE_MESSAGE{
		
		STATE_PLAY_PERFECT_CARD,
		STATE_PLAY_PERFECT_SKILL,
		
	}
	
	// 操作的种类：手牌（ 战牌 技牌 特殊牌 武器牌 防御牌 ） 事件牌 人物牌 怪物牌
	public enum CATEGORY
	{
		HANDCARD(0),EVENTCARD(1),CHARACTER(2),MONSTER(3),SKILL(4),STATES(5),DEFAULT(6),NONE(7);
		
		private int value = 0;
		
		private CATEGORY(int value) {    //必须是private的，否则编译错误
	        this.value = value;
	    }
		
	    public static CATEGORY valueOf(int value) {    //手写的从int到enum的转换函数
	        switch (value) {
	        case 0:
	            return HANDCARD;
	        case 1:
	            return EVENTCARD;
	        case 2:
	            return CHARACTER;
	        case 3:
	            return MONSTER;
	        case 4:
	        	return SKILL;
	        case 5:
	            return STATES;
	        case 6:
	            return DEFAULT;
	        case 7:
	        	return NONE;
	        default:
	            return null;
	        }
	    }
	    
	    public int toValue() {
	        return this.value;
	    }
	    
	    public SimpleProto.REACTOR_TYPE reactor()
	    {
	    	if( value == 0)
	    		return SimpleProto.REACTOR_TYPE.REACTOR_CARD;
	    	if( value == 4)
	    		return SimpleProto.REACTOR_TYPE.REACTOR_SKILL;
	    		
	    	return null;
	    }
	    
	}
	
	////////////////////////////////////////////
	// 操作 ESSENCE 配套   不能随意修改
	// 当前牌的交互方式
	////////////////////////////////////////////
	public enum INTERACTIVE_TYPE
	{
		DEAL_DAMAGE(0),							// 造成伤害
		DEAL_CURE(1),							// 治疗
		DEAL_DRAWCARD(2),						// 抓牌
		DEAL_DISCARD(3),						// 弃牌
		DEAL_STEAL(4),							// 偷牌
		DEAL_DEFAULT(5),						// 默认操作方式
		DEAL_EQUIPMENT(6),						// 装备
		DEAL_FIGHTPLUS(7),						// 战力增加
			
		HP_TO_VALUE(8);							// hp 变成 指定值
		
		private int value = 0;
		
		private INTERACTIVE_TYPE(int value) {    //必须是private的，否则编译错误
	        this.value = value;
	    }
		
	    public static INTERACTIVE_TYPE valueOf(int value) {    //手写的从int到enum的转换函数
	        switch (value) {
	        case 0:
	            return DEAL_DAMAGE;
	        case 1:
	            return DEAL_CURE;
	        case 2:
	            return DEAL_DRAWCARD;
	        case 3:
	            return DEAL_DISCARD;
	        case 4:
	        	return DEAL_STEAL;
	        case 5:
	        	return DEAL_DEFAULT;
	        case 6:
	        	return DEAL_EQUIPMENT;
	        case 7:
	        	return DEAL_FIGHTPLUS;
	        case 8:
	        	return HP_TO_VALUE;
	        default:
	            return null;
	        }
	    }
	    
	    public int toValue() {
	        return this.value;
	    }
		
	}
	
	////////////////////////////////////////////
	// 操作 ESSENCE 配套   不能随意修改
	// 根据阵营选择玩家配置
	////////////////////////////////////////////
	public enum CAMP_TYPE
	{
		CAMP_FRIEND_S(0),	CAMP_FIREND_A(1), 
		CAMP_ENEMY_S(2), 	CAMP_ENEMY_A(3),
		CAMP_ANYONE(4),		CAMP_ALLPLAYER(5);

		private int value = 0;
		
		private CAMP_TYPE(int value) {    //必须是private的，否则编译错误
	        this.value = value;
	    }
		
	    public static CAMP_TYPE valueOf(int value) {    //手写的从int到enum的转换函数
	        switch (value) {
	        case 0:
	            return CAMP_FRIEND_S;
	        case 1:
	            return CAMP_FIREND_A;
	        case 2:
	            return CAMP_ENEMY_S;
	        case 3:
	            return CAMP_ENEMY_A;
	        case 4:
	            return CAMP_ANYONE;
	        case 5:
	            return CAMP_ALLPLAYER;
	        default:
	            return null;
	        }
	    }
	    
	    public int toValue() {
	        return this.value;
	    }
	}
	
	////////////////////////////////////////////
	// 操作 ESSENCE 配套   不能随意修改
	// 选择人的具体形式  
	// （手牌 武器 （即可手牌又可武器）  仅仅是人）
	////////////////////////////////////////////
	public enum CHOOSE_TYPE
	{
		NONE(0),HANDCARD(1),EQUIPMENT(2),ALLCARD(3),PLAYER(4);
		
		private int value = 0;
		
		private CHOOSE_TYPE(int value) {    //必须是private的，否则编译错误
	        this.value = value;
	    }
		
	    public static CHOOSE_TYPE valueOf(int value) {    //手写的从int到enum的转换函数
	        switch (value) {
	        case 0:
	            return NONE;
	        case 1:
	            return HANDCARD;
	        case 2:
	            return EQUIPMENT;
	        case 3:
	            return ALLCARD;
	        case 4:
	            return PLAYER;
	        default:
	            return null;
	        }
	    }
	    
	    public int toValue() {
	        return this.value;
	    }
	}
	
	////////////////////////////////////////////
	// 操作 dynamic info 配套   不能随意修改
	// 卡牌使用数据
	// 选择玩家 xxxx
	////////////////////////////////////////////
	
	public static class OperateValue
	{
		public Integer value = null;
		public Integer preValue = null;
		
		public OperateValue( Integer value, Integer preValue )
		{
			this.value = value;
			this.preValue = preValue;
		}
		
		public OperateValue( Integer value )
		{
			this.value = value;
		}
	}
	
	public static class InteractiveInfo
	{
		public static class PidValues				// 单个玩家的信息
		{
			public Long pid;										// 玩家的id	
			
			public Vector<Object> values = new Vector<Object>();	// 玩家的操作数据
			
			public PidValues(Long pid) { this.pid = pid; }
			
			public PidValues(Long pid, Vector<Object> objects) 
			{ 
				this.pid = pid; 
				values.addAll(objects);
			}
			
			public PidValues(Long pid, Object obj)
			{
				this.pid = pid; 
				values.add(obj);
			}
		}
		
		// recv player ids
		public Vector<PidValues> pids = new Vector<PidValues>();
		// recv player type
		public CardExecuteInfo einfo;
		// perform check flag, whether the interactive itself performed?
		protected boolean performed;
	
		public InteractiveInfo(CardExecuteInfo info)
		{
			einfo = info;
			performed = false;
		}
		
		public Integer getLocation(int rid)
		{
			return 0;
		}
		
		public void setPerformed(boolean flag) { performed = flag; }
		
		public boolean performed(){ return performed; }
	}
	
	public static class PerformInfo			// 卡牌 or skill
	{
		public long pid;	// 打出此操作--玩家的id
		public long rid;	// 打出此操作--玩家所在房间的id
		public int  cid;	// 打出此操作--操作的unique id
		
		public Vector<InteractiveInfo> recvs = new Vector<InteractiveInfo>();
		public boolean perFlag = false;
		
		public PerformInfo( long playerid, long roomid ,   InteractiveInfo recvid )
		{
			this.pid = playerid;
			this.rid = roomid;

			recvs.add(recvid);
		}
		
		public PerformInfo( long playerid,int cardid , Vector<InteractiveInfo> recvs )
		{
			this.pid = playerid;
			this.cid = cardid;
			
			this.recvs = recvs;
		}
		
		public CATEGORY categoryType()
		{
			return BaseCard.categoryType(cid);
		}
		
		public CardInfo cardinfo()
		{
			if( categoryType() == CATEGORY.HANDCARD )
				return new CardInfo(cid, pid, perFlag);
			return null;
		}	
	}
	
	/**
	 * @return Returns the unique id associated with this game object.
	 */
	public boolean Enter(T entity);
	
	/**
	 * @return Returns the unique id associated with this game object.
	 */
	public boolean Excute(T entity);
	
	/**
	 * @return Returns the unique id associated with this game object.
	 */
	public boolean Exit(T entity);
	
	/**
	 * @return Returns the unique id associated with this game object.
	 */
	public boolean HandlerMsg(E event);
	
	public boolean IsBegan();
	
	public void HandlerMessage(STATE_MESSAGE message,Object param);

	
}