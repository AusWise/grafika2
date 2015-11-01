package view;

import presenter.Presenter;


public interface View {
	public void updateView();
	public void setPresenter(Presenter presenter);
}
