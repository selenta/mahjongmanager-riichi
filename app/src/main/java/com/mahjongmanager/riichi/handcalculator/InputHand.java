package com.mahjongmanager.riichi.handcalculator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mahjongmanager.riichi.Hand;
import com.mahjongmanager.riichi.MainActivity;
import com.mahjongmanager.riichi.R;
import com.mahjongmanager.riichi.Tile;
import com.mahjongmanager.riichi.components.HandKeyboard;

import java.util.ArrayList;

public class InputHand extends Fragment implements View.OnClickListener {
    private Hand fragHand;

    private HandKeyboard handKeyboard;

    private Button clearHandButton;
    private Button nextButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.fragment_handcalculator_1keyboard, container, false);

        fragHand = ((MainActivity)getActivity()).getCurrentHand();

        registerUIElements(myInflatedView);
        handKeyboard.initialize(this);
        handKeyboard.setHand(fragHand);

        return myInflatedView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clearHandButton:
                fragHand = new Hand(new ArrayList<Tile>());
                checkNextEnablement();
                handKeyboard.setHand(fragHand);
                break;
        }
    }

    public void checkNextEnablement(){
        boolean validCurrentHand = handKeyboard.isValidCurrentHand();
        if(validCurrentHand){
            ((MainActivity)getActivity()).setCurrentHand(getHand());
            nextButton.setEnabled(true);
        } else {
            ((MainActivity)getActivity()).setCurrentHand(new Hand(new ArrayList<Tile>()));
            nextButton.setEnabled(false);
        }
    }

    public Hand getHand(){
        fragHand = handKeyboard.getHand();
        return fragHand;
    }

    private void registerUIElements(View myInflatedView){
        handKeyboard = (HandKeyboard) myInflatedView.findViewById(R.id.handKeyboard);
        handKeyboard.setOnClickListener(this);

        nextButton = (Button) myInflatedView.findViewById(R.id.nextButton);

        clearHandButton = (Button) myInflatedView.findViewById(R.id.clearHandButton);
        clearHandButton.setOnClickListener(this);
    }
}