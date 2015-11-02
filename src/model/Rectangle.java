package model;

public class Rectangle implements Shape{
	private int x,y,height,width;
	
	public Rectangle(int x, int y, int width, int height) {
		super();
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
	}

	@Override
	public String toString(){
		return "Rectangle " + x + " " + y + " " + height + " " + width;
	}

	@Override
	public java.awt.Shape toSwingShape() {
		return new java.awt.Rectangle(x,y,width,height);
	}
	
	
}
