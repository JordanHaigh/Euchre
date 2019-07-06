import java.util.*;

public class Deck {

    private LinkedList<Card> cards = new LinkedList<>();
    private String[] cardValues = {"7","8","9","10","J","Q","K","A"};


    public Deck() {
        //Instantiate Deck
        for (Suit suit : Suit.values()) {
            for(String value : cardValues){
                cards.add(new Card(suit, value));
            }
        }
    }

    public void shuffle(){
        Collections.shuffle(cards);
    }

    public LinkedList<Card> getCards() {
        return cards;
    }

    public Card removeCard(){
        return cards.remove();
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for(Card card : cards){
            sb.append(card.toString()).append("\n");
        }

        return sb.toString();
    }
}

