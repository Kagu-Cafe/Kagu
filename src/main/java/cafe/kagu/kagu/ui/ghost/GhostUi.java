/**
 * 
 */
package cafe.kagu.kagu.ui.ghost;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JComponent;
import javax.swing.JFrame;

import org.lwjgl.opengl.Display;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinUser;

import cafe.kagu.kagu.eventBus.EventBus;
import cafe.kagu.kagu.eventBus.EventHandler;
import cafe.kagu.kagu.eventBus.Handler;
import cafe.kagu.kagu.eventBus.Event.EventPosition;
import cafe.kagu.kagu.eventBus.impl.EventCheatRenderTick;
import cafe.kagu.kagu.eventBus.impl.EventRenderObs;
import cafe.kagu.kagu.mods.ModuleManager;
import cafe.kagu.kagu.mods.impl.ghost.ModObsProofUi;
import cafe.kagu.kagu.utils.OSUtil;
import net.minecraft.client.Minecraft;

/**
 * @author DistastefulBannock
 *
 */
public class GhostUi extends JFrame {
	
	private static final long serialVersionUID = -4192554565787800580L;
	private static Minecraft mc = Minecraft.getMinecraft();
	private static GhostUi ghostUi = null;
	
	/**
	 * Called at the start of the client
	 */
	public static void start() {
		if (!OSUtil.isWindows())
			return;
		
		ghostUi = new GhostUi();
		ghostUi.setContentPane(new DrawPane());
		
		long eventDelay = 1000 /* One second */ / 144 /* Our desired fps */;
		
		// Do our event
		new Timer("Ghost UI Event", true).scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				// Paint changes
				ghostUi.repaint();
			}
		}, 0, eventDelay);
	}
	
	public GhostUi() {
		
		// Makes the window not appear in the task bar
		setType(Type.UTILITY);
		
		// Makes the window invisible
		setUndecorated(true);
		setBackground(new Color(255, 255, 255, 0));
		
		// Makes the window always render on top of everything
		setAlwaysOnTop(true);
		
		// Make it so you cannot focus into the window
		setFocusable(false);
		
		// Sets more stuff that will be overridden later
		setSize(100, 100);
		setLocation(400, 500);
		
		// Makes window visible
		setVisible(true);
		
		// Makes the window not cancel mouse events (totally not skidded from this github project https://github.com/aeris170/Crosshair-Overlay)
		HWND handleToWindow = new HWND(); // Create a new useless window handle
		handleToWindow.setPointer(Native.getComponentPointer(this)); // Get the pointer for our jframe, then put it into our now useful window handle
		int windowLong = User32.INSTANCE.GetWindowLong(handleToWindow, WinUser.GWL_EXSTYLE); // Normally returns a dword, but that's the size of an int so we use int idfk
		windowLong = windowLong | WinUser.WS_EX_LAYERED /* Makes it layered or smth idk too lazy to read up on it */ | WinUser.WS_EX_TRANSPARENT /* Makes mouse events fall through the window */;
		User32.INSTANCE.SetWindowLong(handleToWindow, WinUser.GWL_EXSTYLE, windowLong); // Set the window long with our changes
		
		// Register this class to the event bus
		EventBus.setSubscriber(this, true);
		
	}
	
	@EventHandler
	private Handler<EventCheatRenderTick> onCheatRenderTick = e -> {
		if (e.isPost() || ghostUi == null || mc == null || !Display.isCreated())
			return;
		GhostUi ghostUi = this.ghostUi;
		ModObsProofUi modObsProofUi = ModuleManager.modObsProofUi;
		
		// If the game isn't in focus or the obs proof ui is disabled then don't render overlay
		if (!Display.isActive() || modObsProofUi.isDisabled()) {
			if (ghostUi.isVisible())
				ghostUi.setVisible(false);
			return;
		}
		
		if (!ghostUi.isVisible())
			ghostUi.setVisible(true);
		
		// Set the overlay size and position
		ghostUi.setSize(mc.displayWidth - 1, mc.displayHeight + 1);
		ghostUi.setLocation(Display.getX() + modObsProofUi.getOffsetX().getValue(), Display.getY() + modObsProofUi.getOffsetY().getValue());
		
	};
	
	private static class DrawPane extends JComponent{
		
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D graphics2d = (Graphics2D)g;
			graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			
			
			// Kagu hook
			{
				EventRenderObs eventRenderObs = new EventRenderObs(EventPosition.PRE, graphics2d);
				eventRenderObs.post();
				if (eventRenderObs.isCanceled())
					return;
			}
			
			// Kagu hook
			{
				EventRenderObs eventRenderObs = new EventRenderObs(EventPosition.POST, graphics2d);
				eventRenderObs.post();
			}
			
		}
		
	}
	
}
