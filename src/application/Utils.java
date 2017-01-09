package application;

import java.awt.image.BufferedImage;
//import java.io.File;
//
//import javax.imageio.ImageIO;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;

public final class Utils {
	public static final String alphabetType = "Alphabet";
	public static final String numberType = "Number";
	public static final int thresholdLimit = 128;
	
	/**
	 * Generic method for putting element running on a non-JavaFX thread on the
	 * JavaFX thread, to properly update the UI.
	 * 
	 * @param property
	 *            a {@link ObjectProperty}
	 * @param value
	 *            the value to set for the given {@link ObjectProperty}
	 */
	public static <T> void onFXThread(final ObjectProperty<T> property, final T value){
		Platform.runLater(() -> {
			property.set(value);
		});
	}
	
	/**
	 * Generic method to get black and white BufferedImage 
	 * from a threshold matrix.
	 * 
	 * @param mat
	 * 			Integer thresholded matrix
	 * @return A black and white BufferedImage
	 */
	public static BufferedImage getImageFromMatrix(int mat[][]){
		int h = mat.length;
		int w = mat[0].length;
		BufferedImage bufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_BINARY);
		
		/*
		 * Iterate through all elements in matrix
		 * to be put on BufferedImage as pixels.
		 * 
		 * BufferedImage needs rgb value to be set on its pixel.
		 */
	    for (int i=0;i<h;i++) {
	        for (int j=0;j<w;j++) {
	            int pixel = mat[i][j];
	            // Get pixel's rgb value
	            int rgb = (int)pixel << 16 | (int)pixel << 8 | (int)pixel;
	            // Set image's pixel to the specified rgb value
	            bufferedImage.setRGB(j, i, rgb);
	        }
	    }
		
		return bufferedImage;
	}
	
	/**
	 * Generic method to get a threshold matrix from image.
	 * 
	 * A threshold limit is specified as a constant value
	 * to identify whether a pixel is more likely to be black or white.
	 * 
	 * @param image
	 * 			A BufferedImage (Can be RGB or grayscale)
	 * @return 2D integer array with thresholded value
	 */
	public static int[][] getThresholdMatrixFromImage(BufferedImage image){
		int h = image.getHeight(), w = image.getWidth();
	    int mat[][] = new int[h][w];
	    
	    /*
	     * Iterate through all pixels in the image
	     * For each pixels, check whether pixel's gray value
	     * is more than thresholdLimit.
	     * - Yes, mark it as white (255)
	     * - No, mark it as black (0)
	     */
	    for (int i=0;i<h;i++) {
	        for(int j=0;j<w;j++) {
	        	int gray = image.getRGB(j,i)& 0xFF;
	        	if(gray > Utils.thresholdLimit){
	        		// Set value as white pixel (255)
	        		mat[i][j] = 255;
	        	}
	        	else{
	        		// Set value as black pixel (0)
	        		mat[i][j] = 0;
	        	}
	        }
	    }
	    return mat;
	}

	/**
	 * Generic method to transpose a matrix A -> A'.
	 * Overloading method for integer matrix.
	 * 
	 * @param mat
	 * 			A 2D integer matrix
	 * @return 2D integer transposed matrix
	 */
	public static int[][] transpose2DMatrix(int[][] mat){
		int height = mat.length;
		int width = mat[0].length;
		int transposedMatrix[][] = new int[width][height];
		
		for(int i=0;i<height;i++){
			for(int j=0;j<width;j++){
				transposedMatrix[j][i] = mat[i][j];
			}
		}
		
		return transposedMatrix;
	}

	/**
	 * Generic method to transpose a matrix A -> A'.
	 * Overloading method for double data type matrix.
	 * 
	 * @param mat
	 * 			A 2D double data type matrix
	 * @return 2D double data type transposed matrix
	 */
	public static double[][] transpose2DMatrix(double[][] mat){
		int height = mat.length;
		int width = mat[0].length;
		double transposedMatrix[][] = new double[width][height];
		
		for(int i=0;i<height;i++){
			for(int j=0;j<width;j++){
				transposedMatrix[j][i] = mat[i][j];
			}
		}
		
		return transposedMatrix;
	}
	
}