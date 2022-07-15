/**
 * 
 */
package cafe.kagu.kagu.utils;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vector3d;

/**
 * @author lavaflowglow
 *
 */
public class WorldUtils {
	
	private static Minecraft mc = Minecraft.getMinecraft();
	
	/**
	 * Called at the start of the client
	 */
	public static void start() {
		
	}
	
	/**
	 * @param amount The amount to extend
	 * @param posX The x starting pos
	 * @param posX The y starting pos
	 * @param posX The y starting pos
	 * @return The extended block pos, uses strafe motion for the extend yaw
	 */
	public static double[] extendPosition(double amount, double posX, double posY, double posZ) {
		if (!MovementUtils.isPlayerMoving())
			return new double[] {posX, posY, posZ};
		double yaw = RotationUtils.getStrafeYaw() + 90;
		posX += Math.cos(Math.toRadians(yaw)) * amount;
		posZ += Math.sin(Math.toRadians(yaw)) * amount;
		return new double[] {posX, posY, posZ};
	}
	
	/**
	 * @param pos The pos to check
	 * @return true if the block in that position is solid, otherwise false
	 */
	public static boolean isBlockSolid(BlockPos pos) {
		IBlockState blockState = mc.theWorld.getBlockState(pos);
		return blockState.getBlock().canCollideCheck(blockState, false);
	}
	
}
