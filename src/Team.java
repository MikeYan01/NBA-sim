package src;

import java.util.*;
import java.io.*;

public class Team {
    // team name
    public String name;

    // team stats in a game
    public int totalScore;
    public int totalRebound;
    public int totalAssist;
    public int totalSteal;
    public int totalBlock;
    public int totalFoul;
    public int totalTurnover;
    public int totalShotMade;
    public int totalShotAttempted;
    public int total3Made;
    public int total3Attempted;
    public int totalFreeMade;
    public int totalFreeAttempted;
    public int totalScoreAllowed;

    // total num of fouls in current quarter
    public int quarterFoul = 0;

    // whether the team possess the ball in the next play
    public boolean hasBall;

    // whether the team is able to challenge the foul or not
    public boolean canChallenge;

    // all players in this teams
    public final List<Player> players = new ArrayList<Player>();

    public final Map<String, Player> starters = new HashMap<>(); // starting lineup
    public final Map<String, List<Player>> benches = new HashMap<>(); // normal bench
    public final Map<String, List<Player>> rareBenches = new HashMap<>(); // bench that rarely show up

    /**
     * Construct a Team object, which can be conceived as an NBA team.
     * 
     * @param name The team's name
     */
    public Team(String name) {
        this.name = name;

        this.totalScore = 0;
        this.totalRebound = 0;
        this.totalAssist = 0;
        this.totalSteal = 0;
        this.totalBlock = 0;
        this.totalFoul = 0;
        this.totalTurnover = 0;
        this.totalShotMade = 0;
        this.totalShotAttempted = 0;
        this.total3Made = 0;
        this.total3Attempted = 0;
        this.totalFreeMade = 0;
        this.totalFreeAttempted = 0;
        this.totalScoreAllowed = 0;

        this.quarterFoul = 0;

        this.hasBall = false;
        this.canChallenge = true;

        loadPlayers(name, players, starters, benches, rareBenches);
    }

    /**
     * Load team rosters files and generate all Players objects of the team.
     * 
     * @param name The team's name
     * @param players Starting lineup list
     * @param benches Normal benches map
     * @param rareBenches Rarely-appeared benches map
     */
    public static void loadPlayers(String name, List<Player> players, Map<String, Player> starters, Map<String, List<Player>> benches,
                                   Map<String, List<Player>> rareBenches) {
        String filePath = Constants.ROSTER_PATH + name + Constants.ROSTER_EXTENSION;

        try (BufferedReader file = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"))) {
            String record;
            Boolean isFirst = true;
            while ((record = file.readLine()) != null) {
                // skip first header line
                if (isFirst) {
                    isFirst = false;
                    continue;
                }

                // get current player's ratings
                String[] attributes = record.split(",");
                String currentPos = attributes[1];
                String currentRotationType = attributes[3];
                
                Player player = new Player(attributes[0], attributes[1], attributes[2], attributes[3], attributes[4], attributes[5],
                                           attributes[6], attributes[7], attributes[8],attributes[9], attributes[10], attributes[11],
                                           attributes[12], attributes[13], attributes[14], attributes[15], attributes[16], attributes[17],
                                           attributes[18], attributes[19], attributes[20], attributes[21], attributes[22], attributes[23],
                                           attributes[24]);
                
                // add player to the team player list
                players.add(player);

                // add player based on rotationType
                if (currentRotationType.equals("1")) {
                    starters.put(currentPos, player);
                    player.hasBeenOnCourt = true;
                } else if (currentRotationType.equals("2")) {
                    if (!benches.containsKey(currentPos)) {
                        List<Player> posList = new ArrayList<>();
                        posList.add(player);
                        benches.put(currentPos, posList);
                    } else {
                        benches.get(currentPos).add(player);
                    }
                } else if (currentRotationType.equals("3")) {
                    if (!rareBenches.containsKey(currentPos)) {
                        List<Player> posList = new ArrayList<>();
                        posList.add(player);
                        rareBenches.put(currentPos, posList);
                    } else {
                        rareBenches.get(currentPos).add(player);
                    }
                }
            }

            // sort benches by general rating in descending order
            for (String pos : benches.keySet()) {
                Collections.sort(benches.get(pos), (o1, o2) -> { return o2.rating - o1.rating; });
            }
        } catch (Exception e) {}
    }
}
