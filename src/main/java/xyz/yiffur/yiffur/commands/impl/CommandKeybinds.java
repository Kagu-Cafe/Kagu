/**
 * 
 */
package xyz.yiffur.yiffur.commands.impl;

import xyz.yiffur.yiffur.commands.Command;
import xyz.yiffur.yiffur.commands.CommandAction;
import xyz.yiffur.yiffur.commands.CommandManager;
import xyz.yiffur.yiffur.utils.ChatUtils;

/**
 * @author lavaflowglow
 *
 */
public class CommandKeybinds extends Command {
	
	private static ActionRequirement save = new ActionRequirement((CommandAction)args -> {
		return false;
	}, "save");
	
	private static ActionRequirement load = new ActionRequirement((CommandAction)args -> {
		return false;
	}, "load");
	
	private static ActionRequirement list = new ActionRequirement((CommandAction)args -> {
		for (Command command : CommandManager.getCommands()) {
			ChatUtils.addChatMessage(command.getName() + " - ." + command.getName() + " " + command.getUsage());
		}
		return true;
	}, "list");
	
	public CommandKeybinds() {
		super("keybinds", "save/load/list name", save, load, list);
	}
	
}
