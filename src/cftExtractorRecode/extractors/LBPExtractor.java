package cftExtractorRecode.extractors;

/**
 * Classe que realiza a implementação de um extrator de características usando o método LBP - Local Binary Pattern.
 * @author Lia Nara Balta Quinta. 
 */
/*
 * Class that carries out the implementation of an extrator of characteristics using the approach LBP - Local Binary 
 * Pattern.
 * @author Lia Nara Balta Quinta. 
 */

import ij.ImagePlus;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;

import java.util.Arrays;
import java.util.HashSet;

public class LBPExtractor extends Extractor {

	private static HashSet<String> attributeNames = new HashSet<String>(Arrays.asList("LBP11", "LBPROT11"));

	private int initialNeighbors;
	private int finalNeighbors;
	private int incrementNeighbors;
	private int initialRay;
	private int finalRay;
	private int incrementRay;
	private int width;
	private int height;
	private int[] resultadoLBP;

	/*
	 * It Processes the image in 8 bits and calls the LBP and the LBPROT, that carry out the extraction of attributes of the image
	 * @param ImagePlus imp It possesss a reference of the image
	 */

	private double extractLBP11() {
		return resultadoLBP[0];
	}

	private double extractLBPROT11() {
		return resultadoLBP[1];
	}

	@Override
	public void extractAttributes() {
		extractFeatures(6, 10, 2, 1, 13, 2);
		this.attributes.put("LBP11", String.valueOf(resultadoLBP[0]));
		this.attributes.put("LBPROT11", String.valueOf(resultadoLBP[1]));
	}

	public void extractFeatures(int inicio, int fim, int incremento, int inicioPixel, int fimPixel, int incrementoPixel) {

		setInitialRay(inicioPixel);
		setFinalRay(fimPixel);
		setIncrementRay(incrementoPixel);
		setInitialNeighbors(inicio);
		setFinalNeighbors(fim);
		setIncrementNeighbors(incremento);

		ImageProcessor ip = image.getProcessor();
		image.unlock();
		ImageConverter ic = new ImageConverter(image);
		ic.convertToGray8();
		ip = image.getProcessor();

		int width = ip.getWidth();
		int height = ip.getHeight();
		int image[][] = new int[width][height];

		resultadoLBP = new int[2]; // (tamanho*tamanho2)*2];

		int lbpTemp[] = new int[2];
		int k = 0;

		for (int c = 0; c < width; c++) {
			for (int d = 0; d < height; d++) {
				image[c][d] = ip.getPixel(c, d);
			}
		}

		for (int r = getInitialRay(); r <= getFinalRay(); r += getIncrementRay()) {
			for (int v = getInitialNeighbors(); v <= getFinalNeighbors(); v += getIncrementNeighbors()) {
				int binary[] = new int[v];
				lbpTemp = getLBPCalculation(ip, 0, 0, binary, image, v, r);
				resultadoLBP[0] += lbpTemp[0];
				if (resultadoLBP[1] > lbpTemp[1])
					resultadoLBP[1] = lbpTemp[1];
				k++;

			}
		}
		resultadoLBP[0] /= k;
	}

	/*
	 * Calculate the LBP value
	 * @param ImageProcessor ip It possesss informations of the image
	 * @param int i X co-ordinated of the pixel central
	 * @param int j Y co-ordinated of the pixel central
	 * @param int binary[] Vector with the LBP binary value
	 * @param int image[][] Matrix with the image pixels value
	 * @param int v Number of neighbors
	 * @param int r Value of the ray
	 * @return int va[] Vector with the value of the LBP and the LBPROT
	 */

	public int[] getLBPCalculation(ImageProcessor ip, int i, int j, int binary[], int image[][], int v, int r) {

		int lbpValue = 0;
		int cont = 0;
		int va[] = new int[2];
		double w;

		double lbpVector[] = new double[v];

		while (cont < v) {
			double X = i + r * (Math.cos((2 * Math.PI * cont) / v));
			double Y = j - r * (Math.sin((2 * Math.PI * cont) / v));
			w = ip.getInterpolatedValue(X, Y);

			if (image[i][j] < w || image[i][j] == w) {
				binary[cont] = 1;
				lbpValue += (Math.pow(2, cont));
			} else {
				binary[cont] = 0;
			}

			lbpVector[cont] = (Math.pow(2, cont));
			cont++;
		}
		va[0] = lbpValue;
		va[1] = (Rotation(binary, lbpVector, v));

		return va;
	}

	/*
	 * Realiza a rotação invariante do valor do LBP
	 * @param int binary[] Vector with the binary of the LBP
	 * @param double lbpVector[] Vector with the weight of each pixel
	 * @return value Value of the rotation invariant of the LBP value
	 */
	private int Rotation(int binary[], double lbpVector[], int v) {
		int aux = 0, sum = 0, value = -1;

		for (int x = 0; x < v; x++) {
			int y = 0;
			sum = 0;
			while (y < v) {
				aux = (x + y) % v;
				sum += binary[aux] * lbpVector[y];
				y++;
			}
			if (sum < value || value == -1) {
				value = sum;
			}

		}
		return value;
	}

	public void setWidth(int Width) {
		this.width = Width;
	}

	public int getWidth() {
		return width;
	}

	public void setHeight(int Height) {
		this.height = Height;
	}

	public int getHeight() {
		return height;
	}

	public void setInitialRay(int InitialRay) {
		this.initialRay = InitialRay;
	}

	public void setFinalRay(int FinalRay) {
		this.finalRay = FinalRay;
	}

	public void setIncrementRay(int IncrementRay) {
		this.incrementRay = IncrementRay;
	}

	public int getInitialRay() {
		return initialRay;
	}

	public int getFinalRay() {
		return finalRay;
	}

	public int getIncrementRay() {
		return incrementRay;
	}

	public void setInitialNeighbors(int InitialNeighbors) {
		this.initialNeighbors = InitialNeighbors;
	}

	public void setFinalNeighbors(int FinalNeighbors) {
		this.finalNeighbors = FinalNeighbors;
	}

	public void setIncrementNeighbors(int IncrementNeighbors) {
		this.incrementNeighbors = IncrementNeighbors;
	}

	public int getInitialNeighbors() {
		return initialNeighbors;
	}

	public int getFinalNeighbors() {
		return finalNeighbors;
	}

	public int getIncrementNeighbors() {
		return incrementNeighbors;
	}

	public ImagePlus getImagePlus() {
		return image;
	}

	public void setImagePlus(ImagePlus imagePlus) {
		this.image = imagePlus;
		extractFeatures(6, 10, 2, 1, 13, 2);
	}

	@Override
	public HashSet<String> getAtributtesNames() {
		return attributeNames;
	}

}
