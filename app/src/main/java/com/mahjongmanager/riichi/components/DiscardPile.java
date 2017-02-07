package com.mahjongmanager.riichi.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mahjongmanager.riichi.MainActivity;
import com.mahjongmanager.riichi.R;
import com.mahjongmanager.riichi.common.Tile;
import com.mahjongmanager.riichi.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class DiscardPile extends LinearLayout {
    private Context context;

    private LinearLayout firstRow;
    private LinearLayout secondRow;
    private LinearLayout thirdRow;

    private List<Tile> tiles = new ArrayList<>();

    public DiscardPile(Context ctx){
        this(ctx, null);
    }
    public DiscardPile(Context ctx, AttributeSet attrs) {
        this(ctx, attrs, 0);
    }
    public DiscardPile(Context ctx, AttributeSet attrs, int defStyle ){
        super(ctx, attrs, defStyle);
        context = ctx;
        initializeView();
    }

    private void initializeView(){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.component_discardpile, this);

        firstRow = (LinearLayout) findViewById(R.id.firstRow);
        secondRow = (LinearLayout) findViewById(R.id.secondRow);
        thirdRow = (LinearLayout) findViewById(R.id.thirdRow);

        // We don't want this to get taller as we add more tiles, and it should not get
        // wider unless we add more than 18 tiles (the normal amount)
        // height = 3x tile height
        // width  = 6x tile width
        int width = 40;
        int height = 60;
        if( !isInEditMode() ){
            width  = getUtils().HAND_DISPLAY_TILE_WIDTH;
            height = (int) ((double)width * Utils.TILE_RATIO);
        }

        firstRow.setMinimumWidth( width*6 );
        firstRow.setMinimumHeight( height );
        secondRow.setMinimumWidth( width*6 );
        secondRow.setMinimumHeight( height );
        thirdRow.setMinimumWidth( width*6 );
        thirdRow.setMinimumHeight( height );
    }

    public void addTiles(List<Tile> ts){
        tiles.addAll(ts);
        drawTiles();
    }
    public void addTile(Tile tile){
        tiles.add(tile);
        drawTile(tile);
    }

    private void drawTiles(){
        firstRow.removeAllViews();
        secondRow.removeAllViews();
        thirdRow.removeAllViews();
        for(Tile tile : tiles){
            drawTile(tile);
        }
    }
    private void drawTile(Tile t){
        if( isInEditMode() ){
            return;
        }

        ImageView image = getUtils().getHandDisplayTileView(t, false);
        if( firstRow.getChildCount() < 6 ){
            firstRow.addView(image);
        } else if( secondRow.getChildCount() < 6 ){
            secondRow.addView(image);
        } else {
            thirdRow.addView(image);
        }
    }

    public void clear(){
        tiles.clear();
        firstRow.removeAllViews();
        secondRow.removeAllViews();
        thirdRow.removeAllViews();
    }

    Utils _utils;
    private Utils getUtils(){
        if( _utils==null && !isInEditMode() ){
            _utils = ((MainActivity) context).getUtils();
        }
        return _utils;
    }
}
