package io.element.room.impl;

import io.element.Character.Character.CHARACTER;
import io.element.app.App;
import io.element.player.BasePlayer;
import io.element.player.Player;
import io.element.protobuf.LoginProto;
import io.element.room.Room;
import io.element.state.State.CAMP_TYPE;
import io.element.state.State.CHOOSE_TYPE;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.Map.Entry;


public class EntityManager {
	
	public enum TEAM_FLAG { TEAM_NONE, TEAM_RED , TEAM_BLUE; }

	// location & player = map
	protected HashMap<Integer,Player> m_hashPlayers = new HashMap<Integer, Player>();
		
	protected int m_activeIndex;
	
	protected Room m_owner;
	
	public class ActivePlayerInfo
	{
		public Player  m_player = null;
		public TEAM_FLAG m_camp;
		
		public ActivePlayerInfo(Player player, TEAM_FLAG flag)
		{
			m_player = player;
			m_camp   = flag;
		}
		
		public ActivePlayerInfo(){}
	}
	
	public EntityManager(Room owner)
	{
		m_owner = owner;		
	}
	
	public int maxPlayerCount()
	{
		if(m_owner.Type() == LoginProto.ROOM_TYPE.LOGIN_ROOMTYPE_SIMPLE_2V2)
			return 4;
		
		App.LOGGER.error("the room max num uncorrect!!, room id is {}", m_owner.ID());
		return 0;
	}
	
	public boolean initPlayers(List<LoginProto.Player> players)
	{
		int count = players.size();
		for(int i=0;i<count;++i)
		{
			LoginProto.Player player = players.get(i);
			BasePlayer.Builder builder = new BasePlayer.Builder();
			Player newPlayer = builder.location( player.getLocation() )
									  .room((BaseRoom)m_owner)	
									  .id(player.getId())
									  .Name(player.getName())
									  .Character( CHARACTER.valueOf(player.getRoleid()) ).builder();
			if (!this.addPlayer(newPlayer))
				break;
		}
		
		if(m_hashPlayers.size() != maxPlayerCount() )
			return false;
		
		return true;
	}
	
	public boolean initPlayers(List<LoginProto.Player> players, int activeLocation)
	{
		if( activeLocation >= players.size() )
			return false;
		
		if( !initPlayers(players) )
			return false;
		
		for (Entry<Integer,Player> entry : m_hashPlayers.entrySet()) {
			Player player = entry.getValue();
			if(player.GetLocation() == activeLocation )
			{	
				setActivePlayer(player);
				return true;
			}
		}

		return false;
	}
	
	public ActivePlayerInfo nextActivePlayer( )
	{
		if(	m_owner.Type() == LoginProto.ROOM_TYPE.LOGIN_ROOMTYPE_SIMPLE_2V2 ||
			m_owner.Type() == LoginProto.ROOM_TYPE.LOGIN_ROOMTYPE_SIMPLE_3V3 )
			return nextSimpleActive();
		
		return new ActivePlayerInfo();
	}
	
	public ActivePlayerInfo  nextSimpleActive()
	{
		Player curActive = this.getActivePlayer();
		
		// player set location 0 1 2 3 4 5 : max player 6
		// and 0 2 4 means red  camp
		// so  1 3 5 means blue camp
		
		int next = (curActive.GetLocation()+1) % maxPlayerCount();
		Player nextActivePlayer =  m_hashPlayers.get(next);
		
		this.setActivePlayer(nextActivePlayer);
		return new ActivePlayerInfo(nextActivePlayer,EntityManager.getTeamFlag(m_owner.Type(), nextActivePlayer.GetLocation()));
	}
	
	public static TEAM_FLAG getTeamFlag(LoginProto.ROOM_TYPE type, int location)
	{
		if(	type == LoginProto.ROOM_TYPE.LOGIN_ROOMTYPE_SIMPLE_2V2 ||
			type == LoginProto.ROOM_TYPE.LOGIN_ROOMTYPE_SIMPLE_3V3 )
		{		
			if(location % 2 == 0) return TEAM_FLAG.TEAM_RED;
			else return TEAM_FLAG.TEAM_BLUE;
		}
		
		return TEAM_FLAG.TEAM_NONE;
	}
	
	private void setActivePlayer( Player p )
	{
		m_activeIndex = p.GetLocation();
	}
	
	public Player getActivePlayer()
	{
		Player player = m_hashPlayers.get(m_activeIndex);
		return player;
	}
	
	public boolean addPlayer(Player p)
	{
		if(m_hashPlayers.size() >= maxPlayerCount())
			return false;
			
		m_hashPlayers.put( p.GetLocation() , p);
		return true;
	}
	
	public Player getPlayerByLocation(int location)
	{
		return m_hashPlayers.get(location);
	}
	
	public Player getPlayerById(long id)
	{
		for (Entry<Integer, Player> entry : m_hashPlayers.entrySet()) {  
			  
			if( entry.getValue().GetPlayerGuid() == id )
				return entry.getValue();
		}
		return null;
	}
	
	public final HashMap<Integer,Player> getPlayers(){ return m_hashPlayers; }
	
	public int getPlayerCount() { return maxPlayerCount(); }
	
	public int getPlayerCount(TEAM_FLAG camp)
	{
		return 0;	
	}
	
	public Vector<Integer> getVaildLocation(CHOOSE_TYPE chooseType, CAMP_TYPE campType, long hostid)
	{
		int location = -1;
		for (Entry<Integer, Player> entry : m_hashPlayers.entrySet()) {  
			  
			if( entry.getValue().GetPlayerGuid() == hostid ) 
				location = entry.getKey().intValue();
		} 
		if( location == -1 )
			return null;
		
		Vector<Integer> locations = new Vector<Integer>();
		if( campType == CAMP_TYPE.CAMP_FRIEND_S )
		{
			for (Entry<Integer, Player> entry : m_hashPlayers.entrySet()) 
			{
				if( entry.getKey() / 2 == location / 2)
					locations.add(entry.getKey());
			}
			return locations;
		}
		else if( campType == CAMP_TYPE.CAMP_ENEMY_S )
		{
			for (Entry<Integer, Player> entry : m_hashPlayers.entrySet()) 
			{
				if( entry.getKey() / 2 != location / 2)
					locations.add(entry.getKey());
			}
			return locations;
		}else if( campType == CAMP_TYPE.CAMP_ANYONE )
		{
			for (Entry<Integer, Player> entry : m_hashPlayers.entrySet()) 
					locations.add(entry.getKey());
			return locations;
		}
		
		return null;
	}
}
