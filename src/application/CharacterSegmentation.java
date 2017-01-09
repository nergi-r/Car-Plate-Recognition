package application;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class CharacterSegmentation {
	
	private ArrayList<Character> characterList;
	private int mat[][];
	private boolean vis[][];
	private final double separatorMultiplier = 2.9;
	private final double minToleratedDifference = 0.9;
	private final double maxToleratedDifference = 1.1;
	private final int minimumValidArea = 10;
	private final int minimumValidHeight = 3;
	private final int minimumValidWidth = 2;
	
	/**
	 * Create a character segmentation object that
	 * receives image in the form of a 2D threshold matrix
	 * and able to segment the large matrix into small matrices
	 * consisting of individual separated characters.
	 * 
	 * @param mat
	 * 			2D integer threshold matrix
	 */
	public CharacterSegmentation(int mat[][]) {
		int h = mat.length;
		int w = mat[0].length;
		this.mat = new int[h][w];
		for(int i=0;i<h;i++){
			for(int j=0;j<w;j++){
				this.mat[i][j] = mat[i][j];
			}
		}
		vis = new boolean[h][w];
		characterList = new ArrayList<Character>();
	}
	
	/**
	 * Method that does a brute force search
	 * iterating through each column and row
	 * and return the position of the first occurrence of an
	 * unused white pixel (Value of 255).
	 * 
	 * @return Position (y,x) of unused white pixel
	 */
	private Position findUnusedWhitePixel(){
		int h = mat.length;
		int w = mat[0].length;
		int x = 0, y = 0;
		
		/*
		 * Iterate through all elements in the matrix
		 * based on column first.
		 * 
		 * @return - Position (y,x) if an unused white pixel is found;
		 * 			- null if no white pixel is found 
		 */
		for(int i=0;i<w;i++){
			for(int j=0;j<h;j++){
				if(mat[j][i] == 255 && vis[j][i] == false){
					y = j;
					x = i;
					Position pos = new Position(y,x);
					return pos;
				}
			}
		}
		return null;
	}

	/**
	 * Method to find all the connected white pixels
	 * with the corresponding @param pos and return
	 * the component as a new Character object.
	 * 
	 * (Finding Connected Component)
	 * Done using Breadth-First-Search Algorithm.
	 * 
	 * @param pos
	 * 			The initial position of the first white pixel
	 * @return
	 * 			Matrix of the component as a Character object
	 */
	private Character findCharacter(Position pos){
		int h = mat.length;
		int w = mat[0].length;
		int y = pos.getY();
		int x = pos.getX();
		int nextX, nextY;
		int minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;
		int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;
		
		/*
		 * Direction matrix
		 * [0] = North West	[1] = North	[2] = North East
		 * [3] = West	[4] = East
		 * [5] = South West [6] = South	[7] = South East 
		 */
		int dy[] = {-1, -1, -1,
					 0,      0,
					 1,  1,  1};
		int dx[] = {-1,  0,  1,
					-1,      1,
					-1,  0,  1};
		
		Position currentPos, nextPos;
		Queue<Position> q = new LinkedList<Position>();
		Queue<Position> visitedPoints = new LinkedList<Position>();
		
		vis[y][x] = true;
		q.add(pos);
		
		// Run BFS
		while(!q.isEmpty()){
			// Poll means get front() and then pop()
			currentPos = q.poll();
			y = currentPos.getY();
			x = currentPos.getX();
			maxY = Math.max(maxY, y);
			minY = Math.min(minY, y);
			maxX = Math.max(maxX, x);
			minX = Math.min(minX, x);
			visitedPoints.add(currentPos);

			/*
			 * Traverse to 8 directions to find
			 * the next unvisited white pixels,
			 * and add it to the set of visited points.
			 */
			for(int i=0;i<8;i++){
				nextY = y + dy[i];
				nextX = x + dx[i];
				if(nextY < 0 || nextX < 0 || nextY >=h || nextX >= w) continue;
				
				if(mat[nextY][nextX] == 255 && vis[nextY][nextX] == false){
					vis[nextY][nextX] = true;
					nextPos = new Position(nextY, nextX);
					q.add(nextPos);
				}
			}
		}

		// Determine the size of Character matrix based on the outermost points.
		Character character = new Character(Math.abs(maxY-minY+1), Math.abs(maxX-minX+1));
		
		// Set the pixel of all visited points as white and leave the rest as black.
		while(!visitedPoints.isEmpty()){
			currentPos = visitedPoints.poll();
			y = currentPos.getY();
			x = currentPos.getX();
			character.setPixel(y - minY, x - minX, 255);
		}
		
		return character;
	}
	
	/**
	 * Method to get the list of segmented characters.
	 * 
	 * @return ArrayList of segmented characters
	 */
	public ArrayList<Character> getCharacterList(){
		return characterList;
	}
	
	/**
	 * Procedure to begin the segmentation process.
	 * 
	 * Separating each characters from the whole image matrix
	 * so that they can be used individually as a Character object.
	 * 
	 * Will add all the Characters to an Array List.
	 * 
	 * Segmentation is done by finding all the valid characters.
	 * Then, classify each character to be either alphabet or number
	 * based on the respective character's distance with its left
	 * neighbor character.
	 */
	public void run(){
		int h = mat.length;
		int w = mat[0].length;
		int lastPos = 0;
		int minDistanceBetweenCharacter = Integer.MAX_VALUE;
		int characterCount = 0;
		double averageCloseDistance = 0.0;
		int correctPredictionOfMinDistance = 0;
		Position pos = null;
		ArrayList<Integer> distanceList = new ArrayList<Integer>();
		
		int minHeight = 0, maxHeight = 0;
		boolean first = true;
		
		// While there still might be an unidentified character
		while(true){
			pos = findUnusedWhitePixel();
			if(pos == null){
				break;
			}
			
			/*
			 *  Create a new Character object as a container
			 *  for the connected component found using
			 *  findCharacter method and set all the used
			 *  white pixels visit status to visited.
			 */
			Character character = findCharacter(pos);
			for(int i=0;i<h;i++){
				for(int j=0;j<w;j++){
					if(vis[i][j] == true) mat[i][j] = 0;
				}
			}
			
			/*
			 * The first character's height is used as a reference
			 * to check the validity of all the next characters' height.
			 * 
			 * Thus, we need to validate whether the first found character
			 * really is a identifiable character (not just some random dots or image noise).
			 * 
			 * Validation is based on the character's height, width and area.
			 * Width cannot be greater than height because characters are
			 * always positioned vertically in the images (Height > Width
			 * holds true for all valid characters).
			 * 
			 * Store the minimum and maximum height for the other characters' height
			 * should be, in order to be marked as valid.
			 */
			if(first){
				if(character.getArea() < minimumValidArea
						|| character.getHeight() < minimumValidHeight
						|| character.getWidth() < minimumValidWidth 
						|| character.getWidth() > character.getHeight()) continue;
				minHeight = (int) (character.getHeight() * minToleratedDifference);
				maxHeight = (int) (character.getHeight() * maxToleratedDifference);
				first = false;
			}

			/*
			 * Validate characters based on height.
			 * 
			 * Compute the distance between each character
			 * and save the minimum among them.
			 * 
			 * Distance is calculated based on the rightmost point of Character i
			 * and leftmost point of Character i+1
			 */
			if(character.getHeight() >= minHeight && character.getHeight() <= maxHeight){
				characterList.add(character);
				
				if(characterCount>0){
					minDistanceBetweenCharacter = Math.min(minDistanceBetweenCharacter, pos.getX() - lastPos);
				}
				distanceList.add(pos.getX() - lastPos);
				lastPos = pos.getX() + character.getWidth();
				characterCount++;
			}
		}

		/*
		 * Compute the average of all the distance between characters
		 * which are still below the product of minimum distance and
		 * separator multiplier constant.
		 */
		for(int i=0;i<characterList.size();i++){
			if((double) (distanceList.get(i)) <= separatorMultiplier * (double)(minDistanceBetweenCharacter)){
				averageCloseDistance += (double) (distanceList.get(i));
				correctPredictionOfMinDistance++;
			}
		}
		averageCloseDistance = averageCloseDistance / (double) (correctPredictionOfMinDistance);

		/*
		 * Set each character type to be either alphabet or number
		 * based on its distance to its left neighbor character.
		 * 
		 * If the distance is relatively big (marked as far distance),
		 * 	set the character type to be different from its left neighbor character.
		 * Else
		 * 	set the character type to be the same with its left neighbor character.
		 */
		String currentType = Utils.alphabetType;
		for(int i=0;i<characterList.size();i++){
			Character character = characterList.get(i);
			if(i == 0){
				character.setType(currentType);
			}
			else{
				if((double) (distanceList.get(i)) > separatorMultiplier * (double) (averageCloseDistance)){
					if(currentType == Utils.alphabetType){
						currentType = Utils.numberType;
					}
					else{
						currentType = Utils.alphabetType;
					}
				}
				character.setType(currentType);
			}
		}
		
	}
	
}
