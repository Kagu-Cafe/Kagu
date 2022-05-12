/**
 * 
 */
package xyz.yiffur.yiffur.commands.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C01PacketChatMessage;
import xyz.yiffur.yiffur.commands.Command;
import xyz.yiffur.yiffur.commands.CommandAction;

/**
 * @author lavaflowglow
 *
 */
public class CommandSay extends Command {
	
	private static ActionRequirement say = new ActionRequirement((CommandAction)args -> {
		String message = "";
		for (String str : args) {
			message += (message.length() == 0 ? "" : " ") + str;
		}
		Minecraft.getMinecraft().getNetHandler().getNetworkManager().sendPacketNoEvent(new C01PacketChatMessage(message));
		return true;
	});
	
	public CommandSay() {
		super("say", "", say);
	}
	
}
