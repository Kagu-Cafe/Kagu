/**
 * 
 */
package xyz.yiffur.yiffur.commands;

import xyz.yiffur.yiffur.eventBus.EventBus;
import xyz.yiffur.yiffur.eventBus.Subscriber;
import xyz.yiffur.yiffur.eventBus.YiffEvents;
import xyz.yiffur.yiffur.eventBus.impl.EventChatSendMessage;
import xyz.yiffur.yiffur.mods.ModuleManager;

/**
 * @author lavaflowglow
 *
 */
public class CommandManager {
	
	/**
	 * Called when the client starts, creates a new command manager object and hooks it onto the event bus
	 */
	public static void start() {
		EventBus.setSubscriber(new CommandManager(), true);
	}
	
	/**
	 * Handles chat messages
	 */
	@YiffEvents
	public Subscriber<EventChatSendMessage> chatEventListener = e -> {
		
		// Is the event is post than return, we only want pre events on this
		if (e.isPost()) {
			return;
		}
		
		// If the message starts with a . than it's a command, so if it doesn't than we return
		if (!e.getMessage().startsWith(".")) {
			return;
		}
		
		// Cancel the event
		e.cancel();
		
	};
	
}
