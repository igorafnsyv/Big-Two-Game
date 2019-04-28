/**
 * 
 * Subclass of Hand class, used to model a Sing hand of cards used in Big Two card game
 * @author Igor Afanasyev
 *
 */
public class Single extends Hand {
	
	/**
	 * Public Constructor for constructing a Single Hand for the specified player and with specified cards
	 * @param player
	 * 				CardGamePlayer object which will receive the hand
	 * @param cards
	 * 				CardList object which contains cards to construct Single hand
	 */
	
	public Single(CardGamePlayer player, CardList cards) {
		super(player, cards);	
	}
	
	
	public boolean isValid() {
		if (this.size() != 1)	
			return false;
		else return true;
		
	}
	
	public String getType() {
		return "Single";
	}

}
