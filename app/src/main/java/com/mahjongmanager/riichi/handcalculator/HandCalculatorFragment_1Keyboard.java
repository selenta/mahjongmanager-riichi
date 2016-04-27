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

import java.util.ArrayList;

public class HandCalculatorFragment_1Keyboard extends Fragment implements View.OnClickListener {

    private Tile.Suit currentSuit;

    private Hand fragHand;
    private boolean validCurrentHand = false;

    private Button manzuButton;
    private Button pinzuButton;
    private Button souzuButton;
    private Button honorsButton;

    private Button phButton1;
    private Button phButton2;
    private Button phButton3;
    private Button phButton4;
    private Button phButton5;
    private Button phButton6;
    private Button phButton7;
    private Button phButton8;
    private Button phButton9;

    private Button clearHandButton;

    private HandDisplay handDisplay;
    private TextView errorMessage;
    private Button nextButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.fragment_handcalculator_1keyboard, container, false);

        fragHand = ((MainActivity)getActivity()).getCurrentHand();

        registerButtons(myInflatedView);
        currentSuit = Tile.Suit.MANZU;
        errorMessage.setText("");
        setKeyboardSuit();

        return myInflatedView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.manzuButton:
                currentSuit = Tile.Suit.MANZU;
                setKeyboardSuit();
                break;
            case R.id.pinzuButton:
                currentSuit = Tile.Suit.PINZU;
                setKeyboardSuit();
                break;
            case R.id.souzuButton:
                currentSuit = Tile.Suit.SOUZU;
                setKeyboardSuit();
                break;
            case R.id.honorsButton:
                currentSuit = Tile.Suit.HONOR;
                setKeyboardSuit();
               break;
            case R.id.placeholderButton1:
                addTile(1);
                checkButtonEnablement(phButton1, 1);
                checkNextEnablement();
                break;
            case R.id.placeholderButton2:
                addTile(2);
                checkButtonEnablement(phButton2, 2);
                checkNextEnablement();
                break;
            case R.id.placeholderButton3:
                addTile(3);
                checkButtonEnablement(phButton3, 3);
                checkNextEnablement();
                break;
            case R.id.placeholderButton4:
                addTile(4);
                checkButtonEnablement(phButton4, 4);
                checkNextEnablement();
                break;
            case R.id.placeholderButton5:
                addTile(5);
                checkButtonEnablement(phButton5, 5);
                checkNextEnablement();
                break;
            case R.id.placeholderButton6:
                addTile(6);
                checkButtonEnablement(phButton6, 6);
                checkNextEnablement();
                break;
            case R.id.placeholderButton7:
                addTile(7);
                checkButtonEnablement(phButton7, 7);
                checkNextEnablement();
                break;
            case R.id.placeholderButton8:
                addTile(8);
                checkButtonEnablement(phButton8, 8);
                checkNextEnablement();
                break;
            case R.id.placeholderButton9:
                addTile(9);
                checkButtonEnablement(phButton9, 9);
                checkNextEnablement();
                break;
            case R.id.clearHandButton:
                fragHand = new Hand(new ArrayList<Tile>());
                checkNextEnablement();
                handDisplay.setHand(fragHand);
                errorMessage.setText("");
                checkAllButtonEnablement();
                break;
        }
    }

    private void addTile( Integer numb ){
        if( fragHand.tiles.size()>=18 ){
            errorMessage.setText("Too many tiles: Impossible to create valid hand");
            return;
        }

        Tile t = getTileForButton(numb);
        if( !tileBreaksHand(t) ){
            fragHand.addTile(t);
            handDisplay.setHand(fragHand);
        }
    }
    private boolean tileBreaksHand(Tile t){
        if( validCurrentHand ){
            Hand testHand = new Hand(fragHand);
            testHand.addTile(t);

            ScoreCalculator testSc = new ScoreCalculator(testHand);
            if( testSc.scoredHand==null ){
                return true;
            }
        }
        return false;
    }

    private void checkNextEnablement(){
        ScoreCalculator sc = new ScoreCalculator(fragHand);
        validCurrentHand = (sc.scoredHand!=null);

        if( validCurrentHand ){
            ((MainActivity)getActivity()).setCurrentHand(fragHand);
            nextButton.setEnabled(true);
        } else {
            ((MainActivity)getActivity()).setCurrentHand(new Hand(new ArrayList<Tile>()));
            nextButton.setEnabled(false);
        }
    }

    private void setKeyboardSuit(){
        manzuButton.setEnabled(true);
        pinzuButton.setEnabled(true);
        souzuButton.setEnabled(true);
        honorsButton.setEnabled(true);
        switch (currentSuit){
            case MANZU:
                manzuButton.setEnabled(false);
                setButtonsToNumbers();
                break;
            case PINZU:
                pinzuButton.setEnabled(false);
                setButtonsToNumbers();
                break;
            case SOUZU:
                souzuButton.setEnabled(false);
                setButtonsToNumbers();
                break;
            case HONOR:
                honorsButton.setEnabled(false);
                setButtonsToHonors();
                break;
        }
    }
    private void setButtonsToNumbers(){
        phButton1.setTextSize(20);
        phButton1.setText("1");
        phButton2.setTextSize(20);
        phButton2.setText("2");
        phButton3.setTextSize(20);
        phButton3.setText("3");
        phButton4.setTextSize(20);
        phButton4.setText("4");

        phButton6.setTextSize(20);
        phButton6.setText("6");
        phButton7.setTextSize(20);
        phButton7.setText("7");
        phButton8.setTextSize(20);
        phButton8.setText("8");

        checkAllButtonEnablement();

        phButton5.setVisibility(View.VISIBLE);
        phButton9.setVisibility(View.VISIBLE);
    }
    private void setButtonsToHonors(){
        phButton1.setTextSize(16);
        phButton1.setText("E");
        phButton2.setTextSize(16);
        phButton2.setText("S");
        phButton3.setTextSize(16);
        phButton3.setText("We");
        phButton4.setTextSize(16);
        phButton4.setText("N");

        phButton6.setTextSize(16);
        phButton6.setText("Wh");
        phButton7.setTextSize(16);
        phButton7.setText("G");
        phButton8.setTextSize(16);
        phButton8.setText("R");

        checkAllButtonEnablement();

        phButton5.setVisibility(View.GONE);
        phButton9.setVisibility(View.GONE);
    }
    private void checkAllButtonEnablement(){
        checkButtonEnablement(phButton1, 1);
        checkButtonEnablement(phButton2, 2);
        checkButtonEnablement(phButton3, 3);
        checkButtonEnablement(phButton4, 4);
        checkButtonEnablement(phButton5, 5);
        checkButtonEnablement(phButton6, 6);
        checkButtonEnablement(phButton7, 7);
        checkButtonEnablement(phButton8, 8);
        checkButtonEnablement(phButton9, 9);
    }
    private void checkButtonEnablement(Button b, int number){
        b.setEnabled(!fragHand.containsMaxOfTile(getTileForButton(number)));
    }
    private Tile getTileForButton(int i){
        if( currentSuit==Tile.Suit.HONOR ){
            switch (i){
                case 1:
                    return new Tile("East", "HONOR");
                case 2:
                    return new Tile("South", "HONOR");
                case 3:
                    return new Tile("West", "HONOR");
                case 4:
                    return new Tile("North", "HONOR");
                case 6:
                    return new Tile("White", "HONOR");
                case 7:
                    return new Tile("Green", "HONOR");
                case 8:
                    return new Tile("Red", "HONOR");
            }
        }
        return new Tile(i, currentSuit.toString());
    }

    private void registerButtons(View myInflatedView){
        handDisplay = (HandDisplay) myInflatedView.findViewById(R.id.handDisplay);

        errorMessage = (TextView) myInflatedView.findViewById(R.id.errorMessageLabel);

        nextButton = (Button) myInflatedView.findViewById(R.id.nextButton);

        manzuButton = (Button) myInflatedView.findViewById(R.id.manzuButton);
        manzuButton.setOnClickListener(this);
        pinzuButton = (Button) myInflatedView.findViewById(R.id.pinzuButton);
        pinzuButton.setOnClickListener(this);
        souzuButton = (Button) myInflatedView.findViewById(R.id.souzuButton);
        souzuButton.setOnClickListener(this);
        honorsButton = (Button) myInflatedView.findViewById(R.id.honorsButton);
        honorsButton.setOnClickListener(this);

        phButton1 = (Button) myInflatedView.findViewById(R.id.placeholderButton1);
        phButton1.setOnClickListener(this);
        phButton2 = (Button) myInflatedView.findViewById(R.id.placeholderButton2);
        phButton2.setOnClickListener(this);
        phButton3 = (Button) myInflatedView.findViewById(R.id.placeholderButton3);
        phButton3.setOnClickListener(this);
        phButton4 = (Button) myInflatedView.findViewById(R.id.placeholderButton4);
        phButton4.setOnClickListener(this);
        phButton5 = (Button) myInflatedView.findViewById(R.id.placeholderButton5);
        phButton5.setOnClickListener(this);
        phButton6 = (Button) myInflatedView.findViewById(R.id.placeholderButton6);
        phButton6.setOnClickListener(this);
        phButton7 = (Button) myInflatedView.findViewById(R.id.placeholderButton7);
        phButton7.setOnClickListener(this);
        phButton8 = (Button) myInflatedView.findViewById(R.id.placeholderButton8);
        phButton8.setOnClickListener(this);
        phButton9 = (Button) myInflatedView.findViewById(R.id.placeholderButton9);
        phButton9.setOnClickListener(this);

        clearHandButton = (Button) myInflatedView.findViewById(R.id.clearHandButton);
        clearHandButton.setOnClickListener(this);
    }
}
