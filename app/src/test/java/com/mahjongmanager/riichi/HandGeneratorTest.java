package com.mahjongmanager.riichi;

import android.util.Log;

import com.mahjongmanager.riichi.common.Hand;
import com.mahjongmanager.riichi.common.Tile;
import com.mahjongmanager.riichi.utils.HandGenerator;

import org.junit.Test;

import java.util.ArrayList;

public class HandGeneratorTest {

    // Chiitoitsu hands are never created directly
//    @Test
//    public void generatorTest1(){
//        HandGenerator hg = new HandGenerator();
//
//        Hand h = new Hand(new ArrayList<Tile>());
//        hg.addChiitoitsu(h);
//        Log.d("generatorTest1", "Chiitoitsu: " + h.toString());
//        hg.restrictFoundersToTanyao(h);
//        Log.d("generatorTest1", "+Tanyao: " + h.toString());
//        hg.addHonitsuToChiitoitsu(h);
//        Log.d("generatorTest1", "+Honitsu: " + h.toString());
//        hg.restrictFoundersToHonroutou(h);
//        Log.d("generatorTest1", "+Honroutou: " + h.toString());
//        hg.addChinitsuToChiitoitsu(h);
//        Log.d("generatorTest1", "+Chinitsu: " + h.toString());
//    }

    @Test
    public void completelyRandomHand(){
        for(int i=0; i<30; i++){
            Hand h = new Hand(new ArrayList<Tile>());

            while( h.han==0 ){
                HandGenerator hg = new HandGenerator();
                Hand hTemp = hg.completelyRandomHand();
                hg.addSituationalYaku(hTemp);

                ScoreCalculator sc = new ScoreCalculator(hTemp);
                if( sc.validatedHand!=null ){
                    h = sc.validatedHand;
                }
            }

            Log.d("completelyRandomHand", "hand: " + h.toString());
        }
    }
}
