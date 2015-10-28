package model;

import java.util.Collection;
import java.util.Iterator;

public class Shapes implements Iterable<Shape> {
	private Collection<Shape> shapes;
	
	public void add(Shape shape){
		shapes.add(shape);
	}

	@Override
	public Iterator<Shape> iterator() {
		return shapes.iterator();
	}
	
	public void clear(){
		shapes.clear();
	}
}
