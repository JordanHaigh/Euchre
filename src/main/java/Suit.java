public enum Suit{
    HEARTS, DIAMONDS, CLUBS, SPADES;

    public static Suit alternateSuit(Suit suit){
        if(suit.equals(DIAMONDS)) return HEARTS;
        else if(suit.equals(HEARTS)) return DIAMONDS;
        else if(suit.equals(SPADES)) return CLUBS;
        else return SPADES;
    }
}
