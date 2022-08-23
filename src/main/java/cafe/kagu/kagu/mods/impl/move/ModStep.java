/**
 * 
 */
package cafe.kagu.kagu.mods.impl.move;

import cafe.kagu.kagu.eventBus.EventHandler;
import cafe.kagu.kagu.eventBus.Handler;
import cafe.kagu.kagu.eventBus.impl.EventPlayerUpdate;
import cafe.kagu.kagu.mods.Module;
import cafe.kagu.kagu.settings.impl.ModeSetting;
import cafe.kagu.kagu.utils.ChatUtils;
import cafe.kagu.kagu.utils.MovementUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

/**
 * @author DistastefulBannock
 *
 */
public class ModStep extends Module {
	
	public ModStep() {
		super("Step", Category.MOVEMENT);
		setSettings(mode);
	}
	
	private ModeSetting mode = new ModeSetting("Mode", "Vanilla", "Vanilla", "0.38 Motion", "Test");
	
	@EventHandler
	private Handler<EventPlayerUpdate> onPlayerUpdate = e -> {
		if (e.isPost())
			return;
		setInfo(mode.getMode());
		
		EntityPlayerSP thePlayer = mc.thePlayer;
		switch (mode.getMode()) {
			case "Vanilla":{
				if (MovementUtils.canStep(1) && MovementUtils.isTrueOnGround()) {
					thePlayer.setPosition(thePlayer.posX, thePlayer.posY + 1, thePlayer.posZ);
				}
			}break;
			case "0.38 Motion":{
				if (MovementUtils.canStep(1.1) && !MovementUtils.canStep(0.6) && MovementUtils.isTrueOnGround()) {
					thePlayer.motionY = 0.38;
				}
			}break;
			case "Test":{
				if (thePlayer.motionY != -0.0784000015258789 && !MovementUtils.isTrueOnGround())
					ChatUtils.addChatMessage(thePlayer.motionY);
				if (MovementUtils.canStep(1) && MovementUtils.isTrueOnGround()) {
					thePlayer.jump();
					Minecraft.getMinecraft().thePlayer.motionY += 0.05;
					if (!MovementUtils.isTrueOnGround(0.2))
						thePlayer.motionY -= 0.05;
				}
			}break;
		}
	};
	
}
