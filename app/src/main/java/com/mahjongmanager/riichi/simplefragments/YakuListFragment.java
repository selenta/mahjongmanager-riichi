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

import com.mahjongmanager.riichi.CSVFile;
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
        initializeSpinner();
        spinner.setSelection(0);
        setView();

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

    private void initializeSpinner(){
        List<String> viewOptions = new ArrayList<>();
        viewOptions.add(YAKU_LIST_BY_PATTERN);
        viewOptions.add(YAKU_LIST_BY_HAN);
        viewOptions.add(YAKU_BY_FREQUENCY_LABEL);
        viewOptions.add(YAKU_BY_AVERAGE_HAN);
//        viewOptions.add(YAKU_COMBINATORICS);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>
                (getActivity(), R.layout.spinner_item_large, R.id.spinneritem, viewOptions);
        spinner.setAdapter(dataAdapter);

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
