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

/**
 * @author Xavi
 */
public class Controller {

	private MainPanel viewMain;
	private Login viewLogin;
	private Register viewRegister;
	private HallOfFame viewhallOfFame;
	
	private User currentUser;
	private Game currentGame;
	
	private static final File[] foldersToSearch = {
			new File("." + File.separator + "img" + File.separator + "cards_es"),
			new File("." + File.separator + "img" + File.separator + "cards_fr")
	};
	
	public Controller() {
		
		new Config();
		Database.setConnectionString();
		
		viewMain = new MainPanel();
		initMainEventHandler();
	}
	
	private void initMainEventHandler() {
		
		viewMain.getBtnLoadCards().addActionListener(new ActionListener() {
			
			/**
			 * When the specified action is performed, try to load the cards that will be used for the game
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				
				List<Card> cardList = new ArrayList<Card>();
				
				for(File folder : foldersToSearch) {
					
					cardList = Card.getCardsfromFolder(folder);
					
					Database.insertCardsToCollection(cardList, folder.getName());
				}
				
				JOptionPane.showMessageDialog(viewMain.getFrame(), "Cards have been loaded", "Info", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		viewMain.getBtnLogin().addActionListener(new ActionListener() {
			
			/**
			 * When the specified action is performed, try to bring up the user login view
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				
				viewLogin = new Login();
				viewLogin.getFrame().setLocationRelativeTo(viewMain.getFrame());
				initLoginEventHandlers();
			}
		});
		
		viewMain.getBtnRegister().addActionListener(new ActionListener() {
			
			/**
			 * When the specified action is performed, try to bring up the register user view
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				
				viewRegister = new Register();
				viewRegister.getFrame().setLocationRelativeTo(viewMain.getFrame());
				initRegisterEventHandlers();
			}
		});
		
		
		viewMain.getBtnStart().addActionListener(new ActionListener() {
			
			/**
			 * When the specified action is performed, try to start the game
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				
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
					
					JOptionPane.showMessageDialog(viewMain.getFrame(), "There must be a user logged in", "Info", JOptionPane.INFORMATION_MESSAGE);
				}			
				
			}
			
		});
		
		viewMain.getBtnSave().addActionListener(new ActionListener() {
			
			/**
			 * When the specified action is performed, try to save the current game
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (currentUser != null) {
					
					try {
						
						String[] splitGameLanguage = currentGame.getGameLanguage().split("_");
						
						Database.insertScore(
								currentUser.getName(), 
								splitGameLanguage[1], 
								currentGame.getPlayerTotalScore(), 
								Config.getCollections().getString("scores")
						);
						
						JOptionPane.showMessageDialog(viewMain.getFrame(), "The score has been saved", "Info", JOptionPane.INFORMATION_MESSAGE);
						
						viewMain.getBtnSave().setEnabled(false);
						
					} catch (Exception e2) {
						
						JOptionPane.showMessageDialog(viewLogin.getFrame(), "Error: Score could not be saved", "Error", JOptionPane.ERROR_MESSAGE);
					}
					
				}else {
					
					JOptionPane.showMessageDialog(viewMain.getFrame(), "There must be a user logged in", "Info", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		
		viewMain.getBtnHallOfFame().addActionListener(new ActionListener() {
			
			/**
			 * When the specified action is performed, try to bring up the hall of fame view
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (currentUser != null) {
					
					List<String> scoreList = Database.getScoresFromCollection(Config.getCollections().getString("scores"));
					
					viewhallOfFame = new HallOfFame();
					viewhallOfFame.getFrame().setLocationRelativeTo(viewMain.getFrame());
					
					viewhallOfFame.getListScores().setListData(scoreList.toArray(new String[0]));
					
				}else {
					
					JOptionPane.showMessageDialog(viewMain.getFrame(), "There must be a user logged in", "Info", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		
		viewMain.getBtnLogout().addActionListener(new ActionListener() {
			
			/**
			 * When the specified action is performed, try to log out
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (currentUser != null) {
					
					Database.disconnectFromDatabase();
					
					currentUser = null;
					viewMain.getBtnLogin().setEnabled(true);
					viewMain.getBtnLogin().setBackground(new Color(197, 216, 234));
					
					resetGameBoard(false);
					
				}else {
					
					JOptionPane.showMessageDialog(viewMain.getFrame(), "There must be a user logged in", "Info", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		
		viewMain.getBtnNewCard().addActionListener(new ActionListener() {
			
			/**
			 * When the specified action is performed, try to go to the next turn by getting a new card
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				
				newGameTurn(true);
			}
		});
		
		viewMain.getBtnStand().addActionListener(new ActionListener() {
			
			/**
			 * When the specified action is performed, try to go to the next turn by making the player stand
			 */
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
			
			
			/**
			 * If the user exists, perform the log in
			 * @param userName Name of the user
			 * @param userPassword Password of the user
			 * @throws NoSuchAlgorithmException When the user does not exist
			 */
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
			
			/**
			 * When the specified action is performed, try to register the user
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				
				try {
					
					String userName = viewRegister.getTextUser().getText();
					char[] userPassword = viewRegister.getPasswordUser().getPassword();
					char[] userPasswordRepeated = viewRegister.getPasswordUserRepeat().getPassword();
					
					if (!userName.isEmpty() && (userPassword.length > 0) && (userPasswordRepeated.length > 0)) {
						
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
					
					JOptionPane.showMessageDialog(viewRegister.getFrame(), "Error: " + e2.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
					
				}catch (Exception e1) {
					
					JOptionPane.showMessageDialog(viewRegister.getFrame(), "Error: The user could not be created: " + e1.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
				}			
			}
		});
	}
	

	/**
	 * With the provided starter (Crupier or User) and language, reset the game board and try to start a new game
	 * by obtaining a new list of cards and shuffling them 
	 * @param chosenStarter Starter of the game (Crupier or User)
	 * @param chosenLanguage Language of the cards to start the game with
	 */
	private void startGame(int chosenStarter, String chosenLanguage) {
		
		resetGameBoard(true);
		
		Boolean playerStarts = chosenStarter == 1 ? true : false;
		
		List<Card> cardList = Database.getCardsFromCollection(chosenLanguage);
		
		Collections.shuffle(cardList);
		
		currentGame = new Game(playerStarts, cardList, chosenLanguage);
		
		if (!playerStarts) newGameTurn(true);
	}
	
	
	/**
	 * Reset the relevant buttons, labels and images
	 * @param resetPlayerButtons If the buttons related to the player actions (New card and Stand) should be reset
	 */
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

	/**
	 * Main loop of the game
	 * Start a new turn and, if takeNewCard is true, draw a new card
	 * Here is also where it is decided if it's the turn of the User or Crupier and if the game must finish
	 * @param takeNewCard If a new card must be draw
	 */
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
				JOptionPane.showMessageDialog(viewMain.getFrame(), "User stands", "Info", JOptionPane.INFORMATION_MESSAGE);
			}
			
		}else {
			
			if (takeNewCard) {
				
				currentGame.makeCrupierPlay(cardFromList);
				
				viewMain.getBtnCrupier().setIcon(new ImageIcon(cardImage));
				viewMain.getLblTotalScoreCrupier().setText(currentGame.getCrupierTotalScore().toString());
				viewMain.getLblScoreHistoryCrupier().setText(String.join(" ", currentGame.getCrupierScoreHistory()));
				
			}else {
				
				JOptionPane.showMessageDialog(viewMain.getFrame(), "Crupier stands", "Info", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		
		
		if (!currentGame.winnerExists()) {
			
			if (currentGame.isPlayerTurn()) {
				
				currentGame.setIsPlayerTurn(false);
				JOptionPane.showMessageDialog(viewMain.getFrame(), "Crupier turn", "Info", JOptionPane.INFORMATION_MESSAGE);
				
				if (currentGame.crupierStands()) {
					
					newGameTurn(false);
					
				}else {
					
					newGameTurn(true);
				}
				
			}else {
				
				currentGame.setIsPlayerTurn(true);
				JOptionPane.showMessageDialog(viewMain.getFrame(), "User turn", "Info", JOptionPane.INFORMATION_MESSAGE);
			}
		
		}else {
			
			endGame();
		}
	}

	/**
	 * If an Ace card has been draw, allow the user to choose how many points he receives
	 * @return Integer with the points chosen
	 */
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

	/**
	 * Set the state of relevant buttons and decide who wins 
	 */
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
			
			JOptionPane.showMessageDialog(viewMain.getFrame(), "Game over. The user wins!", "Info", JOptionPane.INFORMATION_MESSAGE);
			
		}else {
			
			JOptionPane.showMessageDialog(viewMain.getFrame(), "Game over. The crupier wins!", "Info", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * From the provided card, return the associated image 
	 * @param card Card to obtain it's image
	 * @return Image The image of the card
	 */
	private Image getImageFromCard(Card card) {
		
		Image cardImage = null;
		
		String cardBase64 = Database.getBase64FromCard(card,currentGame.getGameLanguage());
		
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
