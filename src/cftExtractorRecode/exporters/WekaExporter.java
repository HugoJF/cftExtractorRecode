package cftExtractorRecode.exporters;

import java.io.IOException;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.Saver;

public class WekaExporter extends FileExporter {

	/**
	 * What saver to use when exporting
	 */
	private Saver saver;
	
	/**
	 * Instances being exported
	 */
	private Instances instances;
	
	/**
	 * Create an exporter with specific saver
	 * 
	 * @param instances - Instances to export
	 * @param saver - saver to use
	 */
	public WekaExporter(Instances instances, Saver saver) {
		this.instances = instances;
		this.saver = saver;
	}
	
	/**
	 * Create an exporter with ArffSaver saver
	 * 
	 * @param instances - instances to export
	 */
	public WekaExporter(Instances instances) {
		this.instances = instances;
	}
	
	@Override
	public void export() {
		if(saver == null) {
			saver = new ArffSaver();
		}
		
		this.saver.setInstances(instances);
		
		if(path != "") {
			try {
				this.saver.setFile(this.getFile());
				this.saver.writeBatch();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

}
