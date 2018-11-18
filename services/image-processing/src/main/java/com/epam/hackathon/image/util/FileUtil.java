package com.epam.hackathon.image.util;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static java.nio.file.Files.readAllBytes;

public class FileUtil {
	
	private static Logger logger=Logger.getLogger(FileUtil.class.getName());
	
	private static char fileSeparator='/';
	//private static ResourceBundle resourceBundle=ResourceBundle.getBundle("data");
	
	/***
	 * Loads an input image and converts it to ByteBuffer object
	 * @param imagePath absolute path of the input image
	 * @return returns image in ByteBuffer object form
	 */
	public static ByteBuffer loadImage(String imagePath) {
		byte[] bytes;
		ByteBuffer theByteBuffer=ByteBuffer.allocate(0);
		try {
			bytes = readAllBytes(Paths.get(imagePath));
			theByteBuffer=ByteBuffer.wrap(bytes);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return theByteBuffer;
	}
	
	/***
	 * Create a list of all the image files and returns the list.
	 * @param folderLocation location where all the images are stored
	 * @return list of absolute paths of the image files.
	 */
	public static List<String> getAllImagesFromFolder(String folderLocation){
		List<String> allImages=new ArrayList<String>();
		File folder=new File(folderLocation);
		File[] allFiles=folder.listFiles();
		for(File file:allFiles) {
			if(file.isFile()) {
				try {
					if(ImageIO.read(file)!=null) {
						if(isJpegImage(file)) {
							allImages.add(folderLocation+fileSeparator+file.getName());
						}else {
							logger.warning(file.getName()+" will be ignored as its not a jpeg image file.");
						}
						
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		
		return allImages;
	}

	/***
	 * Check if the input file is an jpeg or not
	 * @param file INput image file
	 * @return true or false depending on whether its an jpeg image or not.
	 */
	private static boolean isJpegImage(File file) {
		return file.getName().endsWith(".jpg");
	}

//	/***
//	 * This method fetches the value of the given key from the resource bundle defined in this class.
//	 * @param key input key
//	 * @return value of the given key.
//	 */
//	public static String getValueFromResourceBundle(String key) {
//		if(key==null) {
//			return "";
//		}
//		String value=resourceBundle.getString(key);
//		return value;
//	}
	
	public static char getFileSeparator() {
		return fileSeparator;
	}
	
	public static String getFileNameWithoutExtensionFromFilePath(String filePath) {
		//Create File object to get filename.
		File lostFile=new File(filePath);
		String lostImagefileName=lostFile.getName();

		//Remove extension from filename
		String lostImageFileNameWithoutExtension=lostImagefileName.substring(0, lostImagefileName.indexOf(".jpg"));
		
		return lostImageFileNameWithoutExtension;
	}
	
}
