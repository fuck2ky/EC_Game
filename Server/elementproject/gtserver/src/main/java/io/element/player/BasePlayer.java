package io.element.player;

import io.element.communication.impl.ClientSession;

public class BasePlayer implements Player{

	protected ClientSession 	m_session;			// 当前角色的会话
	
	protected long				m_id;				// 当前角色的唯一id
	
	protected String			m_sName;			// 当前角色的名字
	
	protected int 				m_iLocation;		// 当前角色的位置
	
	protected boolean 			m_deactive = false;	// 掉线标记
	
	protected boolean			m_match = false;	// 匹配标记
	
	public BasePlayer(ClientSession session)
	{
		m_session = session;
		m_iLocation = -1;
	}
			
	public boolean isActive()
	{	
		// 强制掉线标记
		if( m_deactive )
			return false;
		
		return m_session != null ? m_session.isActive() : false;
	}
	
	public long GetPlayerGuid() {
		// TODO Auto-generated method stub
		return m_id;
	}

	public void SetPlayerGuid(long id) {
		// TODO Auto-generated method stub
		m_id = id;
	}

	public String GetPlayerName() {
		// TODO Auto-generated method stub
		return m_sName;
	}

	public void SetPlayerName(String name) {
		// TODO Auto-generated method stub
		m_sName = name;
	}

	public int GetLocation() {
		// TODO Auto-generated method stub
		return m_iLocation;
	}

	public void SetLocation(int location) {
		// TODO Auto-generated method stub
		m_iLocation = location;
	}

	public ClientSession GetSession() {
		// TODO Auto-generated method stub
		return m_session;
	}
	
	public void SetMatching( boolean flag )
	{
		m_match = flag;
	}
	
	public boolean isMatching()
	{
		return m_match;
	}
	
	public void SetActive( boolean flag )
	{
		m_deactive = !flag;
	}
		
	public Object sendMessage(Object message) {
		if(m_session != null)
			return m_session.sendMessage(message);
		
		return null;
	}
		
	public boolean InRoom() {
		return m_iLocation > 0;
	}

	public void OnTick(long deltaTime) {
			
		
	}
	
}
