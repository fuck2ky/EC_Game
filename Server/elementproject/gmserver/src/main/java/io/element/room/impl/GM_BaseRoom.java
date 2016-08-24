package io.element.room.impl;

import java.util.List;

import io.element.app.App;
import io.element.communication.RoomSession;
import io.element.protobuf.LoginProto;
import io.element.room.RoomConfig;

public class GM_BaseRoom extends BaseRoom{

	// players in entity manager
	// maybe we need a card manager later
	
	protected boolean 		m_bActive;				//	用于是否销毁房间的flag
	
	protected RoomSession 	m_sSession;
	
	public GM_BaseRoom(long roomid, RoomSession session, String name)
	{
		super();
		m_index 	= roomid;
		m_sSession 	= session; 
		m_bActive 	= true;
		m_name 		= name;
		
		session.setOwner(this);
	}
		
	public GM_BaseRoom()
	{
		super();
		m_bActive 	= true;
	}
		
	public boolean CheckPlayersCard_Perfect()
	{
		return false;
	}
	
	public boolean CheckPlayersSkill_Perfect()
	{
		return false;
	}
	
	public boolean Check_Perfect()
	{
		return CheckPlayersCard_Perfect() || CheckPlayersSkill_Perfect();
	}
	
	public boolean CheckPlayersCard_Stealth()
	{
		return false;
	}
	
	public boolean CheckPlayersSkill_Stealth()
	{
		return false;
	}
	
	public boolean Check_Stealth()
	{
		return CheckPlayersCard_Stealth() || CheckPlayersSkill_Stealth();
	}
	
	public void OnTick(long deltaTime) {
		
		if(!m_bActive)
			return;
		
		m_sSession.OnTick(deltaTime);
		super.OnTick(deltaTime);
	}
	
	public boolean Active(boolean flag)
	{
		m_bActive = flag;
		return m_bActive;
	}

	public boolean Active()
	{		
		if(!m_bActive && m_dispatcher.isDone())			// 连接断开 任务做完了
		{
			if(m_dispatcher.isDone())
				//future work done, close session
				session().close();
			
			return false;
		}else if( !m_bActive && !m_dispatcher.isDone() )	// 连接断开 任务没做完
			return true;
				
		return m_bActive;
	}
	
	public RoomSession session()
	{
		return m_sSession;
	}
	
	public int GetInitActiveLocation(List<LoginProto.Player> players)
	{
		return 0;			// 默认房间的起始active location为 0
	}
	
	public RoomConfig config() 
	{
		RoomConfig config = (RoomConfig) App.context.getBean("sRoomConfig");
		return config;
	}
		
}
