/**
 * 
 */
package cafe.kagu.kagu.utils;

import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentDamage;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0BPacketEntityAction.Action;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C0EPacketClickWindow;

/**
 * @author lavaflowglow
 * C0E documentation on wiki.vg >>> https://yiffing.zone/7834e997cd
 * Inventory slots numbered >>> https://wiki.vg/images/1/13/Inventory-slots.png
 */
public class InventoryUtils {
	
	private static Minecraft mc = Minecraft.getMinecraft();
	
	/**
	 * Called at the start of the client
	 */
	public static void start() {
		
	}
	
	/**
	 * @return true if the players inventory is full, otherwise false
	 */
	public static boolean isPlayerInventoryFull() {
		EntityPlayerSP thePlayer = mc.thePlayer;
		List<Slot> slots = thePlayer.inventoryContainer.getInventorySlots();
		
		for (int i = 9; i < 45; i++) {
			try {
				if (!slots.get(i).getHasStack())
					return false;
			} catch (Exception e) {
				
			}
		}
		
		return true;
	}
	
	/**
	 * Sends a close packet for a container
	 * @param container The container to close
	 */
	public static void sendCloseContainer(Container container) {
		mc.getNetHandler().getNetworkManager().sendPacket(new C0DPacketCloseWindow(container.windowId));
	}
	
	/**
	 * Sends an open packet for the inventory
	 */
	public static void sendOpenInventory() {
		mc.getNetHandler().getNetworkManager().sendPacket(new C0BPacketEntityAction(mc.thePlayer, Action.OPEN_INVENTORY));
	}
	
	/**
	 * Simulates a shift left click inside of a container
	 * @param container The container, used to get transaction id and window id
	 * @param slot The slot to click
	 */
	public static void shiftLeftClick(Container container, int slot) {
		mc.playerController.windowClick(container.windowId, slot, 0, 1, mc.thePlayer);
	}
	
	/**
	 * Swaps two items between the inventory and the hotbar
	 * @param container The container
	 * @param hotbarSlot The first slot
	 * @param slot The inventory slot
	 */
	public static void swapHotbarItems(Container container, int hotbarSlot, int slot) {
		if (hotbarSlot > 35)
			hotbarSlot -= 36;
		mc.playerController.windowClick(container.windowId, slot, hotbarSlot, 2, mc.thePlayer);
	}
	
	/**
	 * Gets the best weapon from a list of slots and returns it
	 * @param slots The slots to search
	 * @param sword Set to true if it should only use swords, if it's false it'll search for axes
	 * @param firstCheck The first slot number to check, use -1 to ignore
	 * @return The slot with the best weapon, null if nothing is found
	 */
	public static Slot getBestWeapon(List<Slot> slots, boolean sword, int firstCheck) {
		Slot bestWeapon = null;
		double bestScore = Double.MIN_VALUE;
		boolean firstChecked = false;
		for (int i = firstCheck; i >= 9 || !firstChecked; i--) {
			if (i < 0) {
				i = slots.size();
				firstChecked = true;
				continue;
			}
			Slot slot = slots.get(i);
			if (!slot.getHasStack() || !(sword ? slot.getStack().getItem() instanceof ItemSword : slot.getStack().getItem() instanceof ItemAxe)) {
				if (!firstChecked) {
					i = slots.size();
					firstChecked = true;
				}
				continue;
			}
			
			// Score weapon
			ItemStack stack = slot.getStack();
			double score = (sword ? ((ItemSword)stack.getItem()).getAttackDamage() : ((ItemTool)stack.getItem()).getDamageVsEntity())
					+ EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.getEffectId(), stack) * 1.25
					+ EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.getEffectId(), stack) * 0.75
					+ EnchantmentHelper.getEnchantmentLevel(Enchantment.knockback.getEffectId(), stack) * 0.65;
			if (score > bestScore) {
				bestScore = score;
				bestWeapon = slot;
			}
			if (!firstChecked) {
				i = slots.size();
				firstChecked = true;
			}
		}
		return bestWeapon;
	}
	
	/**
	 * Gets the best bow from a list of slots and returns it
	 * @param slots The slots to search
	 * @param firstCheck The first slot number to check, use -1 to ignore
	 * @return The slot with the best bow, null if nothing is found
	 */
	public static Slot getBestBow(List<Slot> slots, int firstCheck) {
		Slot bestBow = null;
		double bestScore = Double.MIN_VALUE;
		boolean firstChecked = false;
		for (int i = firstCheck; i >= 9 || !firstChecked; i--) {
			if (i < 0) {
				i = slots.size();
				firstChecked = true;
				continue;
			}
			Slot slot = slots.get(i);
			if (!slot.getHasStack() || !(slot.getStack().getItem() instanceof ItemBow)) {
				if (!firstChecked) {
					i = slots.size();
					firstChecked = true;
				}
				continue;
			}
			
			// Score weapon
			ItemStack stack = slot.getStack();
			double score = (EnchantmentHelper.getEnchantmentLevel(Enchantment.power.getEffectId(), stack)* 0.5D + 0.5D)
					+ EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.getEffectId(), stack) * 0.75;
			if (score > bestScore) {
				bestScore = score;
				bestBow = slot;
			}
			if (!firstChecked) {
				i = slots.size();
				firstChecked = true;
			}
		}
		return bestBow;
	}
	
	/**
	 * @param stack The stack to test
	 * @return The score that the enchantments add to the tool
	 */
	public static double scoreToolEnchantments(ItemStack stack) {
		return EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.getEffectId(), stack)
				+ EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.getEffectId(), stack) * 0.3
				- EnchantmentHelper.getEnchantmentLevel(Enchantment.silkTouch.getEffectId(), stack) * 0.25;
	}
	
	/**
	 * Gets the best pickaxe from a list of slots and returns it
	 * @param slots The slots to search
	 * @param firstCheck The first slot number to check, use -1 to ignore
	 * @return The slot with the best pickaxe, null if nothing is found
	 */
	public static Slot getBestPickaxe(List<Slot> slots, int firstCheck) {
		Slot bestPickaxe = null;
		double bestScore = Double.MIN_VALUE;
		boolean firstChecked = false;
		for (int i = firstCheck; i >= 9 || !firstChecked; i--) {
			if (i < 0) {
				i = slots.size();
				firstChecked = true;
				continue;
			}
			Slot slot = slots.get(i);
			if (!slot.getHasStack() || !(slot.getStack().getItem() instanceof ItemPickaxe)) {
				if (!firstChecked) {
					i = slots.size();
					firstChecked = true;
				}
				continue;
			}
			
			// Score weapon
			ItemStack stack = slot.getStack();
			ItemTool tool = (ItemTool)stack.getItem();
			double score = tool.getStrVsBlock(stack, Blocks.stone) + scoreToolEnchantments(stack);
			if (tool.getToolMaterial() == ToolMaterial.GOLD)
				score -= 6.0001;
			if (score > bestScore) {
				bestScore = score;
				bestPickaxe = slot;
			}
			if (!firstChecked) {
				i = slots.size();
				firstChecked = true;
			}
		}
		return bestPickaxe;
	}
	
	/**
	 * Gets the best axe from a list of slots and returns it
	 * @param slots The slots to search
	 * @param firstCheck The first slot number to check, use -1 to ignore
	 * @return The slot with the best axe, null if nothing is found
	 */
	public static Slot getBestAxe(List<Slot> slots, int firstCheck) {
		Slot bestAxe = null;
		double bestScore = Double.MIN_VALUE;
		boolean firstChecked = false;
		for (int i = firstCheck; i >= 9 || !firstChecked; i--) {
			if (i < 0) {
				i = slots.size();
				firstChecked = true;
				continue;
			}
			Slot slot = slots.get(i);
			if (!slot.getHasStack() || !(slot.getStack().getItem() instanceof ItemAxe)) {
				if (!firstChecked) {
					i = slots.size();
					firstChecked = true;
				}
				continue;
			}
			
			// Score weapon
			ItemStack stack = slot.getStack();
			ItemTool tool = (ItemTool)stack.getItem();
			double score = tool.getStrVsBlock(stack, Blocks.planks) + scoreToolEnchantments(stack);
			if (tool.getToolMaterial() == ToolMaterial.GOLD)
				score -= 6.0001;
			if (score > bestScore) {
				bestScore = score;
				bestAxe = slot;
			}
			if (!firstChecked) {
				i = slots.size();
				firstChecked = true;
			}
		}
		return bestAxe;
	}
	
	/**
	 * Gets the best spade from a list of slots and returns it
	 * @param slots The slots to search
	 * @param firstCheck The first slot number to check, use -1 to ignore
	 * @return The slot with the best pickaze, null if nothing is found
	 */
	public static Slot getBestSpade(List<Slot> slots, int firstCheck) {
		Slot bestSpade = null;
		double bestScore = Double.MIN_VALUE;
		boolean firstChecked = false;
		for (int i = firstCheck; i >= 9 || !firstChecked; i--) {
			if (i < 0) {
				i = slots.size();
				firstChecked = true;
				continue;
			}
			Slot slot = slots.get(i);
			if (!slot.getHasStack() || !(slot.getStack().getItem() instanceof ItemSpade)) {
				if (!firstChecked) {
					i = slots.size();
					firstChecked = true;
				}
				continue;
			}
			
			// Score weapon
			ItemStack stack = slot.getStack();
			ItemTool tool = (ItemTool)stack.getItem();
			double score = tool.getStrVsBlock(stack, Blocks.dirt) + scoreToolEnchantments(stack);
			if (tool.getToolMaterial() == ToolMaterial.GOLD)
				score -= 6.0001;
			if (score > bestScore) {
				bestScore = score;
				bestSpade = slot;
			}
			if (!firstChecked) {
				i = slots.size();
				firstChecked = true;
			}
		}
		return bestSpade;
	}
	
	/**
	 * Gets the best shears from a list of slots and returns it
	 * @param slots The slots to search
	 * @param firstCheck The first slot number to check, use -1 to ignore
	 * @return The slot with the best shears, null if nothing is found
	 */
	public static Slot getBestShears(List<Slot> slots, int firstCheck) {
		Slot bestShears = null;
		double bestScore = Double.MIN_VALUE;
		boolean firstChecked = false;
		for (int i = firstCheck; i >= 9 || !firstChecked; i--) {
			if (i < 0) {
				i = slots.size();
				firstChecked = true;
				continue;
			}
			Slot slot = slots.get(i);
			if (!slot.getHasStack() || !(slot.getStack().getItem() instanceof ItemShears)) {
				if (!firstChecked) {
					i = slots.size();
					firstChecked = true;
				}
				continue;
			}
			
			// Score weapon
			ItemStack stack = slot.getStack();
			ItemShears shears = (ItemShears)stack.getItem();
			double score = shears.getStrVsBlock(stack, Blocks.wool) + scoreToolEnchantments(stack);
			if (score > bestScore) {
				bestScore = score;
				bestShears = slot;
			}
			if (!firstChecked) {
				i = slots.size();
				firstChecked = true;
			}
		}
		return bestShears;
	}
	
}
