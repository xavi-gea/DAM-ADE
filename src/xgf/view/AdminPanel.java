package xgf.view;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTable;

/**
 * @author Xavi
 */
public class AdminPanel implements ViewType {

	private JFrame frame;
	private JButton btnNewUser;
	private JButton btnImportCSV;
	private JButton btnLogout;
	private JScrollPane scrollPane;
	private JTextArea textAreaXMLContent;
	private JTextField textNewQuery;
	private JButton btnNewQuery;
	private JButton btnExportCSV;
	private JScrollPane scrollPane_1;
	private JTable tableQueryResult;

	/**
	 * Create the application.
	 */
	public AdminPanel() {
		
		frame = new JFrame();
		frame.setBounds(100, 100, 666, 524);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Admin");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 24));
		lblNewLabel.setBounds(10, 5, 82, 29);
		frame.getContentPane().add(lblNewLabel);
		
		btnImportCSV = new JButton("Importar CSV");
		btnImportCSV.setBounds(260, 11, 120, 23);
		frame.getContentPane().add(btnImportCSV);
		
		btnNewUser = new JButton("Nuevo Usuario");
		btnNewUser.setBounds(390, 11, 120, 23);
		frame.getContentPane().add(btnNewUser);		
		
		btnLogout = new JButton("Cerrar Sesión");
		btnLogout.setBounds(520, 11, 120, 23);
		frame.getContentPane().add(btnLogout);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 42, 630, 248);
		frame.getContentPane().add(scrollPane);
		
		textAreaXMLContent = new JTextArea();
		textAreaXMLContent.setEditable(false);
		scrollPane.setViewportView(textAreaXMLContent);
		
		JLabel lblNewLabel_1 = new JLabel("Introduce Consulta:");
		lblNewLabel_1.setBounds(10, 303, 114, 14);
		frame.getContentPane().add(lblNewLabel_1);
		
		textNewQuery = new JTextField();
		textNewQuery.setBounds(134, 300, 246, 20);
		frame.getContentPane().add(textNewQuery);
		textNewQuery.setColumns(10);
		
		btnNewQuery = new JButton("Consultar");
		btnNewQuery.setBounds(390, 300, 120, 23);
		frame.getContentPane().add(btnNewQuery);
		
		btnExportCSV = new JButton("Exportar CSV");
		btnExportCSV.setBounds(520, 300, 120, 23);
		frame.getContentPane().add(btnExportCSV);
		
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 331, 630, 143);
		frame.getContentPane().add(scrollPane_1);
		
		tableQueryResult = new JTable();
		tableQueryResult.setEnabled(false);
		scrollPane_1.setViewportView(tableQueryResult);
		
		frame.setVisible(true);
	}
	
	public JButton getBtnImportCSV() {
		return btnImportCSV;
	}
	
	public JButton getBtnNewUser() {
		return btnNewUser;
	}
	
	public JButton getBtnLogout() {
		return btnLogout;
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
	
	public JButton getBtnExportCSV() {
		return btnExportCSV;
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
