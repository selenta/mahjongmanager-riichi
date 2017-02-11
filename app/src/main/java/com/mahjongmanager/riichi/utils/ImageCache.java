package com.mahjongmanager.riichi.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.util.LruCache;

import com.mahjongmanager.riichi.MainActivity;
import com.mahjongmanager.riichi.R;
import com.mahjongmanager.riichi.common.Tile;

import java.util.Arrays;
import java.util.List;

public class ImageCache {
    private static MainActivity activity;

    private static final int DEFAULT_MEM_CACHE_SIZE = 1024 * 5; // 5MB
    private static LruCache<String, BitmapDrawable> mMemoryCache;

    public static final String HAND_DISPLAY_KEY = "HandDisplay ";
    public static final String KEYBOARD_KEY_LARGE = "KeyboardLarge ";
    public static final String KEYBOARD_KEY_SMALL = "KeyboardSmall ";

    public static void init(MainActivity ma){
        activity = ma;
        mMemoryCache = new LruCache<String, BitmapDrawable>(DEFAULT_MEM_CACHE_SIZE) {
            /**
             * Measure item size in kilobytes rather than units which is more practical
             * for a bitmap cache
             */
            // NOTE for some reason, this makes the Large button version of the HandCalculator
            //      fail to load its button images correctly on tablets with
            //      larger than standard resolutions ... wth?
//            @Override
//            protected int sizeOf(String key, BitmapDrawable value) {
//                final int bitmapSize = getBitmapSize(value) / 1024;
//                return bitmapSize == 0 ? 1 : bitmapSize;
//            }
        };
    }

    /**
     * Get from memory cache.
     *
     * @param keyString Unique identifier for which item to get
     * @return The bitmap drawable if found in cache, null otherwise
     */
    public static BitmapDrawable getBitmapFromCache(String keyString) {
        BitmapDrawable memValue = null;
//        Log.wtf("wtfIsCacheDoing", "keyString: "+keyString);
//        Log.wtf("wtfIsCacheDoing", "wtf cache, size? "+mMemoryCache.size());
//        Log.wtf("wtfIsCacheDoing", "wtf cache, max size? "+mMemoryCache.maxSize());
//        Log.wtf("wtfIsCacheDoing", "wtf cache, miss count? "+mMemoryCache.missCount());
//        Log.wtf("wtfIsCacheDoing", "wtf cache, put count? "+mMemoryCache.putCount());

        if (mMemoryCache != null) {
            memValue = mMemoryCache.get(keyString);
        }

        if( memValue==null && keyString.contains(HAND_DISPLAY_KEY) ){
            populateImageCacheForHandDisplay();
            memValue = mMemoryCache.get(keyString);
        } else if( memValue==null && keyString.contains(KEYBOARD_KEY_LARGE) ){
            populateImageCacheForKeyboard(KEYBOARD_KEY_LARGE);
            memValue = mMemoryCache.get(keyString);
        } else if( memValue==null && keyString.contains(KEYBOARD_KEY_SMALL) ){
            populateImageCacheForKeyboard(KEYBOARD_KEY_SMALL);
            memValue = mMemoryCache.get(keyString);
        }

        return memValue;
    }

    /////////////////////////////////////////////////////
    /////////////          Populate          ////////////
    /////////////////////////////////////////////////////
    private static void populateImageCacheForHandDisplay(){
        // First, do the normal (vertical) version of the tiles
        List<Tile> allTiles = getAllTilesWithImages();
        for(Tile t : allTiles){
            BitmapDrawable dBmap = getBitmapDrawableFromFile(getTileImageInt(t), Utils.HAND_DISPLAY_TILE_WIDTH, null, t.faceDown);
            String keyString = t.getImageCacheKey(ImageCache.HAND_DISPLAY_KEY);
            addBitmapToCache(keyString, dBmap);
        }
        BitmapDrawable frontDBmap = getBitmapDrawableFromFile(R.drawable.front, Utils.HAND_DISPLAY_TILE_WIDTH);
        String frontKeyString = ImageCache.HAND_DISPLAY_KEY + "Front";
        addBitmapToCache(frontKeyString, frontDBmap);

        // Second, do a rotated version of all the tile images
        Matrix matrix = new Matrix();
        matrix.postRotate(270);
        for(Tile t : allTiles){
            BitmapDrawable dBmap = getBitmapDrawableFromFile(getTileImageInt(t), Utils.HAND_DISPLAY_TILE_WIDTH, matrix, false);
            String keyString = t.getImageCacheKey(ImageCache.HAND_DISPLAY_KEY, true);
            addBitmapToCache(keyString, dBmap);
        }
        BitmapDrawable rotatedFrontDBmap = getBitmapDrawableFromFile(R.drawable.front, Utils.HAND_DISPLAY_TILE_WIDTH, matrix, false);
        String rotatedFrontKeyString = ImageCache.HAND_DISPLAY_KEY + "Front Rotated";
        addBitmapToCache(rotatedFrontKeyString, rotatedFrontDBmap);
    }
    private static void populateImageCacheForKeyboard(String keyboardKey){
        int width = 0;
        if( keyboardKey.equals(ImageCache.KEYBOARD_KEY_LARGE) ){
            width = Utils.KEYBOARD_TILE_WIDTH_LARGE;
        } else if( keyboardKey.equals(ImageCache.KEYBOARD_KEY_SMALL) ){
            width = Utils.KEYBOARD_TILE_WIDTH_SMALL;
        }

        List<Tile> allTiles = getAllTilesWithImages();
        for(Tile t : allTiles){
            BitmapDrawable dBmap = getBitmapDrawableFromFile(getTileImageInt(t), width);
            String keyString = t.getImageCacheKey(keyboardKey);
            addBitmapToCache(keyString, dBmap);
        }
        BitmapDrawable frontDBmap = getBitmapDrawableFromFile(R.drawable.front, width);
        String frontKeyString = keyboardKey + "Front";
        addBitmapToCache(frontKeyString, frontDBmap);
    }

    private static BitmapDrawable getBitmapDrawableFromFile(int imageInt, int width ){
        return getBitmapDrawableFromFile(imageInt, width, null, false);
    }
    private static BitmapDrawable getBitmapDrawableFromFile(int imageInt, int width, Matrix matrix, boolean isFacedown ){
        int height = (int) ((double)width * Utils.TILE_RATIO);

        Bitmap origBmap = BitmapFactory.decodeResource(activity.getResources(), imageInt);
        Bitmap resizedBmap;
        if( isFacedown ){
            resizedBmap = Bitmap.createScaledBitmap(origBmap, width, height, false);
        } else {
            int padding = 2 * Utils.TILE_PADDING;
            resizedBmap = Bitmap.createScaledBitmap(origBmap, width-padding, height-padding, false);
        }
        Bitmap rotatedBitmap;
        if( matrix==null ){
            rotatedBitmap = resizedBmap;
        } else {
            rotatedBitmap = Bitmap.createBitmap(resizedBmap, 0, 0, resizedBmap.getWidth(), resizedBmap.getHeight(), matrix, true);
        }
        return new BitmapDrawable(activity.getResources(), rotatedBitmap);
    }

    /**
     * Adds a bitmap to both memory and disk cache.
     * @param keyString Unique identifier for the bitmap to store
     * @param value The bitmap drawable to store
     */
    private static void addBitmapToCache(String keyString, BitmapDrawable value) {
        if (keyString == null || value == null) {
            return;
        }

        if (mMemoryCache != null) {
//            Log.wtf("wtfIsCacheDoing", "keyString: "+keyString);
//            Log.wtf("wtfIsCacheDoing", "wtf cache, size? "+mMemoryCache.size());
            mMemoryCache.put(keyString, value);
        }
    }

    private static List<Tile> getAllTilesWithImages(){
        List<Tile> tiles = HandGenerator.allTiles();

        for(Tile.Suit suit : Arrays.asList(Tile.Suit.MANZU, Tile.Suit.PINZU, Tile.Suit.SOUZU)){
            Tile red5 = new Tile(5, suit);
            red5.red = true;
            tiles.add(red5);
        }
        Tile facedownTile = new Tile(1, Tile.Suit.MANZU);
        facedownTile.faceDown = true;
        tiles.add(facedownTile);

        Tile blankTile = new Tile(0, Tile.Suit.MANZU);
        tiles.add(blankTile);

        return tiles;
    }

    private static int getTileImageInt(Tile t){
        if( t.faceDown ){
            return R.drawable.back;
        }

        switch (t.suit){
            case MANZU:
                switch (t.number){
                    case 1:
                        return R.drawable.man1;
                    case 2:
                        return R.drawable.man2;
                    case 3:
                        return R.drawable.man3;
                    case 4:
                        return R.drawable.man4;
                    case 5:
                        if( !t.red ){
                            return R.drawable.man5;
                        } else {
                            return R.drawable.man5_dora;
                        }
                    case 6:
                        return R.drawable.man6;
                    case 7:
                        return R.drawable.man7;
                    case 8:
                        return R.drawable.man8;
                    case 9:
                        return R.drawable.man9;
                }
                break;
            case PINZU:
                switch (t.number){
                    case 1:
                        return R.drawable.pin1;
                    case 2:
                        return R.drawable.pin2;
                    case 3:
                        return R.drawable.pin3;
                    case 4:
                        return R.drawable.pin4;
                    case 5:
                        if( !t.red ){
                            return R.drawable.pin5;
                        } else {
                            return R.drawable.pin5_dora;
                        }
                    case 6:
                        return R.drawable.pin6;
                    case 7:
                        return R.drawable.pin7;
                    case 8:
                        return R.drawable.pin8;
                    case 9:
                        return R.drawable.pin9;
                }
                break;
            case SOUZU:
                switch (t.number){
                    case 1:
                        return R.drawable.sou1;
                    case 2:
                        return R.drawable.sou2;
                    case 3:
                        return R.drawable.sou3;
                    case 4:
                        return R.drawable.sou4;
                    case 5:
                        if( !t.red ){
                            return R.drawable.sou5;
                        } else {
                            return R.drawable.sou5_dora;
                        }
                    case 6:
                        return R.drawable.sou6;
                    case 7:
                        return R.drawable.sou7;
                    case 8:
                        return R.drawable.sou8;
                    case 9:
                        return R.drawable.sou9;
                }
                break;
            case HONOR:
                switch (t.value){
                    case "East":
                        return R.drawable.ton;
                    case "South":
                        return R.drawable.nan;
                    case "West":
                        return R.drawable.shaa;
                    case "North":
                        return R.drawable.pei;
                    case "White":
                        return R.drawable.haku;
                    case "Green":
                        return R.drawable.hatsu;
                    case "Red":
                        return R.drawable.chun;
                }
                break;
        }
        return R.drawable.blank;
    }
}
