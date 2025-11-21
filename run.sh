#!/bin/zsh

# NBA Simulator - Run Script
# Usage:
#   ./run.sh                    # Run full season in Chinese (default)
#   ./run.sh en                 # Run full season in English
#   ./run.sh zh                 # Run full season in Chinese
#   ./run.sh en Team1 Team2     # Single game in English
#   ./run.sh zh Team1 Team2     # Single game in Chinese
#   ./run.sh predict [N]        # Run championship prediction (N times, default 100)
#   ./run.sh en predict [N]     # Run prediction in English

echo "Compiling source code..."
javac -encoding UTF-8 src/*.java
if [ $? -ne 0 ]; then
    echo "Compilation failed!"
    exit 1
fi
echo "Compiled successfully!"

echo "Simulating the result..."

# Parse language argument
LANG_ARG=""
TEAM1=""
TEAM2=""

# Check for predict mode
if [ "$1" = "predict" ]; then
    if [ -n "$2" ]; then
        java src/Main --predict=$2
    else
        java src/Main --predict=100
    fi
    echo "Finished!"
    exit 0
fi

# Check for language + predict
if [[ "$1" == "en" || "$1" == "zh" ]] && [[ "$2" == "predict" ]]; then
    if [ -n "$3" ]; then
        java src/Main --lang=$1 --predict=$3
    else
        java src/Main --lang=$1 --predict=100
    fi
    echo "Finished!"
    exit 0
fi

if [ $# -eq 0 ]; then
    # No arguments - default Chinese, full season
    java src/Main
elif [ $# -eq 1 ]; then
    # One argument - language for full season
    if [ "$1" = "en" ] || [ "$1" = "english" ] || [ "$1" = "EN" ]; then
        java src/Main --lang=en
    elif [ "$1" = "zh" ] || [ "$1" = "chinese" ] || [ "$1" = "ZH" ]; then
        java src/Main --lang=zh
    else
        # Assume it's a team name (old behavior)
        java src/Main $1 $2
    fi
elif [ $# -eq 2 ]; then
    # Two arguments - single game with default language
    java src/Main $1 $2
elif [ $# -eq 3 ]; then
    # Three arguments - language + two teams
    if [ "$1" = "en" ] || [ "$1" = "english" ] || [ "$1" = "EN" ]; then
        java src/Main --lang=en $2 $3
    elif [ "$1" = "zh" ] || [ "$1" = "chinese" ] || [ "$1" = "ZH" ]; then
        java src/Main --lang=zh $2 $3
    else
        # Old behavior - assume three team names
        java src/Main $1 $2 $3
    fi
else
    java src/Main "$@"
fi

echo "Finished!"