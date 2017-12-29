import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.SoftBevelBorder;
import javax.swing.JLabel;
import java.awt.FlowLayout;


public class ToolBox extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6575993377834022393L;
	/**
	 * 
	 */
	public String CURRENT_TOOL="Selection";
	private ToolHandler tH;
	private JLabel[] objects;
	private ImageIcon[] icons;
	private String[] sysTools = {"Selection"}, 
					 componentNames={"JLabel", "JTextField", "JButton","JTextArea","JCheckBox","JRadioButton","JToggleButton","JComboBox"},
					 menuObjNames={"JMenuBar","JMenu"},
					 menuItemNames={"JMenuItem","JCheckBoxMenuItem","JRadioButtonMenuItem"},
					 otherObjectNames={"Timer","JFileChooser", "EventHandler"};
	public JPanel panel, toolPanel, iconPanel, componentPanel, menuObjectPanel,menuItemPanel, otherObjectsPanel;
	private JButton componentBtn, menuObjBtn, othersBtn;
	private JPanel btnsPanel;
	private JPanel objectsPanel;
	
	/**
	 * 
	 */
	public ToolBox() {
		setBackground(new Color(211, 211, 211));
		setLayout(new BorderLayout());
		
		panel = new JPanel();
		
		tH=new ToolHandler();
		panel.setLayout(new BorderLayout());
		
		add(panel, BorderLayout.CENTER);
		
		btnsPanel = new JPanel();
		panel.add(btnsPanel, BorderLayout.NORTH);
		btnsPanel.setLayout(new GridLayout(4, 1, 20, 5));
		
		componentBtn=new JButton("JComponents");
		btnsPanel.add(componentBtn);
		menuObjBtn=new JButton("JMenu Objects");
		btnsPanel.add(menuObjBtn);
		othersBtn= new JButton("Other Components");
		btnsPanel.add(othersBtn);
		
		objectsPanel = new JPanel();
		panel.add(objectsPanel, BorderLayout.SOUTH);
		objectsPanel.setLayout(new BorderLayout());
		
		iconPanel= new JPanel();
		objectsPanel.add(iconPanel, BorderLayout.CENTER);
		
		toolPanel=new JPanel();
		objectsPanel.add(toolPanel, BorderLayout.NORTH);
		
		componentPanel=new JPanel();
		iconPanel.add(componentPanel);
		menuObjectPanel=new JPanel();
		iconPanel.add(menuObjectPanel);
		otherObjectsPanel=new JPanel();
		iconPanel.add(otherObjectsPanel);
		menuItemPanel=new JPanel();
		iconPanel.add(menuItemPanel);
		othersBtn.addActionListener(tH);
		menuObjBtn.addActionListener(tH);
		componentBtn.addActionListener(tH);
		
		for (int i=0;i<5;i++) {
			loadIcons(i);
		}
	}
	
	/** Returns an ImageIcon, or null if the path was invalid. 
	 * 			From: oracle.com*/
	protected ImageIcon createImageIcon(String path,
	                                           int description) {
		String desc="";
		switch (description){
			case 0:
				desc="System Tools";
				break;
			case 1:
				desc="JMenu Objects";
				break;
			case 2:
				desc="JComponents";
				break;
			case 3:
				desc="Other Objects";
				break;
			case 4:
				desc="MenuItems";
				break;
		}
	    java.net.URL imgURL = getClass().getResource(path);
	    if (imgURL != null) {
	        return new ImageIcon(imgURL, desc);
	    } else {
	        System.err.println("Couldn't find file: " + path);
	        return null;
	    }
	}
	
	/**
	 * @param cat
	 */
	private void loadIcons(int cat) {
		String path="icos/";
		String ext =".gif";
		String[] objNames = null;
		JPanel useThis = new JPanel();
		switch (cat) {
			case 0:
				useThis = toolPanel;
				icons = new ImageIcon[1];
				objects = new JLabel[1];
				objNames=sysTools;
				ext=".jpg";
				break;
			case 1:		
				useThis = menuObjectPanel;
				icons = new ImageIcon[2];
				objects = new JLabel[2];
				objNames=menuObjNames;
				path+="menu";
				break;
			case 2:
				useThis =componentPanel;
				icons = new ImageIcon[8];
				objects = new JLabel[8];
				objNames=componentNames;
				path+="comp";
				break;
			case 3:
				useThis=otherObjectsPanel;
				icons= new ImageIcon[3];
				objects = new JLabel[3];
				objNames=otherObjectNames;
				path+="other";
				break;
			case 4:
				useThis=menuItemPanel;
				icons= new ImageIcon[3];
				objects = new JLabel[3];
				objNames=menuItemNames;
				path+="menui";
				break;
		}		
		
		useThis.setLayout(new GridLayout(0,2));
		
		for (int i=0;i<icons.length;i++){
			icons[i]=createImageIcon(path+i+ext,cat);
			objects[i]=new JLabel(objNames[i],icons[i],JLabel.CENTER);
			objects[i].setVerticalTextPosition(JLabel.BOTTOM);
			objects[i].setHorizontalTextPosition(JLabel.CENTER);
			objects[i].setBorder(new SoftBevelBorder(BevelBorder.RAISED));
			objects[i].setFont(new Font("Tahoma", Font.PLAIN, 8));
			objects[i].addMouseListener(tH);
			useThis.add(objects[i]);
		}
		if (useThis!=toolPanel) useThis.setVisible(false);
		
	}
	
	/**
	 * 
	 */
	public void setToolsToSelection(){
		if (tH.clicked!=null) {
			tH.clicked.setBorder(tH.defaultBorder);
			tH.clicked.setBackground(tH.clicked.getBackground().brighter());
			tH.clicked.setOpaque(false);
		}
		tH.clicked=(JLabel) toolPanel.getComponent(0);
		tH.clicked.setBorder(tH.selectBorder);
		tH.clicked.setOpaque(true);
		tH.clicked.setBackground(tH.clicked.getBackground().darker());
		String object = tH.clicked.getText().trim();
		CURRENT_TOOL=object;
	}
	
	/**
	 * @author smvorwerk
	 *
	 */
	private class ToolHandler implements MouseListener, ActionListener {
		public JLabel clicked=null, hover=null;
		public Border defaultBorder =new SoftBevelBorder(BevelBorder.RAISED);
		public Border selectBorder = new SoftBevelBorder(BevelBorder.LOWERED);
		@Override
		public void mouseClicked(MouseEvent e) {
			if (clicked!=null) {
				clicked.setBorder(defaultBorder);
				clicked.setBackground(clicked.getBackground().brighter());
				clicked.setOpaque(false);
			}
			clicked = (JLabel)e.getSource();
			clicked.setBorder(selectBorder);
			clicked.setOpaque(true);
			clicked.setBackground(clicked.getBackground().darker());
			String object = clicked.getText().trim();
			CURRENT_TOOL=object;
			System.out.println(CURRENT_TOOL);
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			hover = (JLabel)e.getSource();
			hover.setForeground(new Color(100, 100, 100));
		}

		@Override
		public void mouseExited(MouseEvent e) {
			hover = (JLabel)e.getSource();
			hover.setForeground(Color.black);				
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void actionPerformed(ActionEvent a) {
			String cmd = a.getActionCommand();

			if (cmd.equals("JComponents")) {
				componentPanel.setVisible(true);
				menuObjectPanel.setVisible(false);
				otherObjectsPanel.setVisible(false);
				menuItemPanel.setVisible(false);
			} else if (cmd.equals("JMenu Objects")) {
				componentPanel.setVisible(false);
				menuObjectPanel.setVisible(true);
				otherObjectsPanel.setVisible(false);
				menuItemPanel.setVisible(false);
			} else if (cmd.equals("Other Components")) {
				componentPanel.setVisible(false);
				menuObjectPanel.setVisible(false);
				otherObjectsPanel.setVisible(true);
				menuItemPanel.setVisible(false);
			}
			repaint();
		}

	}

	public String getCurrentTool() {
		return CURRENT_TOOL;
	}
}
