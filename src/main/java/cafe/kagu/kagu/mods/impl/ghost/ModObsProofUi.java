/**
 * 
 */
package cafe.kagu.kagu.mods.impl.ghost;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import org.apache.commons.lang3.RandomUtils;

import cafe.kagu.kagu.eventBus.EventHandler;
import cafe.kagu.kagu.eventBus.Handler;
import cafe.kagu.kagu.eventBus.impl.EventRenderObs;
import cafe.kagu.kagu.mods.Module;
import cafe.kagu.kagu.settings.impl.IntegerSetting;
import cafe.kagu.kagu.settings.impl.LabelSetting;
import cafe.kagu.kagu.utils.ChatUtils;
import cafe.kagu.kagu.utils.OSUtil;
import javafx.geometry.Rectangle2D;

/**
 * @author DistastefulBannock
 *
 */
public class ModObsProofUi extends Module {
	
	public ModObsProofUi() {
		super("ObsProofUI", Category.GHOST);
		setSettings(offsetX, offsetY, windowsOnlyLabel);
	}
	
	private LabelSetting windowsOnlyLabel = new LabelSetting("Windows only feature").setDependency(() -> !OSUtil.isWindows());
	private IntegerSetting offsetX = new IntegerSetting("Offset X", 8, -20, 20, 1).setDependency(OSUtil::isWindows);
	private IntegerSetting offsetY = new IntegerSetting("Offset Y", 30, -40, 40, 1).setDependency(OSUtil::isWindows);
	
	@Override
	public void onEnable() {
		if (!OSUtil.isWindows()) {
			ChatUtils.addChatMessage("This is a windows only feature, sorry " + System.getProperty("os.name").toLowerCase() + " user");
			toggle();
		}
	}
	
	@EventHandler
	private Handler<EventRenderObs> onRenderObs = e -> {
		if (e.isPost())
			return;
		int x = RandomUtils.nextInt(0, mc.displayWidth) - 50;
		int y = RandomUtils.nextInt(0, mc.displayHeight) - 50;
		Graphics2D graphics2d = e.getGraphics();
		graphics2d.setStroke(new BasicStroke(0.02f));
		graphics2d.setColor(Color.PINK);
		graphics2d.fillRect(x, y, x + 100, y + 100);
	};
	
	/**
	 * @return the offsetX
	 */
	public IntegerSetting getOffsetX() {
		return offsetX;
	}
	
	/**
	 * @return the offsetY
	 */
	public IntegerSetting getOffsetY() {
		return offsetY;
	}
	
}
