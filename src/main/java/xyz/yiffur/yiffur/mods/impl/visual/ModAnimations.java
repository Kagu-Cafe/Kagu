/**
 * 
 */
package xyz.yiffur.yiffur.mods.impl.visual;

import xyz.yiffur.yiffur.eventBus.Subscriber;
import xyz.yiffur.yiffur.eventBus.YiffEvents;
import xyz.yiffur.yiffur.eventBus.impl.EventTick;
import xyz.yiffur.yiffur.mods.Module;
import xyz.yiffur.yiffur.settings.impl.ModeSetting;
import xyz.yiffur.yiffur.utils.ChatUtils;

/**
 * @author Waterbongo
 *
 */
public class ModAnimations extends Module {

	public ModAnimations() {
		super("Animations", Category.VISUAL);
	}
	public ModeSetting animation = new ModeSetting("Animation", "1.7", "1.7", "others");
	
	@Override
	public void initialize() {
		setSettings(animation);
	}
	@YiffEvents
	public Subscriber<EventTick> onTick = e -> {
		if (animation.is("1.7")) {
			ChatUtils.addChatMessage(animation.getMode());
		}
		setInfo(animation.getMode());
	};
}
