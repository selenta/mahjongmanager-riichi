package com.mahjongmanager.riichi.handcalculator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.mahjongmanager.riichi.Hand;
import com.mahjongmanager.riichi.MainActivity;
import com.mahjongmanager.riichi.R;
import com.mahjongmanager.riichi.Tile;
import com.mahjongmanager.riichi.utils.Log;

import java.util.HashSet;

public class WinningTile extends Fragment implements View.OnClickListener {

    private Hand actHand;

    private RadioGroup winningTileOptions;
    private RadioGroup selfDrawOptions;

    private Button nextButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.fragment_handcalculator_2winningtile, container, false);
        registerUI(myInflatedView);

        actHand = ((MainActivity)getActivity()).getCurrentHand();
        Log.i("actHand", "actHand: " + actHand.toString());

        setWinningTileOptions();
        checkHandState();
        return myInflatedView;
    }

    private void setWinningTile(){
        RadioButton rButt = (RadioButton)winningTileOptions.findViewById(winningTileOptions.getCheckedRadioButtonId());
        String rButtTxt = rButt.getText().toString();
        Log.d("setWinningTile", "rButtTxt: " + rButtTxt);

        for( Tile t : actHand.tiles ){
            t.winningTile = false;
        }
        actHand.findTile(rButtTxt).winningTile = true;

        for( Tile t : actHand.tiles ){
            Log.d("printWinningTile", "(tile, wT): (" + t.toString() + ", " + t.winningTile.toString() + ")");
        }

        checkNextEnablement();
        ((MainActivity)getActivity()).setCurrentHand(actHand);
    }

    private void setSelfDraw(){
        RadioButton rButt = (RadioButton)selfDrawOptions.findViewById(selfDrawOptions.getCheckedRadioButtonId());
        String rButtTxt = rButt.getText().toString();
        Log.d("setSelfDraw", "rButtTxt: " + rButtTxt);

        Tile wt = actHand.getWinningTile();
        if( wt!=null ){
            if( rButtTxt.equals("Yes") ){
                wt.calledFrom = Tile.CalledFrom.NONE;
                actHand.selfDrawWinningTile = true;
            } else {
                wt.calledFrom = Tile.CalledFrom.LEFT;
                actHand.selfDrawWinningTile = false;
            }
        } else {
            Log.d("setSelfDraw", "no winning tile");
        }
        ((MainActivity)getActivity()).setCurrentHand(actHand);
    }

    private void checkNextEnablement(){
        if( winningTileOptions.getCheckedRadioButtonId()!=-1 ){
            nextButton.setEnabled(true);
        } else {
            nextButton.setEnabled(false);
        }
    }

    private void setWinningTileOptions(){
        HashSet<String> uniqueTiles = new HashSet<String>();
        for( Tile t : actHand.tiles ){
            if( !isDefinitivelyKanTile(actHand, t) && t.revealedState==Tile.RevealedState.NONE ){
                uniqueTiles.add(t.toString());
            }
        }
        Log.i("WinningTileOptions", "Winning tile options: " + uniqueTiles.toString());

        // Doing it this way to preserve sort order of tiles
        while( uniqueTiles.size() > 0 ){
            for( Tile t : actHand.tiles ){
                if( uniqueTiles.contains(t.toString()) ){
                    RadioButton rdbtn = new RadioButton(getContext());
                    rdbtn.setText(t.toString());
                    winningTileOptions.addView(rdbtn);
                    uniqueTiles.remove(t.toString());
                }
            }
        }
    }
    private boolean isDefinitivelyKanTile(Hand h, Tile t){
        return h.countTile(t)==4
                && (t.suit == Tile.Suit.HONOR
                        || t.revealedState == Tile.RevealedState.ADDEDKAN
                        || t.revealedState == Tile.RevealedState.OPENKAN
                        || t.revealedState == Tile.RevealedState.CLOSEDKAN
                        || !isChiiCandidate(h, t));
    }
    // This is hardly the "smartest" way to do this, but probably best not to over-analyze
    // Looking for at least two neighboring tiles that indicate it could be part of a chii
    private boolean isChiiCandidate(Hand h, Tile t ){
        Tile lowerTile=null;
        Tile upperTile=null;
        int candidateNumber;
        switch(t.number){
            case 1:
                candidateNumber = 2;
                break;
            case 2:
                candidateNumber = 1;
                break;
            default:
                candidateNumber = t.number-2;
                break;
        }

        // Code inspection say it's always null? Seems to work fine
        while( lowerTile==null && upperTile==null ){
            lowerTile = h.findTile(String.valueOf(candidateNumber), t.suit.toString() );
            if( candidateNumber+1==t.number ){
                upperTile = h.findTile(String.valueOf(candidateNumber+2), t.suit.toString() );
            } else {
                upperTile = h.findTile(String.valueOf(candidateNumber+1), t.suit.toString() );
            }
            if( lowerTile==null || upperTile==null ){
                lowerTile = null;
                upperTile = null;
            }

            candidateNumber++;
            if( candidateNumber==t.number ){
                candidateNumber++;
            } else if( candidateNumber>t.number+2 ){
                break;
            }
        }
        return !(lowerTile==null && upperTile==null) ;
    }

    /**
     * The hand may already have a WinningTile or Ron/Tsumo chosen if the user reached this
     * page by pressing the Back button. So select those options.
     */
    private void checkHandState(){
        Tile wt = actHand.getWinningTile();
        if( wt!=null ){
            for(int i=1; i < winningTileOptions.getChildCount(); i++ ){
                RadioButton rb = (RadioButton) winningTileOptions.getChildAt(i);
                boolean isMatch = rb.getText().equals(wt.toString());
                rb.setChecked(isMatch);
            }
        }

        if(actHand.selfDrawWinningTile){
            for(int i=1; i < selfDrawOptions.getChildCount(); i++ ){
                RadioButton rb = (RadioButton) selfDrawOptions.getChildAt(i);
                boolean isMatch = rb.getText().equals("True");
                rb.setChecked(isMatch);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nextButton:
                setSelfDraw();
                ((MainActivity)getActivity()).goToHandCalculatorOtherInfo(v);
                break;
        }
    }

    private void registerUI(View myInflatedView){
        nextButton = (Button) myInflatedView.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(this);
        winningTileOptions = (RadioGroup) myInflatedView.findViewById(R.id.winningTileOptions);
        winningTileOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                setWinningTile();
            }
        });
        selfDrawOptions = (RadioGroup) myInflatedView.findViewById(R.id.selfDrawOptions);
    }
}
