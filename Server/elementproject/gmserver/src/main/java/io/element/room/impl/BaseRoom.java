package io.element.room.impl;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.concurrent.FutureTask;

import io.element.card.pool.CardPool;
import io.element.event.Message;
import io.element.event.impl.BaseMessage;
import io.element.protobuf.LoginProto;
import io.element.reflect.Handler;
import io.element.room.Room;
import io.element.state.impl.BaseStateMachine;
import io.element.util.HandlerUtil;

public class BaseRoom implements Room{
	
	// delay task, future task, immediately task queue in dispatcher
	protected BaseDispatcher m_dispatcher = null;
	
	// players saved in manager
	protected EntityManager  m_entityManager = null;
	
	// current room state machine 
	protected BaseStateMachine m_stateMachine = null;
	
	protected CardPool m_cardPool = null;
		
	// current room id
	protected long m_index;
	
	// current room name
	protected String m_name;

	// handler reflect the message and their handler function
	protected static HashMap<Message, Handler> m_handlers = new HashMap<Message, Handler>();

	public BaseRoom()
	{
		m_dispatcher    = new BaseDispatcher(this);
		m_entityManager = new EntityManager(this);
		m_stateMachine  = new BaseStateMachine(this);
	}
	
	public BaseDispatcher getMessageDispatcher()
	{
		return m_dispatcher;
	}
	
	public BaseStateMachine getStateMachine()
	{
		return m_stateMachine;
	}
	
	public EntityManager getEntityManager()
	{
		return m_entityManager;
	}
	
	public CardPool getCardPool()
	{
		return m_cardPool;
	}
		
	// we temply call in room manager 
	public static void RegisterRoomMsgCmd()
	{
		MESSAGE_ROOM_REGISTER_HANDLER(Message.MESSGAE_TYPE.SERVER_SET_FUTURE_TASK, 		"Message_SetFutureTask");
		MESSAGE_ROOM_REGISTER_HANDLER(Message.MESSGAE_TYPE.SERVER_CLEAR_DELAYED_INFO, 	"Message_ClearDelayedTimerInfo");
		
	}
		
	public static void MESSAGE_ROOM_REGISTER_HANDLER(Message.MESSGAE_TYPE type, String str_method)
	{
		Method method = null;
		try {
			method = HandlerUtil.getMethod("io.element.room.impl.BaseRoom", str_method);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(method == null)
			return;
		
		BaseMessage key = new BaseMessage(type);
		Handler handler = new Handler(method);
		m_handlers.put(key, handler);
	}
		
	public void OnTick(long deltaTime) {
		// TODO Auto-generated method stub
			
		m_dispatcher.OnTick(deltaTime);
		
	}
	
	public void Update()
	{
		m_stateMachine.Update();
	}
	
	public Long ID() {
		// TODO Auto-generated method stub
		return m_index;
	}
	
	public void SetID(long id)
	{
		this.m_index = id;
	}

	public LoginProto.ROOM_TYPE Type()
	{
		return LoginProto.ROOM_TYPE.LOGIN_ROOMTYPE_SIMPLE_2V2;
	}
	
	public String getRoomName(){ return m_name; }

	public void HandleMsg(Message msg) {
		// TODO Auto-generated method stub
			
		Handler handler = BaseRoom.m_handlers.get(msg);
		if(handler == null)
			return;
		
		Handler tempHandler = null;
		try {
			 tempHandler = (Handler) handler.clone();			
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// 下面这句话实现了房间事件的 override
		tempHandler.SetObject(this);
		tempHandler.SetParam(msg.GetParam());
		tempHandler.invoke();
	}
	
	//***********************************************
	// the message function called begin for handler 
	// called by SERVER_SET_FUTURE_TASK, and we specify 
	// method with Message_ string
	//***********************************************
	public void Message_SetFutureTask(Object obj)
	{
		@SuppressWarnings("unchecked")
		FutureTask<Integer> task = (FutureTask<Integer>) obj;
		m_dispatcher.SetExecutingTask( task);
	}
		
	public void Message_ClearDelayedTimerInfo(Object obj)
	{
		m_dispatcher.ClearDelayedTimeVec();
	}
	
	//***********************************************
	// the message function called end for handler
	//***********************************************
}
