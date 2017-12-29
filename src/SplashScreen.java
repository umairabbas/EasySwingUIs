import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.border.CompoundBorder;
import javax.swing.border.BevelBorder;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.JProgressBar;


public class SplashScreen extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2176452655934362058L;
	private JProgressBar progressBar;
	private JLabel lblShadow, lblTitle;
	private JPanel cpPanel;
	private Container cp;
	private Timer t;
	private int progress = 0;
	private SwingUIEditor guiEditor;
	
	public SplashScreen() {
		guiEditor = new SwingUIEditor();
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenSize.width - getWidth()) / 4, (screenSize.height - getHeight()) / 4);
		cp = getContentPane();
		cpPanel= new JPanel();
		cpPanel.setBorder(new CompoundBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null), new BevelBorder(BevelBorder.RAISED, null, null, null, null)));
		cpPanel.setBackground(new Color(56,83,112));
		cpPanel.setLayout(null);

		setUndecorated(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setPreferredSize(new Dimension(425,250));

		lblTitle = new JLabel("EasySwingUIs");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setBackground(new Color(92,115,139));
		lblTitle.setForeground(new Color(255,181,66));
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 42));
		lblTitle.setBounds(25, 54, 335, 77);
		cpPanel.add(lblTitle);

		lblShadow = new JLabel("EasySwingUIs");
		lblShadow.setOpaque(true);
		lblShadow.setBorder(new CompoundBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null), new BevelBorder(BevelBorder.RAISED, null, null, null, null)));
		lblShadow.setHorizontalAlignment(SwingConstants.CENTER);
		lblShadow.setForeground(new Color(56, 83, 122));
		lblShadow.setFont(new Font("Tahoma", Font.BOLD, 42));
		lblShadow.setBackground(new Color(92, 115, 139));
		lblShadow.setBounds(25, 55, 345, 77);
		cpPanel.add(lblShadow);

		progressBar = new JProgressBar();
		progressBar.setMinimum(0);
		progressBar.setMaximum(100);
		progressBar.setBackground(new Color(0, 128, 128));
		progressBar.setOpaque(true);
		progressBar.setBorder(new CompoundBorder(new LineBorder(new Color(47, 79, 79), 2, true), new MatteBorder(0, 1, 2, 1, (Color) new Color(143, 188, 143))));
		progressBar.setBounds(73, 142, 317, 38);
		progressBar.setStringPainted(true);
		progressBar.setForeground(new Color(255,181,66));
		cpPanel.add(progressBar);

		Border border = BorderFactory.createTitledBorder("Loading...");
		progressBar.setBorder(border);

		cp.add(cpPanel);
		pack();

		t = new Timer(25,this);
		t.start();
		setVisible(true);
	}

	public static void main(String[] args) {
		SplashScreen loader = new SplashScreen();
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		//Initialize progress property.
		progressBar.setValue(progress);
		progress += Math.random()*11;
		//don't let it go passed 100%
		progressBar.setValue(Math.min(progress, 100));
		if (progress>=100){
			t.stop();
			guiEditor.setVisible(true);
			dispose();
		}
	}
}

