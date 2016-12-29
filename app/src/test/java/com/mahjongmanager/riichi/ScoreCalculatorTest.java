package com.mahjongmanager.riichi;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class ScoreCalculatorTest {

    // (1)  Ittsuu
    // (2)  Riichi, Ippatsu, Tsumo, Pinfu
    // (3)  Riichi, Ippatsu, Iipeikou, Sanshoku Doujun, Tanyao, Pinfu
    // (4)  Kokushi Musou
    // (5)  Double Riichi, Chii Toitsu
    // (6)  Toitoi, Shousangen, Red Dragon, Green Dragon
    // (7)  San Kantsu, White Dragon, Honroutou, Rinshan, Toitoi
    // (8)  Chanta, Prevailing Wind, Seat Wind, San Ankou
    // (9)  Sanshoku Doukou, Junchan, Houtei
    // (10)  Ryanpeikou, Honitsu
    // (11)  Chinitsu, Chan Kan
    // (12)  Nagashi Mangan
    // (13)  Extreme fu (1 Han, 110 Fu)
    // (14)  Extreme fu (2 Han, 110 Fu)
    // (15)  Confusing structure (no score)
    // ()  TODO Value is less than theoretical, because of called tile (inferred for HC)
    // ()  TODO Value is less than theoretical, because closed kan was not terminal (inferred for HC)
    // ()  TODO Value is less than theoretical, because of winning tile (inferred for HC)
    // ()  TODO Has too many of a tile (maybe in dora indicator)

    // Unaccounted Hands:
    // Sanrenkou
    // Suurenkou
    // Dai Sharin
    // Shiisanpuuta
    // Shiisuupuuta
    // Parenchan


    @Test
    public void handTest1() {
        // This hand should have: Ittsuu
        // Han: 2       Fu: 32 (40)
        Tile t1 = new Tile(1, Tile.Suit.SOUZU);
        Tile t2 = new Tile(2, Tile.Suit.SOUZU);
        Tile t3 = new Tile(3, Tile.Suit.SOUZU);
        Tile t4 = new Tile(4, Tile.Suit.SOUZU);
        Tile t5 = new Tile(5, Tile.Suit.SOUZU);
        Tile t6 = new Tile(6, Tile.Suit.SOUZU);
        Tile t7 = new Tile(7, Tile.Suit.SOUZU);
        Tile t8 = new Tile(8, Tile.Suit.SOUZU);
        Tile t9 = new Tile(9, Tile.Suit.SOUZU);
        Tile t10 = new Tile(1, Tile.Suit.MANZU);
        Tile t11 = new Tile(2, Tile.Suit.MANZU);
        Tile t12 = new Tile(3, Tile.Suit.MANZU);
        Tile t13 = new Tile(Tile.Dragon.RED);
        Tile t14 = new Tile(Tile.Dragon.RED);

        t6.winningTile = true;
        t6.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        Hand validatedHand = sc.validatedHand;

        Assert.assertSame( 2, validatedHand.han);
        Assert.assertSame(32, validatedHand.fu );
    }

    @Test
    public void handTest2() {
        // This hand should have: Riichi, Ippatsu, Tsumo, Pinfu
        // Han: 4       Fu: 20
        Tile t1 = new Tile(3, Tile.Suit.SOUZU);
        Tile t2 = new Tile(4, Tile.Suit.SOUZU);
        Tile t3 = new Tile(4, Tile.Suit.SOUZU);
        Tile t4 = new Tile(5, Tile.Suit.SOUZU);
        Tile t5 = new Tile(5, Tile.Suit.SOUZU);
        Tile t6 = new Tile(6, Tile.Suit.SOUZU);
        Tile t7 = new Tile(7, Tile.Suit.SOUZU);
        Tile t8 = new Tile(8, Tile.Suit.SOUZU);
        Tile t9 = new Tile(9, Tile.Suit.SOUZU);
        Tile t10 = new Tile(1, Tile.Suit.MANZU);
        Tile t11 = new Tile(2, Tile.Suit.MANZU);
        Tile t12 = new Tile(3, Tile.Suit.MANZU);
        Tile t13 = new Tile(3, Tile.Suit.PINZU);
        Tile t14 = new Tile(3, Tile.Suit.PINZU);

        t6.winningTile = true;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));
        h.riichi = true;
        h.ippatsu = true;

        ScoreCalculator sc = new ScoreCalculator(h);
        Hand validatedHand = sc.validatedHand;

        Assert.assertSame(4, validatedHand.han);
        Assert.assertSame(20, validatedHand.fu);
    }

    @Test
    public void handTest3() {
        // This hand should have: Riichi, Ippatsu, Iipeikou, Sanshoku Doujun, Tanyao, Pinfu
        // Han: 7       Fu: 20
        Tile t1 = new Tile(3, Tile.Suit.SOUZU);
        Tile t2 = new Tile(3, Tile.Suit.SOUZU);
        Tile t3 = new Tile(4, Tile.Suit.SOUZU);
        Tile t4 = new Tile(4, Tile.Suit.SOUZU);
        Tile t5 = new Tile(5, Tile.Suit.SOUZU);
        Tile t6 = new Tile(5, Tile.Suit.SOUZU);
        Tile t7 = new Tile(3, Tile.Suit.PINZU);
        Tile t8 = new Tile(4, Tile.Suit.PINZU);
        Tile t9 = new Tile(5, Tile.Suit.PINZU);
        Tile t10 = new Tile(3, Tile.Suit.MANZU);
        Tile t11 = new Tile(4, Tile.Suit.MANZU);
        Tile t12 = new Tile(5, Tile.Suit.MANZU);
        Tile t13 = new Tile(3, Tile.Suit.PINZU);
        Tile t14 = new Tile(3, Tile.Suit.PINZU);

        t6.winningTile = true;
        t6.calledFrom = Tile.CalledFrom.LEFT;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));
        h.riichi = true;
        h.ippatsu = true;

        ScoreCalculator sc = new ScoreCalculator(h);
        Hand validatedHand = sc.validatedHand;

        Assert.assertSame(7, validatedHand.han);
        Assert.assertSame(30, validatedHand.fu);
    }

    @Test
    public void handTest4() {
        // This hand should have: Kokushi Musou
        // Han: 13       Fu: ---
        Tile t1 = new Tile(1, Tile.Suit.SOUZU);
        Tile t2 = new Tile(9, Tile.Suit.SOUZU);
        Tile t3 = new Tile(1, Tile.Suit.MANZU);
        Tile t4 = new Tile(9, Tile.Suit.MANZU);
        Tile t5 = new Tile(1, Tile.Suit.PINZU);
        Tile t6 = new Tile(9, Tile.Suit.PINZU);
        Tile t7 = new Tile(9, Tile.Suit.PINZU);
        Tile t8 = new Tile(Tile.Wind.EAST);
        Tile t9 = new Tile(Tile.Wind.SOUTH);
        Tile t10 = new Tile(Tile.Wind.WEST);
        Tile t11 = new Tile(Tile.Wind.NORTH);
        Tile t12 = new Tile(Tile.Dragon.WHITE);
        Tile t13 = new Tile(Tile.Dragon.GREEN);
        Tile t14 = new Tile(Tile.Dragon.RED);

        t6.winningTile = true;
        t6.calledFrom = Tile.CalledFrom.LEFT;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        Hand validatedHand = sc.validatedHand;

        Assert.assertSame(26, validatedHand.han);
    }

    @Test
    public void handTest5() {
        // This hand should have: Double Riichi, Chii Toitsu
        // Han: 4       Fu: 25
        Tile t1 = new Tile(1, Tile.Suit.SOUZU);
        Tile t2 = new Tile(1, Tile.Suit.SOUZU);
        Tile t3 = new Tile(6, Tile.Suit.MANZU);
        Tile t4 = new Tile(6, Tile.Suit.MANZU);
        Tile t5 = new Tile(4, Tile.Suit.PINZU);
        Tile t6 = new Tile(4, Tile.Suit.PINZU);
        Tile t7 = new Tile(Tile.Wind.EAST);
        Tile t8 = new Tile(Tile.Wind.EAST);
        Tile t9 = new Tile(Tile.Wind.WEST);
        Tile t10 = new Tile(Tile.Wind.WEST);
        Tile t11 = new Tile(Tile.Dragon.WHITE);
        Tile t12 = new Tile(Tile.Dragon.WHITE);
        Tile t13 = new Tile(Tile.Dragon.RED);
        Tile t14 = new Tile(Tile.Dragon.RED);

        t6.winningTile = true;
        t6.calledFrom = Tile.CalledFrom.LEFT;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));
        h.doubleRiichi = true;

        ScoreCalculator sc = new ScoreCalculator(h);
        Hand validatedHand = sc.validatedHand;

        Assert.assertSame(4, validatedHand.han);
        Assert.assertSame(25, validatedHand.fu);
    }

    @Test
    public void handTest6() {
        // This hand should have: Toitoi, Shousangen, Red Dragon, Green Dragon
        // Han: 6       Fu: 44
        Tile t1 = new Tile(1, Tile.Suit.SOUZU);
        Tile t2 = new Tile(1, Tile.Suit.SOUZU);
        Tile t3 = new Tile(1, Tile.Suit.SOUZU);
        Tile t4 = new Tile(4, Tile.Suit.PINZU);
        Tile t5 = new Tile(4, Tile.Suit.PINZU);
        Tile t6 = new Tile(4, Tile.Suit.PINZU);
        Tile t7 = new Tile( Tile.Dragon.WHITE);
        Tile t8 = new Tile( Tile.Dragon.WHITE);
        Tile t9 = new Tile(Tile.Dragon.GREEN);
        Tile t10 = new Tile(Tile.Dragon.GREEN);
        Tile t11 = new Tile(Tile.Dragon.GREEN);
        Tile t12 = new Tile(Tile.Dragon.RED);
        Tile t13 = new Tile(Tile.Dragon.RED);
        Tile t14 = new Tile(Tile.Dragon.RED);

        t9.revealedState = t10.revealedState = t11.revealedState = Tile.RevealedState.PON;
        t9.calledFrom = Tile.CalledFrom.LEFT;

        t6.calledFrom = Tile.CalledFrom.LEFT;
        t6.winningTile = true;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        Hand validatedHand = sc.validatedHand;

        Assert.assertSame(6, validatedHand.han);
        Assert.assertSame(44, validatedHand.fu);
    }

    @Test
    public void handTest7() {
        // This hand should have: San Kantsu, White Dragon, Honroutou, Rinshan, Toitoi
        // Han: 9       Fu: ??
        Tile t1 = new Tile(1, Tile.Suit.SOUZU);
        Tile t2 = new Tile(1, Tile.Suit.SOUZU);
        Tile t3 = new Tile(1, Tile.Suit.SOUZU);
        Tile t4 = new Tile(9, Tile.Suit.PINZU);
        Tile t5 = new Tile(9, Tile.Suit.PINZU);
        Tile t6 = new Tile(9, Tile.Suit.PINZU);
        Tile t7 = new Tile( Tile.Wind.NORTH);
        Tile t8 = new Tile( Tile.Wind.NORTH);
        Tile t9 = new Tile( Tile.Wind.NORTH);
        Tile t10 = new Tile(Tile.Dragon.GREEN);
        Tile t11 = new Tile(Tile.Dragon.GREEN);
        Tile t12 = new Tile(Tile.Dragon.GREEN);
        Tile t13 = new Tile(Tile.Dragon.RED);
        Tile t14 = new Tile(Tile.Dragon.RED);

        Tile t15 = new Tile(1, Tile.Suit.SOUZU);
        Tile t16 = new Tile(9, Tile.Suit.PINZU);
        Tile t17 = new Tile(Tile.Dragon.GREEN);

        t1.revealedState = t2.revealedState = t3.revealedState = t15.revealedState = Tile.RevealedState.CLOSEDKAN;
        t4.revealedState = t5.revealedState = t6.revealedState = t16.revealedState = Tile.RevealedState.OPENKAN;
        t6.calledFrom = Tile.CalledFrom.LEFT;
        t10.revealedState = t11.revealedState = t12.revealedState = t17.revealedState = Tile.RevealedState.OPENKAN;
        t10.calledFrom = Tile.CalledFrom.RIGHT;

        t9.winningTile = true;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17));
        h.rinshan = true;

        ScoreCalculator sc = new ScoreCalculator(h);
        Hand validatedHand = sc.validatedHand;

        Assert.assertSame(8, validatedHand.han);
    }

    @Test
    public void handTest8() {
        // This hand should have: Chanta, Prevailing Wind, Seat Wind, San Ankou
        // Han: 5       Fu: 46  (50)
        Tile t1 = new Tile(1, Tile.Suit.SOUZU);
        Tile t2 = new Tile(2, Tile.Suit.SOUZU);
        Tile t3 = new Tile(3, Tile.Suit.SOUZU);
        Tile t4 = new Tile(9, Tile.Suit.PINZU);
        Tile t5 = new Tile(9, Tile.Suit.PINZU);
        Tile t6 = new Tile(Tile.Wind.NORTH);
        Tile t7 = new Tile(Tile.Wind.NORTH);
        Tile t8 = new Tile(Tile.Wind.NORTH);
        Tile t9 = new Tile( Tile.Wind.EAST);
        Tile t10 = new Tile(Tile.Wind.EAST);
        Tile t11 = new Tile(Tile.Wind.EAST);
        Tile t12 = new Tile(Tile.Wind.WEST);
        Tile t13 = new Tile(Tile.Wind.WEST);
        Tile t14 = new Tile(Tile.Wind.WEST);

        t1.revealedState = t2.revealedState = t3.revealedState = Tile.RevealedState.CHI;
        t2.calledFrom = Tile.CalledFrom.LEFT;

        t5.calledFrom = Tile.CalledFrom.CENTER;
        t5.winningTile = true;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        Hand validatedHand = sc.validatedHand;

        Assert.assertSame(5, validatedHand.han);
        Assert.assertSame(46, validatedHand.fu);
    }

    @Test
    public void handTest9() {
        // This hand should have: Sanshoku Doukou, Junchan, Houtei
        // Han: 5       Fu: 42
        Tile t1 = new Tile(1, Tile.Suit.SOUZU);
        Tile t2 = new Tile(1, Tile.Suit.SOUZU);
        Tile t3 = new Tile(1, Tile.Suit.SOUZU);
        Tile t4 = new Tile(7, Tile.Suit.PINZU);
        Tile t5 = new Tile(8, Tile.Suit.PINZU);
        Tile t6 = new Tile(9, Tile.Suit.PINZU);
        Tile t7 = new Tile(1, Tile.Suit.PINZU);
        Tile t8 = new Tile(1, Tile.Suit.PINZU);
        Tile t9 = new Tile(1, Tile.Suit.PINZU);
        Tile t10 = new Tile(9, Tile.Suit.MANZU);
        Tile t11 = new Tile(9, Tile.Suit.MANZU);
        Tile t12 = new Tile(1, Tile.Suit.MANZU);
        Tile t13 = new Tile(1, Tile.Suit.MANZU);
        Tile t14 = new Tile(1, Tile.Suit.MANZU);

        t1.revealedState = t2.revealedState = t3.revealedState = Tile.RevealedState.PON;
        t2.calledFrom = Tile.CalledFrom.LEFT;

        t5.calledFrom = Tile.CalledFrom.CENTER;
        t5.winningTile = true;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));
        h.houtei = true;

        ScoreCalculator sc = new ScoreCalculator(h);
        Hand validatedHand = sc.validatedHand;

        Assert.assertSame(5, validatedHand.han);
        Assert.assertSame(42, validatedHand.fu);
    }

    @Test
    public void handTest10() {
        // This hand should have: Ryanpeikou, Honitsu
        // Han: 6       Fu: --
        Tile t1 = new Tile(1, Tile.Suit.SOUZU);
        Tile t2 = new Tile(1, Tile.Suit.SOUZU);
        Tile t3 = new Tile(2, Tile.Suit.SOUZU);
        Tile t4 = new Tile(2, Tile.Suit.SOUZU);
        Tile t5 = new Tile(3, Tile.Suit.SOUZU);
        Tile t6 = new Tile(3, Tile.Suit.SOUZU);
        Tile t7 = new Tile(5, Tile.Suit.SOUZU);
        Tile t8 = new Tile(5, Tile.Suit.SOUZU);
        Tile t9 = new Tile(6, Tile.Suit.SOUZU);
        Tile t10 = new Tile(6, Tile.Suit.SOUZU);
        Tile t11 = new Tile(7, Tile.Suit.SOUZU);
        Tile t12 = new Tile(7, Tile.Suit.SOUZU);
        Tile t13 = new Tile(Tile.Dragon.RED);
        Tile t14 = new Tile(Tile.Dragon.RED);

        t5.calledFrom = Tile.CalledFrom.CENTER;
        t5.winningTile = true;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        Hand validatedHand = sc.validatedHand;

        Assert.assertSame(6, validatedHand.han);
    }

    @Test
    public void handTest11() {
        // This hand should have: Chinitsu, Chan Kan
        // Han: 7       Fu: --
        Tile t1 = new Tile(1, Tile.Suit.SOUZU);
        Tile t2 = new Tile(2, Tile.Suit.SOUZU);
        Tile t3 = new Tile(2, Tile.Suit.SOUZU);
        Tile t4 = new Tile(3, Tile.Suit.SOUZU);
        Tile t5 = new Tile(3, Tile.Suit.SOUZU);
        Tile t6 = new Tile(3, Tile.Suit.SOUZU);
        Tile t7 = new Tile(3, Tile.Suit.SOUZU);
        Tile t8 = new Tile(4, Tile.Suit.SOUZU);
        Tile t9 = new Tile(5, Tile.Suit.SOUZU);
        Tile t10 = new Tile(5, Tile.Suit.SOUZU);
        Tile t11 = new Tile(5, Tile.Suit.SOUZU);
        Tile t12 = new Tile(5, Tile.Suit.SOUZU);
        Tile t13 = new Tile(6, Tile.Suit.SOUZU);
        Tile t14 = new Tile(7, Tile.Suit.SOUZU);
        Tile t15 = new Tile(8, Tile.Suit.SOUZU);

        t9.revealedState = t10.revealedState = t11.revealedState = t12.revealedState = Tile.RevealedState.CLOSEDKAN;

        t8.calledFrom = Tile.CalledFrom.CENTER;
        t8.winningTile = true;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15));
        h.chanKan = true;

        ScoreCalculator sc = new ScoreCalculator(h);
        Hand validatedHand = sc.validatedHand;

        Assert.assertSame(7, validatedHand.han);
    }

    @Test
    public void handTest12() {
        // This hand should have: Nagashi Mangan
        // Han: 5       Fu: --
        Tile t1 = new Tile(1, Tile.Suit.SOUZU);
        Tile t2 = new Tile(2, Tile.Suit.SOUZU);
        Tile t3 = new Tile(2, Tile.Suit.SOUZU);
        Tile t4 = new Tile(3, Tile.Suit.SOUZU);
        Tile t5 = new Tile(3, Tile.Suit.SOUZU);
        Tile t6 = new Tile(4, Tile.Suit.SOUZU);
        Tile t7 = new Tile(3, Tile.Suit.SOUZU);
        Tile t8 = new Tile(3, Tile.Suit.SOUZU);
        Tile t9 = new Tile(5, Tile.Suit.SOUZU);
        Tile t10 = new Tile(5, Tile.Suit.SOUZU);
        Tile t11 = new Tile(5, Tile.Suit.SOUZU);
        Tile t12 = new Tile(6, Tile.Suit.SOUZU);
        Tile t13 = new Tile(7, Tile.Suit.SOUZU);
        Tile t14 = new Tile(8, Tile.Suit.SOUZU);

        Tile t15 = new Tile(5, Tile.Suit.SOUZU);

        t9.revealedState = t10.revealedState = t11.revealedState = t15.revealedState = Tile.RevealedState.CLOSEDKAN;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15));
        h.nagashiMangan = true;

        ScoreCalculator sc = new ScoreCalculator(h);
        Hand validatedHand = sc.validatedHand;

        Assert.assertSame(5, validatedHand.han);
    }

    @Test
    public void handTest13() {
        // This hand should have: Extreme fu
        // Han: 1       Fu: 102 (110)
        Tile t1 = new Tile(Tile.Dragon.RED);
        Tile t2 = new Tile(Tile.Dragon.RED);
        Tile t3 = new Tile(Tile.Dragon.RED);
        Tile t4 = new Tile(Tile.Dragon.RED);
        Tile t5 = new Tile(9, Tile.Suit.SOUZU);
        Tile t6 = new Tile(9, Tile.Suit.SOUZU);
        Tile t7 = new Tile(9, Tile.Suit.SOUZU);
        Tile t8 = new Tile(9, Tile.Suit.SOUZU);
        Tile t9 = new Tile( Tile.Wind.NORTH);
        Tile t10 = new Tile(Tile.Wind.NORTH);
        Tile t11 = new Tile(Tile.Wind.NORTH);
        Tile t12 = new Tile(6, Tile.Suit.MANZU);
        Tile t13 = new Tile(7, Tile.Suit.MANZU);
        Tile t14 = new Tile(8, Tile.Suit.MANZU);

        Tile t15 = new Tile(Tile.Wind.SOUTH);
        Tile t16 = new Tile(Tile.Wind.SOUTH);

        t1.revealedState = t2.revealedState = t3.revealedState = t4.revealedState = Tile.RevealedState.CLOSEDKAN;
        t5.revealedState = t6.revealedState = t7.revealedState = t8.revealedState = Tile.RevealedState.CLOSEDKAN;

        t10.calledFrom = Tile.CalledFrom.CENTER;
        t10.winningTile = true;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16));
        h.prevailingWind = Tile.Wind.SOUTH;
        h.playerWind = Tile.Wind.SOUTH;

        ScoreCalculator sc = new ScoreCalculator(h);
        Hand validatedHand = sc.validatedHand;

        Assert.assertSame(  1, validatedHand.han);
        Assert.assertSame(102, validatedHand.fu );
    }

    @Test
    public void handTest14() {
        // This hand should have: Extreme fu
        // Han: 2       Fu: 108 (110)
        Tile t1 = new Tile(Tile.Wind.EAST);
        Tile t2 = new Tile(Tile.Wind.EAST);
        Tile t3 = new Tile(Tile.Wind.EAST);
        Tile t4 = new Tile(Tile.Wind.EAST);
        Tile t5 = new Tile(9, Tile.Suit.SOUZU);
        Tile t6 = new Tile(9, Tile.Suit.SOUZU);
        Tile t7 = new Tile(9, Tile.Suit.SOUZU);
        Tile t8 = new Tile(9, Tile.Suit.SOUZU);
        Tile t9 = new Tile( Tile.Wind.NORTH);
        Tile t10 = new Tile(Tile.Wind.NORTH);
        Tile t11 = new Tile(Tile.Wind.NORTH);
        Tile t12 = new Tile(Tile.Wind.NORTH);
        Tile t13 = new Tile(6, Tile.Suit.MANZU);
        Tile t14 = new Tile(7, Tile.Suit.MANZU);
        Tile t15 = new Tile(8, Tile.Suit.MANZU);

        Tile t16 = new Tile(Tile.Wind.SOUTH);
        Tile t17 = new Tile(Tile.Wind.SOUTH);

        t1.revealedState = t2.revealedState = t3.revealedState = t4.revealedState = Tile.RevealedState.CLOSEDKAN;
        t5.revealedState = t6.revealedState = t7.revealedState = t8.revealedState = Tile.RevealedState.CLOSEDKAN;
        t9.revealedState = t10.revealedState = t11.revealedState = t12.revealedState = Tile.RevealedState.OPENKAN;
        t10.calledFrom = Tile.CalledFrom.CENTER;

        t16.winningTile = true;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17));
        h.prevailingWind = Tile.Wind.SOUTH;
        h.playerWind = Tile.Wind.SOUTH;

        ScoreCalculator sc = new ScoreCalculator(h);
        Hand validatedHand = sc.validatedHand;

        Assert.assertSame(  2, validatedHand.han);
        Assert.assertSame(108, validatedHand.fu );
    }

    @Test
    public void handTest15() {
        // This hand is primarily testing the ability to sort this hand correctly
        // Han:        Fu:
        Tile t1 = new Tile(1, Tile.Suit.MANZU);
        Tile t2 = new Tile(1, Tile.Suit.MANZU);
        Tile t3 = new Tile(1, Tile.Suit.MANZU);
        Tile t4 = new Tile(1, Tile.Suit.MANZU);
        Tile t5 = new Tile(2, Tile.Suit.MANZU);
        Tile t6 = new Tile(2, Tile.Suit.MANZU);
        Tile t7 = new Tile(2, Tile.Suit.MANZU);
        Tile t8 = new Tile(2, Tile.Suit.MANZU);
        Tile t9 = new Tile( 3, Tile.Suit.MANZU);
        Tile t10 = new Tile(3, Tile.Suit.MANZU);
        Tile t11 = new Tile(3, Tile.Suit.MANZU);
        Tile t12 = new Tile(3, Tile.Suit.MANZU);
        Tile t13 = new Tile(4, Tile.Suit.MANZU);
        Tile t14 = new Tile(4, Tile.Suit.MANZU);

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));
        h.prevailingWind = Tile.Wind.SOUTH;
        h.playerWind = Tile.Wind.SOUTH;

        ScoreCalculator sc = new ScoreCalculator(h);
        Hand scoredHand = sc.scoredHand;
        Assert.assertTrue(scoredHand!=null);

        //Assert.assertSame(  2, validatedHand.han);
        //Assert.assertSame(108, validatedHand.fu );
    }

    @Test
    public void handTest17() {
        // This hand is primarily testing the ability to sort this hand correctly
        // Value is less than theoretical, because closed kan was not terminal
        Tile t1  = new Tile(1, Tile.Suit.MANZU);
        Tile t2  = new Tile(1, Tile.Suit.MANZU);
        Tile t3  = new Tile(1, Tile.Suit.MANZU);
        Tile t4  = new Tile(1, Tile.Suit.MANZU);
        Tile t5  = new Tile(2, Tile.Suit.MANZU);
        Tile t6  = new Tile(3, Tile.Suit.MANZU);
        Tile t7  = new Tile(4, Tile.Suit.MANZU);
        Tile t8  = new Tile(4, Tile.Suit.MANZU);
        Tile t9  = new Tile(4, Tile.Suit.MANZU);
        Tile t10 = new Tile(4, Tile.Suit.MANZU);
        Tile t11 = new Tile(5, Tile.Suit.PINZU);
        Tile t12 = new Tile(6, Tile.Suit.PINZU);
        Tile t13 = new Tile(7, Tile.Suit.PINZU);
        Tile t14 = new Tile(8, Tile.Suit.PINZU);
        Tile t15 = new Tile(8, Tile.Suit.PINZU);

//        t7.revealedState = t8.revealedState = t9.revealedState = t10.revealedState = Tile.RevealedState.CLOSEDKAN;

        t5.calledFrom = Tile.CalledFrom.LEFT;
        t5.winningTile = true;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15));

        ScoreCalculator sc = new ScoreCalculator(h);
        Hand scoredHand = sc.scoredHand;
        Assert.assertTrue(scoredHand!=null);
        Hand validatedHand = sc.validatedHand;
        Assert.assertTrue(validatedHand==null);
    }


    @Test
    public void problemHand1() {
        // Hand was not returning sanshoku doujun correctly
        Tile t1  = new Tile(1, Tile.Suit.MANZU);
        Tile t2  = new Tile(2, Tile.Suit.MANZU);
        Tile t3  = new Tile(3, Tile.Suit.MANZU);
        Tile t4  = new Tile(4, Tile.Suit.MANZU);
        Tile t5  = new Tile(4, Tile.Suit.MANZU);
        Tile t6  = new Tile(6, Tile.Suit.MANZU);
        Tile t7  = new Tile(7, Tile.Suit.MANZU);
        Tile t8  = new Tile(8, Tile.Suit.MANZU);
        Tile t9  = new Tile(6, Tile.Suit.PINZU);
        Tile t10 = new Tile(7, Tile.Suit.PINZU);
        Tile t11 = new Tile(8, Tile.Suit.PINZU);
        Tile t12 = new Tile(6, Tile.Suit.SOUZU);
        Tile t13 = new Tile(7, Tile.Suit.SOUZU);
        Tile t14 = new Tile(8, Tile.Suit.SOUZU);

        t13.calledFrom = Tile.CalledFrom.CENTER;
        t13.winningTile = true;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));
        h.riichi = true;

        ScoreCalculator sc = new ScoreCalculator(h);
        Hand validatedHand = sc.validatedHand;
        Assert.assertNotNull(validatedHand);
        Assert.assertTrue(validatedHand.sanshokuDoujun);
    }

    @Test
    public void problemHand2() {
        // This hand was observed (incorrectly) being scored as Chiitoitsu
        // Han: 10       Fu: 30
        Tile t1 = new Tile(1, Tile.Suit.MANZU);
        Tile t2 = new Tile(1, Tile.Suit.MANZU);
        Tile t3 = new Tile(4, Tile.Suit.MANZU);
        Tile t4 = new Tile(4, Tile.Suit.MANZU);
        Tile t5 = new Tile(5, Tile.Suit.MANZU);
        Tile t6 = new Tile(5, Tile.Suit.MANZU);
        Tile t7 = new Tile(6, Tile.Suit.MANZU);
        Tile t8 = new Tile(6, Tile.Suit.MANZU);
        Tile t9  = new Tile(7, Tile.Suit.MANZU);
        Tile t10 = new Tile(7, Tile.Suit.MANZU);
        Tile t11 = new Tile(8, Tile.Suit.MANZU);
        Tile t12 = new Tile(8, Tile.Suit.MANZU);
        Tile t13 = new Tile(9, Tile.Suit.MANZU);
        Tile t14 = new Tile(9, Tile.Suit.MANZU);

        t3.calledFrom = Tile.CalledFrom.CENTER;
        t3.winningTile = true;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        Hand validatedHand = sc.validatedHand;
        Assert.assertTrue(validatedHand!=null);

        Assert.assertSame( 10, validatedHand.han);
        Assert.assertSame( 30, validatedHand.fu );
    }

    @Test
    public void doraIndicatorTest(){
        // This hand should have: Extreme fu
        // Han: 1       Fu: 102 (110)
        Tile t1 = new Tile(Tile.Dragon.RED);
        Tile t2 = new Tile(Tile.Dragon.RED);
        Tile t3 = new Tile(Tile.Dragon.RED);
        Tile t4 = new Tile(9, Tile.Suit.SOUZU);
        Tile t5 = new Tile(9, Tile.Suit.SOUZU);
        Tile t6 = new Tile(9, Tile.Suit.SOUZU);
        Tile t7 = new Tile(9, Tile.Suit.SOUZU);
        Tile t8 = new Tile( Tile.Wind.NORTH);
        Tile t9 = new Tile( Tile.Wind.NORTH);
        Tile t10 = new Tile(Tile.Wind.NORTH);
        Tile t11 = new Tile(6, Tile.Suit.MANZU);
        Tile t12 = new Tile(7, Tile.Suit.MANZU);
        Tile t13 = new Tile(8, Tile.Suit.MANZU);

        Tile t14 = new Tile(Tile.Wind.SOUTH);
        Tile t15 = new Tile(Tile.Wind.SOUTH);

        t4.revealedState = t5.revealedState = t6.revealedState = t7.revealedState = Tile.RevealedState.CLOSEDKAN;

        t10.calledFrom = Tile.CalledFrom.CENTER;
        t10.winningTile = true;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15));
        h.prevailingWind = Tile.Wind.SOUTH;
        h.playerWind = Tile.Wind.SOUTH;

        h.doraIndicator1 = new Tile(Tile.Dragon.GREEN);
        h.doraIndicator2 = new Tile(Tile.Wind.WEST);
        h.doraIndicator3 = new Tile(6, Tile.Suit.MANZU);
        h.doraIndicator4 = new Tile(7, Tile.Suit.MANZU);

        ScoreCalculator sc = new ScoreCalculator(h);
        Hand validatedHand = sc.validatedHand;

        Assert.assertSame(  9, validatedHand.han);
        Assert.assertSame(  8, validatedHand.dora);
    }
}