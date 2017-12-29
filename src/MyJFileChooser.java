import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;

import SwingMan.SwingThing;
import javax.swing.border.CompoundBorder;

public class MyJFileChooser extends JLabel implements Editable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5494448086859151500L;
	private String name, source, scope="local", type="JFileChooser";
	private boolean[] implementMethods= {false,false};
	private ImageIcon uiIcon;
	private final Border DEFAULT_BORDER=new SoftBevelBorder(BevelBorder.RAISED), SELECT_BORDER=new CompoundBorder(new LineBorder(Color.red, 1, true), new SoftBevelBorder(BevelBorder.RAISED));
	private int x, y;
	private static final int width=50, height=50;
	private ArrayList<String> propertyNames, propertyValues;

	public MyJFileChooser (String name, int x, int y) {
		setText(name);
		this.name=name;
		this.x=x;
		this.y=y;
		uiIcon=createImageIcon("icos/other1.gif", "JFileChooser");
		setIcon(uiIcon);
		setHorizontalAlignment(JLabel.CENTER);
		setVerticalTextPosition(JLabel.BOTTOM);
		setHorizontalTextPosition(JLabel.CENTER);
		setBorder(DEFAULT_BORDER);
		setFont(new Font("Tahoma", Font.BOLD, 8));
		setText(name);
		source = "";
	}

	protected ImageIcon createImageIcon(String path, String description) {
		java.net.URL imgURL = getClass().getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL, description);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}
	@Override
	public void addTo(JPanel p) {
		setBounds(x,y,width,height);
		p.add(this);
	}

	@Override
	public void erase() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean contains(int x, int y) {
		int x1=x, y1 = y;
		if (x1>=this.x && y1>=this.y && x1 <=this.x+width && y1 <= this.y+height) return true;
		else return false;
	}

	@Override
	public void setSize(int w, int h) {
		// do nothing for this, it doesn't matter
	}

	@Override
	public String describeInCode() {
		String code = "\n\t\t//JFileChooser "+name;
		if (scope.equals("local")) code += "\n\t\tJFileChooser "+name+" = new JFileChooser();";
		else code+="\n\t\t"+name+" = new JFileChooser();";
		if (source.length()>0) code+="\n"+source+"\n\n";

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

	@Override
	public void moveBy(int dx, int dy) {
		x+=dx;
		y+=dy;
	}
	
	@Override
	public int getXPos() {
		// TODO Auto-generated method stub
		return x;
	}

	@Override
	public int getYPos() {
		// TODO Auto-generated method stub
		return y;
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
		this.x=x;
	}

	@Override
	public void setYPos(int y) {
		this.y=y;
	}
	
	public String toString() {
		return "JFileChooser ["+name+"]"+getPropertyValues()+"~"+x+"~"+y;
	}

	@Override
	public void loadProperties() {
		propertyNames = new ArrayList<String>();
		propertyValues = new ArrayList<String>();
		
		propertyNames.add("Variable Name");
		propertyNames.add("Scope");
		propertyNames.add("Source Code");

		propertyValues.add(name+","+OPTION_IS_TEXT);
		propertyValues.add(scope+","+OPTION_IS_SCOPE);		
		propertyValues.add(source+","+OPTION_IS_SOURCE);		
	}

	@Override
	public String getHeaderInfo() {
		/*return "import java.io.BufferedReader;\n"+
			   "import java.io.File;\n"+
			   "import java.io.FileInputStream;\n"+
			   "import java.io.FileWriter;\n"+
			   "import java.io.IOException;\n"+
			   "import java.io.InputStreamReader;\n"+
			   "import java.io.ObjectInputStream;\n"+
			   "import java.io.ObjectOutputStream;\n";
		*/
		return "import java.io.*;\n";
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
		switch (index){
		case 0:
			name=value;
			break;
		case 1:
			scope=value;
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
