package io.element.state.interactive.impl;

import java.util.Vector;

import com.google.protobuf.ByteString;

import io.element.player.BasePlayer;
import io.element.protobuf.GlobalProto;
import io.element.protobuf.LoginProto;
import io.element.protobuf.SimpleProto;
import io.element.room.Room;
import io.element.room.impl.GM_BaseRoom;
import io.element.state.State;
import io.element.util.HandlerUtil;

public class InterDrawCardState extends InteractiveState{
	
	protected int m_playerIndex = 0;
	
	public InterDrawCardState(PerformInfo info, int index, int playerIndex) {
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
		
		// get player and deal draw
		long pid = iInfo.pids.get(m_playerIndex).pid;
		BasePlayer player = (BasePlayer) room.getEntityManager().getPlayerById( pid );
		room.getCardPool().drawHandCards( iInfo.einfo.inavValue , player);
		
		// send msg for new draw card
		this.sendDrawCardMsg(room);
		
		// update state machine
		room.getStateMachine().ChangeState();
		return true;
	}
	
	protected void sendDrawCardMsg(GM_BaseRoom room)
	{
		InteractiveInfo iInfo = m_perInfo.recvs.get( m_interIndex );
		
		// get player
		long pid = iInfo.pids.get(m_playerIndex).pid;
		BasePlayer player = (BasePlayer) room.getEntityManager().getPlayerById( pid );
		
		// get new draw cards
		Vector<Integer> news = player.getNewDrawCardIds();
		Vector<State.OperateValue> values= new Vector<State.OperateValue>();
		
		for(int i=0; i<news.size(); ++i)
		{
			OperateValue value = new OperateValue(news.get(i));
			values.add(value);
		}
		
		if( room.Type() == LoginProto.ROOM_TYPE.LOGIN_ROOMTYPE_SIMPLE_2V2 ||
			room.Type() == LoginProto.ROOM_TYPE.LOGIN_ROOMTYPE_SIMPLE_3V3 )
		{
			SimpleProto.Reactor actor = HandlerUtil.getReactor(iInfo, m_playerIndex, values);
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
		return INTERACTIVE_TYPE.DEAL_DRAWCARD;
	}
}








