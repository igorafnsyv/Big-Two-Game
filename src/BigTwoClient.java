import java.util.*;
import java.net.*;
import java.io.*;


/**
 * This class is used to model a Big Two card game. Implements CardGame interface
 * 
 * @author Igor Afanasyev, 3035392775
 *
 */
public class BigTwoClient implements CardGame, NetworkGame {
	
	private int numOfPlayers;
	private Deck deck;
	private ArrayList<CardGamePlayer> playerList = new ArrayList <CardGamePlayer>();
	private ArrayList<Hand> handsOnTable = new ArrayList <Hand>();
	private int currentIdx;
	private int playerID;
	private String playerName;
	private String serverIP;
	private int serverPort;
	private Socket sock;
	private ObjectOutputStream oos;
	
	
	
	private BigTwoTable bigTwoTable;
	private boolean legalHand;
	
	
	/**
	 * Constructor for creating a Big Two card game
	 * 
	 */
	
	public BigTwoClient() {
		for (int i = 0; i < 4; i++) {
			
			playerList.add(new CardGamePlayer("Player " + i));
		}
		deck = new BigTwoDeck(); 
		deck.shuffle();	
		bigTwoTable = new BigTwoTable(this);
		makeConnection();
	}
	
	
	/**
	 * Method for retrieving the deck of cards being used
	 * 
	 * @return deck
	 * 				a Deck object associated with this Big Two object
	 */
	public Deck getDeck() {
		return deck;
	}
	
	/**
	 * 
	 * Method for retrieving the list of player
	 * @return playerList
	 * 					a playerList object associated with this Big Two object
	 */
	public ArrayList<CardGamePlayer> getPlayerList() {
		return playerList;
	}
	/**
	 * 
	 * Method for retrieving the list of players
	 * 
	 * @return handsOnTable
	 * 					handsOnTable ArrayList associated with this Big Two object
	 */
	public ArrayList<Hand> getHandsOnTable() {
		return handsOnTable;
	}
	
	
	/**
	 * Method for retrieving the index of the current player
	 * 
	 * @return currentIdx
	 * 					currentIdx associated with this Big Two object
	 */
	public int getCurrentIdx() {
		return currentIdx;
	}
	
	
	/**
	 * Method for starting the game with a shuffled deck of cards supplied as the argument.
	 * Implements the Big Two game logic
	 * 
	 * @param deck
	 * 			Big Two deck object associated with this Big Two game
	 */
	public void start (Deck deck) {
		this.deck = deck;
		int diamond3player = 0;
		for (int i = 0; i < 4; i++) {
			getPlayerList().get(i).removeAllCards();
			for (int j = 0; j < 13; j++) {
				int currentIdx = i * 13 + j;
				getPlayerList().get(i).addCard(deck.getCard(currentIdx));
			}
			CardList playerCards = this.getPlayerList().get(i).getCardsInHand();
			if (playerCards.contains(new Card(0,2))) {
				diamond3player = i;
			}	
			playerList.get(i).sortCardsInHand();
			
		}
		
		//bigTwoTable.setActivePlayer(diamond3player);
		currentIdx = diamond3player;
		bigTwoTable.setActivePlayer(playerID);
		
		
	}		

	
	@Override
	/**
	 * a method for making a move by a player with the specified playerID 
	 * using the cards specified by the list of indices.
	 * 
	 * @param playerID
	 * @param cardIdx
	 * 				indexes of selected cards in form of integer array
	 */
	public void makeMove(int playerID, int[] cardIdx) {
		checkMove(playerID, cardIdx);
		if (legalHand) {
			
			//reset legalHand to false so that next hand is verified
			legalHand = false;
			CardList selectedCards = playerList.get(playerID).play(cardIdx);
			Hand selectedHand = composeHand(playerList.get(playerID), selectedCards);
			handsOnTable.add(selectedHand);
			bigTwoTable.printMsg("{" + selectedHand.getType() + "} ");
			bigTwoTable.printMsg(selectedHand.toString() + "\n");
			playerList.get(playerID).removeCards(selectedHand);
			if (!endOfGame()) {
				playerID++;
				if (playerID == 4)
					playerID = 0;
				bigTwoTable.setActivePlayer(playerID);
				bigTwoTable.updateCardsInfo();
				bigTwoTable.repaint();
				}
			//end of game
			else {
				bigTwoTable.printMsg("Game Ends!\n");
				bigTwoTable.printMsg("\n");
				bigTwoTable.disable();
				for (int i = 0; i < 4; i++) {
					int numOfCards = playerList.get(i).getNumOfCards();
					if (numOfCards == 0) {
						bigTwoTable.printMsg(playerList.get(i).getName() + " wins the game\n");
					}
					else if (numOfCards > 1)
						bigTwoTable.printMsg(playerList.get(i).getName() + " has " + numOfCards + " cards\n");
					else
						bigTwoTable.printMsg(playerList.get(i).getName() + " has 1 card\n");
				}
			}
		}
	}
	
	@Override
	/**
	 * a method for checking a move made by a player
	 * @param playerID
	 * @param cardIdx
	 * 				indexes of selected cards in form of integer array
	 */
	public void checkMove(int playerID, int[] cardIdx) {
		bigTwoTable.repaint();
		Hand playerHand = null;
		while (playerHand == null) {
			
			if (handsOnTable.isEmpty() && (cardIdx == null)) {
				bigTwoTable.printMsg("Not a legal move!!! <=== Cannot pass the first move\n");
				break;
			}
			
			if (cardIdx == null && handsOnTable.get(handsOnTable.size() - 1).getPlayer() == playerList.get(playerID)) {
				
				// This player is the last one to make a move
				bigTwoTable.printMsg("Not a legal move!!! <=== Cannot pass\n");
				break;
			}
			if (cardIdx == null) {
				bigTwoTable.printMsg("{Pass}\n");
				if (playerID == 3)
					playerID = -1;
				bigTwoTable.setActivePlayer(playerID + 1);
				bigTwoTable.repaint();
				break;
			}
			if (cardIdx != null)  {	
				CardList playedCards = playerList.get(playerID).play(cardIdx);
				
				if (handsOnTable.isEmpty()) {
					//if it is the First Move
					if (playedCards.contains(new BigTwoCard(0,2))) {
						//if Hand contains Diamond Three
						Hand selectedHand = composeHand(playerList.get(currentIdx), playedCards);
						//if Hand composition failed -> player has not selected proper hand
						if (selectedHand == null) {
							bigTwoTable.resetCardsPosition();
							bigTwoTable.printMsg("Not a legal move!!! <=== Hand cannot be composed\n");
							break;
						}
						else {
							legalHand = true;
						}
						
					} 
					else {
						//does not contain diamond 3
						bigTwoTable.printMsg("Not a legal move!!! <=== Select Diamond 3\n");
						bigTwoTable.resetCardsPosition();
					}
					
				}
				if (!handsOnTable.isEmpty()) {
					Hand selectedHand = composeHand(playerList.get(currentIdx), playedCards);
					if (selectedHand == null) {
						bigTwoTable.printMsg("Not a legal move. <=== Not a legal hand!!!\n");
						break;
					}
					else {
						boolean canPlayAnyCard = false;
						// current player was the last one to play the hand
						if (playerList.get(playerID) == handsOnTable.get(handsOnTable.size() - 1).getPlayer()) {
							canPlayAnyCard = true;
						}
						
						if (selectedHand.beats(handsOnTable.get(handsOnTable.size() - 1)) || canPlayAnyCard) {
							legalHand = true;
							break;
						}
						//selected Hand does not beat the top Hand on Table
						else {
							bigTwoTable.printMsg("{" + selectedHand.getType() + "} ");
							bigTwoTable.printMsg(selectedHand.toString() + "<=== Not a legal move!!!\n");
							
							break;
						}
						
					}
					
				}
				
				break;
			}
			
		}

	}
	
	/**
	 * 
	 * Method for starting the Big Two game.
	 * 
	 * @param args
	 * 				Default parameters supplied through console
	 */
	
	public static void main (String [] args) {
		BigTwoClient game = new BigTwoClient();
		game.start(game.getDeck());
	}
	/**
	 * Method for returning a valid hand from the specified list of cards of the player
	 * @param player
	 * 				CardGamePlayer object associated with this Big Two game
	 * @param cards
	 * 				CardList object which will be assigned to player object associated with this Big Two game
	 * @return hand/ null
	 * 				Returns either Hand object if it can be composed or null if not hand can be composed
	 */
	public static Hand composeHand (CardGamePlayer player, CardList cards) {
		
		Hand hand = null;
		
		if (cards.size() == 1) {
			hand = new Single(player, cards);
			if (hand.isValid()) 
				return hand;
			else 
				return null;
		}
		if (cards.size() == 2) {
			hand = new Pair(player, cards);
			if (hand.isValid())
				return hand;
			else return null;
		}
		
		if (cards.size() == 3) {
			hand = new Triple(player, cards);
			if (hand.isValid())
				return hand;
			else return null;
		}
			
		if (cards.size() == 5) {
			hand = new StraightFlush (player, cards);
			if (hand.isValid()) {
				return hand;
			}
			else {
				hand = new Quad(player, cards);
				if (hand.isValid())
					return hand;
				else {
					hand = new FullHouse(player, cards);
					if (hand.isValid())
						return hand;
					else {
						hand = new Flush(player, cards);
						if (hand.isValid())
							return hand;
						else {
							hand = new Straight (player, cards);
							if (hand.isValid())
								return hand;
						}
					}
				}
			}
		}
			
		
		return null;
	}


	@Override
	/**
	 * a method for getting the number of players.
	 * return numOfPlayers
	 * 						will return 4
	 */
	public int getNumOfPlayers() {
		// TODO Auto-generated method stub
		return numOfPlayers;
	}



	@Override
	/**
	 *  a method for checking if the game ends.
	 *  @return boolean
	 *  
	 */
	public boolean endOfGame() {
		// TODO Auto-generated method stub
		for (int i = 0; i < 4; i++) {
			if (playerList.get(i).getNumOfCards() == 0)
				return true;
		}
		return false;
	}
	
	//NetworkGame interface methods
	public int getPlayerId() {
		return playerID;
	}
	
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}
	
	public String getPlayerName() {
		return playerName;
	}
	
	public String getServerIP() {
		return serverIP;
	}
	
	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}
	
	public int getServerPort() {
		return serverPort;
	}
}
