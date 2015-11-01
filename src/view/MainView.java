package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import presenter.Presenter;

public class MainView implements View, ActionListener{

	Presenter presenter;
	
	private JFrame frame;

	JMenuBar menuBar;
	JMenu mnFile;
	JMenuItem mntmOpen;
	JMenuItem mntmSave;
	JMenuItem mntmSaveAs;
	JMenuItem mntmClose;

	/**
	 * Create the application.
	 */
	public MainView() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
	
		menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 129, 21);
		frame.getContentPane().add(menuBar);
		
		mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		mntmOpen = new JMenuItem("Open");
		mnFile.add(mntmOpen);
		mntmOpen.addActionListener(this);
		
		mntmSave = new JMenuItem("Save");
		mnFile.add(mntmSave);
		mntmSave.addActionListener(this);
		
		mntmSaveAs = new JMenuItem("Save as");
		mnFile.add(mntmSaveAs);
		mntmSaveAs.addActionListener(this);
		
		mntmClose = new JMenuItem("Close");
		mnFile.add(mntmClose);
		mntmClose.addActionListener(this);
		
		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == mntmOpen)
			try {
				load();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		else if(e.getSource() == mntmSave)
			try {
				save();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		else if(e.getSource() == mntmSaveAs)
			try {
				saveAs();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		else if(e.getSource() == mntmClose){
			if(presenter.isSaved())
				System.exit(0);
			else {
				//Custom button text
				Object[] options = {"Yes",
				                    "No",
				                    "Cancel"};
				int n = JOptionPane.showOptionDialog(frame,
				    "Would you like to save?",
				    "Save?",
				    JOptionPane.YES_NO_CANCEL_OPTION,
				    JOptionPane.QUESTION_MESSAGE,
				    null,
				    options,
				    options[2]);
				
				if(n==0)
					try {
						save();
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				else if(n==1)
					System.exit(0);
			}
			
		}
	}
	
	private void load() throws FileNotFoundException{
		JFileChooser fileChooser = new JFileChooser();
		if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
			File file = fileChooser.getSelectedFile();
			presenter.load(file);
		}
	}
	
	private void save() throws FileNotFoundException{
		if(presenter.hasFile())
			presenter.save();
		else
			saveAs();
	}
	
	private void saveAs() throws FileNotFoundException{
		JFileChooser fileChooser = new JFileChooser();
		if(fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION){
			File file = fileChooser.getSelectedFile();
			presenter.saveAs(file);
		}
	}
	
	@Override
	public void updateView() {
		frame.repaint();
	}
	
	@Override
	public void setPresenter(Presenter presenter){
		this.presenter = presenter;
	}
}
