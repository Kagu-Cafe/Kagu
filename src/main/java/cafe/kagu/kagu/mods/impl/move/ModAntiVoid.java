/**
 * 
 */
package cafe.kagu.kagu.mods.impl.move;

import java.util.LinkedList;
import java.util.Queue;

import cafe.kagu.kagu.eventBus.EventHandler;
import cafe.kagu.kagu.eventBus.Handler;
import cafe.kagu.kagu.eventBus.impl.EventChatSendMessage;
import cafe.kagu.kagu.eventBus.impl.EventCheatProcessTick;
import cafe.kagu.kagu.eventBus.impl.EventPacketSend;
import cafe.kagu.kagu.eventBus.impl.EventPlayerUpdate;
import cafe.kagu.kagu.mods.Module;
import cafe.kagu.kagu.mods.ModuleManager;
import cafe.kagu.kagu.mods.impl.exploit.ModBlink;
import cafe.kagu.kagu.mods.impl.visual.ModTargetHud;
import cafe.kagu.kagu.settings.impl.DoubleSetting;
import cafe.kagu.kagu.settings.impl.IntegerSetting;
import cafe.kagu.kagu.settings.impl.ModeSetting;
import cafe.kagu.kagu.utils.MovementUtils;
import cafe.kagu.kagu.utils.TimerUtil;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.Packet;

/**
 * @author DistastefulBannock
 *
 */
public class ModAntiVoid extends Module {
	
	public ModAntiVoid() {
		super("AntiVoid", Category.MOVEMENT);
		setSettings(mode, fallDistanceSetting, cooldown);
	}
	
	// Settings
	private ModeSetting mode = new ModeSetting("Mode", "Blink", "Blink");
	private DoubleSetting fallDistanceSetting = new DoubleSetting("Fall Distance", 6, 2, 20, 0.5);
	private IntegerSetting cooldown = new IntegerSetting("Cooldown", 250, 0, 2500, 50);
	
	// Vars
	private boolean isOverVoid = false;
	private double[] lastSafePosition = new double[6];
	private Queue<Packet<?>> blinkPackets = new LinkedList<>();
	private double fallDistance = 0;
	private TimerUtil cooldownTimer = new TimerUtil();
	
	@Override
	public void onEnable() {
		isOverVoid = MovementUtils.isOverVoid();
		EntityPlayerSP thePlayer = mc.thePlayer;
		lastSafePosition[0] = thePlayer.posX;
		lastSafePosition[1] = thePlayer.posY;
		lastSafePosition[2] = thePlayer.posZ;
		lastSafePosition[3] = thePlayer.motionX;
		lastSafePosition[4] = thePlayer.motionY;
		lastSafePosition[5] = thePlayer.motionZ;
		fallDistance = 0;
		blinkPackets.clear();
	}
	
	@EventHandler
	private Handler<EventPlayerUpdate> onPlayerUpdate = e -> {
		if (e.isPost())
			return;
		
		// Vars
		EntityPlayerSP thePlayer = mc.thePlayer;
		boolean isOverVoid = this.isOverVoid;
		
		// Fall distance calculations
		if (MovementUtils.isTrueOnGround()) {
			fallDistance = 0;
		}
		else if (thePlayer.posY < thePlayer.lastTickPosY) {
			fallDistance += thePlayer.lastTickPosY - thePlayer.posY;
		}
		
		// Info
		setInfo(mode.getMode(), ModTargetHud.DECIMAL_FORMAT.format(fallDistance) + "/" + ModTargetHud.DECIMAL_FORMAT.format(fallDistanceSetting.getValue()));
		
		// Lag back if needed
		if(isOverVoid && fallDistance >= fallDistanceSetting.getValue()) {
			switch (mode.getMode()) {
				case "Blink":{
					cooldownTimer.reset();
					ModBlink modBlink = ModuleManager.modBlink;
					if (modBlink.isEnabled()) {
						modBlink.getSentPackets().removeAll(blinkPackets);
					}
					blinkPackets.clear();
					thePlayer.setPosition(lastSafePosition[0], lastSafePosition[1], lastSafePosition[2]);
					thePlayer.setMotion(lastSafePosition[3], lastSafePosition[4], lastSafePosition[5]);
				}break;
			}
		}
		
		if (!isOverVoid) {
			blinkPackets.iterator().forEachRemaining(mc.getNetHandler().getNetworkManager()::sendPacket);
			blinkPackets.clear();
		}
		
	};
	
	@EventHandler
	private Handler<EventPacketSend> onPacketSend = e -> {
		if (e.isPost() || mc.isSingleplayer())
			return;
		boolean isOverVoid = this.isOverVoid;
		switch (mode.getMode()) {
			case "Blink":{
				if (isOverVoid) {
					if (blinkPackets.offer(e.getPacket()))
						e.cancel();
				}
			}break;
		}
	};
	
	@EventHandler
	private Handler<EventCheatProcessTick> onCheatProcessTick = e -> {
		if (e.isPost())
			return;
		EntityPlayerSP thePlayer = mc.thePlayer;
		isOverVoid = MovementUtils.isOverVoid() && cooldownTimer.hasTimeElapsed(cooldown.getValue());
		if (!isOverVoid) {
			lastSafePosition[0] = thePlayer.posX;
			lastSafePosition[1] = thePlayer.posY;
			lastSafePosition[2] = thePlayer.posZ;
			lastSafePosition[3] = thePlayer.motionX;
			lastSafePosition[4] = thePlayer.motionY;
			lastSafePosition[5] = thePlayer.motionZ;
		}
	};
	
}
