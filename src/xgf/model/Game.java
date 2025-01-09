package xgf.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Xavi
 */
public class Game {
	
	public static Boolean gameInProgress = false;
	private String gameLanguage;

	private Boolean isPlayerTurn;
	
	private Boolean playerStands = false;
	private Boolean crupierStands = false;
	
	private Integer playerTotalScore = 0;
	private List<String> playerScoreHistory = new ArrayList<String>();
	
	private Integer crupierTotalScore = 0;
	private List<String> crupierScoreHistory = new ArrayList<String>();
	
	private List<Card> cardList;
	
	public Game(Boolean isPlayerTurn, List<Card> cardList, String gameLanguage) {
		
		super();
		this.isPlayerTurn = isPlayerTurn;
		this.cardList = cardList;
		this.gameLanguage = gameLanguage;
		Game.gameInProgress = true;
	}

	/**
	 * This method represents the AI of the Crupier, with multiple conditions that determines if he should keep playing
	 * by increasing it's total score or if he stands
	 * @param card
	 */
	public void makeCrupierPlay(Card card) {
		
		if (crupierTotalScore <= 16) {
			
			Integer cardPoints = card.getPoints();
			
			if (cardPoints == 1 && gameLanguage.equals("cards_fr")) {
				
				if ((this.crupierTotalScore + 11) > 21) {
					
					this.crupierTotalScore += cardPoints;
					this.crupierScoreHistory.add(cardPoints.toString());
					
				}else {
					
					this.crupierTotalScore += 11;
					this.crupierScoreHistory.add("11");
				}
				
			}else {
				
				this.crupierTotalScore += cardPoints;
				this.crupierScoreHistory.add(cardPoints.toString());
			}
			
			if (crupierTotalScore >= 17) {
				
				this.crupierStands = true;
			}
		}
	}
	
	/**
	 * From the current list of available cards, return a new one and remove it to avoid repetition 
	 * @return A new card
	 */
	public Card getNewCard() {
		
		Card newCard = cardList.getFirst();
		cardList.removeFirst();
		
		return newCard;
	}
	
	/**
	 * Check if during the current game there is a winner 
	 * @return True or False depending of the result
	 */
	public Boolean winnerExists() {
		
		if ((this.playerTotalScore >= 21 || this.crupierTotalScore >= 21) || (this.playerStands && this.crupierStands)) {
			
			return true;
			
		}else {
			
			return false;
		}
	}

	public Boolean isGameInProgress() {
		return gameInProgress;
	}

	public String getGameLanguage() {
		return gameLanguage;
	}

	public Boolean isPlayerTurn() {
		return isPlayerTurn;
	}

	public Integer getPlayerTotalScore() {
		return playerTotalScore;
	}

	public void increasePlayerTotalScore(Integer score) {
		this.playerTotalScore += score;
	}
	
	public List<String> getPlayerScoreHistory() {
		return playerScoreHistory;
	}

	public void increasePlayerScoreHistory(Integer score) {
		this.playerScoreHistory.add(score.toString());
	}
	
	public Integer getCrupierTotalScore() {
		return crupierTotalScore;
	}

	public List<String> getCrupierScoreHistory() {
		return crupierScoreHistory;
	}

	public void setIsPlayerTurn(Boolean isPlayerTurn) {
		this.isPlayerTurn = isPlayerTurn;
	}

	public Boolean crupierStands() {
		return crupierStands;
	}

	public void setPlayerStands(Boolean playerStands) {
		this.playerStands = playerStands;
	}
}
