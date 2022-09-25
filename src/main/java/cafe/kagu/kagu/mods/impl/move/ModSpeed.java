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
import cafe.kagu.kagu.utils.WorldUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
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
	
	private ModeSetting mode = new ModeSetting("Mode", "Vanilla", "Vanilla", "Strafe", "Strafe On Ground", "Hypixel", "Vulcan Hover", "Test");
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
					onGroundTicks++;
				}
				
				if (onGroundTicks >= 2) {
					thePlayer.jump();
					onGroundTicks = 0;
//					hypixelYaw = RotationUtils.getStrafeYaw();
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
			case "Vulcan Hover":{
				if (!MovementUtils.isPlayerMoving()) {
					MovementUtils.setMotion(0);
					return;
				}
				
				if (MovementUtils.isTrueOnGround() && thePlayer.motionY == -0.0784000015258789) {
					thePlayer.jump();
					onGroundTicks = 0;
				}
				onGroundTicks++;
				
				if (MovementUtils.isTrueOnGround(0.65) && !MovementUtils.isTrueOnGround() && thePlayer.motionY < 0.26) {
//					if (onGroundTicks % 2 == 0) {
						thePlayer.motionY = 0.2984000015258789f - (Math.random() / 500);
//					}
				}
				
			}break;
			case "Test":{
				if (MovementUtils.isTrueOnGround()) {
					MovementUtils.setMotion(1);
					thePlayer.jump();
				}else {
//					MovementUtils.setMotion(1);
				}
			}break;
		}
		
	};
	
}
