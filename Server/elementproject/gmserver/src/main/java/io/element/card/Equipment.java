package io.element.card;

import io.element.player.BasePlayer;
import io.element.state.State.PerformInfo;

public interface Equipment {
	
	// check consume valid
	public boolean isValidConsume();
	
	// 装备的爆发方法
	public void consume(BasePlayer player);
	
	// 装备的被动触发
	public void trigger(PerformInfo info);
	
	// 得到攻击增益
	public int atkPlus();
	
	// 得到命中增益
	public int ratingPlus();
		
}
