package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JScrollBar;

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
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;

import javax.swing.ScrollPaneConstants;

public class MainView extends JFrame implements View, ActionListener, ListSelectionListener, MouseListener, MouseMotionListener, KeyListener{
	private Presenter presenter;

	private JFrame frame;
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
	private JPanel imagePanel;
	
	private enum ShapeType {
		NULL,
		RECTANGLE, 
		ELLIPSE, 
		POLYGON
	};
	
	Image image;
	
	private List<Integer> xPointsList = new ArrayList<Integer>();
	private List<Integer> yPointsList = new ArrayList<Integer>();
	
	private ShapeType addingShape = ShapeType.NULL;  
	
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
		this.frame = this;
		
		this.setMinimumSize(new Dimension(350,200));
		this.setBounds(100, 100, 700, 400);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		table = new JTable(
				new DefaultTableModel(
			new Object[][] {},
			new String[] {
				"Nr", "Name"
			}
		));
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFillsViewportHeight(true);
		ListSelectionModel listSelectionModel = table.getSelectionModel();
		listSelectionModel.addListSelectionListener(this);
		getContentPane().setLayout(new BorderLayout(0, 0));
		getContentPane().add(table, BorderLayout.EAST);
		table.setDefaultRenderer(Object.class, MyTableCellRenderer.INSTANCE);
	
		menuBar = new JMenuBar();
		this.getContentPane().add(menuBar, BorderLayout.NORTH);
		
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
		
		mntmExport = new JMenuItem("Export");
		mnFile.add(mntmExport);
		mntmExport.addActionListener(this);
		
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
		
		scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setPreferredSize(new Dimension(492, 327));
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		frame.addComponentListener(new ComponentListener(){

			@Override
			public void componentHidden(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void componentMoved(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void componentResized(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				
				java.awt.Rectangle bounds = scrollPane.getViewport().getViewRect();
				Dimension size = scrollPane.getViewport().getViewSize();
				
				int _x = (size.width - bounds.width) / 2;
				int _y = (size.height - bounds.height) / 2;
				
				//scrollPane.getViewport().setViewPosition(new Point(_x, _y));
				
				scrollPane.getViewport().setLocation(_x, _y);
				
			}

			@Override
			public void componentShown(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				
			}});
		
		imagePanel = new JPanel() {
			
			@Override
			public void paintComponent(Graphics g){
				Graphics2D g2d = (Graphics2D)g;
				
				if(image!=null){
					g2d.drawImage(image, 0, 0, null);
					imagePanel.setPreferredSize(new Dimension(image.getWidth(null), image.getHeight(null)));
					
					scrollPane.setViewportView(imagePanel);
				}
				
				Shape shape;
				java.awt.Shape swingShape;
				for(int i=0;i<presenter.size();i++){
					shape = presenter.get(i);
					swingShape = shape.toSwingShape();
					g2d.draw(swingShape);
				}
				
				int width,height, _x, _y;
				switch(addingShape){
					case RECTANGLE:
						width = Math.abs(x-x0);
						height = Math.abs(y-y0);
						_x = Math.min(x,  x0);
						_y = Math.min(y, y0);
						g2d.drawRect(_x, _y, width, height);
						break;
					case ELLIPSE:
						width = Math.abs(x-x0);
						height = Math.abs(y-y0);
						_x = Math.min(x,  x0);
						_y = Math.min(y, y0);
						g2d.drawOval(_x, _y, width, height);
						break;
					case POLYGON:
						for(int i=0;i<xPointsList.size();i++)
							g2d.drawRect(xPointsList.get(i), yPointsList.get(i), 2, 2);
						break;
				}
				
			}
		};
		
		imagePanel.setBackground(Color.WHITE);
		imagePanel.setLayout(new BorderLayout(0, 0));
		
		scrollPane.setViewportView(imagePanel);
		
		imagePanel.addMouseListener(this);
		imagePanel.addMouseMotionListener(this);
		frame.addKeyListener(this);
		
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == mntmOpen)
			try {
				load();
			} catch (IOException e1) {
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
		else if(e.getSource() == mntmExport){
			try {
				this.export();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else if(e.getSource() == mntmClose){
			if(presenter.isSaved() || image == null)
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
						System.exit(0);
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
	
	private void load() throws IOException{
		JFileChooser fileChooser = new JFileChooser();
		if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
			File file = fileChooser.getSelectedFile();
			
			File imageFile = null;
			File shapesFile = null;
			String imageFilePath;
			String shapesFilePath;
			if(file.getName().endsWith("jpg")){
				imageFile = file;
				imageFilePath = imageFile.getAbsolutePath();
				shapesFilePath = imageFilePath.substring(0, imageFilePath.length()-3) + "txt";
				shapesFile = new File(shapesFilePath);
				if(Files.exists(Paths.get(shapesFilePath)))
					shapesFile = new File(shapesFilePath);
				else
					shapesFile = Files.createFile(Paths.get(shapesFilePath)).toFile();
					
				
			}
			else if(file.getName().endsWith("txt")){
				shapesFile = file;
				shapesFilePath = shapesFile.getAbsolutePath();
				imageFilePath = shapesFilePath.substring(0, shapesFilePath.length()-3) + "jpg";
				imageFile = new File(imageFilePath);
			}
			
			
			image = ImageIO.read(imageFile);
			
			presenter.load(shapesFile);
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
	
	private void export() throws IOException{
		JFileChooser fileChooser = new JFileChooser();
		if(fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION){
			File file = fileChooser.getSelectedFile();
			export(file);
		}
	}
	
	private void export(File file) throws IOException{
		BufferedImage exportImage = new BufferedImage(image.getWidth(null),image.getHeight(null),BufferedImage.TYPE_INT_ARGB);
		Graphics g = exportImage.getGraphics();
		g.drawImage(image, 0, 0, null);
		
		Graphics2D g2d = (Graphics2D)g;
		
		Shape shape;
		java.awt.Shape swingShape;
		for(int i=0;i<presenter.size();i++){
			shape = presenter.get(i);
			swingShape = shape.toSwingShape();
			g2d.setColor(Color.BLACK);
			g2d.draw(swingShape);
		}
		
		g.dispose();
		
		ImageIO.write(exportImage, "png", file);
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
		
		this.mnEdit.setEnabled(image != null);
		this.mntmSaveAs.setEnabled(image != null);
		this.mntmExport.setEnabled(image!=null);
	
		this.mntmSave.setEnabled(!presenter.isSaved() && image!= null);
		
		TableModel tableModel = table.getModel();
		DefaultTableModel defaultTableModel = (DefaultTableModel)tableModel;
		defaultTableModel.setRowCount(0);
		
		Shape shape;
		for(int i=0;i<presenter.size();i++){
			shape = presenter.get(i);
			defaultTableModel.addRow(new Object[]{i, shape.getName()});
		}
		
		this.mntmRemove.setEnabled(this.table.getSelectedRow()!=-1);
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
			frame.setFocusable(true);
			frame.requestFocusInWindow();
			
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
	private JScrollPane scrollPane;
	private JMenuItem mntmExport;
	
	@Override
	public void mousePressed(MouseEvent e) {
		if(addingShape == ShapeType.RECTANGLE || addingShape == ShapeType.ELLIPSE){
			x = x0 = e.getX();
			y = y0 = e.getY();
		}
		else{
			x0 = 0;
			y0 = 0;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		int width,height, _x, _y;
		switch(addingShape){
			case RECTANGLE:
				width = Math.abs(x-x0);
				height = Math.abs(y-y0);
				_x = Math.min(x,  x0);
				_y = Math.min(y, y0);
				presenter.add(new Rectangle(_x,_y,width,height));
				addingShape = ShapeType.NULL;
				x = y = x0 = y0 = 0;
				break;
			case ELLIPSE:
				width = Math.abs(x-x0);
				height = Math.abs(y-y0);
				_x = Math.min(x,  x0);
				_y = Math.min(y, y0);
				presenter.add(new Ellipse(_x,_y,width,height));
				addingShape = ShapeType.NULL;
				x = y = x0 = y0 = 0;
				break;
		}
	}

	
	@Override
	public void mouseDragged(MouseEvent e) {
		if(addingShape == ShapeType.RECTANGLE || addingShape == ShapeType.ELLIPSE){
			x = e.getX();
			y = e.getY();
			
			repaint();
		}
		else{
			x = 0;
			y = 0;
		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {}

	@Override
	public void keyPressed(KeyEvent e) {
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
	public void keyReleased(KeyEvent arg0) {}

	@Override
	public void keyTyped(KeyEvent e) {}
	
	
}
