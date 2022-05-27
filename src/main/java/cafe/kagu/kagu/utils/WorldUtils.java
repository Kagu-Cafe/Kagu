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
public class WorldUtils {
	
	private static Logger logger = LogManager.getLogger();
	
	public static double getDistanceToPlayerHead(EntityLivingBase entity, boolean useEntityRenderPos, boolean usePlayerRenderPos) {
		Minecraft mc = Minecraft.getMinecraft();
		
		// Get the entity's bounding box
		// Get bounding box
		AxisAlignedBB boundingBox = entity.getEntityBoundingBox();
		Vector3d playerHeadCoords = new Vector3d(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);
		
		// Handle null bounding boxes
		if (boundingBox == null) {
			logger.error("The bounding boxes is null, returning default distance calculations. This may cause issues");
			return entity.getDistance(playerHeadCoords.getX(), playerHeadCoords.getY(), playerHeadCoords.getZ());
		}
		
		// If enabled correct bounding box coords to match interpolation
		if (useEntityRenderPos) {
			Vector3d entityRenderCoords = DrawUtils3D.get3dEntityOffsets(entity);
			
			// Recreate the bounding box with the interpolated coords
			boundingBox = new AxisAlignedBB(entityRenderCoords.getX(), entityRenderCoords.getY(), entityRenderCoords.getZ(), entityRenderCoords.getX() + (boundingBox.maxX - boundingBox.minX), entityRenderCoords.getY() + (boundingBox.maxY - boundingBox.minY), entityRenderCoords.getZ() + (boundingBox.maxZ - boundingBox.minZ));
		}
		if (usePlayerRenderPos) {
			playerHeadCoords = DrawUtils3D.get3dPlayerOffsets();
		}
		
		// Get the closest point in the bounding box
		Vector3d closestPointInBoundingBox = new Vector3d(0, 0, 0);
		
		// X
		if (playerHeadCoords.getX() <= boundingBox.minX) {
			closestPointInBoundingBox.setX(boundingBox.minX);
		}
		else if (playerHeadCoords.getX() >= boundingBox.maxX) {
			closestPointInBoundingBox.setX(boundingBox.maxX);
		}
		else {
			closestPointInBoundingBox.setX(playerHeadCoords.getX());
		}
		
		// Y
		if (playerHeadCoords.getY() <= boundingBox.minY) {
			closestPointInBoundingBox.setY(boundingBox.minY);
		}
		else if (playerHeadCoords.getY() >= boundingBox.maxY) {
			closestPointInBoundingBox.setY(boundingBox.maxY);
		}
		else {
			closestPointInBoundingBox.setY(playerHeadCoords.getY());
		}
		
		// Z
		if (playerHeadCoords.getZ() <= boundingBox.minZ) {
			closestPointInBoundingBox.setZ(boundingBox.minZ);
		}
		else if (playerHeadCoords.getZ() >= boundingBox.maxZ) {
			closestPointInBoundingBox.setZ(boundingBox.maxZ);
		}
		else {
			closestPointInBoundingBox.setZ(playerHeadCoords.getX());
		}
		
	}
	
}
