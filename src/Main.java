package src;

public class Main {

    /**
     * Main entry of the program.
     * 
     * @param args Command line arguments:
     *             --lang=en or --lang=zh : Set language (English or Chinese)
     *             team1 team2 : Host a single game between two teams
     *             (no args) : Run full season simulation
     */
    public static void main(String[] args) throws Exception {
        // Set default language to Chinese
        LocalizedStrings.setLanguage(LocalizedStrings.Language.CHINESE);
        Constants.setTeamLanguage(false); // false = Chinese
        
        // Parse arguments for language setting
        String[] gameArgs = parseLanguageAndGetGameArgs(args);
        
        Game game = new Game();

        // Host single game or host a season based on arguments length
        if (gameArgs.length >= 2) {
            game.hostGame(gameArgs[0], gameArgs[1]);
        } else {
            game.hostSeason();
        } 

        return;
    }
    
    /**
     * Parse language argument and return remaining arguments for game.
     * 
     * @param args Original command line arguments
     * @return Remaining arguments after removing language flag
     */
    private static String[] parseLanguageAndGetGameArgs(String[] args) {
        if (args.length == 0) {
            return args;
        }
        
        // Check first argument for language flag
        if (args[0].startsWith("--lang=")) {
            String langCode = args[0].substring(7).toLowerCase();
            
            if (langCode.equals("en") || langCode.equals("english")) {
                LocalizedStrings.setLanguage(LocalizedStrings.Language.ENGLISH);
                Constants.setTeamLanguage(true); // true = English
                System.out.println("Language set to: English");
            } else if (langCode.equals("zh") || langCode.equals("chinese") || langCode.equals("zh_cn")) {
                LocalizedStrings.setLanguage(LocalizedStrings.Language.CHINESE);
                Constants.setTeamLanguage(false); // false = Chinese
                System.out.println("语言设置为: 中文");
            } else {
                System.err.println("Unknown language: " + langCode + ". Using default (Chinese).");
            }
            
            // Return remaining arguments (skip the language flag)
            String[] remainingArgs = new String[args.length - 1];
            System.arraycopy(args, 1, remainingArgs, 0, args.length - 1);
            return remainingArgs;
        }
        
        // No language flag found, return all arguments
        return args;
    }
}
