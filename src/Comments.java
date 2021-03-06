package src;

import java.util.*;

public class Comments {
    // The Random object to generate random number
    public static Random random = new Random();

    // random index of corpus
    private static int rdm = -1;

    // The string builder for comment output
    public static StringBuilder sb = new StringBuilder(Constants.MAX_SB_LEN);

    /**
     * Randomly pick one sentence from the corpus and output it.
     * 
     * @param corpus Possible live comments collection.
     * @param output Boolean identifier for comment output.
     * @return Picked live comment.
     */
    public static String pickStringOutput(String[] corpus, boolean output) {
        rdm = Utilities.generateRandomNum(random, 1, corpus.length) - 1;
        String pickedComment = corpus[rdm];

        if (output) System.out.println(corpus[rdm]);
        return pickedComment;
    }

    /**
     * Get player's last name from full name.
     * 
     * @param name Player's full name
     * @return Player's last name
     */
    public static String getLastName(String name) {
        if (!name.contains("·")) return name;

        String[] fullName = name.split("·");
        return fullName[fullName.length - 1];
    }

    /**
     * Generates comments when jumping ball.
     * 
     * @param winTeam Team that wins jump ball
     * @return Player's last name
     */
    public static void getJumpBallComments(Team team1, Team team2, Team winTeam) {
        sb.delete( 0, sb.length() );
        sb.append("欢迎收看NBA比赛!\n今天为您带来的是").append(team1.name).append("对阵").append(team2.name).append("的实况转播!\n");
        sb.append("球员差不多热身完毕!裁判看表准备开始比赛!\n双方队员围到中圈跳球!\n");
        sb.append(winTeam.name).append("跳球获胜，率先进攻!");
        System.out.println(sb.toString());
    }

    /**
     * Generates two players jumping ball.
     * 
     * @param offensePlayer Offense player
     * @param defensePlayer Defense player
     * @param winPlayer The player that wins the jumpball
     */
    public static void getJumpBallComments(String offensePlayer, String defensePlayer, String winPlayer) {
        sb.delete( 0, sb.length() );
        offensePlayer = getLastName(offensePlayer);
        defensePlayer = getLastName(defensePlayer);
        winPlayer = getLastName(winPlayer);

        String[] resources1 = {
            offensePlayer + "杀入禁区!" + defensePlayer + "上来阻挠!\n双方纠缠不清!各自紧紧抓着一边篮球不放!\n裁判吹哨示意争球!",
            offensePlayer + "遭遇重重防守围堵!\n混乱之中和" + defensePlayer + "拼抢在一起!\n裁判上来制止并且给了争球!",
            defensePlayer + "防守极其卖力!\n顶上来直接伸手死死抱住皮球!\n" + offensePlayer + "也不甘放手!双方僵持不下!\n哨响!裁判给了争球!"
        };
        String[] resources2 = {
            "双方来到中线跳球!\n" + winPlayer + "手疾眼快跳到皮球!",
            "双方来到罚球线跳球!\n" + winPlayer + "跳球获胜!"
        };

        pickStringOutput(resources1, true);
        pickStringOutput(resources2, true);
    }
    
    /**
     * Generate shot position comments.
     * 
     * @param get Team that wins jump ball
     * @return Player's shot position string
     */
    public static String getShotPos(Random random, int distance) {
        int degree = Utilities.generateRandomNum(random, 1, 180);

        if (distance <= 10) return "篮下";
        else if (degree <= 30 && distance <= 15) return "左侧底角三秒区附近";
        else if (degree <= 30 && distance > 15) return "左侧底角附近";
        else if (degree <= 60 && distance <= 15) return "左侧45度角三秒区附近";
        else if (degree <= 60 && distance > 15) return "左侧45度角附近";
        else if (degree <= 120 && distance <= 20) return "罚球线附近";
        else if (degree <= 120 && distance > 20) return "弧顶";
        else if (degree <= 150 && distance <= 15) return "右侧45度角三秒区附近";
        else if (degree <= 150 && distance > 15) return "右侧45度角附近";
        else if (degree <= 180 && distance <= 15) return "右侧底角三秒区附近";
        else return "右侧底角附近";
    }

    /**
     * Generate layup comments.
     * 
     * @return Player's layup comments string
     */
    public static String pickLayup(Random random) {
        String[] resources = {
            "上篮",
            "滑翔上篮",
            "三步上篮",
            "接球顺步上篮",
            "突破上篮",
            "拉杆上篮",
            "一条龙上篮",
            "折叠上篮",
            "垫步上篮",
            "高抛上篮",
            "大跨步上篮",
            "小抛投上篮",
            "打板上篮",
            "低手上篮",
            "finger roll挑篮",
            "换手上篮",
            "反手上篮",
            "凌波微步上篮",
            "回头望月上篮",
            "梯云纵上篮",
            "举火烧天飘逸上篮",
            "360度转身上篮",
            "爱的魔力转圈圈上篮",
            "蜻蜓点水点篮",
            "强起放篮"
        };
        
        return pickStringOutput(resources, false);
    }

    /**
     * Generate dunk comments.
     * 
     * @param dunkerType Player's dunkerType
     * @return Player's dunk comments string
     */
    public static String pickDunk(Random random, int dunkerType) {
        String[] resources1 = {
            "扣篮",
            "单手暴扣",
            "双手暴扣",
            "砸框重扣",
            "抡臂重扣",
            "战斧暴扣",
            "背身扣篮"
        };
        String[] resources2 = {
            "扣篮",
            "单手暴扣",
            "双手暴扣",
            "砸框重扣",
            "抡臂重扣",
            "战斧暴扣",
            "背身扣篮",

            "折叠暴扣",
            "滑翔暴扣",
            "360度转身暴扣",
            "旱地拔葱暴扣",
            "罚球线起跳暴扣",
            "大风车暴扣",
            "平框暴扣"
        };

        return pickStringOutput(dunkerType == 3 ? resources1 : resources2, false);
    }

    /**
     * Generate shot comments.
     * 
     * @param distance Player's shot distance
     * @return Player's shot comments string
     */
    public static String pickShot(Random random, int distance) {
        String[] resources1 = {
            "投篮",
            "跳投",
            "后仰跳投",
            "后撤步跳投",
            "前倾跳投",
            "急停跳投",
            "干拔跳投",
            "潇洒滞空跳投",
            "Fade-away Shot",
            "骑马射箭",
            "横移跳投",
            "试探步跳投"
        };
        String[] resources2 = {
            "投篮",
            "跳投",
            "后仰跳投",
            "后撤步跳投",
            "前倾跳投",
            "急停跳投",
            "干拔跳投",
            "潇洒滞空跳投",
            "Fade-away Shot",
            "骑马射箭",
            "横移跳投",
            "试探步跳投",

            "抛投",
            "单脚丫性感小抛投",
            "丝滑柔顺小抛投",
            "勾手投篮",
            "滑翔抛射",
            "转身跳投",
            "背身跳投",
            "翻身跳投"
        };

        String suffix = distance >= Constants.MIN_THREE_SHOT ? "三分" : "";
        String result = pickStringOutput(distance >= Constants.SHOT_CHOICE_THLD ? resources1 : resources2, false);

        sb.delete( 0, sb.length() );
        sb.append(result).append(suffix);
        return sb.toString();
    }

    /**
     * Generate shot choice comments.
     * 
     * @param distance Player's shot distance
     * @return Player's shot comments string
     */
    public static String getShotChoice(Random random, Player player, int distance, String shotPos) {
        int temp = Utilities.generateRandomNum(random);
        int dunkerType = player.dunkerType;
        String movement = "";
        if (distance <= Constants.MAX_CLOSE_SHOT) {
            if (dunkerType == 1) {
                if (temp <= Constants.TYPE_1_LAYUP) movement = pickLayup(random);
                else if (temp <= Constants.TYPE_1_LAYUP + Constants.TYPE_1_DUNK) movement = pickDunk(random, dunkerType);
            } else if (dunkerType == 2) {
                if (temp <= Constants.TYPE_2_LAYUP) movement = pickLayup(random);
                else if (temp <= Constants.TYPE_2_LAYUP + Constants.TYPE_2_DUNK) movement = pickDunk(random, dunkerType);
            } else {
                if (temp <= Constants.TYPE_3_LAYUP) movement = pickLayup(random);
                else if (temp <= Constants.TYPE_3_LAYUP + Constants.TYPE_3_DUNK) movement = pickDunk(random, dunkerType);
            }

            if (movement.equals("")) movement = pickShot(random, distance);
        } else movement = pickShot(random, distance);

        sb.delete( 0, sb.length() );
        sb.append(distance).append("英尺外");
        if (Utilities.generateRandomNum(random) <= Constants.SHOT_POSITION_PERCENT) sb.append(shotPos);
        sb.append(movement).append("!");

        System.out.println(sb.toString());
        return movement;
    }

    /**
     * Generate player celebration comments with a specified percentage.
     * 
     * @param name Player's name
     * @param percent The percent to generate celebrate comment
     */
    public static void getCelebrateComment(String name, int percent) {
        if (Utilities.generateRandomNum(random) <= percent) {
            String lastName = getLastName(name);
            String[] resources = {
                lastName + "看起来面无表情!十分淡定!",
                lastName + "兴高采烈!跟板凳席上的队友不停击掌!",
                lastName + "兴奋地与队友撞胸庆祝!",
                lastName + "霸气环视全场!",
                lastName + "振臂高呼!",
                lastName + "双手举起带动场边观众庆祝!",
                lastName + "疯狂捶胸怒吼庆祝!",
                "队友纷纷围上来跟" + lastName + "撞胸庆祝!",
                lastName + "跟场边球迷有说有笑!十分自信!"
            };
            pickStringOutput(resources, true);
        }
    }

    /**
     * Generate player upset comments with a specified percentage.
     * 
     * @param name Player's name
     * @param percent The percent to generate upset comment
     */
    public static void getUpsetComment(String name, int percent) {
        if (Utilities.generateRandomNum(random) <= percent) {
            String lastName = getLastName(name);
            String[] resources = {
                lastName + "一脸懊恼!",
                lastName + "很不满意!",
                lastName + "一脸懵逼!",
                lastName + "面露不满!",
                lastName + "一边往回走一边嘴唠唠叨叨地抱怨!",
                lastName + "表情激动，口吐芬芳!",
                lastName + "抬手表示无语!",
                "教练在场边对着" + lastName + "大声呵斥!要求他更加专注!",
                "教练无可奈何!对着" + lastName + "欲言又止!",
                "教练目不忍视!在边线摇头叹气!",
                lastName + "一脸尴尬!"
            };
            pickStringOutput(resources, true);
        }
    }

    /**
     * Generate comments when player gets ball.
     * 
     * @param teamName Offense team
     * @param name Player name
     * @param defensePlayer Defense player name
     */
    public static void getBallComment(String teamName, String name, String defensePlayer) {
        String lastName = getLastName(name);
        String defenseLastName = getLastName(defensePlayer);

        String[] resources1 = {
            teamName + "进攻!",
            teamName + "球权!",
            teamName + "发起进攻!",
            teamName + "的进攻回合!"
        };
        String[] resources2 = {
            "球给到" + lastName + "!",
            "球传到" + lastName + "手上!",
            "队友转移球到" + lastName + "手中!",
            "队友分球给" + lastName + "!",
            "连续几次传球后球到了" + lastName + "手上!",
            lastName + "跑出来接球!\n一边运球一边呼叫队友跑位!",
            lastName + "弧顶接球!",
            lastName + "持球进攻!",
            lastName + "拿球!看他怎么处理!",
            lastName + "持球!\n分球底线!队友没机会又给回!",
            "队友跑位拉开空间让" + lastName + "进攻!",
            "队友击地传球找到" + lastName + "!",
            lastName + "持球推进!",
            lastName + "持球!示意队友拉开空间!",
            lastName + "拿球组织进攻!",
            lastName + "拿球!和队友挡拆配合一下自己准备攻了!",
            lastName + "伸手要球!进攻欲望强烈!",
            lastName + "疯狂暗示队友给球!队友心领神会马上传给他!",
            "几次大范围倒球!球到了" + lastName + "手上!"
        };
        String[] resources3 = {
            "对上" + defenseLastName + "!",
            "面对" + defenseLastName + "的防守!",
            "对位" + defenseLastName + "!",
            "防守的是" + defenseLastName + "!",
            defenseLastName + "换上来补防!",
            defenseLastName + "贴上来防守!",
            defenseLastName + "扎好马步顶防!"
        };
        
        sb.delete( 0, sb.length() );
        sb.append("\n").append(pickStringOutput(resources1, false))
          .append("\n").append(pickStringOutput(resources2, false))
          .append("\n").append(pickStringOutput(resources3, false));
        System.out.println(sb.toString());
    }

    /**
     * Generate player turnover comments.
     * 
     * @param name Player's name
     */
    public static void getTurnoverComment(String name) {
        String lastName = getLastName(name);
        String[] resources = {
            lastName + "大力传球给前场队友推反击!\n给大了!球出界!",
            lastName + "没接住球!球飞出界!",
            lastName + "没拿稳球!摸了一下出界了!",
            lastName + "运球踩到边线!裁判立即吹哨!",
            lastName + "运球砸到脚上!\n球直接滚出界外!",
            lastName + "突然黄油手!把球滑出界外!",
            lastName + "脚下一滑直接摔倒!\n皮球飞出界外!",
            lastName + "小碎步有点多!\n裁判抓住吹罚走步!",
            lastName + "脑子一热!停球后又继续运了!\n裁判火眼金睛及时吹哨!",
            lastName + "运球过半场惨遭堵截!不慎运球回场!",
            lastName + "脚下拌蒜!运球运到一半直接滑倒了!"
        };

        pickStringOutput(resources, true);
        getUpsetComment(name, Constants.UPSET_HIGH_PERCENT);
    }

    /**
     * Generate comments after a non fast-break turnover.
     * 
     * @param team Team name
     */
    public static void getNonFastBreak(String team) {
        String[] resources = {
            team + "稳住球权!",
            team + "组织进攻慢慢过半场!",
            team + "带球推进前场!",
            team + "不着急进攻!缓慢带球过半场并观察对方跑位!"
        };
        pickStringOutput(resources, true);
    }

    /**
     * Generate comments after a non fast-break turnover.
     * 
     * @param offensePlayer Offense player name
     * @param defensePlayer Defense player name
     */
    public static void getStealComment(String offensePlayer, String defensePlayer) {
        String offenseLastName = getLastName(offensePlayer);
        String defenseLastName = getLastName(defensePlayer);
        String[] resources = {
            defenseLastName + "死亡缠绕直接掏球!",
            defenseLastName + "半空杀出!直接横断!",
            defenseLastName + "真是摸金校尉!趁人不备暗中盗球!",
            defenseLastName + "眼疾手快!小手一摸皮球到手!",
            defenseLastName + "鬼魅般钻出捅走球!",
            offenseLastName + "手滑了!\n" + defenseLastName + "趁机捞走皮球!",
            offenseLastName + "走神了!\n" + defenseLastName + "斜刺里杀出直接断球!"
        };
        pickStringOutput(resources, true);
    }

    /**
     * Generate comments after a block.
     * 
     * @param defensePlayer Defense player name
     */
    public static void getBlockComment(String defensePlayer) {
        String defenseLastName = getLastName(defensePlayer);
        String[] resources = {
            defenseLastName + "送出少儿不宜，魔龙降世无情火锅!",
            defenseLastName + "用力跳起!手掌干扰到了皮球!",
            defenseLastName + "排球大帽直接扇飞!",
            defenseLastName + "拍马赶到!摧心掌直接打飞!",
            defenseLastName + "耍起降龙十八掌!直接将球扇飞!",
            defenseLastName + "追魂夺命!钉板大帽!",
            defenseLastName + "直接送上美味大火锅!",
            defenseLastName + "大帽拒绝!\n果然是进攻高一尺!火锅高一丈!",
            defenseLastName + "起飞!直接送出遮天蔽日无情大血帽!",
            defenseLastName + "如影随形!直接钉板大帽!"
        };
        pickStringOutput(resources, true);
    }

    /**
     * Generate comments when player makes a free throw.
     * 
     * @param count The number of ongoing free throw
     * @param onlyOneShot Whether only one free throw in total
     */
    public static void getMakeFreeThrowComment(int count, boolean onlyOneShot) {
        String countPrefix = onlyOneShot ? "罚球" : count + "罚";
        String[] resources = {
            "命中!",
            "得手!",
            "稳稳命中!",
            "轻松命中!",
            "有了!",
            "颠了几下还是落入网中!"
        };
        sb.delete( 0, sb.length() );
        sb.append(countPrefix).append(pickStringOutput(resources, false));
        System.out.println(sb.toString());
    }

    /**
     * Generate comments when player misses a free throw.
     * 
     * @param count The number of ongoing free throw
     * @param onlyOneShot Whether only one free throw in total
     */
    public static void getMissFreeThrowComment(int count, boolean onlyOneShot) {
        String countPrefix = onlyOneShot ? "罚球" : count + "罚";
        String[] resources = {
            "不中!",
            "弹框不中!",
            "打铁!",
            "短了!",
            "铁了!",
            "丢了!",
            "颠了几下滚了出来!",
            "涮框不中!",
            "砸前框不中!",
            "刷框而出!"
        };
        sb.delete( 0, sb.length() );
        sb.append(countPrefix).append(pickStringOutput(resources, false));
        System.out.println(sb.toString());
    }

    /**
     * Generate comments for starters in garbage time.
     * 
     * @param team The team to be commented
     */
    public static void getStartersComment(Team team) {
        int randomIndex = Utilities.generateRandomNum(random, 0, team.starters.size() - 1);
        Player randomPlayer = (Player)team.starters.values().toArray()[randomIndex];
        String playerName = getLastName(randomPlayer.name);
        String[] resources = {
            "镜头给到场边休息的" + playerName + "!\n尽管已经垃圾时间了但他仍然专心致志地在观看比赛!",
            "镜头给到场边休息的" + playerName + "!\n他正和身旁的队友大声交流着什么!",
            "镜头给到场边休息的" + playerName + "!\n此时已经换上训练外套坐在场边冰敷缓解疲劳!",
            "镜头给到场边休息的" + playerName + "!\n正瘫坐在椅子上!看来今天是累坏了!",
            "镜头给到场边休息的" + playerName + "!\n他正在面无表情一边看球一边啃手指!",
            "镜头给到场边休息的" + playerName + "!\n正裹着厚厚的毛巾止汗!"
        };
        pickStringOutput(resources, true);
    }

    /**
     * Generate comments when player makes And-one shot.
     * 
     * @param name Player name
     */
    public static void getAndOneComment(String name) {
        String[] resources = {
            "球进同时哨响!\n还要加罚!",
            "同时裁判吹哨!\n给了加罚!",
            "AAAAAnd One!",
            "球进的同时!AAAAAnd One!",
            "球进!还要加罚!"
        };
        pickStringOutput(resources, true);
        getCelebrateComment(name, Constants.CELEBRATE_HIGH_PERCENT);
    }

    /**
     * Generate comments when one team reaches quarter foul times bonus.
     * 
     * @param offenseTeam Offense team name
     * @param defenseTeam Defense team name
     */
    public static void getReachFoulTimes(String offenseTeam, String defenseTeam) {
        String[] resources = {
            defenseTeam + "犯规次数到了!\n" + offenseTeam + "需要进行两次罚球!",
            defenseTeam + "这节已经五次犯规了!\n" + offenseTeam + "有两次罚球机会!"
        };
        pickStringOutput(resources, true);
    }

    /**
     * Generate comments when the player draws a foul.
     * 
     * @param offensePlayer Offense player name
     * @param defensePlayer Defense player name
     */
    public static void getFoulComment(String offensePlayer, String defensePlayer) {
        String offenseLastName = getLastName(offensePlayer);
        String defenseLastName = getLastName(defensePlayer);

        String[] resources = {
            "哨响!\n" + defenseLastName + "打手犯规!",
            defenseLastName + "冲下去一把拽下来!\n裁判果断吹哨!",
            defenseLastName + "直接一把抱住不让出手!\n" + offenseLastName + "都乐了!",
            defenseLastName + "出手压住!\n哨响!打手犯规!",
            defenseLastName + "遮天蔽日按下来!\n哨响!裁判给了犯规!",
            defenseLastName + "被晃开了!有点不甘再冲上去伸手阻拦!\n哨响!裁判给了打手犯规!",
            "哨响!\n" + defenseLastName + "抢在出手前提前犯规了!"
        };
        
        pickStringOutput(resources, true);
    }

    /**
     * Generate comments when the player draws a flagrant foul.
     * 
     * @param offensePlayer Offense player name
     * @param defensePlayer Defense player name
     */
    public static void getFlagFoulComment(String offensePlayer, String defensePlayer) {
        String offenseLastName = getLastName(offensePlayer);
        String defenseLastName = getLastName(defensePlayer);
        String[] resources = {
            defenseLastName + "一把直接拉了下来!\n动作太大了!裁判直接给了恶意犯规!",
            defenseLastName + "直接顶上去把" + offenseLastName + "撞倒在地!\n双方险些酿成冲突!\n裁判给了" + defenseLastName + "恶意犯规!",
            defenseLastName + "估计之前的回合中和" + offenseLastName + "有一些不愉快!\n这一回直接上去报复性推搡了一把!\n裁判及时吹停并给了恶意犯规!"
        };

        pickStringOutput(resources, true);
    }

    /**
     * Generate comments when the player prepares to go to the free throw line.
     * 
     * @param player Free throw player name
     */
    public static void getFreeThrowPrepareComment(String player) {
        String playerLastName = getLastName(player);
        String[] resources = {
            playerLastName + "罚球!",
            playerLastName + "走上罚球线!",
            "考验" + playerLastName + "的罚球了!"
        };

        pickStringOutput(resources, true);
    }

    /**
     * Generate foul challenge comments.
     * 
     * @param teamName Challenge team name
     * @return Whether the challenge succeed
     */
    public static boolean getChallengeComment(String teamName) {
        String[] resources1 = {
            teamName + "对于这个判罚不太认可!向裁判组提出挑战!",
            teamName + "教练走向裁判!要求挑战这个判罚!",
            teamName + "十分不满!提出挑战要求裁判重新吹罚!",
        };

        pickStringOutput(resources1, true);

        // challenge successful
        if (Utilities.generateRandomNum(random) <= Constants.CHALLENGE_SUCCESS) {
            String[] resources2 = {
                "裁判反复观看录像!确认刚才的吹罚是个误判!\n挑战成功!球权归还" + teamName + "!",
                "裁判聚在一起激烈讨论!\n最终达成一致!" + teamName + "挑战成功并且拿回球权!"
            };
            pickStringOutput(resources2, true);
            return true;
        } else {
            String[] resources2 = {
                "裁判反复观看录像!认为刚才吹罚没有问题!\n挑战失败!" + teamName + "还是被吹犯规!",
                "裁判聚在一起激烈讨论!\n最终达成一致!" + teamName + "挑战失败!"
            };
            pickStringOutput(resources2, true);
            return false;
        }
    }

    /**
     * Generate comments when the player makes a shot.
     * 
     * @param offenseName Offense player name
     * @param defenseName Defense player name
     * @param distance Player's shot distance
     * @param movement Player's shot choice
     */
    public static void getMakeShotsComment(String offenseName, String defenseName, int distance, String movement) {
        String[] resources1;

        // dunk or normal 2-point shot
        if (movement.contains("扣")) {
            String[] temp = {
                "稳稳得手!",
                "力拔山兮气盖世!",
                "手起刀落!",
                "BOOM!Shakala!",
                "分数到账!",
                "强硬命中!",
                "篮筐在颤抖!",
                "力劈华山!",
                "开天辟地!",
                "DUNK!!!",
                "没中!然而裁判响哨!\n慢镜头回放显示" + getLastName(defenseName) + "干扰球了!这球有效!"
            };
            resources1 = temp;
        } else {
            String[] temp = {
                "进了!",
                "进进进!",
                "就是能进!服不服!",
                "打进!",
                "得手!",
                "稳稳命中!",
                "稳稳得手!",
                "分数到账!",
                "Bingo!有了!",
                "So easy!",
                "中中中!",
                "咔嚓命中!",
                "飘逸命中!",
                "皮球轻巧入网!",
                "空心命中!",
                "空心入袋!",
                "打板命中!",
                "高打板命中!",
                "斜擦板命中!",
                "Nothing but the net!",
                "没中!然而裁判响哨!\n慢镜头回放显示" + getLastName(defenseName) + "干扰球了!这球有效!"
            };
            resources1 = temp;
        }

        // 3 points
        String[] resources2 = {
            "进了!",
            "进进进!",
            "就是能进!服不服!",
            "打进!",
            "得手!",
            "稳稳命中!",
            "稳稳得手!",
            "分数到账!",
            "Bingo!有了!",
            "So easy!",
            "中中中!",
            "咔嚓命中!",
            "飘逸命中!",
            "皮球轻巧入网!",
            "空心命中!",
            "空心入袋!",
            "打板命中!",
            "高打板命中!",
            "斜擦板命中!",
            "Nothing but the net!",
            "没中!然而裁判响哨!\n慢镜头回放显示" + getLastName(defenseName) + "干扰球了!这球有效!",

            "一针见血!毫厘不差!",
            "箭如雨下!弹无虚发!",
            "点火!起飞!BOOM!",
            "BOOM!Shakala!",
            "二营长的意大利炮!正中目标!",
            "我的三分剑!是地狱的火焰!",
            "一支三分剑!千军万马来相见!",
            "这城市那么空!这三分那么痛!",
            "醉饮世间穿肠药!梦怀三分枯骨刀!",
            "让三分飞一会儿再中!",
            "撤步拉弯弓!箭去破苍穹!",
            "问君能有几多愁!不如来颗三分球!",
            "山雨欲来风满楼!三分射到你发愁!",
            "龙驹跳踏起天风!画戟荧煌射秋水!",
            "疯魔三分太乙仙!人在江湖月在天!",
            "长虹贯日!直坠网心!",
            "三分巡航导航精确制导命中!",
            "一发入魂!洞穿篮网!",
            "手起刀落!一剑封喉!",
            "百步穿杨!",
            "三分来得太快就像龙卷风!",
            "Bang! Bang! Bang!"
        };

        pickStringOutput(distance < Constants.MIN_THREE_SHOT ? resources1 : resources2, true);
        getCelebrateComment(offenseName, Constants.CELEBRATE_LOW_PERCENT);
    }

    /**
     * Generate comments when the player misses a shot.
     * 
     * @param movement Player's shot choice
     * @param offenseName Player's name
     */
    public static void getMissShotsComment(String movement, String offenseName) {
        String offenseLastName = getLastName(offenseName);
        String[] resources;

        if (movement.contains("扣")) {
            String[] temp = {
                "扣飞了。。。",
                "力道太大!球弹出来了!",
                "用力过猛!没扣进!",
                "起跳高度不够有点勉强!球砸框而出!",
                "居然没扣进!这是要五大囧了!"
            };
            resources = temp;
        } else {
            String[] temp = {
                "不中!",
                "打铁!",
                "铁了!",
                "没沾框!",
                "短了!",
                "弹框不中!",
                "球涮框而出!",
                "涮框不中!",
                "球打到篮框上高高弹起!",
                "铁铁铁!",
                "三不沾。。。",
                "对面防的很死!直接投了个三不沾!",
                "Duang!",
                "歪了!",
                "球在篮筐上颠了几下滚了出来!",
                "空气球!",
                "力道太大!球差点直接弹出界!",
                "刷框而出!In-AND-OUT!",
                "磕框而出!"
            };
            resources = temp;
        }

        pickStringOutput(resources, true);
        getUpsetComment(offenseLastName, Constants.UPSET_LOW_PERCENT);
    }

    /**
     * Generate comments when the player plays well / plays bad.
     * 
     * @param player Player object
     * @param isGoodstatus Whether the player is in good status or bad status
     */
    public static void getStatusComment(Player player, boolean isGoodstatus) {
        if (isGoodstatus &&
            (player.score >= Constants.MIN_GOOD_SCORE ||
            (player.shotMade >= Constants.MIN_SHOT_MADE && player.shotMade * 1.0 / player.shotAttempted >= Constants.MIN_GOOD_SHOT_PERCENT))
            || !isGoodstatus &&
            (player.shotAttempted >= Constants.MIN_SHOT_ATTEMPTED && player.shotMade * 1.0 / player.shotAttempted <= Constants.MAX_BAD_SHOT_PERCENT)) {
            
            String lastName = getLastName(player.name);
            String[] resources;

            if (isGoodstatus) {
                String[] temp = {
                    lastName + "今晚状态很好啊!",
                    lastName + "今晚状态不错!",
                    lastName + "手感火热!",
                    lastName + "这场比赛真是不可阻挡啊!",
                    lastName + "目前手感上佳!",
                    lastName + "今晚命中率很高!",
                    lastName + "目前效率很高!"
                };
                resources = temp;
            } else {
                String[] temp = {
                    lastName + "今晚怎么了!不在状态!",
                    lastName + "手感冰凉!",
                    lastName + "今晚打了多少个铁了。。。",
                    lastName + "今晚真是四仰化三铁。。。",
                    lastName + "今晚真是化身米兰的小铁匠。。。",
                    lastName + "手感不好啊!",
                    lastName + "命中率惨不忍睹!",
                    lastName + "今晚打铁有点多啊!"
                };
                resources = temp;
            }

            String suffix = "目前" + player.shotAttempted + "投" + player.shotMade + "中拿到" + player.score + "分!";
            pickStringOutput(resources, true);
            System.out.println(suffix);
        }
    }

    /**
     * Generate comments when the player grabs a rebound.
     * 
     * @param name Player name
     * @param isOrb Whether the current rebound is offensive rebound or defensive rebound
     */
    public static void getReboundComment(String name, boolean isOrb) {
        String lastName = getLastName(name);
        String rebType = isOrb ? "前场篮板" : "后场篮板";

        String[] resources = {
            lastName + "实力捡下" + rebType + "!",
            lastName + "收下" + rebType + "!",
            lastName + "冲上来保下" + rebType + "!",
            lastName + "抓下" + rebType + "!",
            lastName + "保护住" + rebType + "!",
            lastName + "人群中拨到" + rebType + "!",
            lastName + "的" + rebType + "!",
            lastName + "跳起拿到" + rebType + "!",
            lastName + "高高跃起保护" + rebType + "!",
            lastName + "舍我其谁!人群中拼下" + rebType + "!"
        };
        pickStringOutput(resources, true);
    }

    /**
     * Generate comments when the defense player blocks the ball out-of-bound.
     * 
     * @param defensePlayer Defense player name
     */
    public static void getOutOfBound(String defenseName) {
        String[] resources = {
            "皮球直接滚出界外!",
            "皮球直接飞进观众席的人群中!",
            "球飞出界!",
            "界外球!"
        };
        pickStringOutput(resources, true);
        getCelebrateComment(defenseName, Constants.CELEBRATE_HIGH_PERCENT);
    }

    /**
     * Generate comments when the offense player misses the shot and the ball is out-of-bound.
     * 
     * @param offensePlayer Offense player name
     */
    public static void shotOutOfBound(String offensePlayer) {
        String[] resources = {
            "皮球直接滚出界外!球权转换!",
            "皮球直接弹出界外!这球有点离谱!",
            "球飞出界!球权转换!"
        };
        pickStringOutput(resources, true);
        getUpsetComment(offensePlayer, Constants.UPSET_LOW_PERCENT);
    }

    /**
     * Generate comments when the player get injured.
     * 
     * @param name Player name
     */
    public static void getInjuryComment(String name) {
        String lastName = getLastName(name);
        String[] resources = {
            "镜头突然给到" + lastName + "!\n他痛苦的趴在地上!\n应该是在刚才的对抗中受伤了!",
            "镜头突然给到" + lastName + "!\n一瘸一拐地走下场!应该是受伤了!",
            "这边裁判突然吹停比赛!\n" + lastName + "面部在刚才的回合中被撞出血了!下场接受治疗!",
            lastName + "突破进入人堆!突然重重倒地!\n看回放慢镜头显示他似乎是崴脚了!\n队友围上来把他抬出场外!",
            "裁判突然吹停比赛!\n" + lastName + "被撞翻在地!痛苦地捂着膝盖!",
            "裁判突然上前中止比赛!镜头同时给到了" + lastName + "!\n他在刚才的回合中倒地了!一直握着自己的大腿!\n队友上来把他搀扶下场!",
            "这时镜头突然给到了" + lastName + "!\n他在刚才的回合中重重摔在地上!半天倒地不起!\n估计是脑震荡了!\n队友上来把他抬下场!"
        };
        pickStringOutput(resources, true);
    }

    /**
     * Generate comments when the player makes fast-break after a turnover.
     * 
     * @param teamName Team name
     * @param offensePlayer Offense player name
     */
    public static void getFastBreak(String teamName, String offensePlayer) {
        String offenseLastName = getLastName(offensePlayer);
        String[] resources = {
            teamName + "发动闪电反击!\n" + offenseLastName + "持球直捣黄龙轻取两分!",
            teamName + "趁机发动防守反击!\n" + offenseLastName + "接球一条龙突破暴扣收下两分!",
            teamName + "防守反击的机会!\n" + offenseLastName + "早已冲到篮下轻取两分!",
            teamName + "闪电推进前场!\n" + offenseLastName + "轻松笑纳两分!",
            teamName + "推动反击!\n" + offenseLastName + "下快攻轻松上篮得手!",
            offenseLastName + "骑着哈雷冲前场!1打0!\n轻松上篮命中!",
            offenseLastName + "千里奔袭至对方篮下!轻取两分!"
        };
        pickStringOutput(resources, true);
        getCelebrateComment(offensePlayer, Constants.CELEBRATE_HIGH_PERCENT);
    }

    /**
     * Generate comments when the player makes fast-break after a turnover.
     * 
     * @param offensePlayer Offense player name
     * @param type Offensive foul type (1 - Charging foul, 2 - Illegal screen foul)
     */
    public static void getOffensiveFoul(String offensePlayer, int type) {
        String offenseLastName = getLastName(offensePlayer);
        String[] resources1 = {
            "哨响!\n裁判吹了" + offenseLastName + "进攻犯规!",
            offenseLastName + "如蛮牛一般拱进人堆!\n哨响!裁判吹罚" + offenseLastName + "带球撞人!",
            offenseLastName + "进攻挥肘了!\n裁判及时吹哨制止!",
            offenseLastName + "杀到篮下!掀翻了防守!\n哨响!裁判给了一个进攻犯规!",
            offenseLastName + "突破时沉肩开路!\n动作有点大!裁判马上吹哨给了进攻犯规!"
        };
        String[] resources2 = {
            "哨响!\n裁判吹了" + offenseLastName + "掩护犯规!",
            offenseLastName + "上来挡拆!脚步不太稳连动了好几步!\n裁判吹哨吹罚移动挡拆犯规!",
            "哨响!\n" + offenseLastName + "被吹了掩护时有拉拽嫌疑!"
        };

        if (type == 1) pickStringOutput(resources1, true);
        else if (type == 2) pickStringOutput(resources2, true);
        getUpsetComment(offensePlayer, Constants.UPSET_HIGH_PERCENT);
    }

    /**
     * Generate comments when the player makes fast-break after a turnover.
     * 
     * @param defensePlayer Defense player name
     * @param type Defensive foul type (1 - Blocking foul, 2 - Reach in foul)
     */
    public static void getDefensiveFoul(String defensePlayer, int type) {
        String defenseLastName = getLastName(defensePlayer);
        String[] resources1 = {
            defenseLastName + "紧紧贴防!\n手上小动作不太干净!\n哨响!裁判吹罚" + defenseLastName + "防守犯规!",
            defenseLastName + "靠上去阻拦!被撞倒了!\n哨响!裁判给的是阻挡犯规!"
        };
        String[] resources2 = {
            "裁判突然哨响!\n吹的是" + defenseLastName + "无球防守的时候似乎手上动作不太干净!",
            defenseLastName + "上来协防!干扰的动作有点大!\n裁判立即吹哨给了犯规!",
            defenseLastName + "逼上来想要掏球!结果不幸掏到手臂了!\n哨响!裁判给了防守犯规!"
        };

        if (type == 1) pickStringOutput(resources1, true);
        else if (type == 2) pickStringOutput(resources2, true);
        getUpsetComment(defensePlayer, Constants.UPSET_HIGH_PERCENT);
    }

    /**
     * Generate comments when a team calls timeout.
     * 
     * @param teamName Team name
     */
    public static void getTimeOutComment(String teamName) {
        sb.delete( 0, sb.length() );
        sb.append("\n").append(teamName).append("请求暂停!\n双方都有人员调整!");
        System.out.println(sb.toString());
    }

    /**
     * Generate comments when a player gets fouled out.
     * 
     * @param name Player name
     * @param isNormalFoul Player gets fouled by normal foul or flagrant foul
     */
    public static void getFoulOutComment(String name, boolean isNormalFoul) {
        String lastName = getLastName(name);
        String[] resources;

        if (isNormalFoul) {
            String[] temp = {
                lastName + "可能要六犯离场!正在等待裁判进一步确认!",
                lastName + "犯规次数到了!应该是要直接被罚出场外!"
            };
            resources = temp;
        } else {
            String[] temp = {
                lastName + "恶意犯规次数满了!可能要被罚出场外!\n看一下裁判的意见!",
                lastName + "又一次恶意犯规!应该是要直接被罚出场外!\n裁判还在和技术台确认!"
            };
            resources = temp;
        }

        sb.delete( 0, sb.length() );
        sb.append(pickStringOutput(resources, false));
        sb.append("\n这边技术台回放确认了!").append(lastName).append("确认被罚出场!\n现场镜头给到他!此刻正一脸郁闷地往球员通道走去!");
        System.out.println(sb.toString());
    }

    /**
     * Generate comments when a player gets substituted to prevent too much fouls.
     * 
     * @param name Player's name
     */
    public static void getFoulProtectComment(String name) {
        String lastName = getLastName(name);
        String[] resources = {
            lastName + "犯规次数有点多!教练决定提前换他下场休息!",
            lastName + "运气不佳!又一次犯规了!受到犯规困扰不得不提前先下场休息一下!",
            lastName + "饱受犯规困扰!教练无奈将他换下场休息!"
        };
        pickStringOutput(resources, true);
    }

    /**
     * Generate comments when a player gets substituted.
     * 
     * @param currentPlayer In player name
     * @param previousPlayer Out player name
     */
    public static void getSubstituteComment(String currentPlayer, String previousPlayer) {
        if (!currentPlayer.equals(previousPlayer)) {
            sb.delete( 0, sb.length() );
            sb.append(currentPlayer).append("换下").append(previousPlayer).append("!");
            System.out.println(sb.toString());
        }
    }

    /**
     * Print current time and score.
     * 
     * @param time Current quarter time left
     * @param currentQuarter Current quarter number
     */
    public static void getTimeAndScore(int time, int currentQuarter, Team team1, Team team2) {
        String minute = String.valueOf(time / 60);
        String second = String.valueOf(time % 60);
        if (time % 60 < 10) second = "0" + second;

        sb.delete( 0, sb.length() );
        if (currentQuarter <= 4) sb.append("第").append(currentQuarter).append("节 ");
        else sb.append("加时第").append(currentQuarter - 4).append("节 ");
        sb.append(minute).append(":").append(second).append("秒  ")
          .append(team1.name).append(" ").append(team1.totalScore).append(":").append(team2.totalScore).append(" ").append(team2.name);

        System.out.println(sb.toString()); 
    }

    /**
     * Generate comments when a quarter ends.
     * 
     * @param currentQuarter Current quarter number
     */
    public static void quarterEnd(int currentQuarter, Team team1, Team team2) {
        sb.delete( 0, sb.length() );
        sb.append("\n第").append(currentQuarter).append("节结束!\n目前的比分是: ")
        .append(team1.name).append(" ").append(team1.totalScore).append(":").append(team2.totalScore).append(" ").append(team2.name).append("\n");

        sb.append("\n==============================================================================\n");

        sb.append("\n第").append(currentQuarter + 1).append("节比赛开始!");

        System.out.println(sb.toString()); 
    }

    /**
     * Generate comments when regular time ends.
     */
    public static void regularEnd(Team team1, Team team2) {
        sb.delete( 0, sb.length() );
        sb.append("\n常规时间走完!\n目前双方战成").append(team1.totalScore).append("平!\n");
        sb.append("\n==============================================================================\n");
        sb.append("加时赛开始!");

        System.out.println(sb.toString()); 
    }

    /**
     * Generate comments when the game ends.
     * 
     * @param team1Scores team1's scores of all quarters
     * @param team2Scores team2's scores of all quarters
     */
    public static void gameEnd(Team team1, Team team2, List<Integer> team1Scores, List<Integer> team2Scores) {
        sb.delete( 0, sb.length() );

        sb.append("\n==============================================================================\n");
        
        sb.append("\n全场比赛结束!\n最终比分是: ")
          .append(team1.name).append(" ").append(team1.totalScore).append(":").append(team2.totalScore).append(" ").append(team2.name).append("\n");

        String winTeam = team1.totalScore >= team2.totalScore ? team1.name : team2.name;
        String loseTeam = winTeam.equals(team1.name) ? team2.name : team1.name;
        sb.append("恭喜").append(winTeam).append("以").append(Math.max(team1.totalScore, team2.totalScore) - Math.min(team1.totalScore, team2.totalScore))
          .append("分的优势战胜").append(loseTeam).append("!\n");

        sb.append("\n每节比分详情:\n")
          .append(team1.name).append("\n").append(team1Scores.get(0)).append("\t");
        for (int i = 1; i < team1Scores.size(); i++) sb.append(team1Scores.get(i) - team1Scores.get(i - 1)).append("\t");
        sb.append("\n");
        sb.append(team2.name).append("\n").append(team2Scores.get(0)).append("\t");
        for (int i = 1; i < team2Scores.size(); i++) sb.append(team2Scores.get(i) - team2Scores.get(i - 1)).append("\t");
        sb.append("\n");

        System.out.print(sb.toString());

        getTeamData(team1);
        getTeamData(team2);
    }

    /**
     * Print a player's stat.
     * 
     * @param player Player object
     */
    public static void getPlayerData(Player player) {
        sb.delete( 0, sb.length() );

        if (player.hasBeenOnCourt) {
            sb.append(player.name).append(": ").append(player.score).append("分，").append(player.rebound).append("篮板，")
          .append(player.assist).append("助攻，").append(player.steal).append("抢断，").append(player.block).append("盖帽，")
          .append(player.turnover).append("失误，").append(player.foul).append("犯规 ")
          .append("投篮").append(player.shotMade).append("-").append(player.shotAttempted)
          .append("，三分").append(player.threeMade).append("-").append(player.threeAttempted)
          .append("，罚球").append(player.freeThrowMade).append("-").append(player.freeThrowAttempted);
        } else {
            sb.append(player.name).append(": ").append("************************** 球员未上场 **************************");
        }
        
        System.out.println(sb.toString());
    }

    /**
     * Print a team's stat.
     * 
     * @param team Team name
     */
    public static void getTeamData(Team team) {
        sb.delete( 0, sb.length() );

        // display each player's data and update total statistics
        sb.append("\n").append(team.name).append("球员数据统计:");
        System.out.println(sb.toString());

        for (int i = 0; i < team.players.size(); i++) {
            Player currentPlayer = team.players.get(i);
            getPlayerData(currentPlayer);
            team.totalRebound += currentPlayer.rebound;
            team.totalAssist += currentPlayer.assist;
            team.totalSteal += currentPlayer.steal;
            team.totalBlock += currentPlayer.block;
            team.totalFoul += currentPlayer.foul;
            team.totalTurnover += currentPlayer.turnover;
            team.totalShotAttempted += currentPlayer.shotAttempted;
            team.totalShotMade += currentPlayer.shotMade;
            team.total3Attempted += currentPlayer.threeAttempted;
            team.total3Made += currentPlayer.threeMade;
            team.totalFreeAttempted += currentPlayer.freeThrowAttempted;
            team.totalFreeMade += currentPlayer.freeThrowMade;
        }

        sb.delete( 0, sb.length() );
        sb.append("\n").append(team.name).append("全队数据统计:\n");
        sb.append(team.totalScore).append("分，").append(team.totalRebound).append("篮板，").append(team.totalAssist).append("助攻，")
          .append(team.totalSteal).append("抢断，").append(team.totalBlock).append("盖帽，").append(team.totalTurnover).append("失误，")
          .append(team.totalFoul).append("犯规\n");

        double totalShotPercentage = team.totalShotAttempted != 0 ? team.totalShotMade * 100.0 / team.totalShotAttempted : 0.0;
        double total3Percentage = team.total3Attempted != 0 ? team.total3Made * 100.0 / team.total3Attempted : 0.0;
        double totalFreePercentage = team.totalFreeAttempted != 0 ? team.totalFreeMade * 100.0 / team.totalFreeAttempted : 0.0;

        sb.append("投篮: ").append(team.totalShotMade).append("-").append(team.totalShotAttempted)
          .append("(").append(String.format("%.2f", totalShotPercentage)).append("%)")
          .append("  三分: ").append(team.total3Made).append("-").append(team.total3Attempted)
          .append("(").append(String.format("%.2f", total3Percentage)).append("%)")
          .append("  罚球: ").append(team.totalFreeMade).append("-").append(team.totalFreeAttempted)
          .append("(").append(String.format("%.2f", totalFreePercentage)).append("%)");

        System.out.println(sb.toString());
    }
}
