package io.element.room;

import java.util.ArrayList;
import java.util.List;

import io.element.player.BasePlayer;
import io.element.protobuf.LoginProto;
import io.element.protobuf.LoginProto.ROOM_TYPE;

public class BaseRoom implements Room<LoginProto.ROOM_TYPE,BasePlayer> {

	protected long 					m_id;
	
	protected String  				m_strName;
	
	protected LoginProto.ROOM_TYPE	m_type;
		
	protected List<BasePlayer>   	m_players = new ArrayList<BasePlayer>();
		
	public BaseRoom( long id,
					 String name,
					 LoginProto.ROOM_TYPE type)
	{
		m_id =id;
		m_strName = name;
		m_type = type;
	}
	
	public long getID() {
		// TODO Auto-generated method stub
		return m_id;
	}

	public void setID(long id) {
		// TODO Auto-generated method stub
		m_id = id;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return m_strName;
	}

	public void setName(String name) {
		// TODO Auto-generated method stub
		m_strName = name;
	}

	public ROOM_TYPE type() {
		// TODO Auto-generated method stub
		return m_type;
	}
	
	public List<BasePlayer> getPlayers() {
		// TODO Auto-generated method stub
		return  m_players;
	}
	
	public boolean addPlayer(BasePlayer player)
	{
		if(player == null)
			return false;
		
		return m_players.add(player);
	}

	public void OnTick(long deltaTime)
	{
		
	}

}
