package com.mahjongmanager.riichi;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HanFuCalculatorFragment extends Fragment implements View.OnClickListener {
    private List<Integer> hanValues = Arrays.asList( 1,2,3,4,5,6,7,8,9,10,11,12,13 );
    private List<Integer> fuValues = Arrays.asList( 20,25,30,40,50,60,70,80,90,100,110 );

    private int han;
    private TextView hanLabel;
    private int fu;
    private TextView fuLabel;

    private CheckBox dealerCheckbox;
    private CheckBox tsumoCheckbox;

    private Button increaseHanButton;
    private Button decreaseHanButton;
    private Button increaseFuButton;
    private Button decreaseFuButton;

    private TextView scoreValue;

    ScoreCalculator sc = new ScoreCalculator();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        han = 2;
        fu = 30;

        View myInflatedView = inflater.inflate(R.layout.fragment_hanfucalculator, container, false);

        hanLabel = (TextView) myInflatedView.findViewById(R.id.hanValue);
        fuLabel = (TextView) myInflatedView.findViewById(R.id.fuValue);

        dealerCheckbox = (CheckBox) myInflatedView.findViewById(R.id.dealerCheckbox);
        dealerCheckbox.setOnClickListener(this);
        tsumoCheckbox = (CheckBox) myInflatedView.findViewById(R.id.tsumoCheckbox);
        tsumoCheckbox.setOnClickListener(this);

        increaseHanButton = (Button) myInflatedView.findViewById(R.id.increaseHanButton);
        increaseHanButton.setOnClickListener(this);
        decreaseHanButton = (Button) myInflatedView.findViewById(R.id.decreaseHanButton);
        decreaseHanButton.setOnClickListener(this);
        increaseFuButton = (Button) myInflatedView.findViewById(R.id.increaseFuButton);
        increaseFuButton.setOnClickListener(this);
        decreaseFuButton = (Button) myInflatedView.findViewById(R.id.decreaseFuButton);
        decreaseFuButton.setOnClickListener(this);

        scoreValue = (TextView) myInflatedView.findViewById(R.id.scoreValue);

        updateScore();
        return myInflatedView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dealerCheckbox:
                break;
            case R.id.tsumoCheckbox:
                break;
            case R.id.increaseHanButton:
                han = hanValues.get(hanValues.indexOf(han)+1);
                break;
            case R.id.decreaseHanButton:
                han = hanValues.get(hanValues.indexOf(han)-1);
               break;
            case R.id.increaseFuButton:
                fu = fuValues.get(fuValues.indexOf(fu)+1);
                break;
            case R.id.decreaseFuButton:
                fu = fuValues.get(fuValues.indexOf(fu)-1);
                break;
        }
        updateScore();
    }

    //Calculate the score and update the UI state
    private void updateScore(){
        updateUI();

        String result = sc.scoreHanFu(han, fu, dealerCheckbox.isChecked(), tsumoCheckbox.isChecked());

        scoreValue.setText( result );
    }

    private void updateUI(){
        hanLabel.setText(String.valueOf(han));
        fuLabel.setText(String.valueOf(fu));

        if( han == hanValues.get(0) ){
            //Can't decrease below min
            decreaseHanButton.setEnabled(false);
        } else if( han == hanValues.get(hanValues.size()-1) ){
            //Can't increase past max size
            increaseHanButton.setEnabled(false);
        } else {
            increaseHanButton.setEnabled(true);
            decreaseHanButton.setEnabled(true);
        }

        if( fu == fuValues.get(0) ){
            //Can't decrease below min
            decreaseFuButton.setEnabled(false);
        } else if( fu == fuValues.get(fuValues.size()-1) ){
            //Can't increase past max size
            increaseFuButton.setEnabled(false);
        } else {
            increaseFuButton.setEnabled(true);
            decreaseFuButton.setEnabled(true);
        }
    }
}
