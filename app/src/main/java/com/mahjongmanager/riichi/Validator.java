package com.mahjongmanager.riichi;

import com.mahjongmanager.riichi.common.Hand;
import com.mahjongmanager.riichi.common.Meld;
import com.mahjongmanager.riichi.common.Tile;
import com.mahjongmanager.riichi.utils.Log;

import java.util.HashSet;
import java.util.List;

public class Validator {

    /**
     * Check whether the tile is valid and internally consistent according to the rules of Mahjong.
     * Checks for the following:
     * <ul>
     *     <li> Tile has exactly one value
     *     <li> The Value is appropriate for its Suit
     *     <li> Only 5s can be red
     *     <li> A tile that is Called cannot also be an Added tile
     * </ul>
     *
     * @return Boolean that indicates whether the tile is valid
     */
    public static boolean validate(Tile tile){
        return tileIsNotNaked(tile)
                && tileOnlyOneValue(tile)
                && tileValueMatchesSuit(tile)
                && tileRedIsFive(tile)
                && tileCalledCannotBeAdded(tile);
    }
    private static boolean tileIsNotNaked(Tile tile){
        if( tile.suit==null ){
            Log.e("validateTile", "Tile is naked! It has no suit!");
            return false;
        }
        return true;
    }
    private static boolean tileOnlyOneValue(Tile tile){
        boolean n = tile.number!=null;
        boolean d = tile.dragon!=null;
        boolean w = tile.wind!=null;
        if(!( (n&&!d&&!w)||(!n&&d&&!w)||(!n&&!d&&w) )){
            Log.e("validateTile", "Tile does not have exactly one non-null number/wind/dragon: " + tile.number + " " + tile.wind + " " + tile.dragon );
            return false;
        }
        return true;
    }
    private static boolean tileValueMatchesSuit(Tile tile){
        if( tile.suit==Tile.Suit.HONOR && tile.number!=null ){
            Log.e("validateTile", "Honor tiles should not have a number: " + tile.number );
            return false;
        } else if( tile.suit!=Tile.Suit.HONOR && (tile.dragon!=null || tile.wind!=null) ){
            Log.e("validateTile", "Suited tiles should not have a wind or dragon value: " + tile.wind + " " + tile.dragon);
            return false;
        }
        return true;
    }
    private static boolean tileRedIsFive(Tile tile){
        if (tile.red && (tile.number==null || tile.number!=5)) {
            Log.e("validateTile", "Only 5s can be red: " + tile.toString());
            return false;
        }
        return true;
    }
    private static boolean tileCalledCannotBeAdded(Tile tile){
        if( tile.calledFrom!=Tile.CalledFrom.NONE && tile.revealedState==Tile.RevealedState.ADDEDKAN ){
            Log.e("validateTile", "CalledTile cannot be an AddedKan: "+tile.toString());
            return false;
        }
        return true;
    }

    /**
     * Check whether the meld of tiles is a valid, internally consistent, set of tiles
     * according to the rules of Mahjong. Checks for the following:
     * <ul>
     *     <li> There are either 2(pair), 3, or 4 tiles
     *     <li> Each tile is in a valid state
     *     <li> There is at most one called tile
     *     <li> There is at most one added tile (and only if there is a called tile)
     *     <li> All tiles have same revealedStatus (unless there is a called tile or an AddedKan)
     *     <li> calledFrom status is consistent with revealedStatus
     *     <li> State of called tile is compatible with other tiles (and vice versa)
     *     <li> Chiis are called from Left player
     * </ul>
     *
     * @return Boolean that indicates whether the list of tiles is a valid meld
     */
    public static boolean validate(Meld meld){
        if( meld.getTiles().size()<2 || meld.getTiles().size()>4 ){
            Log.e("validateMeld", "Too many, or too few, tiles: " + meld);
            return false;
        } else if( !validateTiles(meld.getTiles()) ){
            return false;
        }

        // Pairs have far less complicated validate conditions
        if ( meld.getTiles().size()==2 ){
            return meldPair(meld);
        }

        return meldUniqueCalledTile(meld)
                && meldUniqueAddedTile(meld)
                && meldNonCalledTilesMatchRevealedState(meld)
                && meldRevealedStateMatchesTileCount(meld)
                && meldConsistentCalledTileState(meld)
                && meldIfChiiIsCalledFromLeft(meld)
                && meldHasCalledTileIfRevealed(meld)
                && meldIsRevealedIfKan(meld);
    }
    private static boolean meldPair(Meld meld){
        if( !meld.firstTile().value.equals(meld.secondTile().value) ){
            Log.e("validateMeld", "It's not a pair if the two tiles aren't the same stupid!: "+meld);
            return false;
        }
        Tile calledTile = meld.getCalledTile();
        if( calledTile!=null && !calledTile.winningTile ){
            Log.e("validateMeld", "A called tile in a pair can only be called if it's the winning tile: "+calledTile.toString());
            return false;
        }
        Tile.RevealedState meldRevealedState = meld.getNonCalledRevealedState();
        if( meldRevealedState!= Tile.RevealedState.NONE ){
            String msg = "At least one of the tiles in the pair has to be concealed: ";
            msg += (meldRevealedState==null) ? "null" : meldRevealedState.toString();
            Log.e("validateMeld", msg);
            return false;
        }
        return true;
    }
    private static boolean meldUniqueCalledTile(Meld meld){
        Tile calledTile = meld.getCalledTile();
        for(Tile t : meld.getTiles()){
            if( t!=calledTile && t.calledFrom!=Tile.CalledFrom.NONE ){
                Log.e("validateMeld", "More than one tile with a calledFrom status: " + calledTile.toString() + " + " + t.toString());
                return false;
            }
        }
        return true;
    }
    private static boolean meldUniqueAddedTile(Meld meld){
        Tile addedTile = meld.getAddedTile();
        for(Tile t : meld.getTiles()){
            if( t!=addedTile && t.revealedState== Tile.RevealedState.ADDEDKAN ){
                Log.e("validateMeld", "More than one tile with a addedTile status: " + addedTile.toString() + " + " + t.toString());
                return false;
            }
        }
        return true;
    }
    private static boolean meldNonCalledTilesMatchRevealedState(Meld meld){
        Tile calledTile = meld.getCalledTile();
        Tile.RevealedState meldRevealedState = meld.getNonCalledRevealedState();

        for( Tile t : meld.getTiles() ){
            if( t != calledTile
                    && !t.winningTile
                    && meldRevealedState != t.revealedState
                    && t.revealedState != Tile.RevealedState.ADDEDKAN ){
                Log.e("validateMeld", "(Non-called)Tiles do not match revealed states: " + meldRevealedState.toString() + " - " + t.revealedState.toString()+" - "+meld);
                return false;
            }
        }
        return true;
    }
    private static boolean meldRevealedStateMatchesTileCount(Meld meld){
        Tile.RevealedState meldRevealedState = meld.getNonCalledRevealedState();

        if( meld.size()==3 ){
            if( meldRevealedState == Tile.RevealedState.CLOSEDKAN || meldRevealedState == Tile.RevealedState.OPENKAN ){
                Log.e("validateMeld", "ClosedKan/OpenKan must be of size 4: "+meld);
                return false;
            }
        } else {
            if( meldRevealedState == Tile.RevealedState.CHI || meldRevealedState == Tile.RevealedState.NONE ){
                Log.e("validateMeld", "Unrevealed melds must be of size 3: "+meldRevealedState.toString()+" - "+meld);
                return false;
            }
        }
        return true;
    }
    private static boolean meldConsistentCalledTileState(Meld meld){
        Tile calledTile = meld.getCalledTile();
        if( calledTile!=null ){
            Tile addedTile = meld.getAddedTile();
            Tile.RevealedState meldRevealedState = meld.getNonCalledRevealedState();

            switch (calledTile.revealedState){
                case NONE:
                    if( !calledTile.winningTile ){
                        Log.e("validateMeld", "Called tile can't be in a revealed state of NONE unless it is a winning tile: "+calledTile+" - "+calledTile.revealedState.toString());
                        return false;
                    }
                    break;
                case PON:
                    if( meld.size()!=3 && addedTile==null ){
                        Log.e("validateMeld", "Called Chii/Pon must be of size 3 unless part of AddedKan: "+calledTile+" - "+calledTile.revealedState.toString()+" - "+meld);
                        return false;
                    }
                    break;
                case CLOSEDKAN:
                    Log.e("validateMeld", "A called tile cannot be part of a closed kan: "+calledTile+" - "+calledTile.revealedState.toString());
                    return false;
                case ADDEDKAN:
                    Log.e("validateMeld", "Called tile cannot be AddedKan: "+meld);
                    return false;
            }

            if( calledTile.revealedState!=meldRevealedState && calledTile.revealedState!=Tile.RevealedState.NONE ){
                Log.e("validateMeld", "CalledTile state does not match the meldState of the rest of the tiles: "+calledTile.revealedState.toString()+" - "+meldRevealedState.toString());
                return false;
            }
        }
        return true;
    }
    private static boolean meldIfChiiIsCalledFromLeft(Meld meld){
        Tile calledTile = meld.getCalledTile();
        if( meld.isChii()
                && calledTile!=null
                && !calledTile.winningTile
                && calledTile.calledFrom!=Tile.CalledFrom.LEFT ){
            Log.e("validateMeld", "Called tile is part of a chii and wasn't called from left player: "+calledTile+" - "+calledTile.revealedState.toString()+" - "+calledTile.calledFrom.toString()+" - "+meld);
            return false;
        }
        return true;
    }
    private static boolean meldHasCalledTileIfRevealed(Meld meld){
        Tile calledTile = meld.getCalledTile();
        Tile.RevealedState meldRevealedState = meld.getNonCalledRevealedState();

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
    private static boolean meldIsRevealedIfKan(Meld meld){
        Tile.RevealedState meldRevealedState = meld.getNonCalledRevealedState();

        if( meld.size()==4 && meldRevealedState==Tile.RevealedState.NONE ){
            Log.e("validateMeld", "A set of 4 cannot be unrevealed: "+meld+" - "+meldRevealedState.toString());
            return false;
        }

        return true;
    }


    /**
     * Check whether the hand of tiles is a complete, internally consistent, set of tiles
     * that form a winning hand under Riichi Mahjong rules. Checks for the following:
     * <ul>
     *     <li> There are no unsorted tiles (it is finished being scored)
     *     <li> Does not have too few or too many tiles
     *     <li> Each Meld validates (or at least each tile, if hand is abnormal)
     *     <li> No more than 4 copies of any tile (including dora indicators)
     *     <li> Only one winning tile
     *     <li> Hand score is not obviously nonsense (unnecessary? should be caught by test cases)
     *     <li> Situational yaku (haitei/houtei/chankan/etc) are not in conflict
     *     <li> If the hand is open, there are no yaku that can only exist in a closed hand
     *     <li> All tiles in the Hand object are properly accounted for in melds
     *     <li> The winning tile is not from a kan      TODO or open meld?
     * </ul>
     * Does not check every theoretical conflict between yaku.
     * Good reference: http://arcturus.su/wiki/Yaku_compatability
     *
     * @return Boolean that indicates whether the hand is perfectly valid
     */
    public static boolean validate(Hand hand){
        if( hand.unsortedTiles.size()!=0 ){
            Log.e("validateHand", "unsortedTiles is not empty: "+hand.unsortedTiles);
            return false;
        } else if( hand.tiles.size()<14 || hand.tiles.size()>18 ){
            Log.e("validateHand", "too few (or too many) total number of tiles: "+hand.tiles);
            return false;
        }

        return handAllMelds(hand)
                && handNotTooManyTiles(hand)
                && handOnlyOneWinningTile(hand)
                && handNoDuplicateTiles(hand)
                && handRealScore(hand)
                && handHaiteiHoutei(hand)
                && handChanKan(hand)
                && handRinshan(hand)
                && handNoClosedYakuWithOpenState(hand)
                && handNoMissingTiles(hand)
                && handWinningTileNotInKan(hand);
    }
    private static boolean handNotTooManyTiles(Hand hand){
        for( Tile t : hand.tiles ){
            int tCount = hand.countTileComplete(t);

            if(tCount>4){
                Log.e("validateHand", "hand contains too many copies of this tile: "+tCount+"x "+t);
                return false;
            }
        }
        return true;
    }
    private static boolean handOnlyOneWinningTile(Hand hand){
        Tile winningTile = null;
        for( Tile t : hand.tiles ){
            if( winningTile==null && t.winningTile ){
                winningTile = t;
            } else if( winningTile!=null && t.winningTile ){
                Log.e("validateHand", "More than one winning tile present: "+winningTile+" + "+t);
                return false;
            }
        }
        return true;
    }
    private static boolean handNoDuplicateTiles(Hand hand){
        HashSet<Tile> noDupes = new HashSet<>();
        for(Tile t : hand.tiles ){
            if( !noDupes.add(t) ){
                Log.e("validateHand", "Tiles list has duplicate instances of Tile object: "+t);
                return false;
            }
        }
        return true;
    }
    private static boolean handRealScore(Hand hand){
        //validate that score is not impossible (e.g. 1 han 20 fu)
        if( (hand.han==1&&hand.fu==20) || (hand.han==1&&hand.fu==25) || (hand.han==2&&hand.fu==25&&hand.tsumo) ){
            Log.e("validateHand", "Impossible score: han-"+hand.han+" fu-"+hand.fu+" tsumo-"+hand.tsumo+" - "+hand.toStringVerbose());
            return false;
        }
        return true;
    }
    private static boolean handHaiteiHoutei(Hand hand){
        if( hand.haitei && (!hand.selfDrawWinningTile || hand.houtei || hand.rinshan || hand.chanKan ) ){
            Log.e("validateHand", "Cannot have haitei without selfDraw or with houtei/rinshan/chankan: "+hand.selfDrawWinningTile+" - "+hand.houtei+" - "+hand.rinshan+" - "+hand.chanKan+" - "+hand.toStringVerbose());
            return false;
        }
        if( hand.houtei && (hand.selfDrawWinningTile || hand.ippatsu || hand.tsumo || hand.rinshan || hand.chanKan ) ){
            Log.e("validateHand", "Cannot have houtei with selfDraw or ippatsu/tsumo/rinshan/chankan: "+hand.selfDrawWinningTile+" - "+hand.ippatsu+" - "+hand.tsumo+" - "+hand.rinshan+" - "+hand.chanKan+" - "+hand.toStringVerbose());
            return false;
        }
        return true;
    }
    private static boolean handChanKan(Hand hand){
        if( hand.chanKan ){
            int wTileCount = (hand.getWinningTile()==null) ? 0 : hand.countTile(hand.getWinningTile());
            if( wTileCount!=1 || hand.selfDrawWinningTile || hand.tsumo || hand.rinshan ){
                Log.e("validateHand", "Cannot have chanKan with more than one copy of winning tile in hand (or with selfDraw/tsumo/rinshan): "+wTileCount+" "+hand.getWinningTile()+" - "+hand.selfDrawWinningTile+" - "+hand.tsumo+" - "+hand.rinshan+" - "+hand.toStringVerbose());
                return false;
            }
        }
        return true;
    }
    private static boolean handRinshan(Hand hand){
        if( hand.rinshan && (!hand.hasKan() || !hand.selfDrawWinningTile) ){
            Log.e("validateHand", "Hand must contain a Kan to win with rinshan: "+hand.toStringVerbose());
            return false;
        }
        return true;
    }
    private static boolean handNoClosedYakuWithOpenState(Hand hand){
        if( hand.isOpen() && (hand.riichi || hand.tsumo || hand.ippatsu || hand.doubleRiichi || hand.pinfu || hand.iipeikou ) ){
            Log.e("validateHand", "Open hand has a conflicting yaku: "+hand.toStringVerbose());
            return false;
        }
        return true;
    }
    private static boolean handNoMissingTiles(Hand hand){
        int usedTiles = hand.meld1.size() + hand.meld2.size() + hand.meld3.size() + hand.meld4.size() + hand.pair.size() + hand.unsortedTiles.size();

        if( !hand.hasAbnormalStructure() && usedTiles!=hand.tiles.size() ){
            Log.e("validateHand", "Tile counts don't match! tiles: "+ hand.toStringVerbose());
            return false;
        }
        return true;
    }
    private static boolean handAllMelds(Hand hand){
        if( hand.hasAbnormalStructure() ){
            return validateTiles(hand.tiles);
        }

        if( !(validate(hand.pair)
                && validate(hand.meld1)
                && validate(hand.meld2)
                && validate(hand.meld3)
                && validate(hand.meld4)) ){
            Log.e("validateHand", "Something went wrong validating the melds, here's the whole hand: "+hand.toStringVerbose());
            return false;
        }
        return true;
    }
    private static boolean handWinningTileNotInKan(Hand hand){
        Meld winningMeld = hand.getWinningMeld();
        if( winningMeld!=null && winningMeld.isKan() ){
            Log.e("validateHand", "WinningTile cannot be part of a kan: "+hand.toStringVerbose());
            return false;
        }
        return true;
    }


    private static boolean validateTiles(List<Tile> tiles){
        for(Tile t : tiles ){
            if( !validate(t) ){
                Log.e("validateTiles", "Tile is in invalid state: "+t+" - "+tiles);
                return false;
            }
        }
        return true;
    }
}
