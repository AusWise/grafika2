package model;

public class Ellipse implements Shape{
	private int x,y,height,width;
	
	public Ellipse(int x, int y, int width, int height) {
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

	@Override
	public java.awt.Shape toSwingShape() {
		return new java.awt.geom.Ellipse2D.Double(x,y,width,height);	
	}
}
