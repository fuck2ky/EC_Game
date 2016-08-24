package io.element.room;

import java.util.List;

public interface Room<T,P> {

	public long 	getID();
	
	public void 	setID(long id);
	
	public String 	getName();

	public void  	setName(String name);
	
	public T		type();
	
	public List<P> 	getPlayers();
	
	public boolean		addPlayer(P player);
		
}
