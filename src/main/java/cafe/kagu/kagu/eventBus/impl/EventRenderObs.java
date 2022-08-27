/**
 * 
 */
package cafe.kagu.kagu.eventBus.impl;

import java.awt.Graphics2D;

import cafe.kagu.kagu.eventBus.Event;

/**
 * @author DistastefulBannock
 *
 */
public class EventRenderObs extends Event {
	
	private Graphics2D graphics;
	
	/**
	 * @param eventPosition The position of the event
	 * @param graphics The graphics object
	 */
	public EventRenderObs(EventPosition eventPosition, Graphics2D graphics) {
		super(eventPosition);
		this.graphics = graphics;
	}
	
	/**
	 * @return the graphics
	 */
	public Graphics2D getGraphics() {
		return graphics;
	}
	
}
