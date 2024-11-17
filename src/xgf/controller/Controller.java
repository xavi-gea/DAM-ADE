package xgf.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import xgf.model.Database;
import xgf.model.Model;
import xgf.model.User;
import xgf.view.AdminPanel;
import xgf.view.Login;
import xgf.view.Register;
import xgf.view.View;

public class Controller {
	
	private View view;
	private Model model;
	
	private Login viewLogin;
	private AdminPanel viewAdmin;
	private Register viewRegister;
	
	public Controller(View view, Model model) {
		
		this.view = view;
		this.model = model;
		
		viewLogin = new Login();
		
		initLoginEventHandler();
	}

	private void initLoginEventHandler() {
		
		viewLogin.getBtnLogin().addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				String userName = viewLogin.getTextUser().getText();
				char[] userPassword = viewLogin.getPasswordUser().getPassword();
				
				User user = null;
				
				try {
					
					user = new User(userName,userPassword);
					
				} catch (NoSuchAlgorithmException e1) {
					
					JOptionPane.showMessageDialog(viewLogin.getFrame(), "Error: No se ha podido iniciar sesión");
					
					// TODO Auto-generated catch block
					//e1.printStackTrace();
				}
				
				Database database = new Database("population");
				
				Connection connection;
				
				try {
					
					connection = database.connectToDatabase(user.getName(), user.getPassword());
					
					user.setType(database.getUserType(connection,user.getName()));
					
					connection.close();
					
					viewLogin.getFrame().setVisible(false);
					
				} catch (SQLException e1) {
					
					JOptionPane.showMessageDialog(viewLogin.getFrame(), "Error: Usuario o contraseña incorrectos");
					
					// TODO Auto-generated catch block
					//e1.printStackTrace();
				}
				
				viewLogin.getTextUser().setText("");
				viewLogin.getPasswordUser().setText("");
				
				if (user.getType().equals("admin")) {
					
					viewAdmin = new AdminPanel();
					initAdminEventHandlers();
					
				}else if(user.getType().equals("client")) {
					
					System.out.println("Pasa a ClientPanel");
				}
			}
		});
	}

	private void initAdminEventHandlers() {
		
		viewAdmin.getBtnNewUser().addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				System.out.println("va admin va va");
				viewRegister = new Register();
				initRegisterEventHandlers();
			}
		});
	}
	
	private void initRegisterEventHandlers() {

		viewRegister.getBtnRegister().addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				System.out.println("va registra va va");
			}
		});
	}
}
