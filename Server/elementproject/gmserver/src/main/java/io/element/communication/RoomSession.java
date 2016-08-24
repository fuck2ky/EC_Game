package io.element.communication;

import io.element.communication.Session.Reliable;
import io.element.room.impl.GM_BaseRoom;
import io.element.time.Counter;
import io.nadron.communication.DeliveryGuaranty;
import io.netty.channel.Channel;

public class RoomSession implements Reliable{

	protected Channel m_channel;
	
	protected Counter m_counter = new Counter();
	
	protected GM_BaseRoom m_owner;
	
	public RoomSession(Channel channel)
	{
		m_channel = channel;
		m_counter.Reset();
		m_counter.SetPeriod(10000);
	}
	
	public Object sendMessage(Object message) {
		m_channel.writeAndFlush( message );
		m_counter.Recount();
		return message;
	}
	
	public void setOwner(GM_BaseRoom room)
	{
		m_owner = room;
	}

	public DeliveryGuaranty getDeliveryGuaranty() {
		// TODO Auto-generated method stub
		return null;
	}

	public void close() {
		// TODO Auto-generated method stub
		m_channel.close();
	}
	
	public boolean isActive()
	{
		return m_channel == null ? false : m_channel.isActive();
	}
	
	public Channel getChannel()
	{
		return m_channel;
	}
	
	public void OnTick(long deltaTime)
	{	
		if(m_owner == null)
			return;
		
		if(!this.isActive())
		{
			m_owner.Active(false);
			return;
		}
		
		// i think the idle state handler could handler the room connection
		m_counter.IncCounter(deltaTime);
		if(m_counter.IsFull())
		{
			
		}
		
	}
	
	
}
