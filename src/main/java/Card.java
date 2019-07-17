import java.util.Objects;

public class Card {

    private Suit suit;
    private String value;

    public Card(Suit suit, String value) {
        this.suit = suit;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Suit getSuit() {
        return suit;
    }

    public Suit determineAlternateSuit(){
        return Suit.alternateSuit(suit);
    }

    @Override
    public String toString() {
        return "{" + suit + ", " + value + '}';
    }

    public boolean equals(Suit suit, String value){
        return this.suit.equals(suit) && this.value.equals(value);

    }

}

