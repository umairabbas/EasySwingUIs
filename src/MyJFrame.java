import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.SystemColor;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

import SwingMan.SwingThing;
import javax.swing.UIManager;


public class MyJFrame extends JPanel implements Editable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2321832095629099309L;
	private String contentPaneName;
	private String titleText, name, source, eventHandlerName, scope="isClass", type="JFrame";
	private int width, height, x ,y;
	private ArrayList<String> propertyNames, propertyValues;
	private boolean exitOnClose=true;
	public JPanel titlePanel, titleTextPanel, contentPanel; 
	public MyJMenuBar menuPanel;
	public JLabel titleTextLabel;
	public final Border DEFAULT_BORDER = new BevelBorder(BevelBorder.RAISED);
	public final Border SELECT_BORDER = new ResizeBorder(Color.red);
	public int contentWidth, contentHeight;
	public Color background, foreground;
	
	public MyJFrame(String name, String titleText,int width, int height) {
		setBorder(UIManager.getBorder("InternalFrame.border"));
		this.name=name;
		contentPaneName="cp";
		this.titleText=titleText;
		this.width = width;
		this.height=height;
		this.x=5;
		this.y=5;
		setLayout(new BorderLayout());
	
		titlePanel = new JPanel();
		titlePanel.setLayout(new BorderLayout());
		
		add(titlePanel, BorderLayout.NORTH);
		
		titleTextPanel = new JPanel();
		titleTextPanel.setForeground(SystemColor.textHighlightText);
		titleTextPanel.setBackground(SystemColor.activeCaption);
		titlePanel.add(titleTextPanel,BorderLayout.NORTH);
		titleTextPanel.setLayout(new BorderLayout());
					
		titleTextLabel = new JLabel(titleText);
		titleTextLabel.setIcon(new ImageIcon(MyJFrame.class.getResource("/javax/swing/plaf/basic/icons/JavaCup16.png")));
		titleTextPanel.add(titleTextLabel, BorderLayout.WEST);
		
		menuPanel = new MyJMenuBar("menuBar");
		menuPanel.setBorder(new MatteBorder(0, 0, 1, 0, Color.gray));
		//titlePanel.add(menuPanel);
		
		contentPanel = new JPanel();
		contentPanel.setBounds(7, 23, 436, 270);
		add(contentPanel,BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		eventHandlerName="";
		source="";
		loadProperties();
	}
	public MyJFrame(ArrayList<String> properties) {
		setBorder(UIManager.getBorder("InternalFrame.border"));
		this.name=properties.get(0);
		this.titleText=properties.get(1);
		this.width = Integer.parseInt(properties.get(2));
		this.height= Integer.parseInt(properties.get(3));
		this.x=5;
		this.y=5;
		setLayout(new BorderLayout());
	
		titlePanel = new JPanel();
		titlePanel.setLayout(new BorderLayout());
		
		add(titlePanel, BorderLayout.NORTH);
		
		titleTextPanel = new JPanel();
		titleTextPanel.setForeground(SystemColor.textHighlightText);
		titleTextPanel.setBackground(SystemColor.activeCaption);
		titlePanel.add(titleTextPanel,BorderLayout.NORTH);
		titleTextPanel.setLayout(new BorderLayout());
					
		titleTextLabel = new JLabel(titleText);
		titleTextLabel.setIcon(new ImageIcon(MyJFrame.class.getResource("/javax/swing/plaf/basic/icons/JavaCup16.png")));
		titleTextPanel.add(titleTextLabel, BorderLayout.WEST);
		
		menuPanel = new MyJMenuBar("menuBar");
		menuPanel.setBorder(new MatteBorder(0, 0, 1, 0, Color.gray));
		//titlePanel.add(menuPanel);
		
		contentPanel = new JPanel();
		contentPanel.setBounds(7, 23, 436, 270);
		add(contentPanel,BorderLayout.CENTER);
		contentPanel.setLayout(null);
		contentPanel.setForeground(getColorFromProperties(properties.get(4)));
		contentPanel.setBackground(getColorFromProperties(properties.get(5)));
		eventHandlerName=properties.get(6);
		source="\npublic class "+this.name+" extends JFrame {"+
			   "\n\n\t//Constructor\n"+
			   "\tpublic "+this.name+"() {"+
			   "\n\t\tsuper();"+
			   "\n\t\tsetTitle(\""+titleText+"\");"+
			   "\n\t\tsetSize("+width+","+height+");"+
			   "\n\t\tsetDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);"+
			   "\n\t\tgetContentPane().setLayout(new FlowLayout());"+
			   "\n\t\tJPanel "+ name.toLowerCase()+"Panel" +"= new JPanel();"+
			   "\n\t\t"+name.toLowerCase()+"Panel"+".setPreferredSize(new Dimension("+width+","+height+"));"+
			   "\n\t\t"+name.toLowerCase()+"Panel"+".setLayout(null);";
		if (!contentPanel.getForeground().equals(new Color(51,51,51))) source+="\n\t\t"+name.toLowerCase()+"Panel"+".setForeground(new Color ("+getColorFromProperties(properties.get(4)).getRed()+","+getColorFromProperties(properties.get(4)).getGreen()+","+getColorFromProperties(properties.get(4)).getBlue()+");";
		if (!contentPanel.getBackground().equals(new Color(238,238,238))) source+="\n\t\t"+name.toLowerCase()+"Panel"+".setBackground(new Color ("+getColorFromProperties(properties.get(5)).getRed()+","+getColorFromProperties(properties.get(5)).getGreen()+","+getColorFromProperties(properties.get(5)).getBlue()+");";
		source+="\n\n";
	}

	@Override
	public Color getColorFromProperties(String colorString) {
		String s = colorString.substring(colorString.indexOf('(')+1, colorString.lastIndexOf(')'));
		String[] c =s.split(",");
		int r = Integer.parseInt(c[0].trim());
		int g = Integer.parseInt(c[1].trim());
		int b = Integer.parseInt(c[2].trim());
		Color col = new Color(r,g,b);
		return col;
	}
	public void addJMenuBar() {
		menuPanel.setSize(new Dimension(titlePanel.getWidth(),titlePanel.getHeight()));
		menuPanel.add(new JLabel("<Add JMenu Objects Here>"));
		titlePanel.add(menuPanel,BorderLayout.SOUTH);
	}
	public void addToMenuBar(MyJMenu jMnu) {
		menuPanel.add(jMnu);
		menuPanel.repaint();
		
	}
	public MyJMenuBar getMenuBar() {
		return menuPanel;
	}
	@Override
	public void addTo(JPanel p) {
		setBounds(x,y,width,height);
		p.add(this);		
	}

	@Override
	public void erase() {
		contentPanel.removeAll();
		titleText="";
	}

	@Override
	public String describeInCode() {
		String code="public class "+this.name+" extends JFrame {"+
		   "\n\n\t//Constructor\n"+
		   "\tpublic "+this.name+"() {"+
		   "\n\t\tsuper();"+
		   "\n\t\tsetTitle(\""+titleText+"\");"+
		   "\n\t\tsetSize("+width+","+height+");"+
		   "\n\t\tsetDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);"+
		   "\n\t\tgetContentPane().setLayout(new FlowLayout());"+
		   "\n\t\tJPanel "+ contentPaneName +"= new JPanel();"+
		   "\n\t\t"+contentPaneName+".setPreferredSize(new Dimension("+width+","+height+"));"+
		   "\n\t\t"+contentPaneName+".setLayout(null);\n";
		if (!contentPanel.getForeground().equals(new Color(51,51,51))) code+="\n\t\t"+contentPaneName+".setForeground(new Color ("+getColorFromProperties(propertyValues.get(4)).getRed()+","+getColorFromProperties(propertyValues.get(4)).getGreen()+","+getColorFromProperties(propertyValues.get(4)).getBlue()+"));\n";
		if (!contentPanel.getBackground().equals(new Color(238,238,238))) code+="\n\t\t"+contentPaneName+".setBackground(new Color ("+getColorFromProperties(propertyValues.get(5)).getRed()+","+getColorFromProperties(propertyValues.get(5)).getGreen()+","+getColorFromProperties(propertyValues.get(5)).getBlue()+"));\n";
		if (source.length()>0) code+="\n"+source;
		code+="\n";
		return code;
	}

	@Override
	public void highLight() {
		contentPanel.setBorder(SELECT_BORDER);
	}

	@Override
	public void unHighLight() {
		setBorder(DEFAULT_BORDER);
		contentPanel.setBorder(null);
	}
	
	@Override
	public void setName(String newName) {
		name = newName;		
	}

	@Override
	public String getName() {
		return name;
	}
	public void setTitleText(String titleText) {
		this.titleText = titleText;
	}

	public String getTitleText() {
		return titleText;
	}
	@Override
	public boolean contains(int x, int y) {
		int x1=x, y1 = y;
		if (x1>=this.x && y1>=this.y && x1 <=this.x+width && y1 <= this.y+height) return true;
		else return false;
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		int offsetR = getInsets().right+2;
				
		titlePanel.setSize(getWidth()-offsetR, titlePanel.getHeight());
		titleTextPanel.setSize(getWidth()-offsetR,titleTextPanel.getHeight());

	}
	
	@Override
	public void moveBy(int dx, int dy) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void resize(int newWidth, int newHeight) {
		this.width = newWidth;
		this.height = newHeight;
		setBounds(x,y,width,height);
	}

	@Override
	public int getXPos() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getYPos() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getWDimension() {
		// TODO Auto-generated method stub
		return width;
	}

	@Override
	public int getHDimension() {
		// TODO Auto-generated method stub
		return height;
	}

	@Override
	public void setXPos(int x) {
		//do nothing for jframes
	}

	@Override
	public void setYPos(int y) {
		//do nothing for jframes
	}
	
	public String toString() {
		return "JFrame ["+name+"]"+getPropertyValues();
	}
	@Override
	public String getPropertyValues() {
		String s="";
		for (String str : propertyValues){
			if(str.substring(0, str.lastIndexOf(',')).length()>0) s+="~"+str.substring(0, str.lastIndexOf(','));
			else s+="~ ";
		}
		return s;
	}
	@Override
	public void loadProperties() {
		propertyNames = new ArrayList<String>();
		propertyValues = new ArrayList<String>();
		
		propertyNames.add("Class Name");
		propertyNames.add("Frame Title");
		propertyNames.add("Content Panel Name");
		propertyNames.add("Width");
		propertyNames.add("Height");
		propertyNames.add("Foreground");
		propertyNames.add("Background");
		propertyNames.add("Source Code");
		
		propertyValues.add(name+","+OPTION_IS_TEXT);
		propertyValues.add(titleText+","+OPTION_IS_TEXT);
		propertyValues.add(contentPaneName+","+OPTION_IS_TEXT);
		propertyValues.add(Integer.toString(width)+","+OPTION_IS_INT);
		propertyValues.add(Integer.toString(height)+","+OPTION_IS_INT);
		propertyValues.add("rgb("+Integer.toString(contentPanel.getForeground().getRed())+", "+Integer.toString(contentPanel.getForeground().getGreen())+", "+Integer.toString(contentPanel.getForeground().getBlue())+")"+","+OPTION_IS_COLOR);
		propertyValues.add("rgb("+Integer.toString(contentPanel.getBackground().getRed())+", "+Integer.toString(contentPanel.getBackground().getGreen())+", "+Integer.toString(contentPanel.getBackground().getBlue())+")"+","+OPTION_IS_COLOR);
		propertyValues.add(source+","+OPTION_IS_SOURCE);
		
		name=propertyValues.get(0).substring(0, propertyValues.get(0).indexOf(','));
		titleText=propertyValues.get(1).substring(0, propertyValues.get(1).indexOf(','));
		width=Integer.parseInt(propertyValues.get(3).substring(0, propertyValues.get(3).indexOf(',')));
		height=Integer.parseInt(propertyValues.get(4).substring(0, propertyValues.get(4).indexOf(',')));
	}

	@Override
	public String getHeaderInfo() {
		return "import javax.swing.*;\n"+
			   "import java.awt.Color;\n"+
			   "import java.awt.Dimension;\n"+
			   "import java.awt.FlowLayout;\n"+
			   "import java.awt.Font;\n";
	}
	@Override
	public String[] getPropertyNamesAndValues() {
		String[] output = new String[propertyNames.size()];
		for (int i=0;i<output.length;i++){
			output[i] = propertyNames.get(i)+","+propertyValues.get(i); 
		}
		return output;
	}
	
	@Override
	public void setSource(String newSource) {
		source=newSource;
	}
	@Override
	public void setProperties(int index, String value) {
		switch (index) {
		case 0:
			name=value;
			break;
		case 1:
			titleText=value;
			titleTextLabel.setText(value);
			break;
		case 2:
			contentPaneName=value;
			if (contentPaneName.length()>1) contentPaneName="cp";
			if (contentPaneName.split(" ").length>1) {
				String[] s = contentPaneName.split(" ");
				contentPaneName=s[0].toLowerCase()+s[1]+"Panel";
			}
			break;
		case 3:
			width=Integer.parseInt(value);
			contentPanel.setBounds(contentPanel.getX(), contentPanel.getY(), width, getHDimension());
			break;
		case 4:
			height=Integer.parseInt(value);
			contentPanel.setBounds(contentPanel.getX(), contentPanel.getY(), getWDimension(), height);
			break;
		case 5:
			contentPanel.setForeground(getColorFromProperties(value));
			//setForeground(getColorFromProperties(value));
			break;
		case 6:
			contentPanel.setBackground(getColorFromProperties(value));
			//setBackground(getColorFromProperties(value));
			break;
		}
		loadProperties();
	}

	@Override
	public String getSource() {
		return source;
	}

	@Override
	public String getScope() {
		return scope;
	}

	@Override
	public void setScope(String scope) {
		this.scope = scope;
	}
	@Override
	public String getType() {
		return type;
	}
	public String getContentPaneName() {
		return contentPaneName;
	}
}
