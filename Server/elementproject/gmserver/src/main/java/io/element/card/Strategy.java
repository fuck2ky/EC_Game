package io.element.card;

import io.element.room.impl.GM_BaseRoom;
import io.element.state.State.PerformInfo;

public interface Strategy {

	public int doOperation(PerformInfo info, GM_BaseRoom room , int index);
		
}
