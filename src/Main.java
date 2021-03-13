package src;

public class Main {

    /**
     * Main entry of the program.
     * 
     * @param args By default no args are needed, but user can specify two teams to host a game between them
     */
    public static void main(String[] args) throws Exception {
        Game game = new Game();

        // host single game or host a season based on arguments length
        if (args.length > 0) {
            game.hostGame(args[0], args[1]);
        } else {
            game.hostSeason();
        } 

        return;
    }
}
