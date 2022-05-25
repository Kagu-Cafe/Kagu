/**
 * 
 */
package cafe.kagu.kagu.mods.impl.combat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import cafe.kagu.kagu.eventBus.EventHandler;
import cafe.kagu.kagu.eventBus.Handler;
import cafe.kagu.kagu.eventBus.impl.EventPlayerUpdate;
import cafe.kagu.kagu.mods.Module;
import cafe.kagu.kagu.settings.SettingDependency;
import cafe.kagu.kagu.settings.impl.BooleanSetting;
import cafe.kagu.kagu.settings.impl.DoubleSetting;
import cafe.kagu.kagu.settings.impl.ModeSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author lavaflowglow
 *
 */
public class ModKillAura extends Module {

	public ModKillAura() {
		super("KillAura", Category.COMBAT);
		setSettings(rotationMode, blockMode, preferredTargetMetrics, targetSelectionMode, hitRange, blockRange, minAps,
				maxAps, targetAll, targetPlayers, targetAnimals, targetMobs);
	}
	
	// Modes
	private ModeSetting rotationMode = new ModeSetting("Rotation Mode", "Lock", "Lock");
	private ModeSetting blockMode = new ModeSetting("Block Mode", "None", "None");
	private ModeSetting preferredTargetMetrics = new ModeSetting("Preferred Target Metrics", "Distance", "Distance");
	private ModeSetting targetSelectionMode = new ModeSetting("Target Selection", "Instant", "Instant");
	
	// Ranges
	private DoubleSetting hitRange = new DoubleSetting("Hit Range", 3, 1, 7, 0.1);
	private DoubleSetting blockRange = new DoubleSetting("Block Range", 3, 1, 7, 0.1);
	
	// APS settings
	private DoubleSetting minAps = new DoubleSetting("Min APS", 3, 1, 7, 0.1);
	private DoubleSetting maxAps = new DoubleSetting("Max APS", 3, 1, 7, 0.1);
	
	// Targets
	private BooleanSetting targetAll = new BooleanSetting("Target Everything", true);
	private BooleanSetting targetPlayers = (BooleanSetting) new BooleanSetting("Target Players", true).setDependency((SettingDependency)() -> {return targetAll.isDisabled();});
	private BooleanSetting targetAnimals = (BooleanSetting) new BooleanSetting("Target Animals", false).setDependency((SettingDependency)() -> {return targetAll.isDisabled();});
	private BooleanSetting targetMobs = (BooleanSetting) new BooleanSetting("Target Mobs", false).setDependency((SettingDependency)() -> {return targetAll.isDisabled();});
	
	@EventHandler
	private Handler<EventPlayerUpdate> onPlayerUpdate = e -> {
		if (e.isPost())
			return;
		
	};
	
	private EntityLivingBase lastTarget = null;
	private EntityLivingBase[] getTargets() {
		
		ArrayList<Entity> potentialTargets = new ArrayList<>(mc.theWorld.loadedEntityList);
		
		// Remove non living and out of range entities
		potentialTargets = (ArrayList<Entity>) potentialTargets
				.stream()
				.filter(ent -> 
						ent instanceof EntityLivingBase && 
						ent.getDistanceToEntity(mc.thePlayer) <= Math.max(hitRange.getValue(), blockRange.getValue()))
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
				potentialTargets.sort(Comparator.comparingDouble(ent -> ent.getDistanceToEntity(mc.thePlayer)));
				Collections.reverse(potentialTargets);
			}break;
		}
		
		// Now that the targets are filtered and sorted we can use the users preferred
		// selection mode to make a decision on who the target should be
		
		return new EntityLivingBase[0];
	}
	
}
