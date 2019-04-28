

/**
 * This class is a subclass of the Hand class, and is used to model a hand of Full House
 * 
 * @author Igor Afanasyev
 *
 */
public class FullHouse extends Hand {
	
	
	/**
	 * Public constructor for creating a Full House hand with the specified player and cards
	 * @param player
	 * 				CardGamePlayer object to which a hand will be assigned
	 * @param cards
	 * 				CardList object which will be moved to hand
	 */
	public FullHouse(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	
	/**
	 * Method for retrieving a top card of this hand
	 * 
	 * @return topCard
	 * 					Impossible to believe but returns a top card of this hand. Amazing, right?
	 */		
	public Card getTopCard() {
		int cards [] = new int [13];
		int tripletRank = -1;			//default value
		this.sort();
		Card topCard = this.getCard(4);			//default value
		for (int i = 0; i < 5; i++) {
			cards[this.getCard(i).getRank()]++;
			if (cards[this.getCard(i).getRank()] == 3) {
				tripletRank = this.getCard(i).getRank();
			}
		}
		
		for (int i = 4; i >= 0; i--) {
			if (this.getCard(i).getRank() == tripletRank) {
				topCard = this.getCard(i);
				break;
			}
		}
		return topCard;
		
	}
	/**
	 * Method for checking whether this hand beats another one
	 * @param hand
	 * 				Hand object which will be compared with this hand
	 * 
	 * @return boolean
	 * 				true or false depending on the result
	 */
	public boolean beats(Hand hand) {
		if (this.size() != hand.size()) {
			return false;
		}
		//check whether the card is neither Flush, Straight or Full House
		if ((hand.getType().compareTo("Flush") != 0) && (hand.getType().compareTo("Straight") != 0) && (hand.getType().compareTo("Full House") !=0))
			return false;
		if (hand.getType().compareTo("Flush") == 0)
			return true;
		if (hand.getType().compareTo("Straight") == 0)
			return true;
		
		if (this.getTopCard().compareTo(hand.getTopCard()) == 1)
			return true;
		return false;
		
	}
	/**
	 * Method for checking whether this hand is a valid Full House hand
	 * @return result
	 * 				true or false
	 */
	public boolean isValid() {
		if (this.size() != 5)
			return false;
		int cards [] = new int [13];
		boolean result = true;
		for (int i = 0; i < 5; i++) {
			cards[this.getCard(i).getRank()]++;			//count how many times each rank appeared
		}
		for (int i = 0; i < cards.length; i++) {
			if ((cards[i] == 1) || (cards[i] == 4)) {
				result = false;
				break;
			}
		}
		return result;
		
	}
	
	/**
	 * Method for retrieving the type of Hand in the string format
	 * 
	 * @return string
	 * 				String containing hand's type
	 * 
	 */
	public String getType() {
		return "Full House";
	}

}
