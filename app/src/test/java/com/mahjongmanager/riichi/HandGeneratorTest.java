package com.mahjongmanager.riichi;

import android.util.Log;

import org.junit.Test;

import java.util.ArrayList;

public class HandGeneratorTest {

    @Test
    public void generatorTest1(){
        HandGenerator hg = new HandGenerator();

        Hand h = new Hand(new ArrayList<Tile>());
        hg.addChiitoitsu(h);
        Log.d("generatorTest1", "Chiitoitsu: " + h.toString());
        hg.addTanyaoToChiitoitsu(h);
        Log.d("generatorTest1", "+Tanyao: " + h.toString());
        hg.addHonitsuToChiitoitsu(h);
        Log.d("generatorTest1", "+Honitsu: " + h.toString());
        hg.addHonroutouToChiitoitsu(h);
        Log.d("generatorTest1", "+Honroutou: " + h.toString());
        hg.addChinitsuToChiitoitsu(h);
        Log.d("generatorTest1", "+Chinitsu: " + h.toString());
    }

    @Test
    public void completelyRandomHand(){
        Hand h = new Hand(new ArrayList<Tile>());

        while( h.han==0 ){
            HandGenerator hg = new HandGenerator();
            Hand hTemp = hg.completelyRandomHand();
            hg.addOtherYaku(hTemp);

            ScoreCalculator sc = new ScoreCalculator(hTemp);
            if( sc.validatedHand!=null ){
                h = sc.validatedHand;
            }
        }

        Log.d("completelyRandomHand", "hand: " + h.toString());
    }
}
