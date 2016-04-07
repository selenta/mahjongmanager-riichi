package com.mahjongmanager.riichi.speedquiz;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mahjongmanager.riichi.Hand;
import com.mahjongmanager.riichi.HandDisplay;
import com.mahjongmanager.riichi.HandGenerator;
import com.mahjongmanager.riichi.MainActivity;
import com.mahjongmanager.riichi.R;
import com.mahjongmanager.riichi.ScoreCalculator;
import com.mahjongmanager.riichi.Tile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpeedQuizFragment_2ScoreHand extends Fragment implements View.OnClickListener {
    private List<Integer> hanValues = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13);
    private List<Integer> fuValues = Arrays.asList( 20,25,30,40,50,60,70,80,90,100,110 );

    private TextView secondCounter;

    private int han;
    private TextView hanLabel;
    private int fu;
    private TextView fuLabel;

    private Button increaseHanButton;
    private Button decreaseHanButton;
    private Button increaseFuButton;
    private Button decreaseFuButton;

    private HandDisplay handDisplay;
    private TextView prevailingWindLabel;
    private TextView playerWindLabel;
    private TextView selfDrawLabel;
    private LinearLayout winningTileContainer;
    private TextView otherYakuLabel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.fragment_speedquiz_2scorehand, container, false);

        assignComponents(myInflatedView);

        sampleHand(myInflatedView);
        //generateRandomHand();

        initHand();
        updateUI();
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

    private void initHand(){
        Hand fragHand = ((MainActivity) getActivity()).getCurrentHand();

        han = 2;
        ((MainActivity)getActivity()).setCurrentHanGuess(han);
        fu = 30;
        ((MainActivity)getActivity()).setCurrentFuGuess(fu);

        handDisplay.setIncludeWinningTile(false);
        handDisplay.setHand(fragHand);

        prevailingWindLabel.setText(fragHand.getString(fragHand.prevailingWind));
        playerWindLabel.setText(fragHand.getString(fragHand.playerWind));
        selfDrawLabel.setText((fragHand.selfDrawWinningTile)?"Yes":"No");

        TextView winningTileImage = ((MainActivity)getContext()).getUtils().getTileView(fragHand.getWinningTile());
        winningTileContainer.addView(winningTileImage);

        String oys = "Other Yaku: ";
        if( fragHand.nagashiMangan ){
            oys = addStrClean(oys, "Nagashi Mangan");
        }
        if( fragHand.doubleRiichi ){
            oys = addStrClean(oys, "Double Riichi");
        }
        if( fragHand.riichi ){
            oys = addStrClean(oys, "Riichi");
        }
        if( fragHand.ippatsu ){
            oys = addStrClean(oys, "Ippatsu");
        }
        if( fragHand.tsumo ){
            oys = addStrClean(oys, "Tsumo");
        }
        if( fragHand.rinshan){
            oys = addStrClean(oys, "Rinshan");
        }
        if( fragHand.chanKan){
            oys = addStrClean(oys, "Chan Kan");
        }
        if( fragHand.haitei ){
            oys = addStrClean(oys, "Haitei");
        }
        if( fragHand.houtei){
            oys = addStrClean(oys, "Houtei");
        }
        if( fragHand.dora!=0){
            oys = addStrClean(oys, "Dora "+ fragHand.dora.toString());
        }
        otherYakuLabel.setText(oys);
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
        hanLabel.setText(String.valueOf(han));
        ((MainActivity)getActivity()).setCurrentHanGuess(han);
        fuLabel.setText(String.valueOf(fu));
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

    private void assignComponents(View myInflatedView){
        handDisplay = (HandDisplay) myInflatedView.findViewById(R.id.handDisplay);

        secondCounter = (TextView) myInflatedView.findViewById(R.id.secondCounter);

        hanLabel = (TextView) myInflatedView.findViewById(R.id.hanValue);
        fuLabel = (TextView) myInflatedView.findViewById(R.id.fuValue);

        increaseHanButton = (Button) myInflatedView.findViewById(R.id.increaseHanButton);
        increaseHanButton.setOnClickListener(this);
        decreaseHanButton = (Button) myInflatedView.findViewById(R.id.decreaseHanButton);
        decreaseHanButton.setOnClickListener(this);
        increaseFuButton = (Button) myInflatedView.findViewById(R.id.increaseFuButton);
        increaseFuButton.setOnClickListener(this);
        decreaseFuButton = (Button) myInflatedView.findViewById(R.id.decreaseFuButton);
        decreaseFuButton.setOnClickListener(this);

        prevailingWindLabel = (TextView) myInflatedView.findViewById(R.id.prevailingWindLabel);
        playerWindLabel = (TextView) myInflatedView.findViewById(R.id.playerWindLabel);
        selfDrawLabel = (TextView) myInflatedView.findViewById(R.id.selfDrawLabel);
        winningTileContainer = (LinearLayout) myInflatedView.findViewById(R.id.winnintTileContainer);
        otherYakuLabel = (TextView) myInflatedView.findViewById(R.id.otherYaku);
    }

    private void sampleHand(View myInflatedView){
        if (((MainActivity)getActivity()).getScoredHands().size()<10) {
            Hand h = new Hand(new ArrayList<Tile>());

            while( h.han==0 ){
                HandGenerator hg = new HandGenerator();
                Hand hTemp = hg.completelyRandomHand();
                hg.addOtherYaku(hTemp);

                ScoreCalculator sc = new ScoreCalculator(hTemp);
                if( sc.validatedHand!=null ){
                    h = sc.validatedHand;
                }
            }

            ((MainActivity) getActivity()).setCurrentHand(h);
        } else {
            ((MainActivity) getActivity()).speedQuizScoreScreen(myInflatedView);
        }
    }
}
