package com.mahjongmanager.riichi.components;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.mahjongmanager.riichi.Hand;
import com.mahjongmanager.riichi.MainActivity;
import com.mahjongmanager.riichi.R;
import com.mahjongmanager.riichi.ScoreCalculator;
import com.mahjongmanager.riichi.Tile;
import com.mahjongmanager.riichi.handcalculator.HandCalculatorFragment_1Keyboard;
import com.mahjongmanager.riichi.utils.ImageCache;

import java.util.ArrayList;

public class HandKeyboard extends LinearLayout implements View.OnClickListener {
    Context context;

    private Fragment fragment;
    private HandDisplay handDisplay;

    private boolean validCurrentHand = false;

    private Tile.Suit currentSuit = Tile.Suit.MANZU;

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

    private LinearLayout firstButtonContainer;
    private LinearLayout secondButtonContainer;
    private LinearLayout thirdButtonContainer;
    private LinearLayout fourthButtonContainer;

    private boolean isLargeKeyboardMode = false;
    private String TILE_KEYBOARD = "LargeUIKeyboard";

    /////////////////////////////////////////
    ///////////   Constructors   ////////////
    /////////////////////////////////////////
    public HandKeyboard(Context ctx){
        this(ctx, null);
    }
    public HandKeyboard(Context ctx, AttributeSet attrs) {
        this(ctx, attrs, 0);
    }
    public HandKeyboard(Context ctx, AttributeSet attrs, int defStyle ){
        super(ctx, attrs, defStyle);
        context = ctx;
        init();
    }
    private void init(){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.component_handkeyboard, this);

        registerUIElements();
        setKeyboardSuit();
        checkForLargeKeyboardMode();
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
                break;
            case R.id.placeholderButton2:
                addTile(2);
                checkButtonEnablement(phButton2, 2);
                break;
            case R.id.placeholderButton3:
                addTile(3);
                checkButtonEnablement(phButton3, 3);
                break;
            case R.id.placeholderButton4:
                addTile(4);
                checkButtonEnablement(phButton4, 4);
                break;
            case R.id.placeholderButton5:
                addTile(5);
                checkButtonEnablement(phButton5, 5);
                break;
            case R.id.placeholderButton6:
                addTile(6);
                checkButtonEnablement(phButton6, 6);
                break;
            case R.id.placeholderButton7:
                addTile(7);
                checkButtonEnablement(phButton7, 7);
                break;
            case R.id.placeholderButton8:
                addTile(8);
                checkButtonEnablement(phButton8, 8);
                break;
            case R.id.placeholderButton9:
                addTile(9);
                checkButtonEnablement(phButton9, 9);
                break;
        }
    }
    private void addTile( Integer numb ){
        Hand fragHand = getFragmentHand();
        if( fragHand.tiles.size()>=18 ){
//            errorMessage.setText("Too many tiles: Impossible to create valid hand");
            return;
        }

        Tile t = getTileForButton(numb);
        if( !tileBreaksHand(t) ){
            fragHand.addTile(t);
            handDisplay.setHand(fragHand);
            ((HandCalculatorFragment_1Keyboard)fragment).checkNextEnablement(); // TODO whatever, clean later
        }
    }
    private boolean tileBreaksHand(Tile t){
        if( validCurrentHand ){
            Hand testHand = new Hand(getFragmentHand());
            testHand.addTile(t);

            ScoreCalculator testSc = new ScoreCalculator(testHand);
            if( testSc.scoredHand==null ){
                return true;
            }
        }
        return false;
    }

    private void setKeyboardSuit(){
        if(isInEditMode()){     // This is only here so that Android Studio will display component
            return;
        }

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
        phButton5.setVisibility(View.VISIBLE);
        phButton9.setVisibility(View.VISIBLE);

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
    }
    private void setButtonsToHonors(){
        phButton5.setVisibility(View.GONE);
        phButton9.setVisibility(View.GONE);

        setButtonImage(phButton1, 1);
        setButtonImage(phButton2, 2);
        setButtonImage(phButton3, 3);
        setButtonImage(phButton4, 4);

        setButtonImage(phButton6, 6);
        setButtonImage(phButton7, 7);
        setButtonImage(phButton8, 8);

        checkAllButtonEnablement();
    }
    private void checkForLargeKeyboardMode(){
        if(isInEditMode()){     // This is only here so that Android Studio will display component
            return;
        }
        SharedPreferences sharedPref = ((MainActivity)context).getPreferences(Context.MODE_PRIVATE);
        isLargeKeyboardMode = sharedPref.getBoolean(TILE_KEYBOARD, false);
        if( !isLargeKeyboardMode ){
            return;
        }
    }

    public void checkAllButtonEnablement(){
        if(fragment==null){
            return;
        }
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
        b.setEnabled(!getFragmentHand().containsMaxOfTile(getTileForButton(number)));
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
    private void setButtonImage(ImageButton b, int i){
        Tile t = getTileForButton(i);
        String cacheKey = t.getImageCacheKey(ImageCache.KEYBOARD_KEY);
        BitmapDrawable tileDrawable = getImageCache().getBitmapFromCache(cacheKey);
        b.setImageDrawable(tileDrawable);
    }

    public void setValidCurrentHand(boolean b){
        validCurrentHand = b;
    }
    private Hand getFragmentHand(){
        Hand h = ((HandCalculatorFragment_1Keyboard)fragment).getHand();   // TODO whatever, clean later
        if(h==null){
            h = new Hand(new ArrayList<Tile>());
        }
        return h;
    }

    private void registerUIElements(){
        manzuButton = (Button) findViewById(R.id.manzuButton);
        manzuButton.setOnClickListener(this);
        pinzuButton = (Button) findViewById(R.id.pinzuButton);
        pinzuButton.setOnClickListener(this);
        souzuButton = (Button) findViewById(R.id.souzuButton);
        souzuButton.setOnClickListener(this);
        honorsButton = (Button) findViewById(R.id.honorsButton);
        honorsButton.setOnClickListener(this);

        phButton1 = (ImageButton) findViewById(R.id.placeholderButton1);
        phButton1.setOnClickListener(this);
        phButton2 = (ImageButton) findViewById(R.id.placeholderButton2);
        phButton2.setOnClickListener(this);
        phButton3 = (ImageButton) findViewById(R.id.placeholderButton3);
        phButton3.setOnClickListener(this);
        phButton4 = (ImageButton) findViewById(R.id.placeholderButton4);
        phButton4.setOnClickListener(this);
        phButton5 = (ImageButton) findViewById(R.id.placeholderButton5);
        phButton5.setOnClickListener(this);
        phButton6 = (ImageButton) findViewById(R.id.placeholderButton6);
        phButton6.setOnClickListener(this);
        phButton7 = (ImageButton) findViewById(R.id.placeholderButton7);
        phButton7.setOnClickListener(this);
        phButton8 = (ImageButton) findViewById(R.id.placeholderButton8);
        phButton8.setOnClickListener(this);
        phButton9 = (ImageButton) findViewById(R.id.placeholderButton9);
        phButton9.setOnClickListener(this);

//        firstButtonContainer  = (LinearLayout) findViewById(R.id.firstButtonContainer);
//        secondButtonContainer = (LinearLayout) findViewById(R.id.secondButtonContainer);
//        thirdButtonContainer  = (LinearLayout) findViewById(R.id.thirdButtonContainer);
//        fourthButtonContainer = (LinearLayout) findViewById(R.id.fourthButtonContainer);
    }
    public void initialize(Fragment f, HandDisplay hd){
        fragment = f;
        handDisplay = hd;
    }

    ImageCache _imageCache;
    private ImageCache getImageCache(){
        if( _imageCache==null ){
            _imageCache = ((MainActivity)context).getImageCache();
        }
        return _imageCache;
    }
}
