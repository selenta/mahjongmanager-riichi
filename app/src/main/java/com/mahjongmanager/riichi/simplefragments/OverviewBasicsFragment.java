package com.mahjongmanager.riichi.simplefragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mahjongmanager.riichi.common.Hand;
import com.mahjongmanager.riichi.R;
import com.mahjongmanager.riichi.common.Tile;
import com.mahjongmanager.riichi.components.HandDisplay;
import com.mahjongmanager.riichi.utils.HandGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OverviewBasicsFragment extends Fragment {
    HandDisplay manzuExample;
    HandDisplay pinzuExample;
    HandDisplay souzuExample;
    HandDisplay windsExample;
    HandDisplay dragonsExample;
    HandDisplay chiiExample;
    HandDisplay ponExample;
    HandDisplay closedKanExample;
    HandDisplay openKanExample;
    HandDisplay addedOpenKanExample;

    ImageView deadWallPicture;
    ImageView beforeFirstTurnPicture;
    ImageView winningHandPicture;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.fragment_overviewbasics, container, false);

        registerThings(myInflatedView);
        populateTileExamples();
        populateRevealedMeldExamples();

        return myInflatedView;
    }

    private void populateTileExamples(){
        Hand manzuExampleHand = new Hand(HandGenerator.allManzu());
        manzuExample.setHand(manzuExampleHand);
        Hand pinzuExampleHand = new Hand(HandGenerator.allPinzu());
        pinzuExample.setHand(pinzuExampleHand);
        Hand souzuExampleHand = new Hand(HandGenerator.allSouzu());
        souzuExample.setHand(souzuExampleHand);
        Hand windsExampleHand = new Hand(HandGenerator.allWinds());
        windsExample.setHand(windsExampleHand);
        Hand dragonsExampleHand = new Hand(HandGenerator.allDragons());
        dragonsExample.setHand(dragonsExampleHand);
    }

    private void populateRevealedMeldExamples(){
        chiiExample.setHand(chiiExampleHand());
        ponExample.setHand(ponExampleHand());
        closedKanExample.setHand(closedKanExampleHand());
        openKanExample.setHand(openKanExampleHand());
        addedOpenKanExample.setHand(addedOpenKanExampleHand());
    }

    private Hand chiiExampleHand(){
        Tile t1 = new Tile(1, Tile.Suit.PINZU);
        Tile t2 = new Tile(2, Tile.Suit.PINZU);
        Tile t3 = new Tile(3, Tile.Suit.PINZU);

        t1.revealedState = t2.revealedState = t3.revealedState = Tile.RevealedState.CHI;
        t3.calledFrom = Tile.CalledFrom.LEFT;

        return createMeldExampleHand(Arrays.asList(t1, t2, t3));
    }
    private Hand ponExampleHand(){
        Tile t1 = new Tile(8, Tile.Suit.MANZU);
        Tile t2 = new Tile(8, Tile.Suit.MANZU);
        Tile t3 = new Tile(8, Tile.Suit.MANZU);

        t1.revealedState = t2.revealedState = t3.revealedState = Tile.RevealedState.PON;
        t2.calledFrom = Tile.CalledFrom.CENTER;

        return createMeldExampleHand(Arrays.asList(t1, t2, t3));
    }
    private Hand closedKanExampleHand(){
        Tile t1 = new Tile(Tile.Wind.EAST);
        Tile t2 = new Tile(Tile.Wind.EAST);
        Tile t3 = new Tile(Tile.Wind.EAST);
        Tile t4 = new Tile(Tile.Wind.EAST);

        t1.revealedState = t2.revealedState = t3.revealedState = t4.revealedState = Tile.RevealedState.CLOSEDKAN;

        return createMeldExampleHand(Arrays.asList(t1, t2, t3, t4));
    }
    private Hand openKanExampleHand(){
        Tile t1 = new Tile(5, Tile.Suit.SOUZU);
        Tile t2 = new Tile(5, Tile.Suit.SOUZU);
        Tile t3 = new Tile(5, Tile.Suit.SOUZU);
        Tile t4 = new Tile(5, Tile.Suit.SOUZU);

        t1.revealedState = t2.revealedState = t3.revealedState = t4.revealedState = Tile.RevealedState.OPENKAN;
        t3.calledFrom = Tile.CalledFrom.RIGHT;

        return createMeldExampleHand(Arrays.asList(t1, t2, t3, t4));
    }
    private Hand addedOpenKanExampleHand(){
        Tile t1 = new Tile(Tile.Dragon.RED);
        Tile t2 = new Tile(Tile.Dragon.RED);
        Tile t3 = new Tile(Tile.Dragon.RED);
        Tile t4 = new Tile(Tile.Dragon.RED);

        t3.calledFrom = Tile.CalledFrom.CENTER;
        t1.revealedState = t2.revealedState = t3.revealedState = Tile.RevealedState.PON;
        t4.revealedState = Tile.RevealedState.ADDEDKAN;

        return createMeldExampleHand(Arrays.asList(t1, t2, t3, t4));
    }
    private Hand createMeldExampleHand(List<Tile> tiles){
        Hand h = new Hand(new ArrayList<Tile>());
        h.setMeld(tiles);
        h.tiles.addAll(tiles);
        return h;
    }

    private void registerThings(View myInflatedView){
        manzuExample = (HandDisplay) myInflatedView.findViewById(R.id.manzuTiles);
        manzuExample.setState(HandDisplay.SPEED_QUIZ_UNSORTED);
        pinzuExample = (HandDisplay) myInflatedView.findViewById(R.id.pinzuTiles);
        pinzuExample.setState(HandDisplay.SPEED_QUIZ_UNSORTED);
        souzuExample = (HandDisplay) myInflatedView.findViewById(R.id.souzuTiles);
        souzuExample.setState(HandDisplay.SPEED_QUIZ_UNSORTED);
        windsExample = (HandDisplay) myInflatedView.findViewById(R.id.windTiles);
        windsExample.setState(HandDisplay.SPEED_QUIZ_UNSORTED);
        dragonsExample = (HandDisplay) myInflatedView.findViewById(R.id.dragonTiles);
        dragonsExample.setState(HandDisplay.SPEED_QUIZ_UNSORTED);

        chiiExample = (HandDisplay) myInflatedView.findViewById(R.id.chiiExample);
        chiiExample.setState(HandDisplay.FU_DISPLAY);
        ponExample = (HandDisplay) myInflatedView.findViewById(R.id.ponExample);
        ponExample.setState(HandDisplay.FU_DISPLAY);
        closedKanExample = (HandDisplay) myInflatedView.findViewById(R.id.closedKanExample);
        closedKanExample.setState(HandDisplay.FU_DISPLAY);
        openKanExample = (HandDisplay) myInflatedView.findViewById(R.id.openKanExample);
        openKanExample.setState(HandDisplay.FU_DISPLAY);
        addedOpenKanExample = (HandDisplay) myInflatedView.findViewById(R.id.addedKanExample);
        addedOpenKanExample.setState(HandDisplay.FU_DISPLAY);

        deadWallPicture = (ImageView) myInflatedView.findViewById(R.id.deadWallPicture);
        beforeFirstTurnPicture = (ImageView) myInflatedView.findViewById(R.id.beforeFirstTurnPicture);
        winningHandPicture = (ImageView) myInflatedView.findViewById(R.id.winningHandPicture);
    }
}
