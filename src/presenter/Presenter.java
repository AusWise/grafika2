package presenter;

import java.io.File;
import java.io.FileNotFoundException;

import view.View;
import model.ShapesFile;


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
}
