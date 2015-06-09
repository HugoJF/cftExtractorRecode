package cftExtractorRecode;

import java.util.ArrayList;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import cftExtractorRecode.exporters.WekaExporter;
import cftExtractorRecode.exporters.Exporter;
import cftExtractorRecode.exporters.InstanceGenerator;
import cftExtractorRecode.extractors.*;
import cftExtractorRecode.image_set.Image;
import cftExtractorRecode.image_set.ImageClass;
import cftExtractorRecode.image_set.ImageSet;
import configuration.Configuration;

public class CftExtractorRecode {
	
	public ArrayList<String> ignoreParametersList = new ArrayList<String>();
	public ArrayList<String> onlyParametersList = new ArrayList<String>();
	
	public static void main(String[] args) {
		CftExtractorRecode cer = new CftExtractorRecode();
		cer.setupConfiguration(args);
		cer.runFromCLI();
	}

	/**
	 * Prepare the Configuration class to run
	 * 
	 * @param args Runtime args
	 */
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
				this.addParameterToIgnoreList(ignore);
			}
		}
		
		if(Configuration.getConfiguration("parameters.onlylist") != null) {
			for(String only : Configuration.getConfiguration("parameters.onlylist").split(",")) {
				this.addParameterToOnlyList(only);
			}
		}
		
		try {
			Configuration.verifyArgs();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Configuration.debugParameters();
	}
	/**
	 * @param only - add parameter to extraction list
	 */
	public void addParameterToOnlyList(String only) {
		this.onlyParametersList.add(only);
	}

	/**
	 * @param ignore - add parameter to ignore list
	 */
	public void addParameterToIgnoreList(String ignore) {
		this.ignoreParametersList.add(ignore);
	}
	
	
	/**
	 * Method for when running directly from CLI
	 */
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
		InstanceGenerator instanceExporter = new InstanceGenerator(is, cae.getAllUsedAttributesNames());
		instanceExporter.debug();
		instanceExporter.export();
		WekaExporter arffExporter = new WekaExporter(instanceExporter.getInstances(), new ArffSaver());
		arffExporter.setPath(Configuration.getConfiguration("arff.path"));
		arffExporter.export();
		
	}
	
	/**
	 * Extracts CFT Attributes from ImageSet and returns the results
	 * 
	 * @param relation ImageSet name
	 * @param path ImageSet path
	 * @return Weka Instances of attributes extracted
	 */
	public Instances generateInstances(String relation, String path) {
		ImageSet is = null;
		try {
			is = new ImageSet(path);
		} catch (Exception ex) { 
			ex.printStackTrace();
		}
		is.setRelation(relation);
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
		InstanceGenerator InstanceGenerator = new InstanceGenerator(is, cae.getAllUsedAttributesNames());
		InstanceGenerator.debug();
		InstanceGenerator.export();
		return InstanceGenerator.getInstances();
	}

}
