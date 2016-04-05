package com.mahjongmanager.riichi;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

public class Utils {
    MainActivity activity;

    public Utils(MainActivity ma){
        activity = ma;
    }

    public ImageView getTileView(Tile t){
        int width = 60;
        Double tileRatio = 1.26;    //TODO is this actually correct? According to which tile set?
        int height = (int) ((double)width*tileRatio);

        Bitmap origBmap = BitmapFactory.decodeResource(activity.getResources(), t.getImageInt());
        Bitmap resizedBmap = Bitmap.createScaledBitmap(origBmap, width, height, true);
//        Drawable d = new BitmapDrawable(getResources(), resizedBmap);
        ImageView view = new ImageView(activity);
        view.setImageBitmap(resizedBmap);

        return view;
    }

}
