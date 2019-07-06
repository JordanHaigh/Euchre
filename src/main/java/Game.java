import java.util.*;

public class Game {
    private Team team1;
    private Team team2;

    private Deck cards;
    private Player dealer;
    private Suit trump;

    private static LinkedList<Player> playRotation = new LinkedList<>();
    private static LinkedList<Player> playRotationClone = new LinkedList<>(); //used when players are removed from linked list

    Scanner console = new Scanner(System.in);


    public Game(Team team1, Team team2, Deck cards) {

        this.team1 = team1;
        this.team2 = team2;
        this.cards = cards;

        playRotation.add(team1.getPlayer1());
        playRotation.add(team2.getPlayer1());
        playRotation.add(team1.getPlayer2());
        playRotation.add(team2.getPlayer2());

    }

    public void start() {
        cards.shuffle();

        //choose dealer
        dealer = playRotation.remove();
        dealer.isDealer = true;
        playRotation.add(dealer);

        playRotationClone = playRotation;

        //dealer hands cards to players
        dealCards();

        //Calling Round
        callingRound();

    }

    private void callingRound() {
        Card trumpCard = cards.removeCard();
        System.out.println("Trump is: " + trumpCard.toString());
        //go round table starting from left of dealer

        int decision;
        boolean decisionMade = false;

        for (Player player : playRotation) {
            System.out.println();
            decision = getBoundedInt(player.getName() + ", what is your decision? [1] Pass  [2] Order it Up", 1, 2);

            //does player want to pass
            if (decision == 1) {
                continue; //next player
            }

            //player wants to order it up
            decisionMade = true;
            //dealer takes card and replaces worst card
            decision = getBoundedInt(dealer.getName() + ", Please select a card to replace: \n" + dealer.handToString(), 1, 5);
            dealer.removeCard(decision - 1);
            dealer.addCard(trumpCard);

            // trump is set
            trump = trumpCard.getSuit();

            //does player who ordered it up want to go alone?
            decision = getBoundedInt(player.getName() + ", what is your decision? [1] Go Alone  [2] Play with Partner", 1, 2);

            if (decision == 1) {
                //going alone - remove players partner from rotation
                int teamId = player.getTeamId();
                Team team = getTeamById(teamId);
                removeTeammate(team, player);

                //ready to play without partner
            }

            //ready to play with partner
            break;
        }

        if (!decisionMade) {
            //everyone passed
            //turn trump down
            System.out.println("Trump has been turned down");
            //player starting from left can decide to make it
            for (Player player : playRotation) {
                decision = getBoundedInt(player.getName() + ", what is your decision? [1] Pass  [2] Make Trump", 1, 2);
                if (decision == 1)
                    continue; //passed

                //making trump
                decisionMade = true;
                decision = getBoundedInt(player.getName() + ", what is Trumps? [1] Hearts  [2] Diamonds  [3]Clubs  [4]Spades", 1, 4);
                switch (decision) {
                    case 1:
                        trump = Suit.HEARTS;
                        break;
                    case 2:
                        trump = Suit.DIAMONDS;
                        break;
                    case 3:
                        trump = Suit.CLUBS;
                        break;
                    case 4:
                        trump = Suit.SPADES;
                        break;
                }

                //ready to play
                break;
            }

            //decision still not made, end round
            if(!decisionMade){
                System.out.println("Round has ended. Nobody made trump");
                printScores();
                //start new round
                reset();
                start();

            }
        }

        playHands();
    }

    private void playHands(){

        System.out.println("Starting game...");

    }

    private void dealCards() {
        for (Player player : playRotation) {
            if (player.getTeamId() == dealer.getTeamId())
                player.addHand(dealHand(2));
            else
                player.addHand(dealHand(3));
        }
        for (Player player : playRotation) {
            if (player.getTeamId() == dealer.getTeamId())
                player.addHand(dealHand(3));
            else
                player.addHand(dealHand(2));
        }

    }

    private List<Card> dealHand(int numberOfCards) {
        List<Card> hand = new ArrayList<>();

        for (int i = 0; i < numberOfCards; i++) {
            hand.add(cards.removeCard());
        }
        return hand;
    }


    private void reset(){
        dealer.isDealer = false;
        playRotation = playRotationClone; //adds any removed players back to the queue

        team1.getPlayer1().resetHand();
        team1.getPlayer2().resetHand();
        team2.getPlayer1().resetHand();
        team2.getPlayer2().resetHand();

        cards = new Deck();
    }
    private Team getTeamById(int id) {
        if (id == 1) {
            return team1;
        }
        return team2;
    }

    private void removeTeammate(Team team, Player playerStayingIn) {
        if (team.getPlayer1() == playerStayingIn) {
            playRotation.remove(team.getPlayer2());
        } else {
            playRotation.remove(team.getPlayer1());
        }

    }

    private void printScores(){
        System.out.println("Scores:");
        System.out.println("\t Team 1:" + team1.toString());
        System.out.println("\t Team 2:" + team2.toString());
    }

    private int getBoundedInt(String message, int lowerBound, int upperBound) {
        do {
            System.out.println(message);
            try {
                String input = console.nextLine();
                int intInput = Integer.parseInt(input);

                if (intInput < lowerBound || intInput > upperBound)
                    throw new Exception();

                return intInput;

            } catch (Exception e) {
                System.out.println("Invalid Input. Try Again");
            }
        } while (true);
    }
}
