public class Team{
    private int id;
    private Player player1;
    private Player player2;

    private int gamePoints;
    private int roundPoints;

    private boolean isBiddingTeam;
    private boolean isGoingAlone;

    public Team(int id, Player player1, Player player2) {
        this.id = id;
        this.player1 = player1;
        this.player2 = player2;

        gamePoints = 0;
        roundPoints = 0;
    }

    public int getId() {return id;}

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public int getGamePoints(){return gamePoints;}

    public int getRoundPoints(){return roundPoints;}

    public boolean isBiddingTeam() { return isBiddingTeam; }

    public void setBiddingTeam(boolean biddingTeam) {this.isBiddingTeam = biddingTeam; }

    public boolean isGoingAlone() { return isGoingAlone; }

    public void setGoingAlone(boolean goingAlone) {this.isGoingAlone = goingAlone; }

    public void updateGamePoints(int winningPoints){
        this.gamePoints+= winningPoints;
    }

    public void incrementRoundPoints(){
        roundPoints++;
    }

    public void resetBooleansAndRoundPoints(){
        isBiddingTeam = false;
        isGoingAlone = false;
        roundPoints = 0;

    }

    @Override
    public String toString() {
        return "Team{" + player1 + ", " + player2 +"} GamePoints: " + gamePoints;
    }
}

