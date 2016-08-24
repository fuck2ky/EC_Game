package io.element.room.impl;

import java.util.List;

import io.element.communication.ServerSteering;
import io.element.communication.Session.BroadCast;
import io.element.communication.impl.SimpleSteering;
import io.element.gtserver.Const;
import io.element.player.BasePlayer;
import io.element.protobuf.LoginProto;
import io.element.room.BaseRoom;
import io.element.time.Counter;

public class SessionRoom extends BaseRoom implements BroadCast<BasePlayer>{

	public enum ROOM_STATUS
	{
		DEACTIVE,	// 等待激活标记
		ACTIVE,		// 已经激活标记
		DELETE,		// 允许删除标记
	}
	
	protected ServerSteering		m_steering;
	
	protected ROOM_STATUS 			m_active = ROOM_STATUS.DEACTIVE;		
		
	protected Counter				m_activeCounter;
		
	public SessionRoom(long id, String name, LoginProto.ROOM_TYPE type) {
		super(id, name, type);

		if(type == LoginProto.ROOM_TYPE.LOGIN_ROOMTYPE_SIMPLE_2V2 ||  type == LoginProto.ROOM_TYPE.LOGIN_ROOMTYPE_SIMPLE_3V3 )
			m_steering = new SimpleSteering(this);
		
		m_activeCounter = new Counter();
		m_activeCounter.SetPeriod(Const.SESSION_ROOM_DEACTIVE_GAP);
		m_activeCounter.Recount();
	}

	// 广播给当前房间的所有玩家
	public void broadcast() {
		if(ssCheckVaild())
			m_steering.broadcast();
	}

	public void broadcast(int location) {
		// TODO Auto-generated method stub
		
	}
	
	public void broadcast( BasePlayer player )
	{
		
	}

	public void broadcast(List<BasePlayer> lists) {
		// TODO Auto-generated method stub
		
	}
	
	public boolean ssCheckVaild(){
		if( m_steering != null && m_steering.checkVaild() )
			return true;
		return false;
	}
	
	public ServerSteering steering()
	{
		return m_steering;
	}
	
	public void setStatus( ROOM_STATUS flag )
	{
		if( flag == ROOM_STATUS.ACTIVE && ssCheckVaild())
		{
			m_active = ROOM_STATUS.ACTIVE;
			return;
		}
		
		m_active = ROOM_STATUS.DEACTIVE;
	}
	
	public ROOM_STATUS getStatus()
	{
		return m_active;
	}
	
	public void CloseLogicConnect()
	{
		if(m_steering != null)
		{
			m_steering.close();
		}
	}
		
	@Override
	public void OnTick(long deltaTime)
	{	
		// 房间正常链接了 = 〉 关闭超时定时器
		if( m_active == ROOM_STATUS.ACTIVE && m_activeCounter != null )
			m_activeCounter = null;
		
		// 房间还没有正常链接 = 〉 定时器超时处理 房间关闭
		if( m_activeCounter != null )
		{
			m_activeCounter.IncCounter(deltaTime);
			if( m_activeCounter.IsFull() )
			{	
				m_active = ROOM_STATUS.DELETE;
				return;
			}
		}
		
		// 链路异常 = 〉 房间关闭
		if( !ssCheckVaild() )
		{
			m_active = ROOM_STATUS.DELETE;
			return;
		}
		
		// 所有玩家都断开链接 = 〉 房间关闭
		boolean playersBrokenAll = true;
		for (BasePlayer p : m_players) {
			if( p.isActive() )
				playersBrokenAll = false;
		}
		
		if( playersBrokenAll )
		{
			m_active = ROOM_STATUS.DELETE;
			return;
		}
		
		// 正常的房间逻辑
		DoLogic(deltaTime);
	}
	
	public void DoLogic(long deltaTime)
	{
		
	}
	

}
