package com.mahjongmanager.riichi;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class HandDisplay extends LinearLayout {
    private LinearLayout tileList;

    private Hand hand;
    private Boolean simpleDisplay = true;

    private Double tileRatio = 1.26;

    public HandDisplay(Context context){
        super(context);
        initializeView(context);
    }
    public HandDisplay(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeView(context);
    }
    public HandDisplay(Context context, AttributeSet attrs, int defStyle ){
        super(context, attrs, defStyle);
        initializeView(context);
    }

    public void setHand(Hand h){
        hand = h;
        displayHand();
    }

    private void initializeView(Context context){
        tileList = (LinearLayout) findViewById(R.id.tileList);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.component_handdisplay, this);

        displayHand();
    }

    private void displayHand(){
        if( hand==null ){
            return;
        }

        if( !hand.validateCompleteState() ){
            simpleDisplay = true;
            for( Tile t : hand.tiles ){
                simpleDisplayAddTile(t);
            }
        } else {
            simpleDisplay = false;
        }
    }

    private void simpleDisplayAddTile(Tile t){
//        int r = 2;
//        double width = 40;
//        double height = width*tileRatio;
//
//        float[] outerR = new float[] {r, r, r, r, r, r, r, r};
//        Shape tileOutline = new RoundRectShape(outerR, null, null);
//        tileOutline.resize((float)width, (float)height);
//
//        Drawable tileImage = getContext().getDrawable(t.getImageInt());
//
//        Drawable[] layers = new Drawable[2];
//        layers[0] =   //getResources().getDrawable(R.drawable.t, this);
//        layers[1] = tileImage;
//
//        //TODO Display the god damn tile you piece of shit code, just make it work...
//        LayerDrawable layerDrawable = new LayerDrawable(layers);
//
//        tileList.setImageDrawable(layerDrawable);
//
    }
}
