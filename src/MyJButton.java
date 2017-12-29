import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.SoftBevelBorder;

import SwingMan.SwingThing;


public class MyJButton extends JButton implements Editable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7271662909659343679L;
	private String text, name, source, mnemonicVal, eventHandler, scope="local", type="JButton";
	private int x, y, width, height;
	public Border DEFAULT_BORDER;// new SoftBevelBorder(BevelBorder.RAISED);
	public Border SELECT_BORDER = new ResizeBorder(Color.red);
	private ArrayList<String> propertyNames, propertyValues;
	
	public MyJButton(String text, int x, int y, int width, int height) {
		setText(text);
		this.name=text;
		this.text=text;
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
		DEFAULT_BORDER=getBorder();
		setFocusable(false);
		loadProperties();
		mnemonicVal="";
		source="";
		eventHandler="";
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
		propertyNames.add("Mnemonic");
		propertyNames.add("Foreground");
		propertyNames.add("Background");
		propertyNames.add("actionPerformed");
		propertyNames.add("Append Source Code");
		propertyValues.add(name+","+OPTION_IS_TEXT);
		propertyValues.add(text+","+OPTION_IS_TEXT);
		propertyValues.add(scope+","+OPTION_IS_SCOPE);
		propertyValues.add(Integer.toString(width)+","+OPTION_IS_INT);
		propertyValues.add(Integer.toString(height)+","+OPTION_IS_INT);
		propertyValues.add(mnemonicVal+","+OPTION_IS_CHAR);
		propertyValues.add("rgb("+Integer.toString(getForeground().getRed())+", "+Integer.toString(getForeground().getGreen())+", "+Integer.toString(getForeground().getBlue())+")"+","+OPTION_IS_COLOR);
		propertyValues.add("rgb("+Integer.toString(getBackground().getRed())+", "+Integer.toString(getBackground().getGreen())+", "+Integer.toString(getBackground().getBlue())+")"+","+OPTION_IS_COLOR);
		propertyValues.add(eventHandler+","+OPTION_IS_TEXT);
		propertyValues.add(source+","+OPTION_IS_SOURCE);
	}


	
	@Override
	public void addTo(JPanel p) {
		setBounds(x,y,width,height);
		p.add(this);
	}

	@Override
	public void erase() {
		
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
		String code = "\n\t\t//Add JButton "+name;
		if (scope.equals("local")) code+="\n\t\tJButton "+name+" = new JButton(\""+text+"\");";
		else code+="\n\t\t"+name+" = new JButton(\""+text+"\");";
		code+="\n\t\t"+name+".setBounds("+x+","+y+","+width+","+height+");";
		if ( (mnemonicVal.length()>0) && !(mnemonicVal.equals(" ")) ) code+="\n\t\t"+name+".setMnemonic(\'"+mnemonicVal+"\');";
		if (!getForeground().equals(new Color(51,51,51))) code+="\n\t\t"+name+".setForeground(new Color ("+getColorFromProperties(propertyValues.get(6)).getRed()+","+getColorFromProperties(propertyValues.get(6)).getGreen()+","+getColorFromProperties(propertyValues.get(6)).getBlue()+"));";
		if (!getBackground().equals(new Color(238,238,238))) code+="\n\t\t"+name+".setBackground(new Color ("+getColorFromProperties(propertyValues.get(7)).getRed()+","+getColorFromProperties(propertyValues.get(7)).getGreen()+","+getColorFromProperties(propertyValues.get(7)).getBlue()+"));";
		if (propertyValues.get(8).substring(0,propertyValues.get(8).indexOf(',')).length()>0) code+="\n\t\t"+name+".addActionListener(new ActionListener() {"+"\n\t\t\t@Override\n\t\t\tpublic void actionPerformed(ActionEvent e) {\n\t\t\t\t"+propertyValues.get(8).substring(0,propertyValues.get(8).indexOf(','))+"\n\t\t\t}\n\t\t});";
		if (source.length()>0) code+="\n"+source;

		code+="\n\n";
		return code;
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
	public void resize(int newWidth, int newHeight) {
		this.width = newWidth;
		this.height = newHeight;
		setBounds(x,y,width,height);
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
		return "JButton ["+name+"]"+getPropertyValues()+"~"+x+"~"+y;
	}

	@Override
	public String getHeaderInfo() {
		//return "import javax.swing.JButton;\n";
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
			mnemonicVal=value;
			break;
		case 6:
			if (!getColorFromProperties(value).equals(new Color(51,51,51))) setForeground(getColorFromProperties(value));
			break;
		case 7:
			if (!getColorFromProperties(value).equals(new Color(238,238,238)))setBackground(getColorFromProperties(value));
			break;
		case 8:
			eventHandler=value;
			break;
		}
		loadProperties();
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
}
