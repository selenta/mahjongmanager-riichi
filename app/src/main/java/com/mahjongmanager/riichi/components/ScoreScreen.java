package com.mahjongmanager.riichi.components;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.mahjongmanager.riichi.common.Fu;
import com.mahjongmanager.riichi.common.Hand;
import com.mahjongmanager.riichi.common.Meld;
import com.mahjongmanager.riichi.R;
import com.mahjongmanager.riichi.ScoreCalculator;
import com.mahjongmanager.riichi.ScoreCalculator.Wait;
import com.mahjongmanager.riichi.common.Tile;
import com.mahjongmanager.riichi.common.TileSet;
import com.mahjongmanager.riichi.common.Yaku;
import com.mahjongmanager.riichi.utils.ExampleHands;
import com.mahjongmanager.riichi.utils.FuHelper;
import com.mahjongmanager.riichi.utils.Log;
import com.mahjongmanager.riichi.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ScoreScreen extends LinearLayout implements View.OnClickListener {
    private Hand hand;

    private TableLayout hanTable;
    private TableLayout fuTable;
    private TableLayout waitTable;
    private TextView hanTotalLabel;
    private TextView fuTotalLabel;

    private TextView scoreBreakdown;
    private TextView scoreValue;
    private TextView shantenLabel;

    private LinearLayout scoreContainer;
    private LinearLayout tenpaiContainer;
    private LinearLayout incompleteContainer;
    private LinearLayout invalidContainer;

    private List<ScoreDetail> scoreDetailList = new ArrayList<>();

    private static final int MODE_SCORING = 0;
    private static final int MODE_TENPAI  = 1;
    private static final int MODE_SHANTEN = 2;
    private static final int MODE_INVALID = 3;

    public ScoreScreen(Context ctx){
        this(ctx, null);
    }
    public ScoreScreen(Context ctx, AttributeSet attrs) {
        this(ctx, attrs, 0);
    }
    public ScoreScreen(Context ctx, AttributeSet attrs, int defStyle ){
        super(ctx, attrs, defStyle);
        init();
    }
    private void init(){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.component_scorescreen, this);

        hanTable      = (TableLayout) findViewById(R.id.hanTable);
        fuTable       = (TableLayout) findViewById(R.id.fuTable);
        waitTable     = (TableLayout) findViewById(R.id.waitTable);
        hanTotalLabel = (TextView) findViewById(R.id.hanTotalLabel);
        fuTotalLabel  = (TextView) findViewById(R.id.fuTotalLabel);

        scoreValue = (TextView) findViewById(R.id.scoreValue);
        scoreBreakdown = (TextView) findViewById(R.id.scoreBreakdown);
        shantenLabel = (TextView) findViewById(R.id.shantenLabel);

        scoreContainer = (LinearLayout) findViewById(R.id.scoreContainer);
        tenpaiContainer = (LinearLayout) findViewById(R.id.tenpaiContainer);
        incompleteContainer = (LinearLayout) findViewById(R.id.incompleteContainer);
        invalidContainer = (LinearLayout) findViewById(R.id.invalidContainer);
    }

    public void setHand(Hand h){
        hand = h;
        determineMode();
    }
    public void hideScoreTotal(){
        scoreValue.setVisibility(GONE);
    }

    private void determineMode(){
        ScoreCalculator sc = new ScoreCalculator(hand, true);

        if( sc.validatedHand==null || hand.getWinningTile()==null ){
            int shanten = sc.shanten;
            if( shanten > 0 ){
                setMode(MODE_SHANTEN);
                String label = shanten+" tiles away";
                shantenLabel.setText( label );
            } else if(shanten==0) {
                setMode(MODE_TENPAI);
                displayWaitValues( sc.waits );
            } else {
                setMode(MODE_INVALID);
            }
        } else if( sc.validatedHand==null ){
            setMode(MODE_INVALID);
        } else {
            hand = sc.validatedHand;
            setMode(MODE_SCORING);
            displayHanFu();
        }
    }
    private void setMode(int m){
        switch (m){
            case MODE_SCORING:
                scoreContainer.setVisibility(View.VISIBLE);
                tenpaiContainer.setVisibility(View.GONE);
                incompleteContainer.setVisibility(View.GONE);
                invalidContainer.setVisibility(View.GONE);
                break;
            case MODE_TENPAI:
                scoreContainer.setVisibility(View.GONE);
                tenpaiContainer.setVisibility(View.VISIBLE);
                incompleteContainer.setVisibility(View.GONE);
                invalidContainer.setVisibility(View.GONE);
                break;
            case MODE_SHANTEN:
                scoreContainer.setVisibility(View.GONE);
                tenpaiContainer.setVisibility(View.GONE);
                incompleteContainer.setVisibility(View.VISIBLE);
                invalidContainer.setVisibility(View.GONE);
                break;
            case MODE_INVALID:
                scoreContainer.setVisibility(View.GONE);
                tenpaiContainer.setVisibility(View.GONE);
                incompleteContainer.setVisibility(View.GONE);
                invalidContainer.setVisibility(View.VISIBLE);
                break;
        }
    }

    ////////////////////////////////////////////////////////////////////////
    ////////////////////////        On Click        ////////////////////////
    ////////////////////////////////////////////////////////////////////////
    @Override
    public void onClick(View v){
        for(ScoreDetail sd : scoreDetailList){
            if( v==sd.tableRow ){
                Log.d("ClickDetect", "ScoreDetail: "+sd.toString()+" - "+sd.meld);
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
        YakuDescription yd = new YakuDescription(getContext());
        yd.setYaku(scoreDetail.yaku);
        yd.showLabels();
        yd.hidePrimaryName();

        ScrollView sv = new ScrollView(getContext());
        sv.addView(yd);

        AlertDialog.Builder d = new AlertDialog.Builder(getContext());
        d.setView(sv);
        d.setTitle(scoreDetail.yaku.getLocalizedString());
        d.show();
    }
    private void createFuPopup(ScoreDetail scoreDetail){
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
            TextView tv = new TextView(getContext());
            tv.setText(generalDesc);
            tv.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            tv.setPadding(0,0,0,30);
            container.addView(tv);

            GridLayout fuTable = buildFuTable(scoreDetail);
            container.addView(fuTable);
        } else {
            TextView description = new TextView(getContext());
            container.addView(description);
            String dStr = scoreDetail.fu.description;
            description.setText(dStr);
        }

        AlertDialog.Builder d = new AlertDialog.Builder(getContext());
        d.setView(container);
        d.setTitle(scoreDetail.toString());
        d.show();
    }
    private GridLayout buildFuTable(ScoreDetail scoreDetail){
        GridLayout grid = new GridLayout(getContext());
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

        View line = new View(getContext());
        line.setMinimumHeight(2);
        GridLayout.LayoutParams lineParams = new GridLayout.LayoutParams();
        lineParams.height = 4;
        lineParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED,2);
        line.setLayoutParams(lineParams);
        line.setBackgroundColor(0xFFAAAAAA);
        grid.addView(line);

        TextView totalLabel = new TextView(getContext());
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
        TextView tv = new TextView(getContext());
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
    private void displayHanFu(){
        if(hand==null){
            return;
        }
        Log.i("displayScore", "hanList: " + hand.hanList.toString());
        Log.i("displayScore", "fuList: " + hand.fuList.toString());

        updateLabels();
        addHanFuRows();
    }
    private void updateLabels(){
        int han = hand.countHan();
        int fu  = hand.countFu();

        int roundedFu = (fu==25) ? 25 : (int) Math.ceil(fu/10.0)*10;
        hanTotalLabel.setText( String.valueOf(han) );
        fuTotalLabel.setText( ""+fu+" ("+roundedFu+")");

        String result = ScoreCalculator.scoreHanFu(han, fu, hand.playerWind==Tile.Wind.EAST, hand.selfDrawWinningTile);
        scoreValue.setText(result);

        //Do this last, so that we can make it a little more pretty
        String sBreakdown = getScoreText(roundedFu);
        scoreBreakdown.setText(sBreakdown);
    }
    private String getScoreText(int roundedFu){
        int han = hand.countHan();
        int fu  = hand.countFu();

        String sBreakdown = ""+han+" Han "+roundedFu+" Fu";
        if( han==5 || (han==4&&fu>=40) || (han==3&&fu>=70) ){
            sBreakdown += " (Mangan)";
        } else if( han<5 ){
            return sBreakdown;
        } else if( han==6 || han==7 ){
            sBreakdown += " (Haneman)";
        } else if( han==8 || han==9 || han==10 ){
            sBreakdown += " (Baiman)";
        } else if( han==11|| han==12 ){
            sBreakdown += " (Sanbaiman)";
        } else if( han<14 ){
            sBreakdown += " (Yakuman)";
        } else if( han<27 ){
            sBreakdown += " (2x Yakuman)";
        } else if( han<40 ){
            sBreakdown += " (3x Yakuman)";
        } else if( han<53 ){
            sBreakdown += " (4x Yakuman)";
        } else if( han<66 ){
            sBreakdown += " (5x Yakuman)";
        } else {
            sBreakdown += " (ERROR)";
        }
        return sBreakdown;
    }

    /////////////////////////////////////////////////////////////////////////////
    ////////////////////////    Populate Score Tables   /////////////////////////
    /////////////////////////////////////////////////////////////////////////////
    private void addHanFuRows(){
        for( Yaku.Name yName : hand.hanList.keySet() ){
            addHanRow(yName);
        }
        for( Fu.Name fName : hand.fuList.keySet() ){
            addFuRow(fName);
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
    private void addFuRow(Fu.Name fName){
        Fu fu = getFu(fName);
        String val = hand.fuList.get(fName).toString();

        ScoreDetail sd = new ScoreDetail( fu, val );
        sd.tableRow.setOnClickListener(this);
        scoreDetailList.add(sd);

        fuTable.addView(sd.tableRow, fuTable.getChildCount()-1);
    }

    private class ScoreDetail {
        Fu fu;
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
        public ScoreDetail(Fu f, String v){
            if( f.name==Fu.Name.MELD_1 ){
                meld = hand.meld1;
            } else if( f.name==Fu.Name.MELD_2 ){
                meld = hand.meld2;
            } else if( f.name==Fu.Name.MELD_3 ){
                meld = hand.meld3;
            } else if( f.name==Fu.Name.MELD_4 ){
                meld = hand.meld4;
            }
            fu = f;
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
            if(fu!=null) {
                if( fu.name==Fu.Name.MELD_1 || fu.name==Fu.Name.MELD_2 || fu.name==Fu.Name.MELD_3 || fu.name== Fu.Name.MELD_4){
                    s = FuHelper.getFuName(meld);
                } else {
                    s = fu.getLocalizedString();
                }
            } else {
                s = yaku.getLocalizedString();
            }
            return Utils.prettifyName(s);
        }
    }

    private Yaku getYaku(Yaku.Name name){
        for( Yaku y : ExampleHands.allYaku ){
            if( y.name.equals(name) ){
                return y;
            }
        }
        return null;
    }
    private Fu getFu(Fu.Name name){
        for( Fu f : FuHelper.allFu ){
            if( f.name.equals(name) ){
                return f;
            }
        }
        return null;
    }

    /////////////////////////////////////////////////////////////////////////////
    ////////////////////////    Populate Wait Tables    /////////////////////////
    /////////////////////////////////////////////////////////////////////////////
    private void displayWaitValues(List<Wait> waits){
        for(Wait wait : waits){
            addWaitRow(wait);
            createSeparatorLine();
        }
    }
    private void addWaitRow(Wait wait){
        TableRow newRow = new TableRow( getContext() );
        newRow.setGravity(Gravity.CENTER);
        newRow.setPadding(0,15,0,15);

        String winTypeLabel = (wait.isTsumo) ? "Tsumo" : "Ron";
        newRow.addView(tableValue(winTypeLabel));

        newRow.addView(buildTileList(wait.tiles));

        if( wait.han > 0 ){
            newRow.addView(tableValue(String.valueOf(wait.han)));
            newRow.addView(tableValue(String.valueOf(wait.fu)));
        } else {
            newRow.addView(tableError());
        }

        waitTable.addView(newRow);
    }
    private LinearLayout buildTileList(TileSet tiles){
        LinearLayout waitContainer = new LinearLayout(getContext());
        waitContainer.setOrientation(HORIZONTAL);
        waitContainer.setGravity(Gravity.CENTER);

        for(Tile tile : tiles ){
            ImageView image = Utils.getHandDisplayTileView(tile, false);
            waitContainer.addView(image);
        }
        return waitContainer;
    }
    private TextView tableValue(String value){
        TextView label = new TextView(getContext());
        label.setTextSize(16);
        label.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        label.setText(value);
        return label;
    }
    private TextView tableError(){
        TextView label = tableValue("No Yaku");

        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.span = 2;
        label.setLayoutParams(params);

        label.setTextColor(0xffcc0000);
        label.setTypeface(null, Typeface.ITALIC);
        return label;
    }
    private void createSeparatorLine(){
        View line = new View(getContext());

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
        line.setLayoutParams(params);

        line.setBackgroundColor(0xFFAAAAAA);
        waitTable.addView(line);
    }
}
