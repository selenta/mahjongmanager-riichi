package com.mahjongmanager.riichi;

import com.mahjongmanager.riichi.common.Hand;
import com.mahjongmanager.riichi.common.Yaku;
import com.mahjongmanager.riichi.utils.ExampleHands;

import junit.framework.Assert;

import org.junit.Test;

public class ExampleHandsTest {

    @Test
    public void chiitoitsuHandTest(){
        Hand h = ExampleHands.getChiitoitsuHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.hasYaku(Yaku.Name.CHIITOITSU));
        Assert.assertEquals(1, h.hanList.size());
    }

    @Test
    public void pinfuHandTest(){
        Hand h = ExampleHands.getPinfuHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.hasYaku(Yaku.Name.PINFU));
        Assert.assertEquals(1, h.hanList.size());
    }

    @Test
    public void iipeikouHandTest(){
        Hand h = ExampleHands.getIipeikouHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.hasYaku(Yaku.Name.IIPEIKOU));
        Assert.assertEquals(1, h.hanList.size());
    }

    @Test
    public void sanshokuDoujunHandTest(){
        Hand h = ExampleHands.getSanshokuDoujunHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.hasYaku(Yaku.Name.SANSHOKUDOUJUN));
        Assert.assertEquals(1, h.hanList.size());
    }

    @Test
    public void ittsuuHandTest(){
        Hand h = ExampleHands.getIttsuuHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.hasYaku(Yaku.Name.ITTSUU));
        Assert.assertEquals(1, h.hanList.size());
    }

    @Test
    public void ryanpeikouHandTest(){
        Hand h = ExampleHands.getRyanpeikouHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.hasYaku(Yaku.Name.RYANPEIKOU));
        Assert.assertEquals(1, h.hanList.size());
    }

    @Test
    public void toitoiHandTest(){
        Hand h = ExampleHands.getToitoiHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.hasYaku(Yaku.Name.TOITOI));
        Assert.assertEquals(1, h.hanList.size());
    }

    @Test
    public void sanAnkouHandTest(){
        Hand h = ExampleHands.getSanAnkouHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.hasYaku(Yaku.Name.SANANKOU));
        Assert.assertEquals(1, h.hanList.size());
    }

    @Test
    public void sanshokuDoukouHandTest(){
        Hand h = ExampleHands.getSanshokuDoukouHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.hasYaku(Yaku.Name.SANSHOKUDOUKOU));
        Assert.assertEquals(1, h.hanList.size());
    }

    @Test
    public void sanKantsuHandTest(){
        Hand h = ExampleHands.getSanKantsuHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.hasYaku(Yaku.Name.SANKANTSU));
        Assert.assertEquals(1, h.hanList.size());
    }

    @Test
    public void tanyaoHandTest(){
        Hand h = ExampleHands.getTanyaoHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.hasYaku(Yaku.Name.TANYAO));
        Assert.assertEquals(1, h.hanList.size());
    }

    @Test
    public void yakuhaiHandTest(){
        Hand h = ExampleHands.getYakuhaiHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.hasYakuhai());
        Assert.assertEquals(1, h.hanList.size());
    }

    @Test
    public void chantaHandTest(){
        Hand h = ExampleHands.getChantaHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.hasYaku(Yaku.Name.CHANTA));
        Assert.assertEquals(1, h.hanList.size());
    }

    @Test
    public void junchanHandTest(){
        Hand h = ExampleHands.getJunchanHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.hasYaku(Yaku.Name.JUNCHAN));
        Assert.assertEquals(1, h.hanList.size());
    }

    @Test
    public void honroutouHandTest(){
        Hand h = ExampleHands.getHonroutouHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.hasYaku(Yaku.Name.HONROUTOU));
        Assert.assertTrue(h.hasYaku(Yaku.Name.TOITOI));
        Assert.assertEquals(2, h.hanList.size());
    }

    @Test
    public void honroutouChiitoitsuHandTest(){
        Hand h = ExampleHands.getHonroutouChiitoitsuHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.hasYaku(Yaku.Name.CHIITOITSU));
        Assert.assertTrue(h.hasYaku(Yaku.Name.HONROUTOU));
        Assert.assertEquals(2, h.hanList.size());
    }

    @Test
    public void shousangenHandTest(){
        Hand h = ExampleHands.getShousangenHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.hasYaku(Yaku.Name.SHOUSANGEN));
        Assert.assertEquals(2, h.hanList.size());
    }

    @Test
    public void honitsuHandTest(){
        Hand h = ExampleHands.getHonitsuHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.hasYaku(Yaku.Name.HONITSU));
        Assert.assertEquals(1, h.hanList.size());
    }

    @Test
    public void chinitsuHandTest(){
        Hand h = ExampleHands.getChinitsuHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.hasYaku(Yaku.Name.CHINITSU));
        Assert.assertEquals(1, h.hanList.size());
    }

    @Test
    public void kokushiMusouHandTest(){
        Hand h = ExampleHands.getKokushiMusouHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.hasYaku(Yaku.Name.KOKUSHIMUSOU));
        Assert.assertEquals(1, h.hanList.size());
    }

    @Test
    public void kokushiMusou13SidedHandTest(){
        Hand h = ExampleHands.getKokushiMusou13SidedHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.hasYaku(Yaku.Name.KOKUSHIMUSOU13SIDED));
        Assert.assertEquals(1, h.hanList.size());
    }

    @Test
    public void suuAnkouHandTest(){
        Hand h = ExampleHands.getSuuAnkouHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.hasYaku(Yaku.Name.SUUANKOU));
        Assert.assertEquals(1, h.hanList.size());
    }

    @Test
    public void suuAnkouTankiHandTest(){
        Hand h = ExampleHands.getSuuAnkouTankiHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.hasYaku(Yaku.Name.SUUANKOUTANKI));
        Assert.assertEquals(1, h.hanList.size());
    }

    @Test
    public void daisangenHandTest(){
        Hand h = ExampleHands.getDaisangenHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.hasYaku(Yaku.Name.DAISANGEN));
        Assert.assertEquals(1, h.hanList.size());
    }

    @Test
    public void shousuushiiHandTest(){
        Hand h = ExampleHands.getShousuushiiHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.hasYaku(Yaku.Name.SHOUSUUSHII));
        Assert.assertEquals(1, h.hanList.size());
    }

    @Test
    public void daisuushiiHandTest(){
        Hand h = ExampleHands.getDaisuushiiHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.hasYaku(Yaku.Name.DAISUUSHII));
        Assert.assertEquals(1, h.hanList.size());
    }

    @Test
    public void tsuuiisouHandTest(){
        Hand h = ExampleHands.getTsuuiisouHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.hasYaku(Yaku.Name.TSUUIISOU));
        Assert.assertEquals(1, h.hanList.size());
    }

    @Test
    public void daichiseiHandTest(){
        Hand h = ExampleHands.getDaichiseiHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.hasYaku(Yaku.Name.DAICHISEI));
        Assert.assertEquals(1, h.hanList.size());
    }

    @Test
    public void chinroutouHandTest(){
        Hand h = ExampleHands.getChinroutouHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.hasYaku(Yaku.Name.CHINROUTOU));
        Assert.assertEquals(1, h.hanList.size());
    }

    @Test
    public void ryuuiisouHandTest(){
        Hand h = ExampleHands.getRyuuiisouHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.hasYaku(Yaku.Name.RYUUIISOU));
        Assert.assertEquals(1, h.hanList.size());
    }

    @Test
    public void chuurenPoutouHandTest(){
        Hand h = ExampleHands.getChuurenPoutouHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.hasYaku(Yaku.Name.CHUURENPOUTOU));
        Assert.assertEquals(1, h.hanList.size());
    }

// turn on this test after implementing 9-sided wait
//    @Test
//    public void chuurenPoutou9SidedHandTest(){
//        Hand h = ExampleHands.getChuurenPoutou9SidedHand();
//        Assert.assertNotNull(h);
//        Assert.assertTrue(h.chuurenPoutou9wait);
//        Assert.assertEquals(1, h.hanList.size());
//    }

    @Test
    public void suuKantsuHandTest(){
        Hand h = ExampleHands.getSuuKantsuHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.hasYaku(Yaku.Name.SUUKANTSU));
        Assert.assertEquals(1, h.hanList.size());
    }
}
