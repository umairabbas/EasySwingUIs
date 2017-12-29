import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

import SwingMan.SwingThing;


public class MyJMenuBar extends JPanel implements Editable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4141465834848999134L;
	public Border DEFAULT_BORDER= new MatteBorder(0, 0, 1, 0, Color.gray);
	public Border SELECT_BORDER = new LineBorder(Color.green.darker());
	private String name, source, scope="local", type="JMenuBar";
	int x, y, width, height;
	private MyJMenu chosenMenu;
	private ArrayList<String> propertyNames, propertyValues;

	public MyJMenuBar(String name) {
		setLayout(new FlowLayout());
		this.name = name;
		x=getX();
		y=getY();
		width=getWidth();
		height=getHeight();
		source="";
		loadProperties();
	}
	@Override
	public void paint(Graphics g){
		int cx=0;
		for (Component comps : getComponents()){
			comps.setBounds(cx, 1, comps.getWidth(), getHeight()-1);
			cx+=comps.getWidth()+10;
		}
		super.paint(g);
	}
	@Override
	public void moveBy(int dx, int dy) {
		//can't be moved
	}

	@Override
	public int getXPos() {
		return getX();
	}

	@Override
	public int getYPos() {
		return getY();
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
		return getWidth();
	}

	@Override
	public int getHDimension() {
		// TODO Auto-generated method stub
		return getHeight();
	}

	@Override
	public void addTo(JPanel p) {
		//add to title panels
		p.add(this, BorderLayout.SOUTH);
	}

	@Override
	public void erase() {
		removeAll();
	}

	@Override
	public boolean contains(int x, int y) {
		int x1=x, y1 = y;
		if (x1>=getX() && y1>=getY() && x1 <=getX()+getWidth() && y1 <= getY()+getHeight()) return true;
		else return false;
	}

	@Override
	public void setSize(int w, int h) {
		width =w;
		height =h;		
	}

	@Override
	public String describeInCode() {
		String code ="\n\t\t//JMenuBar "+name; 
		if (scope.equals("local")) code+="\n\t\tJMenuBar "+name+"=new JMenuBar();";
		else code+="\n\t\t"+name+"=new JMenuBar();";
		if (!getBackground().equals(new Color(238,238,238))) code+="\n\t\t"+name+".setBackground(new Color ("+getBackground().getRed()+","+getBackground().getGreen()+","+getBackground().getBlue()+"));";
		if (source.length()>0) code+="\n"+source;

		code+="\n\t\tsetJMenuBar("+name+");\n\n";
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
	
	public String toString() {
		return "JMenuBar["+name+"]"+getPropertyValues();
	}
	@Override
	public void loadProperties() {
		propertyNames = new ArrayList<String>();
		propertyValues = new ArrayList<String>();
		
		propertyNames.add("Variable Name");
		propertyNames.add("Scope");
		propertyNames.add("Background");
		propertyNames.add("Source Code");

		propertyValues.add(name+","+OPTION_IS_TEXT);
		propertyValues.add(scope+","+OPTION_IS_SCOPE);
		propertyValues.add("rgb("+Integer.toString(getBackground().getRed())+", "+Integer.toString(getBackground().getGreen())+", "+Integer.toString(getBackground().getBlue())+")"+","+OPTION_IS_COLOR);
		propertyValues.add(source+","+OPTION_IS_SOURCE);
	}

	@Override
	public String getHeaderInfo() {
		//return "import javax.swing.JMenuBar;\n";
		return "import java.awt.event.*;\n";
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
			name = value;
			break;
		case 1:
			scope=value;
			break;
		case 2:
			setBackground(getColorFromProperties(value));
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
}
