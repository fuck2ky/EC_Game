package io.element.card.impl;

import io.element.card.Equipment;
import io.element.player.BasePlayer;
import io.element.state.State.PerformInfo;

public class WeaponCard extends HandCard implements Equipment{

	public WeaponCard(int id) {
		super(id);
		// TODO Auto-generated constructor stub
	}

	public boolean isValidConsume() {
		// TODO Auto-generated method stub
		return false;
	}

	public void consume(BasePlayer player) {
		// TODO Auto-generated method stub
		
	}

	public void trigger(PerformInfo info) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HANDCARD getInnerType() {
		// TODO Auto-generated method stub
		return null;
	}

	public int atkPlus() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int ratingPlus() {
		// TODO Auto-generated method stub
		return 0;
	}

}
