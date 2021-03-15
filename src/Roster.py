import requests, sys
from bs4 import BeautifulSoup
import os
import math

"""
URL and headers
"""
headers = {'User-Agent':'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.89 Safari/537.36'}
URL = "https://www.2kratings.com/"

"""
read teams rosters
"""
TEAM_LIST = ["76ers", "bulls", "celtics", "kings", "suns", "warriors", "wizards", "knicks", "trail-blazers", "clippers", "nuggets", "timberwolves", \
             "pacers", "pistons", "lakers", "rockets", "grizzlies", "heat", "jazz", "mavericks", "raptors", "nets", "hawks", "bucks", "thunder", \
             "spurs", "cavaliers", "magic", "pelicans", "hornets"]

# TEAM_LIST = ["1011-mavericks", "9596-bulls", "0708-celtics", "1314-spurs", "0001-lakers", "1617-warriors"]

TEAM_NAME_MAPPING = {
    "76ers": "76人",
    "bulls": "公牛",
    "celtics": "凯尔特人",
    "kings": "国王",
    "suns": "太阳",
    "warriors": "勇士",
    "wizards": "奇才",
    "knicks": "尼克斯",
    "trail-blazers": "开拓者",
    "clippers": "快船",
    "nuggets": "掘金",
    "timberwolves": "森林狼",
    "pacers": "步行者",
    "pistons": "活塞",
    "lakers": "湖人",
    "rockets": "火箭",
    "grizzlies": "灰熊",
    "heat": "热火",
    "jazz": "爵士",
    "mavericks": "独行侠",
    "raptors": "猛龙",
    "nets": "篮网",
    "hawks": "老鹰",
    "bucks": "雄鹿", 
    "thunder": "雷霆",
    "spurs": "马刺", 
    "cavaliers": "骑士",
    "magic": "魔术",
    "pelicans": "鹈鹕",
    "hornets": "黄蜂",
    "1011-mavericks": "1011小牛",
    "9596-bulls": "9596公牛",
    "0708-celtics": "0708凯尔特人",
    "1314-spurs": "1314马刺",
    "0001-lakers": "0001湖人",
    "1617-warriors": "1617勇士"
}

for TEAM_NAME in TEAM_LIST:

    print(TEAM_NAME)

    ROSTER_PATH = "./database/player-names/" + TEAM_NAME + ".txt"
    NAME_LIST = []

    with open(ROSTER_PATH, 'r') as rf:
        lines = rf.readlines()

        for line in lines:
            if line == '\n' or len(line) <= 1:
                continue
            
            if line[-1] == '\n':
                NAME_LIST.append(line[:-1])
            else:
                NAME_LIST.append(line)

    """
    each team's detail
    """
    if not os.path.exists("./tempFolder"):
        os.mkdir("./tempFolder")

    # old data files
    OLD_FILE_NAME = "./database/roster/" + TEAM_NAME_MAPPING[TEAM_NAME] + ".csv"
    old_file_lines = []
    old_file_data = {} # line -> line split

    with open(OLD_FILE_NAME, 'r') as of:
        old_file_lines = of.readlines()
        for line in old_file_lines:
            if line[-1] == '\n':
                line = line[:-1]
            old_file_data[line] = line.split(",")
        of.close()

    # new data files
    FILE_NAME = "./tempFolder/" + TEAM_NAME_MAPPING[TEAM_NAME] + ".csv"
    with open(FILE_NAME, 'a+') as f:
        f.write("name,position,playerType,rotationType,rating,insideRating,midRating,threeRating,freeThrowPercent,interiorDefense,")
        f.write("perimeterDefense,orbRating,drbRating,astRating,stlRating,blkRating,layupRating,standDunk,drivingDunk,athleticism,")
        f.write("durability,offConst,defConst,drawFoul,isMrClutch,enName\n")
        f.close()

    for NAME in NAME_LIST:
        """
        request and parse
        """
        result = []
        r = requests.get(URL + NAME, headers = headers)
        pageStr = r.content
        soup = BeautifulSoup(pageStr, "html.parser", from_encoding="gbk")

        """
        overall rating
        """
        overall_rating = soup.find('div', attrs={'class':'w-100 text-center mb-4'}).find('span')
        result.append(overall_rating.text) # overall rating

        """
        three columns
        """
        column1 = soup.find('div', attrs={'class':'tab-content mb-4 pb-2'}).find('div', attrs={'class':'col-12 col-md-4 ml-md-n1'})
        column23 = soup.find('div', attrs={'class':'tab-content mb-4 pb-2'}).find_all('div', attrs={'class':'col-12 col-md-4'})

        """
        column1
        """
        column1_cards = column1.find_all('div', attrs={'class':'card'})
        # card 1: Outside Scoring
        column1_card1 = column1_cards[0].find('ul', attrs={'class':'list-group list-no-bullet'}).find_all('span')
        column1_card1_result = [each.text for each in column1_card1]
        # card 2: Athleticism
        column1_card2_header = column1_cards[1].find('div', attrs={'class':'card-header'}).find('span')
        column1_card2_content = column1_cards[1].find('ul', attrs={'class':'list-group list-no-bullet'}).find_all('span')
        column1_card2_result = [each.text for each in column1_card2_content]

        """
        column2
        """
        column2_cards = column23[0].find_all('div', attrs={'class':'card'})
        # card 1: Inside Scoring, card 2: Playmaking
        column2_card1 = column2_cards[0].find('ul', attrs={'class':'list-group list-no-bullet'}).find_all('span')
        column2_card1_result = [each.text for each in column2_card1]
        column2_card2 = column2_cards[1].find('div', attrs={'class':'card-header'}).find('span')
        column2_card2_content = column2_cards[1].find('ul', attrs={'class':'list-group list-no-bullet'}).find_all('span')
        column2_card2_result = [int(each.text) for each in column2_card2_content]

        """
        column3
        """
        column3_cards = column23[1].find_all('div', attrs={'class':'card'})
        # card 1: Defending, card 2: Rebounding
        column3_card1 = column3_cards[0].find('ul', attrs={'class':'list-group list-no-bullet'}).find_all('span')
        column3_card1_result = [each.text for each in column3_card1]

        column3_card2 = column3_cards[1].find('ul', attrs={'class':'list-group list-no-bullet'}).find_all('span')
        column3_card2_result = [each.text for each in column3_card2]

        """
        get result
        """
        result.append(column1_card1_result[0]) # Close Shot
        result.append(column1_card1_result[1]) # Mid-Range Shot
        result.append(column1_card1_result[2]) # Three-Point Shot
        result.append(column1_card1_result[3]) # Free Throw

        result.append(column3_card1_result[0]) # Interior Defense
        result.append(column3_card1_result[1]) # Perimeter Defense
        result.append(column3_card2_result[0]) # Offensive Rebound
        result.append(column3_card2_result[1]) # Defensive Rebound

        ast_avg = (sum(column2_card2_result) - column2_card2_result[2]) / 4
        result.append(str(math.ceil(ast_avg))) # Assist Rating

        result.append(column3_card1_result[2]) # Steal
        result.append(column3_card1_result[3]) # Block

        result.append(column2_card1_result[0]) # Layup
        result.append(column2_card1_result[1]) # Standing Dunk
        result.append(column2_card1_result[2]) # Driving Dunk

        result.append(column1_card2_header.text) # Athleticism
        result.append(column1_card2_result[6]) # Durability

        result.append(column1_card1_result[5]) # Offensive Consistency
        result.append(column3_card1_result[7]) # Defensive Consistency

        result.append(column2_card1_result[6]) # Draw Foul

        """
        write to file
        """
        with open(FILE_NAME, 'a+') as f:
            temp_data = []
            for key in old_file_data.keys(): 
                if (old_file_data[key][-1] == NAME):
                    temp_data = old_file_data[key]
                    break
            
            f.write(temp_data[0] + "," + temp_data[1] + "," + temp_data[2] + "," + temp_data[3] + ",")

            for i in range(len(result)):
                f.write(result[i] + ",")
            
            f.write(temp_data[-2] + "," + NAME + "\n")
