/**
 * 
 */
package cafe.kagu.kagu.mods.impl.ghost;

import cafe.kagu.kagu.eventBus.EventHandler;
import cafe.kagu.kagu.eventBus.Handler;
import cafe.kagu.kagu.eventBus.impl.EventTick;
import cafe.kagu.kagu.mods.Module;
import cafe.kagu.kagu.settings.impl.BooleanSetting;
import cafe.kagu.kagu.settings.impl.DoubleSetting;
import cafe.kagu.kagu.settings.impl.LabelSetting;
import cafe.kagu.kagu.settings.impl.ModeSetting;
import net.minecraft.entity.EntityLivingBase;

/**
 * @author DistastefulBannock
 *
 */
public class ModAutoRod extends Module {
	
	public ModAutoRod() {
		super("AutoRod", Category.GHOST);
		setSettings(labelSetting, maxDistance, mode, afterAttack);
		// TODO: Finish module
	}
	
	private LabelSetting labelSetting = new LabelSetting("Switchs to and uses rod");
	private DoubleSetting maxDistance = new DoubleSetting("Max Distance", 6, 3, 7, 0.1);
	private ModeSetting mode = new ModeSetting("Mode", "Legit", "Legit");
	private BooleanSetting afterAttack = new BooleanSetting("After Attack", true);
	
	private EntityLivingBase target = null;
	
	@Override
	public void onEnable() {
		target = null;
	}
	
	@Override
	public void onDisable() {
		target = null;
	}
	
	@EventHandler
	private Handler<EventTick> onTick = e -> {
		if (e.isPost())
			return;
		
	};
	
}
