package src;

public class Player {
    // Enum definitions
    public enum PlayerType {
        ALL_ROUNDED(1),      // All-rounded player
        INSIDER(2),          // Inside player
        MID_RANGE(3),        // Mid-range specialist
        INSIDE_OUTSIDE(4),   // Inside + outside player
        OUTSIDER(5);         // Outside player

        private final int value;

        PlayerType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static PlayerType fromInt(int value) {
            for (PlayerType type : PlayerType.values()) {
                if (type.value == value) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Invalid PlayerType value: " + value);
        }

        public static PlayerType fromString(String value) {
            return fromInt(Integer.parseInt(value));
        }
    }

    public enum DunkerType {
        RARELY_DUNK(1),      // Rarely dunks
        NORMAL(2),           // Normal dunker
        EXCELLENT(3);        // Excellent dunker

        private final int value;

        DunkerType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static DunkerType fromInt(int value) {
            for (DunkerType type : DunkerType.values()) {
                if (type.value == value) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Invalid DunkerType value: " + value);
        }
    }

    public enum RotationType {
        STARTER(1),          // Starting lineup
        BENCH(2),            // Normal bench player
        DEEP_BENCH(3);       // Bench that rarely shows up

        private final int value;

        RotationType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static RotationType fromInt(int value) {
            for (RotationType type : RotationType.values()) {
                if (type.value == value) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Invalid RotationType value: " + value);
        }

        public static RotationType fromString(String value) {
            return fromInt(Integer.parseInt(value));
        }
    }

    public enum ShotType {
        DUNK,      // Dunk shot
        LAYUP,     // Layup shot
        JUMPER;    // Jump shot (including mid-range and three-pointers)
    }

    public String name;
    public String englishName;
    public String position;
    public String teamName;

    public PlayerType playerType;
    public DunkerType dunkerType;
    public RotationType rotationType;

    // Rating
    public int rating;
    public int insideRating;
    public int midRating;
    public int threeRating;
    public double freeThrowPercent;
    public int interiorDefense;
    public int perimeterDefense;
    public int orbRating;
    public int drbRating;
    public int astRating;
    public int stlRating;
    public int blkRating;
    public int layupRating;
    public int standDunk;
    public int drivingDunk;
    public int athleticism;
    public int durability;
    public int offConst;
    public int defConst;
    public int drawFoul;

    // Bonus badge
    public boolean isStar;

    // Game stats
    public int score;
    public int rebound;
    public int assist;
    public int steal;
    public int block;
    public int shotMade;
    public int shotAttempted;
    public int threeMade;
    public int threeAttempted;
    public int freeThrowMade;
    public int freeThrowAttempted;
    public int turnover;
    public int foul;
    public int flagFoul;

    // Minutes tracking
    public int secondsPlayed;  // Total seconds played in current game
    public int currentStintSeconds;  // Seconds in current stint (continuous play time)
    public int lastSubbedOutTime;  // Game time (in seconds) when player was last subbed out

    // Whether the player can/cannot be on court
    public boolean canOnCourt;

    // Whether the player has/hasn't been on court
    public boolean hasBeenOnCourt;
    
    // Whether the player is currently on court
    public boolean isOnCourt;

    /**
     * Construct a Player object, which can be conceived as an NBA player.
     * 
     * @param name Player's name (Chinese)
     * @param englishName Player's English name
     * @param position Player's position on the court (C / PF / SF / SG / PG)
     * @param playerType Player's player type (1: all-rounded, 2: insider, 3: mid-range, 4: inside+outside, 5: outsider)
     * @param rotationType Player's rotation type (1: starter, 2: bench, 3: deep bench)
     * @param rating Player's general rating
     * @param insideRating Player's close shot rating
     * @param midRating Player's mid-range shot rating
     * @param threeRating Player's three-point shot rating
     * @param freeThrowPercent Player's free-throw shot rating
     * @param interiorDefense Player's interior defense rating
     * @param perimeterDefense Player's perimeter defense rating
     * @param orbRating Player's offensive rebound rating
     * @param drbRating Player's defensive rebound rating
     * @param astRating Player's playmaking and assist rating
     * @param stlRating Player's steal rating
     * @param blkRating Player's block rating
     * @param layupRating Player's layup rating
     * @param standDunk Player's standing dunk rating
     * @param drivingDunk Player's driving dunk rating
     * @param athleticism Player's general athleticism rating (speed, acceleration, strength, ...)
     * @param durability Player's durability to injuries
     * @param offConst Player's offensive consistency
     * @param defConst Player's defensive consistency
     * @param drawFoul Player's ability to draw foul
     * @param teamName Player's team name
     */
    public Player(String name, String englishName, String position, String playerType, String rotationType, String rating, String insideRating, String midRating, 
                  String threeRating, String freeThrowPercent, String interiorDefense, String perimeterDefense, String orbRating,
                  String drbRating, String astRating, String stlRating, String blkRating, String layupRating, String standDunk,
                  String drivingDunk, String athleticism, String durability, String offConst, String defConst, String drawFoul, String teamName) {
        this.name = name;
        this.englishName = englishName;
        this.position = position;
        this.playerType = PlayerType.fromString(playerType);
        this.rotationType = RotationType.fromString(rotationType);

        this.rating = Integer.parseInt(rating);
        this.insideRating = Integer.parseInt(insideRating);
        this.midRating = Integer.parseInt(midRating);
        this.threeRating = Integer.parseInt(threeRating);
        this.freeThrowPercent = Double.valueOf(freeThrowPercent);
        this.interiorDefense = Integer.parseInt(interiorDefense);
        this.perimeterDefense = Integer.parseInt(perimeterDefense);
        this.orbRating = Integer.parseInt(orbRating);
        this.drbRating = Integer.parseInt(drbRating);
        this.astRating = Integer.parseInt(astRating);
        this.stlRating = Integer.parseInt(stlRating);
        this.blkRating = Integer.parseInt(blkRating);
        this.layupRating = Integer.parseInt(layupRating);
        this.standDunk = Integer.parseInt(standDunk);
        this.drivingDunk = Integer.parseInt(drivingDunk);
        this.athleticism = Integer.parseInt(athleticism);
        this.durability = Integer.parseInt(durability);
        this.offConst = Integer.parseInt(offConst);
        this.defConst = Integer.parseInt(defConst);
        this.drawFoul = Integer.parseInt(drawFoul);
        this.teamName = teamName;

        // Determine dunker type based on dunk ratings
        // Type 1 - rarely dunk: standDunk + drivingDunk <= 60
        // Type 2 - normal: other cases
        // Type 3 - excellent dunker: standDunk + drivingDunk >= 160, or one of them exceeds 90
        if (this.standDunk + this.drivingDunk <= Constants.DUNK_SUM_LB) {
            this.dunkerType = DunkerType.RARELY_DUNK;
        } else if (this.standDunk + this.drivingDunk >= Constants.DUNK_SUM_UB || 
                (this.standDunk + this.drivingDunk < Constants.DUNK_SUM_UB
                    && (this.standDunk >= Constants.DUNK_EXCEL_LB || this.drivingDunk >= Constants.DUNK_EXCEL_LB))) {
            this.dunkerType = DunkerType.EXCELLENT;
        } else {
            this.dunkerType = DunkerType.NORMAL;
        }

        this.isStar = this.rating >= Constants.PLAYER_STAR_LB ? true : false;

        this.score = 0;
        this.rebound = 0;
        this.assist = 0;
        this.steal = 0;
        this.block = 0;
        this.shotMade = 0;
        this.shotAttempted = 0;
        this.threeMade = 0;
        this.threeAttempted = 0;
        this.freeThrowMade = 0;
        this.freeThrowAttempted = 0;
        this.turnover = 0;
        this.foul = 0;
        this.flagFoul = 0;

        this.secondsPlayed = 0;
        this.currentStintSeconds = 0;
        this.lastSubbedOutTime = 0;

        this.canOnCourt = true;
        this.hasBeenOnCourt = false;
        this.isOnCourt = false;
    }
    
    /**
     * Get the display name for this player based on the current locale.
     * Returns English name if locale is English, otherwise returns Chinese name.
     * 
     * @return The localized player name
     */
    public String getDisplayName() {
        if (LocalizedStrings.getCurrentLanguage() == LocalizedStrings.Language.ENGLISH) {
            return englishName;
        } else {
            return name;
        }
    }
}
