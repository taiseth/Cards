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
	
	/*
	 * Returns the card removed from the hand
	 * given the index 
	 */
	public Card give(int index)
	{
		return hand.remove(index);
	}
	
	/*
	 * Returns the card removed from the hand
	 * given a value and suit
	 * Returns null if the card does not exist in the hand 
	 */
	public Card give(int value, String suit)
	{
		Card c = new Card(value, suit);
		int index = hand.indexOf(c); //finds where card is located in hand
		if(index == -1)
		{
			return null;
		}
		else
		{
			return hand.remove(index);
		}
	}
	
	/*
	 * Makes a copy of the hand before clearing it
	 * Returns the copy of the hand before it was cleared
	 */
	public ArrayList<Card> empty()
	{
		ArrayList<Card> cards = new ArrayList<Card>();
		for(int i = 0 ; i < hand.size(); i++)
		{
			Card c = new Card((hand.get(i)).getValue(), (hand.get(i)).getSuit());
			cards.add(c);
		}
		
		hand.clear();
		return cards;		
	}
}
