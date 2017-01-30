package com.mahjongmanager.riichi;

import com.mahjongmanager.riichi.common.Hand;
import com.mahjongmanager.riichi.utils.ExampleHands;

import junit.framework.Assert;

import org.junit.Test;

public class ExampleHandsTest {

    @Test
    public void chiitoitsuHandTest(){
        Hand h = ExampleHands.getChiitoitsuHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.chiiToitsu);
        Assert.assertEquals(1, h.hanList.size());
    }

    @Test
    public void pinfuHandTest(){
        Hand h = ExampleHands.getPinfuHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.pinfu);
        Assert.assertEquals(1, h.hanList.size());
    }

    @Test
    public void iipeikouHandTest(){
        Hand h = ExampleHands.getIipeikouHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.iipeikou);
        Assert.assertEquals(1, h.hanList.size());
    }

    @Test
    public void sanshokuDoujunHandTest(){
        Hand h = ExampleHands.getSanshokuDoujunHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.sanshokuDoujun);
        Assert.assertEquals(1, h.hanList.size());
    }

    @Test
    public void ittsuuHandTest(){
        Hand h = ExampleHands.getIttsuuHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.ittsuu);
        Assert.assertEquals(1, h.hanList.size());
    }

    @Test
    public void ryanpeikouHandTest(){
        Hand h = ExampleHands.getRyanpeikouHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.ryanpeikou);
        Assert.assertEquals(1, h.hanList.size());
    }

    @Test
    public void toitoiHandTest(){
        Hand h = ExampleHands.getToitoiHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.toitoi);
        Assert.assertEquals(1, h.hanList.size());
    }

    @Test
    public void sanAnkouHandTest(){
        Hand h = ExampleHands.getSanAnkouHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.sanAnkou);
        Assert.assertEquals(1, h.hanList.size());
    }

    @Test
    public void sanshokuDoukouHandTest(){
        Hand h = ExampleHands.getSanshokuDoukouHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.sanshokuDoukou);
        Assert.assertEquals(1, h.hanList.size());
    }

    @Test
    public void sanKantsuHandTest(){
        Hand h = ExampleHands.getSanKantsuHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.sanKantsu);
        Assert.assertEquals(1, h.hanList.size());
    }

    @Test
    public void tanyaoHandTest(){
        Hand h = ExampleHands.getTanyaoHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.tanyao);
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
        Assert.assertTrue(h.chanta);
        Assert.assertEquals(1, h.hanList.size());
    }

    @Test
    public void junchanHandTest(){
        Hand h = ExampleHands.getJunchanHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.junchan);
        Assert.assertEquals(1, h.hanList.size());
    }

    @Test
    public void honroutouHandTest(){
        Hand h = ExampleHands.getHonroutouHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.honroutou);
        Assert.assertTrue(h.toitoi);
        Assert.assertEquals(2, h.hanList.size());
    }

    @Test
    public void honroutouChiitoitsuHandTest(){
        Hand h = ExampleHands.getHonroutouChiitoitsuHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.chiiToitsu);
        Assert.assertTrue(h.honroutou);
        Assert.assertEquals(2, h.hanList.size());
    }

    @Test
    public void shousangenHandTest(){
        Hand h = ExampleHands.getShousangenHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.shousangen);
        Assert.assertEquals(2, h.hanList.size());
    }

    @Test
    public void honitsuHandTest(){
        Hand h = ExampleHands.getHonitsuHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.honitsu);
        Assert.assertEquals(1, h.hanList.size());
    }

    @Test
    public void chinitsuHandTest(){
        Hand h = ExampleHands.getChinitsuHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.chinitsu);
        Assert.assertEquals(1, h.hanList.size());
    }

    @Test
    public void kokushiMusouHandTest(){
        Hand h = ExampleHands.getKokushiMusouHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.kokushiMusou);
        Assert.assertEquals(1, h.hanList.size());
    }

    @Test
    public void kokushiMusou13SidedHandTest(){
        Hand h = ExampleHands.getKokushiMusou13SidedHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.kokushiMusou13wait);
        Assert.assertEquals(1, h.hanList.size());
    }

    @Test
    public void suuAnkouHandTest(){
        Hand h = ExampleHands.getSuuAnkouHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.suuAnkou);
        Assert.assertEquals(1, h.hanList.size());
    }

    @Test
    public void suuAnkouTankiHandTest(){
        Hand h = ExampleHands.getSuuAnkouTankiHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.suuAnkouTanki);
        Assert.assertEquals(1, h.hanList.size());
    }

    @Test
    public void daisangenHandTest(){
        Hand h = ExampleHands.getDaisangenHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.daisangen);
        Assert.assertEquals(1, h.hanList.size());
    }

    @Test
    public void shousuushiiHandTest(){
        Hand h = ExampleHands.getShousuushiiHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.shousuushii);
        Assert.assertEquals(1, h.hanList.size());
    }

    @Test
    public void daisuushiiHandTest(){
        Hand h = ExampleHands.getDaisuushiiHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.daisuushii);
        Assert.assertEquals(1, h.hanList.size());
    }

    @Test
    public void tsuuiisouHandTest(){
        Hand h = ExampleHands.getTsuuiisouHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.tsuuiisou);
        Assert.assertEquals(1, h.hanList.size());
    }

    @Test
    public void daichiseiHandTest(){
        Hand h = ExampleHands.getDaichiseiHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.daichisei);
        Assert.assertEquals(1, h.hanList.size());
    }

    @Test
    public void chinroutouHandTest(){
        Hand h = ExampleHands.getChinroutouHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.chinroutou);
        Assert.assertEquals(1, h.hanList.size());
    }

    @Test
    public void ryuuiisouHandTest(){
        Hand h = ExampleHands.getRyuuiisouHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.ryuuiisou);
        Assert.assertEquals(1, h.hanList.size());
    }

    @Test
    public void chuurenPoutouHandTest(){
        Hand h = ExampleHands.getChuurenPoutouHand();
        Assert.assertNotNull(h);
        Assert.assertTrue(h.chuurenPoutou);
        Assert.assertEquals(1, h.hanList.size());
    }

    // TODO turn on this test after implementing 9-sided wait
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
        Assert.assertTrue(h.suuKantsu);
        Assert.assertEquals(1, h.hanList.size());
    }
}
