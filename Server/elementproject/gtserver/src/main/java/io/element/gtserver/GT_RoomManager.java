package io.element.gtserver;

import io.element.room.impl.SessionRoom;
import io.element.room.impl.SessionRoom.ROOM_STATUS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class GT_RoomManager implements GT_Managers.Manager {
	
	public enum CHANGE_ROOM_STATUS
	{
		STATUS_NONE, ADD_SUCCESS, ADD_REPEAT, REMOVE_SUCCESS, REMOVE_CANNOT_FIND
	}
		
	protected HashMap<Long, SessionRoom> m_rooms = new HashMap<Long, SessionRoom>();
	
	public GT_RoomManager() {}  
	    	
	public void init()
	{
		
	}
	 
	public void OnTick(long deltaTime)
	{
		Iterator<Entry<Long, SessionRoom>> iter = m_rooms.entrySet().iterator();
		List<Long> removeRooms = new ArrayList<Long>();
		
		// active room tick
		while (iter.hasNext()) {
			Entry<Long, SessionRoom> entry = iter.next();
			SessionRoom room = entry.getValue();
			room.OnTick(deltaTime);
			
			if( room.getStatus() == ROOM_STATUS.DELETE )
				removeRooms.add(room.getID());
		}
		
		// 删除 de active rooms
		for (Long id : removeRooms) {
			this.removeRoom(id);
		}
		
	}
	
	public synchronized CHANGE_ROOM_STATUS addRoom(SessionRoom room)
	{
		if(m_rooms.get(room.getID()) != null)
			return CHANGE_ROOM_STATUS.ADD_REPEAT;
		
		m_rooms.put(room.getID(), room);
		return CHANGE_ROOM_STATUS.ADD_SUCCESS;
	}
	
	public synchronized CHANGE_ROOM_STATUS removeRoom(SessionRoom room)
	{
		return removeRoom(room.getID());
	}
	
	public synchronized CHANGE_ROOM_STATUS removeRoom(Long id)
	{
		SessionRoom room = m_rooms.get(id);
		if( room == null )
			return CHANGE_ROOM_STATUS.REMOVE_CANNOT_FIND;
		
		room.CloseLogicConnect();
		m_rooms.remove(id);
		return CHANGE_ROOM_STATUS.REMOVE_SUCCESS;
	}
	
	public SessionRoom getRoomByID(long index)
	{
		return m_rooms.get(index);
	}
}
