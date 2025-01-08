package xgf.controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

import java.awt.Image;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.io.ByteArrayInputStream;
import javax.swing.JOptionPane;

import xgf.model.*;
import xgf.view.*;

public class Controller {

	private MainPanel viewMain;
	private Login viewLogin;
	private Register viewRegister;
	
	private User currentUser;
	private Game currentGame;
	
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
					
					int chosenStarter = JOptionPane.showOptionDialog(
							viewMain.getFrame(),
							"Who starts?",
							"",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE,
							null,     
							options,  
							options[0]
					);
					
					if (chosenStarter != -1) {
						
						String chosenLanguage = viewMain.getCbbCardsSuit().getSelectedIndex() == 0 ? "cards_es" : "cards_fr";
						
						startGame(chosenStarter,chosenLanguage);
					}
					
				}else {
					
					JOptionPane.showMessageDialog(viewMain.getFrame(), "There must be a user logged in");
				}			
				
			}
			
		});
		
		viewMain.getBtnSave().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (currentUser != null) {
					
					try {
						
						String[] splitGameLanguage = currentGame.getGameLanguage().split("_");
						
						Database.insertScore(currentUser.getName(), splitGameLanguage[1], currentGame.getPlayerTotalScore());
						
						JOptionPane.showMessageDialog(viewMain.getFrame(), "The score has been saved");
						
						viewMain.getBtnSave().setEnabled(false);
						
					} catch (Exception e2) {
						
						JOptionPane.showMessageDialog(viewLogin.getFrame(), "Error: Score could not be saved", "", JOptionPane.ERROR_MESSAGE);
					}
					
				}else {
					
					JOptionPane.showMessageDialog(viewMain.getFrame(), "There must be a user logged in");
				}
			}
		});
		
		// hall of fame
		
		viewMain.getBtnLogout().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				// if gameStarted is not true
				
				if (currentUser != null) {
					
					Database.disconnectFromDatabase();
					
					currentUser = null;
					viewMain.getBtnLogin().setEnabled(true);
					viewMain.getBtnLogin().setBackground(new Color(197, 216, 234));
					
					resetGameBoard(false);
					
				}else {
					
					JOptionPane.showMessageDialog(viewMain.getFrame(), "There must be a user logged in");
				}
			}
		});
		
		viewMain.getBtnNewCard().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				newGameTurn(true);
			}
		});
		
		viewMain.getBtnStand().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				viewMain.getBtnNewCard().setEnabled(false);
				
				newGameTurn(false);
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
	

	private void startGame(int chosenStarter, String chosenLanguage) {
		
		resetGameBoard(true);
		
		Boolean playerStarts = chosenStarter == 1 ? true : false;
		
		List<Card> cardList = Database.getCardsFromCollection(chosenLanguage);
		
		Collections.shuffle(cardList);
		
		currentGame = new Game(playerStarts, cardList, chosenLanguage);
		
		if (!playerStarts) newGameTurn(true);
	}
	
	private void resetGameBoard(Boolean resetPlayerButtons) {
		
		viewMain.getBtnCrupier().setIcon(null);
		viewMain.getLblTotalScoreCrupier().setText(null);
		viewMain.getLblScoreHistoryCrupier().setText(null);
		
		viewMain.getBtnPlayer().setIcon(null);
		viewMain.getLblTotalScorePlayer().setText(null);
		viewMain.getLblScoreHistoryPlayer().setText(null);
		
		viewMain.getBtnSave().setEnabled(false);
		
		if (resetPlayerButtons) {
			
			viewMain.getBtnNewCard().setEnabled(true);
			viewMain.getBtnStand().setEnabled(true);
		}
	}

	private void newGameTurn(Boolean takeNewCard) {
		
		Card cardFromList = null;
		Image cardImage = null;
		
		if (takeNewCard) {
			
			cardFromList = currentGame.getNewCard();
			cardImage = getImageFromCard(cardFromList);
		}
		
		if (currentGame.isPlayerTurn()) {
			
			if (takeNewCard) {
				
				Integer cardPoints = cardFromList.getPoints();
				
				if (cardPoints == 1 && currentGame.getGameLanguage().equals("cards_fr")) {
					
					cardPoints = getAcePoints();
				}
				
				currentGame.increasePlayerTotalScore(cardPoints);
				currentGame.increasePlayerScoreHistory(cardPoints);
				
				viewMain.getBtnPlayer().setIcon(new ImageIcon(cardImage));
				viewMain.getLblTotalScorePlayer().setText(currentGame.getPlayerTotalScore().toString());
				viewMain.getLblScoreHistoryPlayer().setText(String.join(" ", currentGame.getPlayerScoreHistory()));
				
			}else {
				
				currentGame.setPlayerStands(true);
				JOptionPane.showMessageDialog(viewMain.getFrame(), "User stands","",JOptionPane.INFORMATION_MESSAGE);
			}
			
		}else {
			
			if (takeNewCard) {
				
				currentGame.makeCrupierPlay(cardFromList);
				
				viewMain.getBtnCrupier().setIcon(new ImageIcon(cardImage));
				viewMain.getLblTotalScoreCrupier().setText(currentGame.getCrupierTotalScore().toString());
				viewMain.getLblScoreHistoryCrupier().setText(String.join(" ", currentGame.getCrupierScoreHistory()));
				
			}else {
				
				JOptionPane.showMessageDialog(viewMain.getFrame(), "Crupier stands","",JOptionPane.INFORMATION_MESSAGE);
			}
		}
		
		
		if (!currentGame.winnerExists()) {
			
			if (currentGame.isPlayerTurn()) {
				
				currentGame.setIsPlayerTurn(false);
				JOptionPane.showMessageDialog(viewMain.getFrame(), "Crupier turn","",JOptionPane.INFORMATION_MESSAGE);
				
				if (currentGame.crupierStands()) {
					
					newGameTurn(false);
					
				}else {
					
					newGameTurn(true);
				}
				
			}else {
				
				currentGame.setIsPlayerTurn(true);
				JOptionPane.showMessageDialog(viewMain.getFrame(), "User turn","",JOptionPane.INFORMATION_MESSAGE);
			}
		
		}else {
			
			endGame();
		}
	}

	private Integer getAcePoints() {
		
		String[] options = {"1","11"};
		
		int chosenStarter = JOptionPane.showOptionDialog(
				viewMain.getFrame(),
				"How many points do you want?",
				"",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,     
				options,  
				options[0]
		);
		
		return chosenStarter == 1 ? 11 : 1;
	}

	private void endGame() {
		
		Game.gameInProgress = false;
		
		viewMain.getBtnSave().setEnabled(true);
		viewMain.getBtnNewCard().setEnabled(false);
		viewMain.getBtnStand().setEnabled(false);
		
		Boolean playerWins = false;
		
		if (currentGame.getPlayerTotalScore() == 21 || currentGame.getCrupierTotalScore() > 21) {
			
			playerWins = true;
		}
		
		if(playerWins) {
			
			JOptionPane.showMessageDialog(viewMain.getFrame(), "Game over. The user wins!","",JOptionPane.INFORMATION_MESSAGE);
			
		}else {
			
			JOptionPane.showMessageDialog(viewMain.getFrame(), "Game over. The crupier wins!","",JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private Image getImageFromCard(Card card) {
		
		Image cardImage = null;
		
		String cardBase64 = Database.getBase64FromCard(card,currentGame.getGameLanguage());
		
//		System.out.println(cardBase64);
		
		byte[] base64ToBytes = Base64.getDecoder().decode(cardBase64);
		
		try {
			
			BufferedImage cardBufferedImage = ImageIO.read(new ByteArrayInputStream(base64ToBytes));
			
			cardImage = cardBufferedImage.getScaledInstance(-1, 400, Image.SCALE_SMOOTH);
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		return cardImage;
	}
}
