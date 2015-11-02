package presenter;

import java.io.File;
import java.io.FileNotFoundException;

import view.View;
import model.ShapesFile;
import model.Shape;


public class Presenter {
	ShapesFile model;
	View view;
	
	public Presenter(ShapesFile model, View view) {
		this.model = model;
		this.view = view;
	}

	public void load(File file) throws FileNotFoundException{
		model.load(file);
		view.updateView();
	}
	
	public void save() throws FileNotFoundException{
		model.save();
		view.updateView();
	}
	
	public void saveAs(File file) throws FileNotFoundException{
		model.saveAs(file);
		view.updateView();
	}
	
	public void updateView(){
		view.updateView();
	}
	
	public boolean isSaved(){
		return model.isSaved();
	}
	
	public boolean hasFile(){
		return model.hasFile();
	}
	
	public int size(){
		return model.size();
	}
	
	public Shape get(int i){
		return model.get(i);
	}
	
	public void remove(int i){
		model.remove(i);
		this.updateView();
	}
	
	public void add(Shape shape){
		model.add(shape);
		this.updateView();
	}
}
