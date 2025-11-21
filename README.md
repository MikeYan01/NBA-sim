# NBA Simulator

[中文](README_CN.md)

In 2020, the world suffered from the impact of the COVID-19, and the NBA also struggled for a long time before continuing the 2019-2020 and 2020-2021 seasons. It's been a tough year for all NBA players, staff and NBA fans. I had the opportunity to write this NBA simulation program because there were no games to watch during the epidemic, and I hoped to see NBA players continue to play on another court in this way.

Because of the epidemic, many teams were forced to delay their games due to the shortage of players, the shortened season length, the intense schedule, and player injuries. Out of sympathy for all the players who miss games, this program has collected full team rosters for all teams and always ensures that both teams are at full strength before the start of each game (of course, injuries encountered during the game are inevitable). I hope that this is a form of motivation during the special times.

## Introduction

Generate simulated NBA season results based on real NBA teams, player abilities and stats!

- Collect a large number of real NBA players' past game stats and NBA2K player attributes for reference
- Collect a large corpus of text commentary to comment on games play by play
- Consider as many possible events in the game as possible, bringing the game more ups and downs
- Generate detailed player stats and recap after each game, and also detailed player & team stats ranking at the end of the regular season
- Complete 82-game regular season + playoffs simulation. See if your supported team can win the championship!

## Reference sources

- Game parameters setting：[Basketball Reference](https://www.basketball-reference.com/)
- Player shot choice, live comment corpus：[Hupu NBA](https://nba.hupu.com/)
- Player attributes：[2K Ratings](https://www.2kratings.com/)

## File Structure

```
.
├── database/
│   ├── comments/
│   │   ├── comments_en_US.json    English game commentary corpus
│   │   └── comments_zh_CN.json    Chinese game commentary corpus
│   ├── localization/
│   │   ├── strings_en_US.json     English UI strings and labels
│   │   └── strings_zh_CN.json     Chinese UI strings and labels
│   ├── roster/
│   │   ├── 76ers.csv              Team rosters with detailed player attributes
│   │   ├── Bucks.csv              (All 30 NBA teams in English filenames)
│   │   ├── Bulls.csv
│   │   ├── ...
│   │   └── CHEAT.csv                Special test team
│   └── schedule/
│       └── schedule-82games.txt   Full 82-game regular season schedule
├── output/
│   ├── playin-results/            Play-in tournament game result files
│   ├── playoffs-results/          Playoff game result files
│   ├── recap/
│   │   ├── playin-recap.txt       Play-in tournament recaps
│   │   ├── playoff-recap.txt      Playoff game recaps by series
│   │   └── recap.txt              Regular season recaps by date
│   ├── regular-results/           Regular season game result files
│   └── regular-stats/
│       ├── standing.txt           Conference standings
│       └── stat.txt               Player and team statistics rankings
├── src/
│   ├── CommentLoader.java         Load externalized commentary from JSON
│   ├── Comments.java              Generate live game commentary
│   ├── Constants.java             Program-wide constants and team names
│   ├── Game.java                  Simulate games and seasons
│   ├── LocalizedStrings.java      Localization system for bilingual support
│   ├── Main.java                  Main program entry point
│   ├── Player.java                Player class with attributes and stats
│   ├── SeasonStats.java           Season statistics and rankings
│   ├── StatsFormatter.java        Format stats output with localization
│   ├── Team.java                  Team class with roster management
│   └── Utilities.java             Game mechanics and play simulation
├── run.sh                         Compile and run script
├── README.md                      English documentation
└── README_CN.md                   Chinese documentation
```

## Run

The program is developed on Java (JDK 25.0), so a proper [Java Platform](https://www.oracle.com/java/technologies/javase-downloads.html) is needed to be installed.

### Bilingual Support

The simulator supports both **English** and **Chinese** languages for all output, including:
- Game commentary and play-by-play descriptions
- Player and team statistics
- Standings and rankings
- Game recaps and summaries

**Language Selection:**

Make sure you are under **the program root directory**:
    
- For Linux/Unix users, you can directly run the shell script:
  - **Chinese mode (default)**: `./run.sh` or `./run.sh zh`
  - **English mode**: `./run.sh en`
  - **Single game (Chinese)**: `./run.sh Team1 Team2`
  - **Single game (English)**: `./run.sh en Team1 Team2`

- For Windows users, if you have trouble running the shell file, you can manually compile and run:
  - To compile: `javac -encoding UTF-8 src/*.java`
  - **Season (Chinese)**: `java src.Main`
  - **Season (English)**: `java src.Main --lang=en`
  - **Single game (Chinese)**: `java src.Main Team1 Team2`
  - **Single game (English)**: `java src.Main --lang=en Team1 Team2`

**Notes:**
- Team names in commands can use either **English names** (e.g., lakers, thunder) or **Chinese names** (e.g., 湖人, 雷霆)
- All output files will be generated in the selected language
- The system automatically translates team names between languages for roster file access

To re-run the program, just type the command again in the CLI. The program will delete the last simulation result and generate new result files. 

### Championship Prediction Mode

You can simulate the season multiple times to predict the championship probability for each team. This mode runs much faster as it suppresses detailed game outputs.

- **Run 100 simulations (default)**: `./run.sh predict`
- **Run N simulations**: `./run.sh predict N` (e.g., `./run.sh predict 1000`)
- **English mode**: `./run.sh en predict`

The results will be saved to `output/championship_prediction.txt`.

## Result Files

All the result files will be generated in the `output/` folder under the root directory. There are 4 types of result files:

**Game results(All files under `output/regular-results/`, `output/playin-results/` and `output/playoffs-results/` for season mode，.txt file under `output/` for a single game)**

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

**Game recap files(`output/recap/`)**

- `recap.txt` - Regular season game recaps grouped by date, showing final scores and top scorers for each game
- `playoff-recap.txt` - Playoff game recaps grouped by series, showing all games in each series
- `playin-recap.txt` - Play-in tournament game recaps grouped by matchup

## Process

NBA game basic rules:

  - 2 teams in each game
  - Each quarter lasts for 12 minutes, with one quarter consisting of several plays in which the team with possession of the ball attacks and the other team defends; each offensive and defensive play is at most 24 seconds (14 seconds for second chances), and the game moves to the next quarter when time runs out in each quarter
  - Normally, team with higher score wins at the end of 4 quarters. If the scores are tied, an additional 5 minutes of overtime is required, and if the teams are still tied at the end of the overtime, another 5 minutes of overtime is required, until there is a winner

Before the start of each play:

1. Determine the time remaining in the current quarter and decide whether to go to the next quarter or end the game
  
2. Determine whether both teams need to make substitutions. Intelligent substitution system evaluates multiple factors including fould trouble, fatigue management, minutes distribution and current performance.


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
    
    Set the player's normal shot distance as 1 to 35 feet, and generate a random number between [1, 35] as the shot distance. A shot distance >= 23 feet is considered as a three-point shot.

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

    - Players with high offensive consistency are more likely to make shots in clutch time

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
