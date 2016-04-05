package com.mahjongmanager.riichi;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HandDisplay extends LinearLayout {
    Context context;

    private LinearLayout tileList;
    private TextView tileText;

    private Hand hand;
    private Boolean simpleDisplay = true;

    /////////////////////////////////////////
    ///////////   Constructors   ////////////
    /////////////////////////////////////////
    public HandDisplay(Context ctx){
        this(ctx, null);
    }
    public HandDisplay(Context ctx, AttributeSet attrs) {
        this(ctx, attrs, 0);
    }
    public HandDisplay(Context ctx, AttributeSet attrs, int defStyle ){
        super(ctx, attrs, defStyle);
        context = ctx;
        initializeView();
    }

    private void initializeView(){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.component_handdisplay, this);

        tileText = (TextView) findViewById(R.id.tileText);
        tileList = (LinearLayout) findViewById(R.id.tileList);

        //TODO If component is configured incorrectly, display error message, instead of blank
        if(context==null){
            return;
        }
        displayHand();
    }

    ///////////////////////////////////////////
    /////////////     Main     ////////////////
    ///////////////////////////////////////////
    public void setHand(Hand h){
        hand = h;
        displayHand();
    }

    private void displayHand(){
        if( hand==null ){
            return;
        }
        if( !hand.validateCompleteState() ){
            simpleDisplay = true;
            tileText.setText(hand.toString());
            displaySimpleHand();
        } else {
            simpleDisplay = false;
            tileText.setText(hand.printAllSets());
            displayComplexHand();

            //TODO Temporary. Remove once I have a better way to display complex hands
            displaySimpleHand();
        }
    }

    private void displaySimpleHand(){
        tileList.removeAllViews();
        for( Tile t : hand.tiles ){
            simpleAddTile(t);
        }
    }

    private void simpleAddTile(Tile t){
//        int r = 2;

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

        ImageView view = ((MainActivity)context).getUtils().getTileView(t);
        tileList.addView(view);
    }

    private void displayComplexHand(){

    }
}
