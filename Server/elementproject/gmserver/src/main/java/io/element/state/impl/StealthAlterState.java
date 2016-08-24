package io.element.state.impl;

import com.google.protobuf.ByteString;

import io.element.app.App;
import io.element.protobuf.GlobalProto;
import io.element.protobuf.SimpleProto;
import io.element.protobuf.SimpleProto.ReactorInfo;
import io.element.room.Room;
import io.element.room.impl.GM_BaseRoom;
import io.element.room.impl.GM_SimpleRoom;
import io.element.state.card.impl.CardState;
import io.element.state.card.impl.CardStateFactory;
import io.element.state.impl.AskingResponseState.AlterInfo;
import io.element.task.impl.Task_StateRun;
import io.element.util.HandlerUtil;

public class StealthAlterState extends BaseGameState{

	protected AlterInfo m_flag;
	
	protected int m_interIndex;
	
	public StealthAlterState(AlterInfo info, int index)
	{
		m_flag = info;
		m_interIndex = index;
	}
	
	@Override
	public boolean Enter(Room entity) {

		GM_BaseRoom room = (GM_SimpleRoom) entity;
		
		// 都检查了一遍
		if( IsBegan() )
			return false;
		
		// 一次广播一个人 去play stealth card
		if( !this.sendStealthAskMsg(room) )
			return false;
				
		// set delayed task to run next ask
		room.getMessageDispatcher().DispatchDelayedTask( new Task_StateRun(room.ID()) , room.config().getResponseGapTime());
		m_bBegin = true;
		return m_bBegin;	
	}
	
	@Override
	public boolean Excute(Room entity) {

		GM_BaseRoom room = (GM_BaseRoom) entity;
		
		if(m_flag.use())
		{
			// 有人打得是隐蛊 数据其实应该检查？？ maybe
			// 反正打隐蛊 进入（隐蛊hand card state----检查perfect)
			// 隐蛊数据保存在 card info 的 cid中
			// perfect的数据保存在 card info的perflag中 表示此牌是否被无邪了
			CardState state = CardStateFactory.createCardState( m_flag.info().cid ,  m_flag.info().pid, m_flag);
			room.getStateMachine().ChangeState(state);
		}
		else  // 否则直接处理
			room.getStateMachine().ChangeState();
		
		return true;
	}
	
	public boolean IsBegan()
	{
		return m_flag.check();
	}
	
	public boolean sendStealthAskMsg(Room entity)
	{
		GM_BaseRoom room = (GM_BaseRoom) entity;
		
		PerformInfo pInfo = room.getStateMachine().GetLastHandCardState().getPerformInfo();
		if(pInfo == null)
			return false;
		
		try {
			SimpleProto.S2G_StealthPlayer.Builder builder = SimpleProto.S2G_StealthPlayer.newBuilder();
			ReactorInfo sendRInfo = HandlerUtil.getReactorInfo( pInfo,m_interIndex );
			SimpleProto.S2G_StealthPlayer msg = builder.setRoomid( room.ID() )
					   								   .setActor( sendRInfo )
					   								   .build();
			
			ByteString msgByte = HandlerUtil.CREATE_S2G_SIMPLEMESSAGE( SimpleProto.S2G_MSGTYPE.S2G_NOTIFY_ASK_STEALTH , msg.toByteString()).toByteString();
			room.session().sendMessage( HandlerUtil.CREATE_S2G_GLOBALMESSAGE( GlobalProto.MESSAGE.MESSAGE_SIMPLE_S2G, msgByte) );
		}catch (Exception e) {
			App.LOGGER.warn(e.toString());
			return false;
		}

		return m_flag.check(true);
	}
	
}
