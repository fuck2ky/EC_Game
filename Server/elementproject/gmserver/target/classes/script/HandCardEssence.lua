------------------------------------------------------------------------------
--	purpose	: card detail info store
--  author	: Xiaotian Wang
------------------------------------------------------------------------------

-- PerfectVaild			�Ƿ���Ա� ���ľ�

-- InteractiveType		������ʽ   INTERACTIVE_TYPE �� count ƥ��

-- CampType				ѡ���˵���Ӫ��ͬһ��Ӫ �ѷ���Ӫ �з���Ӫ��

-- ChooseType			ѡ���˵ķ�ʽ������ װ�� ȫ����

-- PropertyPlus			��������			

-- Count 				���Ƶ�����	


function CreatEnumTable(tbl, index) 
    local enumtbl = {} 
    local enumindex = index or -1   -- lua ��1��ʼ
    for i, v in ipairs(tbl) do 
        enumtbl[v] = enumindex + i 
    end 
    return enumtbl 
end 

-- �� card java ����һ��
Interactive = 
{ 
    "DEAL_DAMAGE", 
    "DEAL_CURE",
    "DEAL_DRAWCARD",
    "DEAL_DISCARD",
    "DEAL_STEAL",
    "DEAL_DEFAULT",
    "DEAL_EQUIPMENT",
    "DEAL_FIGHTPLUS",
    
    "HP_TO_VALUE",
}
Interactive = CreatEnumTable(Interactive)

CampEnum = 
{
	"CAMP_FRIEND_S",
	"CAMP_FIREND_A",
	"CAMP_ENEMY_S",
	"CAMP_ENEMY_A",
	"CAMP_ANYONE",
	"CAMP_ALLPLAYER",
}
CampEnum = CreatEnumTable(CampEnum)

ChooseEnum = 
{
	"NONE",
	"HANDCARD",
	"EQUIPMENT",
	"ALLCARD",
	"PLAYER",
}
ChooseEnum = CreatEnumTable(ChooseEnum)

-- ������
SpecialCard = 
{
	--SP_ICE_HEART = 
	[0] = 
	{
		PerfectVaild 	= true,
		InteractiveType = { 
							{ Interactive = Interactive.DEAL_DEFAULT, Count = 0 },
						  },
		CampType		= {	CampEnum.CAMP_ANYONE, },
		ChooseType		= { ChooseEnum.PLAYER, },
		Count			= { Simple = 3, }
	},
	
	--SP_BOX_ELIXIR = 
	[1] =
	{
		PerfectVaild 	= true,
		InteractiveType = {
							{ Interactive = Interactive.DEAL_CURE, Count = 2 },
						  },
		CampType  		= { CampEnum.CAMP_ANYONE, },
		ChooseType		= { ChooseEnum.PLAYER, },
		Count			= { Simple = 3, }
	},
	
	--SP_STEALTH = 
	[2] =
	{
		PerfectVaild	= true,
		InteractiveType = {
							{ Interactive = Interactive.DEAL_DEFAULT, Count = 0},
						  },
		
		CampType		= { CampEnum.CAMP_ANYONE, },
		ChooseType		= { ChooseEnum.NONE },
		Count			= { Simple = 4, }
	},

}

-- ����
SkillCard = 
{
	--SK_MOUSE_FRUIT = 
	[150] =
	{
		PerfectVaild	= true,
		InteractiveType = {
						  	{ Interactive = Interactive.DEAL_DRAWCARD, Count = 2},
						  },
		
		CampType 		= { CampEnum.CAMP_ANYONE, },
		ChooseType		= { ChooseEnum.PLAYER, },
		Count			= { Simple = 3 },
	},
	
	--SK_STEAL =
	[151] = 
	{
		PerfectVaild 	= true,
		InteractiveType = {
						  	{ Interactive = Interactive.DEAL_STEAL , Count = 1 }
						  },
		CampType		= { CampEnum.CAMP_ANYONE, },
		ChooseType		= { ChooseEnum.HANDCARD, },
		Count			= { Simple = 2, },
		
	},
	
	--SK_COOPER_DART = 
	[152] = 
	{
		PerfectVaild 	= true,
		InteractiveType	= {
						  	{ Interactive = Interactive.DEAL_DISCARD , Count = 1, }
						  },
		
		CampType		= { CampEnum.CAMP_ANYONE, },
		ChooseType		= { ChooseEnum.ALLCARD, },
		Count 			= { Simple = 3, } 
	},
	
	--SK_CURE_GREATLY = 
	[153] = 
	{
		PerfectVaild	= true,
		InteractiveType = {
							{ Interactive = Interactive.DEAL_CURE , Count = 1, }
						  },
		CampType		= { CampEnum.CAMP_FIREND_A, },
		ChooseType		= { ChooseEnum.NONE, },
		Count			= { Simple = 2, }
	},
}

-- ս��
BattleCard = 
{
	--BT_SKY_VOICE = 
	[200] = 
	{
		PerfectVaild 	= true,
		InteractiveType = {
							{ Interactive = Interactive.DEAL_FIGHTPLUS , Count = 2, }
						  },
		CampType	 	= { CampEnum.CAMP_FIREND_A, },
		ChooseType   	= { ChooseEnum.NONE, },
		Count  		 	= { Simple = 8, }
	},
	
	--BT_WORM_KING = 
	[201] = 
	{
		PerfectVaild 	= true,
		InteractiveType = {
							{ Interactive = Interactive.DEAL_FIGHTPLUS , Count = 3, }
						  },
		CampType	 	= { CampEnum.CAMP_FIREND_S, },	
		ChooseType   	= { ChooseEnum.NONE, },
		Count  		 	= { Simple = 5, }				  
	},
	
	--BT_FIGHT_VOLITION = 
	[202] = 
	{
		PerfectVaild 	= true,
		InteractiveType = {
							{ Interactive = Interactive.DEAL_FIGHTPLUS , Count = 0, }
						  },
		CampType		= { CampEnum.CAMP_FIREND_S, },	
		ChooseType   	= { ChooseEnum.NONE, },
		Count  		 	= { Simple = 2, }	
	}
}

CardEssence.HandCard = {

	-- ������
	SpecialCard = SpecialCard,
	-- ����
	SkillCard   = SkillCard,
	-- ս��
	BattleCard	= BattleCard,

}































