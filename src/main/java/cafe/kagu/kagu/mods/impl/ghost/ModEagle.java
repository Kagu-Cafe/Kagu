/**
 * 
 */
package cafe.kagu.kagu.mods.impl.ghost;

import cafe.kagu.kagu.Kagu;
import cafe.kagu.kagu.eventBus.EventHandler;
import cafe.kagu.kagu.eventBus.Handler;
import cafe.kagu.kagu.eventBus.impl.EventPlayerUpdate;
import cafe.kagu.kagu.eventBus.impl.EventTick;
import cafe.kagu.kagu.mods.Module;
import cafe.kagu.kagu.mods.impl.move.ModNoSlow;
import cafe.kagu.kagu.settings.impl.DoubleSetting;
import cafe.kagu.kagu.settings.impl.IntegerSetting;
import cafe.kagu.kagu.utils.ChatUtils;
import cafe.kagu.kagu.utils.MovementUtils;
import cafe.kagu.kagu.utils.SpoofUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author DistastefulBannock
 *
 */
public class ModEagle extends Module {
	
	public ModEagle() {
		super("Eagle", Category.GHOST);
		setSettings(releaseDelay, predictionFactor);
	}
	
	private IntegerSetting releaseDelay = new IntegerSetting("Release delay", 0, 0, 500, 1);
	private DoubleSetting predictionFactor = new DoubleSetting("Pred factor", 2, 1, 3, 0.05);
	
	private boolean shouldSneak = false;
	private long releaseAt = 0;
	
	@Override
	public void onEnable() {
		shouldSneak = false;
		releaseAt = 0;
	}
	
	@Override
	public void onDisable() {
		shouldSneak = false;
		releaseAt = 0;
	}

	@EventHandler
	private Handler<EventPlayerUpdate> onUpdate = e -> {
		if (e.isPre())
			return;
		if (mc.thePlayer.noClip || !MovementUtils.isTrueOnGround()){
			setShouldSneak(false);
			return;
		}

		double predFactor = predictionFactor.getValue();
		boolean xMotionOffBlock = mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer,
				mc.thePlayer.getEntityBoundingBox().offset(mc.thePlayer.motionX * predFactor, -0.6, 0)).isEmpty();
		boolean zMotionOffBlock = mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer,
				mc.thePlayer.getEntityBoundingBox().offset(0, -0.6, mc.thePlayer.motionZ * predFactor)).isEmpty();
		boolean xzMotionOffBlock = mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer,
				mc.thePlayer.getEntityBoundingBox().offset(mc.thePlayer.motionX * predFactor, -0.6, mc.thePlayer.motionZ * predFactor)).isEmpty();
		boolean noMotionOffBlock = mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer,
				mc.thePlayer.getEntityBoundingBox().expand(-0.1d, 0, -0.1d).offset(0.05, -0.6, 0.05)).isEmpty();
		setShouldSneak(xMotionOffBlock || zMotionOffBlock || xzMotionOffBlock || noMotionOffBlock);
	};
	
	/**
	 * @param shouldSneak the shouldSneak to set
	 */
	public void setShouldSneak(boolean shouldSneak) {
		this.shouldSneak = shouldSneak;
		if (shouldSneak)
			releaseAt = System.currentTimeMillis() + releaseDelay.getValue();
	}
	
	/**
	 * @return the shouldSneak
	 */
	public boolean isShouldSneak() {
		return shouldSneak || System.currentTimeMillis() < releaseAt;
	}
	
}
