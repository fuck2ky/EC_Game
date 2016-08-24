------------------------------------------------------------------------------
--	purpose	: state config store
--  author	: Xiaotian Wang
------------------------------------------------------------------------------

Game_Begin = {
	id				= 100,
	lastTime 		= 10000,					--  剩余时间
	positiveEnd		= true,						-- 	是否可以主动结束
}

Phase_Begin = {
	id				= 101,
	lastTime 		= 10000,					--  剩余时间
	positiveEnd		= true,						-- 	是否可以主动结束
		
}

Phase_SkillCard = {
	id				= 102,
	lastTime 		= 10000,					--  剩余时间
	positiveEnd		= true,						-- 	是否可以主动结束
		
}

Phase_BattleAsk = {
	id				= 103,
	lastTime 		= 10000,					--  剩余时间
	positiveEnd		= true,						-- 	是否可以主动结束
		
}

Phase_BattleAssit = {
	id				= 104,
	lastTime 		= 10000,					--  剩余时间
	positiveEnd		= true,						-- 	是否可以主动结束
		
}

Phase_BattleSkillCard = {
	id				= 105,
	lastTime 		= 10000,					--  剩余时间
	positiveEnd		= true,						-- 	是否可以主动结束
		
}

Phase_BattleCalculate = {
	id				= 106,
	lastTime 		= 10000,					--  剩余时间
	positiveEnd		= true,						-- 	是否可以主动结束
		
}

Phase_Recharge = {
	id				= 107,
	lastTime 		= 10000,					--  剩余时间
	positiveEnd		= true,						-- 	是否可以主动结束
		
}

Phase_Discard = {
	id				= 108,
	lastTime 		= 10000,					--  剩余时间
	positiveEnd		= true,						-- 	是否可以主动结束
		
}

StateEssence.Simple = {

	Game_Begin			= Game_Begin,
	Phase_Begin 		= Phase_Begin,
	Phase_SkillCard 	= Phase_SkillCard,
	Phase_BattleAsk		= Phase_BattleAsk,
	Phase_BattleAssit	= Phase_BattleAssit,
	Phase_BattleSkillCard = Phase_BattleSkillCard,
	Phase_BattleCalculate = Phase_BattleCalculate,
	Phase_Recharge		= Phase_Recharge,
	Phase_Discard		= Phase_Discard,
	
}