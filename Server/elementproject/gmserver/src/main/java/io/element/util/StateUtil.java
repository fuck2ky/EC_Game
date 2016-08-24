package io.element.util;

import io.element.app.App;
import io.element.state.impl.BaseGameState;

import java.util.Stack;

public class StateUtil {

	public static void PrintStateStack(Stack<BaseGameState> m_sStack)
	{
		App.LOGGER.info("there left few state unhandler!!! please check it!!!");
		for (BaseGameState baseGameState : m_sStack) {
			App.LOGGER.info( "The Handler name" + baseGameState.getClass().toString() );
		}
	}
	
}
