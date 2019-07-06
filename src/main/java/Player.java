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

    public void removeCard(int index){
        hand.remove(index);
    }

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

