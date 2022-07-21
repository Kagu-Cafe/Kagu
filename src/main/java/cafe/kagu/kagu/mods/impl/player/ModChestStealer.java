/**
 * 
 */
package cafe.kagu.kagu.mods.impl.player;

import cafe.kagu.kagu.eventBus.EventHandler;
import cafe.kagu.kagu.eventBus.Handler;
import cafe.kagu.kagu.eventBus.impl.EventTick;
import cafe.kagu.kagu.mods.Module;
import cafe.kagu.kagu.settings.impl.BooleanSetting;
import cafe.kagu.kagu.settings.impl.IntegerSetting;
import cafe.kagu.kagu.settings.impl.ModeSetting;
import cafe.kagu.kagu.utils.TimerUtil;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;

/**
 * @author lavaflowglow
 *
 */
public class ModChestStealer extends Module {
	
	public ModChestStealer() {
		super("ChestStealer", Category.PLAYER);
		setSettings(c0ePosition, chestNameCheck, instant, delay);
	}
	
	private ModeSetting c0ePosition = new ModeSetting("C0E Position", "PRE", "PRE", "POST");
	private BooleanSetting chestNameCheck = new BooleanSetting("Chest Name Check", true);
	private BooleanSetting instant = new BooleanSetting("Instant", false);
	private IntegerSetting delay = (IntegerSetting) new IntegerSetting("Millis Delay", 0, 0, 2500, 50).setDependency(instant::isDisabled);
	
	private TimerUtil delayTimer = new TimerUtil();
	
	@EventHandler
	private Handler<EventTick> onTick = e -> {
		if (c0ePosition.is("PRE") ? e.isPost() : e.isPre())
			return;
		if (!(mc.currentScreen instanceof GuiChest))
			return;
		
		GuiChest guiChest = (GuiChest)mc.currentScreen;
		guiChest.getInventorySlots();
		
	};
	
}
