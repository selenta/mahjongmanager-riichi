package com.mahjongmanager.riichi.components;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import com.mahjongmanager.riichi.Hand;
import com.mahjongmanager.riichi.MainActivity;
import com.mahjongmanager.riichi.R;
import com.mahjongmanager.riichi.ScoreCalculator;
import com.mahjongmanager.riichi.Tile;
import com.mahjongmanager.riichi.handcalculator.HandCalculatorFragment_1Keyboard;
import com.mahjongmanager.riichi.utils.HandGenerator;
import com.mahjongmanager.riichi.utils.ImageCache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HandKeyboard extends LinearLayout implements View.OnClickListener {
    Context context;

    private Fragment fragment;
    private HandDisplay handDisplay;

    private Hand hand;

    private boolean validCurrentHand = false;
    private Tile.Suit currentSuit = Tile.Suit.MANZU;

    private TextView errorMessage;

    private CheckBox openMeldsCheckbox;
    private Button openChiiButton;
    private Button openPonButton;
    private Button openKanButton;

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

    private LinearLayout largeTilesModeContainer;
    private LinearLayout smallTilesModeContainer;

    private LinearLayout firstButtonContainer;
    private LinearLayout secondButtonContainer;
    private LinearLayout thirdButtonContainer;
    private LinearLayout fourthButtonContainer;

    private enum OpenTileMode {
        NONE, OPEN_CHII, OPEN_PON, OPEN_KAN
    }
    private OpenTileMode openTileMode = OpenTileMode.NONE;
    private boolean isKeyboardSmallTilesMode = false;

    private String KEYBOARD_TILE_SIZE_SETTING = "KeyboardSmallTiles";
    private int smallTilePadding = 10;
    private int smallTileMargin  = 10;

    private List<TileButton> buttonList = new ArrayList<>();

    ///////////////////////////////////////////////////
    ////////////////   Constructors   /////////////////
    ///////////////////////////////////////////////////
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
        checkKeyboardMode();
        setErrorMessage(null);
    }
    private void checkKeyboardMode(){
        if(isInEditMode()){     // This is only here so that Android Studio will display component
            return;
        }
        SharedPreferences sharedPref = ((MainActivity)context).getPreferences(Context.MODE_PRIVATE);
        isKeyboardSmallTilesMode = sharedPref.getBoolean(KEYBOARD_TILE_SIZE_SETTING, false);
        if( isKeyboardSmallTilesMode ){
            largeTilesModeContainer.setVisibility(GONE);
            createSmallButtons();
        } else {
            smallTilesModeContainer.setVisibility(GONE);
            setKeyboardSuit();
        }
    }

    @Override
    public void onClick(View v) {
        if( v.getClass()==ImageButton.class ){
            TileButton smallButton = getButton((ImageButton)v);
            if( smallButton!=null ){
                addTile(new Tile(smallButton.tile));
            }
        }
        switch (v.getId()) {
            case R.id.openMeldsCheckbox:
                boolean isChecked = openMeldsCheckbox.isChecked();
                openTileMode = (isChecked) ? OpenTileMode.OPEN_CHII : OpenTileMode.NONE;
                setOpenTileMode();
                break;
            case R.id.openChii:
                openTileMode = OpenTileMode.OPEN_CHII;
                setOpenTileMode();
                break;
            case R.id.openPon:
                openTileMode = OpenTileMode.OPEN_PON;
                setOpenTileMode();
                break;
            case R.id.openKan:
                openTileMode = OpenTileMode.OPEN_KAN;
                setOpenTileMode();
                break;
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
                break;
            case R.id.placeholderButton2:
                addTile(2);
                break;
            case R.id.placeholderButton3:
                addTile(3);
                break;
            case R.id.placeholderButton4:
                addTile(4);
                break;
            case R.id.placeholderButton5:
                addTile(5);
                break;
            case R.id.placeholderButton6:
                addTile(6);
                break;
            case R.id.placeholderButton7:
                addTile(7);
                break;
            case R.id.placeholderButton8:
                addTile(8);
                break;
            case R.id.placeholderButton9:
                addTile(9);
                break;
        }
    }
    private void setOpenTileMode(){
        if( openTileMode==OpenTileMode.NONE ){
            openChiiButton.setVisibility(INVISIBLE);
            openPonButton.setVisibility(INVISIBLE);
            openKanButton.setVisibility(INVISIBLE);
        } else {
            openChiiButton.setVisibility(VISIBLE);
            openPonButton.setVisibility(VISIBLE);
            openKanButton.setVisibility(VISIBLE);
        }

        openChiiButton.setEnabled(true);
        openPonButton.setEnabled(true);
        openKanButton.setEnabled(true);
        switch (openTileMode){
            case OPEN_CHII:
                openChiiButton.setEnabled(false);
                break;
            case OPEN_PON:
                openPonButton.setEnabled(false);
                break;
            case OPEN_KAN:
                openKanButton.setEnabled(false);
                break;
        }

        if( isKeyboardSmallTilesMode ){
            setOpenTileEnablementSmall();
        } else {
            setOpenTileEnablementLarge();
        }
    }
    private void addTile( Tile tile ){
        switch (openTileMode){
            case NONE:
                addTileSingle(tile);
                break;
            case OPEN_CHII:
                addOpenChii(tile);
                break;
            case OPEN_PON:
                addOpenPon(tile);
                break;
            case OPEN_KAN:
                addOpenKan(tile);
                break;
        }
    }
    private void addTileSingle(Tile tile){
        if( hand.tiles.size()>=18 ){
            setErrorMessage("Hand is too large to add an additional tile.");
            return;
        }

        if( !tileBreaksHand(tile) ){
            hand.addTile(tile);
            handDisplay.setHand(hand);
            checkValidCurrentHand();
        } else {
            setErrorMessage("Adding an additional "+tile.toString()+" tile would break this hand.");
        }
    }
    private boolean tileBreaksHand(Tile t){
        if( validCurrentHand ){
            Hand testHand = new Hand(hand);
            testHand.addTile(t);

            ScoreCalculator testSc = new ScoreCalculator(testHand);
            if( testSc.scoredHand==null ){
                return true;
            }
        }
        return false;
    }
    private void addOpenChii(Tile tile){
        if( hand.tiles.size() > 15 ){
            setErrorMessage("Hand is too large to add an additional open chii.");
            return;
        } else if( tile.suit==Tile.Suit.HONOR ){
            setErrorMessage("Can't add a chii of honors, not sure what you want me to do.");
            return;
        }

        Tile secondTile = new Tile(tile.number + 1, tile.suit.toString());
        Tile thirdTile = new Tile(tile.number + 2, tile.suit.toString());

        tile.calledFrom = Tile.CalledFrom.LEFT;
        tile.revealedState = secondTile.revealedState = thirdTile.revealedState = Tile.RevealedState.CHI;

        addTiles(Arrays.asList(tile, secondTile, thirdTile));
    }
    private void addOpenPon(Tile tile){
        if( hand.tiles.size() > 15 ){
            setErrorMessage("Hand is too large to add an additional open pon.");
            return;
        }

        Tile secondTile = new Tile(tile);
        Tile thirdTile = new Tile(tile);

        secondTile.calledFrom = Tile.CalledFrom.CENTER;
        tile.revealedState = secondTile.revealedState = thirdTile.revealedState = Tile.RevealedState.PON;

        addTiles(Arrays.asList(tile, secondTile, thirdTile));
    }
    private void addOpenKan(Tile tile){
        if( hand.tiles.size() > 14 ){
            setErrorMessage("Hand is too large to add an additional open kan.");
            return;
        }

        Tile secondTile = new Tile(tile);
        Tile thirdTile  = new Tile(tile);
        Tile fourthTile = new Tile(tile);

        secondTile.calledFrom = Tile.CalledFrom.CENTER;
        tile.revealedState = secondTile.revealedState = thirdTile.revealedState = fourthTile.revealedState = Tile.RevealedState.OPENKAN;

        addTiles(Arrays.asList(tile, secondTile, thirdTile, fourthTile));
    }
    private void addTiles(List<Tile> tiles){
        if( !tilesBreakHand(tiles) ){
            for(Tile t : tiles){
                hand.tiles.add(t);
            }
            hand.setMeld(tiles);
            handDisplay.setHand(hand);
            checkValidCurrentHand();
        }
    }
    private boolean tilesBreakHand(List<Tile> tiles){
        Hand testHand = new Hand(hand);
        for(Tile t : tiles){
            boolean addResult = testHand.addTile(t);
            if( !addResult ){
                setErrorMessage("Tile could not be added to hand, contains too many: "+t.toString());
                return true;
            }
        }
        if( testHand.emptyMeldCount()==0 ){
            setErrorMessage("No empty melds!");
            return true;
        }
        return false;
    }

    //////////////////////////////////////////////////////
    ////////////////   Large Tile Mode   /////////////////
    //////////////////////////////////////////////////////
    private void setOpenTileEnablementLarge(){
        if( openTileMode==OpenTileMode.OPEN_CHII && currentSuit!=Tile.Suit.HONOR){
            phButton8.setVisibility(INVISIBLE);
            phButton9.setVisibility(INVISIBLE);
        } else if( currentSuit==Tile.Suit.HONOR ){
            phButton8.setVisibility(VISIBLE);
        } else {
            phButton8.setVisibility(VISIBLE);
            phButton9.setVisibility(VISIBLE);
        }
    }
    private void setKeyboardSuit(){
        if(isInEditMode() || isKeyboardSmallTilesMode){     // This is only here so that Android Studio will display component
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
        phButton5.setVisibility(VISIBLE);
        phButton9.setVisibility(VISIBLE);
        setOpenTileEnablementLarge();

        setButtonImage(phButton1, 1);
        setButtonImage(phButton2, 2);
        setButtonImage(phButton3, 3);
        setButtonImage(phButton4, 4);
        setButtonImage(phButton5, 5);

        setButtonImage(phButton6, 6);
        setButtonImage(phButton7, 7);
        setButtonImage(phButton8, 8);
        setButtonImage(phButton9, 9);
    }
    private void setButtonsToHonors(){
        phButton5.setVisibility(GONE);
        phButton9.setVisibility(GONE);
        setOpenTileEnablementLarge();

        setButtonImage(phButton1, 1);
        setButtonImage(phButton2, 2);
        setButtonImage(phButton3, 3);
        setButtonImage(phButton4, 4);

        setButtonImage(phButton6, 6);
        setButtonImage(phButton7, 7);
        setButtonImage(phButton8, 8);
    }

    private void addTile( int number ){
        Tile t = getTileForButton(number);
        addTile(t);
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
        String cacheKey = t.getImageCacheKey(ImageCache.KEYBOARD_KEY_LARGE);
        BitmapDrawable tileDrawable = getImageCache().getBitmapFromCache(cacheKey);
        b.setImageDrawable(tileDrawable);
    }

    //////////////////////////////////////////////////////
    ////////////////   Small Tile Mode   /////////////////
    //////////////////////////////////////////////////////
    private void setOpenTileEnablementSmall(){
        for(TileButton tb : buttonList){
            if( tb.tile.suit!=Tile.Suit.HONOR && (tb.tile.number==8 || tb.tile.number==9) ){
                if( openTileMode==OpenTileMode.OPEN_CHII ){
                    tb.button.setVisibility(INVISIBLE);
                } else {
                    tb.button.setVisibility(VISIBLE);
                }
            }
        }
    }
    private void createSmallButtons(){
        List<Tile> tiles = HandGenerator.allTiles();
        for(Tile t : tiles){
            TileButton tileButton = new TileButton(t);
            tileButton.button.setOnClickListener(this);
            buttonList.add(tileButton);
            addSmallButtonToLayout(tileButton);
        }
    }
    private void addSmallButtonToLayout(TileButton tileButton){
        switch(tileButton.suit){
            case "MANZU":
                firstButtonContainer.addView(tileButton.button);
                break;
            case "PINZU":
                secondButtonContainer.addView(tileButton.button);
                break;
            case "SOUZU":
                thirdButtonContainer.addView(tileButton.button);
                break;
            case "HONOR":
                ImageButton honorButton = tileButton.button;
                fourthButtonContainer.addView(honorButton);
                if( tileButton.tile.toString().equals("North") ){
                    int spacerSize = ImageCache.KEYBOARD_TILE_WIDTH_SMALL + 2*smallTileMargin + 2*smallTilePadding;
                    Space spacer = new Space(context);
                    spacer.setMinimumWidth(spacerSize);
                    fourthButtonContainer.addView(spacer);
                }
                break;
        }
    }

    class TileButton {
        public String value;
        public String suit;
        public Tile tile;
        public ImageButton button;

        public TileButton(Tile t){
            tile = t;
            value = tile.value;
            suit = tile.suit.toString();
            createButtonWithImage();
        }

        private void createButtonWithImage(){
            button = new ImageButton(context);
            button.setBackgroundColor(0xFFD6D7D7);

            button.setPadding(smallTilePadding,smallTilePadding,smallTilePadding,smallTilePadding);
            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
            params.setMargins(smallTileMargin,smallTileMargin,smallTileMargin,smallTileMargin);
            button.setLayoutParams(params);

            String cacheKey = tile.getImageCacheKey(ImageCache.KEYBOARD_KEY_SMALL);
            BitmapDrawable tileDrawable = getImageCache().getBitmapFromCache(cacheKey);
            button.setImageDrawable(tileDrawable);
        }
    }
    private TileButton getButton(String v, String s){
        for( TileButton tb : buttonList ){
            if( tb.suit.equals(s) && tb.value.equals(v) ){
                return tb;
            }
        }
        return null;
    }
    private TileButton getButton(ImageButton button){
        for( TileButton tb : buttonList ){
            if( tb.button==button ){
                return tb;
            }
        }
        return null;
    }

    ////////////////////////////////////////////////////
    /////////////////      Other      //////////////////
    ////////////////////////////////////////////////////
    private void setErrorMessage(String s){
        errorMessage.setText(s);
    }

    private void checkValidCurrentHand(){
        ScoreCalculator sc = new ScoreCalculator(hand);
        validCurrentHand = (sc.scoredHand != null);
        ((HandCalculatorFragment_1Keyboard)fragment).checkNextEnablement(); // TODO whatever, clean later
    }
    public boolean isValidCurrentHand(){
        return validCurrentHand;
    }

    private void registerUIElements(){
        errorMessage = (TextView) findViewById(R.id.errorMessageLabel);

        handDisplay  = (HandDisplay) findViewById(R.id.handDisplay);
        handDisplay.setOnClickListener(this);
        handDisplay.setState(HandDisplay.HAND_CALCULATOR, this);

        openMeldsCheckbox = (CheckBox) findViewById(R.id.openMeldsCheckbox);
        openMeldsCheckbox.setOnClickListener(this);
        openChiiButton = (Button) findViewById(R.id.openChii);
        openChiiButton.setOnClickListener(this);
        openPonButton = (Button) findViewById(R.id.openPon);
        openPonButton.setOnClickListener(this);
        openKanButton = (Button) findViewById(R.id.openKan);
        openKanButton.setOnClickListener(this);

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

        largeTilesModeContainer  = (LinearLayout) findViewById(R.id.largeTilesModeContainer);
        smallTilesModeContainer  = (LinearLayout) findViewById(R.id.smallTilesModeContainer);

        firstButtonContainer  = (LinearLayout) findViewById(R.id.firstButtonContainer);
        secondButtonContainer = (LinearLayout) findViewById(R.id.secondButtonContainer);
        thirdButtonContainer  = (LinearLayout) findViewById(R.id.thirdButtonContainer);
        fourthButtonContainer = (LinearLayout) findViewById(R.id.fourthButtonContainer);
    }
    public void initialize(Fragment f){
        fragment = f;
    }

    public Hand getHand(){ return hand; }
    public void setHand(Hand h){
        hand = h;
        handDisplay.setHand(hand);
        setErrorMessage(null);
        checkValidCurrentHand();
    }

    ImageCache _imageCache;
    private ImageCache getImageCache(){
        if( _imageCache==null ){
            _imageCache = ((MainActivity)context).getImageCache();
        }
        return _imageCache;
    }
}
