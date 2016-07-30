package com.mahjongmanager.riichi.simplefragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mahjongmanager.riichi.R;

public class ScoringOverviewFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.fragment_scoringoverview, container, false);


        String basePointsEquationStr = "base points = <i>fu</i> x 2<small><sup>(2 + <i>han</i>)</sup></small>";
        ((TextView) myInflatedView.findViewById(R.id.basePointsEquation)).setText(Html.fromHtml( basePointsEquationStr ));

        return myInflatedView;
    }
}
