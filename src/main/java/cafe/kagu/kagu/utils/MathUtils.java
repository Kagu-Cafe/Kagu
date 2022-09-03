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
		return getDistance2D(distX, y1, y2);
	}
	
	/**
	 * Calculates the distance between two points
	 * @param side1 The length of 1 of the sides
	 * @param y1 The first y position
	 * @param y2 The second y position
	 * @return The distance between the two points
	 */
	public static double getDistance2D(double side1, double y1, double y2) {
		double distY = y2 - y1;
		return getDistance2D(side1, distY);
	}
	
	/**
	 * Calculates the distance between two points
	 * @param side1 The length of the first side
	 * @param side2 The length of the second side
	 * @return The distance between the two points
	 */
	public static double getDistance2D(double side1, double side2) {
		return Math.sqrt((side1 * side1) + (side2 * side2));
	}
	
	/**
	 * Lerps two values
	 * @param start The starting value
	 * @param end The ending value
	 * @param progress The current progress
	 * @return The final lerped number
	 */
	public static double lerp(double start, double end, double progress) {
		return start + Math.abs(end - start) * progress;
	}
	
	/**
	 * Calculates the range between two values and returns it
	 * @param value1 The first value
	 * @param value2 The second value
	 * @return The range between the two values
	 */
	public static double getRange(double value1, double value2) {
		return Math.abs(value2 - value1);
	}
	
	/**
	 * Calculates the range between two values and returns it
	 * @param value1 The first value
	 * @param value2 The second value
	 * @return The range between the two values
	 */
	public static float getRange(float value1, float value2) {
		return Math.abs(value2 - value1);
	}
	
}
