package xgf.controller;

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
	
	private User user;
	
	private static File[] foldersToSearch = {
			new File("." + File.separator + "img" + File.separator + "cards_es"),
			new File("." + File.separator + "img" + File.separator + "cards_fr")
	};
	
	public Controller() {
		
		Database.connectToDatabase();
		
		viewMain = new MainPanel();
		initMainEventHandler();
	}
	
	private void initMainEventHandler() {
		
		viewMain.getBtnLogin().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				// if not logged in
				
				viewLogin = new Login();
				initLoginEventHandler();
			}
		});
		
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
	}

	private void initLoginEventHandler() {
		
		viewLogin.getBtnLogin().addActionListener(new ActionListener() {
			
			/**
			 * When the specified action is performed, try to log the user in
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				
				String userName = viewLogin.getTextUser().getText();
				char[] userPassword = viewLogin.getPasswordUser().getPassword();
				
				user = null;
				
				try {
					
					userLogin(userName, userPassword);
					
				} catch (NoSuchAlgorithmException e2) {
					
					JOptionPane.showMessageDialog(viewLogin.getFrame(), "Error: Wrong user or password", "", 0);
				}
			}
			
			private void userLogin(String userName, char[] userPassword) throws NoSuchAlgorithmException {
				
				user = new User(userName, userPassword);
				
//				if (Database.userExists(user.getName(), user.getPassword())) {
//					
//					
//				}
			}
			
			
			
		});
	}
}
