/**
 * 
 */
package cafe.kagu.kagu.mods.impl.move;

import cafe.kagu.kagu.eventBus.Event;
import cafe.kagu.kagu.eventBus.EventHandler;
import cafe.kagu.kagu.eventBus.Handler;
import cafe.kagu.kagu.eventBus.impl.EventPlayerUpdate;
import cafe.kagu.kagu.eventBus.impl.EventRender3D;
import cafe.kagu.kagu.eventBus.impl.EventTick;
import cafe.kagu.kagu.mods.Module;
import cafe.kagu.kagu.settings.impl.BooleanSetting;
import cafe.kagu.kagu.settings.impl.DoubleSetting;
import cafe.kagu.kagu.settings.impl.ModeSetting;
import cafe.kagu.kagu.utils.DrawUtils3D;
import cafe.kagu.kagu.utils.MovementUtils;
import cafe.kagu.kagu.utils.SpoofUtils;
import cafe.kagu.kagu.utils.WorldUtils;
import cafe.kagu.kagu.utils.WorldUtils.PlaceOnBlock;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

/**
 * @author lavaflowglow
 *
 */
public class ModScaffold extends Module {
	
	public ModScaffold() {
		super("Scaffold", Category.MOVEMENT);
		setSettings(rotationMode, c08Position, itemMode, maxBlockReach, keepY, visuals, safewalk, accountForMovement, extend, extendDistance, tower, towerMode);
	}
	
	private ModeSetting rotationMode = new ModeSetting("Rotation Mode", "None", "None");
	private ModeSetting c08Position = new ModeSetting("C08 Position", "PRE", "PRE", "POST"); // PRE sends before the c03, this is default mc behaviour. POST sends after the c03, this isn't default behaviour but may bypass other anticheats
	private ModeSetting itemMode = new ModeSetting("Item Selection", "Server", "Server", "Client", "Synced", "None");
	
	private BooleanSetting keepY = new BooleanSetting("Keep Y", false);
	private BooleanSetting visuals = new BooleanSetting("Visuals", true);
	private BooleanSetting safewalk = new BooleanSetting("Safewalk", true);
	private BooleanSetting accountForMovement = new BooleanSetting("Account For Movement", false);
	
	private DoubleSetting maxBlockReach = new DoubleSetting("Max Block Reach", 3, 1, 4, 0.5);
	
	// Extend
	private BooleanSetting extend = new BooleanSetting("Extend", false);
	private DoubleSetting extendDistance = (DoubleSetting) new DoubleSetting("Extend Distance", 1, 0.1, 6, 0.1).setDependency(extend::isEnabled);
	
	// Tower
	private BooleanSetting tower = new BooleanSetting("Tower", false);
	private ModeSetting towerMode = (ModeSetting) new ModeSetting("Tower Mode", "NCP", "NCP").setDependency(tower::isEnabled);
	
	private BlockPos placePos = null;
	private PlaceOnBlock placeOnInfo = null;
	private int keepYPosition = Integer.MAX_VALUE;
	
	@Override
	public void onEnable() {
		placePos = null;
		placeOnInfo = null;
		keepYPosition = (int)(mc.thePlayer.posY - 1);
	}
	
	/**
	 * Used for block selection & safewalk
	 */
	@EventHandler
	private Handler<EventTick> onTick = e -> {
		if (e.isPost())
			return;
		
		// Vars
		EntityPlayerSP thePlayer = mc.thePlayer;
		boolean shouldKeepY = keepY.isEnabled() && thePlayer.posY > keepYPosition;
		double keepYPosition = this.keepYPosition;
		double[] playerPos = new double[] {thePlayer.posX, thePlayer.posY, thePlayer.posZ};
		
		// Same y recalculation if on ground
		if (MovementUtils.isTrueOnGround()) {
			keepYPosition = (int)(mc.thePlayer.posY - 1);
		}
		
		// Account for movement
		if (accountForMovement.isEnabled()) {
			playerPos[0] += thePlayer.motionX;
			playerPos[1] += thePlayer.motionY + 0.0784000015258789;
			playerPos[2] += thePlayer.motionZ;
		}
		
		// Safewalk
		if (safewalk.isEnabled())
			SpoofUtils.setSpoofSneakMovement(true);
		
		// Default place pos
		placePos = new BlockPos(playerPos[0], shouldKeepY ? keepYPosition : (playerPos[1] - 1), playerPos[2]);
		
		// keep y catch incase you fall below the scaffold and land again
		if (!shouldKeepY && keepY.isEnabled() && MovementUtils.isTrueOnGround()) {
			keepYPosition = placePos.getY();
		}
		
		// Extend
		if (extend.isEnabled() && WorldUtils.isBlockSolid(placePos)) {
			for (double d = 0.1; d < extendDistance.getValue(); d += 0.1) {
				double[] pos = WorldUtils.extendPosition(d, playerPos[0], shouldKeepY ? keepYPosition : (playerPos[1] - 1), playerPos[2]);
				BlockPos newPos = new BlockPos(pos[0], pos[1], pos[2]);
				if (!WorldUtils.isBlockSolid(newPos)) {
					placePos = newPos;
					break;
				}
			}
		}
		
		// If the block we want to place is already solid then return, no point in doing further calculations
		if (WorldUtils.isBlockSolid(placePos))
			return;
		
		// Find what block and placing we should place on, this method uses basic pathfinding to efficiently find which block to place on
		placeOnInfo = WorldUtils.getPlaceOn(placePos, maxBlockReach.getValue());
	};
	
	/**
	 * Used for visuals
	 */
	@EventHandler
	private Handler<EventRender3D> onRender3D = e -> {
		if (visuals.isDisabled())
			return;
		
		BlockPos placePos = this.placePos;
		
		if (placePos != null)
			DrawUtils3D.drawColored3DWorldBox(placePos.getX(), placePos.getY(), placePos.getZ(), placePos.getX() + 1, placePos.getY() + 1, placePos.getZ() + 1, 0xffffffff);
		
		if (placeOnInfo != null) {
			DrawUtils3D.drawColored3DWorldBox(placeOnInfo.getPlaceOn().getX(), placeOnInfo.getPlaceOn().getY(), placeOnInfo.getPlaceOn().getZ(), placeOnInfo.getPlaceOn().getX() + 1, placeOnInfo.getPlaceOn().getY() + 1, placeOnInfo.getPlaceOn().getZ() + 1, 0xff0000ff);
		}
		
	};
	
	/**
	 * Used for rotations, always rotates on the pre event
	 */
	@EventHandler
	private Handler<EventPlayerUpdate> rotationPlayerUpdate = e -> {
		if (e.isPost())
			return;
	};
	
	/**
	 * Used to send c08 packets, can be on the pre or the post
	 */
	@EventHandler
	private Handler<EventPlayerUpdate> c08PlayerUpdate = e -> {
		if (c08Position.is("PRE") ? e.isPost() : e.isPre())
			return;
		
		// Will be raplces later, this was just for a test
		mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem(), placeOnInfo.getPlaceOn(), placeOnInfo.getPlaceFacing(), new Vec3(0, 0, 0));
		
	};
	
}
