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
import android.widget.TextView;

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

    ///////////////////////////////////////////////////////////////
    /////////////////        Hand Display         /////////////////
    ///////////////////////////////////////////////////////////////
    public static Double TILE_RATIO = 1.36;
    private int tilePadding = 8;
    private int tileCornerRadius = 8;

    public ImageView getHandDisplayTileView(Tile t, boolean rotated){
        if( activity==null ){
            return null;
        }

        String keyString = t.getImageCacheKey(ImageCache.HAND_DISPLAY_KEY, rotated);

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
        String frontTileKey = ImageCache.HAND_DISPLAY_KEY + "Front";
        int width  = ImageCache.HAND_DISPLAY_TILE_WIDTH;
        int height = ImageCache.getTileHeight(width);
        ShapeDrawable tileOutline = getTileOutline(width, height);

        if( isRotated ){
            frontTileKey+=" Rotated";
            tileOutline = getTileOutline(height, width);
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

    //////////////////////////////////////////////////////////////////////////
    ////////////////////     Populate ImageCache      ////////////////////////
    //////////////////////////////////////////////////////////////////////////
    public void populateImageCacheForHandDisplay(){
        int width = ImageCache.HAND_DISPLAY_TILE_WIDTH;

        // First, do the normal (vertical) version of the tiles
        List<Tile> allTiles = getAllTilesWithImages();
        for(Tile t : allTiles){
            BitmapDrawable dBmap = getBitmapDrawableFromFile(t.getImageInt(), width, null, t.faceDown);
            String keyString = t.getImageCacheKey(ImageCache.HAND_DISPLAY_KEY);
            imageCache.addBitmapToCache(keyString, dBmap);
        }
        BitmapDrawable frontDBmap = getBitmapDrawableFromFile(R.drawable.front, width);
        String frontKeyString = ImageCache.HAND_DISPLAY_KEY + "Front";
        imageCache.addBitmapToCache(frontKeyString, frontDBmap);

        // Second, do a rotated version of all the tile images
        Matrix matrix = new Matrix();
        matrix.postRotate(270);
        for(Tile t : allTiles){
            BitmapDrawable dBmap = getBitmapDrawableFromFile(t.getImageInt(), width, matrix, false);
            String keyString = t.getImageCacheKey(ImageCache.HAND_DISPLAY_KEY, true);
            imageCache.addBitmapToCache(keyString, dBmap);
        }
        BitmapDrawable rotatedFrontDBmap = getBitmapDrawableFromFile(R.drawable.front, width, matrix, false);
        String rotatedFrontKeyString = ImageCache.HAND_DISPLAY_KEY + "Front Rotated";
        imageCache.addBitmapToCache(rotatedFrontKeyString, rotatedFrontDBmap);
    }
    public void populateImageCacheForKeyboard(String keyboardKey, int width){
        List<Tile> allTiles = getAllTilesWithImages();
        for(Tile t : allTiles){
            BitmapDrawable dBmap = getBitmapDrawableFromFile(t.getImageInt(), width);
            String keyString = t.getImageCacheKey(keyboardKey);
            imageCache.addBitmapToCache(keyString, dBmap);
        }
        BitmapDrawable frontDBmap = getBitmapDrawableFromFile(R.drawable.front, width);
        String frontKeyString = keyboardKey + "Front";
        imageCache.addBitmapToCache(frontKeyString, frontDBmap);
    }
    private BitmapDrawable getBitmapDrawableFromFile(int imageInt, int width ){
        return getBitmapDrawableFromFile(imageInt, width, null, false);
    }
    private BitmapDrawable getBitmapDrawableFromFile(int imageInt, int width, Matrix matrix, boolean isFacedown ){
        int height = (int) ((double)width*TILE_RATIO);

        Bitmap origBmap = BitmapFactory.decodeResource(activity.getResources(), imageInt);
        Bitmap resizedBmap;
        if( isFacedown ){
            resizedBmap = Bitmap.createScaledBitmap(origBmap, width+2*tilePadding, height+2*tilePadding, false);
        } else {
            resizedBmap = Bitmap.createScaledBitmap(origBmap, width, height, false);
        }
        Bitmap rotatedBitmap;
        if( matrix==null ){
            rotatedBitmap = resizedBmap;
        } else {
            rotatedBitmap = Bitmap.createBitmap(resizedBmap, 0, 0, resizedBmap.getWidth(), resizedBmap.getHeight(), matrix, true);
        }
        return new BitmapDrawable(activity.getResources(), rotatedBitmap);
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

    //////////////////////////////////////////////////////////////////////////
    ////////////////////     Common Util Methods      ////////////////////////
    //////////////////////////////////////////////////////////////////////////
    public enum MeldState {
        CLOSEDSET, OPENCHII, OPENPON, OPENKAN, ADDEDKAN, CLOSEDKAN
    }

    /**
     * When provided a Meld, will return a local enum indicating how it should be
     * displayed when viewed as part of a hand.
     *
     * @param meld The Meld object in question
     * @return Local enum indicating the how the meld should be displayed
     */
    public static MeldState getMeldState(Meld meld){ return getMeldState(meld.getTiles()); }
    private static MeldState getMeldState(List<Tile> tiles){
        Tile firstTile = tiles.get(0);
        if( firstTile.revealedState==Tile.RevealedState.CHI ){
            return MeldState.OPENCHII;
        } else if( firstTile.revealedState==Tile.RevealedState.PON || firstTile.revealedState==Tile.RevealedState.ADDEDKAN ){
            if( tiles.size()==3 ){
                return MeldState.OPENPON;
            } else {
                return MeldState.ADDEDKAN;
            }
        } else if( firstTile.revealedState==Tile.RevealedState.OPENKAN ){
            return MeldState.OPENKAN;
        } else if( firstTile.revealedState==Tile.RevealedState.CLOSEDKAN ){
            return MeldState.CLOSEDKAN;
        }

        return MeldState.CLOSEDSET;
    }

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

    public static String prettifyName(String s){
        char[] chars = s.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i]=='.' || chars[i]=='\'') {
                found = false;
            }
        }
        return String.valueOf(chars);
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
