package com.mahjongmanager.riichi.simplefragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.mahjongmanager.riichi.R;

public class OptionsFragment extends Fragment implements View.OnClickListener {

    private CheckBox tileKeyboardCheckbox;
    private CheckBox randomWindsCheckbox;
    private CheckBox separateClosedMeldsCheckbox;
    private CheckBox addSituationalYakuCheckbox;

    private RadioGroup terminology;
    private RadioGroup speedQuizMaxHands;

    private String TERMINOLOGY = "Terminology";
    private String KEYBOARD_TILE_SIZE = "KeyboardSmallTiles";
    private String SQ_RANDOM_WINDS = "SQRandomWinds";
    private String SQ_SEPARATE_CLOSED_MELDS = "SQSeparateClosedMelds";
    private String SQ_SITUATIONAL_YAKU = "SQSituationalYaku";
    private String SQ_MAX_HANDS = "SQMaxHands";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.fragment_options, container, false);

        registerCheckBoxes(myInflatedView);
        registerRadioGroups(myInflatedView);
        loadSavedSettings();

        return myInflatedView;
    }
    private void loadSavedSettings(){
        loadTerminology();
        loadSetting(tileKeyboardCheckbox, KEYBOARD_TILE_SIZE, false);
        loadSetting(randomWindsCheckbox, SQ_RANDOM_WINDS, false);
        loadSetting(separateClosedMeldsCheckbox, SQ_SEPARATE_CLOSED_MELDS, true);
        loadSetting(addSituationalYakuCheckbox, SQ_SITUATIONAL_YAKU, false);
        loadMaxHands();
    }
    private void loadSetting( CheckBox cBox, String settingName, boolean def ){
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        boolean val = sharedPref.getBoolean(settingName, def);
        cBox.setChecked(val);
    }
    private void loadTerminology(){
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String val = sharedPref.getString(TERMINOLOGY, "Romaji");

        for( int i=0; i<terminology.getChildCount(); i++ ){
            RadioButton child = (RadioButton)terminology.getChildAt(i);
            boolean isCorrect = child.getText().equals(val);
            child.setChecked(isCorrect);
        }
    }
    private void loadMaxHands(){
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        Integer val = sharedPref.getInt(SQ_MAX_HANDS, 10);

        for( int i=0; i<speedQuizMaxHands.getChildCount(); i++ ){
            RadioButton child = (RadioButton)speedQuizMaxHands.getChildAt(i);
            boolean isCorrect = Integer.valueOf(child.getText().toString()).equals(val);
            child.setChecked(isCorrect);
        }
    }

    ///////////////////////////////////////////////////////
    //////////////             Main          //////////////
    ///////////////////////////////////////////////////////
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.tileKeyboadCheckbox:
                savePreferenceBoolean(tileKeyboardCheckbox.isChecked(), KEYBOARD_TILE_SIZE);
                break;
            case R.id.randomWindsCheckbox:
                savePreferenceBoolean(randomWindsCheckbox.isChecked(), SQ_RANDOM_WINDS);
                break;
            case R.id.separateClosedMeldsCheckbox:
                savePreferenceBoolean(separateClosedMeldsCheckbox.isChecked(), SQ_SEPARATE_CLOSED_MELDS);
                break;
            case R.id.addSituationalYakuCheckbox:
                savePreferenceBoolean(addSituationalYakuCheckbox.isChecked(), SQ_SITUATIONAL_YAKU);
                break;
        }
    }
    private void savePreferenceBoolean(boolean bool, String label){
        SharedPreferences settings = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(label, bool);
        editor.apply();
    }


    ///////////////////////////////////////////////////////
    //////////////             Init          //////////////
    ///////////////////////////////////////////////////////
    private void registerCheckBoxes(View myInflatedView){
        tileKeyboardCheckbox = (CheckBox) myInflatedView.findViewById(R.id.tileKeyboadCheckbox);
        tileKeyboardCheckbox.setOnClickListener(this);

        randomWindsCheckbox = (CheckBox) myInflatedView.findViewById(R.id.randomWindsCheckbox);
        randomWindsCheckbox.setOnClickListener(this);

        separateClosedMeldsCheckbox = (CheckBox) myInflatedView.findViewById(R.id.separateClosedMeldsCheckbox);
        separateClosedMeldsCheckbox.setOnClickListener(this);

        addSituationalYakuCheckbox = (CheckBox) myInflatedView.findViewById(R.id.addSituationalYakuCheckbox);
        addSituationalYakuCheckbox.setOnClickListener(this);
    }
    private void registerRadioGroups(View myInflatedView){
        terminology = (RadioGroup) myInflatedView.findViewById(R.id.terminology);
        terminology.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                saveTerminology(group, checkedId);
            }
        });

        speedQuizMaxHands = (RadioGroup) myInflatedView.findViewById(R.id.speedQuizMaxHands);
        speedQuizMaxHands.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                saveMaxHands(group, checkedId);
            }
        });
    }
    private void saveTerminology(RadioGroup group, int checkedId){
        switch (checkedId){
            case R.id.terminologyEnglish:
                savePreference(TERMINOLOGY, "English");
                break;
            case R.id.terminologyRomaji:
                savePreference(TERMINOLOGY, "Romaji");
                break;
            case R.id.terminologyKanji:
                savePreference(TERMINOLOGY, "Kanji");
                break;
        }
    }
    private void saveMaxHands(RadioGroup group, int checkedId){
        switch (checkedId) {
            case R.id.speedQuizMaxHands10:
                savePreference(SQ_MAX_HANDS, 10);
                break;
            case R.id.speedQuizMaxHands20:
                savePreference(SQ_MAX_HANDS, 20);
                break;
        }
    }
    private void savePreference(String keyString, String value){
        SharedPreferences settings = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(keyString, value);
        editor.apply();
    }
    private void savePreference(String keyString, Integer value){
        SharedPreferences settings = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(keyString, value);
        editor.apply();
    }
}
