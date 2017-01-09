package application;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

public class FXController {

	private final String noImageLoadedError = "Image has not been loaded.";

	private int imageMatrix[][];
	private boolean imageLoaded;
	private final int imageSize = 28;
	
	@FXML
	private Button uploadImageButton;
	@FXML
	private Button runButton;
	@FXML
	private ImageView originalImage;
	@FXML
	private ImageView blackWhiteImage;
	@FXML
	private ImageView character1;
	@FXML
	private ImageView character2;
	@FXML
	private ImageView character3;
	@FXML
	private ImageView character4;
	@FXML
	private ImageView character5;
	@FXML
	private ImageView character6;
	@FXML
	private ImageView character7;
	@FXML
	private ImageView character8;
	@FXML
	private TextField recognitionResult;
	@FXML
	private TextField character1Type;
	@FXML
	private TextField character2Type;
	@FXML
	private TextField character3Type;
	@FXML
	private TextField character4Type;
	@FXML
	private TextField character5Type;
	@FXML
	private TextField character6Type;
	@FXML
	private TextField character7Type;
	@FXML
	private TextField character8Type;
	
	private ArrayList<ImageView> charactersImageView;
	private ArrayList<TextField> charactersTextField;
	
	/**
	 * Procedure to load image from computer and
	 * process the image to black and white and
	 * display both of them to JavaFX GUI.
	 * 
	 * @param event
	 * 			Action done on the getPicture GUI button
	 * @exception java.io.IOException
	 * 			Raised when there is an interruption / failure when reading image
	 */
	@FXML
	protected void getPicture(ActionEvent event){
		
		/*
		 * Creates a FileChooser object to load folder browser
		 * to choose an image to bs ued
		 */
		FileChooser fileChooser = new FileChooser();

		FileChooser.ExtensionFilter extfilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
		FileChooser.ExtensionFilter extfilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
		fileChooser.getExtensionFilters().addAll(extfilterPNG, extfilterJPG);
		
		File imageFile = fileChooser.showOpenDialog(null);
		
		// If you need to directly load image using the file path
		// File imageFile = new File(getClass().getResource("resources/1.png").getPath());

		// Read image from file
		BufferedImage img = null;
		try {
			img = ImageIO.read(imageFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Get threshold matrix from the image
		imageMatrix = Utils.getThresholdMatrixFromImage(img);
		
		// Display the original image to JavaFX GUI
		Image displayImage = SwingFXUtils.toFXImage(img, null);
		updateImageView(originalImage, displayImage);
		
		/*
		 * Display the processed black and white image to JavaFX GUI
		 * besides the original image
		 */
		BufferedImage BWImage = Utils.getImageFromMatrix(imageMatrix);
		displayImage = SwingFXUtils.toFXImage(BWImage, null);
		updateImageView(blackWhiteImage, displayImage);
		
		imageLoaded = true;
	}
	
	/**
	 * Procedure to predict all the characters in loaded image.
	 * 
	 * Steps:
	 * 1. Creates a CharacterSegmentation object to get all the
	 * 		individual characters in the image.
	 * 2. Creates a Prediction object for both alphabet and number
	 * 		to predict all the characters.
	 * 3. Display the images of segmented characters and
	 * 		the final text result of the prediction.
	 * 
	 * @return Return early if run button was pressed before
	 * loading image has been done
	 */
	@FXML
	protected void run(){

		// Return if no image has been loaded
		if(imageLoaded == false){
			updateTextField(recognitionResult, noImageLoadedError);
			return;
		}
		
		// Initialize ImageView and TextField objects to null
		initImageViewArray();
		initTextFieldArray();

		String predictionResult = "";
		
		/*
		 * Creates a CharacterSegmentation object to segment the characters
		 * from the given input matrix
		 */
		CharacterSegmentation characterSegmentation = new CharacterSegmentation(imageMatrix);
		characterSegmentation.run();
		
		// Get all the segmented characters in an ArrayList of Character objects
		ArrayList<Character> characterList = characterSegmentation.getCharacterList();

		/*
		 * Loading weights parameters
		 * 
		 * When we need to run the program in eclipse
		 * File(getClass().getResource("resources/weights.txt").getPath());
		 * 
		 * When we need to export the program to .jar, add '/' before the path name
		 * File(getClass().getResource("/resources/weights.txt").getPath());
		 */
		
		/*
		 * Load weights parameters for the neural network
		 * that predicts numbers and creates the prediction object
		 */
		File numberWeightsFile = new File(getClass().getResource("/resources/numberWeights.txt").getPath());
		Prediction numberPrediction = new Prediction(784, 200, 10, numberWeightsFile);

		/*
		 * Load weights parameters for the neural network
		 * that predicts alphabets and creates the prediction object
		 */
		File alphabetWeightsFile = new File(getClass().getResource("/resources/alphabetWeights.txt").getPath());
		Prediction alphabetPrediction = new Prediction(784, 200, 26, alphabetWeightsFile);

		for(int i=0;i<characterList.size();i++){
			
			// Predict characters using neural network with its corresponding type
			if(characterList.get(i).getType() == Utils.alphabetType){
				predictionResult += alphabetPrediction.predict(
						characterList.get(i).getNormalizedSquareShaped(imageSize),
						characterList.get(i).getType()
						);
			}
			else if(characterList.get(i).getType() == Utils.numberType){
				predictionResult += numberPrediction.predict(
						characterList.get(i).getNormalizedSquareShaped(imageSize),
						characterList.get(i).getType()
						);
			}
			
			// Add spaces after each character
			predictionResult += " ";
			
			/*
			 * Get square shaped image of an individual character 
			 * with it corresponding type and display it to JavaFX GUI
			 */
			BufferedImage characterImage = Utils.getImageFromMatrix(
					characterList.get(i).getSquareShaped(imageSize)
					);
			updateImageView(charactersImageView.get(i), SwingFXUtils.toFXImage(characterImage,null));
			updateTextField(charactersTextField.get(i), characterList.get(i).getType());
		}
		
		// Display the final prediction result
		updateTextField(recognitionResult, predictionResult);
	}
	
	/**
	 * Update the ImageView in JavaFX main thread.
	 * 
	 * @param view
	 * 			ImageView to be updated
	 * @param image
	 * 			Image to be shown
	 */
	private void updateImageView(ImageView view, Image image){
		Utils.onFXThread(view.imageProperty(), image);
	}
	
	/**
	 * Update the TextField in JavaFX main thread.
	 * 
	 * @param textField
	 * 			TextField to be updated
	 * @param text
	 * 			Text to be shown
	 */
	private void updateTextField(TextField textField, String text){
		textField.setText(text);
	}
	
	/**
	 * Reset all the ImageView objects
	 */
	private void initImageViewArray(){
		charactersImageView = new ArrayList<ImageView>();
		charactersImageView.add(character1);
		charactersImageView.add(character2);
		charactersImageView.add(character3);
		charactersImageView.add(character4);
		charactersImageView.add(character5);
		charactersImageView.add(character6);
		charactersImageView.add(character7);
		charactersImageView.add(character8);
		character1.setImage(null);
		character2.setImage(null);
		character3.setImage(null);
		character4.setImage(null);
		character5.setImage(null);
		character6.setImage(null);
		character7.setImage(null);
		character8.setImage(null);
	}

	/**
	 * Reset all the TextField objects
	 */
	private void initTextFieldArray(){
		charactersTextField = new ArrayList<TextField>();
		charactersTextField.add(character1Type);
		charactersTextField.add(character2Type);
		charactersTextField.add(character3Type);
		charactersTextField.add(character4Type);
		charactersTextField.add(character5Type);
		charactersTextField.add(character6Type);
		charactersTextField.add(character7Type);
		charactersTextField.add(character8Type);
		character1Type.setText("Character");
		character2Type.setText("Character");
		character3Type.setText("Character");
		character4Type.setText("Character");
		character5Type.setText("Character");
		character6Type.setText("Character");
		character7Type.setText("Character");
		character8Type.setText("Character");
		character1Type.setStyle(null);
		character2Type.setStyle(null);
		character3Type.setStyle(null);
		character4Type.setStyle(null);
		character5Type.setStyle(null);
		character6Type.setStyle(null);
		character7Type.setStyle(null);
		character8Type.setStyle(null);
	}
	
}
