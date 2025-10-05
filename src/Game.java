package src;

import java.io.*;
import java.util.*;

public class Game {
    // NBA daily schedule file
    public String schedulePath;

    // current year and next year's prefix
    public String currentYear;
    public String nextYear;

    // result directories
    public String singleResultsPath;
    public String regularResultsPath;
    public String regularStatsPath;
    public String playoffsResultsPath;
    public String standingFilePath;
    public String statFilePath;
    public String recapPath;

    // PrintStream for result output
    PrintStream ps;

    // List to store game recaps for the season
    private List<GameRecapData> seasonRecaps;
    
    // Store last playoff game teams for recap collection
    private Team lastPlayoffTeam1;
    private Team lastPlayoffTeam2;
    
    // Store current season standings for recap W/L records
    private Map<String, List<Integer>> currentStanding;

    /**
     * Inner class to store game recap data
     */
    public static class GameRecapData {
        public String date;
        public String awayTeam;
        public String homeTeam;
        public int awayScore;
        public int homeScore;
        public double awayFgPct;
        public double homeFgPct;
        public double away3pPct;
        public double home3pPct;
        public List<PlayerRecapData> awayTopPlayers;
        public List<PlayerRecapData> homeTopPlayers;
        // W/L records after this game
        public int awayWins;
        public int awayLosses;
        public int homeWins;
        public int homeLosses;

        public GameRecapData(String date, String awayTeam, String homeTeam, int awayScore, int homeScore,
                           double awayFgPct, double homeFgPct, double away3pPct, double home3pPct) {
            this.date = date;
            this.awayTeam = awayTeam;
            this.homeTeam = homeTeam;
            this.awayScore = awayScore;
            this.homeScore = homeScore;
            this.awayFgPct = awayFgPct;
            this.homeFgPct = homeFgPct;
            this.away3pPct = away3pPct;
            this.home3pPct = home3pPct;
            this.awayTopPlayers = new ArrayList<>();
            this.homeTopPlayers = new ArrayList<>();
            this.awayWins = 0;
            this.awayLosses = 0;
            this.homeWins = 0;
            this.homeLosses = 0;
        }
    }

    /**
     * Inner class to store player recap data
     */
    public static class PlayerRecapData {
        public String name;
        public int points;
        public int rebounds;
        public int assists;
        public String marker; // Special markers like ⭐, 💯

        public PlayerRecapData(String name, int points, int rebounds, int assists, String marker) {
            this.name = name;
            this.points = points;
            this.rebounds = rebounds;
            this.assists = assists;
            this.marker = marker;
        }
    }

    /**
     * Construct new Game instance to host a game or a season
     */
    public Game() {
        this.schedulePath = Constants.SCHEDULE_PATH;

        // current year and next year's prefix
        this.currentYear = Constants.CURRENT_YEAR;
        this.nextYear = Constants.NEXT_YEAR;

        this.singleResultsPath = Constants.SINGLE_GAME_DIR;
        this.regularResultsPath = Constants.REGULAR_GAMES_DIR;
        this.regularStatsPath = Constants.REGULAR_STATS_DIR;
        this.playoffsResultsPath = Constants.PLAYOFFS_GAMES_DIR;
        this.standingFilePath = Constants.REGULAR_STATS_DIR + Constants.STANDING_NAME;
        this.statFilePath = Constants.REGULAR_STATS_DIR + Constants.STAT_NAME;
        this.recapPath = Constants.RECAP_DIR;

        // initialize file paths
        Utilities.initializePath(this.regularResultsPath);
        Utilities.initializePath(this.regularStatsPath);
        Utilities.initializePath(this.playoffsResultsPath);
        Utilities.initializePath(this.recapPath);
        Utilities.initializePath(Constants.PLAYIN_GAMES_DIR);
        
        // initialize recap list
        this.seasonRecaps = new ArrayList<>();
    }

    /**
     * Host a game between two teams.
     * @param info By default, info is the date when the game is hosted; for playoff games, info is the round and game number.
     * @param gameMode Whether the game is a single game (default), regular season game or playoffs game
     * @param stat SeasonStats object, store regular season's player stats
     * @param seriesTeam1 For playoff series, the first team in series order (for consistent file naming). Can be null.
     * @param seriesTeam2 For playoff series, the second team in series order (for consistent file naming). Can be null.
     * @return Winner of the game
     */
    public String hostGame(String team1Name, String team2Name, String info, String gameMode, SeasonStats stat,
                          String seriesTeam1, String seriesTeam2) throws Exception {
        Random random = new Random();

        // For playoff series, use series order for file naming; otherwise use actual game order
        String fileTeam1 = (seriesTeam1 != null) ? seriesTeam1 : team1Name;
        String fileTeam2 = (seriesTeam2 != null) ? seriesTeam2 : team2Name;
        
        // Get team names for file path
        String team1FileName = LocalizedStrings.getCurrentLanguage() == LocalizedStrings.Language.CHINESE ?
                               Constants.translateToChinese(fileTeam1) : fileTeam1;
        String team2FileName = LocalizedStrings.getCurrentLanguage() == LocalizedStrings.Language.CHINESE ?
                               Constants.translateToChinese(fileTeam2) : fileTeam2;

        // each game's result file
        String filePath;
        if (gameMode.equals("playoffs") || gameMode.equals("playin")) {
            String dir = gameMode.equals("playin") ? Constants.PLAYIN_GAMES_DIR : playoffsResultsPath;
            filePath = dir + team1FileName + team2FileName + "-" + info + Constants.RESULT_EXTENSION;
        }
        else if (gameMode.equals("regular")) {
            // games in Oct, Nov, Dec are hosted in current year
            if (info.charAt(0) == '1') filePath = regularResultsPath + currentYear + info + "-" + team1FileName + team2FileName + Constants.RESULT_EXTENSION;
            else filePath = regularResultsPath + nextYear + info + "-" + team1FileName + team2FileName + Constants.RESULT_EXTENSION;
        }
        else {
            filePath = singleResultsPath + team1FileName + team2FileName + Constants.RESULT_EXTENSION;
        }
                                    
        ps = new PrintStream(filePath);
        System.setOut(ps);

        Team team1 = new Team(team1Name);
        Team team2 = new Team(team2Name);

        Map<String, Player> teamOneOnCourt = new HashMap<>();
        Map<String, Player> teamTwoOnCourt = new HashMap<>();
        for (String pos : team1.starters.keySet()) {
            Player p = team1.starters.get(pos);
            teamOneOnCourt.put(pos, p);
            p.isOnCourt = true;
            p.secondsPlayed = 0;
            p.currentStintSeconds = 0;
            p.lastSubbedOutTime = 0;
        }
        for (String pos : team2.starters.keySet()) {
            Player p = team2.starters.get(pos);
            teamTwoOnCourt.put(pos, p);
            p.isOnCourt = true;
            p.secondsPlayed = 0;
            p.currentStintSeconds = 0;
            p.lastSubbedOutTime = 0;
        }

        int quarterTime = 720;
        int currentQuarter = 1;
        boolean hasSubstituted = false;
        boolean hasGarbageSubstituted = false;
        boolean startersBack = false;

        // decide whether current play is second chance play
        boolean isSecondChance = false;

        List<Integer> team1Scores = new LinkedList<>();
        List<Integer> team2Scores = new LinkedList<>();

        // Print game header for playoff and play-in games showing home/away
        if (gameMode.equals("playoffs") || gameMode.equals("playin")) {
            String team1Display = LocalizedStrings.getCurrentLanguage() == LocalizedStrings.Language.CHINESE ?
                                 Constants.translateToChinese(team1.name) : team1.name;
            String team2Display = LocalizedStrings.getCurrentLanguage() == LocalizedStrings.Language.CHINESE ?
                                 Constants.translateToChinese(team2.name) : team2.name;
            
            String awayLabel = LocalizedStrings.get("game.away");
            String homeLabel = LocalizedStrings.get("game.home");
            
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.println(team1Display + " (" + awayLabel + ") @ " + team2Display + " (" + homeLabel + ")");
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.println();
        }

        // start game
        Utilities.jumpBall(random, team1, team2);
        
        // Track total game time for minutes management
        int totalGameTime = 0;

        // Play-by-play simulation
        while (quarterTime >= 0) {
            // decide play time and substitution
            int currentPlayTime;
            if (!isSecondChance) {
                currentPlayTime = quarterTime > 24 ? Utilities.generateRandomPlayTime(random, 24) : quarterTime;
            } else {
                currentPlayTime = quarterTime > 24 ? Utilities.generateRandomPlayTime(random, 14) : quarterTime;
                isSecondChance = false;
            }
            
            // Update player minutes for both teams
            Utilities.updatePlayerMinutes(teamOneOnCourt, currentPlayTime);
            Utilities.updatePlayerMinutes(teamTwoOnCourt, currentPlayTime);
            totalGameTime += currentPlayTime;
            
            // after each quarter, update two teams' quarter scores
            if (quarterTime == 0) Utilities.updateQuarterScores(team1Scores, team2Scores, team1.totalScore, team2.totalScore);
            
            // quarters end or games end
            if (quarterTime == 0 && currentQuarter <= 3) {
                Comments.quarterEnd(currentQuarter, team1, team2);
                quarterTime = 720;
                currentQuarter += 1;
                team1.quarterFoul = 0;
                team2.quarterFoul = 0;
                hasSubstituted = false; 
            } else if (quarterTime == 0 && currentQuarter >= 4 && team1.totalScore != team2.totalScore) {
                Comments.gameEnd(team1, team2, team1Scores, team2Scores);
                team1.totalScoreAllowed = team2.totalScore;
                team2.totalScoreAllowed = team1.totalScore;
                break;
            } else if (quarterTime == 0 && currentQuarter >= 4 && team1.totalScore == team2.totalScore) {
                quarterTime = 300;
                currentQuarter += 1;
                team1.quarterFoul = 0;
                team2.quarterFoul = 0;
                Comments.regularEnd(team1, team2);
            }

            // Check if it's garbage time
            int scoreDiff = Math.abs(team1.totalScore - team2.totalScore);
            boolean isGarbageTime = (currentQuarter == 4 && scoreDiff >= Constants.DIFF1 && quarterTime <= Constants.TIME_LEFT1) ||
                                   (currentQuarter == 4 && scoreDiff >= Constants.DIFF2 && quarterTime <= Constants.TIME_LEFT2) ||
                                   (currentQuarter == 4 && scoreDiff >= Constants.DIFF3 && quarterTime <= Constants.TIME_LEFT3);

            // Intelligent substitution system - each team independently decides whether to substitute
            Utilities.checkIntelligentSubstitutions(random, team1, teamOneOnCourt, currentQuarter, quarterTime, 
                                                   totalGameTime, team1, team2, isGarbageTime);
            Utilities.checkIntelligentSubstitutions(random, team2, teamTwoOnCourt, currentQuarter, quarterTime,
                                                   totalGameTime, team1, team2, isGarbageTime);

            // get offsense team, defense team, offsense player, defense player
            Team offenseTeam = team1.hasBall ? team1 : team2;
            Team defenseTeam = !team1.hasBall ? team1 : team2;
            Map<String, Player> offenseTeamOnCourt = team1.hasBall ? teamOneOnCourt : teamTwoOnCourt;
            Map<String, Player> defenseTeamOnCourt = !team1.hasBall ? teamOneOnCourt : teamTwoOnCourt;
            Player offensePlayer = Utilities.choosePlayerBasedOnRating(random, offenseTeamOnCourt, "rating",
                                                                       currentQuarter, quarterTime, offenseTeam, defenseTeam);
            
            if (offensePlayer == null) {
                System.err.println("ERROR: offensePlayer is null!");
                System.err.println("Offense team: " + offenseTeam.name);
                System.err.println("Defense team: " + defenseTeam.name);
                System.err.println("OffenseTeamOnCourt size: " + offenseTeamOnCourt.size());
                System.err.println("Team1 starters size: " + team1.starters.size());
                System.err.println("Team2 starters size: " + team2.starters.size());
                throw new RuntimeException("Cannot continue game without offense player");
            }
            
            Player defensePlayer = Utilities.chooseDefensePlayer(random, offensePlayer, defenseTeamOnCourt);
            Comments.getBallComment(Constants.getLocalizedTeamName(offenseTeam.name), offensePlayer.getDisplayName(), defensePlayer.getDisplayName());

            // judge ball possession lost: turnover, steal, jumpball lose
            Utilities.LoseBallResult loseBallResult = Utilities.judgeLoseBall(random, defenseTeam, defenseTeamOnCourt, offensePlayer, defensePlayer);
            if (loseBallResult == Utilities.LoseBallResult.LOSE_BALL_NO_SCORE) {
                offenseTeam.hasBall = false;
                defenseTeam.hasBall = true;
                quarterTime -= currentPlayTime;
                continue;
            } else if (loseBallResult == Utilities.LoseBallResult.LOSE_BALL_AND_SCORE) {
                quarterTime -= currentPlayTime;
                Comments.getTimeAndScore(quarterTime, currentQuarter, team1, team2);
                continue;
            } else if (loseBallResult == Utilities.LoseBallResult.JUMP_BALL_WIN) {
                quarterTime -= currentPlayTime;
                continue;
            }

            // judge offense foul or defense foul (no free-throw)
            Utilities.FoulResult foulResult = Utilities.judgeNormalFoul(random, offenseTeamOnCourt, defenseTeamOnCourt, offensePlayer, defensePlayer,
                                                      offenseTeam, defenseTeam, currentQuarter, quarterTime, team1, team2);
            if (foulResult == Utilities.FoulResult.OFFENSIVE_FOUL) {
                offenseTeam.hasBall = false;
                defenseTeam.hasBall = true;
                quarterTime -= currentPlayTime;
                continue;
            }
            else if (foulResult == Utilities.FoulResult.DEFENSIVE_FOUL) {
                quarterTime -= currentPlayTime;
                continue;
            }

            // check if any team player get injured
            if (Utilities.judgeInjury(random, offenseTeamOnCourt, defenseTeamOnCourt, offenseTeam, defenseTeam)) continue;

            // get shot distance, position, choice
            int distance = Utilities.getShotDistance(random, offensePlayer);
            String shotPos = Comments.getShotPos(random, distance);
            String shotMovement = Comments.getShotChoice(random, offensePlayer, distance, shotPos);

            // judge block
            Utilities.BlockResult blockResult = Utilities.judgeBlock(random, distance, offenseTeamOnCourt, defenseTeamOnCourt, offensePlayer, defensePlayer);
            if (blockResult == Utilities.BlockResult.BLOCK_OFFENSIVE_REBOUND) {
                quarterTime -= currentPlayTime;
                continue;
            }
            else if (blockResult == Utilities.BlockResult.BLOCK_DEFENSIVE_REBOUND) {
                offenseTeam.hasBall = false;
                defenseTeam.hasBall = true;
                quarterTime -= currentPlayTime;
                continue;
            }

            // judge shot percentage
            double percentage = Utilities.calculatePercentage(random, distance, offensePlayer, defensePlayer, offenseTeamOnCourt,
                                                              shotMovement, quarterTime, currentQuarter, offenseTeam, defenseTeam);

            // judge whether to make the shot
            Utilities.ShotResult shotResult = Utilities.judgeMakeShot(random, distance, offensePlayer, defensePlayer, offenseTeam, defenseTeam, offenseTeamOnCourt,
                                                    defenseTeamOnCourt, percentage, quarterTime - currentPlayTime, currentQuarter, team1, team2,
                                                    shotMovement);
            if (shotResult == Utilities.ShotResult.MADE_SHOT || shotResult == Utilities.ShotResult.DEFENSIVE_REBOUND || shotResult == Utilities.ShotResult.OUT_OF_BOUNDS) {
                offenseTeam.hasBall = false;
                defenseTeam.hasBall = true;
                quarterTime -= currentPlayTime;
                continue;
            }
            else if (shotResult == Utilities.ShotResult.OFFENSIVE_REBOUND) {
                quarterTime -= currentPlayTime;
                isSecondChance = true;
                continue;
            }
        }
        
        // for regular season games, update season stat and collect recap data
        if (gameMode.equals("regular")) {
            for (Player p : team1.players) stat.updatePlayerStats(p);
            for (Player p : team2.players) stat.updatePlayerStats(p);
            stat.updateTeamStats(team1);
            stat.updateTeamStats(team2);
            
            // Collect recap data
            collectGameRecap(team1, team2, info);
        }
        
        // for play-in games, collect recap data
        if (gameMode.equals("playin")) {
            collectPlayInGameData(team1, team2, info);
        }
        
        // for playoff games, store the teams for series recap collection
        if (gameMode.equals("playoffs")) {
            lastPlayoffTeam1 = team1;
            lastPlayoffTeam2 = team2;
        }

        return team1.totalScore > team2.totalScore ? team1Name : team2Name;
    }

    /**
     * Function overloading, temporarily generate a fake SeasonStats() object for playoff games.
     * 
     * @return Winner or the game
     */
    public String hostGame(String team1Name, String team2Name, String info, String gameMode) throws Exception {
        return hostGame(team1Name, team2Name, info, gameMode, new SeasonStats(), null, null);
    }
    
    /**
     * Function overloading for backward compatibility with SeasonStats parameter.
     * 
     * @return Winner or the game
     */
    public String hostGame(String team1Name, String team2Name, String info, String gameMode, SeasonStats stat) throws Exception {
        return hostGame(team1Name, team2Name, info, gameMode, stat, null, null);
    }

    /**
     * Function overloading, temporarily generate a fake SeasonStats() object for single games.
     * 
     * @return Winner or the game
     */
    public String hostGame(String team1Name, String team2Name) throws Exception {
        return hostGame(team1Name, team2Name, "01-01", "single", new SeasonStats(), null, null);
    }

    /**
     * Collect game recap data from a completed game
     */
    private void collectGameRecap(Team team1, Team team2, String date) {
        // Calculate team shooting percentages
        double team1FgPct = team1.totalShotMade > 0 ? (team1.totalShotMade * 100.0 / team1.totalShotAttempted) : 0.0;
        double team2FgPct = team2.totalShotMade > 0 ? (team2.totalShotMade * 100.0 / team2.totalShotAttempted) : 0.0;
        double team13pPct = team1.total3Made > 0 ? (team1.total3Made * 100.0 / team1.total3Attempted) : 0.0;
        double team23pPct = team2.total3Made > 0 ? (team2.total3Made * 100.0 / team2.total3Attempted) : 0.0;

        GameRecapData recap = new GameRecapData(date, team1.name, team2.name, team1.totalScore, team2.totalScore,
                                                 team1FgPct, team2FgPct, team13pPct, team23pPct);

        // Get top 3 players from each team
        recap.awayTopPlayers = getTopPlayers(team1);
        recap.homeTopPlayers = getTopPlayers(team2);
        
        // W/L records will be updated after the game in hostSeason()
        // Don't read from currentStanding here as it hasn't been updated yet

        seasonRecaps.add(recap);
    }

    /**
     * Get top 3 performing players from a team based on points
     */
    private List<PlayerRecapData> getTopPlayers(Team team) {
        List<PlayerRecapData> topPlayers = new ArrayList<>();
        
        // Sort players by points (descending)
        List<Player> sortedPlayers = new ArrayList<>(team.players);
        sortedPlayers.sort((p1, p2) -> p2.score - p1.score);

        // Take top 3 players
        for (int i = 0; i < Math.min(3, sortedPlayers.size()); i++) {
            Player p = sortedPlayers.get(i);
            String marker = "";
            
            // Count stats >= 10 for triple-double check
            int doubleDigitStats = 0;
            if (p.score >= 10) doubleDigitStats++;
            if (p.rebound >= 10) doubleDigitStats++;
            if (p.assist >= 10) doubleDigitStats++;
            if (p.steal >= 10) doubleDigitStats++;
            if (p.block >= 10) doubleDigitStats++;
            
            // Count stats >= 8 for near triple-double check
            int nearDoubleStats = 0;
            if (p.score >= 8) nearDoubleStats++;
            if (p.rebound >= 8) nearDoubleStats++;
            if (p.assist >= 8) nearDoubleStats++;
            if (p.steal >= 8) nearDoubleStats++;
            if (p.block >= 8) nearDoubleStats++;
            
            // Add special markers for exceptional performances (priority order)
            if (p.score >= 50) {
                marker = "🌟 ";  // 50+ points
            } else if (p.score >= 40) {
                marker = "⭐ ";  // 40+ points
            } else if (doubleDigitStats >= 3) {
                marker = "🔥 ";  // Triple double
            } else if ((p.score >= 15 && p.rebound >= 15) || (p.score >= 15 && p.assist >= 15) || 
                       (p.rebound >= 15 && p.assist >= 15)) {
                marker = "💯 ";  // Big double-double (15+15)
            } else if (nearDoubleStats >= 3) {
                marker = "💪 ";  // Near triple double (3 stats >= 8)
            }
            
            topPlayers.add(new PlayerRecapData(p.getDisplayName(), p.score, p.rebound, p.assist, marker));
        }

        return topPlayers;
    }

    /**
     * Write season recaps to file, grouped by date
     */
    private void writeSeasonRecap() {
        try {
            PrintStream recapPs = new PrintStream(recapPath + Constants.RECAP_NAME);
            System.setOut(recapPs);

            // Group recaps by date
            Map<String, List<GameRecapData>> recapsByDate = new LinkedHashMap<>();
            for (GameRecapData recap : seasonRecaps) {
                recapsByDate.computeIfAbsent(recap.date, k -> new ArrayList<>()).add(recap);
            }

            // Write recaps grouped by date
            for (Map.Entry<String, List<GameRecapData>> entry : recapsByDate.entrySet()) {
                String date = entry.getKey();
                List<GameRecapData> gamesOnDate = entry.getValue();

                // Date header
                System.out.println();
                System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                System.out.println(LocalizedStrings.get("recap.header") + " - " + date);
                System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                System.out.println();

                // Write each game on this date
                for (GameRecapData game : gamesOnDate) {
                    writeGameRecap(game);
                }

                System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            }

            recapPs.close();
        } catch (Exception e) {
            System.err.println("Error writing season recap: " + e.getMessage());
        }
    }

    /**
     * Write a single game recap
     */
    private void writeGameRecap(GameRecapData game) {
        // Translate team names based on current language
        String awayTeamDisplay = LocalizedStrings.getCurrentLanguage() == LocalizedStrings.Language.CHINESE ?
                                Constants.translateToChinese(game.awayTeam) : game.awayTeam;
        String homeTeamDisplay = LocalizedStrings.getCurrentLanguage() == LocalizedStrings.Language.CHINESE ?
                                Constants.translateToChinese(game.homeTeam) : game.homeTeam;
        
        // Build W/L record strings
        String awayRecord = "";
        String homeRecord = "";
        if (game.awayWins > 0 || game.awayLosses > 0) {
            awayRecord = "(" + game.awayWins + "-" + game.awayLosses + ") ";
        }
        if (game.homeWins > 0 || game.homeLosses > 0) {
            homeRecord = " (" + game.homeWins + "-" + game.homeLosses + ")";
        }
        
        // Final score line with W/L records
        System.out.println(LocalizedStrings.get("game.finalscore") + ": " + awayRecord + awayTeamDisplay + " " + game.awayScore + 
                          " " + LocalizedStrings.get("game.at") + " " + homeTeamDisplay + " " + game.homeScore + homeRecord);
        
        // Shooting percentages line
        System.out.println(LocalizedStrings.get("stat.fieldgoal.pct") + ": " + 
                          String.format("%.1f", game.awayFgPct) + "% vs " + String.format("%.1f", game.homeFgPct) + "% | " +
                          LocalizedStrings.get("stat.threepoint.pct") + ": " + 
                          String.format("%.1f", game.away3pPct) + "% vs " + String.format("%.1f", game.home3pPct) + "%");
        
        // Away team top players
        String awayLabel = LocalizedStrings.get("game.away");
        System.out.println("  " + awayTeamDisplay + " (" + awayLabel + "):");
        for (PlayerRecapData player : game.awayTopPlayers) {
            System.out.println("    " + player.marker + player.name + " - " + 
                             player.points + LocalizedStrings.get("stat.points.short") + " " +
                             player.rebounds + LocalizedStrings.get("stat.rebounds.short") + " " +
                             player.assists + LocalizedStrings.get("stat.assists.short"));
        }
        
        // Home team top players
        String homeLabel = LocalizedStrings.get("game.home");
        System.out.println("  " + homeTeamDisplay + " (" + homeLabel + "):");
        for (PlayerRecapData player : game.homeTopPlayers) {
            System.out.println("    " + player.marker + player.name + " - " + 
                             player.points + LocalizedStrings.get("stat.points.short") + " " +
                             player.rebounds + LocalizedStrings.get("stat.rebounds.short") + " " +
                             player.assists + LocalizedStrings.get("stat.assists.short"));
        }
        
        System.out.println();
    }

    /**
     * Simulate a season (regular season + playoffs).
     */
    public void hostSeason() {
        SeasonStats stat = new SeasonStats();

        // Always use English team names for internal keys (schedule uses English names)
        String[] eastDivision = Constants.EAST_TEAMS_EN;
        String[] westDivision = Constants.WEST_TEAMS_EN;

        // <teamName, [totalWin, totalLose, division(0 west, 1 east)]>
        Map<String, List<Integer>> standing = new HashMap<>();
        for (String team : eastDivision) {
            List<Integer> record = new ArrayList<>(Arrays.asList(0, 0, 1));
            standing.put(team, record);
        }
        for (String team : westDivision) {
            List<Integer> record = new ArrayList<>(Arrays.asList(0, 0, 0));
            standing.put(team, record);
        }
        
        // Store standing reference for recap W/L records
        this.currentStanding = standing;

        // simulate regular season and calculate each team's win / lose count
        try (BufferedReader file = new BufferedReader(new InputStreamReader(new FileInputStream(schedulePath), "UTF-8"))) {
            String line;
            String currentDate = "";
            while ((line = file.readLine()) != null) {
                // skip empty line
                if (line.length() == 0) continue;
                
                // current line only contains a date, set as current date
                else if ((line.charAt(0) == '0' || line.charAt(0) == '1') && line.charAt(2) == '-') currentDate = line;

                // teams line, host game between two teams
                else {
                    String[] parts = line.split(" ");
                    String team1, team2;
                    
                    // Handle "Trail Blazers" which has a space in the name
                    if (parts.length == 3) {
                        // Could be "Team1 Trail Blazers" or "Trail Blazers Team2"
                        if (parts[0].equals("Trail") && parts[1].equals("Blazers")) {
                            team1 = "Trail Blazers";
                            team2 = parts[2];
                        } else if (parts[1].equals("Trail") && parts[2].equals("Blazers")) {
                            team1 = parts[0];
                            team2 = "Trail Blazers";
                        } else {
                            // Fallback: shouldn't happen
                            team1 = parts[0];
                            team2 = parts[1];
                        }
                    } else if (parts.length == 4) {
                        // Both teams have spaces: "Trail Blazers Trail Blazers" (shouldn't happen in practice)
                        team1 = parts[0] + " " + parts[1];
                        team2 = parts[2] + " " + parts[3];
                    } else {
                        // Normal case: two single-word team names
                        team1 = parts[0];
                        team2 = parts[1];
                    }
                    
                    String gameWinner = hostGame(team1, team2, currentDate, "regular", stat);
                    String gameLoser = gameWinner.equals(team1) ? team2 : team1;

                    int winnerWin = standing.get(gameWinner).get(0);
                    standing.get(gameWinner).set(0, winnerWin + 1);

                    int loserLose = standing.get(gameLoser).get(1);
                    standing.get(gameLoser).set(1, loserLose + 1);
                    
                    // Update the most recent recap with updated W/L records
                    if (!seasonRecaps.isEmpty()) {
                        GameRecapData lastRecap = seasonRecaps.get(seasonRecaps.size() - 1);
                        // team1 is away, team2 is home
                        List<Integer> team1Record = standing.get(team1);
                        List<Integer> team2Record = standing.get(team2);
                        
                        if (team1Record != null) {
                            lastRecap.awayWins = team1Record.get(0);
                            lastRecap.awayLosses = team1Record.get(1);
                        }
                        if (team2Record != null) {
                            lastRecap.homeWins = team2Record.get(0);
                            lastRecap.homeLosses = team2Record.get(1);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error during regular season simulation: " + e.getMessage());
            e.printStackTrace();
        }

        // generate stats leaderboard
        try {
            ps = new PrintStream(statFilePath);
            System.setOut(ps);

            System.out.println(LocalizedStrings.get("leaderboard.player_ppg"));
            stat.printPlayerRank(stat.playerPerScores);

            System.out.println("\n" + LocalizedStrings.get("leaderboard.player_rpg"));
            stat.printPlayerRank(stat.playerPerRebs);

            System.out.println("\n" + LocalizedStrings.get("leaderboard.player_apg"));
            stat.printPlayerRank(stat.playerPerAsts);

            System.out.println("\n" + LocalizedStrings.get("leaderboard.player_spg"));
            stat.printPlayerRank(stat.playerPerStls);

            System.out.println("\n" + LocalizedStrings.get("leaderboard.player_bpg"));
            stat.printPlayerRank(stat.playerPerBlks);

            System.out.println("\n" + LocalizedStrings.get("leaderboard.player_ftmpg"));
            stat.printPlayerRank(stat.playerPerFts);

            System.out.println("\n" + LocalizedStrings.get("leaderboard.player_3pmpg"));
            stat.printPlayerRank(stat.playerPerThrees);

            System.out.println("\n" + LocalizedStrings.get("leaderboard.team_ppg"));
            stat.printTeamRank(stat.teamPerScores);

            System.out.println("\n" + LocalizedStrings.get("leaderboard.team_papg"));
            stat.printTeamRank(stat.teamPerScoresAllowed);

            System.out.println("\n" + LocalizedStrings.get("leaderboard.team_fgmpg"));
            stat.printTeamRank(stat.teamPerShotsMade);

            System.out.println("\n" + LocalizedStrings.get("leaderboard.team_fgpct"));
            stat.printTeamRank(stat.teamPerShotsPercent);

            System.out.println("\n" + LocalizedStrings.get("leaderboard.team_3ppct"));
            stat.printTeamRank(stat.teamPerThreePercent);
        } catch (Exception e) {
            System.out.println(e);
        }

        // generate west & east divisions top 8 seeds
        try {
            ps = new PrintStream(standingFilePath);
            System.setOut(ps);

            // top 8 seeds in both divisions
            String[] westTemp = new String[8];
            String[] eastTemp = new String[8];
            
            // value is totalWin for easy sorting
            Map<String, Integer> westStanding = new HashMap<>();
            Map<String, Integer> eastStanding = new HashMap<>();

            for (String key : standing.keySet()) {
                if (standing.get(key).get(2) == 0) westStanding.put(key, standing.get(key).get(0));
                if (standing.get(key).get(2) == 1) eastStanding.put(key, standing.get(key).get(0));
            }

            Comparator<Map.Entry<String, Integer>> vComparator = (o1, o2) -> {
                // first sort by total win
                if (o1.getValue() != o2.getValue()) {
                    return o2.getValue() - o1.getValue();
                } else {
                    int o1Lose = standing.get(o1.getKey()).get(1);
                    int o2Lose = standing.get(o2.getKey()).get(1);

                    double o1winRate = o1.getValue() * 100.0 / (o1.getValue() + o1Lose);
                    double o2winRate = o2.getValue() * 100.0 / (o2.getValue() + o2Lose);
                    
                    // then sort by win rate
                    if (o2winRate > o1winRate) return 1;
                    else if (o1winRate < o2winRate) return -1;
                    else return o1Lose - o2Lose; // same total win and win rate, sort by total lose
                }
            };
            
            System.out.println(LocalizedStrings.get("conference.west_standings"));
            List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(westStanding.entrySet());
            Collections.sort(list, vComparator);
            SeasonStats.printStanding(standing, list);
            
            // Copy top 10 teams for play-in tournament
            String[] westTop10 = new String[10];
            for (int i = 0; i < 10; i++) westTop10[i] = list.get(i).getKey();

            System.out.println("\n" + LocalizedStrings.get("conference.east_standings"));
            list = new ArrayList<Map.Entry<String, Integer>>(eastStanding.entrySet());
            Collections.sort(list, vComparator);
            SeasonStats.printStanding(standing, list);
            
            // Copy top 10 teams for play-in tournament
            String[] eastTop10 = new String[10];
            for (int i = 0; i < 10; i++) eastTop10[i] = list.get(i).getKey();

            // Write season recap before play-in
            writeSeasonRecap();

            // Host play-in tournament to determine final 7th and 8th seeds
            String[] finalWestSeeds = hostPlayInTournament(westTop10, true);
            String[] finalEastSeeds = hostPlayInTournament(eastTop10, false);

            // Write play-in recap
            writePlayInRecap();

            // Combine top 6 + play-in winners (reuse westTemp and eastTemp arrays)
            System.arraycopy(westTop10, 0, westTemp, 0, 6);
            westTemp[6] = finalWestSeeds[0]; // 7th seed
            westTemp[7] = finalWestSeeds[1]; // 8th seed
            System.arraycopy(eastTop10, 0, eastTemp, 0, 6);
            eastTemp[6] = finalEastSeeds[0]; // 7th seed
            eastTemp[7] = finalEastSeeds[1]; // 8th seed

            List<String> westSeeds = reorderSeeds(westTemp);
            List<String> eastSeeds = reorderSeeds(eastTemp);

            hostPlayoffs(westSeeds, eastSeeds);
        } catch (Exception e) {}
    }

    // List to store play-in recap data
    private List<PlayInRecapData> playinRecaps = new ArrayList<>();
    
    // List to store playoff recap data
    private List<PlayoffRoundRecap> playoffRecaps = new ArrayList<>();
    private String currentPlayoffRound = "";
    
    // Map to track team seeding for home court advantage (lower number = higher seed)
    private Map<String, Integer> teamSeedMap = new HashMap<>();

    /**
     * Inner class to store playoff round recap data
     */
    public static class PlayoffRoundRecap {
        public String roundName;
        public List<SeriesRecap> series;

        public PlayoffRoundRecap(String roundName) {
            this.roundName = roundName;
            this.series = new ArrayList<>();
        }
    }

    /**
     * Inner class to store series recap
     */
    public static class SeriesRecap {
        public String team1;
        public String team2;
        public String seriesName;
        public int team1Wins;
        public int team2Wins;
        public String winner;
        public List<GameRecapData> games;
        
        // Series MVP data
        public String mvpPlayerName;      // Chinese name (for internal tracking)
        public String mvpPlayerEnglishName;  // English name
        public double mvpAvgPoints;
        public double mvpAvgRebounds;
        public double mvpAvgAssists;
        public double mvpAvgSteals;
        public double mvpAvgBlocks;

        public SeriesRecap(String team1, String team2, String seriesName) {
            this.team1 = team1;
            this.team2 = team2;
            this.seriesName = seriesName;
            this.team1Wins = 0;
            this.team2Wins = 0;
            this.games = new ArrayList<>();
        }
    }

    /**
     * Inner class to store play-in game recap data
     */
    public static class PlayInRecapData {
        public String roundName;
        public GameRecapData gameData;
        public String winnerStatus;
        public String loserStatus;

        public PlayInRecapData(String roundName, GameRecapData gameData, String winnerStatus, String loserStatus) {
            this.roundName = roundName;
            this.gameData = gameData;
            this.winnerStatus = winnerStatus;
            this.loserStatus = loserStatus;
        }
    }

    /**
     * Inner class to store playoff series recap data
     */
    public static class PlayoffSeriesRecapData {
        public String roundName;        // e.g., "西部首轮" or "WesternFirst Round"
        public String team1;            // English name (internal)
        public String team2;            // English name (internal)
        public int team1Wins;
        public int team2Wins;
        public List<GameRecapData> games;
        public String seriesMVP;        // Player name (will be localized on display)
        public double mvpPoints;
        public double mvpRebounds;
        public double mvpAssists;
        public double mvpSteals;
        public double mvpBlocks;

        public PlayoffSeriesRecapData(String roundName, String team1, String team2) {
            this.roundName = roundName;
            this.team1 = team1;
            this.team2 = team2;
            this.team1Wins = 0;
            this.team2Wins = 0;
            this.games = new ArrayList<>();
        }
    }

    /**
     * Host play-in tournament for one conference
     * 
     * @param top10 Array of top 10 teams in the conference
     * @param isWest Whether this is Western conference
     * @return Array with [7th seed, 8th seed]
     */
    private String[] hostPlayInTournament(String[] top10, boolean isWest) throws Exception {
        String conference = isWest ? LocalizedStrings.get("conference.west") : LocalizedStrings.get("conference.east");
        String[] finalSeeds = new String[2];
        
        // Game 1: 7v8 - Winner gets 7th seed, loser goes to final
        // 7th seed has home court advantage
        String team7 = top10[6];
        String team8 = top10[7];
        String round7v8 = conference + LocalizedStrings.get("playin.7v8");
        String winner7v8 = hostGame(team8, team7, round7v8, "playin"); // team8 away, team7 home
        String loser7v8 = winner7v8.equals(team7) ? team8 : team7;
        
        // Collect 7v8 recap status
        collectPlayInRecap(team7, team8, round7v8, winner7v8, loser7v8,
                          LocalizedStrings.get("playin.secured_prefix") + " " + LocalizedStrings.get("playin.secured.7"),
                          LocalizedStrings.get("playin.advance_prefix") + " " + LocalizedStrings.get("playin.advanced"));
        
        finalSeeds[0] = winner7v8; // 7th seed secured
        
        // Game 2: 9v10 - Winner goes to final, loser eliminated
        // 9th seed has home court advantage
        String team9 = top10[8];
        String team10 = top10[9];
        String round9v10 = conference + LocalizedStrings.get("playin.9v10");
        String winner9v10 = hostGame(team10, team9, round9v10, "playin"); // team10 away, team9 home
        String loser9v10 = winner9v10.equals(team9) ? team10 : team9;
        
        // Collect 9v10 recap status
        collectPlayInRecap(team9, team10, round9v10, winner9v10, loser9v10,
                          LocalizedStrings.get("playin.advance_prefix") + " " + LocalizedStrings.get("playin.advanced"),
                          LocalizedStrings.get("playin.eliminated_prefix") + " " + LocalizedStrings.get("playin.eliminated"));
        
        // Game 3: 8th seed battle - Winner gets 8th seed, loser eliminated
        // Higher seed (loser of 7v8, who was 7th or 8th) has home court advantage
        String roundFinal = conference + LocalizedStrings.get("playin.final_battle");
        // loser7v8 is either 7th or 8th seed (higher), winner9v10 is 9th or 10th seed (lower)
        String winner8th = hostGame(winner9v10, loser7v8, roundFinal, "playin"); // winner9v10 away, loser7v8 home
        String loser8th = winner8th.equals(loser7v8) ? winner9v10 : loser7v8;
        
        // Collect final recap status
        collectPlayInRecap(loser7v8, winner9v10, roundFinal, winner8th, loser8th,
                          LocalizedStrings.get("playin.secured_prefix") + " " + LocalizedStrings.get("playin.secured.8"),
                          LocalizedStrings.get("playin.eliminated_prefix") + " " + LocalizedStrings.get("playin.eliminated"));
        
        finalSeeds[1] = winner8th; // 8th seed secured
        
        return finalSeeds;
    }

    /**
     * Collect play-in game data right after the game ends
     */
    private void collectPlayInGameData(Team team1, Team team2, String roundName) {
        // Calculate team shooting percentages
        double team1FgPct = team1.totalShotMade > 0 ? (team1.totalShotMade * 100.0 / team1.totalShotAttempted) : 0.0;
        double team2FgPct = team2.totalShotMade > 0 ? (team2.totalShotMade * 100.0 / team2.totalShotAttempted) : 0.0;
        double team13pPct = team1.total3Made > 0 ? (team1.total3Made * 100.0 / team1.total3Attempted) : 0.0;
        double team23pPct = team2.total3Made > 0 ? (team2.total3Made * 100.0 / team2.total3Attempted) : 0.0;

        GameRecapData gameData = new GameRecapData("", team1.name, team2.name, team1.totalScore, team2.totalScore,
                                                     team1FgPct, team2FgPct, team13pPct, team23pPct);

        // Get top 3 players from each team
        gameData.awayTopPlayers = getTopPlayers(team1);
        gameData.homeTopPlayers = getTopPlayers(team2);
        
        // Store with roundName for later status assignment
        PlayInRecapData recap = new PlayInRecapData(roundName, gameData, "", "");
        playinRecaps.add(recap);
    }

    /**
     * Collect play-in game recap
     */
    private void collectPlayInRecap(String team1, String team2, String roundName, String winner, String loser,
                                    String winnerStatus, String loserStatus) {
        // Find the most recently added recap (should be for this game)
        if (!playinRecaps.isEmpty()) {
            PlayInRecapData recap = playinRecaps.get(playinRecaps.size() - 1);
            recap.winnerStatus = winnerStatus;
            recap.loserStatus = loserStatus;
        }
    }

    /**
     * Write play-in recap file
     */
    private void writePlayInRecap() {
        try {
            PrintStream playinPs = new PrintStream(recapPath + Constants.PLAYIN_RECAP_NAME);
            System.setOut(playinPs);

            for (PlayInRecapData recap : playinRecaps) {
                System.out.println();
                System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                System.out.println(recap.roundName);
                System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                System.out.println();
                
                writeGameRecap(recap.gameData);
                
                // Determine winner and loser
                String winnerEnglish = recap.gameData.awayScore > recap.gameData.homeScore ? 
                               recap.gameData.awayTeam : recap.gameData.homeTeam;
                String loserEnglish = winnerEnglish.equals(recap.gameData.awayTeam) ? 
                              recap.gameData.homeTeam : recap.gameData.awayTeam;
                
                String winner = LocalizedStrings.getCurrentLanguage() == LocalizedStrings.Language.CHINESE ?
                               Constants.translateToChinese(winnerEnglish) : winnerEnglish;
                String loser = LocalizedStrings.getCurrentLanguage() == LocalizedStrings.Language.CHINESE ?
                              Constants.translateToChinese(loserEnglish) : loserEnglish;
                
                System.out.println("   " + winner + ": " + recap.winnerStatus);
                System.out.println("   " + loser + ": " + recap.loserStatus);
                System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            }

            playinPs.close();
        } catch (Exception e) {
            System.err.println("Error writing play-in recap: " + e.getMessage());
        }
    }

    /**
     * Write playoff recap file
     */
    private void writePlayoffRecap() {
        try {
            PrintStream playoffPs = new PrintStream(recapPath + Constants.PLAYOFF_RECAP_NAME);
            System.setOut(playoffPs);

            for (PlayoffRoundRecap roundRecap : playoffRecaps) {
                System.out.println();
                System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                System.out.println(roundRecap.roundName);
                System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                System.out.println();

                for (SeriesRecap series : roundRecap.series) {
                    writeSeriesRecap(series);
                    System.out.println();
                }
            }

            playoffPs.close();
        } catch (Exception e) {
            System.err.println("Error writing playoff recap: " + e.getMessage());
        }
    }

    /**
     * Write a single series recap
     */
    private void writeSeriesRecap(SeriesRecap series) {
        // Translate team names for display
        String team1Display = LocalizedStrings.getCurrentLanguage() == LocalizedStrings.Language.CHINESE ?
                             Constants.translateToChinese(series.team1) : series.team1;
        String team2Display = LocalizedStrings.getCurrentLanguage() == LocalizedStrings.Language.CHINESE ?
                             Constants.translateToChinese(series.team2) : series.team2;
        
        // Get MVP display name
        String mvpDisplay = "";
        if (series.mvpPlayerName != null && !series.mvpPlayerName.isEmpty()) {
            mvpDisplay = LocalizedStrings.getCurrentLanguage() == LocalizedStrings.Language.ENGLISH ?
                        series.mvpPlayerEnglishName : series.mvpPlayerName;
        }

        // Series header
        System.out.println("────────────────────────────────────────────────────────────");
        System.out.println("【" + team1Display + team2Display + "-" + series.seriesName + "】");
        
        // Series result
        String seriesResultLabel = LocalizedStrings.get("playoff.recap.series_result");
        System.out.println(seriesResultLabel + ": " + team1Display + " " + 
                          series.team1Wins + ":" + series.team2Wins + " " + team2Display);
        
        // Series MVP
        if (!mvpDisplay.isEmpty()) {
            String mvpLabel = LocalizedStrings.get("playoff.recap.series_mvp");
            String avgLabel = LocalizedStrings.get("playoff.recap.average");
            String ptsLabel = LocalizedStrings.get("stat.points.short");
            String rebLabel = LocalizedStrings.get("stat.rebounds.short");
            String astLabel = LocalizedStrings.get("stat.assists.short");
            String stlLabel = LocalizedStrings.get("stat.steals.short");
            String blkLabel = LocalizedStrings.get("stat.blocks.short");
            
            System.out.println(mvpLabel + ": " + mvpDisplay + " (" + avgLabel + " " +
                             series.mvpAvgPoints + ptsLabel + " " +
                             series.mvpAvgRebounds + rebLabel + " " +
                             series.mvpAvgAssists + astLabel + " " +
                             series.mvpAvgSteals + stlLabel + " " +
                             series.mvpAvgBlocks + blkLabel + ")");
        }
        
        System.out.println();

        // Write each game in the series
        for (int i = 0; i < series.games.size(); i++) {
            GameRecapData game = series.games.get(i);
            int gameNum = i + 1;
            
            // Calculate series record by checking which series team won each game
            int team1WinsSoFar = 0;
            int team2WinsSoFar = 0;
            for (int j = 0; j <= i; j++) {
                GameRecapData g = series.games.get(j);
                // Determine winner of this game
                String gameWinner = g.awayScore > g.homeScore ? g.awayTeam : g.homeTeam;
                // Check if winner is team1 or team2 in the series
                if (gameWinner.equals(series.team1)) {
                    team1WinsSoFar++;
                } else {
                    team2WinsSoFar++;
                }
            }
            
            // Game header with series record showing team names and their wins
            String seriesRecordLabel = LocalizedStrings.get("playoff.recap.series_record");
            System.out.print("G" + gameNum + ": (" + seriesRecordLabel + " " + 
                           team1Display + " " + team1WinsSoFar + "-" + team2WinsSoFar + " " + team2Display + ")");
            
            // Check if this is the clinching game
            if ((team1WinsSoFar == 4 || team2WinsSoFar == 4) && i == series.games.size() - 1) {
                // Check if this is championship (总决赛)
                String championshipLabel = LocalizedStrings.get("playoff.round.championship");
                boolean isChampionship = series.seriesName.contains(championshipLabel);
                
                String advanceLabel = isChampionship ? 
                    LocalizedStrings.get("playoff.recap.champion") : 
                    LocalizedStrings.get("playoff.recap.advance");
                String winner = team1WinsSoFar == 4 ? team1Display : team2Display;
                System.out.print(" " + winner + advanceLabel);
            }
            System.out.println();
            
            writeGameRecap(game);
            
            if (i < series.games.size() - 1) {
                System.out.println("────────────────────────────────────────");
            }
        }
        
        System.out.println("────────────────────────────────────────────────────────────");
    }

    /**
     * Reorder all division seeds (1st vs 8th, 2nd vs 7th, 3rd vs 6th, 4th vs 5th).
     * 
     * @param temp A temporary array which contains all division seeds
     * @return Ordered division seeds
     */
    public List<String> reorderSeeds(String[] temp) {
        List<String> result = new LinkedList<>();
        int[] order = {0, 7, 3, 4, 1, 6, 2, 5};
        for (int num : order) result.add(temp[num]);
        return result;
    }

    /**
     * Determine home and away teams for a playoff game based on 2-2-1-1-1 format
     * Games 1, 2, 5, 7: Higher seed at home
     * Games 3, 4, 6: Lower seed at home
     * 
     * @param team1 First team (potentially higher seed)
     * @param team2 Second team (potentially lower seed)
     * @param gameNumber Game number in the series (1-7)
     * @return Array where [0] = away team, [1] = home team
     */
    private String[] getHomeAwayTeams(String team1, String team2, int gameNumber) {
        // Determine which team is higher seed
        int team1Seed = teamSeedMap.getOrDefault(team1, 99);
        int team2Seed = teamSeedMap.getOrDefault(team2, 99);
        
        String higherSeed = team1Seed < team2Seed ? team1 : team2;
        String lowerSeed = team1Seed < team2Seed ? team2 : team1;
        
        // 2-2-1-1-1 format: Games 1,2,5,7 at higher seed; Games 3,4,6 at lower seed
        boolean higherSeedHome = (gameNumber == 1 || gameNumber == 2 || gameNumber == 5 || gameNumber == 7);
        
        if (higherSeedHome) {
            return new String[]{lowerSeed, higherSeed}; // away, home
        } else {
            return new String[]{higherSeed, lowerSeed}; // away, home
        }
    }

    /**
     * Host a best-of-seven series between two teams.
     * 
     * @param seriesName The prefix of current series. e.g. 'First Round G1', 'Semi final G7'
     * @return The winner of the series
     */
    public String hostSeries(String team1, String team2, String seriesName) {
        int gameCount = 1;
        int team1Win = 0, team2Win = 0;
        
        // Create series recap object
        SeriesRecap seriesRecap = new SeriesRecap(team1, team2, seriesName);
        
        // Track player stats across the series for MVP calculation
        Map<String, int[]> playerSeriesStats = new HashMap<>(); // name -> [games, pts, reb, ast, stl, blk]
        Map<String, String> playerEnglishNames = new HashMap<>(); // Chinese name -> English name
        Map<String, String> playerTeamMap = new HashMap<>(); // Chinese name -> team English name

        try {
            while (gameCount <= 7) {
                // Determine home and away teams based on game number and seeding
                String[] homeAway = getHomeAwayTeams(team1, team2, gameCount);
                String awayTeam = homeAway[0];
                String homeTeam = homeAway[1];
                
                // Pass series order (team1, team2) for consistent file naming
                String gameWinner = hostGame(awayTeam, homeTeam, seriesName + "G" + gameCount, "playoffs", 
                                            new SeasonStats(), team1, team2);
                
                // Update win counts first
                if (gameWinner.equals(team1)) team1Win += 1;
                else team2Win += 1;
                
                // Collect game recap data using the stored teams from hostGame
                // Pass team1 and team2 names to maintain series order consistency
                collectPlayoffGameData(lastPlayoffTeam1, lastPlayoffTeam2, seriesRecap, gameCount, 
                                      team1Win, team2Win, playerSeriesStats, playerEnglishNames, playerTeamMap,
                                      team1, team2);
                
                gameCount++;

                if (team1Win == 4 || team2Win == 4) break;
            }

            // Set final win counts BEFORE calculating MVP
            seriesRecap.team1Wins = team1Win;
            seriesRecap.team2Wins = team2Win;
            
            // Calculate series MVP
            calculateSeriesMVP(seriesRecap, playerSeriesStats, playerEnglishNames, playerTeamMap);
            
            // Store the series recap
            addToPlayoffRecaps(seriesRecap);

            return team1Win == 4 ? team1 : team2;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Collect playoff game data for recap
     */
    private void collectPlayoffGameData(Team team1, Team team2, SeriesRecap seriesRecap, int gameCount,
                                       int team1Wins, int team2Wins, Map<String, int[]> playerSeriesStats,
                                       Map<String, String> playerEnglishNames, Map<String, String> playerTeamMap,
                                       String seriesTeam1Name, String seriesTeam2Name) {
        // team1 and team2 here are in away/home order from the actual game
        // We need to store them in away/home order for display, but track series wins correctly
        
        // Create game recap using away/home order (team1 = away, team2 = home)
        GameRecapData gameData = new GameRecapData("G" + gameCount, team1.name, team2.name, 
                                                   team1.totalScore, team2.totalScore,
                                                   team1.totalShotMade > 0 ? (team1.totalShotMade * 100.0 / team1.totalShotAttempted) : 0.0,
                                                   team2.totalShotMade > 0 ? (team2.totalShotMade * 100.0 / team2.totalShotAttempted) : 0.0,
                                                   team1.total3Made > 0 ? (team1.total3Made * 100.0 / team1.total3Attempted) : 0.0,
                                                   team2.total3Made > 0 ? (team2.total3Made * 100.0 / team2.total3Attempted) : 0.0);

        // Get top 3 players from each team (team1 = away, team2 = home)
        gameData.awayTopPlayers = getTopPlayers(team1);
        gameData.homeTopPlayers = getTopPlayers(team2);

        // Update series stats for all players who played
        for (Player p : team1.players) {
            if (p.hasBeenOnCourt) {
                updatePlayerSeriesStats(p, playerSeriesStats, playerEnglishNames, playerTeamMap);
            }
        }
        for (Player p : team2.players) {
            if (p.hasBeenOnCourt) {
                updatePlayerSeriesStats(p, playerSeriesStats, playerEnglishNames, playerTeamMap);
            }
        }

        seriesRecap.games.add(gameData);
    }

    /**
     * Update player's series stats for MVP calculation
     */
    private void updatePlayerSeriesStats(Player p, Map<String, int[]> playerSeriesStats, 
                                        Map<String, String> playerEnglishNames, Map<String, String> playerTeamMap) {
        String name = p.name;
        int[] stats = playerSeriesStats.getOrDefault(name, new int[6]);
        stats[0]++; // games played
        stats[1] += p.score;
        stats[2] += p.rebound;
        stats[3] += p.assist;
        stats[4] += p.steal;
        stats[5] += p.block;
        playerSeriesStats.put(name, stats);
        playerEnglishNames.put(name, p.englishName);
        playerTeamMap.put(name, p.teamName); // Track which team this player belongs to
    }

    /**
     * Calculate series MVP based on performance
     */
    private void calculateSeriesMVP(SeriesRecap seriesRecap, Map<String, int[]> playerSeriesStats,
                                   Map<String, String> playerEnglishNames, Map<String, String> playerTeamMap) {
        String mvpName = "";
        double maxScore = 0;
        
        // Determine winning team
        String winningTeam = seriesRecap.team1Wins == 4 ? seriesRecap.team1 : seriesRecap.team2;
        
        // Find player with highest MVP score from winning team only
        for (Map.Entry<String, int[]> entry : playerSeriesStats.entrySet()) {
            String playerName = entry.getKey();
            int[] stats = entry.getValue();
            int games = stats[0];
            
            if (games >= 1) {
                // Only consider players from the winning team for MVP
                String playerTeam = playerTeamMap.get(playerName);
                if (!playerTeam.equals(winningTeam)) {
                    continue; // Skip players not on the winning team
                }
                
                double avgPts = stats[1] * 1.0 / games;
                double avgReb = stats[2] * 1.0 / games;
                double avgAst = stats[3] * 1.0 / games;
                double avgStl = stats[4] * 1.0 / games;
                double avgBlk = stats[5] * 1.0 / games;
                
                // MVP calculation: 1*points + 0.5*rebounds + 0.7*assist + 0.6*steals + 0.6*blocks
                double mvpScore = avgPts * 1.0 + avgReb * 0.5 + avgAst * 0.7 + avgStl * 0.6 + avgBlk * 0.6;
                
                if (mvpScore > maxScore) {
                    maxScore = mvpScore;
                    mvpName = playerName;
                }
            }
        }
        
        if (!mvpName.isEmpty()) {
            int[] mvpStats = playerSeriesStats.get(mvpName);
            int games = mvpStats[0];
            seriesRecap.mvpPlayerName = mvpName;
            seriesRecap.mvpPlayerEnglishName = playerEnglishNames.get(mvpName);
            seriesRecap.mvpAvgPoints = Utilities.roundDouble(mvpStats[1] * 1.0 / games);
            seriesRecap.mvpAvgRebounds = Utilities.roundDouble(mvpStats[2] * 1.0 / games);
            seriesRecap.mvpAvgAssists = Utilities.roundDouble(mvpStats[3] * 1.0 / games);
            seriesRecap.mvpAvgSteals = Utilities.roundDouble(mvpStats[4] * 1.0 / games);
            seriesRecap.mvpAvgBlocks = Utilities.roundDouble(mvpStats[5] * 1.0 / games);
        }
    }

    /**
     * Add series recap to playoff recaps, grouping by round
     */
    private void addToPlayoffRecaps(SeriesRecap seriesRecap) {
        // Find or create the round recap
        PlayoffRoundRecap roundRecap = null;
        for (PlayoffRoundRecap pr : playoffRecaps) {
            if (pr.roundName.equals(seriesRecap.seriesName)) {
                roundRecap = pr;
                break;
            }
        }
        
        if (roundRecap == null) {
            roundRecap = new PlayoffRoundRecap(seriesRecap.seriesName);
            playoffRecaps.add(roundRecap);
        }
        
        roundRecap.series.add(seriesRecap);
    }

    /**
     * Host playoff series to generate conference champion.
     * 
     * @param seeds All 8 seeds of the division
     * @param isWest Whether current division is West conference. By default True means West and False for East
     * @return The conference champion
     */
    public String getConferenceChamp(List<String> seeds, boolean isWest) {
        String DIVISION = LocalizedStrings.get(isWest ? "conference.west" : "conference.east");
        String FIRST_PREFIX = LocalizedStrings.get("playoff.round.first");
        String SECOND_PREFIX = LocalizedStrings.get("playoff.round.semi");
        String THIRD_PREFIX = LocalizedStrings.get("playoff.round.final");

        // Store seeding information for home court advantage
        // Seeds are 1-indexed in basketball terminology, but 0-indexed in the list
        for (int i = 0; i < seeds.size(); i++) {
            teamSeedMap.put(seeds.get(i), i + 1); // 1 = highest seed, 8 = lowest seed
        }

        // each round has at most 4 win teams
        int[] winIndexes = new int[4];

        // two teams of each series, and winner of them
        String team1;
        String team2;
        String winner;

        // conference champion
        String conferenceChamp = "";

        try {
            // first round
            for (int i = 0; i < 8; i += 2) {
                team1 = seeds.get(i);
                team2 = seeds.get(i + 1);
                winner = hostSeries(team1, team2, DIVISION + FIRST_PREFIX);
                winIndexes[i/2] = winner.equals(team1) ? i : i+1;
            }

            // second round
            List<String> secondRound = new LinkedList<>();
            for (int i = 0; i < 4; i++) secondRound.add(seeds.get(winIndexes[i]));

            for (int i = 0; i < 4; i += 2) {
                team1 = secondRound.get(i);
                team2 = secondRound.get(i + 1);
                winner = hostSeries(team1, team2, DIVISION + SECOND_PREFIX);
                winIndexes[i/2] = winner.equals(team1) ? i : i+1;
            }

            // third round
            List<String> thirdRound = new LinkedList<>();
            for (int i = 0; i < 2; i++) thirdRound.add(secondRound.get(winIndexes[i]));
            team1 = thirdRound.get(0);
            team2 = thirdRound.get(1);
            conferenceChamp = hostSeries(team1, team2, DIVISION + THIRD_PREFIX);
        } catch (Exception e) {}

        return conferenceChamp;
    }

    /**
     * Host playoff series to generate two conference champions, then host the NBA finals.
     * 
     * @param westSeeds All 8 seeds of West division
     * @param eastSeeds All 8 seeds of East division
     */
    public void hostPlayoffs(List<String> westSeeds, List<String> eastSeeds) {
        String FINAL_PREFIX = LocalizedStrings.get("playoff.round.championship");
        String westChamp = getConferenceChamp(westSeeds, true);
        String eastChamp = getConferenceChamp(eastSeeds, false);
        hostSeries(westChamp, eastChamp, FINAL_PREFIX);
        
        // Write playoff recap after all playoff games complete
        writePlayoffRecap();
    }
}
