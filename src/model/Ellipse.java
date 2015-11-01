package model;

public class Ellipse implements Shape{
	private int x,y,height,width;
	
	public Ellipse(int x, int y, int height, int width) {
		super();
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
	}

	@Override
	public String toString(){
		return "Ellipse " + x + " " + y + " " + height + " " + width;
	}
}
