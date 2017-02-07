package com.mahjongmanager.riichi.speedquiz;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mahjongmanager.riichi.common.Hand;
import com.mahjongmanager.riichi.components.HandDisplay;
import com.mahjongmanager.riichi.MainActivity;
import com.mahjongmanager.riichi.R;
import com.mahjongmanager.riichi.common.Tile;
import com.mahjongmanager.riichi.utils.AppSettings;
import com.mahjongmanager.riichi.utils.Utils;

import java.util.Arrays;
import java.util.List;

public class ScoreHand extends Fragment implements View.OnClickListener {
    private List<Integer> hanValues = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 26);
    private List<Integer> fuValues = Arrays.asList( 20,25,30,40,50,60,70,80,90,100,110 );

    private TextView secondCounter;

    private TextView hanLabel;
    private int han;
    private TextView hanValueDisplay;
    private TextView fuLabel;
    private int fu;
    private TextView fuValudDisplay;

    private Button increaseHanButton;
    private Button decreaseHanButton;
    private Button increaseFuButton;
    private Button decreaseFuButton;

    private HandDisplay handDisplay;
    private LinearLayout prevailingWindTileContainer;
    private LinearLayout playerWindTileContainer;
    private LinearLayout winningTileContainer;
    private TextView selfDrawLabel;
    private TextView otherYakuLabel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.fragment_speedquiz_2scorehand, container, false);
        assignUIElements(myInflatedView);

        han = 2;
        fu = 30;

        initHandDisplaySettings();
        initHand();
        updateUI();
        checkTextSize();
        return myInflatedView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.increaseHanButton:
                han = hanValues.get(hanValues.indexOf(han)+1);
                break;
            case R.id.decreaseHanButton:
                han = hanValues.get(hanValues.indexOf(han)-1);
                break;
            case R.id.increaseFuButton:
                fu = fuValues.get(fuValues.indexOf(fu)+1);
                break;
            case R.id.decreaseFuButton:
                fu = fuValues.get(fuValues.indexOf(fu)-1);
                break;
        }
        updateUI();
    }

    public void updateSecondCounter(String s){
        secondCounter.setText(s);
    }

    private void initHandDisplaySettings(){
        if( AppSettings.getSpeedQuizSeparateClosedMelds() ){
            handDisplay.setState(HandDisplay.SPEED_QUIZ);
        } else {
            handDisplay.setState(HandDisplay.SPEED_QUIZ_UNSORTED);
        }
    }
    private void initHand(){
        MainActivity activity = ((MainActivity) getActivity());

        Hand currentHand = activity.getCurrentHand();
        if( currentHand==null ){
            activity.generateSpeedQuizHand();
            currentHand = activity.getCurrentHand();
        }
        handDisplay.setHand(currentHand);

        displaySingleTiles(currentHand);

        selfDrawLabel.setText((currentHand.selfDrawWinningTile)?"Yes":"No");

        String oys = otherYakuStr(currentHand);
        otherYakuLabel.setText(oys);
    }
    private void displaySingleTiles(Hand hand){
        Utils utils = ((MainActivity)getActivity()).getUtils();

        Tile prevailingWindTile = new Tile(hand.prevailingWind);
        ImageView prevailingWindTileImage = utils.getHandDisplayTileView(prevailingWindTile, false);
        prevailingWindTileContainer.addView(prevailingWindTileImage);

        Tile playerWindTile = new Tile(hand.playerWind);
        ImageView playerWindTileImage = utils.getHandDisplayTileView(playerWindTile, false);
        playerWindTileContainer.addView(playerWindTileImage);

        ImageView winningTileImage = utils.getHandDisplayTileView(hand.getWinningTile(), false);
        winningTileContainer.addView(winningTileImage);
    }
    private String otherYakuStr(Hand h){
        String oys = "Other Yaku: ";
        if( h.nagashiMangan ){
            oys = addStrClean(oys, "Nagashi Mangan");
        }
        if( h.doubleRiichi ){
            oys = addStrClean(oys, "Double Riichi");
        }
        if( h.riichi ){
            oys = addStrClean(oys, "Riichi");
        }
        if( h.ippatsu ){
            oys = addStrClean(oys, "Ippatsu");
        }
        if( h.rinshan){
            oys = addStrClean(oys, "Rinshan");
        }
        if( h.chanKan){
            oys = addStrClean(oys, "Chan Kan");
        }
        if( h.haitei ){
            oys = addStrClean(oys, "Haitei");
        }
        if( h.houtei){
            oys = addStrClean(oys, "Houtei");
        }
        if( h.arbitraryDora!=0){
            oys = addStrClean(oys, "Dora "+ h.arbitraryDora);
        }
        return oys;
    }
    private String addStrClean( String base, String add ){
        String b = base;
        if( b.charAt(b.length()-2)!=':' ){
            b = b + ", ";
        }
        b = b + add;
        return b;
    }

    private void updateUI(){
        hanValueDisplay.setText(String.valueOf(han));
        ((MainActivity)getActivity()).setCurrentHanGuess(han);
        fuValudDisplay.setText(String.valueOf(fu));
        ((MainActivity)getActivity()).setCurrentFuGuess(fu);

        if( han == hanValues.get(0) ){
            //Can't decrease below min
            decreaseHanButton.setEnabled(false);
        } else if( han == hanValues.get(hanValues.size()-1) ){
            //Can't increase past max size
            increaseHanButton.setEnabled(false);
        } else {
            increaseHanButton.setEnabled(true);
            decreaseHanButton.setEnabled(true);
        }

        if( fu == fuValues.get(0) ){
            //Can't decrease below min
            decreaseFuButton.setEnabled(false);
        } else if( fu == fuValues.get(fuValues.size()-1) ){
            //Can't increase past max size
            increaseFuButton.setEnabled(false);
        } else {
            increaseFuButton.setEnabled(true);
            decreaseFuButton.setEnabled(true);
        }
    }

    private void checkTextSize(){
        if( Utils.SCREEN_SIZE < 1024 ){
            otherYakuLabel.setTextSize( otherYakuLabel.getTextSize() *0.55f);
            hanLabel.setTextSize( hanLabel.getTextSize() *0.55f);
            fuLabel.setTextSize(  fuLabel.getTextSize()  *0.55f);
            increaseHanButton.setTextSize( increaseHanButton.getTextSize() *0.55f);
            increaseFuButton.setTextSize(  increaseFuButton.getTextSize()  *0.55f);
            hanValueDisplay.setTextSize( hanValueDisplay.getTextSize() *0.55f);
            fuValudDisplay.setTextSize(  fuValudDisplay.getTextSize()  *0.55f);
            decreaseHanButton.setTextSize( decreaseHanButton.getTextSize() *0.55f);
            decreaseFuButton.setTextSize(  decreaseFuButton.getTextSize()  *0.55f);
        }
    }

    private void assignUIElements(View myInflatedView){
        handDisplay = (HandDisplay) myInflatedView.findViewById(R.id.handDisplay);

        secondCounter = (TextView) myInflatedView.findViewById(R.id.secondCounter);

        hanLabel = (TextView) myInflatedView.findViewById(R.id.hanLabel);
        fuLabel = (TextView) myInflatedView.findViewById(R.id.fuLabel);

        hanValueDisplay = (TextView) myInflatedView.findViewById(R.id.hanValue);
        fuValudDisplay = (TextView) myInflatedView.findViewById(R.id.fuValue);

        increaseHanButton = (Button) myInflatedView.findViewById(R.id.increaseHanButton);
        increaseHanButton.setOnClickListener(this);
        decreaseHanButton = (Button) myInflatedView.findViewById(R.id.decreaseHanButton);
        decreaseHanButton.setOnClickListener(this);
        increaseFuButton = (Button) myInflatedView.findViewById(R.id.increaseFuButton);
        increaseFuButton.setOnClickListener(this);
        decreaseFuButton = (Button) myInflatedView.findViewById(R.id.decreaseFuButton);
        decreaseFuButton.setOnClickListener(this);

        prevailingWindTileContainer = (LinearLayout) myInflatedView.findViewById(R.id.prevailingWindTileContainer);
        playerWindTileContainer = (LinearLayout) myInflatedView.findViewById(R.id.playerWindTileContainer);
        winningTileContainer = (LinearLayout) myInflatedView.findViewById(R.id.speedQuizWinningTileContainer);
        selfDrawLabel = (TextView) myInflatedView.findViewById(R.id.selfDrawLabel);
        otherYakuLabel = (TextView) myInflatedView.findViewById(R.id.otherYaku);
    }
}
