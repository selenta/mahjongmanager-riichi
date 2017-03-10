package com.mahjongmanager.riichi.components;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import com.mahjongmanager.riichi.MainActivity;
import com.mahjongmanager.riichi.R;
import com.mahjongmanager.riichi.ScoreCalculator;
import com.mahjongmanager.riichi.common.Hand;
import com.mahjongmanager.riichi.common.Round;
import com.mahjongmanager.riichi.common.Tile;
import com.mahjongmanager.riichi.common.TileSet;
import com.mahjongmanager.riichi.utils.AppSettings;
import com.mahjongmanager.riichi.utils.Log;
import com.mahjongmanager.riichi.utils.Utils;

import java.util.Arrays;

public class DrawDiscard extends LinearLayout implements View.OnClickListener {
    private Fragment parentFragment;

    // Round/general info
    private TextView title;
    private TextView remainingTiles;
    private TextView remainingTurns;
    private LinearLayout prevailingWindTileImage;
    private LinearLayout playerWindTileImage;

    // Main
    private DiscardPile discardPile;
    private TextView currentHandInstructions;
    private HandDisplay handDisplay;

    // Confirm
    private LinearLayout confirmTileContainer;
    private LinearLayout confirmDiscardContainer;
    private HandDisplay confirmTileHandDisplay;
    private TextView confirmTileTooltip;
    private LinearLayout selectClosedKanContainer;
    private HandDisplay selectKanHandDisplay;

    // Buttons/actions
    private LinearLayout callTileButtons;
    private Button callChiiButton;
    private Button callPonButton;
    private Button callKanButton;

    private LinearLayout selfDrawContainer;
    private Button declareTsumoButton;
    private Button declareTsumoPassButton;

    private Button scoreHandButton;

    private LinearLayout playerTurnActionContainer;
    private Button declareKanButton;
    private Space declareKanSpacer;
    private Button confirmDiscardButton;

    private Button cancelClosedKanButton;

    // Data
    private Round round;
    private Tile.Wind playerWind;
    private Hand playerHand;

    private Tile lastDraw;
    private Tile stagedDiscard;

    private static final int BUTTONS_DISCARD = 1;
    private static final int BUTTONS_CALLING = 2;
    private static final int BUTTONS_TSUMO = 3;
    private static final int BUTTONS_SCORE = 4;
    private static final int BUTTONS_CLOSED_KAN = 5;

    private boolean pendingTsumoChoice = false;
    private boolean tooltipsEnabled = false;
    private boolean pendingClosedKanChoice = false;

    public DrawDiscard(Context ctx){
        this(ctx, null);
    }
    public DrawDiscard(Context ctx, AttributeSet attrs) {
        this(ctx, attrs, 0);
    }
    public DrawDiscard(Context ctx, AttributeSet attrs, int defStyle ){
        super(ctx, attrs, defStyle);

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View myInflatedView = inflater.inflate(R.layout.component_drawdiscard, this);
        assignUIElements(myInflatedView);
        checkTextSize();

        checkTooltips();
    }

    public void setRound(Round r, Tile.Wind pWind){
        round = r;
        playerWind = pWind;
        playerHand = round.getHand(playerWind);

        setTileImage(prevailingWindTileImage, new Tile(round.getRoundWind()));
        setTileImage(playerWindTileImage, new Tile(playerWind));

        proceedUntilPlayerTurn();
        startPlayerTurn();

        updateWallCounters();
        updateButtonState();
    }
    public void setParentFragment(Fragment frag){
        parentFragment = frag;
        if( parentFragment instanceof OnClickListener ){
            scoreHandButton.setOnClickListener((OnClickListener) parentFragment);
            declareTsumoButton.setOnClickListener((OnClickListener) parentFragment);
        }
    }
    public void setTitle(String t){
        title.setText(t);
    }
    public void declareTsumo(){
        round.declareTsumo();
        ((MainActivity)getContext()).setCurrentRound(round);
        ((MainActivity)getContext()).setCurrentHand(playerHand);
    }

    /////////////////////////////////////////
    ///////////       View       ////////////
    /////////////////////////////////////////
    @Override
    public void onClick(View v) {
        if( v.getClass()==ImageView.class ){
            Tile kanTile = selectKanHandDisplay.getTileFromImage((ImageView) v);
            if( pendingClosedKanChoice && kanTile!=null ){
                closedKan(kanTile);
                updateWallCounters();
                updateButtonState();
                return;
            }

            Tile tile = handDisplay.getTileFromImage((ImageView) v);
            selectTileForConfirmation(tile);
        }

        switch (v.getId()) {
            case R.id.callChiiButton:
                break;
            case R.id.callPonButton:
                break;
            case R.id.callKanButton:
                break;
            case R.id.confirmDiscardButton:
                confirmDiscard();
                break;
            case R.id.tsumoPassButton:
                pendingTsumoChoice = false;
                selectTileForConfirmation(lastDraw);
                break;
            case R.id.declareKanButton:
                pendingClosedKanChoice = true;
                break;
            case R.id.cancelClosedKanButton:
                pendingClosedKanChoice = false;
                break;
        }
        updateWallCounters();
        updateButtonState();
    }

    private void updateWallCounters(){
        int rt = round.tilesInWall();
        remainingTiles.setText( "" + rt + " tiles" );
        remainingTurns.setText( "" + rt/4 + " turns" );
    }
    private void updateButtonState(){
        Tile.Wind currP = round.getActivePlayerWind();

        if( pendingTsumoChoice ){
            setButtonState(BUTTONS_TSUMO);
        } else if( pendingClosedKanChoice ){
            setButtonState(BUTTONS_CLOSED_KAN);
            displayKanOptions();
        } else if( round.isOver() ){
            ((MainActivity)getContext()).setCurrentRound(round);
            ((MainActivity)getContext()).setCurrentHand(playerHand);
            setButtonState(BUTTONS_SCORE);
        } else if( currP == playerWind){
            setButtonState(BUTTONS_DISCARD);
        } else {
            setButtonState(BUTTONS_CALLING);
        }
    }
    private void setButtonState(int s){
        currentHandInstructions.setVisibility(INVISIBLE);
        callTileButtons.setVisibility(GONE);
        confirmTileContainer.setVisibility(INVISIBLE);
        confirmDiscardContainer.setVisibility(INVISIBLE);
        selectClosedKanContainer.setVisibility(GONE);
        playerTurnActionContainer.setVisibility(GONE);
        confirmDiscardButton.setVisibility(GONE);
        scoreHandButton.setVisibility(GONE);
        selfDrawContainer.setVisibility(GONE);
        declareKanButton.setVisibility(GONE);
        declareKanSpacer.setVisibility(GONE);
        cancelClosedKanButton.setVisibility(GONE);

        switch (s){
            case BUTTONS_SCORE:
                scoreHandButton.setVisibility(VISIBLE);
                break;
            case BUTTONS_DISCARD:
                currentHandInstructions.setVisibility(VISIBLE);
                confirmTileContainer.setVisibility(VISIBLE);
                confirmDiscardContainer.setVisibility(VISIBLE);
                playerTurnActionContainer.setVisibility(VISIBLE);
                confirmDiscardButton.setVisibility(VISIBLE);
                if( stagedDiscard==null ){
                    confirmDiscardButton.setEnabled(false);
                } else {
                    confirmDiscardButton.setEnabled(true);
                }
                if( playerHand.couldClosedKan() ){
                    declareKanButton.setVisibility(VISIBLE);
                    declareKanSpacer.setVisibility(VISIBLE);
                }
                break;
            case BUTTONS_CALLING:
                callTileButtons.setVisibility(VISIBLE);
                break;
            case BUTTONS_TSUMO:
                selfDrawContainer.setVisibility(VISIBLE);
                break;
            case BUTTONS_CLOSED_KAN:
                currentHandInstructions.setVisibility(INVISIBLE);
                confirmTileContainer.setVisibility(VISIBLE);
                confirmDiscardContainer.setVisibility(GONE);
                selectClosedKanContainer.setVisibility(VISIBLE);
                cancelClosedKanButton.setVisibility(VISIBLE);
                break;
        }
    }

    private void setTileImage(LinearLayout container, Tile t){
        ImageView tileImage = Utils.getHandDisplayTileView(t, false);
        container.removeAllViews();
        container.addView(tileImage);
        container.setVisibility(View.VISIBLE);
    }

    private void displayKanOptions(){
        TileSet kanOptions = new TileSet();
        for( Tile t : playerHand.unsortedTiles ){
            if( Utils.findTiles(playerHand.unsortedTiles, t).size()==4 ){
                kanOptions.add(t);
            }
        }

        Hand dummyHand = new Hand( kanOptions.toList() );
        for( Tile t : dummyHand.tiles ){
            dummyHand.unsortedTiles.remove(t);
            dummyHand.setMeld( Arrays.asList(t) );
        }
        selectKanHandDisplay.setHand(dummyHand);
    }

    /////////////////////////////////////////
    ///////////      Model       ////////////
    /////////////////////////////////////////
    private void selectTileForConfirmation(Tile t){
        if( pendingTsumoChoice || t==null ){
            return;
        } else if( !playerHand.unsortedTiles.contains(t) ){
            return;
        }


        stagedDiscard = t;
        confirmTileHandDisplay.setHand( new Hand(Arrays.asList(stagedDiscard)) );
        if( tooltipsEnabled ){
            confirmTileTooltip.setText("("+stagedDiscard.toString()+")");
        }
    }

    private void confirmDiscard(){
        if( !playerHand.tiles.contains(stagedDiscard) ){
            Log.e("confirmDiscard", "Player hand does not contain the staged tile!: "+stagedDiscard + " - "+playerHand);
        }
        round.discard(stagedDiscard);
        discardPile.addTile(stagedDiscard);
        stagedDiscard = null;

        round.endTurn();
        proceedUntilPlayerTurn();
        startPlayerTurn();
    }
    private void proceedUntilPlayerTurn(){
        while( round.getActivePlayerWind()!=playerWind && !round.isOver()){
            round.draw();
            round.discard(round.getLastDraw());
            round.endTurn();
        }
    }
    private void startPlayerTurn(){
        if( !round.isOver() ){
            round.draw();
            lastDraw = round.getLastDraw();
            handDisplay.setHand(playerHand, lastDraw);
        } else {
            handDisplay.setHand(playerHand);
        }

        ScoreCalculator sc = new ScoreCalculator(playerHand);
        if( sc.scoredHand!=null ){
            pendingTsumoChoice = true;
            setButtonState(BUTTONS_TSUMO);
        } else {
            selectTileForConfirmation(lastDraw);
        }
    }

    private void closedKan(Tile kanTile){
        round.declareClosedKan(kanTile);
        pendingClosedKanChoice = false;

        startPlayerTurn();
    }

    /////////////////////////////////////////
    ///////////       Init       ////////////
    /////////////////////////////////////////
    private void assignUIElements(View myInflatedView){
        // Round/general info
        title = (TextView) myInflatedView.findViewById(R.id.handBuilder4pTitle);
        remainingTiles = (TextView) myInflatedView.findViewById(R.id.remainingTiles);
        remainingTurns = (TextView) myInflatedView.findViewById(R.id.estimatedDraws);
        prevailingWindTileImage = (LinearLayout) myInflatedView.findViewById(R.id.prevailingWindTileContainer);
        playerWindTileImage     = (LinearLayout) myInflatedView.findViewById(R.id.playerWindTileContainer);

        // Main
        discardPile = (DiscardPile) myInflatedView.findViewById(R.id.discardPile);
        currentHandInstructions = (TextView) myInflatedView.findViewById(R.id.currentHandInstructions);
        handDisplay = (HandDisplay) myInflatedView.findViewById(R.id.handDisplay);
        handDisplay.setParentLayout(this);

        // Confirm
        confirmTileContainer = (LinearLayout) myInflatedView.findViewById(R.id.confirmTileContainer);
        confirmDiscardContainer = (LinearLayout) myInflatedView.findViewById(R.id.confirmDiscardContainer);
        confirmTileHandDisplay = (HandDisplay) myInflatedView.findViewById(R.id.confirmTileImage);
        confirmTileTooltip = (TextView) myInflatedView.findViewById(R.id.confirmTileTooltip);
        selectClosedKanContainer = (LinearLayout) myInflatedView.findViewById(R.id.selectClosedKanContainer);
        selectKanHandDisplay = (HandDisplay) myInflatedView.findViewById(R.id.selectKanHandDisplay);
        selectKanHandDisplay.setParentLayout(this);

        // Buttons/actions
        callTileButtons = (LinearLayout) myInflatedView.findViewById(R.id.callTileButtons);
        callChiiButton = (Button) myInflatedView.findViewById(R.id.callChiiButton);
        callChiiButton.setOnClickListener(this);
        callPonButton = (Button) myInflatedView.findViewById(R.id.callPonButton);
        callPonButton.setOnClickListener(this);
        callKanButton = (Button) myInflatedView.findViewById(R.id.callKanButton);
        callKanButton.setOnClickListener(this);

        selfDrawContainer = (LinearLayout) myInflatedView.findViewById(R.id.selfDrawContainer);
        declareTsumoButton = (Button) myInflatedView.findViewById(R.id.tsumoButton);
        declareTsumoPassButton = (Button) myInflatedView.findViewById(R.id.tsumoPassButton);
        declareTsumoPassButton.setOnClickListener(this);

        scoreHandButton = (Button) myInflatedView.findViewById(R.id.scoreHandButton);

        playerTurnActionContainer = (LinearLayout) myInflatedView.findViewById(R.id.playerTurnContainer);
        declareKanButton = (Button) myInflatedView.findViewById(R.id.declareKanButton);
        declareKanButton.setOnClickListener(this);
        declareKanSpacer = (Space) myInflatedView.findViewById(R.id.declareKanSpacer);
        confirmDiscardButton = (Button) myInflatedView.findViewById(R.id.confirmDiscardButton);
        confirmDiscardButton.setOnClickListener(this);

        cancelClosedKanButton = (Button) myInflatedView.findViewById(R.id.cancelClosedKanButton);
        cancelClosedKanButton.setOnClickListener(this);
    }
    private void checkTextSize(){
        if( Utils.SCREEN_SIZE < 1024 ){
            title.setTextSize(title.getTextSize()*0.55f);
        }
    }
    private void checkTooltips(){
        tooltipsEnabled = AppSettings.getDrawDiscardTooltips();
        if( tooltipsEnabled ){
            confirmTileTooltip.setVisibility(VISIBLE);
        }
    }
}
