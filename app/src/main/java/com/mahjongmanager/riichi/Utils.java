package com.mahjongmanager.riichi;

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

import java.util.List;

public class Utils {
    MainActivity activity;
    public Utils(MainActivity ma){
        activity = ma;
    }

    ////////////////////////////////////////////////////////////////
    //////////////////          Main              //////////////////
    ////////////////////////////////////////////////////////////////

    private int tileWidth = 50;
    private Double tileRatio = 1.36;    //TODO is this actually correct? According to which tile set?
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

    /**
     * Check whether the set of tiles is a valid, internally consistent, set of tiles
     * according to the rules of Mahjong. Checks for the following:
     * <ul>
     *     <li> There are either 2(pair), 3, or 4 tiles
     *     <li> There is at most one called tile
     *     <li> All tiles have same revealedStatus (unless there is a called tile or an AddedKan)
     *     <li> calledFrom status is consistent with revealedStatus
     *     <li> All tiles have non-null revealedState and calledFrom values
     *     <li> Chiis are called from Left player
     * </ul>
     * TODO account for AddedKan
     * TODO verify that all the things I said are being done are being done
     *
     * @param s List of tiles
     * @return Boolean that indicates whether the list of tiles is a valid set in Mahjong
     */
    public static boolean validateSet( List<Tile> s ){
        if( s.size()<2 || s.size()>4 ){
            Log.e("validateSet", "Too many, or too few, tiles: " + s.toString());
            return false;
        }

        if( !validateUniqueCalledTile(s) ){
            return false;
        } else if( !validateUniqueAddedTile(s) ){
            return false;
        }

        if( s.size()==2 ){
            return validatePair(s);
        }

        return validateRevealedStates(s);

    }
    private static boolean validatePair(List<Tile> pair){
        if( !pair.get(0).value.equals(pair.get(1).value) ){
            Log.e("validateSet", "It's not a pair if the two tiles aren't the same stupid!: "+pair.toString());
            return false;
        }
        Tile calledTile = getCalledTile(pair);
        if( calledTile!=null && !calledTile.winningTile ){
            Log.e("validateSet", "A called tile in a pair can only be called if it's the winning tile: "+calledTile.toString());
            return false;
        }
        Tile.RevealedState setRevealedState = getNonCalledRevealedState(pair);
        if( setRevealedState!= Tile.RevealedState.NONE ){
            Log.e("validateSet", "At least one of the tiles in the pair has to be concealed: "+setRevealedState.toString());
            return false;
        }
        return true;
    }
    private static boolean validateUniqueCalledTile(List<Tile> set){
        Tile calledTile = getCalledTile(set);
        for(Tile t : set){
            if( t!=calledTile && t.calledFrom!=Tile.CalledFrom.NONE ){
                Log.e("validateSet", "More than one tile with a calledFrom status: " + calledTile.toString() + " + " + t.toString());
                return false;
            }
        }
        return true;
    }
    private static boolean validateUniqueAddedTile(List<Tile> set){
        Tile addedTile = getAddedTile(set);
        for(Tile t : set){
            if( t!=addedTile && t.revealedState== Tile.RevealedState.ADDEDKAN ){
                Log.e("validateSet", "More than one tile with a addedTile status: " + addedTile.toString() + " + " + t.toString());
                return false;
            }
        }
        return true;
    }
    private static boolean validateRevealedStates(List<Tile> set){
        // Validate revealed state
        //      All non-called tiles match (except in case of AddedKan)
        //      State makes sense based on number of tiles in the set
        //      Called tile is non-conflicting with the other tiles' state
        //      Other tiles' states are non-conflicting with called tile's state
        // CalledTile for Chii comes from left player
        return !(!nonCalledTilesMatchRevealedState(set)
                || !revealedStateMatchesTileCount(set)
                || !consistentCalledTileState(set)
                || !ifChiiIsCalledFromLeft(set)
                || !hasCalledTileIfRevealed(set)
                || !isRevealedIfKan(set));

    }
    private static boolean nonCalledTilesMatchRevealedState(List<Tile> set){
        Tile calledTile = getCalledTile(set);
        Tile.RevealedState setRevealedState = getNonCalledRevealedState(set);

        for( Tile t : set ){
            if( t != calledTile
                        && !t.winningTile
                        && setRevealedState != t.revealedState
                        && t.revealedState != Tile.RevealedState.ADDEDKAN ){
                Log.e("validateSet", "(Non-called)Tiles do not match revealed states: " + setRevealedState.toString() + " - " + t.revealedState.toString()+" - "+set.toString());
                return false;
            }
        }
        return true;
    }
    private static boolean revealedStateMatchesTileCount(List<Tile> set){
        Tile.RevealedState setRevealedState = getNonCalledRevealedState(set);

        if( set.size()==3 ){
            if( setRevealedState == Tile.RevealedState.CLOSEDKAN || setRevealedState == Tile.RevealedState.OPENKAN ){
                Log.e("validateSet", "ClosedKan/OpenKan must be a set of size 4: "+set.toString());
                return false;
            }
        } else {
            if( setRevealedState == Tile.RevealedState.CHI || setRevealedState == Tile.RevealedState.NONE ){
                Log.e("validateSet", "Closed/Chii must be a set of size 3: "+setRevealedState.toString()+" - "+set.toString());
                return false;
            }
        }
        return true;
    }
    private static boolean consistentCalledTileState(List<Tile> set){
        Tile calledTile = getCalledTile(set);
        if( calledTile!=null ){
            Tile addedTile = getAddedTile(set);
            Tile.RevealedState setRevealedState = getNonCalledRevealedState(set);

            switch (calledTile.revealedState){
                case NONE:
                    if( !calledTile.winningTile ){
                        Log.e("validateSet", "Called tile can't be in a revealed state of NONE unless it is a winning tile: "+calledTile.toString()+" - "+calledTile.revealedState.toString());
                        return false;
                    }
                    break;
                case PON:
                    if( set.size()!=3 && addedTile==null ){
                        Log.e("validateSet", "Called Chii/Pon must be a set of size 3 unless part of AddedKan: "+calledTile.toString()+" - "+calledTile.revealedState.toString()+" - "+set.toString());
                        return false;
                    }
                    break;
                case CLOSEDKAN:
                    Log.e("validateSet", "A called tile cannot be part of a closed kan: "+calledTile.toString()+" - "+calledTile.revealedState.toString());
                    return false;
                case ADDEDKAN:
                    Log.e("validateSet", "Called tile cannot be AddedKan: "+set.toString());
                    return false;
            }

            if( calledTile.revealedState!=setRevealedState && calledTile.revealedState!=Tile.RevealedState.NONE ){
                Log.e("validateSet", "CalledTile state does not match the setState of the rest of the tiles: "+calledTile.revealedState.toString()+" - "+setRevealedState.toString());
                return false;
            }
        }
        return true;
    }
    private static boolean ifChiiIsCalledFromLeft(List<Tile> set){
        Tile calledTile = getCalledTile(set);
        if( isChii(set)
                && calledTile!=null
                && !calledTile.winningTile
                && calledTile.calledFrom!=Tile.CalledFrom.LEFT ){
            Log.e("validateSet", "Called tile is part of a chii and wasn't called from left player: "+calledTile.toString()+" - "+calledTile.revealedState.toString()+" - "+calledTile.calledFrom.toString()+" - "+set.toString());
            return false;
        }
        return true;
    }
    private static boolean hasCalledTileIfRevealed(List<Tile> set){
        Tile calledTile = getCalledTile(set);
        Tile.RevealedState setRevealedState = getNonCalledRevealedState(set);

        if( calledTile==null ){
            switch (setRevealedState){
                case CHI:
                case PON:
                case OPENKAN:
                case ADDEDKAN:
                    Log.e("validateSet", "Cannot have a revealed Pon/Chii/OpenKan/AddedKan without a called tile: "+setRevealedState.toString());
                    return false;
            }
        }
        return true;
    }
    private static boolean isRevealedIfKan(List<Tile> set){
        Tile.RevealedState setRevealedState = getNonCalledRevealedState(set);

        if( set.size()==4 && setRevealedState==Tile.RevealedState.NONE ){
            Log.e("validateSet", "A set of 4 cannot be unrevealed: "+set.toString()+" - "+setRevealedState.toString());
            return false;
        }

        return true;
    }

    private static boolean isChii(List<Tile> set){
        return set.size()==3 && !set.get(0).value.equals(set.get(1).value);
    }
    private static Tile.RevealedState getNonCalledRevealedState(List<Tile> set){
        Tile calledTile = getCalledTile(set);
        for( Tile t : set ){
            if( t != calledTile && t.revealedState!=Tile.RevealedState.ADDEDKAN ){
                return t.revealedState;
            }
        }
        return null;
    }
    /**
     * Returns the first tile in the given list that has a CalledFrom that is not NONE
     * @param tiles List of tiles to be checked
     * @return Tile with CalledFrom!=NONE
     */
    public static Tile getCalledTile(List<Tile> tiles){
        for(Tile t : tiles){
            if( t.calledFrom!= Tile.CalledFrom.NONE ){
                return t;
            }
        }
        return null;
    }
    /**
     * Returns the first tile in the given list that has a RevealedState of ADDEDKAN
     * @param tiles List of tiles to be checked
     * @return Tile with RevealedState=ADDEDKAN
     */
    public static Tile getAddedTile(List<Tile> tiles){
        for(Tile t : tiles){
            if( t.revealedState==Tile.RevealedState.ADDEDKAN ){
                return t;
            }
        }
        return null;
    }
}
