package io.element.player;

import io.element.communication.impl.ClientSession;


public interface Player {

	public long 			GetPlayerGuid();
	public void 			SetPlayerGuid(long id);
	
	public String			GetPlayerName();
	public void				SetPlayerName(String name);
	
	public int				GetLocation();
	public void				SetLocation(int location);
	public boolean			InRoom();
	
	public ClientSession	GetSession();
	
	public void				OnTick(long deltaTime);
	
}
