/**
 * 
 */
package cafe.kagu.kagu.mods.impl.player;

import cafe.kagu.kagu.eventBus.EventHandler;
import cafe.kagu.kagu.eventBus.Handler;
import cafe.kagu.kagu.eventBus.impl.EventPlayerUpdate;
import cafe.kagu.kagu.eventBus.impl.EventTick;
import cafe.kagu.kagu.mods.Module;
import cafe.kagu.kagu.settings.impl.ModeSetting;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C03PacketPlayer;

/**
 * @author DistastefulBannock
 *
 */
public class ModNoFall extends Module {
	
	public ModNoFall() {
		super("NoFall", Category.PLAYER);
		setSettings(mode);
	}
	
	private ModeSetting mode = new ModeSetting("Mode", "Hypixel", "Hypixel", "Always On Ground", "Always Off Ground", "Packet", "Test");
	
	@EventHandler
	private Handler<EventTick> onTick = e ->{
		if (e.isPost())
			return;
		setInfo(mode.getMode());
	};
	
	@EventHandler
	private Handler<EventPlayerUpdate> onPlayerUpdate = e -> {
		if (e.isPost())
			return;
		EntityPlayerSP thePlayer = mc.thePlayer;
		switch (mode.getMode()) {
			case "Hypixel":{
				if (thePlayer.fallDistance > 2) {
					mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C03PacketPlayer(true));
					thePlayer.fallDistance = 0;
				}
			}break;
			case "Always On Ground":{
				e.setOnGround(true);
			}break;
			case "Always Off Ground":{
				e.setOnGround(false);
			}break;
			case "Packet":{
				if (thePlayer.fallDistance > 2) {
					mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C03PacketPlayer(true));
				}
			}break;
			case "Test":{
				
			}break;
		}
	};
	
}
