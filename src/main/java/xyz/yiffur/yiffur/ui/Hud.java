/**
 * 
 */
package xyz.yiffur.yiffur.ui;

import xyz.yiffur.yiffur.Yiffur;
import xyz.yiffur.yiffur.eventBus.Subscriber;
import xyz.yiffur.yiffur.eventBus.YiffEvents;
import xyz.yiffur.yiffur.eventBus.impl.Event2DRender;
import xyz.yiffur.yiffur.font.FontRenderer;
import xyz.yiffur.yiffur.font.FontUtils;

/**
 * @author lavaflowglow
 *
 */
public class Hud {
	
	@YiffEvents
	public Subscriber<Event2DRender> renderHud = e -> {
		
		// We only want to render on the post event
		if (e.isPre()) {
			return;
		}
		
		// The fonts used
		FontRenderer mainFr = FontUtils.ROBOTO_LIGHT_10;
		
		// Render hud
		mainFr.drawString(Yiffur.getName() + " v" + Yiffur.getVersion(), 4, 4, 0x80000000);
		mainFr.drawString(Yiffur.getName() + " v" + Yiffur.getVersion(), 3, 3, -1);
		
	};
	
}
