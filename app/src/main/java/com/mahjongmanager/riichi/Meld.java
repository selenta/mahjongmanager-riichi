package com.mahjongmanager.riichi;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class Meld {
    public List<Tile> tiles = new ArrayList<>();

    public Meld(Meld oldM){
        tiles.addAll(oldM.tiles);
        Utils.sort(tiles);
    }
    public Meld(List<Tile> startingTiles){
        tiles.addAll(startingTiles);
        Utils.sort(tiles);
    }
    public Meld(){}

    public void addTile(Tile t){
        tiles.add(t);
        Utils.sort(tiles);
    }
    public void setTiles(List<Tile> newTiles){
        tiles.clear();
        tiles.addAll(newTiles);
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
     * TODO verify that all the things I said are being done are being done
     *
     * @return Boolean that indicates whether the list of tiles is a valid set in Mahjong
     */
    public boolean validate(){
        if( tiles.size()<2 || tiles.size()>4 ){
            Log.e("validateSet", "Too many, or too few, tiles: " + tiles.toString());
            return false;
        }

        if( !validateUniqueCalledTile() ){
            return false;
        } else if( !validateUniqueAddedTile() ){
            return false;
        }

        if( tiles.size()==2 ){
            return validatePair();
        }

        return validateRevealedStates();
    }
    private boolean validatePair(){
        if( !tiles.get(0).value.equals(tiles.get(1).value) ){
            Log.e("validateSet", "It's not a pair if the two tiles aren't the same stupid!: "+toString());
            return false;
        }
        Tile calledTile = getCalledTile();
        if( calledTile!=null && !calledTile.winningTile ){
            Log.e("validateSet", "A called tile in a pair can only be called if it's the winning tile: "+calledTile.toString());
            return false;
        }
        Tile.RevealedState setRevealedState = getNonCalledRevealedState();
        if( setRevealedState!= Tile.RevealedState.NONE ){
            Log.e("validateSet", "At least one of the tiles in the pair has to be concealed: "+setRevealedState.toString());
            return false;
        }
        return true;
    }
    private boolean validateUniqueCalledTile(){
        Tile calledTile = getCalledTile();
        for(Tile t : tiles){
            if( t!=calledTile && t.calledFrom!=Tile.CalledFrom.NONE ){
                Log.e("validateSet", "More than one tile with a calledFrom status: " + calledTile.toString() + " + " + t.toString());
                return false;
            }
        }
        return true;
    }
    private boolean validateUniqueAddedTile(){
        Tile addedTile = getAddedTile();
        for(Tile t : tiles){
            if( t!=addedTile && t.revealedState== Tile.RevealedState.ADDEDKAN ){
                Log.e("validateSet", "More than one tile with a addedTile status: " + addedTile.toString() + " + " + t.toString());
                return false;
            }
        }
        return true;
    }
    private boolean validateRevealedStates(){
        // Validate revealed state
        //      All non-called tiles match (except in case of AddedKan)
        //      State makes sense based on number of tiles in the set
        //      Called tile is non-conflicting with the other tiles' state
        //      Other tiles' states are non-conflicting with called tile's state
        // CalledTile for Chii comes from left player
        return !(!nonCalledTilesMatchRevealedState()
                || !revealedStateMatchesTileCount()
                || !consistentCalledTileState()
                || !ifChiiIsCalledFromLeft()
                || !hasCalledTileIfRevealed()
                || !isRevealedIfKan());

    }
    private boolean nonCalledTilesMatchRevealedState(){
        Tile calledTile = getCalledTile();
        Tile.RevealedState setRevealedState = getNonCalledRevealedState();

        for( Tile t : tiles ){
            if( t != calledTile
                    && !t.winningTile
                    && setRevealedState != t.revealedState
                    && t.revealedState != Tile.RevealedState.ADDEDKAN ){
                Log.e("validateSet", "(Non-called)Tiles do not match revealed states: " + setRevealedState.toString() + " - " + t.revealedState.toString()+" - "+toString());
                return false;
            }
        }
        return true;
    }
    private boolean revealedStateMatchesTileCount(){
        Tile.RevealedState setRevealedState = getNonCalledRevealedState();

        if( size()==3 ){
            if( setRevealedState == Tile.RevealedState.CLOSEDKAN || setRevealedState == Tile.RevealedState.OPENKAN ){
                Log.e("validateSet", "ClosedKan/OpenKan must be a set of size 4: "+toString());
                return false;
            }
        } else {
            if( setRevealedState == Tile.RevealedState.CHI || setRevealedState == Tile.RevealedState.NONE ){
                Log.e("validateSet", "Unrevealed sets must be a set of size 3: "+setRevealedState.toString()+" - "+toString());
                return false;
            }
        }
        return true;
    }
    private boolean consistentCalledTileState(){
        Tile calledTile = getCalledTile();
        if( calledTile!=null ){
            Tile addedTile = getAddedTile();
            Tile.RevealedState setRevealedState = getNonCalledRevealedState();

            switch (calledTile.revealedState){
                case NONE:
                    if( !calledTile.winningTile ){
                        Log.e("validateSet", "Called tile can't be in a revealed state of NONE unless it is a winning tile: "+calledTile.toString()+" - "+calledTile.revealedState.toString());
                        return false;
                    }
                    break;
                case PON:
                    if( tiles.size()!=3 && addedTile==null ){
                        Log.e("validateSet", "Called Chii/Pon must be a set of size 3 unless part of AddedKan: "+calledTile.toString()+" - "+calledTile.revealedState.toString()+" - "+toString());
                        return false;
                    }
                    break;
                case CLOSEDKAN:
                    Log.e("validateSet", "A called tile cannot be part of a closed kan: "+calledTile.toString()+" - "+calledTile.revealedState.toString());
                    return false;
                case ADDEDKAN:
                    Log.e("validateSet", "Called tile cannot be AddedKan: "+tiles.toString());
                    return false;
            }

            if( calledTile.revealedState!=setRevealedState && calledTile.revealedState!=Tile.RevealedState.NONE ){
                Log.e("validateSet", "CalledTile state does not match the setState of the rest of the tiles: "+calledTile.revealedState.toString()+" - "+setRevealedState.toString());
                return false;
            }
        }
        return true;
    }
    private boolean ifChiiIsCalledFromLeft(){
        Tile calledTile = getCalledTile();
        if( isChii()
                && calledTile!=null
                && !calledTile.winningTile
                && calledTile.calledFrom!=Tile.CalledFrom.LEFT ){
            Log.e("validateSet", "Called tile is part of a chii and wasn't called from left player: "+calledTile.toString()+" - "+calledTile.revealedState.toString()+" - "+calledTile.calledFrom.toString()+" - "+toString());
            return false;
        }
        return true;
    }
    private boolean hasCalledTileIfRevealed(){
        Tile calledTile = getCalledTile();
        Tile.RevealedState setRevealedState = getNonCalledRevealedState();

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
    private boolean isRevealedIfKan(){
        Tile.RevealedState setRevealedState = getNonCalledRevealedState();

        if( tiles.size()==4 && setRevealedState==Tile.RevealedState.NONE ){
            Log.e("validateSet", "A set of 4 cannot be unrevealed: "+tiles.toString()+" - "+setRevealedState.toString());
            return false;
        }

        return true;
    }

    private Tile.RevealedState getNonCalledRevealedState(){
        for( Tile t : tiles ){
            if( t != getCalledTile() && t.revealedState!=Tile.RevealedState.ADDEDKAN ){
                return t.revealedState;
            }
        }
        return null;
    }
    /**
     * Returns the first tile in the given list that has a CalledFrom that is not NONE
     * @return Tile with CalledFrom!=NONE
     */
    public Tile getCalledTile(){
        for(Tile t : tiles){
            if( t.calledFrom!= Tile.CalledFrom.NONE ){
                return t;
            }
        }
        return null;
    }
    /**
     * Returns the first tile in the given list that has a RevealedState of ADDEDKAN
     * @return Tile with RevealedState=ADDEDKAN
     */
    public Tile getAddedTile(){
        for(Tile t : tiles){
            if( t.revealedState==Tile.RevealedState.ADDEDKAN ){
                return t;
            }
        }
        return null;
    }
    public Tile getRandomTile(){
        return tiles.get(new Random().nextInt(tiles.size()));
    }


    //////////////////////////////////////////////////////////////////////
    /////////////////////       Utility Methods      /////////////////////
    //////////////////////////////////////////////////////////////////////
    public int size(){ return tiles.size(); }
    public boolean isPair(){ return tiles.size()==2 && tiles.get(0).value.equals(tiles.get(1).value); }
    public boolean isChii(){ return tiles.size()==3 && !tiles.get(0).value.equals(tiles.get(1).value); }
    public boolean isKan(){ return tiles.size()==4; }
    public Tile.Suit getSuit(){ return tiles.get(0).suit; }

    public boolean isOpenOrWinningTile(){
        for( Tile t : tiles ){
            if( (t.revealedState!=Tile.RevealedState.NONE && t.revealedState!=Tile.RevealedState.CLOSEDKAN)
                    || t.calledFrom!=Tile.CalledFrom.NONE ){
                return true;
            }
        }
        return false;
    }
    public String toString(){ return tiles.toString(); }
}
