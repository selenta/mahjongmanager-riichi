package com.mahjongmanager.riichi;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;


import java.util.HashSet;

public class HandCalculatorFragment_2WinningTile extends Fragment implements View.OnClickListener {

    private Hand actHand;

    private RadioGroup winningTileOptions;
    private RadioGroup selfDrawOptions;

    private Button nextButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.fragment_handcalculator_2winningtile, container, false);

        actHand = ((MainActivity)getActivity()).getCurrentHand();
        Log.i("actHand", "actHand: " + actHand.toString());


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

        setWinningTileOptions();
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
            uniqueTiles.add(t.toString());
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nextButton:
                setSelfDraw();
                ((MainActivity)getActivity()).goToHandCalculatorOtherInfo(v);
                break;
        }
    }
}
