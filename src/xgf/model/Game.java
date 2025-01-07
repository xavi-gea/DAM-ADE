package xgf.model;

import java.util.List;

public class Game {
	
	private Boolean gameInProgress;

	private Boolean isPlayerTurn;
	
	private Integer playerTotalScore;
	private Integer[] playerScoreHistory;
	
	private Integer crupierTotalScore;
	private Integer[] crupierScoreHistory;
	
	private List<Card> cardList;
	
	public Game(Boolean isPlayerTurn, List<Card> cardList) {
		
		super();
		this.isPlayerTurn = isPlayerTurn;
		this.cardList = cardList;
		
		//if (!isPlayerTurn) makeCrupierPlay();
	}

	private void makeCrupierPlay() {
		
		if (crupierTotalScore >= 17) {

			//plant
			
		}else {
			
			
		}
	}
	
	public Card getNewCard() {
		
		Card newCard = cardList.getFirst();
		cardList.removeFirst();
		
		return newCard;
	}

	public Boolean getGameInProgress() {
		return gameInProgress;
	}
}
