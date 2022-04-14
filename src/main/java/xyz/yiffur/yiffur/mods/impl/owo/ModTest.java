/**
 * 
 */
package xyz.yiffur.yiffur.mods.impl.owo;

import org.apache.commons.lang3.RandomUtils;

import net.minecraft.client.Minecraft;
import xyz.yiffur.yiffur.eventBus.Subscriber;
import xyz.yiffur.yiffur.eventBus.YiffEvents;
import xyz.yiffur.yiffur.eventBus.impl.EventTick;
import xyz.yiffur.yiffur.mods.Module;

/**
 * @author lavaflowglow
 *
 */
public class ModTest extends Module {
	
	public ModTest() {
		super("Test", Category.OWO);
	}
	
	@Override
	public void onEnable() {
		setInfo("test");
	}
	
	@YiffEvents
	public Subscriber<EventTick> onTick = e -> {
		if (e.isPost()) {
			return;
		}
		if (Minecraft.getMinecraft().thePlayer.ticksExisted % 10 == 0)
			setInfo(new String(RandomUtils.nextBytes(RandomUtils.nextInt(1, 30))));
	};
	
}
