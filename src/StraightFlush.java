/**
 * Subclass of Hand class, used to model a Straight Flush hand of cards used in Big Two card game
 * @author Igor Afanasyev
 *
 */
public class StraightFlush extends Hand {
	
	
	/**
	 * Public Constructor for constructing a Straight Flush Hand for the specified player and with specified cards
	 * @param player
	 * 				CardGamePlayer object which will receive the hand
	 * @param cards
	 * 				CardList object which contains cards to construct Straight Flush hand
	 */
	
	public StraightFlush (CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	public String getType() {
		return "Straight Flush";
	}
	
	public boolean isValid() {
		if (this.size() != 5)
			return false;
		
		boolean result = true;
		for (int i = 1; i < 5; i++) {
			if (this.getCard(i).getSuit() != this.getCard(i - 1).getSuit()) {
				result = false;
				break;
			}
			if (this.getCard(i).getRank() - this.getCard(i - 1).getRank() != 1) {
				//Below code handles the situation when we get flush such as J,Q,K,A,2 or 10,J,K,Q,A
				if (((i == 3) ||( i== 4)) && (this.getCard(i).getRank() == 0)) //if the card is A and is the 4th or 5th card
					continue;
				result = false;
			}
		}
		
		return result;
	}
	
	public boolean beats(Hand hand) {
		
		if (this.size() != hand.size())
			return false;
		
		if (hand.getType().compareTo("Straight Flush") != 0)
			return true;
		
		if (this.getTopCard().compareTo(hand.getTopCard()) == 1)
			return true;
		
		return false;
		
	}

}
