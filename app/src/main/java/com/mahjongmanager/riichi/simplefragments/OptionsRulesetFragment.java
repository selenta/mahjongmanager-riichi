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

public class OptionsRulesetFragment extends Fragment implements View.OnClickListener {

    private CheckBox kanDora;
    private CheckBox limitUraDora;
    private CheckBox goldenDora;
    private CheckBox westRound;
    private CheckBox kanDoraImmediately;
    private CheckBox agariYame;
    private CheckBox allowBoxing;
    private CheckBox riichiWhenLowOnPoints;
    private CheckBox atamaHaneRon;
    private CheckBox kanCanChangeWait;
    private CheckBox doubleYakuman;

    private CheckBox openTanyao;
    private CheckBox openPinfu;
    private CheckBox openRiichi;
    private CheckBox nagashiMangan;
    private CheckBox sanrenkou;
    private CheckBox suurenkou;
    private CheckBox daisharin;
    private CheckBox shiisanpuuta;
    private CheckBox shiisuupuuta;
    private CheckBox parenchan;

    private CheckBox abortive4Winds;
    private CheckBox abortive4Riichi;
    private CheckBox abortiveKyushukyuhai;
    private CheckBox abortive4Kans;

    private RadioGroup redDoraCount;
    private RadioGroup threePlayerStartingPoints;
    private RadioGroup threePlayerPlacementBonus1;
    private RadioGroup threePlayerChomboSize;
    private RadioGroup fourPlayerStartingPoints;
    private RadioGroup fourPlayerPlacementBonus1;
    private RadioGroup fourPlayerPlacementBonus2;
    private RadioGroup fourPlayerChomboSize;
    private RadioGroup fivePlayerStartingPoints;
    private RadioGroup fivePlayerPlacementBonus1;
    private RadioGroup fivePlayerPlacementBonus2;
    private RadioGroup fivePlayerChomboSize;

    private String KAN_DORA       = "KanDora";
    private String LIMIT_URA_DORA = "LimitUraDora";
    private String GOLDEN_DORA    = "GoldenDora";
    private String WEST_ROUND     = "WestRound";
    private String KAN_DORA_IMMEDIATELY      = "KanDoraImmediately";
    private String AGARI_YAME                = "AgariYame";
    private String ALLOW_BOXING              = "AllowBoxing";
    private String RIICHI_WHEN_LOW_ON_POINTS = "RiichiWhenLowOnPoints";
    private String ATAMA_HANE_RON        = "AtamaHaneRon";
    private String KAN_CAN_CHANGE_WAIT   = "KanCanChangeWait";
    private String DOUBLE_YAKUMAN        = "DoubleYakuman";

    private String OPEN_TANYAO    = "OpenTanyao";
    private String OPEN_PINFU     = "OpenPinfu";
    private String OPEN_RIICHI    = "OpenRiichi";
    private String NAGASHI_MANGAN = "NagashiMangan";
    private String SANRENKOU      = "Sanrenkou";
    private String SUURENKOU      = "Suurenkou";
    private String DAISHARIN      = "Daisharin";
    private String SHIISANPUUTA   = "Shiisanpuuta";
    private String SHIISUUPUUTA   = "Shiisuupuuta";
    private String PARENCHAN      = "Parenchan";

    private String ABORTIVE_4_WINDS      = "Abortive4Winds";
    private String ABORTIVE_4_RIICHI     = "Abortive4Riichi";
    private String ABORTIVE_KYUSHUKYUHAI = "AbortiveKyushukyuhai";
    private String ABORTIVE_4_KANS       = "Abortive4Kans";

    private String RED_DORA_COUNT                 = "RedDoraCount";
    private String THREE_PLAYER_STARTING_POINTS   = "ThreePlayerStartingPoints";
    private String THREE_PLAYER_PLACEMENT_BONUS_1 = "ThreePlayerPlacementBonus1";
    private String THREE_PLAYER_CHOMBO_SIZE       = "ThreePlayerChomboSize";
    private String FOUR_PLAYER_STARTING_POINTS    = "FourPlayerStartingPoints";
    private String FOUR_PLAYER_PLACEMENT_BONUS_1  = "FourPlayerPlacementBonus1";
    private String FOUR_PLAYER_PLACEMENT_BONUS_2  = "FourPlayerPlacementBonus2";
    private String FOUR_PLAYER_CHOMBO_SIZE        = "FourPlayerChomboSize";
    private String FIVE_PLAYER_STARTING_POINTS    = "FivePlayerStartingPoints";
    private String FIVE_PLAYER_PLACEMENT_BONUS_1  = "FivePlayerPlacementBonus1";
    private String FIVE_PLAYER_PLACEMENT_BONUS_2  = "FivePlayerPlacementBonus2";
    private String FIVE_PLAYER_CHOMBO_SIZE        = "FivePlayerChomboSize";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.fragment_options_ruleset, container, false);

        registerCheckBoxes(myInflatedView);
        registerRadioGroups(myInflatedView);
        loadSavedSettings();

        return myInflatedView;
    }
    private void loadSavedSettings(){
        loadSetting(kanDora, KAN_DORA, true);
        loadSetting(limitUraDora, LIMIT_URA_DORA, false);
        loadSetting(goldenDora, GOLDEN_DORA, false);
        loadSetting(westRound, WEST_ROUND, true);
        loadSetting(kanDoraImmediately, KAN_DORA_IMMEDIATELY, false);
        loadSetting(agariYame, AGARI_YAME, true);
        loadSetting(allowBoxing, ALLOW_BOXING, true);
        loadSetting(riichiWhenLowOnPoints, RIICHI_WHEN_LOW_ON_POINTS, false);
        loadSetting(atamaHaneRon, ATAMA_HANE_RON, false);
        loadSetting(kanCanChangeWait, KAN_CAN_CHANGE_WAIT, false);
        loadSetting(doubleYakuman, DOUBLE_YAKUMAN, true);

        loadSetting(openTanyao, OPEN_TANYAO, true);
        loadSetting(openPinfu, OPEN_PINFU, false);
        loadSetting(openRiichi, OPEN_RIICHI, false);
        loadSetting(nagashiMangan, NAGASHI_MANGAN, true);
        loadSetting(sanrenkou, SANRENKOU, true);
        loadSetting(suurenkou, SUURENKOU, false);
        loadSetting(daisharin, DAISHARIN, false);
        loadSetting(shiisanpuuta, SHIISANPUUTA, false);
        loadSetting(shiisuupuuta, SHIISUUPUUTA, false);
        loadSetting(parenchan, PARENCHAN, true);

        loadSetting(abortive4Winds, ABORTIVE_4_WINDS, true);
        loadSetting(abortive4Riichi, ABORTIVE_4_RIICHI, true);
        loadSetting(abortiveKyushukyuhai, ABORTIVE_KYUSHUKYUHAI, true);
        loadSetting(abortive4Kans, ABORTIVE_4_KANS, true);

        loadSetting(redDoraCount, RED_DORA_COUNT, 3);
        loadSetting(threePlayerStartingPoints, THREE_PLAYER_STARTING_POINTS, 35000);
        loadSetting(threePlayerPlacementBonus1, THREE_PLAYER_PLACEMENT_BONUS_1, 15);
        loadSetting(threePlayerChomboSize, THREE_PLAYER_CHOMBO_SIZE, 6000);
        loadSetting(fourPlayerStartingPoints, FOUR_PLAYER_STARTING_POINTS, 25000);
        loadSetting(fourPlayerPlacementBonus1, FOUR_PLAYER_PLACEMENT_BONUS_1, 15);
        loadSetting(fourPlayerPlacementBonus2, FOUR_PLAYER_PLACEMENT_BONUS_2, 5);
        loadSetting(fourPlayerChomboSize, FOUR_PLAYER_CHOMBO_SIZE, 3000);
        loadSetting(fivePlayerStartingPoints, FIVE_PLAYER_STARTING_POINTS, 25000);
        loadSetting(fivePlayerPlacementBonus1, FIVE_PLAYER_PLACEMENT_BONUS_1, 20);
        loadSetting(fivePlayerPlacementBonus2, FIVE_PLAYER_PLACEMENT_BONUS_2, 10);
        loadSetting(fivePlayerChomboSize, FIVE_PLAYER_CHOMBO_SIZE, 2000);
    }
    private void loadSetting(CheckBox cBox, String settingName, boolean def ){
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        boolean val = sharedPref.getBoolean(settingName, def);
        cBox.setChecked(val);
    }
    private void loadSetting(RadioGroup rGroup, String settingName, Integer def ){
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        Integer val = sharedPref.getInt(settingName, def);
        for( int i=0; i<rGroup.getChildCount(); i++ ){
            RadioButton child = (RadioButton)rGroup.getChildAt(i);
            boolean isCorrect = child.getText().equals(val.toString());
            child.setChecked(isCorrect);
        }
    }

    ///////////////////////////////////////////////////////
    //////////////             Main          //////////////
    ///////////////////////////////////////////////////////
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.kanDoraCheckBox:
                saveSettingBoolean(kanDora.isChecked(), KAN_DORA);
                break;
            case R.id.limitUraDora:
                saveSettingBoolean(limitUraDora.isChecked(), LIMIT_URA_DORA);
                break;
            case R.id.goldenDora:
                saveSettingBoolean(goldenDora.isChecked(), GOLDEN_DORA);
                break;
            case R.id.westRound:
                saveSettingBoolean(westRound.isChecked(), WEST_ROUND);
                break;
            case R.id.kanDoraImmediately:
                saveSettingBoolean(kanDoraImmediately.isChecked(), KAN_DORA_IMMEDIATELY);
                break;
            case R.id.agariYame:
                saveSettingBoolean(agariYame.isChecked(), AGARI_YAME);
                break;
            case R.id.allowBoxing:
                saveSettingBoolean(allowBoxing.isChecked(), ALLOW_BOXING);
                break;
            case R.id.riichiWhenLowOnPoints:
                saveSettingBoolean(riichiWhenLowOnPoints.isChecked(), RIICHI_WHEN_LOW_ON_POINTS);
                break;
            case R.id.atamaHaneRon:
                saveSettingBoolean(atamaHaneRon.isChecked(), ATAMA_HANE_RON);
                break;
            case R.id.kanCanChangeWait:
                saveSettingBoolean(kanCanChangeWait.isChecked(), KAN_CAN_CHANGE_WAIT);
                break;
            case R.id.doubleYakuman:
                saveSettingBoolean(doubleYakuman.isChecked(), DOUBLE_YAKUMAN);
                break;
            case R.id.openTanyao:
                saveSettingBoolean(openTanyao.isChecked(), OPEN_TANYAO);
                break;
            case R.id.openPinfu:
                saveSettingBoolean(openPinfu.isChecked(), OPEN_PINFU);
                break;
            case R.id.openRiichi:
                saveSettingBoolean(openRiichi.isChecked(), OPEN_RIICHI);
                break;
            case R.id.nagashiMangan:
                saveSettingBoolean(nagashiMangan.isChecked(), NAGASHI_MANGAN);
                break;
            case R.id.sanrenkou:
                saveSettingBoolean(sanrenkou.isChecked(), SANRENKOU);
                break;
            case R.id.suurenkou:
                saveSettingBoolean(suurenkou.isChecked(), SUURENKOU);
                break;
            case R.id.daisharin:
                saveSettingBoolean(daisharin.isChecked(), DAISHARIN);
                break;
            case R.id.shiisanpuuta:
                saveSettingBoolean(shiisanpuuta.isChecked(), SHIISANPUUTA);
                break;
            case R.id.shiisuupuuta:
                saveSettingBoolean(shiisuupuuta.isChecked(), SHIISUUPUUTA);
                break;
            case R.id.parenchan:
                saveSettingBoolean(parenchan.isChecked(), PARENCHAN);
                break;
            case R.id.abortive4Winds:
                saveSettingBoolean(abortive4Winds.isChecked(), ABORTIVE_4_WINDS);
                break;
            case R.id.abortive4Riichi:
                saveSettingBoolean(abortive4Riichi.isChecked(), ABORTIVE_4_RIICHI);
                break;
            case R.id.abortiveKyushukyuhai:
                saveSettingBoolean(abortiveKyushukyuhai.isChecked(), ABORTIVE_KYUSHUKYUHAI);
                break;
            case R.id.abortive4Kans:
                saveSettingBoolean(abortive4Kans.isChecked(), ABORTIVE_4_KANS);
                break;
        }
    }
    private void saveSettingBoolean(Boolean bool, String label){
        SharedPreferences settings = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(label, bool);
        editor.apply();
    }

    ///////////////////////////////////////////////////////
    //////////////             Init          //////////////
    ///////////////////////////////////////////////////////
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
        saveSettingButton(redDoraCount, RED_DORA_COUNT);
    }
    private void setThreePlayerStartingPoints(){
        saveSettingButton(threePlayerStartingPoints, THREE_PLAYER_STARTING_POINTS);
    }
    private void setThreePlayerPlacementBonus1(){
        saveSettingButton(threePlayerPlacementBonus1, THREE_PLAYER_PLACEMENT_BONUS_1);
    }
    private void setThreePlayerChomboSize(){
        saveSettingButton(threePlayerChomboSize, THREE_PLAYER_CHOMBO_SIZE);
    }
    private void setFourPlayerStartingPoints(){
        saveSettingButton(fourPlayerStartingPoints, FOUR_PLAYER_STARTING_POINTS);
    }
    private void setFourPlayerPlacementBonus1(){
        saveSettingButton(fourPlayerPlacementBonus2, FOUR_PLAYER_PLACEMENT_BONUS_1);
    }
    private void setFourPlayerPlacementBonus2(){
        saveSettingButton(fourPlayerPlacementBonus2, FOUR_PLAYER_PLACEMENT_BONUS_2);
    }
    private void setFourPlayerChomboSize(){
        saveSettingButton(fourPlayerChomboSize, FOUR_PLAYER_CHOMBO_SIZE);
    }
    private void setFivePlayerStartingPoints(){
        saveSettingButton(fivePlayerStartingPoints, FIVE_PLAYER_STARTING_POINTS);
    }
    private void setFivePlayerPlacementBonus1(){
        saveSettingButton(fivePlayerPlacementBonus1, FIVE_PLAYER_PLACEMENT_BONUS_1);
    }
    private void setFivePlayerPlacementBonus2(){
        saveSettingButton(fivePlayerPlacementBonus2, FIVE_PLAYER_PLACEMENT_BONUS_2);
    }
    private void setFivePlayerChomboSize(){
        saveSettingButton(fivePlayerChomboSize, FIVE_PLAYER_CHOMBO_SIZE);
    }
    private void saveSettingButton(RadioGroup rGroup, String label){
        RadioButton rButt = (RadioButton)rGroup.findViewById(rGroup.getCheckedRadioButtonId());
        Integer intVal = Integer.valueOf(rButt.getText().toString());

        SharedPreferences settings = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(label, intVal);
        editor.apply();
    }
}
