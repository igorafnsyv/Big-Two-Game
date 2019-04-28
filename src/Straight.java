/**
 * Subclass of Hand class, used to model a Straight hand of cards used in Big Two card game
 * @author Igor Afanasyev
 *
 */
public class Straight extends Hand {
	
	/**
	 * Public Constructor for constructing a Straight Hand for the specified player and with specified cards
	 * @param player
	 * 				CardGamePlayer object which will receive the hand
	 * @param cards
	 * 				CardList object which contains cards to construct Straight hand
	 */
	
	public Straight(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	
	
	public boolean beats(Hand hand) {
		if (this.size() != hand.size()) {
			return false;
		} else {
			if (hand.getType().compareTo("Straight") != 0) {	//in case the 5 cards in another hand are not Straight
				return false;									//then this hand cannot beat these cards
			}
			Card topCard = hand.getTopCard();
			if (this.getTopCard().compareTo(topCard) == 1) {
				return true;
			} else {
				return false;
			}
			
		}
	}
	
	
	public boolean isValid() {
		if (this.size() == 5) {
			boolean valid = true;
			this.sort();
			for (int i = 1; i < 5; i++) {
				if (this.getCard(i).getRank() - this.getCard(i - 1).getRank() != 1) {
					//Below code handles the situation when we get flush such as J,Q,K,A,2 or 10,J,K,Q,A
					if (((i == 3) ||( i== 4)) && (this.getCard(i).getRank() == 0)) //if the card is A and is the 4th or 5th card
						continue;
					valid = false;
				}
			}
			return valid;
		}
		return false;
	}
	
	public String getType() {
		return "Straight";
	}

}
