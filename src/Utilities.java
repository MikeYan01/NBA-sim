package src;

import java.io.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Utilities {
    
    /**
     * Result enum for judgeLoseBall method
     */
    public enum LoseBallResult {
        NO_LOSE_BALL,           // 0 - continue with possession
        LOSE_BALL_NO_SCORE,     // 1 - turnover, steal without fast break, jump ball loss
        LOSE_BALL_AND_SCORE,    // 2 - steal with successful fast break
        JUMP_BALL_WIN           // 3 - win the jump ball, keep possession
    }
    
    /**
     * Result enum for judgeBlock method
     */
    public enum BlockResult {
        NO_BLOCK,               // 0 - no block occurred
        BLOCK_OFFENSIVE_REBOUND, // 1 - blocked but offense gets rebound or out of bounds
        BLOCK_DEFENSIVE_REBOUND  // 2 - blocked and defense gets rebound
    }
    
    /**
     * Result enum for judgeNormalFoul method
     */
    public enum FoulResult {
        NO_FOUL,                // 0 - no foul occurred
        OFFENSIVE_FOUL,         // 1 - offensive foul (turnover)
        DEFENSIVE_FOUL          // 2 - defensive foul (free throws if in bonus)
    }
    
    /**
     * Result enum for judgeMakeShot method
     */
    public enum ShotResult {
        MADE_SHOT,              // 1 - made the shot (or made last free throw)
        OFFENSIVE_REBOUND,      // 2 - missed shot, offense gets rebound
        DEFENSIVE_REBOUND,      // 3 - missed shot, defense gets rebound (or successful challenge)
        OUT_OF_BOUNDS           // 4 - shot went out of bounds
    }
    
    /**
     * Result enum for makeFreeThrow method
     */
    public enum FreeThrowResult {
        ERROR,                  // 0 - should never happen
        MADE_LAST_FREE_THROW,   // 1 - made the last free throw
        OFFENSIVE_REBOUND,      // 2 - missed last free throw, offense gets rebound (or flagrant foul continuation)
        DEFENSIVE_REBOUND       // 3 - missed last free throw, defense gets rebound
    }
    
    /**
     * Generate a random number from range [min, max].
     * 
     * @param random The Random object to generate random number
     * @param min The lower bound of the range
     * @param max The upper bound of the range
     * @return A random number
     */
    public static int generateRandomNum(Random random, int min, int max) {
        if (min == max) return min;
        return random.nextInt(max - min + 1) + min;
    }

    /**
     * Overloading, generate a random number from default range [1, 100].
     * 
     * @param random The Random object to generate random number
     * @return A random number
     */
    public static int generateRandomNum(Random random) {
        return generateRandomNum(random, 1, 100);
    }

    /**
     * Round a double number to any given scale.
     * 
     * @param num The double number to be rounded
     * @parm scale The scale to rouned
     * @return A rounded number
     */
    public static double roundDouble(double num, int scale) {
        BigDecimal b = new BigDecimal(num);
        double result = b.setScale(scale, RoundingMode.HALF_UP).doubleValue();
        return result;
    }

    /**
     * Overloading, round a double number to default 2 scale.
     * 
     * @param num The double number to be rounded
     * @return A rounded number
     */
    public static double roundDouble(double num) {
        return roundDouble(num, 2);
    }

    /**
     * Initialize the file paths. 
     * If the path exists, delete all files in the specified directory; otherwise create a new one.
     * 
     * @param directory The directory to be operated
     */
    public static void initializePath(String directory) {
        File dir = new File(directory);
        
        if (!dir.exists()) dir.mkdirs();
        else {
            File[] listFiles = dir.listFiles();
            for (File file : listFiles) {
                file.delete();
            }
        }
    }

    /**
     * Generate current play's time based on NBA-realistic distributions.
     * Uses a weighted probability system to simulate realistic possession times:
     * - 24s shot clock: Average 15-17s (bell curve centered around 16s)
     * - 14s shot clock: Average 8-10s (bell curve centered around 9s)
     * 
     * @param time Maximum time of current play (24 for full possession, 14 for offensive rebound)
     * @return current play's time in seconds
     */
    public static int generateRandomPlayTime(Random random, int time) {
        if (time == 24) {
            // Distribution: 5% very quick (4-7s), 80% normal (8-18s), 15% slow (19-24s)
            int roll = generateRandomNum(random, 1, 100);
            
            if (roll <= 5) {
                // Very quick play: 4-7 seconds
                return generateRandomNum(random, 4, 7);
            } else if (roll <= 85) {
                // Use triangle distribution for more realistic clustering
                int r1 = generateRandomNum(random, 8, 18);
                int r2 = generateRandomNum(random, 8, 18);
                return (r1 + r2) / 2; // Averages toward middle values (12-14)
            } else {
                // Slow, deliberate play: 19-24 seconds
                return generateRandomNum(random, 19, 24);
            }
            
        } else if (time == 14) {
            // Offensive rebound / reset possession (14 seconds)
            // Real NBA average: ~8-10 seconds
            // Distribution: 50% quick putback (4-6s), 40% normal reset (7-11s), 10% full reset (12-14s)
            
            int roll = generateRandomNum(random, 1, 100);
            
            if (roll <= 50) {
                // Quick putback or tip-in: 4-6 seconds
                return generateRandomNum(random, 4, 6);
            } else if (roll <= 90) {
                // Normal reset play: 7-11 seconds (bell curve around 9)
                int r1 = generateRandomNum(random, 7, 11);
                int r2 = generateRandomNum(random, 7, 11);
                return (r1 + r2) / 2; // Averages toward 9 seconds
            } else {
                // Full reset to perimeter: 12-14 seconds
                return generateRandomNum(random, 12, 14);
            }
            
        } else {
            // Fallback for other shot clock times (e.g., end of quarter situations)
            // Use proportional scaling based on 24-second distribution
            int scaledMin = Math.max(4, time / 6);
            int scaledMax = time;
            return generateRandomNum(random, scaledMin, scaledMax);
        }
    }

    /**
     * Overloading for choosePlayerBasedOnRating() function, remove quarter and team information.
     * 
     * @param TeamOnCourt Current players on the court
     * @param attr The rating criteria, including 'rating', 'orb', 'drb' and 'ast'
     * @return Selected player
     */
    public static Player choosePlayerBasedOnRating(Random random, Map<String, Player> TeamOnCourt, String attr) {
        return choosePlayerBasedOnRating(random, TeamOnCourt, attr, 0, 0, null, null);
    }

    /**
     * Select a player based on rating.
     * 
     * @param TeamOnCourt Current players on the court
     * @param attr The rating criteria, including 'rating', 'orb', 'drb' and 'ast'
     * @param currentQuarter Current quarter number
     * @param quarterTime Time left in current quarter
     * @param offenseTeam Offense team
     * @param defenseTeam Defense team
     * @return Selected player
     */
    public static Player choosePlayerBasedOnRating(Random random, Map<String, Player> TeamOnCourt, String attr,
                                                   int currentQuarter, int quarterTime, Team offenseTeam, Team defenseTeam) {
        double major = Constants.MAJOR_SCORE_FACTOR;
        double minor = Constants.MINOR_SCORE_FACTOR;
        
        double totalRating = 0;
        for (String pos : TeamOnCourt.keySet()) {
            if (attr.equals("rating")) 
                totalRating += (major*TeamOnCourt.get(pos).rating + 
                                minor*Math.max(TeamOnCourt.get(pos).insideRating, TeamOnCourt.get(pos).layupRating) +
                                minor*Math.max(TeamOnCourt.get(pos).midRating, TeamOnCourt.get(pos).threeRating) +
                                minor*TeamOnCourt.get(pos).offConst);
            if (attr.equals("orb")) totalRating += TeamOnCourt.get(pos).orbRating;
            if (attr.equals("drb")) totalRating += TeamOnCourt.get(pos).drbRating;
            if (attr.equals("ast")) totalRating += TeamOnCourt.get(pos).astRating;
        }
        double avgRating = totalRating * 1.0 / 5;

        double[] poss = new double[5];
        String[] positions = {"C", "PF", "SF", "SG", "PG"};

        double poss1, poss2, poss3, poss4 = 0;
        int basePoss = 100 / 5;
        if (attr.equals("rating")) {
            // get top-highest rating players
            int highestRating = 0;
            PriorityQueue<Player> selectedPlayerList = new PriorityQueue<>((a, b) -> { return b.rating - a.rating; });
            Player selectedPlayer = null;

            for (Player p : TeamOnCourt.values())
                highestRating = Math.max(highestRating, p.rating);

            for (Player p : TeamOnCourt.values()) {
                if (highestRating - p.rating <= Constants.RATING_RANGE && p.playerType != Player.PlayerType.INSIDER)
                    selectedPlayerList.offer(p);
            }
            
            // higher chance to select the player with highest rating
            if (selectedPlayerList.size() >= 1) {
                if (selectedPlayerList.size() == 1 ||
                    (generateRandomNum(random) <= Constants.SINGLE_STAR_EXTRA && selectedPlayerList.peek().rating <= Constants.GENERAL_THLD))
                    selectedPlayer = selectedPlayerList.peek();
                else {
                    int totalStarRating = 0;
                    for (Player p : selectedPlayerList) totalStarRating += p.rating;

                    int currentRatingSum = 0;
                    int randomPick = generateRandomNum(random, 1, totalStarRating);
                    for (Player p : selectedPlayerList) {
                        currentRatingSum += p.rating;
                        if (randomPick <= currentRatingSum) {
                            selectedPlayer = p;
                            break;
                        }
                    }
                }

                // clutch time, give star players with top-highest rating
                if (currentQuarter >= 4 && quarterTime <= Constants.TIME_LEFT_CLUTCH
                    && Math.abs(offenseTeam.totalScore - defenseTeam.totalScore) <= Constants.CLOSE_GAME_DIFF) {
                    if (generateRandomNum(random) <= Constants.CLUTCH_PERCENT && selectedPlayer.isStar) {
                        return selectedPlayer;
                    }
                }
            }

            for (int i = 0; i < poss.length; i++) {
                String currentPos = positions[i];
                Player player = TeamOnCourt.get(currentPos);
                if (player == null) {
                    System.err.println("Warning: Missing player at position " + currentPos);
                    poss[i] = 0;
                    continue;
                }
                poss[i] = (10 * (basePoss + major*player.rating + 
                                minor*Math.max(player.insideRating, player.layupRating) +
                                minor*Math.max(player.midRating, player.threeRating) +
                                minor*player.offConst - avgRating ));
            }
        } else {
            for (int i = 0; i < poss.length; i++) {
                String currentPos = positions[i];
                Player player = TeamOnCourt.get(currentPos);
                if (player == null) {
                    System.err.println("Warning: Missing player at position " + currentPos);
                    poss[i] = 0;
                    continue;
                }

                if (attr.equals("orb")) {
                    // Use power scaling for rebounds to create NBA-realistic distribution
                    // REBOUND_POWER_SCALE controls the advantage elite rebounders get
                    // Higher values (closer to 1.0) = more advantage for elite rebounders
                    double normalizedRating = Math.pow(Math.max(player.orbRating, 1), Constants.REBOUND_POWER_SCALE);
                    poss[i] = normalizedRating * 100;
                } else if (attr.equals("drb")) {
                    double normalizedRating = Math.pow(Math.max(player.drbRating, 1), Constants.REBOUND_POWER_SCALE);
                    poss[i] = normalizedRating * 100;
                } else {
                    poss[i] = Math.max( (1000 * (Constants.AST_SCALE * player.astRating - avgRating) / totalRating), 0);
                }
            }
        }
        
        // For rebounds, normalize probabilities to sum to 1000 for proper distribution
        if (attr.equals("orb") || attr.equals("drb")) {
            double totalPoss = 0;
            for (int i = 0; i < poss.length; i++) {
                totalPoss += poss[i];
            }
            if (totalPoss > 0) {
                for (int i = 0; i < poss.length; i++) {
                    poss[i] = (poss[i] / totalPoss) * 1000;
                }
            }
        }
        
        int pick = generateRandomNum(random, 1, 1000);
        if (pick <= poss[0] && TeamOnCourt.get("C") != null) return TeamOnCourt.get("C");
        else if (pick <= poss[0] + poss[1] && TeamOnCourt.get("PF") != null) return TeamOnCourt.get("PF");
        else if (pick <= poss[0] + poss[1] + poss[2] && TeamOnCourt.get("SF") != null) return TeamOnCourt.get("SF");
        else if (pick <= poss[0] + poss[1] + poss[2] + poss[3] && TeamOnCourt.get("SG") != null) return TeamOnCourt.get("SG");
        else if (TeamOnCourt.get("PG") != null) return TeamOnCourt.get("PG");
        
        // Fallback: return any non-null player
        for (Player p : TeamOnCourt.values()) {
            if (p != null) return p;
        }
        
        // This should never happen, but return null as last resort
        System.err.println("ERROR: No players found in TeamOnCourt map!");
        return null;
    }

    /**
     * Select a player to defense the offense player.
     * 
     * @param offensePlayer Offense player
     * @param defenseTeamOnCourt Current defense players on the court
     * @return Selected player
     */
    public static Player chooseDefensePlayer(Random random, Player offensePlayer, Map<String, Player> defenseTeamOnCourt) {
        String offensePos = offensePlayer.position;
        List<String> otherPos = new ArrayList<>();
        for (String pos : defenseTeamOnCourt.keySet()) 
            if (pos != offensePos) otherPos.add(pos);
        
        int poss = generateRandomNum(random);
        if (poss <= Constants.SAME_POS) return defenseTeamOnCourt.get(offensePos);
        else if (poss <= Constants.SAME_POS + Constants.OTHER_POS) return defenseTeamOnCourt.get( otherPos.get(0) );
        else if (poss <= Constants.SAME_POS + 2 * Constants.OTHER_POS) return defenseTeamOnCourt.get( otherPos.get(1) );
        else if (poss <= Constants.SAME_POS + 3 * Constants.OTHER_POS) return defenseTeamOnCourt.get( otherPos.get(2) );
        else return defenseTeamOnCourt.get( otherPos.get(3) );
    }

    /**
     * Generate actions after losing ball.
     * 
     * @param defenseTeam Defense team
     * @param offensePlayer Offense player
     * @param defensePlayer Defense player
     * @param defenseTeamOnCourt Current defense players on the court
     * @return LoseBallResult indicating the outcome
     */
    public static LoseBallResult judgeLoseBall(Random random, Team defenseTeam, Map<String, Player> defenseTeamOnCourt, Player offensePlayer, Player defensePlayer) {
        double range = 60 * Constants.STEAL_BASE + Constants.STEAL_RATING_SCALE * defensePlayer.stlRating
                        + Constants.STEAL_DEFENSE_SCALE * Math.max(defensePlayer.interiorDefense, defensePlayer.perimeterDefense)
                        + defensePlayer.athleticism;
        if (defensePlayer.stlRating >= Constants.STEAL_BONUS_THLD1 && defensePlayer.stlRating < Constants.STEAL_BONUS_THLD2)
            range *= Constants.STEAL_BONUS_SCALE1;
        else if (defensePlayer.stlRating >= Constants.STEAL_BONUS_THLD2 && defensePlayer.stlRating < Constants.STEAL_BONUS_THLD3)
            range *= Constants.STEAL_BONUS_SCALE2;
        else if (defensePlayer.stlRating >= Constants.STEAL_BONUS_THLD3 && defensePlayer.stlRating < Constants.STEAL_BONUS_THLD4)
            range *= Constants.STEAL_BONUS_SCALE3;
        else if (defensePlayer.stlRating >= Constants.STEAL_BONUS_THLD4) range *= Constants.STEAL_BONUS_SCALE4;

        int poss = generateRandomNum(random);
        // chance to jump ball
        if (poss <= 1) {
            if (generateRandomNum(random) <= Constants.JUMP_BALL_PLAY) {
                String winPlayer = jumpBall(random, offensePlayer.getDisplayName(), defensePlayer.getDisplayName());
                return winPlayer.equals(offensePlayer.getDisplayName()) ? LoseBallResult.JUMP_BALL_WIN : LoseBallResult.LOSE_BALL_NO_SCORE;
            }
        }

        // chance to turnover
        else if (poss <= 1 + Constants.TURNOVER) {
            offensePlayer.turnover++;
            Comments.getTurnoverComment(offensePlayer.getDisplayName());
            return LoseBallResult.LOSE_BALL_NO_SCORE;
        }

        // steal 
        else if (60 * poss <= 60 * (1 + Constants.TURNOVER) + range) {
            offensePlayer.turnover++;
            defensePlayer.steal++;
            Comments.getStealComment(offensePlayer.getDisplayName(), defensePlayer.getDisplayName());

            // low chance to start a non-fast-break play, high chance to start a fast break
            int fastBreak = generateRandomNum(random);
            if (fastBreak <= Constants.NON_FASTBREAK) {
                Comments.getNonFastBreak(Constants.getLocalizedTeamName(defenseTeam.name));
                return LoseBallResult.LOSE_BALL_NO_SCORE;
            } else {
                int fastBreakTemp = generateRandomNum(random);
                
                // finish by himself or teammate
                Player finisher;
                if (fastBreakTemp <= Constants.SAME_POS) finisher = defensePlayer;
                else {
                    List<String> otherTeammate = new ArrayList<>();
                    for (String pos : defenseTeamOnCourt.keySet()) 
                        if (pos != defensePlayer.position) otherTeammate.add(pos);

                    if (poss <= Constants.SAME_POS + Constants.OTHER_POS) finisher = defenseTeamOnCourt.get( otherTeammate.get(0) );
                    else if (poss <= Constants.SAME_POS + 2 * Constants.OTHER_POS) finisher = defenseTeamOnCourt.get( otherTeammate.get(1) );
                    else if (poss <= Constants.SAME_POS + 3 * Constants.OTHER_POS) finisher = defenseTeamOnCourt.get( otherTeammate.get(2) );
                    else finisher = defenseTeamOnCourt.get( otherTeammate.get(3) );
                }

                Comments.getFastBreak(Constants.getLocalizedTeamName(defenseTeam.name), finisher.getDisplayName());
                defenseTeam.totalScore += 2;
                finisher.score += 2;
                finisher.shotMade++;
                finisher.shotAttempted++;
            }
            return LoseBallResult.LOSE_BALL_AND_SCORE;
        }
        return LoseBallResult.NO_LOSE_BALL;
    }

    /**
     * Generate actions after a block.
     * 
     * @param distance Shot distance
     * @param offensePlayer Offense player
     * @param defensePlayer Defense player
     * @param offenseTeamOnCourt Current offense players on the court
     * @param defenseTeamOnCourt Current defense players on the court
     * @return BlockResult indicating the outcome
     */
    public static BlockResult judgeBlock(Random random, int distance, Map<String, Player> offenseTeamOnCourt, Map<String, Player> defenseTeamOnCourt,
                                 Player offensePlayer, Player defensePlayer) {
        double range = Constants.BLOCK_RATING_SCALE * defensePlayer.blkRating +
                        Math.max(defensePlayer.interiorDefense, defensePlayer.perimeterDefense) + defensePlayer.athleticism;

        if (defensePlayer.blkRating >= Constants.BLOCK_BONUS_THLD1 && defensePlayer.blkRating < Constants.BLOCK_BONUS_THLD2)
            range *= Constants.BLOCK_BONUS_SCALE1;
        else if (defensePlayer.blkRating >= Constants.BLOCK_BONUS_THLD2 && defensePlayer.blkRating < Constants.BLOCK_BONUS_THLD3)
            range *= Constants.BLOCK_BONUS_SCALE2;
        else if (defensePlayer.blkRating >= Constants.BLOCK_BONUS_THLD3 && defensePlayer.blkRating < Constants.BLOCK_BONUS_THLD4)
            range *= Constants.BLOCK_BONUS_SCALE3;
        else if (defensePlayer.blkRating >= Constants.BLOCK_BONUS_THLD4 && defensePlayer.blkRating < Constants.BLOCK_BONUS_THLD5)
            range *= Constants.BLOCK_BONUS_SCALE4;
        else if (defensePlayer.blkRating >= Constants.BLOCK_BONUS_THLD5) range *= Constants.BLOCK_BONUS_SCALE5;

        int poss = generateRandomNum(random);
        if (60 * poss <= range) {
            offensePlayer.shotAttempted++;
            if (distance >= Constants.MIN_THREE_SHOT) offensePlayer.threeAttempted++;
            defensePlayer.block++;
            Comments.getBlockComment(defensePlayer.getDisplayName());

            // low chance to out of bound, high chance to go to rebound juding
            int outOfBound = generateRandomNum(random);
            if (outOfBound <= Constants.BLOCK_OUT_OF_BOUND) {
                Comments.getOutOfBound(defensePlayer.getDisplayName());
                return BlockResult.BLOCK_OFFENSIVE_REBOUND;
            } else {
                boolean stillOffense = judgeRebound(random, offenseTeamOnCourt, defenseTeamOnCourt);
                return stillOffense ? BlockResult.BLOCK_OFFENSIVE_REBOUND : BlockResult.BLOCK_DEFENSIVE_REBOUND;
            }
        }
        return BlockResult.NO_BLOCK;
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
        
        int orbORdrb = generateRandomNum(random);
        int rebAssign = generateRandomNum(random);
        Player rebounder = null;
        if ((offRebBonus && orbORdrb <= Constants.ORB_WITH_BONUS) || (!offRebBonus && orbORdrb <= Constants.ORB_WITHOUT_BONUS)) {
            if (rebAssign <= Constants.REBOUND_RATING_BONUS_PERCENT) {
                for (String pos : offenseTeamOnCourt.keySet()) {
                    if (offenseTeamOnCourt.get(pos).orbRating >= Constants.REBOUND_RATING_BONUS) {
                        rebounder = offenseTeamOnCourt.get(pos);
                        break;
                    }
                }
                if (rebounder == null) rebounder = choosePlayerBasedOnRating(random, offenseTeamOnCourt, "orb");
            } else {
                rebounder = choosePlayerBasedOnRating(random, offenseTeamOnCourt, "orb");
            }

            Comments.getReboundComment(rebounder.getDisplayName(), true);
            rebounder.rebound++;
            return true;
        } else {
            if (rebAssign <= Constants.REBOUND_RATING_BONUS_PERCENT) {
                for (String pos : defenseTeamOnCourt.keySet()) {
                    if (defenseTeamOnCourt.get(pos).drbRating >= Constants.REBOUND_RATING_BONUS) {
                        rebounder = defenseTeamOnCourt.get(pos);
                        break;
                    }
                }
                if (rebounder == null) rebounder = choosePlayerBasedOnRating(random, defenseTeamOnCourt, "drb");
            } else {
                rebounder = choosePlayerBasedOnRating(random, defenseTeamOnCourt, "drb");
            }

            Comments.getReboundComment(rebounder.getDisplayName(), false);
            rebounder.rebound++;
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
        if (previousPlayer.foul == Constants.FOULS_TO_FOUL_OUT || previousPlayer.flagFoul == Constants.FLAGRANT_FOULS_TO_EJECT) {
            Comments.getFoulOutComment(previousPlayer.getDisplayName(), previousPlayer.foul == Constants.FOULS_TO_FOUL_OUT ? true : false);
            previousPlayer.canOnCourt = false;

            Player currentPlayer = findSubPlayer(previousPlayer, team);
            teamOnCourt.put(previousPlayer.position, currentPlayer);
            Comments.getSubstituteComment(currentPlayer.getDisplayName(), previousPlayer.getDisplayName());
        }
    }

    /**
     * Prevent the starter players from getting fouled out quickly.
     * 
     * @param team Team having this player
     * @param previousPlayer The player getting fouled out
     * @param teamOnCourt Players on the court
     * @param currentQuarter Current quarter number
     */
    public static void foulProtect(Player previousPlayer, Team team, Map<String, Player> teamOnCourt, int currentQuarter) {
        if (previousPlayer.rotationType == Player.RotationType.STARTER && 
            ((currentQuarter == 1 && previousPlayer.foul == Constants.QUARTER1_PROTECT) ||
             (currentQuarter == 2 && previousPlayer.foul == Constants.QUARTER2_PROTECT) ||
             (currentQuarter == 3 && previousPlayer.foul == Constants.QUARTER3_PROTECT))) {
            Comments.getFoulProtectComment(previousPlayer.getDisplayName());

            Player currentPlayer = findSubPlayer(previousPlayer, team);
            
            // Update on-court status
            previousPlayer.isOnCourt = false;
            currentPlayer.isOnCourt = true;
            currentPlayer.currentStintSeconds = 0;
            
            teamOnCourt.put(previousPlayer.position, currentPlayer);
            Comments.getSubstituteComment(currentPlayer.getDisplayName(), previousPlayer.getDisplayName());
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
     * @return FoulResult indicating the outcome
     */
    public static FoulResult judgeNormalFoul(Random random, Map<String, Player> offenseTeamOnCourt, Map<String, Player> defenseTeamOnCourt,
                                      Player offensePlayer, Player defensePlayer, Team offenseTeam, Team defenseTeam, int currentQuarter,
                                      int quarterTime, Team team1, Team team2) {
        int poss = generateRandomNum(random);
        int foulTemp = generateRandomNum(random);

        if (poss <= Constants.OFF_FOUL) {
            Player fouler;

            // high chance to foul on offensePlayer, small chance on teammates
            if (foulTemp <= Constants.SAME_POS) {
                fouler = offensePlayer;
                Comments.getOffensiveFoul(fouler.getDisplayName(), 1);
            } else {
                List<String> otherTeammate = new ArrayList<>();
                for (String pos : offenseTeamOnCourt.keySet()) 
                    if (pos != offensePlayer.position) otherTeammate.add(pos);

                if (foulTemp <= Constants.SAME_POS + Constants.OTHER_POS) fouler = offenseTeamOnCourt.get( otherTeammate.get(0) );
                else if (foulTemp <= Constants.SAME_POS + 2 * Constants.OTHER_POS) fouler = offenseTeamOnCourt.get( otherTeammate.get(1) );
                else if (foulTemp <= Constants.SAME_POS + 3 * Constants.OTHER_POS) fouler = offenseTeamOnCourt.get( otherTeammate.get(2) );
                else fouler = offenseTeamOnCourt.get( otherTeammate.get(3) );
                Comments.getOffensiveFoul(fouler.getDisplayName(), 2);
            }

            // challenge the foul
            if (currentQuarter >= Constants.CHALLENGE_START_QUARTER && offenseTeam.canChallenge &&
                generateRandomNum(random) <= Constants.FOUL_CHALLENGE) {
                boolean isSuccessful = Comments.getChallengeComment(Constants.getLocalizedTeamName(offenseTeam.name));
                offenseTeam.canChallenge = false;
                
                if (isSuccessful) return FoulResult.NO_FOUL;
            }

            fouler.turnover++;
            fouler.foul++;
            judgeFoulOut(fouler, offenseTeam, offenseTeamOnCourt);
            foulProtect(fouler, offenseTeam, offenseTeamOnCourt, currentQuarter);
            return FoulResult.OFFENSIVE_FOUL;
        } else if (poss <= Constants.OFF_FOUL + Constants.DEF_FOUL) {
            Player fouler;

            // high chance to foul on offensePlayer, small chance on teammates
            if (foulTemp <= Constants.SAME_POS) {
                fouler = defensePlayer;
                Comments.getDefensiveFoul(fouler.getDisplayName(), 1);
            } else {
                List<String> otherTeammate = new ArrayList<>();
                for (String pos : defenseTeamOnCourt.keySet()) 
                    if (pos != defensePlayer.position) otherTeammate.add(pos);

                if (poss <= Constants.SAME_POS + Constants.OTHER_POS) fouler = defenseTeamOnCourt.get( otherTeammate.get(0) );
                else if (poss <= Constants.SAME_POS + 2 * Constants.OTHER_POS) fouler = defenseTeamOnCourt.get( otherTeammate.get(1) );
                else if (poss <= Constants.SAME_POS + 3 * Constants.OTHER_POS) fouler = defenseTeamOnCourt.get( otherTeammate.get(2) );
                else fouler = defenseTeamOnCourt.get( otherTeammate.get(3) );
                Comments.getDefensiveFoul(fouler.getDisplayName(), 2);
            }

            // challenge the foul
            if (currentQuarter >= Constants.CHALLENGE_START_QUARTER && defenseTeam.canChallenge &&
                generateRandomNum(random) <= Constants.FOUL_CHALLENGE) {
                boolean isSuccessful = Comments.getChallengeComment(Constants.getLocalizedTeamName(defenseTeam.name));
                defenseTeam.canChallenge = false;
                
                if (isSuccessful) return FoulResult.NO_FOUL;
            }

            fouler.foul++;
            judgeFoulOut(fouler, defenseTeam, defenseTeamOnCourt);
            foulProtect(fouler, defenseTeam, defenseTeamOnCourt, currentQuarter);

            defenseTeam.quarterFoul++;
            if (defenseTeam.quarterFoul >= Constants.BONUS_FOUL_THRESHOLD) {
                Comments.getReachFoulTimes(Constants.getLocalizedTeamName(offenseTeam.name), Constants.getLocalizedTeamName(defenseTeam.name));

                makeFreeThrow(random, offensePlayer, offenseTeamOnCourt, defenseTeamOnCourt, offenseTeam,
                              2, quarterTime, currentQuarter, team1, team2, false);
            }
            return FoulResult.DEFENSIVE_FOUL;
        }
        return FoulResult.NO_FOUL;
    }

    /**
     * Generates actions when two teams jumping ball before the game starts.
     */
    public static void jumpBall(Random random, Team team1, Team team2) {
        Team winTeam = Utilities.generateRandomNum(random) <= Constants.JUMP_BALL_FIFTY_FIFTY ? team1 : team2;
        winTeam.hasBall = true;
        Comments.getJumpBallComments(team1, team2, winTeam);
    }

    /**
     * Generates actions when two players jumping ball.
     * @param offensePlayer Offense player
     * @param defensePlayer Defense player
     * @return The player that wins the jumpball 
     */
    public static String jumpBall(Random random, String offensePlayer, String defensePlayer) {
        String winPlayer = Utilities.generateRandomNum(random) <= Constants.JUMP_BALL_FIFTY_FIFTY ? offensePlayer : defensePlayer;
        Comments.getJumpBallComments(offensePlayer, defensePlayer, winPlayer);
        return winPlayer;
    }

    /**
     * Find a player to substitute another teammate on the court.
     * 
     * @param previousPlayer The player to be substituted
     * @param team The team making substitution
     * @return The incoming player 
     */
    public static Player findSubPlayer(Player previousPlayer, Team team) {
        Player currentPlayer = null;
        Random random = new Random();

        if (previousPlayer.rotationType == Player.RotationType.STARTER) {
            if (team.benches.get( previousPlayer.position ).get(0).canOnCourt)
                currentPlayer = team.benches.get( previousPlayer.position ).get(0);

            if (currentPlayer == null && team.rareBenches.containsKey(previousPlayer.position)) {
                List<Player> availableDeepBench = new ArrayList<>();
                for (Player p : team.rareBenches.get(previousPlayer.position)) {
                    if (p.canOnCourt) availableDeepBench.add(p);
                }
                if (!availableDeepBench.isEmpty()) {
                    currentPlayer = availableDeepBench.get(generateRandomNum(random, 0, availableDeepBench.size() - 1));
                }
            }
        } else if (previousPlayer.rotationType == Player.RotationType.BENCH) {
            if (team.starters.get( previousPlayer.position ).canOnCourt)
                currentPlayer = team.starters.get( previousPlayer.position );

            if (currentPlayer == null && team.rareBenches.containsKey(previousPlayer.position)) {
                List<Player> availableDeepBench = new ArrayList<>();
                for (Player p : team.rareBenches.get(previousPlayer.position)) {
                    if (p.canOnCourt) availableDeepBench.add(p);
                }
                if (!availableDeepBench.isEmpty()) {
                    currentPlayer = availableDeepBench.get(generateRandomNum(random, 0, availableDeepBench.size() - 1));
                }
            }
        } else if (previousPlayer.rotationType == Player.RotationType.DEEP_BENCH) {
            if (team.starters.get( previousPlayer.position ).canOnCourt)
                currentPlayer = team.starters.get( previousPlayer.position );

            if (currentPlayer == null && team.benches.containsKey(previousPlayer.position)) {
                if (team.benches.get( previousPlayer.position ).get(0).canOnCourt)
                    currentPlayer = team.benches.get( previousPlayer.position ).get(0);
            }
        }
        return currentPlayer == null ? previousPlayer : currentPlayer;
    }

    /**
     * Make substitutions for a team.
     * 
     * @param subBench Whether substitution is starter -> bench or bench -> starter
     * @param garbageFlag Whether rareBenches have been substituted
     * @param teamOnCourt Team players on the court
     */
    public static void makeSubstitutions(Team team, boolean subBench, boolean garbageFlag, Map<String, Player> teamOnCourt) {
        Random random = new Random();
        
        for (String pos : team.benches.keySet()) {
            Player previousPlayer = teamOnCourt.get(pos);
            Player currentPlayer = null;

            // subBench True: starter -> bench, False: bench -> starter
            if (subBench) {
                if (garbageFlag && team.rareBenches.containsKey(pos)) {
                    // Randomly select from available deep bench players
                    List<Player> availableDeepBench = new ArrayList<>();
                    for (Player p : team.rareBenches.get(pos)) {
                        if (p.canOnCourt) availableDeepBench.add(p);
                    }
                    if (!availableDeepBench.isEmpty()) {
                        currentPlayer = availableDeepBench.get(generateRandomNum(random, 0, availableDeepBench.size() - 1));
                    } else {
                        currentPlayer = team.benches.get(pos).get(0);
                    }
                } else {
                    currentPlayer = team.benches.get(pos).get(0);
                }
            } else {
                currentPlayer = findSubPlayer(previousPlayer, team);
            }

            Comments.getSubstituteComment(currentPlayer.getDisplayName(), previousPlayer.getDisplayName());
            teamOnCourt.put(pos, currentPlayer);
            currentPlayer.hasBeenOnCourt = true;
        }
    }

    /**
     * Generate actions during a timeout.
     * 
     * @param subBench Whether substitution is starter -> bench or bench -> starter
     * @param garbageFlag Whether rareBenches have been substituted
     * @param teamOneOnCourt Team 1's players on the court
     * @param teamTwoOnCourt Team 2's players on the court
     */
    public static void timeOutSub(Team team1, Team team2, boolean subBench, boolean garbageFlag,
                                  Map<String, Player> teamOneOnCourt, Map<String, Player> teamTwoOnCourt) {
        String currentPossess = team1.hasBall ? team1.name : team2.name;
        Comments.getTimeOutComment(currentPossess);
        
        // Randomize substitution order to avoid identical playing times
        Random random = new Random();
        if (random.nextBoolean()) {
            makeSubstitutions(team1, subBench, garbageFlag, teamOneOnCourt);
            makeSubstitutions(team2, subBench, garbageFlag, teamTwoOnCourt);
        } else {
            makeSubstitutions(team2, subBench, garbageFlag, teamTwoOnCourt);
            makeSubstitutions(team1, subBench, garbageFlag, teamOneOnCourt);
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
        int shotChoice = generateRandomNum(random);
        switch (offensePlayer.playerType) {
            case ALL_ROUNDED:
                distance = generateRandomNum(random, Constants.MIN_CLOSE_SHOT, Constants.MAX_THREE_SHOT);
                break;
            case INSIDER:
                distance = generateRandomNum(random, Constants.MIN_CLOSE_SHOT, Constants.MAX_CLOSE_SHOT);
                break;
            case MID_RANGE:
                distance = generateRandomNum(random, Constants.MIN_CLOSE_SHOT, Constants.MID_THREE_SHOT);
                if (distance >= Constants.MIN_MID_SHOT && generateRandomNum(random) <= Constants.TYPE3_PERCENT) 
                    distance -= (Constants.MIN_MID_SHOT - Constants.MIN_CLOSE_SHOT);
                break;
            case INSIDE_OUTSIDE:
                if (shotChoice <= Constants.TYPE4_CLOSE_SHOT)
                    distance = generateRandomNum(random, Constants.MIN_CLOSE_SHOT, Constants.MAX_CLOSE_SHOT);
                else if (shotChoice <= Constants.TYPE4_CLOSE_SHOT + Constants.TYPE4_MID_SHOT)
                    distance = generateRandomNum(random, Constants.MIN_MID_SHOT, Constants.MIN_MID_SHOT);
                else distance = generateRandomNum(random, Constants.MIN_THREE_SHOT, Constants.MAX_THREE_SHOT);
                break;
            case OUTSIDER:
                if (shotChoice <= Constants.TYPE5_CLOSE_SHOT)
                    distance = generateRandomNum(random, Constants.MIN_CLOSE_SHOT, Constants.MAX_CLOSE_SHOT);
                else if (shotChoice <= Constants.TYPE5_CLOSE_SHOT + Constants.TYPE5_MID_SHOT)
                    distance = generateRandomNum(random, Constants.MIN_MID_SHOT, Constants.MIN_MID_SHOT);
                else distance = generateRandomNum(random, Constants.MIN_THREE_SHOT, Constants.MAX_THREE_SHOT);
                break;
            default:
                break;
        }
        if (distance >= Constants.MIN_DIST_CURVE && generateRandomNum(random) <= Constants.DIST_CURVE_PERCENT)
            distance -= Constants.DIST_CURVE;
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
        if (distance <= Constants.MID_MID_SHOT) percentage = Constants.INIT_CLOSE_SHOT_COFF * distance + Constants.INIT_CLOSE_SHOT_INTCP;
        else if (distance <= Constants.MAX_MID_SHOT) percentage = distance + Constants.INIT_MID_SHOT_INTCP;
        else percentage = Constants.INIT_THREE_SHOT_COFF * (distance - Constants.MIN_THREE_SHOT) * (distance - Constants.MIN_THREE_SHOT)
                          + Constants.INIT_THREE_SHOT_INTCP;

        // based on shot choice, adjust percentage
        if (movement.contains(LocalizedStrings.get("commentary.shot.dunk_marker"))) percentage *= Constants.DUNK_SCALE;
        else if (movement.contains(LocalizedStrings.get("commentary.shot.layup_marker"))) percentage += Constants.SHOT_COFF * offensePlayer.layupRating;
        else {
            if (distance <= Constants.MAX_CLOSE_SHOT) percentage += Constants.SHOT_COFF * (offensePlayer.insideRating - Constants.OFFENSE_BASE);
            else if (distance <= Constants.MAX_MID_SHOT) percentage += Constants.SHOT_COFF * (offensePlayer.midRating - Constants.OFFENSE_BASE);
            else percentage += Constants.SHOT_COFF * (offensePlayer.threeRating - Constants.OFFENSE_BASE);
        }

        // based on defender, adjust percentage
        if (distance <= Constants.MAX_CLOSE_SHOT) percentage -= Constants.DEFENSE_COFF * (defensePlayer.interiorDefense - Constants.DEFENSE_BASE);
        else percentage -= Constants.DEFENSE_COFF * (defensePlayer.perimeterDefense - Constants.DEFENSE_BASE);

        // check defense density
        int temp = generateRandomNum(random);
        if (temp <= Constants.DEFENSE_EASY) percentage += Constants.DEFENSE_BUFF;
        else if (temp <= Constants.DEFENSE_EASY + Constants.DEFENSE_HARD) percentage -= Constants.DEFENSE_BUFF;

        // offensive consistency & defense player's defensive consistency
        double consistencyDiff = Constants.CONSISTENCY_COFF * (offensePlayer.offConst - defensePlayer.defConst);
        if (consistencyDiff > Constants.CONSISTENCY_MAX_BONUS) percentage += Constants.CONSISTENCY_MAX_BONUS;
        else if (consistencyDiff < -Constants.CONSISTENCY_MAX_BONUS) percentage -= Constants.CONSISTENCY_MAX_BONUS;
        else percentage += consistencyDiff;

        // athleticism - uses sigmoid function with distance-dependent weighting
        // Close shots (drives, dunks): athleticism matters more (higher weight)
        // Mid-range: moderate athleticism impact
        // Three-pointers: minimal athleticism impact (shooting touch > athleticism)
        double athleticismDiff = offensePlayer.athleticism - defensePlayer.athleticism;
        double athleticismImpact = calculateAthleticismImpact(athleticismDiff, distance);
        percentage += athleticismImpact;

        // clutch time penalty with linear decay based on offensive consistency
        // Higher offConst = less penalty (closer to 1.0), lower offConst = more penalty (closer to CLUTCH_SHOT_COFF)
        if (currentQuarter >= 4
            && Math.abs(team1.totalScore - team2.totalScore) <= Constants.CLOSE_GAME_DIFF && quarterTime <= Constants.TIME_LEFT_CLUTCH) {
            // Linear interpolation: at offConst=25, use full penalty (0.6); at offConst=99, use minimal penalty (1.0)
            // Formula: penalty = CLUTCH_SHOT_COFF + (1.0 - CLUTCH_SHOT_COFF) * (offConst - 25) / (99 - 25)
            double clutchPenalty = Constants.CLUTCH_SHOT_COFF + 
                                  (1.0 - Constants.CLUTCH_SHOT_COFF) * (offensePlayer.offConst - 25) / 74.0;
            percentage *= clutchPenalty;
        }
        
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
     * @return ShotResult indicating the outcome
     */
    public static ShotResult judgeMakeShot(Random random, int distance, Player offensePlayer, Player defensePlayer, Team offenseTeam, 
                                    Team defenseTeam, Map<String, Player> offenseTeamOnCourt, Map<String, Player> defenseTeamOnCourt,
                                    double percentage, int quarterTime, int currentQuarter, Team team1, Team team2, String movement) {
        int judgeShot = generateRandomNum(random, 1, 10000);

        // make the shot
        if (judgeShot < (int)(100 * percentage)) {
            offensePlayer.shotMade++;
            offensePlayer.shotAttempted++;

            if (distance >= Constants.MIN_THREE_SHOT) {
                offensePlayer.threeAttempted++;
                offensePlayer.threeMade++;
                offensePlayer.score += 3;
                offenseTeam.totalScore += 3;
            }
            else {
                offensePlayer.score += 2;
                offenseTeam.totalScore += 2;
            }

            Comments.getMakeShotsComment(offensePlayer.getDisplayName(), defensePlayer.getDisplayName(), distance, movement);
            if (generateRandomNum(random) <= Constants.STATUS_COMMENT_PERCENT) Comments.getStatusComment(offensePlayer, true);
            Comments.getTimeAndScore(quarterTime, currentQuarter, team1, team2);

            // chance to give starters extra live comments in garbage time
            if (currentQuarter >= 4 && Math.abs(team1.totalScore - team2.totalScore) >= Constants.DIFF2) {
                int temp = generateRandomNum(random);
                if (temp <= Constants.EXTRA_COMMENT) {
                    Comments.getStartersComment(team1);
                } else if (temp <= 2 * Constants.EXTRA_COMMENT) {
                    Comments.getStartersComment(team2);
                }
            }

            // find the teammate with the highest astRating
            int highestAstRating = 0;
            Player highestPlayer = null;
            for (String pos : offenseTeamOnCourt.keySet()) {
                if (pos == offensePlayer.position) continue;

                int currentAst = offenseTeamOnCourt.get(pos).astRating;
                if (highestAstRating < currentAst) {
                    highestAstRating = currentAst;
                    highestPlayer = offenseTeamOnCourt.get(pos);
                }
            }

            int assistAssign = generateRandomNum(random);
            if (assistAssign <= Constants.HIGH_BOTH_RATING) {
                // high rating and high astRating, or highest astRating in the team
                if ((highestPlayer.rating >= Constants.HIGH_BOTH_RATING_THLD && highestPlayer.astRating >= Constants.HIGH_BOTH_RATING_THLD) ||
                    assistAssign <= Constants.HIGHEST_RATING_PERCENT) {
                    highestPlayer.assist += 1;
                }
            } else {
                int astTemp = generateRandomNum(random);
                if ((offensePlayer.isStar && astTemp <= Constants.STAR_PLAYER_AST) ||
                    (!offensePlayer.isStar && astTemp <= Constants.NON_STAR_PLAYER_AST)) {
                    Player assister;
                    while (true) {
                        assister = choosePlayerBasedOnRating(random, offenseTeamOnCourt, "ast");
                        if (assister.position != offensePlayer.position) break;
                    }
                    assister.assist += 1;
                }
            }

            // judge free throw chance
            int andOneTemp = generateRandomNum(random, 1, 10000);
            int drawFoulPercent = calculateFoulPercent(distance, offensePlayer, defensePlayer, true);

            if (andOneTemp <= drawFoulPercent) {
                defensePlayer.foul++;
                Comments.getAndOneComment(offensePlayer.getDisplayName());
                judgeFoulOut(defensePlayer, defenseTeam, defenseTeamOnCourt);
                foulProtect(defensePlayer, defenseTeam, defenseTeamOnCourt, currentQuarter);
                FreeThrowResult andOneResult = makeFreeThrow(random, offensePlayer, offenseTeamOnCourt, defenseTeamOnCourt, offenseTeam, 1,
                                                 quarterTime, currentQuarter, team1, team2, false);
                // Convert FreeThrowResult to ShotResult
                return convertFreeThrowToShotResult(andOneResult);
            }
            return ShotResult.MADE_SHOT;
        }

        // miss the shot
        else {
            int foulTemp = generateRandomNum(random, 1, 10000);
            int drawFoulPercent = calculateFoulPercent(distance, offensePlayer, defensePlayer, false);

            // get a foul
            if (foulTemp <= drawFoulPercent) {
                // flagrant foul
                if (generateRandomNum(random) <= Constants.FLAG_FOUL) {
                    defensePlayer.flagFoul++;
                    Comments.getFlagFoulComment(offensePlayer.getDisplayName(), defensePlayer.getDisplayName());
                    judgeFoulOut(defensePlayer, defenseTeam, defenseTeamOnCourt);

                    // two free throws, one shot
                    FreeThrowResult flagrantResult = makeFreeThrow(random, offensePlayer, offenseTeamOnCourt, defenseTeamOnCourt,
                                         offenseTeam, 2, quarterTime, currentQuarter, team1, team2, true);
                    return convertFreeThrowToShotResult(flagrantResult);
                }

                Comments.getFoulComment(offensePlayer.getDisplayName(), defensePlayer.getDisplayName());

                // challenge the foul
                if (currentQuarter >= Constants.CHALLENGE_START_QUARTER && defenseTeam.canChallenge &&
                    generateRandomNum(random) <= Constants.FOUL_CHALLENGE) {
                    boolean isSuccessful = Comments.getChallengeComment(Constants.getLocalizedTeamName(defenseTeam.name));
                    defenseTeam.canChallenge = false;
                    
                    if (isSuccessful) return ShotResult.DEFENSIVE_REBOUND;
                }

                defensePlayer.foul++;
                defenseTeam.quarterFoul++;
                
                judgeFoulOut(defensePlayer, defenseTeam, defenseTeamOnCourt);
                foulProtect(defensePlayer, defenseTeam, defenseTeamOnCourt, currentQuarter);

                FreeThrowResult freeThrowResult;
                if (distance <= Constants.MAX_MID_SHOT) 
                    freeThrowResult = makeFreeThrow(random, offensePlayer, offenseTeamOnCourt, defenseTeamOnCourt,
                                                    offenseTeam, 2, quarterTime, currentQuarter, team1, team2, false);
                else freeThrowResult = makeFreeThrow(random, offensePlayer, offenseTeamOnCourt, defenseTeamOnCourt,
                                                     offenseTeam, 3, quarterTime, currentQuarter, team1, team2, false);

                return convertFreeThrowToShotResult(freeThrowResult);
            }

            offensePlayer.shotAttempted++;
            if (distance >= Constants.THREE_POINT_LINE_DISTANCE) offensePlayer.threeAttempted++;
            Comments.getMissShotsComment(movement, offensePlayer.getDisplayName());
            if (generateRandomNum(random) <= Constants.STATUS_COMMENT_PERCENT) Comments.getStatusComment(offensePlayer, false);

            // shot out of bound
            if (generateRandomNum(random) <= Constants.SHOT_OUT_OF_BOUND) {
                Comments.shotOutOfBound(offensePlayer.getDisplayName());
                return ShotResult.OUT_OF_BOUNDS;
            }

            return judgeRebound(random, offenseTeamOnCourt, defenseTeamOnCourt) ? ShotResult.OFFENSIVE_REBOUND : ShotResult.DEFENSIVE_REBOUND;
        }
    }
    
    /**
     * Helper method to convert FreeThrowResult to ShotResult
     */
    private static ShotResult convertFreeThrowToShotResult(FreeThrowResult freeThrowResult) {
        switch (freeThrowResult) {
            case MADE_LAST_FREE_THROW:
                return ShotResult.MADE_SHOT;
            case OFFENSIVE_REBOUND:
                return ShotResult.OFFENSIVE_REBOUND;
            case DEFENSIVE_REBOUND:
                return ShotResult.DEFENSIVE_REBOUND;
            default:
                return ShotResult.DEFENSIVE_REBOUND; // Fallback
        }
    }

    /**
     * Calculate foul percentage based on player rating.
     * 
     * @param distance Shot distance
     * @param offensePlayer The offense player
     * @param defensePlayer The defense player
     * @param isAndOne Decide whether current foul is an And-One foul or not.
     * @return foul percentage
     */
    public static int calculateFoulPercent(int distance, Player offensePlayer, Player defensePlayer, boolean isAndOne) {
        double drawFoulPercent;
        int basePercent;

        if (isAndOne)
            basePercent = distance <= Constants.MAX_CLOSE_SHOT
                            ? Constants.AND_ONE_CLOSE_BASE
                            : distance <= Constants.MAX_MID_SHOT ? Constants.AND_ONE_MID_BASE : Constants.AND_ONE_THREE_BASE;
        else basePercent = distance <= Constants.MAX_CLOSE_SHOT
                            ? Constants.NORMAL_CLOSE_BASE
                            : distance <= Constants.MAX_MID_SHOT ? Constants.NORMAL_MID_BASE : Constants.NORMAL_THREE_BASE;

        if (offensePlayer.drawFoul >= Constants.FOUL_RATING_THLD1) {
            drawFoulPercent = basePercent * (100 + Constants.FOUL_COFF1 * offensePlayer.drawFoul);
        } else if (offensePlayer.drawFoul >= Constants.FOUL_RATING_THLD2) {
            drawFoulPercent = basePercent * (100 + Constants.FOUL_COFF2 * offensePlayer.drawFoul);
        } else {
            drawFoulPercent = basePercent * (100 + Constants.FOUL_COFF3 * offensePlayer.drawFoul);
        }
        
        if (offensePlayer.isStar && !defensePlayer.isStar) drawFoulPercent *= Constants.STAR_FOUL_SCALE;
        return (int)drawFoulPercent;
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
     * @return FreeThrowResult indicating the outcome
     */
    public static FreeThrowResult makeFreeThrow(Random random, Player player, Map<String, Player> offenseTeamOnCourt, Map<String, Player> defenseTeamOnCourt,
                                    Team offenseTeam, int times, int quarterTime, int currentQuarter, Team team1, Team team2, boolean isFlagFoul) {
        int timesLeft = times;
        boolean onlyOneShot = timesLeft == 1 ? true : false;
        int count = 0;

        Comments.getFreeThrowPrepareComment(player.getDisplayName());

        while (timesLeft > 0) {
            timesLeft--;
            count++;

            if (generateRandomNum(random) <= player.freeThrowPercent) {
                player.freeThrowAttempted++;
                player.freeThrowMade++;
                player.score++;
                offenseTeam.totalScore++;
                Comments.getMakeFreeThrowComment(count, onlyOneShot);
                Comments.getTimeAndScore(quarterTime, currentQuarter, team1, team2);

                if (timesLeft == 0) return isFlagFoul ? FreeThrowResult.OFFENSIVE_REBOUND : FreeThrowResult.MADE_LAST_FREE_THROW;
            } else {
                player.freeThrowAttempted++;
                Comments.getMissFreeThrowComment(count, onlyOneShot);

                if (timesLeft == 0) 
                    return isFlagFoul ? FreeThrowResult.OFFENSIVE_REBOUND
                                      : judgeRebound(random, offenseTeamOnCourt, defenseTeamOnCourt) ? FreeThrowResult.OFFENSIVE_REBOUND : FreeThrowResult.DEFENSIVE_REBOUND;
            }
        }
        return FreeThrowResult.ERROR;
    }

    /**
     * Deal with team injury and substitute.
     * 
     * @param team Current team
     * @param teamOnCourt Current team players on the court
     * @return true - A player gets injured, false - No player gets injured
     */
    public static boolean handleInjury(Random random, Map<String, Player> teamOnCourt, Team team) {
        for (String pos : teamOnCourt.keySet()) {
            if (generateRandomNum(random, 1, Constants.INJURY_PROBABILITY_DIVISOR) <= Constants.INJURY_BASE_PROBABILITY - teamOnCourt.get(pos).durability) {
                Player previousPlayer = teamOnCourt.get(pos);
                Player currentPlayer = findSubPlayer(previousPlayer, team);
                
                previousPlayer.canOnCourt = false;
                teamOnCourt.put(previousPlayer.position, currentPlayer);
                Comments.getInjuryComment(previousPlayer.getDisplayName());
                Comments.getSubstituteComment(currentPlayer.getDisplayName(), previousPlayer.getDisplayName());
                return true;
            }
        }
        return false;
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
        return handleInjury(random, offenseTeamOnCourt, offenseTeam) || handleInjury(random, defenseTeamOnCourt, defenseTeam);
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
    
    /**
     * Intelligent substitution system that evaluates multiple factors
     * 
     * @param team Team to check for substitutions
     * @param teamOnCourt Current players on court
     * @param currentQuarter Current quarter (1-4 for regulation, 5+ for OT)
     * @param quarterTime Seconds remaining in quarter
     * @param gameTime Total seconds elapsed in game (for fatigue tracking)
     * @param team1 Team 1 reference (for score differential)
     * @param team2 Team 2 reference (for score differential)
     * @param isGarbageTime Whether the game is in garbage time
     * @return true if substitutions were made, false otherwise
     */
    public static boolean checkIntelligentSubstitutions(Random random, Team team, Map<String, Player> teamOnCourt,
                                                       int currentQuarter, int quarterTime, int gameTime,
                                                       Team team1, Team team2, boolean isGarbageTime) {
        // Garbage time: prioritize giving deep bench players minutes
        if (isGarbageTime) {
            return checkGarbageTimeSubstitutions(random, team, teamOnCourt);
        }
        
        // Q1 first 6 minutes: Keep all starters in (unless fouled out or injured)
        if (currentQuarter == Constants.QUARTER_1 && quarterTime > Constants.Q1_NO_SUB_TIME) {
            return false; // No substitutions in first 6 minutes of Q1
        }
        
        // Overtime: only play starters unless injured/fouled out
        if (currentQuarter >= Constants.OVERTIME_QUARTER) {
            return checkOvertimeSubstitutions(team, teamOnCourt);
        }
        
        boolean madeSubs = false;
        int scoreDiff = Math.abs(team1.totalScore - team2.totalScore);
        boolean isClutchTime = currentQuarter == Constants.CLUTCH_QUARTER && quarterTime <= Constants.TIME_LEFT_CLUTCH && scoreDiff <= Constants.CLOSE_GAME_DIFF;
        boolean isCloseGame = scoreDiff <= Constants.CLOSE_GAME_DIFF;
        
        // Clutch time: keep best players in
        if (isClutchTime) {
            return ensureStartersInClutch(team, teamOnCourt);
        }
        
        // Proactively check if rested starters with safe foul situation can return
        // This ensures foul-protected starters don't sit too long
        if (generateRandomNum(random, 1, 100) < Constants.SUB_CHECK_PROBABILITY) {
            for (String pos : team.starters.keySet()) {
                Player starter = team.starters.get(pos);
                Player currentPlayer = teamOnCourt.get(pos);
                
                if (starter != null && starter.canOnCourt && !starter.isOnCourt && 
                    currentPlayer.rotationType != Player.RotationType.STARTER) {
                    
                    int restTime = gameTime - starter.lastSubbedOutTime;
                    int minRest = isCloseGame ? Constants.MIN_REST_TIME_CLOSE_GAME : Constants.MIN_REST_TIME;
                    int targetMinutes = getTargetMinutes(starter);
                    
                    // If starter has rested enough, is under target minutes, AND foul situation is safe
                    if (restTime >= minRest && starter.secondsPlayed < targetMinutes && isFoulSituationSafe(starter, currentQuarter)) {
                        teamOnCourt.put(pos, starter);
                        currentPlayer.isOnCourt = false;
                        currentPlayer.lastSubbedOutTime = gameTime;
                        currentPlayer.currentStintSeconds = 0;
                        
                        starter.isOnCourt = true;
                        starter.hasBeenOnCourt = true;
                        starter.currentStintSeconds = 0;
                        
                        Comments.getSubstituteComment(starter.getDisplayName(), currentPlayer.getDisplayName());
                        return true; // Successfully brought back a starter
                    }
                }
            }
        }
        
        // Random chance to check for substitutions
        // This spreads out substitutions naturally instead of clustering them
        if (generateRandomNum(random, 1, 100) >= Constants.SUB_DECISION_PROBABILITY) {
            return false; // Skip this substitution check
        }
        
        // Find ONE player who most needs to be subbed
        String posToSub = null;
        int highestPriority = 0;
        
        for (String pos : teamOnCourt.keySet()) {
            Player currentPlayer = teamOnCourt.get(pos);
            int priority = 0;
            
            // Critical: foul trouble (highest priority)
            if (shouldSubForFoulTrouble(currentPlayer, currentQuarter)) {
                priority = Constants.FOUL_TROUBLE_PRIORITY;
            }
            // High: fatigue
            else if (shouldSubForFatigue(currentPlayer, isCloseGame)) {
                priority = Constants.FATIGUE_BASE_PRIORITY + (int)(currentPlayer.currentStintSeconds / Constants.FATIGUE_SECONDS_TO_PRIORITY); // More tired = higher priority
            }
            // High: minutes cap - dynamic based on durability for starters
            else if (currentPlayer.secondsPlayed >= getTargetMinutes(currentPlayer)) {
                priority = Constants.MINUTES_CAP_PRIORITY;
            }
            // Medium: performance (cold shooter - only if not close game)
            else if (!isCloseGame && shouldSubForPerformance(currentPlayer)) {
                priority = Constants.PERFORMANCE_PRIORITY;
            }
            
            if (priority > highestPriority) {
                highestPriority = priority;
                posToSub = pos;
            }
        }
        
        // Make ONE substitution if needed
        if (posToSub != null && highestPriority > 0) {
            Player currentPlayer = teamOnCourt.get(posToSub);
            Player newPlayer = findBestSubstitute(team, currentPlayer, gameTime, isCloseGame, currentQuarter);
            
            if (newPlayer != null && newPlayer != currentPlayer) {
                teamOnCourt.put(posToSub, newPlayer);
                currentPlayer.isOnCourt = false;
                currentPlayer.lastSubbedOutTime = gameTime;
                currentPlayer.currentStintSeconds = 0;
                
                newPlayer.isOnCourt = true;
                newPlayer.hasBeenOnCourt = true;
                newPlayer.currentStintSeconds = 0;
                
                Comments.getSubstituteComment(newPlayer.getDisplayName(), currentPlayer.getDisplayName());
                madeSubs = true;
            }
        }
        
        return madeSubs;
    }
    
    /**
     * Check if player should be subbed due to foul trouble
     */
    private static boolean shouldSubForFoulTrouble(Player player, int currentQuarter) {
        if (currentQuarter == Constants.QUARTER_1 && player.foul >= Constants.QUARTER1_PROTECT) return true;
        if (currentQuarter == Constants.QUARTER_2 && player.foul >= Constants.QUARTER2_PROTECT) return true;
        if (currentQuarter >= Constants.QUARTER_3 && player.foul >= Constants.QUARTER3_PROTECT) return true;
        return false;
    }
    
    /**
     * Check if player's foul situation is safe for the current quarter
     * (opposite of shouldSubForFoulTrouble - checks if it's safe to bring them back)
     */
    private static boolean isFoulSituationSafe(Player player, int currentQuarter) {
        // In Q1, safe if less than 2 fouls
        if (currentQuarter == Constants.QUARTER_1) return player.foul < Constants.QUARTER1_PROTECT;
        // In Q2, safe if less than 4 fouls (player with 2-3 fouls can come back)
        if (currentQuarter == Constants.QUARTER_2) return player.foul < Constants.QUARTER2_PROTECT;
        // In Q3+, safe if less than 5 fouls (player with 2-4 fouls can come back)
        if (currentQuarter >= Constants.QUARTER_3) return player.foul < Constants.QUARTER3_PROTECT;
        return true;
    }
    
    /**
     * Calculate target minutes for a starter based on durability and athleticism.
     * 
     * @param player The player to calculate target minutes for
     * @return Target minutes in seconds
     */
    private static int getTargetMinutes(Player player) {
        if (player.rotationType != Player.RotationType.STARTER) {
            return Constants.NON_STARTER_MAX_MINUTES; // Bench players use default high limit
        }
        
        // Base minutes determined by durability (major factor)
        int baseMinutes;
        int durability = player.durability;
        if (durability >= Constants.HIGH_DURABILITY_THRESHOLD) baseMinutes = Constants.HIGH_DURABILITY_MINUTES;
        else if (durability >= Constants.MEDIUM_DURABILITY_THRESHOLD) baseMinutes = Constants.MEDIUM_DURABILITY_MINUTES;
        else if (durability >= Constants.LOW_DURABILITY_THRESHOLD) baseMinutes = Constants.LOW_DURABILITY_MINUTES;
        else baseMinutes = Constants.VERY_LOW_DURABILITY_MINUTES;
        
        int athleticismAdjustment;
        int athleticism = player.athleticism;
        if (athleticism >= Constants.ATHLETICISM_ELITE_THRESHOLD) {
            athleticismAdjustment = Constants.ATHLETICISM_ELITE_BONUS;
        } else if (athleticism >= Constants.ATHLETICISM_HIGH_THRESHOLD) {
            athleticismAdjustment = Constants.ATHLETICISM_HIGH_BONUS;
        } else if (athleticism >= Constants.ATHLETICISM_ABOVE_AVG_THRESHOLD) {
            athleticismAdjustment = Constants.ATHLETICISM_ABOVE_AVG_PENALTY;
        } else if (athleticism >= Constants.ATHLETICISM_AVG_THRESHOLD) {
            athleticismAdjustment = Constants.ATHLETICISM_AVG_PENALTY;
        } else if (athleticism >= Constants.ATHLETICISM_BELOW_AVG_THRESHOLD) {
            athleticismAdjustment = Constants.ATHLETICISM_BELOW_AVG_PENALTY;
        } else if (athleticism >= Constants.ATHLETICISM_LOW_THRESHOLD) {
            athleticismAdjustment = Constants.ATHLETICISM_LOW_PENALTY;
        } else {
            athleticismAdjustment = Constants.ATHLETICISM_VERY_LOW_PENALTY;
        }
        
        // Apply adjustment, ensure minimum of 18 minutes for any starter
        int targetMinutes = baseMinutes + athleticismAdjustment;
        return Math.max(targetMinutes, Constants.MIN_STARTER_MINUTES); // Minimum 18 minutes for starters
    }
    
    /**
     * Check if player should be subbed due to fatigue
     */
    private static boolean shouldSubForFatigue(Player player, boolean isCloseGame) {
        // Starters: rest after stints (to control total minutes)
        if (player.rotationType == Player.RotationType.STARTER) {
            int maxStint = isCloseGame ? Constants.MAX_STARTER_STINT_CLOSE_GAME : Constants.MAX_STARTER_STINT_NORMAL_GAME;
            return player.currentStintSeconds >= maxStint;
        }
        // Bench: rest after 5 minute stints
        else if (player.rotationType == Player.RotationType.BENCH) {
            return player.currentStintSeconds >= Constants.MAX_BENCH_STINT;
        }
        return false;
    }
    
    /**
     * Check if player should be subbed due to poor performance (cold shooting)
     */
    private static boolean shouldSubForPerformance(Player player) {
        // Only sub if player has taken enough shots and is shooting poorly
        if (player.shotAttempted >= Constants.MIN_SHOTS_FOR_HOT) {
            double shotPct = (double) player.shotMade / player.shotAttempted;
            // Keep sub cold shooters out (<30%)
            if (shotPct < Constants.COLD_SHOOTER_THRESHOLD) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Find best available substitute for a player
     */
    private static Player findBestSubstitute(Team team, Player currentPlayer, int gameTime, boolean isCloseGame, int currentQuarter) {
        String pos = currentPlayer.position;
        Player.RotationType currentRotation = currentPlayer.rotationType;
        
        // Priority 1: Bring back starters if they've rested enough AND foul situation is safe
        if (currentRotation == Player.RotationType.BENCH || currentRotation == Player.RotationType.DEEP_BENCH) {
            Player starter = team.starters.get(pos);
            if (starter != null && starter.canOnCourt && !starter.isOnCourt) {
                int restTime = gameTime - starter.lastSubbedOutTime;
                int minRest = isCloseGame ? 60 : Constants.MIN_REST_TIME;  // Shorter rest in close games
                
                // Check if starter has rested enough, is under minutes cap, AND foul situation is safe
                if (restTime >= minRest && starter.secondsPlayed < 2340 && isFoulSituationSafe(starter, currentQuarter)) {
                    return starter;
                }
            }
        }
        
        // Priority 2: Rotate to bench if starter needs rest
        if (currentRotation == Player.RotationType.STARTER) {
            if (team.benches.containsKey(pos)) {
                for (Player benchPlayer : team.benches.get(pos)) {
                    if (benchPlayer.canOnCourt && !benchPlayer.isOnCourt) {
                        // Check if bench player hasn't played too much
                        int targetMinutes = Constants.BENCH_TARGET_MINUTES;
                        if (benchPlayer.secondsPlayed < targetMinutes + Constants.BENCH_MINUTES_BUFFER) {  // +5 min buffer
                            return benchPlayer;
                        }
                    }
                }
            }
        }
        
        // Priority 3: Use deep bench if regular bench is tired
        if (currentRotation == Player.RotationType.BENCH) {
            if (team.rareBenches.containsKey(pos)) {
                // Randomly select from available deep bench players
                List<Player> availableDeepBench = new ArrayList<>();
                for (Player deepBench : team.rareBenches.get(pos)) {
                    if (deepBench.canOnCourt && !deepBench.isOnCourt) {
                        availableDeepBench.add(deepBench);
                    }
                }
                if (!availableDeepBench.isEmpty()) {
                    Random random = new Random();
                    return availableDeepBench.get(generateRandomNum(random, 0, availableDeepBench.size() - 1));
                }
            }
        }
        
        // No substitute found, keep current player
        return currentPlayer;
    }
    
    /**
     * Ensure starters are in during clutch time
     */
    private static boolean ensureStartersInClutch(Team team, Map<String, Player> teamOnCourt) {
        // Find ONE non-starter to replace with a starter
        for (String pos : team.starters.keySet()) {
            Player starter = team.starters.get(pos);
            Player currentPlayer = teamOnCourt.get(pos);
            
            // If starter is available and not on court, put them in
            if (starter.canOnCourt && !starter.isOnCourt && currentPlayer != starter) {
                teamOnCourt.put(pos, starter);
                currentPlayer.isOnCourt = false;
                starter.isOnCourt = true;
                starter.hasBeenOnCourt = true;
                
                Comments.getSubstituteComment(starter.getDisplayName(), currentPlayer.getDisplayName());
                return true; // Only sub ONE player per call
            }
        }
        
        return false;
    }
    
    /**
     * Handle garbage time substitutions - give deep bench players minutes
     * More aggressive substitution rate to ensure all deep bench get playing time
     */
    private static boolean checkGarbageTimeSubstitutions(Random random, Team team, Map<String, Player> teamOnCourt) {
        // Higher chance (50%) to check for substitutions in garbage time
        if (generateRandomNum(random, 1, 100) >= Constants.GARBAGE_TIME_SUB_PROBABILITY) {
            return false;
        }
        
        // Find starters or regular bench players still on court
        for (String pos : teamOnCourt.keySet()) {
            Player currentPlayer = teamOnCourt.get(pos);
            
            // If there's a starter or regular bench on court, try to sub them with deep bench
            if (currentPlayer.rotationType == Player.RotationType.STARTER || 
                currentPlayer.rotationType == Player.RotationType.BENCH) {
                
                // Try to find an available deep bench player
                if (team.rareBenches.containsKey(pos)) {
                    List<Player> availableDeepBench = new ArrayList<>();
                    for (Player deepBench : team.rareBenches.get(pos)) {
                        if (deepBench.canOnCourt && !deepBench.isOnCourt) {
                            availableDeepBench.add(deepBench);
                        }
                    }
                    
                    if (!availableDeepBench.isEmpty()) {
                        Player newPlayer = availableDeepBench.get(generateRandomNum(random, 0, availableDeepBench.size() - 1));
                        
                        teamOnCourt.put(pos, newPlayer);
                        currentPlayer.isOnCourt = false;
                        newPlayer.isOnCourt = true;
                        newPlayer.hasBeenOnCourt = true;
                        newPlayer.currentStintSeconds = 0;
                        
                        Comments.getSubstituteComment(newPlayer.getDisplayName(), currentPlayer.getDisplayName());
                        return true; // Only sub ONE player per call
                    }
                }
            }
        }
        
        return false;
    }
    
    /**
     * Handle overtime substitutions - only starters play unless injured/fouled out
     */
    private static boolean checkOvertimeSubstitutions(Team team, Map<String, Player> teamOnCourt) {
        // Find ONE non-starter to replace or ONE injured player to replace
        for (String pos : teamOnCourt.keySet()) {
            Player currentPlayer = teamOnCourt.get(pos);
            Player starter = team.starters.get(pos);
            
            // Put starter in if they're available and not already in
            if (starter != null && starter.canOnCourt && !starter.isOnCourt && currentPlayer != starter) {
                teamOnCourt.put(pos, starter);
                currentPlayer.isOnCourt = false;
                starter.isOnCourt = true;
                starter.hasBeenOnCourt = true;
                
                Comments.getSubstituteComment(starter.getDisplayName(), currentPlayer.getDisplayName());
                return true; // Only sub ONE player per call
            }
            // If starter can't play, find next best available
            else if (!currentPlayer.canOnCourt) {
                Player replacement = findSubPlayer(currentPlayer, team);
                if (replacement != currentPlayer) {
                    teamOnCourt.put(pos, replacement);
                    replacement.isOnCourt = true;
                    replacement.hasBeenOnCourt = true;
                    
                    Comments.getSubstituteComment(replacement.getDisplayName(), currentPlayer.getDisplayName());
                    return true; // Only sub ONE player per call
                }
            }
        }
        
        return false;
    }
    
    /**
     * Calculate athleticism impact on shooting percentage using sigmoid function.
     * Uses distance-dependent weighting: athleticism matters more on close shots.
     * 
     * Mathematical model:
     * 1. Sigmoid function: f(x) = k * (2 / (1 + e^(-x/scale)) - 1)
     *    - Maps any difference to range [-k, +k] with smooth diminishing returns
     *    - scale controls how quickly the curve saturates (higher = more gradual)
     * 
     * 2. Distance-based weight:
     *    - Close shots (≤12ft): 100% weight (athleticism critical for drives/dunks)
     *    - Mid-range (13-22ft): 60% weight (athleticism helps but less critical)
     *    - Three-point (≥23ft): 30% weight (shooting touch > athleticism)
     * 
     * @param athleticismDiff Difference in athleticism (offense - defense)
     * @param distance Shot distance in feet
     * @return Percentage adjustment based on athleticism
     */
    private static double calculateAthleticismImpact(double athleticismDiff, int distance) {
        // Sigmoid parameters
        final double MAX_IMPACT = 4.0;  // Maximum percentage boost/penalty at extreme differences
        final double SIGMOID_SCALE = 15.0;  // Controls curve steepness (higher = more gradual)
        
        // Calculate sigmoid value: maps (-∞, +∞) to (-1, +1)
        // At diff=0: impact=0
        // At diff=15: impact ≈ 0.63 * MAX_IMPACT (63% of max)
        // At diff=30: impact ≈ 0.86 * MAX_IMPACT (86% of max)
        // At diff=50: impact ≈ 0.96 * MAX_IMPACT (96% of max, nearly saturated)
        double sigmoidValue = 2.0 / (1.0 + Math.exp(-athleticismDiff / SIGMOID_SCALE)) - 1.0;
        
        // Distance-based weight factor
        double distanceWeight;
        if (distance <= Constants.MAX_CLOSE_SHOT) {
            // Close shots: full athleticism impact
            distanceWeight = 1.0;
        } else if (distance <= Constants.MAX_MID_SHOT) {
            // Mid-range: moderate athleticism impact
            distanceWeight = 0.6;
        } else {
            // Three-pointers: minimal athleticism impact
            distanceWeight = 0.3;
        }
        
        return MAX_IMPACT * sigmoidValue * distanceWeight;
    }
    
    /**
     * Update player minutes after each play
     */
    public static void updatePlayerMinutes(Map<String, Player> teamOnCourt, int playTime) {
        for (Player p : teamOnCourt.values()) {
            if (p != null) {
                p.secondsPlayed += playTime;
                p.currentStintSeconds += playTime;
            }
        }
    }
}
