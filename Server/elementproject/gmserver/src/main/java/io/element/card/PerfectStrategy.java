package io.element.card;

import io.element.room.impl.GM_BaseRoom;
import io.element.state.State.InteractiveInfo;
import io.element.state.State.PerformInfo;
import io.element.state.impl.AskingResponseState;
import io.element.state.impl.AskingResponseState.ASK_RESPONSE;

// 冰心诀的询问策略
public class PerfectStrategy implements Strategy{

	public int doOperation(PerformInfo info, GM_BaseRoom room, int index) 
	{	
		if( index >= info.recvs.size() || info.pid == 0 || room == null )
			return 1;
		
		InteractiveInfo interInfo = info.recvs.get(index);
		if( interInfo.performed() )
			return 2;
		
		interInfo.setPerformed(true);
		room.getStateMachine().PushCurrentState( new AskingResponseState(ASK_RESPONSE.ASK_ICE_HEART) );
		return 0;
	}
	
}
