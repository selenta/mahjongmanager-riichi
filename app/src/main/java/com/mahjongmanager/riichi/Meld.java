package com.mahjongmanager.riichi;

import com.mahjongmanager.riichi.utils.Log;
import com.mahjongmanager.riichi.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Meld {
    private List<Tile> tiles = new ArrayList<>();

    public Meld(Meld oldM){
        tiles.addAll(oldM.tiles);
        sort();
    }
    public Meld(List<Tile> startingTiles){
        tiles.addAll(startingTiles);
        sort();
    }
    public Meld(){}

    public void setTiles(List<Tile> newTiles){
        tiles.clear();
        for(Tile t : newTiles){
            addTile(t);
        }
    }
    public void addTile(Tile t){
        tiles.add(t);
        Utils.sort(tiles);
    }
    public List<Tile> getTiles(){
        return tiles;
    }

    /**
     * Check whether the meld of tiles is a valid, internally consistent, set of tiles
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
     * @return Boolean that indicates whether the list of tiles is a valid meld in Mahjong
     */
    public boolean validate(){
        if( tiles.size()<2 || tiles.size()>4 ){
            Log.e("validateMeld", "Too many, or too few, tiles: " + tiles.toString());
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
            Log.e("validateMeld", "It's not a pair if the two tiles aren't the same stupid!: "+toString());
            return false;
        }
        Tile calledTile = getCalledTile();
        if( calledTile!=null && !calledTile.winningTile ){
            Log.e("validateMeld", "A called tile in a pair can only be called if it's the winning tile: "+calledTile.toString());
            return false;
        }
        Tile.RevealedState meldRevealedState = getNonCalledRevealedState();
        if( meldRevealedState!= Tile.RevealedState.NONE ){
            Log.e("validateMeld", "At least one of the tiles in the pair has to be concealed: "+meldRevealedState.toString());
            return false;
        }
        return true;
    }
    private boolean validateUniqueCalledTile(){
        Tile calledTile = getCalledTile();
        for(Tile t : tiles){
            if( t!=calledTile && t.calledFrom!=Tile.CalledFrom.NONE ){
                Log.e("validateMeld", "More than one tile with a calledFrom status: " + calledTile.toString() + " + " + t.toString());
                return false;
            }
        }
        return true;
    }
    private boolean validateUniqueAddedTile(){
        Tile addedTile = getAddedTile();
        for(Tile t : tiles){
            if( t!=addedTile && t.revealedState== Tile.RevealedState.ADDEDKAN ){
                Log.e("validateMeld", "More than one tile with a addedTile status: " + addedTile.toString() + " + " + t.toString());
                return false;
            }
        }
        return true;
    }
    private boolean validateRevealedStates(){
        // Validate revealed state
        //      All non-called tiles match (except in case of AddedKan)
        //      State makes sense based on number of tiles in the meld
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
        Tile.RevealedState meldRevealedState = getNonCalledRevealedState();

        for( Tile t : tiles ){
            if( t != calledTile
                    && !t.winningTile
                    && meldRevealedState != t.revealedState
                    && t.revealedState != Tile.RevealedState.ADDEDKAN ){
                Log.e("validateMeld", "(Non-called)Tiles do not match revealed states: " + meldRevealedState.toString() + " - " + t.revealedState.toString()+" - "+toString());
                return false;
            }
        }
        return true;
    }
    private boolean revealedStateMatchesTileCount(){
        Tile.RevealedState meldRevealedState = getNonCalledRevealedState();

        if( size()==3 ){
            if( meldRevealedState == Tile.RevealedState.CLOSEDKAN || meldRevealedState == Tile.RevealedState.OPENKAN ){
                Log.e("validateMeld", "ClosedKan/OpenKan must be of size 4: "+toString());
                return false;
            }
        } else {
            if( meldRevealedState == Tile.RevealedState.CHI || meldRevealedState == Tile.RevealedState.NONE ){
                Log.e("validateMeld", "Unrevealed melds must be of size 3: "+meldRevealedState.toString()+" - "+toString());
                return false;
            }
        }
        return true;
    }
    private boolean consistentCalledTileState(){
        Tile calledTile = getCalledTile();
        if( calledTile!=null ){
            Tile addedTile = getAddedTile();
            Tile.RevealedState meldRevealedState = getNonCalledRevealedState();

            switch (calledTile.revealedState){
                case NONE:
                    if( !calledTile.winningTile ){
                        Log.e("validateMeld", "Called tile can't be in a revealed state of NONE unless it is a winning tile: "+calledTile.toString()+" - "+calledTile.revealedState.toString());
                        return false;
                    }
                    break;
                case PON:
                    if( tiles.size()!=3 && addedTile==null ){
                        Log.e("validateMeld", "Called Chii/Pon must be of size 3 unless part of AddedKan: "+calledTile.toString()+" - "+calledTile.revealedState.toString()+" - "+toString());
                        return false;
                    }
                    break;
                case CLOSEDKAN:
                    Log.e("validateMeld", "A called tile cannot be part of a closed kan: "+calledTile.toString()+" - "+calledTile.revealedState.toString());
                    return false;
                case ADDEDKAN:
                    Log.e("validateMeld", "Called tile cannot be AddedKan: "+tiles.toString());
                    return false;
            }

            if( calledTile.revealedState!=meldRevealedState && calledTile.revealedState!=Tile.RevealedState.NONE ){
                Log.e("validateMeld", "CalledTile state does not match the meldState of the rest of the tiles: "+calledTile.revealedState.toString()+" - "+meldRevealedState.toString());
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
            Log.e("validateMeld", "Called tile is part of a chii and wasn't called from left player: "+calledTile.toString()+" - "+calledTile.revealedState.toString()+" - "+calledTile.calledFrom.toString()+" - "+toString());
            return false;
        }
        return true;
    }
    private boolean hasCalledTileIfRevealed(){
        Tile calledTile = getCalledTile();
        Tile.RevealedState meldRevealedState = getNonCalledRevealedState();

        if( calledTile==null ){
            switch (meldRevealedState){
                case CHI:
                case PON:
                case OPENKAN:
                case ADDEDKAN:
                    Log.e("validateMeld", "Cannot have a revealed Pon/Chii/OpenKan/AddedKan without a called tile: "+meldRevealedState.toString());
                    return false;
            }
        }
        return true;
    }
    private boolean isRevealedIfKan(){
        Tile.RevealedState meldRevealedState = getNonCalledRevealedState();

        if( tiles.size()==4 && meldRevealedState==Tile.RevealedState.NONE ){
            Log.e("validateMeld", "A set of 4 cannot be unrevealed: "+tiles.toString()+" - "+meldRevealedState.toString());
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
    public Tile firstTile(){
        if( tiles.size()>0 ){
            return tiles.get(0);
        }
        return null;
    }
    public Tile secondTile(){
        if( tiles.size()>1 ){
            return tiles.get(1);
        }
        return null;
    }
    public Tile thirdTile(){
        if( tiles.size()>2 ){
            return tiles.get(2);
        }
        return null;
    }
    public Tile fourthTile(){
        if( tiles.size()>3 ){
            return tiles.get(3);
        }
        return null;
    }


    //////////////////////////////////////////////////////////////////////
    /////////////////////       Utility Methods      /////////////////////
    //////////////////////////////////////////////////////////////////////
    public void sort(){
        Utils.sort(tiles);
    }
    public int size(){ return tiles.size(); }
    public boolean isPair(){ return tiles.size()==2 && tiles.get(0).value.equals(tiles.get(1).value); }
    public boolean isChii(){ return tiles.size()==3 && !tiles.get(0).value.equals(tiles.get(1).value); }
    public boolean isKan(){ return tiles.size()==4; }
    public Tile.Suit getSuit(){ return tiles.get(0).suit; }

    public boolean isClosed(){
        for( Tile t : tiles ){
            if( (t.revealedState !=Tile.RevealedState.NONE && t.revealedState !=Tile.RevealedState.CLOSEDKAN)
                    || t.calledFrom !=Tile.CalledFrom.NONE ){
                return false;
            }
        }
        return true;
    }
    public boolean hasWinningTile(){
        for( Tile t : tiles ){
            if(t.winningTile){
                return true;
            }
        }
        return false;
    }
    public String toString(){ return tiles.toString(); }
    public String toStringVerbose(){
        String s = tiles.toString();
        if( getNonCalledRevealedState()!=null ){
            s = s+" "+getNonCalledRevealedState().toString();
        }
        if( getCalledTile()!=null ){
            s = s+" "+getCalledTile().calledFrom.toString();
        }
        return s;
    }
}
