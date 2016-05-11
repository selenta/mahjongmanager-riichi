package com.mahjongmanager.riichi.simplefragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.mahjongmanager.riichi.MainActivity;
import com.mahjongmanager.riichi.Yaku;
import com.mahjongmanager.riichi.components.YakuDescription;
import com.mahjongmanager.riichi.utils.CSVFile;
import com.mahjongmanager.riichi.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class YakuListFragment extends Fragment {
    Spinner spinner;

    LinearLayout yakuListByPattern;
    LinearLayout yakuListByHan;
    LinearLayout yakuFrequency;
    LinearLayout yakuByAverageHan;
    LinearLayout yakuCombinatorics;

    TableLayout yakuFrequencyTable;
    TableLayout yakuByAverageHanTable;
    TableLayout yakuCombinatoricsTable;

    static final String YAKU_LIST_BY_PATTERN = "Yaku Guide sorted by Pattern";
    static final String YAKU_LIST_BY_HAN = "Yaku Guide sorted by Han";
    static final String YAKU_BY_FREQUENCY_LABEL = "Han sorted by Frequency";
    static final String YAKU_BY_AVERAGE_HAN = "Han sorted by Avg. Hand Size";
    static final String YAKU_COMBINATORICS = "Yaku Combinatorics (math)";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.fragment_yakulist, container, false);

        registerControls(myInflatedView);

        populateRealWorldDataTable();
        initializeDropdown();
        spinner.setSelection(0);
        setView();

        populateYakuSortedByPattern();

        return myInflatedView;
    }

    private void setView(){
        yakuListByPattern.setVisibility(View.GONE);
        yakuListByHan.setVisibility(View.GONE);
        yakuFrequency.setVisibility(View.GONE);
        yakuByAverageHan.setVisibility(View.GONE);
        yakuCombinatorics.setVisibility(View.GONE);
        switch (spinner.getSelectedItem().toString()){
            case YAKU_LIST_BY_PATTERN:
                yakuListByPattern.setVisibility(View.VISIBLE);
                break;
            case YAKU_LIST_BY_HAN:
                yakuListByHan.setVisibility(View.VISIBLE);
                break;
            case YAKU_BY_FREQUENCY_LABEL:
                yakuFrequency.setVisibility(View.VISIBLE);
                break;
            case YAKU_BY_AVERAGE_HAN:
                yakuByAverageHan.setVisibility(View.VISIBLE);
                break;
            case YAKU_COMBINATORICS:
                yakuCombinatorics.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void populateRealWorldDataTable(){
        InputStream inputStream = null;
        try {
            inputStream = getResources().getAssets().open("RiichiStats.csv");
            CSVFile csvFile = new CSVFile(inputStream);
            List<String[]> scoreList = csvFile.read();

            for( String[] yakuStat : scoreList ){
                Log.i("readCsv", "yakuStat: " + Arrays.toString(yakuStat));
                addRow(yakuStat[1], yakuStat[2], yakuFrequencyTable);
                addRow(yakuStat[1], yakuStat[4], yakuByAverageHanTable);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

        table.addView(newRow);
    }

    private void populateYakuSortedByPattern(){
        List<Yaku> allYaku = ((MainActivity)getContext()).getExampleHands().allYaku;
        for( Yaku y : allYaku ){
            YakuDescription yd = new YakuDescription(getContext());
            yd.setYaku(y);

            checkForCategoryByPattern(y, yd);
            yakuListByPattern.addView(yd);
        }
    }
    private void checkForCategoryByPattern(Yaku y, YakuDescription yd){
        switch (y.name){
            case PINFU:
                addYakuCategoryLabel(yakuListByPattern, "Yaku based on Sequences" );
                yd.showLabels();
                break;
            case TOITOI:
                addYakuCategoryLabel(yakuListByPattern, "Yaku based on Triplets/Quads" );
                yd.showLabels();
                break;
            case TANYAO:
                addYakuCategoryLabel(yakuListByPattern, "Yaku based on Terminals/Honors" );
                yd.showLabels();
                break;
            case HONITSU:
                addYakuCategoryLabel(yakuListByPattern, "Yaku based on Suits" );
                yd.showLabels();
                break;
            case TSUMO:
                addYakuCategoryLabel(yakuListByPattern, "Yaku based on Luck" );
                yd.showLabels();
                break;
            case RIICHI:
                addYakuCategoryLabel(yakuListByPattern, "Special Criteria" );
                yd.showLabels();
                break;
            case KOKUSHIMUSOU:
                addYakuCategoryLabel(yakuListByPattern, "Yakuman Hands" );
                yd.showLabels();
                break;
            case TENHOU:
                addYakuCategoryLabel(yakuListByPattern, "Yakuman on Opening Hands" );
                yd.showLabels();
                break;
            case SANRENKOU:
                addYakuCategoryLabel(yakuListByPattern, "Uncommon Yaku" );
                yd.showLabels();
                break;
        }
    }
    private void addYakuCategoryLabel(LinearLayout ll, String s){
        TextView tv = new TextView(getContext());
        tv.setText(s);
        tv.setTextSize(22);
        tv.setTypeface(null, Typeface.BOLD);
        tv.setPadding(50,30,0,5);
        ll.addView(tv);
    }

    private void initializeDropdown(){
        List<String> viewOptions = new ArrayList<>();
        viewOptions.add(YAKU_LIST_BY_PATTERN);
//        viewOptions.add(YAKU_LIST_BY_HAN);
        viewOptions.add(YAKU_BY_FREQUENCY_LABEL);
        viewOptions.add(YAKU_BY_AVERAGE_HAN);
//        viewOptions.add(YAKU_COMBINATORICS);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>
                (getActivity(), R.layout.spinner_item_large, R.id.spinneritem, viewOptions);
        spinner.setAdapter(dataAdapter);
    }
    private void registerControls(View myInflatedView){
        spinner = (Spinner) myInflatedView.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                setView();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        yakuListByPattern= (LinearLayout) myInflatedView.findViewById(R.id.yakuListByPattern);
        yakuListByHan= (LinearLayout) myInflatedView.findViewById(R.id.yakuListByHan);
        yakuFrequency = (LinearLayout) myInflatedView.findViewById(R.id.yakuFrequency);
        yakuByAverageHan = (LinearLayout) myInflatedView.findViewById(R.id.yakuByAverageHan);
        yakuCombinatorics = (LinearLayout) myInflatedView.findViewById(R.id.yakuCombinatorics);

        yakuFrequencyTable = (TableLayout) myInflatedView.findViewById(R.id.yakuFrequencyTable);
        yakuByAverageHanTable = (TableLayout) myInflatedView.findViewById(R.id.yakuByAverageHanTable);
        yakuCombinatoricsTable = (TableLayout) myInflatedView.findViewById(R.id.yakuCombinatoricsTable);
    }
}
