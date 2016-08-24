package io.element.room.impl;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class GM_RoomManager {

	public enum CHANGE_ROOM_STATUS
	{
		STATUS_NONE, ADD_SUCCESS, ADD_REPEAT, REMOVE_SUCCESS, REMOVE_CANNOT_FIND
	}
	
	protected HashMap<Long, GM_BaseRoom> m_hashRooms = new HashMap<Long, GM_BaseRoom>();
	
	public GM_RoomManager()
	{
	}
	
	public void init()
	{
		BaseRoom.RegisterRoomMsgCmd();
	}
	
	public void OnTick(long deltaTime)
	{
		Iterator<Entry<Long, GM_BaseRoom>> iter = m_hashRooms.entrySet().iterator();
		
		while (iter.hasNext()) {
			Entry<Long, GM_BaseRoom> entry = iter.next();
			GM_BaseRoom room = entry.getValue();
			
			room.OnTick(deltaTime);		
			if(!room.Active())
				iter.remove();
		}
	}
	
	public synchronized CHANGE_ROOM_STATUS addRoom(long index)
	{
		BaseRoom room =  m_hashRooms.get(index);
		if(room != null)
			return CHANGE_ROOM_STATUS.ADD_REPEAT;
		GM_BaseRoom newone = new GM_BaseRoom();
		m_hashRooms.put(index, newone);

		return CHANGE_ROOM_STATUS.ADD_SUCCESS;
	}
	
	public synchronized CHANGE_ROOM_STATUS addRoom(GM_BaseRoom room)
	{
		if(m_hashRooms.get(room.ID()) != null)
			return CHANGE_ROOM_STATUS.ADD_REPEAT;
		
		m_hashRooms.put(room.ID(), room);
		return CHANGE_ROOM_STATUS.ADD_SUCCESS;
	}
	
	public synchronized CHANGE_ROOM_STATUS removeRoom(long index)
	{
		return CHANGE_ROOM_STATUS.STATUS_NONE;
	}
	
	public synchronized CHANGE_ROOM_STATUS removeRoom(BaseRoom room)
	{
		Object remove = m_hashRooms.remove(room.ID());
		
		if(remove != null)
			return CHANGE_ROOM_STATUS.REMOVE_SUCCESS;
		return CHANGE_ROOM_STATUS.REMOVE_CANNOT_FIND;
	}
	
	public BaseRoom getRoomByID(long index)
	{
		return m_hashRooms.get(index);
	}
	
}
