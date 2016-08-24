package io.element.room;

import io.element.event.Message;
import io.element.protobuf.LoginProto;

public interface Room {
	

	
	/**
	 * @return Returns the unique id associated with this game object.
	 */
	public void OnTick(long deltaTime);
	
	/**
	 * @return Returns the unique id associated with this game object.
	 */
	public Long ID();
	
	/**
	 * 控制来自state的一些状态处理
	 */
	public void HandleMsg(Message msg);
	
	/**
	 * @return Returns the unique type associated with this room object.
	 */
	public LoginProto.ROOM_TYPE Type();
		
}
