public class Team{
    private int id;
    private Player player1;
    private Player player2;

    private int points;

    public Team(int id, Player player1, Player player2) {
        this.id = id;
        this.player1 = player1;
        this.player2 = player2;

        points = 0;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void updatePoints(int winningPoints){
        this.points += winningPoints;

    }

    @Override
    public String toString() {
        return "Team{" + player1 + ", " + player2 +"} Points: " + points;
    }
}

