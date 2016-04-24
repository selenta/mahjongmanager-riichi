package com.mahjongmanager.riichi.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.util.Log;
import android.widget.TextView;

import com.mahjongmanager.riichi.MainActivity;
import com.mahjongmanager.riichi.Meld;
import com.mahjongmanager.riichi.Tile;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Utils {
    private MainActivity activity;
    public Utils(MainActivity ma){
        activity = ma;
    }

    ////////////////////////////////////////////////////////////////
    //////////////////          Main              //////////////////
    ////////////////////////////////////////////////////////////////
    private int tileWidth = 50;
    private Double tileRatio = 1.36;
    private int tileHeight = (int) ((double)tileWidth*tileRatio);
    private int tilePadding = 8;
    private int tileCornerRadius = 8;

    public enum SetState {
        CLOSEDSET, OPENCHII, OPENPON, OPENKAN, ADDEDKAN, CLOSEDKAN
    }

    public TextView getTileView(Tile t){
        if( activity==null ){
            //This should never happen
            return null;
        } else if( t.faceDown ){
            return getTileFaceDown(t);
        }

        float[] outerR = new float[8];
        for(int i=0; i<8; i++ ){
            outerR[i] = tileCornerRadius;
        }
        Shape tileOutline = new RoundRectShape(outerR, new RectF(3,3,3,3), null);
        tileOutline.resize((float)tileWidth, (float)tileHeight);
        ShapeDrawable drawableShape = new ShapeDrawable(tileOutline);

        Bitmap origBmap = BitmapFactory.decodeResource(activity.getResources(), t.getImageInt());
        Bitmap resizedBmap = Bitmap.createScaledBitmap(origBmap, tileWidth, tileHeight, false);
        Drawable dBmap = new BitmapDrawable(activity.getResources(), resizedBmap);


        Drawable[] layers = new Drawable[2];
        layers[0] = drawableShape;
        layers[1] = dBmap;
        LayerDrawable layerDrawable = new LayerDrawable(layers);
        layerDrawable.setLayerInset(1, tilePadding,tilePadding,tilePadding,tilePadding);

        TextView view = new TextView(activity);
        view.setBackground(layerDrawable);

        return view;
    }
    private TextView getTileFaceDown(Tile t){
        int faceDownWidth = tileWidth + 2*tilePadding;
        int faceDownHeight = tileHeight + 2*tilePadding;

        Bitmap origBmap = BitmapFactory.decodeResource(activity.getResources(), t.getImageInt());
        Bitmap resizedBmap = Bitmap.createScaledBitmap(origBmap, faceDownWidth, faceDownHeight, false);
        Drawable dBmap = new BitmapDrawable(activity.getResources(), resizedBmap);

        Drawable[] layers = new Drawable[1];
        layers[0] = dBmap;
        LayerDrawable layerDrawable = new LayerDrawable(layers);

        TextView view = new TextView(activity);
        view.setBackground(layerDrawable);

        return view;
    }
    public TextView getTileViewRotated(Tile t){
        if( activity==null ){
            //This should never happen
            return null;
        }

        float[] outerR = new float[8];
        for(int i=0; i<8; i++ ){
            outerR[i] = tileCornerRadius;
        }
        Shape tileOutline = new RoundRectShape(outerR, new RectF(3,3,3,3), null);
        tileOutline.resize((float)tileWidth, (float)tileHeight);
        ShapeDrawable drawableShape = new ShapeDrawable(tileOutline);

        Bitmap origBmap = BitmapFactory.decodeResource(activity.getResources(), t.getImageInt());
        Bitmap resizedBmap = Bitmap.createScaledBitmap(origBmap, tileWidth, tileHeight, false);
        Matrix matrix = new Matrix();
        matrix.postRotate(270);
        Bitmap rotatedBitmap = Bitmap.createBitmap(resizedBmap, 0, 0, resizedBmap.getWidth(), resizedBmap.getHeight(), matrix, true);
        Drawable dBmap = new BitmapDrawable(activity.getResources(), rotatedBitmap);

        Drawable[] layers = new Drawable[2];
        layers[0] = drawableShape;
        layers[1] = dBmap;
        LayerDrawable layerDrawable = new LayerDrawable(layers);
        layerDrawable.setLayerInset(1, tilePadding,tilePadding,tilePadding,tilePadding);

        TextView view = new TextView(activity);
        view.setBackground(layerDrawable);

        return view;
    }

    /**
     * When provided a Set (aka Meld) from a complete hand, will return a local enum indicating
     * the nature of the set, and thus how it should be displayed as part of a complete hand.
     *
     * @param set The full list of tiles in a Set (aka Meld)
     * @return Local enum indicating the nature of the set
     */
    public static SetState getSetState(List<Tile> set){
        Tile firstTile = set.get(0);
        if( firstTile.revealedState==Tile.RevealedState.CHI ){
            return SetState.OPENCHII;
        } else if( firstTile.revealedState==Tile.RevealedState.PON || firstTile.revealedState==Tile.RevealedState.ADDEDKAN ){
            if( set.size()==3 ){
                return SetState.OPENPON;
            } else {
                return SetState.ADDEDKAN;
            }
        } else if( firstTile.revealedState==Tile.RevealedState.OPENKAN ){
            return SetState.OPENKAN;
        } else if( firstTile.revealedState==Tile.RevealedState.CLOSEDKAN ){
            return SetState.CLOSEDKAN;
        }

        return SetState.CLOSEDSET;
    }
    public static SetState getSetState(Meld meld){ return getSetState(meld.tiles); }

    public static boolean containsHonorsOrTerminalsOnly(Meld meld){ return containsHonorsOrTerminalsOnly(meld.tiles); }
    public static boolean containsHonorsOrTerminalsOnly(List<Tile> tiles){
        for( Tile t : tiles ){
            if( t.number!=null && t.number>1 && t.number<9 ){
                return false;
            }
        }
        return true;
    }
    public static boolean containsHonorsOrTerminals( List<Tile> s ){
        for( Tile t : s ){
            if( t.dragon!=null || t.wind!=null || t.number==1 || t.number==9 ){
                return true;
            }
        }
        return false;
    }
    public static boolean containsHonors( List<Tile> s ){
        for( Tile t : s ){
            if( t.dragon!=null || t.wind!=null ){
                return true;
            }
        }
        return false;
    }
    public static boolean containsTerminals( List<Tile> s ){
        for( Tile t : s ){
            if( !t.suit.toString().equals("HONOR") && (t.number==1||t.number==9) ){
                return true;
            }
        }
        return false;
    }
    public static boolean containsSimples( List<Tile> s ){
        for( Tile t : s ){
            if( t.number!=null && t.number>1 && t.number<9 ){
                return true;
            }
        }
        return false;
    }
    public static boolean containsWinningTile( List<Tile> s ){
        for( Tile t : s ){
            if( t.winningTile ){
                return true;
            }
        }
        return false;
    }

    public static void sort( List<Tile> tz ){
        Collections.sort(tz, new Comparator<Tile>() {
            @Override
            public int compare(Tile p1, Tile p2) {
                return p1.sortId - p2.sortId; // Ascending
            }
        });
    }
}
