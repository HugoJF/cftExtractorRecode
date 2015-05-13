package cftExtractorRecode.exporters;

import org.apache.log4j.Logger;

import weka.core.Instances;
import cftExtractorRecode.image_set.ImageSet;

public abstract class Exporter {
	
	protected ImageSet imageSet;
	
	protected Logger LOGGER = Logger.getLogger(Exporter.class);
	
	public void setImageSet(ImageSet is) {
		this.imageSet = is;
	}
	
	public ImageSet getImageSet() {
		return this.imageSet;
	}
	
	public abstract void export();
}
