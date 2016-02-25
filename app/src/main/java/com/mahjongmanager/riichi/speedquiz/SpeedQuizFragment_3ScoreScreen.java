package com.mahjongmanager.riichi.speedquiz;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mahjongmanager.riichi.Hand;
import com.mahjongmanager.riichi.MainActivity;
import com.mahjongmanager.riichi.R;
import com.mahjongmanager.riichi.ScoreCalculator;

import java.util.ArrayList;
import java.util.List;

public class SpeedQuizFragment_3ScoreScreen extends Fragment implements View.OnClickListener {
    private TextView correctScoreLabel;
    private TextView newHighScore;
    private TextView incorrectScoreLabel;

    private LinearLayout incorrectButtonContainer;

    List<Hand> completeList = new ArrayList<>();
    private int correctGuesses   = 0;
    private int highScore        = 0;
    private int incorrectGuesses = 0;
    private List<Hand> handsToReview = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.fragment_speedquiz_3scorescreen, container, false);

        correctScoreLabel   = (TextView) myInflatedView.findViewById(R.id.correctScore);
        newHighScore        = (TextView) myInflatedView.findViewById(R.id.newHighScoreNotice);
        incorrectScoreLabel = (TextView) myInflatedView.findViewById(R.id.incorrectScore);
        incorrectButtonContainer = (LinearLayout) myInflatedView.findViewById(R.id.incorrectHandButtonContainer);

        loadHighScore();
        scoreGuesses();
        updateUI();

        return myInflatedView;
    }

    private void loadHighScore(){
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        highScore = sharedPref.getInt("SpeedQuizHighScore", 0);
    }

    private void scoreGuesses(){
        completeList = ((MainActivity)getActivity()).getScoredHands();
        List<int[]> guessList = ((MainActivity)getActivity()).getHanFuGuesses();

        for(int i=0; i<completeList.size(); i++){
            ScoreCalculator sc = new ScoreCalculator(completeList.get(i));
            if( judgeGuess(guessList.get(i), sc) ){
                correctGuesses++;
            } else {
                incorrectGuesses++;
                handsToReview.add(completeList.get(i));
            }
        }
        checkNewHighScore();
        createButtonsForIncorrectHands();
    }
    private boolean judgeGuess( int[] guess, ScoreCalculator sc ){
        int hanGuess = guess[0];
        int fuGuess = guess[1];
        boolean isManganOrMore = sc.scoreBasePoints(sc.validatedHand.han, sc.validatedHand.fu) >= 2000;
        Integer roundedFu = (sc.validatedHand.fu==25) ? 25 : (int) Math.ceil(sc.validatedHand.fu/10.0)*10;

        return hanGuess==sc.validatedHand.han && ( isManganOrMore || fuGuess==roundedFu);
    }
    private void checkNewHighScore(){
        if( correctGuesses>highScore ){
            newHighScore.setVisibility(View.VISIBLE);
            SharedPreferences settings = getActivity().getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("SpeedQuizHighScore", correctGuesses);
            editor.apply();
        }
    }
    private void createButtonsForIncorrectHands(){
        for( Hand h : handsToReview ){
            //add a button to incorrectButtonContainer
            Button newButt = new Button(getContext());
            String newButtText = "Review Hand #" + (completeList.indexOf(h)+1);
            newButt.setText(newButtText);
            newButt.setId(completeList.indexOf(h));
            newButt.setOnClickListener(this);

            incorrectButtonContainer.addView(newButt);
        }
    }

    @Override
    public void onClick(View v) {
        if( v.getId() < completeList.size() ){
            ((MainActivity)getActivity()).setCurrentHand(completeList.get(v.getId()));
            ((MainActivity)getActivity()).speedQuizReviewHand(v);
        }
    }

    private void updateUI(){
        correctScoreLabel.setText(String.valueOf(correctGuesses));
        incorrectScoreLabel.setText(String.valueOf(incorrectGuesses));
    }
}
