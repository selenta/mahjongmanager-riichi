package com.mahjongmanager.riichi.simplefragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.mahjongmanager.riichi.R;
import com.mahjongmanager.riichi.utils.AppSettings;

public class OptionsFragment extends Fragment implements View.OnClickListener {

    private CheckBox tileKeyboardCheckbox;
    private CheckBox randomWindsCheckbox;
    private CheckBox separateClosedMeldsCheckbox;
    private CheckBox addSituationalYakuCheckbox;
    private CheckBox allowHonorsCheckbox;
    private CheckBox enableBannerAdsCheckbox;

    private RadioGroup terminology;
    private RadioGroup speedQuizNumberOfSuits;
    private RadioGroup speedQuizMaxHands;

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
        tileKeyboardCheckbox.setChecked( AppSettings.getKeyboardTileSize() );
        randomWindsCheckbox.setChecked( AppSettings.getSpeedQuizRandomWinds() );
        separateClosedMeldsCheckbox.setChecked( AppSettings.getSpeedQuizSeparateClosedMelds() );
        addSituationalYakuCheckbox.setChecked( AppSettings.getSpeedQuizSituationalYaku() );
        loadNumberOfSuits();
        allowHonorsCheckbox.setChecked( AppSettings.getSpeedQuizAllowHonors() );
        enableBannerAdsCheckbox.setChecked( AppSettings.getBannerAdsEnabled() );
        loadMaxHands();
    }
    private void loadTerminology(){
        String val = AppSettings.getTerminology();

        for( int i=0; i<terminology.getChildCount(); i++ ){
            RadioButton child = (RadioButton)terminology.getChildAt(i);
            boolean isCorrect = child.getText().equals(val);
            child.setChecked(isCorrect);
        }
    }
    private void loadNumberOfSuits(){
        int val = AppSettings.getSpeedQuizNumberOfSuits();

        for( int i=0; i<speedQuizNumberOfSuits.getChildCount(); i++ ){
            RadioButton child = (RadioButton)speedQuizNumberOfSuits.getChildAt(i);
            boolean isCorrect = (val == Integer.parseInt(child.getText().toString()));
            child.setChecked(isCorrect);
        }
    }
    private void loadMaxHands(){
        Integer val = AppSettings.getSpeedQuizMaxHands();

        for( int i=0; i<speedQuizMaxHands.getChildCount(); i++ ){
            RadioButton child = (RadioButton)speedQuizMaxHands.getChildAt(i);
            boolean isCorrect = (val == Integer.parseInt(child.getText().toString()));
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
                AppSettings.setKeyboardTileSize(tileKeyboardCheckbox.isChecked());
                break;
            case R.id.randomWindsCheckbox:
                AppSettings.setSpeedQuizRandomWinds(randomWindsCheckbox.isChecked());
                break;
            case R.id.separateClosedMeldsCheckbox:
                AppSettings.setSpeedQuizSeparateClosedMelds(separateClosedMeldsCheckbox.isChecked());
                break;
            case R.id.addSituationalYakuCheckbox:
                AppSettings.setSpeedQuizSituationalYaku(addSituationalYakuCheckbox.isChecked());
                break;
            case R.id.allowHonorsCheckbox:
                AppSettings.setSpeedQuizAllowHonors(allowHonorsCheckbox.isChecked());
                break;
            case R.id.enableBannerAdsCheckbox:
                AppSettings.setBannerAdsEnabled(enableBannerAdsCheckbox.isChecked());
                break;
        }
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

        allowHonorsCheckbox = (CheckBox) myInflatedView.findViewById(R.id.allowHonorsCheckbox);
        allowHonorsCheckbox.setOnClickListener(this);

        enableBannerAdsCheckbox = (CheckBox) myInflatedView.findViewById(R.id.enableBannerAdsCheckbox);
        enableBannerAdsCheckbox.setOnClickListener(this);
    }
    private void registerRadioGroups(View myInflatedView){
        terminology = (RadioGroup) myInflatedView.findViewById(R.id.terminology);
        terminology.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                saveTerminology(group, checkedId);
            }
        });

        speedQuizNumberOfSuits = (RadioGroup) myInflatedView.findViewById(R.id.speedQuizNumberOfSuits);
        speedQuizNumberOfSuits.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                saveNumberOfSuits(group, checkedId);
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
                AppSettings.setTerminology("English");
                break;
            case R.id.terminologyRomaji:
                AppSettings.setTerminology("Romaji");
                break;
            case R.id.terminologyKanji:
                AppSettings.setTerminology("Kanji");
                break;
        }
    }
    private void saveNumberOfSuits(RadioGroup group, int checkedId){
        switch (checkedId){
            case R.id.speedQuizNumberOfSuits1:
                AppSettings.setSpeedQuizNumberOfSuits(1);
                break;
            case R.id.speedQuizNumberOfSuits2:
                AppSettings.setSpeedQuizNumberOfSuits(2);
                break;
            case R.id.speedQuizNumberOfSuits3:
                AppSettings.setSpeedQuizNumberOfSuits(3);
                break;
        }
    }
    private void saveMaxHands(RadioGroup group, int checkedId){
        switch (checkedId) {
            case R.id.speedQuizMaxHands10:
                AppSettings.setSpeedQuizMaxHands(10);
                break;
            case R.id.speedQuizMaxHands20:
                AppSettings.setSpeedQuizMaxHands(20);
                break;
        }
    }
}
