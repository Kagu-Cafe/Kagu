/**
 * 
 */
package xyz.yiffur.yiffur.ui.clickgui;

import xyz.yiffur.yiffur.mods.Module;
import xyz.yiffur.yiffur.mods.Module.Category;

/**
 * @author lavaflowglow
 *
 */
public class Tab {

	private double x = 0, y = 0;
	private boolean expanded = false;
	private Category category;

	// Not stored in a file
	private Module[] modules = new Module[0];

	/**
	 * @return the x
	 */
	public double getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public double getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * @return the expanded
	 */
	public boolean isExpanded() {
		return expanded;
	}

	/**
	 * @param expanded the expanded to set
	 */
	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	/**
	 * @return the category
	 */
	public Category getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(Category category) {
		this.category = category;
	}

	/**
	 * @return the modules
	 */
	public Module[] getModules() {
		return modules;
	}

	/**
	 * @param modules the modules to set
	 */
	public void setModules(Module[] modules) {
		this.modules = modules;
	}

}
