package cftExtractorRecode;

import ij.ImagePlus;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import cftExtractorRecode.extractors.Extractor;
import cftExtractorRecode.image_set.Image;
import cftExtractorRecode.image_set.ImageClass;
import cftExtractorRecode.image_set.ImageSet;

public class CftAttributesExtractor {

	/**
	 * Array of extractors being used
	 */
	private ArrayList<Extractor> extractors = new ArrayList<Extractor>();

	/**
	 * If not empty, only attributes inside this list will be outputed to final
	 * .arff file.
	 */
	private ArrayList<String> onlyList = new ArrayList<String>();

	/**
	 * Attributes to ignore
	 */
	private ArrayList<String> ignoreList = new ArrayList<String>();

	/**
	 * The imageset being extracted
	 */
	private ImageSet imageset;

	private final static Logger LOGGER = Logger.getLogger(ImageClass.class);

	/**
	 * @param is
	 *            - The imageset being extracted
	 */
	public CftAttributesExtractor(ImageSet is) {
		this.imageset = is;
	}

	/**
	 * Starts the extraction
	 */
	public void extractAttributes() {
		for (ImageClass ic : imageset.getImageClasses()) {
			for (Image image : ic.getImages()) {
				for (Extractor extractor : extractors) {
					extractor.setImage(new ImagePlus(image.getAbsolutePath()));
					HashMap<String, String> attributes = extractor.getAllAttributes();
					image.addMultipleAttributes(attributes);
				}
			}
		}
	}

	/**
	 * @param ex
	 *            - add an Extractor to the list of extractors being used
	 */
	public void addExtractor(Extractor ex) {
		this.extractors.add(ex);
	}

	/**
	 * Returns every supported attribute name
	 * 
	 * @return - A list of attributes names
	 */
	public ArrayList<String> getAllAttributesNames() {
		ArrayList<String> names = new ArrayList<String>();
		for (Extractor ex : extractors) {
			for (String name : ex.getAtributtesNames()) {
				names.add(name);
			}
		}
		return names;
	}

	/**
	 * Returns only the used attribute name
	 * 
	 * @return
	 */
	public ArrayList<String> getAllUsedAttributesNames() {
		ArrayList<String> names = new ArrayList<String>();
		for (Extractor ex : extractors) {
			for (String name : ex.getAtributtesNames()) {
				if (!ignoreList.contains(name)) {
					if (onlyList.size() == 0 || onlyList.size() != 0 && onlyList.contains(name)) {
						names.add(name);
					}
				}
			}
		}
		LOGGER.info("Returning all attribute names used: " + names.size() + " attributes");
		return names;
	}

	/**
	 * Sets the list of ignored attributes
	 * 
	 * @param ignoreParametersList
	 *            - ArrayList of attribute names to ignore
	 */
	public void setIgnoreList(ArrayList<String> ignoreParametersList) {
		this.ignoreList = ignoreParametersList;
	}

	/**
	 * Sets the list of attributes to extract
	 * 
	 * @param onlyParametersList
	 *            - ArrayList of attribute names to ignore
	 */
	public void setOnlyList(ArrayList<String> onlyParametersList) {
		this.onlyList = onlyParametersList;
	}
}
