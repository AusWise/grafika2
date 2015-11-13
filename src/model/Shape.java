package model;

public abstract class Shape{
	protected String name;
	
	public Shape(){
		this.name = "Shape";
	}
	
	public String getName(){
		return name;
	}
	
	public abstract java.awt.Shape toSwingShape();
}
