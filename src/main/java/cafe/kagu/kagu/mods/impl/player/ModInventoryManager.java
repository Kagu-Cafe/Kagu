/**
 * 
 */
package cafe.kagu.kagu.mods.impl.player;

import java.util.List;

import cafe.kagu.kagu.eventBus.EventHandler;
import cafe.kagu.kagu.eventBus.Handler;
import cafe.kagu.kagu.eventBus.impl.EventTick;
import cafe.kagu.kagu.mods.Module;
import cafe.kagu.kagu.settings.impl.BooleanSetting;
import cafe.kagu.kagu.settings.impl.IntegerSetting;
import cafe.kagu.kagu.settings.impl.ModeSetting;
import cafe.kagu.kagu.utils.InventoryUtils;
import cafe.kagu.kagu.utils.MovementUtils;
import cafe.kagu.kagu.utils.TimerUtil;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.world.WorldSettings.GameType;

/**
 * @author lavaflowglow
 *
 */
public class ModInventoryManager extends Module {
	
	public ModInventoryManager() {
		super("Inventory Manager", Category.PLAYER);
		setSettings(c0ePosition, mode, instant, delay, inventorySpoof, weaponChoice, dropItems, adventureCheck, autoArmor, armorSwapMode);
	}
	
	private ModeSetting c0ePosition = new ModeSetting("C0E Position", "PRE", "PRE", "POST");
	private ModeSetting mode = new ModeSetting("Mode", "Silent", "Silent", "Inventory Open", "No Movement");
	private ModeSetting inventorySpoof = new ModeSetting("Inventory Spoof", "None", "None", "Open & Close on Action", "Open on Start, Close on Stop").setDependency(() -> mode.is("Silent") || mode.is("No Movement"));
	private ModeSetting weaponChoice = new ModeSetting("Weapon Choice", "Sword", "Sword", "Axe");
	private BooleanSetting instant = new BooleanSetting("Instant", false);
	private IntegerSetting delay = new IntegerSetting("Millis Delay", 0, 0, 2500, 50).setDependency(instant::isDisabled);
	private BooleanSetting dropItems = new BooleanSetting("Drop Items", true);
	private BooleanSetting adventureCheck = new BooleanSetting("Adventure Mode Check", true);
	private BooleanSetting autoArmor = new BooleanSetting("Auto Armor", true);
	private ModeSetting armorSwapMode = new ModeSetting("Armor Swap Mode", "Instant", "Instant", "Separate actions").setDependency(() -> instant.isDisabled() && autoArmor.isEnabled());
	
	private TimerUtil delayTimer = new TimerUtil();
	private boolean dropOrSort = false;
	private boolean inventoryOpen = false;
	
	private final int weaponSlot = 0 + 36;
	private final int bowSlot = 1 + 36;
	private final int pickaxeSlot = 2 + 36;
	private final int axeSlot = 3 + 36;
	private final int spadeSlot = 4 + 36;
	private final int shearsSlot = 5 + 36;
	private final int gappleSlot = 6 + 36;
	private final int blockSlot = 7 + 36;
	
	private Slot bestWeapon = null;
	private Slot bestBow = null;
	private Slot bestPickaxe = null;
	private Slot bestAxe = null;
	private Slot bestSpade = null;
	private Slot bestShears = null;
	
	@Override
	public void onEnable() {
		inventoryOpen = false;
	}
	
	@EventHandler
	private Handler<EventTick> onTick = e -> {
		if (c0ePosition.is("PRE") ? e.isPost() : e.isPre())
			return;
		
		if (mode.is("Inventory Open") && !(mc.currentScreen instanceof GuiInventory)) {
			return;
		}
		
		if (mode.is("No Movement") && MovementUtils.isPlayerMoving()) {
			closeInventory();
			return;
		}
		
		if (instant.isDisabled() && !delayTimer.hasTimeElapsed(delay.getValue(), false))
			return;
		
		if (adventureCheck.isEnabled() && mc.playerController.getCurrentGameType() == GameType.ADVENTURE) {
			closeInventory();
			return;
		}
		
		Container container = mc.thePlayer.inventoryContainer;
		List<Slot> slots = container.getInventorySlots();
		
		bestWeapon = InventoryUtils.getBestWeapon(slots, weaponChoice.is("Sword"), weaponSlot);
		bestBow = InventoryUtils.getBestBow(slots, bowSlot);
		bestPickaxe = InventoryUtils.getBestPickaxe(slots, pickaxeSlot);
		bestAxe = InventoryUtils.getBestAxe(slots, axeSlot);
		bestSpade = InventoryUtils.getBestSpade(slots, spadeSlot);
		bestShears = InventoryUtils.getBestShears(slots, shearsSlot);
		
		if (dropItems.isEnabled() && dropOrSort) {
			if (drop()) {
				if (inventorySpoof.is("Open & Close on Action"))
					closeInventory();
				dropOrSort = !dropOrSort;
				delayTimer.reset();
				if (instant.isDisabled())
					return;
			}
		}
		
		if (sort()) {
			if (inventorySpoof.is("Open & Close on Action"))
				closeInventory();
			dropOrSort = !dropOrSort;
			delayTimer.reset();
			if (instant.isDisabled())
				return;
		}
		
		if (dropItems.isEnabled()) {
			if (drop()) {
				if (inventorySpoof.is("Open & Close on Action"))
					closeInventory();
				dropOrSort = false;
				delayTimer.reset();
				if (instant.isDisabled())
					return;
			}
		}
		
		dropOrSort = !dropOrSort;
		closeInventory();
	};
	
	/**
	 * Opens inventory
	 */
	private void openInventory() {
		if (inventoryOpen || mode.is("Inventory Open") || mode.is("None"))
			return;
		InventoryUtils.sendOpenInventory();
	}
	
	/**
	 * Closes inventory
	 */
	private void closeInventory() {
		if (!inventoryOpen || mode.is("Inventory Open") || mode.is("None"))
			return;
		InventoryUtils.sendCloseContainer(mc.thePlayer.inventoryContainer);
	}
	
	/**
	 * Drops items
	 * @return true if it did an action, otherwise false
	 */
	private boolean drop() {
		
		return false;
	}
	
	/**
	 * Sorts items
	 * @return true if it did an action, otherwise false
	 */
	private boolean sort() {
		EntityPlayerSP thePlayer = mc.thePlayer;
		Container container = thePlayer.inventoryContainer;
		
//		final int weaponSlot = this.weaponSlot;
//		final int bowSlot = this.bowSlot;
//		final int pickaxeSlot = this.pickaxeSlot;
//		final int axeSlot = this.axeSlot;
//		final int spadeSlot = this.spadeSlot;
//		final int shearsSlot = this.shearsSlot;
//		final int gappleSlot = this.gappleSlot;
//		final int blockSlot = this.blockSlot;
		
		Slot bestWeapon = this.bestWeapon;
		Slot bestBow = this.bestBow;
		Slot bestPickaxe = this.bestPickaxe;
		Slot bestAxe = this.bestAxe;
		Slot bestSpade = this.bestSpade;
		Slot bestShears = this.bestShears;
		
		// Weapon
		if (bestWeapon != null && bestWeapon.slotNumber != weaponSlot) {
			openInventory();
			InventoryUtils.swapHotbarItems(container, weaponSlot, bestWeapon.slotNumber);
			if (instant.isDisabled())
				return true;
		}
		
		// Bow
		if (bestBow != null && bestBow.slotNumber != bowSlot) {
			openInventory();
			InventoryUtils.swapHotbarItems(container, bowSlot, bestBow.slotNumber);
			if (instant.isDisabled())
				return true;
		}
		
		// Pickaxe
		if (bestPickaxe != null && bestPickaxe.slotNumber != pickaxeSlot) {
			openInventory();
			InventoryUtils.swapHotbarItems(container, pickaxeSlot, bestPickaxe.slotNumber);
			if (instant.isDisabled())
				return true;
		}
		
		// Axe
		if (bestAxe != null && bestAxe.slotNumber != axeSlot && bestAxe.slotNumber != weaponSlot) {
			openInventory();
			InventoryUtils.swapHotbarItems(container, axeSlot, bestAxe.slotNumber);
			if (instant.isDisabled())
				return true;
		}
		
		// Spade
		if (bestSpade != null && bestSpade.slotNumber != spadeSlot) {
			openInventory();
			InventoryUtils.swapHotbarItems(container, spadeSlot, bestSpade.slotNumber);
			if (instant.isDisabled())
				return true;
		}
		
		// Shears
		if (bestShears != null && bestShears.slotNumber != shearsSlot) {
			openInventory();
			InventoryUtils.swapHotbarItems(container, shearsSlot, bestShears.slotNumber);
			if (instant.isDisabled())
				return true;
		}
		
		return false;
	}
	
}
