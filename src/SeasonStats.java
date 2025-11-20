package src;

import java.util.*;

public class SeasonStats {
    // player total stats table
    public final Map<String, Integer> playerTotalGames;
    public final Map<String, Integer> playerTotalScores;
    public final Map<String, Integer> playerTotalRebs;
    public final Map<String, Integer> playerTotalOffensiveRebs;
    public final Map<String, Integer> playerTotalDefensiveRebs;
    public final Map<String, Integer> playerTotalAsts;
    public final Map<String, Integer> playerTotalStls;
    public final Map<String, Integer> playerTotalBlks;
    public final Map<String, Integer> playerTotalThrees;
    public final Map<String, Integer> playerTotalFts;
    public final Map<String, Integer> playerTotalShotsAttempted;
    public final Map<String, Integer> playerTotalShotsMade;

    // player per-game stats table
    public final Map<String, Double> playerPerScores;
    public final Map<String, Double> playerPerRebs;
    public final Map<String, Double> playerPerOffensiveRebs;
    public final Map<String, Double> playerPerDefensiveRebs;
    public final Map<String, Double> playerPerAsts;
    public final Map<String, Double> playerPerStls;
    public final Map<String, Double> playerPerBlks;
    public final Map<String, Double> playerPerThrees;
    public final Map<String, Double> playerPerFts;
    public final Map<String, Double> playerPerShotsAttempted;
    public final Map<String, Double> playerPerShotsMade;
    
    // player additional stats
    public final Map<String, Integer> playerTotalThreesAttempted;
    public final Map<String, Integer> playerTotalFtsAttempted;
    public final Map<String, Integer> playerTotalSecondsPlayed;  // Total seconds played
    public final Map<String, Double> playerPerThreesAttempted;
    public final Map<String, Double> playerPerFtsAttempted;
    public final Map<String, Double> playerPerMinutesPlayed;  // Average minutes per game
    public final Map<String, String> playerTeamMap;  // maps player name to team name
    public final Map<String, String> playerEnglishNameMap;  // maps Chinese name to English name

    // team total stats table
    public final Map<String, Integer> teamTotalGames;
    public final Map<String, Integer> teamTotalScores;
    public final Map<String, Integer> teamTotalScoresAllowed;
    public final Map<String, Integer> teamTotalShotsMade;
    public final Map<String, Integer> teamTotalThreeMade;
    public final Map<String, Integer> teamTotalFreeMade;
    public final Map<String, Integer> teamTotalShotsAttempted;
    public final Map<String, Integer> teamTotalThreeAttempted;

    // team per-game stats table
    public final Map<String, Double> teamPerScores;
    public final Map<String, Double> teamPerScoresAllowed;
    public final Map<String, Double> teamPerShotsMade;
    public final Map<String, Double> teamPerThreeMade;
    public final Map<String, Double> teamPerFreeMade;
    public final Map<String, Double> teamPerShotsPercent;
    public final Map<String, Double> teamPerThreePercent;

    /**
     * Construct a SeasonStats object to store all player stats.
     */
    public SeasonStats() {
        playerTotalGames = new HashMap<>();
        playerTotalScores = new HashMap<>();
        playerTotalRebs = new HashMap<>();
        playerTotalOffensiveRebs = new HashMap<>();
        playerTotalDefensiveRebs = new HashMap<>();
        playerTotalAsts = new HashMap<>();
        playerTotalStls = new HashMap<>();
        playerTotalBlks = new HashMap<>();
        playerTotalThrees = new HashMap<>();
        playerTotalFts = new HashMap<>();
        playerTotalShotsAttempted = new HashMap<>();
        playerTotalShotsMade = new HashMap<>();

        playerPerScores = new HashMap<>();
        playerPerRebs = new HashMap<>();
        playerPerOffensiveRebs = new HashMap<>();
        playerPerDefensiveRebs = new HashMap<>();
        playerPerAsts = new HashMap<>();
        playerPerStls = new HashMap<>();
        playerPerBlks = new HashMap<>();
        playerPerThrees = new HashMap<>();
        playerPerFts = new HashMap<>();
        playerPerShotsAttempted = new HashMap<>();
        playerPerShotsMade = new HashMap<>();
        
        playerTotalThreesAttempted = new HashMap<>();
        playerTotalFtsAttempted = new HashMap<>();
        playerTotalSecondsPlayed = new HashMap<>();
        playerPerThreesAttempted = new HashMap<>();
        playerPerFtsAttempted = new HashMap<>();
        playerPerMinutesPlayed = new HashMap<>();
        playerTeamMap = new HashMap<>();
        playerEnglishNameMap = new HashMap<>();

        teamTotalGames = new HashMap<>();
        teamTotalScores = new HashMap<>();
        teamTotalScoresAllowed = new HashMap<>();
        teamTotalShotsMade = new HashMap<>();
        teamTotalThreeMade = new HashMap<>();
        teamTotalFreeMade = new HashMap<>();
        teamTotalShotsAttempted = new HashMap<>();
        teamTotalThreeAttempted = new HashMap<>();

        teamPerScores = new HashMap<>();
        teamPerScoresAllowed = new HashMap<>();
        teamPerShotsMade = new HashMap<>();
        teamPerThreeMade = new HashMap<>();
        teamPerFreeMade = new HashMap<>();
        teamPerShotsPercent = new HashMap<>();
        teamPerThreePercent = new HashMap<>();
    }

    /**
     * Update a player's season stats after a game.
     * 
     * @param p A player object
     */
    public void updatePlayerStats(Player p) {
        String name = p.name;
        int score = p.score;
        int reb = p.rebound;
        int oreb = p.offensiveRebound;
        int dreb = p.defensiveRebound;
        int ast = p.assist;
        int stl = p.steal;
        int blk = p.block;
        int three = p.threeMade;
        int threeAttempted = p.threeAttempted;
        int ft = p.freeThrowMade;
        int ftAttempted = p.freeThrowAttempted;
        int shotMade = p.shotMade;
        int shotAttempted = p.shotAttempted;
        int secondsPlayed = p.secondsPlayed;

        if (p.hasBeenOnCourt) {
            // Store player's team name and English name
            playerTeamMap.put(name, p.teamName);
            playerEnglishNameMap.put(name, p.englishName);
            
            // update total stats
            playerTotalGames.put(name, playerTotalGames.getOrDefault(name, 0) + 1);
            playerTotalScores.put(name, playerTotalScores.getOrDefault(name, 0) + score);
            playerTotalRebs.put(name, playerTotalRebs.getOrDefault(name, 0) + reb);
            playerTotalOffensiveRebs.put(name, playerTotalOffensiveRebs.getOrDefault(name, 0) + oreb);
            playerTotalDefensiveRebs.put(name, playerTotalDefensiveRebs.getOrDefault(name, 0) + dreb);
            playerTotalAsts.put(name, playerTotalAsts.getOrDefault(name, 0) + ast);
            playerTotalStls.put(name, playerTotalStls.getOrDefault(name, 0) + stl);
            playerTotalBlks.put(name, playerTotalBlks.getOrDefault(name, 0) + blk);
            playerTotalThrees.put(name, playerTotalThrees.getOrDefault(name, 0) + three);
            playerTotalThreesAttempted.put(name, playerTotalThreesAttempted.getOrDefault(name, 0) + threeAttempted);
            playerTotalFts.put(name, playerTotalFts.getOrDefault(name, 0) + ft);
            playerTotalFtsAttempted.put(name, playerTotalFtsAttempted.getOrDefault(name, 0) + ftAttempted);
            playerTotalShotsAttempted.put(name, playerTotalShotsAttempted.getOrDefault(name, 0) + shotAttempted);
            playerTotalShotsMade.put(name, playerTotalShotsMade.getOrDefault(name, 0) + shotMade);
            playerTotalSecondsPlayed.put(name, playerTotalSecondsPlayed.getOrDefault(name, 0) + secondsPlayed);

            // update per-game stats
            playerPerScores.put(name, Utilities.roundDouble(playerTotalScores.get(name) * 1.0 / playerTotalGames.get(name)));
            playerPerRebs.put(name, Utilities.roundDouble(playerTotalRebs.get(name) * 1.0 / playerTotalGames.get(name)));
            playerPerOffensiveRebs.put(name, Utilities.roundDouble(playerTotalOffensiveRebs.get(name) * 1.0 / playerTotalGames.get(name)));
            playerPerDefensiveRebs.put(name, Utilities.roundDouble(playerTotalDefensiveRebs.get(name) * 1.0 / playerTotalGames.get(name)));
            playerPerAsts.put(name, Utilities.roundDouble(playerTotalAsts.get(name) * 1.0 / playerTotalGames.get(name)));
            playerPerStls.put(name, Utilities.roundDouble(playerTotalStls.get(name) * 1.0 / playerTotalGames.get(name)));
            playerPerBlks.put(name, Utilities.roundDouble(playerTotalBlks.get(name) * 1.0 / playerTotalGames.get(name)));
            playerPerThrees.put(name, Utilities.roundDouble(playerTotalThrees.get(name) * 1.0 / playerTotalGames.get(name)));
            playerPerThreesAttempted.put(name, Utilities.roundDouble(playerTotalThreesAttempted.get(name) * 1.0 / playerTotalGames.get(name)));
            playerPerFts.put(name, Utilities.roundDouble(playerTotalFts.get(name) * 1.0 / playerTotalGames.get(name)));
            playerPerFtsAttempted.put(name, Utilities.roundDouble(playerTotalFtsAttempted.get(name) * 1.0 / playerTotalGames.get(name)));
            playerPerMinutesPlayed.put(name, Utilities.roundDouble(playerTotalSecondsPlayed.get(name) / 60.0 / playerTotalGames.get(name)));

            if (shotAttempted > 0) {
                playerPerShotsAttempted.put(name, Utilities.roundDouble(playerTotalShotsAttempted.get(name) * 1.0 / playerTotalGames.get(name)));
                playerPerShotsMade.put(name, Utilities.roundDouble(playerTotalShotsMade.get(name) * 1.0 / playerTotalGames.get(name)));
            }
        }
    }

    /**
     * Update a team's season stats after a game.
     * 
     * @param t A team object
     */
    public void updateTeamStats(Team t) {
        String name = t.name;
        int score = t.totalScore;
        int scoreAllowed = t.totalScoreAllowed;
        int shotsMade = t.totalShotMade;
        int threeMade = t.total3Made;
        int freeMade = t.totalFreeMade;
        int shotsAttempted = t.totalShotAttempted;
        int threeAttempted = t.total3Attempted;

        // update total stats
        teamTotalGames.put(name, teamTotalGames.getOrDefault(name, 0) + 1);
        teamTotalScores.put(name, teamTotalScores.getOrDefault(name, 0) + score);
        teamTotalScoresAllowed.put(name, teamTotalScoresAllowed.getOrDefault(name, 0) + scoreAllowed);
        teamTotalShotsMade.put(name, teamTotalShotsMade.getOrDefault(name, 0) + shotsMade);
        teamTotalThreeMade.put(name, teamTotalThreeMade.getOrDefault(name, 0) + threeMade);
        teamTotalFreeMade.put(name, teamTotalFreeMade.getOrDefault(name, 0) + freeMade);
        teamTotalShotsAttempted.put(name, teamTotalShotsAttempted.getOrDefault(name, 0) + shotsAttempted);
        teamTotalThreeAttempted.put(name, teamTotalThreeAttempted.getOrDefault(name, 0) + threeAttempted);

        // update per-game stats
        teamPerScores.put(name, Utilities.roundDouble(teamTotalScores.get(name) * 1.0 / teamTotalGames.get(name)));
        teamPerScoresAllowed.put(name, Utilities.roundDouble(teamTotalScoresAllowed.get(name) * 1.0 / teamTotalGames.get(name)));
        teamPerShotsMade.put(name, Utilities.roundDouble(teamTotalShotsMade.get(name) * 1.0 / teamTotalGames.get(name)));
        teamPerThreeMade.put(name, Utilities.roundDouble(teamTotalThreeMade.get(name) * 1.0 / teamTotalGames.get(name)));
        teamPerFreeMade.put(name, Utilities.roundDouble(teamTotalFreeMade.get(name) * 1.0 / teamTotalGames.get(name)));
        teamPerShotsPercent.put(name, Utilities.roundDouble(teamTotalShotsMade.get(name) * 1.0 / teamTotalShotsAttempted.get(name), 3));
        teamPerThreePercent.put(name, Utilities.roundDouble(teamTotalThreeMade.get(name) * 1.0 / teamTotalThreeAttempted.get(name), 3));
    }

    /**
     * Sort a given table and reorder in a list.
     * 
     * @param table A stat table
     * @return A sorted stat list 
     */
    public List<Map.Entry<String, Double>> sortStats(Map<String, Double> table) {
        Comparator<Map.Entry<String, Double>> vComparator = (o1, o2) -> {
            if (table.equals(teamPerScoresAllowed)) return o1.getValue().compareTo(o2.getValue());
            return o2.getValue().compareTo(o1.getValue());
        };

        List<Map.Entry<String, Double>> sortedList = new ArrayList<Map.Entry<String, Double>>(table.entrySet());
        Collections.sort(sortedList, vComparator);
        return sortedList;
    }

    /**
     * Print the rank of a player stat table.
     * 
     * @param table A stat table
     */
    public void printPlayerRank(Map<String, Double> table) {
        // current rank
        int rank = 1;

        String name;
        String teamName;
        double score;
        double reb;
        double ast;
        double stl;
        double blk;
        double perShotMade;
        double perShotAttempted;
        double perThreeMade;
        double perThreeAttempted;
        double perFtMade;
        double perFtAttempted;

        for (Map.Entry<String, Double> player : sortStats(table)) {
            // output each player's basic 5 stats, except for three mades per game ranking or free-throws per game
            if (!table.equals(playerPerThrees) && !table.equals(playerPerFts)) {
                name = player.getKey();
                teamName = playerTeamMap.getOrDefault(name, "");
                score = playerPerScores.get(name);
                reb = playerPerRebs.get(name);
                double oreb = playerPerOffensiveRebs.getOrDefault(name, 0.0);
                double dreb = playerPerDefensiveRebs.getOrDefault(name, 0.0);
                ast = playerPerAsts.get(name);
                stl = playerPerStls.get(name);
                blk = playerPerBlks.get(name);
                perShotMade = playerPerShotsMade.getOrDefault(name, 0.0);
                perShotAttempted = playerPerShotsAttempted.getOrDefault(name, 0.0);
                perThreeMade = playerPerThrees.getOrDefault(name, 0.0);
                perThreeAttempted = playerPerThreesAttempted.getOrDefault(name, 0.0);
                perFtMade = playerPerFts.getOrDefault(name, 0.0);
                perFtAttempted = playerPerFtsAttempted.getOrDefault(name, 0.0);
                double perMinutes = playerPerMinutesPlayed.getOrDefault(name, 0.0);
                
                // Translate team name if in Chinese mode
                String teamDisplay = "";
                if (!teamName.isEmpty()) {
                    teamDisplay = LocalizedStrings.getCurrentLanguage() == LocalizedStrings.Language.CHINESE ?
                                 Constants.translateToChinese(teamName) : teamName;
                }
                
                // Get localized player name
                String displayName = name;  // Default to Chinese name
                if (LocalizedStrings.getCurrentLanguage() == LocalizedStrings.Language.ENGLISH) {
                    displayName = playerEnglishNameMap.getOrDefault(name, name);
                }

                // Build stats string
                StringBuilder sb = new StringBuilder();
                sb.append(rank).append(" ");
                if (!teamDisplay.isEmpty()) {
                    sb.append("(").append(teamDisplay).append(") ");
                }
                sb.append(displayName).append(" ");
                
                sb.append(score).append(LocalizedStrings.get("stat.points.short")).append(" ");
                
                // Rebound stats with ORB/DRB breakdown
                sb.append(reb).append(LocalizedStrings.get("stat.rebounds.short"));
                if (oreb > 0 || dreb > 0) {
                    sb.append("(")
                      .append(oreb).append(LocalizedStrings.get("stat.rebounds.offensive.short"))
                      .append("+")
                      .append(dreb).append(LocalizedStrings.get("stat.rebounds.defensive.short"))
                      .append(")");
                }
                sb.append(" ");
                
                sb.append(ast).append(LocalizedStrings.get("stat.assists.short")).append(" ");
                sb.append(stl).append(LocalizedStrings.get("stat.steals.short")).append(" ");
                sb.append(blk).append(LocalizedStrings.get("stat.blocks.short")).append("  ");
                
                // Field goal stats
                if (perShotAttempted > 0) {
                    sb.append(LocalizedStrings.get("stat.fieldgoal.label"))
                      .append(perShotMade).append("/").append(perShotAttempted).append(" ")
                      .append(String.format("%.2f", perShotMade * 100.0 / perShotAttempted)).append("%  ");
                } else {
                    sb.append(LocalizedStrings.get("stat.fieldgoal.label")).append("0.0/0.0 0.00%  ");
                }
                
                // Three-point stats
                if (perThreeAttempted > 0) {
                    sb.append(LocalizedStrings.get("stat.threepoint.label"))
                      .append(perThreeMade).append("/").append(perThreeAttempted).append(" ")
                      .append(String.format("%.2f", perThreeMade * 100.0 / perThreeAttempted)).append("%  ");
                } else if (perThreeMade > 0) {
                    // Player made 3s but attempted is 0 (shouldn't happen, but handle it)
                    sb.append(LocalizedStrings.get("stat.threepoint.label"))
                      .append(perThreeMade).append("/0.0 0.00%  ");
                } else {
                    sb.append(LocalizedStrings.get("stat.threepoint.label")).append("0.0  ");
                }
                
                // Free throw stats
                if (perFtAttempted > 0) {
                    sb.append(LocalizedStrings.get("stat.freethrow.label"))
                      .append(perFtMade).append("/").append(perFtAttempted).append(" ")
                      .append(String.format("%.2f", perFtMade * 100.0 / perFtAttempted)).append("%  ");
                } else {
                    sb.append(LocalizedStrings.get("stat.freethrow.label")).append("0.0/0.0 0.00%  ");
                }
                
                // Minutes played (at the end)
                sb.append(LocalizedStrings.get("stat.minutes.long"))
                  .append(String.format("%.1f", perMinutes));

                System.out.println(sb.toString());
            } else {
                // For Three-Pointers and Free Throws rankings, use English name in English mode
                String playerName = player.getKey();
                if (LocalizedStrings.getCurrentLanguage() == LocalizedStrings.Language.ENGLISH) {
                    playerName = playerEnglishNameMap.getOrDefault(playerName, playerName);
                }
                System.out.println(rank + " " + playerName + "  " + player.getValue());
            }

            rank++;
            if (rank > Constants.MAX_PLAYER_RANK) break;
        }
    }

    /**
     * Print the rank of a player stat table.
     * 
     * @param table A stat table
     */
    public void printTeamRank(Map<String, Double> table) {
        // current rank
        int rank = 1;

        for (Map.Entry<String, Double> team : sortStats(table)) {
            // Translate team name if in Chinese mode
            String teamDisplay = LocalizedStrings.getCurrentLanguage() == LocalizedStrings.Language.CHINESE ?
                                Constants.translateToChinese(team.getKey()) : team.getKey();
            
            System.out.println(rank + " " + teamDisplay + "  " + team.getValue());

            rank++;
            if (rank > Constants.MAX_TEAM_RANK) break;
        }
    }

    /**
     * Print out all teams' division standing.
     * 
     * @param standing The hashmap which contains all team's win and lose num
     * @param list The list container for standing rank
     */
    public static void printStanding(Map<String, List<Integer>> standing, List<Map.Entry<String, Integer>> list) {
        int rank = 1;
        for (Map.Entry<String, Integer> team : list) {
            // Translate team name if in Chinese mode
            String teamDisplay = LocalizedStrings.getCurrentLanguage() == LocalizedStrings.Language.CHINESE ?
                                Constants.translateToChinese(team.getKey()) : team.getKey();
            
            double winRate = team.getValue() * 100.0 / (team.getValue() + standing.get(team.getKey()).get(1));
            System.out.println(rank + " " + teamDisplay + " " + team.getValue() + "-" + standing.get(team.getKey()).get(1)
                               + "  " + LocalizedStrings.get("stat.winrate") + String.format("%.2f", winRate) + "%");
            rank++;
        }
    }
}
