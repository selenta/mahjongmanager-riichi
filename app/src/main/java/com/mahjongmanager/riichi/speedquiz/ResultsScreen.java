package com.mahjongmanager.riichi.speedquiz;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
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

public class ResultsScreen extends Fragment implements View.OnClickListener {
    private TextView correctScoreLabel;
    private TextView newHighScore;
    private TextView incorrectScoreLabel;
    private Button mainMenuButton;

    private LinearLayout incorrectButtonContainer;

    private List<Hand> completeList = new ArrayList<>();
    private int correctGuesses   = 0;
    private int incorrectGuesses = 0;
    private List<Hand> handsToReview = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.fragment_speedquiz_3scorescreen, container, false);
        registerUI(myInflatedView);

        // If a user got here form pressing Back the variables are correct, just need to update UI
        if( completeList.isEmpty() ) {
            scoreGuesses();
        }
        updateDisplayedScores();
        checkNewHighScore();
        createButtonsForIncorrectHands();
        createMainMenuButtonTimer();

        return myInflatedView;
    }

    private void scoreGuesses(){
        completeList = ((MainActivity)getActivity()).getScoredHands();
        List<int[]> guessList = ((MainActivity)getActivity()).getHanFuGuesses();

        for(int i=0; i<completeList.size(); i++){
            ScoreCalculator sc = new ScoreCalculator(completeList.get(i));
            if( scoreGuess(guessList.get(i), sc) ){
                correctGuesses++;
            } else {
                incorrectGuesses++;
                handsToReview.add(completeList.get(i));
            }
        }
    }
    private boolean scoreGuess(int[] guess, ScoreCalculator sc ){
        int hanGuess = guess[0];
        int fuGuess = guess[1];
        boolean isManganOrMore = ScoreCalculator.scoreBasePoints(sc.validatedHand.han, sc.validatedHand.fu) >= 2000;
        Integer roundedFu = (sc.validatedHand.fu==25) ? 25 : (int) Math.ceil(sc.validatedHand.fu/10.0)*10;

        return hanGuess==sc.validatedHand.han && ( isManganOrMore || fuGuess==roundedFu);
    }
    private void updateDisplayedScores(){
        correctScoreLabel.setText(String.valueOf(correctGuesses));
        incorrectScoreLabel.setText(String.valueOf(incorrectGuesses));
    }
    private void checkNewHighScore(){
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        int highScore = sharedPref.getInt("SpeedQuizHighScore", 0);

        if( correctGuesses> highScore){
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
            ((MainActivity)getActivity()).goToSpeedQuizReviewHand(v);
        }
    }

    private void createMainMenuButtonTimer(){
        new CountDownTimer(2000, 500){
            @Override
            public void onTick(long millisUntilFinished){}

            @Override
            public void onFinish(){
                mainMenuButton.setEnabled(true);
            }
        }.start();
    }

    private void registerUI(View myInflatedView){
        correctScoreLabel   = (TextView) myInflatedView.findViewById(R.id.correctScore);
        newHighScore        = (TextView) myInflatedView.findViewById(R.id.newHighScoreNotice);
        incorrectScoreLabel = (TextView) myInflatedView.findViewById(R.id.incorrectScore);
        incorrectButtonContainer = (LinearLayout) myInflatedView.findViewById(R.id.incorrectHandButtonContainer);
        mainMenuButton      = (Button) myInflatedView.findViewById(R.id.mainMenuButton);
    }
}
