package xgf.view;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTable;

public class AdminPanel {

	private JFrame frame;
	private JButton btnNewUser;
	private JButton btnImportCSV;
	private JScrollPane scrollPane;
	private JTextArea textAreaXMLContent;
	private JTextField textNewQuery;
	private JButton btnNewQuery;
	private JTable tableQueryResult;
	private JScrollPane scrollPane_1;

	/**
	 * Create the application.
	 */
	public AdminPanel() {
		
		frame = new JFrame();
		frame.setBounds(100, 100, 640, 524);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Admin");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 24));
		lblNewLabel.setBounds(10, 5, 82, 29);
		frame.getContentPane().add(lblNewLabel);
		
		btnNewUser = new JButton("Nuevo Usuario");
		btnNewUser.setBounds(496, 11, 118, 23);
		frame.getContentPane().add(btnNewUser);
		
		btnImportCSV = new JButton("Importar CSV");
		btnImportCSV.setBounds(356, 11, 130, 23);
		frame.getContentPane().add(btnImportCSV);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 42, 604, 248);
		frame.getContentPane().add(scrollPane);
		
		textAreaXMLContent = new JTextArea();
		textAreaXMLContent.setEditable(false);
		scrollPane.setViewportView(textAreaXMLContent);
		
		JLabel lblNewLabel_1 = new JLabel("Introduce Consulta");
		lblNewLabel_1.setBounds(10, 304, 110, 14);
		frame.getContentPane().add(lblNewLabel_1);
		
		textNewQuery = new JTextField();
		textNewQuery.setBounds(126, 301, 360, 20);
		frame.getContentPane().add(textNewQuery);
		textNewQuery.setColumns(10);
		
		btnNewQuery = new JButton("Consultar");
		btnNewQuery.setBounds(496, 300, 118, 23);
		frame.getContentPane().add(btnNewQuery);
		
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 331, 604, 143);
		frame.getContentPane().add(scrollPane_1);
		
		tableQueryResult = new JTable();
		tableQueryResult.setEnabled(false);
		scrollPane_1.setViewportView(tableQueryResult);
		
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
	
	public JTextField getTextNewQuery() {
		return textNewQuery;
	}
	
	public JButton getBtnNewQuery() {
		return btnNewQuery;
	}
	
	public JTable getTableQueryResult() {
		return tableQueryResult;
	}
	
	public void setTableQueryResult(JTable tableQueryResult) {
		this.tableQueryResult = tableQueryResult;
	}
	
	public JFrame getFrame() {
		return frame;
	}
}
