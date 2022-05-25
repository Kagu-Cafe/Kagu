/**
 * 
 */
package cafe.kagu.kagu.mods.impl.move;

import cafe.kagu.kagu.eventBus.Handler;
import cafe.kagu.kagu.eventBus.EventHandler;
import cafe.kagu.kagu.eventBus.impl.EventPlayerUpdate;
import cafe.kagu.kagu.eventBus.impl.EventTick;
import cafe.kagu.kagu.mods.Module;
import cafe.kagu.kagu.settings.impl.BooleanSetting;
import cafe.kagu.kagu.utils.ChatUtils;

/**
 * @author lavaflowglow
 *
 */
public class ModSprint extends Module {

	public ModSprint() {
		super("Sprint", Category.MOVEMENT);
		setSettings(omni);
	}

	public BooleanSetting omni = new BooleanSetting("Omni", false);
	
	@EventHandler
	public Handler<EventPlayerUpdate> onUpdate = e -> {
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
