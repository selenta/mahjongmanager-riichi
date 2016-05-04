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

    private RadioGroup terminology;

    private String TERMINOLOGY = "Terminology";
    private String KEYBOARD_TILE_SIZE = "KeyboardSmallTiles";

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

    ///////////////////////////////////////////////////////
    //////////////             Main          //////////////
    ///////////////////////////////////////////////////////
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.tileKeyboadCheckbox:
                savePreferenceBoolean(tileKeyboardCheckbox.isChecked(), KEYBOARD_TILE_SIZE);
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
    }
    private void registerRadioGroups(View myInflatedView){
        terminology = (RadioGroup) myInflatedView.findViewById(R.id.terminology);
        terminology.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                saveTerminology(group, checkedId);
            }
        });
    }
    private void saveTerminology(RadioGroup group, int checkedId){
        RadioButton rEnglish = (RadioButton) group.findViewById(R.id.terminologyEnglish);
        RadioButton rRomaji  = (RadioButton) group.findViewById(R.id.terminologyRomaji);
        RadioButton rKanji   = (RadioButton) group.findViewById(R.id.terminologyKanji);
        RadioButton selected = (RadioButton) group.findViewById(checkedId);

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
    private void savePreference(String keyString, String value){
        SharedPreferences settings = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(keyString, value);
        editor.apply();
    }
}
