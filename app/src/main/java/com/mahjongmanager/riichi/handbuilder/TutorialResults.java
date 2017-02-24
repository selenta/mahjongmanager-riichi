package com.mahjongmanager.riichi.handbuilder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mahjongmanager.riichi.MainActivity;
import com.mahjongmanager.riichi.R;
import com.mahjongmanager.riichi.common.Round;
import com.mahjongmanager.riichi.common.Tile;
import com.mahjongmanager.riichi.components.DiscardPile;
import com.mahjongmanager.riichi.components.HandDisplay;
import com.mahjongmanager.riichi.utils.TutorialLibrary;

public class TutorialResults extends Fragment implements View.OnClickListener {
    DiscardPile playerDiscardPile;
    DiscardPile expectedDiscardPile;
    HandDisplay playerHandDisplay;
    HandDisplay expectedHandDisplay;
    Round playerRound;

    boolean explanationNeeded = true;
    Button nextButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Yeah, I know I could create a new layout... but why?
        View myInflatedView = inflater.inflate(R.layout.fragment_handbuilder_tutorial_2results, container, false);
        assignUIElements(myInflatedView);

        loadData();
        evaluateSuccess();

        return myInflatedView;
    }

    /////////////////////////////////////////
    ///////////       View       ////////////
    /////////////////////////////////////////
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nextButton:
                if(explanationNeeded){
                    ((MainActivity)getActivity()).goToTutorialExplanation(null);
                } else {
                    ((MainActivity)getActivity()).backToMainMenu(null);
                }
                break;
        }
    }

    /////////////////////////////////////////
    ///////////       Init       ////////////
    /////////////////////////////////////////
    private void assignUIElements(View myInflatedView){
        playerDiscardPile = (DiscardPile) myInflatedView.findViewById(R.id.playerDiscardPile);
        expectedDiscardPile = (DiscardPile) myInflatedView.findViewById(R.id.expectedDiscardPile);

        playerHandDisplay = (HandDisplay) myInflatedView.findViewById(R.id.playerHandDisplay);
        expectedHandDisplay = (HandDisplay) myInflatedView.findViewById(R.id.expectedHandDisplay);

        nextButton = (Button) myInflatedView.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(this);
    }
    private void loadData(){
        playerRound = ((MainActivity)getActivity()).getCurrentRound();

        playerDiscardPile.addTiles(playerRound.getDiscards(Tile.Wind.SOUTH));
        playerHandDisplay.setHand(playerRound.getHand(Tile.Wind.SOUTH));

        expectedDiscardPile.addTiles(TutorialLibrary.getTutorialExpectedDiscards());
        expectedHandDisplay.setHand(TutorialLibrary.getTutorialExpectedHand());
    }
    private void evaluateSuccess(){
        if( explanationNeeded ){
            nextButton.setText("View Explanation");
        } else {
            nextButton.setText("Back to Main Menu");
        }
    }
}
