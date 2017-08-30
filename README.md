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

Demo:

Initial layout

![car plate recognition demo](https://user-images.githubusercontent.com/24991776/29876599-79c3c80c-8dc7-11e7-8940-aa78a8c670fd.png)

Browse Image

![car plate recognition demo2](https://user-images.githubusercontent.com/24991776/29877228-3bcf20a8-8dc9-11e7-8fc1-1bda002a56d9.png)

Before and after pre-processing done on uploaded image.

Display the detected numbers and characters

![car plate recognition demo3](https://user-images.githubusercontent.com/24991776/29876601-79f3a252-8dc7-11e7-9000-395198f877a7.png)
