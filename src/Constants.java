package src;

public class Constants {
    //--------------------------------Player.java
    /** Dunk constants */
    public final static int DUNK_SUM_LB = 60;
    public final static int DUNK_SUM_UB = 160;
    public final static int DUNK_EXCEL_LB = 90;

    /** Star player rating constant */
    public final static int PLAYER_STAR_LB = 88;
    //-------------------------------------------


    //--------------------------------SeasonStats.java
    /** Max ranks for tables */
    public final static int MAX_PLAYER_RANK = 100;
    public final static int MAX_TEAM_RANK = 30;
    //-------------------------------------------


    //--------------------------------Comments.java
    /** Max length of StringBuilder for live comments */
    public final static int MAX_SB_LEN = 128;

    /** Percent to output shot position in live comments */
    public final static int SHOT_POSITION_PERCENT = 30;

    /** Dunk percent based on dunkType */
    public final static int TYPE_1_LAYUP = 80;
    public final static int TYPE_1_DUNK = 10;
    public final static int TYPE_2_LAYUP = 60;
    public final static int TYPE_2_DUNK = 30;
    public final static int TYPE_3_LAYUP = 40;
    public final static int TYPE_3_DUNK = 50;

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
    //-------------------------------------------


    //--------------------------------Game.java
    /** Current year and next year's prefix */
    public final static String CURRENT_YEAR = "2020-";
    public final static String NEXT_YEAR = "2021-";

    /** Files directories and paths */
    public final static String SCHEDULE_PATH = "database/schedule/schedule-82games.txt";
    public final static String SINGLE_GAME_DIR = "output/";
    public final static String REGULAR_GAMES_DIR = "output/regular-results/";
    public final static String REGULAR_STATS_DIR = "output/regular-stats/";
    public final static String PLAYOFFS_GAMES_DIR = "output/playoffs-results/";
    public final static String STANDING_NAME = "standing.txt";
    public final static String STAT_NAME = "stat.txt";

    /** Time left in quarters to substitute */
    public final static int ODD_QUARTERS_TIME_LEFT = 180;
    public final static int EVEN_QUARTERS_TIME_LEFT = 540;

    /** Score difference to enter garbage time */
    public final static int DIFF1 = 30;
    public final static int TIME_LEFT1 = 720;
    public final static int DIFF2 = 18;
    public final static int TIME_LEFT2 = 360;
    public final static int DIFF3 = 9;
    public final static int TIME_LEFT3 = 60;

    /** Clutch time */
    public final static int CLUTCH_DIFF = 8;
    public final static int TIME_LEFT_CLUTCH = 360;

    /** Current western and eastern teams */
    public final static String[] EAST_TEAMS = {"76人", "公牛", "凯尔特人", "奇才", "黄蜂",
                                               "步行者", "活塞", "热火", "猛龙", "篮网",
                                               "尼克斯", "老鹰", "雄鹿", "骑士", "魔术"};
    public final static String[] WEST_TEAMS = {"勇士", "国王", "太阳", "开拓者", "快船",
                                               "掘金", "灰熊", "湖人", "火箭", "独行侠",
                                               "森林狼", "爵士", "雷霆", "马刺", "鹈鹕"};
    //-------------------------------------------


    //--------------------------------Utilities.java
    /** Play time */
    public final static int MIN_PLAY_TIME = 4;
    public final static int TIME_MIN_THLD = 10;
    public final static int TIME_MAX_THLD = 17;
    public final static int ADD_TIME_PERCENT = 80;
    public final static int ADD_TIME = 8;
    public final static int SUB_TIME_PERCENT = 60;
    public final static int SUB_TIME = 6;

    /** Choose offense player based on ratings and time */
    public final static double MAJOR_SCORE_FACTOR = 0.55;
    public final static double MINOR_SCORE_FACTOR = 0.15;
    public final static int SINGLE_STAR_PERCENT_1 = 3;
    public final static int SINGLE_STAR_PERCENT_2 = 6;
    public final static int SINGLE_STAR_EXTRA = 25;
    public final static int GENERAL_THLD = 90;
    public final static int CLUTCH_PERCENT = 60;
    public final static int RATING_RANGE = 10;
    public final static int REB_AST_SCALE = 2;

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
    public final static int REBOUND_RATING_BONUS = 88;
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
    public final static int MAX_CLOSE_SHOT = 12;
    public final static int MIN_MID_SHOT = 13;
    public final static int MID_MID_SHOT = 20;
    public final static int MAX_MID_SHOT = 22;
    public final static int MIN_THREE_SHOT = 23;
    public final static int MID_THREE_SHOT = 26;
    public final static int MAX_THREE_SHOT = 32;
    public final static int MIN_DIST_CURVE = 27;
    public final static int DIST_CURVE_PERCENT = 85;
    public final static int DIST_CURVE = 4;

    /** Shot choices percent */
    public final static int TYPE3_PERCENT = 30;
    public final static int TYPE4_CLOSE_SHOT = 40;
    public final static int TYPE4_MID_SHOT = 15;
    public final static int TYPE5_CLOSE_SHOT = 20;
    public final static int TYPE5_MID_SHOT = 30;

    /** Initial shot percent */
    public final static double INIT_CLOSE_SHOT_COFF = -0.3;
    public final static double INIT_CLOSE_SHOT_INTCP = 33;
    public final static double INIT_MID_SHOT_INTCP = 6;
    public final static double INIT_THREE_SHOT_COFF = -31/81;
    public final static double INIT_THREE_SHOT_INTCP = 41;

    /** Shot percent adjust based on shot type and distance */
    public final static double DUNK_SCALE = 2.5;
    public final static double SHOT_COFF = 0.3;
    public final static int OFFENSE_BASE = 70;

    /** High astRating players increase shot percent */
    public final static int AST_RATING_THLD1 = 83;
    public final static int AST_RATING_THLD2 = 87;
    public final static int AST_RATING_THLD3 = 93;
    public final static double AST_RATING_BONUS1 = 0.5;
    public final static double AST_RATING_BONUS2 = 0.75;
    public final static double AST_RATING_BONUS3 = 1;

    /** Defense players affect percent */
    public final static double DEFENSE_COFF = 0.3;
    public final static int DEFENSE_BASE = 41;

    /** Defense density percent */
    public final static int DEFENSE_EASY = 10;
    public final static int DEFENSE_HARD = 35;
    public final static int DEFENSE_BUFF = 10;

    /** Offense and defense consistency */
    public final static double CONSISTENCY_COFF = 0.3;
    public final static double CONSISTENCY_MAX_BONUS = 4;

    /** Athleticism */
    public final static double ATHLETIC_COFF = 0.15;

    /** Clutch time */
    public final static double CLUTCH_SHOT_COFF = 0.6;

    /** Status comment percent */
    public final static int STATUS_COMMENT_PERCENT = 40;

    /** Extra comment in garbage time */
    public final static int EXTRA_COMMENT = 15;

    /** Assist allocation */
    public final static int HIGH_BOTH_RATING = 40;
    public final static int HIGH_BOTH_RATING_THLD = 86;
    public final static int HIGHEST_RATING_PERCENT = 18;
    public final static int STAR_PLAYER_AST = 70;
    public final static int NON_STAR_PLAYER_AST = 95;

    /** Foul percent */
    public final static int AND_ONE_CLOSE_BASE = 5;
    public final static int AND_ONE_MID_BASE = 2;
    public final static int AND_ONE_THREE_BASE = 1;
    public final static int NORMAL_CLOSE_BASE = 12;
    public final static int NORMAL_MID_BASE = 6;
    public final static int NORMAL_THREE_BASE = 2;

    /** Foul ratings and scales */
    public final static int FOUL_RATING_THLD1 = 94;
    public final static int FOUL_RATING_THLD2 = 85;
    public final static double FOUL_COFF1 = 2.8;
    public final static double FOUL_COFF2 = 2.3;
    public final static double FOUL_COFF3 = 2.1;
    public final static double STAR_FOUL_SCALE = 1.5;

    /** Flagrant foul */
    public final static int FLAG_FOUL = 5;

    /** Shot out-of-bound */
    public final static int SHOT_OUT_OF_BOUND = 3;
    //-------------------------------------------
}