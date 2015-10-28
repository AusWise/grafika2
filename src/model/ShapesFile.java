package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class ShapesFile extends Shapes {
	private File file;
	private boolean saved;
	
	private void setFile(File file){
		this.file = file;
	}
	
	private void setSaved(boolean saved){
		this.saved = saved;
	}
	
	public void load(File file) throws FileNotFoundException{
		this.setFile(file);
		this.load();
		this.setSaved(true);
	}
	
	private void load() throws FileNotFoundException{
		Scanner fileScanner = new Scanner(this.file);
		
		this.clear();
		
		String type, line;
		Scanner lineScanner;
		Shape shape = null;
		while(fileScanner.hasNextLine()){
			line = fileScanner.nextLine();
			lineScanner = new Scanner(line);
			type = lineScanner.next();
			if(type == "Rectangle"){
				int x = lineScanner.nextInt();
				int y = lineScanner.nextInt();
				int height = lineScanner.nextInt();
				int width= lineScanner.nextInt();
				shape = new Rectangle(x,y,height,width);
			}
			else if (type == "Ellipse"){
				int x = lineScanner.nextInt();
				int y = lineScanner.nextInt();
				int height = lineScanner.nextInt();
				int width= lineScanner.nextInt();
				shape = new Ellipse(x,y,height,width);
			}
			else if(type == "Polygon"){
				List<Point> points = new LinkedList<Point>();
				int x,y;
				while(lineScanner.hasNext()){
					x = lineScanner.nextInt();
					y = lineScanner.nextInt();
					points.add(new Point(x, y));
				}
				
				shape = new Polygon(points);
			}
			
			this.add(shape);
		}
	}
	
	public void save() throws FileNotFoundException{
		this.setSaved(saved);
			
		PrintWriter writer = new PrintWriter(this.file);
		for(Shape shape: this)
			writer.println(shape);
	}
		
	public void saveAs(File file) throws FileNotFoundException{
		this.setFile(file);
		this.save();
	}
}
