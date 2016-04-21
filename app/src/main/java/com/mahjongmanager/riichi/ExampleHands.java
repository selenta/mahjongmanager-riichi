package com.mahjongmanager.riichi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Copying the example hands at: https://en.wikipedia.org/wiki/Japanese_Mahjong_yaku
public class ExampleHands {
    public static List<Hand> allHands(){
        List<Hand> list = new ArrayList<>();
        list.add(getPinfuHand());
        list.add(getIipeikouHand());
        list.add(getSanshokuDoujunHand());
        list.add(getIttsuuHand());
        list.add(getRyanpeikouHand());
        list.add(getToitoiHand());
        list.add(getSanAnkouHand());
        list.add(getSanshokuDoukouHand());
        list.add(getSanKantsuHand());
        list.add(getTanyaoHand());
        list.add(getYakuhaiHand());
        list.add(getChantaHand());
        list.add(getJunchanHand());
        list.add(getHonroutouHand());
        list.add(getHonroutouChiitoitsuHand());
        list.add(getShousangenHand());
        list.add(getHonitsuHand());
        list.add(getChinitsuHand());
        return list;
    }
    public static List<Hand> allYakumanHands(){
        List<Hand> list = new ArrayList<>();
        list.add(getKokushiMusouHand());
        list.add(getKokushiMusou13SidedHand());
        list.add(getSuuAnkouHand());
        list.add(getSuuAnkouTankiHand());
        list.add(getDaisangenHand());
        list.add(getShousuushiiHand());
        list.add(getDaishuushiiHand());
        list.add(getTsuuiisouHand());
        list.add(getDaichiseiHand());
        list.add(getRyuuiisouHand());
        list.add(getChuurenPoutouHand());
        list.add(getChuurenPoutou9SidedHand());
        list.add(getSuuKantsuHand());
        return list;
    }

    // Run-based hands
    public static Hand getPinfuHand(){
        Tile t1  = new Tile(1, "MANZU");
        Tile t2  = new Tile(2, "MANZU");
        Tile t3  = new Tile(3, "MANZU");
        Tile t4  = new Tile(5, "MANZU");
        Tile t5  = new Tile(5, "MANZU");
        Tile t6  = new Tile(2, "PINZU");
        Tile t7  = new Tile(3, "PINZU");
        Tile t8  = new Tile(4, "PINZU");
        Tile t9  = new Tile(7, "PINZU");
        Tile t10 = new Tile(8, "PINZU");
        Tile t11 = new Tile(9, "PINZU");
        Tile t12 = new Tile(4, "SOUZU");
        Tile t13 = new Tile(5, "SOUZU");
        Tile t14 = new Tile(6, "SOUZU");

        t6.winningTile = true;
        t6.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }
    public static Hand getIipeikouHand(){
        Tile t1  = new Tile(2, "MANZU");
        Tile t2  = new Tile(3, "MANZU");
        Tile t3  = new Tile(4, "MANZU");
        Tile t4  = new Tile(5, "PINZU");
        Tile t5  = new Tile(5, "PINZU");
        Tile t6  = new Tile(6, "PINZU");
        Tile t7  = new Tile(6, "PINZU");
        Tile t8  = new Tile(7, "PINZU");
        Tile t9  = new Tile(7, "PINZU");
        Tile t10 = new Tile(9, "SOUZU");
        Tile t11 = new Tile(9, "SOUZU");
        Tile t12 = new Tile(9, "SOUZU");
        Tile t13 = new Tile("GREEN", "HONOR");
        Tile t14 = new Tile("GREEN", "HONOR");

        t2.winningTile = true;
        t2.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }
    public static Hand getSanshokuDoujunHand(){
        Tile t1  = new Tile(2, "MANZU");
        Tile t2  = new Tile(3, "MANZU");
        Tile t3  = new Tile(4, "MANZU");
        Tile t4  = new Tile(1, "PINZU");
        Tile t5  = new Tile(1, "PINZU");
        Tile t6  = new Tile(1, "PINZU");
        Tile t7  = new Tile(2, "PINZU");
        Tile t8  = new Tile(3, "PINZU");
        Tile t9  = new Tile(4, "PINZU");
        Tile t10 = new Tile(2, "SOUZU");
        Tile t11 = new Tile(3, "SOUZU");
        Tile t12 = new Tile(4, "SOUZU");
        Tile t13 = new Tile("SOUTH", "HONOR");
        Tile t14 = new Tile("SOUTH", "HONOR");

        t14.winningTile = true;
        t14.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }
    public static Hand getIttsuuHand(){
        Tile t1  = new Tile(9, "MANZU");
        Tile t2  = new Tile(9, "MANZU");
        Tile t3  = new Tile(9, "MANZU");
        Tile t4  = new Tile(1, "SOUZU");
        Tile t5  = new Tile(2, "SOUZU");
        Tile t6  = new Tile(3, "SOUZU");
        Tile t7  = new Tile(4, "SOUZU");
        Tile t8  = new Tile(5, "SOUZU");
        Tile t9  = new Tile(6, "SOUZU");
        Tile t10 = new Tile(7, "SOUZU");
        Tile t11 = new Tile(8, "SOUZU");
        Tile t12 = new Tile(9, "SOUZU");
        Tile t13 = new Tile("WHITE", "HONOR");
        Tile t14 = new Tile("WHITE", "HONOR");

        t4.winningTile = true;
        t4.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }
    public static Hand getRyanpeikouHand(){
        Tile t1  = new Tile(5, "MANZU");
        Tile t2  = new Tile(5, "MANZU");
        Tile t3  = new Tile(7, "PINZU");
        Tile t4  = new Tile(7, "PINZU");
        Tile t5  = new Tile(8, "PINZU");
        Tile t6  = new Tile(8, "PINZU");
        Tile t7  = new Tile(9, "PINZU");
        Tile t8  = new Tile(9, "PINZU");
        Tile t9  = new Tile(7, "SOUZU");
        Tile t10 = new Tile(7, "SOUZU");
        Tile t11 = new Tile(8, "SOUZU");
        Tile t12 = new Tile(8, "SOUZU");
        Tile t13 = new Tile(9, "SOUZU");
        Tile t14 = new Tile(9, "SOUZU");

        t5.winningTile = true;
        t5.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }

    // Triplet/Quad-based hands
    public static Hand getToitoiHand(){
        Tile t1  = new Tile(8, "MANZU");
        Tile t2  = new Tile(8, "MANZU");
        Tile t3  = new Tile(8, "MANZU");
        Tile t4  = new Tile(3, "PINZU");
        Tile t5  = new Tile(3, "PINZU");
        Tile t6  = new Tile(3, "PINZU");
        Tile t7  = new Tile(1, "SOUZU");
        Tile t8  = new Tile(1, "SOUZU");
        Tile t9  = new Tile(1, "SOUZU");
        Tile t10 = new Tile(7, "SOUZU");
        Tile t11 = new Tile(7, "SOUZU");
        Tile t12 = new Tile(7, "SOUZU");
        Tile t13 = new Tile("NORTH", "HONOR");
        Tile t14 = new Tile("NORTH", "HONOR");

        t1.revealedState = t2.revealedState = t3.revealedState = Tile.RevealedState.PON;
        t2.calledFrom = Tile.CalledFrom.CENTER;
        t10.winningTile = true;
        t10.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }
    public static Hand getSanAnkouHand(){
        Tile t1  = new Tile(1, "MANZU");
        Tile t2  = new Tile(1, "MANZU");
        Tile t3  = new Tile(4, "MANZU");
        Tile t4  = new Tile(4, "MANZU");
        Tile t5  = new Tile(4, "MANZU");
        Tile t6  = new Tile(1, "PINZU");
        Tile t7  = new Tile(2, "PINZU");
        Tile t8  = new Tile(3, "PINZU");
        Tile t9  = new Tile(9, "PINZU");
        Tile t10 = new Tile(9, "PINZU");
        Tile t11 = new Tile(9, "PINZU");
        Tile t12 = new Tile(2, "SOUZU");
        Tile t13 = new Tile(2, "SOUZU");
        Tile t14 = new Tile(2, "SOUZU");

        t1.winningTile = true;
        t1.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }
    public static Hand getSanshokuDoukouHand(){
        Tile t1  = new Tile(3, "MANZU");
        Tile t2  = new Tile(3, "MANZU");
        Tile t3  = new Tile(3, "MANZU");
        Tile t4  = new Tile(3, "PINZU");
        Tile t5  = new Tile(3, "PINZU");
        Tile t6  = new Tile(3, "PINZU");
        Tile t7  = new Tile(6, "PINZU");
        Tile t8  = new Tile(7, "PINZU");
        Tile t9  = new Tile(8, "PINZU");
        Tile t10 = new Tile(3, "SOUZU");
        Tile t11 = new Tile(3, "SOUZU");
        Tile t12 = new Tile(3, "SOUZU");
        Tile t13 = new Tile("WEST", "HONOR");
        Tile t14 = new Tile("WEST", "HONOR");

        t3.winningTile = true;
        t3.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }
    public static Hand getSanKantsuHand(){
        Tile t1  = new Tile(9, "MANZU");
        Tile t2  = new Tile(9, "MANZU");
        Tile t3  = new Tile(9, "MANZU");
        Tile t4  = new Tile(9, "MANZU");
        Tile t5  = new Tile(2, "SOUZU");
        Tile t6  = new Tile(3, "SOUZU");
        Tile t7  = new Tile(4, "SOUZU");
        Tile t8  = new Tile(5, "SOUZU");
        Tile t9  = new Tile(5, "SOUZU");
        Tile t10 = new Tile(8, "PINZU");
        Tile t11 = new Tile(8, "PINZU");
        Tile t12 = new Tile(8, "PINZU");
        Tile t13 = new Tile(8, "PINZU");
        Tile t14 = new Tile("NORTH", "HONOR");
        Tile t15 = new Tile("NORTH", "HONOR");
        Tile t16 = new Tile("NORTH", "HONOR");
        Tile t17 = new Tile("NORTH", "HONOR");

        t1.revealedState = t2.revealedState = t3.revealedState = t4.revealedState = Tile.RevealedState.CLOSEDKAN;
        t10.revealedState = t11.revealedState = t12.revealedState = Tile.RevealedState.PON;
        t13.revealedState = Tile.RevealedState.ADDEDKAN;
        t11.calledFrom = Tile.CalledFrom.RIGHT;
        t14.revealedState = t15.revealedState = t16.revealedState = t17.revealedState = Tile.RevealedState.CLOSEDKAN;

        t5.winningTile = true;
        t5.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }

    // Honor/Terminal-based hands
    public static Hand getTanyaoHand(){
        Tile t1  = new Tile(2, "MANZU");
        Tile t2  = new Tile(3, "MANZU");
        Tile t3  = new Tile(4, "MANZU");
        Tile t4  = new Tile(2, "PINZU");
        Tile t5  = new Tile(2, "PINZU");
        Tile t6  = new Tile(2, "PINZU");
        Tile t7  = new Tile(6, "PINZU");
        Tile t8  = new Tile(7, "PINZU");
        Tile t9  = new Tile(8, "PINZU");
        Tile t10 = new Tile(5, "SOUZU");
        Tile t11 = new Tile(5, "SOUZU");
        Tile t12 = new Tile(6, "SOUZU");
        Tile t13 = new Tile(7, "SOUZU");
        Tile t14 = new Tile(8, "SOUZU");

        t10.winningTile = true;
        t10.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }
    public static Hand getYakuhaiHand(){
        Tile t1  = new Tile(5, "MANZU");
        Tile t2  = new Tile(5, "MANZU");
        Tile t3  = new Tile(1, "PINZU");
        Tile t4  = new Tile(2, "PINZU");
        Tile t5  = new Tile(3, "PINZU");
        Tile t6  = new Tile(5, "PINZU");
        Tile t7  = new Tile(6, "PINZU");
        Tile t8  = new Tile(7, "PINZU");
        Tile t9  = new Tile(8, "SOUZU");
        Tile t10 = new Tile(8, "SOUZU");
        Tile t11 = new Tile(8, "SOUZU");
        Tile t12 = new Tile("RED", "HONOR");
        Tile t13 = new Tile("RED", "HONOR");
        Tile t14 = new Tile("RED", "HONOR");

        t14.winningTile = true;
        t14.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }
    public static Hand getChantaHand(){
        Tile t1  = new Tile(7, "MANZU");
        Tile t2  = new Tile(8, "MANZU");
        Tile t3  = new Tile(9, "MANZU");
        Tile t4  = new Tile(1, "PINZU");
        Tile t5  = new Tile(1, "PINZU");
        Tile t6  = new Tile(1, "PINZU");
        Tile t7  = new Tile(7, "PINZU");
        Tile t8  = new Tile(8, "PINZU");
        Tile t9  = new Tile(9, "PINZU");
        Tile t10 = new Tile(9, "SOUZU");
        Tile t11 = new Tile(9, "SOUZU");
        Tile t12 = new Tile(9, "SOUZU");
        Tile t13 = new Tile("WHITE", "HONOR");
        Tile t14 = new Tile("WHITE", "HONOR");

        t9.winningTile = true;
        t9.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }
    public static Hand getJunchanHand(){
        Tile t1  = new Tile(1, "MANZU");
        Tile t2  = new Tile(2, "MANZU");
        Tile t3  = new Tile(3, "MANZU");
        Tile t4  = new Tile(9, "MANZU");
        Tile t5  = new Tile(9, "MANZU");
        Tile t6  = new Tile(9, "MANZU");
        Tile t7  = new Tile(9, "PINZU");
        Tile t8  = new Tile(9, "PINZU");
        Tile t9  = new Tile(1, "SOUZU");
        Tile t10 = new Tile(2, "SOUZU");
        Tile t11 = new Tile(3, "SOUZU");
        Tile t12 = new Tile(7, "SOUZU");
        Tile t13 = new Tile(8, "SOUZU");
        Tile t14 = new Tile(9, "SOUZU");

        t11.winningTile = true;
        t11.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }
    public static Hand getHonroutouHand(){
        Tile t1  = new Tile(1, "MANZU");
        Tile t2  = new Tile(1, "MANZU");
        Tile t3  = new Tile(1, "MANZU");
        Tile t4  = new Tile(9, "PINZU");
        Tile t5  = new Tile(9, "PINZU");
        Tile t6  = new Tile(9, "PINZU");
        Tile t7  = new Tile(1, "SOUZU");
        Tile t8  = new Tile(1, "SOUZU");
        Tile t9  = new Tile(1, "SOUZU");
        Tile t10 = new Tile(9, "SOUZU");
        Tile t11 = new Tile(9, "SOUZU");
        Tile t12 = new Tile(9, "SOUZU");
        Tile t13 = new Tile("GREEN", "HONOR");
        Tile t14 = new Tile("GREEN", "HONOR");

        t4.revealedState = t5.revealedState = t6.revealedState = Tile.RevealedState.PON;
        t5.calledFrom = Tile.CalledFrom.RIGHT;

        t9.winningTile = true;
        t9.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }
    public static Hand getHonroutouChiitoitsuHand(){
        Tile t1  = new Tile(1, "MANZU");
        Tile t2  = new Tile(1, "MANZU");
        Tile t3  = new Tile(1, "PINZU");
        Tile t4  = new Tile(1, "PINZU");
        Tile t5  = new Tile(9, "SOUZU");
        Tile t6  = new Tile(9, "SOUZU");
        Tile t7  = new Tile("EAST", "HONOR");
        Tile t8  = new Tile("EAST", "HONOR");
        Tile t9  = new Tile("SOUTH", "HONOR");
        Tile t10 = new Tile("SOUTH", "HONOR");
        Tile t11 = new Tile("WHITE", "HONOR");
        Tile t12 = new Tile("WHITE", "HONOR");
        Tile t13 = new Tile("NORTH", "HONOR");
        Tile t14 = new Tile("NORTH", "HONOR");

        t8.winningTile = true;
        t8.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }
    public static Hand getShousangenHand(){
        Tile t1  = new Tile(2, "MANZU");
        Tile t2  = new Tile(3, "MANZU");
        Tile t3  = new Tile(4, "MANZU");
        Tile t4  = new Tile(6, "SOUZU");
        Tile t5  = new Tile(7, "SOUZU");
        Tile t6  = new Tile(8, "SOUZU");
        Tile t7  = new Tile("WHITE", "HONOR");
        Tile t8  = new Tile("WHITE", "HONOR");
        Tile t9  = new Tile("WHITE", "HONOR");
        Tile t10 = new Tile("GREEN", "HONOR");
        Tile t11 = new Tile("GREEN", "HONOR");
        Tile t12 = new Tile("GREEN", "HONOR");
        Tile t13 = new Tile("RED", "HONOR");
        Tile t14 = new Tile("RED", "HONOR");

        t9.winningTile = true;
        t9.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }

    // Suit-based
    public static Hand getHonitsuHand(){
        Tile t1  = new Tile(1, "PINZU");
        Tile t2  = new Tile(1, "PINZU");
        Tile t3  = new Tile(1, "PINZU");
        Tile t4  = new Tile(3, "PINZU");
        Tile t5  = new Tile(4, "PINZU");
        Tile t6  = new Tile(5, "PINZU");
        Tile t7  = new Tile(7, "PINZU");
        Tile t8  = new Tile(8, "PINZU");
        Tile t9  = new Tile(9, "PINZU");
        Tile t10 = new Tile("SOUTH", "HONOR");
        Tile t11 = new Tile("SOUTH", "HONOR");
        Tile t12 = new Tile("SOUTH", "HONOR");
        Tile t13 = new Tile("WHITE", "HONOR");
        Tile t14 = new Tile("WHITE", "HONOR");

        t6.winningTile = true;
        t6.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }
    public static Hand getChinitsuHand(){
        Tile t1  = new Tile(1, "SOUZU");
        Tile t2  = new Tile(2, "SOUZU");
        Tile t3  = new Tile(3, "SOUZU");
        Tile t4  = new Tile(4, "SOUZU");
        Tile t5  = new Tile(4, "SOUZU");
        Tile t6  = new Tile(4, "SOUZU");
        Tile t7  = new Tile(5, "SOUZU");
        Tile t8  = new Tile(5, "SOUZU");
        Tile t9  = new Tile(6, "SOUZU");
        Tile t10 = new Tile(6, "SOUZU");
        Tile t11 = new Tile(6, "SOUZU");
        Tile t12 = new Tile(7, "SOUZU");
        Tile t13 = new Tile(8, "SOUZU");
        Tile t14 = new Tile(9, "SOUZU");

        t2.winningTile = true;
        t2.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }

    // Yakuman
    public static Hand getKokushiMusouHand(){
        Tile t1  = new Tile(1, "MANZU");
        Tile t2  = new Tile(9, "MANZU");
        Tile t3  = new Tile(1, "PINZU");
        Tile t4  = new Tile(9, "PINZU");
        Tile t5  = new Tile(1, "SOUZU");
        Tile t6  = new Tile(9, "SOUZU");
        Tile t7  = new Tile("EAST", "HONOR");
        Tile t8  = new Tile("SOUTH", "HONOR");
        Tile t9  = new Tile("WEST", "HONOR");
        Tile t10 = new Tile("NORTH", "HONOR");
        Tile t11 = new Tile("WHITE", "HONOR");
        Tile t12 = new Tile("WHITE", "HONOR");
        Tile t13 = new Tile("GREEN", "HONOR");
        Tile t14 = new Tile("RED", "HONOR");

        t2.winningTile = true;
        t2.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }
    public static Hand getKokushiMusou13SidedHand(){
        Tile t1  = new Tile(1, "MANZU");
        Tile t2  = new Tile(9, "MANZU");
        Tile t3  = new Tile(1, "PINZU");
        Tile t4  = new Tile(9, "PINZU");
        Tile t5  = new Tile(1, "SOUZU");
        Tile t6  = new Tile(9, "SOUZU");
        Tile t7  = new Tile("EAST", "HONOR");
        Tile t8  = new Tile("SOUTH", "HONOR");
        Tile t9  = new Tile("WEST", "HONOR");
        Tile t10 = new Tile("NORTH", "HONOR");
        Tile t11 = new Tile("WHITE", "HONOR");
        Tile t12 = new Tile("WHITE", "HONOR");
        Tile t13 = new Tile("GREEN", "HONOR");
        Tile t14 = new Tile("RED", "HONOR");

        t12.winningTile = true;
        t12.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }
    public static Hand getSuuAnkouHand(){
        Tile t1  = new Tile(4, "MANZU");
        Tile t2  = new Tile(4, "MANZU");
        Tile t3  = new Tile(4, "MANZU");
        Tile t4  = new Tile(8, "MANZU");
        Tile t5  = new Tile(8, "MANZU");
        Tile t6  = new Tile(8, "MANZU");
        Tile t7  = new Tile(9, "PINZU");
        Tile t8  = new Tile(9, "PINZU");
        Tile t9  = new Tile(9, "PINZU");
        Tile t10 = new Tile(2, "SOUZU");
        Tile t11 = new Tile(2, "SOUZU");
        Tile t12 = new Tile(2, "SOUZU");
        Tile t13 = new Tile("EAST", "HONOR");
        Tile t14 = new Tile("EAST", "HONOR");

        t6.winningTile = true;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }
    public static Hand getSuuAnkouTankiHand(){
        Tile t1  = new Tile(5, "MANZU");
        Tile t2  = new Tile(5, "MANZU");
        Tile t3  = new Tile(5, "MANZU");
        Tile t4  = new Tile(2, "PINZU");
        Tile t5  = new Tile(2, "PINZU");
        Tile t6  = new Tile(2, "PINZU");
        Tile t7  = new Tile(9, "PINZU");
        Tile t8  = new Tile(9, "PINZU");
        Tile t9  = new Tile(9, "PINZU");
        Tile t10 = new Tile("NORTH", "HONOR");
        Tile t11 = new Tile("NORTH", "HONOR");
        Tile t12 = new Tile("NORTH", "HONOR");
        Tile t13 = new Tile("GREEN", "HONOR");
        Tile t14 = new Tile("GREEN", "HONOR");

        t14.winningTile = true;
        t14.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }
    public static Hand getDaisangenHand(){
        Tile t1  = new Tile(5, "MANZU");
        Tile t2  = new Tile(6, "MANZU");
        Tile t3  = new Tile(7, "MANZU");
        Tile t4  = new Tile(4, "PINZU");
        Tile t5  = new Tile(4, "PINZU");
        Tile t6  = new Tile("WHITE", "HONOR");
        Tile t7  = new Tile("WHITE", "HONOR");
        Tile t8  = new Tile("WHITE", "HONOR");
        Tile t9  = new Tile("GREEN", "HONOR");
        Tile t10 = new Tile("GREEN", "HONOR");
        Tile t11 = new Tile("GREEN", "HONOR");
        Tile t12 = new Tile("RED", "HONOR");
        Tile t13 = new Tile("RED", "HONOR");
        Tile t14 = new Tile("RED", "HONOR");

        t10.winningTile = true;
        t10.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }
    public static Hand getShousuushiiHand(){
        Tile t1  = new Tile(6, "SOUZU");
        Tile t2  = new Tile(7, "SOUZU");
        Tile t3  = new Tile(8, "SOUZU");
        Tile t4  = new Tile("EAST", "HONOR");
        Tile t5  = new Tile("EAST", "HONOR");
        Tile t6  = new Tile("EAST", "HONOR");
        Tile t7  = new Tile("SOUTH", "HONOR");
        Tile t8  = new Tile("SOUTH", "HONOR");
        Tile t9  = new Tile("SOUTH", "HONOR");
        Tile t10 = new Tile("WEST", "HONOR");
        Tile t11 = new Tile("WEST", "HONOR");
        Tile t12 = new Tile("WEST", "HONOR");
        Tile t13 = new Tile("NORTH", "HONOR");
        Tile t14 = new Tile("NORTH", "HONOR");

        t11.winningTile = true;
        t11.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }
    public static Hand getDaishuushiiHand(){
        Tile t1  = new Tile(3, "PINZU");
        Tile t2  = new Tile(3, "PINZU");
        Tile t3  = new Tile("EAST", "HONOR");
        Tile t4  = new Tile("EAST", "HONOR");
        Tile t5  = new Tile("EAST", "HONOR");
        Tile t6  = new Tile("SOUTH", "HONOR");
        Tile t7  = new Tile("SOUTH", "HONOR");
        Tile t8  = new Tile("SOUTH", "HONOR");
        Tile t9  = new Tile("WEST", "HONOR");
        Tile t10 = new Tile("WEST", "HONOR");
        Tile t11 = new Tile("WEST", "HONOR");
        Tile t12 = new Tile("NORTH", "HONOR");
        Tile t13 = new Tile("NORTH", "HONOR");
        Tile t14 = new Tile("NORTH", "HONOR");

        t13.winningTile = true;
        t13.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }
    public static Hand getTsuuiisouHand(){
        Tile t1  = new Tile("EAST", "HONOR");
        Tile t2  = new Tile("EAST", "HONOR");
        Tile t3  = new Tile("EAST", "HONOR");
        Tile t4  = new Tile("SOUTH", "HONOR");
        Tile t5  = new Tile("SOUTH", "HONOR");
        Tile t6  = new Tile("SOUTH", "HONOR");
        Tile t7  = new Tile("WEST", "HONOR");
        Tile t8  = new Tile("WEST", "HONOR");
        Tile t9  = new Tile("WHITE", "HONOR");
        Tile t10 = new Tile("WHITE", "HONOR");
        Tile t11 = new Tile("WHITE", "HONOR");
        Tile t12 = new Tile("GREEN", "HONOR");
        Tile t13 = new Tile("GREEN", "HONOR");
        Tile t14 = new Tile("GREEN", "HONOR");

        t3.winningTile = true;
        t3.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }
    public static Hand getDaichiseiHand(){
        Tile t1  = new Tile("EAST", "HONOR");
        Tile t2  = new Tile("EAST", "HONOR");
        Tile t3  = new Tile("SOUTH", "HONOR");
        Tile t4  = new Tile("SOUTH", "HONOR");
        Tile t5  = new Tile("WEST", "HONOR");
        Tile t6  = new Tile("WEST", "HONOR");
        Tile t7  = new Tile("NORTH", "HONOR");
        Tile t8  = new Tile("NORTH", "HONOR");
        Tile t9  = new Tile("WHITE", "HONOR");
        Tile t10 = new Tile("WHITE", "HONOR");
        Tile t11 = new Tile("GREEN", "HONOR");
        Tile t12 = new Tile("GREEN", "HONOR");
        Tile t13 = new Tile("RED", "HONOR");
        Tile t14 = new Tile("RED", "HONOR");

        t9.winningTile = true;
        t9.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }
    public static Hand getRyuuiisouHand(){
        Tile t1  = new Tile(2, "SOUZU");
        Tile t2  = new Tile(2, "SOUZU");
        Tile t3  = new Tile(3, "SOUZU");
        Tile t4  = new Tile(3, "SOUZU");
        Tile t5  = new Tile(4, "SOUZU");
        Tile t6  = new Tile(4, "SOUZU");
        Tile t7  = new Tile(6, "SOUZU");
        Tile t8  = new Tile(6, "SOUZU");
        Tile t9  = new Tile(6, "SOUZU");
        Tile t10 = new Tile(8, "SOUZU");
        Tile t11 = new Tile(8, "SOUZU");
        Tile t12 = new Tile("GREEN", "HONOR");
        Tile t13 = new Tile("GREEN", "HONOR");
        Tile t14 = new Tile("GREEN", "HONOR");

        t8.winningTile = true;
        t8.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }
    public static Hand getChuurenPoutouHand(){
        Tile t1  = new Tile(1, "MANZU");
        Tile t2  = new Tile(1, "MANZU");
        Tile t3  = new Tile(1, "MANZU");
        Tile t4  = new Tile(2, "MANZU");
        Tile t5  = new Tile(3, "MANZU");
        Tile t6  = new Tile(4, "MANZU");
        Tile t7  = new Tile(4, "MANZU");
        Tile t8  = new Tile(5, "MANZU");
        Tile t9  = new Tile(6, "MANZU");
        Tile t10 = new Tile(7, "MANZU");
        Tile t11 = new Tile(8, "MANZU");
        Tile t12 = new Tile(9, "MANZU");
        Tile t13 = new Tile(9, "MANZU");
        Tile t14 = new Tile(9, "MANZU");

        t11.winningTile = true;
        t11.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }
    public static Hand getChuurenPoutou9SidedHand(){
        Tile t1  = new Tile(1, "MANZU");
        Tile t2  = new Tile(1, "MANZU");
        Tile t3  = new Tile(1, "MANZU");
        Tile t4  = new Tile(2, "MANZU");
        Tile t5  = new Tile(3, "MANZU");
        Tile t6  = new Tile(4, "MANZU");
        Tile t7  = new Tile(5, "MANZU");
        Tile t8  = new Tile(6, "MANZU");
        Tile t9  = new Tile(7, "MANZU");
        Tile t10 = new Tile(8, "MANZU");
        Tile t11 = new Tile(8, "MANZU");
        Tile t12 = new Tile(9, "MANZU");
        Tile t13 = new Tile(9, "MANZU");
        Tile t14 = new Tile(9, "MANZU");

        t10.winningTile = true;
        t10.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }
    public static Hand getSuuKantsuHand(){
        Tile t1  = new Tile(1, "MANZU");
        Tile t2  = new Tile(1, "MANZU");
        Tile t3  = new Tile(1, "MANZU");
        Tile t4  = new Tile(1, "MANZU");
        Tile t5  = new Tile(4, "PINZU");
        Tile t6  = new Tile(4, "PINZU");
        Tile t7  = new Tile(4, "PINZU");
        Tile t8  = new Tile(4, "PINZU");
        Tile t9  = new Tile(9, "PINZU");
        Tile t10 = new Tile(9, "PINZU");
        Tile t11 = new Tile("WHITE", "HONOR");
        Tile t12 = new Tile("WHITE", "HONOR");
        Tile t13 = new Tile("WHITE", "HONOR");
        Tile t14 = new Tile("WHITE", "HONOR");
        Tile t15 = new Tile("NORTH", "HONOR");
        Tile t16 = new Tile("NORTH", "HONOR");
        Tile t17 = new Tile("NORTH", "HONOR");
        Tile t18 = new Tile("NORTH", "HONOR");

        t1.revealedState = t2.revealedState = t3.revealedState = t4.revealedState = Tile.RevealedState.CLOSEDKAN;
        t5.revealedState = t6.revealedState = t7.revealedState = Tile.RevealedState.PON;
        t8.revealedState = Tile.RevealedState.ADDEDKAN;
        t7.calledFrom = Tile.CalledFrom.RIGHT;
        t11.revealedState = t12.revealedState = t13.revealedState = t14.revealedState = Tile.RevealedState.OPENKAN;
        t12.calledFrom = Tile.CalledFrom.CENTER;
        t15.revealedState = t16.revealedState = t17.revealedState = t18.revealedState = Tile.RevealedState.CLOSEDKAN;


        t9.winningTile = true;
        t9.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }
}
