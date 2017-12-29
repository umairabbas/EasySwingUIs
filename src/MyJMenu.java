import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;


public class MyJMenu extends JLabel implements MenuObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2429425303963860106L;
	public JPopupMenu menuItemsPanel;
	public MyJMenuBar parentBar;
	private MyJMenu parentMenu;
	private boolean parentIsJMenuBar, hasChildren=false;
	private Border DEFAULT_BORDER=null;
	private Border SELECT_BORDER = new LineBorder(Color.blue.darker());
	private Color highlightedBG = Color.blue.darker();
	private String name,text, source, actionPerformed, mnemonicVal, scope="local", type="JMenu";
	private int x, y, width, height, childCount=0;
	private ArrayList<String> propertyNames, propertyValues;

	public MyJMenu(String name, String text, MyJMenuBar parent) {
		setText(text);
		setFont(new Font("Arial", Font.PLAIN,  10));
		this.name=name;
		this.text=text;
		this.parentBar=parent;
		menuItemsPanel = new JPopupMenu();
		menuItemsPanel.add(new JMenuItem("Add a sub menu object..."));
		parent.add(this);
		width= getGraphics().getFontMetrics().stringWidth(text)+10;
		height=parent.getHeight()-2;
		x=0;
		for (Component comps : parent.getComponents()){
			x+=comps.getWidth();
		}
		y=17;
		parentIsJMenuBar=true;
		actionPerformed="";
		source="";
		mnemonicVal="";
	}
	public MyJMenu(String name, String text, MyJMenu parent) {
		setText(text);
		setFont(new Font("Arial", Font.PLAIN,  10));
		this.name=name;
		this.text=text;
		this.parentMenu=parent;
		menuItemsPanel = new JPopupMenu();
		menuItemsPanel.add(new JMenuItem("Add a sub menu object..."));
		parent.add(this);
		width= getGraphics().getFontMetrics().stringWidth(text)+5;
		height=parent.getHeight()-2;
		x=10;
		for (Component comps : parent.getComponents()){
			x+=comps.getWidth();
		}
		y=17;
		parentIsJMenuBar=false;
	}
	
	public void showMenuItemsPanel() {
		menuItemsPanel.setVisible(true);
		menuItemsPanel.show(this, 0, height);
	}
	public void hideMenuItemsPanel() {
		menuItemsPanel.setVisible(false);
	}
	@Override
	public void moveBy(int dx, int dy) {
		//can't be moved
	}
	
	public Editable getParentObj() {
		if (parentIsJMenuBar) return parentBar;
		else				  return parentMenu;
	}
	
	@Override
	public int getXPos() {
		return x;
	}

	@Override
	public int getYPos() {
		return y;
	}

	@Override
	public void setXPos(int x) {
		// not valid
	}

	@Override
	public void setYPos(int y) {
		// not valid
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
	public void addTo(JPanel p) {
		setBounds(x,y-16,width,height);
		menuItemsPanel.removeAll();
		p.add(this);
	}

	@Override
	public void erase() {
		removeAll();
	}

	@Override
	public boolean contains(int x, int y) {
		int x1=x, y1 = y;
		if (x1>=this.x && y1>=this.y && x1 <=this.x+width && y1 <= this.y+height) return true;
		else return false;
	}

	@Override
	public void setSize(int w, int h) {
		width=w;
		height=h;
	}

	public String getText() {
		return text;
	}

	@Override
	public String describeInCode() {
		String code="\n\t\t//JMenu "+name; 
		if (scope.equals("local")) code+="\n\t\tJMenu "+name+"=new JMenu(\""+text+"\");";
		else code+="\n\t\t"+name+"=new JMenu(\""+text+"\");";
		if (mnemonicVal.length()>0) code+="\n\t\t"+name+".setMnemonic(\'"+mnemonicVal+"\');";
		if (actionPerformed.length()>0) code+="\n\t\t"+name+".addActionListener(new ActionListener() {"+"\n\t\t\t@Override\n\t\t\tpublic void actionPerformed(ActionEvent e) {\n\t\t\t\t"+actionPerformed+"\n\t\t\t}\n\t\t});";
		if (source.length()>0) code+="\n"+source;

		code+="\n\n";
		return code;
	}

	@Override
	public void highLight() {
		setBorder(SELECT_BORDER);
	}

	@Override
	public void unHighLight() {
		setBorder(DEFAULT_BORDER);
	}

	@Override
	public void setName(String newName) {
		name = newName;
	}

	@Override
	public String getName() {
		return name;
	}
	
	public void addChild(MenuObject mObj) {
		menuItemsPanel.add((JMenuItem) mObj);
		menuItemsPanel.paintComponents(getGraphics());
	}
	
	public String toString() {
		return "JMenu ["+name+"]"+"~"+name+"~"+text+"~"+scope+"~"+mnemonicVal+"~"+actionPerformed+"~"+source+"~"+x+"~"+y;
	}
	
	@Override
	public void loadProperties() {
		propertyNames = new ArrayList<String>();
		propertyValues = new ArrayList<String>();
		
		propertyNames.add("Variable Name");
		propertyNames.add("Text");
		propertyNames.add("Scope");
		propertyNames.add("Mnemonic");
		propertyNames.add("actionPerformed");
		propertyNames.add("Source Code");

		propertyValues.add(name+","+OPTION_IS_TEXT);
		propertyValues.add(text+","+OPTION_IS_TEXT);
		propertyValues.add(scope+","+OPTION_IS_SCOPE);
		propertyValues.add(mnemonicVal+","+OPTION_IS_CHAR);
		propertyValues.add(actionPerformed+","+OPTION_IS_TEXT);
		propertyValues.add(source+","+OPTION_IS_SOURCE);
	}

	@Override
	public String getHeaderInfo() {
	/*	return "import java.awt.event.InputEvent;\n"+
			   "import java.awt.event.KeyEvent;\n"+
			   "import javax.swing.KeyStroke;\n";
	*/
		return "";
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
		propertyValues.set(index, value);
		switch (index) {
		case 0:
			name=value;
			break;
		case 1:
			text=value;
			setText(value);
			break;
		case 2:
			scope=value;
			break;
		case 3:
			if (value.trim().length()==1) {
				mnemonicVal=value;
			}
			break;
		case 4:
			actionPerformed=value;
			break;
		}
		loadProperties();

	}

	@Override
	public String getSource() {
		return source;
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
	@Override
	public String getPropertyValues() {
		String s="";
		for (String str : propertyValues){
			System.out.println(str);
			s+="~"+str.substring(0, str.lastIndexOf(','));
		}
		return s.substring(0,s.length()-propertyValues.get(propertyValues.size()-1).length());
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
	@Override
	public void addMenuItemToParent(MyJMenu parent) {
		
	}
}
