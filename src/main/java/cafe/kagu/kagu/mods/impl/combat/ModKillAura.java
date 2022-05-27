/**
 * 
 */
package cafe.kagu.kagu.mods.impl.combat;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

import javax.vecmath.Vector3d;

import org.apache.commons.lang3.RandomUtils;

import cafe.kagu.kagu.eventBus.EventBus;
import cafe.kagu.kagu.eventBus.EventHandler;
import cafe.kagu.kagu.eventBus.Handler;
import cafe.kagu.kagu.eventBus.impl.EventPlayerUpdate;
import cafe.kagu.kagu.eventBus.impl.EventSettingUpdate;
import cafe.kagu.kagu.mods.Module;
import cafe.kagu.kagu.settings.SettingDependency;
import cafe.kagu.kagu.settings.impl.BooleanSetting;
import cafe.kagu.kagu.settings.impl.DoubleSetting;
import cafe.kagu.kagu.settings.impl.IntegerSetting;
import cafe.kagu.kagu.settings.impl.ModeSetting;
import cafe.kagu.kagu.utils.SpoofUtils;
import cafe.kagu.kagu.utils.TimerUtil;
import cafe.kagu.kagu.utils.ChatUtils;
import cafe.kagu.kagu.utils.DrawUtils3D;
import cafe.kagu.kagu.utils.PlayerUtils;
import cafe.kagu.kagu.utils.RotationUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;

/**
 * @author lavaflowglow
 *
 */
public class ModKillAura extends Module {

	public ModKillAura() {
		super("KillAura", Category.COMBAT);
		setSettings(rotationMode, blockMode, preferredTargetMetrics, targetSelectionMode, swingMode, positioningMode,
				hitRange, blockRange, hitChance, minAps, maxAps, targetAll, targetPlayers, targetAnimals, targetMobs);
		EventBus.setSubscriber(new ApsMinMaxFixer(this), true);
	}
	
	// Modes
	private ModeSetting rotationMode = new ModeSetting("Rotation Mode", "Lock", "Lock", "Lock+");
	private ModeSetting blockMode = new ModeSetting("Block Mode", "None", "None", "Vanilla 1");
	private ModeSetting preferredTargetMetrics = new ModeSetting("Preferred Target Metrics", "Distance", "Distance");
	private ModeSetting targetSelectionMode = new ModeSetting("Target Selection", "Instant", "Instant");
	private ModeSetting swingMode = new ModeSetting("Swing Mode", "Swing", "Swing", "Server Side", "No Swing");
	private ModeSetting positioningMode = new ModeSetting("Positioning", "Vanilla", "Vanilla", "Vanilla+", "Target Render Pos", "Player Render Pos", "Target & Player Render Pos");
	
	// Ranges
	private DoubleSetting hitRange = new DoubleSetting("Hit Range", 3, 1, 7, 0.1);
	private DoubleSetting blockRange = (DoubleSetting) new DoubleSetting("Block Range", 3, 1, 7, 0.1).setDependency((SettingDependency)() -> !blockMode.is("None"));
	
	// Hit chance
	private IntegerSetting hitChance = new IntegerSetting("Hit Chance", 100, 0, 100, 1);
	
	// APS settings
	private DoubleSetting minAps = new DoubleSetting("Min APS", 10, 0, 20, 0.1);
	private DoubleSetting maxAps = new DoubleSetting("Max APS", 10, 0.1, 20, 0.1);
	
	// Targets
	private BooleanSetting targetAll = new BooleanSetting("Target Everything", true);
	private BooleanSetting targetPlayers = (BooleanSetting) new BooleanSetting("Target Players", true).setDependency((SettingDependency)() -> targetAll.isDisabled());
	private BooleanSetting targetAnimals = (BooleanSetting) new BooleanSetting("Target Animals", false).setDependency((SettingDependency)() -> targetAll.isDisabled());
	private BooleanSetting targetMobs = (BooleanSetting) new BooleanSetting("Target Mobs", false).setDependency((SettingDependency)() -> targetAll.isDisabled());
	
	// Vars
	private double aps = minAps.getValue();
	private boolean blocking = false;
	private TimerUtil apsTimer = new TimerUtil();
	
	@EventHandler
	private Handler<EventPlayerUpdate> onPlayerUpdate = e -> {
		if (e.isPost())
			return;
		
		setInfo(new DecimalFormat("0.00").format(aps) + " APS", rotationMode.getMode());
		
		EntityLivingBase[] targets = getTargets();
		if (targets.length == 0) {
			stopBlocking();
			return;
		}
		
		EntityLivingBase target = targets[0];
		
		if (target == null) {
			stopBlocking();
			return;
		}
		
		double distanceFromPlayer = getDistanceFromPlayerEyes(target);
		
		// Check if the target is within the block distance
		if (!blockMode.is("None")) {
			
			// Block or unblock
			if (distanceFromPlayer <= blockRange.getValue())
				startBlocking();
			else
				stopBlocking();
			
			// Block animations
			if (blocking)
				SpoofUtils.setSpoofBlocking(true);
			
		}
		
		// Get and set the rotations
		float[] rotations = getRotations(target, e);
		e.setRotationYaw(rotations[0]);
		e.setRotationPitch(rotations[1]);
		SpoofUtils.setSpoofedYaw(rotations[0]);
		SpoofUtils.setSpoofedPitch(rotations[1]);
		
		// Check if we're able to hit
		boolean canHit = canHit(rotations);
		if (canHit && distanceFromPlayer <= hitRange.getValue() && apsTimer.hasTimeElapsed((long) (1000 / aps), true)) {
			
			// Swing
			switch (swingMode.getMode()) {
				case "Swing":{
					mc.thePlayer.swingItem();
				}break;
				case "Server Side":{
					mc.getNetHandler().getNetworkManager().sendPacket(new C0APacketAnimation());
				}break;
				case "No Swing":break;
			}
			
			// Hit chance, bypasses percent checks
			if (hitChance.getValue() != 0 && RandomUtils.nextInt(0, 101) <= hitChance.getValue())
				mc.getNetHandler().getNetworkManager().sendPacket(new C02PacketUseEntity(target, Action.ATTACK));
			
			setAps();
			
		}
		
	};
	
	/**
	 * Gets the rotations to look at the target
	 * @param target The target
	 * @param eventPlayerUpdate The event player update
	 * @return A float array containing yaw and pitch, may also contain separate data
	 */
	private float[] getRotations(EntityLivingBase target, EventPlayerUpdate eventPlayerUpdate) {
		
		Vector3d playerPos = shouldUseRenderPosPlayer() ? DrawUtils3D.get3dEntityOffsets(mc.thePlayer) : new Vector3d(mc.thePlayer.posX + 0.5, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ + 0.5);
		Vector3d targetPos = shouldUseRenderPosTarget() ? DrawUtils3D.get3dEntityOffsets(target) : new Vector3d(target.posX + 0.5, target.posY + target.getEyeHeight(),target.posZ + 0.5);
		
		switch (rotationMode.getMode()) {
			case "Lock":{
				return RotationUtils.getRotations(playerPos, targetPos);
			}
			case "Lock+":{
				return RotationUtils.getRotations(playerPos, PlayerUtils.getClosestPointInBoundingBox(playerPos, target.getEntityBoundingBox()));
			}
		}
		
		return new float[] {0, 0};
	}
	
	/**
	 * Checks if able to hit the target or not
	 * @param rotations The rotations for the entity
	 * @return true if able to hit the target, false otherwise
	 */
	private boolean canHit(float[] rotations) {
		return true;
	}
	
	private EntityLivingBase lastTarget = null;
	
	/**
	 * Gets an array of valid targets
	 * @return An array of valid targets
	 */
	private EntityLivingBase[] getTargets() {
		
		ArrayList<Entity> potentialTargets = new ArrayList<>(mc.theWorld.loadedEntityList);
		
		// Remove non living and out of range entities
		potentialTargets = (ArrayList<Entity>) potentialTargets
				.stream()
				.filter(ent -> 
						ent instanceof EntityLivingBase && 
						getDistanceFromPlayerEyes((EntityLivingBase)ent) <= Math.max(hitRange.getValue(), blockRange.getValue()) && 
						ent != mc.thePlayer && 
						(((EntityLivingBase)ent).getMaxHealth() <= 0 || ((EntityLivingBase)ent).getHealth() > 0))
				.collect(Collectors.toList());
		
		// Remove targets that don't fit the required target settings
		if (targetAll.isDisabled()) {
			potentialTargets = (ArrayList<Entity>) potentialTargets
					.stream()
					.filter(ent -> {
						if (targetPlayers.isEnabled() && ent instanceof EntityPlayer);
						else if (targetAnimals.isEnabled() && (ent instanceof EntityAnimal || ent instanceof EntityWaterMob));
						else if (targetMobs.isEnabled() && ent instanceof EntityMob);
						else return false;
						
						return true;
					}
					).collect(Collectors.toList());
		}
		
		// Sort them based on the config that the user has
		switch (preferredTargetMetrics.getMode()) {
			case "Distance":{
				potentialTargets.sort(Comparator.comparingDouble(ent -> getDistanceFromPlayerEyes((EntityLivingBase)ent)));
			}break;
		}
		
		// Now that the targets are filtered and sorted we can use the users preferred
		// selection mode to make a decision on who the target should be
		EntityLivingBase[] targets = new EntityLivingBase[0];
		switch (targetSelectionMode.getMode()) {
			case "Instant":{
				targets = potentialTargets.toArray(targets);
			}break;
		}
		
		return targets;
	}
	
	/**
	 * Starts blocking
	 */
	private void startBlocking() {
		if (blocking)
			return;
		
		blocking = true;
	}
	
	/**
	 * Stops blocking
	 */
	private void stopBlocking() {
		if (!blocking)
			return;
		
		blocking = false;
	}
	
	/**
	 * Sets the aps
	 */
	private void setAps() {
		aps = RandomUtils.nextDouble(minAps.getValue(), maxAps.getValue());
	}
	
	/**
	 * Gets the distance to the player's eyes
	 * @param entityLivingBase The entity to check the distance for
	 * @return
	 */
	private double getDistanceFromPlayerEyes(EntityLivingBase entityLivingBase) {
		if (positioningMode.is("Vanilla")) {
			return entityLivingBase.getDistanceToEntity(mc.thePlayer);
		}
		return PlayerUtils.getDistanceToPlayerEyes(entityLivingBase, shouldUseRenderPosTarget(), shouldUseRenderPosPlayer());
	}
	
	/**
	 * @return Whether or not to use the player's render pos instead of their actual pos
	 */
	private boolean shouldUseRenderPosPlayer() {
		return positioningMode.is("Player Render Pos") || positioningMode.is("Target & Player Render Pos");
	}
	
	/**
	 * @return Whether or not to use the target's render pos instead of their actual pos
	 */
	private boolean shouldUseRenderPosTarget() {
		return positioningMode.is("Target Render Pos") || positioningMode.is("Target & Player Render Pos");
	}
	
	private static class ApsMinMaxFixer{
		
		public ApsMinMaxFixer(ModKillAura modKillAura) {
			this.modKillAura = modKillAura;
		}
		
		private ModKillAura modKillAura;
		
		/**
		 * Used to make sure the min aps isn't greater than the max aps and vice versa
		 */
		@EventHandler
		private Handler<EventSettingUpdate> onSettingUpdate = e -> {
			
			if (e.getSetting() == modKillAura.minAps) {
				if (modKillAura.minAps.getValue() > modKillAura.maxAps.getValue())
					modKillAura.maxAps.setValue(modKillAura.minAps.getValue());
				if (modKillAura.aps < modKillAura.minAps.getValue())
					modKillAura.setAps();
			}
			else if (e.getSetting() == modKillAura.maxAps) {
				if (modKillAura.maxAps.getValue() < modKillAura.minAps.getValue())
					modKillAura.minAps.setValue(modKillAura.maxAps.getValue());
				if (modKillAura.aps > modKillAura.maxAps.getValue())
					modKillAura.setAps();
			}
			
		};
		
	}
	
}
