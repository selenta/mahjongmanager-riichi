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
    public Meld(){}

    public void setTiles(List<Tile> newTiles){
        tiles.clear();
        for(Tile t : newTiles){
            addTile(t);
        }
        sort();
    }
    public void addTile(Tile t){
        tiles.add(t);
        sort();
    }
    public List<Tile> getTiles(){
        return tiles;
    }


    public Tile.RevealedState getNonCalledRevealedState(){
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
    private void sort(){
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
