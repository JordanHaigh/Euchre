import java.util.*;

public class Game {
    private Team team1;
    private Team team2;

    private Deck cards;
    private Player dealer;
    private Suit trump;
    private Suit leftBowerSuit;


    private static LinkedList<Player> playRotation = new LinkedList<>();
    private static LinkedList<Player> playRotationClone = new LinkedList<>(); //used when players are removed from linked list

    private Scanner console = new Scanner(System.in);


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
        dealer.setDealer(true);
        playRotation.add(dealer);

        playRotationClone = new LinkedList<>();
        for(Player p : playRotation){
            playRotationClone.add(p); //fixes shallow copy
        }

         //dealer hands cards to players
         dealCards();

         for(Player p : playRotation){
             System.out.println(p.getName() + "'s Hand: " + p.handToString());
         }

        System.out.println(dealer.getName() + " deals the cards.");

         //Calling Round
         callingRound();

    }

    private void callingRound() {
        Card callingCard = cards.removeCard();
        System.out.println("Calling Card is: " + callingCard.toString());
        //go round table starting from left of dealer

        boolean decisionMade = false;

        ////////////////////First round of decisions ///////////////////////
        for (Player player : playRotation) {
            if(player.isBot())
                decisionMade = callingRound_BotMakesFirstDecision(player, callingCard);
            else
                decisionMade = callingRound_PlayerMakesFirstDecision(player, callingCard);

            if(decisionMade)
                break;

        }
        ////////////////////Second Round round of decisions ///////////////////////
        if (!decisionMade) {
            //everyone passed, turn trump down
            System.out.println("Calling Card has been turned down");
            //player starting from left can decide to make it
            for (Player player : playRotation) {
                if(player.isBot())
                    decisionMade = callingRound_BotMakesSecondDecision(player, callingCard);

                else
                    decisionMade = callingRound_PlayerMakesSecondDecision(player);


                if(decisionMade)
                    break;
            }

            //decision still not made, end round
            if (!decisionMade) {
                System.out.println("Round has ended. Nobody made trump");
                printScores();
                //start new round
                reset();
                start();

            }
        }

        playHands();
    }

    private boolean callingRound_BotMakesFirstDecision(Player player, Card callingCard){

        int handStrength = player.calculateHandStrength(callingCard.getSuit(), callingCard.determineAlternateSuit()); //best hand is 17 points
        if(handStrength >= 11) {
            //confident hand strength to order it up
            System.out.println(player.getName() + " wants to order it up!");
            callingRound_orderUp(player, callingCard);
            return true;
        }
        //bot doesnt have a strong hand and passes
        System.out.println(player + " passes.");
        return false;

    }

    private boolean callingRound_PlayerMakesFirstDecision(Player player, Card trumpCard){
        int decision = getBoundedInt(player.getName() + ", what is your decision? [1] Pass  [2] Order it Up", 1, 2);

        //does player want to pass
        if (decision == 1) {
            return false; //decision not made
        }

        //decision made
        //player wants to order it up
        callingRound_orderUp(player, trumpCard);

        //ready to play with/without partner
        return true;

    }

    private void callingRound_orderUp(Player player, Card trumpCard){
        //assign bidding team
        Team biddingTeam = getTeamById(player.getTeamId());
        biddingTeam.setBiddingTeam(true);

        //dealer takes card and replaces worst card
        int decision = getBoundedInt(dealer.getName() + ", You Are Dealer. Please select a card to replace: \n" + dealer.handToString(), 1, 5);
        dealer.removeCard(decision - 1);
        dealer.addCard(trumpCard);

        // trump is set
        trump = trumpCard.getSuit();
        assignLeftBower();

        //does player who ordered it up want to go alone?
        if(player.isBot()){
            callingRound_BotDecidesToGoAlone(player);
        }
        else{
            callingRound_playerDecidesToGoAlone(player);
        }

        //ready to play
    }

    private void callingRound_BotDecidesToGoAlone(Player player){
        int handStrength = player.getHandStrength();
        if(handStrength > 13){ //high chance of winning
            //Bot decides to go alone
            System.out.println(player.getName() + " wants to go alone!");
            int teamId = player.getTeamId();
            Team team = getTeamById(teamId);
            removeTeammate(team, player);
            team.setGoingAlone(true);

            //ready to play without partner
        }
        else
            System.out.println(player.getName() + " will play with their partner.");

    }

    private void callingRound_playerDecidesToGoAlone(Player player){
        int decision = getBoundedInt(player.getName() + ", what is your decision? [1] Go Alone  [2] Play with Partner", 1, 2);

        if (decision == 1) {
            //going alone - remove players partner from rotation
            int teamId = player.getTeamId();
            Team team = getTeamById(teamId);
            removeTeammate(team, player);
            team.setGoingAlone(true);

            //ready to play without partner
        }
    }

    private boolean callingRound_BotMakesSecondDecision(Player player, Card callingCard){
        //go through all suits but the suit missed, and find best hand strength, if strength > 13, that person makes it
        Suit bestSuitForHand = player.findBestSuitForHandStrength(callingCard.getSuit());
        if(bestSuitForHand != null){
            //found a suit to call trump
            trump = bestSuitForHand;
            System.out.println(player.getName() + " has decided to make trumps!");
            return true; //decision made
        }
        else{
            System.out.println(player.getName() + " has passed again.");
            return false; //bot doesnt make a decision and passes
        }

    }

    private boolean callingRound_PlayerMakesSecondDecision(Player player){
        int decision = getBoundedInt(player.getName() + ", what is your decision? [1] Pass  [2] Make Trump", 1, 2);
        if (decision == 1)
            return false; //decision not made

        //making trump
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
        assignLeftBower();


        System.out.println(trump.toString() + " are trumps.");

        Team biddingTeam = getTeamById(player.getTeamId());
        biddingTeam.setBiddingTeam(true);

        //ready to play
        return true; //decision made


    }

    private void assignLeftBower(){
        leftBowerSuit = Suit.alternateSuit(trump);
    }

    private void playHands() {
        System.out.println("Round commence!");
        System.out.println("Trump is " + trump);

        int currentRound = 0;
        while(currentRound < 5){ //Play five hands - maybe make this prettier
            playRound();
            currentRound++;
        }

        System.out.println("All hands have been played!");

        if(team1.getRoundPoints() > team2.getRoundPoints()){
            System.out.println("Team 1 Wins the Hand!");

            int pointsToAdd = determinePointsToAddToGameScore(team1);

            //update game points based on game conditions
            team1.updateGamePoints(pointsToAdd);
            System.out.println("Team 1 gains " + pointsToAdd + " points");

            //if team1 has enough points to win the game, exit
            if(teamHasEnoughPointsToWin(team1)){
                System.out.println("Team 1 Wins the Game!");
                System.exit(0);
            }
        }
        else{
            System.out.println("Team 2 Wins the Hand!");
            int pointsToAdd = determinePointsToAddToGameScore(team2);
            //update game points based on game conditions
            team2.updateGamePoints(pointsToAdd);
            System.out.println("Team 2 gains " + pointsToAdd + " points");


            //if team2 has enough points to win the game, exit
            if(teamHasEnoughPointsToWin(team2)){
                System.out.println("Team 2 Wins the Game!");
                System.exit(0);
            }
        }

        //nobody has hit 10 points, keep playing

        System.out.println("Current Scores: ");
        System.out.println("Team 1: " + team1.getGamePoints());
        System.out.println("Team 2: " + team2.getGamePoints());

        reset();
        start();
    }

    private int determinePointsToAddToGameScore(Team winningTeam){
        if(winningTeam.isBiddingTeam()){
            if(winningTeam.isGoingAlone()){
                if(winningTeam.getRoundPoints() == 3 || winningTeam.getRoundPoints() == 4)//Bidder goes alone and wins 3 or 4 tricks
                    return 1;
                else //Bidder goes alone and wins 5 tricks (march)
                    return 4;
            }

            if(winningTeam.getRoundPoints() == 3 || winningTeam.getRoundPoints() == 4) //Bidding partnership (makers) wins 3 or 4 tricks
                return 1; //1 Point
            else if(winningTeam.getRoundPoints() == 5)//Bidding partnership (makers) wins 5 tricks (march)
                return 2;


        }
        else{ //Defenders
            if(winningTeam.isGoingAlone())//Defender goes alone and wins 3 or more tricks (regional)
                return 4;
            else//Defenders win 3 or more tricks (Euchred)
                return 2;
        }

        return 10000; //Won't get here
    }

    private boolean teamHasEnoughPointsToWin(Team team){
        return team.getGamePoints() >= 10;
    }

    private void playRound(){
        Suit currentSuit = null; // it'll get updated in the first pass of the for loop
        HashMap<Card, Player> cardsPlayedAndByWhom = new HashMap<>();
        int decision;
        //each person plays a card
        for (Player player : playRotation) {
            decision = getBoundedInt(player.getName() + ", what card do you play?\n" + player.handToString(), 1, player.getHandLength());
            Card card;
            if (cardsPlayedAndByWhom.size() == 0) {
                //then we can add whatever card
                card = player.removeCard(decision - 1);
                cardsPlayedAndByWhom.put(card, player);
                currentSuit = card.getSuit();

                //if the left bower was played we need to override the current suit to be the trump suit
                if(leftBowerChosenByPlayer(card))
                    currentSuit = trump;
                System.out.println("Current Suit is " + currentSuit);
            } else {
                //check what suit that the decision corresponds to
                Suit decidedSuit = player.checkSuitOfCard(decision - 1);
                if (decidedSuit.equals(currentSuit)) {
                    //all good, add it to the table
                    card = player.removeCard(decision - 1);
                    cardsPlayedAndByWhom.put(card, player);

                } else {
                    //its the wrong suit, need to check if they accidentally chose the wrong card or if they dont have any suit cards to play
                    if (!player.isValidMove(currentSuit)) {
                        //check if the card being played is the left bower
                        if (leftBowerWasChosenByPlayer(player, decision)) {
                            //valid move
                            card = player.removeCard(decision - 1);
                            cardsPlayedAndByWhom.put(card, player);
                        } else {
                            //player has current suit cards but didnt play them, this is invalid
                            int properDecision = retryCardPlay(player, currentSuit);
                            card = player.removeCard(properDecision - 1);
                            cardsPlayedAndByWhom.put(card, player);
                        }
                    } else {
                        //player doesnt have any suit cards, so now they can play whatever they like
                        card = player.removeCard(decision - 1);
                        cardsPlayedAndByWhom.put(card, player);

                    }
                }
            }

            System.out.println("Current Cards on the Table:");
            for(Map.Entry<Card, Player> cardPlayerEntry : cardsPlayedAndByWhom.entrySet()){
                System.out.println(cardPlayerEntry.getKey().toString() + " played by " + cardPlayerEntry.getValue().toString());
            }
        }

        System.out.println("Round over!");
        determingWinningTeam(currentSuit, cardsPlayedAndByWhom);

    }

    private boolean leftBowerWasChosenByPlayer(Player player, int decision) {
        Card peekingCard = player.peekCard(decision - 1);
        return leftBowerChosenByPlayer(peekingCard);
    }

    private boolean leftBowerChosenByPlayer(Card card){
        return card.equals(leftBowerSuit, "J");
    }

    private boolean bowerWasPlayedDuringRound(Suit bowerSuit, List<Card> currentSuitCardsPlayed) {
        for (Card card : currentSuitCardsPlayed) {
            if (card.equals(bowerSuit, "J")) {
                return true;
            }
        }
        return false;
    }

    private int getIndexOfBowerInList(Suit bowerSuit, List<Card> cardList) {
        for (int i = 0; i < cardList.size(); i++) {
            if (cardList.get(i).equals(bowerSuit, "J")) {
                return i;
            }
        }
        return -1;
    }

    private void determingWinningTeam(Suit currentSuit, HashMap<Card, Player> cardsPlayedAndByWhom) {

        List<Card> allCards = new ArrayList<>();

        for (Map.Entry<Card, Player> cardPlayerEntry : cardsPlayedAndByWhom.entrySet()) {
            allCards.add(cardPlayerEntry.getKey());
        }

        List<Card> cardsInOrder = new ArrayList<>();

        //check if right bower was played
        if (bowerWasPlayedDuringRound(trump, allCards)) {
            //remove from trump cards played
            int rightBowerIndex = getIndexOfBowerInList(trump, allCards);
            Card rightBower = allCards.remove(rightBowerIndex);

            // add to p1 of list
            cardsInOrder.add(0, rightBower);
        }

        //check if left bower was played
        if (bowerWasPlayedDuringRound(leftBowerSuit, allCards)) {
            //remove from current suit cards played
            //get index of bower in list
            int leftBowerIndex = getIndexOfBowerInList(leftBowerSuit, allCards);
            Card leftBower = allCards.remove(leftBowerIndex);

            //add to p2 of list (or p1 if right bower not played
            if(cardsInOrder.size() == 1)
                cardsInOrder.add(1, leftBower);
            else
                cardsInOrder.add(0, leftBower);
        }

        //add remaining trump cards
        List<Card> trumpCardsPlayed = getCardsPlayedBasedOnSuit(allCards, trump);

        //sort all other trumps A,K,Q,10,9,8,7
        String[] otherTrumpOrder = new String[]{"A", "K", "Q", "10", "9", "8", "7"};
        for (String trumpValue : otherTrumpOrder) {
            for (Card card : trumpCardsPlayed) {
                if (card.getValue().equals(trumpValue))
                    cardsInOrder.add(card);
            }
        }

        //if trump is the same as the hands suit, then we need to add the current suit cards
        if (trump != currentSuit) {
            //get current suit cards
            List<Card> currentSuitCardsPlayed = getCardsPlayedBasedOnSuit(allCards, currentSuit);
            //sort other current suit cards A,K,Q,J,10,9,8,7
            String[] currentSuitOrder = new String[]{"A", "K", "Q", "J", "10", "9", "8", "7"};

            for (String suitValue : currentSuitOrder) {
                for (Card card : currentSuitCardsPlayed) {
                    if (card.getValue().equals(suitValue))
                        cardsInOrder.add(card);
                }
            }
        }

        //other cards are duds.

        //get winner
        Player winningPlayer = cardsPlayedAndByWhom.get(cardsInOrder.get(0));
        System.out.println(winningPlayer.getName() + " Wins the Round!");

        Team winningTeam = getTeamById(winningPlayer.getTeamId());
        winningTeam.incrementRoundPoints();
        reassignPlayRotationBasedOnWinner(winningPlayer);
    }


    private List<Card> getCardsPlayedBasedOnSuit(List<Card> allCards, Suit suit) {
        List<Card> cardsPlayed = new ArrayList<>();

        for (Card card : allCards) {
            if (card.getSuit().equals(suit)) {
                cardsPlayed.add(card);
            }
        }
        return cardsPlayed;
    }

    private int retryCardPlay(Player player, Suit currentSuit) {
        //eg. suit is hearts. player has a 4 heart cards and 1 club and accidentally picked the clubs card . so they need
        //to go in here and loop till they pick a suit card.
        int decision;
        while (true) {
            System.out.println(player.getName() + ", that card cannot be played.");
            decision = getBoundedInt(player.getName() + ", what card do you play?\n" + player.handToString(), 1, player.getHandLength());
            Suit decidedSuit = player.checkSuitOfCard(decision - 1);
            if (decidedSuit.equals(currentSuit)) {
                //all good, add it to the table
                return decision;
            }
        }

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

    private void reassignPlayRotationBasedOnWinner(Player winner){
        //whilst the winner doesnt equal the first person in the play rotation, keep removing and appending
        while(!winner.equals(playRotation.get(0))){
            Player p = playRotation.remove(0);
            playRotation.add(p);
        }
    }

    private List<Card> dealHand(int numberOfCards) {
        List<Card> hand = new ArrayList<>();

        for (int i = 0; i < numberOfCards; i++) {
            hand.add(cards.removeCard());
        }
        return hand;
    }


    private void reset() {
        dealer.setDealer(false);

        for(Player player : playRotation)
            player.resetHand();

        playRotation = new LinkedList<>();
        for(Player p : playRotationClone){
            playRotation.add(p);  //fixes shallow copy - adds any removed players back to queue
        }

        cards = new Deck();

        team1.resetBooleansAndRoundPoints();
        team2.resetBooleansAndRoundPoints();
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

    private void printScores() {
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
