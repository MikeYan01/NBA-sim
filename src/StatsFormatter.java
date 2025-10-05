package src;

/**
 * Utility class for formatting statistics output with internationalization support.
 * Provides methods to format stats in different languages using LocalizedStrings.
 */
public class StatsFormatter {
    
    /**
     * Set the language for stats output.
     */
    public static void setLanguage(LocalizedStrings.Language lang) {
        LocalizedStrings.setLanguage(lang);
    }
    
    /**
     * Get the current language.
     */
    public static LocalizedStrings.Language getLanguage() {
        return LocalizedStrings.getLanguage();
    }
    
    /**
     * Format player box score line for detailed rankings.
     */
    public static String formatPlayerBoxScore(int rank, String displayName, double score, double reb, 
                                               double ast, double stl, double blk, 
                                               double perShotMade, double perShotAttempted,
                                               double perThreeMade, double perThreeAttempted,
                                               double perFtMade, double perFtAttempted) {
        // Build three-point stats string
        String threeStats = perThreeAttempted > 0 ? 
            String.format("%s%.1f/%.1f %.2f%%", LocalizedStrings.get("stat.threepoint.label"), 
                perThreeMade, perThreeAttempted, perThreeMade * 100.0 / perThreeAttempted) : 
            String.format("%s%.1f", LocalizedStrings.get("stat.threepoint.label"), perThreeMade);
        
        // Build free throw stats string
        String ftStats = perFtAttempted > 0 ? 
            String.format("%s%.1f/%.1f %.2f%%", LocalizedStrings.get("stat.freethrow.label"),
                perFtMade, perFtAttempted, perFtMade * 100.0 / perFtAttempted) : 
            String.format("%s%.1f", LocalizedStrings.get("stat.freethrow.label"), perFtMade);

        return String.format("%d %s %.1f%s %.1f%s %.1f%s %.1f%s %.1f%s  %s%.1f/%.1f %.2f%%  %s  %s",
            rank, displayName, score, LocalizedStrings.get("stat.points.short"), 
            reb, LocalizedStrings.get("stat.rebounds.short"),
            ast, LocalizedStrings.get("stat.assists.short"),
            stl, LocalizedStrings.get("stat.steals.short"),
            blk, LocalizedStrings.get("stat.blocks.short"),
            LocalizedStrings.get("stat.fieldgoal.label"),
            perShotMade, perShotAttempted, perShotMade * 100.0 / perShotAttempted,
            threeStats, ftStats);
    }
    
    /**
     * Format simple player ranking line (for 3PT and FT rankings).
     */
    public static String formatSimpleRanking(int rank, String displayName, double value) {
        return String.format("%d %s  %.2f", rank, displayName, value);
    }
    
    /**
     * Format standing line.
     */
    public static String formatStanding(int rank, String teamName, int wins, int losses, double winRate) {
        return String.format("%d %s: %d-%d  %s%.2f%%", 
            rank, teamName, wins, losses, LocalizedStrings.get("stat.winrate"), winRate);
    }
    
    /**
     * Format game recap header.
     */
    public static String formatGameRecapHeader(String gameInfo) {
        return LocalizedStrings.get("game.recap") + " - " + gameInfo;
    }
    
    /**
     * Format final score line.
     */
    public static String formatFinalScore(String winner, int winScore, String loser, int loseScore) {
        return String.format("%s: %s %d - %d %s", 
            LocalizedStrings.get("game.finalscore"), winner, winScore, loseScore, loser);
    }
    
    /**
     * Format player performance line for recap.
     */
    public static String formatPlayerPerformance(String teamName, String playerName, int score, int reb, int ast) {
        return String.format("%s: %s - %d%s %d%s %d%s", 
            teamName, playerName, score, LocalizedStrings.get("stat.points.short"),
            reb, LocalizedStrings.get("stat.rebounds.short"),
            ast, LocalizedStrings.get("stat.assists.short"));
    }
    
    /**
     * Format final score with away/home notation.
     */
    public static String formatAwayHomeScore(String awayTeam, int awayScore, String homeTeam, int homeScore, String overtimeSuffix) {
        return String.format("%s: %s %d %s %s %d%s", 
            LocalizedStrings.get("game.finalscore"), 
            awayTeam, awayScore, LocalizedStrings.get("game.at"), homeTeam, homeScore, overtimeSuffix);
    }
    
    /**
     * Format shooting percentages line.
     */
    public static String formatShootingStats(double awayFgPct, double homeFgPct, double away3PtPct, double home3PtPct) {
        return String.format("%s: %.1f%% vs %.1f%% | %s: %.1f%% vs %.1f%%", 
            LocalizedStrings.get("stat.fieldgoal.pct"), awayFgPct, homeFgPct, 
            LocalizedStrings.get("stat.threepoint.pct"), away3PtPct, home3PtPct);
    }
    
    /**
     * Format team label (Away/Home).
     */
    public static String formatTeamLabel(String teamName, boolean isAway) {
        String location = isAway ? LocalizedStrings.get("game.away") : LocalizedStrings.get("game.home");
        return String.format("  %s (%s):", teamName, location);
    }
    
    /**
     * Format player stats line in recap (with badge).
     */
    public static String formatPlayerStatsLine(String badge, String playerName, int score, int reb, int ast) {
        return String.format("    %s%s - %d%s %d%s %d%s", 
            badge, playerName, score, LocalizedStrings.get("stat.points.short"),
            reb, LocalizedStrings.get("stat.rebounds.short"),
            ast, LocalizedStrings.get("stat.assists.short"));
    }
}
