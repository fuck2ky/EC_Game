package io.element.task.impl;

import io.element.event.impl.BaseTask;
import io.element.room.impl.BaseRoom;
import io.element.server.GM_Mangers;

public class Task_StateRun extends BaseTask{

	public Task_StateRun(long roomid)
	{
		this.m_roomid = roomid;
	}
	
	public Integer call() throws Exception {
		
		long roomid = this.roomid();
		BaseRoom room = (BaseRoom) GM_Mangers.getRoomManager().getRoomByID(roomid);
		
		if(room == null)
			return -1;
		
		room.Update();
		return 1;
	}
	
}
