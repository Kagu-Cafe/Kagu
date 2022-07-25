/**
 * 
 */
package cafe.kagu.kagu.utils;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C0EPacketClickWindow;

/**
 * @author lavaflowglow
 * C0E documentation on wiki.vg >>> https://yiffing.zone/7834e997cd
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
		
		for (int i = 9; i < 44; i++) {
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
	 * Simulates a shift left click inside of a container
	 * @param container The container, used to get transaction id and window id
	 * @param slot The slot to click
	 */
	public static void shiftLeftClick(Container container, int slot) {
		mc.playerController.windowClick(container.windowId, slot, 0, 1, mc.thePlayer);
	}
	
}
