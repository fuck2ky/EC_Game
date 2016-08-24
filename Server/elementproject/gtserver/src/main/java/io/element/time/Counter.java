package io.element.time;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Counter {

	protected long m_lCounter;

	protected long m_lPeriod;
	
	protected long m_lBeginTime;
	
	protected TimeUnit m_tUnit;
	
	protected ReentrantLock m_lock = new ReentrantLock();
	
	public Counter()
	{
		m_tUnit = TimeUnit.MILLISECONDS;
		
		Reset();
	}
	
	public long GetCurrentTime()
	{
		Calendar calendar = Calendar.getInstance();
		return calendar.getTimeInMillis();
	}
	
	public void SetPeriod(long time)
	{
		m_lPeriod = time;
	}
	
	public void Reset()
	{
		m_lock.lock();
		
		m_lBeginTime = GetCurrentTime();
		m_lCounter = 0;
		m_lPeriod  = Long.MAX_VALUE;
		
		m_lock.unlock();
	}
	
	public void Recount()
	{
		m_lock.lock();
		
		m_lBeginTime = GetCurrentTime();
		m_lCounter = 0;
		
		m_lock.unlock();
	}
	
	public boolean IsFull()
	{
		return m_lCounter >= m_lPeriod ? true : false;
	}
	
	public boolean IncCounter(long delta)
	{
		m_lock.lock();
		m_lCounter += delta;
		m_lock.unlock();
		
		return IsFull();
	}
	
	public void setTimeUnit(TimeUnit t)
	{
		m_tUnit = t;
	}
	
	public long lastTime(){ return Math.max(m_lPeriod-m_lCounter, 0); }

}
