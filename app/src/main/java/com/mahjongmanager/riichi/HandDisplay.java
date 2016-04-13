package com.mahjongmanager.riichi;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HandDisplay extends LinearLayout {
    Context context;

    private LinearLayout closedTilesContainer;
    private LinearLayout openTilesContainer;
    private LinearLayout winningTileContainer;
    private TextView tileText;

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

        tileText = (TextView) findViewById(R.id.tileText);
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
            tileText.setText(hand.toString());
            displaySimpleHand();
        } else {
            tileText.setText(hand.printAllSets());
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
        addSetComplex(hand.set1);
        addSetComplex(hand.set2);
        addSetComplex(hand.set3);
        addSetComplex(hand.set4);
        addPairComplex(withoutTile(hand.pair, hand.getWinningTile()));
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

    private void addSetComplex(List<Tile> tiles){
        if( tiles.size()<3 || tiles.size()>4 ){
            //This should never happen
            return;
        }

        Utils.SetState setState = Utils.getSetState(tiles);
        switch (setState.toString()){
            case "INVALID":
                //This should never happen
                break;
            case "CLOSEDSET":
                addClosedSet(withoutTile(tiles, hand.getWinningTile()));
                break;
            case "OPENCHII":
                addOpenChii(tiles);
                break;
            case "OPENPON":
                addOpenPon(tiles);
                break;
            case "OPENKAN":
                addOpenKan(tiles);
                break;
            case "ADDEDKAN":
                addAddedKan(tiles);
                break;
            case "CLOSEDKAN":
                addClosedKan(tiles);
                break;
        }
    }
    private void addClosedSet(List<Tile> set) {
        if( closedTilesContainer.getChildCount()!=0 ){
            addSpacer(closedTilesContainer, 8);
        }
        for( Tile t : set ){
            addTileClosed(t);
        }
    }
    private void addPairComplex(List<Tile> pair){
        if( closedTilesContainer.getChildCount()!=0 ){
            addSpacer(closedTilesContainer, 8);
        }
        for( Tile t : pair ){
            addTileClosed(t);
        }
    }
    private void addOpenChii(List<Tile> tiles) {
        addOpenPon(tiles);      // Logically the same thing, except calledFrom always is LEFT
    }
    private void addOpenPon(List<Tile> tiles){
        addAddedKan(tiles);     // Neat, don't have to copy code here, addedTile will just be null
    }
    private void addOpenKan(List<Tile> tiles) {
        addAddedKan(tiles);     // ... I guess these are all the same thing if in a valid state
    }
    private void addAddedKan(List<Tile> tiles) {
        if( openTilesContainer.getChildCount()==0 ){
            addSpacer(openTilesContainer, 25);
        } else {
            addSpacer(openTilesContainer, 8);
        }

        Tile calledTile = Utils.getCalledTile(tiles);
        Tile addedTile = Utils.getAddedTile(tiles);

        List<Tile> remainderSet = withoutTile(tiles, calledTile);
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
    private void addClosedKan(List<Tile> tiles) {
        if( openTilesContainer.getChildCount()==0 ){
            addSpacer(openTilesContainer, 25);
        } else {
            addSpacer(openTilesContainer, 8);
        }

        Tile redFiveTile = null;
        for(Tile t : tiles){
            if( t.red ){
                redFiveTile = t;
            }
        }

        tiles.get(0).faceDown = true;
        addTileOpen(tiles.get(0));
        addTileOpen(tiles.get(1));
        if( redFiveTile!=null ){
            addTileOpen(redFiveTile);
        } else {
            addTileOpen(tiles.get(2));
        }
        tiles.get(3).faceDown = true;
        addTileOpen(tiles.get(3));
    }

    private void addTileClosed(Tile t){
        TextView view = getUtils().getTileView(t);
        closedTilesContainer.addView(view);
    }
    private void addTileOpen(Tile t){
        TextView view = getUtils().getTileView(t);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 20, 0, 0);
        view.setLayoutParams(lp);

        openTilesContainer.addView(view);
    }
    private void addTileCalled(Tile calledTile, Tile addedTile){}   // TODO
    private void addTileWinningTile(){
        addSpacer(winningTileContainer, 25);

        Tile winningTile = hand.getWinningTile();
        TextView view = getUtils().getTileView(winningTile);
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
