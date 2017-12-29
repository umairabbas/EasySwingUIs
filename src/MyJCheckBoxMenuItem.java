import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;


public class MyJCheckBoxMenuItem extends JCheckBoxMenuItem implements MenuObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7002811951926237951L;
	private String name, text, source, actionPerformed, accelerator, mnemonicVal, scope="local", type="JCheckBoxMenuItem";
	private MyJMenu parent;
	private boolean isChecked=false;
	private ArrayList<String> propertyNames, propertyValues;

	public MyJCheckBoxMenuItem(String name, String text, MyJMenu parent) {
		this.name=name;
		this.text=text;
		this.parent=parent;
		setText(text);
		source="";
		accelerator="";
		mnemonicVal="";
		actionPerformed="";
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
		String code = "\n\t\t//Add JCheckBoxMenuItem "+name;
		if (scope.equals("local")) code+="\n\t\tJCheckBoxMenuItem "+name+" = new JCheckBoxMenuItem(\""+text+"\");";
		else code+="\n\t\t"+name+" = new JCheckBoxMenuItem(\""+text+"\");";
		if (mnemonicVal.length()>0) code+="\n\t\t"+name+".setMnemonic(\'"+mnemonicVal+"\');";
		if (accelerator.length()>0) code+="\n\t\t"+name+".setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_"+accelerator+", InputEvent.CTRL_MASK));";	
		if (isChecked) code+="\n\t\t"+name+".setSelected(true);";
		if (actionPerformed.length()>0) code+="\n\t\t"+name+".addActionListener(new ActionListener() {"+"\n\t\t\t@Override\n\t\t\tpublic void actionPerformed(ActionEvent e) {\n\t\t\t\t"+actionPerformed+"\n\t\t\t}\n\t\t});";
		if (source.length()>0) code+="\n"+source;

		code+="\n\n";
		return code;
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
		return "JCheckBoxMenuItem ["+name+"]"+getPropertyValues()+"~"+parent.getName();
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
		propertyNames.add("isSelected");
		propertyNames.add("actionPerformed");
		propertyNames.add("Source Code");

		propertyValues.add(name+","+OPTION_IS_TEXT);
		propertyValues.add(text+","+OPTION_IS_TEXT);
		propertyValues.add(scope+","+OPTION_IS_SCOPE);
		propertyValues.add(mnemonicVal+","+OPTION_IS_CHAR);
		propertyValues.add(accelerator+","+OPTION_IS_CHAR);
		propertyValues.add(Boolean.toString(isChecked)+","+OPTION_IS_BOOLEAN);
		propertyValues.add(actionPerformed+","+OPTION_IS_TEXT);
		propertyValues.add(source+","+OPTION_IS_SOURCE);
	}

	@Override
	public String getHeaderInfo() {
		//return "import javax.swing.JCheckBoxMenuItem;\n";
		return "";
	}
	@Override
	public boolean contains(int x, int y){
		return(false);
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
			if (value.length()==1) {
				mnemonicVal=value;
				setMnemonic(mnemonicVal.charAt(0));
			}
			break;
		case 4:
			isChecked=Boolean.parseBoolean(value);
			setSelected(isChecked);
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
	public String getName() {
		return name;
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
