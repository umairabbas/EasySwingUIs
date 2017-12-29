import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.MouseInputListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.Position;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import SwingMan.BasicGUITool;
import SwingMan.SwingThing;
import java.awt.SystemColor;
import javax.swing.JScrollBar;

	/*|---------------------------------------------|
	 *|	BasicGUITool figures						|
	 *|---------------------------------------------|
	 *| private JPanel statusBar;					|
	 *|	protected ArrayList<SwingThing> figures;	|
	 *|	protected JPanel toolPanel, drawPanel;		|
	 *|	protected JLabel statusLabel;				|
	 *|	protected Container contentPane;			|
	 *|	   contentPane.setLayout(new BorderLayout())|
	 *|---------------------------------------------|
	*/

public class SwingUIEditor extends BasicGUITool {
 
	/**
	 * 
	 */
	private static final long serialVersionUID = -8397025653395705508L;
	private JMenuBar menuBar;
	private JMenu mnuFile,mnuEdit,mnuObject,mnHelp;
	private JButton cutButton, copyButton, pasteButton, undoButton, redoButton, deleteButton;
	private JMenuItem cutItem, copyItem, pasteItem, undoItem, redoItem, deleteItem;
	private ToolBox wtb;
	private PropertiesPanel pp;
	private MyJFrame myDesignFrame;
	private EventHandler eh;
	private JFileChooser fileChooser;
	private int btnCount =0, lblCount =0, textAreaCount =0, textFieldCount =0, 
				checkBoxCount=0, radioButtonCount=0, toggleButtonCount=0, comboBoxCount=0,
				timerCount=0, fileChooserCount=0, buttonGroupCount=0,
				ehCount=0, btnGroupCount=0;
	private Color darkerDrawPanelBG;
	private DefaultMutableTreeNode root;
	private DefaultTreeModel treeModel;
	private TreePath path;
	private JSplitPane leftSplitPane;
	private JToolBar toolBar;
	private JPopupMenu popupMenu;
	//private ArrayList<String[]> changesMade=new ArrayList<String[]>();
	private String appendedSource="";
	private ArrayList<MenuObject> menuObjects;
	private SwingUIEditorFilter thisFilter= new SwingUIEditorFilter();
	private JavaFileFilter javaFilter = new JavaFileFilter();
	
	public SwingUIEditor() {
		setVisible(false);
		setTitle("EasySwingUIs");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		darkerDrawPanelBG=drawPanel.getBackground().darker().darker();
		drawPanel.setBackground(darkerDrawPanelBG);

		contentPane.remove(drawPanel);
		
		wtb = new ToolBox();
		toolPanel.add(wtb);
		toolPanel.setBorder(new CompoundBorder(new MatteBorder(0, 1, 0, 0,Color.BLACK ), new CompoundBorder(new MatteBorder(0, 1, 0, 0, new Color(105, 105, 105)), new MatteBorder(0, 1, 0, 0, new Color(160, 160, 160)))));
		
		eh = new EventHandler();

		//resize from the BasicGUITool to allow for
		//our implementations
		setBounds(0,0,975,725);
		setPreferredSize(new Dimension(975,725));
		fileChooser=new JFileChooser();
		
		//build the rest of the gui
		buildMenu();
		buildToolbar();

		//now resize the components to reflect the new dimensions
		toolPanel.setPreferredSize(new Dimension(195,600));
		
		//customize the toolPanel to reflect our design
		toolPanel.setBackground(new Color(238,238,238));
		pp = new PropertiesPanel(this);
		pp.setPreferredSize(new Dimension(195,600));
		
		leftSplitPane=new JSplitPane();
		leftSplitPane.setContinuousLayout(true);
		leftSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		leftSplitPane.setRightComponent(drawPanel);
		
		newFile();
		getMyDesignFrame().setBorder(new BevelBorder(BevelBorder.RAISED));

		root = new DefaultMutableTreeNode(getMyDesignFrame());
		
		treeModel = new DefaultTreeModel(root);
		pp.objTree.setModel(treeModel);
		pp.objTree.setEditable(false);
		pp.objTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		pp.objTree.setShowsRootHandles(true);
		eh.selected=getMyDesignFrame();
		getMyDesignFrame().highLight();
		eh.updateTree(getMyDesignFrame().toString().substring(0,getMyDesignFrame().toString().indexOf(getMyDesignFrame().getName())+1));
		pp.objTree.addTreeSelectionListener(eh);
		
		contentPane.add(leftSplitPane, BorderLayout.CENTER);
		pp.loadProperties(getMyDesignFrame());
		
		popupMenu = new JPopupMenu();
		eh.addPopup(getMyDesignFrame(), popupMenu);
		eh.addPopup(getMyDesignFrame().contentPanel, popupMenu);
		
		JMenuItem menuItem = new JMenuItem("Cut");
		menuItem.addActionListener(eh);
		popupMenu.add(menuItem);
		menuItem = new JMenuItem("Copy");
		menuItem.addActionListener(eh);
		popupMenu.add(menuItem);
		menuItem=new JMenuItem("Paste");
		menuItem.addActionListener(eh);
		popupMenu.add(menuItem);
		menuItem=new JMenuItem("Delete Object");
		menuItem.setActionCommand("Delete");
		menuItem.addActionListener(eh);
		popupMenu.add(menuItem);
		popupMenu.addSeparator();
		menuItem=new JMenuItem("View Object Source");
		menuItem.setActionCommand("ViewSource");
		menuItem.addActionListener(eh);
		popupMenu.add(menuItem);

		//changesMade.add(0,figures.toString().substring(1,figures.toString().length()-1).split(", J"));
	}
	
	
	public void callRefresh() {
		refresh();
	}
	public DefaultMutableTreeNode addObject(Object child) {
	    DefaultMutableTreeNode parentNode = root;
	    pp.loadProperties((Editable) child);
	    return addObject(parentNode, child, true);
	}
	
	public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent,
	                                        Object child,
	                                        boolean shouldBeVisible) {
	    DefaultMutableTreeNode childNode =
	            new DefaultMutableTreeNode(child);
	    treeModel.insertNodeInto(childNode, parent,
	                             parent.getChildCount());

	    //Make sure the user can see the lovely new node.
	    if (shouldBeVisible) {
	    	pp.objTree.scrollPathToVisible(new TreePath(childNode.getPath()));
	    }
	    return childNode;
	}

	private void buildMenu() {
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		menuBar.setBackground(new Color(176, 196, 222));
		
		mnuFile = new JMenu("File");
		mnuFile.setMnemonic('F');
		menuBar.add(mnuFile);
		
		JMenuItem menuItem = new JMenuItem("New");
		menuItem.addActionListener(eh);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		mnuFile.add(menuItem);
		
		menuItem = new JMenuItem("Open");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		menuItem.addActionListener(eh);
		mnuFile.add(menuItem);
		
		menuItem = new JMenuItem("Save");
		menuItem.addActionListener(eh);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mnuFile.add(menuItem);
		
		mnuFile.addSeparator();
		
		/*
		menuItem = new JMenuItem("Import");
		menuItem.addActionListener(eh);
		mnuFile.add(menuItem);
		*/
		menuItem = new JMenuItem("Export");
		menuItem.addActionListener(eh);
		mnuFile.add(menuItem);
		
		mnuFile.addSeparator();
		
		menuItem = new JMenuItem("Exit");
		menuItem.addActionListener(eh);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, InputEvent.CTRL_MASK));
		mnuFile.add(menuItem);
		
		mnuEdit = new JMenu("Edit");
		mnuEdit.setMnemonic('E');
		menuBar.add(mnuEdit);
		
		cutItem = new JMenuItem("Cut");
		cutItem.setEnabled(false);
		cutItem.addActionListener(eh);
		cutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));
		mnuEdit.add(cutItem);
		
		copyItem = new JMenuItem("Copy");
		copyItem.setEnabled(false);
		copyItem.addActionListener(eh);
		copyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
		mnuEdit.add(copyItem);
		
		pasteItem = new JMenuItem("Paste");
		pasteItem.setEnabled(false);
		pasteItem.addActionListener(eh);
		pasteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
		mnuEdit.add(pasteItem);
		
		mnuEdit.addSeparator();
		
		deleteItem=new JMenuItem("Delete Object");
		deleteItem.setActionCommand("Delete");
		deleteItem.addActionListener(eh);
		deleteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
		mnuEdit.add(deleteItem);

		mnuEdit.addSeparator();
		
		menuItem=new JMenuItem("Edit Full Source");
		menuItem.setActionCommand("FullSource");
		menuItem.addActionListener(eh);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK));
		mnuEdit.add(menuItem);
		
		/*undoItem = new JMenuItem("Undo");
		undoItem.setEnabled(false);
		undoItem.addActionListener(eh);
		undoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK));
		mnuEdit.add(undoItem);
		
		redoItem = new JMenuItem("Redo");
		redoItem.setEnabled(false);
		redoItem.addActionListener(eh);
		redoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_MASK));
		mnuEdit.add(redoItem);
		 */
		/*mnuObject = new JMenu("Object");
		mnuObject.setMnemonic('O');
		menuBar.add(mnuObject);
		
		menuItem = new JMenuItem("Add Event Handler");
		menuItem.setActionCommand("AddEH");
		menuItem.addActionListener(eh);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK));
		mnuObject.add(menuItem);
		
		menuItem = new JMenuItem("Set Look and Feel");
		menuItem.setActionCommand("SetLnF");
		menuItem.addActionListener(eh);
		mnuObject.add(menuItem);
		
		mnuObject.addSeparator();
		
		menuItem = new JMenuItem("Export Object as a Separate Class");
		menuItem.setEnabled(false);
		menuItem.addActionListener(eh);
		menuItem.setActionCommand("ExportObj");
		mnuObject.add(menuItem);
		*/
		
		mnHelp = new JMenu("Help/About");
		menuBar.add(mnHelp);
		
		menuItem = new JMenuItem("Software Description and Usage Overview");
		menuItem.setActionCommand("HelpUse");
		menuItem.addActionListener(eh);
		mnHelp.add(menuItem);
		
		mnHelp.addSeparator();
		
		menuItem = new JMenuItem("About EasySwingUI");
		menuItem.setActionCommand("About");
		menuItem.addActionListener(eh);
		mnHelp.add(menuItem);
		
		setJMenuBar(menuBar);
	}
	private void buildToolbar() {
		ImageIcon[] tbIcons = loadIcons();
		toolBar = new JToolBar();
		toolBar.setBackground(new Color(95, 158, 160));
		contentPane.add(toolBar, BorderLayout.NORTH);
		
		JButton tbBtn = new JButton(" New ");
		tbBtn.setIcon(tbIcons[0]);
		tbBtn.setForeground(new Color(250, 250, 210));
		tbBtn.setActionCommand("New");
		tbBtn.setBorder(new LineBorder(new Color(255, 215, 0), 1, true));
		tbBtn.setBackground(new Color(95, 158, 160));
		toolBar.add(tbBtn);
		
		tbBtn = new JButton(" Open ");
		tbBtn.setIcon(tbIcons[1]);
		tbBtn.setActionCommand("Open");
		tbBtn.setBorder(new LineBorder(new Color(255, 215, 0), 1, true));
		tbBtn.setForeground(new Color(250, 250, 210));
		tbBtn.setBackground(new Color(95, 158, 160));
		toolBar.add(tbBtn);
		
		tbBtn = new JButton(" Save GUI ");
		tbBtn.setIcon(tbIcons[2]);
		tbBtn.setActionCommand("Save");
		tbBtn.setBorder(new LineBorder(new Color(255, 215, 0), 1, true));
		tbBtn.setForeground(new Color(250, 250, 210));
		tbBtn.setBackground(new Color(95, 158, 160));
		toolBar.add(tbBtn);
		
		cutButton = new JButton(" Cut ");
		cutButton.setIcon(tbIcons[4]);
		cutButton.setActionCommand("Cut");
		cutButton.setBorder(new LineBorder(new Color(255, 215, 0), 1, true));
		cutButton.setForeground(new Color(250, 250, 210));
		cutButton.setBackground(new Color(95, 158, 160));
		cutButton.setEnabled(false);
		toolBar.add(cutButton);
		
		copyButton = new JButton(" Copy ");
		copyButton.setIcon(tbIcons[5]);
		copyButton.setActionCommand("Copy");
		copyButton.setBorder(new LineBorder(new Color(255, 215, 0), 1, true));
		copyButton.setForeground(new Color(250, 250, 210));
		copyButton.setBackground(new Color(95, 158, 160));
		copyButton.setEnabled(false);
		toolBar.add(copyButton);
		
		pasteButton = new JButton(" Paste ");
		pasteButton.setEnabled(false);
		pasteButton.setIcon(tbIcons[6]);
		pasteButton.setActionCommand("Paste");
		pasteButton.setBorder(new LineBorder(new Color(255, 215, 0), 1, true));
		pasteButton.setForeground(new Color(250, 250, 210));
		pasteButton.setBackground(new Color(95, 158, 160));
		toolBar.add(pasteButton);
		
		/*
		undoButton = new JButton(" Undo ");
		undoButton.setEnabled(false);
		undoButton.setIcon(tbIcons[7]);
		undoButton.setActionCommand("Undo");
		undoButton.setBorder(new LineBorder(new Color(255, 215, 0), 1, true));
		undoButton.setForeground(new Color(250, 250, 210));
		undoButton.setBackground(new Color(95, 158, 160));
		toolBar.add(undoButton);
		
		JLabel label = new JLabel(" | ");
		toolBar.add(label);
		
		redoButton = new JButton(" Redo ");
		redoButton.setEnabled(false);
		redoButton.setIcon(tbIcons[8]);
		redoButton.setActionCommand("Redo");
		redoButton.setHorizontalTextPosition(SwingConstants.LEFT);
		redoButton.setBorder(new LineBorder(new Color(255, 215, 0), 1, true));
		redoButton.setForeground(new Color(250, 250, 210));
		redoButton.setBackground(new Color(95, 158, 160));
		toolBar.add(redoButton);
		*/
		
		deleteButton = new JButton(" Delete ");
		deleteButton.setIcon(tbIcons[9]);
		deleteButton.setActionCommand("Delete");
		deleteButton.setBorder(new LineBorder(new Color(255, 215, 0), 1, true));
		deleteButton.setForeground(new Color(250, 250, 210));
		deleteButton.setBackground(new Color(95, 158, 160));
		deleteButton.setEnabled(false);
		toolBar.add(deleteButton);
	
	}
	private ImageIcon[] loadIcons() {
		ImageIcon[] icons = new ImageIcon[10];
		String path="icos/toolbar";
		String ext=".png";
		for (int i=0;i<icons.length;i++){
			icons[i]=createImageIcon(path+i+ext,"ToolBar Icon");
		}
		return icons;
	}
	/** Returns an ImageIcon, or null if the path was invalid. 
	 * 			From: oracle.com*/
	protected ImageIcon createImageIcon(String path,
	                                           String description) {
	    java.net.URL imgURL = getClass().getResource(path);
	    if (imgURL != null) {
	        return new ImageIcon(imgURL, description);
	    } else {
	        System.err.println("Couldn't find file: " + path);
	        return null;
	    }
	}
	@Override
	protected void readFiguresFromFile(ObjectInputStream inStream) {
		//Dimension size = (Dimension) inStream.readObject(  );
        try {
			int figuresSize = inStream.readInt();
			int menuObjectSize=inStream.readInt();
			figures.clear();
			menuObjects.clear();
			drawPanel.removeAll();
			setMyDesignFrame((MyJFrame) inStream.readObject());
			getMyDesignFrame().addTo(drawPanel);
			if (eh.hasMenu) {
				wtb.menuObjectPanel.add(eh.mnuObjHolder, 0);
			}
			eh = new EventHandler();
			figures.add(getMyDesignFrame());
			addEventListeners();
			getMyDesignFrame().setBorder(new BevelBorder(BevelBorder.RAISED));
			wtb.setToolsToSelection();
			pp = new PropertiesPanel(this);
			leftSplitPane.setLeftComponent(pp);
			root = new DefaultMutableTreeNode(getMyDesignFrame());
			
			treeModel = new DefaultTreeModel(root);
			pp.objTree.setModel(treeModel);
			pp.objTree.setEditable(false);
			pp.objTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
			pp.objTree.setShowsRootHandles(true);
			eh.selected=getMyDesignFrame();
			getMyDesignFrame().highLight();
			eh.updateTree(getMyDesignFrame().toString().substring(0,getMyDesignFrame().toString().indexOf(getMyDesignFrame().getName())+1));
			pp.objTree.addTreeSelectionListener(eh);
			for (int i=1;i<figuresSize;i++){
				figures.add((SwingThing) inStream.readObject());
				addObject(figures.get(i));
			}
			for (int i=0;i<menuObjectSize;i++) {
				menuObjects.add((MenuObject) inStream.readObject());
				path = pp.objTree.getNextMatch(getMyDesignFrame().menuPanel.toString(), 0, Position.Bias.Forward);
				addObject((DefaultMutableTreeNode) path.getLastPathComponent(),menuObjects.get(i),true);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        repaint();
	}

	@Override
	protected void saveFiguresToFile(ObjectOutputStream outStream) {
		try {
			outStream.writeInt(figures.size());
			outStream.writeInt(menuObjects.size());
			for (int i=0;i<figures.size();i++) outStream.writeObject(figures.get(i));
			for (int i=0;i<menuObjects.size();i++) outStream.writeObject(menuObjects.get(i));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
            //Close the ObjectOutputStream
            try {
                if (outStream != null) {
                    outStream.flush();
                    outStream.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
	}

	public ArrayList<SwingThing> getFigures() {
		return figures;
	}
	public ArrayList<MenuObject> getMenuObjects() {
		return menuObjects;
	}
	public String getSource(){
		if (!figures.isEmpty() && figures.get(0)!=null){
			String headers =((Editable)figures.get(0)).getHeaderInfo(), preConstructor="", constructor="", code;

			for (int i=1;i<figures.size();i++){
				Editable s = (Editable) figures.get(i);
				headers+=s.getHeaderInfo();
				if ( (!s.getScope().equals("local")) && (!s.getScope().equals("isClass"))){
					preConstructor+="\n\t"+s.getScope()+" "+s.getType()+" "+s.getName()+";";
				}
				constructor+=s.describeInCode();
				if (!(s.toString().startsWith("JMenu")) && !(s instanceof MenuObject))constructor+="\t\t"+getMyDesignFrame().getContentPaneName()+".add("+s.getName()+");\n\n";
			}
			for (int j=0;j<menuObjects.size();j++){
				MenuObject m = menuObjects.get(j);
				constructor+=m.describeInCode();
				if(m instanceof MyJMenu) constructor+="\n\t\t"+((MyJMenu)m).parentBar.getName()+".add("+m.getName()+");\n";
			}
			headers+="\n\n";
			String[] lines =((Editable)figures.get(0)).describeInCode().split("\n");
			code = headers + lines[0] + preConstructor+"\n";
			for (int i=1;i<lines.length;i++) code+="\n"+lines[i];
			code+=constructor+"\n\t\tadd("+getMyDesignFrame().getContentPaneName()+");";
			code+="\n\t}\n";
			code+="\n\tpublic static void main(String[] args) {\n\t\t"+getMyDesignFrame().getName()+" "+getMyDesignFrame().getName().substring(0,1).toLowerCase()+"=new "+getMyDesignFrame().getName()+"();\n\t\t"+getMyDesignFrame().getName().substring(0,1).toLowerCase()+".setVisible(true);\n\t}\n\n";
			return code;
		} else {
			return null;
		}
	}
	@Override
	protected void exportJavaCode(File javaSourceFile) {
		if (!figures.isEmpty() && figures.get(0)!=null){
			String headers =((Editable)figures.get(0)).getHeaderInfo(), preConstructor="", constructor="", code;

			for (int i=1;i<figures.size();i++){
				Editable s = (Editable) figures.get(i);
				headers+=s.getHeaderInfo();
				if ( (!s.getScope().equals("local")) && (!s.getScope().equals("isClass"))){
					preConstructor+="\n\t"+s.getScope()+" "+s.getType()+" "+s.getName()+";";
				}
				constructor+=s.describeInCode();
				if (!(s.toString().startsWith("JMenu")) && !(s instanceof MenuObject))constructor+="\t\t"+getMyDesignFrame().getContentPaneName()+".add("+s.getName()+");\n\n";
			}
			for (int j=0;j<menuObjects.size();j++){
				MenuObject m = menuObjects.get(j);
				constructor+=m.describeInCode();
				if(m instanceof MyJMenu) constructor+="\n\t\t"+((MyJMenu)m).parentBar.getName()+".add("+m.getName()+");\n";
			}
			headers+="\n\n";
			String[] lines =((Editable)figures.get(0)).describeInCode().split("\n");
			code = headers + lines[0] + preConstructor+"\n";
			for (int i=1;i<lines.length;i++) code+="\n"+lines[i];
			code+=constructor+"\n\t\tadd("+getMyDesignFrame().getContentPaneName()+");";
			code+="\n\t}\n";
			code+="\n\tpublic static void main(String[] args) {\n\t\t"+getMyDesignFrame().getName()+" "+getMyDesignFrame().getName().substring(0,1).toLowerCase()+"=new "+getMyDesignFrame().getName()+"();\n\t\t"+getMyDesignFrame().getName().substring(0,1).toLowerCase()+".setVisible(true);\n\t}\n\n";
			code+=appendedSource+"\n\n}";
			try {
				FileWriter writer = new FileWriter(javaSourceFile);
				writer.write(code);
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}		
	}
	public void openFile(){
		fileChooser.setFileFilter(thisFilter);
		 int returnVal = fileChooser.showOpenDialog(SwingUIEditor.this);
	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	        	File file = fileChooser.getSelectedFile();
	        	FileInputStream istream;
				try {
					istream = new FileInputStream(file);
		            ObjectInputStream ois = new ObjectInputStream(istream);
		        	readFiguresFromFile(ois);

				} catch (FileNotFoundException e) {
					e.printStackTrace();
					statusLabel.setText("There was an error retrieving your file...");
				} catch (IOException e) {
					statusLabel.setText("There was an error retrieving your file...");
					e.printStackTrace();
				}
	        }
	}
	public void exportFile(){
		fileChooser.setFileFilter(javaFilter);
        int returnVal = fileChooser.showSaveDialog(SwingUIEditor.this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
        	File file;
        	if (fileChooser.getSelectedFile().getPath().substring(fileChooser.getSelectedFile().getPath().lastIndexOf('.')+1,fileChooser.getSelectedFile().getPath().length()).equalsIgnoreCase("java")){
            	file = fileChooser.getSelectedFile();
            } else {
				file =new File(fileChooser.getSelectedFile().getPath()+".java");
			}
        	exportJavaCode(file);
        }
	}
	public void saveFile(){
		fileChooser.setFileFilter(thisFilter);
        int returnVal = fileChooser.showSaveDialog(SwingUIEditor.this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file;
			if (fileChooser.getSelectedFile().getPath().substring(fileChooser.getSelectedFile().getPath().lastIndexOf('.')+1,fileChooser.getSelectedFile().getPath().length()).equalsIgnoreCase("jForm")){
            	file = fileChooser.getSelectedFile();
            } else {
				file =new File(fileChooser.getSelectedFile().getPath()+".jForm");
			}
        	try {
                FileOutputStream fos = new FileOutputStream(file);
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				saveFiguresToFile(oos);
			} catch (IOException e) {
				e.printStackTrace();
				statusLabel.setText("There was an error loading your file...");
			}
	        
        }
	}
	@Override
	protected void refresh() {
		if (!figures.isEmpty() && figures.get(0)!=null){
			//Each line in the ArrayList myShapes is redrawn upon
			//a window repaint event
			drawPanel.remove(getMyDesignFrame());
			setMyDesignFrame((MyJFrame) figures.get(0));
			getMyDesignFrame().addTo(drawPanel);
			getMyDesignFrame().contentPanel.removeAll();
			if (eh.hasMenu) {
				getMyDesignFrame().menuPanel.removeAll();
				for (int j=0;j<menuObjects.size();j++){
					MenuObject m = menuObjects.get(j);
					if (m instanceof MyJMenu) m.addTo(getMyDesignFrame().menuPanel);
				}
			} 
			for (int i=1;i<figures.size();i++){
				SwingThing s = figures.get(i);
				if (s.toString().startsWith("JMenuBar")){
					continue;
			//	} else if (s.toString().startsWith("JMenu")) {
			//		s.addTo(myDesignFrame.menuPanel);
				} else {
					s.addTo(getMyDesignFrame().contentPanel);
				}
			}
			drawPanel.repaint();
		}
	}
	
	public void paint(Graphics g) {
		refresh();
		super.paint(g);
	}
	
	private int getSelectionIndex(int x, int y) {
		int s=-1;
		unHighLight();

		for (int i=figures.size()-1; i>=0;i--){
			if (figures.get(i).contains(x, y)) {
				s = i;
				break;
			}
		}
		
		if (s>-1) {
			statusLabel.setText(figures.get(s).getName()+" selected...");
			System.out.println(s);
		}
		else statusLabel.setText("Nothing selected...");
		
		return s;
	}
	
	public int getMenuSelectionIndex(int x, int y) {
		int s=-1;
		unHighLight();

		for (int i=menuObjects.size()-1; i>=0;i--){
			System.out.println(menuObjects.get(i));
			if (menuObjects.get(i).contains(x, y)) {
				s = i;
				break;
			}
		}
		
		if (s>-1) {
			statusLabel.setText(menuObjects.get(s).getName()+" selected...");
			System.out.println(s);
		}
		else statusLabel.setText("Nothing selected...");
		
		return s;
	}

	private void unHighLight() {
		for (int i=0;i<figures.size();i++) figures.get(i).unHighLight();
		if (eh.hasMenu) for (int i=0;i<menuObjects.size();i++) menuObjects.get(i).unHighLight();
		if (eh.selected instanceof MyJMenu) {
			((MyJMenu)eh.selected).hideMenuItemsPanel();
			wtb.menuItemPanel.setVisible(false);
			wtb.componentPanel.setVisible(false);
			wtb.menuObjectPanel.setVisible(true);
			wtb.otherObjectsPanel.setVisible(false);
			pp.objTree.setSelectionPath(pp.objTree.getLeadSelectionPath());
		}
		eh.selected=null;
		eh.selectionIndex=-1;
	}

	public void newFile() {
		menuObjects = new ArrayList<MenuObject>();
		drawPanel.removeAll();
		btnCount =0;
		lblCount =0;
		textAreaCount =0;
		textFieldCount =0;
		checkBoxCount =0;
		radioButtonCount=0;
		toggleButtonCount=0;
		comboBoxCount=0;
		timerCount=0;
		fileChooserCount=0;
		buttonGroupCount=0;
		figures.clear();
		if (eh.hasMenu) {
			wtb.menuObjectPanel.add(eh.mnuObjHolder, 0);
		}
		eh = new EventHandler();
		setMyDesignFrame(new MyJFrame("NewJFrame","New JFrame",300,200));
		getMyDesignFrame().setBounds(5, 5, 300, 200);
		drawPanel.add(getMyDesignFrame());
		drawPanel.setBackground(darkerDrawPanelBG);
		getMyDesignFrame().setSize(300,200);
		figures.add(getMyDesignFrame());
		addEventListeners();
		getMyDesignFrame().setBorder(new BevelBorder(BevelBorder.RAISED));
		wtb.setToolsToSelection();
		pp = new PropertiesPanel(this);
		leftSplitPane.setLeftComponent(pp);
		root = new DefaultMutableTreeNode(getMyDesignFrame());
		
		treeModel = new DefaultTreeModel(root);
		pp.objTree.setModel(treeModel);
		pp.objTree.setEditable(false);
		pp.objTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		pp.objTree.setShowsRootHandles(true);
		eh.selected=getMyDesignFrame();
		getMyDesignFrame().highLight();
		eh.updateTree(getMyDesignFrame().toString().substring(0,getMyDesignFrame().toString().indexOf(getMyDesignFrame().getName())+1));
		pp.objTree.addTreeSelectionListener(eh);
		repaint();
		
	}

	private void addEventListeners() {
		getMyDesignFrame().contentPanel.addMouseListener(eh);
		getMyDesignFrame().contentPanel.addMouseMotionListener(eh);
		getMyDesignFrame().menuPanel.addMouseListener(eh);
		getMyDesignFrame().menuPanel.addMouseMotionListener(eh);
		drawPanel.addMouseListener(eh);
	}
	private class EventHandler implements MouseInputListener, ActionListener, TreeSelectionListener {
		private boolean resizing=false, drawing=false, moving=false, copied=false;
		private int startX, startY; 
		private int lastX, lastY; 
		private String currentTool="Selection";
		private Editable selected=null, pasteObject=null;
		private JLabel mnuObjHolder;
		private boolean hasMenu=false;
		private MyJMenuBar menuBar=null;
		private int jmenuCount=0, jmenuItemCount=0, jmenuChkBoxCount=0, jmenuRadBtnCount=0;
		private int editPointer=0, selectionIndex;
		
		@Override
		public void mouseClicked(MouseEvent e) {
			if (wtb.getCurrentTool()!= null) currentTool=wtb.getCurrentTool();

			if (currentTool.equals("JMenuBar")) {
				if (!hasMenu) {
					unHighLight();
					addMenuBar();
					selected=getMyDesignFrame().getMenuBar();
					selected.highLight();
					menuBar=(MyJMenuBar) selected;
					hasMenu=true;
					statusLabel.setText("JMenuBar "+selected.getName()+" created...");
					addObject(selected);
					editPointer++;
				} else {
					statusLabel.setText("A JMenuBar Already Exists for this JFrame...");
				}
			} else if (currentTool.equals("JMenu")) {
				if (hasMenu) {
					statusLabel.setText("Adding JMenu Object");
					if (jmenuCount==0) getMyDesignFrame().menuPanel.removeAll();
					MyJMenu mnu = new MyJMenu("jmenu"+jmenuCount,"Menu "+jmenuCount,getMyDesignFrame().menuPanel);
					jmenuCount++;
					menuObjects.add(mnu);
					unHighLight();
					refresh();
					getMyDesignFrame().repaint();
					statusLabel.setText("JMenu added!");
					path = pp.objTree.getNextMatch(getMyDesignFrame().menuPanel.toString(), 0, Position.Bias.Forward);
					addObject((DefaultMutableTreeNode) path.getLastPathComponent(),mnu,true);
					editPointer++;
				} else {
					statusLabel.setText("Add a JMenuBar Before Adding A JMenu...");
				}
			} else if (currentTool.equals("JMenuItem") && selected instanceof MyJMenu) {
				MyJMenuItem mnuItem = new MyJMenuItem("menuItem"+jmenuItemCount,"Menu Item", (MyJMenu)selected);
				jmenuItemCount++;
				((MyJMenu)selected).addChild(mnuItem);
				menuObjects.add(mnuItem);
				path = pp.objTree.getNextMatch(selected.toString().substring(0,selected.toString().indexOf(']')+1), 0, Position.Bias.Forward);
				addObject((DefaultMutableTreeNode) path.getLastPathComponent(),mnuItem,true);
				((MyJMenu)selected).showMenuItemsPanel();
				editPointer++;
			} else if (currentTool.equals("JCheckBoxMenuItem") && selected instanceof MyJMenu) {
				MyJCheckBoxMenuItem mnuItem = new MyJCheckBoxMenuItem("mnuChkBox"+jmenuChkBoxCount,"Menu Item", (MyJMenu)selected);
				jmenuChkBoxCount++;
				((MyJMenu)selected).addChild(mnuItem);
				menuObjects.add(mnuItem);
				path = pp.objTree.getNextMatch(selected.toString().substring(0,selected.toString().indexOf(']')+1), 0, Position.Bias.Forward);
				addObject((DefaultMutableTreeNode) path.getLastPathComponent(),mnuItem,true);
				((MyJMenu)selected).showMenuItemsPanel();
				editPointer++;
			} else if (currentTool.equals("JRadioButtonMenuItem") && selected instanceof MyJMenu) {
				MyJRadioButtonMenuItem mnuItem = new MyJRadioButtonMenuItem("menuItem"+jmenuRadBtnCount,"Menu Item", (MyJMenu)selected);
				jmenuRadBtnCount++;
				((MyJMenu)selected).addChild(mnuItem);
				menuObjects.add(mnuItem);
				path = pp.objTree.getNextMatch(selected.toString().substring(0,selected.toString().indexOf(']')+1), 0, Position.Bias.Forward);
				addObject((DefaultMutableTreeNode) path.getLastPathComponent(),mnuItem,true);
				((MyJMenu)selected).showMenuItemsPanel();
				editPointer++;
			} else if (currentTool.equals("Selection")) {
				if (e.getSource()==getMyDesignFrame().contentPanel) {
					selectionIndex=getSelectionIndex(e.getX(),e.getY());
					if (selectionIndex >= 0) {
						selected=(Editable) figures.get(selectionIndex);
						selected.highLight();
						updateTree(selected.toString());
						copyItem.setEnabled(true);
						cutItem.setEnabled(true);
						copyButton.setEnabled(true);
						cutButton.setEnabled(true);
					}
				} else {
					System.out.println("inside menu selection shit");
					selectionIndex=getMenuSelectionIndex(e.getX(),e.getY());
					if (selectionIndex >= 0) {
						selected=(MenuObject) menuObjects.get(selectionIndex);
						selected.highLight();
						updateTree(selected.toString());
					}
				}
				if (selected instanceof MyJMenu) {
					((MyJMenu)selected).showMenuItemsPanel();
					wtb.menuItemPanel.setVisible(true);
					wtb.componentPanel.setVisible(false);
					wtb.menuObjectPanel.setVisible(false);
					wtb.otherObjectsPanel.setVisible(false);
				}
				if (e.getSource()==drawPanel) {
					if (selected!=null) selected.unHighLight();
					selected=null;
					copyItem.setEnabled(false);
					cutItem.setEnabled(false);
					copyButton.setEnabled(false);
					cutButton.setEnabled(false);
				}
			}
		}


		private void updateTree(String componentName) {
			path = pp.objTree.getNextMatch(componentName, 0, Position.Bias.Forward);
			pp.objTree.setSelectionPath(path);
			pp.loadProperties(eh.selected);
		}


		private void addMenuBar() {
			menuObjects=new ArrayList<MenuObject>();
			getMyDesignFrame().addJMenuBar();
			statusLabel.setText("JMenuBar Added...");
			mnuObjHolder = (JLabel) wtb.menuObjectPanel.getComponent(0);
			wtb.menuObjectPanel.remove(wtb.menuObjectPanel.getComponent(0));	
			wtb.menuObjectPanel.add(new JLabel(), 0);
			wtb.setToolsToSelection();
			currentTool="Selection";
			wtb.CURRENT_TOOL="Selection";
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			figures.add(getMyDesignFrame().getMenuBar());
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			currentTool=wtb.CURRENT_TOOL;
			
			if (e.getSource()==getMyDesignFrame().contentPanel){
				if (currentTool.startsWith("JMenu")) {
					setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
				}

			} else if (e.getSource()==getMyDesignFrame().menuPanel) {
				if (currentTool.startsWith("JMenu")) {
					getMyDesignFrame().menuPanel.highLight();
					setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				}
			}
		}

		@Override
		public void mouseExited(MouseEvent e) {
			if ( getCursor()!=Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR) ) setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if (wtb.getCurrentTool()!= null) currentTool=wtb.getCurrentTool();
			if (e.getSource()==getMyDesignFrame().contentPanel) {
				if (currentTool!="Selection") {
					getMyDesignFrame().setBorder(getMyDesignFrame().DEFAULT_BORDER);
					startX = e.getX();
					startY = e.getY();
					lastX=startX;
					lastY=startY;
					drawing=true;
					unHighLight();
					selected=null;
				} else {
					mouseClicked(e);
					if (selected!=null){
						resizing=checkForResizeSelection(e.getPoint());
						if (!resizing && selected!=figures.get(0)) {
							setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
							startX = e.getX();
							startY = e.getY();
							
							lastX=startX;
							lastY=startY;
							moving=true;
						}
					}
				}
			} else {
				if (selected!=null){
					resizing=checkForResizeSelection(e.getPoint());
				}
			}
			repaint();
		}

		private boolean checkForResizeSelection(Point clickPoint) {
			int width, height;
			drawing=false;
			moving=false;

			if (selected instanceof MyJFrame) {
				width = getMyDesignFrame().contentPanel.getWidth();
				height= getMyDesignFrame().contentPanel.getHeight();

			} else {
				width = selected.getXPos()+selected.getWDimension();
				height = selected.getYPos()+selected.getHDimension();
			} 
			
			Rectangle rectSE = new Rectangle(width-5, height-5,6,6);
			Rectangle rectNE = new Rectangle(width-5,selected.getYPos(),6,6);
			Rectangle rectNW = new Rectangle(selected.getXPos(),selected.getYPos(),6,6);
			Rectangle rectSW = new Rectangle(selected.getXPos(),height-5,6,6);
			
			if (rectSE.contains(clickPoint)) {
				lastX = clickPoint.x;
				lastY = clickPoint.y;
				startX = selected.getXPos();
				startY = selected.getYPos();
				return true;
			} else if (rectNE.contains(clickPoint)) {
				startX=selected.getXPos();
				startY=selected.getYPos()+selected.getHDimension();
				lastX = clickPoint.x;
				lastY = clickPoint.y;
				return true;
			} else if (rectSW.contains(clickPoint)) {
				startX=selected.getXPos()+selected.getWDimension();
				startY=selected.getYPos();
				lastX = clickPoint.x;
				lastY = clickPoint.y;
				return true;
			} else if (rectNW.contains(clickPoint)) {
				startX=selected.getXPos()+selected.getWDimension();
				startY=selected.getYPos()+selected.getHDimension();
				lastX = clickPoint.x;
				lastY = clickPoint.y;
				return true;
			} else {
				drawing=false;
				moving=false;
				return false;
			}	
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (resizing || drawing){
				lastX=e.getX();
				lastY = e.getY();
				if (lastX<startX) {
					int tmp = lastX;
					lastX=startX;
					startX=tmp;
				}
				if (lastY<startY) {
					int tmp = lastY;
					lastY=startY;
					startY=tmp;
				}
				int width=Math.abs(startX-lastX),
			    	height=Math.abs(startY-lastY);
				if (resizing) {
					selected.setXPos(startX);
					selected.setYPos(startY);
					if (selected==getMyDesignFrame()){
						width=e.getX()+5+(getMyDesignFrame().getInsets().left);
						height=e.getY()+5+(getMyDesignFrame().getInsets().top)+getMyDesignFrame().titlePanel.getHeight();
					}
					selected.setSize(width, height);
					resizing=false;
					selected.unHighLight();
					pp.loadProperties(selected);
					eh.editPointer++;
				//	changesMade.add(eh.editPointer,figures.toString().substring(1,figures.toString().length()-1).split(", J"));
				} else {
					if (width>3 && height>3) addComponent(currentTool,startX,startY,width,height);
					drawing=false;
					selected=(Editable) figures.get(figures.size()-1);
				}
			} else if (moving){
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				moving=false;
				
				//editPointer++;
				//changesMade.add(eh.editPointer,figures.toString().substring(1,figures.toString().length()-1).split(", J"));
			}
			repaint();
			if (selected!=null)selected.highLight();
			/*if (eh.selectionIndex>0) {
				undoItem.setEnabled(true);
				redoItem.setEnabled(true);
				undoButton.setEnabled(true);
				redoButton.setEnabled(true);
			}*/
		}

		private void addComponent(String component, int x,
				int y, int width, int height) {
			
			if (x+width > getMyDesignFrame().contentPanel.getWidth()) {
				x =getMyDesignFrame().contentPanel.getWidth()-width;
			}
			if (y+height > getMyDesignFrame().contentPanel.getHeight()) {
				y =getMyDesignFrame().contentPanel.getHeight()-height;
			}
			if (x<=0) x = getMyDesignFrame().getInsets().left;
			if (y<=0) y = 0;

			Editable btn=null;
			
			if (component.equals("JButton")) {
				btn = new MyJButton("btn"+btnCount,x,y,width,height);
				btnCount++;
			} else if (component.equals("JLabel")) {
				btn = new MyJLabel("lbl"+lblCount,x,y,width,height);
				lblCount++;
			} else if (component.equals("JTextArea")) {
				btn = new MyJTextArea("txtArea"+textAreaCount,x,y,width,height);
				textAreaCount++;
			} else if (component.equals("JTextField")) {
				btn = new MyJTextField("txtField"+textFieldCount,x,y,width,height);
				textFieldCount++;
			} else if (component.equals("JCheckBox")) {
				btn = new MyJCheckBox("chkBox"+checkBoxCount,"JCheckBox",x,y,width,height);
				checkBoxCount++;
			} else if (component.equals("JRadioButton")) {
				btn = new MyJRadioButton("jRadBtn"+radioButtonCount, "JRadioButton",x,y,width,height);
				radioButtonCount++;
			} else if (component.equals("JToggleButton")) {
				btn = new MyJToggleButton("tglBtn"+toggleButtonCount,x,y,width,height);
				toggleButtonCount++;
			} else if (component.equals("JComboBox")) {
				btn = new MyJComboBox("cmbBox"+comboBoxCount,x,y,width,height);
				comboBoxCount++;
			} else if (component.equals("Timer")) {
				btn = new MyTimer("timer"+timerCount,100,x,y);
				timerCount++;
			} else if (component.equals("JFileChooser")) {
				btn = new MyJFileChooser("jfc"+fileChooserCount,x,y);
				fileChooserCount++;
			} else if (component.equals("EventHandler")) {
				btn = new MyEventHandler("eh"+ehCount,x,y);
				ehCount++;
			} else if (component.equals("ButtonGroup")) {
				btn=new MyButtonGroup("btnGroup"+btnGroupCount,x,y);
				btnGroupCount++;
			}
			if (btn!=null){
				figures.add(btn);	
				addObject(btn);
			}
			wtb.setToolsToSelection();
			refresh();
			eh.editPointer++;
		//	changesMade.add(eh.editPointer,figures.toString().substring(1,figures.toString().length()-1).split(", J"));
		//	if (!undoItem.isEnabled()) undoItem.setEnabled(true);
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if (resizing || drawing) {
				Graphics g =null;
				if (resizing && selected==figures.get(0)) {
					g = drawPanel.getGraphics();
					startX=getMyDesignFrame().getX();
					startY=getMyDesignFrame().getY()+getMyDesignFrame().titlePanel.getHeight();
				}
				else g = getMyDesignFrame().contentPanel.getGraphics();
				
				g.setColor(Color.RED);
				int width=Math.abs(startX-lastX),
			    	height=Math.abs(startY-lastY);
				
				int drawX=Math.min(startX,lastX), drawY=Math.min(startY,lastY);

				if (resizing && selected==figures.get(0)){
					g.setXORMode(drawPanel.getBackground());
					g.drawRect(drawX, drawY, width, height);

					lastX=getMyDesignFrame().getX()+e.getX();
					lastY=getMyDesignFrame().getY()+getMyDesignFrame().titlePanel.getHeight()+e.getY();
				} else {
					g.setXORMode(getMyDesignFrame().contentPanel.getBackground());
					g.drawRect(drawX, drawY, width, height);

					lastX=e.getX();
					lastY = e.getY();
				}

				width=Math.abs(startX-lastX);
			    height=Math.abs(startY-lastY);
			    drawX=Math.min(startX,lastX);
			    drawY=Math.min(startY,lastY);
			    g.drawRect(drawX, drawY, width, height);

			} else if (moving) {
				Graphics g = getMyDesignFrame().contentPanel.getGraphics();
				g.setColor(Color.black);
				g.drawLine(selected.getXPos(), 0, selected.getXPos(), getMyDesignFrame().contentPanel.getWidth());
				g.drawLine(0, selected.getYPos(), getMyDesignFrame().contentPanel.getHeight(), selected.getYPos());
				selected.moveBy(e.getX()-lastX, e.getY()-lastY);
				g.setXORMode(Color.black);

				
				g.drawLine(selected.getXPos(), 0, selected.getXPos(), getMyDesignFrame().contentPanel.getWidth());
				g.drawLine(0, selected.getYPos(), getMyDesignFrame().contentPanel.getHeight(), selected.getYPos());

				lastX=e.getX();
				lastY=e.getY();
				repaint();
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {

		}
		public void cutObject() {
			if (eh.selectionIndex>0){
				eh.selected.unHighLight();
				eh.pasteObject=eh.selected;
				figures.remove(eh.selectionIndex);
				eh.selectionIndex=-1;
				eh.selected=null;
				if (!pasteItem.isEnabled()) pasteItem.setEnabled(true);
				//eh.editPointer++;
				//changesMade.add(eh.editPointer,figures.toString().substring(1,figures.toString().length()-1).split(", J"));
				statusLabel.setText("Object cut!");
				repaint();
			}			
		}
	/*	public void redoEdit() {
			if (eh.editPointer<changesMade.size()-1){
				figures.clear();
				eh.editPointer++;
				String[] dataSet= changesMade.get(eh.editPointer);
				System.out.println("changesMade: \n"+changesMade.get(eh.editPointer));
				applyDataSetToCanvas(dataSet);
				if (eh.editPointer>=changesMade.size()) redoItem.setEnabled(false);
				if (!undoItem.isEnabled()) undoItem.setEnabled(true);
				statusLabel.setText("Action redone!");
				repaint();
			}
		}
		public void undoEdit() {
			if (eh.editPointer>0){
				String[] dataSet= changesMade.get(eh.editPointer-1);
				eh.editPointer--;
				applyDataSetToCanvas(dataSet);
				if (eh.editPointer-1<0) undoItem.setEnabled(false);
				if (!redoItem.isEnabled()) redoItem.setEnabled(true);
				statusLabel.setText("Action undone!");
				repaint();
			}
		}
	*/
		public void pasteObject() {
			if (eh.pasteObject!=null){
				figures.add(eh.pasteObject);
				eh.selectionIndex=figures.size()-1;
				if (eh.selected!=null) {
					eh.selected.unHighLight();
				}
				if (!copied) eh.pasteObject=null;
				eh.selected=(Editable) figures.get(selectionIndex);
				eh.selected.highLight();
				//eh.editPointer++;
				//changesMade.add(eh.editPointer,figures.toString().substring(1,figures.toString().length()-1).split(", J"));
				pasteItem.setEnabled(false);
				statusLabel.setText("Object Successfully Pasted!");
				repaint();
			}			
		}
/*		private void copyPaste(Editable object) {
			String component=object.getName(); 
			int x=object.getXPos(), y=object.getYPos(), width=object.getWDimension(), height=object.getHDimension();
			String[] properties =object.getPropertyValues().split("~");
			Editable btn = null;
			if (object instanceof MyJButton) {
				btn = new MyJButton("btn"+btnCount,x,y,width,height);
				btnCount++;
			} else if (object instanceof MyJLabel) {
				btn = new MyJLabel("lbl"+lblCount,x,y,width,height);
				lblCount++;
			} else if (object instanceof MyJTextArea) {
				btn = new MyJTextArea("txtArea"+textAreaCount,x,y,width,height);
				textAreaCount++;
			} else if (object instanceof MyJTextField) {
				btn = new MyJTextField("txtField"+textFieldCount,x,y,width,height);
				textFieldCount++;
			} else if (object instanceof MyJCheckBox) {
				btn = new MyJCheckBox("chkBox"+checkBoxCount,"JCheckBox",x,y,width,height);
				checkBoxCount++;
			} else if (object instanceof MyJRadioButton) {
				btn = new MyJRadioButton("jRadBtn"+radioButtonCount, "JRadioButton",x,y,width,height);
				radioButtonCount++;
			} else if (object instanceof MyJToggleButton) {
				btn = new MyJToggleButton("tglBtn"+toggleButtonCount,x,y,width,height);
				toggleButtonCount++;
			} else if (object instanceof MyJComboBox) {
				btn = new MyJComboBox("cmbBox"+comboBoxCount,x,y,width,height);
				comboBoxCount++;
			} else if (object instanceof MyTimer) {
				btn = new MyTimer("timer"+timerCount,100,x,y);
				timerCount++;
			} else if (object instanceof MyJFileChooser) {
				btn = new MyJFileChooser("jfc"+fileChooserCount,x,y);
				fileChooserCount++;
			} else if (object instanceof MyEventHandler) {
				btn = new MyEventHandler("eh"+ehCount,x,y);
				ehCount++;
			} else if (object instanceof MyButtonGroup) {
				btn=new MyButtonGroup("btnGroup"+btnGroupCount,x,y);
				btnGroupCount++;
			}
			if (btn!=null){
				int pointer=0;
				for (String s : properties) {
					btn.setProperties(pointer, s);
					pointer++;
				}
				figures.add(btn);	
				addObject(btn);
			}
		}
*/

		@Override
		public void actionPerformed(ActionEvent o) {
			if (o.getActionCommand().equals("New")) {
				newFile();
			} else if (o.getActionCommand().equals("Open")) {
				openFile();
			} else if (o.getActionCommand().equals("Save")) { 
				saveFile();
			} else if (o.getActionCommand().equals("Save As")) { 
				saveFile();
			} else if (o.getActionCommand().equals("Cut")) { 
				cutObject();
				copied=false;
			} else if (o.getActionCommand().equals("Copy")) { 
				cutObject();
				copied=true;
			} else if (o.getActionCommand().equals("Paste")) { 
				pasteObject();
			/*
			} else if (o.getActionCommand().equals("Undo")) { 
				undoEdit();
			} else if (o.getActionCommand().equals("Redo")) {
				redoEdit();
			*/
			} else if (o.getActionCommand().equals("Delete")) { 
				deleteObject();
			} else if (o.getActionCommand().equals("FullSource")) {
				MyJFrame dummy = new MyJFrame(myDesignFrame.getName(),myDesignFrame.getTitleText(),myDesignFrame.getWDimension(),myDesignFrame.getHDimension());
				dummy.setSource(getSource());
				dummy.setScope("fullSource");
				SourceEditor fse = new SourceEditor(getSource(),dummy,SwingUIEditor.this.pp);
				fse.setVisible(true);
			} else if (o.getActionCommand().equals("Export")) {
				exportFile();
			}
		}


		public void deleteObject() {
			if (eh.selectionIndex>0){
				DefaultTreeModel model = (DefaultTreeModel)pp.objTree.getModel(); // Find node to remove 
				TreePath path = pp.objTree.getNextMatch(eh.selected.toString(), 0, Position.Bias.Forward); 
				MutableTreeNode node = (MutableTreeNode)path.getLastPathComponent(); 

				System.out.println(eh.selected);
				System.out.println(eh.selectionIndex);
				if (!(selected instanceof MenuObject)){
					eh.selected.unHighLight();
					figures.remove(eh.selectionIndex);
					eh.selected=null;
				} else {
					eh.selected.unHighLight();
					menuObjects.remove(eh.selectionIndex);
				}
				
				// Remove node; if node has descendants, all descendants are removed as well 
				model.removeNodeFromParent(node); 
				eh.selectionIndex=-1;
				eh.selected=null;
				
				statusLabel.setText("Object deleted!");
				repaint();
			}
		}


		@Override
		public void valueChanged(TreeSelectionEvent te) {
	        JTree tree = (JTree) te.getSource();
	        TreePath object = tree.getSelectionPath();
	        
	        try {
		        String o = object.toString();
		        for (int i=figures.size()-1;i>=0;i--){
		        	String y=figures.get(i).toString();
		        	if (y.equals(o)) {
		    	        unHighLight();
		        		selected=(Editable) figures.get(i);
		        		selected.highLight();
		        		break;
		        	}
		        }
		        for (int i=menuObjects.size()-1;i>=0;i--){
		        	String y=menuObjects.get(i).toString();
		        	if (y.equals(o)) {
		    	        unHighLight();
		        		selected=(Editable) menuObjects.get(i);
		        		selected.highLight();
		        		break;
		        	}
		        }
		        pp.loadProperties(selected);
		        
	        } catch (NullPointerException e) {
	        	statusLabel.setText("Object deleted...");
	        }
	        pp.repaint();
	        repaint();
		        
		}
		public void addPopup(Component component, final JPopupMenu popup) {
			component.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					if (e.isPopupTrigger()) {
						showMenu(e);
					}
				}
				public void mouseReleased(MouseEvent e) {
					if (e.isPopupTrigger()) {
						showMenu(e);
					}
				}
				private void showMenu(MouseEvent e) {
					popup.show(e.getComponent(), e.getX(), e.getY());
					Component[] c = popup.getComponents();
					if (e.getComponent() instanceof MyJFrame){
						//0:cut 1:copy 2:paste 3:delete
						c[0].setEnabled(false);
						c[1].setEnabled(false);
						if (eh.pasteObject==null) c[2].setEnabled(false);
						else c[2].setEnabled(true);
						c[3].setEnabled(false);
					} else {
						c[0].setEnabled(true);
						c[1].setEnabled(true);
						if (eh.pasteObject==null) c[2].setEnabled(false);
						else c[2].setEnabled(true);
						c[3].setEnabled(true);
					}
				}
			});
		}
	}

	private class JavaFileFilter extends FileFilter {
		public static final String JAVA = "java";

		public boolean accept(File file) {
			return getExtension(file).equalsIgnoreCase(JAVA);
		}

		public String getDescription() {
			return "java source files (*.java)";
		}

		private String getExtension(File file) {
			String path = file.getPath();
			int lastDot = path.lastIndexOf(".");
			return path.substring(lastDot + 1);
		}
	}

	private class SwingUIEditorFilter extends FileFilter {
		public static final String Extension = "jForm";

		public boolean accept(File file) {
			return getExtension(file).equalsIgnoreCase(Extension);
		}

		public String getDescription() {
			return "SwingUIEditor files (*.jForm)";
		}

		private String getExtension(File file) {
			String path = file.getPath();
			int lastDot = path.lastIndexOf(".");
			return path.substring(lastDot + 1);
		}
	}

	public void applyDataSetToCanvas(String[] dataSet) {
		if (dataSet.length<1) return;
		int set=0;
		pp.objTree = new JTree();
		pp.objTree.setBackground(new Color(255, 255, 240));
		pp.objTree.setBorder(new BevelBorder(BevelBorder.LOWERED));
		pp.objTree.revalidate();
		figures.clear();
		do {
			SwingThing s = getShapeFromData(dataSet[set]);
			if (set<1) {
				eh.updateTree(getMyDesignFrame().toString().substring(0,getMyDesignFrame().toString().indexOf(getMyDesignFrame().getName())+1));
			}
			figures.add(s);
			addObject(s);
			set++;
		} while (set<dataSet.length);
		unHighLight();
		eh.selected=(Editable) figures.get(figures.size()-1);
		eh.selected.highLight();
		repaint();
	}

	private SwingThing getShapeFromData(String data) {
		Editable returnObject=null;
		System.out.println(data);
		data=data.trim();
			String[] actions=data.split("~");
			String name;
			ArrayList<String> properties=new ArrayList<String>();
			int x1,y1,height,width;
			
			if (actions[0].contains("Button")){
				x1=Integer.parseInt(actions[actions.length-2]);
				y1=Integer.parseInt(actions[actions.length-1]);
				returnObject=new MyJButton("text", x1, y1, 10, 10);
			} else if (actions[0].contains("CheckBoxMenuItem")){
				MyJMenu parent=null;
				for(SwingThing e : figures){
					if (e.getName().equals(actions[actions.length-1])) {
						parent=(MyJMenu) e;
						break;
					}
				}
				if (parent!=null) {
					returnObject=new MyJMenuItem("name","text",parent);
				}
			} else if (actions[0].contains("CheckBox")){
				x1=Integer.parseInt(actions[actions.length-2]);
				y1=Integer.parseInt(actions[actions.length-1]);
				returnObject=new MyJCheckBox("name","text",x1,y1,10,10);
			} else if (actions[0].contains("ComboBox")){
				x1=Integer.parseInt(actions[actions.length-2]);
				y1=Integer.parseInt(actions[actions.length-1]);
				returnObject=new MyJComboBox("name",x1,y1,10,10);
			} else if (actions[0].contains("FileChooser")){
				x1=Integer.parseInt(actions[actions.length-2]);
				y1=Integer.parseInt(actions[actions.length-1]);
				returnObject=new MyJFileChooser("name",x1,y1);
			} else if (actions[0].contains("Frame")){
				returnObject=new MyJFrame("name","title",100,100);
			} else if (actions[0].contains("Label")){
				x1=Integer.parseInt(actions[actions.length-2]);
				y1=Integer.parseInt(actions[actions.length-1]);
				returnObject=new MyJLabel("name",x1,y1,10,10);
			} else if (actions[0].contains("Menu")){
				x1=Integer.parseInt(actions[actions.length-2]);
				y1=Integer.parseInt(actions[actions.length-1]);
				returnObject=new MyJMenu("name","text", eh.menuBar);
			} else if (actions[0].contains("MenuBar")){
				returnObject=new MyJMenuBar("name");
				eh.menuBar=(MyJMenuBar) returnObject;
				eh.hasMenu=true;
			} else if (actions[0].contains("MenuItem")){
				MyJMenu parent=null;
				for(SwingThing e : figures){
					if (e.getName().equals(actions[actions.length-1])) {
						parent=(MyJMenu) e;
						break;
					}
				}
				if (parent!=null) {
					returnObject=new MyJMenuItem("name","text",parent);
				}
			} else if (actions[0].contains("RadioButtonMenuItem")){
				MyJMenu parent=null;
				for(SwingThing e : figures){
					if (e.getName().equals(actions[actions.length-1])) {
						parent=(MyJMenu) e;
						break;
					}
				}
				if (parent!=null) {
					returnObject=new MyJMenuItem("name","text",parent);
				}
			} else if (actions[0].contains("RadioButton")){
				x1=Integer.parseInt(actions[actions.length-2]);
				y1=Integer.parseInt(actions[actions.length-1]);
				returnObject=new MyJRadioButton("name","text",x1,y1,10,10);
			} else if (actions[0].contains("TextArea")){
				x1=Integer.parseInt(actions[actions.length-2]);
				y1=Integer.parseInt(actions[actions.length-1]);
				returnObject=new MyJTextArea("name",x1,y1,10,10);
			} else if (actions[0].contains("TextField")){
				x1=Integer.parseInt(actions[actions.length-2]);
				y1=Integer.parseInt(actions[actions.length-1]);
				returnObject=new MyJTextField("name",x1,y1,10,10);
			} else if (actions[0].contains("ToggleButton")){
				x1=Integer.parseInt(actions[actions.length-2]);
				y1=Integer.parseInt(actions[actions.length-1]);
				returnObject=new MyJToggleButton("name",x1,y1,10,10);
			} else if (actions[0].contains("Timer")){
				//name=actions[1].substring(actions[1].indexOf('[')+1, actions[1].indexOf(']'));
				x1=Integer.parseInt(actions[actions.length-2]);
				y1=Integer.parseInt(actions[actions.length-1]);
				returnObject=new MyTimer("name",10,x1,y1);
			}
		System.out.println("returnObject:"+returnObject);
		for (int i=0;i<actions.length-1;i++){
			System.out.println(i+": "+actions[i+1]);
			returnObject.setProperties(i, actions[i+1]);
		}
		return returnObject;
	}


	public void setAppendedSource(String appendedSource) {
		this.appendedSource = appendedSource;
	}


	public String getAppendedSource() {
		return appendedSource;
	}


	public void setMyDesignFrame(MyJFrame myDesignFrame) {
		this.myDesignFrame = myDesignFrame;
	}


	public MyJFrame getMyDesignFrame() {
		return myDesignFrame;
	}
}
