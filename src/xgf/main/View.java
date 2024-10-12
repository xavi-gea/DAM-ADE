package xgf.main;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import java.awt.FlowLayout;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.border.MatteBorder;
import java.awt.Color;

public class View extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JButton btnChooseDirectory;
	private JPanel panel_1;
	private JTextField textToFindInDirectory;
	private JButton btnFindInDirectory;
	private JCheckBox chckbxCheckUppercase;
	private JCheckBox chckbxCheckAccent;
	private JTextField textToReplaceInDirectory;
	private JCheckBox chckbxReplaceText;

	/**
	 * Create the frame.
	 */
	public View() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 730, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		
		JPanel panel = new JPanel();
		panel.setBorder(new MatteBorder(0, 0, 1, 0, (Color) new Color(0, 0, 0)));
		contentPane.add(panel);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		btnChooseDirectory = new JButton("Elije un directorio para mostrar contenido");
		panel.add(btnChooseDirectory);
		
		panel_1 = new JPanel();
		panel_1.setBorder(new MatteBorder(0, 0, 1, 0, (Color) new Color(0, 0, 0)));
		contentPane.add(panel_1);
		panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		chckbxCheckUppercase = new JCheckBox("Respetar mayúsculas");
		chckbxCheckUppercase.setSelected(true);
		panel_1.add(chckbxCheckUppercase);
		
		chckbxCheckAccent = new JCheckBox("Respetar acentos");
		chckbxCheckAccent.setSelected(true);
		panel_1.add(chckbxCheckAccent);
		
		chckbxReplaceText = new JCheckBox("Reemplazar texto con:");
		panel_1.add(chckbxReplaceText);
		
		textToReplaceInDirectory = new JTextField();
		textToReplaceInDirectory.setEnabled(false);
		panel_1.add(textToReplaceInDirectory);
		textToReplaceInDirectory.setColumns(10);
		
		btnFindInDirectory = new JButton("Elije un directorio para buscar el texto:");
		panel_1.add(btnFindInDirectory);
		
		textToFindInDirectory = new JTextField();
		panel_1.add(textToFindInDirectory);
		textToFindInDirectory.setColumns(10);
		
		setVisible(true);
	}
	
	public JPanel getContentPane() {
		return contentPane;
	}

	public JButton getBtnchooseDirectory() {
		return btnChooseDirectory;
	}

	public JTextField getTextToFindInDirectory() {
		return textToFindInDirectory;
	}

	public JButton getBtnFindInDirectory() {
		return btnFindInDirectory;
	}

	public JCheckBox getChckbxCheckUppercase() {
		return chckbxCheckUppercase;
	}

	public JCheckBox getChckbxCheckAccent() {
		return chckbxCheckAccent;
	}

	public JTextField getTextToReplaceInDirectory() {
		return textToReplaceInDirectory;
	}

	public JCheckBox getChckbxReplaceText() {
		return chckbxReplaceText;
	}
}
