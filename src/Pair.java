
/**
 * This class is a sublass of Hand class, used to model the Pair hand of cards used in 
 * Big Two card game
 * 
 * @author Igor Afanasyev
 *
 */
public class Pair extends Hand {
	/**
	 * Public Constructor for constructing a Pair Hand for the specified player and with specified cards
	 * @param player
	 * 				CardGamePlayer object which will receive the hand
	 * @param cards
	 * 				CardList object which contains cards to construct Pair
	 */
	
	public Pair(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
		
	public boolean isValid() {
		if (this.size() == 2) {
			Card card1 = this.getCard(0);
			Card card2 = this.getCard(1);
			if (card1.getRank() == card2.getRank()) {
				return true;
			}
		} 
		return false;
		
	}
	
	public String getType() {
		return "Pair";
	}

}
