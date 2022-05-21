/**
 * 
 */
package cafe.kagu.kagu.utils;

/**
 * @author lavaflowglow
 *
 */
public class MathUtils {
	
	/**
	 * Calculates the distance between two points
	 * @param x1 The first x position
	 * @param y1 The first y position
	 * @param x2 The second x position
	 * @param y2 The second y position
	 * @return The distance between the two points
	 */
	public static double getDistance2D(double x1, double y1, double x2, double y2) {
		double distX = Math.max(x1, x2) - Math.min(x1, x2);
		double distY = Math.max(y1, y2) - Math.min(y1, y2);
		return Math.sqrt((distX * distX) + (distY * distY));
	}
	
}
