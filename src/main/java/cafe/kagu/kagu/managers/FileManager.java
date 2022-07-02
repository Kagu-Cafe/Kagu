/**
 * 
 */
package cafe.kagu.kagu.managers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author lavaflowglow
 *
 */
public class FileManager {
	
	// Directories
	public static final File 
						KAGU_DIR = new File("Kagu"),
						DEFAULTS_DIR = new File(KAGU_DIR, "defaults"),
						ASSETS_DIR = new File(KAGU_DIR, "assets"),
						KEYBINDS_DIR = new File(KAGU_DIR, "keybinds"),
						CONFIGS_DIR = new File(KAGU_DIR, "configs");
	
	// Files
	public static final File
	
						// Defaults
						DEFAULT_KEYBINDS = new File(DEFAULTS_DIR, "keybinds.kagu"),
						DEFAULT_CONFIG = new File(DEFAULTS_DIR, "config.kagu"),
						
						// Assets
						BACKGROUND_SHADER = new File(ASSETS_DIR, "background.frag"),
						COLOR_SHADER = new File(ASSETS_DIR, "color.frag"),
						COLOR_TEXTURE_FRAG_SHADER = new File(ASSETS_DIR, "colorTexture.fs"),
						COLOR_TEXTURE_VERT_SHADER = new File(ASSETS_DIR, "colorTexture.vs");
	
	// Byte buffer size
	private static final int BYTE_BUFFER_SIZE = 8192;
	private static Logger logger = LogManager.getLogger();
	
	/**
	 * Called when the client starts
	 */
	public static void start() {
		
		// Create any missing dirs
		if (!KAGU_DIR.exists()) {
			KAGU_DIR.mkdirs();
		}
		
		if (!DEFAULTS_DIR.exists()) {
			DEFAULTS_DIR.mkdirs();
		}
		
		if (!KEYBINDS_DIR.exists()) {
			KEYBINDS_DIR.mkdirs();
		}
		
		if (!CONFIGS_DIR.exists()) {
			CONFIGS_DIR.mkdirs();
		}
		
		if (!ASSETS_DIR.exists()) {
			ASSETS_DIR.mkdirs();
		}
		
		// Create any missing files
		if (!BACKGROUND_SHADER.exists())
			try {
				downloadFileFromUrl(BACKGROUND_SHADER, "https://raw.githubusercontent.com/Kagu-Cafe/assets-repo/main/background.frag");
			} catch (Exception e) {
				logger.error("Failed to download the main menu background shader, this may cause issues", e);
			}
		if (!COLOR_SHADER.exists())
			try {
				downloadFileFromUrl(COLOR_SHADER, "https://raw.githubusercontent.com/Kagu-Cafe/assets-repo/main/color.frag");
			} catch (Exception e) {
				logger.error("Failed to download the color shader, this may cause issues", e);
			}
		
	}
	
	/**
	 * Reads a file and returns a string
	 * @param file The file to read
	 * @return The string stored in the file
	 */
	public static String readStringFromFile(File file) {
		return new String(readBytesFromFile(file), StandardCharsets.UTF_8);
	}
	
	/**
	 * Reads a file and returns it's bytes
	 * @param file The file to read
	 * @return The bytes that make up the file
	 */
	public static byte[] readBytesFromFile(File file) {
		FileInputStream fis = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			fis = new FileInputStream(file);
			byte[] byteBuffer = new byte[BYTE_BUFFER_SIZE];
			int n;
			while ((n = fis.read(byteBuffer)) != -1) {
				baos.write(byteBuffer, 0, n);
			}
			fis.close();
			baos.close();
			return baos.toByteArray();
		} catch (Exception e) {
			try {
				fis.close();
			} catch (Exception e2) {}
			try {
				baos.close();
			} catch (Exception e2) {}
		}
		return new byte[0];
	}
	
	/**
	 * Writes a string to a file
	 * @param file The file to write to
	 * @param str The string to write
	 */
	public static void writeStringToFile(File file, String str) {
		writeBytesToFile(file, str.getBytes(StandardCharsets.UTF_8));
	}
	
	/**
	 * Writes an array of bytes to a file
	 * @param file The file to write to
	 * @param bytes The bytes to write
	 */
	public static void writeBytesToFile(File file, byte... bytes) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			fos.write(bytes);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			try {
				fos.close();
			} catch (Exception e2) {
				
			}
		}
	}
	
	/**
	 * Downloads some bytes from a url and stores them in a file
	 * @param file The file to store the data to
	 * @param url The url to get the data from
	 * @throws IOException When a connection to this url fails
	 */
	public static void downloadFileFromUrl(File file, String url) throws IOException {
		InputStream in = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			in = new URL(url).openStream();
			byte[] byteBuffer = new byte[BYTE_BUFFER_SIZE];
			int n;
			while ((n = in.read(byteBuffer)) != -1) {
				baos.write(byteBuffer, 0, n);
			}
			in.close();
			baos.close();
			writeBytesToFile(file, baos.toByteArray());
		} catch (IOException e) {
			try {
				in.close();
			} catch (Exception e2) {}
			try {
				baos.close();
			} catch (Exception e2) {}
			throw e;
		}
	}
	
}
