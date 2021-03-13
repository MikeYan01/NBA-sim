# NBA Simulator

## 作者之声

2020年，全球遭受了新冠疫情的冲击，NBA也在停摆边缘挣扎许久后，以Bubble园区复赛、无球迷入馆比赛等方式继续进行2019-2020、2020-2021赛季。这一年对所有NBA的球员、工作人员以及广大NBA球迷都是艰难的。作者也是因为疫情期间无球赛可看，才有契机编写出此NBA模拟程序，希望以这种方式看到NBA球员们继续在另一个球场上拼搏。

因为疫情，很多球队因为球员染病或隔离导致长期人员轮换短缺，甚至直接因为参赛人数不够被迫延误比赛；缩短的赛季时长导致赛程密集，球员伤病迭起。因此，这两个赛季很多球队都出现了走马灯似的球员流动。出于对球员健康的同情，本程序尽可能多地收集了球员名单，并且始终保证每一场开始前，两队球员都是满员出战（当然，比赛中遇到的伤病不可避免），希望通过这种方式不让任何一位球员直接缺席一场比赛，也算是特殊时期中的一种激励方式。

## 程序简介
基于真实的NBA球队、球员能力与数据，生成模拟的NBA赛季结果!

- 预获取大量真实NBA球员过往比赛数据、NBA2K21能力值等数据作为参考
- 预获取大量文字解说语料库，实况播报比赛中的每一个回合
- 尽可能多地考虑了比赛中可能发生的事件，使得比赛过程更加跌宕起伏
- 详尽的球员赛后数据统计，并且常规赛结束后会生成完整的常规赛球队排名、球员与球队的数据排名
- 完整的82场常规赛 + 季后赛模拟，看看你支持的球队能否夺冠！

## 参考数据来源

- 比赛参数设定：[Basketball Reference](https://www.basketball-reference.com/)
- 球员现实出手分布、文字解说素材库：[虎扑NBA](https://nba.hupu.com/)
- 球员能力值：[2K Ratings](https://www.2kratings.com/)

## 文件目录
```
.
├── database    所有球队的球员名单与能力数据
  ├── player-names   所有球队球员纯姓名文本
  ├── roster    每支球队球员名单与能力详情
  └── schedule    球队赛程文件
├── output    比赛结果文件夹
  ├── Team1Team2.txt   单场比赛模式下的结果文本文件
  ├── playoffs-results   赛季模式下所有季后赛比赛文本文件
  ├── regular-results   赛季模式下所有常规赛比赛文本文件
  └── regular-stats   赛季模式下常规赛分区排名、球员数据统计、球队数据统计文件
├── run.sh    编译运行脚本文件
├── src    源代码文件
  ├── Roster.py    2KRatings名单爬取
  ├── Comments.java    解说素材库与选择
  ├── Game.java    模拟一场比赛或一个赛季     
  ├── Main.java    主程序入口     
  ├── Player.java    球员类及其实现
  ├── SeasonStats.java    赛季数据统计类及其实现
  ├── Team.java    球队类及其实现
  └── Utilities.java    比赛各模块的执行
```

## 运行与结果

程序基于Java (JDK 15.0)开发。

手动编译运行方式:

1. 在根目录下，编译代码: `javac src/*.java`
2. 在根目录下，运行代码: 
     - 默认情况下，不输入额外参数`java src/Main`将模拟整个NBA赛季。所有的结果文件将按照常规赛、季后赛分别生成对应文件夹中，并提供常规赛排名、球员数据统计、球队数据统计查询功能（文件目录格式详情请参阅上一节）
     - 如果要指定两队比赛，则输入`java src/Main Team1 Team2`，其中`Team1`和`Team2`是比赛双方球队的中文名 (例如:`java src/Main 湖人 快船`)

shell编译运行方式:

1. 在根目录下：
     - 一次性模拟整个赛季，运行shell脚本: `./run.sh`
     - 指定两队比赛，运行shell脚本: `./run.sh Team1 Team2`

本程序支持重复多次运行，直接在命令行再次输入命令即可，程序将删除上次模拟结果，重新在相同路径下生成新结果文件。所有结果文件都将生成在`output/`文件夹中。共有3类结果文件：

**单场比赛文件(赛季模式下regular-results/和playoffs-results/中的所有文件，单场比赛模式下直接位于output/根目录下的.txt文件)**

- 全场比赛实况文字解说
- 分节比分详情
- 两队球员个人数据统计、全队总数据统计

**赛季排名文件(regular-stats/standing.txt)**

- 东西分区球队排名、胜负场次、胜率

**赛季数据统计文件(regular-stats/stat.txt)**

注：所有榜单均为场均数据统计

- 球员得分榜
- 球员篮板榜
- 球员助攻榜
- 球员抢断榜
- 球员盖帽榜
- 球员三分榜
- 球员罚球榜
- 球队得分榜
- 球队失分榜

## 执行逻辑与步骤

NBA比赛基本规则:

  - 每场比赛有2支球队参加
  - 每节比赛共12分钟，一节比赛由若干回合构成，在一个回合中持有球权的球队进攻，另一支球队防守；每个攻防回合上限耗时24秒，每节时间耗尽则比赛进入下一节
  - 常规情况下，4节比赛结束时，得分高的球队获胜；如果双方战成平手，则需要额外进行5分钟加时赛，加时赛结束时如果双方仍然战平则需要再进行5分钟加时赛，一直到双方决出胜负为止

每个攻防回合开始前，需要进行比赛时间与换人判定:

1. 判断当前比赛剩余的时间，决定是否进入下一节或者结束比赛

2. 判断双方球队是否需要换人

每个攻防回合包含以下流程:

1. 随机选出进攻方当前回合的进攻者（根据球员综合能力值，进攻组织能力等参数决定概率），再随机选出对应的防守者（大概率是同位置的球员，小概率是其他位置的球员换防）
   
2. 判断进攻方在出手前，是否已经丢球:
    - 进攻者出现个人失误（运球/传球出界、二次运球、回场......）
    - 进攻者被对位防守者抢断
      - 如果球员抢断能力值比较优秀，有额外加成
      - 触发抢断后，有大概率打成2分必中快攻（由抢断者或着跟上的队友完成）；有小概率稳住球权，组织一次正常24秒进攻
  
3. 判断进攻方在出手前，是否出现了无罚球的犯规:
    - 进攻方出现进攻犯规（带球撞人、无球掩护犯规......）
    - 防守方出现防守犯规（拉人、推人......）
      - 每次防守犯规会计入本节的犯规统计，如果本节达到5次犯规，进攻方需要额外进行罚球
      - 首发球员过早陷入犯规危机时，会被提前换下场进行保护
    - 每次犯规后，检查犯规者是否达到六次犯规，被罚出场外
  
4. 判断进攻方的出手距离，主要是根据5种球员类型判定可能的出手距离与对应概率:
    设定球员的正常出手距离是1～30英尺，在[1, 30]之间生成随机数作为出手距离。出手距离 >= 24英尺视为三分球出手。

    不同球员的打法、出手倾向不同，导致实际模拟时的随机数生成范围也不尽相同：

    - 全能球员，出手距离定义为1～30英尺
    - 纯篮下球员，无中远投能力，出手距离定义为1～12英尺
    - 可内线&中投球员，但无三分/基本不投三分，出手距离定义为1～25英尺
    - 内线+三分球员，基本不投中投，因此将范围分为1～10英尺、11～23英尺、24～30英尺三段，生成的随机数只有小概率出自11～23英尺
    - 外线射手，大概率进行三分或长中投出手。

    因为生成的随机数理论上在1～30的区间内是均匀分布的，但实际上大多数三分出手都是在三分线附近完成，因此本作对于较远的三分出手进行了优化，有概率将距离校正到离三分线更近的位置。

5. 判定出手位置，根据0~180度之间的角度，结合此前的出手距离，确定出手点

6. 判定出手方式，需要结合出手距离决定:
    - 如果出手距离在10英尺之内
      - 根据扣篮倾向不同的球员（几乎不扣篮的小后卫 / 普通球员 / 身体素质优秀的暴力扣将），设定不同的上篮 / 扣篮 / 投篮概率
    - 出手距离在11英尺及以上，设定只能投篮

7. 判定当前出手是否被盖帽:
    - 进攻者每次进攻都可能被对位防守者盖帽
      - 如果球员盖帽能力值比较优秀，有额外加成
    - 盖帽后，有小概率直接扇出界；大概率需要进入抢篮板判断

8. 计算命中概率，需要结合出手距离、球员能力决定:

    - 常规情况下，投篮命中概率取决于出手距离

    - 额外影响投篮命中概率的因素
      - 扣篮，命中率显著提升
      - 上篮，命中率根据球员上篮能力提升
      - 内线出手主要取决于球员内线近距离投篮能力
      - 中距离出手主要取决于球员中投能力
      - 三分出手主要取决于球员三分能力
      - 内线出手时会受到防守球员内线防守能力的影响
      - 中投和三分出手时会受到防守球员外线防守能力的影响

    - 如果在场存在其他组织进攻能力优秀的球员，会有命中率额外加成

    - 检查防守强度
      - 小概率出现防守漏人、松懈甚至不防守的情况，命中率显著提升
      - 稍大概率出现防守紧逼、严实的情况，命中率显著下降
      - 更多时候，防守强度适中，命中率不变

    - 稳定性
      - 进攻球员的进攻稳定性会额外影响本次进攻的命中率
      - 对位防守球员的防守稳定性会额外影响本次防守的成功率

    - 如果该球员综合能力值较高
      - 明星球员阅读比赛能力强，有更高的可能性得分
      - 根据球员的综合能力值，能力值越高额外加成的命中率越大
  
    - 从第四节开始，本节剩余时间6分钟以内，双方分差在8分以内，比赛将陷入焦灼，普通球员受到压力影响命中率会下降，但具有关键先生特质的球员不受此影响

    - 攻防球员的运动能力差异，可能使得命中率进一步增大/降低

9.  如果出手命中:

    - 检查该球员是受到助攻还是自己单干命中
      - 如果该球员是明星球员，有一定概率来自于助攻，也有一定概率来自于个人单打
      - 如果该球员是普通球员，很大概率来自于助攻

    - 进行加罚检查
      - 按照出手距离（内线/中投/三分），分区间决定犯规概率
      - 明星球员概率在此基础上额外加成
      - 每次犯规后，检查犯规者是否达到六次犯规，被罚出场外

10. 如果投篮不中:

    - 在更新数据前，先进行犯规检查
      - 按照出手距离（内线/中投/三分），分区间决定犯规概率
      - 明星球员概率在此基础上额外加成
      - 犯规有可能是恶意犯规，需要进行两罚一掷，默认由被犯规球员完成罚球
      - 每次犯规后，检查犯规者是否达到六次犯规或两次恶意犯规，被罚出场外
      - 首发球员过早陷入犯规危机时，会被提前换下场进行保护

    - 如果无犯规，更新数据，有很小的概率球会直接弹飞出界，其余情况进入抢篮板阶段

11. 罚球判断

    - 按照球员的罚球能力模拟罚球是否命中
    - 最后一罚如果不中，进入抢篮板阶段

12. 抢篮板

    - 小概率进攻方抢到进攻篮板，大概率防守方抢到防守篮板
    - 如果进攻方orbRating总和大于防守方Rating总和，进攻方就会有更高概率抢到进攻篮板

13. 判断本回合是否有球员受伤
    - 每个球员每个回合有极小的概率受伤
    - 受伤的球员会被本队另一名球员替换下场，且他本场比赛其余时间无法再次登场

## 名单更新

如果需要更新当前球队名单，请在根目录下执行`src/Roster.py`文件，代码将自动爬取最新名单到根目录下的`tempFolder/`文件夹中，用户可视需要作进一步更新、覆盖替换源文件。

注：目前该功能仅支持Linux/Unix系统。
