package cafe.kagu.kagu.managers;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class NetworkManager {

	private static NetworkManager instance = new NetworkManager();
	private final CloseableHttpClient httpClient = HttpClients.createDefault();

	/**
	 * @return the instance
	 */
	public static NetworkManager getInstance() {
		return instance;
	}
	
	/**
	 * Sends a get request
	 * @param request The get request
	 * @return The string returned by the web server, if the response isn't a string then it'll be gibberish
	 * @throws Exception If something goes wrong
	 */
	@SuppressWarnings("unused")
	public String sendGet(HttpGet request) throws Exception {
		request.addHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.71 Safari/537.36");
		try (CloseableHttpResponse response = httpClient.execute(request)) {
			HttpEntity entity = response.getEntity();
			Header headers = entity.getContentType();
			if (entity != null) {
				String result = EntityUtils.toString(entity);
				return result;
			} else {
				throw new Exception("An unknown error has occurred");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "An unknown error has occurred";
		}

	}
	
	/**
	 * Sends a post request
	 * @param post The post request
	 * @param args The args to put in the body of the post
	 * @return The string returned by the web server, if the response isn't a string then it'll be gibberish
	 * @throws Exception If something goes wrong
	 */
	public String sendPost(HttpPost post, BasicNameValuePair... args) throws Exception {
		post.addHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.71 Safari/537.36");
		List<NameValuePair> urlParameters = new ArrayList<>();
		for (BasicNameValuePair basicNameValuePair : args) {
			urlParameters.add(basicNameValuePair);
		}
		post.setEntity(new UrlEncodedFormEntity(urlParameters));
		CloseableHttpResponse response = httpClient.execute(post);
		String stringResponse = EntityUtils.toString(response.getEntity());
		response.close();
		return stringResponse;
	}

}