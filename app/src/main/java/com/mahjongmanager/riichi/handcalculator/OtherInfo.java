package com.mahjongmanager.riichi.handcalculator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.mahjongmanager.riichi.common.Hand;
import com.mahjongmanager.riichi.MainActivity;
import com.mahjongmanager.riichi.R;
import com.mahjongmanager.riichi.ScoreCalculator;
import com.mahjongmanager.riichi.common.Tile;
import com.mahjongmanager.riichi.utils.Log;

import java.util.ArrayList;
import java.util.List;

public class OtherInfo extends Fragment implements View.OnClickListener {

    private Hand actHand;
    //Used only to determine which checkBoxes should be enabled
    private Hand tempScoredHand;

    private Spinner doraCount;

    private CheckBox doubleRiichiCheckBox;
    private CheckBox riichiCheckBox;
    private CheckBox ippatsuCheckBox;
    private CheckBox rinshanCheckBox;
    private CheckBox chanKanCheckBox;
    private CheckBox haiteiHouteiCheckBox;

    private RadioGroup prevailingWindOptions;
    private RadioGroup playerWindOptions;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.fragment_handcalculator_3otherinfo, container, false);

        actHand = ((MainActivity)getActivity()).getCurrentHand();
        Log.i("actHand", "actHand: " + actHand.toString());

        ScoreCalculator sc = new ScoreCalculator(actHand);
        tempScoredHand = sc.scoredHand;

        registerControls(myInflatedView);
        initializeDoraCount();
        checkHandState();
        setCheckBoxEnablement();
        return myInflatedView;
    }

    private void initializeDoraCount(){
        List<String> doraList = new ArrayList<>();
        String pad = "      ";

        for(int i=0; i<11; i++){
            doraList.add( pad + String.valueOf(i) + pad );
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>
                (getActivity(), android.R.layout.simple_spinner_item, doraList);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        doraCount.setAdapter(dataAdapter);
    }

    /**
     * The hand may already have a some of these options chosen if the user reached this
     * page by pressing the Back button. So select those options.
     */
    private void checkHandState(){
        doraCount.setSelection(actHand.arbitraryDora);

        doubleRiichiCheckBox.setChecked(actHand.doubleRiichi);
        riichiCheckBox.setChecked(actHand.riichi);
        ippatsuCheckBox.setChecked(actHand.ippatsu);
        rinshanCheckBox.setChecked(actHand.rinshan);
        chanKanCheckBox.setChecked(actHand.chanKan);
        chanKanCheckBox.setChecked((actHand.haitei || actHand.houtei));

        for(int i=1; i < prevailingWindOptions.getChildCount(); i++ ){
            RadioButton rb = (RadioButton) prevailingWindOptions.getChildAt(i);
            boolean isMatch = rb.getText().toString().equalsIgnoreCase(actHand.prevailingWind.toString());
            if( isMatch ){
                rb.setChecked(true);
            }
        }
        for(int i=1; i < playerWindOptions.getChildCount(); i++ ){
            RadioButton rb = (RadioButton) playerWindOptions.getChildAt(i);
            boolean isMatch = rb.getText().toString().equalsIgnoreCase(actHand.playerWind.toString());
            if( isMatch ){
                rb.setChecked(true);
            }
        }
    }

    private void setDoraCount(){
        actHand.arbitraryDora = Integer.parseInt(doraCount.getSelectedItem().toString().trim());
        ((MainActivity)getActivity()).setCurrentHand(actHand);
    }
    private void setDoubleRiichi(){
        actHand.doubleRiichi = doubleRiichiCheckBox.isChecked();
        setCheckBoxEnablement();
    }
    private void setRiichi(){
        actHand.riichi = riichiCheckBox.isChecked();
        setCheckBoxEnablement();
    }
    private void setIppatsu(){
        actHand.ippatsu = ippatsuCheckBox.isChecked();
        ((MainActivity)getActivity()).setCurrentHand(actHand);
    }
    private void setRinshan(){
        actHand.rinshan = rinshanCheckBox.isChecked();
        ((MainActivity)getActivity()).setCurrentHand(actHand);
    }
    private void setChanKan(){
        actHand.chanKan = chanKanCheckBox.isChecked();
        ((MainActivity)getActivity()).setCurrentHand(actHand);
    }
    private void setHaiteiHoutei(){
        if( haiteiHouteiCheckBox.isChecked() && actHand.selfDrawWinningTile ){
            actHand.haitei = true;
            actHand.houtei = false;
        } else if( haiteiHouteiCheckBox.isChecked() && !actHand.selfDrawWinningTile ){
            actHand.haitei = false;
            actHand.houtei = true;
        } else {
            actHand.haitei = false;
            actHand.houtei = false;
        }
        ((MainActivity)getActivity()).setCurrentHand(actHand);
    }
    private void setRoundWind(){
        RadioButton rButt = (RadioButton)prevailingWindOptions.findViewById(prevailingWindOptions.getCheckedRadioButtonId());
        String rButtTxt = rButt.getText().toString();
        actHand.prevailingWind = Tile.Wind.valueOf(rButtTxt.toUpperCase());
        ((MainActivity)getActivity()).setCurrentHand(actHand);
    }
    private void setPlayerWind(){
        RadioButton rButt = (RadioButton)playerWindOptions.findViewById(playerWindOptions.getCheckedRadioButtonId());
        String rButtTxt = rButt.getText().toString();
        actHand.playerWind = Tile.Wind.valueOf(rButtTxt.toUpperCase());
        ((MainActivity)getActivity()).setCurrentHand(actHand);
    }

    private void setCheckBoxEnablement(){
        //ChanKan: WinningTile must be only tile of type in hand
        int winningTileCount = 0;
        for(Tile t : tempScoredHand.getAllUsedTiles() ){
            if( tempScoredHand.getWinningTile().number==t.number && tempScoredHand.getWinningTile().suit==t.suit ){
                winningTileCount++;
            }
        }
        Log.v("setCheckBoxEnablement","winningTileCount: " + winningTileCount );
        if( winningTileCount!=1 || tempScoredHand.selfDrawWinningTile ){
            chanKanCheckBox.setEnabled(false);
        }

        //Rinshan: Must have at least one Kan in hand, tile must not be part of kan
        if( !tempScoredHand.hasKan() || !tempScoredHand.selfDrawWinningTile ){
            rinshanCheckBox.setEnabled(false);
        }

        //Riichi: Can't riichi if the hand is open
        if( !actHand.isOpen() ){
            if( doubleRiichiCheckBox.isChecked() ){
                riichiCheckBox.setChecked(false);
                riichiCheckBox.setEnabled(false);
                ippatsuCheckBox.setEnabled(true);
            } else if( riichiCheckBox.isChecked() ){
                doubleRiichiCheckBox.setChecked(false);
                doubleRiichiCheckBox.setEnabled(false);
                ippatsuCheckBox.setEnabled(true);
            } else {
                doubleRiichiCheckBox.setEnabled(true);
                riichiCheckBox.setEnabled(true);
                ippatsuCheckBox.setEnabled(false);
                ippatsuCheckBox.setChecked(false);
            }
        } else {
            riichiCheckBox.setEnabled(false);
            doubleRiichiCheckBox.setEnabled(false);
        }
        setIppatsu();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.doubleRiichiCheckBox:
                setDoubleRiichi();
                break;
            case R.id.riichiCheckBox:
                setRiichi();
                break;
            case R.id.ippatsuCheckBox:
                setIppatsu();
                break;
            case R.id.rinshanCheckBox:
                setRinshan();
                break;
            case R.id.chanKanCheckBox:
                setChanKan();
                break;
            case R.id.haiteiHouteiCheckBox:
                setHaiteiHoutei();
                break;
        }
    }

    private void registerControls(View myInflatedView){
        doraCount = (Spinner) myInflatedView.findViewById(R.id.doraCount);
        doraCount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                setDoraCount();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        doubleRiichiCheckBox = (CheckBox) myInflatedView.findViewById(R.id.doubleRiichiCheckBox);
        doubleRiichiCheckBox.setOnClickListener(this);
        riichiCheckBox = (CheckBox) myInflatedView.findViewById(R.id.riichiCheckBox);
        riichiCheckBox.setOnClickListener(this);
        ippatsuCheckBox = (CheckBox) myInflatedView.findViewById(R.id.ippatsuCheckBox);
        ippatsuCheckBox.setOnClickListener(this);
        rinshanCheckBox = (CheckBox) myInflatedView.findViewById(R.id.rinshanCheckBox);
        rinshanCheckBox.setOnClickListener(this);
        chanKanCheckBox = (CheckBox) myInflatedView.findViewById(R.id.chanKanCheckBox);
        chanKanCheckBox.setOnClickListener(this);
        haiteiHouteiCheckBox = (CheckBox) myInflatedView.findViewById(R.id.haiteiHouteiCheckBox);
        haiteiHouteiCheckBox.setOnClickListener(this);
        if( actHand.selfDrawWinningTile ){
            haiteiHouteiCheckBox.setText("Haitei");
        } else {
            haiteiHouteiCheckBox.setText("Houtei");
        }

        prevailingWindOptions = (RadioGroup) myInflatedView.findViewById(R.id.prevailingWindOptions);
        prevailingWindOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                setRoundWind();
            }
        });

        playerWindOptions = (RadioGroup) myInflatedView.findViewById(R.id.playerWindOptions);
        playerWindOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                setPlayerWind();
            }
        });
    }
}