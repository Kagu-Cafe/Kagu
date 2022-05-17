/**
 * 
 */
package xyz.yiffur.yiffur.mods.impl.move;

import xyz.yiffur.yiffur.eventBus.Subscriber;
import xyz.yiffur.yiffur.eventBus.YiffEvents;
import xyz.yiffur.yiffur.eventBus.impl.EventPlayerUpdate;
import xyz.yiffur.yiffur.eventBus.impl.EventTick;
import xyz.yiffur.yiffur.mods.Module;
import xyz.yiffur.yiffur.settings.impl.BooleanSetting;
import xyz.yiffur.yiffur.utils.ChatUtils;

/**
 * @author Waterbongo
 *
 */
public class ModSprint extends Module {

	public ModSprint() {
		super("Sprint", Category.MOVEMENT);
		setSettings(omni);
	}

	public BooleanSetting omni = new BooleanSetting("Omni", false);
	
	@YiffEvents
	public Subscriber<EventPlayerUpdate> onUpdate = e -> {
		if (e.isPost())
			return;
		
		if (mc.thePlayer.onGround && mc.thePlayer.getFoodStats().getFoodLevel() >= 3
				&& !mc.thePlayer.isCollidedHorizontally && !mc.thePlayer.isEating() && !mc.thePlayer.isSprinting()
				&& (omni.isDisabled() ? mc.thePlayer.moveForward > 0
						: (mc.thePlayer.moveForward != 0 || mc.thePlayer.moveStrafing != 0))) {
			mc.thePlayer.setSprinting(true);
		}
		setInfo(omni.isEnabled() ? "Omni" : "Normal");
	};

}
