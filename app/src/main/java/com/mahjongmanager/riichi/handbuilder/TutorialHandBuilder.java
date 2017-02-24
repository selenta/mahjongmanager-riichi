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
import com.mahjongmanager.riichi.utils.TutorialLibrary;

public class TutorialHandBuilder extends Fragment implements View.OnClickListener {
    private DrawDiscard drawDiscard;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Yeah, I know I could create a new layout... but why?
        View myInflatedView = inflater.inflate(R.layout.fragment_handbuilder_2simulate_4p, container, false);
        drawDiscard = (DrawDiscard) myInflatedView.findViewById(R.id.drawDiscardComponent);

        initRound();
        drawDiscard.setParentFragment(this);
        drawDiscard.setTitle("Draw/Discard - Practice");

        return myInflatedView;
    }

    /////////////////////////////////////////
    ///////////       View       ////////////
    /////////////////////////////////////////
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tsumoButton:
                drawDiscard.declareTsumo();
                ((MainActivity)getContext()).goToTutorialResults(null);
                break;
            case R.id.scoreHandButton:
                ((MainActivity)getActivity()).goToTutorialResults(null);
                break;
        }
    }

    /////////////////////////////////////////
    ///////////       Init       ////////////
    /////////////////////////////////////////
    private void initRound(){
        Round round = new Round(Tile.Wind.EAST);

        Tile.Wind playerWind = Tile.Wind.SOUTH;
        round.stackDeck(playerWind, TutorialLibrary.getTutorialTiles());

        drawDiscard.setRound(round, playerWind);
    }
}
