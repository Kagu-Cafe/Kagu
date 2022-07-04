/**
 * 
 */
package cafe.kagu.kagu.commands.impl;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;

import cafe.kagu.kagu.commands.Command;
import cafe.kagu.kagu.commands.CommandAction;
import cafe.kagu.kagu.managers.NetworkManager;
import cafe.kagu.kagu.utils.ChatUtils;
import net.minecraft.client.Minecraft;

/**
 * @author lavaflowglow
 *
 */
public class CommandChangeSkin extends Command {
	
	private static ActionRequirement skin = new ActionRequirement((CommandAction)args -> {
		if (args.length < 1) {
			ChatUtils.addChatMessage("Use the command correctly dumbass");
			return true;
		}
		String armType = args[0].toLowerCase().equals("slim") ? "slim" : "classic";
		String skinUrl = args.length <= 1 ? "https://i.imgur.com/YXRk8fM.png" : args[1];
		HttpPost post = new HttpPost("https://api.minecraftservices.com/minecraft/profile/skins");
		post.addHeader("Authorization", "Bearer " + Minecraft.getMinecraft().getSession().getPlayerID());
		post.addHeader("Content-Type", "application/json");
		try {
			StringEntity json = new StringEntity("{\"variant\": \"" + armType + "\", \"" + skinUrl + "\"}");
			post.setEntity(json);
		}catch(Exception e) {
			
		}
		ChatUtils.addChatMessage("Attempting to change your skin...");
		new Thread(() -> {
			try {
				String response = NetworkManager.getInstance().sendPost(post);
				ChatUtils.addChatMessage(response.isEmpty() ? "Successfully changed your skin, relog to see changes" : ("Something went wrong while changing your skin: " + response));
			} catch (Exception e) {
				ChatUtils.addChatMessage("Something went wrong while changing skins: " + e.getLocalizedMessage());
			}
		}, "Skin changer thread").start();
		return true;
	});
	
	public CommandChangeSkin() {
		super("changeskin", "<classic/slim> <url to skin>", skin);
	}

}
