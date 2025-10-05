package src;

import java.util.*;

public class Comments {
    // The Random object to generate random number
    public static Random random = new Random();

    // Random index of corpus
    private static int rdm = -1;

    // The string builder for comment output
    public static StringBuilder sb = new StringBuilder(Constants.MAX_SB_LEN);

    /**
     * Randomly pick one sentence from the corpus and output it.
     * 
     * @param corpus Possible live comments collection.
     * @param output Boolean indicating whether to print output.
     * @return Picked live comment.
     */
    public static String pickStringOutput(String[] corpus, boolean output) {
        rdm = Utilities.generateRandomNum(random, 1, corpus.length) - 1;
        String pickedComment = corpus[rdm];

        if (output) System.out.println(corpus[rdm]);
        return pickedComment;
    }

    /**
     * Get player's last name from full name.
     * In Chinese mode, splits by middle dot (·).
     * In English mode, splits by space and handles suffixes like Jr., Sr., III, etc.
     * 
     * @param name Player's full name
     * @return Player's last name
     */
    public static String getLastName(String name) {
        // Determine delimiter based on language
        String delimiter = (LocalizedStrings.getCurrentLanguage() == LocalizedStrings.Language.CHINESE) ? "·" : " ";
        
        if (!name.contains(delimiter)) return name;

        String[] parts = name.split(delimiter);
        
        // In English mode, check if last part is a suffix (Jr., Sr., III, IV, etc.)
        if (LocalizedStrings.getCurrentLanguage() == LocalizedStrings.Language.ENGLISH) {
            String lastPart = parts[parts.length - 1];
            // Common name suffixes
            if (lastPart.equals("Jr.") || lastPart.equals("Sr.") || 
                lastPart.equals("III") || lastPart.equals("IV") || 
                lastPart.equals("II") || lastPart.equals("V")) {
                // If it's a suffix and there are at least 2 parts before it, return the second to last
                if (parts.length >= 2) {
                    return parts[parts.length - 2];
                }
            }
        }
        
        return parts[parts.length - 1];
    }

    /**
     * Generates comments when two teams start jump ball.
     * 
     * @param team1 Team 1
     * @param team2 Team 2
     * @param winTeam Team that wins jump ball
     */
    public static void getJumpBallComments(Team team1, Team team2, Team winTeam) {
        // Translate team names if in Chinese mode
        String team1Display = Constants.getLocalizedTeamName(team1.name);
        String team2Display = Constants.getLocalizedTeamName(team2.name);
        String winTeamDisplay = Constants.getLocalizedTeamName(winTeam.name);
        
        String intro = CommentLoader.getRandomFormatted(random, "jumpBall.intro", team1Display, team2Display);
        String preparation = CommentLoader.getRandomFormatted(random, "jumpBall.preparation");
        String result = CommentLoader.getRandomFormatted(random, "jumpBall.teamResult", winTeamDisplay);
        
        System.out.println(intro);
        System.out.println(preparation);
        System.out.println(result);
    }

    /**
     * Generates two players jumping ball.
     * 
     * @param offensePlayer Offense player
     * @param defensePlayer Defense player
     * @param winPlayer The player that wins the jumpball
     */
    public static void getJumpBallComments(String offensePlayer, String defensePlayer, String winPlayer) {
        String offenseLastName = getLastName(offensePlayer);
        String defenseLastName = getLastName(defensePlayer);
        String winLastName = getLastName(winPlayer);

        String conflict = CommentLoader.getRandomFormatted(random, "jumpBall.conflict", offenseLastName, defenseLastName);
        String result = CommentLoader.getRandomFormatted(random, "jumpBall.playerResult", winLastName);

        System.out.println(conflict);
        System.out.println(result);
    }
    
    /**
     * Generate shot position comments.
     * 
     * @param get Team that wins jump ball
     * @return Player's shot position string
     */
    public static String getShotPos(Random random, int distance) {
        int degree = Utilities.generateRandomNum(random, 1, 180);

        if (distance <= 10) return CommentLoader.getString("shotPosition.basket");
        else if (degree <= 30 && distance <= 15) return CommentLoader.getString("shotPosition.leftCornerPaint");
        else if (degree <= 30 && distance > 15) return CommentLoader.getString("shotPosition.leftCorner");
        else if (degree <= 60 && distance <= 15) return CommentLoader.getString("shotPosition.left45Paint");
        else if (degree <= 60 && distance > 15) return CommentLoader.getString("shotPosition.left45");
        else if (degree <= 120 && distance <= 20) return CommentLoader.getString("shotPosition.freeThrowLine");
        else if (degree <= 120 && distance > 20) return CommentLoader.getString("shotPosition.topOfKey");
        else if (degree <= 150 && distance <= 15) return CommentLoader.getString("shotPosition.right45Paint");
        else if (degree <= 150 && distance > 15) return CommentLoader.getString("shotPosition.right45");
        else if (degree <= 180 && distance <= 15) return CommentLoader.getString("shotPosition.rightCornerPaint");
        else return CommentLoader.getString("shotPosition.rightCorner");
    }

    /**
     * Generate layup comments.
     * 
     * @return Player's layup comments string
     */
    public static String pickLayup(Random random) {
        String[] resources = CommentLoader.getStringArray("layup");
        return pickStringOutput(resources, false);
    }

    /**
     * Generate dunk comments.
     * 
     * @param dunkerType Player's dunkerType
     * @return Player's dunk comments string
     */
    public static String pickDunk(Random random, Player.DunkerType dunkerType) {
        String path = (dunkerType == Player.DunkerType.EXCELLENT) ? "dunk.basic" : "dunk.advanced";
        String[] resources = CommentLoader.getStringArray(path);
        return pickStringOutput(resources, false);
    }

    /**
     * Generate shot comments.
     * 
     * @param distance Player's shot distance
     * @return Player's shot comments string
     */
    public static String pickShot(Random random, int distance) {
        String path = (distance >= Constants.SHOT_CHOICE_THLD) ? "shot.close" : "shot.far";
        String[] resources = CommentLoader.getStringArray(path);
        String suffix = distance >= Constants.MIN_THREE_SHOT ? LocalizedStrings.get("commentary.shot.threepoint_suffix") : "";
        String result = pickStringOutput(resources, false);

        sb.delete( 0, sb.length() );
        sb.append(result).append(suffix);
        return sb.toString();
    }

    /**
     * Generate shot choice comments.
     * 
     * @param distance Player's shot distance
     * @return Player's shot comments string
     */
    public static String getShotChoice(Random random, Player player, int distance, String shotPos) {
        int temp = Utilities.generateRandomNum(random);
        Player.DunkerType dunkerType = player.dunkerType;
        String movement = "";
        if (distance <= Constants.MAX_CLOSE_SHOT) {
            if (dunkerType == Player.DunkerType.RARELY_DUNK) {
                if (temp <= Constants.TYPE_1_LAYUP) movement = pickLayup(random);
                else if (temp <= Constants.TYPE_1_LAYUP + Constants.TYPE_1_DUNK) movement = pickDunk(random, dunkerType);
            } else if (dunkerType == Player.DunkerType.NORMAL) {
                if (temp <= Constants.TYPE_2_LAYUP) movement = pickLayup(random);
                else if (temp <= Constants.TYPE_2_LAYUP + Constants.TYPE_2_DUNK) movement = pickDunk(random, dunkerType);
            } else {
                if (temp <= Constants.TYPE_3_LAYUP) movement = pickLayup(random);
                else if (temp <= Constants.TYPE_3_LAYUP + Constants.TYPE_3_DUNK) movement = pickDunk(random, dunkerType);
            }

            if (movement.equals("")) movement = pickShot(random, distance);
        } else movement = pickShot(random, distance);

        sb.delete( 0, sb.length() );
        sb.append(distance).append(LocalizedStrings.get("commentary.distance.feet"));
        if (Utilities.generateRandomNum(random) <= Constants.SHOT_POSITION_PERCENT && LocalizedStrings.getCurrentLanguage() == LocalizedStrings.Language.CHINESE) sb.append(shotPos);
        sb.append(movement).append("!");

        System.out.println(sb.toString());
        return movement;
    }

    /**
     * Generate player celebration comments with a specified percentage.
     * 
     * @param name Player's name
     * @param percent The percent to generate celebrate comment
     */
    public static void getCelebrateComment(String name, int percent) {
        if (Utilities.generateRandomNum(random) <= percent) {
            String lastName = getLastName(name);
            String comment = CommentLoader.getRandomFormatted(random, "celebrate", lastName);
            System.out.println(comment);
        }
    }

    /**
     * Generate player upset comments with a specified percentage.
     * 
     * @param name Player's name
     * @param percent The percent to generate upset comment
     */
    public static void getUpsetComment(String name, int percent) {
        if (Utilities.generateRandomNum(random) <= percent) {
            String lastName = getLastName(name);
            String comment = CommentLoader.getRandomFormatted(random, "upset", lastName);
            System.out.println(comment);
        }
    }

    /**
     * Generate comments when player gets ball.
     * 
     * @param teamName Offense team
     * @param name Player name
     * @param defensePlayer Defense player name
     */
    public static void getBallComment(String teamName, String name, String defensePlayer) {
        String lastName = getLastName(name);
        String defenseLastName = getLastName(defensePlayer);

        String comment1 = CommentLoader.getRandomFormatted(random, "getBall.teamOffense", teamName);
        String comment2 = CommentLoader.getRandomFormatted(random, "getBall.playerReceive", lastName);
        String comment3 = CommentLoader.getRandomFormatted(random, "getBall.defense", defenseLastName);
        
        sb.delete( 0, sb.length() );
        sb.append("\n").append(comment1)
          .append("\n").append(comment2)
          .append("\n").append(comment3);
        System.out.println(sb.toString());
    }

    /**
     * Generate player turnover comments.
     * 
     * @param name Player's name
     */
    public static void getTurnoverComment(String name) {
        String lastName = getLastName(name);
        String comment = CommentLoader.getRandomFormatted(random, "turnover", lastName);
        System.out.println(comment);
        getUpsetComment(name, Constants.UPSET_HIGH_PERCENT);
    }

    /**
     * Generate comments after a non fast-break turnover.
     * 
     * @param team Team name
     */
    public static void getNonFastBreak(String team) {
        String comment = CommentLoader.getRandomFormatted(random, "nonFastBreak", team);
        System.out.println(comment);
    }

    /**
     * Generate comments after a steal.
     * 
     * @param offensePlayer Offense player name
     * @param defensePlayer Defense player name
     */
    public static void getStealComment(String offensePlayer, String defensePlayer) {
        String offenseLastName = getLastName(offensePlayer);
        String defenseLastName = getLastName(defensePlayer);
        String comment = CommentLoader.getRandomFormatted(random, "steal", defenseLastName, offenseLastName);
        System.out.println(comment);
    }

    /**
     * Generate comments after a block.
     * 
     * @param defensePlayer Defense player name
     */
    public static void getBlockComment(String defensePlayer) {
        String defenseLastName = getLastName(defensePlayer);
        String comment = CommentLoader.getRandomFormatted(random, "block", defenseLastName);
        System.out.println(comment);
    }

    /**
     * Generate comments when player makes a free throw.
     * 
     * @param count The number of ongoing free throw
     * @param onlyOneShot Whether only one free throw in total
     */
    public static void getMakeFreeThrowComment(int count, boolean onlyOneShot) {
        String countPrefix = onlyOneShot ? 
            LocalizedStrings.get("commentary.freethrow.label") : 
            count + LocalizedStrings.get("commentary.freethrow.attempt_suffix");
        String[] resources = CommentLoader.getStringArray("freeThrow.make");
        sb.delete( 0, sb.length() );
        sb.append(countPrefix).append(pickStringOutput(resources, false));
        System.out.println(sb.toString());
    }

    /**
     * Generate comments when player misses a free throw.
     * 
     * @param count The number of ongoing free throw
     * @param onlyOneShot Whether only one free throw in total
     */
    public static void getMissFreeThrowComment(int count, boolean onlyOneShot) {
        String countPrefix = onlyOneShot ? 
            LocalizedStrings.get("commentary.freethrow.label") : 
            count + LocalizedStrings.get("commentary.freethrow.attempt_suffix");
        String[] resources = CommentLoader.getStringArray("freeThrow.miss");
        sb.delete( 0, sb.length() );
        sb.append(countPrefix).append(pickStringOutput(resources, false));
        System.out.println(sb.toString());
    }

    /**
     * Generate comments for starters in garbage time.
     * 
     * @param team The team to be commented
     */
    public static void getStartersComment(Team team) {
        int randomIndex = Utilities.generateRandomNum(random, 0, team.starters.size() - 1);
        Player randomPlayer = (Player)team.starters.values().toArray()[randomIndex];
        String playerName = getLastName(randomPlayer.getDisplayName());
        String comment = CommentLoader.getRandomFormatted(random, "startersGarbageTime", playerName);
        System.out.println(comment);
    }

    /**
     * Generate comments when player makes And-one shot.
     * 
     * @param name Player name
     */
    public static void getAndOneComment(String name) {
        String[] resources = CommentLoader.getStringArray("andOne");
        pickStringOutput(resources, true);
        getCelebrateComment(name, Constants.CELEBRATE_HIGH_PERCENT);
    }

    /**
     * Generate comments when one team reaches quarter foul times bonus.
     * 
     * @param offenseTeam Offense team name
     * @param defenseTeam Defense team name
     */
    public static void getReachFoulTimes(String offenseTeam, String defenseTeam) {
        String comment = CommentLoader.getRandomFormatted(random, "reachFoulTimes", defenseTeam, offenseTeam);
        System.out.println(comment);
    }

    /**
     * Generate comments when the player draws a foul.
     * 
     * @param offensePlayer Offense player name
     * @param defensePlayer Defense player name
     */
    public static void getFoulComment(String offensePlayer, String defensePlayer) {
        String offenseLastName = getLastName(offensePlayer);
        String defenseLastName = getLastName(defensePlayer);
        String comment = CommentLoader.getRandomFormatted(random, "foul.defensive", defenseLastName, offenseLastName);
        System.out.println(comment);
    }

    /**
     * Generate comments when the player draws a flagrant foul.
     * 
     * @param offensePlayer Offense player name
     * @param defensePlayer Defense player name
     */
    public static void getFlagFoulComment(String offensePlayer, String defensePlayer) {
        String offenseLastName = getLastName(offensePlayer);
        String defenseLastName = getLastName(defensePlayer);
        String comment = CommentLoader.getRandomFormatted(random, "foul.flagrant", defenseLastName, offenseLastName);
        System.out.println(comment);
    }

    /**
     * Generate comments when the player prepares to go to the free throw line.
     * 
     * @param player Free throw player name
     */
    public static void getFreeThrowPrepareComment(String player) {
        String playerLastName = getLastName(player);
        String comment = CommentLoader.getRandomFormatted(random, "freeThrow.prepare", playerLastName);
        System.out.println(comment);
    }

    /**
     * Generate foul challenge comments.
     * 
     * @param teamName Challenge team name
     * @return Whether the challenge succeed
     */
    public static boolean getChallengeComment(String teamName) {
        String requestComment = CommentLoader.getRandomFormatted(random, "challenge.request", teamName);
        System.out.println(requestComment);

        // challenge successful
        if (Utilities.generateRandomNum(random) <= Constants.CHALLENGE_SUCCESS) {
            String successComment = CommentLoader.getRandomFormatted(random, "challenge.success", teamName);
            System.out.println(successComment);
            return true;
        } else {
            String failComment = CommentLoader.getRandomFormatted(random, "challenge.failure", teamName);
            System.out.println(failComment);
            return false;
        }
    }

    /**
     * Generate comments when the player makes a shot.
     * 
     * @param offenseName Offense player name
     * @param defenseName Defense player name
     * @param distance Player's shot distance
     * @param movement Player's shot choice
     */
    public static void getMakeShotsComment(String offenseName, String defenseName, int distance, String movement) {
        String comment;
        String defenseLastName = getLastName(defenseName);

        // dunk or normal 2-point shot
        if (movement.contains(LocalizedStrings.get("commentary.shot.dunk_marker"))) {
            comment = CommentLoader.getRandomFormatted(random, "makeShot.dunk", defenseLastName);
        } else if (distance < Constants.MIN_THREE_SHOT) {
            comment = CommentLoader.getRandomFormatted(random, "makeShot.twoPoint", defenseLastName);
        } else {
            comment = CommentLoader.getRandomFormatted(random, "makeShot.threePoint", defenseLastName);
        }

        System.out.println(comment);
        getCelebrateComment(offenseName, Constants.CELEBRATE_LOW_PERCENT);
    }

    /**
     * Generate comments when the player misses a shot.
     * 
     * @param movement Player's shot choice
     * @param offenseName Player's name
     */
    public static void getMissShotsComment(String movement, String offenseName) {
        String offenseLastName = getLastName(offenseName);
        String comment;

        if (movement.contains(LocalizedStrings.get("commentary.shot.dunk_marker"))) {
            comment = CommentLoader.getRandomFormatted(random, "missShot.dunk");
        } else {
            comment = CommentLoader.getRandomFormatted(random, "missShot.normal");
        }

        System.out.println(comment);
        getUpsetComment(offenseLastName, Constants.UPSET_LOW_PERCENT);
    }

    /**
     * Generate comments when the player plays well / plays bad.
     * 
     * @param player Player object
     * @param isGoodstatus Whether the player is in good status or bad status
     */
    public static void getStatusComment(Player player, boolean isGoodstatus) {
        if (isGoodstatus &&
            (player.score >= Constants.MIN_GOOD_SCORE ||
            (player.shotMade >= Constants.MIN_SHOT_MADE && player.shotMade * 1.0 / player.shotAttempted >= Constants.MIN_GOOD_SHOT_PERCENT))
            || !isGoodstatus &&
            (player.shotAttempted >= Constants.MIN_SHOT_ATTEMPTED && player.shotMade * 1.0 / player.shotAttempted <= Constants.MAX_BAD_SHOT_PERCENT)) {
            
            String lastName = getLastName(player.getDisplayName());
            String comment;

            if (isGoodstatus) {
                comment = CommentLoader.getRandomFormatted(random, "playerStatus.good", lastName);
            } else {
                comment = CommentLoader.getRandomFormatted(random, "playerStatus.bad", lastName);
            }

            String suffix = LocalizedStrings.get("commentary.player_status.currently") + 
                player.shotAttempted + LocalizedStrings.get("commentary.player_status.fg_made") + 
                player.shotMade + LocalizedStrings.get("commentary.player_status.fg_total") + 
                player.score + LocalizedStrings.get("commentary.player_status.points_suffix");
            System.out.println(comment);
            System.out.println(suffix);
        }
    }

    /**
     * Generate comments when the player grabs a rebound.
     * 
     * @param name Player name
     * @param isOrb Whether the current rebound is offensive rebound or defensive rebound
     */
    public static void getReboundComment(String name, boolean isOrb) {
        String lastName = getLastName(name);
        String rebType = isOrb ? 
            LocalizedStrings.get("commentary.rebound.offensive") : 
            LocalizedStrings.get("commentary.rebound.defensive");
        String comment = CommentLoader.getRandomFormatted(random, "rebound", lastName, rebType);
        System.out.println(comment);
    }

    /**
     * Generate comments when the defense player blocks the ball out-of-bound.
     * 
     * @param defensePlayer Defense player name
     */
    public static void getOutOfBound(String defenseName) {
        String comment = CommentLoader.getRandomFormatted(random, "outOfBound");
        System.out.println(comment);
        getCelebrateComment(defenseName, Constants.CELEBRATE_HIGH_PERCENT);
    }

    /**
     * Generate comments when the offense player misses the shot and the ball is out-of-bound.
     * 
     * @param offensePlayer Offense player name
     */
    public static void shotOutOfBound(String offensePlayer) {
        String comment = CommentLoader.getRandomFormatted(random, "shotOutOfBound");
        System.out.println(comment);
        getUpsetComment(offensePlayer, Constants.UPSET_LOW_PERCENT);
    }

    /**
     * Generate comments when the player get injured.
     * 
     * @param name Player name
     */
    public static void getInjuryComment(String name) {
        String lastName = getLastName(name);
        String comment = CommentLoader.getRandomFormatted(random, "injury", lastName);
        System.out.println(comment);
    }

    /**
     * Generate comments when the player makes fast-break after a turnover.
     * 
     * @param teamName Team name
     * @param offensePlayer Offense player name
     */
    public static void getFastBreak(String teamName, String offensePlayer) {
        String offenseLastName = getLastName(offensePlayer);
        String comment = CommentLoader.getRandomFormatted(random, "fastBreak", teamName, offenseLastName);
        System.out.println(comment);
        getCelebrateComment(offensePlayer, Constants.CELEBRATE_HIGH_PERCENT);
    }

    /**
     * Generate comments when the player makes fast-break after a turnover.
     * 
     * @param offensePlayer Offense player name
     * @param type Offensive foul type (1 - Charging foul, 2 - Illegal screen foul)
     */
    public static void getOffensiveFoul(String offensePlayer, int type) {
        String offenseLastName = getLastName(offensePlayer);
        String comment;

        if (type == 1) {
            comment = CommentLoader.getRandomFormatted(random, "foul.charging", offenseLastName);
        } else if (type == 2) {
            comment = CommentLoader.getRandomFormatted(random, "foul.illegalScreen", offenseLastName);
        } else {
            return;
        }

        System.out.println(comment);
        getUpsetComment(offensePlayer, Constants.UPSET_HIGH_PERCENT);
    }

    /**
     * Generate comments when the player makes fast-break after a turnover.
     * 
     * @param defensePlayer Defense player name
     * @param type Defensive foul type (1 - Blocking foul, 2 - Reach in foul)
     */
    public static void getDefensiveFoul(String defensePlayer, int type) {
        String defenseLastName = getLastName(defensePlayer);
        String comment;

        if (type == 1) {
            comment = CommentLoader.getRandomFormatted(random, "foul.blocking", defenseLastName);
        } else if (type == 2) {
            comment = CommentLoader.getRandomFormatted(random, "foul.reachIn", defenseLastName);
        } else {
            return;
        }

        System.out.println(comment);
        getUpsetComment(defensePlayer, Constants.UPSET_HIGH_PERCENT);
    }

    /**
     * Generate comments when a team calls timeout.
     * 
     * @param teamName Team name
     */
    public static void getTimeOutComment(String teamName) {
        String comment = CommentLoader.getRandomFormatted(random, "timeout", teamName);
        System.out.println("\n" + comment);
    }

    /**
     * Generate comments when a player gets fouled out.
     * 
     * @param name Player name
     * @param isNormalFoul Player gets fouled by normal foul or flagrant foul
     */
    public static void getFoulOutComment(String name, boolean isNormalFoul) {
        String lastName = getLastName(name);
        String comment;

        if (isNormalFoul) {
            comment = CommentLoader.getRandomFormatted(random, "foulOut.normal", lastName);
        } else {
            comment = CommentLoader.getRandomFormatted(random, "foulOut.flagrant", lastName);
        }

        System.out.println(comment);
    }

    /**
     * Generate comments when a player gets substituted to prevent too much fouls.
     * 
     * @param name Player's name
     */
    public static void getFoulProtectComment(String name) {
        String lastName = getLastName(name);
        String comment = CommentLoader.getRandomFormatted(random, "foulProtect", lastName);
        System.out.println(comment);
    }

    /**
     * Generate comments when a player gets substituted.
     * 
     * @param currentPlayer In player name
     * @param previousPlayer Out player name
     */
    public static void getSubstituteComment(String currentPlayer, String previousPlayer) {
        if (!currentPlayer.equals(previousPlayer)) {
            sb.delete( 0, sb.length() );
            sb.append(currentPlayer)
              .append(LocalizedStrings.get("commentary.substitution.replace"))
              .append(previousPlayer).append("!");
            System.out.println(sb.toString());
        }
    }

    /**
     * Print substitution prefix to indicate substitutions are about to happen.
     * @param teamName The name of the team making substitutions
     */
    public static void getSubstitutionPrefix(String teamName) {
        System.out.println("\n════════════════ " + teamName + " " + 
            LocalizedStrings.get("commentary.substitution.prefix") + " ════════════════");
    }

    /**
     * Print current time and score during game.
     * Always displays away team first, home team second.
     * 
     * @param time Current quarter time left
     * @param currentQuarter Current quarter number
     * @param awayTeam The away team (displayed first)
     * @param homeTeam The home team (displayed second)
     */
    public static void getTimeAndScore(int time, int currentQuarter, Team awayTeam, Team homeTeam) {
        String minute = String.valueOf(time / 60);
        String second = String.valueOf(time % 60);
        if (time % 60 < 10) second = "0" + second;

        sb.delete( 0, sb.length() );
        if (currentQuarter <= 4) {
            sb.append(LocalizedStrings.get("commentary.time.quarter_prefix"))
              .append(currentQuarter)
              .append(LocalizedStrings.get("commentary.time.quarter_suffix"))
              .append(" ");
        } else {
            sb.append(LocalizedStrings.get("commentary.time.overtime_prefix"))
              .append(currentQuarter - 4)
              .append(LocalizedStrings.get("commentary.time.quarter_suffix"))
              .append(" ");
        }
        
        // Translate team names if in Chinese mode
        String awayTeamDisplay = Constants.getLocalizedTeamName(awayTeam.name);
        String homeTeamDisplay = Constants.getLocalizedTeamName(homeTeam.name);
        
        sb.append(minute).append(":").append(second)
          .append(LocalizedStrings.get("commentary.time.seconds")).append("  ")
          .append(awayTeamDisplay).append(" ").append(awayTeam.totalScore)
          .append(":").append(homeTeam.totalScore).append(" ").append(homeTeamDisplay);

        System.out.println(sb.toString()); 
    }

    /**
     * Generate comments when a quarter ends.
     * Always displays away team first, home team second.
     * 
     * @param currentQuarter Current quarter number
     * @param awayTeam The away team (displayed first)
     * @param homeTeam The home team (displayed second)
     */
    public static void quarterEnd(int currentQuarter, Team awayTeam, Team homeTeam) {
        sb.delete( 0, sb.length() );
        
        // Translate team names if in Chinese mode
        String awayTeamDisplay = Constants.getLocalizedTeamName(awayTeam.name);
        String homeTeamDisplay = Constants.getLocalizedTeamName(homeTeam.name);
        
        sb.append("\n")
          .append(LocalizedStrings.get("commentary.time.quarter_prefix"))
          .append(currentQuarter)
          .append(LocalizedStrings.get("commentary.time.quarter_suffix"))
          .append(LocalizedStrings.get("commentary.time.quarter_end"))
          .append("!\n")
          .append(LocalizedStrings.get("commentary.time.current_score"))
          .append(" ")
          .append(awayTeamDisplay).append(" ").append(awayTeam.totalScore)
          .append(":").append(homeTeam.totalScore).append(" ").append(homeTeamDisplay)
          .append("\n");

        sb.append("\n==============================================================================\n");

        sb.append("\n")
          .append(LocalizedStrings.get("commentary.time.quarter_prefix"))
          .append(currentQuarter + 1)
          .append(LocalizedStrings.get("commentary.time.quarter_suffix"))
          .append(LocalizedStrings.get("commentary.time.game_start"))
          .append("!");

        System.out.println(sb.toString()); 
    }

    /**
     * Generate comments when regular time ends.
     * Always displays away team first, home team second.
     * 
     * @param awayTeam The away team (displayed first)
     * @param homeTeam The home team (displayed second)
     */
    public static void regularEnd(Team awayTeam, Team homeTeam) {
        sb.delete( 0, sb.length() );
        sb.append("\n")
          .append(LocalizedStrings.get("commentary.regular_end.time_up"))
          .append("!\n")
          .append(LocalizedStrings.get("commentary.regular_end.tied_prefix"))
          .append(awayTeam.totalScore)
          .append(LocalizedStrings.get("commentary.regular_end.tied_suffix"))
          .append("!\n");
        sb.append("\n==============================================================================\n");
        sb.append(LocalizedStrings.get("commentary.regular_end.overtime_start"))
          .append("!");

        System.out.println(sb.toString()); 
    }

    /**
     * Generate comments when the game ends.
     * Always displays away team first, home team second.
     * 
     * @param awayTeam The away team (displayed first)
     * @param homeTeam The home team (displayed second)
     * @param awayScores away team's scores of all quarters
     * @param homeScores home team's scores of all quarters
     */
    public static void gameEnd(Team awayTeam, Team homeTeam, List<Integer> awayScores, List<Integer> homeScores) {
        sb.delete( 0, sb.length() );

        sb.append("\n==============================================================================\n");
        
        // Translate team names if in Chinese mode
        String awayTeamDisplay = Constants.getLocalizedTeamName(awayTeam.name);
        String homeTeamDisplay = Constants.getLocalizedTeamName(homeTeam.name);
        
        sb.append("\n")
          .append(LocalizedStrings.get("commentary.game_end.full_time"))
          .append("!\n")
          .append(LocalizedStrings.get("commentary.game_end.final_score"))
          .append(" ")
          .append(awayTeamDisplay).append(" ").append(awayTeam.totalScore)
          .append(":").append(homeTeam.totalScore).append(" ").append(homeTeamDisplay)
          .append("\n");

        String winTeam = awayTeam.totalScore >= homeTeam.totalScore ? awayTeamDisplay : homeTeamDisplay;
        String loseTeam = (awayTeam.totalScore >= homeTeam.totalScore) ? homeTeamDisplay : awayTeamDisplay;
        sb.append(LocalizedStrings.get("commentary.game_end.congratulations"))
          .append(winTeam)
          .append(LocalizedStrings.get("commentary.game_end.win_by"))
          .append(Math.max(awayTeam.totalScore, homeTeam.totalScore) - Math.min(awayTeam.totalScore, homeTeam.totalScore))
          .append(LocalizedStrings.get("commentary.game_end.points_advantage"))
          .append(LocalizedStrings.get("commentary.game_end.defeat"))
          .append(loseTeam)
          .append("!\n");

        sb.append("\n")
          .append(LocalizedStrings.get("commentary.game_end.quarter_details"))
          .append(":\n")
          .append(awayTeamDisplay).append("\n").append(awayScores.get(0)).append("\t");
        for (int i = 1; i < awayScores.size(); i++) sb.append(awayScores.get(i) - awayScores.get(i - 1)).append("\t");
        sb.append("\n");
        sb.append(homeTeamDisplay).append("\n").append(homeScores.get(0)).append("\t");
        for (int i = 1; i < homeScores.size(); i++) sb.append(homeScores.get(i) - homeScores.get(i - 1)).append("\t");
        sb.append("\n");

        System.out.print(sb.toString());

        getTeamData(awayTeam);
        getTeamData(homeTeam);
    }

    /**
     * Print a player's stat.
     * 
     * @param player Player object
     */
    public static void getPlayerData(Player player) {
        sb.delete( 0, sb.length() );

        if (player.hasBeenOnCourt) {
            int minutes = player.secondsPlayed / 60;
            int seconds = player.secondsPlayed % 60;
            
            sb.append(player.getDisplayName()).append(": ")
              .append(player.score).append(LocalizedStrings.get("commentary.player_stats.points")).append(", ")
              .append(player.rebound).append(LocalizedStrings.get("commentary.player_stats.rebounds")).append(", ")
              .append(player.assist).append(LocalizedStrings.get("commentary.player_stats.assists")).append(", ")
              .append(player.steal).append(LocalizedStrings.get("commentary.player_stats.steals")).append(", ")
              .append(player.block).append(LocalizedStrings.get("commentary.player_stats.blocks")).append(", ")
              .append(player.turnover).append(LocalizedStrings.get("commentary.player_stats.turnovers")).append(", ")
              .append(player.foul).append(LocalizedStrings.get("commentary.player_stats.fouls")).append(" ")
              .append(LocalizedStrings.get("commentary.player_stats.fieldgoals"))
              .append(player.shotMade).append("-").append(player.shotAttempted).append(", ")
              .append(LocalizedStrings.get("commentary.player_stats.threepointers"))
              .append(player.threeMade).append("-").append(player.threeAttempted).append(", ")
              .append(LocalizedStrings.get("commentary.player_stats.freethrows"))
              .append(player.freeThrowMade).append("-").append(player.freeThrowAttempted).append(" ")
              .append(LocalizedStrings.get("commentary.player_stats.playing_time")).append(" ")
              .append(minutes).append(LocalizedStrings.get("commentary.player_stats.minutes"))
              .append(seconds).append(LocalizedStrings.get("commentary.player_stats.seconds"));
        } else {
            sb.append(player.getDisplayName()).append(": ")
              .append(LocalizedStrings.get("commentary.player_stats.dnp"));
        }
        
        System.out.println(sb.toString());
    }

    /**
     * Print a team's stat.
     * 
     * @param team Team name
     */
    public static void getTeamData(Team team) {
        sb.delete( 0, sb.length() );

        // display each player's data and update total statistics
        sb.append("\n").append(Constants.getLocalizedTeamName(team.name)).append(LocalizedStrings.get("commentary.team_stats.header")).append(":");
        System.out.println(sb.toString());

        for (int i = 0; i < team.players.size(); i++) {
            Player currentPlayer = team.players.get(i);
            getPlayerData(currentPlayer);
            team.totalRebound += currentPlayer.rebound;
            team.totalAssist += currentPlayer.assist;
            team.totalSteal += currentPlayer.steal;
            team.totalBlock += currentPlayer.block;
            team.totalFoul += currentPlayer.foul;
            team.totalTurnover += currentPlayer.turnover;
            team.totalShotAttempted += currentPlayer.shotAttempted;
            team.totalShotMade += currentPlayer.shotMade;
            team.total3Attempted += currentPlayer.threeAttempted;
            team.total3Made += currentPlayer.threeMade;
            team.totalFreeAttempted += currentPlayer.freeThrowAttempted;
            team.totalFreeMade += currentPlayer.freeThrowMade;
        }

        sb.delete( 0, sb.length() );
        sb.append("\n").append(Constants.getLocalizedTeamName(team.name)).append(LocalizedStrings.get("commentary.team_stats.total")).append(":\n");
        sb.append(team.totalScore).append(LocalizedStrings.get("commentary.player_stats.points")).append(", ")
          .append(team.totalRebound).append(LocalizedStrings.get("commentary.player_stats.rebounds")).append(", ")
          .append(team.totalAssist).append(LocalizedStrings.get("commentary.player_stats.assists")).append(", ")
          .append(team.totalSteal).append(LocalizedStrings.get("commentary.player_stats.steals")).append(", ")
          .append(team.totalBlock).append(LocalizedStrings.get("commentary.player_stats.blocks")).append(", ")
          .append(team.totalTurnover).append(LocalizedStrings.get("commentary.player_stats.turnovers")).append(", ")
          .append(team.totalFoul).append(LocalizedStrings.get("commentary.player_stats.fouls")).append("\n");

        double totalShotPercentage = team.totalShotAttempted != 0 ? team.totalShotMade * 100.0 / team.totalShotAttempted : 0.0;
        double total3Percentage = team.total3Attempted != 0 ? team.total3Made * 100.0 / team.total3Attempted : 0.0;
        double totalFreePercentage = team.totalFreeAttempted != 0 ? team.totalFreeMade * 100.0 / team.totalFreeAttempted : 0.0;

        sb.append(LocalizedStrings.get("commentary.player_stats.fieldgoals")).append(": ")
          .append(team.totalShotMade).append("-").append(team.totalShotAttempted)
          .append("(").append(String.format("%.2f", totalShotPercentage)).append("%)")
          .append("  ").append(LocalizedStrings.get("commentary.player_stats.threepointers")).append(": ")
          .append(team.total3Made).append("-").append(team.total3Attempted)
          .append("(").append(String.format("%.2f", total3Percentage)).append("%)")
          .append("  ").append(LocalizedStrings.get("commentary.player_stats.freethrows")).append(": ")
          .append(team.totalFreeMade).append("-").append(team.totalFreeAttempted)
          .append("(").append(String.format("%.2f", totalFreePercentage)).append("%)");

        System.out.println(sb.toString());
    }
}
