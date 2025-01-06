package xgf.view;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.GridLayout;

/**
 * @author Xavi
 */
public class Login {

	private JFrame frame;
	private JTextField textUser;
	private JPasswordField passwordUser;
	private JButton btnLogin;

	/**
	 * Create the application.
	 */
	public Login() {
		
		frame = new JFrame();
		frame.setBounds(100, 100, 265, 194);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(39, 11, 170, 135);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 170, 101);
		panel_1.add(panel);
		panel.setLayout(new GridLayout(0, 1, 0, 0));
		
		JLabel lblUser = new JLabel("User");
		panel.add(lblUser);
		lblUser.setHorizontalAlignment(SwingConstants.CENTER);
		
		textUser = new JTextField();
		panel.add(textUser);
		textUser.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password");
		panel.add(lblPassword);
		lblPassword.setHorizontalAlignment(SwingConstants.CENTER);
		
		passwordUser = new JPasswordField();
		panel.add(passwordUser);
		
		JPanel panel2 = new JPanel();
		panel2.setBounds(0, 112, 170, 23);
		panel_1.add(panel2);
		panel2.setLayout(null);
		
		btnLogin = new JButton("Log In");
		btnLogin.setBounds(16, 0, 138, 23);
		panel2.add(btnLogin);
		
		frame.setVisible(true);
	}
	
	public JTextField getTextUser() {
		return textUser;
	}
	
	public JPasswordField getPasswordUser() {
		return passwordUser;
	}
	
	public JButton getBtnLogin() {
		return btnLogin;
	}
	
	public JFrame getFrame() {
		return frame;
	}
}
