package cftExtractorRecode.exporters;

import org.apache.log4j.Logger;

import weka.core.Instances;
import cftExtractorRecode.image_set.ImageSet;

public abstract class Exporter {
	
	/**
	 * ImageSet being used
	 */
	protected ImageSet imageSet;
	
	protected Logger LOGGER = Logger.getLogger(Exporter.class);
	
	/**
	 * @param is - sets as new ImageSet
	 */
	public void setImageSet(ImageSet is) {
		this.imageSet = is;
	}
	
	/**
	 * @return ImageSet being used
	 */
	public ImageSet getImageSet() {
		return this.imageSet;
	}
	
	/**
	 * Method for processing attributes into a resulting file
	 */
	public abstract void export();
}
