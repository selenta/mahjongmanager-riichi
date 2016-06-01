package com.mahjongmanager.riichi.handcalculator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mahjongmanager.riichi.Hand;
import com.mahjongmanager.riichi.components.HandDisplay;
import com.mahjongmanager.riichi.MainActivity;
import com.mahjongmanager.riichi.Meld;
import com.mahjongmanager.riichi.R;
import com.mahjongmanager.riichi.ScoreCalculator;
import com.mahjongmanager.riichi.Tile;
import com.mahjongmanager.riichi.components.ScoreScreen;
import com.mahjongmanager.riichi.utils.Log;

import java.util.Arrays;

public class HandCalculatorFragment_4ScoreScreen extends Fragment {

    private Hand actHand;

    private HandDisplay handDisplay;
    private ScoreScreen scoreScreen;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.fragment_handcalculator_4scorescreen, container, false);
        handDisplay = (HandDisplay) myInflatedView.findViewById(R.id.handDisplay);
        scoreScreen = (ScoreScreen) myInflatedView.findViewById(R.id.scoreScreen);

        actHand = ((MainActivity)getActivity()).getCurrentHand();
        Log.i("actHand", "actHand: " + actHand.toString());
        cleanupAndScoreHand();

        handDisplay.setHand(actHand);
        scoreScreen.setHand(actHand);

        return myInflatedView;
    }

    private void cleanupAndScoreHand(){
        Hand sortedHand = new ScoreCalculator(actHand).scoredHand;

        updateRevealedStatus(sortedHand);

        ScoreCalculator sc = new ScoreCalculator(sortedHand);
        actHand = sc.validatedHand;
    }
    /**
     * In an attempt to minimize the number of user inputs during the HandCalculator, some
     * information has to be inferred, such as the precise revealed status of the tiles. It
     * doesn't really matter (for the purposes of scoring) whether it's an OpenKan or an AddedKan
     * or who you called the tile from. Either way, a hand won't validate without consistent
     * RevealedStatus info, so we need to just make something up so it'll pass validation.
     */
    private void updateRevealedStatus(Hand h){
        for(Meld m : Arrays.asList(h.meld1, h.meld2, h.meld3, h.meld4)){

            Tile calledTile = m.getCalledTile();
            if( calledTile!=null && calledTile.winningTile ) {
                continue;
            }

            Tile.RevealedState newState = Tile.RevealedState.NONE;
            if( m.isKan() && calledTile==null ){
                newState = Tile.RevealedState.CLOSEDKAN;
            } else if( m.isKan() && calledTile!=null ){
                newState = Tile.RevealedState.OPENKAN;
            } else if( m.isChii() && calledTile!=null ){
                newState = Tile.RevealedState.CHI;
            } else if( m.size()==3 && calledTile!=null ){
                newState = Tile.RevealedState.PON;
            }

            for(Tile t : m.getTiles()){
                t.revealedState = newState;
            }
        }
    }
}
