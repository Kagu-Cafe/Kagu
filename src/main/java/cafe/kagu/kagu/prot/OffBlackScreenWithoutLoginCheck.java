/**
 * 
 */
package cafe.kagu.kagu.prot;

import java.util.Timer;
import java.util.TimerTask;

import cafe.kagu.kagu.Kagu;
import cafe.kagu.kagu.prot.ui.GuiBlackScreen;
import net.minecraft.client.Minecraft;

/**
 * @author DistastefulBannock
 *
 */
public class OffBlackScreenWithoutLoginCheck {
	
	/**
	 * Called at cheat start
	 */
	public static void start() {
		new Timer().schedule(new TimerTask() {
			private int flags = 0;
			@Override
			public void run() {
				if (!Kagu.isLoggedIn() && !(Minecraft.getMinecraft().getCurrentScreen() instanceof GuiBlackScreen)) {
					Minecraft.getMinecraft().displayGuiScreen(new GuiBlackScreen());
					flags++;
					if (flags > 10) {
						System.exit(Note.GUISCREEN_CHANGING_WITHOUT_LOGIN);
					}
				}
			}
		}, 0, 250);
	}
	
}
