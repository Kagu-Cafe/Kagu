/**
 * 
 */
package cafe.kagu.kagu.mods.impl.move;

import cafe.kagu.kagu.eventBus.EventHandler;
import cafe.kagu.kagu.eventBus.Handler;
import cafe.kagu.kagu.eventBus.impl.EventPlayerUpdate;
import cafe.kagu.kagu.eventBus.impl.EventTick;
import cafe.kagu.kagu.mods.Module;
import cafe.kagu.kagu.settings.impl.DoubleSetting;
import cafe.kagu.kagu.settings.impl.ModeSetting;
import cafe.kagu.kagu.utils.MovementUtils;
import cafe.kagu.kagu.utils.RotationUtils;
import net.minecraft.client.entity.EntityPlayerSP;

/**
 * @author lavaflowglow
 *
 */
public class ModSpeed extends Module {
	
	public ModSpeed() {
		super("Speed", Category.MOVEMENT);
		setSettings(mode, speed);
	}
	
	private ModeSetting mode = new ModeSetting("Mode", "Vanilla", "Vanilla", "Strafe", "Strafe On Ground");
	private DoubleSetting speed = new DoubleSetting("Speed", 1, 0.1, 10, 0.1).setDependency(() -> mode.is("Vanilla"));
	
	@EventHandler
	private Handler<EventTick> onTick = e -> {
		if (e.isPost())
			return;
		setInfo(mode.getMode());
	};
	
	@EventHandler
	private Handler<EventPlayerUpdate> onPlayerUpdate = e -> {
		if (e.isPost())
			return;
		
		EntityPlayerSP thePlayer = mc.thePlayer;
		
		switch (mode.getMode()) {
			case "Vanilla":{
				if (!MovementUtils.isPlayerMoving()) {
					MovementUtils.setMotion(0);
					return;
				}
				if (MovementUtils.isTrueOnGround())
					thePlayer.jump();
				MovementUtils.setMotion(speed.getValue());
			}break;
			case "Strafe":{
				if (!MovementUtils.isPlayerMoving()) {
					MovementUtils.setMotion(0);
					return;
				}
				if (MovementUtils.isTrueOnGround() && !mc.gameSettings.keyBindJump.isKeyDown())
					thePlayer.jump();
				MovementUtils.setMotion(MovementUtils.getMotion());
			}break;
			case "Strafe On Ground":{
				if (!MovementUtils.isPlayerMoving())
					MovementUtils.setMotion(0);
				else if (MovementUtils.isTrueOnGround()) {
					thePlayer.jump();
					MovementUtils.setMotion(MovementUtils.getMotion());
				}
			}break;
		}
		
	};
	
}
