package model;
import java.util.List;

public class Polygon extends Shape{
	private int[] xpoints,ypoints;
	private int npoints;
	
	public Polygon(int[] xpoints, int[] ypoints, int npoints) {
		super();
		this.xpoints = xpoints;
		this.ypoints = ypoints;
		this.npoints = npoints;
		super.name = "Polygon";
	}

	@Override
	public java.awt.Shape toSwingShape() {
		return new java.awt.Polygon(xpoints,ypoints,npoints);
	}

	@Override
	public String toString() {
		String result = "Polygon ";
		
		for(int i=0;i<npoints;i++)
			result += xpoints[i] + " " + ypoints[i] + " ";
		
		return result;
	}
	
	
	
}
