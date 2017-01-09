Car Plate Recognition

An application to predict Indonesian car plate using Artificial Neural Network.

How it works:
1. Load the given image and do thresholding to create a black & white image.
2. Do character segmentation using Breadth-First-Search.
3. Use neural network to predict each segmented character.

Neural Network has been trained in GNU Octave beforehand.
The weight parameters are saved to a .txt file to be used in the Java application.

Training set from:
1. http://www.ee.surrey.ac.uk/CVSSP/demos/chars74k/
2. Some hand drawn characters
