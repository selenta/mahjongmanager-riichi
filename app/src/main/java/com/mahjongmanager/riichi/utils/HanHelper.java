package com.mahjongmanager.riichi.utils;

import com.mahjongmanager.riichi.common.Hand;
import com.mahjongmanager.riichi.common.Meld;
import com.mahjongmanager.riichi.common.Tile;
import com.mahjongmanager.riichi.common.Yaku;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HanHelper {

    /////////////////////////////////////////////////////////////////////////
    ////////////////////        General Methods        //////////////////////
    /////////////////////////////////////////////////////////////////////////
    /**
     * Examines the hand in depth, including its overall structure, meld structure, pair,
     * and its winning tile. Adding an entry to the hand's hanList for each type of Han present.
     * Checks hand for all yaku of normal meld structure (e.g. does NOT check for Chiitoitsu)
     * @param h The hanList of this Hand will be populated
     */
    public static void populateHanList(Hand h){
        if( h.hasAbnormalStructure() ){
            return;
        }

        checkSuuAnkou(h);
        checkDaisangen(h);
        checkShousuushii(h);
        checkDaisuushii(h);
        checkTsuuiisou(h);
        checkDaichisei(h);
        checkChinroutou(h);
        checkRyuuiisou(h);
        checkChuurenPoutou(h);
        checkSuuKantsu(h);

        checkSuurenkou(h);
        checkDaiSharin(h);
        checkShiisanpuuta(h);
        checkShiisuupuuta(h);
        checkParenchan(h);

        if( h.hasYakuman() ){
            clearNonYakumanYaku(h);
            return;
        }

        // Situational
        checkLuckBasedHan(h);
        checkTsumo(h);
        checkRinshan(h);

        checkPinfu(h);
        checkIipeikou(h);
        checkSanshokuDoujun(h);
        checkIttsuu(h);
        checkRyanpeikou(h);
        checkToitoi(h);
        checkSanAnkou(h);
        checkSanshokuDoukou(h);
        checkSanKantsu(h);
        checkTanyao(h);
        checkYakuhai(h);
        checkJunchan(h);
        checkHonroutou(h);
        checkChanta(h);
        checkShousangen(h);
        checkHonitsu(h);
        checkChinitsu(h);

        checkSanrenkou(h);

        countDora(h);
    }
    private static void checkLuckBasedHan(Hand h){
        if( h.riichi ){
            h.hanList.put(Yaku.Name.RIICHI, 1);
        }
        if(h.ippatsu){
            h.hanList.put(Yaku.Name.IPPATSU, 1);
        }
        if(h.haitei){
            h.hanList.put(Yaku.Name.HAITEI, 1);
        }
        if(h.houtei){
            h.hanList.put(Yaku.Name.HOUTEI, 1);
        }
        if(h.rinshan){
            h.hanList.put(Yaku.Name.RINSHAN, 1);
        }
        if(h.chanKan){
            h.hanList.put(Yaku.Name.CHANKAN, 1);
        }
        if(h.doubleRiichi){
            h.hanList.put(Yaku.Name.DOUBLERIICHI, 2);
        }
    }


    // Unusually structed hands (must be handled seperately)
    public static boolean isChiitoitsu(Hand h){
        List<Tile> tz = Utils.findDuplicateTiles(h.tiles);
        int moreThanTwoCopies = Utils.findDuplicateTiles(tz).size();
        return tz.size()==7 && moreThanTwoCopies==0;
    }
    public static void checkChiitoitsu(Hand h){
        if( isChiitoitsu(h) ){
            h.unsortedTiles.clear();
            h.hanList.put(Yaku.Name.CHIITOITSU, 2);

            checkLuckBasedHan(h);
            checkTanyao(h);
            checkHonroutou(h);
            checkHonitsu(h);
            checkChinitsu(h);
            countDora(h);
        }
    }
    public static boolean isDaichisei(Hand h){
        List<Tile> tz = Utils.findDuplicateTiles(h.tiles);
        List<Tile> doubleDupes = Utils.findDuplicateTiles(tz);
        int suitCount = countSuits(h.tiles);
        return tz.size()==7 && doubleDupes.size()==0 && suitCount==0;
    }
    public static void checkDaichisei(Hand h){
        if( isDaichisei(h) ){
            h.unsortedTiles.clear();
            h.hanList.put(Yaku.Name.DAICHISEI, 26);
        }
    }     // 7 pairs of all honors
    public static boolean isKokushiVariant(Hand h){
        if( !Utils.containsHonorsOrTerminalsOnly(h.tiles) ){
            return false;
        }

        //Different tiles
        HashSet<String> noDupSet = new HashSet<>();
        for( Tile t : h.tiles ){
            noDupSet.add(t.toString());
        }
        return noDupSet.size()==13;
    }
    public static void checkKokushiMusou(Hand h){
        if( isKokushiVariant(h) ){
            if( h.countTile(h.getWinningTile())==2 ){
                h.unsortedTiles.clear();
                h.hanList.put(Yaku.Name.KOKUSHIMUSOU13SIDED, 26);
            } else {
                h.unsortedTiles.clear();
                h.hanList.put(Yaku.Name.KOKUSHIMUSOU, 13);
            }
        }
    }
    public static void checkNagashiMangan(Hand h){
        if( h.nagashiMangan ){
            h.unsortedTiles.clear();
            h.hanList.put(Yaku.Name.NAGASHI, 5);
        }
    }


    // Circumstantial Yaku
    private static void checkTsumo(Hand h){
        for( Tile t : h.tiles ){
            if( t.winningTile && t.calledFrom==Tile.CalledFrom.NONE ){
                h.selfDrawWinningTile = true;
            }
        }
        if( !h.isOpen() && h.selfDrawWinningTile ){
            h.hanList.put(Yaku.Name.TSUMO, 1);
        }
    }
    private static void checkRinshan(Hand h){
        if( h.rinshan ){
            h.hanList.remove(Yaku.Name.TSUMO);
        }
    }

    // Standard Yaku
    private static void checkPinfu(Hand h){
        //If no winning tile has been set, it's ok to still to count this as pinfu
        if( FuHelper.hasFu(h) ){
            h.hanList.remove(Yaku.Name.PINFU);
            return;
        }
        h.hanList.put(Yaku.Name.PINFU, 1);
    }
    private static void checkIipeikou(Hand h){
        if( !h.isOpen() ){
            if( h.meld1.toString().equals(h.meld2.toString())
                    || h.meld1.toString().equals(h.meld3.toString())
                    || h.meld1.toString().equals(h.meld4.toString())
                    || h.meld2.toString().equals(h.meld3.toString())
                    || h.meld2.toString().equals(h.meld4.toString())
                    || h.meld3.toString().equals(h.meld4.toString())
                    ){
                h.hanList.put(Yaku.Name.IIPEIKOU, 1);
            }
        }
    }
    private static void checkSanshokuDoujun(Hand h){
        //Get 3+ chiis
        List<Tile> leadTiles = new ArrayList<>();
        for(Meld m : Arrays.asList(h.meld1, h.meld2, h.meld3, h.meld4)){
            if( m.isChii() && m.getSuit()!=Tile.Suit.HONOR ){
                leadTiles.add(m.firstTile());
            }
        }
        if( leadTiles.size()<3 ){
            return;
        }

        //Same number (remove the ones that don't match number)
        int firstTile = 0;
        int secondTile = 0;
        for( Tile t : leadTiles ){
            if( t.number == leadTiles.get(0).number ){
                firstTile++;
            } else if( t.number == leadTiles.get(1).number ){
                secondTile++;
            }
        }
        if( firstTile<3 && secondTile<3 ){
            return;
        } else if( secondTile==3 ){
            leadTiles.remove(0);
        } else if( firstTile==3 ){
            for(int i=0; i<leadTiles.size(); i++){
                if( leadTiles.get(i).number != leadTiles.get(0).number ){
                    leadTiles.remove(i);
                }
            }
        }

        //Different suits
        HashSet<String> noDupSet = new HashSet<>();
        for( Tile t : leadTiles ){
            noDupSet.add(t.suit.toString());
        }
        if( noDupSet.size()==3 ){
            int han = (h.isOpen()) ? 1 : 2;
            h.hanList.put(Yaku.Name.SANSHOKUDOUJUN, han);
        }
    }
    private static void checkIttsuu(Hand h){
        //find dominant suit first
        Set<String> duplicateSuits = new HashSet<>();
        Set<String> tempSet = new HashSet<>();
        Set<String> tempSet2 = new HashSet<>();
        List<Tile> leadingTiles = new ArrayList<>();
        leadingTiles.add( h.meld1.firstTile() );
        leadingTiles.add( h.meld2.firstTile() );
        leadingTiles.add( h.meld3.firstTile() );
        leadingTiles.add( h.meld4.firstTile() );
        for (Tile dupTile : leadingTiles ){
            if (!tempSet.add(dupTile.suit.toString())) {
                if (!tempSet2.add(dupTile.suit.toString())) {
                    duplicateSuits.add(dupTile.suit.toString());
                }
            }
        }

        //check for a 1-2-3, 4-5-6, and 7-8-9 chii in the dominant suit
        boolean firstMeld = false;
        boolean secondMeld = false;
        boolean thirdMeld = false;
        for( Meld m : Arrays.asList(h.meld1, h.meld2, h.meld3, h.meld4) ){
            if( m.isChii() && duplicateSuits.contains(m.firstTile().suit.toString()) && m.firstTile().number==1 ){
                firstMeld = true;
            } else if( m.isChii() && duplicateSuits.contains(m.firstTile().suit.toString()) && m.firstTile().number==4 ){
                secondMeld = true;
            } else if( m.isChii() && duplicateSuits.contains(m.firstTile().suit.toString()) && m.firstTile().number==7 ){
                thirdMeld = true;
            }
        }

        //if all three sets exist, we have an ittsuu
        if( firstMeld && secondMeld && thirdMeld ){
            int han = (h.isOpen()) ? 1 : 2;
            h.hanList.put(Yaku.Name.ITTSUU, han);
        }
    }
    private static void checkRyanpeikou(Hand h){
        if( h.meld1.toString().equals(h.meld2.toString()) && h.meld3.toString().equals(h.meld4.toString())
                || h.meld1.toString().equals(h.meld3.toString()) && h.meld2.toString().equals(h.meld4.toString())
                || h.meld1.toString().equals(h.meld4.toString()) && h.meld2.toString().equals(h.meld3.toString())
                ){
            h.hanList.put(Yaku.Name.RYANPEIKOU, 3);
            h.hanList.remove(Yaku.Name.IIPEIKOU);
        }
    }
    private static void checkToitoi(Hand h){
        if( !h.meld1.isChii() && !h.meld2.isChii() && !h.meld3.isChii() && !h.meld4.isChii() ){
            h.hanList.put(Yaku.Name.TOITOI, 2);
        }
    }
    private static void checkSanAnkou(Hand h){
        int closedTriplets = 0;
        for(Meld m : Arrays.asList(h.meld1, h.meld2, h.meld3, h.meld4)){
            if( !m.isChii() && m.isClosed() ){
                closedTriplets += 1;
            }
        }

        if( closedTriplets>=3 ){
            h.hanList.put(Yaku.Name.SANANKOU, 2);
        }
    }
    private static void checkSanshokuDoukou(Hand h){
        //Get 3+ triplets
        List<Tile> leadTiles = new ArrayList<>();
        if( !h.meld1.isChii() && h.meld1.getSuit()!=Tile.Suit.HONOR ){
            leadTiles.add(h.meld1.firstTile());
        }
        if( !h.meld2.isChii() && h.meld2.getSuit()!=Tile.Suit.HONOR ){
            leadTiles.add(h.meld2.firstTile());
        }
        if( !h.meld3.isChii() && h.meld3.getSuit()!=Tile.Suit.HONOR ){
            leadTiles.add(h.meld3.firstTile());
        }
        if( !h.meld4.isChii() && h.meld4.getSuit()!=Tile.Suit.HONOR ){
            leadTiles.add(h.meld4.firstTile());
        }
        if( leadTiles.size()<3 ){
            return;
        }

        //Same number (remove the ones that don't match number)
        int firstTile = 0;
        int secondTile = 0;
        for( Tile t : leadTiles ){
            if( t.number == leadTiles.get(0).number ){
                firstTile++;
            } else if( t.number == leadTiles.get(1).number ){
                secondTile++;
            }
        }
        if( firstTile<3 && secondTile<3 ){
            return;
        } else if( firstTile==1 && secondTile==3 ){
            leadTiles.remove(0);
        } else if( firstTile==3 && secondTile==1 ){
            leadTiles.remove(1);
        }

        //Different suits
        HashSet<String> noDupSet = new HashSet<>();
        for( Tile t : leadTiles ){
            noDupSet.add(t.suit.toString());
        }
        if( noDupSet.size()==3 ){
            h.hanList.put(Yaku.Name.SANSHOKUDOUKOU, 2);
        }
    }
    private static void checkSanKantsu(Hand h){
        int kanNumber = 0;
        for(Meld m : Arrays.asList(h.meld1, h.meld2, h.meld3, h.meld4)){
            if( m.isKan() ){
                kanNumber++;
            }
        }
        if( kanNumber==3 ){
            h.hanList.put(Yaku.Name.SANKANTSU, 2);
        }
    }
    private static void checkTanyao(Hand h){
        if( !Utils.containsHonorsOrTerminals(h.tiles) ){
            h.hanList.put(Yaku.Name.TANYAO, 1);
        }
    }
    private static void checkYakuhai(Hand h){
        int han = 0;
        han = (h.hasDragonWhiteSet())       ? han+1 : han;
        han = (h.hasDragonGreenSet())       ? han+1 : han;
        han = (h.hasDragonRedSet())         ? han+1 : han;
        han = (h.hasPrevailingWindSet())    ? han+1 : han;
        han = (h.hasPlayerWindSet())        ? han+1 : han;

        if( han>0 ){
            h.hanList.put(Yaku.Name.YAKUHAI, han);
        }
    }
    private static void checkJunchan(Hand h){
        if( Utils.containsTerminals(h.pair.getTiles())
                && Utils.containsTerminals(h.meld1.getTiles())
                && Utils.containsTerminals(h.meld2.getTiles())
                && Utils.containsTerminals(h.meld3.getTiles())
                && Utils.containsTerminals(h.meld4.getTiles())
                ){
            int han = (h.isOpen()) ? 2 : 3;
            h.hanList.put(Yaku.Name.JUNCHAN, han);
        }
    }
    private static void checkHonroutou(Hand h){
        if( Utils.containsHonorsOrTerminalsOnly(h.tiles) ){
            h.hanList.put(Yaku.Name.HONROUTOU, 2);
        }
    }
    private static void checkChanta(Hand h){
        if( Utils.containsHonorsOrTerminals(h.pair.getTiles())
                && Utils.containsHonorsOrTerminals(h.meld1.getTiles())
                && Utils.containsHonorsOrTerminals(h.meld2.getTiles())
                && Utils.containsHonorsOrTerminals(h.meld3.getTiles())
                && Utils.containsHonorsOrTerminals(h.meld4.getTiles())
                && !h.hasYaku(Yaku.Name.HONROUTOU)
                && !h.hasYaku(Yaku.Name.JUNCHAN)
                ){
            int han = (h.isOpen()) ? 1 : 2;
            h.hanList.put(Yaku.Name.CHANTA, han);
        }
    }
    private static void checkShousangen(Hand h){
        HashSet<String> noDupSet = new HashSet<>();
        if( h.pair.firstTile().dragon!=null ){
            noDupSet.add(h.pair.firstTile().dragon.toString());
        }
        if( h.hasDragonWhiteSet() ){
            noDupSet.add("WHITE");
        }
        if( h.hasDragonGreenSet() ){
            noDupSet.add("GREEN");
        }
        if( h.hasDragonRedSet() ){
            noDupSet.add("RED");
        }
        if( noDupSet.size()==3 && (!h.hasDragonWhiteSet()||!h.hasDragonGreenSet()||!h.hasDragonRedSet()) ){
            h.hanList.put(Yaku.Name.SHOUSANGEN, 2);
        }
    }
    private static void checkHonitsu(Hand h){
        if( Utils.containsHonors(h.tiles) && countSuits(h.tiles)==1 ){
            int han = (h.isOpen()) ? 2 : 3;
            h.hanList.put(Yaku.Name.HONITSU, han);
        }
    }
    private static void checkChinitsu(Hand h){
        if( !Utils.containsHonors(h.tiles) && countSuits(h.tiles)==1 ){
            int han = (h.isOpen()) ? 5 : 6;
            h.hanList.put(Yaku.Name.CHINITSU, han);
        }
    }

    // Yakuman
    private static void checkSuuAnkou(Hand h){
        int selfDrawnTriplets = 0;
        for(Meld m : Arrays.asList(h.meld1, h.meld2, h.meld3, h.meld4)){
            if( !m.isPair() && !m.isChii() && m.isClosed() && m.size()>0 ){
                selfDrawnTriplets++;
            }
        }
        if( selfDrawnTriplets==4 ){
            if( h.pair.hasWinningTile() ){
                h.hanList.put(Yaku.Name.SUUANKOUTANKI, 26);
                h.hanList.remove(Yaku.Name.SUUANKOU);
            } else {
                h.hanList.put(Yaku.Name.SUUANKOU, 13);
            }
        }
    }      // Four concealed triplets (double for pair wait)
    private static void checkDaisangen(Hand h){
        int dragonPons = 0;
        if( h.hasDragonWhiteSet() ){
            dragonPons++;
        }
        if( h.hasDragonGreenSet()){
            dragonPons++;
        }
        if( h.hasDragonRedSet() ){
            dragonPons++;
        }
        if( dragonPons==3 ){
            h.hanList.put(Yaku.Name.DAISANGEN, 13);
        }
    }
    private static void checkShousuushii(Hand h){
        List<Tile.Wind> winds = new ArrayList<>(Arrays.asList(Tile.Wind.values()));
        for( Tile.Wind w : Tile.Wind.values() ){
            int count = h.countTile(new Tile(w));
            if( count>=3 ){
                winds.remove(w);
            }
        }
        if( winds.size()==0 ){
            return;
        }

        int fourthWindCount = h.countTile(new Tile(winds.get(0)));
        if(winds.size()==1 && fourthWindCount==2 ){
            h.hanList.put(Yaku.Name.SHOUSUUSHII, 13);
        }
    }   // Three triplets and a pair of winds
    private static void checkDaisuushii(Hand h){
        List<Tile.Wind> winds = new ArrayList<>(Arrays.asList(Tile.Wind.values()));
        for( Tile.Wind w : Tile.Wind.values() ){
            int tileCount = h.countTile(new Tile(w));
            if( tileCount==3 || tileCount==4 ){
                winds.remove(w);
            }
        }

        if( winds.size()==0 ){
            h.hanList.put(Yaku.Name.DAISUUSHII, 26);
        }
    }    // Four triplets of winds
    private static void checkTsuuiisou(Hand h){
        if( countSuits(h.tiles)==0 && !h.hasYaku(Yaku.Name.CHIITOITSU) ){
            h.hanList.put(Yaku.Name.TSUUIISOU, 13);
        }
    }     // All honors
    private static void checkChinroutou(Hand h){
        if( !Utils.containsHonors(h.tiles) && !Utils.containsSimples(h.tiles) ){
            h.hanList.put(Yaku.Name.CHINROUTOU, 13);
        }
    }    // All terminals
    private static void checkRyuuiisou(Hand h){
        for(Tile t : h.tiles){
            if( t.suit==Tile.Suit.HONOR && t.dragon!=Tile.Dragon.GREEN ){
                return;
            } else if( (t.number==1||t.number==5||t.number==7||t.number==9) ){
                return;
            }
        }
        h.hanList.put(Yaku.Name.RYUUIISOU, 13);
    }     // All green
    private static void checkChuurenPoutou(Hand h){
        if( countSuits(h.tiles)!=1 || Utils.containsHonors(h.tiles) ){
            return;
        }

        Tile.Suit suit = h.tiles.get(0).suit;
        List<Tile> expectedTiles = new ArrayList<>();
        for(int i=1; i<10; i++){
            expectedTiles.add(new Tile(i, suit));
        }
        expectedTiles.add(new Tile(1, suit));
        expectedTiles.add(new Tile(1, suit));
        expectedTiles.add(new Tile(9, suit));
        expectedTiles.add(new Tile(9, suit));

        List<Tile> usedTiles = new ArrayList<>();
        List<Tile> usedExpectedTiles = new ArrayList<>();
        for(Tile realTile : h.tiles){
            for(Tile expectedTile : expectedTiles ){
                if( expectedTile.value.equals(realTile.value)
                        && !usedTiles.contains(realTile)
                        && !usedExpectedTiles.contains(expectedTile) ){
                    usedTiles.add(realTile);
                    usedExpectedTiles.add(expectedTile);
                }
            }
        }
        //Log.v("checkChuurenPoutou", "usedTiles is not empty: "+usedTiles.toString());
        //Log.v("checkChuurenPoutou", "usedExpectedTiles is not empty: " + usedExpectedTiles.toString());
        expectedTiles.removeAll(usedExpectedTiles);
        if( expectedTiles.size()!=0 ){
            //Log.v("checkChuurenPoutou", "expectedTiles is not empty: "+expectedTiles.toString());
            return;
        }
        List<Tile> tempList = new ArrayList<>();
        tempList.addAll(h.tiles);
        tempList.removeAll(usedTiles);
        if( tempList.size()>1 ){
            //Log.v("checkChuurenPoutou", "tempList still too big: "+tempList.toString());
            return;
        }
        h.hanList.put(Yaku.Name.CHUURENPOUTOU, 13);

        //TODO implement 9-sided wait... seems like a pain...
        if(false){
            h.hanList.put(Yaku.Name.CHUURENPOUTOU9SIDED, 26);
        }
    }
    private static void checkSuuKantsu(Hand h){
        if( h.meld1.isKan() && h.meld2.isKan() && h.meld3.isKan() && h.meld4.isKan() ){
            h.hanList.put(Yaku.Name.SUUKANTSU, 13);
        }
    }     // Four Kans

    // Controversial Yaku
    private static void checkSanrenkou(Hand h){
        //Get 3+ triplets
        List<Tile> leadTiles = new ArrayList<>();
        if( !h.meld1.isChii() && h.meld1.getSuit()!=Tile.Suit.HONOR ){
            leadTiles.add(h.meld1.firstTile());
        }
        if( !h.meld2.isChii() && h.meld2.getSuit()!=Tile.Suit.HONOR ){
            leadTiles.add(h.meld2.firstTile());
        }
        if( !h.meld3.isChii() && h.meld3.getSuit()!=Tile.Suit.HONOR ){
            leadTiles.add(h.meld3.firstTile());
        }
        if( !h.meld4.isChii() && h.meld4.getSuit()!=Tile.Suit.HONOR ){
            leadTiles.add(h.meld4.firstTile());
        }
        if( leadTiles.size()<3 ){
            return;
        }

        //Cut down to leading tiles where 3+ are in same suit
        int firstTile = 0;
        int secondTile = 0;
        for( Tile t : leadTiles ){
            if( t.suit==leadTiles.get(0).suit ){
                firstTile++;
            } else if( t.suit==leadTiles.get(1).suit ){
                secondTile++;
            }
        }
        if( firstTile<3 && secondTile<3 ){
            return;
        } else if( firstTile==1 && secondTile==3 ){
            leadTiles.remove(0);
        } else if( firstTile==3 && secondTile==1 ){
            leadTiles.remove(1);
        }

        //Ensure they are sequential
        int numbs[] = new int[leadTiles.size()];
        for(int i=0; i<leadTiles.size() ; i++){
            numbs[i] = leadTiles.get(i).number;
        }
        Arrays.sort(numbs);
        for (int i = 0; i < numbs.length - 2; ++i) {
            if( numbs[i]==numbs[i+1]-1 && numbs[i]==numbs[i+2]-2 ){
                h.hanList.put(Yaku.Name.SANRENKOU, 2);
            }
        }
    }     // Three consecutive triplets
    private static void checkSuurenkou(Hand h){
        //Get 4 triplets
        List<Tile> leadTiles = new ArrayList<>();
        if( !h.meld1.isChii() && h.meld1.getSuit()!=Tile.Suit.HONOR ){
            leadTiles.add(h.meld1.firstTile());
        }
        if( !h.meld2.isChii() && h.meld2.getSuit()!=Tile.Suit.HONOR ){
            leadTiles.add(h.meld2.firstTile());
        }
        if( !h.meld3.isChii() && h.meld3.getSuit()!=Tile.Suit.HONOR ){
            leadTiles.add(h.meld3.firstTile());
        }
        if( !h.meld4.isChii() && h.meld4.getSuit()!=Tile.Suit.HONOR ){
            leadTiles.add(h.meld4.firstTile());
        }
        if( leadTiles.size()<4 ){
            return;
        }

        //Cut down to same suit
        int firstSuit = 0;
        for( Tile t : leadTiles ){
            if( t.suit==leadTiles.get(0).suit ){
                firstSuit++;
            }
        }
        if( firstSuit<4 ){
            return;
        }

        //Ensure they are sequential
        int numbs[] = new int[4];
        for(int i=0; i<leadTiles.size() ; i++){
            numbs[i] = leadTiles.get(i).number;
        }
        Arrays.sort(numbs);
        if( numbs[0]==numbs[1]-1 && numbs[1]==numbs[2]-1&& numbs[2]==numbs[3]-1 ){
            h.hanList.put(Yaku.Name.SUURENKOU, 13);
        }
    }     // Four consecutive triplets
    private static void checkDaiSharin(Hand h){
        List<Tile> tz = Utils.findDuplicateTiles(h.tiles);
        if( tz.size()==7
                && Utils.findDuplicateTiles(tz).size()==0
                && !Utils.containsHonorsOrTerminals(h.tiles)
                && countSuits(h.tiles)==1
                ){
            h.hanList.put(Yaku.Name.DAISHARIN, 13);
        }
    }     // Single suit, pairs from 2-8
    private static void checkShiisanpuuta(Hand h){
        if(false){
            h.hanList.put(Yaku.Name.SHIISANPUUTA, 13);
        }
    }     // Thirteen unconnected tiles on first draw
    private static void checkShiisuupuuta(Hand h){
        if(false){
            h.hanList.put(Yaku.Name.SHIISUUPUUTA, 13);
        }
    }     // Fourteen unconnected tiles (on first draw? wut)
    private static void checkParenchan(Hand h){
        if(false){
            h.hanList.put(Yaku.Name.PARENCHAN, 13);
        }
    }        // Eight consecutive wins as dealer


    private static void clearNonYakumanYaku(Hand h){
        List<Yaku.Name> nonYakumanYaku = new ArrayList<>(Arrays.asList(Yaku.Name.values()));
        nonYakumanYaku.removeAll(Yaku.getYakumanNames());

        for(Yaku.Name name : nonYakumanYaku){
            h.hanList.remove(name);
        }
    }

    private static void countDora(Hand h){
        int dora = h.countDora();
        if( h.hanList.size()!=0 && dora!=0){
            h.hanList.put(Yaku.Name.DORA, dora);
        }
    }

    private static int countSuits( List<Tile> s ){
        HashSet<String> noDupSet = new HashSet<>();
        for( Tile t : s ){
            noDupSet.add(t.suit.toString());
        }
        noDupSet.remove("HONOR");
        return noDupSet.size();
    }
}
