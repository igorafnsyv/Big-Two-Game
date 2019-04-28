

/**
 * This class is a subclass of Deck class. Used to model a deck of cards used in Big Two game
 * 
 * 
 * @author Igor Afanasyev
 *
 */
public class BigTwoDeck extends Deck { //write tester class
	
	/**
	 * Method for initializing a deck of Big Two cards
	 */
	public void initialize() {
		removeAllCards();
		for (int i = 0; i < 4; i++) {
			for (int j = 2; j < 15; j++) {
				if (j == 13) {
					BigTwoCard card = new BigTwoCard(i, 0);	//Adding A of any suit
					addCard(card);
				}
				else if (j == 14) {
					BigTwoCard card = new BigTwoCard(i, 1); //Adding 2 of any suit at top
					addCard(card);
				} 
				else {
					BigTwoCard card = new BigTwoCard(i,j); //Add remaining cards at index [2,12]
					addCard(card);
				}
				
			} //end of for loop
		}
	}

}
