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
import android.widget.ImageView;

import com.mahjongmanager.riichi.MainActivity;
import com.mahjongmanager.riichi.Meld;
import com.mahjongmanager.riichi.R;
import com.mahjongmanager.riichi.Tile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Utils {
    private MainActivity activity;
    private ImageCache imageCache;
    public Utils(MainActivity ma){
        activity = ma;
        imageCache = ma.getImageCache();
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

    private String HAND_DISPLAY_KEY = "HandDisplay ";

    public ImageView getHandDisplayTileView(Tile t, boolean rotated){
        if( activity==null ){
            return null;
        }

        String keyString = t.getImageCacheKey(HAND_DISPLAY_KEY, rotated);

        Drawable dBmap = imageCache.getBitmapFromCache(keyString);
        LayerDrawable tileImage = getCombinedImage(dBmap, t.faceDown, rotated);

        ImageView view = new ImageView(activity);
        view.setImageDrawable(tileImage);

        return view;
    }
    private LayerDrawable getCombinedImage(Drawable bmp, boolean isFacedown, boolean isRotated){
        if( isFacedown ){
            Drawable[] layers = new Drawable[1];
            layers[0] = bmp;
            return new LayerDrawable(layers);
        }
        String frontTileKey = HAND_DISPLAY_KEY + "Front";
        ShapeDrawable tileOutline = getTileOutline(tileWidth, tileHeight);

        if( isRotated ){
            frontTileKey+=" Rotated";
            tileOutline = getTileOutline(tileHeight, tileWidth);
        }

        Drawable[] layers = new Drawable[3];
        layers[0] = tileOutline;
        layers[1] = imageCache.getBitmapFromCache(frontTileKey);
        layers[2] = bmp;
        LayerDrawable layerDrawable = new LayerDrawable(layers);
        layerDrawable.setLayerInset(1, 3,3,3,3);
        layerDrawable.setLayerInset(2, tilePadding,tilePadding,tilePadding,tilePadding);
        return layerDrawable;
    }
    private ShapeDrawable getTileOutline(int width, int height){
        float[] outerR = new float[8];
        for(int i=0; i<8; i++ ){
            outerR[i] = tileCornerRadius;
        }
        Shape tileOutline = new RoundRectShape(outerR, new RectF(3,3,3,3), null);
        tileOutline.resize((float)width, (float)height);
        return new ShapeDrawable(tileOutline);
    }

    public void populateImageCacheForHandDisplay(int width){
        imageCache.clearCache();

        // First, do the normal vertical version of the tiles
        int height = (int) ((double)width*tileRatio);
        List<Tile> allTiles = getAllTilesWithImages();
        for(Tile t : allTiles){
            Bitmap origBmap = BitmapFactory.decodeResource(activity.getResources(), t.getImageInt());
            Bitmap resizedBmap = Bitmap.createScaledBitmap(origBmap, width, height, false);
            BitmapDrawable dBmap = new BitmapDrawable(activity.getResources(), resizedBmap);
            String keyString = t.getImageCacheKey(HAND_DISPLAY_KEY);
            imageCache.addBitmapToCache(keyString, dBmap);
        }
        Bitmap frontOrigBmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.front);
        Bitmap frontResizedBmap = Bitmap.createScaledBitmap(frontOrigBmap, width, height, false);
        BitmapDrawable frontDBmap = new BitmapDrawable(activity.getResources(), frontResizedBmap);
        String frontKeyString = HAND_DISPLAY_KEY + "Front";
        imageCache.addBitmapToCache(frontKeyString, frontDBmap);

        // Second, do a rotated version of all the tile images
        Matrix matrix = new Matrix();
        matrix.postRotate(270);
        for(Tile t : allTiles){
            Bitmap origBmap = BitmapFactory.decodeResource(activity.getResources(), t.getImageInt());
            Bitmap resizedBmap = Bitmap.createScaledBitmap(origBmap, width, height, false);
            Bitmap rotatedBitmap = Bitmap.createBitmap(resizedBmap, 0, 0, resizedBmap.getWidth(), resizedBmap.getHeight(), matrix, true);

            BitmapDrawable dBmap = new BitmapDrawable(activity.getResources(), rotatedBitmap);
            String keyString = t.getImageCacheKey(HAND_DISPLAY_KEY, true);
            imageCache.addBitmapToCache(keyString, dBmap);
        }
        Bitmap rotatedFrontResizedBmap = Bitmap.createBitmap(frontResizedBmap, 0, 0, frontResizedBmap.getWidth(), frontResizedBmap.getHeight(), matrix, true);
        BitmapDrawable rotatedFrontDBmap = new BitmapDrawable(activity.getResources(), rotatedFrontResizedBmap);
        String rotatedFrontKeyString = HAND_DISPLAY_KEY + "Front Rotated";
        imageCache.addBitmapToCache(rotatedFrontKeyString, rotatedFrontDBmap);
    }
    private List<Tile> getAllTilesWithImages(){
        List<Tile> tiles = new ArrayList<>();
        for(int i=1; i<10; i++){
            tiles.add(new Tile(i, "MANZU"));
            tiles.add(new Tile(i, "PINZU"));
            tiles.add(new Tile(i, "SOUZU"));
        }
        tiles.add(new Tile("East", "HONOR"));
        tiles.add(new Tile("South", "HONOR"));
        tiles.add(new Tile("West", "HONOR"));
        tiles.add(new Tile("North", "HONOR"));
        tiles.add(new Tile("White", "HONOR"));
        tiles.add(new Tile("Green", "HONOR"));
        tiles.add(new Tile("Red", "HONOR"));

        for(Tile.Suit suit : Arrays.asList(Tile.Suit.MANZU, Tile.Suit.PINZU, Tile.Suit.SOUZU)){
            Tile red5 = new Tile(5, suit.toString());
            red5.red = true;
            tiles.add(red5);
        }
        Tile facedownTile = new Tile(1, "MANZU");
        facedownTile.faceDown = true;
        tiles.add(facedownTile);

        Tile blankTile = new Tile(0, "MANZU");
        tiles.add(blankTile);

        return tiles;
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
    public static SetState getSetState(Meld meld){ return getSetState(meld.getTiles()); }

    public static boolean containsHonorsOrTerminalsOnly(Meld meld){ return containsHonorsOrTerminalsOnly(meld.getTiles()); }
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
