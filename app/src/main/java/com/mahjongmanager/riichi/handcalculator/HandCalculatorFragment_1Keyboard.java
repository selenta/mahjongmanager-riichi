package com.mahjongmanager.riichi.handcalculator;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mahjongmanager.riichi.Hand;
import com.mahjongmanager.riichi.components.HandDisplay;
import com.mahjongmanager.riichi.MainActivity;
import com.mahjongmanager.riichi.R;
import com.mahjongmanager.riichi.ScoreCalculator;
import com.mahjongmanager.riichi.Tile;
import com.mahjongmanager.riichi.utils.ImageCache;
import com.mahjongmanager.riichi.utils.Utils;

import java.util.ArrayList;

public class HandCalculatorFragment_1Keyboard extends Fragment implements View.OnClickListener {

    private Tile.Suit currentSuit;

    private Hand fragHand;
    private boolean validCurrentHand = false;

    private Button manzuButton;
    private Button pinzuButton;
    private Button souzuButton;
    private Button honorsButton;

    private ImageButton phButton1;
    private ImageButton phButton2;
    private ImageButton phButton3;
    private ImageButton phButton4;
    private ImageButton phButton5;
    private ImageButton phButton6;
    private ImageButton phButton7;
    private ImageButton phButton8;
    private ImageButton phButton9;

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
        setButtonImage(phButton1, 1);
        setButtonImage(phButton2, 2);
        setButtonImage(phButton3, 3);
        setButtonImage(phButton4, 4);
        setButtonImage(phButton5, 5);

        setButtonImage(phButton6, 6);
        setButtonImage(phButton7, 7);
        setButtonImage(phButton8, 8);
        setButtonImage(phButton9, 9);

        checkAllButtonEnablement();

        phButton5.setVisibility(View.VISIBLE);
        phButton9.setVisibility(View.VISIBLE);
    }
    private void setButtonsToHonors(){
        setButtonImage(phButton1, 1);
        setButtonImage(phButton2, 2);
        setButtonImage(phButton3, 3);
        setButtonImage(phButton4, 4);

        setButtonImage(phButton6, 6);
        setButtonImage(phButton7, 7);
        setButtonImage(phButton8, 8);

        checkAllButtonEnablement();

        phButton5.setVisibility(View.GONE);
        phButton9.setVisibility(View.GONE);
    }
    private void setButtonImage(ImageButton b, int i){
        Tile t = getTileForButton(i);
        String cacheKey = t.getImageCacheKey(ImageCache.KEYBOARD_KEY);
        BitmapDrawable tileDrawable = getImageCache().getBitmapFromCache(cacheKey);
        b.setImageDrawable(tileDrawable);
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
    private void checkButtonEnablement(ImageButton b, int number){
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

        phButton1 = (ImageButton) myInflatedView.findViewById(R.id.placeholderButton1);
        phButton1.setOnClickListener(this);
        phButton2 = (ImageButton) myInflatedView.findViewById(R.id.placeholderButton2);
        phButton2.setOnClickListener(this);
        phButton3 = (ImageButton) myInflatedView.findViewById(R.id.placeholderButton3);
        phButton3.setOnClickListener(this);
        phButton4 = (ImageButton) myInflatedView.findViewById(R.id.placeholderButton4);
        phButton4.setOnClickListener(this);
        phButton5 = (ImageButton) myInflatedView.findViewById(R.id.placeholderButton5);
        phButton5.setOnClickListener(this);
        phButton6 = (ImageButton) myInflatedView.findViewById(R.id.placeholderButton6);
        phButton6.setOnClickListener(this);
        phButton7 = (ImageButton) myInflatedView.findViewById(R.id.placeholderButton7);
        phButton7.setOnClickListener(this);
        phButton8 = (ImageButton) myInflatedView.findViewById(R.id.placeholderButton8);
        phButton8.setOnClickListener(this);
        phButton9 = (ImageButton) myInflatedView.findViewById(R.id.placeholderButton9);
        phButton9.setOnClickListener(this);

        clearHandButton = (Button) myInflatedView.findViewById(R.id.clearHandButton);
        clearHandButton.setOnClickListener(this);
    }

    ImageCache _imageCache;
    private ImageCache getImageCache(){
        if( _imageCache==null ){
            _imageCache = ((MainActivity)getActivity()).getImageCache();
        }
        return _imageCache;
    }
}
