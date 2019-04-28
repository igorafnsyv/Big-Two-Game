
/**
 * Subclass of a CardList, used to model a hand of cards
 * 
 * @author Igor Afanasyev, 3035392775
 * 
 *
 */
 public abstract class Hand extends CardList {
	
	private CardGamePlayer player;
	
	/**
	 * Constructor for building a hand with specified player and list of cards
	 * @param player
	 * 				CardGamePlayer object associated with this hand
	 * @param cards
	 * 				CardList object associated with this hand
	 */
	public Hand(CardGamePlayer player, CardList cards) {
		this.player = player;
		for (int i = 0; i < cards.size(); i++) {
			this.addCard(cards.getCard(i));
		}
		
		
		
	}
	
	/**
	 * Method for retrieving the player of this Hand
	 * 
	 * @return player
	 * 				CardGamePlayer object associated with this hand
 	 */
	
	public CardGamePlayer getPlayer() {
		return player;
	}
	
	/**
	 * Method for retrieving the top card of this hand
	 * @return topCard
	 * 					Card object from the sorted list of cards
	 */
	public Card getTopCard() { 
		this.sort();
		return this.getCard(this.size() - 1);
	}
	
	/**
	 * Method for checking whether this hand beats another one
	 * @param hand
	 * 				Hand object which will be compared with this hand
	 * @return boolean
	 * 				true or false depending on the comparison result
	 * 		
	 */
	public boolean beats(Hand hand) {
		if (this.size() != hand.size()) {		//if the sizes are not equal
			return false;
		} else {
			Card topCard = hand.getTopCard();
			if (this.getTopCard().compareTo(topCard) == 1) {		//if the top card of this hand is bigger
				return true;
			} else {
				return false;
			}
			
		}
	}
	/**
	 * Method for verifying whether the hand is valid or not
	 * @return boolean
	 */
	abstract boolean isValid();
	
	/**
	 * method for retrieving the type of the Hand in string format
	 * @return type
	 */
	abstract String getType();

}
