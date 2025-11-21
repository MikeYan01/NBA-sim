package src;

import java.util.*;
import java.io.*;

public class Main {

    /**
     * Main entry of the program.
     * 
     * @param args Command line arguments:
     *             --lang=en or --lang=zh : Set language (English or Chinese)
     *             --predict=100 : Run championship prediction simulation (100 times)
     *             team1 team2 : Host a single game between two teams
     *             (no args) : Run full season simulation
     */
    public static void main(String[] args) throws Exception {
        // Set default language to Chinese
        LocalizedStrings.setLanguage(LocalizedStrings.Language.CHINESE);
        Constants.setTeamLanguage(false); // false = Chinese
        
        // Parse arguments for language setting
        String[] gameArgs = parseLanguageAndGetGameArgs(args);
        
        // Check for prediction mode
        int predictionCount = 0;
        List<String> remainingArgsList = new ArrayList<>();
        for (String arg : gameArgs) {
            if (arg.startsWith("--predict")) {
                if (arg.contains("=")) {
                    try {
                        predictionCount = Integer.parseInt(arg.split("=")[1]);
                    } catch (NumberFormatException e) {
                        predictionCount = 100; // Default
                    }
                } else {
                    predictionCount = 100; // Default
                }
            } else {
                remainingArgsList.add(arg);
            }
        }
        gameArgs = remainingArgsList.toArray(new String[0]);
        
        Game game = new Game();

        // Host single game or host a season based on arguments length
        if (predictionCount > 0) {
            PrintStream console = System.out;
            System.out.println("Running championship prediction simulation (" + predictionCount + " seasons)...");
            
            Map<String, Integer> championCounts = new HashMap<>();
            
            long startTime = System.currentTimeMillis();
            
            for (int i = 0; i < predictionCount; i++) {
                // Print progress every 5 seasons
                if ((i + 1) % 5 == 0) {
                    console.println("Simulated " + (i + 1) + "/" + predictionCount + " seasons...");
                }
                
                // Create new game instance for each simulation to ensure clean state
                game = new Game();
                game.silentMode = true;
                
                String champion = game.hostSeason();
                if (champion != null && !champion.isEmpty()) {
                    championCounts.put(champion, championCounts.getOrDefault(champion, 0) + 1);
                }
            }
            
            // Restore console output
            System.setOut(console);
            
            System.out.println("\nSimulation complete!");
            long endTime = System.currentTimeMillis();
            System.out.println("Time taken: " + (endTime - startTime) / 1000.0 + " seconds");
            
            // Output results
            outputPredictionResults(championCounts, predictionCount);
            
        } else if (gameArgs.length >= 2) {
            game.hostGame(gameArgs[0], gameArgs[1]);
        } else {
            game.hostSeason();
        } 

        return;
    }
    
    /**
     * Output prediction results to file and console.
     */
    private static void outputPredictionResults(Map<String, Integer> championCounts, int totalSimulations) {
        String outputPath = "output/championship_prediction.txt";
        try (PrintStream ps = new PrintStream(outputPath)) {
            ps.println(LocalizedStrings.format("prediction.title", totalSimulations));
            ps.println("==================================================");
            
            // Sort by win count
            List<Map.Entry<String, Integer>> sortedList = new ArrayList<>(championCounts.entrySet());
            sortedList.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));
            
            int rank = 1;
            for (Map.Entry<String, Integer> entry : sortedList) {
                String teamName = entry.getKey();
                int wins = entry.getValue();
                double probability = (double) wins / totalSimulations * 100.0;
                
                // Translate team name if needed
                String displayName = LocalizedStrings.getCurrentLanguage() == LocalizedStrings.Language.CHINESE ?
                                     Constants.translateToChinese(teamName) : teamName;
                
                ps.printf("%d. %s: %d %s (%.1f%%)\n", rank, displayName, wins, LocalizedStrings.get("prediction.wins"), probability);
                rank++;
            }
            
            System.out.println("Results saved to " + outputPath);
            
        } catch (FileNotFoundException e) {
            System.err.println("Error writing prediction results: " + e.getMessage());
        }
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
