package com.mahjongmanager.riichi.handbuilder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mahjongmanager.riichi.R;
import com.mahjongmanager.riichi.utils.AppSettings;

public class HandBuilderStart extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.fragment_handbuilder_1start, container, false);

        int handsWon = AppSettings.getHandBuilderHandsWon();
        String handsWonLabel = "Winning Hands: "+handsWon;
        TextView handBuilderHandsWonLabel = (TextView) myInflatedView.findViewById(R.id.handBuilderHandsWon);
        handBuilderHandsWonLabel.setText(handsWonLabel);

        int manganHands = AppSettings.getHandBuilderManganHands();
        String manganHandsLabel = "Mangan (or larger): "+manganHands;
        TextView handBuilderManganHandsLabel = (TextView) myInflatedView.findViewById(R.id.handBuilderManganHands);
        handBuilderManganHandsLabel.setText(manganHandsLabel);

        return myInflatedView;
    }

    @Override
    public void onStart(){
        super.onStart();
    }
}
