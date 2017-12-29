import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;

import SwingMan.SwingThing;
import javax.swing.border.CompoundBorder;

public class MyButtonGroup extends JLabel implements SwingThing, Editable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3883619313006410702L;
	private String name, source;
	private ImageIcon uiIcon;
	private final Border DEFAULT_BORDER=new SoftBevelBorder(BevelBorder.RAISED), SELECT_BORDER=new CompoundBorder(new LineBorder(Color.red, 1, true), new SoftBevelBorder(BevelBorder.RAISED));
	private int x, y;
	private static final int width=50, height=50;
	
	public MyButtonGroup (String name, int x, int y) {
		setText(name);
		this.name=name;
		this.x=x;
		this.y=y;
		uiIcon=createImageIcon("icos/other4.gif", "ButtonGroup");
		setIcon(uiIcon);
		setHorizontalAlignment(JLabel.CENTER);
		setVerticalTextPosition(JLabel.BOTTOM);
		setHorizontalTextPosition(JLabel.CENTER);
		setBorder(DEFAULT_BORDER);
		setFont(new Font("Tahoma", Font.BOLD, 8));
		setText(name);
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
		String code = "\t\\\\Add JLabel "+name+","+x+","+y+","+width+","+height;
		code+="\n\tJLabel "+name+" = new JLabel(\""+name+"\");";
		code+="\n\t"+name+".setBounds("+x+","+y+","+width+","+height+");";
		code+="\n\tadd("+name+");";
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
		// TODO Auto-generated method stub
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
		return "ButtonGroup ["+name+"]";
	}

	@Override
	public void loadProperties() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getHeaderInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getPropertyNamesAndValues() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSource(String newSource) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setProperties(int index, String value) {
		// TODO Auto-generated method stub
		
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
	/*	String s="";
		for (String str : propertyValues){
			s+=" "+str;
		}
		return s;*/
		return null;
	}

	@Override
	public String getScope() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setScope(String scope) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String getType() {
		return null;
	}
}
