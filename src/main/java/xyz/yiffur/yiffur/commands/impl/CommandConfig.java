/**
 * 
 */
package xyz.yiffur.yiffur.commands.impl;

import java.io.File;

import xyz.yiffur.yiffur.commands.Command;
import xyz.yiffur.yiffur.commands.CommandAction;
import xyz.yiffur.yiffur.managers.ConfigManager;
import xyz.yiffur.yiffur.managers.FileManager;
import xyz.yiffur.yiffur.managers.KeybindManager;
import xyz.yiffur.yiffur.utils.ChatUtils;

/**
 * @author lavaflowglow
 *
 */
public class CommandConfig extends Command {
	
	private static ActionRequirement save = new ActionRequirement((CommandAction)args -> {
		if (args.length < 1)
			return true;
		String stitchedArgs = "";
		for (String str : args) {
			stitchedArgs += (stitchedArgs.isEmpty() ? "" : " ") + str;
		}
		File file = new File(FileManager.CONFIGS_DIR, stitchedArgs + ".yiff");
		ChatUtils.addChatMessage("Saved config to \"" + file.getName() + "\"");
		ConfigManager.save(file);
		return true;
	}, "save");
	
	private static ActionRequirement load = new ActionRequirement((CommandAction)args -> {
		if (args.length < 1)
			return true;
		String stitchedArgs = "";
		for (String str : args) {
			stitchedArgs += (stitchedArgs.isEmpty() ? "" : " ") + str;
		}
		File file = new File(FileManager.CONFIGS_DIR, stitchedArgs + ".yiff");
		if (!file.exists()) {
			ChatUtils.addChatMessage("Could not find file \"" + file.getName() + "\"");
			return true;
		}
		ConfigManager.load(file);
		ConfigManager.save(FileManager.DEFAULT_CONFIG);
		ChatUtils.addChatMessage("Loaded config from \"" + file.getName() + "\"");
		return true;
	}, "load");
	
	private static ActionRequirement list = new ActionRequirement((CommandAction)args -> {
		String[] fileNames = FileManager.CONFIGS_DIR.list();
		ChatUtils.addChatMessage("Found " + fileNames.length + " config file" + (fileNames.length == 1 ? "" : "s"));
		for (String fileName : fileNames) {
			ChatUtils.addChatMessage("    " + fileName);
		}
		return true;
	}, "list");
	
	public CommandConfig() {
		super("config", "<save/load/list> <name>", save, load, list);
	}
	
}
