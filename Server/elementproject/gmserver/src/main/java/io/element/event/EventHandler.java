package io.element.event;

import io.element.event.impl.BaseEvent;

public abstract class EventHandler {

	public abstract <T> boolean applyHandler(BaseEvent<T> event);
	
	
}
