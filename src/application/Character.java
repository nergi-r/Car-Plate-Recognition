package application;

public class Character extends Contour{

	private String type;
	
	/**
	 * Creates a Character object
	 * with a Contour matrix of size
	 * @param h x @param w
	 * and a classification type (Alphabet / Number).
	 * 
	 * @param h
	 * @param w
	 */
	public Character(int h, int w) {
		super(h, w);
	}

	/**
	 * @return Current type as a String (alphabetType / numberType)
	 */
	public String getType(){
		return type;
	}
	
	/**
	 * @param type
	 * 			The type of Character as a String (alphabetType / numberType)
	 */
	public void setType(String type){
		this.type = type;
	}
	
}
