import java.util.Scanner;

public class Program {
    private static Scanner console = new Scanner(System.in);
    private static Team[] teams = new Team[2];

    public static void main(String[] args) {

        System.out.println("Welcome to Euchre!");
        int decision = getBoundedInt("What is your decision? [1] Playing Alone [2] Playing with Friends?", 1, 2);
        if (decision == 1) {
            playingWithBots();
        } else {
            playingWithFriends();
        }

        Deck deck = new Deck();

        Game game = new Game(teams[0], teams[1], deck);
        game.start();
    }

    private static void playingWithBots(){
        //Playing with bots
        System.out.print("Enter your Name: ");

        teams[0] = new Team(1,
                new Player(console.next(), 1, false),
                new Player("COM2", 1, true)
        );

        teams[1] = new Team(2,
                new Player("COM1", 2, true),
                new Player("COM3", 2, true)
        );

    }

    private static void playingWithFriends(){
        //            for(int i = 0; i < 2; i++){
//                System.out.println("Enter Names for Team " + (i+1));
//                System.out.print("First Name: ");
//                Player player1 = new Player(console.next(), (i+1),false);
//                System.out.print("Second Name: ");
//                Player player2 = new Player(console.next(),(i+1),false);
//                teams[i] = new Team((i+1),player1, player2);
//            }
        //todo uncomment when actually playing
        teams[0] = new Team(1,
                new Player("Jay", 1, false),
                new Player("Nelly", 1, false)
        );
        teams[1] = new Team(2,
                new Player("Heath", 2, false),
                new Player("Boris", 2, false)
        );

    }

    private static int getBoundedInt(String message, int lowerBound, int upperBound) {
        do {
            System.out.println(message);
            try {
                String input = console.nextLine();
                int intInput = Integer.parseInt(input);

                if (intInput < lowerBound || intInput > upperBound)
                    throw new Exception();

                return intInput;

            } catch (Exception e) {
                System.out.println("Invalid Input. Try Again");
            }
        } while (true);
    }
}
