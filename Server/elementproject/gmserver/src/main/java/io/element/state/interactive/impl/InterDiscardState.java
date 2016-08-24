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

// 弃牌的代码  牌不足则全弃掉 没做 2-24
public class InterDiscardState extends InteractiveState{

	protected int m_playerIndex;
	
	public InterDiscardState(io.element.state.State.PerformInfo info, int index, int playerIndex) {
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
		
		// get player and deal discard
		long pid = iInfo.pids.get(m_playerIndex).pid;
		BasePlayer recver = (BasePlayer) room.getEntityManager().getPlayerById( pid );
		BasePlayer hoster = (BasePlayer) room.getEntityManager().getPlayerById( m_perInfo.pid );
		
		Vector<Integer> values = new Vector<Integer>();
		for (Object obj :  iInfo.pids.get(m_playerIndex).values) {
			if(obj instanceof Integer)
			{	
				Integer valueInteger = (Integer) obj;
				values.add( valueInteger );
			}
		}
		values = hoster.discardCards( values , recver );
		
		// send msg for new draw card
		this.sendDiscardMsg(room,values);
		
		// update state machine
		room.getStateMachine().ChangeState();
		return true;
	}
	
	public boolean sendDiscardMsg(GM_BaseRoom room, Vector<Integer> cards )
	{
		
		InteractiveInfo iInfo = m_perInfo.recvs.get( m_interIndex );
		
		// get player	
		Vector<State.OperateValue> values= new Vector<State.OperateValue>();

		// get discard cards
		for(int i=0; i<values.size(); ++i)
		{
			OperateValue value = new OperateValue(cards.get(i));
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
		
		return true;
	}
	
	public INTERACTIVE_TYPE interType()
	{
		return INTERACTIVE_TYPE.DEAL_DISCARD;
	}
	
}
