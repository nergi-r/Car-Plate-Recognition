package application;

import java.io.File;

public class Prediction {
	
	NeuralNetwork neuralNetwork;

	/**
	 * Creates a new prediction wrapper object
	 * with neural network underlying to predict
	 * the output of respective input data.
	 * 
	 * @param inputLayerSize
	 * 			Number of neurons in InputLayer
	 * @param hiddenLayerSize
	 * 			Number of neurons in HiddenLayer
	 * @param outputLayerSize
	 * 			Number of neurons in OutputLayer
	 * @param weightsData
	 * 			Weights for each connection between neurons.
	 */
	public Prediction(int inputLayerSize, int hiddenLayerSize,
			int outputLayerSize, File weightsData){
		
		neuralNetwork = new NeuralNetwork(inputLayerSize, hiddenLayerSize, outputLayerSize, weightsData);
	}
	
	/**
	 * A prediction wrapper method for predicting
	 * what is the classification of the input given.
	 * 
	 * Classification is separated between alphabet and number.
	 * 
	 * @param data
	 * 			Normalized threshold matrix of the image given.
	 * @param type
	 * 			Type of the prediction wanted (Alphabet or Number)
	 * @return prediction for the input in the form of a single character ('0-9','A-Z')
	 */
	public char predict(double data[][], String type){
		char result = ' ';
		int prediction = 0;
		if(type == Utils.numberType){
			prediction = neuralNetwork.predict(data);
			// Convert prediction number (1-10) to number ASCII by adding 48 ('0')
			result = (char) (prediction%10 + 48);
		}
		else if(type == Utils.alphabetType){
			prediction = neuralNetwork.predict(data);
			// Convert prediction number (1-26) to alphabet ASCII by adding 64 ('A')
			result = (char) (prediction + 64);
		}
		return result;
	}
}
