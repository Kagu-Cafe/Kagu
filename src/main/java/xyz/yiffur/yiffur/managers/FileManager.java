/**
 * 
 */
package xyz.yiffur.yiffur.managers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * @author lavaflowglow
 *
 */
public class FileManager {
	
	// Directories
	public static final File 
						YIFFUR_DIR = new File("Yiffur"), 
						DEFAULTS_DIR = new File(YIFFUR_DIR, "defaults"), 
						KEYBINDS_DIR = new File(YIFFUR_DIR, "keybinds");
	
	// Files
	public static final File
						// Defaults dir
						DEFAULT_KEYBIND_SET = new File(DEFAULTS_DIR, "keybinds.json");
	
	/**
	 * Called when the client starts
	 */
	public static void start() {
		
		// Create any missing dirs
		if (!YIFFUR_DIR.exists()) {
			YIFFUR_DIR.mkdirs();
		}
		
		if (!DEFAULTS_DIR.exists()) {
			DEFAULTS_DIR.mkdirs();
		}
		
		if (!KEYBINDS_DIR.exists()) {
			KEYBINDS_DIR.mkdirs();
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
		writeBytesToFile(file, str.getBytes());
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
