package application;

public class Position {
	private int x;
	private int y;
	
	public Position (int y,int x){
		this.y = y;
		this.x = x;
	}
	
	public int getY(){
		return y;
	}
	
	public void setY(int y){
		this.y = y;
	}
	
	public int getX(){
		return x;
	}
	
	public void setX(int x){
		this.x = x;
	}
}
