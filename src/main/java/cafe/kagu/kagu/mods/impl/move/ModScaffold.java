/**
 * 
 */
package cafe.kagu.kagu.mods.impl.move;

import org.apache.commons.lang3.RandomUtils;

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
import cafe.kagu.kagu.utils.ChatUtils;
import cafe.kagu.kagu.utils.DrawUtils3D;
import cafe.kagu.kagu.utils.MovementUtils;
import cafe.kagu.kagu.utils.RotationUtils;
import cafe.kagu.kagu.utils.SpoofUtils;
import cafe.kagu.kagu.utils.WorldUtils;
import cafe.kagu.kagu.utils.WorldUtils.PlaceOnBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.WorldSettings.GameType;

/**
 * @author lavaflowglow
 *
 */
public class ModScaffold extends Module {
	
	public ModScaffold() {
		super("Scaffold", Category.MOVEMENT);
		setSettings(rotationMode, c08Position, itemMode, vec3Mode, rayTraceMissMode, maxBlockReach, keepY, visuals, safewalk, accountForMovement, ignoreWorldBorder, extend, extendDistance, tower, towerMode);
	}
	
	private ModeSetting rotationMode = new ModeSetting("Rotation Mode", "None", "None");
	private ModeSetting c08Position = new ModeSetting("C08 Position", "PRE", "PRE", "POST"); // PRE sends before the c03, this is default mc behaviour. POST sends after the c03, this isn't default behaviour but may bypass other anticheats
	private ModeSetting itemMode = new ModeSetting("Item Selection", "Server", "Server", "Client", "Synced", "None");
	private ModeSetting vec3Mode = new ModeSetting("Vec3 Calculation", "Origin", "Origin", "Rand >=0 <=1", "Rand >=0.2 <=0.8", "Raytrace");
	private ModeSetting rayTraceMissMode = (ModeSetting) new ModeSetting("Vec3 Raytrace Miss Backup", "Origin", "Origin", "Rand >=0 <=1", "Rand >=0.2 <=0.8").setDependency(() -> vec3Mode.is("Raytrace"));
	
	private BooleanSetting keepY = new BooleanSetting("Keep Y", false);
	private BooleanSetting visuals = new BooleanSetting("Visuals", true);
	private BooleanSetting safewalk = new BooleanSetting("Safewalk", true);
	private BooleanSetting accountForMovement = new BooleanSetting("Account For Movement", false);
	private BooleanSetting ignoreWorldBorder = new BooleanSetting("Ignore World Border", false);
	
	private DoubleSetting maxBlockReach = new DoubleSetting("Max Block Reach", 3, 1, 4, 0.5);
	
	// Extend
	private BooleanSetting extend = new BooleanSetting("Extend", false);
	private DoubleSetting extendDistance = (DoubleSetting) new DoubleSetting("Extend Distance", 1, 0.1, 6, 0.1).setDependency(extend::isEnabled);
	
	// Tower
	private BooleanSetting tower = new BooleanSetting("Tower", false);
	private ModeSetting towerMode = (ModeSetting) new ModeSetting("Tower Mode", "NCP", "NCP").setDependency(tower::isEnabled);
	
	// Vars used in the scaffold
	private BlockPos placePos = null;
	private PlaceOnBlock placeOnInfo = null;
	private int keepYPosition = Integer.MAX_VALUE;
	private float[] rotations = new float[] {0, 0};
	private float[] lastRotations = new float[] {0, 0};
	
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
		
		// Return if the player is in spectator or adventure mode
		if (mc.playerController.getCurrentGameType() == GameType.SPECTATOR || mc.playerController.getCurrentGameType() == GameType.ADVENTURE) {
			placePos = null;
			placeOnInfo = null;
			return;
		}
		
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
		if (WorldUtils.isBlockSolid(placePos)) {
			placePos = null;
			placeOnInfo = null;
			return;
		}
		
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
		
		switch (rotationMode.getMode()) {
			case "None":{
				rotations[0] = e.getRotationYaw();
				rotations[1] = e.getRotationPitch();
				lastRotations[0] = e.getRotationYaw();
				lastRotations[1] = e.getRotationPitch();
			}break;
		}
		
	};
	
	/**
	 * Used to send c08 packets, can be on the pre or the post
	 */
	@EventHandler
	private Handler<EventPlayerUpdate> c08PlayerUpdate = e -> {
		if (c08Position.is("PRE") ? e.isPost() : e.isPre())
			return;
		
		// Only attempt place if bother place pos and place on info is set
		if (placePos == null || placeOnInfo == null)
			return;
		
		// Vars
		EntityPlayerSP thePlayer = mc.thePlayer;
		WorldClient theWorld = mc.theWorld;
		BlockPos placeOn = placeOnInfo.getPlaceOn();
		EnumFacing placeOnFacing = placeOnInfo.getPlaceFacing();
		IBlockState placeOnState = theWorld.getBlockState(placeOn);
		float[] vec3 = getVec3(vec3Mode.getMode());
		
		// World border check, modified from normal mc because I want to give the user the option to bypass this check
		if (!theWorld.getWorldBorder().contains(placeOn) && ignoreWorldBorder.isDisabled()){
			return;
        }
		
		// This is if you click blocks that show guis or do stuff, the scaffold targeting should avoid blocks like this but in the case it doesn't this should catch it
        if (placeOnState.getBlock().doesBlockActivate()) {
        	return;
        }
        
        // If the block for some reason activates then do nothing, otherwise place the block
        if (!((!thePlayer.isSneaking() || thePlayer.getHeldItem() == null) && placeOnState.getBlock().onBlockActivated(theWorld, placeOn, placeOnState, thePlayer, placeOnFacing, vec3[0], vec3[1], vec3[2]))){
        	mc.getNetHandler().getNetworkManager().sendPacket(new C08PacketPlayerBlockPlacement(placeOn, placeOnFacing.getIndex(), thePlayer.inventory.getCurrentItem(), vec3[0], vec3[1], vec3[2]));
        }
        
	};
	
	/**
	 * @return The vec3 to use for the c08
	 */
	private float[] getVec3(String vec3Mode) {
		
		// "Origin", "Rand >=0 <=1", "Rand >=0.2 <=0.8", "Raytrace"
		switch (vec3Mode) {
			case "Origin":return new float[] {0, 0, 0};
			case "Rand >=0 <=1":return new float[] {RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0, 1), RandomUtils.nextFloat(0, 1)};
			case "Rand >=0.2 <=0.8":return new float[] {RandomUtils.nextFloat(0.2f, 0.8f), RandomUtils.nextFloat(0.2f, 0.8f), RandomUtils.nextFloat(0.2f, 0.8f)};
			case "Raytrace":{
				
				// Taken from mc code, some var names changed to make them easier to understand
				float partialTicks = mc.getTimer().getRenderPartialTicks();
				double reach = maxBlockReach.getValue() + 1;
				Vec3 eyePos = mc.thePlayer.getPositionEyes(partialTicks);
	            Vec3 look = RotationUtils.getLook(rotations[0], rotations[1], lastRotations[0], lastRotations[1]);
	            Vec3 vec32 = eyePos.addVector(look.xCoord * reach, look.yCoord * reach, look.zCoord * reach);
	            
	            // Get the block collision box
	            BlockPos placeOn = placeOnInfo.getPlaceOn();
	            WorldClient theWorld = mc.theWorld;
	            IBlockState placeOnState = theWorld.getBlockState(placeOn);
	            AxisAlignedBB collisionBox = placeOnState.getBlock().getCollisionBoundingBox(theWorld, placeOn, placeOnState);
	            
	            // Do the raytrace
	            MovingObjectPosition rayTrace = collisionBox.calculateIntercept(eyePos, vec32);
	            
	            // Process the results
	            if (rayTrace == null || rayTrace.typeOfHit == MovingObjectType.MISS) {
	            	return getVec3(rayTraceMissMode.getMode());
	            }
	            Vec3 results = rayTrace.hitVec.addVector(-placeOn.getX(), -placeOn.getY(), -placeOn.getZ());
	            return new float[] {(float) results.xCoord, (float) results.yCoord, (float) results.zCoord};
			}
		}
		
		return new float[] {0, 0, 0};
	}
	
}
