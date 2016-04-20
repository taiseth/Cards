import java.util.ArrayList;
public class Hand {
	protected int size;
	private ArrayList<Card> hand = new ArrayList<Card>();
	
	/*
	 * Default constructor for hand 
	 */
	public Hand()
	{
		this.size = 0;
	}
	
	/*
	 * Adds a card to the hand 
	 */
	public void receive(Card c)
	{
		hand.add(c);
		this.size++;
	}
	
	public Card give(int index)
	{
		return hand.remove(index);
	}
}
