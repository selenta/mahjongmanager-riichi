package com.mahjongmanager.riichi.speedquiz;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mahjongmanager.riichi.R;

public class SpeedQuizFragment_1Start extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.fragment_speedquiz_1start, container, false);

        TextView currentHighScoreLabel = (TextView) myInflatedView.findViewById(R.id.speedQuizHighScore);

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        Integer highScore = sharedPref.getInt("SpeedQuizHighScore", 0);

        String highScoreLabel = "Current Record: " + highScore;
        currentHighScoreLabel.setText(highScoreLabel);

        return myInflatedView;
    }
}
