package io.element.player;

import java.util.Vector;

import io.element.Character.Character.CHARACTER;

public interface Player {
	
	public long 			GetPlayerGuid();
	public void 			SetPlayerGuid(long id);
	
	public String			GetPlayerName();
	
	public int				GetLocation();
	
	public Character 		GetCharacter();
	public void				SetCharacter(CHARACTER role);
	
	public int 				GetCurrentHP();
	public int				GetMaxHP();
	
	public Vector<Integer> 	GetHandCard();
	
	public boolean			IsMyTurn();
	
	public boolean			HandleState_Enter(Object obj);
	public boolean			HandleState_Execute(Object obj);

}
