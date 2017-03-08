package com.mahjongmanager.riichi.common;

import com.mahjongmanager.riichi.utils.Log;
import com.mahjongmanager.riichi.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Hand {
    public List<Tile> tiles = new ArrayList<>();
    public List<Tile> unsortedTiles = new ArrayList<>();
    public Meld pair  = new Meld();
    public Meld meld1 = new Meld();
    public Meld meld2 = new Meld();
    public Meld meld3 = new Meld();
    public Meld meld4 = new Meld();

    public Map<Yaku.Name,Integer> hanList = new HashMap<>();
    public Map<Fu.Name,Integer> fuList = new HashMap<>();

    public Tile.Wind prevailingWind = Tile.Wind.EAST;
    public Tile.Wind playerWind = Tile.Wind.WEST;

    public boolean selfDrawWinningTile = false;

    //Luck-based Yaku (These are not recorded in the actual hand/tile state)
    public boolean doubleRiichi  = false;
    public boolean riichi        = false;
    public boolean ippatsu       = false;
    public boolean rinshan       = false;
    public boolean chanKan       = false;
    public boolean haitei        = false;
    public boolean houtei        = false;
    public boolean nagashiMangan = false;

    public List<Tile> doraIndicators = new ArrayList<>();
    public List<Tile> uraDoraIndicators = new ArrayList<>();

    //Only used if we need to keep track of dora and won't be using indicators
    public int arbitraryDora = 0;

    public Hand( List<Tile> h ){
        tiles.addAll(h);
        Utils.sort(tiles);

        unsortedTiles.addAll(h);
        Utils.sort(unsortedTiles);
    }
    public Hand( Hand oldHand ){
        tiles.addAll(oldHand.tiles);
        unsortedTiles.addAll(oldHand.unsortedTiles);
        pair  = new Meld(oldHand.pair);
        meld1 = new Meld(oldHand.meld1);
        meld2 = new Meld(oldHand.meld2);
        meld3 = new Meld(oldHand.meld3);
        meld4 = new Meld(oldHand.meld4);

        for(Yaku.Name name : oldHand.hanList.keySet()){
            hanList.put(name, oldHand.hanList.get(name));
        }
        for(Fu.Name name : oldHand.fuList.keySet()){
            fuList.put(name, oldHand.fuList.get(name));
        }

        prevailingWind = oldHand.prevailingWind;
        playerWind =     oldHand.playerWind;

        selfDrawWinningTile = oldHand.selfDrawWinningTile;

        doubleRiichi  = oldHand.doubleRiichi;
        riichi        = oldHand.riichi;
        ippatsu       = oldHand.ippatsu;
        rinshan       = oldHand.rinshan;
        chanKan       = oldHand.chanKan;
        haitei        = oldHand.haitei;
        houtei        = oldHand.houtei;
        nagashiMangan = oldHand.nagashiMangan;

        doraIndicators.addAll(oldHand.doraIndicators);
        uraDoraIndicators.addAll(oldHand.uraDoraIndicators);

        arbitraryDora = oldHand.arbitraryDora;
    }

    public Tile findTile(String val, String suit){
        for( Tile tile : tiles ){
            if( tile.suit.toString().equalsIgnoreCase(suit) && tile.value.equalsIgnoreCase(val) ){
                return tile;
            }
        }
        return null;
    }
    public Tile findTile( String string ){
        switch (string){
            case "East":
                return findTile("EAST", "HONOR");
            case "South":
                return findTile("SOUTH", "HONOR");
            case "West":
                return findTile("WEST", "HONOR");
            case "North":
                return findTile("NORTH", "HONOR");
            case "White":
                return findTile("WHITE", "HONOR");
            case "Green":
                return findTile("GREEN", "HONOR");
            case "Red":
                return findTile("RED", "HONOR");

        }

        String tileSuit = string.split(" ")[1];

        tileSuit = (tileSuit.equalsIgnoreCase("Man")) ? "MANZU" : tileSuit;
        tileSuit = (tileSuit.equalsIgnoreCase("Pin")) ? "PINZU" : tileSuit;
        tileSuit = (tileSuit.equalsIgnoreCase("Sou")) ? "SOUZU" : tileSuit;

        return findTile(string.split(" ")[0], tileSuit);
    }

    public Tile popUnsortedTile(String val, Tile.Suit suit, Tile.RevealedState rState){
        for( Tile tile : unsortedTiles ){
            if( tile.suit==suit
                    && tile.value.equalsIgnoreCase(val)
                    && (rState==null || tile.revealedState==rState ) ){
                unsortedTiles.remove(tile);
                return tile;
            }
        }
        return null;
    }

    public void setMeld(List<Tile> tiles ){
        if( meld1.size()==0 ){
            meld1.setTiles(tiles);
        } else if( meld2.size()==0 ){
            meld2.setTiles(tiles);
        } else if( meld3.size()==0 ){
            meld3.setTiles(tiles);
        } else if( meld4.size()==0 ){
            meld4.setTiles(tiles);
        } else {
            Log.e("All hand melds set", "all melds: " + meld1.toString() + " "+ meld2.toString() + " "+ meld3.toString() + " "+ meld4.toString());
            Log.e("All hand melds set", "unsortedTiles: " + unsortedTiles.toString());
            Log.e("All hand melds set", "Attempted to setMeld when all melds were already full: " + tiles);
        }
    }

    public boolean addTile( Tile t ){
        if( containsMaxOfTile(t) ){
            return false;
        }

        if( !pair.getTiles().contains(t)
                && !meld1.getTiles().contains(t)
                && !meld2.getTiles().contains(t)
                && !meld3.getTiles().contains(t)
                && !meld4.getTiles().contains(t) ){
            unsortedTiles.add(t);
        }
        tiles.add(t);
        sort();
        return true;
    }
    public boolean discardTile( Tile t ){
        if( pair.getTiles().contains(t)
                || meld1.getTiles().contains(t)
                || meld2.getTiles().contains(t)
                || meld3.getTiles().contains(t)
                || meld4.getTiles().contains(t) ){
            return false;
        }
        tiles.remove(t);
        unsortedTiles.remove(t);
        sort();
        return true;
    }


    public int countHan(){
        int han = 0;
        for(Yaku.Name name : hanList.keySet()){
            han += hanList.get(name);
        }
        return han;
    }
    public int countFu(){
        int fu = 0;
        for(Fu.Name name : fuList.keySet()){
            fu += fuList.get(name);
        }
        return fu;
    }

    public boolean isOpen(){
        return isOpen(tiles);
    }
    public boolean isOpen( List<Tile> list ){
        for( Tile t : list ){
            if(t.revealedState!=Tile.RevealedState.NONE && t.revealedState!=Tile.RevealedState.CLOSEDKAN){
                return true;
            }
        }
        return false;
    }
    public boolean containsMaxOfTile( Tile tilePrime ){
        int tileCount = countTile(tilePrime);
        return (tileCount>=4);
    }
    public List<Tile> getAllUsedTiles(){
        List<Tile> usedTiles = new ArrayList<>();
        usedTiles.addAll(tiles);
        usedTiles.addAll(doraIndicators);
        usedTiles.addAll(uraDoraIndicators);
        return usedTiles;
    }
    public Meld getMeld(Tile t){
        if( pair.getTiles().contains(t) ){
            return pair;
        } else if( meld1.getTiles().contains(t) ){
            return meld1;
        } else if( meld2.getTiles().contains(t) ){
            return meld2;
        } else if( meld3.getTiles().contains(t) ){
            return meld3;
        } else if( meld4.getTiles().contains(t) ){
            return meld4;
        }
        return null;
    }
    public Meld getWinningMeld(){
        for( Tile t : pair.getTiles() ){
            if( t.winningTile ){
                return pair;
            }
        }
        for( Tile t : meld1.getTiles() ){
            if( t.winningTile ){
                return meld1;
            }
        }
        for( Tile t : meld2.getTiles() ){
            if( t.winningTile ){
                return meld2;
            }
        }
        for( Tile t : meld3.getTiles() ){
            if( t.winningTile ){
                return meld3;
            }
        }
        for( Tile t : meld4.getTiles() ){
            if( t.winningTile ){
                return meld4;
            }
        }
        return null;
    }
    public Tile getWinningTile(){
        for( Tile t : tiles ){
            if( t.winningTile ){
                return t;
            }
        }
        return null;
    }
    public int emptyMeldCount(){
        int count = 0;
        count = (meld1.size()==0) ? count + 1 : count;
        count = (meld2.size()==0) ? count + 1 : count;
        count = (meld3.size()==0) ? count + 1 : count;
        count = (meld4.size()==0) ? count + 1 : count;
        return count;
    }
    public int countTile(Tile countedTile){
        int count = 0;
        for( Tile t : tiles ){
            if( t.isSame(countedTile) ){
                count++;
            }
        }
        return count;
    }
    public int countTileComplete(Tile t){
        int count = 0;
        for(Tile tile : getAllUsedTiles()){
            if( t.isSame(tile) ){
                count++;
            }
        }
        return count;
    }
    public int countDora(){
        if( arbitraryDora!=0 ){
            return arbitraryDora;
        }

        int count = 0;
        for(int i=0; i<doraIndicators.size(); i++){
            count += countTile( doraIndicators.get(i).getNextTile() );
            if( riichi && i<uraDoraIndicators.size() ){
                count += countTile( uraDoraIndicators.get(i).getNextTile() );
            }
        }
        return count;
    }
    public boolean hasYaku(Yaku.Name name){
        return hanList.containsKey(name);
    }
    public boolean hasKan(){
        return meld1.isKan() || meld2.isKan() || meld3.isKan() || meld4.isKan();
    }
    public boolean hasAddedKan(){
        return Utils.getMeldState(meld1) == Utils.MeldState.ADDEDKAN
                || Utils.getMeldState(meld2) == Utils.MeldState.ADDEDKAN
                || Utils.getMeldState(meld3) == Utils.MeldState.ADDEDKAN
                || Utils.getMeldState(meld4) == Utils.MeldState.ADDEDKAN;
    }
    public boolean hasClosedKan(){
        return Utils.getMeldState(meld1) == Utils.MeldState.CLOSEDKAN
                || Utils.getMeldState(meld2) == Utils.MeldState.CLOSEDKAN
                || Utils.getMeldState(meld3) == Utils.MeldState.CLOSEDKAN
                || Utils.getMeldState(meld4) == Utils.MeldState.CLOSEDKAN;
    }
    public boolean hasYakuman() {
        return !hanList.isEmpty() && !Collections.disjoint(hanList.keySet(), Yaku.getYakumanNames());
    }
    public boolean hasYakuhai(){
        return hasDragonWhiteSet()
                || hasDragonGreenSet()
                || hasDragonRedSet()
                || hasPrevailingWindSet()
                || hasPlayerWindSet();
    }
    public boolean hasDragonWhiteSet(){
        return countTile(new Tile(Tile.Dragon.WHITE))>=3;
    }
    public boolean hasDragonGreenSet(){
        return countTile(new Tile(Tile.Dragon.GREEN))>=3;
    }
    public boolean hasDragonRedSet(){
        return countTile(new Tile(Tile.Dragon.RED))>=3;
    }
    public boolean hasPrevailingWindSet(){
        return countTile(new Tile(prevailingWind))>=3;
    }
    public boolean hasPlayerWindSet(){
        return countTile(new Tile(playerWind))>=3;
    }

    public boolean hasAbnormalStructure(){
        boolean hasAbYaku = !Collections.disjoint(hanList.keySet(), Yaku.getAbnormalStructureNames());
        return nagashiMangan || hasAbYaku;
    }

    public void sort(){
        Utils.sort(tiles);
        Utils.sort(unsortedTiles);
    }

    public String toString(){
        return (tiles.isEmpty()) ? "[...]" : tiles.toString();
    }
    public String toStringVerbose(){
        String s = " tiles: " + toString() + "\n";
        s += " unsortedTiles: " + unsortedTiles.toString() + "\n";
        s += " pair: " + pair.toStringVerbose() + "\n";
        s += " meld1: " + meld1.toStringVerbose() + "\n";
        s += " meld2: " + meld2.toStringVerbose() + "\n";
        s += " meld3: " + meld3.toStringVerbose() + "\n";
        s += " meld4: " + meld4.toStringVerbose() + "\n";
        String wTile = (getWinningTile()==null) ? "null" : getWinningTile().toString();
        s += " winningTile: " + wTile + "\n";
        s += " hanList: " + hanList.toString() + "\n";
        s += " fuList: " + fuList.toString() + "\n";
        return s;
    }
}
