package src;

import java.util.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Utilities {
    /**
     * Generate a random number from range [min, max].
     * 
     * @param random The Random object to generate random number
     * @param min The lower bound of the range
     * @param max The upper bound of the range
     * @return A random number
     */
    public static int generateRandomNum(Random random, int min, int max) {
        return random.nextInt(max) % (max - min + 1) + min;
    }

    /**
     * Round a double number to 2 scale.
     * 
     * @param num The double number to be rounded
     * @return A rounded number
     */
    public static double roundDouble(double num) {
        BigDecimal b = new BigDecimal(num);
        double result = b.setScale(2, RoundingMode.HALF_UP).doubleValue();
        return result;
    }

    /**
     * Generate current play's time from range [1, 24] in seconds.
     * 
     * @return Play time
     */
    public static int generateRandomPlayTime(Random random) {
        int currentPlayTime = generateRandomNum(random, 4, 24);
        if (currentPlayTime <= 9 && generateRandomNum(random, 1, 10) <= 8) currentPlayTime += 8;
        if (currentPlayTime >= 17 && generateRandomNum(random, 1, 10) <= 8) currentPlayTime -= 6;
        return currentPlayTime;
    }

    /**
     * Select a player based on rating.
     * 
     * @param TeamOnCourt Current players on the court
     * @param attr The rating criteria, including 'rating', 'orb', 'drb' and 'ast'
     * @return Selected player
     */
    public static Player choosePlayerBasedOnRating(Random random, Map<String, Player> TeamOnCourt, String attr) {
        int totalRating = 0;
        for (String pos : TeamOnCourt.keySet()) {
            if (attr.equals("rating")) 
                totalRating += (0.65*TeamOnCourt.get(pos).rating + 0.07*TeamOnCourt.get(pos).astRating + 0.07*TeamOnCourt.get(pos).insideRating
                                + 0.07*TeamOnCourt.get(pos).midRating + 0.07*TeamOnCourt.get(pos).threeRating + 0.07*TeamOnCourt.get(pos).layupRating);
            if (attr.equals("orb")) totalRating += 0.1*TeamOnCourt.get(pos).rating + 0.9*TeamOnCourt.get(pos).orbRating;
            if (attr.equals("drb")) totalRating += 0.1*TeamOnCourt.get(pos).rating + 0.9*TeamOnCourt.get(pos).drbRating;
            if (attr.equals("ast")) totalRating += TeamOnCourt.get(pos).astRating;
        }
        double avgRating = totalRating * 1.0 / 5;

        int poss1, poss2, poss3, poss4 = 0;
        if (attr.equals("rating")) {
            poss1 = (int)(10 * (20 + (0.65*TeamOnCourt.get("C").rating + 0.07*TeamOnCourt.get("C").astRating + 0.07*TeamOnCourt.get("C").insideRating
                    + 0.07*TeamOnCourt.get("C").midRating + 0.07*TeamOnCourt.get("C").threeRating + 0.07*TeamOnCourt.get("C").layupRating - avgRating) ));
            
            poss2 = (int)(10 * (20 + (0.65*TeamOnCourt.get("PF").rating + 0.07*TeamOnCourt.get("PF").astRating + 0.07*TeamOnCourt.get("PF").insideRating
                + 0.07*TeamOnCourt.get("PF").midRating + 0.07*TeamOnCourt.get("PF").threeRating + 0.07*TeamOnCourt.get("PF").layupRating - avgRating) ));
            
            poss3 = (int)(10 * (20 + (0.65*TeamOnCourt.get("SF").rating + 0.07*TeamOnCourt.get("SF").astRating + 0.07*TeamOnCourt.get("SF").insideRating
                + 0.07*TeamOnCourt.get("SF").midRating + 0.07*TeamOnCourt.get("SF").threeRating + 0.07*TeamOnCourt.get("SF").layupRating - avgRating) ));
            
            poss4 = (int)(10 * (20 + (0.65*TeamOnCourt.get("SG").rating + 0.07*TeamOnCourt.get("SF").astRating + 0.07*TeamOnCourt.get("SG").insideRating
                + 0.07*TeamOnCourt.get("SG").midRating + 0.07*TeamOnCourt.get("SG").threeRating + 0.07*TeamOnCourt.get("SG").layupRating - avgRating) ));
        }
        else if (attr.equals("orb")) {
            poss1 = Math.max( (int)(1000 * (2*TeamOnCourt.get("C").orbRating - avgRating) / totalRating), 0);
            poss2 = Math.max( (int)(1000 * (2*TeamOnCourt.get("PF").orbRating - avgRating) / totalRating), 0);
            poss3 = Math.max( (int)(1000 * (2*TeamOnCourt.get("SF").orbRating - avgRating) / totalRating), 0);
            poss4 = Math.max( (int)(1000 * (2*TeamOnCourt.get("C").orbRating - avgRating) / totalRating), 0);
        }
        else if (attr.equals("drb")) {
            poss1 = Math.max( (int)(1000 * (2*TeamOnCourt.get("C").drbRating - avgRating) / totalRating), 0) ;
            poss2 = Math.max( (int)(1000 * (2*TeamOnCourt.get("PF").drbRating - avgRating) / totalRating), 0);
            poss3 = Math.max( (int)(1000 * (2*TeamOnCourt.get("SF").drbRating - avgRating) / totalRating), 0);
            poss4 = Math.max( (int)(1000 * (2*TeamOnCourt.get("SG").drbRating - avgRating) / totalRating), 0);
        }
        else {
            poss1 = Math.max( (int)(1000 * (2*TeamOnCourt.get("C").astRating - avgRating) / totalRating), 0);
            poss2 = Math.max( (int)(1000 * (2*TeamOnCourt.get("PF").astRating - avgRating) / totalRating), 0);
            poss3 = Math.max( (int)(1000 * (2*TeamOnCourt.get("SF").astRating - avgRating) / totalRating), 0);
            poss4 = Math.max( (int)(1000 * (2*TeamOnCourt.get("SG").astRating - avgRating) / totalRating), 0);
        }
        
        int pick = generateRandomNum(random, 1, 1000);
        if (pick <= poss1) return TeamOnCourt.get("C");
        else if (pick <= poss1 + poss2) return TeamOnCourt.get("PF");
        else if (pick <= poss1 + poss2 + poss3) return TeamOnCourt.get("SF");
        else if (pick <= poss1 + poss2 + poss3 + poss4) return TeamOnCourt.get("SG");
        else return TeamOnCourt.get("PG");
    }

    /**
     * Select a player to defense the offense player.
     * 
     * @param offensePlayer Offense player
     * @param defenseTeamOnCourt Current defense players on the court
     * @return Selected player
     */
    public static Player chooseDefensePlayer(Random random, Player offensePlayer, Map<String, Player> defenseTeamOnCourt) {
        int poss = generateRandomNum(random, 1, 100);

        String offensePos = offensePlayer.position;
        List<String> otherPos = new ArrayList<>();
        for (String pos : defenseTeamOnCourt.keySet()) 
            if (pos != offensePos) otherPos.add(pos);
        
        if (poss <= 60) return defenseTeamOnCourt.get(offensePos);
        else if (poss <= 70) return defenseTeamOnCourt.get( otherPos.get(0) );
        else if (poss <= 80) return defenseTeamOnCourt.get( otherPos.get(1) );
        else if (poss <= 90) return defenseTeamOnCourt.get( otherPos.get(2) );
        else return defenseTeamOnCourt.get( otherPos.get(3) );
    }

    /**
     * Generate actions after losing ball.
     * 
     * @param defenseTeam Defense team
     * @param offensePlayer Offense player
     * @param defensePlayer Defense player
     * @param defenseTeamOnCourt Current defense players on the court
     * @return 0 - no lose ball, 1 - lose ball but no score, 2 - loss ball and score
     */
    public static int judgeLoseBall(Random random, Team defenseTeam, Map<String, Player> defenseTeamOnCourt, Player offensePlayer, Player defensePlayer) {
        int poss = generateRandomNum(random, 1, 100);

        double range = 6 * 70 + 5 * defensePlayer.stlRating + defensePlayer.athleticism;
        if (defensePlayer.stlRating >= 80 && defensePlayer.stlRating < 90) range *= 1.15;
        else if (defensePlayer.stlRating >= 90) range *= 1.3;

        // turnover
        if (poss <= 5) {
            offensePlayer.turnover++;
            Comments.getTurnoverComment(offensePlayer.name);
            return 1;
        }

        // steal 
        else if (60 * poss <= range) {
            offensePlayer.turnover++;
            defensePlayer.steal++;
            Comments.getStealComment(offensePlayer.name, defensePlayer.name);

            // 30% chance to start a non-fast-break play
            int fastBreak = generateRandomNum(random, 1, 100);
            if (fastBreak <= 30) {
                Comments.getNonFastBreak(defenseTeam.name);
                return 1;
            }

            // 70% change to start a fast break
            else {
                int fastBreakTemp = generateRandomNum(random, 1, 100);
                Player fastBreakFinisher;
                // 60% chance to finish by himself
                if (fastBreakTemp <= 60) fastBreakFinisher = defensePlayer;
                // 40% chance to finish by teammate
                else {
                    List<String> otherTeammate = new ArrayList<>();
                    for (String pos : defenseTeamOnCourt.keySet()) 
                        if (pos != defensePlayer.position) otherTeammate.add(pos);

                    if (poss <= 70) fastBreakFinisher = defenseTeamOnCourt.get( otherTeammate.get(0) );
                    else if (poss <= 80) fastBreakFinisher = defenseTeamOnCourt.get( otherTeammate.get(1) );
                    else if (poss <= 90) fastBreakFinisher = defenseTeamOnCourt.get( otherTeammate.get(2) );
                    else fastBreakFinisher = defenseTeamOnCourt.get( otherTeammate.get(3) );
                }

                Comments.getFastBreak(defenseTeam.name, fastBreakFinisher.name);
                defenseTeam.totalScore += 2;
                fastBreakFinisher.score += 2;
                fastBreakFinisher.shotMade++;
                fastBreakFinisher.shotAttempted++;
            }
            return 2;
        }
        return 0;
    }

    /**
     * Generate actions after a block.
     * 
     * @param distance Shot distance
     * @param offensePlayer Offense player
     * @param defensePlayer Defense player
     * @param offenseTeamOnCourt Current offense players on the court
     * @param defenseTeamOnCourt Current defense players on the court
     * @return 0 - no block  1 - block and offensive rebound  2 - block and defensive rebound
     */
    public static int judgeBlock(Random random, int distance, Map<String, Player> offenseTeamOnCourt, Map<String, Player> defenseTeamOnCourt,
                                 Player offensePlayer, Player defensePlayer) {
        int poss = generateRandomNum(random, 1, 100);
        double range = 6 * 20 + 5 * defensePlayer.blkRating + defensePlayer.athleticism;
        if (defensePlayer.blkRating >= 80 && defensePlayer.blkRating < 90) range *= 1.25;
        else if (defensePlayer.blkRating >= 90) range *= 1.5;

        if (60 * poss <= range) {
            offensePlayer.shotAttempted++;
            if (distance >= 24) offensePlayer.threeAttempted++;
            defensePlayer.block++;
            Comments.getBlockComment(defensePlayer.name);

            // 40% chance to out of bound
            int outOfBound = generateRandomNum(random, 1, 100);
            if (outOfBound <= 40) {
                Comments.getOutOfBound(defensePlayer.name);
                return 1;
            }

            // 60% change to go to rebound juding
            else {
                boolean stillOffense = judgeRebound(random, offenseTeamOnCourt, defenseTeamOnCourt);
                return stillOffense ? 1 : 2;
            }
        }
        return 0;
    }

    /**
     * Generate actions when two teams fight for a rebound.
     * 
     * @param offenseTeamOnCourt Current offense players on the court
     * @param defenseTeamOnCourt Current defense players on the court
     * @return true - offensive rebound, false - defensive rebound
     */
    public static boolean judgeRebound(Random random, Map<String, Player> offenseTeamOnCourt, Map<String, Player> defenseTeamOnCourt) {
        int offenseTeamReb = 0, defenseTeamReb = 0;
        for (String pos : offenseTeamOnCourt.keySet()) 
            offenseTeamReb += offenseTeamOnCourt.get(pos).orbRating;
        for (String pos : defenseTeamOnCourt.keySet()) 
            defenseTeamReb += defenseTeamOnCourt.get(pos).drbRating;

        boolean offRebBonus = offenseTeamReb > defenseTeamReb ? true : false;
        
        int temp = generateRandomNum(random, 1, 100);
        
        // offensive rebound
        if ((offRebBonus && temp <= 15) || (!offRebBonus && temp <= 10)) {
            Player offenseRebounder = choosePlayerBasedOnRating(random, offenseTeamOnCourt, "orb");
            Comments.getReboundComment(offenseRebounder.name, true);
            offenseRebounder.rebound++;
            return true;
        }
        // defensive rebound
        else {
            Player defenseRebounder = choosePlayerBasedOnRating(random, defenseTeamOnCourt, "drb");
            Comments.getReboundComment(defenseRebounder.name, false);
            defenseRebounder.rebound++;
            return false;
        }
    }

    /**
     * Generate actions when a player gets fouled out.
     * 
     * @param team Team having this player
     * @param previousPlayer The player getting fouled out
     * @param teamOnCourt Players on the court
     * @return 0 - no lose ball, 1 - lose ball but no score, 2 - loss ball and score
     */
    public static void judgeFoulOut(Player previousPlayer, Team team, Map<String, Player> teamOnCourt) {
        if (previousPlayer.foul == 6 || previousPlayer.flagFoul == 2) {
            Comments.getFoulOutComment(previousPlayer.name, previousPlayer.foul == 6 ? true : false);
            previousPlayer.canOnCourt = false;

            Player currentPlayer = findSubPlayer(previousPlayer, team);
            teamOnCourt.put(previousPlayer.position, currentPlayer);
            Comments.getSubstituteComment(currentPlayer.name, previousPlayer.name);
        }
    }

    /**
     * Generate actions after a normal foul.
     * 
     * @param distance Shot distance
     * @param offensePlayer Offense player
     * @param defensePlayer Defense player
     * @param offenseTeam Offense team
     * @param defenseTeam Defense team
     * @param offenseTeamOnCourt Current offense players on the court
     * @param defenseTeamOnCourt Current defense players on the court
     * @param currentQuarter Current quarter number
     * @param quarterTime Times left in current quarter
     * @return 0 - no foul, 1 - offensive foul, 2 - defensive foul
     */
    public static int judgeNormalFoul(Random random, Map<String, Player> offenseTeamOnCourt, Map<String, Player> defenseTeamOnCourt,
                                      Player offensePlayer, Player defensePlayer, Team offenseTeam, Team defenseTeam, int currentQuarter,
                                      int quarterTime, Team team1, Team team2) {
        int poss = generateRandomNum(random, 1, 200);

        // offensive foul
        if (poss <= 3) {
            int foulTemp = generateRandomNum(random, 1, 100);
            Player fouler;

            // 40% chance to foul on offensePlayer, 60% on teammate
            if (foulTemp <= 40) {
                fouler = offensePlayer;
                Comments.getOffensiveFoul(fouler.name, 1);
            } else {
                List<String> otherTeammate = new ArrayList<>();
                for (String pos : offenseTeamOnCourt.keySet()) 
                    if (pos != offensePlayer.position) otherTeammate.add(pos);

                if (poss <= 70) fouler = offenseTeamOnCourt.get( otherTeammate.get(0) );
                else if (poss <= 80) fouler = offenseTeamOnCourt.get( otherTeammate.get(1) );
                else if (poss <= 90) fouler = offenseTeamOnCourt.get( otherTeammate.get(2) );
                else fouler = offenseTeamOnCourt.get( otherTeammate.get(3) );
                Comments.getOffensiveFoul(fouler.name, 2);
            }

            fouler.turnover++;
            fouler.foul++;
            judgeFoulOut(fouler, offenseTeam, offenseTeamOnCourt);
            return 1;
        }

        // defensive foul
        else if (poss <= 6) {
            int foulTemp = generateRandomNum(random, 1, 100);
            Player fouler;

            // 80% chance to foul on defensePlayer, 20% on teammate
            if (foulTemp <= 80) {
                fouler = defensePlayer;
                Comments.getDefensiveFoul(fouler.name, 1);
            } else {
                List<String> otherTeammate = new ArrayList<>();
                for (String pos : defenseTeamOnCourt.keySet()) 
                    if (pos != defensePlayer.position) otherTeammate.add(pos);

                if (poss <= 85) fouler = defenseTeamOnCourt.get( otherTeammate.get(0) );
                else if (poss <= 90) fouler = defenseTeamOnCourt.get( otherTeammate.get(1) );
                else if (poss <= 95) fouler = defenseTeamOnCourt.get( otherTeammate.get(2) );
                else fouler = defenseTeamOnCourt.get( otherTeammate.get(3) );
                Comments.getDefensiveFoul(fouler.name, 2);
            }

            fouler.foul++;
            judgeFoulOut(fouler, defenseTeam, defenseTeamOnCourt);

            defenseTeam.quarterFoul++;
            if (defenseTeam.quarterFoul >= 5) {
                Comments.getReachFoulTimes(offenseTeam.name, defenseTeam.name);

                makeFreeThrow(random, offensePlayer, offenseTeamOnCourt, defenseTeamOnCourt, offenseTeam,
                              2, quarterTime, currentQuarter, team1, team2, false);
            }
            return 2;
        }
        return 0;
    }

    /**
     * Generates actions when jumping ball.
     */
    public static void jumpBall(Random random, Team team1, Team team2) {
        Team winTeam = Utilities.generateRandomNum(random, 1, 100) <= 50 ? team1 : team2;
        winTeam.hasBall = true;
        Comments.getJumpBallComments(team1, team2, winTeam);
    }

    /**
     * Find a player to substitute another teammate on the court.
     * 
     * @param previousPlayer The player to be substituted
     * @param team The team making substitution
     * @return The incoming Player 
     */
    public static Player findSubPlayer(Player previousPlayer, Team team) {
        Player currentPlayer = null;

        if (previousPlayer.rotationType == 1) {
            if (team.benches.get( previousPlayer.position ).get(0).canOnCourt)
                currentPlayer = team.benches.get( previousPlayer.position ).get(0);

            if (currentPlayer == null && team.rareBenches.containsKey(previousPlayer.position)) {
                if (team.rareBenches.get( previousPlayer.position ).get(0).canOnCourt)
                    currentPlayer = team.rareBenches.get( previousPlayer.position ).get(0);
            }
        } else if (previousPlayer.rotationType == 2) {
            if (team.starters.get( previousPlayer.position ).canOnCourt)
                currentPlayer = team.starters.get( previousPlayer.position );

            if (currentPlayer == null && team.rareBenches.containsKey(previousPlayer.position)) {
                if (team.rareBenches.get( previousPlayer.position ).get(0).canOnCourt)
                    currentPlayer = team.rareBenches.get( previousPlayer.position ).get(0);
            }
        } else if (previousPlayer.rotationType == 3) {
            if (team.starters.get( previousPlayer.position ).canOnCourt)
                currentPlayer = team.starters.get( previousPlayer.position );


            if (currentPlayer == null && team.benches.containsKey(previousPlayer.position)) {
                if (team.benches.get( previousPlayer.position ).get(0).canOnCourt)
                    currentPlayer = team.benches.get( previousPlayer.position ).get(0);
            }
        }

        if (currentPlayer == null) currentPlayer = previousPlayer;
        return currentPlayer;
    }

    /**
     * Generate actions during a timeout.
     * 
     * @param subBench Whether substitution is starter -> bench or bench -> starter
     * @param teamOneOnCourt Team 1's players on the court
     * @param teamTwoOnCourt Team 2's players on the court
     * @param garbageFlag Whether rareBenches have been substituted
     */
    public static void timeOutSub(Random random, Team team1, Team team2, boolean subBench, Map<String, Player> teamOneOnCourt,
                                  Map<String, Player> teamTwoOnCourt, boolean garbageFlag) {
        String currentPossess = team1.hasBall ? team1.name : team2.name;
        Comments.getTimeOutComment(currentPossess);

        // starter -> bench
        if (subBench) {
            for (String pos : team1.benches.keySet()) {
                Player previousPlayer = teamOneOnCourt.get(pos);
                Player currentPlayer = null;

                if (garbageFlag && team1.rareBenches.containsKey(pos) && team1.rareBenches.get(pos).get(0).canOnCourt)
                    currentPlayer = team1.rareBenches.get(pos).get(0);
                else currentPlayer = team1.benches.get(pos).get(0);

                Comments.getSubstituteComment(currentPlayer.name, previousPlayer.name);
                teamOneOnCourt.put(pos, currentPlayer);

                // current player is ready to be on court
                currentPlayer.hasBeenOnCourt = true;
            }
            
            for (String pos : team2.benches.keySet()) {
                Player previousPlayer = teamTwoOnCourt.get(pos);
                Player currentPlayer = null;

                if (garbageFlag && team2.rareBenches.containsKey(pos) && team2.rareBenches.get(pos).get(0).canOnCourt)
                    currentPlayer = team2.rareBenches.get(pos).get(0);
                else currentPlayer = team2.benches.get(pos).get(0);

                Comments.getSubstituteComment(currentPlayer.name, previousPlayer.name);
                teamTwoOnCourt.put(pos, currentPlayer);

                // current player is ready to be on court
                currentPlayer.hasBeenOnCourt = true;
            }
        }

        // bench -> starter
        else {
            for (String pos : team1.benches.keySet()) {
                Player previousPlayer = teamOneOnCourt.get(pos);
                Player currentPlayer = findSubPlayer(previousPlayer, team1);

                Comments.getSubstituteComment(currentPlayer.name, previousPlayer.name);
                teamOneOnCourt.put(pos, currentPlayer);

                // current player is ready to be on court
                currentPlayer.hasBeenOnCourt = true;
            }
            for (String pos : team2.benches.keySet()) {
                Player previousPlayer = teamTwoOnCourt.get(pos);
                Player currentPlayer = findSubPlayer(previousPlayer, team2);

                Comments.getSubstituteComment(currentPlayer.name, previousPlayer.name);
                teamTwoOnCourt.put(pos, currentPlayer);

                // current player is ready to be on court
                currentPlayer.hasBeenOnCourt = true;
            }
        }
    }

    /**
     * Generate shooting distance by player type.
     * 
     * @param offensePlayer Offense player
     * @return Shot distance
     */
    public static int getShotDistance(Random random, Player offensePlayer) {
        int distance = 0;
        switch (offensePlayer.playerType) {
            case 1:
                distance = generateRandomNum(random, 1, 30);
                break;
            case 2:
                distance = generateRandomNum(random, 1, 12);
                break;
            case 3:
                distance = generateRandomNum(random, 1, 25);
                break;
            case 4:
                int temp1 = generateRandomNum(random, 1, 100);
                if (temp1 <= 35) distance = generateRandomNum(random, 1, 10);
                else if (temp1 <= 45) distance = generateRandomNum(random, 11, 23);
                else distance = generateRandomNum(random, 24, 30);
                break;
            case 5:
                int temp2 = generateRandomNum(random, 1, 100);
                if (temp2 <= 15) distance = generateRandomNum(random, 1, 20);
                else distance = generateRandomNum(random, 21, 30);
                break;
            default:
                break;
        }
        if (distance >= 27 && generateRandomNum(random, 1, 100) <= 80) distance -= 3;
        return distance;
    }

    /**
     * Calculate shot goal percentage in double.
     * 
     * @param distance Shot distance
     * @param offensePlayer Offense player
     * @param defensePlayer Defense player
     * @param offenseTeamOnCourt Current offense players on the court
     * @param currentQuarter Current quarter number
     * @param quarterTime Times left in current quarter
     * @param movement Shot choice string
     * @return Shot goal percentage
     */
    public static double calculatePercentage(Random random, int distance, Player offensePlayer, Player defensePlayer,
                                             Map<String, Player> offenseTeamOnCourt, String movement, int quarterTime,
                                             int currentQuarter, Team team1, Team team2) {
        double percentage = 0.0;

        // initial value
        if (distance <= 20) percentage = -0.5 * distance + 38;
        else if (distance <= 23) percentage = distance + 8;
        else percentage = -23/35 * (distance - 24) * (distance - 24) + 1353/35;

        // based on shot choice, adjust percentage
        if (movement.contains("扣")) percentage *= 2.5;
        else if (movement.contains("篮")) percentage += 0.25 * offensePlayer.layupRating;
        else {
            if (distance <= 10) percentage += 0.25 * (offensePlayer.insideRating - 80);
            else if (distance <= 23) percentage += 0.25 * (offensePlayer.midRating - 75);
            else percentage += 0.2 * (offensePlayer.threeRating - 75);
        }

        // players with high astRating will increase percentage
        for (String pos : offenseTeamOnCourt.keySet()) {
            if (pos != offensePlayer.position && offenseTeamOnCourt.get(pos).astRating >= 83) {
                if (offenseTeamOnCourt.get(pos).astRating <= 87) percentage += 1;
                else if (offenseTeamOnCourt.get(pos).astRating <= 93) percentage += 2;
                else percentage += 3;
                
                break;
            }
        }

        // based on defender, adjust percentage
        if (distance <= 10) percentage -= 0.3 * (defensePlayer.interiorDefense - 40);
        else percentage -= 0.3 * (defensePlayer.perimeterDefense - 40);

        // check defense density
        int temp = generateRandomNum(random, 1, 100);
        if (!offensePlayer.isStar) {
            if (temp <= 25) percentage += 10;
            else if (temp > 65) percentage -= 10;
        }
        else {
            if (temp <= 20) percentage += 10;
            else if (temp > 60) percentage -= 10;
        }
        percentage += 0.25 * (99 - defensePlayer.defConst); // defense player's defensive consistency

        // star player bonus
        if (offensePlayer.rating >= 83 && offensePlayer.rating <= 86) percentage *= 1.03;
        else if (offensePlayer.rating >= 87 && offensePlayer.rating <= 89) percentage *= 1.06;
        else if (offensePlayer.rating >= 90 && offensePlayer.rating <= 93) percentage *= 1.09;
        else if (offensePlayer.rating >= 94) percentage *= 1.12;

        // clutch time
        if (!offensePlayer.isMrClutch && currentQuarter >= 4 && Math.abs(team1.totalScore - team2.totalScore) <= 5) percentage *= 0.8;

        // offensive consistency
        percentage -= 0.25 * (99 - offensePlayer.offConst);

        // athleticism
        percentage += (offensePlayer.athleticism - defensePlayer.athleticism) / 7;
        
        return percentage;
    }

    /**
     * Generate actions after a player makes a shot.
     * 
     * @param distance Shot distance
     * @param offensePlayer Offense player
     * @param defensePlayer Defense player
     * @param offenseTeam Offense team
     * @param defenseTeam Defense team
     * @param offenseTeamOnCourt Current offense players on the court
     * @param defenseTeamOnCourt Current defense players on the court
     * @param currentQuarter Current quarter number
     * @param quarterTime Times left in current quarter
     * @param movement Shot choice string
     * @param percentage Shot goal percentage
     * @return 1 - make the shot or make the last free throw  2 - offensive rebound  3 - defensive rebound
     */
    public static int judgeMakeShot(Random random, int distance, Player offensePlayer, Player defensePlayer, Team offenseTeam, Team defenseTeam,
                                    Map<String, Player> offenseTeamOnCourt, Map<String, Player> defenseTeamOnCourt, double percentage, int quarterTime,
                                    int currentQuarter, Team team1, Team team2, String movement) {
        int judgeShot = generateRandomNum(random, 1, 10000);

        // make the shot
        if (judgeShot < (int)(100 * percentage)) {
            offensePlayer.shotMade++;
            offensePlayer.shotAttempted++;

            if (distance >= 24) {
                offensePlayer.threeAttempted++;
                offensePlayer.threeMade++;
                offensePlayer.score += 3;
                offenseTeam.totalScore += 3;
            }
            else {
                offensePlayer.score += 2;
                offenseTeam.totalScore += 2;
            }

            Comments.getMakeShotsComment(offensePlayer.name, distance, movement);
            if (generateRandomNum(random, 1, 10) <= 5) Comments.getStatusComment(offensePlayer, true);
            Comments.getTimeAndScore(quarterTime, currentQuarter, team1, team2);

            // get assist directly from 10-cent player
            int firstAst = 0, secondAst = 0;
            for (String pos : offenseTeamOnCourt.keySet()) {
                int currentAst = offenseTeamOnCourt.get(pos).astRating;
                if (currentAst > firstAst) {
                    secondAst = firstAst;
                    firstAst = currentAst;
                } else if (currentAst > secondAst) {
                    secondAst = currentAst;
                }
            }

            int assistAssign = generateRandomNum(random, 1, 100);

            if (assistAssign <= 30) {
                for (String pos : offenseTeamOnCourt.keySet()) {
                    if (pos != offensePlayer.position) {
                        if (offenseTeamOnCourt.get(pos).astRating >= 87 ||
                            (offenseTeamOnCourt.get(pos).astRating == firstAst && assistAssign <= 18)) {
                            offenseTeamOnCourt.get(pos).assist += 1;
                            break;
                        }
                    }
                }
            } else if (assistAssign <= 40) {
                for (String pos : offenseTeamOnCourt.keySet()) {
                    if (pos != offensePlayer.position) {
                        if (offenseTeamOnCourt.get(pos).astRating == secondAst && offensePlayer.astRating == firstAst) {
                            offenseTeamOnCourt.get(pos).assist += 1;
                            break;
                        }
                    }
                }
            } else {
                int astTemp = generateRandomNum(random, 1, 100);
                if ((offensePlayer.isStar && astTemp <= 60) || (!offensePlayer.isStar && astTemp <= 90)) {
                    Player assister;
                    while (true) {
                        assister = choosePlayerBasedOnRating(random, offenseTeamOnCourt, "ast");
                        if (assister.name != offensePlayer.name) break;
                    }
                    assister.assist += 1;
                }
            }

            // judge free throw chance
            int andOneTemp = generateRandomNum(random, 1, 10000);

            int basePercent = distance <= 10 ? 3 : distance <= 23 ? 2 : 1;

            int drawFoulAttr = basePercent * (100 + offensePlayer.drawFoul > 90 ? 9 * offensePlayer.drawFoul / 2
                                                                                : 2 * offensePlayer.drawFoul);
            
            if (offensePlayer.isStar) drawFoulAttr *= 2;

            if (andOneTemp <= drawFoulAttr) {
                defensePlayer.foul++;
                Comments.getAndOneComment(offensePlayer.name);
                judgeFoulOut(defensePlayer, defenseTeam, defenseTeamOnCourt);
                int andOneResult = makeFreeThrow(random, offensePlayer, offenseTeamOnCourt, defenseTeamOnCourt, offenseTeam, 1,
                                                 quarterTime, currentQuarter, team1, team2, false);
                return andOneResult;
            }
            return 1;
        }

        // miss the shot
        else {
            // judge free throw chance
            int foulTemp = generateRandomNum(random, 1, 10000);

            int basePercent = distance <= 10 ? 8 : distance <= 23 ? 5 : 2;
            int drawFoulAttr = basePercent * (100 + offensePlayer.drawFoul > 90 ? 9 * offensePlayer.drawFoul / 2
                                                                                : 2 * offensePlayer.drawFoul);
            if (offensePlayer.isStar) drawFoulAttr *= 2;

            if (foulTemp <= drawFoulAttr) {
                // 5% flag foul
                if (generateRandomNum(random, 1, 100) <= 5) {
                    defensePlayer.flagFoul++;
                    Comments.getFlagFoulComment(offensePlayer.name, defensePlayer.name);
                    judgeFoulOut(defensePlayer, defenseTeam, defenseTeamOnCourt);

                    // two free throws, one shot
                    return makeFreeThrow(random, offensePlayer, offenseTeamOnCourt, defenseTeamOnCourt,
                                         offenseTeam, 2, quarterTime, currentQuarter, team1, team2, true);
                }

                defensePlayer.foul++;
                defenseTeam.quarterFoul++;
                
                Comments.getFoulComment(offensePlayer.name, defensePlayer.name);
                judgeFoulOut(defensePlayer, defenseTeam, defenseTeamOnCourt);

                int freeThrowResult = 0;
                if (distance <= 23) 
                    freeThrowResult = makeFreeThrow(random, offensePlayer, offenseTeamOnCourt, defenseTeamOnCourt,
                                                    offenseTeam, 2, quarterTime, currentQuarter, team1, team2, false);
                else freeThrowResult = makeFreeThrow(random, offensePlayer, offenseTeamOnCourt, defenseTeamOnCourt,
                                                     offenseTeam, 3, quarterTime, currentQuarter, team1, team2, false);

                return freeThrowResult;
            }

            offensePlayer.shotAttempted++;
            if (distance >= 24) offensePlayer.threeAttempted++;
            Comments.getMissShotsComment(movement);
            if (generateRandomNum(random, 1, 10) <= 4) Comments.getStatusComment(offensePlayer, false);

            return judgeRebound(random, offenseTeamOnCourt, defenseTeamOnCourt) ? 2 : 3;
        }
    }

    /**
     * Generate actions after a player makes a free throw.
     * 
     * @param player The player who makes the free throw
     * @param offenseTeam Offense team
     * @param offenseTeamOnCourt Current offense players on the court
     * @param defenseTeamOnCourt Current defense players on the court
     * @param currentQuarter Current quarter number
     * @param quarterTime Times left in current quarter
     * @param times Total free throw times
     * @param isFlagFoul Whether current foul is flagrant foul or not
     * @return 1 - make the last free throw  2 - offensive rebound  3 - defensive rebound
     */
    public static int makeFreeThrow(Random random, Player player, Map<String, Player> offenseTeamOnCourt, Map<String, Player> defenseTeamOnCourt,
                                    Team offenseTeam, int times, int quarterTime, int currentQuarter, Team team1, Team team2, boolean isFlagFoul) {
        
        int timesLeft = times;
        boolean onlyOneShot = timesLeft == 1 ? true : false;
        int count = 0;

        while (timesLeft > 0) {
            timesLeft--;
            count++;

            int freeThrowTemp = generateRandomNum(random, 1, 1000);
            if (freeThrowTemp < player.freeThrowPercent * 10) {
                player.freeThrowAttempted++;
                player.freeThrowMade++;
                player.score++;
                offenseTeam.totalScore++;
                Comments.getMakeFreeThrowComment(count, onlyOneShot);
                Comments.getTimeAndScore(quarterTime, currentQuarter, team1, team2);

                if (timesLeft == 0) return isFlagFoul ? 2 : 1;
            }
            else {
                player.freeThrowAttempted++;
                Comments.getMissFreeThrowComment(count, onlyOneShot);

                if (timesLeft == 0) 
                    return isFlagFoul ? 2
                                      : judgeRebound(random, offenseTeamOnCourt, defenseTeamOnCourt) ? 2 : 3;
            }
        }

        return 0;
    }

    /**
     * Judge whether there is an injury.
     * 
     * @param offenseTeam Offense team
     * @param defenseTeam Defense team
     * @param offenseTeamOnCourt Current offense players on the court
     * @param defenseTeamOnCourt Current defense players on the court
     * @return true - A player gets injured, false - No player gets injured
     */
    public static boolean judgeInjury(Random random, Map<String, Player> offenseTeamOnCourt, Map<String, Player> defenseTeamOnCourt,
                                      Team offenseTeam, Team defenseTeam) {
        for (String pos : offenseTeamOnCourt.keySet()) {
            if (generateRandomNum(random, 1, 1000000) <= 100 + (100 - offenseTeamOnCourt.get(pos).durability)) {
                Player previousPlayer = offenseTeamOnCourt.get(pos);
                Player currentPlayer = findSubPlayer(previousPlayer, offenseTeam);
                previousPlayer.canOnCourt = false;
                offenseTeamOnCourt.put(previousPlayer.position, currentPlayer);
                Comments.getInjuryComment(previousPlayer.name);
                Comments.getSubstituteComment(currentPlayer.name, previousPlayer.name);
                return true;
            }
        }

        for (String pos : defenseTeamOnCourt.keySet()) {
            if (generateRandomNum(random, 1, 1000000) <= 100 + (100 - defenseTeamOnCourt.get(pos).durability)) {
                Player previousPlayer = defenseTeamOnCourt.get(pos);
                Player currentPlayer = findSubPlayer(previousPlayer, defenseTeam);
                previousPlayer.canOnCourt = false;
                defenseTeamOnCourt.put(previousPlayer.position, currentPlayer);
                Comments.getInjuryComment(previousPlayer.name);
                Comments.getSubstituteComment(currentPlayer.name, previousPlayer.name);
                return true;
            }
        }
        
        return false;
    }

    /**
     * Accumulate two teams' quarter scores.
     * 
     * @param team1Scores Team1's all quarter scores
     * @param team2Scores Team2's all quarter scores
     * @param totalScore1 Team1's total scores
     * @param totalScore2 Team2's total scores
     */
    public static void updateQuarterScores(List<Integer> team1Scores, List<Integer> team2Scores, int totalScore1, int totalScore2) {
        team1Scores.add(totalScore1);
        team2Scores.add(totalScore2);
    }
}