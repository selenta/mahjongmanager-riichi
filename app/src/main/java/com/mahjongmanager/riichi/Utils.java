package com.mahjongmanager.riichi;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.widget.TextView;

public class Utils {
    MainActivity activity;

    public Utils(MainActivity ma){
        activity = ma;
    }

    public TextView getTileView(Tile t){
        int width = 50;
        Double tileRatio = 1.26;    //TODO is this actually correct? According to which tile set?
        int height = (int) ((double)width*tileRatio);


        int r = 5;
        float[] outerR = new float[] {r, r, r, r, r, r, r, r};
        Shape tileOutline = new RoundRectShape(outerR, new RectF(2,2,2,2), null);
        tileOutline.resize((float)width, (float)height);
        ShapeDrawable drawableShape = new ShapeDrawable(tileOutline);

        Bitmap origBmap = BitmapFactory.decodeResource(activity.getResources(), t.getImageInt());
        Bitmap resizedBmap = Bitmap.createScaledBitmap(origBmap, width, height, false);
        Drawable dBmap = new BitmapDrawable(activity.getResources(), resizedBmap);


        Drawable[] layers = new Drawable[2];
        layers[0] = drawableShape;
        layers[1] = dBmap;
        LayerDrawable layerDrawable = new LayerDrawable(layers);
        int tilePadding = 8;
        layerDrawable.setLayerInset(1, tilePadding,tilePadding,tilePadding,tilePadding);

        TextView view = new TextView(activity);
        view.setBackground(layerDrawable);

        return view;
    }

}
