/**
 * 
 */
package xyz.yiffur.yiffur.mods.impl.visual;

import net.minecraft.client.renderer.ItemRenderer;
import xyz.yiffur.yiffur.eventBus.Subscriber;
import xyz.yiffur.yiffur.eventBus.YiffEvents;
import xyz.yiffur.yiffur.eventBus.impl.EventCheatTick;
import xyz.yiffur.yiffur.eventBus.impl.EventTick;
import xyz.yiffur.yiffur.mods.Module;
import xyz.yiffur.yiffur.settings.impl.BooleanSetting;
import xyz.yiffur.yiffur.settings.impl.ModeSetting;
import xyz.yiffur.yiffur.utils.ChatUtils;

/**
 * @author lavaflowglow
 *
 */
public class ModAnimations extends Module {

	public ModAnimations() {
		super("Animations", Category.VISUAL);
		setSettings(smallSwing, smoothBlockTransfer, animation);
	}
	
	public ModeSetting animation = new ModeSetting("Animation", "1.7", "1.7");
	public BooleanSetting smallSwing = new BooleanSetting("Small swing", false);
	public BooleanSetting smoothBlockTransfer = new BooleanSetting("Smooth block transfer", false);
	
}
