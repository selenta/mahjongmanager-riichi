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

    private LinearLayout tileList;
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
        tileList = (LinearLayout) findViewById(R.id.tileList);

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
        if( !hand.validateCompleteState() || hasAbnormalStructure(hand) ){
            tileText.setText(hand.toString());
            displaySimpleHand();
        } else {
            tileText.setText(hand.printAllSets());
            displayComplexHand();
        }
    }

    private void displaySimpleHand(){
        tileList.removeAllViews();
        addTiles(hand.tiles);
    }

    private void displayComplexHand(){
        Tile winningTile = hand.getWinningTile();

        addTiles(withoutTile(hand.set1, winningTile));
        addSpacer();
        addTiles(withoutTile(hand.set2, winningTile));
        addSpacer();
        addTiles(withoutTile(hand.set3, winningTile));
        addSpacer();
        addTiles(withoutTile(hand.set4, winningTile));
        addSpacer();
        addTiles(withoutTile(hand.pair, winningTile));
        if( includeWinningTile ){
            addSpacer();
            addTile(winningTile);
        }
    }

    private List<Tile> withoutTile(List<Tile> tiles, Tile tile){
        List<Tile> tempList = new ArrayList<>();
        tempList.addAll(tiles);
        tempList.remove(tile);
        return tempList;
    }

    private void addTiles(List<Tile> tiles){
        for( Tile t : tiles ){
            addTile(t);
        }
    }

    private void addTile(Tile t){
        TextView view = ((MainActivity)context).getUtils().getTileView(t);
        tileList.addView(view);
    }

    private void addSpacer(){
        Space spacer = new Space(getContext());
        spacer.setMinimumWidth(15);
        tileList.addView(spacer);
    }

    private boolean hasAbnormalStructure(Hand h){
        if( hand.kokushiMusou || hand.kokushiMusou13wait || hand.chiiToitsu ){
            return true;
        }
        return false;
    }
}
