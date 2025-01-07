package xgf.controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import xgf.model.*;
import xgf.view.*;

public class Controller {

	private MainPanel viewMain;
	private Login viewLogin;
	private Register viewRegister;
	
	private User currentUser;
	
	private static File[] foldersToSearch = {
			new File("." + File.separator + "img" + File.separator + "cards_es"),
			new File("." + File.separator + "img" + File.separator + "cards_fr")
	};
	
	public Controller() {
		
		//Database.connectToDatabase();
		
		viewMain = new MainPanel();
		initMainEventHandler();
	}
	
	private void initMainEventHandler() {
		
		viewMain.getBtnLoadCards().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				List<Card> cardList = new ArrayList<Card>();
				
				for(File folder : foldersToSearch) {
					
					cardList = Card.getCardsfromFolder(folder);
					
					Database.insertCardsToCollection(cardList, folder.getName());
				}
			}
		});
		
		viewMain.getBtnLogin().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				viewLogin = new Login();
				viewLogin.getFrame().setLocationRelativeTo(viewMain.getFrame());
				initLoginEventHandlers();
			}
		});
		
		viewMain.getBtnRegister().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				viewRegister = new Register();
				viewRegister.getFrame().setLocationRelativeTo(viewMain.getFrame());
				initRegisterEventHandlers();
			}
		});
		
		
		viewMain.getBtnStart().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				// if gameStarted is not true and images loaded
				
				if (currentUser != null) {
					
					String[] options = {"Crupier (A.I.)","User (Human)"};
					
					int chosenOption = JOptionPane.showOptionDialog(
							viewMain.getFrame(),
							"Who starts?",
							"",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE,
							null,     
							options,  
							options[0]
					);
					
					if (chosenOption != -1) {
						
						System.out.println("chosen: " + chosenOption);
					}
					
				}else {
					
					JOptionPane.showMessageDialog(viewMain.getFrame(), "There must be a user logged in");
				}			
				
			}
		});
		
		
		viewMain.getBtnLogout().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				// if gameStarted is not true
				
				if (currentUser != null) {
					
					Database.disconnectFromDatabase();
					
					currentUser = null;
					viewMain.getBtnLogin().setEnabled(true);
					viewMain.getBtnLogin().setBackground(new Color(197, 216, 234));
					
				}else {
					
					JOptionPane.showMessageDialog(viewMain.getFrame(), "There must be a user logged in");
				}
			}
		});
	}

	private void initLoginEventHandlers() {
		
		viewLogin.getBtnLogin().addActionListener(new ActionListener() {
			
			/**
			 * When the specified action is performed, try to log the user in
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				
				String userName = viewLogin.getTextUser().getText();
				char[] userPassword = viewLogin.getPasswordUser().getPassword();
				
				try {
					
					userLogin(userName, userPassword);
					
					System.out.println(viewMain.getBtnLogin().getBackground());
					
					
					
					viewMain.getBtnLogin().setBackground(Color.GREEN);
					viewMain.getBtnLogin().setEnabled(false);
					
				} catch (NoSuchAlgorithmException e2) {
					
					JOptionPane.showMessageDialog(viewLogin.getFrame(), "Error: Wrong user or password", "", JOptionPane.ERROR_MESSAGE);
				}
			}
			
			private void userLogin(String userName, char[] userPassword) throws NoSuchAlgorithmException {
				
				User user = new User(userName, userPassword);
				
				if (Database.userExists(user.getName(), user.getPassword())) {
					
					currentUser = user;
					viewLogin.getFrame().setVisible(false);
					
				}else {
					
					throw new NoSuchAlgorithmException();
				}
			}			
		});
	}
	
	private void initRegisterEventHandlers() {
		
		viewRegister.getBtnRegister().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				try {
					
					String userName = viewRegister.getTextUser().getText();
					char[] userPassword = viewRegister.getPasswordUser().getPassword();
					char[] userPasswordRepeated = viewRegister.getPasswordUserRepeat().getPassword();
					
					if (!userName.isEmpty() && !userPassword.equals(null) && !userPasswordRepeated.equals(null)) {
						
						User user = new User(userName, userPassword);
						
						if (!(user.getPassword().equals(User.getHash(userPasswordRepeated)))) {
							
							throw new NullPointerException("The passwords do not match");
						}
						
						if (Database.userExists(user.getName(), user.getPassword())) {
							
							throw new NullPointerException("The user already exists");
						}
						
						Database.setUpUser(user.getName(), user.getPassword());
						
						viewRegister.getFrame().setVisible(false);
						
						JOptionPane.showMessageDialog(viewMain.getFrame(), "User successfully created");
						
					}else {
						
						throw new NullPointerException("There cannot be empty fields");
					}
					
				} catch (NullPointerException e2) {
					
					JOptionPane.showMessageDialog(viewRegister.getFrame(), "Error: " + e2.getMessage(),"",JOptionPane.ERROR_MESSAGE);
					
				}catch (Exception e1) {
					
					JOptionPane.showMessageDialog(viewRegister.getFrame(), "Error: The user could not be created: " + e1.getMessage(),"",JOptionPane.ERROR_MESSAGE);
				}			
			}
		});
	}
}
