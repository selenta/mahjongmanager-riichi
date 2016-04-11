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
import android.util.Log;
import android.widget.TextView;

import java.util.List;

public class Utils {
    MainActivity activity;
    public Utils(MainActivity ma){
        activity = ma;
    }

    public enum SetState {
        INVALID, CLOSEDSET, OPENCHII, OPENPON, OPENKAN, ADDEDKAN, CLOSEDKAN
    }

    public TextView getTileView(Tile t){
        if( activity==null ){
            //This should never happen
            return null;
        }
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
        if( !t.faceDown ){      // TODO what is going on here? why does this make border disappear?
            int tilePadding = 8;
            layerDrawable.setLayerInset(1, tilePadding,tilePadding,tilePadding,tilePadding);
        }

        TextView view = new TextView(activity);
        view.setBackground(layerDrawable);

        return view;
    }

    public static SetState getSetState(List<Tile> set){
        // TODO remove validate step? Seems expensive, probably not necessary
        if( !validateSet(set) ){
            return SetState.INVALID;
        }

        Tile firstTile = set.get(0);
        if( firstTile.revealedState==Tile.RevealedState.CHI ){
            return SetState.OPENCHII;
        } else if( firstTile.revealedState==Tile.RevealedState.PON ){
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

    public static Tile getCalledTile(List<Tile> tiles){
        for(Tile t : tiles){
            if( t.calledFrom!= Tile.CalledFrom.NONE ){
                return t;
            }
        }
        return null;
    }
    public static Tile getAddedTile(List<Tile> tiles){
        for(Tile t : tiles){
            if( t.revealedState==Tile.RevealedState.ADDEDKAN ){
                return t;
            }
        }
        return null;
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
        Tile calledTile = null;
        for( Tile t : s ){
            if( calledTile==null && t.calledFrom!=Tile.CalledFrom.NONE ){
                calledTile = t;
            } else if( calledTile!=null && t.calledFrom!=Tile.CalledFrom.NONE ){
                Log.e("validateSet", "More than one tile with a calledFrom status: " + calledTile.toString() + " + " + t.toString());
                return false;
            }
        }
        Tile.RevealedState otherTileRevealedState = null;
        for( Tile t : s ){
            if( otherTileRevealedState==null && t!=calledTile ){
                otherTileRevealedState = t.revealedState;
            } else if( otherTileRevealedState!=t.revealedState && t!=calledTile ){
                Log.e("validateSet", "(Non-called)Tiles do not match revealed states: " + otherTileRevealedState.toString() + " - " + t.revealedState.toString());
                return false;
            }
        }
        if( otherTileRevealedState==null ){
            Log.e("validateSet", "Tiles' revealedState is not set... this should not be possible");
            return false;
        }


        if( calledTile==null ){
            switch (otherTileRevealedState){
                case CHI:
                case PON:
                case OPENKAN:
                case ADDEDKAN:
                    Log.e("validateSet", "Cannot have a revealed Pon/Chii/OpenKan/AddedKan without a called tile: "+otherTileRevealedState.toString());
                    return false;
            }
        } else {
            switch (calledTile.revealedState){
                case NONE:
                    if( s.size()==4 ){
                        Log.e("validateSet", "Called tile can't be part of 4 set and be closed: "+calledTile.toString()+" - "+calledTile.revealedState.toString());
                        return false;
                    } else if( !calledTile.winningTile ){
                        Log.e("validateSet", "Called tile can't be in a revealed state of NONE unless it is a winning tile: "+calledTile.toString()+" - "+calledTile.revealedState.toString());
                        return false;
                    }
                    break;
                case CHI:
                    if( calledTile.calledFrom!=Tile.CalledFrom.LEFT ){
                        Log.e("validateSet", "Called tile is part of a run and wasn't called from left player: "+calledTile.toString()+" - "+calledTile.revealedState.toString()+" - "+calledTile.calledFrom.toString());
                        return false;
                    }
                case PON:
                    if( s.size()!=3 ){
                        Log.e("validateSet", "Called Chii/Pon must be a set of size 3: "+calledTile.toString()+" - "+calledTile.revealedState.toString()+" - "+s.toString());
                        return false;
                    }
                    break;
                case CLOSEDKAN:
                    Log.e("validateSet", "A called tile cannot be part of a closed kan: "+calledTile.toString()+" - "+calledTile.revealedState.toString());
                    return false;
                case OPENKAN:
                    if( s.size()!=4 ){
                        Log.e("validateSet", "Called OpenKan must be a set of size 4: "+calledTile.toString()+" - "+calledTile.revealedState.toString()+" - "+s.toString());
                        return false;
                    }
                    break;
                case ADDEDKAN:
                    if( otherTileRevealedState!=Tile.RevealedState.PON ){
                        Log.e("validateSet", "Called tile is AddedKan, the rest of the meld is not of status PON: "+otherTileRevealedState.toString());
                        return false;
                    }
                    break;
            }
        }

        if( s.size()==4 && otherTileRevealedState==Tile.RevealedState.NONE ){
            Log.e("validateSet", "A set of 4 cannot be unrevealed: "+s.toString()+" - "+otherTileRevealedState.toString());
            return false;
        }

        return true;
    }
}
