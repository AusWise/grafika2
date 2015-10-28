package model;
import java.util.List;

public class Polygon implements Shape{
	private List<Point> points;
		
	public Polygon(List<Point> points) {
		super();
		this.points = points;
	}

	@Override
	public String toString(){
		String result = "Polygon ";
		
		for(Point point: points)
			result += point.x + " " + point.y + " ";
		
		return result;
	}
}
