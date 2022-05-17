/**
 * 
 */
package xyz.yiffur.yiffur.mods.impl.player;

import xyz.yiffur.yiffur.eventBus.Subscriber;
import xyz.yiffur.yiffur.eventBus.YiffEvents;
import xyz.yiffur.yiffur.eventBus.impl.EventPlayerUpdate;
import xyz.yiffur.yiffur.eventBus.impl.EventTick;
import xyz.yiffur.yiffur.mods.Module;
import xyz.yiffur.yiffur.settings.impl.ModeSetting;
import xyz.yiffur.yiffur.utils.ChatUtils;

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
	
	@YiffEvents
	public Subscriber<EventTick> onTick = e -> {
		setInfo(mode.getMode());
	};
	
	@YiffEvents
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
