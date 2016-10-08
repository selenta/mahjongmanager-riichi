package com.mahjongmanager.riichi.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.mahjongmanager.riichi.R;
import com.mahjongmanager.riichi.Yaku;

public class YakuDescription extends LinearLayout {
    private Context context;

    private TableRow labelRow;

    private Yaku yaku;

    private TextView hanValueClosed;
    private TextView hanValueOpen;
    private TextView yakuNameEnglish;
    private TextView yakuNameRomaji;
    private TextView yakuNameKanji;
    private TextView description;
    private HandDisplay handDisplay;

    public YakuDescription(Context ctx){
        this(ctx, null);
    }
    public YakuDescription(Context ctx, AttributeSet attrs) {
        this(ctx, attrs, 0);
    }
    public YakuDescription(Context ctx, AttributeSet attrs, int defStyle ){
        super(ctx, attrs, defStyle);
        context = ctx;
        initializeView();
    }

    private void initializeView(){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View myInflatedView = inflater.inflate(R.layout.component_yakudescription, this);

        labelRow        = (TableRow) myInflatedView.findViewById(R.id.labelRow);
        hanValueClosed  = (TextView) myInflatedView.findViewById(R.id.hanValueClosed);
        hanValueOpen    = (TextView) myInflatedView.findViewById(R.id.hanValueOpen);
        yakuNameEnglish = (TextView) myInflatedView.findViewById(R.id.yakuNameEnglish);
        yakuNameRomaji  = (TextView) myInflatedView.findViewById(R.id.yakuNameRomaji);
        yakuNameKanji   = (TextView) myInflatedView.findViewById(R.id.yakuNameKanji);
        description     = (TextView) myInflatedView.findViewById(R.id.description);
        handDisplay     = (HandDisplay) myInflatedView.findViewById(R.id.handDisplay);

        hideLabels();
    }

    public void showLabels(){
        labelRow.setVisibility(VISIBLE);
    }
    public void hideLabels(){
        labelRow.setVisibility(GONE);
    }
    public void hideEnglishName(){
        yakuNameEnglish.setVisibility(GONE);
    }

    public void setYaku(Yaku y){
        yaku = y;

        hanValueClosed.setText(y.hanClosed);
        hanValueOpen.setText(y.hanOpen);
        yakuNameEnglish.setText(y.english);
        yakuNameRomaji.setText(y.romaji);
        yakuNameKanji.setText(y.kanji);
        description.setText(y.description);

        if( y.exampleHand==null ){
            handDisplay.setVisibility(GONE);
        } else {
            handDisplay.setHand(y.exampleHand);
        }
    }

    public Yaku.Name getYakuName(){
        return yaku.name;
    }
}
