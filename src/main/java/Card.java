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

    @Override
    public String toString() {
        return "{" + suit + ", " + value + '}';
    }

    public boolean equals(Suit suit, String value){
        return this.suit.equals(suit) && this.value.equals(value);
    }

    public boolean equals(Card card){
        return this.equals(card.getSuit(), card.getValue());
    }

    public int calculateCardStrength(Suit trump){
        Suit leftBowerSuit = Suit.alternateSuit(trump);

        if(this.equals(trump, "J") || this.equals(leftBowerSuit, "J")) //bowers +4
            return 4;
        else if(this.equals(trump,"A") || this.equals(trump,"K") || this.equals(trump,"Q")) //trump royals and ace +3
            return 3;
        else if(this.equals(trump, "10") || this.equals(trump, "9") ||this.equals(trump, "8") || this.equals(trump, "7")) //trump numbers +2
            return 2;
        else if(value.equals("A") || value.equals("K") || value.equals("Q") || value.equals("J")) //non trump royals and ace +2
            return 2;
        else
            return 1; //non trump numbers +1

    }

    public int compareTo(Card otherCard, Suit trump) {

        //Compare cards based on the hand strength code
        //what is classified as a "weak card"?
        //it depends on the current suit and situation that the card can be played

        //example:
        //what card seems weaker when hearts are trumps?
        // {7 Hearts} or {Ace Diamonds}?
        //Depends on the context of the round.

        int thisCardsStrength = this.calculateCardStrength(trump);
        int otherCardsStrength = otherCard.calculateCardStrength(trump);

        return Integer.compare(thisCardsStrength, otherCardsStrength); // -1,0,1
        //this is the best we can do for bots. Could look into finding better ways
    }
}

