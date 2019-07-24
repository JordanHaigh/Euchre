import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class Player{

    String name;


    private int teamId;
    private List<Card> hand = new ArrayList<>();

    private boolean isDealer;
    private boolean isBot;


    private int handStrength;


    public Player(String name, int teamId, boolean isBot) {
        this.name = name;
        this.teamId = teamId;
        this.isBot = isBot;
    }

    public int getTeamId() { return teamId; }

    public String getName() { return name; }

    public List<Card> getHand() { return hand; }

    public boolean isDealer() { return isDealer; }

    public void setDealer(boolean dealer) { isDealer = dealer; }

    public boolean isBot() { return isBot; }

    public void setBot(boolean bot) { isBot = bot; }

    public int getHandStrength() { return handStrength; }

    public void addCard(Card card){
        this.hand.add(card);
    }

    public void addCard(int index, Card card){ this.hand.add(index, card); }

    public Card getCard(int index){ return hand.get(index);}

    public Card removeCard(int index){
        return hand.remove(index);
    }

    public void removeCard(Card cardToRemove){
        if(!hand.contains(cardToRemove))
            throw new NoSuchElementException("Card not found in " + name + "'s Hand.");
        else
            hand.remove(cardToRemove);
    }

    public Card peekCard(int index){
        return hand.get(index);
    }

    public boolean isValidMove(Suit currentSuit){
        //i have a hand of 4 heart cards and 1 club card and hearts are trumps
        //I want to play the club card
        //I need to look through my hand and see if i have any heart cards to play instead

        //if I do have some heart cards, this is not a valid move
        //if i dont have any heart cards, it is a valid move.

        int counter = 0;

        for(Card card : hand){
            if(card.getSuit().equals(currentSuit))
                counter++;

        }
        return counter == 0;

    }

    public int getHandLength(){ return hand.size();}

    public void addHand(List<Card> hand) {
        this.hand.addAll(hand);
    }

    public void resetHand(){
        this.hand = new ArrayList<>();
    }

    public String handToString(){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < hand.size();i++){
            sb.append("["+(i+1)+"] ")
                    .append(hand.get(i).toString())
                    .append("\t");
        }

        return sb.toString();
    }

    public int calculateHandStrength(Suit trump){
        //best hand is 17 points
        // right bower +4
        // left bower  +4
        // trump ace   +3
        // trump king  +3
        // trump queen +3
        handStrength = 0;

        for(Card card : hand){
            handStrength += card.calculateCardStrength(trump);
        }

        return handStrength;
    }

    public Suit findBestSuitForHandStrength(Suit suitTurnedDown){
        int bestHandStrength = 0;
        Suit bestSuit = null;
        //iterate over all suits except the one we turned down and find the best hand strength
        for (Suit suit : Suit.values()) {
            if(suit.equals(suitTurnedDown)) //Don't calculate hand strength for the suit we just turned down
                continue;

            int newHandStrength = calculateHandStrength(suit);

            if(bestHandStrength < newHandStrength){
                bestHandStrength = newHandStrength;
                bestSuit = suit;
            }
        }

        if(bestHandStrength > 13)
            return bestSuit;
        else
            return null; //if the hand strength is still too low, return null and that will indicate a pass


    }

    public Card determineWeakestCard(Suit trump){
        Card weakestCard = hand.get(0);

        for(Card card : hand){
            if(card.compareTo(weakestCard, trump) < 0)
                weakestCard = card;
        }

        return weakestCard;
    }

    public Card determineStrongestCard(Suit trump){
        Card strongestCard = hand.get(0);

        for(Card card : hand){
            if(card.compareTo(strongestCard, trump) > 0)
                strongestCard = card;
        }

        return strongestCard;
    }

    @Override
    public String toString() {
        return name;
    }

}

