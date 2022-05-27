/**
 * 
 */
package cafe.kagu.kagu.utils;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3d;

import net.minecraft.client.Minecraft;

/**
 * @author lavaflowglow
 *
 */
public class RotationUtils {
	
	/**
	 * Calculates the angle from the players eyes needed to look at pos2
	 * @param targetPos The target position
	 * @return A float array where the first index is the yaw and the second is the pitch
	 */
	public static float[] getRotations(Vector3d targetPos) {
		return getRotations(new Vector3d(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight(), Minecraft.getMinecraft().thePlayer.posZ), 
				targetPos);
	}
	
	/**
	 * Calculates the angle from pos1 needed to look at pos2
	 * @param pos1 The first position
	 * @param pos2 The target position
	 * @return A float array where the first index is the yaw and the second is the pitch
	 */
	public static float[] getRotations(Vector3d pos1, Vector3d pos2) {
		double distX = pos1.getX() - pos2.getX();
		double distY = pos1.getY() - pos2.getY();
		double distZ = pos1.getZ() - pos2.getZ();
		double dist = Math.sqrt((distX * distX) + (distZ * distZ));
		double yaw = Math.toDegrees(Math.atan2(distZ, distX)) + 90;
		double pitch = Math.toDegrees(Math.atan2(distY, dist));
		return new float[] {(float)yaw, (float)pitch};
	}
	
}
