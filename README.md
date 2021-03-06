# NBA Simulator

[中文](README_CN.md)

In 2020, the world suffered from the impact of the COVID-19, and the NBA also struggled for a long time before continuing the 2019-2020 and 2020-2021 seasons. It's been a tough year for all NBA players, staff and NBA fans. I had the opportunity to write this NBA simulation program because there were no games to watch during the epidemic, and I hoped to see NBA players continue to play on another court in this way.

Because of the epidemic, many teams were forced to delay their games due to the shortage of players, the shortened season length, the intense schedule, and player injuries. Out of sympathy for all the players who miss games, this program has collected full team rosters for all teams and always ensures that both teams are at full strength before the start of each game (of course, injuries encountered during the game are inevitable). I hope that this is a form of motivation during the special times.

## Introduction

Generate simulated NBA season results based on real NBA teams, player abilities and stats!

- Collect a large number of real NBA players' past game stats and NBA2K21 player attributes for reference
- Collect a large corpus of text commentary to comment on games play by play
- Consider as many possible events in the game as possible, bringing the game more ups and downs
- Generate detailed player stats after each game, and also detailed player & team stats ranking at the end of the regular season
- Complete 82-game regular season + playoffs simulation. See if your supported team can win the championship!

## Reference sources

- Game parameters setting：[Basketball Reference](https://www.basketball-reference.com/)
- Player shot choice, live comment corpus：[Hupu NBA](https://nba.hupu.com/)
- Player attributes：[2K Ratings](https://www.2kratings.com/)

## File Structure

```
.
├── database    
  ├── roster    Every team's roster with detailed player attributes
  └── schedule    Schedule for the whole regular season
├── output    
  ├── Team1Team2.txt   Result file of a single game
  ├── playoffs-results   Result files of all playoffs games
  ├── regular-results   Result files of all regular season games
  └── regular-stats   All stats and rankings files of the regular season
├── run.sh    A shell script to compile and run the program
├── src    
  ├── Roster.py    2KRatings player attributes scraping
  ├── Comments.java    Generate game live comments
  ├── Constants.java    Constant variables defined in the whole program
  ├── Game.java    Simulate a single game, or a full season    
  ├── Main.java    Main program entry     
  ├── Player.java    Class Player
  ├── SeasonStats.java    Class to update regular season stats and ranking
  ├── Team.java    Class Team
  └── Utilities.java    Define how each module of the game works
```

## Run

The program is developed on Java (JDK 16.0), so a proper [Java Platform](https://www.oracle.com/java/technologies/javase-downloads.html) is needed to be installed.

Make sure you are under **the program root directory**:
    
- For Linux/Unix users, you can directly run shell script to compile and run the program
  - To host a season: `./run.sh`
  - To host a single game: `./run.sh Team1 Team2`, `Team1` and `Team2` are the names of two matching teams

- For Windows users, if you have trouble running the shell file, you can manually compile and run the program:
  - To compile: `javac src/*.java`
  - To host a season: `java src/Main`
  - To host a single game: `java src/Main Team1 Team2`

The re-run the program, just type the command again in the CLI, the program will delete the last simulation result and generate new result files again. 


## Result Files

All the result files will be generated in the `output/` folder under the root directory. There are 3 types of result files:

**Game results(All files under `output/regular-results/` and `output/playoffs-results/` for season mode，.txt file under `output/` for a single game)**

- Live comments play by play
- Quarter scores detail
- Players and teams stats after game

**Team standing (`output/regular-stats/standing.txt`)**

- Eastern and Western conferences standings

**Player and team stats(`output/regular-stats/stat.txt`)**

Player：

- Score per game 
- Rebound per game
- Assist per game
- Steal per game
- Block per game
- Three points shot made per game
- Free throws made per game

Team：

- Score per game
- Opponent score allowed per game
- Shots made per game
- Field goal percent per game
- Three points shot percent per game

## Process

NBA game basic rules:

  - 2 teams in each game
  - Each quarter lasts for 12 minutes, with one quarter consisting of several plays in which the team with possession of the ball attacks and the other team defends; each offensive and defensive play is at most 24 seconds (14 seconds for second chances), and the game moves to the next quarter when time runs out in each quarter
  - Normally, team with higher score wins at the end of 4 quarters. If the scores are tied, an additional 5 minutes of overtime is required, and if the teams are still tied at the end of the overtime, another 5 minutes of overtime is required, until there is a winner

Before the start of each play:

1. Determine the time remaining in the current quarter and decide whether to go to the next quarter or end the game
  
2. Determine whether both teams need to make substitutions
    - Typically both teams will substitute their starters with benches; in the first and third quarters, and starters will come back in the second and fourth quarters
    - If the score deficit is too large in the fourth quarter, both teams will let benches to play for the rest of the game; however, if the deficit is quickly reduced in garbage time, the starters will still be back in the game
    - Overtime will be played by the starters unless a starter player is injured or get fouled out

Every play should have the following process:

1. Randomly select the offense player (based on the player's overall rating and playmaking ability), and then randomly select the corresponding defender (likely have the same position)
   
2. Determine whether offense team loses ball:
    - Personal turnover
    - Get stealed by defense player
    - Held ball
  
3. Determine whether there is foul before the shot:
    - Offensive foul（Charging foul, Illegal screen, ...）
    - Defensive foul（Grabbing, Reach-in, Blocking...）
    - Each team has a chance to challenge the foul. If challenge successfully, the foul will be canceled and the possession will be returned
  
4. Determine the shot distance of the offense players
    
    Set the player's normal shot distance as 1 to 32 feet, and generate a random number between [1, 32] as the shot distance. A shot distance >= 23 feet is considered as a three-point shot.

    Different players have different playing styles and shooting tendencies, resulting in different ranges of random number generation for the actual simulation

5. Determine the shot position and angle

6. Determine the shot type
  
     - In close range, players can layup / dunk / shoot
     - Otherwise, players can only shoot

7. Determine if the shot is blocked

8.  Calculate the shot goal percentage, based on the following factors:

    - Shot distance

    - Layup and dunk have higher percentage than normal shooting

    - Defense density can affect the percentage. Density could be loose, normal or intense

    - Offense player's offense consistency, and defense player's defense consistency

    - Mr.Clutch have more chances to make a shot in clutch time

    - Athleticism difference between offense and defense players

9.  If makes the shot:

    - Check if there is an assist
    - Check if there is an And-One play

10. If misses the shot:

    - Check if there is a foul and give free throws
      - Each team has a chance to challenge the foul. If challenge successfully, the foul will be canceled and the possession will be returned
    - Two teams will compete for the rebound

11. Free throw

    - Based on player's free throw ability

12. Determine rebound

    - Offense team have small possibility to grab offensive rebound, while defense team have much larger possibility to grab defensive rebound
    - Offense team have more chance to grab offensive rebound, if offense team players on the court have better rebound abilities than defense team players

13. Determine if there is any injury in this play
    - An injured player will immediately get substituted by a teammate

## Roster update

If you need to update the current team roster, please execute the Python file `src/Roster.py` in the root directory:

```bash
python src/Roster.py
```

The code will automatically scrape the latest roster to the `tempFolder` folder in the root directory. You can manually make extra updates or directly overwrite the source roster files.
