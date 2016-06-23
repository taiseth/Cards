import java.util.Scanner;

public class Blackjack
{
	// amount of money player has
	private static int money = 1000;
	
	// amount of money player has bet
	private static int pot = 0;
	
	// numerical value of the player's hand
	private static int handVal = 0;
	
	// numerical value of player's hand where Aces only count as 1's
	private static int aceVal = 0;
	
	// indication whether player has one ace in hand
	private static boolean oneAce;
	
	// dealer's hand
	private static String dHand;
	
	//player's hand
	private static String pHand;
	
	// single deck used for blackjack
	private static Deck d;
	
	// hand for dealer
	private static Hand house;
	
	// hand for player
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

		while(round) //while the player still has money or wants to continue playing
		{
			pot = betting(); //must make a bet
			System.out.println("You made a bet of " + pot + "."); //might be unnecessary? could take out

			int statusRound;
			int num21 = deal(); //1 if player has blackjack, else 0
			if(num21 == 1) //go to payout, then start of loop 
			{
				int dealer21 = dealer(); //-1 if dealer blackjack, else player wins with blackjack
				if(dealer21 == -1)
				{
					System.out.println("But so did the dealer. Tie!");
					payout(false, true);
					round = false;
				}
				else
				{
					System.out.println("You win!");
					payout(true, true);
					round = false;
				}
			}
			else //go to player actions
			{
				int statusRound = playerActions();
			}

		}
		f.close();
	}
	
	// Dealer follows the rules given in intro/rules
	// Returns value of dealer's hand
	private static int dealer()
	{
		int dealerVal = 0;
		int dealerAce = 0;
		boolean hasAce = false;
		//get second card
		if(d.getSize() == 0)
		{
			d = new Deck();
			d.shuffle();
		}
		house.receive(d.draw());
		System.out.println("Drawing card...");
		dHand += ", " + (house.view(1)).getRank();
		
		//calculate hand and check for blackjack
		for(int j = 0; j < house.size(); j++)
		{
			if(((house.view(j)).getRank()).equals("Ace"))
			{
				if(hasAce)
				{
					dealerVal += 1;
				}
				else
				{
					dealerVal += 11;
					hasAce = true;
				}
				dealerAce += 1;
			}
			else if(((house.view(j)).getRank()).equals("King") || ((house.view(j)).getRank()).equals("Queen") || ((house.view(j)).getRank()).equals("Jack"))
			{
				dealerVal += 10;
				dealerAce += 10;
			}
			else
			{
				dealerVal += (house.view(j)).getValue();
				dealerAce += (house.view(j)).getValue();
			}
			
			System.out.println("Dealer hand: " + dHand);
			if(dealerVal == 21)
			{
				return -1; //Dealer got blackjack; beats regular 21
			}
		}

		//must hit if hand is less than 17
		//stand if 17 or higher
		boolean hit = true;
		boolean useAce = false;
		while(hit)
		{
			//check if bust
			if(dealerVal > 21)
			{
				if(dealerAce > 21)
				{
					return 0;
				}
				else 
				{
					useAce = true;
				}
			}
			
			//check if dealer should stand
			if((dealerVal >= 17 && dealerVal <= 21) || (dealerAce >= 17 && dealerAce <= 21))
			{
				if(useAce)
				{
					return dealerAce;
				}
				else
				{
					return dealerVal;
				}
			}
			
			//dealer hasn't reached 17 or bust, so hit
			if(d.getSize() == 0)
			{
				d = new Deck();
				d.shuffle();
			}
			house.receive(d.draw());			
			dHand += ", " + (house.view(1)).getRank();
			
			if(((house.view(house.size() - 1).getRank()).equals("Ace")))
			{
				if(hasAce)
				{
					dealerVal += 1;
				}
				else
				{
					dealerVal += 11;
					hasAce = true;
				}
				dealerAce += 1;
			}
			else if(((house.view(house.size() - 1)).getRank()).equals("King") || ((house.view(house.size() - 1)).getRank()).equals("Queen") || ((house.view(house.size() - 1)).getRank()).equals("Jack"))
			{
				dealerVal += 10;
				dealerAce += 10;
			}
			else
			{
				dealerVal += (house.view(house.size() - 1)).getValue();
				dealerAce += (house.view(house.size() - 1)).getValue();
			}
		}		
		return 0;
	}

	private static void playerActions()
	{
		Scanner p = new Scanner(System.in);
		int decision = 0;
		boolean action = true;
		while(action)
		{
			System.out.println("Choose an action: [1] Hit [2] Stand [3] Split [4] Double down");
			decision = p.nextInt();
			if(decision == 1) //Hit
			{
				if(d.getSize() == 0) //check if deck has cards to draw
				{
					d = new Deck();
					d.shuffle();
				}
				player.receive(d.draw());

				//check if player's hand is 21 or bust, otherwise continue loop
				pHand += (player.view(player.size() - 1)).getRank() + ", ";
				pHand = pHand.substring(0, pHand.length() - 2);
				System.out.println("Your hand: " + pHand);

				if(((player.view(player.size() - 1)).getRank()).equals("Ace"))
				{
					if(oneAce)
					{
						handVal += 1;
					}
					else
					{
						handVal += 11;
						oneAce = true;
					}
					aceVal += 1;
				}
				else if(((player.view(player.size() - 1)).getRank()).equals("King") || ((player.view(player.size() - 1)).getRank()).equals("Queen") || ((player.view(player.size() - 1)).getRank()).equals("Jack"))
				{
					handVal += 10;
					aceVal += 10;
				}
				else
				{
					handVal += (player.view(player.size() - 1)).getValue();
					aceVal += (player.view(player.size() - 1)).getValue();
				}

				if(handVal > 21 && aceVal > 21) //both values bust; lost round
				{
					System.out.println("Busted. You lose.");
					payout(false, false);
					action = false;
				}
			}
			else if(decision == 2) //Stand
			{
				//Dealer AI: -1 if blackjack, 0 if bust, else valid value
				int dealerTurn = dealer();
				if(dealerTurn == -1)
				{
					System.out.println("Dealer got blackjack. You lose!");
					payout(false, false);
					action = false;
				}
				else if(dealerTurn == 0)
				{
					System.out.println("Dealer bust. You win!");
					payout(true, false);
					action = false;
				}
				else //compare hands to see if win, lose, or draw
				{
					boolean aceHand = false;
					if(handVal > 21)
					{
						aceHand = true;
					}
					
					if(aceHand)
					{
						if(aceVal > dealerTurn) //win
						{
							System.out.println("You win!");
							payout(true, false);
						}
						else if(aceVal < dealerTurn) //lose
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
					else
					{
						if(handVal > dealerTurn) //win
						{
							System.out.println("You win!");
							payout(true, false);
						}
						else if(aceVal < dealerTurn) //lose
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
					action = false;
				}
			}
			else if(decision == 3) //Split
			{
				
			}
			else if(decision == 4) //Double down
			{

			}
			else //Invalid choice
			{
				System.out.println("Invalid action. Try again.");
			}
		}
		p.close();
	}
	
	private static int split(Hand pair)
	{
		Hand sp1 = new Hand();
		Hand sp2 = new Hand();
		sp1.receive(pair.give(0));
		sp2.receive(pair.give(1));
		if(d.getSize() == 0)
		{
			d = new Deck();
			d.shuffle();
		}
		sp1.receive(d.draw());
		sp2.receive(d.draw());
			
		return 0;
	}

	private static void payout(boolean win, boolean bj)
	{
		if(win && bj) //won with blackjack
		{
			money += (int)(pot * 2.5);
		}
		else if(win && !bj) //won without blackjack
		{
			money += (pot * 2);
		}
		else if(!win && bj) //got a tie with blackjack (can also be regular tie)
		{
			money += pot;
		}
		
		//pot resets regardless of win or loss
		pot = 0;
	}

	//Deals out the hands and checks if player has 21
	//If so, player automatically wins unless dealer also has blackjack
	//Else, round continues
	//Returns 1 for blackjack, otherwise 0
	private static int deal()
	{
		int blackjack = 0;
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
		house.receive(d.draw());
		
		if(d.getSize() == 0)
		{
			d = new Deck();
			d.shuffle();
		}
		player.receive(d.draw());

		dHand = (house.view(0)).getRank();
		System.out.println("Dealer's hand: " + dHand);
		pHand = "";
		oneAce = false;
		handVal = 0;
		aceVal = 0;
		for(int i = 0; i < player.size(); i++) // calculate hand w/ special value cards A, K, Q, J
		{
			pHand += (player.view(i)).getRank() + ", ";
			if(((player.view(i)).getRank()).equals("Ace"))
			{
				if(oneAce)
				{
					handVal += 1;
				}
				else
				{
					handVal += 11;
					oneAce = true;
				}
				aceVal += 1;
			}
			else if(((player.view(i)).getRank()).equals("King") || ((player.view(i)).getRank()).equals("Queen") || ((player.view(i)).getRank()).equals("Jack"))
			{
				handVal += 10;
				aceVal += 10;
			}
			else
			{
				handVal += (player.view(i)).getValue();
				aceVal += (player.view(i)).getValue();
			}
		}

		pHand = pHand.substring(0, pHand.length() - 2);
		System.out.println("Your hand: " + pHand);
		if(handVal == 21) //User has 21, win with payout pot * 2.5
		{
			System.out.println("You got Blackjack!");
			blackjack = 1;
		}

		return blackjack;
	}

	//Player makes a bet of at least $1
	//Allowed to change it if desired
	//Returns the final bet amount
	private static int betting()
	{
		Scanner s = new Scanner(System.in);
		int choice = 0;
		int bet = 0;

		//inital bet
		boolean validBet = false;
		while(!validBet)
		{
			System.out.println("You have $" + money + ".");
			System.out.println("How much would you like to bet?");
			System.out.println("[1] $1 [2] $5 [3] $10 [4] $20 [5] $50 [6] $100 [7] $500 [8] $1000");
			System.out.println("Enter a choice from 1 to 8.");
			choice = s.nextInt();
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

		// player can increase/decrease bet
		boolean cont = true;
		int secBet = 0;
		while(cont)
		{
			System.out.println("You have $" + money + ".");
			System.out.println("Would you like to increase or decrease your bet?");
			System.out.println("Enter 1 to increase, 2 to decrease, 3 for neither");
			choice = s.nextInt();
			if(choice == 1) //increase bet
			{
				if(money != 0) //has money to increase bet
				{
					System.out.println("How much more would you like to bet?");
					System.out.println("[1] $1 [2] $5 [3] $10 [4] $20 [5] $50 [6] $100 [7] $500 [8] $1000");
					System.out.println("Enter a choice from 1 to 8.");
					choice = s.nextInt();
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
					else if(money - bet - secBet < 0)
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
				else //not enough money to increase bet; could reduce
				{
					System.out.println("You do not have enough money to increase your bet.");
				}
			}
			else if(choice == 2) //reduce bet
			{
				System.out.println("How much would you like to decrease?");
				System.out.println("[1] $1 [2] $5 [3] $10 [4] $20 [5] $50 [6] $100 [7] $500 [8] $1000");
				System.out.println("Enter a choice from 1 to 8.");
				choice = s.nextInt();
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
				else if(bet - secBet < 1) //bet must be at lease $1
				{
					System.out.println("You must bet at least $1. Deduction did not go through.");
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
		s.close();
		return bet;
	}

	// the intro with rules
	private static void intro()
	{
		System.out.println("Welcome to BlackJack!\n");
		System.out.println("House Rules");
		System.out.println("1. Dealer must hit if their hand is less than 17");
		System.out.println("2. Dealer stands on all 17s or higher\n");
		System.out.println("Blackjack pays 3 to 2, a normal win pays 2 to 1");
	}
}