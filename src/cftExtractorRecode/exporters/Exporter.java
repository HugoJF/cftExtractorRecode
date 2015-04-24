package cftExtractorRecode.exporters;

import org.apache.log4j.Logger;

import weka.core.Instances;
import cftExtractorRecode.image_set.ImageSet;

public abstract class Exporter {
	
	protected ImageSet imageset;
	
	protected Logger LOGGER = Logger.getLogger(Exporter.class);
	
	public void setImageSet(ImageSet is) {
		this.imageset = is;
	}
	
	public ImageSet getImageSet() {
		return this.imageset;
	}
	
	public abstract void export();
}
