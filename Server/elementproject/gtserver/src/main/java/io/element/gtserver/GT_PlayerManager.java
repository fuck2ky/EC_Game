package io.element.gtserver;


import io.element.communication.impl.ClientSession;
import io.element.player.BasePlayer;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;


public class GT_PlayerManager implements GT_Managers.Manager {

	public static HashMap<String, BasePlayer> m_mapSessions = new HashMap<String, BasePlayer>();
	
	public GT_PlayerManager()
	{

	}

	public void init() {
		// TODO Auto-generated method stub
		
	}

	public void OnTick(long deltaTime) {

		List<String> deactiveList = new ArrayList<String>();
		
		// player tick
		for (Entry<String, BasePlayer> entry : m_mapSessions.entrySet()) {		
			BasePlayer p = entry.getValue();
			p.OnTick(deltaTime);
			if( !p.isActive() )
				deactiveList.add(entry.getKey());
		}
		
		// 删除失去链接的player
		for (String key : deactiveList)
		{
			RemovePlayer(key);
		}
		
	}
	
	public BasePlayer CreateLoginPlayer( String name, ClientSession session )
	{
		BasePlayer p = new BasePlayer(session);
		p.SetPlayerGuid( Const.GLOBAL_PLAYER_ID++ );
		// TODO 拼凑的名字
		p.SetPlayerName( "TestPlayer" + String.format("%d", p.GetPlayerGuid()) );
		AddPlayer(p);
		return p;
	}
	
	public synchronized BasePlayer GetPlayer( String key )
	{
		return m_mapSessions.get(key);
	}
	
	protected synchronized void AddPlayer( BasePlayer p )
	{
		if( m_mapSessions.containsKey( p.GetSession().toString() ) )
			m_mapSessions.remove(p.GetSession().toString());
		
		m_mapSessions.put(p.GetSession().toString(), p);
	}
	
	protected synchronized void AddPlayer( ClientSession session, String name )
	{
		BasePlayer p = new BasePlayer(session);
		p.SetPlayerName(name);
		
		if( m_mapSessions.containsKey( p.GetSession().toString() ) )
			m_mapSessions.remove(p.GetSession().toString());
		
		m_mapSessions.put(p.GetSession().toString(), p);
	}
	
	protected synchronized boolean RemovePlayer( BasePlayer p )
	{
		if( m_mapSessions.containsKey( p.GetSession().toString() ) )
			m_mapSessions.remove(p.GetSession().toString());
		return false;
	}
	
	protected synchronized boolean RemovePlayer( String keySession )
	{
		if( m_mapSessions.containsKey( keySession ) )
			m_mapSessions.remove(keySession);
		return false;
	}
	
}
