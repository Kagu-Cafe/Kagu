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
public class CommandHelp extends Command {
	
	private static ActionRequirement help = new ActionRequirement((CommandAction)args -> {
		ChatUtils.addChatMessage("Commands: ");
		for (Command command : CommandManager.getCommands()) {
			ChatUtils.addChatMessage("    " + command.getName() + " - ." + command.getName() + " " + command.getUsage());
		}
		return true;
	});
	
	public CommandHelp() {
		super("help", "", help);
	}
	
}
