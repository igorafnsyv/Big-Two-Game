/**
 * Subclass of Hand class, used to model a Quad hand of cards used in Big 
 * 
 * 
 * @author Igor Afanasyev
 *
 */
public class Quad extends Hand {
	/**
	 * Public Constructor for constructing a Quad Hand for the specified player and with specified cards
	 * @param player
	 * 				CardGamePlayer object which will receive the hand
	 * @param cards
	 * 				CardList object which contains cards to construct Quad hand
	 */
	
	public Quad(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	public String getType() {
		return "Quad";
	}
	
	public boolean isValid() {
		if (this.size() != 5) {
			return false;
		}
		int counter = 0;
		boolean result = true;
		int ranks [] = new int [13];
		for (int i = 0; i < 5; i++) {
			ranks[this.getCard(i).getRank()]++;		//count how many times each rank appeared
		}
		for (int i = 0; i < ranks.length; i++) {
			if (ranks[i] == 1)
				counter++;
			if ((ranks[i] == 2) || (ranks[i] == 3)) {	//ranks can appear 0, 1 or 4 times
				result = false;
				break;
			}
			if (counter > 1)
				result = false;
		}
		return result;
	}
	
	public Card getTopCard() {
		int ranks [] = new int [13];
		int quadRank = -1;
		this.sort();
		Card topCard = this.getCard(4);
		for (int i = 0; i < 5; i++) {
			ranks[this.getCard(i).getRank()]++;
			if (ranks[this.getCard(i).getRank()] == 3) {
				quadRank = this.getCard(i).getRank();		//which rank appears forms a Quad
			}
		}
		
		for (int i = 4; i >= 0; i--) {
			if (this.getCard(i).getRank() == quadRank) {		//get the top card with Quad rank
				topCard = this.getCard(i);
				break;
			}
		}
		return topCard;
	}
	
	public boolean beats(Hand hand) {
		if (this.size() != hand.size()) {
			return false;
		}
		//check whether the hand is neither Flush, Straight, Full House of Quad
		if ((hand.getType().compareTo("Flush") != 0) && (hand.getType().compareTo("Straight") != 0) && (hand.getType().compareTo("Full House") !=0) && (hand.getType().compareTo("Quad") !=0))
			return false;
		if (hand.getType().compareTo("Flush") == 0)
			return true;
		if (hand.getType().compareTo("Straight") == 0)
			return true;
		if (hand.getType().compareTo("Full House") == 0)
			return true;
		
		if (this.getTopCard().compareTo(hand.getTopCard()) == 1)
			return true;
		return false;
		
	}

}
