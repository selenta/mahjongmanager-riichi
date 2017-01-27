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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.fragment_options_ruleset, container, false);

        registerCheckBoxes(myInflatedView);
        registerRadioGroups(myInflatedView);
        loadSavedSettings();

        return myInflatedView;
    }
    private void loadSavedSettings(){
        kanDora.setChecked(AppSettings.getRulesKanDora());
        limitUraDora.setChecked(AppSettings.getRulesLimitUraDora());
        goldenDora.setChecked(AppSettings.getRulesGoldenDora());
        westRound.setChecked(AppSettings.getRulesWestRound());
        kanDoraImmediately.setChecked(AppSettings.getRulesKanDoraImmediately());
        agariYame.setChecked(AppSettings.getRulesAgariYame());
        allowBoxing.setChecked(AppSettings.getRulesAllowBoxing());
        riichiWhenLowOnPoints.setChecked(AppSettings.getRulesRiichiWhenLowOnPoints());
        atamaHaneRon.setChecked(AppSettings.getRulesAtamaHaneRon());
        kanCanChangeWait.setChecked(AppSettings.getRulesKanCanChangeWait());
        doubleYakuman.setChecked(AppSettings.getRulesDoubleYakumanAllowed());

        openTanyao.setChecked(AppSettings.getRulesOpenTanyao());
        openPinfu.setChecked(AppSettings.getRulesOpenPinfu());
        openRiichi.setChecked(AppSettings.getRulesOpenRiichi());
        nagashiMangan.setChecked(AppSettings.getRulesNagashiMangan());
        sanrenkou.setChecked(AppSettings.getRulesSanrenkou());
        suurenkou.setChecked(AppSettings.getRulesSuurenkou());
        daisharin.setChecked(AppSettings.getRulesDaisharin());
        shiisanpuuta.setChecked(AppSettings.getRulesShiisanpuuta());
        shiisuupuuta.setChecked(AppSettings.getRulesShiisuupuuta());
        parenchan.setChecked(AppSettings.getRulesParenchan());

        abortive4Winds.setChecked(AppSettings.getRulesAbortiveFourWinds());
        abortive4Riichi.setChecked(AppSettings.getRulesAbortiveFourRiichi());
        abortiveKyushukyuhai.setChecked(AppSettings.getRulesAbortiveKyuushikyuhai());
        abortive4Kans.setChecked(AppSettings.getRulesAbortiveFourKans());

        loadSetting(redDoraCount, AppSettings.getRulesRedDoraCount());
        loadSetting(threePlayerStartingPoints, AppSettings.getRulesThreePlayerStartingPoints());
        loadSetting(threePlayerPlacementBonus1, AppSettings.getRulesThreePlayerPlacementBonus1());
        loadSetting(threePlayerChomboSize, AppSettings.getRulesThreePlayerChomboSize());
        loadSetting(fourPlayerStartingPoints, AppSettings.getRulesFourPlayerStartingPoints());
        loadSetting(fourPlayerPlacementBonus1, AppSettings.getRulesFourPlayerPlacementBonus1());
        loadSetting(fourPlayerPlacementBonus2, AppSettings.getRulesFourPlayerPlacementBonus2());
        loadSetting(fourPlayerChomboSize, AppSettings.getRulesFourPlayerChomboSize());
        loadSetting(fivePlayerStartingPoints, AppSettings.getRulesFivePlayerStartingPoints());
        loadSetting(fivePlayerPlacementBonus1, AppSettings.getRulesFivePlayerPlacementBonus1());
        loadSetting(fivePlayerPlacementBonus2, AppSettings.getRulesFivePlayerPlacementBonus2());
        loadSetting(fivePlayerChomboSize, AppSettings.getRulesFivePlayerChomboSize());
    }
    private void loadSetting(RadioGroup rGroup, Integer value){
        for( int i=0; i<rGroup.getChildCount(); i++ ){
            RadioButton child = (RadioButton)rGroup.getChildAt(i);
            boolean isCorrect = (value == Integer.parseInt(child.getText().toString()));
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
                AppSettings.setRulesKanDora(kanDora.isChecked());
                break;
            case R.id.limitUraDora:
                AppSettings.setRulesLimitUraDora(limitUraDora.isChecked());
                break;
            case R.id.goldenDora:
                AppSettings.setRulesGoldenDora(goldenDora.isChecked());
                break;
            case R.id.westRound:
                AppSettings.setRulesWestRound(westRound.isChecked());
                break;
            case R.id.kanDoraImmediately:
                AppSettings.setRulesKanDoraImmediately(kanDoraImmediately.isChecked());
                break;
            case R.id.agariYame:
                AppSettings.setRulesAgariYame(agariYame.isChecked());
                break;
            case R.id.allowBoxing:
                AppSettings.setRulesAllowBoxing(allowBoxing.isChecked());
                break;
            case R.id.riichiWhenLowOnPoints:
                AppSettings.setRulesRiichiWhenLowOnPoints(riichiWhenLowOnPoints.isChecked());
                break;
            case R.id.atamaHaneRon:
                AppSettings.setRulesAtamaHaneRon(atamaHaneRon.isChecked());
                break;
            case R.id.kanCanChangeWait:
                AppSettings.setRulesKanCanChangeWait(kanCanChangeWait.isChecked());
                break;
            case R.id.doubleYakuman:
                AppSettings.setRulesDoubleYakumanAllowed(doubleYakuman.isChecked());
                break;
            case R.id.openTanyao:
                AppSettings.setRulesOpenTanyao(openTanyao.isChecked());
                break;
            case R.id.openPinfu:
                AppSettings.setRulesOpenPinfu(openPinfu.isChecked());
                break;
            case R.id.openRiichi:
                AppSettings.setRulesOpenRiichi(openRiichi.isChecked());
                break;
            case R.id.nagashiMangan:
                AppSettings.setRulesNagashiMangan(nagashiMangan.isChecked());
                break;
            case R.id.sanrenkou:
                AppSettings.setRulesSanrenkou(sanrenkou.isChecked());
                break;
            case R.id.suurenkou:
                AppSettings.setRulesSuurenkou(suurenkou.isChecked());
                break;
            case R.id.daisharin:
                AppSettings.setRulesDaisharin(daisharin.isChecked());
                break;
            case R.id.shiisanpuuta:
                AppSettings.setRulesShiisanpuuta(shiisanpuuta.isChecked());
                break;
            case R.id.shiisuupuuta:
                AppSettings.setRulesShiisuupuuta(shiisuupuuta.isChecked());
                break;
            case R.id.parenchan:
                AppSettings.setRulesParenchan(parenchan.isChecked());
                break;
            case R.id.abortive4Winds:
                AppSettings.setRulesAbortiveFourWinds(abortive4Winds.isChecked());
                break;
            case R.id.abortive4Riichi:
                AppSettings.setRulesAbortiveFourRiichi(abortive4Riichi.isChecked());
                break;
            case R.id.abortiveKyushukyuhai:
                AppSettings.setRulesAbortiveKyuushukyuhai(abortiveKyushukyuhai.isChecked());
                break;
            case R.id.abortive4Kans:
                AppSettings.setRulesAbortiveFourKans(abortive4Kans.isChecked());
                break;
        }
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
        int value = getButtonValue(redDoraCount);
        AppSettings.setRulesRedDoraCount(value);
    }
    private void setThreePlayerStartingPoints(){
        int value = getButtonValue(threePlayerStartingPoints);
        AppSettings.setRulesThreePlayerStartingPoints(value);
    }
    private void setThreePlayerPlacementBonus1(){
        int value = getButtonValue(threePlayerPlacementBonus1);
        AppSettings.aetRulesThreePlayerPlacementBonus1(value);
    }
    private void setThreePlayerChomboSize(){
        int value = getButtonValue(threePlayerChomboSize);
        AppSettings.setRulesThreePlayerChomboSize(value);
    }
    private void setFourPlayerStartingPoints(){
        int value = getButtonValue(fourPlayerStartingPoints);
        AppSettings.setRulesFourPlayerStartingPoints(value);
    }
    private void setFourPlayerPlacementBonus1(){
        int value = getButtonValue(fourPlayerPlacementBonus2);
        AppSettings.setRulesFourPlayerPlacementBonus1(value);
    }
    private void setFourPlayerPlacementBonus2(){
        int value = getButtonValue(fourPlayerPlacementBonus2);
        AppSettings.setRulesFourPlayerPlacementBonus2(value);
    }
    private void setFourPlayerChomboSize(){
        int value = getButtonValue(fourPlayerChomboSize);
        AppSettings.setRulesFourPlayerChomboSize(value);
    }
    private void setFivePlayerStartingPoints(){
        int value = getButtonValue(fivePlayerStartingPoints);
        AppSettings.setRulesFivePlayerStartingPoints(value);
    }
    private void setFivePlayerPlacementBonus1(){
        int value = getButtonValue(fivePlayerPlacementBonus1);
        AppSettings.setRulesFivePlayerPlacementBonus1(value);
    }
    private void setFivePlayerPlacementBonus2(){
        int value = getButtonValue(fivePlayerPlacementBonus2);
        AppSettings.setRulesFivePlayerPlacementBonus2(value);
    }
    private void setFivePlayerChomboSize(){
        int value = getButtonValue(fivePlayerChomboSize);
        AppSettings.setRulesFivePlayerChomboSize(value);
    }
    private int getButtonValue(RadioGroup rGroup){
        RadioButton rButt = (RadioButton)rGroup.findViewById(rGroup.getCheckedRadioButtonId());
        return Integer.valueOf(rButt.getText().toString());
    }
}
