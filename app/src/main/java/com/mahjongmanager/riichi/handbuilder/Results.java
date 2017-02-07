package com.mahjongmanager.riichi.handbuilder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mahjongmanager.riichi.MainActivity;
import com.mahjongmanager.riichi.R;
import com.mahjongmanager.riichi.ScoreCalculator;
import com.mahjongmanager.riichi.common.Hand;
import com.mahjongmanager.riichi.components.HandDisplay;
import com.mahjongmanager.riichi.components.ScoreScreen;
import com.mahjongmanager.riichi.utils.AppSettings;
import com.mahjongmanager.riichi.utils.Utils;

public class Results extends Fragment {
    private TextView scoreScreenTitleLabel;

    private HandDisplay handDisplay;
    private ScoreScreen scoreScreen;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.fragment_handbuilder_3scorescreen, container, false);

        scoreScreenTitleLabel = (TextView) myInflatedView.findViewById(R.id.scoreScreenTitleLabel);
        handDisplay = (HandDisplay) myInflatedView.findViewById(R.id.handDisplay);
        scoreScreen = (ScoreScreen) myInflatedView.findViewById(R.id.scoreScreen);

        cleanupAndScoreHand();

        checkTextSize();
        return myInflatedView;
    }

    private void cleanupAndScoreHand(){
        Hand actHand = ((MainActivity)getActivity()).getCurrentHand();

        ScoreCalculator sc = new ScoreCalculator(actHand, true);
        if( sc.validatedHand!=null ){
            actHand = sc.validatedHand;
            checkHighScore(actHand);
        }

        scoreScreen.setHand(actHand);
        handDisplay.setHand(actHand);
    }

    private void checkHighScore(Hand h){
        int recordHan = AppSettings.getHandBuilderHanRecord();
        int recordFu = AppSettings.getHandBuilderFuRecord();

        int oldScore = ScoreCalculator.scoreBasePoints(recordHan, recordFu);
        int newScore = ScoreCalculator.scoreBasePoints(h.han, h.fu);

        if( newScore > oldScore ){
            AppSettings.setHandBuilderHanRecord(h.han);
            AppSettings.setHandBuilderFuRecord(h.fu);
        }
    }

    //Update title to fit smaller screens
    private void checkTextSize(){
        if(Utils.SCREEN_SIZE < 1024 ){
            scoreScreenTitleLabel.setTextSize(scoreScreenTitleLabel.getTextSize()*0.55f);
        }
    }
}
