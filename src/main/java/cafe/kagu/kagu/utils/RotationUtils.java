/**
 * 
 */
package cafe.kagu.kagu.utils;

import javax.vecmath.Vector3d;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

/**
 * @author lavaflowglow
 *
 */
public class RotationUtils {
	
	private static Minecraft mc = Minecraft.getMinecraft();
	
	/**
	 * Called at the start of the client
	 */
	public static void start() {
		
	}
	
	/**
	 * Calculates the angle from the players eyes needed to look at pos2
	 * @param targetPos The target position
	 * @return A float array where the first index is the yaw and the second is the pitch
	 */
	public static float[] getRotations(Vector3d targetPos) {
		EntityPlayerSP thePlayer = mc.thePlayer;
		return getRotations(new Vector3d(thePlayer.posX, thePlayer.posY + thePlayer.getEyeHeight(), thePlayer.posZ), 
				targetPos);
	}
	
	/**
	 * Calculates the angle from pos1 needed to look at pos2
	 * @param pos1 The first position
	 * @param pos2 The target position
	 * @return A float array where the first index is the yaw and the second is the pitch
	 */
	public static float[] getRotations(Vector3d pos1, Vector3d pos2) {
		// soh cah toa
		double distX = pos2.getX() - pos1.getX();
		double distY = pos2.getY() - pos1.getY();
		double distZ = pos2.getZ() - pos1.getZ();
		double dist = Math.sqrt(distX * distX + distZ * distZ);
		double yaw = Math.toDegrees(Math.atan2(distZ, distX));
		double pitch = Math.toDegrees(Math.atan2(distY, dist));
		return new float[] {(float)yaw - 90, (float)-pitch};
	}
	
	/**
	 * @return The strafe yaw of the player, takes the players intended movements and generates a yaw with them
	 */
	public static float getStrafeYaw() {
		float yaw = 0;
		EntityPlayerSP thePlayer = mc.thePlayer;
		float moveForward = thePlayer.moveForward;
		float moveStrafing = thePlayer.moveStrafing;
		
		if (moveStrafing > 0) {
			yaw -= moveForward == 0 ? 90 : 45;
		}
		else if (moveStrafing < 0) {
			yaw += moveForward == 0 ? 90 : 45;
		}
		
		if (moveForward < 0) {
			yaw += 180;
			if (moveStrafing > 0) {
				yaw += 90;
			}
			else if (moveStrafing < 0) {
				yaw -= 90;
			}
		}
		
		yaw += thePlayer.rotationYaw;
		return yaw;
	}
	
}
