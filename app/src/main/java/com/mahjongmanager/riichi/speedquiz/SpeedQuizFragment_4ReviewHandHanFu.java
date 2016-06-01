package com.mahjongmanager.riichi.speedquiz;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mahjongmanager.riichi.Hand;
import com.mahjongmanager.riichi.components.HandDisplay;
import com.mahjongmanager.riichi.MainActivity;
import com.mahjongmanager.riichi.R;
import com.mahjongmanager.riichi.ScoreCalculator;
import com.mahjongmanager.riichi.components.ScoreScreen;
import com.mahjongmanager.riichi.utils.Log;

import java.util.Arrays;

public class SpeedQuizFragment_4ReviewHandHanFu extends Fragment {

    private Hand actHand;
    private Integer actHandIdx;

    private HandDisplay handDisplay;
    private ScoreScreen scoreScreen;

    private TextView handNumber;
    private TextView playerGuessReminder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.fragment_speedquiz_4reviewhandhanfu, container, false);

        actHand = ((MainActivity)getActivity()).getCurrentHand();
        actHandIdx = ((MainActivity)getActivity()).getScoredHands().indexOf(actHand);
        Log.i("actHand", "actHand: " + actHand.toString());
        Log.i("actHandIdx", "actHandIdx: " + actHandIdx.toString());

        registerControls(myInflatedView);
        handDisplay.setHand(actHand);
        scoreScreen.setHand(actHand);
        scoreScreen.hideScoreTotal();

        displayScores();

        return myInflatedView;
    }

    private void displayScores(){
        ScoreCalculator sc = new ScoreCalculator(actHand);
        Hand sh = sc.validatedHand;
        Log.i("displayScore", "hanList: " + sh.hanList.toString());
        Log.i("displayScore", "fuList: " + sh.fuList.toString());

        String handNumbStr = "Hand #" + (actHandIdx+1);
        handNumber.setText(handNumbStr);
        int[] playerGuess = ((MainActivity)getActivity()).getHanFuGuesses().get(actHandIdx);
        Log.i("displayScore", "playerGuess: " + Arrays.toString(playerGuess));
        String playerGuessStr = "You guessed: "+playerGuess[0]+" Han "+playerGuess[1]+" Fu";
        playerGuessReminder.setText(playerGuessStr);
    }

    private void registerControls(View myInflatedView){
        handDisplay = (HandDisplay) myInflatedView.findViewById(R.id.handDisplay);
        scoreScreen = (ScoreScreen) myInflatedView.findViewById(R.id.scoreScreen);

        handNumber = (TextView) myInflatedView.findViewById(R.id.handNumber);
        playerGuessReminder  = (TextView) myInflatedView.findViewById(R.id.playerGuessReminder);
    }
}
