package model;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Shapes implements Iterable<Shape> {
	private List<Shape> shapes;
	
	public Shapes(){
		shapes = new LinkedList<Shape>();
	}
	
	public void add(Shape shape){
		shapes.add(shape);
	}
	
	public Shape get(int i){
		return shapes.get(i);
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
	
	public void remove(int i){
		shapes.remove(i);
	}
}
