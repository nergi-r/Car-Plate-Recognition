package application;

public class Contour {
	
	private int mat[][];
	private int height, width;
	/*
	 * Constant size multiplier for creating boundary 
	 * when shaping matrix into a square-shaped matrix
	 */
	private final double frameSize = 1.23;
	
	/**
	 * Creates a Contour object
	 * by using a 2D integer matrix.
	 * 
	 * @param h
	 * @param w
	 */
	public Contour(int h, int w) {
		mat = new int[h][w];
		height = h;
		width = w;
	}
	
	/**
	 * Set matrix value at position (@param y, @param x) with value @param val.
	 * 
	 * @param y
	 * 			Row position
	 * @param x
	 * 			Column position
	 * @param val
	 * 			Matrix value
	 */
	public void setPixel(int y, int x, int val){
		mat[y][x] = val;
	}
	
	/**
	 * Get matrix value at position (@param y, @param x).
	 * 
	 * @param y
	 * 			Row position
	 * @param x
	 * 			Column position
	 * @return Matrix value
	 */
	public int getPixel(int y,int x){
		return mat[y][x];
	}
	
	/**
	 * @return height
	 */
	public int getHeight(){
		return height;
	}
	
	/**
	 * @return width
	 */
	public int getWidth(){
		return width;
	}
	
	/**
	 * Get area of matrix by multiplying
	 * its height and width.
	 * 
	 * @return Area of matrix
	 */
	public int getArea(){
		return height * width;
	}

	/**
	 * @return 2D integer matrix
	 */
	public int[][] getMatrix(){
		return mat;
	}
	
	/**
	 * Method to convert a rectangle matrix into
	 * a square-shaped matrix by adding boundaries
	 * and set the new boundaries' values as 0 (Black colored).
	 * 
	 * Overloading method for returning square-shaped matrix
	 * without any resizing.
	 * 
	 * @return 2D integer square-shaped matrix
	 */
	public int[][] getSquareShaped(){
		// Create a new size for the matrix to be square-shaped
		int squareSize = (int) (frameSize * (double) height);
		int upperBound = (squareSize - height) / 2;
		int leftBound = (squareSize - width) / 2;
		int square[][] = new int[squareSize][squareSize];

		/*
		 * Copy all the values of initial matrix
		 * and leave all the boundaries' values as 0 (Black Colored)
		 */
		for(int i=0;i<height;i++){
			for(int j=0;j<width;j++){
				square[i+upperBound][j+leftBound] = mat[i][j];
			}
		}
		return square;
	}

	/**
	 * Method to convert a rectangle matrix into
	 * a square-shaped matrix by adding boundaries
	 * and set the new boundaries' values as 0 (Black colored).
	 * 
	 * Overloading method for returning a resized square-shaped matrix.
	 * 
	 * @return 2D integer square-shaped matrix
	 */
	public int[][] getSquareShaped(int targetSize){
		// Create a new size for the matrix to be square-shaped
		int squareSize = (int) (frameSize * (double) height);
		int upperBound = (squareSize - height) / 2;
		int leftBound = (squareSize - width) / 2;
		int square[][] = new int[squareSize][squareSize];

		/*
		 * Copy all the values of initial matrix
		 * and leave all the boundaries' values as 0 (Black Colored)
		 */
		for(int i=0;i<height;i++){
			for(int j=0;j<width;j++){
				square[i+upperBound][j+leftBound] = mat[i][j];
			}
		}
		
		/*
		 * Defines scale to be used for scaling multiplier
		 * on height and width.
		 * 
		 * Defines edgeIndex as the position of the value
		 * in initial matrix that should be used
		 * for each of the element (i,j) in the new matrix.
		 */
		double scale = (double) (targetSize) / (double) (squareSize);
		int edgeIndex[] = new int[targetSize];
		for(int i=0;i<targetSize;i++){
			int index = (int) (Math.round((double)(i+1) - 0.5f) / scale + 0.5f);
			edgeIndex[i] = Math.min(index, squareSize-1);
		}

		/*
		 * Creates a new resized matrix
		 * Copy all the values of the initial matrix
		 * to the new matrix based on the edgeIndex
		 */
		int resizedSquare[][] = new int [targetSize][targetSize];
		for(int i=0;i<targetSize;i++){
			for(int j=0;j<targetSize;j++){
				resizedSquare[i][j] = square[edgeIndex[i]][edgeIndex[j]];
			}
		}
		
		return resizedSquare;
	}
	
	/**
	 * Method to convert a rectangle matrix into
	 * a normalized square-shaped matrix by adding boundaries
	 * and set the new boundaries' values as 0 (Black colored).
	 * 
	 * Normalized means each value should range between (0.0 and 1.0).
	 * 
	 * Overloading method for returning normalized square-shaped matrix
	 * without any resizing.
	 * 
	 * @return 2D double data type normalized square-shaped matrix
	 */
	public double[][] getNormalizedSquareShaped(){
		// Create a new size for the matrix to be square-shaped
		int squareSize = (int) (frameSize * (double) height);
		int upperBound = (squareSize - height) / 2;
		int leftBound = (squareSize - width) / 2;
		double square[][] = new double[squareSize][squareSize];

		/*
		 * Copy all the values of initial matrix
		 * and leave all the boundaries' values as 0 (Black Colored)
		 */
		for(int i=0;i<height;i++){
			for(int j=0;j<width;j++){
				// Divide value with the maximum value of 8-byte pixel (255)
				square[i+upperBound][j+leftBound] = (double) (mat[i][j]) / 255.0;
			}
		}
		
		return square;
	}

	/**
	 * Method to convert a rectangle matrix into
	 * a normalized square-shaped matrix by adding boundaries
	 * and set the new boundaries' values as 0 (Black colored).
	 * 
	 * Normalized means each value should range between (0.0 and 1.0).
	 * 
	 * Overloading method for returning a normalized resized square-shaped matrix.
	 * 
	 * @return 2D double data type normalized square-shaped matrix
	 */
	public double[][] getNormalizedSquareShaped(int targetSize){
		// Create a new size for the matrix to be square-shaped
		int squareSize = (int) (frameSize * (double) height);
		int upperBound = (squareSize - height) / 2;
		int leftBound = (squareSize - width) / 2;
		int square[][] = new int[squareSize][squareSize];

		/*
		 * Copy all the values of initial matrix
		 * and leave all the boundaries' values as 0 (Black Colored)
		 */
		for(int i=0;i<height;i++){
			for(int j=0;j<width;j++){
				square[i+upperBound][j+leftBound] = mat[i][j];
			}
		}
		
		/*
		 * Defines scale to be used for scaling multiplier
		 * on height and width.
		 * 
		 * Defines edgeIndex as the position of the value
		 * in initial matrix that should be used
		 * for each of the element (i,j) in the new matrix.
		 */
		double scale = (double) (targetSize) / (double) (squareSize);
		int edgeIndex[] = new int[targetSize];
		for(int i=0;i<targetSize;i++){
			int index = (int) (Math.round((double)(i+1) - 0.5) / scale + 0.5);
			edgeIndex[i] = Math.min(index, squareSize-1);
		}

		/*
		 * Creates a new resized matrix
		 * Copy all the values of the initial matrix
		 * to the new matrix based on the edgeIndex
		 */
		double resizedSquare[][] = new double [targetSize][targetSize];
		for(int i=0;i<targetSize;i++){
			for(int j=0;j<targetSize;j++){
				// Divide value with the maximum value of 8-byte pixel (255)
				resizedSquare[i][j] = (double) (square[edgeIndex[i]][edgeIndex[j]] / 255.0);
			}
		}
		
		return resizedSquare;
	}
	
}
