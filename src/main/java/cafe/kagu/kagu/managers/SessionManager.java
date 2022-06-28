/**
 * 
 */
package cafe.kagu.kagu.managers;

import com.mojang.authlib.Agent;
import com.mojang.authlib.AuthenticationService;
import com.mojang.authlib.UserAuthentication;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import net.minecraft.util.Session.Type;

/**
 * @author lavaflowglow
 *
 */
public class SessionManager {
	
	private static UserAuthentication userAuthentication;
	
	static {
		AuthenticationService authenticationService = new YggdrasilAuthenticationService(Minecraft.getMinecraft().getProxy(), "");
		authenticationService.createMinecraftSessionService();
		userAuthentication = authenticationService.createUserAuthentication(Agent.MINECRAFT);
	}
	
	/**
	 * Called when the client starts
	 */
	public static void start() {
		
	}
	
	/**
	 * Logs into a permium minecraft account
	 * @param email The accounts email
	 * @param password The accounts password
	 * @return true if there were no issues with authorizartion, otherwise false
	 */
	public static boolean loginPremium(String email, String password) {
		
		try {
			userAuthentication.logOut();
			userAuthentication.setUsername(email);
			userAuthentication.setPassword(password);
			userAuthentication.logIn();
			return true;
		} catch (AuthenticationException e) {
			return false;
		}
		
	}
	
	/**
	 * Logs into a cracked minecraft account
	 * @param name The name to use
	 */
	public static void loginCracked(String name) {
		Minecraft.getMinecraft().setSession(new Session(name, name, "0", Type.LEGACY.toString()));
	}
	
}
