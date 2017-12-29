import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ListCellRenderer;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.border.LineBorder;
import javax.swing.text.MaskFormatter;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;

import SwingMan.BasicGUITool;


/**
 * @author smvorwerk
 *
 */
public class PropertiesPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5860655539011183364L;
	public JTree objTree;
	private JSplitPane splitPane;
	private JScrollPane treeScrollPane, propsScrollPane;
	private JPanel propertiesPanel, propsLeftSidePanel, propsRightSidePanel, propNamePanel, propValuePanel;
	private JLabel lblPropNameTitle, lblPropertyValue;
	private Font fontProperty=getFont();
	private String objectSource;
	public BasicGUITool parent;
	private Editable currentObject;
	private JButton fontButton;
	private FontChooser fc;

	/**
	 * @param parent
	 */
	public PropertiesPanel(BasicGUITool parent) {
		this.parent = parent;
		setBorder(new MatteBorder(0, 0, 0, 2, new Color(105,105,105)));
		setLayout(new BorderLayout());
		
		objTree = new JTree();
		objTree.setBackground(new Color(255, 255, 240));
		objTree.setBorder(new BevelBorder(BevelBorder.LOWERED));
		
		splitPane = new JSplitPane();
		splitPane.setDividerLocation(300);
		add(splitPane);
		splitPane.setContinuousLayout(true);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		
		treeScrollPane = new JScrollPane(objTree);
		splitPane.setLeftComponent(treeScrollPane);
		
		propertiesPanel = new JPanel();
		propertiesPanel.setLayout(new GridLayout(0,2));
		
		propsScrollPane = new JScrollPane(propertiesPanel);
		splitPane.setRightComponent(propsScrollPane);
		
		propsLeftSidePanel = new JPanel();
		propsLeftSidePanel.setBackground(Color.white);
		propertiesPanel.add(propsLeftSidePanel);
		propsLeftSidePanel.setLayout(new BorderLayout());
		
		lblPropNameTitle = new JLabel("Property Name");
		lblPropNameTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblPropNameTitle.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPropNameTitle.setBorder(new LineBorder(Color.black));
		propsLeftSidePanel.add(lblPropNameTitle, BorderLayout.NORTH);
		
		propNamePanel = new JPanel();
		propNamePanel.setBorder(new LineBorder(Color.black));
		propNamePanel.setBackground(Color.white);
		propsLeftSidePanel.add(propNamePanel, BorderLayout.CENTER);
		
		propsRightSidePanel = new JPanel();
		propsRightSidePanel.setBackground(Color.white);
		propertiesPanel.add(propsRightSidePanel);
		propsRightSidePanel.setLayout(new BorderLayout());
		
		lblPropertyValue = new JLabel("Property Value");
		lblPropertyValue.setHorizontalAlignment(SwingConstants.CENTER);
		lblPropertyValue.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPropertyValue.setBorder(new LineBorder(Color.black));
		propsRightSidePanel.add(lblPropertyValue, BorderLayout.NORTH);
		
		propValuePanel = new JPanel();
		propValuePanel.setBorder(new LineBorder(Color.black));
		propValuePanel.setBackground(Color.white);
		propsRightSidePanel.add(propValuePanel, BorderLayout.CENTER);
		
		propNamePanel.setLayout(null);
		propValuePanel.setLayout(null);
		
	}
	/**
	 * @param object
	 */
	public void loadProperties(Editable object){
		propNamePanel.removeAll();
		propValuePanel.removeAll();
		currentObject = object;
		currentObject.loadProperties();
		String[] propList = object.getPropertyNamesAndValues();
		String[] propNames = new String[propList.length];
		String[] propVals = new String[propList.length];
		int[] propTypes = new int[propList.length];
		for (int i=0;i<propList.length;i++){
			//start til first comma
			propNames[i]=propList[i].substring(0, propList[i].indexOf(','));
			//everything in the middle
			propVals[i]=propList[i].substring(propList[i].indexOf(',')+1,propList[i].lastIndexOf(','));
			//start after the last comma til the end
			propTypes[i]=Integer.parseInt(propList[i].substring(propList[i].lastIndexOf(',')+1,propList[i].length()));
		}
		updatePropertyList(propNames, propVals, propTypes);
		repaint();
		propNamePanel.repaint();
		propValuePanel.repaint();
	}
	/**
	 * @param names
	 * @param values
	 * @param types
	 */
	public void updatePropertyList(String[] names, String[] values, int[] types){
		JLabel[] currentPropName=new JLabel[names.length];
		JComponent[] propValues = new JComponent[names.length];
		propNamePanel.setLayout(new GridLayout(names.length+1,1));
		propValuePanel.setLayout(new GridLayout(names.length+1,1));
		for (int i=0;i<names.length;i++){
			currentPropName[i] = new JLabel(names[i]);
			currentPropName[i].setBorder(new LineBorder(Color.black));
			propNamePanel.add(currentPropName[i]);
			propValues[i] = getAssociatedPropertyComponent(types[i]);
			propValues[i].setBorder(new LineBorder(Color.black));
			propValuePanel.add(propValues[i]);
			switch (types[i]) {
				case Editable.OPTION_IS_TEXT:
					((JTextField)propValues[i]).setText(values[i]);
					break;
				case Editable.OPTION_IS_INT:
					((JFormattedTextField)propValues[i]).setText(values[i]);
					break;
				case Editable.OPTION_IS_BOOLEAN:
					((JComboBox)propValues[i]).addItem(values[i]);
					if (values[i].equals("true")) ((JComboBox)propValues[i]).addItem("false");
					else ((JComboBox)propValues[i]).addItem("true");
					break;
				case Editable.OPTION_IS_CHAR:
					if (propValues[i] instanceof JFormattedTextField) {
						((JFormattedTextField)propValues[i]).setText(values[i]);
					} else {
						((JTextField)propValues[i]).setText(values[i]);
					}
					break;
				case Editable.OPTION_IS_COLOR:
					((JButton)propValues[i]).setText(values[i]);
					((JButton)propValues[i]).addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							Color c = JColorChooser.showDialog(null, "<< Choose A Color >>", ((JButton)e.getSource()).getBackground());
							((JButton)e.getSource()).setBackground(c);
							((JButton)e.getSource()).setText("rgb("+c.getRed()+","+c.getGreen()+","+c.getBlue()+")");
						}
					});
					break;
				case Editable.OPTION_IS_FONT:
					((JButton)propValues[i]).setText(values[i]);
					((JButton)propValues[i]).addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							fc = new FontChooser(fontProperty);
							fc.setVisible(true);
						}
					});
					break;
				case Editable.OPTION_IS_EH:
					((JButton)propValues[i]).setText(values[i]);
					((JButton)propValues[i]).addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							fontButton=(JButton)e.getSource();
							EventHandlerChooser ehc = new EventHandlerChooser();
							ehc.setVisible(true);
							ehc.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
						}
					});
					break;
				case Editable.OPTION_IS_SOURCE:
					objectSource=values[i];
					((JButton)propValues[i]).addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							SourceEditor srcEdit = new SourceEditor(currentObject.describeInCode(), currentObject, PropertiesPanel.this);
							srcEdit.setVisible(true);
						}
					});
					break;
				case Editable.OPTION_IS_SCOPE:
					((JComboBox)propValues[i]).addItem(values[i]);
					if (values[i].equals("local")){
						((JComboBox)propValues[i]).addItem("public");
						((JComboBox)propValues[i]).addItem("private");
					} else if (values[i].equals("public")) {
						((JComboBox)propValues[i]).addItem("local");
						((JComboBox)propValues[i]).addItem("private");
					} else if (values[i].equals("private")) {
						((JComboBox)propValues[i]).addItem("public");
						((JComboBox)propValues[i]).addItem("local");
					} else {
						((JComboBox)propValues[i]).remove(0);
						((JComboBox)propValues[i]).addItem("local");
						((JComboBox)propValues[i]).addItem("public");
						((JComboBox)propValues[i]).addItem("private");
					}
				break;
			}
		}
		JButton saveButton = new JButton("Save Changes");
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveChanges();
				repaint();
			}
		});
		JButton restoreButton = new JButton("Restore Values");
		restoreButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadProperties(currentObject);
			}
		});
		propNamePanel.add(restoreButton);
		propValuePanel.add(saveButton);
		repaint();
	}
	
	/**
	 * 
	 */
	protected void saveChanges() {
		Component[] comps= propNamePanel.getComponents();
		Component[] newValues= propValuePanel.getComponents();
		for (int i=0;i<comps.length-2;i++){
			String newProperty="";
			if (newValues[i] instanceof JTextField) newProperty = ((JTextField)newValues[i]).getText();
			else if (newValues[i] instanceof JFormattedTextField) newProperty = ((JFormattedTextField)newValues[i]).getText();
			else if (newValues[i] instanceof JComboBox) newProperty = ((JComboBox)newValues[i]).getSelectedItem().toString();
			else if (newValues[i] instanceof JButton) newProperty = ((JButton)newValues[i]).getText();
			currentObject.setProperties(i, newProperty);
		}

		((SwingUIEditor)parent).callRefresh();
	}
	
	/**
	 * @param type
	 * @return
	 */
	private JComponent getAssociatedPropertyComponent(int type) {

		switch (type) {
			case Editable.OPTION_IS_TEXT:
				JTextField textField = new JTextField();
				return textField;
			case Editable.OPTION_IS_INT:
				NumberFormat customFormat = NumberFormat.getIntegerInstance();
		        customFormat.setMaximumIntegerDigits(4);
		        customFormat.setMinimumIntegerDigits(2);
		        JFormattedTextField textFieldForIntegerValues = new JFormattedTextField(customFormat);
		        return textFieldForIntegerValues;
			case Editable.OPTION_IS_BOOLEAN:
				JComboBox comboBox = new JComboBox();
				return comboBox;
			case Editable.OPTION_IS_CHAR:
				try {
					//? represents a single letter
					MaskFormatter charFormatter = new MaskFormatter("?");
					JFormattedTextField charFormattedTextField = new JFormattedTextField(charFormatter);
					charFormattedTextField.addKeyListener(new KeyListener() {
						@Override
						public void keyPressed(KeyEvent arg0) {
						}

						@Override
						public void keyReleased(KeyEvent arg0) {				
						}

						@Override
						public void keyTyped(KeyEvent e) {
							char c = e.getKeyChar();
							if (!(c>='A' && c<='Z')){
								if (!(c>='a' && c<='z')) {
									JOptionPane.showMessageDialog(PropertiesPanel.this,"Please enter a single character [A-Z] or [a-z].");
								} else {
									((JFormattedTextField)e.getComponent()).setText(Character.toString(c));
								}
							} else {
								((JFormattedTextField)e.getComponent()).setText(Character.toString(c));
							}
							if (((JFormattedTextField)e.getComponent()).getText().length()>1) ((JFormattedTextField)e.getComponent()).setText(Character.toString(c));
							if (((JFormattedTextField)e.getComponent()).getText().length()==0) ((JFormattedTextField)e.getComponent()).setText("");
						}
					});
					return charFormattedTextField;
				} catch (ParseException e) {
					e.printStackTrace();
					JTextField charTextField = new JTextField();
					charTextField.addKeyListener(new KeyListener() {
						@Override
						public void keyPressed(KeyEvent arg0) {
						}

						@Override
						public void keyReleased(KeyEvent arg0) {				
						}

						@Override
						public void keyTyped(KeyEvent e) {
							char c = e.getKeyChar();
							if (!(c>='A' && c<='Z') || !(c>='a' && c<='z')) JOptionPane.showMessageDialog(PropertiesPanel.this,"Please enter a single character [A-Z] or [a-z].");
							if (((JTextField)e.getComponent()).getText().length()>1) ((JTextField)e.getComponent()).setText(Character.toString(c));
						}
					});
					return charTextField;
				}
			case Editable.OPTION_IS_COLOR:
				JButton buttonColor = new JButton();
				return buttonColor;
			case Editable.OPTION_IS_FONT:
				JButton buttonFont = new JButton();
				fontButton=buttonFont;
				return buttonFont;
			case Editable.OPTION_IS_EH:
				JButton buttonMulti=new JButton("Options");
				return buttonMulti;
			case Editable.OPTION_IS_SOURCE:
				JButton button = new JButton("Edit Source...");
				return button;
			case Editable.OPTION_IS_SCOPE:
				JComboBox comboBoxScope = new JComboBox();
				return comboBoxScope;
			default:
				return new JTextField();
		}
	}
	private void closeFontWindow(Font chosenFont) {
		fontProperty=new Font(chosenFont.getName(),chosenFont.getStyle(),chosenFont.getSize());
		fontButton.setText(("font("+fontProperty.getName()+","+fontProperty.getStyle()+","+fontProperty.getSize()+")"));
	}

	/**
	 * @author smvorwerk
	 *
	 */
	private class FontChooser extends JDialog {

		private static final long serialVersionUID = 1L;
		private final JPanel contentPanel = new JPanel();
		private JScrollPane familyScroller, styleScroller, sizeScroller;
		private JList fontFamilyList, fontStyleList, fontSizeList;
		private Font returnFont=null;
		
		/**
		 * @param currentFont
		 */
		public FontChooser(Font currentFont) {
			setBounds(100, 100, 450, 300);
			setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			setVisible(true);
			getContentPane().setLayout(new BorderLayout());
			contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
			getContentPane().add(contentPanel, BorderLayout.CENTER);
			contentPanel.setLayout(new GridLayout(1, 3));
			
			JPanel fontFamPanel = new JPanel();
			fontFamPanel.setBorder(new TitledBorder("Font Family"));
			contentPanel.add(fontFamPanel);
			fontFamPanel.setLayout(new BorderLayout());
			fontFamilyList = new JList();
			fontFamilyList.setBorder(new BevelBorder(BevelBorder.LOWERED));
			familyScroller=new JScrollPane(fontFamilyList);
			fontFamPanel.add(familyScroller, BorderLayout.CENTER);
			
			JPanel fontStylePanel = new JPanel();
			fontStylePanel.setBorder(new TitledBorder("Font Style"));
			contentPanel.add(fontStylePanel);
			fontStylePanel.setLayout(new BorderLayout());
			fontStyleList = new JList();
			fontStyleList.setBorder(new BevelBorder(BevelBorder.LOWERED));
			styleScroller=new JScrollPane(fontStyleList);
			fontStylePanel.add(styleScroller, BorderLayout.CENTER);
			
			JPanel fontSizePanel = new JPanel();
			fontSizePanel.setBorder(new TitledBorder("Font Size"));
			contentPanel.add(fontSizePanel);
			fontSizePanel.setLayout(new BorderLayout());
			fontSizeList = new JList();
			fontSizeList.setBorder(new BevelBorder(BevelBorder.LOWERED));
			sizeScroller=new JScrollPane(fontSizeList);
			fontSizePanel.add(sizeScroller, BorderLayout.CENTER);
			
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			JButton okButton = new JButton("OK");
			okButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int style=0, size=10;
					System.out.println(fontFamilyList.getSelectedValue()+","+fontStyleList.getSelectedValue()+","+fontSizeList.getSelectedValue());
					if (fontStyleList.getSelectedValue().equals("BOLD")) style =1;
					else if (fontStyleList.getSelectedValue().equals("ITALIC")) style=2;
					else if (fontStyleList.getSelectedValue().equals("BOLD+ITALIC")) style=3;
					size = Integer.parseInt((String) fontSizeList.getSelectedValue());
					returnFont= new Font((String) fontFamilyList.getSelectedValue(), style, size);
					closeFontWindow(returnFont);
					dispose();
				}
			});
			buttonPane.add(okButton);
			getRootPane().setDefaultButton(okButton);
			
			JButton cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			buttonPane.add(cancelButton);
			
			JPanel previewPanel = new JPanel();
			previewPanel.setLayout(new BorderLayout());
			getContentPane().add(previewPanel, BorderLayout.NORTH);
			JLabel lblPreview = new JLabel("Preview of Font Choice");
			previewPanel.add(lblPreview, BorderLayout.CENTER);
			String currFontStyle="PLAIN";
			if (currentFont.getStyle()==1) currFontStyle="BOLD";
			else if (currentFont.getStyle()==2) currFontStyle="ITALIC";
			else if (currentFont.getStyle()==3) currFontStyle="BOLD+ITALIC";
			String currFont = currentFont.getFamily()+", "+currFontStyle+", "+currentFont.getSize();
			JLabel lblFont = new JLabel("Preview of Font: "+currFont);
			previewPanel.add(lblFont, BorderLayout.SOUTH);
			
			loadFonts();
			lblPreview.setFont(currentFont);
			revalidate();
		}
		
		/**
		 * 
		 */
		private void loadFonts() {
			String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
			String[] styles = { "PLAIN", "BOLD", "ITALIC", "BOLD+ITALIC" };
			String[] sizes = { "8", "9", "10", "11", "12", "14", "16", "18", "20", "22", "24", "26", "28", "36", "48", "72" };
			
			fontFamilyList.setListData(fonts);
			fontStyleList.setListData(styles);
			fontSizeList.setListData(sizes);
		}

	}
	
	/**
	 * @author smvorwerk
	 *
	 */
	private class EventHandlerChooser extends JDialog {

		/**
		 * 
		 */
		private static final long serialVersionUID = -4670826925240491791L;
		private final JPanel contentPanel = new JPanel();
		private final JCheckBox chckbxActionlistener, chckbxMouselistener, chckbxMousemotionlistener,chckbxWindowlistener;

		public EventHandlerChooser() {
			setTitle("EventHandler Implementations");
			setBounds(100, 100, 450, 300);
			getContentPane().setLayout(new BorderLayout());
			contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
			getContentPane().add(contentPanel, BorderLayout.CENTER);
			contentPanel.setLayout(new BorderLayout(0, 0));
			JLabel lblTitle = new JLabel("Please Choose The EventHandler's Implementations: ");
			lblTitle.setFont(new Font("Tahoma", Font.BOLD, 12));
			contentPanel.add(lblTitle, BorderLayout.NORTH);
			JPanel panel = new JPanel();
			contentPanel.add(panel, BorderLayout.CENTER);
			panel.setLayout(new GridLayout(4, 1));
			chckbxActionlistener = new JCheckBox("ActionListener");
			panel.add(chckbxActionlistener);
			chckbxMouselistener = new JCheckBox("MouseListener");
			panel.add(chckbxMouselistener);
			chckbxMousemotionlistener = new JCheckBox("MouseMotionListener");
			panel.add(chckbxMousemotionlistener);
			chckbxWindowlistener = new JCheckBox("WindowListener");
			panel.add(chckbxWindowlistener);
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			JButton okButton = new JButton("OK");
			okButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String s="";
					if (chckbxActionlistener.isSelected()) s+="ActionListener,";
					if (chckbxMouselistener.isSelected()) s+="MouseListener,";
					if (chckbxMousemotionlistener.isSelected()) s+="MouseMotionListener,";
					if (chckbxWindowlistener.isSelected()) s+="WindowListener,";
					s=s.substring(0,s.length()-1);
					Component[] comps = propValuePanel.getComponents();
					((JButton)comps[1]).setText(s);
					dispose();
				}
			});
			buttonPane.add(okButton);
			getRootPane().setDefaultButton(okButton);
			JButton cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			buttonPane.add(cancelButton);
		}
		

	}
}
