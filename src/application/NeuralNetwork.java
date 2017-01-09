package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class NeuralNetwork {

	int inputLayerSize;
	int hiddenLayerSize;
	int outputLayerSize;
	double[] weights;
	double[][] theta1, theta2;
	double[][] transposedTheta1, transposedTheta2;
	
	/**
	 * Creates a new neural network model object which
	 * consists of 3 layers (InputLayer, HiddenLayer and OutputLayer)
	 * to classify input data using multi-classification method.
	 * 
	 * @param inputLayerSize
	 * 			Number of neurons in InputLayer
	 * @param hiddenLayerSize
	 * 			Number of neurons in HiddenLayer
	 * @param outputLayerSize
	 * 			Number of neurons in OutputLayer
	 * @param weightsData
	 * 			Weights for each connection between neurons.
	 * @exception java.io.FileNotFoundException
	 * 			If weightsData is null; failed to locate file.
	 */
	public NeuralNetwork(int inputLayerSize, int hiddenLayerSize,
							int outputLayerSize, File weightsData){
		
		this.inputLayerSize = inputLayerSize;
		this.hiddenLayerSize = hiddenLayerSize;
		this.outputLayerSize = outputLayerSize;
		weights = new double[(inputLayerSize+1) * hiddenLayerSize + (hiddenLayerSize + 1) * outputLayerSize];
		theta1 = new double[hiddenLayerSize][inputLayerSize + 1];
		theta2 = new double[outputLayerSize][hiddenLayerSize + 1];
		
		/*
		 * weightsData is a .txt file generated using Octave.
		 * Weights have been obtained by training 
		 * a neural network model using Octave.
		 */
		Scanner scanner;
		try {
			scanner = new Scanner(weightsData);
			int index = 0;
			
			// Move all data from file to array weights
			while(scanner.hasNextDouble()){
				weights[index] = scanner.nextDouble();
				index++;
			}
			
			/*
			 * Reconstruct 1D weights matrix to two 2D weights matrix.
			 * 
			 * Theta1 is for weights between InputLayer and HiddenLayer.
			 * Theta2 is for weights between HiddenLayer and OutputLayer.
			 */
			index = 0;
			for(int i=0;i<inputLayerSize+1;i++){
				for(int j=0;j<hiddenLayerSize;j++){
					theta1[j][i] = weights[index];
					index++;
				}
			}

			index = 0;
			for(int i=0;i<hiddenLayerSize+1;i++){
				for(int j=0;j<outputLayerSize;j++){
					theta2[j][i] = weights[index + (inputLayerSize+1) * hiddenLayerSize];
					index++;
				}
			}
			
			/*
			 * Transpose both matrices so that matrix multiplication
			 * can be done between weights and neurons' values
			 */
			transposedTheta1 = Utils.transpose2DMatrix(theta1);
			transposedTheta2 = Utils.transpose2DMatrix(theta2);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method that receives @param data as values for
	 * neurons in InputLayer and return the output neuron's index
	 * with highest probability.
	 * 
	 * @param data
	 * 			Normalized threshold matrix of the image given.
	 * @return Classifier's output prediction of the input
	 */
	public int predict(double[][] data){
		double h1[] = new double[hiddenLayerSize + 1];
		double h2[] = new double[outputLayerSize + 1];
		
		/*
		 * Run feed forward propagation.
		 * 
		 * Values in input layer will be multiplied by Theta1
		 * to obtain values for hidden layer.
		 * Values in hidden layer will be multiplied by Theta2
		 * to obtain values for output layer.
		 * 
		 * Feed forward propagation is done by using matrix multiplication
		 * and sigmoid activation function.
		 */
		int height = data.length;
		int width = data[0].length;
		int index = 1;
		double data1D[] = new double[height * width + 1];
		data1D[0] = 1.0;
		for(int i=0;i<width;i++){
			for(int j=0;j<height;j++){
				data1D[index] = data[j][i];
				index++;
			}
		}
		
		h1[0] = 1.0;
		for(int i=0;i<hiddenLayerSize;i++){
			for(int j=0;j<data1D.length;j++){
				h1[i+1] += data1D[j] * transposedTheta1[j][i];
			}
		}
		for(int i=0;i<h1.length;i++){
			h1[i] = sigmoid(h1[i]);
		}
		
		for(int i=0;i<outputLayerSize;i++){
			for(int j=0;j<h1.length;j++){
				h2[i+1] += h1[j] * transposedTheta2[j][i];
			}
		}

		for(int i=0;i<h2.length;i++){
			h2[i] = sigmoid(h2[i]);
		}
		
		/*
		 * Find index of neuron in output layer
		 * with the highest probability
		 */
		double maxProbability = Integer.MIN_VALUE;
		int prediction = 0;
		for(int i=1;i<=outputLayerSize;i++){
			if(h2[i] > maxProbability){
				maxProbability = h2[i];
				prediction = i;
			}
		}

		return prediction;
	}

	/**
	 * Sigmoid activation function.
	 * 
	 * @param val
	 * @return Sigmoid value of @param val
	 */
	public double sigmoid(double val){
		return 1.0 / (1.0 + Math.exp(-val));
	}
}
