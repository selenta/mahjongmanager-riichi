package com.mahjongmanager.riichi.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;

import com.mahjongmanager.riichi.Hand;
import com.mahjongmanager.riichi.MainActivity;
import com.mahjongmanager.riichi.Meld;
import com.mahjongmanager.riichi.R;
import com.mahjongmanager.riichi.Tile;
import com.mahjongmanager.riichi.utils.ImageCache;
import com.mahjongmanager.riichi.utils.Log;
import com.mahjongmanager.riichi.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class HandDisplay extends LinearLayout implements View.OnClickListener {
    private Context activity;
    private HandKeyboard parentKeyboard = null;

    private LinearLayout closedTilesContainer;
    private LinearLayout openTilesContainer;
    private LinearLayout winningTileContainer;

    private Hand hand;
    private List<TileDisplay> tileList = new ArrayList<>();

    public static final int FU_DISPLAY = 1;
    public static final int HAND_CALCULATOR = 2;
    public static final int SPEED_QUIZ = 3;
    public static final int SPEED_QUIZ_UNSORTED = 4;
    public static final int YAKU_DESCRIPTION = 5;

    private Boolean isEditable;
    private Boolean separateClosedMelds;
    private Boolean includeWinningTile;
    private Boolean separateWinningTile;

    /////////////////////////////////////////
    ///////////   Constructors   ////////////
    /////////////////////////////////////////
    public HandDisplay(Context ctx){
        this(ctx, null);
    }
    public HandDisplay(Context ctx, AttributeSet attrs) {
        this(ctx, attrs, 0);
    }
    public HandDisplay(Context ctx, AttributeSet attrs, int defStyle ){
        super(ctx, attrs, defStyle);
        activity = ctx;
        initializeView();
    }

    private void initializeView(){
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.component_handdisplay, this);

        closedTilesContainer = (LinearLayout) findViewById(R.id.closedTilesContainer);
        openTilesContainer   = (LinearLayout) findViewById(R.id.openTilesContainer);
        winningTileContainer = (LinearLayout) findViewById(R.id.winningTileContainer);

        calcHeight();
        setState(YAKU_DESCRIPTION);
    }

    /**
     * FU_DISPLAY
     *     SeparateClosedMelds - no
     *     Separate WinningTile - no
     *
     * HAND_CALCULATOR
     *     SeparateClosedMelds - no
     *     Separate WinningTile - no
     *
     * SPEED_QUIZ
     *     SeparateClosedMelds - yes
     *     Separate WinningTile - yes
     *
     * SPEED_QUIZ_UNSORTED
     *     SeparateClosedMelds - no
     *     Separate WinningTile - yes
     *
     * YAKU_DESCRIPTION
     *     SeparateClosedMelds - yes
     *     Separate WinningTile - yes
     *
     * Only hide winning tile during SpeedQuiz
     * Only editable during HAND_CALCULATOR
     * Always show open melds in their true form
     */
    public void setState(Integer state, HandKeyboard handKeyboard){
        switch (state){
            case FU_DISPLAY:
                isEditable = false;
                includeWinningTile = true;
                separateClosedMelds = false;
                separateWinningTile = false;
                break;
            case HAND_CALCULATOR:
                if( handKeyboard==null ){
                    Log.e("setHandDisplayState", "Attempted to set state to HAND_CALCULATOR without a keyboard.");
                }
                parentKeyboard = handKeyboard;
                isEditable = true;
                includeWinningTile = true;
                separateClosedMelds = false;
                separateWinningTile = false;
                break;
            case SPEED_QUIZ:
                isEditable = false;
                includeWinningTile = false;
                separateClosedMelds = true;
                separateWinningTile = true;
                break;
            case SPEED_QUIZ_UNSORTED:
                isEditable = false;
                includeWinningTile = false;
                separateClosedMelds = false;
                separateWinningTile = true;
                break;
            case YAKU_DESCRIPTION:
                isEditable = false;
                includeWinningTile = true;
                separateClosedMelds = true;
                separateWinningTile = true;
                break;
        }
    }
    public void setState(int s){
        setState(s, null);
    }

    private void calcHeight(){
        if( isInEditMode() ){
            return;
        }

        int size = getUtils().getActualTileWidth(ImageCache.HAND_DISPLAY_KEY);

        if( hand!=null && hand.hasAddedKan() ){
            size = size * 17/10 + 10;
            closedTilesContainer.setMinimumHeight(size);
        } else if( hand!=null && hand.isOpen() ){
            size = size * 14/10 + 10;
            closedTilesContainer.setMinimumHeight(size);
        }
    }

    ///////////////////////////////////////////
    /////////////     Main     ////////////////
    ///////////////////////////////////////////
    @Override
    public void onClick(View v) {
        if( isEditable && v.getClass()==ImageView.class ){
            TileDisplay td = getTileDisplay((ImageView) v);
            if( td!=null ){
                deleteTile(td);
            }
        }
    }
    private void deleteTile(TileDisplay tileDisplay){
        if( tileDisplay.tile.revealedState!=Tile.RevealedState.NONE ){
            Meld meld = hand.getMeld(tileDisplay.tile);
            for( Tile t : meld.getTiles() ){
                hand.tiles.remove(t);
            }
            meld.setTiles(new ArrayList<Tile>());
        } else {
            hand.tiles.remove(tileDisplay.tile);
            hand.unsortedTiles.remove(tileDisplay.tile);
        }
        tileList.remove(tileDisplay);
        displayHand();
        parentKeyboard.setHand(hand);
    }

    public Hand getHand(){
        return hand;
    }
    public void setHand(Hand h){
        hand = h;
        displayHand();
    }
    private void displayHand(){
        if( hand==null ){
            return;
        }
        calcHeight();

        closedTilesContainer.removeAllViews();
        openTilesContainer.removeAllViews();
        winningTileContainer.removeAllViews();

        if( separateClosedMelds && !hand.hasAbnormalStructure() ){
            displayComplexHand();
        } else {
            displaySimpleHand();
        }
    }

    private void displaySimpleHand(){
        List<Tile> usedTiles = new ArrayList<>();
        if( checkShowMeld(hand.meld1) ){
            addSetComplex(hand.meld1);
            usedTiles.addAll(hand.meld1.getTiles());
        }
        if( checkShowMeld(hand.meld2)  ){
            addSetComplex(hand.meld2);
            usedTiles.addAll(hand.meld2.getTiles());
        }
        if( checkShowMeld(hand.meld3) ){
            addSetComplex(hand.meld3);
            usedTiles.addAll(hand.meld3.getTiles());
        }
        if( checkShowMeld(hand.meld4)  ){
            addSetComplex(hand.meld4);
            usedTiles.addAll(hand.meld4.getTiles());
        }

        if( includeWinningTile && separateWinningTile ){
            usedTiles.add(hand.getWinningTile());
            addTileWinningTile();
        } else if( !includeWinningTile ){
            usedTiles.add(hand.getWinningTile());
        }

        for( Tile t : hand.tiles ){
            if( !usedTiles.contains(t) ){
                addTileClosed(t);
            }
        }
    }
    private boolean checkShowMeld(Meld m) {
        return separateClosedMelds || m.isKan() || !m.isClosed() && !m.hasWinningTile();
    }
    /**
     * Displaying a completed hand in the most pretty format possible is a bit more complex
     * than it initially seems. Properties of a cleanly displayed finished hand include:
     * <ul>
     *     <li> There are three sections of the hand: Closed, Open, and Winning Tile.
     *     <li> The section order is: Closed, Open, then Winning Tile.
     *     <li> The Winning Tile is never displayed as part of its set/pair
     *     <li> Each set/pair/winning-tile should be visually separated with a spacer
     *     <li> The pair is displayed at the end of the Closed section
     *     <li> Sets within the closed and open sections are sorted by their first tile
     *     <li> Tiles in the Open section are visually lower than the other two sections
     *     <li> Open Chiis obviously must be called from left-player
     *     <li> Open Pons/Kans will be displayed as called from across player if unknown
     *     <li> Closed Kans are displayed in the Open section
     *     <li> Closed Kans should show the back of the tile on tiles 1 and 4
     *     <li> AddedKan appear identical to OpenPon, with additional tile placed above called tile
     * </ul>
     */
    private void displayComplexHand() {
        addSetComplex(hand.meld1);
        addSetComplex(hand.meld2);
        addSetComplex(hand.meld3);
        addSetComplex(hand.meld4);
        addClosedSet( hand.pair);
        if( includeWinningTile && separateWinningTile ){
            addTileWinningTile();
        }
    }

    // Removes the specified tile from the list, so that the winning tile can be placed separately
    private List<Tile> withoutTile(List<Tile> tiles, Tile tile){
        List<Tile> tempList = new ArrayList<>();
        tempList.addAll(tiles);
        tempList.remove(tile);
        return tempList;
    }

    private void addSetComplex(Meld meld){
        if( meld.size()<3 || meld.size()>4 ){
            //This should never happen
            return;
        }

        Utils.MeldState meldState = Utils.getMeldState(meld);
        switch (meldState.toString()){
            case "INVALID":
                //This should never happen
                break;
            case "CLOSEDSET":
                addClosedSet(meld);
                break;
            case "OPENCHII":
                addOpenChii(meld);
                break;
            case "OPENPON":
                addOpenPon(meld);
                break;
            case "OPENKAN":
                addOpenKan(meld);
                break;
            case "ADDEDKAN":
                addAddedKan(meld);
                break;
            case "CLOSEDKAN":
                addClosedKan(meld);
                break;
        }
    }
    private void addClosedSet(Meld meld) {
        if( closedTilesContainer.getChildCount()!=0 ){
            addSpacer(closedTilesContainer, 8);
        }

        List<Tile> remainderSet;
        if( separateWinningTile ){
            remainderSet = withoutTile(meld.getTiles(), hand.getWinningTile());
        } else {
            remainderSet = meld.getTiles();
        }

        for( Tile t : remainderSet ){
            addTileClosed(t);
        }
    }
    private void addOpenChii(Meld meld) {
        addOpenPon(meld);      // Logically the same thing, except calledFrom always is LEFT
    }
    private void addOpenPon(Meld meld){
        addAddedKan(meld);     // Neat, don't have to copy code here, addedTile will just be null
    }
    private void addOpenKan(Meld meld) {
        addAddedKan(meld);     // ... I guess these are all the same thing if in a valid state
    }
    private void addAddedKan(Meld meld) {
        if( openTilesContainer.getChildCount()==0 ){
            addSpacer(openTilesContainer, 25);
        } else {
            addSpacer(openTilesContainer, 8);
        }

        Tile calledTile = meld.getCalledTile();
        Tile addedTile = meld.getAddedTile();

        List<Tile> remainderSet = withoutTile(meld.getTiles(), calledTile);
        remainderSet = withoutTile(remainderSet, addedTile);

        switch( calledTile.calledFrom.toString() ){
            case "LEFT":
                addTileCalled(calledTile, addedTile);
                addTileOpen(remainderSet.get(0));
                addTileOpen(remainderSet.get(1));
                if( remainderSet.size()==3 ){
                    addTileOpen(remainderSet.get(2));
                }
                break;
            case "CENTER":
                addTileOpen(remainderSet.get(0));
                addTileCalled(calledTile, addedTile);
                addTileOpen(remainderSet.get(1));
                if( remainderSet.size()==3 ){
                    addTileOpen(remainderSet.get(2));
                }
                break;
            case "RIGHT":
                addTileOpen(remainderSet.get(0));
                addTileOpen(remainderSet.get(1));
                addTileCalled(calledTile, addedTile);
                if( remainderSet.size()==3 ){
                    addTileOpen(remainderSet.get(2));
                }
                break;
        }
    }
    private void addClosedKan(Meld meld) {
        if( openTilesContainer.getChildCount()==0 ){
            addSpacer(openTilesContainer, 25);
        } else {
            addSpacer(openTilesContainer, 8);
        }

        Tile redFiveTile = null;
        for(Tile t : meld.getTiles()){
            if( t.red ){
                redFiveTile = t;
            }
        }

        meld.getTiles().get(0).faceDown = true;
        addTileOpen(meld.getTiles().get(0));
        addTileOpen(meld.getTiles().get(1));
        if( redFiveTile!=null ){
            addTileOpen(redFiveTile);
        } else {
            addTileOpen(meld.getTiles().get(2));
        }
        meld.getTiles().get(3).faceDown = true;
        addTileOpen(meld.getTiles().get(3));
    }

    private void addTileClosed(Tile t){
        TileDisplay tileDisplay = new TileDisplay(t, false);
        tileList.add(tileDisplay);

        tileDisplay.view.setOnClickListener(this);
        closedTilesContainer.addView(tileDisplay.view);
    }
    private void addTileOpen(Tile t){
        TileDisplay tileDisplay = new TileDisplay(t, false);
        tileList.add(tileDisplay);

        tileDisplay.view.setOnClickListener(this);
        openTilesContainer.addView(tileDisplay.view);
    }
    private void addTileCalled(Tile calledTile, Tile addedTile){
        LinearLayout container = new LinearLayout(activity);
        container.setOrientation(VERTICAL);
        container.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        container.setGravity(Gravity.BOTTOM);

        if( addedTile!=null ){
            TileDisplay aDisplay = new TileDisplay(addedTile, true);
            tileList.add(aDisplay);

            aDisplay.view.setOnClickListener(this);
            container.addView(aDisplay.view);
        }
        TileDisplay cDisplay = new TileDisplay(calledTile, true);
        tileList.add(cDisplay);

        cDisplay.view.setOnClickListener(this);
        container.addView(cDisplay.view);

        openTilesContainer.addView(container);
    }
    private void addTileWinningTile(){
        Tile winningTile = hand.getWinningTile();
        if( winningTile==null ){
            return;
        }
        addSpacer(winningTileContainer, 25);

        TileDisplay tileDisplay = new TileDisplay(winningTile, false);
        tileList.add(tileDisplay);

        tileDisplay.view.setOnClickListener(this);
        winningTileContainer.addView(tileDisplay.view);
    }

    /**
     * To visually separate sets/pair, a spacer really helps
     * @param view Add a spacer to the end of this view, in preparation to add a new set
     */
    private void addSpacer(LinearLayout view, int width){
        Space spacer = new Space(getContext());
        spacer.setMinimumWidth(width);
        view.addView(spacer);
    }

    class TileDisplay {
        public String value;
        public String suit;
        public Tile tile;
        public ImageView view;

        public TileDisplay(Tile t, boolean rotated){
            tile = t;
            value = tile.value;
            suit = tile.suit.toString();
            view = getUtils().getHandDisplayTileView(tile, rotated);
        }
    }
    private TileDisplay getTileDisplay(ImageView imageView){
        for( TileDisplay tileDisplay : tileList ){
            if( tileDisplay.view == imageView ){
                return tileDisplay;
            }
        }
        return null;
    }


    Utils _utils;
    private Utils getUtils(){
        if( _utils==null && !isInEditMode() ){
            _utils = ((MainActivity) activity).getUtils();
        }
        return _utils;
    }
}
