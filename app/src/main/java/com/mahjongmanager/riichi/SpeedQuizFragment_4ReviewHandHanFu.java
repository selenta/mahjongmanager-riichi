package com.mahjongmanager.riichi;

import android.annotation.SuppressLint;
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

import java.util.Arrays;

public class SpeedQuizFragment_4ReviewHandHanFu extends Fragment {

    private Hand actHand;
    private Integer actHandIdx;

    private TableLayout hanTable;
    private TableLayout fuTable;
    private TextView hanTotalLabel;
    private TextView fuTotalLabel;

    private TextView handNumber;
    private TextView playerGuessReminder;

    private TextView scoreBreakdown;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.fragment_speedquiz_4reviewhandhanfu, container, false);

        actHand = ((MainActivity)getActivity()).getCurrentHand();
        actHandIdx = ((MainActivity)getActivity()).getScoredHands().indexOf(actHand);
        Log.i("actHand", "actHand: " + actHand.toString());
        Log.i("actHandIdx", "actHandIdx: " + actHandIdx.toString());

        registerControls(myInflatedView);

        displayScores();

        return myInflatedView;
    }

    @SuppressLint("SetTextI18n")
    private void displayScores(){
        ScoreCalculator sc = new ScoreCalculator(actHand);
        Hand sh = sc.validatedHand;
        Integer roundedFu = (sh.fu==25) ? 25 : (int) Math.ceil(sh.fu/10.0)*10;
        Log.i("displayScore", "hanList: " + sh.hanList.toString());
        Log.i("displayScore", "fuList: " + sh.fuList.toString());

        String handNumbStr = "Hand #" + (actHandIdx+1);
        handNumber.setText(handNumbStr);
        int[] playerGuess = ((MainActivity)getActivity()).getHanFuGuesses().get(actHandIdx);
        Log.i("displayScore", "playerGuess: " + Arrays.toString(playerGuess));
        String playerGuessStr = "You guessed: "+playerGuess[0]+" Han "+playerGuess[1]+" Fu";
        playerGuessReminder.setText(playerGuessStr);

        hanTotalLabel.setText(sh.han.toString());
        fuTotalLabel.setText(sh.fu.toString()+" ("+roundedFu.toString()+")");

        for( String l : sh.hanList.keySet() ){
            addRow( l, sh.hanList.get(l).toString(), hanTable );
        }
        for( String l : sh.fuList.keySet() ){
            addRow( l, sh.fuList.get(l).toString(), fuTable );
        }

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
        } else if( sh.han>=13 && sh.han<26  ){
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
        hanTable      = (TableLayout) myInflatedView.findViewById(R.id.hanTable);
        fuTable       = (TableLayout) myInflatedView.findViewById(R.id.fuTable);
        hanTotalLabel = (TextView) myInflatedView.findViewById(R.id.hanTotalLabel);
        fuTotalLabel  = (TextView) myInflatedView.findViewById(R.id.fuTotalLabel);

        handNumber = (TextView) myInflatedView.findViewById(R.id.handNumber);
        playerGuessReminder  = (TextView) myInflatedView.findViewById(R.id.playerGuessReminder);

        scoreBreakdown = (TextView) myInflatedView.findViewById(R.id.scoreBreakdown);
    }
}
