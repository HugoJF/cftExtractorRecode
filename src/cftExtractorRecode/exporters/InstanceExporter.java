package cftExtractorRecode.exporters;

import java.util.ArrayList;

import cftExtractorRecode.image_set.Image;
import cftExtractorRecode.image_set.ImageClass;
import cftExtractorRecode.image_set.ImageSet;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class InstanceExporter extends Exporter{

	private Instances instances;
	
	private ImageSet imageset;
	
	private ArrayList<String> attributes;
	
	public InstanceExporter(ImageSet is, ArrayList<String> attributes) {
		this.imageset = is;
		this.attributes = attributes;
	}
	
	@Override
	public void export() {
		FastVector attributesNames = new FastVector();
		for(String att : attributes) {
			attributesNames.addElement(new Attribute(att));
		}
		
		FastVector classValues = new FastVector();
		for(ImageClass ic : this.imageset.getImageClasses()) {
			classValues.addElement(ic.getClassName());
			LOGGER.info(ic.getClassName());
		}
		attributesNames.addElement(new Attribute("class", classValues));
		
		instances = new Instances(imageset.getRelation(), attributesNames, 1);
		instances.setClassIndex(attributesNames.size() - 1);

		for(ImageClass ic : imageset.getImageClasses()) {
			for(Image image : ic.getImages()) {
				double[] instanceValues = new double[attributes.size() + 1];
				for(int i = 0; i < attributes.size(); i++) {
					instanceValues[i] = Double.valueOf(image.getAttributeValue(attributes.get(i)));
				}
				instanceValues[instanceValues.length - 1] = 0D;
				Instance instance = new Instance(1, instanceValues);
				instance.setDataset(instances);
				LOGGER.info(image.getFolderName());
				instance.setClassValue(image.getFolderName());
				instances.add(instance);
			}
		}
	}

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
