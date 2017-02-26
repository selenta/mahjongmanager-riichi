package com.mahjongmanager.riichi.handbuilder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.mahjongmanager.riichi.R;
import com.mahjongmanager.riichi.common.Hand;
import com.mahjongmanager.riichi.common.Tile;
import com.mahjongmanager.riichi.components.DiscardPile;
import com.mahjongmanager.riichi.components.HandDisplay;
import com.mahjongmanager.riichi.components.ScoreScreen;
import com.mahjongmanager.riichi.utils.TutorialLibrary;
import com.mahjongmanager.riichi.utils.Log;
import com.mahjongmanager.riichi.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TutorialExplanation extends Fragment implements View.OnClickListener {
    private TextView discardPileLabel;
    private DiscardPile discardPile;
    private HandDisplay handDisplay;
    private HandDisplay lastDrawTile;
    private LinearLayout recommendedDiscardImage;
    private LinearLayout explanationContainer;
    private TableLayout explanationTable;

    private ScoreScreen scoreScreen;

    private List<Tile> allDraws;
    private List<Tile> expectedDiscards;

    private List<Tile> discards = new ArrayList<>();
    private Hand hand;
    private Tile lastDraw;

    private int currentDiscard = 0;

    private Button backButton;
    private Button nextButton;
    private Button mainMenuButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Yeah, I know I could create a new layout... but why?
        View myInflatedView = inflater.inflate(R.layout.fragment_handbuilder_tutorial_3explanation, container, false);
        assignUIElements(myInflatedView);

        initData();
        updateButtons();
        updateDisplays();

        return myInflatedView;
    }

    /////////////////////////////////////////
    ///////////       View       ////////////
    /////////////////////////////////////////
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backButton:
                loadPrevDiscard();
                break;
            case R.id.nextButton:
                loadNextDiscard();
                break;
        }
        updateButtons();
        updateDisplays();
    }
    private void updateButtons(){
        if( currentDiscard==0 ){
            backButton.setEnabled(false);
        } else if( currentDiscard==18 ){
            nextButton.setEnabled(false);
            mainMenuButton.setVisibility(View.VISIBLE);
        } else {
            backButton.setEnabled(true);
            nextButton.setEnabled(true);
            mainMenuButton.setVisibility(View.GONE);
        }
    }
    private void updateDisplays(){
        if( currentDiscard>=18 ){
            return;
        }

        TutorialLibrary.DiscardDetails dd = TutorialLibrary.allDiscardDetails.get(currentDiscard);
        if( dd.number!=currentDiscard+1 ){
            Log.w("updateDisplays", "Currently loaded DiscardDetails doesn't match: "+dd.number+" - "+(currentDiscard+1));
        }
        explanationTable.removeAllViews();
        for(int i=0; i<dd.tiles.size(); i++){
            TableRow newRow = new TableRow( getContext() );
            newRow.setGravity(Gravity.CENTER);
            newRow.setPadding(0,10,0,10);

            //Tile display
            HandDisplay hd = new HandDisplay(getActivity());
            newRow.addView( hd );
            hd.setHand( new Hand(dd.tiles.get(i)) );
            hd.setMinimumWidth(160);

            //Explanation text
            newRow.addView( createDescription(dd.descriptions.get(i)) );

            explanationTable.addView(newRow);
            explanationTable.addView(createSeparatorLine());
        }

        Tile nextDiscard = expectedDiscards.get(currentDiscard);
        setTileImage(recommendedDiscardImage, nextDiscard);
    }
    private TextView createDescription(String s){
        TextView tv = new TextView(getActivity());

        TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
        tv.setLayoutParams(params);

        tv.setText( s );
        return tv;
    }
    private View createSeparatorLine(){
        View line = new View(getContext());

        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.height = 1;
        params.width = GridLayout.LayoutParams.MATCH_PARENT;
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 2);
        line.setLayoutParams(params);

        line.setBackgroundColor(0xFF666666);
        return line;
    }
    private void setTileImage(LinearLayout container, Tile t){
        ImageView tileImage = Utils.getHandDisplayTileView(t, false);
        container.removeAllViews();
        container.addView(tileImage);
        container.setVisibility(View.VISIBLE);
    }

    private void setScoreVisibility(boolean bool){
        if( bool ){
//            discardPileLabel.setVisibility(View.GONE);
//            discardPile.setVisibility(View.GONE);
            lastDrawTile.setVisibility(View.GONE);
            explanationContainer.setVisibility(View.GONE);
            scoreScreen.setHand(hand);
            scoreScreen.setVisibility(View.VISIBLE);
        } else {
//            discardPileLabel.setVisibility(View.VISIBLE);
//            discardPile.setVisibility(View.VISIBLE);
            scoreScreen.setVisibility(View.GONE);
            lastDrawTile.setVisibility(View.VISIBLE);
            explanationContainer.setVisibility(View.VISIBLE);
        }
    }

    /////////////////////////////////////////
    ///////////       Model      ////////////
    /////////////////////////////////////////
    private void loadNextDiscard(){
        Tile discard = expectedDiscards.get(currentDiscard);
        discards.add(discard);
        discardPile.addTile(discard);

        if( discard!=lastDraw ){
            hand.discardTile(discard);
            hand.addTile(lastDraw);
            handDisplay.setHand(hand);
        }

        currentDiscard++;
        if( 13+currentDiscard < allDraws.size() ){
            lastDraw = allDraws.get(13+currentDiscard);
            lastDrawTile.setHand(new Hand(Arrays.asList(lastDraw)));
        } else {
            setScoreVisibility(true);
        }
    }

    private void loadPrevDiscard(){
        if( currentDiscard==18 ){
            setScoreVisibility(false);
        }

        Tile prevDiscard = discards.get( discards.size()-1 );
        discards.remove( prevDiscard );
        discardPile.clear();
        discardPile.addTiles(discards);

        currentDiscard--;

        // now "Draw" the correct tile
        //      If the discarded tile is the drawn tile, do NOT add it to the hand
        lastDraw = allDraws.get(13+currentDiscard);
        lastDrawTile.setHand(new Hand(Arrays.asList(lastDraw)));
        lastDrawTile.setVisibility(View.VISIBLE);

        if( lastDraw!=prevDiscard ){
            hand.discardTile(lastDraw);
            hand.addTile(prevDiscard);
            handDisplay.setHand(hand);
        }
    }

    /////////////////////////////////////////
    ///////////       Init       ////////////
    /////////////////////////////////////////
    private void assignUIElements(View myInflatedView){
        discardPileLabel = (TextView) myInflatedView.findViewById(R.id.discardPileLabel);
        discardPile = (DiscardPile) myInflatedView.findViewById(R.id.discardPile);
        handDisplay = (HandDisplay) myInflatedView.findViewById(R.id.handDisplay);
        lastDrawTile = (HandDisplay) myInflatedView.findViewById(R.id.lastDrawTile);
        recommendedDiscardImage = (LinearLayout) myInflatedView.findViewById(R.id.recommendedDiscardImage);
        explanationContainer = (LinearLayout) myInflatedView.findViewById(R.id.explanationContainer);
        explanationTable = (TableLayout) myInflatedView.findViewById(R.id.explanationTable);
        scoreScreen = (ScoreScreen) myInflatedView.findViewById(R.id.scoreScreen);
        scoreScreen.hideScoreTotal();

        backButton = (Button) myInflatedView.findViewById(R.id.backButton);
        backButton.setOnClickListener(this);
        nextButton = (Button) myInflatedView.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(this);
        mainMenuButton = (Button) myInflatedView.findViewById(R.id.mainMenuButton);
    }
    private void initData(){
        allDraws = TutorialLibrary.getTutorialTiles();
        expectedDiscards = TutorialLibrary.getTutorialExpectedDiscards();

        hand = new Hand(allDraws.subList(0, 13));
        handDisplay.setHand(hand);

        lastDraw = allDraws.get(13+currentDiscard);
        lastDrawTile.setHand(new Hand(Arrays.asList(lastDraw)));
    }
}
