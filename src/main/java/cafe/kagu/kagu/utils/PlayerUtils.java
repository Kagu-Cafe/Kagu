/**
 * 
 */
package cafe.kagu.kagu.utils;

import javax.vecmath.Vector3d;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;

/**
 * @author lavaflowglow
 *
 */
public class PlayerUtils {
	
	private static Logger logger = LogManager.getLogger();
	
	/**
	 * Gets the distance to the player's eyes
	 * @param entity The entity to check the distance for
	 * @param useEntityRenderPos Whether or not to use the interpolated render positions for the entity
	 * @param usePlayerRenderPos Whether or not to use the interpolated render positions for the player
	 * @return The distance from the player's eyes to the closest point in the entity's hitbox
	 */
	public static double getDistanceToPlayerEyes(EntityLivingBase entity, boolean useEntityRenderPos, boolean usePlayerRenderPos) {
		Minecraft mc = Minecraft.getMinecraft();
		
		// Get the entity's bounding box
		// Get bounding box
		AxisAlignedBB boundingBox = entity.getEntityBoundingBox();
		Vector3d playerEyeCoords = getPlayerEyePositions(usePlayerRenderPos);
		
		// Handle null bounding boxes
		if (boundingBox == null) {
			logger.error("The bounding boxes is null, returning default distance calculations. This may cause issues");
			return entity.getDistanceToEntity(mc.thePlayer);
		}
		
		// If enabled correct bounding box coords to match interpolation
		if (useEntityRenderPos) {
			Vector3d entityRenderCoords = DrawUtils3D.get3dEntityOffsets(entity);
			
			// Recreate the bounding box with the interpolated coords
			boundingBox = new AxisAlignedBB(entityRenderCoords.getX(), entityRenderCoords.getY(), entityRenderCoords.getZ(), entityRenderCoords.getX() + (boundingBox.maxX - boundingBox.minX), entityRenderCoords.getY() + (boundingBox.maxY - boundingBox.minY), entityRenderCoords.getZ() + (boundingBox.maxZ - boundingBox.minZ));
		}
		
		// Get the closest point in the bounding box
		Vector3d closestPointInBoundingBox = new Vector3d(0, 0, 0);
		
		// X
		if (playerEyeCoords.getX() <= boundingBox.minX) {
			closestPointInBoundingBox.setX(boundingBox.minX);
		}
		else if (playerEyeCoords.getX() >= boundingBox.maxX) {
			closestPointInBoundingBox.setX(boundingBox.maxX);
		}
		else {
			closestPointInBoundingBox.setX(playerEyeCoords.getX());
		}
		
		// Y
		if (playerEyeCoords.getY() <= boundingBox.minY) {
			closestPointInBoundingBox.setY(boundingBox.minY);
		}
		else if (playerEyeCoords.getY() >= boundingBox.maxY) {
			closestPointInBoundingBox.setY(boundingBox.maxY);
		}
		else {
			closestPointInBoundingBox.setY(playerEyeCoords.getY());
		}
		
		// Z
		if (playerEyeCoords.getZ() <= boundingBox.minZ) {
			closestPointInBoundingBox.setZ(boundingBox.minZ);
		}
		else if (playerEyeCoords.getZ() >= boundingBox.maxZ) {
			closestPointInBoundingBox.setZ(boundingBox.maxZ);
		}
		else {
			closestPointInBoundingBox.setZ(playerEyeCoords.getX());
		}
		
		// Calculate distance and return
		double distX = playerEyeCoords.getX() - closestPointInBoundingBox.getX();
		double distY = playerEyeCoords.getY() - closestPointInBoundingBox.getY();
		double distZ = playerEyeCoords.getZ() - closestPointInBoundingBox.getZ();
		return Math.sqrt(distX * distX + distY * distY + distZ * distZ);
	}
	
	/**
	 * Gets the player's eye position
	 * @param useRenderPos Whether to use the player's render position or not
	 * @return The player's eye position
	 */
	public static Vector3d getPlayerEyePositions(boolean useRenderPos) {
		Minecraft mc = Minecraft.getMinecraft();
		Vector3d playerEyeCoords = new Vector3d(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);
		if (useRenderPos) {
			playerEyeCoords = DrawUtils3D.get3dPlayerOffsets();
		}
		return playerEyeCoords;
		
	}
	
}
