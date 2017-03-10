package com.mahjongmanager.riichi.utils;

import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.mahjongmanager.riichi.MainActivity;
import com.mahjongmanager.riichi.common.Meld;
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
    private static MainActivity activity;

    public static int SCREEN_SIZE;
    public static Double TILE_RATIO = 1.36;
    public static int TILE_PADDING;

    public static int HAND_DISPLAY_TILE_WIDTH;
    public static int KEYBOARD_TILE_WIDTH_LARGE;
    public static int KEYBOARD_TILE_WIDTH_SMALL;
    public static int KEYBOARD_SMALL_MARGIN;
    public static int KEYBOARD_SMALL_PADDING;

    public static void init(MainActivity ma){
        activity = ma;
        setTileSizeConstants();
    }

    private static void setTileSizeConstants(){
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        SCREEN_SIZE = metrics.widthPixels;
        if( metrics.heightPixels < SCREEN_SIZE ){
            SCREEN_SIZE = metrics.heightPixels;
        }
        Log.i("screenSize", "Screen size in pixels: "+ SCREEN_SIZE);

        borderWidth      = 1+ SCREEN_SIZE / 512;
        TILE_PADDING     = SCREEN_SIZE / 128;
        tileCornerRadius = SCREEN_SIZE / 128;

        // TODO clean the keyboard float values, they seem to work, but are currently arbitrary
        HAND_DISPLAY_TILE_WIDTH   = calcTileWidth(SCREEN_SIZE, 16, 0.02f, false);
        KEYBOARD_TILE_WIDTH_LARGE = calcTileWidth(SCREEN_SIZE,  5, 0.3f, true);
        KEYBOARD_TILE_WIDTH_SMALL = calcTileWidth(SCREEN_SIZE,  9, 0.2f, true);

        KEYBOARD_SMALL_MARGIN  = SCREEN_SIZE / 100;
        KEYBOARD_SMALL_PADDING = SCREEN_SIZE / 100;
    }
    private static int calcTileWidth(int screenSize, int numberOfTiles, float edgePadding, boolean includeSpacers){
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
    private static int borderWidth;
    private static int tileCornerRadius;

    /**
     * The method to get the actual image used in the HandDisplay
     * @param t The tile you want the image of
     * @param rotated If the image is rotated (for a called tile)
     * @return Image of tile
     */
    public static ImageView getHandDisplayTileView(Tile t, boolean rotated){
        if( activity==null ){
            return null;
        }

        String keyString = t.getImageCacheKey(ImageCache.HAND_DISPLAY_KEY, rotated);

        Drawable dBmap = ImageCache.getBitmapFromCache(keyString);
        LayerDrawable tileImage = getCombinedImage(dBmap, t.faceDown, rotated);

        ImageView view = new ImageView(activity);
        view.setImageDrawable(tileImage);

        return view;
    }
    private static LayerDrawable getCombinedImage(Drawable bmp, boolean isFacedown, boolean isRotated){
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
        layers[1] = ImageCache.getBitmapFromCache(frontTileKey);
        layers[2] = bmp;
        LayerDrawable layerDrawable = new LayerDrawable(layers);
        layerDrawable.setLayerInset(1, borderWidth,borderWidth,borderWidth,borderWidth);
        layerDrawable.setLayerInset(2, TILE_PADDING, TILE_PADDING, TILE_PADDING, TILE_PADDING);
        return layerDrawable;
    }
    private static ShapeDrawable getTileOutline(int width, int height){
        float[] outerR = new float[8];
        for(int i=0; i<8; i++ ){
            outerR[i] = tileCornerRadius;
        }
        Shape tileOutline = new RoundRectShape(outerR, new RectF(borderWidth,borderWidth,borderWidth,borderWidth), null);
        tileOutline.resize((float)width, (float)height);
        return new ShapeDrawable(tileOutline);
    }

    public static ImageView getHandDisplayPlaceholderTileView(){
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
    public static int getActualTileWidth( String keyString ){
        Tile example = new Tile(1, Tile.Suit.MANZU);
        if( keyString.equals(ImageCache.KEYBOARD_KEY_LARGE) ){
            return ImageCache.getBitmapFromCache(example.getImageCacheKey(ImageCache.KEYBOARD_KEY_LARGE)).getIntrinsicWidth();
        } else if( keyString.equals(ImageCache.KEYBOARD_KEY_SMALL) ){
            return ImageCache.getBitmapFromCache(example.getImageCacheKey(ImageCache.KEYBOARD_KEY_SMALL)).getIntrinsicWidth();
        } else if( keyString.equals(ImageCache.HAND_DISPLAY_KEY) ){
            return ImageCache.getBitmapFromCache(example.getImageCacheKey(ImageCache.KEYBOARD_KEY_SMALL)).getIntrinsicWidth();
        }
        return 0;
    }


    //////////////////////////////////////////////////////////////////////////
    ////////////////////     Common Util Methods      ////////////////////////
    //////////////////////////////////////////////////////////////////////////
    public enum MeldState {
        INVALID, CLOSEDSET, OPENCHII, OPENPON, OPENKAN, ADDEDKAN, CLOSEDKAN
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
            return MeldState.INVALID;
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
            if( t.number>1 && t.number<9 ){
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
            if( t.number>1 && t.number<9 ){
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
     * Return only the tiles in the list of the specified type
     * @param list List to be searched
     * @param tile Tile being looked for
     * @return All copies of that tile from the list
     */
    public static List<Tile> findTiles(List<Tile> list, Tile tile){
        List<Tile> temp = new ArrayList<>();
        for(Tile t : list){
            if(tile.isSame(t)){
                temp.add(t);
            }
        }
        return temp;
    }

    /**
     * Returns a set of all tiles in the provided list that were duplicates (only one
     * tile of each type).
     * @param tiles List of tiles to search for duplicates
     * @return Set of one copy of each tile that had a duplicate
     */
    public static List<Tile> findDuplicateTiles(Collection<Tile> tiles ){
        List<Tile> duplicateTiles = new ArrayList<>();
        Set<String> tempSet = new HashSet<>();

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
