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
		Configuration config = cer.setupConfiguration(args);
		cer.run(config);
	}

	/**
	 * Prepare the Configuration class to run
	 * 
	 * @param args Runtime args
	 * @return 
	 */
	public Configuration setupConfiguration(String args[]) {
		Configuration config = new Configuration();
		
		
		config.addNewValidParameter("imageset.path", true);
		config.addNewValidParameter("imageset.relation", true);
		config.addNewValidParameter("random.seed", true);
		config.addNewValidParameter("arff.path", true);
		config.addNewValidParameter("parameters.ignorelist", false);
		config.addNewValidParameter("parameters.onlylist", false);
		
		config.readFromRunArgs(args);
		
		if(config.getConfiguration("parameters.ignorelist") != null) {
			for(String ignore : config.getConfiguration("parameters.ignorelist").split(",")) {
				this.addParameterToIgnoreList(ignore);
			}
		}
		
		if(config.getConfiguration("parameters.onlylist") != null) {
			for(String only : config.getConfiguration("parameters.onlylist").split(",")) {
				this.addParameterToOnlyList(only);
			}
		}
		
		try {
			config.verifyArgs();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		config.debugParameters();
		
		return config;
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
	public void run(Configuration config) {
		ImageSet is = null;
		try {
			is = new ImageSet(config.getConfiguration("imageset.path"));
		} catch (Exception ex) { 
			ex.printStackTrace();
		}
		is.setRelation(config.getConfiguration("imageset.relation"));
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
		arffExporter.setPath(config.getConfiguration("arff.path"));
		arffExporter.export();
		
	}
	
	/**
	 * Extracts CFT Attributes from ImageSet and returns the results
	 * 
	 * @param relation ImageSet name
	 * @param path ImageSet path
	 * @return Weka Instances of attributes extracted
	 */
	public Instances generateInstances(Configuration config) {
		ImageSet is = null;
		try {
			//is = new ImageSet(path);
			is = new ImageSet(config.getConfiguration("imageset.path"));
		} catch (Exception ex) { 
			ex.printStackTrace();
		}
		//is.setRelation(relation);
		is.setRelation(config.getConfiguration("imageset.relation"));
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
