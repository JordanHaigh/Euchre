import java.util.ArrayList;
import java.util.List;

public class Player{

    String name;


    private int teamId;
    private List<Card> hand = new ArrayList<>();
    public boolean isDealer;



    public Player(String name, int teamId) {
        this.name = name;
        this.teamId = teamId;
    }

    public int getTeamId() {
        return teamId;
    }

    public String getName() {
        return name;
    }

    public void addCard(Card card){
        this.hand.add(card);
    }

    public Card removeCard(int index){
        return hand.remove(index);
    }

    public Suit checkSuitOfCard(int index){
        return hand.get(index).getSuit();
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
            sb.append("["+(i+1)+"]")
                    .append("\t")
                    .append(hand.get(i).toString())
                    .append("\n");

        }

        return sb.toString();
    }

    @Override
    public String toString() {
        return name;
    }
}

