package cftExtractorRecode;

import ij.ImagePlus;

import java.util.ArrayList;
import java.util.HashMap;

import cftExtractorRecode.extractors.Extractor;
import cftExtractorRecode.image_set.Image;
import cftExtractorRecode.image_set.ImageClass;
import cftExtractorRecode.image_set.ImageSet;

public class CftAttributesExtractor {
	
	private ArrayList<Extractor> extractors = new ArrayList<Extractor>();
	private ImageSet imageset;
	
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
}
