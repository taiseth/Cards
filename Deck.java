import java.util.ArrayList;
import java.util.Collections;

public class Deck 
{
	private int size = 52;
	private ArrayList<Card> deck = new ArrayList<Card>();
	private String suits[] = {"clubs", "diamonds", "hearts", "spades"};
	
	/*
	 * Default constructor for a deck
	 */
	public Deck()
	{
		create(0);
	}
	
	/*
	 * Constructor for a deck with joker(s)
	 */
	public Deck(int joker)
	{
		size += joker;
		create(joker);
	}
	
	/*
	 * Creates a deck of 52 cards with jokers
	 * if parameter is non-zero
	 */
	private void create(int joker)
	{
		// fills the deck with default 52 cards
		for(int i = 0; i < 4; i++) //goes through each suit
		{
			for(int j = 0; j < 13; j++) //goes from 1 to 13
			{
				Card crd = new Card(j + 1, suits[i]);
				deck.add(crd);
			}
		}
		
		//add the amount of jokers
		if(joker != 0)
		{
			for(int k = 0; k < joker; k++)
			{
				Card c = new Card(0, "joker");
				deck.add(c);
			}
		}
	}
	
	/*
	 * Draw a card from the top of the deck
	 * Card is removed from deck, deck is decremented
	 */
	public Card draw()
	{
		Card top = deck.get(0);
		deck.remove(0);
		size--;
		return top;
	}
	
	/*
	 * Randomizes the elements in the deck list
	 */
	public void shuffle()
	{
		Collections.shuffle(deck);
	}
	
	/*
	 * Returns the size of the deck
	 */
	public int getSize()
	{
		return size;
	}
	
	/*
	 * Adds a card to the top of the deck
	 */
	public void addTop(Card c)
	{
		deck.add(0, c);
		size++;
	}
	
	/*
	 * Adds a card to the bottom of the deck
	 */
	public void addBottom(Card c)
	{
		deck.add(c);
		size++;
	}
}
