import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import SwingMan.SwingThing;


public class MyJLabel extends JLabel implements Editable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5243123045165938175L;
	private String text, name, source, font, eventHandlerName, scope="local", type="JLabel";
	private int x, y, width, height;
	private boolean isOpaque=false;
	public Border DEFAULT_BORDER;
	public Border SELECT_BORDER = new ResizeBorder(Color.red);
	private ArrayList<String> propertyNames, propertyValues;
	
	public MyJLabel(String text, int x, int y, int width, int height) {
		setText(text);
		this.name=text;
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
		DEFAULT_BORDER=getBorder();
		String fontStyle ="0";
		if (getFont().getStyle()==0) fontStyle="Font.PLAIN";
		else if (getFont().getStyle()==1) fontStyle="Font.BOLD";
		else if (getFont().getStyle()==2) fontStyle="Font.ITALIC";
		else if (getFont().getStyle()==3) fontStyle="Font.BOLD + Font.ITALIC"; 
		this.font = "Font(\""+getFont().getFamily()+"\", "+fontStyle+", "+getFont().getSize()+")";
		eventHandlerName="";
		source="";
		loadProperties();
	}
	
	@Override
	public void loadProperties() {
		propertyNames = new ArrayList<String>();
		propertyValues = new ArrayList<String>();
		propertyNames.add("Variable Name");
		propertyNames.add("Text");
		propertyNames.add("Scope");
		propertyNames.add("Width");
		propertyNames.add("Height");
		propertyNames.add("Font");
		propertyNames.add("isOpaque");
		propertyNames.add("Foreground");
		propertyNames.add("Background");
		propertyNames.add("Source Code");
		propertyValues.add(name+","+OPTION_IS_TEXT);
		propertyValues.add(getText()+","+OPTION_IS_TEXT);
		propertyValues.add(scope+","+OPTION_IS_SCOPE);		
		propertyValues.add(Integer.toString(width)+","+OPTION_IS_INT);
		propertyValues.add(Integer.toString(height)+","+OPTION_IS_INT);
		propertyValues.add("font("+getFont().getName()+", "+getFont().getStyle()+", "+getFont().getSize()+")"+","+OPTION_IS_FONT);
		propertyValues.add(Boolean.toString(isOpaque)+","+OPTION_IS_BOOLEAN);
		propertyValues.add("rgb("+Integer.toString(getForeground().getRed())+", "+Integer.toString(getForeground().getGreen())+", "+Integer.toString(getForeground().getBlue())+")"+","+OPTION_IS_COLOR);
		propertyValues.add("rgb("+Integer.toString(getBackground().getRed())+", "+Integer.toString(getBackground().getGreen())+", "+Integer.toString(getBackground().getBlue())+")"+","+OPTION_IS_COLOR);
		propertyValues.add(source+","+OPTION_IS_SOURCE);
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
		width =w;
		height =h;		
	}

	@Override
	public String describeInCode() {
		String code = "\n\t\t//Add JLabel "+name+","+x+","+y+","+width+","+height;
		if (scope.equals("local")) code+="\n\t\tJLabel "+name+" = new JLabel(\""+getText()+"\");";
		else code+="\n\t\t"+name+" = new JLabel(\""+getText()+"\");";
		code+="\n\t\t"+name+".setBounds("+x+","+y+","+width+","+height+");";
		if (!propertyValues.get(4).startsWith("font(\"Dialog")) {
			String style="Font.PLAIN";
			switch (getFont().getStyle()){
			case 1:
				style="Font.BOLD";
				break;
			case 2:
				style="Font.ITALIC";
				break;
			case 3:
				style="3";
				break;
			}
			code+="\n\t\t"+name+".setFont(new Font(\""+getFont().getFamily()+"\","+style+","+getFont().getSize()+"));";
		}
		if (isOpaque) code+="\n\t\t"+name+".setOpaque(true);";
		if (!getForeground().equals(new Color(51,51,51))) code+="\n\t\t"+name+".setForeground(new Color ("+getColorFromProperties(propertyValues.get(7)).getRed()+","+getColorFromProperties(propertyValues.get(7)).getGreen()+","+getColorFromProperties(propertyValues.get(7)).getBlue()+"));";
		if (!getBackground().equals(new Color(238,238,238))) code+="\n\t\t"+name+".setBackground(new Color ("+getColorFromProperties(propertyValues.get(8)).getRed()+","+getColorFromProperties(propertyValues.get(8)).getGreen()+","+getColorFromProperties(propertyValues.get(8)).getBlue()+"));";
		if (source.length()>0) code+="\n"+source;

		//code+="\n\tadd("+name+");";
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
		return "JLabel ["+name+"]"+getPropertyValues()+"~"+x+"~"+y;
	}

	@Override
	public String getHeaderInfo() {
	//	return "import javax.swing.JLabel;\n";
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
		switch (index){
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
			width=Integer.parseInt(value);
			break;
		case 4:
			height=Integer.parseInt(value);
			break;
		case 5:
			font=value;
			String[] s= value.substring(value.indexOf('(')+1, value.lastIndexOf(')')).split(",");
			setFont(new Font(s[0].trim(),Integer.parseInt(s[1].trim()),Integer.parseInt(s[2].trim())));
			break;
		case 6:
			isOpaque=Boolean.parseBoolean(value);
			setOpaque(isOpaque);
			break;
		case 7:
			setForeground(getColorFromProperties(value));
			break;
		case 8:
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
