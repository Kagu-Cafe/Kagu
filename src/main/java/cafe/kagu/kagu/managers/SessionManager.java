/**
 * 
 */
package cafe.kagu.kagu.managers;

import java.net.URI;
import java.util.Map;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;
import org.yaml.snakeyaml.Yaml;

import com.mojang.authlib.Agent;
import com.mojang.authlib.AuthenticationService;
import com.mojang.authlib.UserAuthentication;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.util.UUIDTypeAdapter;

import cafe.kagu.kagu.ui.gui.GuiAltManager;
import cafe.kagu.kagu.ui.gui.GuiAltManager.MicrosoftAlt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.util.Session;
import net.minecraft.util.Session.Type;
import spark.Spark;

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
		// Start the webserver to listen for requests on port 3621, the webserver will be used for microsoft auth
		Spark.port(3621);
		
		// Used for microsoft auth
		Spark.get("/", (request, response) -> {
			
			// Get all the 
			String code = request.queryMap("code").value();
			Map<String, String> authInfo = new Yaml().load(FileManager.readStringFromFile(FileManager.MICROSOFT_CONFIGURATION));
			String clientId = authInfo.get("Azure Client Id");
			String secret = authInfo.get("Azure Secret");
			
			// Send the request
			String tokenResponse = "";
			try {
				tokenResponse = getAuthTokenResponseFromAuthCode(clientId, secret, code, false);
			} catch (Exception e) {
				return "Something went wrong, please try again";
			}
			
			// Create the microsoft alt and add it to the alt manager
			JSONObject jsonResponse = new JSONObject(tokenResponse);
			MicrosoftAlt alt = new MicrosoftAlt();
			alt.setUsername("Unknown account");
			alt.setRefreshToken(jsonResponse.getString("refresh_token"));
			GuiAltManager.getAlts().add(alt);
			GuiAltManager.saveAlts();
			
			return "Added microsoft account to the alt manager, you may close this tab";
		});
	}
	
	/**
	 * Logs into a permium minecraft account
	 * @param email The accounts email
	 * @param password The accounts password
	 * @return true if there were no issues with authorization, otherwise false
	 */
	public static boolean loginPremium(String email, String password) {
		try {
			userAuthentication.logOut();
			userAuthentication.setUsername(email);
			userAuthentication.setPassword(password);
			userAuthentication.logIn();
			Minecraft.getMinecraft().setSession(new Session(userAuthentication.getSelectedProfile().getName(), UUIDTypeAdapter.fromUUID(userAuthentication.getSelectedProfile().getId()), userAuthentication.getAuthenticatedToken(), userAuthentication.getUserType().getName()));
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
	
	/**
	 * Starts microsoft authentication
	 */
	public static void startMicrosoftAuth() {
		Map<String, String> authInfo = new Yaml().load(FileManager.readStringFromFile(FileManager.MICROSOFT_CONFIGURATION));
		String clientId = authInfo.get("Azure Client Id");
		String url = "https://login.live.com/oauth20_authorize.srf?client_id=" + clientId + "&response_type=code&redirect_uri=http://localhost:3621&scope=XboxLive.signin%20offline_access";
		try {
			Minecraft.getMinecraft().currentScreen.setClickedLinkURI(new URI(url));
		}catch (Exception e) {
			return;
		}
		Minecraft.getMinecraft().displayGuiScreen(new GuiConfirmOpenLink(Minecraft.getMinecraft().currentScreen, url, 31102009, false));
	}
	
	/**
	 * @param clientId The azure client id
	 * @param secret The azure secret
	 * @param code The code or refresh token
	 * @param refreshToken Whether the code is a refresh token or not
	 * @return The response
	 * @throws Exception If something went wrong
	 */
	private static String getAuthTokenResponseFromAuthCode(String clientId, String secret, String code, boolean refreshToken) throws Exception {
		HttpPost post = new HttpPost("https://login.live.com/oauth20_token.srf");
		String body = "client_id=" + clientId + "&client_secret=" + secret + "&code=" + code + "&grant_type=" + (refreshToken ? "refresh_token" : "authorization_code") + "&redirect_uri=http://localhost:3621";
		StringEntity entity = new StringEntity(body);
		post.setEntity(entity);
		post.setHeader("Content-Type", "application/x-www-form-urlencoded");
		return NetworkManager.getInstance().sendPost(post);
	}
	
}
