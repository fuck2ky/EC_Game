package io.element.event.handlers.impl;

import java.lang.reflect.Method;
import java.util.HashMap;

import io.element.app.App;
import io.element.cmd.S2G;
import io.element.event.impl.BaseEvent;
import io.element.event.impl.G2S_CreateRoomEvent;
import io.element.event.impl.G2S_LoginEvent;
import io.element.protobuf.LoginProto;
import io.element.protobuf.SimpleProto;
import io.element.protobuf.LoginProto.CREATEROOM_STATUS;
import io.element.protobuf.LoginProto.G2S_MSGTYPE;
import io.element.reflect.Handler;
import io.element.room.impl.GM_BaseRoom;
import io.element.server.GM_Mangers;
import io.element.task.impl.Task_SimplePhase;
import io.element.util.HandlerUtil;

// 专门和room dispatcher交互的handler & 负责login proto  便于复用代码
public class G2S_LoginTaskHandler extends G2S_CreateRoomHandler{

	protected static HashMap<G2S_LoginEvent<?>, Handler> m_handlers = new HashMap<G2S_LoginEvent<?>, Handler>();
	
	@Override
	public <T> boolean applyHandler(BaseEvent<T> event) {
				
		Handler handler = G2S_LoginTaskHandler.m_handlers.get(event);
				
		if(handler == null)
			return false;
		
		Handler tempHandler = null;
		try {
			 tempHandler = (Handler) handler.clone();			
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		tempHandler.SetObject(this);
		tempHandler.SetParam(event);
		tempHandler.invoke();
		return event.continuation;
	}
	
	static {
		G2S_LoginTaskHandler.RegisterLoginEventCmd();	// EVENT_LOGIN_TYPE handler
		
	}
	
	public static void RegisterLoginEventCmd()
	{
		EVENT_LOGIN_REGISTER_HANDLER(G2S_MSGTYPE.G2S_REQUEST_CREATE_NEWROOM, "G2S_CreateNewRoomTask");
	}
	
	public static void EVENT_LOGIN_REGISTER_HANDLER(G2S_MSGTYPE type, String str_method)
	{
		Method method = null;
		try {
			method = HandlerUtil.getMethod(G2S_LoginTaskHandler.class.getName(), str_method);
		} catch (Exception e) {
			App.LOGGER.error( "注册函数失败，函数名为%s", str_method);
		}
		
		if(method == null)
			return;
		
		G2S_LoginEvent<?> key = new G2S_LoginEvent<Object>(type);
		Handler handler = new Handler(method);
		m_handlers.put(key, handler);
	}
	
	// get function handler
	public Handler ReflectHandler(LoginProto.G2S_MSGTYPE type) {
		Handler handler = G2S_LoginTaskHandler.m_handlers.get(type);
		if(handler == null)
			return null;
		
		Handler tempHandler = null;
		try {
			 tempHandler = (Handler) handler.clone();
			 tempHandler.SetObject(this);
		} catch (CloneNotSupportedException e) {

		}
		
		return tempHandler;
	}
	
	// log in event handlers begin	////////////////////////////
	////////////////////////////////////////////////////////////
	
	public void G2S_CreateNewRoomTask(Object event) 
	{
		G2S_CreateRoomEvent vEvent 	 		 = (G2S_CreateRoomEvent) event;
		LoginProto.G2S_CreateNewRoom essence = vEvent.GetConvertBuffer(); 	
		GM_BaseRoom 		room 	 		 = (GM_BaseRoom) GM_Mangers.getRoomManager().getRoomByID(essence.getRoom().getId());
		
		if(room == null)
		{	
			vEvent.continuation = false;
			return;
		}
				
		if( vEvent.GetStatus() != CREATEROOM_STATUS.CREATEROOM_SUCCESS )
		{
			vEvent.continuation = false;		
			S2G.Cmd_Resp_CreateNewRoom(room, vEvent.GetStatus());
			return;
		}

		// resp for create game room
		S2G.Cmd_Resp_CreateNewRoom(room, vEvent.GetStatus());
		
		// create a task according to room type immediately
		room.getMessageDispatcher().DispatchTask_Tail(new Task_SimplePhase(room.ID(), SimpleProto.PHASE_TYPE.PHASE_NONE ));
	}
	
	// log in event handlers end	////////////////////////////
	////////////////////////////////////////////////////////////
	
	
}
