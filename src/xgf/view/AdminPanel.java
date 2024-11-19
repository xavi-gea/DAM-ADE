package xgf.view;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

public class AdminPanel {

	private JFrame frame;
	private JButton btnNewUser;
	private JButton btnImportCSV;
	private JScrollPane scrollPane;
	private JTextArea textAreaXMLContent;

	/**
	 * Create the application.
	 */
	public AdminPanel() {
		
		frame = new JFrame();
		frame.setBounds(100, 100, 640, 430);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Admin");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 24));
		lblNewLabel.setBounds(10, 11, 82, 29);
		frame.getContentPane().add(lblNewLabel);
		
		btnNewUser = new JButton("Nuevo Usuario");
		btnNewUser.setBounds(496, 20, 118, 23);
		frame.getContentPane().add(btnNewUser);
		
		btnImportCSV = new JButton("Importar CSV");
		btnImportCSV.setBounds(356, 20, 130, 23);
		frame.getContentPane().add(btnImportCSV);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 51, 604, 329);
		frame.getContentPane().add(scrollPane);
		
		textAreaXMLContent = new JTextArea();
		textAreaXMLContent.setEditable(false);
		scrollPane.setViewportView(textAreaXMLContent);
		
		frame.setVisible(true);
	}

	public JButton getBtnNewUser() {
		return btnNewUser;
	}

	public JButton getBtnImportCSV() {
		return btnImportCSV;
	}
	
	public JTextArea getTextAreaXMLContent() {
		return textAreaXMLContent;
	}
	
	public JFrame getFrame() {
		return frame;
	}

}
