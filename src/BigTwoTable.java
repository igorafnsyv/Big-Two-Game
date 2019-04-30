import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * 
 * It is used to build a GUI for the Big Two card game and handle all user actions.
 * 
 * @author Igor Afanasyev, 3035392775
 *
 */

public class BigTwoTable implements CardGameTable {
	
	private CardGame game;
	private boolean [] selected;
	private int activePlayer;
	private JFrame frame;
	private JPanel bigTwoPanel;
	private JMenu menu = new JMenu("Game");
	private JButton playButton = new JButton("Play");
	private JButton passButton = new JButton("Pass");
	private JButton sendButton = new JButton("Send");
	private JTextArea msgArea;
	private JTextArea incomingMsg;
	private JTextField outgoingMsg;
	private Image[][] cardImages = new Image[4][13];
	private Image cardBackImage = new ImageIcon("images/" + "b" + ".gif").getImage();
	private Image[] avatars = new Image[4];
	private int[] imagesX = new int[52];
	private int[] imagesY = new int[52];
		
	
	/**
	 * Public constructor for BigTwoTable class
	 * @param game
	 * 				The parameter game is a reference to a card game associates with this table.
	 */
	public BigTwoTable(CardGame game) {
		
		this.game = game;
		selected = new boolean [52];
		//avatars are added at the stage of component painting
		//updateCardsInfo();	//sets images accordingly
		String frameName = "";
		for (int i = 0; i < 4; i++) {
			if (game.getPlayerList().get(i).getName() != "") {
				frameName = game.getPlayerList().get(i).getName();
			}
		}
		frame = new JFrame(frameName);
		msgArea = new JTextArea(21, 35); //rows, columns
		
		msgArea.setLineWrap(true);
		
		msgArea.setLineWrap(true);
		msgArea.setEditable(false);
		JScrollPane scroller = new JScrollPane(msgArea);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		incomingMsg = new JTextArea(21,35);
		incomingMsg.setLineWrap(true);
		incomingMsg.setEditable(false);
		JScrollPane qScroller = new JScrollPane(incomingMsg);
		qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		JPanel textPanel = new JPanel();
		BoxLayout boxlayout = new BoxLayout(textPanel, BoxLayout.Y_AXIS);
		textPanel.setLayout(boxlayout);
		outgoingMsg = new JTextField(20);
		textPanel.add(scroller);
		textPanel.add(qScroller);
		textPanel.add(outgoingMsg);
		

		
		playButton.addActionListener(new PlayButtonListener());
		passButton.addActionListener(new PassButtonListener());
		sendButton.addActionListener(new SendButtonListener());
		bigTwoPanel = new BigTwoPanel();	
		
		//menu implementation
		JMenuBar menuBar = new JMenuBar();
		JMenuItem restartItem = new JMenuItem("Restart");
		restartItem.addActionListener(new RestartMenuItemListener());
		JMenuItem quitItem = new JMenuItem("Quit");
		quitItem.addActionListener(new QuitMenuItemListener());
		menu.add(restartItem);
		menu.add(quitItem);
		menuBar.add(menu);
		
		frame.add(menuBar, BorderLayout.NORTH);
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(playButton);//, BorderLayout.WEST);
		buttonPanel.add(passButton);//, BorderLayout.WEST);
		buttonPanel.add(sendButton);	//button to send messages
		frame.add(buttonPanel, BorderLayout.SOUTH);
		frame.add(bigTwoPanel, BorderLayout.CENTER);
		frame.add(textPanel, BorderLayout.EAST);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		

	}
	
	/**
	 * Additional method to update cardImages array after player's turn
	 * 
	 */
	public void updateCardsInfo() {

		for (int i = 0; i < 4; i++) {
			
			game.getPlayerList().get(i).getCardsInHand().sort();
			int numOfCards = game.getPlayerList().get(i).getNumOfCards();
			for (int j = 0; j < numOfCards; j++) {
				
				Card card = this.game.getPlayerList().get(i).getCardsInHand().getCard(j);
				int rank = card.getRank();
				int suit = card.getSuit();
				
				if (rank == 0) {
					
					rank = 11;
					
				}
				else if (rank == 1) {
					
					rank = 12;
					
				}
				else {
					
					rank -= 2;
					
				}
				
				int cardIndex = rank + (suit * 13);
				cardImages[i][j] = new ImageIcon("images/" + (cardIndex) + ".gif").getImage();
			}
		}
	}

	/**
	 * a method for setting the index of the active player (i.e., the current player).
	 */
	public void setActivePlayer(int activePlayer) {
		
		
		if (activePlayer < 0 || activePlayer >= game.getPlayerList().size()) {
			
			this.activePlayer = -1;
			
		} else {
			
			this.activePlayer = activePlayer;
		}
		this.printMsg("Player " + activePlayer + "'s turn:\n");
	}
	
	/**
	 * a method for getting an array of indices of the cards selected.
	 */
	public int [] getSelected() {
		
		int [] selected_indexes = new int [13];
		for (int i = 0; i < 13; i++)
			selected_indexes[i] = -1;
		int j = -1;
		int counter = 0;
		
		for (int i = activePlayer * 13; i < activePlayer * 13 + game.getPlayerList().get(activePlayer).getNumOfCards(); i++) {
		
			if (selected[i]) {
				j++;
				selected_indexes[j] = counter;
			}	
			counter++;
		}
		if (j == -1)
			return null;
		return selected_indexes;
	}
	/**
	 * a method for resetting the list of selected cards.
	 * Unbelievable, right? Still have to write Javadoc for it. Gosh...
	 */
	public void resetSelected() {
		selected = new boolean[52];	
	}
	
	/**
	 * a method for repainting the GUI.
	 */
	public void repaint() {
		resetCardsPosition();
		frame.repaint();
	}
	
	/**
	 * a method for printing the specified string to the message area of the GUI.
	 */
	public void printMsg(String msg) {
		msgArea.append(msg);
	}
	
	/**
	 * 
	 * a method for clearing the message area of the GUI
	 * 
	 */
	public void clearMsgArea() {
		msgArea.setText(null);
		repaint();
	}
		
	
	/**
	 * a method for resetting the GUI
	 */
	public void reset() {
		resetSelected();
		clearMsgArea();
		enable();
	}
	
	/**
	 *  a method for enabling user interactions with the GUI
	 */
	public void enable() {
		bigTwoPanel.setEnabled(true);
	}
	
	/**
	 * 
	 * a method for disabling user interactions with the GUI
	 */
	public void disable() {
		bigTwoPanel.setEnabled(false);
	}
	
	/**
	 * Will move down the selected cards. Useful when player selects cards
	 * and then decided to pass
	 */
	public void resetCardsPosition() {
		int x = 150;
		int y = 50;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 13; j++) {
				x += 20;
				imagesX[(i * 13) + j] = x;	//save x coordinate of image
				imagesY[(i * 13) + j] = y;	//save y coordinate of image
			}
			x = 150;
			y += 120;
		}
		resetSelected();
	}
	
	public void printTextMsg(String msg) {
		incomingMsg.append(msg + "\n");
	}
	

	class BigTwoPanel extends JPanel implements MouseListener {
		
		int imageWidth = 73;
		int imageHeight = 97;
	

		public BigTwoPanel() {
			addMouseListener(this);
			resetCardsPosition();	
		}
		

		public void mouseClicked(MouseEvent e) {
			
			if (bigTwoPanel.isEnabled()) {	//verifies whether GUI is enabled
				int mouseX = e.getX();
				int mouseY = e.getY();
				ArrayList<Integer> indexes_pressed = new ArrayList<Integer>();
				
				
				for (int i = (activePlayer * 13); i < activePlayer * 13 + game.getPlayerList().get(activePlayer).getNumOfCards(); i++) {
				
					if (mouseX >= imagesX[i] && mouseX <= imagesX[i] + imageWidth) {
						if (mouseY >= imagesY[i] && mouseY <= imagesY[i] + imageHeight) {
							indexes_pressed.add(i);
						}
					}
				}
				int selectedCardIdx = indexes_pressed.get(indexes_pressed.size() - 1);
				if (!selected[selectedCardIdx]) {
					imagesY[selectedCardIdx] -= 30;
				} else {
					imagesY[selectedCardIdx] += 30;
					
				}
				selected[selectedCardIdx] = !selected[selectedCardIdx];
				this.repaint();
			}
		}
		
		public void mousePressed(MouseEvent e) {
			//do nothing
		}
		
		public void mouseEntered(MouseEvent e) {
			//do nothing
		}
		
		public void mouseExited(MouseEvent e) {
			//do nothing
		}
		
		public void mouseReleased(MouseEvent e) {
			//do nothing
		}
		

		public void paintComponent(Graphics g) {
			
			
			Graphics2D g2 = (Graphics2D) g;
			super.paintComponent(g2);
			
			for (int i = 0; i < 4; i++) {
				if (!game.getPlayerList().get(i).getName().isEmpty()) {
					avatars[i] = new ImageIcon("images/avatar" + i + ".png").getImage();
				}
			} 
			int x = 50;
			int y = 50;
			Image background = new ImageIcon("images/table.jpg").getImage();
			g2.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), this);
			String playerName;
			for (int i = 0; i < 4; i++) {
				playerName = game.getPlayerList().get(i).getName();
				if (i == activePlayer) {
					g2.setColor(Color.CYAN);
					
				}
				else {
					g2.setColor(Color.WHITE);

				}
				
				//g2.drawString("Player " + i + "", x + 30, y);
				g2.drawString(playerName, x + 30, y);
				g2.drawImage(avatars[i], x, y, 107 , 93, this);
				y += 120;
				int numOfCards = game.getPlayerList().get(i).getNumOfCards();
				for (int j = 0; j < numOfCards ; j++) {

					Image image = null;
					if (i == activePlayer) 
					{
						image = cardImages[i][j];
					} 
					else 
					{
						image = cardBackImage;
					}

		            g2.drawImage(image, imagesX[(i * 13) + j], imagesY[(i * 13) + j], this);
				}
			}
			
			if (!game.getHandsOnTable().isEmpty()) {
				Hand tableHand = game.getHandsOnTable().get(game.getHandsOnTable().size() - 1);
				int xHand = 70;
				g2.setColor(Color.WHITE);
				g2.drawString("Played by " +tableHand.getPlayer().getName(), x + 30, y);
				for (int i = 0; i < tableHand.size(); i++) {
					Card card = tableHand.getCard(i);
					int suit = card.getSuit();
					int rank = card.getRank();
					if (rank == 0)
						rank = 11;
					else if (rank == 1)
						rank = 12;
					else
						rank -= 2;
					Image image = new ImageIcon("images/" + (suit * 13 + rank) + ".gif").getImage();
					g2.drawImage(image, xHand, 550, this);
					xHand += 74;
				}
				xHand = 450;
				for (int i = 0; i < game.getHandsOnTable().size() - 1; i++) {
					Hand stackedHand = game.getHandsOnTable().get(i);
					for (int j = 0; j < stackedHand.size(); j++) {
						xHand += 5;
						g2.drawImage(cardBackImage, xHand, 550, this );
					}
					
				}
			}
		}
	}


	class PlayButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			if (bigTwoPanel.isEnabled()) {
				if (getSelected() != null)
					game.makeMove(activePlayer, getSelected());
				else
					printMsg("Select Cards or press 'Pass' button\n");
			}
			repaint();
		}
	}
	
	class SendButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (!outgoingMsg.getText().isEmpty()) {
				BigTwoClient client = (BigTwoClient) game;
				client.sendMessage(new CardGameMessage(CardGameMessage.MSG, -1, outgoingMsg.getText()));
				outgoingMsg.setText("");
			}
		}
	}
	
	class PassButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if (bigTwoPanel.isEnabled()) {
				game.makeMove(activePlayer, null);
				resetCardsPosition();	//indicate this in the comments as a new feature
				frame.repaint();
			}
		}
	}
		
	class RestartMenuItemListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			BigTwoDeck newDeck = new BigTwoDeck();
			newDeck.initialize();
			newDeck.shuffle();
			game.getHandsOnTable().clear();
			reset();
			game.start(newDeck);	
			updateCardsInfo();
			frame.repaint();

		}
	}
	
	class QuitMenuItemListener implements ActionListener{		
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
	
}
