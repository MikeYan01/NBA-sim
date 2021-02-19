package src;

public class Player {
    public String name;
    public String position;

    public int playerType; // 1: all-rounded  2: insider  3: mid-range  4: insider + outsider 5: outsider
    public int dunkerType; // 1: rarely dunk  2: normal  3: excellent dunker
    public int rotationType; // 1: starting lineup  2: normal bench  3: bench that rarely show up

    // rating
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

    // bonus badge
    public boolean isMrClutch;
    public boolean isStar;

    // stats in a game
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

    // whether the player can/cannot be on court
    public boolean canOnCourt;

    // whether the player has/hasn't been on court
    public boolean hasBeenOnCourt;

    /**
	 * Construct a Player object, which can be conceived as an NBA player.
     * 
     * @param name Player's name
     * @param position Player's position on the court (C / PF / SF / SG / PG)
     * @param playerType Player's player type, check annotation above
     * @param rotationType Player's rotation type, check annotation above
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
     * @param isMrClutch Whether player is a clutch player
	 */
    public Player(String name, String position, String playerType, String rotationType, String rating, String insideRating, String midRating, 
                  String threeRating, String freeThrowPercent, String interiorDefense, String perimeterDefense, String orbRating,
                  String drbRating, String astRating, String stlRating, String blkRating, String layupRating, String standDunk,
                  String drivingDunk, String athleticism, String durability, String offConst, String defConst, String drawFoul,
                  String isMrClutch) {
        this.name = name;
        this.position = position;
        this.playerType = Integer.parseInt(playerType);
        this.rotationType = Integer.parseInt(rotationType);

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

        // Type 1 - rarely dunk: stankDunk + drivingDunk <= 60
        // Type 2 - normal: other cases
        // Type 3 - excellent dunker: stankDunk + drivingDunk >= 160, or one of them exceeds 90
        if (this.standDunk + this.drivingDunk <= 60) this.dunkerType = 1;
        else if (this.standDunk + this.drivingDunk >= 160 || 
                (this.standDunk + this.drivingDunk < 160 && this.standDunk >= 90) ||
                (this.standDunk + this.drivingDunk < 160 && this.drivingDunk >= 90))
            this.dunkerType = 3;
        else this.dunkerType = 2;

        this.isMrClutch = isMrClutch.equals("1") ? true : false;

        // player with general rating > 85 is a star player
        this.isStar = this.rating > 85 ? true : false;

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

        this.canOnCourt = true;
        this.hasBeenOnCourt = false;
    }
}
