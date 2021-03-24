package src;

import java.io.*;
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
     * Generate current play's time from range [4, time] in seconds.
     * 
     * @param time Maximum time of current play
     * @return current play's time
     */
    public static int generateRandomPlayTime(Random random, int time) {
        int currentPlayTime = generateRandomNum(random, Constants.MIN_PLAY_TIME, time);
        
        if (time == 24) {
            if (currentPlayTime <= Constants.TIME_MIN_THLD && generateRandomNum(random) <= Constants.ADD_TIME_PERCENT)
                currentPlayTime += Constants.ADD_TIME;
            if (currentPlayTime >= Constants.TIME_MAX_THLD && generateRandomNum(random) <= Constants.SUB_TIME_PERCENT)
                currentPlayTime -= Constants.SUB_TIME;
        }
        
        return currentPlayTime;
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
                                minor*Math.max(TeamOnCourt.get(pos).midRating, TeamOnCourt.get(pos).threeRating));
            if (attr.equals("orb")) totalRating += TeamOnCourt.get(pos).orbRating;
            if (attr.equals("drb")) totalRating += TeamOnCourt.get(pos).drbRating;
            if (attr.equals("ast")) totalRating += TeamOnCourt.get(pos).astRating;
        }
        double avgRating = totalRating * 1.0 / 5;

        double[] poss = new double[4];
        String[] positions = {"C", "PF", "SF", "SG", "PG"};

        double poss1, poss2, poss3, poss4 = 0;
        int basePoss = 100 / 5;
        if (attr.equals("rating")) {
            // clutch time, 60% to give star players with top-highest rating
            if (currentQuarter >= 4 && quarterTime <= Constants.TIME_LEFT_CLUTCH
                && Math.abs(offenseTeam.totalScore - defenseTeam.totalScore) <= Constants.CLUTCH_DIFF) {
                if (generateRandomNum(random) <= Constants.CLUTCH_PERCENT) {
                    int highestRating = 0;
                    List<Player> selectedPlayerList = new ArrayList<>();
                    Player selectedPlayer = null;

                    for (Player p : TeamOnCourt.values())
                        highestRating = Math.max(highestRating, p.rating);

                    for (Player p : TeamOnCourt.values()) {
                        if (highestRating - p.rating <= Constants.CLUTCH_RATING_RANGE)
                            selectedPlayerList.add(p);
                    }

                    if (selectedPlayerList.size() == 1) selectedPlayer = selectedPlayerList.get(0);
                    else {
                        int randomIdx = generateRandomNum(random, 1, selectedPlayerList.size());
                        selectedPlayer =  selectedPlayerList.get(randomIdx - 1);
                    }

                    return selectedPlayer;
                }
            }

            for (int i = 0; i < poss.length; i++) {
                String currentPos = positions[i];
                poss[i] = (10 * (basePoss + major*TeamOnCourt.get(currentPos).rating + 
                                minor*Math.max(TeamOnCourt.get(currentPos).insideRating, TeamOnCourt.get(currentPos).layupRating) +
                                minor*Math.max(TeamOnCourt.get(currentPos).midRating, TeamOnCourt.get(currentPos).threeRating) - avgRating ));
            }
        } else {
            for (int i = 0; i < poss.length; i++) {
                String currentPos = positions[i];

                if (attr.equals("orb"))
                    poss[i] = Math.max( (1000 * (Constants.REB_AST_SCALE * TeamOnCourt.get(currentPos).orbRating - avgRating) / totalRating), 0);
                else if (attr.equals("drb"))
                    poss[i] = Math.max( (1000 * (Constants.REB_AST_SCALE * TeamOnCourt.get(currentPos).drbRating - avgRating) / totalRating), 0);
                else
                    poss[i] = Math.max( (1000 * (Constants.REB_AST_SCALE * TeamOnCourt.get(currentPos).astRating - avgRating) / totalRating), 0);
            }
        }
        int pick = generateRandomNum(random, 1, 1000);
        if (pick <= poss[0]) return TeamOnCourt.get("C");
        else if (pick <= poss[0] + poss[1]) return TeamOnCourt.get("PF");
        else if (pick <= poss[0] + poss[1] + poss[2]) return TeamOnCourt.get("SF");
        else if (pick <= poss[0] + poss[1] + poss[2] + poss[3]) return TeamOnCourt.get("SG");
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
     * @return 0 - no lose ball, 1 - lose ball but no score, 2 - loss ball and score, 3 - jump ball win
     */
    public static int judgeLoseBall(Random random, Team defenseTeam, Map<String, Player> defenseTeamOnCourt, Player offensePlayer, Player defensePlayer) {
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
        // 0.6% chance to jump ball
        if (poss <= 1) {
            if (generateRandomNum(random) <= Constants.JUMP_BALL_PLAY) {
                String winPlayer = jumpBall(random, offensePlayer.name, defensePlayer.name);
                return winPlayer.equals(offensePlayer.name) ? 3 : 1;
            }
        }

        // 5% chance to turnover
        else if (poss <= 1 + Constants.TURNOVER) {
            offensePlayer.turnover++;
            Comments.getTurnoverComment(offensePlayer.name);
            return 1;
        }

        // steal 
        else if (60 * poss <= 60 * (1 + Constants.TURNOVER) + range) {
            offensePlayer.turnover++;
            defensePlayer.steal++;
            Comments.getStealComment(offensePlayer.name, defensePlayer.name);

            // 30% chance to start a non-fast-break play, 70% chance to start a fast break
            int fastBreak = generateRandomNum(random);
            if (fastBreak <= Constants.NON_FASTBREAK) {
                Comments.getNonFastBreak(defenseTeam.name);
                return 1;
            } else {
                int fastBreakTemp = generateRandomNum(random);
                Player fastBreakFinisher;
                // 60% chance to finish by himself
                if (fastBreakTemp <= Constants.SAME_POS) fastBreakFinisher = defensePlayer;
                // 40% chance to finish by teammate
                else {
                    List<String> otherTeammate = new ArrayList<>();
                    for (String pos : defenseTeamOnCourt.keySet()) 
                        if (pos != defensePlayer.position) otherTeammate.add(pos);

                    if (poss <= Constants.SAME_POS + Constants.OTHER_POS) fastBreakFinisher = defenseTeamOnCourt.get( otherTeammate.get(0) );
                    else if (poss <= Constants.SAME_POS + 2 * Constants.OTHER_POS)
                        fastBreakFinisher = defenseTeamOnCourt.get( otherTeammate.get(1) );
                    else if (poss <= Constants.SAME_POS + 3 * Constants.OTHER_POS)
                        fastBreakFinisher = defenseTeamOnCourt.get( otherTeammate.get(2) );
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
            Comments.getBlockComment(defensePlayer.name);

            // 40% chance to out of bound, 60% change to go to rebound juding
            int outOfBound = generateRandomNum(random);
            if (outOfBound <= Constants.BLOCK_OUT_OF_BOUND) {
                Comments.getOutOfBound(defensePlayer.name);
                return 1;
            } else {
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

            Comments.getReboundComment(rebounder.name, true);
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

            Comments.getReboundComment(rebounder.name, false);
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
        if (previousPlayer.foul == 6 || previousPlayer.flagFoul == 2) {
            Comments.getFoulOutComment(previousPlayer.name, previousPlayer.foul == 6 ? true : false);
            previousPlayer.canOnCourt = false;

            Player currentPlayer = findSubPlayer(previousPlayer, team);
            teamOnCourt.put(previousPlayer.position, currentPlayer);
            Comments.getSubstituteComment(currentPlayer.name, previousPlayer.name);
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
        if (previousPlayer.rotationType == 1 && 
            ((currentQuarter == 1 && previousPlayer.foul == Constants.QUARTER1_PROTECT) ||
             (currentQuarter == 2 && previousPlayer.foul == Constants.QUARTER2_PROTECT) ||
             (currentQuarter == 3 && previousPlayer.foul == Constants.QUARTER3_PROTECT))) {
            Comments.getFoulProtectComment(previousPlayer.name);

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
        int poss = generateRandomNum(random);
        int foulTemp = generateRandomNum(random);

        // 1% offensive foul, 1% defensive foul
        if (poss <= Constants.OFF_FOUL) {
            Player fouler;

            // 60% chance to foul on offensePlayer, 40% on teammates
            if (foulTemp <= Constants.SAME_POS) {
                fouler = offensePlayer;
                Comments.getOffensiveFoul(fouler.name, 1);
            } else {
                List<String> otherTeammate = new ArrayList<>();
                for (String pos : offenseTeamOnCourt.keySet()) 
                    if (pos != offensePlayer.position) otherTeammate.add(pos);

                if (foulTemp <= Constants.SAME_POS + Constants.OTHER_POS) fouler = offenseTeamOnCourt.get( otherTeammate.get(0) );
                else if (foulTemp <= Constants.SAME_POS + 2 * Constants.OTHER_POS) fouler = offenseTeamOnCourt.get( otherTeammate.get(1) );
                else if (foulTemp <= Constants.SAME_POS + 3 * Constants.OTHER_POS) fouler = offenseTeamOnCourt.get( otherTeammate.get(2) );
                else fouler = offenseTeamOnCourt.get( otherTeammate.get(3) );
                Comments.getOffensiveFoul(fouler.name, 2);
            }
            fouler.turnover++;
            fouler.foul++;
            judgeFoulOut(fouler, offenseTeam, offenseTeamOnCourt);
            foulProtect(fouler, offenseTeam, offenseTeamOnCourt, currentQuarter);
            return 1;
        } else if (poss <= Constants.DEF_FOUL) {
            Player fouler;

            // 60% chance to foul on defensePlayer, 40% on teammates
            if (foulTemp <= Constants.SAME_POS) {
                fouler = defensePlayer;
                Comments.getDefensiveFoul(fouler.name, 1);
            } else {
                List<String> otherTeammate = new ArrayList<>();
                for (String pos : defenseTeamOnCourt.keySet()) 
                    if (pos != defensePlayer.position) otherTeammate.add(pos);

                if (poss <= Constants.SAME_POS + Constants.OTHER_POS) fouler = defenseTeamOnCourt.get( otherTeammate.get(0) );
                else if (poss <= Constants.SAME_POS + 2 * Constants.OTHER_POS) fouler = defenseTeamOnCourt.get( otherTeammate.get(1) );
                else if (poss <= Constants.SAME_POS + 3 * Constants.OTHER_POS) fouler = defenseTeamOnCourt.get( otherTeammate.get(2) );
                else fouler = defenseTeamOnCourt.get( otherTeammate.get(3) );
                Comments.getDefensiveFoul(fouler.name, 2);
            }
            fouler.foul++;
            judgeFoulOut(fouler, defenseTeam, defenseTeamOnCourt);
            foulProtect(fouler, defenseTeam, defenseTeamOnCourt, currentQuarter);

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
     * Generates actions when two teams jumping ball before the game starts.
     */
    public static void jumpBall(Random random, Team team1, Team team2) {
        Team winTeam = Utilities.generateRandomNum(random) <= 50 ? team1 : team2;
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
        String winPlayer = Utilities.generateRandomNum(random) <= 50 ? offensePlayer : defensePlayer;
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
        return currentPlayer == null ? previousPlayer : currentPlayer;
    }

    /**
     * Make substitutions for a team.
     * 
     * @param subBench Whether substitution is starter -> bench or bench -> starter
     * @param garbageFlag Whether rareBenches have been substituted
     * @param teameOnCourt Team players on the court
     */
    public static void makeSubstitutions(Team team, boolean subBench, boolean garbageFlag, Map<String, Player> teamOnCourt) {
        for (String pos : team.benches.keySet()) {
            Player previousPlayer = teamOnCourt.get(pos);
            Player currentPlayer = null;

            // subBench True: starter -> bench, False: bench -> starter
            if (subBench) {
                if (garbageFlag && team.rareBenches.containsKey(pos) && team.rareBenches.get(pos).get(0).canOnCourt)
                    currentPlayer = team.rareBenches.get(pos).get(0);
                else currentPlayer = team.benches.get(pos).get(0);
            } else {
                currentPlayer = findSubPlayer(previousPlayer, team);
            }

            Comments.getSubstituteComment(currentPlayer.name, previousPlayer.name);
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
        makeSubstitutions(team1, subBench, garbageFlag, teamOneOnCourt);
        makeSubstitutions(team2, subBench, garbageFlag, teamTwoOnCourt);
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
            case 1:
                distance = generateRandomNum(random, Constants.MIN_CLOSE_SHOT, Constants.MAX_THREE_SHOT);
                break;
            case 2:
                distance = generateRandomNum(random, Constants.MIN_CLOSE_SHOT, Constants.MAX_CLOSE_SHOT);
                break;
            case 3:
                distance = generateRandomNum(random, Constants.MIN_CLOSE_SHOT, Constants.MID_THREE_SHOT);
                break;
            case 4:
                if (shotChoice <= Constants.TYPE4_CLOSE_SHOT)
                    distance = generateRandomNum(random, Constants.MIN_CLOSE_SHOT, Constants.MAX_CLOSE_SHOT);
                else if (shotChoice <= Constants.TYPE4_CLOSE_SHOT + Constants.TYPE4_MID_SHOT)
                    distance = generateRandomNum(random, Constants.MIN_MID_SHOT, Constants.MIN_MID_SHOT);
                else distance = generateRandomNum(random, Constants.MIN_THREE_SHOT, Constants.MAX_THREE_SHOT);
                break;
            case 5:
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
        if (movement.contains("扣")) percentage *= Constants.DUNK_SCALE;
        else if (movement.contains("篮")) percentage += Constants.SHOT_COFF * offensePlayer.layupRating;
        else {
            if (distance <= Constants.MAX_CLOSE_SHOT) percentage += Constants.SHOT_COFF * (offensePlayer.insideRating - Constants.BASE_DEFENSE);
            else if (distance <= Constants.MAX_MID_SHOT) percentage += Constants.SHOT_COFF * (offensePlayer.midRating - Constants.BASE_DEFENSE);
            else percentage += Constants.SHOT_COFF * (offensePlayer.threeRating - Constants.BASE_DEFENSE);
        }

        // players with high astRating will increase percentage
        for (String pos : offenseTeamOnCourt.keySet()) {
            if (pos != offensePlayer.position && offenseTeamOnCourt.get(pos).astRating >= Constants.AST_RATING_THLD1) {
                if (offenseTeamOnCourt.get(pos).astRating <= Constants.AST_RATING_THLD2) percentage += Constants.AST_RATING_BONUS1;
                else if (offenseTeamOnCourt.get(pos).astRating <= Constants.AST_RATING_THLD3) percentage += Constants.AST_RATING_BONUS2;
                else percentage += Constants.AST_RATING_BONUS3;
            }
        }

        // based on defender, adjust percentage
        if (distance <= Constants.MAX_CLOSE_SHOT) percentage -= Constants.DEFENSE_COFF * (defensePlayer.interiorDefense - Constants.DEFENSE_BASE);
        else percentage -= Constants.DEFENSE_COFF * (defensePlayer.perimeterDefense - Constants.DEFENSE_BASE);

        // check defense density
        int temp = generateRandomNum(random);
        if (temp <= Constants.DEFENSE_EASY) percentage += Constants.DEFENSE_EASY_BONUS;
        else if (temp <= Constants.DEFENSE_EASY + Constants.DEFENSE_HARD) percentage -= Constants.DEFENSE_HARD_DEBUFF;

        // offensive consistency & defense player's defensive consistency
        percentage -= Constants.CONSISTENCY_COFF * (99 - offensePlayer.offConst);
        percentage += Constants.CONSISTENCY_COFF * (99 - defensePlayer.defConst);

        // athleticism
        percentage += Constants.ATHLETIC_COFF * (offensePlayer.athleticism - defensePlayer.athleticism);

        // star player bonus
        if (offensePlayer.rating >= Constants.STAR_RATING_THLD1 && offensePlayer.rating < Constants.STAR_RATING_THLD2)
            percentage *= Constants.STAR_RATING_BONUS1;
        else if (offensePlayer.rating >= Constants.STAR_RATING_THLD2 && offensePlayer.rating < Constants.STAR_RATING_THLD3)
            percentage *= Constants.STAR_RATING_BONUS2;
        else if (offensePlayer.rating >= Constants.STAR_RATING_THLD3 && offensePlayer.rating < Constants.STAR_RATING_THLD4)
            percentage *= Constants.STAR_RATING_BONUS3;
        else if (offensePlayer.rating >= Constants.STAR_RATING_THLD4) percentage *= Constants.STAR_RATING_BONUS4;

        // clutch time
        if (!offensePlayer.isMrClutch && currentQuarter >= 4
            && Math.abs(team1.totalScore - team2.totalScore) <= Constants.CLUTCH_DIFF && quarterTime <= Constants.TIME_LEFT_CLUTCH) 
                percentage *= Constants.CLUTCH_SHOT_COFF;
        
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
     * @return 1 - make the shot or make the last free throw  2 - offensive rebound  3 - defensive rebound  4 - out of bound
     */
    public static int judgeMakeShot(Random random, int distance, Player offensePlayer, Player defensePlayer, Team offenseTeam, 
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

            Comments.getMakeShotsComment(offensePlayer.name, defensePlayer.name, distance, movement);
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
                Comments.getAndOneComment(offensePlayer.name);
                judgeFoulOut(defensePlayer, defenseTeam, defenseTeamOnCourt);
                foulProtect(defensePlayer, defenseTeam, defenseTeamOnCourt, currentQuarter);
                int andOneResult = makeFreeThrow(random, offensePlayer, offenseTeamOnCourt, defenseTeamOnCourt, offenseTeam, 1,
                                                 quarterTime, currentQuarter, team1, team2, false);
                return andOneResult;
            }
            return 1;
        }

        // miss the shot
        else {
            int foulTemp = generateRandomNum(random, 1, 10000);
            int drawFoulPercent = calculateFoulPercent(distance, offensePlayer, defensePlayer, false);

            // get a foul
            if (foulTemp <= drawFoulPercent) {
                // 5% flag foul
                if (generateRandomNum(random) <= Constants.FLAG_FOUL) {
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
                foulProtect(defensePlayer, defenseTeam, defenseTeamOnCourt, currentQuarter);

                int freeThrowResult = 0;
                if (distance <= Constants.MAX_MID_SHOT) 
                    freeThrowResult = makeFreeThrow(random, offensePlayer, offenseTeamOnCourt, defenseTeamOnCourt,
                                                    offenseTeam, 2, quarterTime, currentQuarter, team1, team2, false);
                else freeThrowResult = makeFreeThrow(random, offensePlayer, offenseTeamOnCourt, defenseTeamOnCourt,
                                                     offenseTeam, 3, quarterTime, currentQuarter, team1, team2, false);

                return freeThrowResult;
            }

            offensePlayer.shotAttempted++;
            if (distance >= 23) offensePlayer.threeAttempted++;
            Comments.getMissShotsComment(movement, offensePlayer.name);
            if (generateRandomNum(random) <= Constants.STATUS_COMMENT_PERCENT) Comments.getStatusComment(offensePlayer, false);

            // 3% chance that the ball will be out of bound
            if (generateRandomNum(random) <= Constants.SHOT_OUT_OF_BOUND) {
                Comments.shotOutOfBound(offensePlayer.name);
                return 4;
            }

            return judgeRebound(random, offenseTeamOnCourt, defenseTeamOnCourt) ? 2 : 3;
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

            if (generateRandomNum(random) <= player.freeThrowPercent) {
                player.freeThrowAttempted++;
                player.freeThrowMade++;
                player.score++;
                offenseTeam.totalScore++;
                Comments.getMakeFreeThrowComment(count, onlyOneShot);
                Comments.getTimeAndScore(quarterTime, currentQuarter, team1, team2);

                if (timesLeft == 0) return isFlagFoul ? 2 : 1;
            } else {
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
     * Deal with team injury and substitute.
     * 
     * @param team Current team
     * @param teamOnCourt Current team players on the court
     * @return true - A player gets injured, false - No player gets injured
     */
    public static boolean handleInjury(Random random, Map<String, Player> teamOnCourt, Team team) {
        for (String pos : teamOnCourt.keySet()) {
            if (generateRandomNum(random, 1, 1000000) <= 200 - teamOnCourt.get(pos).durability) {
                Player previousPlayer = teamOnCourt.get(pos);
                Player currentPlayer = findSubPlayer(previousPlayer, team);
                previousPlayer.canOnCourt = false;
                teamOnCourt.put(previousPlayer.position, currentPlayer);
                Comments.getInjuryComment(previousPlayer.name);
                Comments.getSubstituteComment(currentPlayer.name, previousPlayer.name);
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
}
