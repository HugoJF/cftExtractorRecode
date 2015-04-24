package cftExtractorRecode.extractors;

import ij.IJ;
import ij.measure.Measurements;
import ij.measure.ResultsTable;
import ij.plugin.filter.ParticleAnalyzer;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;

import java.util.Arrays;
import java.util.HashSet;

import org.apache.log4j.Logger;

public class FormExtractor extends Extractor {
	private static Logger LOGGER = Logger.getLogger(FormExtractor.class);
	private static final int MIN_SIZE = 10;
	private static final int MAX_SIZE = 10000000;

	private static HashSet<String> attributeNames = new HashSet<String>(Arrays.asList("Form_factor", "Roundness", "Compactness", "Aspect_Ratio"));

	@Override
	public void extractAttributes() {
		double[] attributes;
		ResultsTable rt = new ResultsTable();
		int meansurements = Measurements.AREA + Measurements.PERIMETER + Measurements.ELLIPSE + Measurements.SHAPE_DESCRIPTORS;
		ParticleAnalyzer pa = new ParticleAnalyzer(ParticleAnalyzer.RECORD_STARTS, meansurements, rt, MIN_SIZE, MAX_SIZE);

		attributes = new double[attributeNames.size()];
		ImageConverter ic = new ImageConverter(image);
		ic.convertToGray8();
		ImageProcessor ip = image.getProcessor();
		ip.autoThreshold();

		float area, perimeter, mAxis, miAxis, angle;

		IJ.run("Clear Results");
		pa.analyze(image, ip);

		try {
			area = (float) rt.getValue("Area", 0);
			perimeter = (float) rt.getValue("Perim.", 0);
			angle = (float) rt.getValue("Angle", 0);
			mAxis = (float) rt.getValue("Major", 0);
			miAxis = (float) rt.getValue("Minor", 0);
			attributes[0] = (double) ((4 * Math.PI * area) / (sqr(perimeter))); // form_factor
			attributes[1] = (float) ((4 * area) / (Math.PI * sqr(mAxis))); // Roundness
			attributes[2] = (float) (Math.sqrt((4 / Math.PI) * area) / mAxis); // Compactness
			attributes[3] = (float) (mAxis / miAxis); // Aspect_Ratio
		} catch (Exception e) {
			LOGGER.error("Erro ao calcular os atributos de forma!", e);
		}
		
		this.attributes.put("Form_factor", String.valueOf(attributes[0]));
		this.attributes.put("Roundness", String.valueOf(attributes[1]));
		this.attributes.put("Compactness", String.valueOf(attributes[2]));
		this.attributes.put("Aspect_Ratio", String.valueOf(attributes[3]));
	}

	private double sqr(double x) {
		return x * x;
	}

	@Override
	public HashSet<String> getAtributtesNames() {
		return attributeNames;
	}

}
