
/**
 * This class is a subclass of Hand and is used to model a hand of FLush in Big Two card game
 * 
 * @author Igor Afanasyev
 *
 */

public class Flush extends Hand {
	
	
	/**
	 * Public constructor for building a Flush hand with the specified player and list
	 * @param player
	 * 				CardGamePlayer object who the Flush hand will be assigned to
	 * @param cards
	 * 				CardList object which will be assigned to the player
	 */
	public Flush(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * Method for checking if this hand beats a specified hand
	 * @param hand
	 * 			Hand object which will be compared with this Flush hand
	 * @return boolean
	 *			true or false 
	 */
	public boolean beats(Hand hand) {
		if (this.size() != hand.size()) {
			return false;
		} 
			if ((hand.getType().compareTo("Flush") != 0) && (hand.getType().compareTo("Straight") != 0)) {
				return false;			//in case the Hand is neither Flush or Straight
			}
			if (hand.getType().compareTo("Straight") == 0) {
				return true;			//Flush always beats any Straight
			}
			Card topCard = hand.getTopCard();
			if (this.getTopCard().getSuit() > hand.getTopCard().getSuit()) {
				return true;			//Flush with higher suit beats Flush with lower one
			}
			
			if (this.getTopCard().getSuit() < hand.getTopCard().getSuit()) {
				return false;			//Flush with lower suit looses to the Flush with higher one
			}
			if (this.getTopCard().compareTo(topCard) == 1) {
				return true;
			} else {
				return false;
			}
			
	}
	
	/**
	 * Method for returning a string specifying the type of this hand
	 * 
	 * @return string
	 * 				Type of the card
	 */
	
	public String getType() {
		return "Flush";
	}
	
	/**
	 * Method for checking whether this hand is a valid Flush hand
	 * 
	 * @return boolean
	 * 				true or false depending on the result
	 */
	
	public boolean isValid() {
		if (this.size() != 5) {
			return false;
		}
		for (int i = 1; i < 5; i++) {
			if (this.getCard(i).getSuit() != this.getCard(i - 1).getSuit()) {
				return false;
			}
		}
		return true;
	}

}
