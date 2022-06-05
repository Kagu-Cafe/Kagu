/**
 * 
 */
package cafe.kagu.kagu.mods.impl.visual;

import cafe.kagu.kagu.eventBus.EventHandler;
import cafe.kagu.kagu.eventBus.Handler;
import cafe.kagu.kagu.eventBus.impl.EventKeyUpdate;
import cafe.kagu.kagu.mods.Module;

/**
 * @author lavaflowglow
 *
 */
public class ModThirdPersonPlus extends Module {
	
	public ModThirdPersonPlus() {
		super("ThirdPersonPlus", Category.VISUAL);
	}
	
	@EventHandler
	private Handler<EventKeyUpdate> onKeyUpdate = e -> {
		
	};
	
}
