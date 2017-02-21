package com.mahjongmanager.riichi.components;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mahjongmanager.riichi.MainActivity;
import com.mahjongmanager.riichi.R;
import com.mahjongmanager.riichi.ScoreCalculator;
import com.mahjongmanager.riichi.common.Hand;
import com.mahjongmanager.riichi.common.Round;
import com.mahjongmanager.riichi.common.Tile;
import com.mahjongmanager.riichi.utils.ImageCache;
import com.mahjongmanager.riichi.utils.Log;
import com.mahjongmanager.riichi.utils.Utils;

public class DrawDiscard extends LinearLayout implements View.OnClickListener {
    private Fragment parentFragment;

    private DiscardPile discardPile;
    private HandDisplay handDisplay;

    private TextView title;
    private TextView remainingTiles;
    private TextView remainingTurns;
    private TextView currentHandInstructions;
    private LinearLayout prevailingWindTileImage;
    private LinearLayout playerWindTileImage;
    private LinearLayout confirmDiscardImage;
    private LinearLayout lastDrawImage;

    private LinearLayout confirmDiscardContainer;
    private LinearLayout selfDrawContainer;
    private LinearLayout callTileButtons;

    private Button chiiDiscardButton;
    private Button ponDiscardButton;
    private Button kanDiscardButton;
    private Button confirmDiscardButton;
    private Button scoreHandButton;
    private Button tsumoButton;
    private Button passButton;

    private Round round;
    private Tile.Wind playerWind;
    private Hand playerHand;

    private Tile lastDraw;
    private Tile stagedDiscard;

    private static final int BUTTONS_DISCARD = 1;
    private static final int BUTTONS_CALLING = 2;
    private static final int BUTTONS_TSUMO = 3;
    private static final int BUTTONS_SCORE = 4;

    private boolean pendingTsumoChoice = false;


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

        initHandDisplaySettings();
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
            tsumoButton.setOnClickListener((OnClickListener) parentFragment);
        }
    }
    public void setTitle(String t){
        title.setText(t);
    }
    public void declareTsumo(){
        round.declareTsumo();
        ((MainActivity)getContext()).setCurrentHand(playerHand);
    }

    /////////////////////////////////////////
    ///////////       View       ////////////
    /////////////////////////////////////////
    @Override
    public void onClick(View v) {
        if( v.getClass()==ImageView.class ){
            Tile tile = handDisplay.getTileFromImage((ImageView) v);
            selectDiscard(tile);
        }

        switch (v.getId()) {
            case R.id.chiiDiscardButton:
                break;
            case R.id.ponDiscardButton:
                break;
            case R.id.kanDiscardButton:
                break;
            case R.id.lastDrawImage:
                selectDiscard(lastDraw);
                break;
            case R.id.confirmDiscardButton:
                confirmDiscard();
                break;
            case R.id.passButton:
                pendingTsumoChoice = false;
                selectDiscard(lastDraw);
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

        if(pendingTsumoChoice){
            setButtonState(BUTTONS_TSUMO);
        } else if( round.isOver() ){
            currentHandInstructions.setVisibility(INVISIBLE);
            ((MainActivity)getContext()).setCurrentHand(playerHand);
            setButtonState(BUTTONS_SCORE);
        } else if( currP == playerWind){
            setButtonState(BUTTONS_DISCARD);
        } else {
            setButtonState(BUTTONS_CALLING);
        }
    }
    private void setButtonState(int s){
        switch (s){
            case BUTTONS_SCORE:
                callTileButtons.setVisibility(GONE);
                confirmDiscardContainer.setVisibility(INVISIBLE);
                confirmDiscardButton.setVisibility(GONE);
                scoreHandButton.setVisibility(VISIBLE);
                selfDrawContainer.setVisibility(GONE);
                break;
            case BUTTONS_DISCARD:
                callTileButtons.setVisibility(GONE);
                confirmDiscardContainer.setVisibility(VISIBLE);
                confirmDiscardButton.setVisibility(VISIBLE);
                if( stagedDiscard==null ){
                    confirmDiscardButton.setEnabled(false);
                } else {
                    confirmDiscardButton.setEnabled(true);
                }
                scoreHandButton.setVisibility(GONE);
                selfDrawContainer.setVisibility(GONE);
                break;
            case BUTTONS_CALLING:
                callTileButtons.setVisibility(VISIBLE);
                confirmDiscardContainer.setVisibility(INVISIBLE);
                confirmDiscardButton.setVisibility(GONE);
                scoreHandButton.setVisibility(GONE);
                selfDrawContainer.setVisibility(GONE);
                break;
            case BUTTONS_TSUMO:
                callTileButtons.setVisibility(GONE);
                confirmDiscardContainer.setVisibility(INVISIBLE);
                confirmDiscardButton.setVisibility(GONE);
                scoreHandButton.setVisibility(GONE);
                selfDrawContainer.setVisibility(VISIBLE);
                break;
        }
    }

    private void setTileImage(LinearLayout container, Tile t){
        ImageView tileImage = Utils.getHandDisplayTileView(t, false);
        container.removeAllViews();
        container.addView(tileImage);
        container.setVisibility(View.VISIBLE);
    }

    /////////////////////////////////////////
    ///////////      Model       ////////////
    /////////////////////////////////////////
    private void selectDiscard(Tile t){
        if( pendingTsumoChoice ){
            return;
        }
        stagedDiscard = t;
        setTileImage(confirmDiscardImage, stagedDiscard);
    }

    private void confirmDiscard(){
        if( !playerHand.tiles.contains(stagedDiscard) ){
            Log.e("confirmDiscard", "Player hand does not contain the staged tile!: "+stagedDiscard + " - "+playerHand);
        }
        round.discard(stagedDiscard);
        discardPile.addTile(stagedDiscard);
        stagedDiscard = null;
        confirmDiscardImage.setVisibility(INVISIBLE);

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
        handDisplay.setHand(playerHand);
        if( !round.isOver() ){
            round.draw();
            lastDraw = round.getLastDraw();
            setTileImage(lastDrawImage, lastDraw);
        } else {
            lastDrawImage.setVisibility(INVISIBLE);
        }

        ScoreCalculator sc = new ScoreCalculator(playerHand);
        if( sc.scoredHand!=null ){
            pendingTsumoChoice = true;
            setButtonState(BUTTONS_TSUMO);
        } else {
            selectDiscard(lastDraw);
        }
    }


    /////////////////////////////////////////
    ///////////       Init       ////////////
    /////////////////////////////////////////
    private void assignUIElements(View myInflatedView){
        title = (TextView) myInflatedView.findViewById(R.id.handBuilder4pTitle);
        remainingTiles = (TextView) myInflatedView.findViewById(R.id.remainingTiles);
        remainingTurns = (TextView) myInflatedView.findViewById(R.id.estimatedDraws);

        prevailingWindTileImage = (LinearLayout) myInflatedView.findViewById(R.id.prevailingWindTileContainer);
        playerWindTileImage = (LinearLayout) myInflatedView.findViewById(R.id.playerWindTileContainer);

        discardPile = (DiscardPile) myInflatedView.findViewById(R.id.discardPile);

        currentHandInstructions = (TextView) myInflatedView.findViewById(R.id.currentHandInstructions);
        handDisplay = (HandDisplay) myInflatedView.findViewById(R.id.handDisplay);
        handDisplay.setParentLayout(this);
        lastDrawImage = (LinearLayout) myInflatedView.findViewById(R.id.lastDrawImage);
        lastDrawImage.setOnClickListener(this);
        lastDrawImage.addView(Utils.getHandDisplayPlaceholderTileView());
        lastDrawImage.setVisibility(INVISIBLE);

        confirmDiscardContainer = (LinearLayout) myInflatedView.findViewById(R.id.confirmDiscardContainer);
        confirmDiscardImage = (LinearLayout) myInflatedView.findViewById(R.id.confirmDiscardImage);
        confirmDiscardImage.addView(Utils.getHandDisplayPlaceholderTileView());
        confirmDiscardImage.setVisibility(INVISIBLE);
        confirmDiscardButton = (Button) myInflatedView.findViewById(R.id.confirmDiscardButton);
        confirmDiscardButton.setOnClickListener(this);

        selfDrawContainer = (LinearLayout) myInflatedView.findViewById(R.id.selfDrawContainer);
        tsumoButton = (Button) myInflatedView.findViewById(R.id.tsumoButton);
        passButton = (Button) myInflatedView.findViewById(R.id.passButton);
        passButton.setOnClickListener(this);

        callTileButtons = (LinearLayout) myInflatedView.findViewById(R.id.callTileButtons);
        chiiDiscardButton = (Button) myInflatedView.findViewById(R.id.chiiDiscardButton);
        chiiDiscardButton.setOnClickListener(this);
        ponDiscardButton = (Button) myInflatedView.findViewById(R.id.ponDiscardButton);
        ponDiscardButton.setOnClickListener(this);
        kanDiscardButton = (Button) myInflatedView.findViewById(R.id.kanDiscardButton);
        kanDiscardButton.setOnClickListener(this);

        scoreHandButton = (Button) myInflatedView.findViewById(R.id.scoreHandButton);
    }
    private void checkTextSize(){
        if( Utils.SCREEN_SIZE < 1024 ){
            title.setTextSize(title.getTextSize()*0.55f);
        }
    }
    private void initHandDisplaySettings(){
        handDisplay.setState(HandDisplay.YAKU_DESCRIPTION);

        // To make the padding around the lastDrawImage look identical to the handDisplay
        int hdp = Utils.SCREEN_SIZE / 200 + 1;
        lastDrawImage.setPadding(hdp, hdp, hdp, hdp);
        int size = Utils.getActualTileWidth(ImageCache.HAND_DISPLAY_KEY);
        lastDrawImage.setMinimumHeight(size * 14/10 + 10);
    }
}
