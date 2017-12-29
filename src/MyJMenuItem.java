import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;


public class MyJMenuItem extends JMenuItem implements MenuObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -921141024482876289L;
	private String name, text, source, actionPerformed, accelerator, mnemonicVal, scope="local", type="JMenuItem";
	private MyJMenu parent;
	private ArrayList<String> propertyNames, propertyValues;

	public MyJMenuItem(String name, String text, MyJMenu parent) {
		this.name=name;
		this.text=text;
		this.parent=parent;
		accelerator="";
		mnemonicVal="";
		actionPerformed="";
		setText(text);
		source="";
		loadProperties();
	}
	
	@Override
	public void addTo(JPanel p) {
		parent.menuItemsPanel.add(this);
	}

	@Override
	public void erase() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String describeInCode() {
		String code="\n\t\t//JMenuItem "+name; 
		if (scope.equals("local")) code+="\n\t\tJMenuItem "+name+"=new JMenuItem(\""+text+"\");";
		else code+="\n\t\t"+name+"=new JMenuItem(\""+text+"\");";
 	   	code+="\n\t\t"+parent.getName()+".add("+name+");";
		if (mnemonicVal.length()>0) code+="\n\t\t"+name+".setMnemonic(\'"+mnemonicVal+"\');";
		if (accelerator.length()>0) code+="\n\t\t"+name+".setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_"+accelerator+", InputEvent.CTRL_MASK));";	
		if (propertyValues.get(5).substring(0,propertyValues.get(5).indexOf(',')).length()>0) code+="\n\t\t"+name+".addActionListener(new ActionListener() {"+"\n\t\t\t@Override\n\t\t\tpublic void actionPerformed(ActionEvent e) {\n\t\t\t\t"+propertyValues.get(5).substring(0,propertyValues.get(5).indexOf(','))+"\n\t\t\t}\n\t\t});";
		if (source.length()>0) code+="\n"+source;

		code+="\n\n";
		return code;
	}
	@Override
	public String getName() {
		return name;
	}
	@Override
	public void highLight() {
		setOpaque(true);
		setBackground(Color.blue.darker());
	}

	@Override
	public void unHighLight() {
		setOpaque(false);
	}
	@Override
	public boolean contains(int x, int y){
		return(false);
	}
	@Override
	public void moveBy(int dx, int dy) {
		// TODO Auto-generated method stub
		
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
	public void setXPos(int x) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setYPos(int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getWDimension() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getHDimension() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void addMenuItemToParent(MyJMenu parent) {
		parent.addChild(this);
	}
	
	public String toString() {
		return "JMenuItem ["+name+"]"+getPropertyValues()+"~"+parent.getName();
	}
	
	@Override
	public void loadProperties() {
		propertyNames = new ArrayList<String>();
		propertyValues = new ArrayList<String>();
		
		propertyNames.add("Variable Name");
		propertyNames.add("Text");
		propertyNames.add("Scope");
		propertyNames.add("Mnemonic");
		propertyNames.add("Accelerator");
		propertyNames.add("actionPerformed");
		propertyNames.add("Source Code");

		propertyValues.add(name+","+OPTION_IS_TEXT);
		propertyValues.add(text+","+OPTION_IS_TEXT);
		propertyValues.add(scope+","+OPTION_IS_SCOPE);
		propertyValues.add(mnemonicVal+","+OPTION_IS_CHAR);
		propertyValues.add(accelerator+","+OPTION_IS_CHAR);
		propertyValues.add(actionPerformed+","+OPTION_IS_TEXT);
		propertyValues.add(source+","+OPTION_IS_SOURCE);
	}

	@Override
	public String getHeaderInfo() {
		//return "import javax.swing.JMenuItem;\n";
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
			if (value.trim().length()==1) {
				mnemonicVal=value;
				setMnemonic(mnemonicVal.charAt(0));
			}
			break;
		case 4:
			if (value.trim().length()==1) {
				accelerator=value;
			}
			break;
		case 5:
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
