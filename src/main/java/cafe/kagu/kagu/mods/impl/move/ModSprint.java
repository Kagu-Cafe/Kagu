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
import cafe.kagu.kagu.settings.impl.ModeSetting;
import cafe.kagu.kagu.utils.ChatUtils;
import cafe.kagu.kagu.utils.MovementUtils;

/**
 * @author lavaflowglow
 *
 */
public class ModSprint extends Module {

	public ModSprint() {
		super("Sprint", Category.MOVEMENT);
		setSettings(mode);
	}

	public ModeSetting mode = new ModeSetting("Mode", "On Move", "On Move", "Vanilla", "Omni");
	
	@EventHandler
	private Handler<EventTick> onTick = e -> {
		if (e.isPost())
			return;
		setInfo(mode.getMode());
	};
	
	@EventHandler
	private Handler<EventPlayerUpdate> onUpdate = e -> {
		if (e.isPost())
			return;
		
		if (MovementUtils.isPlayerMoving() && mode.is("On Move")) {
			mc.thePlayer.setSprinting(true);
			return;
		}
		
		if (mc.thePlayer.onGround && mc.thePlayer.getFoodStats().getFoodLevel() >= 3
				&& !mc.thePlayer.isCollidedHorizontally && !mc.thePlayer.isUsingItem()
				&& (mode.is("Vanilla") ? mc.thePlayer.moveForward > 0
						: MovementUtils.isPlayerMoving())) {
			mc.thePlayer.setSprinting(true);
		}
	};

}
