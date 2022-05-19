/**
 * 
 */
package cafe.kagu.kagu.mods.impl.player;

import cafe.kagu.kagu.eventBus.Subscriber;
import cafe.kagu.kagu.eventBus.EventHandler;
import cafe.kagu.kagu.eventBus.impl.EventPlayerUpdate;
import cafe.kagu.kagu.eventBus.impl.EventTick;
import cafe.kagu.kagu.mods.Module;
import cafe.kagu.kagu.settings.impl.ModeSetting;
import cafe.kagu.kagu.utils.ChatUtils;

/**
 * @author Waterbongo
 *
 */
public class ModNoFall extends Module {

	public ModNoFall() {
		super("Nofall", Category.PLAYER);
		setSettings(mode);
	}

	public ModeSetting mode = new ModeSetting("Mode", "Spoof", "Spoof", "V2");
	
	@EventHandler
	public Subscriber<EventTick> onTick = e -> {
		setInfo(mode.getMode());
	};
	
	@EventHandler
	public Subscriber<EventPlayerUpdate> onUpdate = e -> {
		if (e.isPost())
			return;
		if (mode.is("Spoof")) {
			if (mc.thePlayer.fallDistance > 3) {
				e.setOnGround(true);
				mc.thePlayer.fallDistance = 0;
			}
		}
		else if (mode.is("V2")) {
			ChatUtils.addChatMessage("v2 On!");
		}
	};
	

}
