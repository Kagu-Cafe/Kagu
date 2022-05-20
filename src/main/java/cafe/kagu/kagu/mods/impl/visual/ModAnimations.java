/**
 * 
 */
package cafe.kagu.kagu.mods.impl.visual;

import cafe.kagu.kagu.eventBus.Subscriber;
import cafe.kagu.kagu.eventBus.EventHandler;
import cafe.kagu.kagu.eventBus.impl.EventCheatTick;
import cafe.kagu.kagu.eventBus.impl.EventRenderItem;
import cafe.kagu.kagu.eventBus.impl.EventTick;
import cafe.kagu.kagu.mods.Module;
import cafe.kagu.kagu.settings.impl.BooleanSetting;
import cafe.kagu.kagu.settings.impl.DecimalSetting;
import cafe.kagu.kagu.settings.impl.ModeSetting;
import cafe.kagu.kagu.utils.ChatUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.EnumAction;

/**
 * @author lavaflowglow
 *
 */
public class ModAnimations extends Module {

	public ModAnimations() {
		super("Animations", Category.VISUAL);
		setSettings(blockAnimations, itemScaleX, itemScaleY, itemScaleZ, itemTranslateX, itemTranslateY, itemTranslateZ);
	}
	
	public ModeSetting blockAnimations = new ModeSetting("Animation", "1.7", "1.7", "Orbit", "Spin", "Lollipop", "Slash", "None", "Test");
	
	// Item scale
	public DecimalSetting itemScaleX = new DecimalSetting("Item scale x", 1, -2, 2, 0.05);
	public DecimalSetting itemScaleY = new DecimalSetting("Item scale y", 1, -2, 2, 0.05);
	public DecimalSetting itemScaleZ = new DecimalSetting("Item scale z", 1, -2, 2, 0.05);
	
	// Item translate
	public DecimalSetting itemTranslateX = new DecimalSetting("Item translate x", 0, -2, 2, 0.001);
	public DecimalSetting itemTranslateY = new DecimalSetting("Item translate y", 0, -2, 2, 0.001);
	public DecimalSetting itemTranslateZ = new DecimalSetting("Item translate z", 0, -2, 2, 0.001);
	
	@EventHandler
	private Subscriber<EventRenderItem> renderItem = e -> {
		if (e.isPost())
			return;
		
		setInfo(blockAnimations.getMode());
		ItemRenderer ir = mc.getItemRenderer();
		
		// Scaling and translations
		GlStateManager.scale(itemScaleX.getValue(), itemScaleY.getValue(), itemScaleZ.getValue());
		GlStateManager.translate(itemTranslateX.getValue(), itemTranslateY.getValue(), itemTranslateZ.getValue());
		
		// Sword block animations
		if (!blockAnimations.is("None") && e.getAction() == EnumAction.BLOCK && mc.thePlayer.isUsingItem()) {
			e.setAction(EnumAction.CUSTOMBLOCK);
			
			switch (blockAnimations.getMode()) {
			
			case "1.7":{
				ir.transformFirstPersonItem(e.getEquipProgress(), e.getSwingProgress());
				ir.transformFirstPersonBlock();
			}break;
			
			case "Orbit":{
				GlStateManager.rotate(e.getSwingProgress() * 360, 0, 1, 0);
				ir.transformFirstPersonItem(e.getEquipProgress(), 0);
				ir.transformFirstPersonBlock();
			}break;
			
			case "Spin":{
				GlStateManager.rotate(e.getSwingProgress() * 360, 0, 0, 1);
				ir.transformFirstPersonItem(e.getEquipProgress(), 0);
				ir.transformFirstPersonBlock();
			}break;
			
			case "Lollipop":{
				ir.transformFirstPersonItem(e.getEquipProgress(), e.getSwingProgress());
				ir.transformFirstPersonBlock();
				ir.transformFirstPersonBlock();
			}break;
			
			case "Slash":{
				ir.func_178105_d(e.getSwingProgress());
				ir.transformFirstPersonItem(e.getEquipProgress(), e.getSwingProgress());
				ir.transformFirstPersonBlock();
			}break;
			
			case "Test":{
				ir.transformFirstPersonItem(e.getEquipProgress(), e.getSwingProgress());
				ir.transformFirstPersonBlock();
			}break;
			
			default:{}break;
			}
			
		}
		
	};
	
}
