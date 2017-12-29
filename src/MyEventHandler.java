import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.io.Serializable;
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

public class MyEventHandler extends JLabel implements Editable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7076626840048323725L;
	/**
	 * 
	 */
	private String name, source, scope="local", type="EventHandler";
	private ImageIcon uiIcon;
	private String[] offeredImplementations = {"ActionListener", "MouseListener", "MouseMotionListener", "WindowListener" };
	private final Border DEFAULT_BORDER=new SoftBevelBorder(BevelBorder.RAISED), SELECT_BORDER=new CompoundBorder(new LineBorder(Color.red, 1, true), new SoftBevelBorder(BevelBorder.RAISED));
	private int x, y;
	private static final int width=50, height=50;
	private ArrayList<String> propertyNames, propertyValues;
	private String[] implementedTypes = {"ActionListener"};
	private ArrayList<Editable> attachedObjects;

	public MyEventHandler (String name, int x, int y) {
		setText(name);
		this.name=name;
		this.x=x;
		this.y=y;
		uiIcon=createImageIcon("icos/other2.gif", "EventHandler");
		setIcon(uiIcon);
		setHorizontalAlignment(JLabel.CENTER);
		setVerticalTextPosition(JLabel.BOTTOM);
		setHorizontalTextPosition(JLabel.CENTER);
		setBorder(DEFAULT_BORDER);
		setFont(new Font("Tahoma", Font.BOLD, 8));
		setText(name);
		attachedObjects=new ArrayList<Editable>();
		source="";
	}

	private String getAssociatedMethods() {
		String[] types = getImplementedTypes();
		String code="\n";
		if (types.length>0) {
			for (int i=0;i<types.length;i++){
				code+=addMethodsToSource(types[i]);
			}
		}
		return code;
	}

	private String addMethodsToSource(String listenerName) {
		int whichImplementation=-1;
		String code="\n";
		for (int i=0;i<offeredImplementations.length;i++){
			if (listenerName.equals(offeredImplementations[i])) {
				whichImplementation=i;
				break;
			}
		}
		switch (whichImplementation) {
			case -1:
				return code;
			case 0:
				code+="\n\t\t@Override"+
					  "\n\t\tpublic void actionPerformed(ActionEvent arg0) {"+
					  "\n\n\t\t}\n";
				break;
			case 1:
				code+="\n\t\t@Override"+
					  "\n\t\tpublic void mouseClicked(MouseEvent e) {"+
					  "\n\n\t\t}\n"+
					  "\n\t\t@Override"+
					  "\n\t\tpublic void mouseEntered(MouseEvent e) {"+
					  "\n\n\t\t}\n"+
					  "\n\t\t@Override"+
					  "\n\t\tpublic void mouseExited(MouseEvent e) {"+
					  "\n\n\t\t}\n"+
					  "\n\t\t@Override"+
					  "\n\t\tpublic void mousePressed(MouseEvent e) {"+
					  "\n\n\t\t}\n"+
					  "\n\t\t@Override"+
					  "\n\t\tpublic void mouseReleased(MouseEvent e) {"+
					  "\n\n\t\t}\n";
				break;
			case 2:
				code+="\n\t\t@Override"+
					  "\n\t\tpublic void mouseDragged(MouseEvent e) {"+
					  "\n\n\t\t}\n"+
					  "\n\t\t@Override"+
					  "\n\t\tpublic void mouseMoved(MouseEvent e) {"+
					  "\n\n\t\t}\n";
				break;
			case 3:
				code+="\n\t\t@Override"+
					  "\n\t\tpublic void windowActivated(WindowEvent e) {"+
					  "\n\n\t\t}\n"+
					  "\n\t\t@Override"+
					  "\n\t\tpublic void windowClosed(WindowEvent e) {"+	
					  "\n\n\t\t}\n"+
					  "\n\t\t@Override"+
					  "\n\t\tpublic void windowClosing(WindowEvent e) {"+	
					  "\n\n\t\t}\n"+
					  "\n\t\t@Override"+
					  "\n\t\tpublic void windowDeactivated(WindowEvent e) {"+		
					  "\n\n\t\t}\n"+
					  "\n\t\t@Override"+
					  "\n\t\tpublic void windowDeiconified(WindowEvent e) {"+
					  "\n\n\t\t}\n"+
					  "\n\t\t@Override"+
					  "\n\t\tpublic void windowIconified(WindowEvent e) {"+	
					  "\n\n\t\t}\n"+
					  "\n\t\t@Override"+
					  "\n\t\tpublic void windowOpened(WindowEvent e) {"+
					  "\n\n\t\t}\n";
				break;
		}
		return code;
	}

	private String[] getImplementedTypes() {
		String[] types = implementedTypes;
		return types;
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
		String code = "\n\t\t//Event Handler "+name;
		code+="\n\tpublic class "+name+" implements "+implementedTypes+" {";
		code+="\n\t\t"+name+".setBounds("+x+","+y+","+width+","+height+");";
		code+=source;
		code+="\n\t}\n\n";
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
		return "EventHandler ["+name+"]"+getPropertyValues()+"~"+x+"~"+y;
	}

	@Override
	public void loadProperties() {
		propertyNames = new ArrayList<String>();
		propertyValues = new ArrayList<String>();
		
		propertyNames.add("Variable Name");
		propertyNames.add("Implementations");
		propertyNames.add("Scope");
		propertyNames.add("Source Code");

		propertyValues.add(name+","+OPTION_IS_TEXT);
		propertyValues.add(getImplementedTypes()+","+OPTION_IS_EH);
		propertyValues.add(scope+","+OPTION_IS_SCOPE);	
		propertyValues.add(source+","+OPTION_IS_SOURCE);	
	}

	@Override
	public String getHeaderInfo() {
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

	public void setImplementedTypes(String[] s) {
		implementedTypes = s;
	}
	
	@Override
	public void setProperties(int index, String value) {
		switch (index) {
		case 0:
			name=value;
			break;
		case 1:
			String[] s = value.split(",");
			setImplementedTypes(s);
			break;
		case 2:
			scope=value;
			break;
		}
		propertyValues.set(index, value);
		getSource();
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
