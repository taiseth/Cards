import java.util.ArrayList;
import java.util.Collections;

public class Deck
{
	protected int size = 52;
	private ArrayList<Card> deck = new ArrayList<Card>();
	private String suits[] = {"clubs", "diamonds", "hearts", "spades"};
	
	/*
	 * Default constructor for a Deck
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
		this.size = size + joker;
		create(joker);
	}
	
	/*
	 * Creates a deck of 52 cards w/ jokers if indicated 
	 */
	private void create(int joker)
	{
		// fills the deck with default 52 cards
		for(int i = 0; i < 4; i++) // goes through each suit
		{
			for(int j = 0; j < 13; j++) // goes from 1 to 13
			{
				Card crd = new Card(j + 1, suits[i]);
				deck.add(crd);
			}
		}
		
		// adds the amount of jokers
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
		this.size--;
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
		return this.size;
	}
	
	/*
	 * Adds a card to the top of the deck
	 */
	public void addTop(Card c)
	{
		deck.add(0, c);
		this.size++;
	}
	
	/*
	 * Adds a card to the bottom of the deck
	 */
	public void addBottom(Card c)
	{
		deck.add(c);
		this.size++;
	}
}
