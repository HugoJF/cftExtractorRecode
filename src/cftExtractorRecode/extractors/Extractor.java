package cftExtractorRecode.extractors;

import ij.ImagePlus;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Abstract class that defines how the extractors should behave
 * @author victorjussiani
 */
public abstract class Extractor {

	private static Logger LOGGER = Logger.getLogger(Extractor.class);
	protected ImagePlus image;
	protected HashMap<String, String> attributes = new HashMap<String, String>(); 
	/**
	 * Returns all possible attributes that the extractor can extract
	 * @return
	 */
	public abstract HashSet<String> getAtributtesNames();

	/**
	 * Performs attributes extraction
	 */
	public abstract void extractAttributes();

	/**
	 * Set the image into the extractor to extract attributes
	 * @param image
	 */
	public void setImage(ImagePlus image) {
		this.image = image;
		extractAttributes();
	}

	/**
	 * Method responsible for calling the extractors for the configured attributes in the configuration file
	 * @param configuration - Configuration containing the attributes for extraction
	 * @param attributes - Where the attributes calculated will be set
	 */
	public void extract(List<String> attributesNames, List<Double> attributes) {
		Method method;

		for (String attribute : getAtributtesNames()) {
			if (attributesNames.contains(attribute)) {
				try {
					method = this.getClass().getDeclaredMethod("extract".concat(attribute), new Class[] {});
					method.setAccessible(Boolean.TRUE);
					Double value = (Double) method.invoke(this, new Object[] {});
					attributes.add(value);
				} catch (Exception e) {
					LOGGER.error("Ocorreu algum erro ao invocar o extrator para o atirbuto ".concat(attribute), e);
				}
			}
		}
	}

	/**
	 * Method responsible for convert attributes to primitive array
	 * @param attributes - list of attributes
	 * @return primitiveAttributes - List of attributes in primitive form
	 */
	public static double[] toPrimitive(ArrayList<Double> attributes) {
		double[] primitiveAttributes = new double[attributes.size()];
		for (int i = 0; i < attributes.size(); i++) {
			primitiveAttributes[i] = attributes.get(i);
		}
		return primitiveAttributes;
	}

	public HashMap<String, String> getAllAttributes() {
		return this.attributes;
	}
}
