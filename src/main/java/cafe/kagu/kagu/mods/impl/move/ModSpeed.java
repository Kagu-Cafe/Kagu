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
import cafe.kagu.kagu.utils.ChatUtils;
import cafe.kagu.kagu.utils.MovementUtils;
import cafe.kagu.kagu.utils.RotationUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.MathHelper;

/**
 * @author lavaflowglow
 *
 */
public class ModSpeed extends Module {
	
	public ModSpeed() {
		super("Speed", Category.MOVEMENT);
		setSettings(mode, speed);
	}
	
	private ModeSetting mode = new ModeSetting("Mode", "Vanilla", "Vanilla", "Strafe", "Strafe On Ground", "Hypixel", "Test");
	private DoubleSetting speed = new DoubleSetting("Speed", 1, 0.1, 10, 0.1).setDependency(() -> mode.is("Vanilla"));
	
	private double speedDouble = 0;
	private float hypixelYaw = 0;
	private int onGroundTicks = 0;
	
	@Override
	public void onEnable() {
		hypixelYaw = mc.thePlayer.rotationYaw;
		onGroundTicks = 0;
	}
	
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
			case "Hypixel":{
				if (!MovementUtils.isPlayerMoving()) {
					MovementUtils.setMotion(0);
					return;
				}
				if (MovementUtils.isTrueOnGround()) {
					thePlayer.jump();
				}
				
				// Smooth Strafe
				float targetYaw = RotationUtils.getStrafeYaw();
				float currentYaw = hypixelYaw;
				if (Math.abs(targetYaw - currentYaw) > Math.abs(targetYaw + 360 - currentYaw)) {
					targetYaw += 360;
				}
				if (Math.abs(targetYaw - currentYaw) > Math.abs(targetYaw - 360 - currentYaw)) {
					targetYaw -= 360;
				}
				float maxRotationSpeed = 30;
				MovementUtils.setMotion(MovementUtils.getMotion(), hypixelYaw += MathHelper.clamp_float((targetYaw - currentYaw) * 0.6f, -maxRotationSpeed, maxRotationSpeed));
			}break;
			case "Test":{
				if (!MovementUtils.isPlayerMoving()) {
					MovementUtils.setMotion(0);
					return;
				}
				
				if (thePlayer.ticksExisted % 7 == 0) {
					MovementUtils.setMotion(0.31);
				}
				if (MovementUtils.isTrueOnGround()) {
//					speedDouble = 0.35;
					thePlayer.jump();
//					MovementUtils.setMotion(0.34);
//					speedDouble = MovementUtils.getMotion();
				}
			}break;
		}
		
	};
	
}
