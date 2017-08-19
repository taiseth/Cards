import java.util.InputMismatchException;
import java.util.Scanner;

public class Blackjack
{
	//amount of money player has
	private static int money = 1000;
	
	//amount of money player has bet
	private static int pot = 0;
		
	//dealer's hand
	private static String dHand;
		
	//player's hand
	private static String pHand;
	
	//single deck used for blackjack
	private static Deck d;
	
	//hand for dealer
	private static Hand house;
	
	//hand for player
	private static Hand player;
	
	public static void main(String[] args)
	{
		d = new Deck();
		d.shuffle();
		house = new Hand();
		player = new Hand();
		boolean round = true;
		
		intro(); //tell the rules
		
		Scanner f = new Scanner(System.in);
		System.out.println("Press enter to begin");
		f.nextLine();
		
		while(round)
		{
			pot = betting(f); //bet of $1 or more needs to be made before cards are dealt
			
			int num21 = deal(); //1 if player has blackjack with starting cards, else 0
			System.out.println("Your hand: " + pHand);
			System.out.println("Dealer's hand: " + dHand);
			int[] dealerVals = handValue(house);
			if(num21 == 1) //if 21, check dealer's hand
			{
				System.out.println("You got blackjack!");				
				
				if(dealerVals[0] == 21 || dealerVals[1] == 21)
				{
					System.out.println("But so did the dealer. Tie!");
					payout(false, true);					
				}
				else
				{
					System.out.println("You win!");
					payout(true, true);
				}				
			}
			else if(dealerVals[0] == 21 || dealerVals[1] == 21) //dealer has blackjack and player doesn't (immediate loss)
			{
				System.out.println("Dealer got blackjack! You lose!");
				payout(false, false);
			}
			else //player doesn't have blackjack, so plays normally
			{
				int playerResult = playerActions(player, pot, 0, f, -2);
				if(playerResult != 0 && playerResult != -1) //bust or split
				{
					int dealerResult = dealer(); //0 for bust, else normal values
					System.out.println("Dealer's hand: " + dHand);
					comparison(playerResult, dealerResult);
				}
			}
			
			dHand = "";
			pHand = "";
			house = new Hand();
			player = new Hand();
			if(money < 1)
			{
				round = false;
				System.out.println("You don't have enough money to play.");
				System.out.println("Game Over!");
			}
			else
			{
				System.out.println("Would you like to play another round? (y/n)");
				boolean valid = false;
				while(!valid)
				{
					String ans = f.nextLine();
					ans.toLowerCase();
					if(ans.equals("y"))
					{
						round = true;
						valid = true;
					}
					else if(ans.equals("n"))
					{
						System.out.println("Thanks for playing!");
						round = false;
						valid = true;
					}
					else
					{
						System.out.println("Not valid answer. try again.");
					}
				}
			}
		}
		f.close();
	}
	
	//Handles the different choices a player can make
	//such as hit, stand, split, or double down
	//returns 0 for bust, -1 for split which is resolved within itself or the value of the player's hand
	private static int playerActions(Hand h, int bet, int numSplit, Scanner p, int dealerSplit)
	{
		int decision = 0; //1 for hit, 2 for stand, 3 for split, 4 for double down
		boolean action = true; //false once player has chosen a valid decision
		int result = 0;
		Hand temp = h;
		
		while(action)
		{
			System.out.println("Choose an action: [1] Hit [2] Stand [3] Split [4] Double down");
			boolean validInput = false;
			while(!validInput)
			{
				try
				{
					decision = p.nextInt();
					p.nextLine();
					validInput = true;
				}
				catch(InputMismatchException e)
				{
					System.out.println("Invalid input. Enter an integral number");
					p.nextLine();
				}
			}
			
			if(decision == 1) //hit
			{
				if(d.getSize() == 0)
				{
					d = new Deck();
					d.shuffle();
				}
				temp.receive(d.draw());
				
				//check if player's hand is 21 or bust, otherwise continue loop
				int[] values = handValue(temp);
				String tempHand = handString(temp);
				System.out.println("Your hand: " + tempHand);
				
				if(values[0] > 21 && values[1] > 21) //both values bust; lost round
				{
					System.out.println("Busted. You lose.");
					payout(false, false);
					action = false;
					result = 0;
				}				
			}
			else if(decision == 2) //stand
			{
				int[] finalVal = handValue(temp);
				if(finalVal[0] > 21)
				{
					result = finalVal[1];
				}
				else
				{
					result = finalVal[0];
				}
				action = false;
			}
			else if(decision == 3) //split
			{
				if(numSplit >= 3)
				{
					System.out.println("You can no longer split your hand.");
				}
				else if(temp.size() != 2) //can only split if starting hand is pairs
				{
					System.out.println("You have more than two cards. You can't split your hand.");
				}
				else if(((temp.view(0)).getValue() != ((temp.view(1)).getValue()))) //starting hand aren't pairs
				{
					System.out.println("Your hand is not a pair. You can't split your hand.");
				}
				else if(money < pot * (numSplit + 1)) //doesn't have enough money to split
				{
					System.out.println("You don't have enough money to split.");
				}
				else
				{
					money -= bet;
					if(dealerSplit == -2)
					{
						dealerSplit = dealer();
					}
					split(temp, bet, numSplit + 1, p, dealerSplit);
					action = false;
					result = -1;
				}
			}
			else if(decision == 4)//double down
			{
				if(temp.size() != 2) //can only double down starting hand
				{
					System.out.println("You can only double down on your starting hand.");
				}
				else if(money < pot) //doesn't have enough money to double bet
				{
					System.out.println("You don't have enough money to double down.");
				}
				else //bet is doubled and player received one card
				{
					money -= pot;
					pot *= 2;
					if(d.getSize() == 0)
					{
						d = new Deck();
						d.shuffle();
					}
					temp.receive(d.draw());
					
					int[] finalVal = handValue(temp);
					String tempHand = handString(temp);
					System.out.println("Your hand: " + tempHand);
					
					if(finalVal[0] > 21)
					{
						if(finalVal[1] > 21) //busted
						{
							System.out.println("Busted. You lose.");
							payout(false, false);
							action = false;
							result = 0;
						}
						else
						{
							action = false;
							result = finalVal[1];
						}
					}
					else
					{
						action = false;
						result = finalVal[0];
					}
				}
			}
			else //invalid choice
			{
				System.out.println("Invalid action. Try again.");
			}
		}
		return result;
	}
	
	//Takes in a hand, the initial bet on the starting hand, and how many splits
	//the player has done. Max is 3.
	private static void split(Hand h, int initBet, int nSplit, Scanner n, int dSplit)
	{
		//splitting given hand and drawing a card for each hand
		Hand hand1 = new Hand();
		Hand hand2 = new Hand();
		hand1.receive(h.give(0));
		hand2.receive(h.give(1));
		if(d.getSize() == 0)
		{
			d = new Deck();
			d.shuffle();
		}
		hand1.receive(d.draw());
		
		if(d.getSize() == 0)
		{
			d = new Deck();
			d.shuffle();
		}
		hand2. receive(d.draw());
		
		//first hand
		String h1 = handString(hand1);
		System.out.println("Your hand: " + h1);
		int[] h1Val = handValue(hand1);
		if(h1Val[0] == 21 || h1Val[1] == 21)
		{
			System.out.println("Blackjack! You win!");
			payout(true, true);
		}
		else
		{
			int numHand = playerActions(hand1, initBet, nSplit, n, dSplit);
			if(numHand != 0)
			{
				System.out.println("Dealer's hand: " + dHand);
				comparison(numHand, dSplit);
			}
		}
		
		//second hand
		pot = initBet;
		String h2 = handString(hand2);
		System.out.println("Your hand: " + h2);
		int[] h2Val = handValue(hand2);
		if(h2Val[0] == 21 || h2Val[1] == 21)
		{
			System.out.println("Blackjack! You win!");
			payout(true, true);
		}
		else
		{
			int numHand = playerActions(hand2, initBet, nSplit, n, dSplit);
			if(numHand != 0)
			{
				System.out.println("Dealer's hand: " + dHand);
				comparison(numHand, dSplit);
			}
		}
	}
	
	//Takes in the value of a player's hand and the dealer's hand
	//Compares the two to decide who wins or loses the round
	//Or if the round ends in a tie
	private static void comparison(int pVal, int dVal)
	{
		if(dVal == -1) //dealer got blackjack
		{
			System.out.println("Dealer got blackjack. You lose!");
			payout(false, false);
		}
		else if(dVal == 0) //dealer bust
		{
			System.out.println("Dealer bust. You win!");
			payout(true, false);
		}
		else //compare values to see if win, lose, or draw
		{
			if(pVal > dVal) //win
			{
				System.out.println("You win!");
				payout(true, false);
			}
			else if(pVal < dVal) //lose
			{
				System.out.println("You lose!");
				payout(false, false);
			}
			else //tie
			{
				System.out.println("Draw!");
				payout(false, true);
			}
		}
		
		System.out.println("You have $" + money + " left.");
	}
	
	//Handles the payout at the end of a round and resets the pot
	//3:2 payout if blackjack, else 2:1
	private static void payout(boolean win, boolean bj)
	{
		if(win && bj) //won with blackjack
		{
			money += (int) (pot * 2.5);
		}
		else if(win && !bj) //won without blackjack
		{
			money += (pot * 2);
		}
		else if(!win && bj) //tie
		{
			money += pot;
		}
		//lost, doesn't receive any money (false, false)
		//pot resets regardless of win or loss
		pot = 0;
	}
	
	//Dealer follows rules given in intro()
	//Returns value of dealer's hand
	//or 0 if dealer busts
	private static int dealer()
	{		
		int[] dealerVals = handValue(house);	
		//must hit if hand is less than 17
		//stand if 17 or higher
		boolean hit = true;
		boolean useAce = false;
		while(hit)
		{
			//check if bust
			if(dealerVals[0] > 21)
			{
				if(dealerVals[1] > 21)
				{
					return 0;
				}
				else
				{
					useAce = true;
				}
			}
			
			//check if dealer should stand
			if((dealerVals[0] >= 17 && dealerVals[0] <= 21) || (dealerVals[1] >= 17 && dealerVals[1] <= 21))
			{
				if(useAce) //only reason this is true is if dealerVals[0] is bust
				{
					return dealerVals[1];
				}
				else
				{
					return dealerVals[0];
				}
			}
			
			//dealer hasn't reached 17 or bust, so hit
			if(d.getSize() == 0)
			{
				d = new Deck();
				d.shuffle();
			}
			house.receive(d.draw());
			dealerVals = handValue(house);
			dHand = handString(house);
		}
		return 0;
	}
	
	//Deals out the hands and checks if player has 21
	//If so, player wins unless dealer also has blackjack
	//else, round continues
	//returns 1 for blackjack, otherwise 0
	private static int deal()
	{
		int blackjack = 0;
		if(d.getSize() == 0) //no cards left, get new deck
		{
			d = new Deck();
			d.shuffle();
		}
		player.receive(d.draw());
		
		if(d.getSize() == 0)
		{
			d = new Deck();
			d.shuffle();
		}
		house.receive((d.draw()));
		
		if(d.getSize() == 0)
		{
			d = new Deck();
			d.shuffle();
		}
		player.receive(d.draw());
		
		if(d.getSize() == 0)
		{
			d = new Deck();
			d.shuffle();
		}
		house.receive((d.draw()));
		
		dHand = handString(house);
		pHand = handString(player);
		int[] hVal = handValue(player);
		if(hVal[0] == 21 || hVal[1] == 21)
		{
			blackjack = 1;
		}
		return blackjack;
	}
	
	//Returns a hand of cards as a string
	private static String handString(Hand h)
	{
		String s = "";
		for(int i = 0; i < h.size(); i++)
		{
			s += (h.view(i)).getRank() + ", ";
		}
		
		s = s.substring(0, s.length() - 2); //remove comma at end
		return s;
	}
	
	//Takes a hand as a parameter
	//Returns the two possible values the hand could be
	private static int[] handValue(Hand h)
	{
		int[] val = new int[2];
		boolean hasAce = false;
		
		for(int i = 0; i < h.size(); i++)
		{
			if(((h.view(i)).getRank()).equals("Ace"))
			{
				if(hasAce) //if first ace count as 11, subsequent aces count as 1
				{
					val[0] += 1;
				}
				else
				{
					val[0] += 11;
					hasAce = true;
				}
				val[1] += 1;
			}
			else if(((h.view(i)).getRank()).equals("King") || ((h.view(i)).getRank()).equals("Queen") 
					|| ((h.view(i)).getRank()).equals("Jack"))
			{
				val[0] += 10;
				val[1] += 10;
			}
			else
			{
				val[0] += (h.view(i)).getValue();
				val[1] += (h.view(i)).getValue();
			}
		}
		return val;
	}
	
	//Player makes a bet of at least $1
	//Allowed to change bet
	//Returns final bet amount
	private static int betting(Scanner s)
	{
		int choice = 0;
		int bet = 0;
		 
		//initial bet
		boolean validBet = false;
		while(!validBet)
		{
			System.out.println("You have $" + money + ".");
			System.out.println("How much would you like to bet?");
			System.out.println("[1] $1 [2] $5 [3] $10 [4] $20 [5] $50 [6] $100 [7] $500 [8] $1000");
			System.out.println("Enter a choice from 1 to 8.");
			
			boolean validInput = false;
			while(!validInput)
			{
				try
				{
					choice = s.nextInt();
					s.nextLine();
					validInput = true;
				}
				catch(InputMismatchException e)
				{
					System.out.println("Invalid input. Enter an integral number");
					s.nextLine();
					validInput = false;
				}
			}
			
			switch(choice)
			{
				case 1: bet = 1;
						break;
				case 2: bet = 5;
						break;
				case 3: bet = 10;
						break;
				case 4: bet = 20;
						break;
				case 5: bet = 50;
						break;
				case 6: bet = 100;
						break;
				case 7: bet = 500;
						break;
				case 8: bet = 1000;
						break;
				default: bet = 0;
						break;
			}
			
			if(choice < 1 || choice > 8)
			{
				System.out.println("Invalid bet. Try again.");
			}
			else if(money - bet < 0)
			{
				System.out.println("You do not have enough money to make this bet. Try again.");
			}
			else
			{
				money -= bet;
				validBet = true;
				System.out.println("You made a bet of $" + bet + ".");
			}
		}
		
		//player can increase/decrease bet
		boolean cont = true;
		int secBet = 0;
		while(cont)
		{
			System.out.println("You have $" + money + ".");
			System.out.println("Would you like to increase or decrease your bet?");
			System.out.println("Enter 1 to increase, 2 to decrease, 3 for neither");
			boolean validInput = false;
			while(!validInput)
			{
				try
				{
					choice = s.nextInt();
					s.nextLine();
					validInput = true;
				}
				catch(InputMismatchException e)
				{
					System.out.println("Invalid input. Enter an integral number");
					s.nextLine();
					validInput = false;
				}
			}
			
			if(choice == 1) //increase bet
			{
				if(money != 0) //has money to bet
				{
					System.out.println("You have $" + money + ".");
					System.out.println("How much would you like to increase?");
					System.out.println("[1] $1 [2] $5 [3] $10 [4] $20 [5] $50 [6] $100 [7] $500 [8] $1000");
					System.out.println("Enter a choice from 1 to 8.");
					validInput = false;
					while(!validInput)
					{
						try
						{
							choice = s.nextInt();
							s.nextLine();
							validInput = true;
						}
						catch(InputMismatchException e)
						{
							System.out.println("Invalid input. Enter an integral number");
							s.nextLine();
							validInput = false;
						}
					}
					
					switch(choice)
					{
						case 1: secBet = 1;
								break;
						case 2: secBet = 5;
								break;
						case 3: secBet = 10;
								break;
						case 4: secBet = 20;
								break;
						case 5: secBet = 50;
								break;
						case 6: secBet = 100;
								break;
						case 7: secBet = 500;
								break;
						case 8: secBet = 1000;
								break;
						default: secBet = 0;
								break;
					}
					
					if(choice < 1 || choice > 8)
					{
						System.out.println("Invalid bet. Try again.");
					}
					else if(money - secBet < 0)
					{
						System.out.println("You do not have enough money to make this bet. Try again.");
					}
					else
					{
						money -= secBet;
						bet += secBet;
						System.out.println("You made a bet of $" + bet + ".");
					}
				}
				else //not enough money to increase bet
				{
					System.out.println("You do not have enough money to increase your bet.");
				}
			}
			else if(choice == 2) //reduce bet
			{
				System.out.println("How much would you like to decrease?");
				System.out.println("[1] $1 [2] $5 [3] $10 [4] $20 [5] $50 [6] $100 [7] $500 [8] $1000");
				System.out.println("Enter a choice from 1 to 8.");
				validInput = false;
				while(!validInput)
				{
					try
					{
						choice = s.nextInt();
						s.nextLine();
						validInput = true;
					}
					catch(InputMismatchException e)
					{
						System.out.println("Invalid input. Enter an integral number");
						s.nextLine();
						validInput = false;
					}
				}
				
				switch(choice)
				{
					case 1: secBet = 1;
							break;
					case 2: secBet = 5;
							break;
					case 3: secBet = 10;
							break;
					case 4: secBet = 20;
							break;
					case 5: secBet = 50;
							break;
					case 6: secBet = 100;
							break;
					case 7: secBet = 500;
							break;
					case 8: secBet = 1000;
							break;
					default: secBet = 0;
							break;
				}
				
				if(choice < 1 || choice > 8)
				{
					System.out.println("Invalid bet. Try again.");
				}
				else if(bet - secBet < 1) //bet must be at least $1
				{
					System.out.println("You must bet at least $1. Deduction did not go through");
				}
				else
				{
					bet -= secBet;
					money += secBet;
					System.out.println("You made a bet of $" + bet + ".");
				}
			}
			else if(choice == 3) //keep bet unchanged
			{
				cont = false;
			}
			else
			{
				System.out.println("Invalid choice. Try again.");
			}
		}
		return bet;
	}	
	
	//Introducing the game and stating the rules
	private static void intro()
	{
		System.out.println("Welcome to BlackJack!\n");
		System.out.println("House Rules");
		System.out.println("1. Dealer must hit if their hand is less than 17");
		System.out.println("2. Dealer stands on all 17s or higher\n");
		System.out.println("Blackjack pays 3 to 2, a normal win pays 2 to 1");
	}	
}
