package com.mahjongmanager.riichi.components;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.mahjongmanager.riichi.Hand;
import com.mahjongmanager.riichi.MainActivity;
import com.mahjongmanager.riichi.Meld;
import com.mahjongmanager.riichi.R;
import com.mahjongmanager.riichi.ScoreCalculator;
import com.mahjongmanager.riichi.Tile;
import com.mahjongmanager.riichi.Yaku;
import com.mahjongmanager.riichi.utils.FuHelper;
import com.mahjongmanager.riichi.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ScoreScreen extends LinearLayout implements View.OnClickListener {
    Context context;

    private Hand hand;

    private TableLayout hanTable;
    private TableLayout fuTable;
    private TextView hanTotalLabel;
    private TextView fuTotalLabel;

    private TextView scoreBreakdown;
    private TextView scoreValue;

    private List<ScoreDetail> scoreDetailList = new ArrayList<>();

    public ScoreScreen(Context ctx){
        this(ctx, null);
    }
    public ScoreScreen(Context ctx, AttributeSet attrs) {
        this(ctx, attrs, 0);
    }
    public ScoreScreen(Context ctx, AttributeSet attrs, int defStyle ){
        super(ctx, attrs, defStyle);
        context = ctx;
        init();
    }
    private void init(){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.component_scorescreen, this);

        hanTable      = (TableLayout) findViewById(R.id.hanTable);
        fuTable       = (TableLayout) findViewById(R.id.fuTable);
        hanTotalLabel = (TextView) findViewById(R.id.hanTotalLabel);
        fuTotalLabel  = (TextView) findViewById(R.id.fuTotalLabel);

        scoreValue = (TextView) findViewById(R.id.scoreValue);
        scoreBreakdown = (TextView) findViewById(R.id.scoreBreakdown);

    }

    public void setHand(Hand h){
        hand = h;
        displayScores();
    }
    public void hideScoreTotal(){
        scoreValue.setVisibility(GONE);
    }

    ////////////////////////////////////////////////////////////////////////
    ////////////////////////        On Click        ////////////////////////
    ////////////////////////////////////////////////////////////////////////
    @Override
    public void onClick(View v){
        for(ScoreDetail sd : scoreDetailList){
            if( v==sd.tableRow ){
                Log.i("ClickDetect", "ScoreDetail: "+sd.toString()+" - "+sd.meld);
                createPopup(sd);
            }
        }
    }
    private void createPopup(ScoreDetail scoreDetail){
        if( scoreDetail.isHan() ){
            createHanPopup(scoreDetail);
        } else {
            createFuPopup(scoreDetail);
        }
    }
    private void createHanPopup(ScoreDetail scoreDetail){
        Dialog dialog = new Dialog(getContext());
        ScrollView sv = new ScrollView(getContext());

        YakuDescription yd = new YakuDescription(getContext());
        yd.setYaku(scoreDetail.yaku);
        yd.showLabels();
        yd.hideEnglishName();

        sv.addView(yd);
        dialog.setContentView(sv);
        dialog.setTitle(scoreDetail.yaku.english);

        dialog.show();
    }
    private void createFuPopup(ScoreDetail scoreDetail){
        Dialog dialog = new Dialog(getContext());
        LinearLayout container = new LinearLayout(getContext());
        container.setOrientation(VERTICAL);
        container.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        int fuPad = 50;
        container.setPadding(fuPad,fuPad,fuPad,fuPad);
        container.setGravity(Gravity.CENTER_HORIZONTAL);

        if( scoreDetail.meld!=null ){
            Hand hand = new Hand(new ArrayList<Tile>());
            hand.setMeld(scoreDetail.meld.getTiles());
            hand.tiles.addAll(scoreDetail.meld.getTiles());
            HandDisplay handDisplay = new HandDisplay(getContext());
            handDisplay.setState(HandDisplay.FU_DISPLAY);
            handDisplay.setHand(hand);

            container.addView(handDisplay);
        }

        if( scoreDetail.meld!=null ){
            String generalDesc = "Melds that are not sequences have a base Fu value of 2, and can increase depending on whether the meld is open/closed, simples/honors, or is a triplet/quad.";
            TextView tv = new TextView(context);
            tv.setText(generalDesc);
            tv.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            tv.setPadding(0,0,0,30);
            container.addView(tv);

            GridLayout fuTable = buildFuTable(scoreDetail);
            container.addView(fuTable);
        } else {
            TextView description = new TextView(getContext());
            container.addView(description);
            String dStr = FuHelper.getDescription(scoreDetail.toString());
            description.setText(dStr);
        }

        dialog.setContentView(container);
        dialog.setTitle(scoreDetail.toString());

        dialog.show();
    }
    private GridLayout buildFuTable(ScoreDetail scoreDetail){
        GridLayout grid = new GridLayout(context);
        grid.setColumnCount(2);

        TextView baseValLabel = newTextLabel("Base Value", false);
        baseValLabel.setTypeface(null, Typeface.ITALIC);
        grid.addView(baseValLabel);
        TextView baseValValue = newTextValue("2", false);
        baseValValue.setTypeface(null, Typeface.ITALIC);
        grid.addView(baseValValue);

        boolean isOpen = !scoreDetail.meld.isClosed();
        grid.addView(newTextLabel("Closed", isOpen));
        grid.addView(newTextValue("x2", isOpen));

        boolean isSimples = !Utils.containsHonorsOrTerminals(scoreDetail.meld.getTiles());
        grid.addView(newTextLabel("Honors or Terminals", isSimples));
        grid.addView(newTextValue("x2", isSimples));

        boolean isTriplet = !scoreDetail.meld.isKan();
        grid.addView(newTextLabel("Set of 4 Tiles", isTriplet));
        grid.addView(newTextValue("x4", isTriplet));

        View line = new View(context);
        line.setMinimumHeight(2);
        GridLayout.LayoutParams lineParams = new GridLayout.LayoutParams();
        lineParams.height = 4;
        lineParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED,2);
        line.setLayoutParams(lineParams);
        line.setBackgroundColor(0xFFAAAAAA);
        grid.addView(line);

        TextView totalLabel = new TextView(context);
        totalLabel.setText("Total Fu");
        totalLabel.setPadding(300,0,0,0);
        grid.addView(totalLabel);
        grid.addView(newTextValue(scoreDetail.value, false));

        return grid;
    }
    private TextView newTextLabel(String s, boolean strikethrough){
        TextView tv = newTextView(s, strikethrough);
        tv.setPadding(125,0,0,0);
        return tv;
    }
    private TextView newTextValue(String s, boolean strikethrough){
        TextView tv = newTextView(s, strikethrough);
        tv.setWidth(150);
        tv.setGravity(Gravity.RIGHT);
        return tv;
    }
    private TextView newTextView(String s, boolean strikethrough){
        TextView tv = new TextView(context);
        tv.setText(s);
        if( strikethrough ){
            tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tv.setTextColor(0x77000000);
        }
        return tv;
    }

    /////////////////////////////////////////////////////////////////////////
    ////////////////////////        Update UI        ////////////////////////
    /////////////////////////////////////////////////////////////////////////
    private void displayScores(){
        if(hand==null){
            return;
        }
        Log.i("displayScore", "hanList: " + hand.hanList.toString());
        Log.i("displayScore", "fuList: " + hand.fuList.toString());

        updateLabels();
        addRows();
    }

    private void updateLabels(){
        Integer roundedFu = (hand.fu==25) ? 25 : (int) Math.ceil(hand.fu/10.0)*10;
        hanTotalLabel.setText(hand.han.toString());
        fuTotalLabel.setText(hand.fu.toString()+" ("+roundedFu.toString()+")");

        String result = ScoreCalculator.scoreHanFu(hand.han, hand.fu, hand.playerWind== Tile.Wind.EAST, hand.tsumo);
        scoreValue.setText(result);

        //Do this last, so that we can make it a little more pretty
        String sBreakdown = getScoreText(roundedFu);
        scoreBreakdown.setText(sBreakdown);
    }
    private String getScoreText(int roundedFu){
        String sBreakdown = hand.han.toString() + " Han " + roundedFu + " Fu";
        if( hand.han==5 || (hand.han==4&&hand.fu>=40) || (hand.han==3&&hand.fu>=70) ){
            sBreakdown = sBreakdown + " (Mangan)";
        } else if( hand.han<5 ){
            return sBreakdown;
        } else if( hand.han==6 || hand.han==7 ){
            sBreakdown = sBreakdown + " (Haneman)";
        } else if( hand.han==8 || hand.han==9 || hand.han==10 ){
            sBreakdown = sBreakdown + " (Baiman)";
        } else if( hand.han==11|| hand.han==12 ){
            sBreakdown = sBreakdown + " (Sanbaiman)";
        } else if( hand.han<14 ){
            sBreakdown = sBreakdown + " (Yakuman)";
        } else if( hand.han<27 ){
            sBreakdown = sBreakdown + " (2x Yakuman)";
        } else if( hand.han<40 ){
            sBreakdown = sBreakdown + " (3x Yakuman)";
        } else if( hand.han<53 ){
            sBreakdown = sBreakdown + " (4x Yakuman)";
        } else if( hand.han<66 ){
            sBreakdown = sBreakdown + " (5x Yakuman)";
        } else {
            sBreakdown = sBreakdown + " (ERROR)";
        }
        return sBreakdown;
    }

    /////////////////////////////////////////////////////////////////////////////
    ///////////////////////////    Populate Tables    ///////////////////////////
    /////////////////////////////////////////////////////////////////////////////
    private void addRows(){
        for( Yaku.Name yName : hand.hanList.keySet() ){
            addHanRow(yName);
        }
        for( String label : hand.fuList.keySet() ){
            addFuRow(label);
        }
    }
    private void addHanRow(Yaku.Name yakuName){
        Yaku yaku = getYaku(yakuName);
        if( yaku==null ){
            Log.e("addHanRow", "Yaku was null: "+yakuName.toString());
            return;
        }
        String val = hand.hanList.get(yakuName).toString();

        ScoreDetail sd = new ScoreDetail( yaku, hand.isOpen(), val );
        sd.tableRow.setOnClickListener(this);
        scoreDetailList.add(sd);

        hanTable.addView(sd.tableRow, hanTable.getChildCount()-1);
    }
    private void addFuRow(String label){
        String val = hand.fuList.get(label).toString();
        ScoreDetail sd = new ScoreDetail( label, val );
        sd.tableRow.setOnClickListener(this);
        scoreDetailList.add(sd);

        fuTable.addView(sd.tableRow, fuTable.getChildCount()-1);
    }

    private class ScoreDetail {
        String fuName;
        Meld meld;
        Yaku yaku;
        boolean isOpen;
        String value;

        TableRow tableRow;

        public ScoreDetail(Yaku y, boolean o, String v){
            yaku = y;
            isOpen = o;
            value = v;
            createTableRow();
        }
        public ScoreDetail(String name, String v){
            if( name.contains("1") ){
                meld = hand.meld1;
            } else if( name.contains("2") ){
                meld = hand.meld2;
            } else if( name.contains("3") ){
                meld = hand.meld3;
            } else if( name.contains("4") ){
                meld = hand.meld4;
            }
            if( meld!=null ) {
                fuName = FuHelper.getFuName(meld);
            } else {
                fuName = name;
            }
            value = v;
            createTableRow();
        }

        private void createTableRow(){
            TableRow newRow = new TableRow( getContext() );
            newRow.addView( getLabelView() );
            newRow.addView( getValueView() );

            tableRow = newRow;
        }
        private TextView getLabelView(){
            TextView labelView = new TextView( getContext() );

            ViewGroup.LayoutParams Params1 = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
            labelView.setLayoutParams(Params1);
            labelView.setPadding(20,0,0,0);

            labelView.setTextSize(16);
            labelView.setText(toString());

            return labelView;
        }
        private TextView getValueView(){
            TextView valueView = new TextView(getContext());

            ViewGroup.LayoutParams Params2 = new TableRow.LayoutParams(80, ViewGroup.LayoutParams.WRAP_CONTENT);
            valueView.setLayoutParams(Params2);

            valueView.setTextSize(16);
            valueView.setText(value);

            return valueView;
        }

        public boolean isHan(){
            return yaku != null;
        }
        public String toString(){
            String s;
            if(fuName!=null) {
                s = fuName;
            } else {
                s = yaku.getLocalizedString((Activity) context);
            }
            return Utils.prettifyName(s);
        }
    }

    private List<Yaku> _allYaku;
    private Yaku getYaku(Yaku.Name name){
        if( _allYaku==null ){
            _allYaku = ((MainActivity)context).getExampleHands().allYaku;
        }
        for( Yaku y : _allYaku ){
            if( y.name.equals(name) ){
                return y;
            }
        }
        return null;
    }
}
