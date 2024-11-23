package xgf.view;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

/**
 * @author Xavi
 */
public class ClientPanel implements ViewType {

	private JFrame frame;
	private JButton btnLogout;
	private JTextField textNewQuery;
	private JButton btnNewQuery;
	private JButton btnExportCSV;
	private JScrollPane scrollPane_1;
	private JTable tableQueryResult;

	/**
	 * Create the application.
	 */
	public ClientPanel() {
		
		frame = new JFrame();
		frame.setBounds(100, 100, 666, 261);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Cliente");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 24));
		lblNewLabel.setBounds(10, 5, 82, 29);
		frame.getContentPane().add(lblNewLabel);	
		
		btnLogout = new JButton("Cerrar Sesión");
		btnLogout.setBounds(520, 11, 120, 23);
		frame.getContentPane().add(btnLogout);
		
		JLabel lblNewLabel_1 = new JLabel("Introduce Consulta:");
		lblNewLabel_1.setBounds(10, 49, 114, 14);
		frame.getContentPane().add(lblNewLabel_1);
		
		textNewQuery = new JTextField();
		textNewQuery.setBounds(134, 46, 246, 20);
		frame.getContentPane().add(textNewQuery);
		textNewQuery.setColumns(10);
		
		btnNewQuery = new JButton("Consultar");
		btnNewQuery.setBounds(390, 45, 120, 23);
		frame.getContentPane().add(btnNewQuery);
		
		btnExportCSV = new JButton("Exportar CSV");
		btnExportCSV.setBounds(520, 45, 120, 23);
		frame.getContentPane().add(btnExportCSV);
		
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 74, 630, 143);
		frame.getContentPane().add(scrollPane_1);
		
		tableQueryResult = new JTable();
		tableQueryResult.setEnabled(false);
		scrollPane_1.setViewportView(tableQueryResult);
		
		frame.setVisible(true);
	}
	
	public JButton getBtnLogout() {
		return btnLogout;
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
