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
    CheckBox kanDora;
    CheckBox limitUraDora;
    CheckBox goldenDora;
    CheckBox westRound;
    CheckBox kanDoraImmediately;
    CheckBox agariYame;
    CheckBox allowBoxing;
    CheckBox riichiWhenLowOnPoints;
    CheckBox atamaHaneRon;
    CheckBox kanCanChangeWait;
    CheckBox doubleYakuman;

    CheckBox openTanyao;
    CheckBox openPinfu;
    CheckBox openRiichi;
    CheckBox nagashiMangan;
    CheckBox sanrenkou;
    CheckBox suurenkou;
    CheckBox daisharin;
    CheckBox shiisanpuuta;
    CheckBox shiisuupuuta;
    CheckBox parenchan;

    CheckBox abortive4Winds;
    CheckBox abortive4Riichi;
    CheckBox abortiveKyushukyuhai;
    CheckBox abortive4Kans;

    RadioGroup redDoraCount;
    RadioGroup threePlayerStartingPoints;
    RadioGroup threePlayerPlacementBonus1;
    RadioGroup threePlayerChomboSize;
    RadioGroup fourPlayerStartingPoints;
    RadioGroup fourPlayerPlacementBonus1;
    RadioGroup fourPlayerPlacementBonus2;
    RadioGroup fourPlayerChomboSize;
    RadioGroup fivePlayerStartingPoints;
    RadioGroup fivePlayerPlacementBonus1;
    RadioGroup fivePlayerPlacementBonus2;
    RadioGroup fivePlayerChomboSize;

    String KAN_DORA       = "KanDora";
    String LIMIT_URA_DORA = "LimitUraDora";
    String GOLDEN_DORA    = "GoldenDora";
    String WEST_ROUND     = "WestRound";
    String KAN_DORA_IMMEDIATELY      = "KanDoraImmediately";
    String AGARI_YAME                = "AgariYame";
    String ALLOW_BOXING              = "AllowBoxing";
    String RIICHI_WHEN_LOW_ON_POINTS = "RiichiWhenLowOnPoints";
    String ATAMA_HANE_RON        = "AtamaHaneRon";
    String KAN_CAN_CHANGE_WAIT   = "KanCanChangeWait";
    String DOUBLE_YAKUMAN        = "DoubleYakuman";

    String OPEN_TANYAO    = "OpenTanyao";
    String OPEN_PINFU     = "OpenPinfu";
    String OPEN_RIICHI    = "OpenRiichi";
    String NAGASHI_MANGAN = "NagashiMangan";
    String SANRENKOU      = "Sanrenkou";
    String SUURENKOU      = "Suurenkou";
    String DAISHARIN      = "Daisharin";
    String SHIISANPUUTA   = "Shiisanpuuta";
    String SHIISUUPUUTA   = "Shiisuupuuta";
    String PARENCHAN      = "Parenchan";

    String ABORTIVE_4_WINDS      = "Abortive4Winds";
    String ABORTIVE_4_RIICHI     = "Abortive4Riichi";
    String ABORTIVE_KYUSHUKYUHAI = "AbortiveKyushukyuhai";
    String ABORTIVE_4_KANS       = "Abortive4Kans";

    String RED_DORA_COUNT                 = "RedDoraCount";
    String THREE_PLAYER_STARTING_POINTS   = "ThreePlayerStartingPoints";
    String THREE_PLAYER_PLACEMENT_BONUS_1 = "ThreePlayerPlacementBonus1";
    String THREE_PLAYER_CHOMBO_SIZE       = "ThreePlayerChomboSize";
    String FOUR_PLAYER_STARTING_POINTS    = "FourPlayerStartingPoints";
    String FOUR_PLAYER_PLACEMENT_BONUS_1  = "FourPlayerPlacementBonus1";
    String FOUR_PLAYER_PLACEMENT_BONUS_2  = "FourPlayerPlacementBonus2";
    String FOUR_PLAYER_CHOMBO_SIZE        = "FourPlayerChomboSize";
    String FIVE_PLAYER_STARTING_POINTS    = "FivePlayerStartingPoints";
    String FIVE_PLAYER_PLACEMENT_BONUS_1  = "FivePlayerPlacementBonus1";
    String FIVE_PLAYER_PLACEMENT_BONUS_2  = "FivePlayerPlacementBonus2";
    String FIVE_PLAYER_CHOMBO_SIZE        = "FivePlayerChomboSize";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.fragment_options, container, false);

        registerCheckBoxes(myInflatedView);
        registerRadioGroups(myInflatedView);
        loadSavedSettings();

        return myInflatedView;
    }
    private void loadSavedSettings(){
        setCheckedStatus(kanDora, KAN_DORA, true);
        setCheckedStatus(limitUraDora, LIMIT_URA_DORA, false);
        setCheckedStatus(goldenDora, GOLDEN_DORA, false);
        setCheckedStatus(westRound, WEST_ROUND, true);
        setCheckedStatus(kanDoraImmediately, KAN_DORA_IMMEDIATELY, false);
        setCheckedStatus(agariYame, AGARI_YAME, true);
        setCheckedStatus(allowBoxing, ALLOW_BOXING, true);
        setCheckedStatus(riichiWhenLowOnPoints, RIICHI_WHEN_LOW_ON_POINTS, false);
        setCheckedStatus(atamaHaneRon, ATAMA_HANE_RON, false);
        setCheckedStatus(kanCanChangeWait, KAN_CAN_CHANGE_WAIT, false);
        setCheckedStatus(doubleYakuman, DOUBLE_YAKUMAN, true);

        setCheckedStatus(openTanyao, OPEN_TANYAO, true);
        setCheckedStatus(openPinfu, OPEN_PINFU, false);
        setCheckedStatus(openRiichi, OPEN_RIICHI, false);
        setCheckedStatus(nagashiMangan, NAGASHI_MANGAN, true);
        setCheckedStatus(sanrenkou, SANRENKOU, true);
        setCheckedStatus(suurenkou, SUURENKOU, false);
        setCheckedStatus(daisharin, DAISHARIN, false);
        setCheckedStatus(shiisanpuuta, SHIISANPUUTA, false);
        setCheckedStatus(shiisuupuuta, SHIISUUPUUTA, false);
        setCheckedStatus(parenchan, PARENCHAN, true);

        setCheckedStatus(abortive4Winds, ABORTIVE_4_WINDS, true);
        setCheckedStatus(abortive4Riichi, ABORTIVE_4_RIICHI, true);
        setCheckedStatus(abortiveKyushukyuhai, ABORTIVE_KYUSHUKYUHAI, true);
        setCheckedStatus(abortive4Kans, ABORTIVE_4_KANS, true);

        setCheckedStatus(redDoraCount, RED_DORA_COUNT, 3);
        setCheckedStatus(threePlayerStartingPoints, THREE_PLAYER_STARTING_POINTS, 35000);
        setCheckedStatus(threePlayerPlacementBonus1, THREE_PLAYER_PLACEMENT_BONUS_1, 15);
        setCheckedStatus(threePlayerChomboSize, THREE_PLAYER_CHOMBO_SIZE, 6000);
        setCheckedStatus(fourPlayerStartingPoints, FOUR_PLAYER_STARTING_POINTS, 25000);
        setCheckedStatus(fourPlayerPlacementBonus1, FOUR_PLAYER_PLACEMENT_BONUS_1, 15);
        setCheckedStatus(fourPlayerPlacementBonus2, FOUR_PLAYER_PLACEMENT_BONUS_2, 5);
        setCheckedStatus(fourPlayerChomboSize, FOUR_PLAYER_CHOMBO_SIZE, 3000);
        setCheckedStatus(fivePlayerStartingPoints, FIVE_PLAYER_STARTING_POINTS, 25000);
        setCheckedStatus(fivePlayerPlacementBonus1, FIVE_PLAYER_PLACEMENT_BONUS_1, 20);
        setCheckedStatus(fivePlayerPlacementBonus2, FIVE_PLAYER_PLACEMENT_BONUS_2, 10);
        setCheckedStatus(fivePlayerChomboSize, FIVE_PLAYER_CHOMBO_SIZE, 2000);
    }
    private void setCheckedStatus( CheckBox cBox, String settingName, boolean def ){
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        boolean val = sharedPref.getBoolean(settingName, def);
        cBox.setChecked(val);
    }
    private void setCheckedStatus( RadioGroup rGroup, String settingName, Integer def ){
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        Integer val = sharedPref.getInt(settingName, def);
        for( int i=0; i<rGroup.getChildCount(); i++ ){
            RadioButton child = (RadioButton)rGroup.getChildAt(i);
            boolean isCorrect = child.getText().equals(val.toString());
            child.setChecked(isCorrect);
        }
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.kanDoraCheckBox:
                savePreferenceCheckBox(kanDora, KAN_DORA);
                break;
            case R.id.limitUraDora:
                savePreferenceCheckBox(limitUraDora, LIMIT_URA_DORA);
                break;
            case R.id.goldenDora:
                savePreferenceCheckBox(goldenDora, GOLDEN_DORA);
                break;
            case R.id.westRound:
                savePreferenceCheckBox(westRound, WEST_ROUND);
                break;
            case R.id.kanDoraImmediately:
                savePreferenceCheckBox(kanDoraImmediately, KAN_DORA_IMMEDIATELY);
                break;
            case R.id.agariYame:
                savePreferenceCheckBox(agariYame, AGARI_YAME);
                break;
            case R.id.allowBoxing:
                savePreferenceCheckBox(allowBoxing, ALLOW_BOXING);
                break;
            case R.id.riichiWhenLowOnPoints:
                savePreferenceCheckBox(riichiWhenLowOnPoints, RIICHI_WHEN_LOW_ON_POINTS);
                break;
            case R.id.atamaHaneRon:
                savePreferenceCheckBox(atamaHaneRon, ATAMA_HANE_RON);
                break;
            case R.id.kanCanChangeWait:
                savePreferenceCheckBox(kanCanChangeWait, KAN_CAN_CHANGE_WAIT);
                break;
            case R.id.doubleYakuman:
                savePreferenceCheckBox(doubleYakuman, DOUBLE_YAKUMAN);
                break;
            case R.id.openTanyao:
                savePreferenceCheckBox(openTanyao, OPEN_TANYAO);
                break;
            case R.id.openPinfu:
                savePreferenceCheckBox(openPinfu, OPEN_PINFU);
                break;
            case R.id.openRiichi:
                savePreferenceCheckBox(openRiichi, OPEN_RIICHI);
                break;
            case R.id.nagashiMangan:
                savePreferenceCheckBox(nagashiMangan, NAGASHI_MANGAN);
                break;
            case R.id.sanrenkou:
                savePreferenceCheckBox(sanrenkou, SANRENKOU);
                break;
            case R.id.suurenkou:
                savePreferenceCheckBox(suurenkou, SUURENKOU);
                break;
            case R.id.daisharin:
                savePreferenceCheckBox(daisharin, DAISHARIN);
                break;
            case R.id.shiisanpuuta:
                savePreferenceCheckBox(shiisanpuuta, SHIISANPUUTA);
                break;
            case R.id.shiisuupuuta:
                savePreferenceCheckBox(shiisuupuuta, SHIISUUPUUTA);
                break;
            case R.id.parenchan:
                savePreferenceCheckBox(parenchan, PARENCHAN);
                break;
            case R.id.abortive4Winds:
                savePreferenceCheckBox(abortive4Winds, ABORTIVE_4_WINDS);
                break;
            case R.id.abortive4Riichi:
                savePreferenceCheckBox(abortive4Riichi, ABORTIVE_4_RIICHI);
                break;
            case R.id.abortiveKyushukyuhai:
                savePreferenceCheckBox(abortiveKyushukyuhai, ABORTIVE_KYUSHUKYUHAI);
                break;
            case R.id.abortive4Kans:
                savePreferenceCheckBox(abortive4Kans, ABORTIVE_4_KANS);
                break;
        }
    }
    private void savePreferenceCheckBox(CheckBox cBox, String label){
        Boolean checked = cBox.isChecked();

        SharedPreferences settings = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(label, checked);
        editor.apply();
    }

    private void registerCheckBoxes(View myInflatedView){
        kanDora = (CheckBox) myInflatedView.findViewById(R.id.kanDoraCheckBox);
        kanDora.setOnClickListener(this);
        limitUraDora = (CheckBox) myInflatedView.findViewById(R.id.limitUraDora);
        limitUraDora.setOnClickListener(this);
        goldenDora = (CheckBox) myInflatedView.findViewById(R.id.goldenDora);
        goldenDora.setOnClickListener(this);
        westRound = (CheckBox) myInflatedView.findViewById(R.id.westRound);
        westRound.setOnClickListener(this);
        kanDoraImmediately = (CheckBox) myInflatedView.findViewById(R.id.kanDoraImmediately);
        kanDoraImmediately.setOnClickListener(this);
        agariYame = (CheckBox) myInflatedView.findViewById(R.id.agariYame);
        agariYame.setOnClickListener(this);
        allowBoxing = (CheckBox) myInflatedView.findViewById(R.id.allowBoxing);
        allowBoxing.setOnClickListener(this);
        riichiWhenLowOnPoints = (CheckBox) myInflatedView.findViewById(R.id.riichiWhenLowOnPoints);
        riichiWhenLowOnPoints.setOnClickListener(this);
        atamaHaneRon = (CheckBox) myInflatedView.findViewById(R.id.atamaHaneRon);
        atamaHaneRon.setOnClickListener(this);
        kanCanChangeWait = (CheckBox) myInflatedView.findViewById(R.id.kanCanChangeWait);
        kanCanChangeWait.setOnClickListener(this);
        doubleYakuman = (CheckBox) myInflatedView.findViewById(R.id.doubleYakuman);
        doubleYakuman.setOnClickListener(this);

        openTanyao = (CheckBox) myInflatedView.findViewById(R.id.openTanyao);
        openTanyao.setOnClickListener(this);
        openPinfu = (CheckBox) myInflatedView.findViewById(R.id.openPinfu);
        openPinfu.setOnClickListener(this);
        openRiichi = (CheckBox) myInflatedView.findViewById(R.id.openRiichi);
        openRiichi.setOnClickListener(this);
        nagashiMangan = (CheckBox) myInflatedView.findViewById(R.id.nagashiMangan);
        nagashiMangan.setOnClickListener(this);
        sanrenkou = (CheckBox) myInflatedView.findViewById(R.id.sanrenkou);
        sanrenkou.setOnClickListener(this);
        suurenkou = (CheckBox) myInflatedView.findViewById(R.id.suurenkou);
        suurenkou.setOnClickListener(this);
        daisharin = (CheckBox) myInflatedView.findViewById(R.id.daisharin);
        daisharin.setOnClickListener(this);
        shiisanpuuta = (CheckBox) myInflatedView.findViewById(R.id.shiisanpuuta);
        shiisanpuuta.setOnClickListener(this);
        shiisuupuuta = (CheckBox) myInflatedView.findViewById(R.id.shiisuupuuta);
        shiisuupuuta.setOnClickListener(this);
        parenchan = (CheckBox) myInflatedView.findViewById(R.id.parenchan);
        parenchan.setOnClickListener(this);

        abortive4Winds = (CheckBox) myInflatedView.findViewById(R.id.abortive4Winds);
        abortive4Winds.setOnClickListener(this);
        abortive4Riichi = (CheckBox) myInflatedView.findViewById(R.id.abortive4Riichi);
        abortive4Riichi.setOnClickListener(this);
        abortiveKyushukyuhai = (CheckBox) myInflatedView.findViewById(R.id.abortiveKyushukyuhai);
        abortiveKyushukyuhai.setOnClickListener(this);
        abortive4Kans = (CheckBox) myInflatedView.findViewById(R.id.abortive4Kans);
        abortive4Kans.setOnClickListener(this);
    }
    private void registerRadioGroups(View myInflatedView){
        redDoraCount = (RadioGroup) myInflatedView.findViewById(R.id.redDoraCount);
        redDoraCount.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                setRedDoraCount();
            }
        });
        threePlayerStartingPoints = (RadioGroup) myInflatedView.findViewById(R.id.threePlayerStartingPoints);
        threePlayerStartingPoints.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                setThreePlayerStartingPoints();
            }
        });
        threePlayerPlacementBonus1 = (RadioGroup) myInflatedView.findViewById(R.id.threePlayerPlacementBonus1);
        threePlayerPlacementBonus1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                setThreePlayerPlacementBonus1();
            }
        });
        threePlayerChomboSize = (RadioGroup) myInflatedView.findViewById(R.id.threePlayerChomboSize);
        threePlayerChomboSize.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                setThreePlayerChomboSize();
            }
        });
        fourPlayerStartingPoints = (RadioGroup) myInflatedView.findViewById(R.id.fourPlayerStartingPoints);
        fourPlayerStartingPoints.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                setFourPlayerStartingPoints();
            }
        });
        fourPlayerPlacementBonus1 = (RadioGroup) myInflatedView.findViewById(R.id.fourPlayerPlacementBonus1);
        fourPlayerPlacementBonus1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                setFourPlayerPlacementBonus1();
            }
        });
        fourPlayerPlacementBonus2 = (RadioGroup) myInflatedView.findViewById(R.id.fourPlayerPlacementBonus2);
        fourPlayerPlacementBonus2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                setFourPlayerPlacementBonus2();
            }
        });
        fourPlayerChomboSize = (RadioGroup) myInflatedView.findViewById(R.id.fourPlayerChomboSize);
        fourPlayerChomboSize.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                setFourPlayerChomboSize();
            }
        });
        fivePlayerStartingPoints = (RadioGroup) myInflatedView.findViewById(R.id.fivePlayerStartingPoints);
        fivePlayerStartingPoints.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                setFivePlayerStartingPoints();
            }
        });
        fivePlayerPlacementBonus1 = (RadioGroup) myInflatedView.findViewById(R.id.fivePlayerPlacementBonus1);
        fivePlayerPlacementBonus1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                setFivePlayerPlacementBonus1();
            }
        });
        fivePlayerPlacementBonus2 = (RadioGroup) myInflatedView.findViewById(R.id.fivePlayerPlacementBonus2);
        fivePlayerPlacementBonus2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                setFivePlayerPlacementBonus2();
            }
        });
        fivePlayerChomboSize = (RadioGroup) myInflatedView.findViewById(R.id.fivePlayerChomboSize);
        fivePlayerChomboSize.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                setFivePlayerChomboSize();
            }
        });
    }
    private void setRedDoraCount(){
        savePreferenceButton(redDoraCount, RED_DORA_COUNT);
    }
    private void setThreePlayerStartingPoints(){
        savePreferenceButton(threePlayerStartingPoints, THREE_PLAYER_STARTING_POINTS);
    }
    private void setThreePlayerPlacementBonus1(){
        savePreferenceButton(threePlayerPlacementBonus1, THREE_PLAYER_PLACEMENT_BONUS_1);
    }
    private void setThreePlayerChomboSize(){
        savePreferenceButton(threePlayerChomboSize, THREE_PLAYER_CHOMBO_SIZE);
    }
    private void setFourPlayerStartingPoints(){
        savePreferenceButton(fourPlayerStartingPoints, FOUR_PLAYER_STARTING_POINTS);
    }
    private void setFourPlayerPlacementBonus1(){
        savePreferenceButton(fourPlayerPlacementBonus2, FOUR_PLAYER_PLACEMENT_BONUS_1);
    }
    private void setFourPlayerPlacementBonus2(){
        savePreferenceButton(fourPlayerPlacementBonus2, FOUR_PLAYER_PLACEMENT_BONUS_2);
    }
    private void setFourPlayerChomboSize(){
        savePreferenceButton(fourPlayerChomboSize, FOUR_PLAYER_CHOMBO_SIZE);
    }
    private void setFivePlayerStartingPoints(){
        savePreferenceButton(fivePlayerStartingPoints, FIVE_PLAYER_STARTING_POINTS);
    }
    private void setFivePlayerPlacementBonus1(){
        savePreferenceButton(fivePlayerPlacementBonus1, FIVE_PLAYER_PLACEMENT_BONUS_1);
    }
    private void setFivePlayerPlacementBonus2(){
        savePreferenceButton(fivePlayerPlacementBonus2, FIVE_PLAYER_PLACEMENT_BONUS_2);
    }
    private void setFivePlayerChomboSize(){
        savePreferenceButton(fivePlayerChomboSize, FIVE_PLAYER_CHOMBO_SIZE);
    }
    private void savePreferenceButton(RadioGroup rGroup, String label){
        RadioButton rButt = (RadioButton)rGroup.findViewById(rGroup.getCheckedRadioButtonId());
        Integer intVal = Integer.valueOf(rButt.getText().toString());

        SharedPreferences settings = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(label, intVal);
        editor.apply();
    }
}
