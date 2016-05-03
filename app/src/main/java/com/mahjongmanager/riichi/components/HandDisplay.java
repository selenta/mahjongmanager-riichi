package com.mahjongmanager.riichi.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import com.mahjongmanager.riichi.Hand;
import com.mahjongmanager.riichi.MainActivity;
import com.mahjongmanager.riichi.Meld;
import com.mahjongmanager.riichi.R;
import com.mahjongmanager.riichi.Tile;
import com.mahjongmanager.riichi.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class HandDisplay extends LinearLayout {
    Context context;

    private LinearLayout closedTilesContainer;
    private LinearLayout openTilesContainer;
    private LinearLayout winningTileContainer;

    private Hand hand;
    private Boolean includeWinningTile = true;

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
        context = ctx;
        initializeView();
    }

    private void initializeView(){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.component_handdisplay, this);

        closedTilesContainer = (LinearLayout) findViewById(R.id.closedTilesContainer);
        openTilesContainer   = (LinearLayout) findViewById(R.id.openTilesContainer);
        winningTileContainer = (LinearLayout) findViewById(R.id.winningTileContainer);

        //TODO If component is configured incorrectly, display error message, instead of blank
        if(context==null){
            return;
        }
    }

    ///////////////////////////////////////////
    /////////////     Main     ////////////////
    ///////////////////////////////////////////
    /**
     * Change whether or not the HandDisplay will include the winning tile (only set to false
     * if you intend to manually display the winning tile somewhere else). Defaults to true.
     * @param bool Display winning tile
     */
    public void setIncludeWinningTile(boolean bool){
        includeWinningTile = bool;
    }

    public void setHand(Hand h){
        hand = h;
        displayHand();
    }
    private void displayHand(){
        if( hand==null ){
            return;
        }
        // Only display complex hand if the hand is a valid winning hand of normal structure
        if( hand.hasAbnormalStructure() || !hand.validateCompleteState() ){
            displaySimpleHand();
        } else {
            displayComplexHand();
        }
    }
    /**
     * Adds the tiles to HandDisplay with no spacing or distinction for open/called/winning-tile
     */
    private void displaySimpleHand(){
        closedTilesContainer.removeAllViews();
        openTilesContainer.removeAllViews();
        winningTileContainer.removeAllViews();
        for( Tile t : hand.tiles ){
            addTileClosed(t);
        }
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
     *
     */
    private void displayComplexHand() {
        addSetComplex(hand.meld1);
        addSetComplex(hand.meld2);
        addSetComplex(hand.meld3);
        addSetComplex(hand.meld4);
        addClosedSet( hand.pair);
        if( includeWinningTile ){
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

        Utils.SetState setState = Utils.getSetState(meld);
        switch (setState.toString()){
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
        List<Tile> remainderSet = withoutTile(meld.getTiles(), hand.getWinningTile());
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
        ImageView view = getUtils().getHandDisplayTileView(t, false);
        closedTilesContainer.addView(view);
    }
    private void addTileOpen(Tile t){
        ImageView view = getUtils().getHandDisplayTileView(t, false);

        openTilesContainer.addView(view);
    }
    private void addTileCalled(Tile calledTile, Tile addedTile){
        LinearLayout container = new LinearLayout(context);
        container.setOrientation(VERTICAL);
        container.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        container.setGravity(Gravity.BOTTOM);

        if( addedTile!=null ){
            ImageView aTile = getUtils().getHandDisplayTileView(addedTile, true);
            container.addView(aTile);
        }
        ImageView cTile = getUtils().getHandDisplayTileView(calledTile, true);
        container.addView(cTile);

        openTilesContainer.addView(container);
    }
    private void addTileWinningTile(){
        addSpacer(winningTileContainer, 25);

        Tile winningTile = hand.getWinningTile();
        ImageView view = getUtils().getHandDisplayTileView(winningTile, false);
        winningTileContainer.addView(view);
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

    Utils _utils;
    public Utils getUtils(){
        if( _utils==null ){
            _utils = ((MainActivity)context).getUtils();
        }
        return _utils;
    }
}
