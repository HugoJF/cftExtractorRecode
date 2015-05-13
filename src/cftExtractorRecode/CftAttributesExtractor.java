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
	
	private ArrayList<Extractor> extractors = new ArrayList<Extractor>();
	private ArrayList<String> onlyList = new ArrayList<String>();
	private ArrayList<String> ignoreList = new ArrayList<String>();
	private ImageSet imageset;

	private final static Logger LOGGER = Logger.getLogger(ImageClass.class);
	
	public CftAttributesExtractor(ImageSet is) {
		this.imageset = is;
	}
	 
	public void extractAttributes() {
		for(ImageClass ic : imageset.getImageClasses()) {
			for(Image image : ic.getImages()) {
				for(Extractor extractor : extractors) {
					extractor.setImage(new ImagePlus(image.getAbsolutePath()));
					HashMap<String, String> attributes = extractor.getAllAttributes();
					image.addMultipleAttributes(attributes);
				}
			}
		}
	}
	
	public void addExtractor(Extractor ex) {
		this.extractors.add(ex);
	}
	
	
	public ArrayList<String> getAllAttributesNames() {
		ArrayList<String> names = new ArrayList<String>();
		for(Extractor ex : extractors) {
			for(String name : ex.getAtributtesNames()) {
				names.add(name);
			}
		}
		return names;
	}
	
	public ArrayList<String> getAllUsedAttributesNames() {
		ArrayList<String> names = new ArrayList<String>();
		for(Extractor ex : extractors) {
			for(String name : ex.getAtributtesNames()) {
				if(!ignoreList.contains(name)) {
					if(onlyList.size() == 0 || onlyList.size() != 0 && onlyList.contains(name)) {
						names.add(name);
					}
				}
			}
		}
		LOGGER.info("Returning all attribute names used: " + names.size() + " attributes");
		return names;
	}

	public void setIgnoreList(ArrayList<String> ignoreParametersList) {
		this.ignoreList = ignoreParametersList;
	}

	public void setOnlyList(ArrayList<String> onlyParametersList) {
		this.onlyList = onlyParametersList;
	}
}
