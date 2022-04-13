/**
 * 
 */
package xyz.yiffur.yiffur.font;

/**
 * @author lavaflowglow
 *
 */
public class Glyph {

	/**
	 * 
	 * @param x      The x coord of the char on the image
	 * @param y      The y coord of the char on the image
	 * @param width  The width of the char on the image
	 * @param height The height of the char on the image
	 */
	public Glyph(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width + 2;
		this.height = height + 2;
	}

	private int x, y, width, height;

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

}
