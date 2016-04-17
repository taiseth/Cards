import java.util.ArrayList;

public class Deck
{
	protected int size = 52;
	private ArrayList<Card> deck = new ArrayList<Card>();
	
	public Deck(int size)
	{
		this.size = size;
		create(size, 0);
	}
	
	public Deck(int size, int joker)
	{
		this.size = size + joker;
		create(size, joker);
	}
	
	private void create(int amt, int jkr)
	{
		
	}
	
}
