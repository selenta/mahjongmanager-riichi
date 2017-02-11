package com.mahjongmanager.riichi.speedquiz;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mahjongmanager.riichi.R;
import com.mahjongmanager.riichi.utils.AppSettings;

public class SpeedQuizStart extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.fragment_speedquiz_1start, container, false);

        TextView currentHighScoreLabel = (TextView) myInflatedView.findViewById(R.id.speedQuizHighScore);

        Integer highScore = AppSettings.getSpeedQuizHighScore();

        String highScoreLabel = "Current Record: " + highScore;
        currentHighScoreLabel.setText(highScoreLabel);

        return myInflatedView;
    }

    @Override
    public void onStart(){
        super.onStart();
    }
}
