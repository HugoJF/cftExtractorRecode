package cftExtractorRecode.extractors;

import ij.ImagePlus;
import ij.process.ImageProcessor;

import java.awt.Color;
import java.util.Arrays;
import java.util.HashSet;

import org.apache.log4j.Logger;

/**
 * Class responsible for extract the following attributes of color: RGB_R", "RGB_G", "RGB_B", "HSB_H", "HSB_S", "HSB_B
 * 
 * @author victorjussiani
 */
public class ColorExtractor extends Extractor {

	private static Logger LOGGER = Logger.getLogger(ColorExtractor.class);
	private static HashSet<String> attributeNames = new HashSet<String>(Arrays.asList("RGB_R", "RGB_G", "RGB_B", "HSB_H", "HSB_S", "HSB_B"));
	
	/**
	 * Method responsible for extract the RGB channels from image
	 * 
	 * @param imageProcessor - Image processor to the target image
	 * @return Returns the average of the values ​​for the three channels: R[0] G[1] B[2]
	 */
	private double[] captureRGBPixels(ImageProcessor imageProcessor) {
		LOGGER.debug("Extracting RGB channels from image");

		ImagePlus imp = new ImagePlus("Teste", imageProcessor);
		imageProcessor = imp.getProcessor();
		double vetor[] = new double[3];
		for (int i = 0; i < imageProcessor.getWidth(); i++) {
			for (int j = 0; j < imageProcessor.getHeight(); j++) {
				Color color = new Color(imageProcessor.getPixel(i, j));
				vetor[0] += color.getRed();
				vetor[1] += color.getGreen();
				vetor[2] += color.getBlue();
			}
		}

		int qtdade = imageProcessor.getWidth() * imageProcessor.getHeight();
		vetor[0] = (vetor[0] / qtdade);
		vetor[1] = (vetor[1] / qtdade);
		vetor[2] = (vetor[2] / qtdade);
		return vetor;
	}

	/**
	 * Method responsible for extract the HSV channels from image
	 * 
	 * @param imageProcessor - Image processor to the target image
	 * @return Returns the average of the values ​​for the three channels
	 */
	public double[] captureHSBPixels(ImageProcessor imageProcessor) {
		LOGGER.debug("Extracting HSB channels from image");

		ImagePlus imp = new ImagePlus("Teste", imageProcessor);
		imageProcessor = imp.getProcessor();
		double vetorHSB[] = new double[3];
		for (int i = 0; i < imageProcessor.getWidth(); i++) {
			for (int j = 0; j < imageProcessor.getHeight(); j++) {
				Color color = new Color(imageProcessor.getPixel(i, j));
				float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
				vetorHSB[0] += hsb[0];
				vetorHSB[1] += hsb[1];
				vetorHSB[2] += hsb[2];
			}
		}

		int qtdade = imageProcessor.getWidth() * imageProcessor.getHeight();
		vetorHSB[0] = (vetorHSB[0] / qtdade);
		vetorHSB[1] = (vetorHSB[1] / qtdade);
		vetorHSB[2] = (vetorHSB[2] / qtdade);
		return vetorHSB;
	}

	@Override
	public HashSet<String> getAtributtesNames() {
		return attributeNames;
	}

	@Override
	public void extractAttributes() {
		double[] rgb = new double[3];
		double[] hsb = new double[3];
		rgb = captureRGBPixels(this.image.getProcessor());
		hsb = captureHSBPixels(this.image.getProcessor());
		attributes.put("RGB_R", String.valueOf(rgb[0]));
		attributes.put("RGB_G", String.valueOf(rgb[1]));
		attributes.put("RGB_B", String.valueOf(rgb[2]));
		attributes.put("HSB_H", String.valueOf(hsb[0]));
		attributes.put("HSB_S", String.valueOf(hsb[1]));
		attributes.put("HSB_B", String.valueOf(hsb[2]));
	}

}
