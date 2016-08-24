package io.element.state.interactive.impl;

import com.google.protobuf.ByteString;

import io.element.player.BasePlayer;
import io.element.protobuf.GlobalProto;
import io.element.protobuf.LoginProto;
import io.element.protobuf.SimpleProto;
import io.element.room.Room;
import io.element.room.impl.GM_BaseRoom;
import io.element.util.HandlerUtil;


public class InterCureState extends InteractiveState{

	protected int m_playerIndex;
	
	public InterCureState(io.element.state.State.PerformInfo info, int index, int playerIndex) {
		super(info, index);
		m_playerIndex = playerIndex;
	}

	@Override
	public boolean Enter(Room entity) {
		return super.Enter(entity);
	}
	
	@Override
	public boolean Excute(Room entity) {
		
		GM_BaseRoom room = (GM_BaseRoom) entity;
	
		InteractiveInfo iInfo = m_perInfo.recvs.get( m_interIndex );
		if(iInfo == null)
		{
			room.getStateMachine().ChangeState();
			return false;
		}
		
		// get player and deal cure
		long pid = iInfo.pids.get(m_playerIndex).pid;
		BasePlayer recver = (BasePlayer) room.getEntityManager().getPlayerById( pid );
		BasePlayer hoster = (BasePlayer) room.getEntityManager().getPlayerById( m_perInfo.pid );
		
		int preHp = recver.GetCurrentHP();
		int curHp = recver.cured(iInfo.einfo.inavValue, hoster);
		
		// send cure msg to clients !!!!!!!!!!!!!!!
		this.sendCureMsg(room, preHp, curHp);
		
		// update state machine
		room.getStateMachine().ChangeState();
		return true;
	}
	
	protected void sendCureMsg(GM_BaseRoom room, int preHp , int curHp)
	{
		InteractiveInfo iInfo = m_perInfo.recvs.get( m_interIndex );
		OperateValue value = new OperateValue( new Integer(curHp),new Integer(preHp) );
		
		if( room.Type() == LoginProto.ROOM_TYPE.LOGIN_ROOMTYPE_SIMPLE_2V2 ||
			room.Type() == LoginProto.ROOM_TYPE.LOGIN_ROOMTYPE_SIMPLE_3V3 )
		{
			SimpleProto.Reactor actor = HandlerUtil.getReactor(iInfo, m_playerIndex, value);
			SimpleProto.ReactorInfo info = HandlerUtil.getReactorInfo(m_perInfo, actor);
			
			SimpleProto.S2G_ReactorInfo.Builder builder = SimpleProto.S2G_ReactorInfo.newBuilder();
			SimpleProto.S2G_ReactorInfo msg = builder.setRoomid( room.ID() )
					   								 .setActor( info )
					   								 .build();
			
			ByteString msgByte = HandlerUtil.CREATE_S2G_SIMPLEMESSAGE( SimpleProto.S2G_MSGTYPE.S2G_NOTIFY_ASK_PERFECT , msg.toByteString()).toByteString();
			room.session().sendMessage( HandlerUtil.CREATE_S2G_GLOBALMESSAGE( GlobalProto.MESSAGE.MESSAGE_SIMPLE_S2G, msgByte) );
		}
	}
	
	
	public INTERACTIVE_TYPE interType()
	{
		return INTERACTIVE_TYPE.DEAL_CURE;
	}
	
}
