package com.mahjongmanager.riichi.handcalculator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.mahjongmanager.riichi.Hand;
import com.mahjongmanager.riichi.components.HandDisplay;
import com.mahjongmanager.riichi.MainActivity;
import com.mahjongmanager.riichi.Meld;
import com.mahjongmanager.riichi.R;
import com.mahjongmanager.riichi.ScoreCalculator;
import com.mahjongmanager.riichi.Tile;

import java.util.Arrays;

public class HandCalculatorFragment_4ScoreScreen extends Fragment {

    private Hand actHand;

    private HandDisplay handDisplay;

    private TableLayout hanTable;
    private TableLayout fuTable;
    private TextView hanTotalLabel;
    private TextView fuTotalLabel;

    private TextView scoreBreakdown;
    private TextView scoreValue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.fragment_handcalculator_4scorescreen, container, false);

        actHand = ((MainActivity)getActivity()).getCurrentHand();
        Log.i("actHand", "actHand: " + actHand.toString());

        registerControls(myInflatedView);

        displayScores();
        handDisplay.setHand(actHand);

        return myInflatedView;
    }

    private void displayScores(){
        Hand sortedHand = new ScoreCalculator(actHand).scoredHand;
        updateRevealedStatus(sortedHand);
        ScoreCalculator sc = new ScoreCalculator(sortedHand);
        Hand sh = sc.validatedHand;
        if(sh==null){
            return;
        }
        actHand = sh;

        Integer roundedFu = (sh.fu==25) ? 25 : (int) Math.ceil(sh.fu/10.0)*10;
        Log.i("displayScore", "hanList: " + sh.hanList.toString());
        Log.i("displayScore", "fuList: " + sh.fuList.toString());

        hanTotalLabel.setText(sh.han.toString());
        fuTotalLabel.setText(sh.fu.toString()+" ("+roundedFu.toString()+")");

        for( String l : sh.hanList.keySet() ){
            addRow( l, sh.hanList.get(l).toString(), hanTable );
        }
        for( String l : sh.fuList.keySet() ){
            addRow( l, sh.fuList.get(l).toString(), fuTable );
        }


        String result = ScoreCalculator.scoreHanFu(sh.han, sh.fu, sh.playerWind== Tile.Wind.EAST, sh.tsumo);
        scoreValue.setText(result);

        //Do this second, so that we can make it a little more pretty
        String sBreakdown = sh.han.toString() + " Han " + roundedFu.toString() + " Fu";
        if( sh.han==5 || (sh.han==4&&sh.fu>=40) || (sh.han==3&&sh.fu>=70) ){
            sBreakdown = sBreakdown + " (Mangan)";
        } else if( sh.han==6 || sh.han==7 ){
            sBreakdown = sBreakdown + " (Haneman)";
        } else if( sh.han==8 || sh.han==9 || sh.han==10 ){
            sBreakdown = sBreakdown + " (Baiman)";
        } else if( sh.han==11|| sh.han==12 ){
            sBreakdown = sBreakdown + " (Sanbaiman)";
        } else if( sh.han>=13 && sh.han<26 ){
            sBreakdown = sBreakdown + " (Yakuman)";
        } else if( sh.han>=26 && sh.han<39 ){
            sBreakdown = sBreakdown + " (2x Yakuman)";
        } else if( sh.han>=39 && sh.han<52 ){
            sBreakdown = sBreakdown + " (3x Yakuman)";
        } else if( sh.han>=52 && sh.han<65 ){
            sBreakdown = sBreakdown + " (4x Yakuman)";
        } else if( sh.han>=65 ){
            sBreakdown = sBreakdown + " (5x Yakuman)";
        }
        scoreBreakdown.setText(sBreakdown);
    }

    /*
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



    private void addRow( String itemName, String value, TableLayout table ){
        TableRow newRow = new TableRow(getContext());
        TextView labelView = new TextView(getContext());
        TextView valueView = new TextView(getContext());

        LayoutParams Params1 = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1f);
        labelView.setLayoutParams(Params1);
        labelView.setPadding(20,0,0,0);
        LayoutParams Params2 = new TableRow.LayoutParams(80, LayoutParams.WRAP_CONTENT);
        valueView.setLayoutParams(Params2);

        labelView.setTextSize(16);
        valueView.setTextSize(16);

        newRow.addView(labelView);
        newRow.addView(valueView);

        labelView.setText(itemName);
        valueView.setText(value);

        switch (table.getId()){
            case R.id.hanTable:
                hanTable.addView(newRow, fuTable.getChildCount()-1);
                break;
            case R.id.fuTable:
                fuTable.addView(newRow, fuTable.getChildCount()-1);
                break;
        }
    }

    private void registerControls(View myInflatedView){
        handDisplay = (HandDisplay) myInflatedView.findViewById(R.id.handDisplay);

        hanTable      = (TableLayout) myInflatedView.findViewById(R.id.hanTable);
        fuTable       = (TableLayout) myInflatedView.findViewById(R.id.fuTable);
        hanTotalLabel = (TextView) myInflatedView.findViewById(R.id.hanTotalLabel);
        fuTotalLabel  = (TextView) myInflatedView.findViewById(R.id.fuTotalLabel);

        scoreValue = (TextView) myInflatedView.findViewById(R.id.scoreValue);
        scoreBreakdown = (TextView) myInflatedView.findViewById(R.id.scoreBreakdown);
    }
}
