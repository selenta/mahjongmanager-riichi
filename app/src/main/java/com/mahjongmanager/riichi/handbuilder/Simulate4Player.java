package com.mahjongmanager.riichi.handbuilder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mahjongmanager.riichi.MainActivity;
import com.mahjongmanager.riichi.R;
import com.mahjongmanager.riichi.common.Round;
import com.mahjongmanager.riichi.common.Tile;
import com.mahjongmanager.riichi.components.DrawDiscard;
import com.mahjongmanager.riichi.utils.ExampleHands;

public class Simulate4Player extends Fragment implements View.OnClickListener {
    private DrawDiscard drawDiscard;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.fragment_handbuilder_2simulate_4p, container, false);
        drawDiscard = (DrawDiscard) myInflatedView.findViewById(R.id.drawDiscardComponent);

        initRound();
        return myInflatedView;
    }

    /////////////////////////////////////////
    ///////////       View       ////////////
    /////////////////////////////////////////
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tsumoButton:

                // TODO hands are not always scoring correctly
                //  The following hand was scored as Tsumo (1h 30fu):
                //  45567789m 22456s drawing 6m as winning tile
                //  Should be scored as Tsumo+Pinfu (2h 20fu)

                drawDiscard.declareTsumo();
                ((MainActivity)getContext()).goToHandBuilderScoreHand(null);
                break;
            case R.id.scoreHandButton:
                ((MainActivity)getActivity()).goToHandBuilderScoreHand(null);
                break;
        }
    }

    /////////////////////////////////////////
    ///////////       Init       ////////////
    /////////////////////////////////////////
    private void initRound(){
        Round round = new Round(Tile.Wind.EAST);

//       Tile.Wind playerWind = Utils.getRandomWind();
        Tile.Wind playerWind = Tile.Wind.SOUTH;
        round.stackDeck(playerWind, ExampleHands.getHandBuilderTutorialSouth());

        drawDiscard.setRound(round, playerWind);
        drawDiscard.setParentFragment(this);
    }
}
