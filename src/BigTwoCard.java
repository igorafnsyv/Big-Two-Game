

/**
 * This class is a subclass of Card class. Used to model a card used in a Big Two card game
 * 
 * @author Igor Afanasyev
 *
 */
public class BigTwoCard extends Card { //write tester class
	
	
	/**
	 * Constructor for building a card object with specified suit and rank
	 * @param suit
	 * 				suit is an integer between 0 and 3
	 * @param rank
	 * 				rank is an integer between 0 and 12
	 */
	public BigTwoCard(int suit, int rank) {
		
		super(suit,rank);	//calling constructor of the superclass
		
	}
	//ranks: 0 = 'A', 1 = '2', 2 = '3', ..., 8 = '9', 9 = '0', 10 = 'J', 11 =
	//		 *         'Q', 12 = 'K'
	
	/**
	 * Method for comparing the order of this card with the specified card
	 * 
	 * @param card
	 * 				card object which will be compared to this card
	 * 
	 * @return value
	 * 				returns negative integer, zero or positive integer
	 */
	public int compareTo(Card card) {
		if ((this.rank == 1) && (card.getRank() != 1)) { //first card is 2 other one is not
			return 1;
		}
		if ((this.rank == 0) && (card.getRank() > 1)) { //first card is A, other one is lower
			return 1;
		}
		
		if ((this.rank != 1) && (card.getRank() == 1)) { //first card is not 2, but the second one is 2
			return -1;
		}
		
		if ((this.rank > 1) && (card.getRank() == 0)) { //first card is either 3 or higher (but not 2 or A), second one is A
			return - 1;
		}
		
		
		//these should handle case if both cards are 2 or A, including the other
		if (this.rank > card.getRank()) {
			return 1;
		} else if (this.rank < card.getRank()) {
			return -1;
		} else if (this.suit > card.getSuit()) {
			return 1;
		} else if (this.suit < card.getSuit()) {
			return -1;
		} else {
			return 0;
		}
	}
	

}
