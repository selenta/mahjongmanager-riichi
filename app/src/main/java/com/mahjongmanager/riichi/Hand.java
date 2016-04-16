package com.mahjongmanager.riichi;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Hand {
    public List<Tile> tiles = new ArrayList<>();
    public List<Tile> unsortedTiles = new ArrayList<>();
    public List<Tile> pair = new ArrayList<>();
    public List<Tile> set1 = new ArrayList<>();
    public List<Tile> set2 = new ArrayList<>();
    public List<Tile> set3 = new ArrayList<>();
    public List<Tile> set4 = new ArrayList<>();

    public Integer han = 0;
    public Integer fu = 0;

    public Map<String,Integer> hanList = new HashMap<>();
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

    public Hand( List<Tile> h ){
        tiles.addAll(h);
        sort(tiles);

        unsortedTiles.addAll(h);
        sort(unsortedTiles);
    }
    public Hand( Hand oldHand ){
        tiles.addAll(oldHand.tiles);
        unsortedTiles.addAll(oldHand.unsortedTiles);
        pair.addAll(oldHand.pair);
        set1.addAll(oldHand.set1);
        set2.addAll(oldHand.set2);
        set3.addAll(oldHand.set3);
        set4.addAll(oldHand.set4);

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
    }

    public Tile findTile(String val, String suit){
        Tile target = null;
//        Log.d("findTile", "Attenpting to find tile: suit=" + suit + ", val=" + val );

        for( Tile tile : tiles ){
            if( tile.suit.toString().equalsIgnoreCase(suit) && tile.value.equalsIgnoreCase(val) ){
                target = tile;
                break;
            }
        }
        return target;
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

    public void setSet( List<Tile> tbdSet ){
        if( set1.size()==0 ){
            set1 = tbdSet;
        } else if( set2.size()==0 ){
            set2 = tbdSet;
        } else if( set3.size()==0 ){
            set3 = tbdSet;
        } else if( set4.size()==0 ){
            set4 = tbdSet;
        } else {
            Log.e("All hand sets set", "all sets: " + set1.toString() + " "+ set2.toString() + " "+ set3.toString() + " "+ set4.toString());
            Log.e("All hand sets set", "unsortedTiles: " + unsortedTiles.toString());
            Log.e("All hand sets set", "Attempted to setSet when all sets were already full: " + tbdSet);
        }
    }

    public boolean addTile( Tile t ){
        if( containsMaxOfTile(t) ){
            return false;
        }

        if( unsortedTiles.size()==0 && tiles.size()>0 ){
            tiles.add(t);
        } else {
            tiles.add(t);
            unsortedTiles.add(t);
        }
        sort();
        return true;
    }


    // Verify hand consistency, including:
    //TODO can't be haitei without selfDrawWinningTile
    //TODO can't be houtei with Tsumo or selfDrawWinningTile
    //TODO consider other inherently contradictory yaku conditions
    //TODO Chiitoitsu/kokushi/nagashi don't work with most things
    //TODO Can't have Chan Kan if it isn't the winning tile and only copy of tile in hand or if SelfDrawWinningTile is false
    //TODO Can't have Rinshan unless there is at least one Kan, winning tile is not part of kan, and selfDrawWinningTile is true
    //TODO WinningTile can't be part of a set of 4
    public boolean validateCompleteState(){
        if( unsortedTiles.size()!=0 ){
            Log.e("validateCompleteState", "unsortedTiles is not empty: "+unsortedTiles.toString());
            return false;
        } else if( tiles.size()<14 || tiles.size()>18 ){
            Log.e("validateCompleteState", "too few (or too many) total number of tiles: "+tiles.toString());
            return false;
        }

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

        //verify each tile
        for(Tile t : tiles ){
            if( !t.validateTile() ){
                Log.e("validateCompleteState", "Tile is in invalid state: "+t.toString());
                return false;
            }
        }

        //validate that score is not impossible (e.g. 1 han 20 fu)
        if( (han==1&&fu==20) || (han==1&&fu==25) || (han==2&&fu==25&&tsumo) ){
            Log.e("validateCompleteState", "Impossible score: han-"+han.toString()+" fu-"+fu.toString()+" tsumo-"+tsumo.toString()+" - "+toStringVerbose());
            return false;
        }

        if( !chiiToitsu && !kokushiMusou && !nagashiMangan ){
            if( tiles.size()!=(pair.size()+set1.size()+set2.size()+set3.size()+set4.size()) ){
                Log.e("validateCompleteState", "(1/2) Tile counts don't match! tiles: "+unsortedTiles.toString());
                Log.e("validateCompleteState", "(2/2) Tile counts don't match! melds: "+printAllSets());
                return false;
            }

            //All sets are set
            if( pair.size()!=2
                    || (set1.size()<3 || set1.size()>4)
                    || (set2.size()<3 || set2.size()>4)
                    || (set3.size()<3 || set3.size()>4)
                    || (set4.size()<3 || set4.size()>4) ){
                Log.e("validateCompleteState", "Sets are not all set: "+printAllSets());
                return false;
            }

            //ALl tiles in each set match states
            if( !Utils.validateSet(pair)
                || !Utils.validateSet(set1)
                || !Utils.validateSet(set2)
                || !Utils.validateSet(set3)
                || !Utils.validateSet(set4) ){
                Log.e("validateHand", "Something went wrong, here's the whole hand: "+toStringVerbose());
                return false;
            }
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
    public boolean setContains( String val, String suit, List<Tile> set ){
        for( Tile tile : set ){
            if( tile.value.equalsIgnoreCase(val) && tile.suit.toString().equalsIgnoreCase(suit) ){
                return true;
            }
        }
        return false;
    }
    public boolean containsMaxOfTile( Tile tilePrime ){
        int tileCount = 0;
        for( Tile t : tiles ){
            if( t.toString().equals(tilePrime.toString()) ){
                tileCount++;
            }
        }
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
    public List<Tile> getWinningMeld(){
        Log.d("printAllSets", "Print all sets: " + printAllSets());

        for( Tile t : pair ){
            if( t.winningTile ){
                return pair;
            }
        }
        for( Tile t : set1 ){
            if( t.winningTile ){
                return set1;
            }
        }
        for( Tile t : set2 ){
            if( t.winningTile ){
                return set2;
            }
        }
        for( Tile t : set3 ){
            if( t.winningTile ){
                return set3;
            }
        }
        for( Tile t : set4 ){
            if( t.winningTile ){
                return set4;
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
        count = (set1.size()==0) ? count + 1 : count;
        count = (set2.size()==0) ? count + 1 : count;
        count = (set3.size()==0) ? count + 1 : count;
        count = (set4.size()==0) ? count + 1 : count;
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
        return !(set1.size()!=4 && set2.size()!=4 && set3.size()!=4 && set4.size()!=4);
    }
    public boolean hasYakuman(){
        return ( kokushiMusou || suuAnkou      || daisangen || shousuushii
                || daisuushii || tsuuiisou     || daichisei || chinroutou
                || ryuuiisou  || chuurenPoutou || suuKantsu
                || suuAnkouTanki || kokushiMusou13wait || chuurenPoutou9wait);
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
    // TODO this seems like it should be used more, or shouldn't exist, not sure which
    public boolean hasAbnormalStructure(){
        return kokushiMusou || kokushiMusou13wait || chiiToitsu || nagashiMangan;
    }

    public void sort(){
        sort(tiles);
        sort(unsortedTiles);
        sort(pair);
        sort(set1);
        sort(set2);
        sort(set3);
        sort(set4);
    }
    private void sort( List<Tile> tz ){
        Collections.sort(tz, new Comparator<Tile>() {
            @Override
            public int compare(Tile p1, Tile p2) {
                return p1.sortId - p2.sortId; // Ascending
            }
        });
    }
    public String toString(){
        return (tiles.isEmpty()) ? "[...]" : tiles.toString();
//        return printAllSets();
    }
    public String toStringVerbose(){
        String s = " tiles: " + toString() + "\n";
        s = s + " unsortedTiles: " + unsortedTiles.toString() + "\n";
        s = s + " pair: " + pair.toString() + "\n";
        s = s + " set1: " + set1.toString() + "\n";
        s = s + " set2: " + set2.toString() + "\n";
        s = s + " set3: " + set3.toString() + "\n";
        s = s + " set4: " + set4.toString() + "\n";
        s = s + " winningTile: " + getWinningTile().toString() + "\n";
        s = s + " hanList: " + hanList.toString() + "\n";
        s = s + " fuList: " + fuList.toString() + "\n";
        return s;
    }
    public String printAllSets(){
        return "(pair:"+pair.toString()+") (set1:"+set1.toString()+") (set2:"+set2.toString()+") (set3:"+set3.toString()+") (set4:"+set4.toString()+")";
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
