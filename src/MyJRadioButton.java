import java.awt.Color;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;


public class MyJRadioButton extends JPanel implements Editable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 750459431674727459L;
	private String text, name, source, eventHandlerName, scope="local", type="JRadioButton";
	private int x, y, width, height;
	private boolean isOpaque=false;
	public Border DEFAULT_BORDER;
	public Border SELECT_BORDER = new ResizeBorder(Color.red);
	private JRadioButton thisRadButton;
	private ArrayList<String> propertyNames, propertyValues;

	public MyJRadioButton(String name,String defaultText,int x,int y,int width,int height) {
		thisRadButton = new JRadioButton();
		this.name=name;
		this.text=defaultText;
		thisRadButton.setText(defaultText);
		DEFAULT_BORDER=getBorder();
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
		thisRadButton.setBounds(0,0,width,height);
		add(thisRadButton);
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
		propertyNames.add("isOpaque");
		propertyNames.add("Foreground");
		propertyNames.add("Background");
		//propertyNames.add("ButtonGroup");
		propertyNames.add("Source Code");
		propertyValues.add(name+","+OPTION_IS_TEXT);
		propertyValues.add(text+","+OPTION_IS_TEXT);
		propertyValues.add(scope+","+OPTION_IS_SCOPE);
		propertyValues.add(Integer.toString(width)+","+OPTION_IS_INT);
		propertyValues.add(Integer.toString(height)+","+OPTION_IS_INT);
		propertyValues.add(Boolean.toString(isOpaque)+","+OPTION_IS_BOOLEAN);
		propertyValues.add("rgb("+Integer.toString(getForeground().getRed())+", "+Integer.toString(getForeground().getGreen())+", "+Integer.toString(getForeground().getBlue())+")"+","+OPTION_IS_COLOR);
		propertyValues.add("rgb("+Integer.toString(getBackground().getRed())+", "+Integer.toString(getBackground().getGreen())+", "+Integer.toString(getBackground().getBlue())+")"+","+OPTION_IS_COLOR);
		//propertyValues.add(eventHandlerName+","+OPTION_IS_TEXT);
		propertyValues.add(source+","+OPTION_IS_SOURCE);
	}
	
	@Override
	public void addTo(JPanel p) {
		setBounds(x,y,width,height);
		thisRadButton.setBounds(5,5,width-10,height-10);
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
		String code = "\n\t\t//Add JRadioButton "+name;
		if (scope.equals("local")) code+="\n\t\tJRadioButton "+name+" = new JRadioButton(\""+thisRadButton.getText()+"\");";
		else code+="\n\t\t"+name+" = new JRadioButton(\""+thisRadButton.getText()+"\");";
		code+="\n\t\t"+name+".setBounds("+x+","+y+","+width+","+height+");";
		if (isOpaque) code+="\n\t\t"+name+".setOpaque(true);";
		if (!getForeground().equals(new Color(51,51,51))) code+="\n\t\t"+name+".setForeground(new Color ("+getForeground().getRed()+","+getForeground().getGreen()+","+getForeground().getBlue()+"));";
		if (!getBackground().equals(new Color(238,238,238))) code+="\n\t\t"+name+".setBackground(new Color ("+getBackground().getRed()+","+getBackground().getGreen()+","+getBackground().getBlue()+"));";
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
		return "JRadioButton ["+name+"]"+getPropertyValues()+"~"+x+"~"+y;
	}
	@Override
	public String getHeaderInfo() {
	//	return "import javax.swing.JRadioButton;\n";
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
			thisRadButton.setText(value);
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
			isOpaque=Boolean.parseBoolean(value);
			thisRadButton.setOpaque(isOpaque);
			break;
		case 6:
			setForeground(getColorFromProperties(value));
			thisRadButton.setForeground(getColorFromProperties(value));
			break;
		case 7:
			setBackground(getColorFromProperties(value));
			thisRadButton.setBackground(getColorFromProperties(value));
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
