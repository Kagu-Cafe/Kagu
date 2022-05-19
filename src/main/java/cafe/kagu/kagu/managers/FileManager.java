/**
 * 
 */
package cafe.kagu.kagu.managers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author lavaflowglow
 *
 */
public class FileManager {
	
	// Directories
	public static final File 
						KAGU_DIR = new File("Kagu"), 
						DEFAULTS_DIR = new File(KAGU_DIR, "defaults"), 
						KEYBINDS_DIR = new File(KAGU_DIR, "keybinds"),
						CONFIGS_DIR = new File(KAGU_DIR, "configs");
	
	// Files
	public static final File
						
						// Defaults
						DEFAULT_KEYBINDS = new File(DEFAULTS_DIR, "keybinds.kagu"),
						DEFAULT_CONFIG = new File(DEFAULTS_DIR, "config.kagu");
	
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
		
		// Create any missing files
		
	}
	
	/**
	 * Reads a file and returns a string
	 * @param file The file to read
	 * @return The string stored in the file
	 */
	public static String readStringFromFile(File file) {
		return new String(readBytesFromFile(file));
	}
	
	/**
	 * Reads a file and returns it's bytes
	 * @param file The file to read
	 * @return The bytes that make up the file
	 */
	public static byte[] readBytesFromFile(File file) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			int available = fis.available();
			byte[] bytes = new byte[available];
			fis.read(bytes);
			fis.close();
			return bytes;
		} catch (Exception e) {
			try {
				fis.close();
			} catch (Exception e2) {
				
			}
		}
		return new byte[0];
	}
	
	/**
	 * Writes a string to a file
	 * @param file The file to write to
	 * @param str The string to write
	 */
	public static void writeStringToFile(File file, String str) {
		writeBytesToFile(file, str.getBytes(StandardCharsets.UTF_16));
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
	
}
