package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import presenter.Presenter;
import model.Ellipse;
import model.Polygon;
import model.Rectangle;
import model.Shape;

import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.ListSelectionModel;

public class MainView extends JFrame implements View, ActionListener, ListSelectionListener, MouseListener, MouseMotionListener, KeyListener{
	Presenter presenter;

	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenuItem mntmOpen;
	private JMenuItem mntmSave;
	private JMenuItem mntmSaveAs;
	private JMenuItem mntmClose;
	private JTable table;
	private JMenuItem mntmRemove;
	private JMenu mnEdit;
	private JMenu mnAdd;
	private JMenuItem mntmRectangle;
	private JMenuItem mntmOval;
	private JMenuItem mntmPolygon;
	
	private enum ShapeType {
		NULL,
		RECTANGLE, 
		ELLIPSE, 
		POLYGON
	};
	
	private List<Integer> xPointsList = new ArrayList<Integer>();
	private List<Integer> yPointsList = new ArrayList<Integer>();
	
	private ShapeType addingShape = ShapeType.NULL;  
	
	/**
	 * Create the application.
	 */
	public MainView() {
		initialize();
		this.addKeyListener(this);
		this.setFocusable(true);
		this.requestFocusInWindow();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.setBounds(100, 100, 450, 300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(null);
		
		table = new JTable(
				new DefaultTableModel(
			new Object[][] {},
			new String[] {
				"Nr", "Name", "Delete"
			}
		));
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFillsViewportHeight(true);
		table.setBounds(213, 12, 225, 260);
		ListSelectionModel listSelectionModel = table.getSelectionModel();
		listSelectionModel.addListSelectionListener(this);
		getContentPane().add(table);
		table.setDefaultRenderer(Object.class, MyTableCellRenderer.INSTANCE);
	
		menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 129, 21);
		this.getContentPane().add(menuBar);
		
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
		
		mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		
		mnAdd = new JMenu("Add");
		mnEdit.add(mnAdd);
		
		mntmRectangle = new JMenuItem("Rectangle");
		mnAdd.add(mntmRectangle);
		mntmRectangle.addActionListener(this);
		
		mntmOval = new JMenuItem("Oval");
		mnAdd.add(mntmOval);
		mntmOval.addActionListener(this);
		
		mntmPolygon = new JMenuItem("Polygon");
		mnAdd.add(mntmPolygon);
		mntmPolygon.addActionListener(this);
		
		mntmRemove = new JMenuItem("Remove");
		mnEdit.add(mntmRemove);
		mntmRemove.addActionListener(this);
		
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		
		
		this.setVisible(true);
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
				int n = JOptionPane.showOptionDialog(this,
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
		else if(e.getSource() == mntmRemove){
			int row = table.getSelectedRow();
			presenter.remove(row);
		}
		else if(e.getSource() == mntmRectangle)
			addingShape = ShapeType.RECTANGLE;
		else if(e.getSource() == mntmOval)
			addingShape = ShapeType.ELLIPSE;
		else if(e.getSource() == mntmPolygon)
			addingShape = ShapeType.POLYGON;
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
		this.repaint();
	}
	
	@Override
	public void setPresenter(Presenter presenter){
		this.presenter = presenter;
	}
	
	@Override
	public void paint(Graphics g){
		super.paint(g);
	
		this.mntmSave.setEnabled(!presenter.isSaved());
		
		Graphics2D g2d = (Graphics2D)g;
		
		TableModel tableModel = table.getModel();
		DefaultTableModel defaultTableModel = (DefaultTableModel)tableModel;
		defaultTableModel.setRowCount(0);
		
		Shape shape;
		java.awt.Shape swingShape;
		for(int i=0;i<presenter.size();i++){
			shape = presenter.get(i);
			swingShape = shape.toSwingShape();
			g2d.draw(swingShape);
			
			defaultTableModel.addRow(new Object[]{i, "Shape", "x"});
		}
		
		this.mntmRemove.setEnabled(this.table.getSelectedRow()!=-1);
		
		int width,height;
		switch(addingShape){
			case RECTANGLE:
				width = x-x0;
				height = y-y0;
				g2d.drawRect(x0, y0, width, height);
				break;
			case ELLIPSE:
				width = x-x0;
				height = y-y0;
				g2d.drawOval(x0, y0, width, height);
				break;
			case POLYGON:
				for(int i=0;i<xPointsList.size();i++)
					g2d.drawRect(xPointsList.get(i), yPointsList.get(i), 2, 2);
				break;
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		this.mntmRemove.setEnabled(this.table.getSelectedRow()!=-1);
	}
	
	private static class MyTableCellRenderer implements TableCellRenderer{
		
		public static final TableCellRenderer INSTANCE = new MyTableCellRenderer();
		
		private MyTableCellRenderer(){}
		
		JLabel jLabel;
		
		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			jLabel = new JLabel();
			jLabel.setOpaque(true);
			jLabel.setText("" + value);
			if(isSelected)
				jLabel.setBackground(Color.BLUE);
			else if(row%2==1)
				jLabel.setBackground(Color.GRAY);
			return jLabel;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(addingShape == ShapeType.POLYGON){
			this.xPointsList.add(e.getX());
			this.yPointsList.add(e.getY());
			this.repaint();
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}


	private int x0, y0, x, y;
	
	
	@Override
	public void mousePressed(MouseEvent e) {
		 x0 = e.getX();
		 y0 = e.getY();
		 
		 
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	
		int width,height;
		switch(addingShape){
			case RECTANGLE:
				width = x-x0;
				height = y-y0;
				presenter.add(new Rectangle(x0,y0,width,height));
				addingShape = ShapeType.NULL;
				break;
			case ELLIPSE:
				width = x-x0;
				height = y-y0;
				presenter.add(new Ellipse(x0,y0,width,height));
				addingShape = ShapeType.NULL;
				break;
		}
		
		
	}

	
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
//		this.repaint();
		
		x = e.getX();
		y = e.getY();
		
		repaint();
		
//		Graphics g = this.getGraphics();
//		Graphics2D g2d = (Graphics2D)g;
//		
//		switch(addingShape){
//			case RECTANGLE:
//				g2d.drawRect(x0, y0, x-x0, y-y0);
//			case ELLIPSE:
//			case POLYGON:
//		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		System.out.println(e.getKeyCode());
		if(addingShape == ShapeType.POLYGON && e.getKeyCode()==KeyEvent.VK_ENTER){	
			System.out.println("Enter");
			int npoints = xPointsList.size();
			int [] xpoints = new int[npoints];
			int [] ypoints = new int[npoints];
			
			for(int i=0;i<npoints;i++){
				xpoints[i] = xPointsList.get(i);
				ypoints[i] = yPointsList.get(i);
			}
			
			presenter.add(new Polygon(xpoints, ypoints, npoints));
			
			addingShape = ShapeType.NULL;
			xPointsList.clear();
			yPointsList.clear();
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
			
			
	}
}
