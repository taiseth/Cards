
public class Card 
{
	protected int value;
	protected String suit;
	
	/*
	 * Constructor for a card that takes in an
	 * integer for a value and a string for the suit 
	 */
	public Card(int value, String suit)
	{
		this.value = value;
		this.suit = suit.toLowerCase();
	}
	
	/*
	 * Returns the value of the card 
	 */
	public int returnValue()
	{
		return value;
	}
	
	/*
	 * Returns the suit of the card  
	 */
	public String returnSuit()
	{
		String first = suit.charAt(0) + "";
		first = first.toUpperCase();
		String str = first + suit.substring(1, suit.length());
		return str;
	}
	
	/*
	 * Returns color of the card based on the suit
	 * If invalid suit, returns "invalid suit" 
	 */
	public String returnColor()
	{
		if(suit.equals("hearts") || suit.equals("diamonds"))
		{
			return "red";
		}
		else if(suit.equals("clubs") || suit.equals("spades"))
		{
			return "black";
		}
		else
		{
			return "invalid suit";
		}
	}
	
	/*
	 * Returns the rank of the card
	 * such as Ace, Jack, Queen, King, etc. 
	 */
	public String returnRank()
	{
		String rank;
		switch(value)
		{
			case 1: rank = "Ace";
					break;
			case 2: rank = "Two";
					break;
			case 3: rank = "Three";
					break;
			case 4: rank = "Four";
					break;
			case 5: rank = "Five";
					break;
			case 6: rank = "Six";
					break;
			case 7: rank = "Seven";
					break;
			case 8: rank = "Eight";
					break;
			case 9: rank = "Nine";
					break;
			case 10: rank = "Ten";
					break;
			case 11: rank = "Jack";
					break;
			case 12: rank = "Queen";
					break;
			case 13: rank = "King";
					break;
			default: rank = "invalid value";
					break;
		}		
		return rank;
	}
	
	public static void main(String[] args)
	{
		Card ex = new Card(1, "SPADES");
		System.out.println("This card is the " + ex.returnRank() + " of " + ex.returnSuit());
		System.out.println("Color is " + ex.returnColor());
	}
}
