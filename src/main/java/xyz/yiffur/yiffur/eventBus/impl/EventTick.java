/**
 * 
 */
package xyz.yiffur.yiffur.eventBus.impl;

import xyz.yiffur.yiffur.eventBus.Event;

/**
 * @author lavaflowglow
 *
 */
public class EventTick extends Event {

	/**
	 * @param eventPosition The position of the event
	 */
	public EventTick(EventPosition eventPosition) {
		super(eventPosition);
	}

}
