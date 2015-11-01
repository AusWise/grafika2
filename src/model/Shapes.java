package model;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

public class Shapes implements Iterable<Shape> {
	private Collection<Shape> shapes;
	
	public Shapes(){
		shapes = new LinkedList<Shape>();
	}
	
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
	
	public int size(){
		return shapes.size();
	}
}
