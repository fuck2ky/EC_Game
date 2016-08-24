package io.element.communication.impl;

import com.google.protobuf.InvalidProtocolBufferException;

import io.element.communication.ProtoMsgHandler;
import io.element.gtserver.App;
import io.element.protobuf.GlobalProto.MESSAGE;
import io.element.protobuf.SimpleProto;
import io.element.reflect.Handler;
import io.element.util.HandlerUtil;
import io.netty.channel.ChannelHandlerContext;

public class S2GProto_SimpleHandler extends ProtoMsgHandler<SimpleProto.S2G_MSGTYPE,SimpleProto.S2GByteStream>{

	private static final S2GProto_SimpleHandler single = new S2GProto_SimpleHandler();  
	 
	private S2GProto_SimpleHandler() { m_globalType = MESSAGE.MESSAGE_SIMPLE_S2G; }  
	    
	public static S2GProto_SimpleHandler getInstance() {  
        return single;  
	}  
	
	{
		RegisterProtoMsgCmd();
	}
	
	@Override
	public void RegisterProtoMsgCmd() {
		super.RegisterProtoMsgCmd();
		
		PROTOMSG_REGISTER_HANDLER(SimpleProto.S2G_MSGTYPE.S2G_NOTIFY_GLOBAL_GAME_BEGIN,				"CallBack_S2G_NotifyGame_BeginGame");

		PROTOMSG_REGISTER_HANDLER(SimpleProto.S2G_MSGTYPE.S2G_NOTIFY_GLOBAL_PHASE_BEGIN,			"CallBack_S2G_NotifyPhase_BeginGame");
		PROTOMSG_REGISTER_HANDLER(SimpleProto.S2G_MSGTYPE.S2G_NOTIFY_GLOBAL_PHASE_SKILLCARD, 		"CallBack_S2G_NotifyPhase_SkillCard");
		PROTOMSG_REGISTER_HANDLER(SimpleProto.S2G_MSGTYPE.S2G_NOTIFY_GLOBAL_PHASE_BATTLE_ASK, 		"CallBack_S2G_NotifyPhase_BattleAsk");
		PROTOMSG_REGISTER_HANDLER(SimpleProto.S2G_MSGTYPE.S2G_NOTIFY_GLOBAL_PHASE_BATTLE_ASSIT, 	"CallBack_S2G_NotifyPhase_BattleAssit");
		PROTOMSG_REGISTER_HANDLER(SimpleProto.S2G_MSGTYPE.S2G_NOTIFY_GLOBAL_PHASE_BATTLE_SKILLCARD, "CallBack_S2G_NotifyPhase_BattleSkillCard");
		PROTOMSG_REGISTER_HANDLER(SimpleProto.S2G_MSGTYPE.S2G_NOTIFY_GLOBAL_PHASE_BATTLE_CALCULATE, "CallBack_S2G_NotifyPhase_Calculate");
		PROTOMSG_REGISTER_HANDLER(SimpleProto.S2G_MSGTYPE.S2G_NOTIFY_GLOBAL_PHASE_RECHARGE, 		"CallBack_S2G_NotifyPhase_Recharge");
		PROTOMSG_REGISTER_HANDLER(SimpleProto.S2G_MSGTYPE.S2G_NOTIFY_GLOBAL_PHASE_DISCARD, 			"CallBack_S2G_NotifyPhase_Discard");

	}
	
	@Override
	public boolean HandlerMsg(ChannelHandlerContext channel, SimpleProto.S2GByteStream param) {
		SimpleProto.S2G_MSGTYPE type = param.getType();
		Handler handler = m_handlers.get(type);
		if( handler == null || !handler.isVaild())
			return false;
		
		Handler tempHandler = null;
		try {
			 tempHandler = (Handler) handler.clone();			
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			tempHandler.SetObject(this);
			tempHandler.SetParam(param);
			tempHandler.invoke();
		} catch (Exception e) {
			App.LOGGER.info("S2GProto_SimpleHandler invoke method error, type is {}", type.toString());
		}

		return true;
	}
	
	//***********************************************
	// the message function called begin for handler 
	// called by GlobalProtoBufHandler---LOGINPROTO,  
	// we specify method with Callback_ string
	//***********************************************
	
	// S2G_NOTIFY_GLOBAL_GAME_BEGIN
	public void CallBack_S2G_NotifyGame_BeginGame(Object obj) throws InvalidProtocolBufferException
	{
		SimpleProto.S2G_NotifyPhaseBegin msg = SimpleProto.S2G_NotifyPhaseBegin.parseFrom( ((SimpleProto.S2GByteStream) obj).getRequestData() );
		SimpleProto.Room room = msg.getRoom();
		
		HandlerUtil.recordStateInfo(room.getId(), "S2G_NOTIFY_GLOBAL_GAME_BEGIN enter!!!\t\t"+room.getId());
	}
	
	// S2G_NOTIFY_GLOBAL_PHASE_BEGIN
	public void CallBack_S2G_NotifyPhase_BeginGame(Object obj) throws InvalidProtocolBufferException
	{		
		SimpleProto.S2G_NotifyPhaseBegin msg = SimpleProto.S2G_NotifyPhaseBegin.parseFrom( ((SimpleProto.S2GByteStream) obj).getRequestData() );
		SimpleProto.Room room = msg.getRoom();
		SimpleProto.Active active = room.getActive();
		
		HandlerUtil.recordStateInfo(room.getId(), "S2G_NOTIFY_GLOBAL_PHASE_BEGIN enter!!!\t\t"+room.getId());
		HandlerUtil.recordStateInfo(room.getId(), "the current active player id is " + active.getPlayerid());
	}
	
	// S2G_NOTIFY_GLOBAL_PHASE_SKILLCARD
	public void CallBack_S2G_NotifyPhase_SkillCard(Object obj) throws InvalidProtocolBufferException
	{
		SimpleProto.S2G_NotifyPhaseBegin msg = SimpleProto.S2G_NotifyPhaseBegin.parseFrom( ((SimpleProto.S2GByteStream) obj).getRequestData() );
		SimpleProto.Room room = msg.getRoom();

		HandlerUtil.recordStateInfo(room.getId(), "S2G_NOTIFY_GLOBAL_PHASE_SKILLCARD enter!!!\t\t"+room.getId());
	}
	
	// S2G_NOTIFY_GLOBAL_PHASE_BATTLEASK
	public void CallBack_S2G_NotifyPhase_BattleAsk(Object obj) throws InvalidProtocolBufferException
	{
		SimpleProto.S2G_NotifyPhaseBegin msg = SimpleProto.S2G_NotifyPhaseBegin.parseFrom( ((SimpleProto.S2GByteStream) obj).getRequestData() );
		SimpleProto.Room room = msg.getRoom();

		HandlerUtil.recordStateInfo(room.getId(), "S2G_NOTIFY_GLOBAL_PHASE_BATTLEASK enter!!!\t\t"+room.getId());
	}
		
	// S2G_NOTIFY_GLOBAL_PHASE_BATTLE_ASSIT
	public void CallBack_S2G_NotifyPhase_BattleAssit(Object obj) throws InvalidProtocolBufferException
	{
		SimpleProto.S2G_NotifyPhaseBegin msg = SimpleProto.S2G_NotifyPhaseBegin.parseFrom( ((SimpleProto.S2GByteStream) obj).getRequestData() );
		SimpleProto.Room room = msg.getRoom();
		
		HandlerUtil.recordStateInfo(room.getId(), "S2G_NOTIFY_GLOBAL_PHASE_BATTLE_ASSIT enter!!!\t\t"+room.getId());
	}
	
	// S2G_NOTIFY_GLOBAL_PHASE_BATTLE_SKILLCARD
	public void CallBack_S2G_NotifyPhase_BattleSkillCard(Object obj) throws InvalidProtocolBufferException
	{
		SimpleProto.S2G_NotifyPhaseBegin msg = SimpleProto.S2G_NotifyPhaseBegin.parseFrom( ((SimpleProto.S2GByteStream) obj).getRequestData() );
		SimpleProto.Room room = msg.getRoom();
		
		HandlerUtil.recordStateInfo(room.getId(), "S2G_NOTIFY_GLOBAL_PHASE_BATTLE_SKILLCARD enter!!!\t\t"+room.getId());
	}
	
	// S2G_NOTIFY_GLOBAL_PHASE_BATTLE_CALCULATE
	public void CallBack_S2G_NotifyPhase_Calculate(Object obj) throws InvalidProtocolBufferException
	{
		SimpleProto.S2G_NotifyPhaseBegin msg = SimpleProto.S2G_NotifyPhaseBegin.parseFrom( ((SimpleProto.S2GByteStream) obj).getRequestData() );
		SimpleProto.Room room = msg.getRoom();
		
		HandlerUtil.recordStateInfo(room.getId(), "S2G_NOTIFY_GLOBAL_PHASE_BATTLE_CALCULATE enter!!!\t\t"+room.getId());
	}
	
	// S2G_NOTIFY_GLOBAL_PHASE_RECHARGE
	public void CallBack_S2G_NotifyPhase_Recharge(Object obj) throws InvalidProtocolBufferException
	{
		SimpleProto.S2G_NotifyPhaseBegin msg = SimpleProto.S2G_NotifyPhaseBegin.parseFrom( ((SimpleProto.S2GByteStream) obj).getRequestData() );
		SimpleProto.Room room = msg.getRoom();
		
		HandlerUtil.recordStateInfo(room.getId(), "S2G_NOTIFY_GLOBAL_PHASE_RECHARGE enter!!!\t\t"+room.getId());
	}
	
	// S2G_NOTIFY_GLOBAL_PHASE_DISCARD
	public void CallBack_S2G_NotifyPhase_Discard(Object obj) throws InvalidProtocolBufferException
	{
		SimpleProto.S2G_NotifyPhaseBegin msg = SimpleProto.S2G_NotifyPhaseBegin.parseFrom( ((SimpleProto.S2GByteStream) obj).getRequestData() );
		SimpleProto.Room room = msg.getRoom();
		
		HandlerUtil.recordStateInfo(room.getId(), "S2G_NOTIFY_GLOBAL_PHASE_DISCARD enter!!!\t\t"+room.getId());
	}
	
	//***********************************************
	// the message function called end for handler
	//***********************************************

}
