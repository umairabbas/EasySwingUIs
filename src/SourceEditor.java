import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;

import SwingMan.BasicGUITool;
import java.awt.GridLayout;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;


/**
 * @author smvorwerk
 *
 */
public class SourceEditor extends JDialog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6493190097841535601L;
	private final JPanel contentPanel = new JPanel(), buttonPane = new JPanel();
	private final JTextPane sourcePane = new JTextPane();
	private final JScrollPane scrollPane = new JScrollPane(sourcePane);
	private final Editable thisObject;
	private final String originalSource;
	private final JButton saveButton= new JButton("Save"), cancelButton = new JButton("Cancel");;
	private final PropertiesPanel parentInstance;
	private final StyledDocument doc; 
	private final Style[] style= new Style[6];
	private int stylePointer=0;
	//got these from: http://download.oracle.com/javase/tutorial/java/nutsandbolts/_keywords.html
	private String[] keywords= {"abstract", "continue", "for", "new", "switch", "assert", "default", 
						"goto*", "package", "synchronized", "boolean", "do", "if", "private", "this", "break", "double", 
						"implements", "protected", "throw", "byte", "else", "import", "public", "throws", "case", "enum", 
						"instanceof", "return", "transient", "catch", "extends", "int", "short", "try", "char", "final", "interface", 
						"static", "void", "class", "finally", "long", "strictfp**", "volatile", "const*", "float", "native",
						"super", "while" },
					primitives={"byte", "short", "int", "long", "float", "double", "char", "null", "boolean" },
					commentSpecifiers = {"//", "/*", "*/" },
					others = {"=", ";\n" , "{\n","/n}/n", "};\n", "(",")", "@Override"};
	private final JScrollPane scrollPane2 = new JScrollPane();
	private final JTextPane editorPane = new JTextPane();
	private final JLabel lblEditorTitle = new JLabel("Add Custom Source Here (each new line will be automatically formatted):");
	private final JLabel lblGetSourceHeader = new JLabel("Generated Source (non-editable, to change these values, use the Visual UI Editor):");
	private final JLabel label = new JLabel("}");
	
	/**
	 * @param source
	 * @param object
	 * @param parent
	 */
	public SourceEditor(String source, Editable object, PropertiesPanel parent) {
		setAlwaysOnTop(true);
		setTitle("Append Source to Object {"+object.getName()+"}");
		thisObject=object;
		parentInstance=parent;
		sourcePane.setEditable(false);
		sourcePane.setText(source);
		originalSource=source;
		sourcePane.setFont(new Font("Monospaced", Font.PLAIN, 12));
		loadStyles();
		doc = sourcePane.getStyledDocument();
		try {
			doc.remove(0, doc.getLength());
		} catch (BadLocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		doc.addDocumentListener(new MyDocumentListener());
		
		setBounds(100, 100, 699, 499);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(2, 1, 5, 5));
		scrollPane.setViewportBorder(new LineBorder(Color.RED.darker(), 1, true));
		contentPanel.add(scrollPane);
		lblGetSourceHeader.setForeground(Color.MAGENTA.darker());
		lblGetSourceHeader.setFont(new Font("Courier New", Font.BOLD, 12));
		
		scrollPane.setColumnHeaderView(lblGetSourceHeader);
		scrollPane2.setViewportBorder(new LineBorder(Color.BLUE.darker(), 1, true));
		
		contentPanel.add(scrollPane2);
		
		scrollPane2.setViewportView(editorPane);
		editorPane.setFont(new Font("Monospaced", Font.PLAIN, 12));
		if (thisObject.getScope().equals("fullSource")){
			editorPane.setText(((SwingUIEditor)parentInstance.parent).getAppendedSource());
			//stylizeText();
			sourcePane.setText(source);
		} else {
			stylizeText();
			editorPane.setText(object.getSource());
		}
		
		lblEditorTitle.setForeground(Color.GREEN.darker());
		lblEditorTitle.setFont(new Font("Courier New", Font.BOLD, 12));
		
		scrollPane2.setColumnHeaderView(lblEditorTitle);
		
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (thisObject.getScope().equals("fullSource")) {
					((SwingUIEditor)parentInstance.parent).setAppendedSource(editorPane.getText());
				} else {
					String[] lines = editorPane.getText().split("\n");
					for (int i=0;i<lines.length;i++) lines[i]="\t\t"+lines[i]+"\n";
					thisObject.setSource(editorPane.getText());
					parentInstance.loadProperties(thisObject);
				}
				dispose();
			}
		});
		label.setForeground(new Color(0, 178, 0));
		label.setFont(new Font("Courier New", Font.BOLD, 12));
		
		buttonPane.add(label);
		buttonPane.add(saveButton);
		getRootPane().setDefaultButton(saveButton);
		
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		buttonPane.add(cancelButton);
		
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	/**
	 * 
	 */
	private void stylizeText() {
		String[] lineTokens = originalSource.split("\n");
		List<String> tokens;
		int length=0;
		for (int i=0; i<lineTokens.length;i++) {
			String whitespaces="";
			if (lineTokens[i].length()>1) {
			/*	while (lineTokens[i].substring(0,1).equals("\t")) {
					lineTokens[i]=lineTokens[i].substring(1,lineTokens[i].length());
					length++;
					whitespaces+="\t";
				}
				while (lineTokens[i].substring(0,1).equals(" ")) {
					lineTokens[i]=lineTokens[i].substring(1,lineTokens[i].length());
					length++;
					whitespaces+=" ";
				}*/
			}
			//System.out.println("lineToken["+i+"]: \""+whitespaces+"\""+"\n\t"+lineTokens[i]);
			boolean isComment=(lineTokens[i].startsWith("//") || lineTokens[i].startsWith("\t\t//"));
			if (!isComment) {
				tokens = Arrays.asList(lineTokens[i].split(" "));
				getSpecificWordsAndStyleType(tokens,whitespaces);
			} else {
				length+=lineTokens[i].length();
				stylePointer+=length;
				try {
					//System.out.println("StylePointer="+stylePointer);
					doc.insertString(stylePointer-length, whitespaces+lineTokens[i]+"\n", style[2]);
					stylePointer++;
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	}
	/**
	 * @param tokens
	 * @param whitespaces
	 */
	private void getSpecificWordsAndStyleType(List<String> tokens, String whitespaces) {
		int amt = tokens.size();
		//System.out.println(tokens+",{"+amt+"}");
		try {
			//System.out.println("StylePointerWhiteSpaces="+stylePointer+"\n"+"WhiteSpaces:"+whitespaces);
			int length=whitespaces.length();
			stylePointer+=length;
			doc.insertString(stylePointer-length, whitespaces, style[3]);
		} catch (BadLocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//this is the same as the (variable each representing : token.get(i)) for-each loop
		for (int i=0;i<amt;i++) {
			int length=tokens.get(i).length();
			
			//System.out.println("\t\ttoken["+i+"]"+!(i+1>=amt));
			stylePointer+=length+1;
			int styleType = wordHasStyle(tokens.get(i));
			//setParagraphAttributes(offset, length, the "AttribueSet" or style, boolean for replace the old word or insert, i assume)
			if (!(i+1>=amt) && (styleType>=0) ) {
					try {
						//System.out.println("StylePointerWord="+stylePointer+"\n\tWord:"+tokens.get(i));
						doc.insertString(stylePointer-length, tokens.get(i)+" ", style[styleType]);
						//stylePointer++;
					} catch (BadLocationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			} else {
				try {
					//System.out.println("StylePointerNewLineWord="+stylePointer+"\n\tWord:"+tokens.get(i));
					doc.insertString(stylePointer-length, tokens.get(i)+"\n", style[styleType]);
					
				} catch (BadLocationException e) {
					System.out.println("token:"+tokens.get(i));
					e.printStackTrace();
				}
			}
		}
	}


	/**
	 * @param word
	 * @return
	 */
	private int wordHasStyle(String word) {
		/*if (word.split("=").length>1) {
			List<String> s= Arrays.asList(word.split("="));
			getSpecificWordsAndStyleType(s,"");
			return -1;
		} 
		if (word.split(".").length>1) {
			List<String> s= Arrays.asList(word.split("."));
			getSpecificWordsAndStyleType(s,"");
			return -1;
		}*/
		if (isAKeyword(word)) return 0;
		else if (isAPrimitive(word)) return 1;
		else if (isAComment(word)) return 2;
		else if (isInTheOthersCategory(word)) return 4;
		else if (isThisObjectsName(word)) return 5;
		else return 3; //3 being normal in the list
	}

	/**
	 * @param word
	 * @return
	 */
	private boolean isThisObjectsName(String word) {
		return ( (thisObject.getName().equals(word.trim())) || ( ( (word.indexOf('(')>0) ? thisObject.getName().equals(word.substring(0,word.indexOf('('))) : false ) ) );
	}

	/**
	 * @param word
	 * @return
	 */
	private boolean isInTheOthersCategory(String word) {
		//this is just like a for-each loop in php
		//for each element in keywords checkList represents that element inside the for loop
		for(String checkList : others) {
			if (checkList.equals(word.trim())) return true;
		}
		return false;
	}

	/**
	 * @param word
	 * @return
	 */
	private boolean isAComment(String word) {
		//this is just like a for-each loop in php
		//for each element in keywords checkList represents that element inside the for loop
		for(String checkList : commentSpecifiers) {
			if (checkList.startsWith(word.trim())) return true;
		}
		return false;
	}

	/**
	 * @param word
	 * @return
	 */
	private boolean isAPrimitive(String word) {
		//this is just like a for-each loop in php
		//for each element in keywords checkList represents that element inside the for loop
		for(String checkList : primitives) {
			if (checkList.equals(word.trim())) return true;
		}
		return false;
	}

	/**
	 * @param word
	 * @return
	 */
	private boolean isAKeyword(String word) {
		//this is just like a for-each loop in php
		//for each element in keywords checkList represents that element inside the for loop
		for(String checkList : keywords) {
			if (checkList.equals(word.trim())) return true;
		}
		return false;
	}


	/**
	 * 
	 */
	private void loadStyles() {
		style[0] = sourcePane.addStyle("keyword", null);
		StyleConstants.setForeground(style[0], new Color(128,0,128));
		StyleConstants.setBold(style[0], true); 
		
		style[1] = sourcePane.addStyle("primitive", null);
		StyleConstants.setForeground(style[1], Color.blue.darker());
		
		style[2] = sourcePane.addStyle("comment", null);
		StyleConstants.setForeground(style[2], Color.green.darker());
		StyleConstants.setItalic(style[2], true);
		
		style[3] = sourcePane.addStyle("normal", null);
		StyleConstants.setForeground(style[3], Color.black);
		
		style[4] = sourcePane.addStyle("others", null);
		StyleConstants.setForeground(style[4], Color.LIGHT_GRAY.darker());
		
		style[5] = sourcePane.addStyle("thisObjectsName", null);
		StyleConstants.setForeground(style[5], Color.red.brighter());
		StyleConstants.setBold(style[5], true);
	}
	
	private class MyDocumentListener implements DocumentListener {

						
		@Override
		public void changedUpdate(DocumentEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void insertUpdate(DocumentEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void removeUpdate(DocumentEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}

}
