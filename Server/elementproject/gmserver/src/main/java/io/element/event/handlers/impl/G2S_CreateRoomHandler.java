package io.element.event.handlers.impl;

import java.util.List;

import io.element.communication.RoomSession;
import io.element.event.EventHandler;
import io.element.event.impl.BaseEvent;
import io.element.event.impl.G2S_CreateRoomEvent;
import io.element.protobuf.LoginProto;
import io.element.protobuf.LoginProto.CREATEROOM_STATUS;
import io.element.room.impl.GM_BaseRoom;
import io.element.room.impl.GM_RoomManager.CHANGE_ROOM_STATUS;
import io.element.room.impl.GM_SimpleRoom;
import io.element.server.GM_Mangers;

public class G2S_CreateRoomHandler extends EventHandler {

	@Override
	public <T> boolean applyHandler(BaseEvent<T> event) {
		
		if( event == null || !((Object)event instanceof G2S_CreateRoomEvent) )
			return false;
		
		G2S_CreateRoomEvent 			vevent  = (G2S_CreateRoomEvent) event;
		LoginProto.G2S_CreateNewRoom 	buffer  = (LoginProto.G2S_CreateNewRoom)vevent.GetBuffer();
		LoginProto.Room					essence = buffer.getRoom();
		RoomSession        				session = new RoomSession( vevent.GetChannel() );
		
		vevent.SetStatus( this.createRoom( 	essence.getType(), 
											essence.getId(), 
											essence.getName(),
											essence.getPlayersList(),
											session ) );
		return true;
	}

	public CREATEROOM_STATUS createRoom(	LoginProto.ROOM_TYPE type,
											long roomid, 
											String name,
											List<LoginProto.Player> players,
											RoomSession session	)
	{
		GM_BaseRoom room = null;
		
		switch (type) {
		case LOGIN_ROOMTYPE_SIMPLE_2V2:
			room = new GM_SimpleRoom(roomid,session,name);
			break;
		case LOGIN_ROOMTYPE_SIMPLE_3V3:
			room = new GM_SimpleRoom(roomid,session,name);
			break;
		default:
			break;
		}
		
		if( !room.getEntityManager().initPlayers(players,room.GetInitActiveLocation(players)) )
			return CREATEROOM_STATUS.CREATEROOM_FAILED_UNKNOWN;
			
		CREATEROOM_STATUS code = CREATEROOM_STATUS.CREATEROOM_SUCCESS;
		if(room != null)
		{ 
			CHANGE_ROOM_STATUS status = GM_Mangers.getRoomManager().addRoom(room) ;
			if( status == CHANGE_ROOM_STATUS.ADD_REPEAT )
				code = CREATEROOM_STATUS.CREATEROOM_FAILED_DUMPILCATE;
		}
		
		return code;
	}
}
