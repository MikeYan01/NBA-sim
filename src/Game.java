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

    // PrintStream for result output
    PrintStream ps;

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

        // initialize file paths
        Utilities.initializePath(this.regularResultsPath);
        Utilities.initializePath(this.regularStatsPath);
        Utilities.initializePath(this.playoffsResultsPath);
    }

    /**
     * Host a game between two teams.
     * @param info By default, info is the date when the game is hosted; for playoff games, info is the round and game number.
     * @param gameMode Whether the game is a single game (default), regular season game or playoffs game
     * @param stat SeasonStats object, store regular season's player stats
     * @return Winner of the game
     */
    public String hostGame(String team1Name, String team2Name, String info, String gameMode, SeasonStats stat) throws Exception {
        Random random = new Random();

        // each game's result file
        String filePath;
        if (gameMode.equals("playoffs")) filePath = playoffsResultsPath + team1Name + team2Name + "-" + info + ".txt";
        else if (gameMode.equals("regular")) {
            // games in Oct, Nov, Dec are hosted in current year
            if (info.charAt(0) == '1') filePath = regularResultsPath + currentYear + info + "-" + team1Name + team2Name + ".txt";
            else filePath = regularResultsPath + nextYear + info + "-" + team1Name + team2Name + ".txt";
        }
        else {
            filePath = singleResultsPath + team1Name + team2Name + ".txt";
        }
                                    
        ps = new PrintStream(filePath);
        System.setOut(ps);

        Team team1 = new Team(team1Name);
        Team team2 = new Team(team2Name);

        Map<String, Player> teamOneOnCourt = new HashMap<>();
        Map<String, Player> teamTwoOnCourt = new HashMap<>();
        for (String pos : team1.starters.keySet()) 
            teamOneOnCourt.put(pos, team1.starters.get(pos));
        for (String pos : team2.starters.keySet()) 
            teamTwoOnCourt.put(pos, team2.starters.get(pos));

        int quarterTime = 720;
        int currentQuarter = 1;
        boolean hasSubstituted = false;
        boolean hasGarbageSubstituted = false;
        boolean startersBack = false;

        // decide whether current play is second chance play
        boolean isSecondChance = false;

        List<Integer> team1Scores = new LinkedList<>();
        List<Integer> team2Scores = new LinkedList<>();

        // start game
        Utilities.jumpBall(random, team1, team2);

        // Play-by-play simulation
        while (quarterTime >= 0) {
            // decide play time consumption and substitution
            int currentPlayTime;
            if (!isSecondChance) {
                currentPlayTime = quarterTime > 24 ? Utilities.generateRandomPlayTime(random, 24) : quarterTime;
            } else {
                currentPlayTime = quarterTime > 24 ? Utilities.generateRandomPlayTime(random, 14) : quarterTime;
                isSecondChance = false;
            }
            
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

            // substitution
            if (!hasSubstituted) {
                if (quarterTime < Constants.ODD_QUARTERS_TIME_LEFT && (currentQuarter == 1 || currentQuarter == 3)) {
                    Utilities.timeOutSub(team1, team2, true, false, teamOneOnCourt, teamTwoOnCourt);
                    hasSubstituted = true;
                }

                if (quarterTime < Constants.EVEN_QUARTERS_TIME_LEFT && (currentQuarter == 2 || (currentQuarter == 4 && !hasGarbageSubstituted))) {
                    Utilities.timeOutSub(team1, team2, false, false, teamOneOnCourt, teamTwoOnCourt);
                    hasSubstituted = true;
                }
            }
            if (!hasGarbageSubstituted && !startersBack) {
                if (currentQuarter == 4 && 
                    ( (Math.abs(team1.totalScore - team2.totalScore) >= Constants.DIFF1 && quarterTime <= Constants.TIME_LEFT1)
                    || (Math.abs(team1.totalScore - team2.totalScore) >= Constants.DIFF2 && quarterTime <= Constants.TIME_LEFT2)
                    || (Math.abs(team1.totalScore - team2.totalScore) >= Constants.DIFF3 && quarterTime <= Constants.TIME_LEFT3))) {
                    Utilities.timeOutSub(team1, team2, true, true, teamOneOnCourt, teamTwoOnCourt);
                    hasGarbageSubstituted = true;
                }
            }
            if (hasGarbageSubstituted && !startersBack) {
                if (currentQuarter == 4 && 
                    ((Math.abs(team1.totalScore - team2.totalScore) <= Constants.CLUTCH_DIFF && quarterTime <= Constants.TIME_LEFT_CLUTCH))) {
                    Utilities.timeOutSub(team1, team2, false, false, teamOneOnCourt, teamTwoOnCourt);
                    startersBack = true;
                }
            }

            // get offsense team, defense team, offsense player, defense player
            Team offenseTeam = team1.hasBall ? team1 : team2;
            Team defenseTeam = !team1.hasBall ? team1 : team2;
            Map<String, Player> offenseTeamOnCourt = team1.hasBall ? teamOneOnCourt : teamTwoOnCourt;
            Map<String, Player> defenseTeamOnCourt = !team1.hasBall ? teamOneOnCourt : teamTwoOnCourt;
            Player offensePlayer = Utilities.choosePlayerBasedOnRating(random, offenseTeamOnCourt, "rating",
                                                                       currentQuarter, quarterTime, offenseTeam, defenseTeam);
            Player defensePlayer = Utilities.chooseDefensePlayer(random, offensePlayer, defenseTeamOnCourt);
            Comments.getBallComment(offenseTeam.name, offensePlayer.name, defensePlayer.name);

            // judge ball possession lost: turnover, steal, jumpball lose
            int loseBallValue = Utilities.judgeLoseBall(random, defenseTeam, defenseTeamOnCourt, offensePlayer, defensePlayer);
            if (loseBallValue == 1) {
                offenseTeam.hasBall = false;
                defenseTeam.hasBall = true;
                quarterTime -= currentPlayTime;
                continue;
            } else if (loseBallValue == 2) {
                quarterTime -= currentPlayTime;
                Comments.getTimeAndScore(quarterTime, currentQuarter, team1, team2);
                continue;
            } else if (loseBallValue == 3) {
                quarterTime -= currentPlayTime;
                continue;
            }

            // judge offense foul or defense foul (no free-throw)
            int foulValue = Utilities.judgeNormalFoul(random, offenseTeamOnCourt, defenseTeamOnCourt, offensePlayer, defensePlayer,
                                                      offenseTeam, defenseTeam, currentQuarter, quarterTime, team1, team2);
            if (foulValue == 1) {
                offenseTeam.hasBall = false;
                defenseTeam.hasBall = true;
                quarterTime -= currentPlayTime;
                continue;
            }
            else if (foulValue == 2) {
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
            int blockValue = Utilities.judgeBlock(random, distance, offenseTeamOnCourt, defenseTeamOnCourt, offensePlayer, defensePlayer);
            if (blockValue == 1) {
                quarterTime -= currentPlayTime;
                continue;
            }
            else if (blockValue == 2) {
                offenseTeam.hasBall = false;
                defenseTeam.hasBall = true;
                quarterTime -= currentPlayTime;
                continue;
            }

            // judge shot percentage
            double percentage = Utilities.calculatePercentage(random, distance, offensePlayer, defensePlayer, offenseTeamOnCourt,
                                                              shotMovement, quarterTime, currentQuarter, offenseTeam, defenseTeam);

            // judge whether to make the shot
            int shotValue = Utilities.judgeMakeShot(random, distance, offensePlayer, defensePlayer, offenseTeam, defenseTeam, offenseTeamOnCourt,
                                                    defenseTeamOnCourt, percentage, quarterTime - currentPlayTime, currentQuarter, team1, team2,
                                                    shotMovement);
            if (shotValue == 1 || shotValue == 3 || shotValue == 4) {
                offenseTeam.hasBall = false;
                defenseTeam.hasBall = true;
                quarterTime -= currentPlayTime;
                continue;
            }
            else if (shotValue == 2) {
                quarterTime -= currentPlayTime;
                isSecondChance = true;
                continue;
            }
        }
        
        // for regular season games, update season stat
        if (gameMode.equals("regular")) {
            for (Player p : team1.players) stat.updatePlayerStats(p);
            for (Player p : team2.players) stat.updatePlayerStats(p);
            stat.updateTeamStats(team1);
            stat.updateTeamStats(team2);
        }

        return team1.totalScore > team2.totalScore ? team1Name : team2Name;
    }

    /**
     * Function overloading, temporarily generate a fake SeasonStats() object for playoff games.
     * 
     * @return Winner or the game
     */
    public String hostGame(String team1Name, String team2Name, String info, String gameMode) throws Exception {
        return hostGame(team1Name, team2Name, info, gameMode, new SeasonStats());
    }

    /**
     * Function overloading, temporarily generate a fake SeasonStats() object for single games.
     * 
     * @return Winner or the game
     */
    public String hostGame(String team1Name, String team2Name) throws Exception {
        return hostGame(team1Name, team2Name, "01-01", "single", new SeasonStats());
    }

    /**
     * Simulate a season (regular season + playoffs).
     */
    public void hostSeason() {
        SeasonStats stat = new SeasonStats();

        String[] eastDivision = Constants.EAST_TEAMS;
        String[] westDivision = Constants.WEST_TEAMS;

        // <teamName, [totalWin, totalLose, division(0 west, 1 east)]>
        Map<String, List<Integer>> standing = new HashMap<>();
        for (String team : eastDivision) standing.put(team, Arrays.asList(0, 0, 1));
        for (String team : westDivision) standing.put(team, Arrays.asList(0, 0, 0));

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
                    String[] teams = line.split(" ");
                    String gameWinner = hostGame(teams[0], teams[1], currentDate, "regular", stat);
                    String gameLoser = gameWinner.equals(teams[0]) ? teams[1] : teams[0];

                    int winnerWin = standing.get(gameWinner).get(0);
                    standing.get(gameWinner).set(0, winnerWin + 1);

                    int loserLose = standing.get(gameLoser).get(1);
                    standing.get(gameLoser).set(1, loserLose + 1);
                }
            }
        } catch (Exception e) {}

        // generate stats leaderboard
        try {
            ps = new PrintStream(statFilePath);
            System.setOut(ps);

            System.out.println("球员场均得分榜");
            stat.printPlayerRank(stat.playerPerScores);

            System.out.println("\n球员场均篮板榜");
            stat.printPlayerRank(stat.playerPerRebs);

            System.out.println("\n球员场均助攻榜");
            stat.printPlayerRank(stat.playerPerAsts);

            System.out.println("\n球员场均抢断榜");
            stat.printPlayerRank(stat.playerPerStls);

            System.out.println("\n球员场均盖帽榜");
            stat.printPlayerRank(stat.playerPerBlks);

            System.out.println("\n球员场均罚球命中个数榜");
            stat.printPlayerRank(stat.playerPerFts);

            System.out.println("\n球员场均三分命中个数榜");
            stat.printPlayerRank(stat.playerPerThrees);

            System.out.println("\n球队场均得分榜");
            stat.printTeamRank(stat.teamPerScores);

            System.out.println("\n球队场均失分榜");
            stat.printTeamRank(stat.teamPerScoresAllowed);

            System.out.println("\n球队场均投篮命中数榜");
            stat.printTeamRank(stat.teamPerShotsMade);

            System.out.println("\n球队场均投篮命中率榜");
            stat.printTeamRank(stat.teamPerShotsPercent);

            System.out.println("\n球队场均三分命中率榜");
            stat.printTeamRank(stat.teamPerThreePercent);
        } catch (Exception e) {}

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
            
            System.out.println("西部排名");
            List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(westStanding.entrySet());
            Collections.sort(list, vComparator);
            SeasonStats.printStanding(standing, list);
            for (int i = 0; i < 8; i++) westTemp[i] = list.get(i).getKey(); // copy west seeds

            System.out.println("\n东部排名");
            list = new ArrayList<Map.Entry<String, Integer>>(eastStanding.entrySet());
            Collections.sort(list, vComparator);
            SeasonStats.printStanding(standing, list);
            for (int i = 0; i < 8; i++) eastTemp[i] = list.get(i).getKey(); // copy east seeds

            List<String> westSeeds = reorderSeeds(westTemp);
            List<String> eastSeeds = reorderSeeds(eastTemp);

            hostPlayoffs(westSeeds, eastSeeds);
        } catch (Exception e) {}
    }

    /**
     * Reorder all division seeds (1st vs 8th, 2nd vs 7th, 3rd vs 6th, 4th vs 5th).
     * 
     * @param temp A temporary array which contains all division seeds
     * @return Ordered division seeds
     */
    public List<String> reorderSeeds(String[] temp) {
        List<String> result = new LinkedList<>();
        result.add(temp[0]);
        result.add(temp[7]);
        result.add(temp[3]);
        result.add(temp[4]);
        result.add(temp[1]);
        result.add(temp[6]);
        result.add(temp[2]);
        result.add(temp[5]);

        return result;
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

        try {
            while (gameCount <= 7) {
                String gameWinner = hostGame(team1, team2, seriesName + "G" + gameCount, "playoffs");
                gameCount++;

                if (gameWinner.equals(team1)) team1Win += 1;
                else team2Win += 1;

                if (team1Win == 4 || team2Win == 4) break;
            }

            return team1Win == 4 ? team1 : team2;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Host playoff series to generate conference champion.
     * 
     * @param seeds All 8 seeds of the division
     * @param isWest Whether current division is West conference. By default True means West and False for East
     * @return The conference champion
     */
    public String getConferenceChamp(List<String> seeds, boolean isWest) {
        String DIVISION = isWest ? "西部" : "东部";
        String FIRST_PREFIX = "首轮";
        String SECOND_PREFIX = "半决赛";
        String THIRD_PREFIX = "决赛";

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
        String FINAL_PREFIX = "总决赛";
        String westChamp = getConferenceChamp(westSeeds, true);
        String eastChamp = getConferenceChamp(eastSeeds, false);
        hostSeries(westChamp, eastChamp, FINAL_PREFIX);
    }
}
