package com.mahjongmanager.riichi.handcalculator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mahjongmanager.riichi.Hand;
import com.mahjongmanager.riichi.components.HandDisplay;
import com.mahjongmanager.riichi.MainActivity;
import com.mahjongmanager.riichi.R;
import com.mahjongmanager.riichi.ScoreCalculator;
import com.mahjongmanager.riichi.Tile;
import com.mahjongmanager.riichi.components.HandKeyboard;

import java.util.ArrayList;

public class HandCalculatorFragment_1Keyboard extends Fragment implements View.OnClickListener {
    private Hand fragHand;

    private TextView errorMessage;
    private HandDisplay handDisplay;
    private HandKeyboard handKeyboard;

    private Button clearHandButton;
    private Button nextButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.fragment_handcalculator_1keyboard, container, false);

        fragHand = ((MainActivity)getActivity()).getCurrentHand();

        registerUIElements(myInflatedView);
        handKeyboard.initialize(this, handDisplay);
        errorMessage.setText(null);

        return myInflatedView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clearHandButton:
                fragHand = new Hand(new ArrayList<Tile>());
                checkNextEnablement();
                handDisplay.setHand(fragHand);
                errorMessage.setText("");
                handKeyboard.checkAllButtonEnablement();
                break;
        }
    }

    public void checkNextEnablement(){
        ScoreCalculator sc = new ScoreCalculator(fragHand);
        boolean validCurrentHand = (sc.scoredHand != null);
        handKeyboard.setValidCurrentHand(validCurrentHand);

        if(validCurrentHand){
            ((MainActivity)getActivity()).setCurrentHand(fragHand);
            nextButton.setEnabled(true);
        } else {
            ((MainActivity)getActivity()).setCurrentHand(new Hand(new ArrayList<Tile>()));
            nextButton.setEnabled(false);
        }
    }

    public Hand getHand(){
        return fragHand;
    }

    private void registerUIElements(View myInflatedView){
        handDisplay  = (HandDisplay)  myInflatedView.findViewById(R.id.handDisplay);
        handKeyboard = (HandKeyboard) myInflatedView.findViewById(R.id.handKeyboard);

        errorMessage = (TextView) myInflatedView.findViewById(R.id.errorMessageLabel);

        nextButton = (Button) myInflatedView.findViewById(R.id.nextButton);

        clearHandButton = (Button) myInflatedView.findViewById(R.id.clearHandButton);
        clearHandButton.setOnClickListener(this);
    }
}
