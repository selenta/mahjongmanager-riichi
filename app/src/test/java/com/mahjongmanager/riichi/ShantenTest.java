package com.mahjongmanager.riichi;

import com.mahjongmanager.riichi.common.Hand;
import com.mahjongmanager.riichi.common.Tile;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class ShantenTest {

    @Test
    public void shantenTest1() {
        // Closed, no pair, no triplet
        // Shanten: 1
        Tile t1  = new Tile(3, Tile.Suit.MANZU);
        Tile t2  = new Tile(3, Tile.Suit.MANZU);
        Tile t3  = new Tile(4, Tile.Suit.MANZU);
        Tile t4  = new Tile(4, Tile.Suit.MANZU);
        Tile t5  = new Tile(5, Tile.Suit.MANZU);
        Tile t6  = new Tile(5, Tile.Suit.MANZU);
        Tile t7  = new Tile(7, Tile.Suit.SOUZU);
        Tile t8  = new Tile(8, Tile.Suit.SOUZU);
        Tile t9  = new Tile(9, Tile.Suit.SOUZU);
        Tile t10 = new Tile(1, Tile.Suit.PINZU);
        Tile t11 = new Tile(2, Tile.Suit.PINZU);
        Tile t12 = new Tile(5, Tile.Suit.PINZU);
        Tile t13 = new Tile(6, Tile.Suit.PINZU);

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13));

        ScoreCalculator sc = new ScoreCalculator(h, true);
        Assert.assertSame(1, sc.shanten );
    }

    @Test
    public void shantenTest2() {
        // Closed, no yaku
        // Shanten: 1
        Tile t1  = new Tile(3, Tile.Suit.MANZU);
        Tile t2  = new Tile(3, Tile.Suit.MANZU);
        Tile t3  = new Tile(3, Tile.Suit.MANZU);
        Tile t4  = new Tile(5, Tile.Suit.MANZU);
        Tile t5  = new Tile(5, Tile.Suit.MANZU);
        Tile t6  = new Tile(5, Tile.Suit.MANZU);
        Tile t7  = new Tile(7, Tile.Suit.SOUZU);
        Tile t8  = new Tile(8, Tile.Suit.SOUZU);
        Tile t9  = new Tile(9, Tile.Suit.SOUZU);
        Tile t10 = new Tile(2, Tile.Suit.PINZU);
        Tile t11 = new Tile(2, Tile.Suit.PINZU);
        Tile t12 = new Tile(3, Tile.Suit.PINZU);
        Tile t13 = new Tile(6, Tile.Suit.PINZU);

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13));

        ScoreCalculator sc = new ScoreCalculator(h, true);
        Assert.assertSame(1, sc.shanten );
    }


    @Test
    public void shantenTest3() {
        // Open, no yaku
        // Shanten: 1
        Tile t1  = new Tile(3, Tile.Suit.MANZU);
        Tile t2  = new Tile(3, Tile.Suit.MANZU);
        Tile t3  = new Tile(3, Tile.Suit.MANZU);
        Tile t4  = new Tile(5, Tile.Suit.MANZU);
        Tile t5  = new Tile(5, Tile.Suit.MANZU);
        Tile t6  = new Tile(5, Tile.Suit.MANZU);
        Tile t7  = new Tile(7, Tile.Suit.SOUZU);
        Tile t8  = new Tile(8, Tile.Suit.SOUZU);
        Tile t9  = new Tile(9, Tile.Suit.SOUZU);
        Tile t10 = new Tile(2, Tile.Suit.PINZU);
        Tile t11 = new Tile(2, Tile.Suit.PINZU);
        Tile t12 = new Tile(3, Tile.Suit.PINZU);
        Tile t13 = new Tile(6, Tile.Suit.PINZU);

        t1.revealedState = t2.revealedState = t3.revealedState = Tile.RevealedState.PON;
        t1.calledFrom = Tile.CalledFrom.RIGHT;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13));

        ScoreCalculator sc = new ScoreCalculator(h, true);
        Assert.assertSame(1, sc.shanten );
    }


    @Test
    public void shantenTest4() {
        // Closed, several partials
        // Shanten: 2
        Tile t1  = new Tile(1, Tile.Suit.MANZU);
        Tile t2  = new Tile(3, Tile.Suit.MANZU);
        Tile t3  = new Tile(3, Tile.Suit.MANZU);
        Tile t4  = new Tile(5, Tile.Suit.MANZU);
        Tile t5  = new Tile(5, Tile.Suit.MANZU);
        Tile t6  = new Tile(9, Tile.Suit.MANZU);
        Tile t7  = new Tile(4, Tile.Suit.SOUZU);
        Tile t8  = new Tile(5, Tile.Suit.SOUZU);
        Tile t9  = new Tile(6, Tile.Suit.SOUZU);
        Tile t10 = new Tile(2, Tile.Suit.PINZU);
        Tile t11 = new Tile(3, Tile.Suit.PINZU);
        Tile t12 = new Tile(4, Tile.Suit.PINZU);
        Tile t13 = new Tile(5, Tile.Suit.PINZU);

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13));

        ScoreCalculator sc = new ScoreCalculator(h, true);
        Assert.assertSame(2, sc.shanten );
    }

    @Test
    public void shantenTest5() {
        // Closed, single-suit
        // Shanten: 1
        Tile t1  = new Tile(1, Tile.Suit.PINZU);
        Tile t2  = new Tile(1, Tile.Suit.PINZU);
        Tile t3  = new Tile(3, Tile.Suit.PINZU);
        Tile t4  = new Tile(4, Tile.Suit.PINZU);
        Tile t5  = new Tile(4, Tile.Suit.PINZU);
        Tile t6  = new Tile(4, Tile.Suit.PINZU);
        Tile t7  = new Tile(4, Tile.Suit.PINZU);
        Tile t8  = new Tile(5, Tile.Suit.PINZU);
        Tile t9  = new Tile(6, Tile.Suit.PINZU);
        Tile t10 = new Tile(8, Tile.Suit.PINZU);
        Tile t11 = new Tile(8, Tile.Suit.PINZU);
        Tile t12 = new Tile(9, Tile.Suit.PINZU);
        Tile t13 = new Tile(9, Tile.Suit.PINZU);

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13));

        ScoreCalculator sc = new ScoreCalculator(h, true);
        Assert.assertSame(1, sc.shanten );
    }

    @Test
    public void shantenTest6() {
        // Closed, single-suit
        // Shanten: 1
        Tile t1  = new Tile(1, Tile.Suit.PINZU);
        Tile t2  = new Tile(1, Tile.Suit.PINZU);
        Tile t3  = new Tile(1, Tile.Suit.PINZU);
        Tile t4  = new Tile(1, Tile.Suit.PINZU);
        Tile t5  = new Tile(3, Tile.Suit.PINZU);
        Tile t6  = new Tile(4, Tile.Suit.PINZU);
        Tile t7  = new Tile(4, Tile.Suit.PINZU);
        Tile t8  = new Tile(5, Tile.Suit.PINZU);
        Tile t9  = new Tile(8, Tile.Suit.PINZU);
        Tile t10 = new Tile(9, Tile.Suit.PINZU);
        Tile t11 = new Tile(9, Tile.Suit.PINZU);
        Tile t12 = new Tile(9, Tile.Suit.PINZU);
        Tile t13 = new Tile(9, Tile.Suit.PINZU);

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13));

        ScoreCalculator sc = new ScoreCalculator(h, true);
        Assert.assertSame(1, sc.shanten );
    }

    @Test
    public void shantenWorstCaseTest() {
        // Worst possible hand (can't be more than 6 tiles from chiitoitsu)
        // Shanten: 6
        Tile t1  = new Tile(1, Tile.Suit.MANZU);
        Tile t2  = new Tile(4, Tile.Suit.MANZU);
        Tile t3  = new Tile(7, Tile.Suit.MANZU);
        Tile t4  = new Tile(2, Tile.Suit.PINZU);
        Tile t5  = new Tile(5, Tile.Suit.PINZU);
        Tile t6  = new Tile(8, Tile.Suit.PINZU);
        Tile t7  = new Tile(3, Tile.Suit.SOUZU);
        Tile t8  = new Tile(6, Tile.Suit.SOUZU);
        Tile t9  = new Tile(9, Tile.Suit.SOUZU);
        Tile t10 = new Tile(Tile.Wind.EAST);
        Tile t11 = new Tile(Tile.Wind.SOUTH);
        Tile t12 = new Tile(Tile.Dragon.GREEN);
        Tile t13 = new Tile(Tile.Dragon.RED);

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13));

        ScoreCalculator sc = new ScoreCalculator(h, true);
        Assert.assertSame(6, sc.shanten );
    }

    @Test
    public void shantenChitoitsuTest1() {
        // Closed, single-suit
        // Shanten: 1
        Tile t1  = new Tile(1, Tile.Suit.MANZU);
        Tile t2  = new Tile(1, Tile.Suit.MANZU);
        Tile t3  = new Tile(1, Tile.Suit.MANZU);
        Tile t4  = new Tile(2, Tile.Suit.PINZU);
        Tile t5  = new Tile(3, Tile.Suit.PINZU);
        Tile t6  = new Tile(5, Tile.Suit.PINZU);
        Tile t7  = new Tile(5, Tile.Suit.PINZU);
        Tile t8  = new Tile(6, Tile.Suit.PINZU);
        Tile t9  = new Tile(6, Tile.Suit.PINZU);
        Tile t10 = new Tile(3, Tile.Suit.SOUZU);
        Tile t11 = new Tile(3, Tile.Suit.SOUZU);
        Tile t12 = new Tile(Tile.Wind.WEST);
        Tile t13 = new Tile(Tile.Wind.WEST);

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13));

        ScoreCalculator sc = new ScoreCalculator(h, true);
        Assert.assertSame(1, sc.shanten );
    }

    @Test
    public void shantenChitoitsuTest2() {
        // Closed, single-suit
        // Shanten: 2
        Tile t1  = new Tile(1, Tile.Suit.MANZU);
        Tile t2  = new Tile(1, Tile.Suit.MANZU);
        Tile t3  = new Tile(1, Tile.Suit.MANZU);
        Tile t4  = new Tile(2, Tile.Suit.PINZU);
        Tile t5  = new Tile(3, Tile.Suit.PINZU);
        Tile t6  = new Tile(5, Tile.Suit.PINZU);
        Tile t7  = new Tile(5, Tile.Suit.PINZU);
        Tile t8  = new Tile(6, Tile.Suit.PINZU);
        Tile t9  = new Tile(6, Tile.Suit.PINZU);
        Tile t10 = new Tile(3, Tile.Suit.SOUZU);
        Tile t11 = new Tile(4, Tile.Suit.SOUZU);
        Tile t12 = new Tile(Tile.Wind.WEST);
        Tile t13 = new Tile(Tile.Wind.WEST);

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13));

        ScoreCalculator sc = new ScoreCalculator(h, true);
        Assert.assertSame(2, sc.shanten );
    }

    @Test
    public void shantenKokushiTest1() {
        // Closed, single-suit
        // Shanten: 1
        Tile t1  = new Tile(1, Tile.Suit.MANZU);
        Tile t2  = new Tile(1, Tile.Suit.MANZU);
        Tile t3  = new Tile(1, Tile.Suit.PINZU);
        Tile t4  = new Tile(9, Tile.Suit.PINZU);
        Tile t5  = new Tile(1, Tile.Suit.SOUZU);
        Tile t6  = new Tile(9, Tile.Suit.SOUZU);
        Tile t7  = new Tile(Tile.Wind.EAST);
        Tile t8  = new Tile(Tile.Wind.SOUTH);
        Tile t9  = new Tile(Tile.Wind.WEST);
        Tile t10 = new Tile(Tile.Wind.NORTH);
        Tile t11 = new Tile(Tile.Dragon.RED);
        Tile t12 = new Tile(Tile.Dragon.GREEN);
        Tile t13 = new Tile(Tile.Dragon.GREEN);

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13));

        ScoreCalculator sc = new ScoreCalculator(h, true);
        Assert.assertSame(1, sc.shanten );
    }

    @Test
    public void shantenKokushiTest2() {
        // Closed, single-suit
        // Shanten: 3
        Tile t1  = new Tile(1, Tile.Suit.MANZU);
        Tile t2  = new Tile(1, Tile.Suit.MANZU);
        Tile t3  = new Tile(1, Tile.Suit.PINZU);
        Tile t4  = new Tile(4, Tile.Suit.PINZU);
        Tile t5  = new Tile(1, Tile.Suit.SOUZU);
        Tile t6  = new Tile(2, Tile.Suit.SOUZU);
        Tile t7  = new Tile(Tile.Wind.EAST);
        Tile t8  = new Tile(Tile.Wind.SOUTH);
        Tile t9  = new Tile(Tile.Wind.WEST);
        Tile t10 = new Tile(Tile.Wind.NORTH);
        Tile t11 = new Tile(Tile.Dragon.RED);
        Tile t12 = new Tile(Tile.Dragon.GREEN);
        Tile t13 = new Tile(Tile.Dragon.GREEN);

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13));

        ScoreCalculator sc = new ScoreCalculator(h, true);
        Assert.assertSame(3, sc.shanten );
    }
}
