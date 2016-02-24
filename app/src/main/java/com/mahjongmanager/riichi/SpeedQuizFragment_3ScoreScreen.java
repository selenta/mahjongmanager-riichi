package com.mahjongmanager.riichi;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SpeedQuizFragment_3ScoreScreen extends Fragment implements View.OnClickListener {
    private TextView correctScoreLabel;
    private TextView incorrectScoreLabel;

    private LinearLayout incorrectButtonContainer;

    List<Hand> completeList = new ArrayList<>();
    private int correctGuesses   = 0;
    private int incorrectGuesses = 0;
    private List<Hand> handsToReview = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.fragment_speedquiz_3scorescreen, container, false);

        correctScoreLabel = (TextView) myInflatedView.findViewById(R.id.correctScore);
        incorrectScoreLabel = (TextView) myInflatedView.findViewById(R.id.incorrectScore);

        incorrectButtonContainer = (LinearLayout) myInflatedView.findViewById(R.id.incorrectHandButtonContainer);

        scoreGuesses();
        updateUI();

        return myInflatedView;
    }

    private void scoreGuesses(){
        completeList = ((MainActivity)getActivity()).getScoredHands();
        List<int[]> guessList = ((MainActivity)getActivity()).getHanFuGuesses();

        for(int i=0; i<completeList.size(); i++){
            ScoreCalculator sc = new ScoreCalculator(completeList.get(i));
            int hanGuess = guessList.get(i)[0];
            int fuGuess = guessList.get(i)[1];
            if( hanGuess==sc.validatedHand.han
                    && (sc.scoreBasePoints(sc.validatedHand.han,sc.validatedHand.fu)>2000
                        || fuGuess==sc.validatedHand.fu) ){
                correctGuesses++;
            } else {
                incorrectGuesses++;
                handsToReview.add(completeList.get(i));
            }
        }

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
