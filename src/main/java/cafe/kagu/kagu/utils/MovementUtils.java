/**
 * 
 */
package cafe.kagu.kagu.utils;

import java.util.List;

import cafe.kagu.kagu.eventBus.EventBus;
import cafe.kagu.kagu.mods.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.AxisAlignedBB;

/**
 * @author lavaflowglow
 *
 */
public class MovementUtils {
	
	private static Minecraft mc = Minecraft.getMinecraft();
	
	/**
	 * Called at the start of the client
	 */
	public static void start() {
		
	}
	
	/**
	 * @return true if on ground, otherwise false
	 */
	public static boolean isTrueOnGround() {
		return isTrueOnGround(0.0784000015258789);
	}
	
	/**
	 * @param distance The distance from the ground to check
	 * @return true if on ground, otherwise false
	 */
	public static boolean isTrueOnGround(double distance) {
		EntityPlayerSP thePlayer = mc.thePlayer;
		return !mc.theWorld.getCollidingBoundingBoxes(thePlayer, thePlayer.getEntityBoundingBox().expand(0, distance, 0)).isEmpty();
	}
	
	/**
	 * @return true if the player is moving, otherwise false
	 */
	public static boolean isPlayerMoving() {
		EntityPlayerSP thePlayer = mc.thePlayer;
		return thePlayer.moveForward != 0 || thePlayer.moveStrafing != 0;
	}
	
	/**
	 * Sets the motion of the player, uses strafe motion
	 * @param motion The motion to set
	 */
	public static void setMotion(double motion) {
		double yaw = RotationUtils.getStrafeYaw() + 90;
		EntityPlayerSP thePlayer = mc.thePlayer;
		thePlayer.motionX = Math.cos(Math.toRadians(yaw)) * motion;
		thePlayer.motionZ = Math.sin(Math.toRadians(yaw)) * motion;
	}
	
	/**
	 * Sets the motion of the player
	 * @param motion The motion to set
	 * @param yaw The yaw to use for motion calculations
	 */
	public static void setMotion(double motion, double yaw) {
		yaw += 90;
		EntityPlayerSP thePlayer = mc.thePlayer;
		thePlayer.motionX = Math.cos(Math.toRadians(yaw)) * motion;
		thePlayer.motionZ = Math.sin(Math.toRadians(yaw)) * motion;
	}
	
}
