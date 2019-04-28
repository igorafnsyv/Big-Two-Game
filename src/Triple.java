/**
 * Subclass of Hand class, used to model a Triple hand of cards used in Big Two card game
 * @author Igor Afanasyev
 *
 */
public class Triple extends Hand {
	
	/**
	 * Public Constructor for constructing a Triple Hand for the specified player and with specified cards
	 * @param player
	 * 				CardGamePlayer object which will receive the hand
	 * @param cards
	 * 				CardList object which contains cards to construct Triple hand
	 */
	
	public Triple(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	public boolean isValid() {
		if (this.size() == 3) {
			Card card1 = this.getCard(0);
			Card card2 = this.getCard(1);
			Card card3 = this.getCard(2);
			if ((card1.getRank() == card2.getRank()) && (card2.getRank() == card3.getRank())) {
				return true;
			}
			
		}
		return false;
	}
	
	public String getType() {
		return "Triple";
	}

}
