/**
 * 
 */
package xyz.yiffur.yiffur.commands.impl;

import org.lwjgl.input.Keyboard;

import xyz.yiffur.yiffur.commands.Command;
import xyz.yiffur.yiffur.commands.CommandAction;
import xyz.yiffur.yiffur.managers.KeybindManager;
import xyz.yiffur.yiffur.mods.Module;
import xyz.yiffur.yiffur.mods.ModuleManager;
import xyz.yiffur.yiffur.utils.ChatUtils;

/**
 * @author lavaflowglow
 *
 */
public class CommandBind extends Command {
	
	private static ActionRequirement add = new ActionRequirement((CommandAction)args -> {
		if (args.length < 2) {
			return false;
		}
		
		try {
			String moduleName = args[0];
			int keyCode = Keyboard.getKeyIndex(args[1].toUpperCase().replace(" ", "_"));
			for (Module module : ModuleManager.getModules()) {
				if (module.getName().equalsIgnoreCase(moduleName)) {
					KeybindManager.addKeybind(module.getName(), keyCode);
					ChatUtils.addChatMessage("Binded " + module.getName() + " to " + Keyboard.getKeyName(keyCode));
					return true;
				}
			}
			ChatUtils.addChatMessage("Could not find module \"" + moduleName + "\"");
			return true;
		} catch (Exception e) {
			return false;
		}
		
	}, "add");
	
	private static ActionRequirement remove = new ActionRequirement((CommandAction)args -> {
		if (args.length < 1) {
			return false;
		}
		
		try {
			String moduleName = args[0];
			for (Module module : ModuleManager.getModules()) {
				if (module.getName().equalsIgnoreCase(moduleName)) {
					KeybindManager.removeKeybind(module.getName());
					ChatUtils.addChatMessage("Cleared binds for " + module.getName());
					return true;
				}
			}
			ChatUtils.addChatMessage("Could not find module \"" + moduleName + "\"");
			return true;
		} catch (Exception e) {
			return false;
		}
		
	}, "remove");
	
	public CommandBind() {
		super("bind", "<add/remove> <module name> <keybind>", add, remove);
	}

}
