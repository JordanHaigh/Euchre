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

        //example:
        //what card seems weaker when hearts are trumps?
        // {7 Hearts} or {Ace Diamonds}?


        int thisCardsStrength = this.calculateCardStrength(trump);
        int otherCardsStrength = otherCard.calculateCardStrength(trump);

        if(thisCardsStrength != otherCardsStrength)
            return thisCardsStrength < otherCardsStrength ? -1 : 1; //Return -1 if this card is less than the other, else return 1

        //if the cards have the same strength value (i.e. theyre both in the same range of cards)
        //then we need to analyse further
        if(thisCardsStrength == 1){
            //both cards are non trump numbers
            return compareCardsBasedOnValue(new String[]{"7","8","9","10"}, otherCard);
        }
        else if(thisCardsStrength == 2){
            //both cards either non trump royals or trump numbers

            //the following order is going to place preference on non trump royals over trump numbers.
            //this might need to be tweaked?
            return compareCardsBasedOnValue(new String[]{"A","K","Q","J","10","9","8","7"}, otherCard);

        }
        else if(thisCardsStrength == 3){
            //both cards are royal trumps
            return compareCardsBasedOnValue(new String[]{"A","K","Q"}, otherCard);

        }
        else{ //Card strength is 4
            //both cards are bowers
            if(this.equals(trump, "J")) //if this card is the right bower, its the higher card. return 1
                return 1;
            else
                return -1; // else this card must be the left bower

        }
    }

    private int compareCardsBasedOnValue(String[] order, Card otherCard){
        int thisCardsPositionInArray = positionInStringArray(order, this.value);
        int otherCardsPositionInArray = positionInStringArray(order, otherCard.getValue());

        return Integer.compare(thisCardsPositionInArray, otherCardsPositionInArray);

    }

    private int positionInStringArray(String[] array, String key){

        for(int i = 0; i < array.length;i++){
            if(array[i].equals(key))
                return i;
        }
        return -1;
    }
}

