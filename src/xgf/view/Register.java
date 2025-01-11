package xgf.view;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * @author Xavi
 */
public class Register {

	private JFrame frmRegister;
	private JTextField textUser;
	private JPasswordField passwordUser;
	private JButton btnRegister;
	private JPasswordField passwordUserRepeat;

	/**
	 * Create the application.
	 */
	public Register() {
		
		frmRegister = new JFrame();
		frmRegister.setTitle("Register");
		frmRegister.setResizable(false);
		frmRegister.setAlwaysOnTop(true);
		frmRegister.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmRegister.setBounds(100, 100, 265, 264);
		frmRegister.getContentPane().setLayout(null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(39, 11, 170, 203);
		frmRegister.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 170, 169);
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
		
		JLabel lblRepetirContrasea = new JLabel("Repeat Password");
		lblRepetirContrasea.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblRepetirContrasea);
		
		passwordUserRepeat = new JPasswordField();
		panel.add(passwordUserRepeat);
		
		JPanel panel2 = new JPanel();
		panel2.setBounds(0, 180, 170, 23);
		panel_1.add(panel2);
		panel2.setLayout(null);
		
		btnRegister = new JButton("Create User");
		btnRegister.setBounds(16, 0, 138, 23);
		panel2.add(btnRegister);
		
		frmRegister.setVisible(true);
	}
	
	public JTextField getTextUser() {
		return textUser;
	}
	
	public JPasswordField getPasswordUser() {
		return passwordUser;
	}
	
	public JButton getBtnRegister() {
		return btnRegister;
	}
	
	public JPasswordField getPasswordUserRepeat() {
		return passwordUserRepeat;
	}
	
	public JFrame getFrame() {
		return frmRegister;
	}
	
}
