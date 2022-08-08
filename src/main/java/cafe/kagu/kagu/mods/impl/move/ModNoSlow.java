/**
 * 
 */
package cafe.kagu.kagu.mods.impl.move;

import cafe.kagu.kagu.mods.Module;
import cafe.kagu.kagu.settings.impl.BooleanSetting;

/**
 * @author lavaflowglow
 *
 */
public class ModNoSlow extends Module {
	
	public ModNoSlow() {
		super("NoSlow", Category.MOVEMENT);
		setSettings(cancelItemSlowdown, cancelSneakSlowdown, cancelWebSlowdown, cancelLadderSlowdown, cancelLadderDescentSlowdown);
	}
	
	private BooleanSetting cancelItemSlowdown = new BooleanSetting("Cancel Item Slowdown", true);
	private BooleanSetting cancelSneakSlowdown = new BooleanSetting("Cancel Sneak Slowdown", false);
	private BooleanSetting cancelWebSlowdown = new BooleanSetting("Cancel Web Slowdown", false);
	private BooleanSetting cancelLadderSlowdown = new BooleanSetting("Cancel Ladder Slowdown", false);
	private BooleanSetting cancelLadderDescentSlowdown = new BooleanSetting("Cancel Ladder Descent Slowdown", false);
	
	/**
	 * @return the cancelItemSlowdown
	 */
	public BooleanSetting getCancelItemSlowdown() {
		return cancelItemSlowdown;
	}
	
	/**
	 * @return the cancelSneakSlowdown
	 */
	public BooleanSetting getCancelSneakSlowdown() {
		return cancelSneakSlowdown;
	}
	
	/**
	 * @return the cancelWebSlowdown
	 */
	public BooleanSetting getCancelWebSlowdown() {
		return cancelWebSlowdown;
	}
	
	/**
	 * @return the cancelLadderSlowdown
	 */
	public BooleanSetting getCancelLadderSlowdown() {
		return cancelLadderSlowdown;
	}
	
	/**
	 * @return the cancelLadderDescentSlowdown
	 */
	public BooleanSetting getCancelLadderDescentSlowdown() {
		return cancelLadderDescentSlowdown;
	}
	
}
