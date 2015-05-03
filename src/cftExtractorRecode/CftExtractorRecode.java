package cftExtractorRecode;

import java.util.ArrayList;

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
	
	public ArrayList<String> ignoreParametersList = new ArrayList<String>();
	public ArrayList<String> onlyParametersList = new ArrayList<String>();
	public CftExtractorRecode cer;
	
	public static void main(String[] args) {
		CftExtractorRecode cer = new CftExtractorRecode();
		cer.setupConfiguration(args);
		cer.runFromCLI();
	}

	public void setupConfiguration(String args[]) {
		Configuration.addNewValidParameter("imageset.path", true);
		Configuration.addNewValidParameter("imageset.relation", true);
		Configuration.addNewValidParameter("random.seed", true);
		Configuration.addNewValidParameter("arff.path", true);
		Configuration.addNewValidParameter("parameters.ignorelist", false);
		Configuration.addNewValidParameter("parameters.onlylist", false);
		
		Configuration.readFromRunArgs(args);
		
		if(Configuration.getConfiguration("parameters.ignorelist") != null) {
			for(String ignore : Configuration.getConfiguration("parameters.ignorelist").split(",")) {
				cer.addParameterToIgnoreList(ignore);
			}
		}
		
		if(Configuration.getConfiguration("parameters.onlylist") != null) {
			for(String only : Configuration.getConfiguration("parameters.onlylist").split(",")) {
				cer.addParameterToOnlyList(only);
			}
		}
		
		try {
			Configuration.verifyArgs();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Configuration.debugParameters();
	}
	public void addParameterToOnlyList(String only) {
		this.onlyParametersList.add(only);
	}

	public void addParameterToIgnoreList(String ignore) {
		this.ignoreParametersList.add(ignore);
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
		cae.setIgnoreList(this.ignoreParametersList);
		cae.setOnlyList(this.onlyParametersList);
		cae.extractAttributes();
		
		for(ImageClass ic : is.getImageClasses()) {
			for(Image image : ic.getImages()) {
				image.debug();
			}
		}
		InstanceExporter instanceExporter = new InstanceExporter(is, cae.getAllUsedAttributesNames());
		instanceExporter.debug();
		instanceExporter.export();
		ArffExporter arffExporter = new ArffExporter(instanceExporter.getInstances(), new ArffSaver());
		arffExporter.setPath(Configuration.getConfiguration("arff.path"));
		arffExporter.export();
		
	}

}
