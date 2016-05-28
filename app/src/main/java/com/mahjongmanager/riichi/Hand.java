package com.mahjongmanager.riichi;

import android.util.Log;

import com.mahjongmanager.riichi.utils.Utils;

import java.util.ArrayList;
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

    public Integer han = 0;
    public Integer fu = 0;

    public Map<Yaku.Name,Integer> hanList = new HashMap<>();
    public Map<String,Integer> fuList = new HashMap<>();

    public Tile.Wind prevailingWind = Tile.Wind.EAST;
    public Tile.Wind playerWind = Tile.Wind.WEST;

    public Boolean selfDrawWinningTile = false;

    //Yaku
    public Boolean doubleRiichi = false;
    public Boolean riichi = false;
    public Boolean ippatsu = false;
    public Boolean tsumo = false;
    public Boolean rinshan = false;
    public Boolean chanKan = false;
    public Boolean haitei = false;
    public Boolean houtei = false;
    public Boolean chiiToitsu = false;
    public Boolean nagashiMangan = false;
    public Boolean pinfu = false;
    public Boolean iipeikou = false;
    public Boolean sanshokuDoujun = false;
    public Boolean ittsuu = false;
    public Boolean ryanpeikou = false;
    public Boolean toitoi = false;
    public Boolean sanAnkou = false;
    public Boolean sanshokuDoukou = false;
    public Boolean sanKantsu = false;
    public Boolean tanyao = false;
    public Boolean whiteDragon = false;
    public Boolean greenDragon = false;
    public Boolean redDragon = false;
    public Boolean roundWind = false;
    public Boolean seatWind = false;
    public Boolean chanta = false;
    public Boolean junchan = false;
    public Boolean honroutou = false;
    public Boolean shousangen = false;
    public Boolean honitsu = false;
    public Boolean chinitsu = false;
    public Boolean kokushiMusou = false;
    public Boolean kokushiMusou13wait = false;
    public Boolean suuAnkou = false;
    public Boolean suuAnkouTanki = false;
    public Boolean daisangen = false;
    public Boolean shousuushii = false;
    public Boolean daisuushii = false;
    public Boolean tsuuiisou = false;
    public Boolean daichisei = false;
    public Boolean chinroutou = false;
    public Boolean ryuuiisou = false;
    public Boolean chuurenPoutou = false;
    public Boolean chuurenPoutou9wait = false;
    public Boolean suuKantsu = false;
    public Boolean sanrenkou = false;
    public Boolean suurenkou = false;
    public Boolean daiSharin = false;
    public Boolean shiisanpuuta = false;
    public Boolean shiisuupuuta = false;
    public Boolean parenchan = false;
    public Integer dora = 0;

    public Tile doraIndicator1 = null;
    public Tile doraIndicator2 = null;
    public Tile doraIndicator3 = null;
    public Tile doraIndicator4 = null;

    public Tile uraDoraIndicator1 = null;
    public Tile uraDoraIndicator2 = null;
    public Tile uraDoraIndicator3 = null;
    public Tile uraDoraIndicator4 = null;

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

        han = oldHand.han;
        fu  = oldHand.fu;

        prevailingWind = oldHand.prevailingWind;
        playerWind =     oldHand.playerWind;

        selfDrawWinningTile = oldHand.selfDrawWinningTile;

        riichi =         oldHand.riichi;
        chiiToitsu =     oldHand.chiiToitsu;
        nagashiMangan =  oldHand.nagashiMangan;
        tsumo =          oldHand.tsumo;
        ippatsu =        oldHand.ippatsu;
        haitei =         oldHand.haitei;
        houtei =         oldHand.houtei;
        rinshan =        oldHand.rinshan;
        chanKan =        oldHand.chanKan;
        doubleRiichi =   oldHand.doubleRiichi;
        pinfu =          oldHand.pinfu;
        iipeikou =       oldHand.iipeikou;
        sanshokuDoujun = oldHand.sanshokuDoujun;
        ittsuu =         oldHand.ittsuu;
        ryanpeikou =     oldHand.ryanpeikou;
        toitoi =         oldHand.toitoi;
        sanAnkou =       oldHand.sanAnkou;
        sanshokuDoukou = oldHand.sanshokuDoukou;
        sanKantsu =      oldHand.sanKantsu;
        tanyao =         oldHand.tanyao;
        whiteDragon =    oldHand.whiteDragon;
        greenDragon =    oldHand.greenDragon;
        redDragon =      oldHand.redDragon;
        roundWind =      oldHand.roundWind;
        seatWind =       oldHand.seatWind;
        chanta =         oldHand.chanta;
        junchan =        oldHand.junchan;
        honroutou =      oldHand.honroutou;
        shousangen =     oldHand.shousangen;
        honitsu =        oldHand.honitsu;
        chinitsu =       oldHand.chinitsu;
        kokushiMusou =   oldHand.kokushiMusou;
        suuAnkou =       oldHand.suuAnkou;
        daisangen =      oldHand.daisangen;
        shousuushii =    oldHand.shousuushii;
        daisuushii =     oldHand.daisuushii;
        tsuuiisou =      oldHand.tsuuiisou;
        daichisei =      oldHand.daichisei;
        chinroutou =     oldHand.chinroutou;
        ryuuiisou =      oldHand.ryuuiisou;
        chuurenPoutou =  oldHand.chuurenPoutou;
        suuKantsu =      oldHand.suuKantsu;
        sanrenkou =      oldHand.sanrenkou;
        suurenkou =      oldHand.suurenkou;
        daiSharin =      oldHand.daiSharin;
        shiisanpuuta =   oldHand.shiisanpuuta;
        shiisuupuuta =   oldHand.shiisuupuuta;
        parenchan =      oldHand.parenchan;
        dora =           oldHand.dora;

        doraIndicator1 = oldHand.doraIndicator1;
        doraIndicator2 = oldHand.doraIndicator2;
        doraIndicator3 = oldHand.doraIndicator3;
        doraIndicator4 = oldHand.doraIndicator4;

        uraDoraIndicator1 = oldHand.uraDoraIndicator1;
        uraDoraIndicator2 = oldHand.uraDoraIndicator2;
        uraDoraIndicator3 = oldHand.uraDoraIndicator3;
        uraDoraIndicator4 = oldHand.uraDoraIndicator4;
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


    // Verify hand consistency, including:
    //TODO consider other inherently contradictory yaku conditions
    //TODO Chiitoitsu/kokushi/nagashi don't work with most things
    // Good spreadsheet to use as reference: http://arcturus.su/wiki/Yaku_compatability
    public boolean validateCompleteState(){
        if( unsortedTiles.size()!=0 ){
            Log.e("validateCompleteState", "unsortedTiles is not empty: "+unsortedTiles.toString());
            return false;
        } else if( tiles.size()<14 || tiles.size()>18 ){
            Log.e("validateCompleteState", "too few (or too many) total number of tiles: "+tiles.toString());
            return false;
        }

        if( !(validateNotTooManyTiles()
                && validateOnlyOneWinningTile()
                && validateEachTile()
                && validateRealScore()
                && validateHaiteiHoutei()
                && validateChanKan()
                && validateRinshan()
                && validateYakuWithOpenState()) ){
            return false;
        }

        if( !hasAbnormalStructure() &&
                (!validateNoMissingTiles()
                        || !validateAllMelds()
                        || !validateWinningTileNotInKan() )){
            return false;
        }
        return true;
    }
    private boolean validateNotTooManyTiles(){
        for( Tile t : tiles ){
            int tCount = 0;
            for( Tile tNested : tiles ){
                if( t.toString().equals(tNested.toString()) ){
                    tCount++;
                }
            }
            if(tCount>4){
                Log.e("validateCompleteState", "hand contains too many copies of this tile: "+tCount+"x "+t.toString());
                return false;
            }
        }
        return true;
    }
    private boolean validateOnlyOneWinningTile(){
        //There is exactly one winning tile
        Tile winningTile = null;
        for( Tile t : tiles ){
            if( winningTile==null && t.winningTile ){
                winningTile = t;
            } else if( winningTile!=null && t.winningTile ){
                Log.e("validateCompleteState", "More than one winning tile present: "+winningTile.toString()+" + "+t.toString());
                return false;
            }
        }
        return true;
    }
    private boolean validateEachTile(){
        //verify each tile
        for(Tile t : tiles ){
            if( !t.validateTile() ){
                Log.e("validateCompleteState", "Tile is in invalid state: "+t.toString());
                return false;
            }
        }
        return true;
    }
    private boolean validateRealScore(){
        //validate that score is not impossible (e.g. 1 han 20 fu)
        if( (han==1&&fu==20) || (han==1&&fu==25) || (han==2&&fu==25&&tsumo) ){
            Log.e("validateCompleteState", "Impossible score: han-"+han.toString()+" fu-"+fu.toString()+" tsumo-"+tsumo.toString()+" - "+toStringVerbose());
            return false;
        }
        return true;
    }
    private boolean validateHaiteiHoutei(){
        if( haitei && (!selfDrawWinningTile || houtei || rinshan || chanKan ) ){
            Log.e("validateHand", "Cannot have haitei without selfDraw or with houtei/rinshan/chankan: "+selfDrawWinningTile+" - "+houtei+" - "+rinshan+" - "+chanKan+" - "+toStringVerbose());
            return false;
        }
        if( houtei && (selfDrawWinningTile || ippatsu || tsumo || rinshan || chanKan ) ){
            Log.e("validateHand", "Cannot have houtei with selfDraw or ippatsu/tsumo/rinshan/chankan: "+selfDrawWinningTile+" - "+ippatsu+" - "+tsumo+" - "+rinshan+" - "+chanKan+" - "+toStringVerbose());
            return false;
        }
        return true;
    }
    private boolean validateChanKan(){
        if( chanKan ){
            int wTileCount = (getWinningTile()==null) ? 0 : countTile(getWinningTile());
            if( wTileCount!=1 || selfDrawWinningTile || tsumo || rinshan ){
                Log.e("validateHand", "Cannot have chanKan with more than one copy of winning tile in hand (or with selfDraw/tsumo/rinshan): "+wTileCount+" "+getWinningTile()+" - "+selfDrawWinningTile+" - "+tsumo+" - "+rinshan+" - "+toStringVerbose());
                return false;
            }
        }
        return true;
    }
    private boolean validateRinshan(){
        if( rinshan && (!hasKan() || !selfDrawWinningTile) ){
            Log.e("validateCompleteState", "Hand must contain a Kan to win with rinshan: "+toStringVerbose());
            return false;
        }
        return true;
    }
    private boolean validateYakuWithOpenState(){
        if( isOpen() && (riichi || tsumo || ippatsu || doubleRiichi || pinfu || iipeikou ) ){
            Log.e("validateCompleteState", "Open hand has a conflicting yaku: "+toStringVerbose());
            return false;
        }
        return true;
    }
    private boolean validateNoMissingTiles(){
        if( tiles.size()!=(pair.size()+meld1.size()+meld2.size()+meld3.size()+meld4.size()) ){
            Log.e("validateCompleteState", "(1/2) Tile counts don't match! tiles: "+unsortedTiles.toString());
            Log.e("validateCompleteState", "(2/2) Tile counts don't match! melds: "+ printAllMelds());
            return false;
        }
        return true;
    }
    private boolean validateAllMelds(){
        //All tiles in each melds match states
        if( !pair.validate()
                || !meld1.validate()
                || !meld2.validate()
                || !meld3.validate()
                || !meld4.validate() ){
            Log.e("validateHand", "Something went wrong, here's the whole hand: "+toStringVerbose());
            return false;
        }
        return true;
    }
    private boolean validateWinningTileNotInKan(){
        Meld winningMeld = getWinningMeld();
        if( winningMeld!=null && winningMeld.isKan() ){
            Log.e("validateHand", "WinningTile cannot be part of a kan: "+toStringVerbose());
            return false;
        }
        return true;
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
        if( doraIndicator1!=null ){
            usedTiles.add(doraIndicator1);
        }
        if( doraIndicator2!=null ){
            usedTiles.add(doraIndicator2);
        }
        if( doraIndicator3!=null ){
            usedTiles.add(doraIndicator3);
        }
        if( doraIndicator4!=null ){
            usedTiles.add(doraIndicator4);
        }
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
            if( t.toString().equals(countedTile.toString()) ){
                count++;
            }
        }
        return count;
    }
    public boolean hasKan(){
        return meld1.isKan() || meld2.isKan() || meld3.isKan() || meld4.isKan();
    }
    public boolean hasYakuman(){
        return ( kokushiMusou || suuAnkou      || daisangen || shousuushii
                || daisuushii || tsuuiisou     || daichisei || chinroutou
                || ryuuiisou  || chuurenPoutou || suuKantsu
                || suuAnkouTanki || kokushiMusou13wait || chuurenPoutou9wait);
    }
    public boolean hasYakuhai(){
        return hasDragonWhiteSet()
                || hasDragonGreenSet()
                || hasDragonRedSet()
                || hasPrevailingWindSet()
                || hasPlayerWindSet();
    }
    public boolean hasDragonWhiteSet(){
        return countTile(new Tile("White", "HONOR"))>=3;
    }
    public boolean hasDragonGreenSet(){
        return countTile(new Tile("Green", "HONOR"))>=3;
    }
    public boolean hasDragonRedSet(){
        return countTile(new Tile("Red", "HONOR"))>=3;
    }
    public boolean hasPrevailingWindSet(){
        return countTile(new Tile(prevailingWind.toString(), "HONOR"))>=3;
    }
    public boolean hasPlayerWindSet(){
        return countTile(new Tile(playerWind.toString(), "HONOR"))>=3;
    }

    public boolean hasAbnormalStructure(){
        return kokushiMusou || kokushiMusou13wait || chiiToitsu || daichisei || nagashiMangan;
    }
    public boolean tilesSortedProperly(){
        int usedTiles = meld1.size() + meld2.size() + meld3.size() + meld4.size() + pair.size() + unsortedTiles.size();
        if( usedTiles != tiles.size() ){
            Log.wtf("InvalidHandState", "tiles list size does not match the number of tiles used in melds/unsortedTiles: "+usedTiles+" "+tiles.size()+"\n"+toStringVerbose());
            return false;
        }
        return true;
    }

    public void sort(){
        Utils.sort(tiles);
        Utils.sort(unsortedTiles);
        pair.sort();
        meld1.sort();
        meld2.sort();
        meld3.sort();
        meld4.sort();
    }

    public String toString(){
        return (tiles.isEmpty()) ? "[...]" : tiles.toString();
    }
    public String toStringVerbose(){
        String s = " tiles: " + toString() + "\n";
        s = s + " unsortedTiles: " + unsortedTiles.toString() + "\n";
        s = s + " pair: " + pair.toStringVerbose() + "\n";
        s = s + " meld1: " + meld1.toStringVerbose() + "\n";
        s = s + " meld2: " + meld2.toStringVerbose() + "\n";
        s = s + " meld3: " + meld3.toStringVerbose() + "\n";
        s = s + " meld4: " + meld4.toStringVerbose() + "\n";
        String wTile = (getWinningTile()==null) ? "null" : getWinningTile().toString();
        s = s + " winningTile: " + wTile + "\n";
        s = s + " hanList: " + hanList.toString() + "\n";
        s = s + " fuList: " + fuList.toString() + "\n";
        return s;
    }
    public String printAllMelds(){
        return "(pair:"+pair.toString()+") (meld1:"+meld1.toString()+") (meld2:"+meld2.toString()+") (meld3:"+meld3.toString()+") (meld4:"+meld4.toString()+")";
    }
    public String getString(Tile.Wind w){
        switch (w){
            case EAST:
                return "East";
            case SOUTH:
                return "South";
            case WEST:
                return "West";
            case NORTH:
                return "North";
        }
        return "---";
    }
    public String getString(Tile.Dragon d){
        switch (d){
            case WHITE:
                return "White";
            case GREEN:
                return "Green";
            case RED:
                return "Red";
        }
        return "---";
    }

}
