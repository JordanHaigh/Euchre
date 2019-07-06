import java.util.Scanner;

public class Program {
    public static void main(String[]args){
        Scanner console = new Scanner(System.in);

        Team[] teams = new Team[2];
        System.out.println("Welcome to Euchre!");
//        for(int i = 0; i < 2; i++){
//            System.out.println("Enter Names for Team " + (i+1));
//            System.out.print("First Name: ");
//            Player player1 = new Player(console.next(), (i+1));
//            System.out.print("Second Name: ");
//            Player player2 = new Player(console.next(),(i+1));
//            teams[i] = new Team((i+1),player1, player2);
//        }

        teams[0] = new Team(1, new Player("Jay", 1), new Player("Nelly", 1));
        teams[1] = new Team(2, new Player("Heath", 2), new Player("Boris", 2));


        Deck deck = new Deck();

        Game game = new Game(teams[0],teams[1], deck);
        game.start();
    }
}
