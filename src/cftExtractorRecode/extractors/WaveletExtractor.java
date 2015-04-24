package cftExtractorRecode.extractors;

import fractsplinewavelets.Filters;
import fractsplinewavelets.FractSplineWavelets;
import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.Roi;
import ij.process.ImageConverter;

import java.util.Arrays;
import java.util.HashSet;

import waveletJava.core.GLCM_Texture;

public class WaveletExtractor extends Extractor {

	private static HashSet<String> attributeNames = new HashSet<String>(Arrays.asList("QUARTER_1_ASM", "QUARTER_1_CONT", "QUARTER_1_CORR", "QUARTER_1_IDM", "QUARTER_1_ENT", "QUARTER_2_ASM", "QUARTER_2_CONT", "QUARTER_2_CORR", "QUARTER_2_IDM", "QUARTER_2_ENT", "QUARTER_3_ASM", "QUARTER_3_CONT", "QUARTER_3_CORR", "QUARTER_3_IDM", "QUARTER_3_ENT", "QUARTER_4_ASM", "QUARTER_4_CONT", "QUARTER_4_CORR", "QUARTER_4_IDM", "QUARTER_4_ENT"));

	private final int[] iters = { 1, 1, 0 };
	private final int asm_index = 0, cont_index = 1, corr_index = 2, idm_index = 3, ent_index = 4;
	private final double degree = -0.49;
	private final double shift = -0.50;

	private GLCM_Texture matrizCo;

	@Override
	public void extractAttributes() {
		preProcessing(image);
		matrizCo = new GLCM_Texture();
		ImagePlus transform = FractSplineWavelets.doTransform(image, iters, Filters.ORTHONORMAL, degree, shift);
		//transform.show();
		int width = image.getWidth() / 2;
		int height = image.getHeight() / 2;

		float[] firstQuarterAttributes = new float[5];
		float[] secondQuarterAttributes = new float[5];
		float[] thirdQuarterAttributes = new float[5];
		float[] fourthQuarterAttributes = new float[5];
		
		firstQuarterAttributes = getWaveletsAttributes(transform, 1, width, height);
		secondQuarterAttributes = getWaveletsAttributes(transform, 2, width, height);
		thirdQuarterAttributes = getWaveletsAttributes(transform, 3, width, height);
		fourthQuarterAttributes = getWaveletsAttributes(transform, 4, width, height);
		

		String[] types = {"ASM", "CONT", "CORR", "IDM", "ENT"};
		for(int i = 0; i < 5; i++) {
			this.attributes.put("QUARTER_1_" + types[i], String.valueOf(firstQuarterAttributes[i]));
			this.attributes.put("QUARTER_2_" + types[i], String.valueOf(secondQuarterAttributes[i]));
			this.attributes.put("QUARTER_3_" + types[i], String.valueOf(thirdQuarterAttributes[i]));
			this.attributes.put("QUARTER_4_" + types[i], String.valueOf(fourthQuarterAttributes[i]));
		}
	}

	private float[] getWaveletsAttributes(ImagePlus transform, int quarter, int width, int height) {
		WindowManager.setTempCurrentImage(transform);
		Roi roi;

		if (quarter == 1) {
			roi = new Roi(0, 0, width, height);
		} else if (quarter == 2) {
			roi = new Roi(width, 0, width, height);
		} else if (quarter == 3) {
			roi = new Roi(0, height, width, height);
		} else {
			roi = new Roi(width, height, width, height);
		}

		IJ.makeRectangle(roi.getBounds().x, roi.getBounds().y, roi.getBounds().width, roi.getBounds().height);
		IJ.run("Duplicate...", "new_image");

		ImagePlus aux = WindowManager.getCurrentImage();
		//aux.show();
		preProcessing(aux);
		matrizCo.run(aux.getProcessor());

		return matrizCo.getAtributes();
	}

	private void preProcessing(ImagePlus imp) {
		WindowManager.setTempCurrentImage(imp);
		ImageConverter conversor;
		conversor = new ImageConverter(imp);
		conversor.convertToGray8();
		WindowManager.setTempCurrentImage(imp);
	}

	@Override
	public HashSet<String> getAtributtesNames() {
		return attributeNames;
	}
}
