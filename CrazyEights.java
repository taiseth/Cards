import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Random;

public class CrazyEights {
	
	//single deck used
	private static Deck d;
	
	//player hand
	private static Hand pHand;
	
	//computer hand
	private static Hand cHand;
	
	//discard card
	private static Card discard;
	
	//suit that is chosen after an eight is played
	private static String reqSuit;
	
	public static void main(String[] args)
	{
		boolean play = true;
		
		Scanner f = new Scanner(System.in);
		intro(f);
		
		System.out.println("Press enter to begin");
		f.nextLine();
		
		while(play)
		{
			d = new Deck();
			d.shuffle();
			pHand = new Hand();
			cHand = new Hand();
			reqSuit = "first";
			
			deal();
			int turn = coinflip(f); //cpu turn 0, player turn 1
			
			boolean actions = true;
			while(actions)
			{
				if(turn == 0) //cpu turn
				{
					//cpu actions
					computerAction();
					turn = 1;
					winner();
				}
				else //player turn
				{
					//player actions
					playerAction(f);
					turn = 0;
					winner();
				}
			}
			//if cpu goes first, method involving cpu turn
			//else player goes first, input card to discard
		}
		
	}
	
	private static int winner()
	{
		if(pHand.size() == 0)
		{
			System.out.println("You won!");
			return 1;
		}
		else if(cHand.size() == 0)
		{
			System.out.println("The CPU won!");
			return 0;
		}
		
		return 0;
	}
	
	
	/* Prompts the player for actions during thier turn */
	private static void playerAction(Scanner s)
	{
		System.out.println("Player turn!");
		System.out.println("The top card of the discard pile is the " + discard.getRank() + " of " + discard.getSuit() + ".");
		if(discard.getValue() == 8) // tell the player what suit is required
		{
			if(!reqSuit.equals("first"))
			{
				System.out.println("You must play a card with the suit: " + reqSuit + ", or an 8.");
			}
		}
		
		System.out.println("Your Hand:");
		for(int i = 0; i < pHand.size() - 1; i++)
		{
			Card temp = pHand.view(i);
			System.out.print("[" + (i+1) + "] " + temp.getRank() + " of " + temp.getSuit() + ", "); 
		}
		Card temp = pHand.view(pHand.size() - 1);
		System.out.println("[" + (pHand.size()) + "] " + temp.getRank() + " of " + temp.getSuit());
		
		boolean chooseCard = true;
		while(chooseCard) //players chooses a card from their hand to play
		{
			System.out.println("Input a number to play a card: ");
			int choice = s.nextInt();
			s.nextLine();
			boolean validVal = false;
			
			if(choice >= 1 && choice <= pHand.size()) //number is within range, playable card
			{
				temp = pHand.give(choice - 1);
				System.out.println("You played the " + temp.getRank() + " of " + temp.getSuit() + ".");
				validVal = true;
			}
			else
			{
				System.out.println("Invalid choice. Try again.");
				validVal = false;
			}
			
			if(validVal) //if valid card, check if card matches discard value or suit
			{
				if(temp.getValue() == discard.getValue() || (temp.getSuit()).equals(discard.getSuit()) || temp.getValue() == 8)
				{
					discard = temp;
					chooseCard = false;
				}
				else
				{
					System.out.println("Non-matching card value or suit. Try again");
				}
			}			
		}
		
		if(discard.getValue() == 8) //played an 8, choose a suit
		{	
			boolean validSuit = true;
			String pSuit = "";
			while(validSuit)
			{
				System.out.println("Choose a suit: [1] Spades [2] Clubs [3] Diamonds [4] Hearts");
				int num = s.nextInt();
				if(num == 1)
				{
					pSuit = "Spades";
					validSuit = false;
				}
				else if(num == 2)
				{
					pSuit = "Clubs";
					validSuit = false;
				}
				else if(num == 3)
				{
					pSuit = "Diamonds";
					validSuit = false;
				}
				else if(num == 4)
				{
					pSuit = "Hearts";
					validSuit = false;
				}
				else
				{
					System.out.println("Invalid input. Try again.");
				}				
			}			
			reqSuit = pSuit;
			System.out.println("You chose " + reqSuit + ", and you have " + pHand.size() + " card(s) left.");
		}
		else
		{
			System.out.println("You have " + pHand.size() + " card(s) left.");
		}
	}
	
	/* Decides what the computer does on its turn */
	private static void computerAction()
	{
		System.out.println("CPU turn!");
		System.out.println("The top card of the discard pile is the " + discard.getRank() + " of " + discard.getSuit() + ".");
		if(discard.getValue() == 8) //if card is an 8
		{
			if(reqSuit.equals("first")) //check if first card or played 8 card
			{
				reqSuit = discard.getSuit();
				cpuChoose();
				return;
				
			}
			else //get/play the requested suit from an previously played 8 card
			{
				cpuChoose();
				return;
			}
		}
		else //match suit or value
		{
			for(int i = 0;i < cHand.size(); i++) //valid non-8 card
			{
				Card temp = cHand.view(i);
				if(((temp.getSuit()).equals(discard.getSuit()) && temp.getValue() != 8) ||
						(temp.getValue() == discard.getValue() && temp.getValue() != 8)) 
				{
					discard = cHand.give(i);
					System.out.println("The CPU plays the " + discard.getRank() + " of " + discard.getSuit() + ".");
					System.out.println("The CPU has " + cHand.size() + " card(s) left.");
					return;
				}
			}
			
			for(int j = 0; j < cHand.size(); j++) //has 8 card
			{
				Card temp = cHand.view(j);
				if(temp.getValue() == 8)
				{
					discard = cHand.give(j);
					System.out.println("The CPU plays the " + discard.getRank() + " of " + discard.getSuit() + ".");
					reqSuit = mostSuits();
					System.out.println("The CPU chose " + reqSuit + ".");
					System.out.println("The CPU has " + cHand.size() + " card(s) left.");
					return;					
				}
			}
			
			boolean drawing = true; //draw until 8, valid card, or empty deck
			while(drawing)
			{
				if(d.getSize() == 0)
				{
					return;
				}
				
				Card temp = d.draw();
				if(temp.getValue() == 8)
				{
					discard = temp;
					System.out.println("The CPU plays the " + discard.getRank() + " of " + discard.getSuit() + ".");
					reqSuit = mostSuits();
					System.out.println("The CPU chose " + reqSuit + ".");
					System.out.println("The CPU has " + cHand.size() + " card(s) left.");
					return;
				}
				else if((temp.getSuit()).equals(discard.getSuit()) || temp.getValue() == discard.getValue())
				{
					discard = temp;
					System.out.println("The CPU plays the " + discard.getRank() + " of " + discard.getSuit() + ".");
					System.out.println("The CPU has " + cHand.size() + " card(s) left.");
					return;
				}
				cHand.receive(temp); // card not valid, so keep in hand
			}
		}
	}
	
	/* Handles CPU cases where 8 is the top card of the discard pile */
	private static void cpuChoose()
	{
		for(int i = 0; i < cHand.size(); i++) //play a card of the same suit
		{
			Card temp = cHand.view(i);
			if((temp.getSuit()).equals(reqSuit) && (temp.getValue() != 8))
			{
				discard = cHand.give(i);
				System.out.println("The CPU plays the " + discard.getRank() + " of " + discard.getSuit() + ".");
				System.out.println("The CPU has " + cHand.size() + " card(s) left.");
				return;
			}
		}
		
		for(int j = 0; j < cHand.size(); j++) //play an 8 and declare suit.
		{
			Card temp = cHand.view(j);
			if(temp.getValue() == 8)
			{
				discard = cHand.give(j);
				System.out.println("The CPU plays the " + discard.getRank() + " of " + discard.getSuit() + ".");
				reqSuit = mostSuits();
				System.out.println("The CPU chose " + reqSuit + ".");
				System.out.println("The CPU has " + cHand.size() + " card(s) left.");
				return;
			}
		}
		
		boolean drawing = true;
		while(drawing) //draw until playable card or 8
		{				
			if(d.getSize() == 0)
			{
				return; //return something that represents passing its turn
			}
			
			Card temp = d.draw();
			if(temp.getValue() == 8) //8 card, change suit
			{
				discard = temp;
				System.out.println("The CPU plays the " + discard.getRank() + " of " + discard.getSuit() + ".");
				reqSuit = mostSuits();
				System.out.println("The CPU chose " + reqSuit + ".");
				System.out.println("The CPU has " + cHand.size() + " card(s) left.");
				return;
			}
			else if((temp.getSuit()).equals(reqSuit)) //same suit
			{
				discard = temp;
				System.out.println("The CPU plays the " + discard.getRank() + " of " + discard.getSuit() + ".");
				System.out.println("The CPU has " + cHand.size() + " card(s) left.");
				return;
			}
			cHand.receive(temp);
		}
	}
	
	/* Checks which suit the CPU has the most
	 * Returns that suit as a String 
	 */
	private static String mostSuits()
	{
		int[] numSuit = new int[4]; //0 = spades, 1 = clubs, 2 = diamonds, 3 = hearts		
		for(int i = 0; i < cHand.size(); i++) //count up number of suits in hand
		{
			Card checkSuit = cHand.view(i);
			if((checkSuit.getSuit()).equals("Spades"))
			{
				numSuit[0]++;
			}
			else if((checkSuit.getSuit()).equals("Clubs"))
			{
				numSuit[1]++;
			}
			else if((checkSuit.getSuit()).equals("Diamonds"))
			{
				numSuit[2]++;
			}
			else
			{
				numSuit[3]++;
			}
		}
		
		int max = Math.max(Math.max(numSuit[0], numSuit[1]), Math.max(numSuit[2], numSuit[3]));
		String suitResult;
		if(max == numSuit[0])
		{
			suitResult = "Spades";
		}
		else if(max == numSuit[1])
		{
			suitResult = "Clubs";
		}
		else if(max == numSuit[2])
		{
			suitResult = "Diamonds";
		}
		else
		{
			suitResult = "Hearts";
		}		
		return suitResult;
	}
	
	/* returns result of coinflip to decide who is first
	 * 0 if cpu starts first, 1 if player starts first */
	private static int coinflip(Scanner s)
	{
		Random r = new Random();
		int flipResult = r.nextInt(2);
		boolean valid = false;
		int coinResult = 0;
		while(!valid)
		{
			System.out.println("Deciding who starts first: Heads or Tails?");
			String coin = s.nextLine();
			coin = coin.toLowerCase();
			if(coin.equals("heads"))
			{
				System.out.println("Heads!");
				coinResult = 0;
				valid = true;
			}
			else if(coin.equals("tails"))
			{
				System.out.println("Tails!");
				coinResult = 1;
				valid = true;
			}
			else
			{
				System.out.println("Invalid choice. Try again.");
			}
		}
		
		if(flipResult == coinResult)
		{
			System.out.println("You start first.");
			return 1;
		}
		else
		{
			System.out.println("The CPU starts first.");
			return 0;
		}
	}
	
	/* Deal out hands and set the top card for the discard pile */
	private static void deal()
	{
		for(int i = 0; i <= 8; i++) //fill both hands with 8 cards each
		{
			pHand.receive(d.draw());
			cHand.receive(d.draw());
		}
		
		discard = d.draw();
	}
	
	/* Introduction to the game and how to play it */
	private static void intro(Scanner s)
	{
		System.out.println("Welcome to Crazy Eights!");
		System.out.println("View how to play? (y/n)");
		
		boolean validAns = false;
		
		while(!validAns)
		{
			String howTo = s.nextLine();
			howTo = howTo.toLowerCase();
			if(howTo.equals("y"))
			{
				System.out.println("Each player is dealt 8 cards. The top card of the deck is used to start the discard pile.");
				System.out.println("Players take turns discarding a card by matching either the suit or value of the current discarded card.");
				System.out.println("If a player does not have valid card to play, then draw from the deck until they do.");
				System.out.println("An Eight card can be used to change the suit that the next player has to play.");
				System.out.println("First to empty their hand wins.\n");
				validAns = true;
			}
			else if(howTo.equals("n"))
			{
				validAns = true;
			}
			else
			{
				System.out.println("Not a valid answer.");
			}
		}
		
	}
}
