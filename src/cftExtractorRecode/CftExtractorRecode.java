package cftExtractorRecode;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import cftExtractorRecode.exporters.ArffExporter;
import cftExtractorRecode.exporters.Exporter;
import cftExtractorRecode.exporters.InstanceExporter;
import cftExtractorRecode.extractors.*;
import cftExtractorRecode.image_set.Image;
import cftExtractorRecode.image_set.ImageClass;
import cftExtractorRecode.image_set.ImageSet;
import configuration.Configuration;

public class CftExtractorRecode {

	public static void main(String[] args) {
		Configuration.addNewValidParameter("imageset.path", true);
		Configuration.addNewValidParameter("imageset.relation", true);
		Configuration.addNewValidParameter("random.seed", true);
		Configuration.addNewValidParameter("arff.path", true);
		
		Configuration.readFromRunArgs(args);

		try {
			Configuration.verifyArgs();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Configuration.debugParameters();
		
		CftExtractorRecode cer = new CftExtractorRecode();
		cer.runFromCLI();
	}
	
	public CftExtractorRecode() {
		
	}
	
	public void runFromCLI() {
		ImageSet is = null;
		try {
			is = new ImageSet(Configuration.getConfiguration("imageset.path"));
		} catch (Exception ex) { 
			ex.printStackTrace();
		}
		is.setRelation(Configuration.getConfiguration("imageset.relation"));
		CftAttributesExtractor cae = new CftAttributesExtractor(is);
		cae.addExtractor(new ColorExtractor());
		cae.addExtractor(new CoOcorrenceMatrixExtractor());
		cae.addExtractor(new FormExtractor());
		cae.addExtractor(new LBPExtractor());
		cae.addExtractor(new WaveletExtractor());
		cae.extractAttributes();
		
		for(ImageClass ic : is.getImageClasses()) {
			for(Image image : ic.getImages()) {
				image.debug();
			}
		}
		InstanceExporter instanceExporter = new InstanceExporter(is, cae.getAllAttributesNames());
		instanceExporter.debug();
		instanceExporter.export();
		ArffExporter arffExporter = new ArffExporter(instanceExporter.getInstances(), new ArffSaver());
		arffExporter.setPath(Configuration.getConfiguration("arff.path"));
		arffExporter.export();
		
	}

}
