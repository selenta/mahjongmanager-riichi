package com.mahjongmanager.riichi;

import com.mahjongmanager.riichi.common.Hand;
import com.mahjongmanager.riichi.common.Tile;
import com.mahjongmanager.riichi.utils.Utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class TenpaiWaitTest {
    // Good list of complex waits: http://arcturus.su/wiki/Complex_waits

    @Test
    public void tenpaiTest1() {
        // Pair wait
        // Wait: tsumo - 2/5 pin - 3h 30f
        // Wait: ron   - 2/5 pin - 2h 40f
        Tile t1  = new Tile(1, Tile.Suit.MANZU);
        Tile t2  = new Tile(2, Tile.Suit.MANZU);
        Tile t3  = new Tile(3, Tile.Suit.MANZU);
        Tile t4  = new Tile(4, Tile.Suit.MANZU);
        Tile t5  = new Tile(5, Tile.Suit.MANZU);
        Tile t6  = new Tile(6, Tile.Suit.MANZU);
        Tile t7  = new Tile(7, Tile.Suit.MANZU);
        Tile t8  = new Tile(8, Tile.Suit.MANZU);
        Tile t9  = new Tile(9, Tile.Suit.MANZU);
        Tile t10 = new Tile(2, Tile.Suit.PINZU);
        Tile t11 = new Tile(3, Tile.Suit.PINZU);
        Tile t12 = new Tile(4, Tile.Suit.PINZU);
        Tile t13 = new Tile(5, Tile.Suit.PINZU);

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13));

        ScoreCalculator sc = new ScoreCalculator(h, true);
        Assert.assertSame(0, sc.shanten );

        Assert.assertTrue( sc.waits.size()==2 );
        for(ScoreCalculator.Wait wait : sc.waits){
            if( wait.han==3 && wait.fu==30 && wait.isTsumo ){
                Assert.assertTrue(wait.tiles.contains( t10 ));
                Assert.assertTrue(wait.tiles.contains( t13 ));
            } else if( wait.han==2 && wait.fu==40 && !wait.isTsumo ){
                Assert.assertTrue(wait.tiles.contains( t10 ));
                Assert.assertTrue(wait.tiles.contains( t13 ));
            } else {
                Assert.fail();
            }
        }
    }

    @Test
    public void tenpaiTest2() {
        // Two-Pair wait
        // Wait: tsumo - 3/6 pin - 2h 30f
        // Wait: ron   - 3/6 pin - 1h 40f
        Tile t1  = new Tile(2, Tile.Suit.MANZU);
        Tile t2  = new Tile(3, Tile.Suit.MANZU);
        Tile t3  = new Tile(4, Tile.Suit.MANZU);
        Tile t4  = new Tile(4, Tile.Suit.MANZU);
        Tile t5  = new Tile(5, Tile.Suit.MANZU);
        Tile t6  = new Tile(6, Tile.Suit.MANZU);
        Tile t7  = new Tile(3, Tile.Suit.PINZU);
        Tile t8  = new Tile(3, Tile.Suit.PINZU);
        Tile t9  = new Tile(3, Tile.Suit.PINZU);
        Tile t10 = new Tile(4, Tile.Suit.PINZU);
        Tile t11 = new Tile(5, Tile.Suit.PINZU);
        Tile t12 = new Tile(6, Tile.Suit.PINZU);
        Tile t13 = new Tile(6, Tile.Suit.PINZU);

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13));

        ScoreCalculator sc = new ScoreCalculator(h, true);
        Assert.assertSame(0, sc.shanten );

        Assert.assertTrue( sc.waits.size()==2 );
        for(ScoreCalculator.Wait wait : sc.waits){
            if( wait.han==2 && wait.fu==30 && wait.isTsumo ){
                Assert.assertTrue(wait.tiles.contains( t7 ));
                Assert.assertTrue(wait.tiles.contains( t13 ));
            } else if( wait.han==1 && wait.fu==40 && !wait.isTsumo ){
                Assert.assertTrue(wait.tiles.contains( t7 ));
                Assert.assertTrue(wait.tiles.contains( t13 ));
            } else {
                Assert.fail();
            }
        }
    }

    @Test
    public void tenpaiTest3() {
        // Three-way wait
        // Wait: tsumo - 3/6 man - 2h 20f
        // Wait: tsumo -   9 man - 4h 20f
        // Wait: ron   - 3/6 man - 1h 30f
        // Wait: ron   -   9 man - 3h 30f
        Tile t1  = new Tile(1, Tile.Suit.MANZU);
        Tile t2  = new Tile(2, Tile.Suit.MANZU);
        Tile t3  = new Tile(3, Tile.Suit.MANZU);
        Tile t4  = new Tile(4, Tile.Suit.MANZU);
        Tile t5  = new Tile(5, Tile.Suit.MANZU);
        Tile t6  = new Tile(6, Tile.Suit.MANZU);
        Tile t7  = new Tile(7, Tile.Suit.MANZU);
        Tile t8  = new Tile(8, Tile.Suit.MANZU);
        Tile t9  = new Tile(3, Tile.Suit.PINZU);
        Tile t10 = new Tile(4, Tile.Suit.PINZU);
        Tile t11 = new Tile(5, Tile.Suit.PINZU);
        Tile t12 = new Tile(6, Tile.Suit.PINZU);
        Tile t13 = new Tile(6, Tile.Suit.PINZU);

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13));

        ScoreCalculator sc = new ScoreCalculator(h, true);
        Assert.assertSame(0, sc.shanten );

        Assert.assertTrue( sc.waits.size()==4 );
        for(ScoreCalculator.Wait wait : sc.waits){
            if( wait.han==2 && wait.fu==20 && wait.isTsumo ){
                Assert.assertTrue(wait.tiles.contains( t3 ));
                Assert.assertTrue(wait.tiles.contains( t6 ));
            } else if( wait.han==4 && wait.fu==20 && wait.isTsumo ){
                Assert.assertTrue(wait.tiles.contains( new Tile(9, Tile.Suit.MANZU) ));
            } else if( wait.han==1 && wait.fu==30 && !wait.isTsumo ){
                Assert.assertTrue(wait.tiles.contains( t3 ));
                Assert.assertTrue(wait.tiles.contains( t6 ));
            } else if( wait.han==3 && wait.fu==30 && !wait.isTsumo ){
                Assert.assertTrue(wait.tiles.contains( new Tile(9, Tile.Suit.MANZU) ));
            } else {
                Assert.fail();
            }
        }
    }

    @Test
    public void tenpaiTest4() {
        // Four-way wait
        // Wait: tsumo - 3/6/9 man/south - 5h 40f
        // Wait: ron   - 3/6/9 man/south - 4h 50f
        Tile t1  = new Tile(3, Tile.Suit.MANZU);
        Tile t2  = new Tile(3, Tile.Suit.MANZU);
        Tile t3  = new Tile(3, Tile.Suit.MANZU);
        Tile t4  = new Tile(4, Tile.Suit.MANZU);
        Tile t5  = new Tile(5, Tile.Suit.MANZU);
        Tile t6  = new Tile(6, Tile.Suit.MANZU);
        Tile t7  = new Tile(7, Tile.Suit.MANZU);
        Tile t8  = new Tile(8, Tile.Suit.MANZU);
        Tile t9  = new Tile(Tile.Wind.SOUTH);
        Tile t10 = new Tile(Tile.Wind.SOUTH);
        Tile t11 = new Tile(Tile.Dragon.GREEN);
        Tile t12 = new Tile(Tile.Dragon.GREEN);
        Tile t13 = new Tile(Tile.Dragon.GREEN);

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13));

        ScoreCalculator sc = new ScoreCalculator(h, true);
        Assert.assertSame(0, sc.shanten );

        Assert.assertTrue( sc.waits.size()==2 );
        for(ScoreCalculator.Wait wait : sc.waits){
            if( wait.han==5 && wait.fu==40 && wait.isTsumo ){
                Assert.assertTrue(wait.tiles.contains( t3 ));
                Assert.assertTrue(wait.tiles.contains( t6 ));
                Assert.assertTrue(wait.tiles.contains( new Tile(9, Tile.Suit.MANZU) ));
                Assert.assertTrue(wait.tiles.contains( t9 ));
            } else if( wait.han==4 && wait.fu==50 && !wait.isTsumo ){
                Assert.assertTrue(wait.tiles.contains( t3 ));
                Assert.assertTrue(wait.tiles.contains( t6 ));
                Assert.assertTrue(wait.tiles.contains( new Tile(9, Tile.Suit.MANZU) ));
                Assert.assertTrue(wait.tiles.contains( t9 ));
            } else {
                Assert.fail();
            }
        }
    }

    @Test
    public void tenpaiTest5() {
        // Seven-way wait (yes way!)
        // Wait: tsumo - 1/2/4/5/6/8/9 man - 5h 40f
        // Wait: ron   - 1/2/4/5/6/8/9 man - 4h 50f
        Tile t1  = new Tile(2, Tile.Suit.MANZU);
        Tile t2  = new Tile(3, Tile.Suit.MANZU);
        Tile t3  = new Tile(3, Tile.Suit.MANZU);
        Tile t4  = new Tile(3, Tile.Suit.MANZU);
        Tile t5  = new Tile(3, Tile.Suit.MANZU);
        Tile t6  = new Tile(4, Tile.Suit.MANZU);
        Tile t7  = new Tile(5, Tile.Suit.MANZU);
        Tile t8  = new Tile(6, Tile.Suit.MANZU);
        Tile t9  = new Tile(7, Tile.Suit.MANZU);
        Tile t10 = new Tile(7, Tile.Suit.MANZU);
        Tile t11 = new Tile(7, Tile.Suit.MANZU);
        Tile t12 = new Tile(7, Tile.Suit.MANZU);
        Tile t13 = new Tile(8, Tile.Suit.MANZU);

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13));

        ScoreCalculator sc = new ScoreCalculator(h, true);
        Assert.assertSame(0, sc.shanten );

        Assert.assertTrue( sc.waits.size()==5 );
        for(ScoreCalculator.Wait wait : sc.waits){
            if( wait.han==7 && wait.fu==30 && wait.isTsumo ){
                Assert.assertTrue(wait.tiles.contains( new Tile(1, Tile.Suit.MANZU) ));
                Assert.assertTrue(wait.tiles.contains( new Tile(9, Tile.Suit.MANZU) ));
            } else if( wait.han==8 && wait.fu==30 && wait.isTsumo ){
                Assert.assertTrue(wait.tiles.contains( t6 ));
                Assert.assertTrue(wait.tiles.contains( t8));
            } else if( wait.han==8 && wait.fu==40 && wait.isTsumo ){
                Assert.assertTrue(wait.tiles.contains( t1 ));
                Assert.assertTrue(wait.tiles.contains( t7 ));
                Assert.assertTrue(wait.tiles.contains( t13 ));
            } else if( wait.han==6 && wait.fu==40 && !wait.isTsumo ){
                Assert.assertTrue(wait.tiles.contains( new Tile(1, Tile.Suit.MANZU) ));
                Assert.assertTrue(wait.tiles.contains( new Tile(9, Tile.Suit.MANZU) ));
            } else if( wait.han==7 && wait.fu==40 && !wait.isTsumo ){
                Assert.assertTrue(wait.tiles.contains( t1 ));
                Assert.assertTrue(wait.tiles.contains( t6 ));
                Assert.assertTrue(wait.tiles.contains( t7 ));
                Assert.assertTrue(wait.tiles.contains( t8));
                Assert.assertTrue(wait.tiles.contains( t13 ));
            } else {
                Assert.fail();
            }
        }
    }
}
