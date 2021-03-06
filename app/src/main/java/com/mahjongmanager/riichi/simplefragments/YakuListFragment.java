package com.mahjongmanager.riichi.simplefragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.mahjongmanager.riichi.common.Yaku;
import com.mahjongmanager.riichi.components.YakuDescription;
import com.mahjongmanager.riichi.utils.CSVFile;
import com.mahjongmanager.riichi.R;
import com.mahjongmanager.riichi.utils.ExampleHands;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class YakuListFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnTouchListener {
    Spinner spinner;

    LinearLayout yakuDescriptions;
    LinearLayout yakuFrequency;
    LinearLayout yakuByAverageHan;
    LinearLayout yakuCombinatorics;

    TableLayout yakuFrequencyTable;
    TableLayout yakuByAverageHanTable;
    TableLayout yakuCombinatoricsTable;

    TextView yakuListNotes;

    static final String YAKU_LIST_COMMON        = "Common Yaku";
    static final String YAKU_LIST_STANDARD      = "Standard Yaku";
    static final String YAKU_LIST_YAKUMAN       = "Standard Yakuman";
    static final String YAKU_LIST_RARE          = "Uncommon Yaku";
    static final String YAKU_BY_FREQUENCY_LABEL = "Han sorted by Frequency";
    static final String YAKU_BY_AVERAGE_HAN     = "Han sorted by Avg. Hand Size";
    static final String YAKU_COMBINATORICS      = "Yaku Combinatorics (math)";

    static final String yakuListCommonNotes   = "Descriptions of the six Yaku that beginners should learn first (with example hands).";
    static final String yakuListStandardNotes = "Descriptions of all non-Yakuman Yaku that are used in most rulesets (with example hands) .";
    static final String yakuListYakumanNotes  = "Descriptions of the Yakuman that are accepted in most rulesets (with example hands) .";
    static final String yakuListRareNotes     = "Descriptions of Yaku that are not accepted in most rulesets, but are common enough that they should still be listed.";

    boolean csvPopulated = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.fragment_yakulist, container, false);

        registerUIElements(myInflatedView);
        setMode(YAKU_LIST_COMMON);

        return myInflatedView;
    }

    //////////////////////////////////////////
    ///////////       Modes       ////////////
    //////////////////////////////////////////
    private boolean userMode = false;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        userMode = true;
        return false;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if( userMode ){
            setMode( parent.getSelectedItem().toString() );
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    // NOTE: this is not thread-safe, and may result in double/triple population of lists
    //      Not sure how to make this safer... seems to mostly work?
    private void setMode(String mode){
        yakuDescriptions.setVisibility(View.GONE);
        yakuFrequency.setVisibility(View.GONE);
        yakuByAverageHan.setVisibility(View.GONE);
        yakuCombinatorics.setVisibility(View.GONE);
        switch (mode){
            case YAKU_LIST_COMMON:
                displayYaku(1);
                break;
            case YAKU_LIST_STANDARD:
                displayYaku(2);
                break;
            case YAKU_LIST_YAKUMAN:
                displayYaku(3);
                break;
            case YAKU_LIST_RARE:
                displayYaku(4);
                break;
            case YAKU_BY_FREQUENCY_LABEL:
                populateRealWorldDataTable();
                yakuFrequency.setVisibility(View.VISIBLE);
                break;
            case YAKU_BY_AVERAGE_HAN:
                populateRealWorldDataTable();
                yakuByAverageHan.setVisibility(View.VISIBLE);
                break;
            case YAKU_COMBINATORICS:
                yakuCombinatorics.setVisibility(View.VISIBLE);
                break;
        }
    }

    ///////////////////////////////////////////////////////
    ///////////       Display Statistics       ////////////
    ///////////////////////////////////////////////////////
    private void populateRealWorldDataTable(){
        if(csvPopulated){
            return;
        }

        InputStream inputStream = null;
        try {
            inputStream = getResources().getAssets().open("RiichiStats.csv");
            CSVFile csvFile = new CSVFile(inputStream);
            List<String[]> scoreList = csvFile.read();

            for( String[] yakuStat : scoreList ){
                addRow(yakuStat[1], yakuStat[2], yakuFrequencyTable);
                addRow(yakuStat[1], yakuStat[4], yakuByAverageHanTable);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        csvPopulated = true;
    }
    private void addRow( String itemName, String col1, TableLayout table ){
        TableRow newRow = new TableRow(getContext());
        TextView yakuName = new TextView(getContext());
        TextView columnOne = new TextView(getContext());

        yakuName.setTextSize(16);
        columnOne.setTextSize(16);
        yakuName.setPadding(10, 0, 0, 0);

        newRow.addView(yakuName);
        newRow.addView(columnOne);

        yakuName.setText(itemName);
        columnOne.setText(col1);

        if( table.getChildCount()==0 ){
            yakuName.setTypeface(null, Typeface.BOLD);
            columnOne.setTypeface(null, Typeface.BOLD);
        }

        if( table.getChildCount()%2==0 ){
            newRow.setBackgroundColor(0xFFEEEEEE);
        } else {
            newRow.setBackgroundColor(0xFFFFFFFF);
        }

        if( itemName.equals("Dora") || itemName.equals("Red Dora") || itemName.equals("Ura Dora") ){
            yakuName.setTypeface(null, Typeface.ITALIC);
            columnOne.setTypeface(null, Typeface.ITALIC);
            yakuName.setText(itemName + "*");
        }

        table.addView(newRow);
    }

    /////////////////////////////////////////////////
    ///////////       Display Yaku       ////////////
    /////////////////////////////////////////////////
    private void displayYaku( int rarity ){
        while( yakuDescriptions.getChildCount()>2 ){
            yakuDescriptions.removeViewAt(2);
        }

        switch (rarity){
            case 1:
                yakuListNotes.setText(yakuListCommonNotes);
                break;
            case 2:
                yakuListNotes.setText(yakuListStandardNotes);
                break;
            case 3:
                yakuListNotes.setText(yakuListYakumanNotes);
                break;
            case 4:
                yakuListNotes.setText(yakuListRareNotes);
        }

        for( Yaku y : ExampleHands.allYaku ){
            if( y.name!=Yaku.Name.DORA && displayYaku(y.rarity, rarity) ){
                YakuDescription yd = new YakuDescription(getContext());
                yd.setYaku(y);

                checkForCategoryLabel(y, yd);
                yakuDescriptions.addView(yd);
            }
        }

        yakuDescriptions.setVisibility(View.VISIBLE);
    }
    private boolean displayYaku(int yakuRarity, int mode){
        if( mode==2 ){
            return yakuRarity==1 || yakuRarity==2;
        }
        return yakuRarity==mode;
    }
    private void checkForCategoryLabel(Yaku y, YakuDescription yd){
        switch (y.name){
            case PINFU:
                addYakuCategoryLabel("Yaku based on Sequences" );
                yd.showLabels();
                break;
            case TOITOI:
                addYakuCategoryLabel("Yaku based on Triplets/Quads" );
                yd.showLabels();
                break;
            case TANYAO:
                addYakuCategoryLabel("Yaku based on Terminals/Honors" );
                yd.showLabels();
                break;
            case HONITSU:
                addYakuCategoryLabel("Yaku based on Suits" );
                yd.showLabels();
                break;
            case TSUMO:
                addYakuCategoryLabel("Yaku based on Luck" );
                yd.showLabels();
                break;
            case RIICHI:
                addYakuCategoryLabel("Special Criteria" );
                yd.showLabels();
                break;
            case KOKUSHIMUSOU:
                addYakuCategoryLabel("Yakuman Hands" );
                yd.showLabels();
                break;
            case TENHOU:
                addYakuCategoryLabel("Yakuman on Opening Hands" );
                yd.showLabels();
                break;
            case SANRENKOU:
                addYakuCategoryLabel("Uncommon Yaku" );
                yd.showLabels();
                break;
        }
    }
    private void addYakuCategoryLabel(String s){
        TextView tv = new TextView(getContext());
        tv.setText(s);
        tv.setTextSize(22);
        tv.setTypeface(null, Typeface.BOLD);
        tv.setPadding(50,30,0,5);
        yakuDescriptions.addView(tv);
    }

    /////////////////////////////////////////
    ///////////       Init       ////////////
    /////////////////////////////////////////
    private void registerUIElements(View myInflatedView){
        spinner = (Spinner) myInflatedView.findViewById(R.id.spinner);
        initializeDropdown();
        spinner.setOnItemSelectedListener(this);
        spinner.setOnTouchListener(this);

        yakuDescriptions= (LinearLayout) myInflatedView.findViewById(R.id.yakuDescriptions);
        yakuFrequency = (LinearLayout) myInflatedView.findViewById(R.id.yakuFrequency);
        yakuByAverageHan = (LinearLayout) myInflatedView.findViewById(R.id.yakuByAverageHan);
        yakuCombinatorics = (LinearLayout) myInflatedView.findViewById(R.id.yakuCombinatorics);

        yakuFrequencyTable = (TableLayout) myInflatedView.findViewById(R.id.yakuFrequencyTable);
        yakuByAverageHanTable = (TableLayout) myInflatedView.findViewById(R.id.yakuByAverageHanTable);
        yakuCombinatoricsTable = (TableLayout) myInflatedView.findViewById(R.id.yakuCombinatoricsTable);

        yakuListNotes = (TextView) myInflatedView.findViewById(R.id.yakuListNotes);
    }
    private void initializeDropdown(){
        List<String> viewOptions = new ArrayList<>();
        viewOptions.add(YAKU_LIST_COMMON);
        viewOptions.add(YAKU_LIST_STANDARD);
        viewOptions.add(YAKU_LIST_YAKUMAN);
        viewOptions.add(YAKU_LIST_RARE);
        viewOptions.add(YAKU_BY_FREQUENCY_LABEL);
        viewOptions.add(YAKU_BY_AVERAGE_HAN);
//        viewOptions.add(YAKU_COMBINATORICS);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_large, R.id.spinneritem, viewOptions);
        spinner.setAdapter(dataAdapter);
    }
}
