package main;

import presenter.Presenter;
import view.MainView;
import view.View;
import model.ShapesFile;

public class Main {
	public static void main(String [] args){
		ShapesFile model = new ShapesFile();
		View view = new MainView();
		
		Presenter presenter = new Presenter(model, view);
		
		view.setPresenter(presenter);
	}
}
