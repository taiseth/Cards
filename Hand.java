import java.util.ArrayList;

public class Hand 
{
	private ArrayList<Card> hand = new ArrayList<Card>();
	
	/*
	 * Default constructor for hand
	 */
	public Hand()
	{
		
	}
	
	/*
	 * Adds a card to the hand
	 */
	public void receive(Card c)
	{
		hand.add(c);
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
	 * Returns the card removed from the hand given a value and suit
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
	 * Returns a card from the hand given an index
	 */
	public Card view(int index)
	{
		return hand.get(index);
	}
	
	/*
	 * Returns  a copy of the hand
	 */
	public ArrayList<Card> getHand()
	{
		ArrayList<Card> handView = new ArrayList<Card>();
		for(int i = 0; i < hand.size(); i++)
		{
			Card c = new Card((hand.get(i)).getValue(), (hand.get(i)).getSuit());
			handView.add(c);
		}
		return handView;
	}
	
	/*
	 * Returns size of hand
	 */
	public int size()
	{
		return hand.size();
	}
	
	/*
	 * Makes a copy of the hand before clearing it
	 * Returns the copy of the hand before it was cleared
	 */
	public ArrayList<Card> empty()
	{
		ArrayList<Card> cards = getHand();		
		hand.clear();
		return cards;
	}
}
