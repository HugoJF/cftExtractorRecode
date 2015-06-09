package cftExtractorRecode.exporters;

import java.util.ArrayList;

import cftExtractorRecode.image_set.Image;
import cftExtractorRecode.image_set.ImageClass;
import cftExtractorRecode.image_set.ImageSet;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class InstanceGenerator extends Exporter{

	/**
	 * Instance being exported
	 */
	private Instances instances;
	
	/**
	 * Attributes to export
	 */
	private ArrayList<String> attributes;
	
	/**
	 * @param is - ImageSet of extracted attributes
	 * @param attributes - attribute name list
	 */
	public InstanceGenerator(ImageSet is, ArrayList<String> attributes) {
		this.imageSet = is;
		this.attributes = attributes;
	}
	
	@Override
	public void export() {
		//Generate attribute name list
		FastVector attributesNames = new FastVector();
		for(String att : attributes) {
			attributesNames.addElement(new Attribute(att));
		}
		//Generate class values list
		FastVector classValues = new FastVector();
		for(ImageClass ic : this.imageSet.getImageClasses()) {
			classValues.addElement(ic.getClassName());
		}
		//Add the class as an a new Attribute
		attributesNames.addElement(new Attribute("class", classValues));
		
		//Create the instances
		instances = new Instances(imageSet.getRelation(), attributesNames, 1);
		//instances.setClassIndex(attributesNames.size() - 1);

		for(ImageClass ic : imageSet.getImageClasses()) {
			for(Image image : ic.getImages()) {
				//Create an array to hold the attribute values
				double[] instanceValues = new double[attributesNames.size()];
				//For every attribute add its value to the arrya
				for(int i = 0; i < attributes.size(); i++) {
					instanceValues[i] = Double.valueOf(image.getAttributeValue(attributes.get(i)));
				}
				//Reset last value of the array (class)
				instanceValues[instanceValues.length - 1] = 0D;
				//Create the final instance object
				Instance instance = new Instance(1, instanceValues);
				//instance.setDataset(instances);
				//instance.setClassValue(image.getFolderName());
				instance.setValue((Attribute) attributesNames.elementAt(attributesNames.size() - 1), image.getFolderName());
				instances.add(instance);
			}
		}
	}

	/**
	 * @return Instances being exported
	 */
	public Instances getInstances() {
		
		return this.instances;
	}

	public void debug() {
		String atts = "";
		for(String att : attributes) {
			atts += att + ", ";
		}
		
		LOGGER.info(atts);
	}
}
