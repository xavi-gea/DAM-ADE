package xgf.view;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;

public class AdminPanel {

	private JFrame frame;
	
	private JButton btnNewUser;

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
		
		frame.setVisible(true);
	}

	public JButton getBtnNewUser() {
		return btnNewUser;
	}
}
