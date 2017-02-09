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
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.mahjongmanager.riichi.MainActivity;
import com.mahjongmanager.riichi.common.Meld;
import com.mahjongmanager.riichi.R;
import com.mahjongmanager.riichi.common.Tile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Utils {
    private MainActivity activity;
    private ImageCache imageCache;
    public Utils(MainActivity ma){
        activity = ma;
        setTileSizeConstants();
        imageCache = ma.getImageCache();
    }

    private void setTileSizeConstants(){
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        SCREEN_SIZE = metrics.widthPixels;
        if( metrics.heightPixels < SCREEN_SIZE ){
            SCREEN_SIZE = metrics.heightPixels;
        }
        Log.i("screenSize", "Screen size in pixels: "+ SCREEN_SIZE);

        borderWidth      = 1+ SCREEN_SIZE / 512;
        tilePadding      = SCREEN_SIZE / 128;
        tileCornerRadius = SCREEN_SIZE / 128;

        // TODO clean the keyboard float values, they seem to work, but are currently arbitrary
        HAND_DISPLAY_TILE_WIDTH   = calcTileWidth(SCREEN_SIZE, 16, 0.02f, false);
        KEYBOARD_TILE_WIDTH_LARGE = calcTileWidth(SCREEN_SIZE,  5, 0.3f, true);
        KEYBOARD_TILE_WIDTH_SMALL = calcTileWidth(SCREEN_SIZE,  9, 0.2f, true);

        KEYBOARD_SMALL_MARGIN  = SCREEN_SIZE / 100;
        KEYBOARD_SMALL_PADDING = SCREEN_SIZE / 100;
    }
    private int calcTileWidth(int screenSize, int numberOfTiles, float edgePadding, boolean includeSpacers){
        // Padding on the sides
        int tileWidth = Math.round((float)screenSize*(1f-edgePadding));

        if( includeSpacers ){
            // Padding between tiles
            tileWidth -= screenSize/200 * (numberOfTiles-1);
        }

        // Divide the remaining space to get the size of each tile
        tileWidth = tileWidth / numberOfTiles;

        return tileWidth;
    }

    ///////////////////////////////////////////////////////////////
    /////////////////        Hand Display         /////////////////
    ///////////////////////////////////////////////////////////////
    public static int SCREEN_SIZE;
    public static Double TILE_RATIO = 1.36;
    private int borderWidth;
    private int tilePadding;
    private int tileCornerRadius;

    public int HAND_DISPLAY_TILE_WIDTH;
    public int KEYBOARD_TILE_WIDTH_LARGE;
    public int KEYBOARD_TILE_WIDTH_SMALL;
    public int KEYBOARD_SMALL_MARGIN;
    public int KEYBOARD_SMALL_PADDING;

    /**
     * The method to get the actual image used in the HandDisplay
     * @param t The tile you want the image of
     * @param rotated If the image is rotated (for a called tile)
     * @return Image of tile
     */
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
        int width  = HAND_DISPLAY_TILE_WIDTH;
        int height = (int) ((double)width * TILE_RATIO);
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
        layerDrawable.setLayerInset(1, borderWidth,borderWidth,borderWidth,borderWidth);
        layerDrawable.setLayerInset(2, tilePadding,tilePadding,tilePadding,tilePadding);
        return layerDrawable;
    }
    private ShapeDrawable getTileOutline(int width, int height){
        float[] outerR = new float[8];
        for(int i=0; i<8; i++ ){
            outerR[i] = tileCornerRadius;
        }
        Shape tileOutline = new RoundRectShape(outerR, new RectF(borderWidth,borderWidth,borderWidth,borderWidth), null);
        tileOutline.resize((float)width, (float)height);
        return new ShapeDrawable(tileOutline);
    }

    public ImageView getHandDisplayPlaceholderTileView(){
        Tile s = new Tile(10, Tile.Suit.MANZU);
        return getHandDisplayTileView(s, false);
    }

    /**
     * Returns the width of the image used as the face of the tile for the specified tilesets.
     * Does NOT return the width of the full tile view that is most commonly used. Expects:
     * <ul>
     *     <li>ImageCache.KEYBOARD_KEY_LARGE</li>
     *     <li>ImageCache.KEYBOARD_KEY_SMALL</li>
     *     <li>ImageCache.HAND_DISPLAY_KEY</li>
     * </ul>
     * @param keyString One of the image size keys from ImageCache
     * @return Width of the images used in specified tileset
     */
    public int getActualTileWidth( String keyString ){
        Tile example = new Tile(1, Tile.Suit.MANZU);
        if( keyString.equals(ImageCache.KEYBOARD_KEY_LARGE) ){
            return imageCache.getBitmapFromCache(example.getImageCacheKey(ImageCache.KEYBOARD_KEY_LARGE)).getIntrinsicWidth();
        } else if( keyString.equals(ImageCache.KEYBOARD_KEY_SMALL) ){
            return imageCache.getBitmapFromCache(example.getImageCacheKey(ImageCache.KEYBOARD_KEY_SMALL)).getIntrinsicWidth();
        } else if( keyString.equals(ImageCache.HAND_DISPLAY_KEY) ){
            return imageCache.getBitmapFromCache(example.getImageCacheKey(ImageCache.KEYBOARD_KEY_SMALL)).getIntrinsicWidth();
        }
        return 0;
    }

    //////////////////////////////////////////////////////////////////////////
    ////////////////////     Populate ImageCache      ////////////////////////
    //////////////////////////////////////////////////////////////////////////
    public void populateImageCacheForHandDisplay(){
        // First, do the normal (vertical) version of the tiles
        List<Tile> allTiles = getAllTilesWithImages();
        for(Tile t : allTiles){
            BitmapDrawable dBmap = getBitmapDrawableFromFile(getTileImageInt(t), HAND_DISPLAY_TILE_WIDTH, null, t.faceDown);
            String keyString = t.getImageCacheKey(ImageCache.HAND_DISPLAY_KEY);
            imageCache.addBitmapToCache(keyString, dBmap);
        }
        BitmapDrawable frontDBmap = getBitmapDrawableFromFile(R.drawable.front, HAND_DISPLAY_TILE_WIDTH);
        String frontKeyString = ImageCache.HAND_DISPLAY_KEY + "Front";
        imageCache.addBitmapToCache(frontKeyString, frontDBmap);

        // Second, do a rotated version of all the tile images
        Matrix matrix = new Matrix();
        matrix.postRotate(270);
        for(Tile t : allTiles){
            BitmapDrawable dBmap = getBitmapDrawableFromFile(getTileImageInt(t), HAND_DISPLAY_TILE_WIDTH, matrix, false);
            String keyString = t.getImageCacheKey(ImageCache.HAND_DISPLAY_KEY, true);
            imageCache.addBitmapToCache(keyString, dBmap);
        }
        BitmapDrawable rotatedFrontDBmap = getBitmapDrawableFromFile(R.drawable.front, HAND_DISPLAY_TILE_WIDTH, matrix, false);
        String rotatedFrontKeyString = ImageCache.HAND_DISPLAY_KEY + "Front Rotated";
        imageCache.addBitmapToCache(rotatedFrontKeyString, rotatedFrontDBmap);
    }
    public void populateImageCacheForKeyboard(String keyboardKey){
        int width = 0;
        if( keyboardKey.equals(ImageCache.KEYBOARD_KEY_LARGE) ){
            width = KEYBOARD_TILE_WIDTH_LARGE;
        } else if( keyboardKey.equals(ImageCache.KEYBOARD_KEY_SMALL) ){
            width = KEYBOARD_TILE_WIDTH_SMALL;
        }

        List<Tile> allTiles = getAllTilesWithImages();
        for(Tile t : allTiles){
            BitmapDrawable dBmap = getBitmapDrawableFromFile(getTileImageInt(t), width);
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
            resizedBmap = Bitmap.createScaledBitmap(origBmap, width, height, false);
        } else {
            int padding = 2*tilePadding;
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
    private List<Tile> getAllTilesWithImages(){
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
        if( tiles.size()==0 ){
            return null;
        }

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

    public static Tile.Wind getRandomWind(){
        List<Tile.Wind> values = Collections.unmodifiableList(Arrays.asList(Tile.Wind.values()));
        return values.get(new Random().nextInt(Tile.Wind.values().length));
    }
    public static Tile.Suit getRandomSuit(){
        List<Tile.Suit> values = Collections.unmodifiableList(Arrays.asList(Tile.Suit.values()));
        return values.get(new Random().nextInt(Tile.Suit.values().length));
    }

    public static Tile randomTile(List<Tile> options){
        return options.get(new Random().nextInt(options.size()));
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

    /**
     *
     * @param list List of tiles to be checked
     * @param tile Tile being looked for
     * @return Whether the list contains a similar tile
     */
    public static boolean listContainsTile(List<Tile> list, Tile tile){
        for(Tile t : list){
            if( t.isSame(tile) ){
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a set of all tiles in the provided list that were duplicates (only one
     * tile of each type).
     * @param tiles List of tiles to search for duplicates
     * @return Set of one copy of each tile that had a duplicate
     */
    public static Set<Tile> findDuplicateTiles(Collection<Tile> tiles ){
        final Set<Tile> duplicateTiles = new HashSet<>();
        final Set<String> tempSet = new HashSet<>();

        for (Tile dupTile : tiles ){
            if (!tempSet.add(dupTile.toString())) {
                boolean isNew = true;
                for(Tile dupTilesListTile : duplicateTiles ){
                    if( dupTilesListTile.isSame(dupTile) ){
                        isNew = false;
                    }
                }
                if( isNew ){
                    duplicateTiles.add(dupTile);
                }
            }
        }
        return duplicateTiles;
    }

    /**
     * Filters the current list down to a single copy of each tile (uses the first copy).
     * Should NOT be used to manipulate anything, only examine.
     * @param original List to be searched
     * @return List of unique tiles
     */
    public static List<Tile> findUniqueTiles(List<Tile> original){
        List<Tile> uniques = new ArrayList<>();

        for(Tile oTile : original){
            boolean isNew = true;
            for(Tile uTile : uniques ){
                if( uTile.isSame(oTile) ){
                    isNew = false;
                }
            }
            if( isNew ){
                uniques.add(oTile);
            }
        }

        return uniques;
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

    public static void sortMelds( List<Meld> mz ){
        Collections.sort(mz, new Comparator<Meld>() {
            @Override
            public int compare(Meld m1, Meld m2) {
                if( m1.size()!=m2.size() ){
                    return m2.size() - m1.size();
                }
                return m1.firstTile().sortId - m2.firstTile().sortId; // Ascending
            }
        });
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
