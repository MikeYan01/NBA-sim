package src;

import java.util.*;

public class SeasonStats {
    // player total stats table
    public final Map<String, Integer> playerTotalGames;
    public final Map<String, Integer> playerTotalScores;
    public final Map<String, Integer> playerTotalRebs;
    public final Map<String, Integer> playerTotalAsts;
    public final Map<String, Integer> playerTotalStls;
    public final Map<String, Integer> playerTotalBlks;
    public final Map<String, Integer> playerTotalThrees;
    public final Map<String, Integer> playerTotalFts;

    // player per-game stats table
    public final Map<String, Double> playerPerScores;
    public final Map<String, Double> playerPerRebs;
    public final Map<String, Double> playerPerAsts;
    public final Map<String, Double> playerPerStls;
    public final Map<String, Double> playerPerBlks;
    public final Map<String, Double> playerPerThrees;
    public final Map<String, Double> playerPerFts;

    // team total stats table
    public final Map<String, Integer> teamTotalGames;
    public final Map<String, Integer> teamTotalScores;
    public final Map<String, Integer> teamTotalScoresAllowed;
    public final Map<String, Integer> teamTotalShotsMade;
    public final Map<String, Integer> teamTotalThreeMade;
    public final Map<String, Integer> teamTotalShotsAttempted;
    public final Map<String, Integer> teamTotalThreeAttempted;

    // team per-game stats table
    public final Map<String, Double> teamPerScores;
    public final Map<String, Double> teamPerScoresAllowed;
    public final Map<String, Double> teamPerShotsMade;
    public final Map<String, Double> teamPerShotsPercent;
    public final Map<String, Double> teamPerThreePercent;

    /**
     * Construct a SeasonStats object to store all player stats.
     */
    public SeasonStats() {
        playerTotalGames = new HashMap<>();
        playerTotalScores = new HashMap<>();
        playerTotalRebs = new HashMap<>();
        playerTotalAsts = new HashMap<>();
        playerTotalStls = new HashMap<>();
        playerTotalBlks = new HashMap<>();
        playerTotalThrees = new HashMap<>();
        playerTotalFts = new HashMap<>();

        playerPerScores = new HashMap<>();
        playerPerRebs = new HashMap<>();
        playerPerAsts = new HashMap<>();
        playerPerStls = new HashMap<>();
        playerPerBlks = new HashMap<>();
        playerPerThrees = new HashMap<>();
        playerPerFts = new HashMap<>();

        teamTotalGames = new HashMap<>();
        teamTotalScores = new HashMap<>();
        teamTotalScoresAllowed = new HashMap<>();
        teamTotalShotsMade = new HashMap<>();
        teamTotalThreeMade = new HashMap<>();
        teamTotalShotsAttempted = new HashMap<>();
        teamTotalThreeAttempted = new HashMap<>();

        teamPerScores = new HashMap<>();
        teamPerScoresAllowed = new HashMap<>();
        teamPerShotsMade = new HashMap<>();
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
        int ast = p.assist;
        int stl = p.steal;
        int blk = p.block;
        int three = p.threeMade;
        int ft = p.freeThrowMade;

        // update total stats
        playerTotalGames.put(name, playerTotalGames.getOrDefault(name, 0) + 1);
        playerTotalScores.put(name, playerTotalScores.getOrDefault(name, 0) + score);
        playerTotalRebs.put(name, playerTotalRebs.getOrDefault(name, 0) + reb);
        playerTotalAsts.put(name, playerTotalAsts.getOrDefault(name, 0) + ast);
        playerTotalStls.put(name, playerTotalStls.getOrDefault(name, 0) + stl);
        playerTotalBlks.put(name, playerTotalBlks.getOrDefault(name, 0) + blk);
        playerTotalThrees.put(name, playerTotalThrees.getOrDefault(name, 0) + three);
        playerTotalFts.put(name, playerTotalFts.getOrDefault(name, 0) + ft);

        // update per-game stats
        playerPerScores.put(name, Utilities.roundDouble(playerTotalScores.get(name) * 1.0 / playerTotalGames.get(name)));
        playerPerRebs.put(name, Utilities.roundDouble(playerTotalRebs.get(name) * 1.0 / playerTotalGames.get(name)));
        playerPerAsts.put(name, Utilities.roundDouble(playerTotalAsts.get(name) * 1.0 / playerTotalGames.get(name)));
        playerPerStls.put(name, Utilities.roundDouble(playerTotalStls.get(name) * 1.0 / playerTotalGames.get(name)));
        playerPerBlks.put(name, Utilities.roundDouble(playerTotalBlks.get(name) * 1.0 / playerTotalGames.get(name)));
        playerPerThrees.put(name, Utilities.roundDouble(playerTotalThrees.get(name) * 1.0 / playerTotalGames.get(name)));
        playerPerFts.put(name, Utilities.roundDouble(playerTotalFts.get(name) * 1.0 / playerTotalGames.get(name)));
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
        int shotsAttempted = t.totalShotAttempted;
        int threeAttempted = t.total3Attempted;

        // update total stats
        teamTotalGames.put(name, teamTotalGames.getOrDefault(name, 0) + 1);
        teamTotalScores.put(name, teamTotalScores.getOrDefault(name, 0) + score);
        teamTotalScoresAllowed.put(name, teamTotalScoresAllowed.getOrDefault(name, 0) + scoreAllowed);
        teamTotalShotsMade.put(name, teamTotalShotsMade.getOrDefault(name, 0) + shotsMade);
        teamTotalThreeMade.put(name, teamTotalThreeMade.getOrDefault(name, 0) + threeMade);
        teamTotalShotsAttempted.put(name, teamTotalShotsAttempted.getOrDefault(name, 0) + shotsAttempted);
        teamTotalThreeAttempted.put(name, teamTotalThreeAttempted.getOrDefault(name, 0) + threeAttempted);

        // update per-game stats
        teamPerScores.put(name, Utilities.roundDouble(teamTotalScores.get(name) * 1.0 / teamTotalGames.get(name)));
        teamPerScoresAllowed.put(name, Utilities.roundDouble(teamTotalScoresAllowed.get(name) * 1.0 / teamTotalGames.get(name)));
        teamPerShotsMade.put(name, Utilities.roundDouble(teamTotalShotsMade.get(name) * 1.0 / teamTotalGames.get(name)));
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
     * Print the rank of a player stat table
     * 
     * @param table A stat table
     */
    public void printPlayerRank(Map<String, Double> table) {
        // current rank and total rank numbers
        int MAX_RANK = 100;
        int rank = 1;

        String name;
        double score;
        double reb;
        double ast;
        double stl;
        double blk;

        for (Map.Entry<String, Double> player : sortStats(table)) {
            // output each player's basic 5 stats, except for three mades per game ranking or turnovers per game
            if (!table.equals(playerPerThrees) && !table.equals(playerPerFts)) {
                name = player.getKey();
                score = playerPerScores.get(name);
                reb = playerPerRebs.get(name);
                ast = playerPerAsts.get(name);
                stl = playerPerStls.get(name);
                blk = playerPerBlks.get(name);

                System.out.println(rank + " " + name + " " + score + "分 " + reb + "板 " + ast + "助 " + 
                                   + stl + "断 " + blk + "帽");
            } else {
                System.out.println(rank + " " + player.getKey() + "  " + player.getValue());
            }

            rank++;
            if (rank > MAX_RANK) break;
        }
    }

    /**
     * Print the rank of a player stat table
     * 
     * @param table A stat table
     */
    public void printTeamRank(Map<String, Double> table) {
        // current rank and total rank numbers
        int MAX_RANK = 30;
        int rank = 1;

        for (Map.Entry<String, Double> team : sortStats(table)) {
            System.out.println(rank + " " + team.getKey() + "  " + team.getValue());

            rank++;
            if (rank > MAX_RANK) break;
        }
    }
}
