package src;

import java.io.File;
import java.time.Year;
import java.util.HashMap;
import java.util.Map;

public class Constants {
    /** File separator in current environment */
    private final static String sep = File.separator;

    /** Dunk constants */
    public final static int DUNK_SUM_LB = 60;
    public final static int DUNK_SUM_UB = 160;
    public final static int DUNK_EXCEL_LB = 90;

    /** Star player rating constant */
    public final static int PLAYER_STAR_LB = 88;

    /** Roster path and file extention */
    public final static String ROSTER_PATH = String.format("database%sroster%s", sep, sep);
    public final static String ROSTER_EXTENSION = ".csv";

    /** Max ranks for tables */
    public final static int MAX_PLAYER_RANK = 100;
    public final static int MAX_TEAM_RANK = 30;

    /** Max length of StringBuilder for live comments */
    public final static int MAX_SB_LEN = 128;

    /** Percent to output shot position in live comments */
    public final static int SHOT_POSITION_PERCENT = 30;

    /** Dunk percent based on dunkType */
    public final static int TYPE_1_LAYUP = 60;
    public final static int TYPE_1_DUNK = 10;
    public final static int TYPE_2_LAYUP = 50;
    public final static int TYPE_2_DUNK = 20;
    public final static int TYPE_3_LAYUP = 40;
    public final static int TYPE_3_DUNK = 30;

    /** Distance threshold to separate shot choice comments */
    public final static int SHOT_CHOICE_THLD = 20;

    /** Percent to output celebrate / upset comments after player actions */
    public final static int CELEBRATE_HIGH_PERCENT = 60;
    public final static int CELEBRATE_LOW_PERCENT = 20;
    public final static int UPSET_HIGH_PERCENT = 60;
    public final static int UPSET_LOW_PERCENT = 20;

    /** Good / bad status conditions */
    public final static int MIN_GOOD_SCORE = 30;
    public final static int MIN_SHOT_MADE = 7;
    public final static double MIN_GOOD_SHOT_PERCENT = 0.75;
    public final static int MIN_SHOT_ATTEMPTED = 7;
    public final static double MAX_BAD_SHOT_PERCENT = 0.2;

    /** Outstanding defensive stats threshold for recap display */
    public final static int MIN_OUTSTANDING_STEALS = 3;
    public final static int MIN_OUTSTANDING_BLOCKS = 3;

    /** Current year and next year's prefix */
    public final static String CURRENT_YEAR = Year.now().getValue() + "-";
    public final static String NEXT_YEAR = (Year.now().getValue() + 1) + "-";

    /** Files directories and paths */
    public final static String SCHEDULE_PATH = String.format("database%sschedule%sschedule-82games.txt", sep, sep);
    public final static String SINGLE_GAME_DIR = String.format("output%s", sep);
    public final static String REGULAR_GAMES_DIR = String.format("output%sregular-results%s", sep, sep);
    public final static String REGULAR_STATS_DIR = String.format("output%sregular-stats%s", sep, sep);
    public final static String PLAYIN_GAMES_DIR = String.format("output%splayin-results%s", sep, sep);
    public final static String PLAYOFFS_GAMES_DIR = String.format("output%splayoffs-results%s", sep, sep);
    public final static String RECAP_DIR = String.format("output%srecap%s", sep, sep);
    public final static String STANDING_NAME = "standing.txt";
    public final static String STAT_NAME = "stat.txt";
    public final static String RECAP_NAME = "recap.txt";
    public final static String PLAYOFF_RECAP_NAME = "playoff-recap.txt";
    public final static String PLAYIN_RECAP_NAME = "playin-recap.txt";
    public final static String RESULT_EXTENSION = ".txt";

    /** Score difference to enter garbage time */
    public final static int DIFF1 = 30;
    public final static int TIME_LEFT1 = 720;
    public final static int DIFF2 = 18;
    public final static int TIME_LEFT2 = 360;
    public final static int DIFF3 = 9;
    public final static int TIME_LEFT3 = 60;

    /** Clutch time */
    public final static int TIME_LEFT_CLUTCH = 360;
    public final static int CLOSE_GAME_DIFF = 12;  // Game is "close" if within 12 points

    /** Intelligent substitution system */
    // Target minutes for different rotation types (in seconds)
    public final static int BENCH_TARGET_MINUTES = 18 * 60;    // ~18 minutes
    
    // Durability-based target minutes for starters (in seconds)
    public final static int HIGH_DURABILITY_THRESHOLD = 90;
    public final static int HIGH_DURABILITY_MINUTES = 32 * 60;     // 1920 seconds
    public final static int MEDIUM_DURABILITY_THRESHOLD = 80;
    public final static int MEDIUM_DURABILITY_MINUTES = 30 * 60;   // 1800 seconds
    public final static int LOW_DURABILITY_THRESHOLD = 70;
    public final static int LOW_DURABILITY_MINUTES = 26 * 60;      // 1560 seconds
    public final static int VERY_LOW_DURABILITY_MINUTES = 22 * 60; // 1320 seconds
    public final static int NON_STARTER_MAX_MINUTES = 35 * 60;     // Bench players use high limit
    
    // Athleticism-based target minutes adjustment for starters (in seconds)
    public final static int ATHLETICISM_ELITE_THRESHOLD = 90;
    public final static int ATHLETICISM_ELITE_BONUS = 2 * 60;      // +2 minutes
    public final static int ATHLETICISM_HIGH_THRESHOLD = 85;
    public final static int ATHLETICISM_HIGH_BONUS = 1 * 60;       // +1 minute
    public final static int ATHLETICISM_ABOVE_AVG_THRESHOLD = 80;
    public final static int ATHLETICISM_ABOVE_AVG_PENALTY = -2 * 60;  // -2 minutes
    public final static int ATHLETICISM_AVG_THRESHOLD = 75;
    public final static int ATHLETICISM_AVG_PENALTY = -3 * 60;     // -3 minutes
    public final static int ATHLETICISM_BELOW_AVG_THRESHOLD = 70;
    public final static int ATHLETICISM_BELOW_AVG_PENALTY = -4 * 60;  // -4 minutes
    public final static int ATHLETICISM_LOW_THRESHOLD = 65;
    public final static int ATHLETICISM_LOW_PENALTY = -5 * 60;     // -5 minutes
    public final static int ATHLETICISM_VERY_LOW_PENALTY = -6 * 60;   // -6 minutes
    public final static int MIN_STARTER_MINUTES = 18 * 60;         // Minimum 18 minutes for any starter
    
    // Maximum continuous stint duration before rest needed (in seconds)
    public final static int MAX_STARTER_STINT = 10 * 60;  // 10 minutes 
    public final static int MAX_STARTER_STINT_CLOSE_GAME = 8 * 60;  // 6 minutes in close games
    public final static int MAX_STARTER_STINT_NORMAL_GAME = 6 * 60;  // 6 minutes in normal games
    public final static int MAX_BENCH_STINT = 6 * 60;     // 6 minutes
    
    // Minimum rest time between stints (in seconds)
    public final static int MIN_REST_TIME = 2 * 60;  // 2 minutes (shorter rest OK)
    public final static int MIN_REST_TIME_CLOSE_GAME = 1 * 60;  // 1 minute in close games
    
    // Bench player limits
    public final static int BENCH_MINUTES_BUFFER = 5 * 60;  // 5 minute buffer for bench players
    
    // Performance-based thresholds
    public final static int MIN_SHOTS_FOR_HOT = 4;      // Need at least 4 shots
    public final static double COLD_SHOOTER_THRESHOLD = 0.3;  // Sub if shooting < 30%
    
    // Injury probability constants
    public final static int INJURY_BASE_PROBABILITY = 200;  // Base injury chance (out of 1,000,000)
    public final static int INJURY_PROBABILITY_DIVISOR = 1000000;
    
    // Substitution probabilities and priorities
    public final static int SUB_CHECK_PROBABILITY = 40;
    public final static int SUB_DECISION_PROBABILITY = 20;
    public final static int GARBAGE_TIME_SUB_PROBABILITY = 100;
    public final static int FOUL_TROUBLE_PRIORITY = 100;
    public final static int MINUTES_CAP_PRIORITY = 80;
    public final static int FATIGUE_BASE_PRIORITY = 50;
    public final static int PERFORMANCE_PRIORITY = 30;
    public final static int FATIGUE_SECONDS_TO_PRIORITY = 60;  // Divide stint seconds by this for priority
    
    // Quarter-specific settings
    public final static int QUARTER_1 = 1;
    public final static int QUARTER_2 = 2;
    public final static int QUARTER_3 = 3;
    public final static int Q1_NO_SUB_TIME = 360;  // No subs in first 6 minutes of Q1 (in seconds)
    public final static int OVERTIME_QUARTER = 5;  // Overtime starts at quarter 5
    public final static int CLUTCH_QUARTER = 4;    // Clutch time in Q4
    
    // Game constants
    public final static int FOULS_TO_FOUL_OUT = 6;
    public final static int FLAGRANT_FOULS_TO_EJECT = 2;
    public final static int BONUS_FOUL_THRESHOLD = 5;  // Team in bonus at 5 fouls
    public final static int JUMP_BALL_FIFTY_FIFTY = 50;  // 50% chance for jump ball
    public final static int THREE_POINT_LINE_DISTANCE = 23;

    /** Team names for display - Chinese */
    public final static String[] EAST_TEAMS_ZH = {"76人", "公牛", "凯尔特人", "奇才", "黄蜂",
                                                  "步行者", "活塞", "热火", "猛龙", "篮网",
                                                  "尼克斯", "老鹰", "雄鹿", "骑士", "魔术"};
    public final static String[] WEST_TEAMS_ZH = {"勇士", "国王", "太阳", "开拓者", "快船",
                                                  "掘金", "灰熊", "湖人", "火箭", "独行侠",
                                                  "森林狼", "爵士", "雷霆", "马刺", "鹈鹕"};
    
    /** Team names for display - English */
    public final static String[] EAST_TEAMS_EN = {"76ers", "Bulls", "Celtics", "Wizards", "Hornets",
                                                  "Pacers", "Pistons", "Heat", "Raptors", "Nets",
                                                  "Knicks", "Hawks", "Bucks", "Cavaliers", "Magic"};
    public final static String[] WEST_TEAMS_EN = {"Warriors", "Kings", "Suns", "Trail Blazers", "Clippers",
                                                  "Nuggets", "Grizzlies", "Lakers", "Rockets", "Mavericks",
                                                  "Timberwolves", "Jazz", "Thunder", "Spurs", "Pelicans"};
    
    /** Active team names for display (switches based on language) */
    public static String[] EAST_TEAMS = EAST_TEAMS_ZH;
    public static String[] WEST_TEAMS = WEST_TEAMS_ZH;

    /** Choose offense player based on ratings and time */
    public final static double MAJOR_SCORE_FACTOR = 0.55;
    public final static double MINOR_SCORE_FACTOR = 0.15;
    public final static int SINGLE_STAR_PERCENT_1 = 3;
    public final static int SINGLE_STAR_PERCENT_2 = 6;
    public final static int SINGLE_STAR_EXTRA = 22;
    public final static int GENERAL_THLD = 90;
    public final static int CLUTCH_PERCENT = 50;
    public final static int RATING_RANGE = 10;
    public final static int AST_SCALE = 2;
    
    /** Rebound distribution power scaling exponent */
    public final static double REBOUND_POWER_SCALE = 0.8;

    /** Choose same position player or other position player */
    public final static int SAME_POS = 52;
    public final static int OTHER_POS = 12;

    /** Lose ball */
    public final static int JUMP_BALL_PLAY = 60;
    public final static int TURNOVER = 5;
    public final static int STEAL_BASE = 1;
    public final static int STEAL_RATING_SCALE = 4;
    public final static int STEAL_DEFENSE_SCALE = 2;
    public final static double STEAL_BONUS_SCALE1 = 1.15;
    public final static double STEAL_BONUS_SCALE2 = 1.3;
    public final static double STEAL_BONUS_SCALE3 = 1.4;
    public final static double STEAL_BONUS_SCALE4 = 1.5;
    public final static int STEAL_BONUS_THLD1 = 83;
    public final static int STEAL_BONUS_THLD2 = 87;
    public final static int STEAL_BONUS_THLD3 = 92;
    public final static int STEAL_BONUS_THLD4 = 95;
    public final static int NON_FASTBREAK = 30;

    /** Block */
    public final static int BLOCK_RATING_SCALE = 3;
    public final static double BLOCK_BONUS_SCALE1 = 1.4;
    public final static double BLOCK_BONUS_SCALE2 = 1.7;
    public final static double BLOCK_BONUS_SCALE3 = 2.2;
    public final static double BLOCK_BONUS_SCALE4 = 3;
    public final static double BLOCK_BONUS_SCALE5 = 3.8;
    public final static int BLOCK_BONUS_THLD1 = 70;
    public final static int BLOCK_BONUS_THLD2 = 83;
    public final static int BLOCK_BONUS_THLD3 = 88;
    public final static int BLOCK_BONUS_THLD4 = 92;
    public final static int BLOCK_BONUS_THLD5 = 95;
    public final static int BLOCK_OUT_OF_BOUND = 40;

    /** Rebound */
    public final static int ORB_WITH_BONUS = 15;
    public final static int ORB_WITHOUT_BONUS = 10;
    public final static int REBOUND_RATING_BONUS = 90;
    public final static int REBOUND_RATING_BONUS_PERCENT = 10;

    /** Foul protect */
    public final static int QUARTER1_PROTECT = 2;
    public final static int QUARTER2_PROTECT = 4;
    public final static int QUARTER3_PROTECT = 5;

    /** Normal foul */
    public final static int OFF_FOUL = 1;
    public final static int DEF_FOUL = 1;

    /** Min and max distance of shot choices */
    public final static int MIN_CLOSE_SHOT = 1;
    public final static int PAINT_CLOSE_SHOT = 3;
    public final static int MAX_CLOSE_SHOT = 12;
    public final static int MIN_MID_SHOT = 13;
    public final static int MID_MID_SHOT = 20;
    public final static int MAX_MID_SHOT = 22;
    public final static int MIN_THREE_SHOT = 23;
    public final static int MID_THREE_SHOT = 26;
    public final static int MAX_THREE_SHOT = 35;
    public final static int MIN_DIST_CURVE = 28;
    public final static int DIST_CURVE_PERCENT = 90;
    public final static int DIST_CURVE = 5;
    public final static int DUNK_MAX_PERCENT = 90;
    public final static double DUNK_PERCENT_MULTIPLIER = 2.0;
    public final static int MIN_SHOT_PERCENT = 20;
    public final static int MAX_SHOT_PERCENT = 90;

    /** Shot choices percent */
    public final static int TYPE3_PERCENT = 30;
    public final static int TYPE4_CLOSE_SHOT = 40;
    public final static int TYPE4_MID_SHOT = 15;
    public final static int TYPE5_CLOSE_SHOT = 20;
    public final static int TYPE5_MID_SHOT = 20;

    /** Initial shot percent */
    public final static double INIT_CLOSE_SHOT_COFF = -0.2;
    public final static double INIT_CLOSE_SHOT_INTCP = 38;
    public final static double INIT_MID_SHOT_INTCP = 8;
    public final static double INIT_THREE_SHOT_COFF = -31/81;
    public final static double INIT_THREE_SHOT_INTCP = 42;

    /** Shot percent adjust based on shot type and distance */
    public final static double DUNK_SCALE = 2.5;
    public final static double SHOT_COFF = 0.2;
    public final static int OFFENSE_BASE = 70;

    /** High astRating players increase shot percent */
    public final static int AST_RATING_THLD1 = 83;
    public final static int AST_RATING_THLD2 = 87;
    public final static int AST_RATING_THLD3 = 93;
    public final static double AST_RATING_BONUS1 = 0.5;
    public final static double AST_RATING_BONUS2 = 0.75;
    public final static double AST_RATING_BONUS3 = 1;

    /** Defense players affect percent */
    public final static double DEFENSE_COFF = 0.1;
    public final static int DEFENSE_BASE = 41;

    /** Defense density percent */
    public final static int DEFENSE_EASY = 10;
    public final static int DEFENSE_HARD = 50;
    public final static int DEFENSE_BUFF = 12;

    /** Offense and defense consistency */
    public final static double CONSISTENCY_COFF = 0.4;
    public final static double CONSISTENCY_MAX_BONUS = 2;

    /** Athleticism */
    public final static double ATHLETIC_COFF = 0.06;

    /** Clutch time */
    public final static int CLUTCH_OFF_CONST = 100;
    public final static double CLUTCH_SHOT_COFF = 0.6;

    /** Status comment percent */
    public final static int STATUS_COMMENT_PERCENT = 40;

    /** Extra comment in garbage time */
    public final static int EXTRA_COMMENT = 15;

    /** Assist allocation */
    public final static int HIGH_BOTH_RATING = 25;
    public final static int HIGH_BOTH_RATING_THLD = 88;
    public final static int HIGHEST_RATING_PERCENT = 10;
    public final static int STAR_PLAYER_AST = 70;
    public final static int NON_STAR_PLAYER_AST = 95;

    /** Foul percent */
    public final static int AND_ONE_CLOSE_BASE = 5;
    public final static int AND_ONE_MID_BASE = 2;
    public final static int AND_ONE_THREE_BASE = 1;
    public final static int NORMAL_CLOSE_BASE = 10;
    public final static int NORMAL_MID_BASE = 6;
    public final static int NORMAL_THREE_BASE = 2;

    /** Foul ratings and scales */
    public final static int FOUL_RATING_THLD1 = 94;
    public final static int FOUL_RATING_THLD2 = 85;
    public final static double FOUL_COFF1 = 4;
    public final static double FOUL_COFF2 = 3.4;
    public final static double FOUL_COFF3 = 2.8;
    public final static double STAR_FOUL_SCALE = 1.3;

    /** Flagrant foul */
    public final static int FLAG_FOUL = 5;

    /** Foul challenge */
    public final static int CHALLENGE_START_QUARTER = 3;
    public final static int FOUL_CHALLENGE = 8;
    public final static int CHALLENGE_SUCCESS = 40;

    /** Shot out-of-bound */
    public final static int SHOT_OUT_OF_BOUND = 3;
    
    /** Team name mapping - Chinese to English */
    private static final Map<String, String> TEAM_NAME_ZH_TO_EN = new HashMap<>();
    private static final Map<String, String> TEAM_NAME_EN_TO_ZH = new HashMap<>();
    
    static {
        // Initialize bidirectional team name mapping
        String[][] mappings = {
            // Eastern Conference
            {"76人", "76ers"},
            {"公牛", "Bulls"},
            {"凯尔特人", "Celtics"},
            {"奇才", "Wizards"},
            {"黄蜂", "Hornets"},
            {"步行者", "Pacers"},
            {"活塞", "Pistons"},
            {"热火", "Heat"},
            {"猛龙", "Raptors"},
            {"篮网", "Nets"},
            {"尼克斯", "Knicks"},
            {"老鹰", "Hawks"},
            {"雄鹿", "Bucks"},
            {"骑士", "Cavaliers"},
            {"魔术", "Magic"},
            // Western Conference
            {"勇士", "Warriors"},
            {"国王", "Kings"},
            {"太阳", "Suns"},
            {"开拓者", "Trail Blazers"},
            {"快船", "Clippers"},
            {"掘金", "Nuggets"},
            {"灰熊", "Grizzlies"},
            {"湖人", "Lakers"},
            {"火箭", "Rockets"},
            {"独行侠", "Mavericks"},
            {"森林狼", "Timberwolves"},
            {"爵士", "Jazz"},
            {"雷霆", "Thunder"},
            {"马刺", "Spurs"},
            {"鹈鹕", "Pelicans"}
        };
        
        for (String[] mapping : mappings) {
            TEAM_NAME_ZH_TO_EN.put(mapping[0], mapping[1]);
            TEAM_NAME_EN_TO_ZH.put(mapping[1], mapping[0]);
        }
    }
    
    /**
     * Set the active team names for display based on language.
     * Call this method when language changes to update team name arrays.
     * Note: This only affects display names, roster files always use English names.
     * 
     * @param useEnglish true for English team names, false for Chinese
     */
    public static void setTeamLanguage(boolean useEnglish) {
        if (useEnglish) {
            EAST_TEAMS = EAST_TEAMS_EN;
            WEST_TEAMS = WEST_TEAMS_EN;
        } else {
            EAST_TEAMS = EAST_TEAMS_ZH;
            WEST_TEAMS = WEST_TEAMS_ZH;
        }
    }
    
    /**
     * Get the roster filename for a team.
     * Always returns English filename regardless of display language.
     * 
     * @param teamName Team name in current display language
     * @return CSV filename for the team (always English)
     */
    public static String getTeamRosterFilename(String teamName) {
        // Translate to English if needed for roster file access
        String rosterName = translateToEnglish(teamName);
        return rosterName + ROSTER_EXTENSION;
    }
    
    /**
     * Translate team name from Chinese to English.
     * 
     * @param chineseName Chinese team name
     * @return English team name, or original if not found
     */
    public static String translateToEnglish(String chineseName) {
        return TEAM_NAME_ZH_TO_EN.getOrDefault(chineseName, chineseName);
    }
    
    /**
     * Translate team name from English to Chinese.
     * 
     * @param englishName English team name
     * @return Chinese team name, or original if not found
     */
    public static String translateToChinese(String englishName) {
        return TEAM_NAME_EN_TO_ZH.getOrDefault(englishName, englishName);
    }
    
    /**
     * Get localized team name based on current language setting.
     * Team names in Team objects are always stored in English,
     * this method translates them for display purposes.
     * 
     * @param englishName English team name (from Team.name)
     * @return Localized team name based on current language
     */
    public static String getLocalizedTeamName(String englishName) {
        if (LocalizedStrings.getLanguage() == LocalizedStrings.Language.CHINESE) {
            return translateToChinese(englishName);
        }
        return englishName;
    }
}
