package cftExtractorRecode.image_set;

import ij.IJ;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import cftExtractorRecode.exceptions.UnsuportedImageException;

/**
 * @author Hugo
 * 
 */
public class Image {
	/**
	 * Absolute Image path
	 */
	private String absolutePath;
	/**
	 * What class it`s inside
	 */
	private String folder;
	/**
	 * HashMap containing attributes names and values of its extracted attributes
	 */
	private HashMap<String, String> attributes = new HashMap<String, String>();
	/**
	 * 
	 */
	private final static Logger LOGGER = Logger.getLogger(Image.class);

	/**
	 * @param absolutePath - path of the Image
	 * @param folder - the image's class/folder
	 * @throws Exception
	 */

	public Image(String absolutePath, String folder) throws Exception {
		this.absolutePath = absolutePath;
		BufferedImage image = ImageIO.read(new File(absolutePath));
		if (image == null) {
			LOGGER.info("Could not open image: " + absolutePath + " using ImageIO library. Trying again with BoofCV library");
			// image = UtilImageIO.loadImage(absolutePath);
			try {
				image = IJ.openImage(absolutePath).getBufferedImage();
			} catch (Exception e) {
				throw new UnsuportedImageException("Supplied file is not an supported image: " + absolutePath);
			}
			if (image == null) {
				throw new UnsuportedImageException("Supplied file is not an supported image: " + absolutePath);
			}
		}
		this.folder = folder;
	}

	/**
	 * @return - File object representation of the Image
	 */
	public File getFile() {
		return new File(this.absolutePath);
	}

	/**
	 * @return - the image's class/folder
	 */
	public String getFolderName() {
		if (this.folder == "") {
			LOGGER.info("Name is empty");
		}
		return this.folder;
	}

	public void addAttribute(String attributeName, String attributeValue) {
		this.attributes.put(attributeName, attributeValue);
	}

	public String getAttributeValue(String attributeName) {
		for (Entry<String, String> att : attributes.entrySet()) {
			if (att.getKey().equals(attributeName)) {
				return att.getValue();
			}
		}
		LOGGER.info("Attribute: " + attributeName + " not located");
		return null;
	}

	public String getAbsolutePath() {
		return this.absolutePath;
	}

	public void addMultipleAttributes(HashMap<String, String> atts) {
		for (Entry<String, String> entry : atts.entrySet()) {
			addAttribute(entry.getKey(), entry.getValue());
		}
	}

	public void debug() {
		LOGGER.debug("DEBUG IMAGE: " + this.absolutePath + " containing " + attributes.size() + " attributes");
	}

}
