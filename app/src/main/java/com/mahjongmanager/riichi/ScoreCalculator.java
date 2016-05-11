package com.mahjongmanager.riichi;

import android.util.Log;

import com.mahjongmanager.riichi.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ScoreCalculator {

    private Hand       unsortedHand;
    private List<Hand> sortedHands;
    public Hand        scoredHand;
    public Hand        validatedHand;

    public ScoreCalculator(){}
    public ScoreCalculator(Hand h){
        unsortedHand = new Hand(h);
        sortedHands = new ArrayList<>();
        processUnsortedHand();

        // TODO validate that hands were processed correctly (have exception yaku or have sets set) as "processed, but not complete"
        //      There is a method to validate a "complete" hand, but not a "processed" hand

        determineScoredHand();
        if( scoredHand!=null && scoredHand.validateCompleteState() ){
            validatedHand = scoredHand;
        }
    }

    ///////////////////////////////////////////////////////////////////
    /////////////////          Main methods           /////////////////
    ///////////////////////////////////////////////////////////////////
    /*
     * Unless the hand is unusual, this will take unsortedHand and interpret in
     * every possible valid hand combination, putting each valid hand combination into
     * sortedHands (the winner will be determined later). This is accomplished by
     * attempting to pull pair/chi/pon/kan off the unsortedTiles list on the hand and slot
     * it into one of the hand's pair/meld slots, creating a clone of the hand for each
     * success. Repeating until all the hands have emptied their unsortedTiles list,
     * or have proven to be invalid combinations of tiles.
     *
     * Due to the unusual structure of kokushiMusou and nagashiMangan, they will skip
     * this step and be set as the scoredHand immediately.
     */
    private void processUnsortedHand(){
        if( unsortedHand==null || unsortedHand.tiles.size()<14 || unsortedHand.tiles.size()>18 || !unsortedHand.tilesSortedProperly() ){
            return;
        }

        //Check for unusually structured hands
        checkKokushiMusou(unsortedHand);
        checkChiitoitsu(unsortedHand);
        checkDaichisei(unsortedHand);
        checkNagashiMangan(unsortedHand);

        //The hand should already be "processed" if it's an unusual hand
        if( scoredHand!=null ){
            return;
        }

        if( unsortedHand.unsortedTiles.size()==0 ){
            scrubScore(unsortedHand);
            scoredHand = unsortedHand;
        }

        // Start the sorting process, now that unusual hands are accounted for
        // Pull out pairs first
        List<Hand> sortingHands = new ArrayList<>();
        Set<Tile> duplicateTiles = findDuplicateTiles(unsortedHand.tiles);

        for( Tile dupeTile : duplicateTiles ){
            if( dupeTile.revealedState==Tile.RevealedState.NONE ){
                Hand sortingHand = new Hand(unsortedHand);
                Tile pairTile1 = sortingHand.popUnsortedTile(dupeTile.value, dupeTile.suit, Tile.RevealedState.NONE);
                Tile pairTile2 = sortingHand.popUnsortedTile(dupeTile.value, dupeTile.suit, Tile.RevealedState.NONE);
                if( pairTile1!=null && pairTile2!=null ){
                    sortingHand.pair.setTiles(Arrays.asList(pairTile1,pairTile2));
                    sortingHands.add(sortingHand);
                }
            }
        }

        slotTilesIntoMelds(sortingHands);
    }
    private void slotTilesIntoMelds( List<Hand> sortingHands ){
        // Iterate over the hand, reading it for each possible combination of chii/pon/kan until all tiles have been examined
        while( sortingHands.size() > 0 ){
            Hand firstHand = sortingHands.get(0);

            // This hand is sorted, remove it from the list
            if( firstHand.unsortedTiles.size()==0 && firstHand.emptyMeldCount()==0 ){
                sortedHands.add(firstHand);
                sortingHands.remove(firstHand);
                continue;
            }

            // It's in an invalid state if there are ever less than 3 tiles in the unsortedTiles list
            if( firstHand.unsortedTiles.size()>=0 && firstHand.unsortedTiles.size()<3 ){
                sortingHands.remove(firstHand);
                continue;
            }

            Hand ponHand = checkForPon(firstHand);
            if( ponHand!=null ){
                sortingHands.add(ponHand);
            }
            Hand kanHand = checkForKan(firstHand);
            if( kanHand!=null ){
                sortingHands.add(kanHand);
            }
            Hand chiiHand = checkForChii(firstHand);
            if( chiiHand!=null ){
                sortingHands.add(chiiHand);
            }

            // Remove the original version of the hand. If it didn't have children, its line ends here
            sortingHands.remove(firstHand);
        }
    }
    private Hand checkForPon(Hand h){
        Tile firstTile = h.unsortedTiles.get(0);
        Hand ponHand = new Hand(h);

        Tile firstPonTile  = ponHand.popUnsortedTile(firstTile.value, firstTile.suit, null);
        Tile secondPonTile = ponHand.popUnsortedTile(firstTile.value, firstTile.suit, firstPonTile.revealedState);
        Tile thirdPonTile  = ponHand.popUnsortedTile(firstTile.value, firstTile.suit, firstPonTile.revealedState);

        if( secondPonTile!=null && thirdPonTile!=null ){
            //Yay, we have a pon!
            ponHand.setMeld(Arrays.asList(firstPonTile, secondPonTile, thirdPonTile));
            return ponHand;
        }
        return null;
    }
    private Hand checkForKan(Hand h){
        Tile firstTile = h.unsortedTiles.get(0);
        if( h.unsortedTiles.size() > 3 ){
            Hand kanHand = new Hand(h);
            Tile firstKanTile  = kanHand.popUnsortedTile(firstTile.value, firstTile.suit, null);
            Tile secondKanTile = kanHand.popUnsortedTile(firstTile.value, firstTile.suit, null);
            Tile thirdKanTile  = kanHand.popUnsortedTile(firstTile.value, firstTile.suit, null);
            Tile fourthKanTile = kanHand.popUnsortedTile(firstTile.value, firstTile.suit, null);

            if( fourthKanTile!=null ){
                //Double Yay! We have a kam!
                kanHand.setMeld(Arrays.asList(firstKanTile, secondKanTile, thirdKanTile, fourthKanTile));
                return kanHand;
            }
        }
        return null;
    }
    private Hand checkForChii(Hand h){
        Tile firstTile = h.unsortedTiles.get(0);
        if( !firstTile.suit.toString().equals("HONOR") && firstTile.number>0 ){
            Hand chiiHand = new Hand(h);

            Tile firstChiiTile = chiiHand.popUnsortedTile(String.valueOf(firstTile.number), firstTile.suit, null);
            Tile secondChiiTile = chiiHand.popUnsortedTile(String.valueOf(firstTile.number + 1), firstTile.suit, firstChiiTile.revealedState);
            Tile thirdChiiTile = chiiHand.popUnsortedTile(String.valueOf(firstTile.number + 2), firstTile.suit, firstChiiTile.revealedState);

            if( secondChiiTile!=null && thirdChiiTile!=null ){
                //Yay, we have a chii!
                chiiHand.setMeld(Arrays.asList(firstChiiTile, secondChiiTile, thirdChiiTile));
                return chiiHand;
            }
        }
        return null;
    }

    /*
     * Only keep the version of the hand that gives the largest score. Set that hand as scoredHand.
     * If any one of the hands in sortedHands passes validation, all the rest must, or be immediately discarded.
     */
    private void determineScoredHand(){
        if( scoredHand!=null ){
            scoreHand(scoredHand);
            countHan(scoredHand);
            countFu(scoredHand);
        } else if( sortedHands.size()>0 ){
            boolean anyHandValidated = false;
            for( Hand unscoredHand : sortedHands ){
                boolean validated = unscoredHand.validateCompleteState();
                anyHandValidated = anyHandValidated || validated;

                if( anyHandValidated && !validated ){
                    continue;
                }

                scoreHand(unscoredHand);
                countHan(unscoredHand);
                countFu(unscoredHand);
                if( scoredHand==null
                        || scoreBasePoints(unscoredHand.han,unscoredHand.fu) > scoreBasePoints(scoredHand.han, scoredHand.fu) ){
                    scoredHand = unscoredHand;
                }
            }
        } else {
            Log.w("incompleteHand", "sortedHands list is empty, and scoredHand is null");
        }
    }

    // Checks hand for all yaku that consist of normal melds (e.g. does NOT check for Chiitoitsu or Kokushi)
    private void scoreHand(Hand h){
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

        if( h.hasYakuman() ){
            clearNormalYaku(h);
            return;
        }

        // Situational
        checkRiichi(h);
        checkNagashiMangan(h);
        checkTsumo(h);
        checkIppatsu(h);
        checkHaitei(h);
        checkHoutei(h);
        checkRinshan(h);
        checkChanKan(h);
        checkDoubleRiichi(h);

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
        checkSuurenkou(h);
        checkDaiSharin(h);
        checkShiisanpuuta(h);
        checkShiisuupuuta(h);
        checkParenchan(h);
    }
    private void countHan(Hand h){
        Integer han = 0;

        if( h.riichi ){
            h.hanList.put("Riichi",1);
            han+=1;
        }
        if(h.tsumo){
            h.hanList.put("Tsumo",1);
            han+=1;
        }
        if(h.ippatsu){
            h.hanList.put("Ippatsu",1);
            han+=1;
        }
        if(h.haitei){
            h.hanList.put("Haitei",1);
            han+=1;
        }
        if(h.houtei){
            h.hanList.put("Houtei",1);
            han+=1;
        }
        if(h.rinshan){
            h.hanList.put("Rinshan",1);
            han+=1;
        }
        if(h.chanKan){
            h.hanList.put("Chan Kan",1);
            han+=1;
        }
        if(h.doubleRiichi){
            h.hanList.put("Double Riichi",2);
            han+=2;
        }
        if(h.chiiToitsu){
            h.hanList.put("Chiitoitsu",2);
            han+=2;
        }
        if(h.pinfu){
            h.hanList.put("Pinfu",1);
            han+=1;
        }
        if(h.iipeikou){
            h.hanList.put("Iipeikou",1);
            han+=1;
        }
        if(h.sanshokuDoujun){
            h.hanList.put("Sanshoku Doujun", (!h.isOpen())?2:1);
            han = (!h.isOpen()) ? han+2 : han+1;
        }
        if(h.ittsuu){
            h.hanList.put("Ittsuu", (!h.isOpen())?2:1);
            han = (!h.isOpen()) ? han+2 : han+1;
        }
        if(h.ryanpeikou){
            h.hanList.put("Ryanpeikou",3);
            han+=3;
        }
        if(h.toitoi){
            h.hanList.put("Toitoi",2);
            han+=2;
        }
        if(h.sanAnkou){
            h.hanList.put("San Ankou",2);
            han+=2;
        }
        if(h.sanshokuDoukou){
            h.hanList.put("Sanshoku Doukou",2);
            han+=2;
        }
        if(h.sanKantsu){
            h.hanList.put("San Kantsu",2);
            han+=2;
        }
        if(h.tanyao){
            h.hanList.put("Tanyao",1);
            han+=1;
        }
        if(h.whiteDragon){
            h.hanList.put("White Dragon",1);
            han+=1;
        }
        if(h.greenDragon){
            h.hanList.put("Green Dragon",1);
            han+=1;
        }
        if(h.redDragon){
            h.hanList.put("Red Dragon",1);
            han+=1;
        }
        if(h.roundWind){
            h.hanList.put("Round Wind",1);
            han+=1;
        }
        if(h.seatWind){
            h.hanList.put("Seat Wind",1);
            han+=1;
        }
        if(h.chanta){
            h.hanList.put("Chanta", (!h.isOpen())?2:1);
            han = (!h.isOpen()) ? han+2 : han+1;
        }
        if(h.junchan){
            h.hanList.put("Junchan", (!h.isOpen())?3:2);
            han = (!h.isOpen()) ? han+3 : han+2;
        }
        if(h.honroutou){
            h.hanList.put("Honroutou",2);
            han+=2;
        }
        if(h.shousangen){
            h.hanList.put("Shousangen",2);
            han+=2;
        }
        if(h.honitsu){
            h.hanList.put("Honitsu", (!h.isOpen())?3:2);
            han = (!h.isOpen()) ? han+3 : han+2;
        }
        if(h.chinitsu){
            h.hanList.put("Chinitsu", (!h.isOpen())?6:5);
            han = (!h.isOpen()) ? han+6 : han+5;
        }
        if(h.kokushiMusou){
            h.hanList.put("Kokushi Musou",13);
            han+=13;
        } else if(h.kokushiMusou13wait){
            h.hanList.put("Kokushi Musou [13 wait]",26);
            han+=26;
        }
        if(h.suuAnkou){
            h.hanList.put("Suu Ankou",13);
            han+=13;
        } else if(h.suuAnkouTanki){
            h.hanList.put("Suu Ankou [Tanki]",26);
            han+=26;
        }
        if(h.daisangen){
            h.hanList.put("Daisangen",13);
            han=+13;
        }
        if(h.shousuushii){
            h.hanList.put("Shousuushii",13);
            han+=13;
        }
        if(h.daisuushii){
            h.hanList.put("Daisuushii",26);
            han+=26;
        }
        if(h.tsuuiisou){
            h.hanList.put("Tsuuiisou",13);
            han+=13;
        }
        if(h.daichisei){
            h.hanList.put("Daichisei",26);
            han+=26;
        }
        if(h.chinroutou){
            h.hanList.put("Chinroutou",13);
            han+=13;
        }
        if(h.ryuuiisou){
            h.hanList.put("Ryuuiisou",13);
            han+=13;
        }
        if(h.chuurenPoutou){
            h.hanList.put("Chuuren Poutou",13);
            han+=13;
        } else if(h.chuurenPoutou9wait){
            h.hanList.put("Chuuren Poutou [9 wait]",26);
            han+=26;
        }
        if(h.suuKantsu){
            h.hanList.put("Suu Kantsu",13);
            han+=13;
        }
        if(h.sanrenkou){
            h.hanList.put("San Renkou",2);
            han+=2;
        }
        if(h.suurenkou){
            h.hanList.put("Suu Renkou",13);
            han+=13;
        }
        if(h.daiSharin){
            h.hanList.put("Daisharin",13);
            han+=13;
        }
        if(h.shiisanpuuta){
            h.hanList.put("Shiisanpuuta",13);
            han+=13;
        }
        if(h.shiisuupuuta){
            h.hanList.put("Shiisuupuuta",13);
            han+=13;
        }
        if(h.parenchan){
            h.hanList.put("Parenchan",13);
            han+=13;
        }
        if(h.nagashiMangan){
            h.hanList.put("Nagashi Mangan",5);
            han=5;
        }
        if(h.dora!=0){
            h.hanList.put("Dora",h.dora);
            han+=h.dora;
        }

        h.han = han;
    }
    private void countFu(Hand h){
        if( h.hasYakuman() || h.nagashiMangan ){
            return;
        }

        // (0) Fu starts at 20
        h.fuList.put("Fuutei", 20);
        Integer fu = 20;

        // (1) Set fu for chiitoitsu
        if( h.chiiToitsu ){
            h.fuList.clear();
            h.fuList.put("Chiitoitsu", 25);
            h.fu = 25;
            return;
        }

        // (2,3,4) Add fu for closed hand, then melds/pair, then wait
        fu += fuFromClosedHand(h);
        fu += fuFromPair(h);
        fu += fuFromMelds(h);
        fu += fuFromWait(h);

        // (5) Add fu for tsumo (pinfu exception)
        if( h.pinfu ){
            boolean isClosed = h.fuList.containsKey("Menzen-Kafu");
            h.fuList.clear();
            h.fuList.put( "Pinfu", 20);
            fu = 20;
            if( isClosed ){
                h.fuList.put("Menzen-Kafu", 10);
                fu += 10;
            }
        } else if( h.selfDrawWinningTile ) {
            h.fuList.put( "Self Draw Winning Tile", 2);
            fu += 2;
        }

        // (6) Set fu for open pinfu
        if( h.fuList.size()==1 && !h.pinfu ){
            h.fuList.put( "Open Pinfu", 10);
            fu += 10;
        }

        h.fu = fu;
    }
    private int fuFromClosedHand(Hand h){
        int closedFu = 0;
        Boolean isOpen = false;
        for( Tile t : h.tiles ){
            if( !t.winningTile ){
                if( t.revealedState!= Tile.RevealedState.NONE && t.revealedState!= Tile.RevealedState.CLOSEDKAN ){
                    isOpen = true;
                }
            }
        }
        if( !isOpen && h.getWinningTile()!=null && h.getWinningTile().calledFrom!=Tile.CalledFrom.NONE ){
            h.fuList.put("Menzen-Kafu", 10);
            closedFu += 10;
        }
        return closedFu;
    }
    private int fuFromPair(Hand h){
        int pairFu = 0;
        if( h.pair.firstTile().dragon!=null ){
            switch (h.pair.firstTile().dragon){
                case WHITE:
                    h.fuList.put( "White Dragon Pair", 2);
                    break;
                case GREEN:
                    h.fuList.put( "Green Dragon Pair", 2);
                    break;
                case RED:
                    h.fuList.put( "Red Dragon Pair", 2);
                    break;
            }
            pairFu += 2;
        } else if( h.pair.firstTile().wind==h.prevailingWind ){
            h.fuList.put( "Prevailing Wind", 2);
            pairFu += 2;
        }
        if( h.pair.firstTile().wind==h.playerWind ){
            h.fuList.put( "Seat Wind", 2);
            pairFu += 2;        // Double counting here for pairs of winds not allowed in all rulesets
        }
        return pairFu;
    }
    private int fuFromMelds(Hand h){
        int allMeldsFu = 0;
        allMeldsFu += fuFromMeld( h, h.meld1, "Meld 1" );
        allMeldsFu += fuFromMeld( h, h.meld2, "Meld 2" );
        allMeldsFu += fuFromMeld( h, h.meld3, "Meld 3" );
        allMeldsFu += fuFromMeld( h, h.meld4, "Meld 4" );
        return allMeldsFu;
    }
    private int fuFromMeld( Hand h, Meld meld, String label ){
        Integer meldFu = 0;
        if( !meld.isChii() ){
            meldFu += 2;
            meldFu = (meld.isClosed())                                       ? meldFu*2 : meldFu;
            meldFu = (Utils.containsHonorsOrTerminalsOnly(meld.getTiles()))  ? meldFu*2 : meldFu;
            meldFu = (meld.isKan())                                          ? meldFu*4 : meldFu;
            h.fuList.put( label, meldFu);
        }
        return meldFu;
    }
    private int fuFromWait(Hand h){
        int waitFu = 0;
        Meld winningMeld = h.getWinningMeld();
        if( winningMeld!=null && winningMeld.isPair() ){
            h.fuList.put( "Pair Wait", 2);
            waitFu += 2;
        } else if( winningMeld!=null && winningMeld.isChii() ){
            if( winningMeld.thirdTile().winningTile && winningMeld.firstTile().number==1 && winningMeld.secondTile().number==2 && winningMeld.thirdTile().number==3 ){
                // Wait on a 3 for a 1-2-3 meld
                h.fuList.put( "Single-sided Wait", 2);
                waitFu += 2;
            } else if( winningMeld.firstTile().winningTile && winningMeld.firstTile().number==7 && winningMeld.secondTile().number==8 && winningMeld.thirdTile().number==9 ){
                // Wait on a 7 for a 7-8-9 meld
                h.fuList.put( "Single-sided Wait", 2);
                waitFu += 2;
            } else if( winningMeld.secondTile().winningTile ){
                // Wait on an inside tile for a chii
                h.fuList.put( "Inside Wait", 2);
                waitFu += 2;
            }
        }
        return waitFu;
    }

    public static Integer scoreBasePoints( Integer han, Integer fu ){
        Integer roundedFu = (fu==25) ? 25 : (int) Math.ceil(fu/10.0)*10;
        Double value = roundedFu * Math.pow(2,2+han);
        if( han>4 ){
            return getBasePointsForLargeHand(han, value.intValue());
        }
        return value.intValue();
    }
    public static String scoreHanFu( Integer han, Integer fu, Boolean dealer, Boolean tsumo ){
        Integer value = scoreBasePoints(han, fu);
        Integer childValue = 0;

        // Exclude invalid hands or hands capped by mangan rules
        // Mangan threshold for base points = 2000
        if( han==0 || (han==1&&fu==20) || (han==1&&fu==25) || (han==2&&fu==25&&tsumo) ){
            return "---";
        } else if( han < 5 && value >= 2000 ){
            if( !dealer && tsumo ){
                return "2000/4000";             // Child tsumo
            } else if( dealer && tsumo ){
                return "4000";                  // Dealer tsumo
            } else if( !dealer ){
                return "8000";                  // Child ron
            } else {
                return "12000";                 // Dealer ron
            }
        }

        value = getBasePointsForLargeHand(han, value);

        if( !tsumo && !dealer ){
            value = 4*value;                // Child Ron
        } else if( tsumo && !dealer ){
            childValue = value;             // Child Tsumo
            value = 2*value;
        } else if( !tsumo ){
            value = 6*value;                // Dealer Ron
        } else {
            value = 2*value;                // Dealer Tsumo
        }

        //format string
        int roundedV = ((value + 99) / 100 ) * 100;
        int roundedCV = ((childValue + 99) / 100 ) * 100;
        return roundedCV==0 ? String.valueOf(roundedV) : String.valueOf(roundedCV) + '/' + String.valueOf(roundedV);
    }
    private static int getBasePointsForLargeHand(int han, int base){
        if( han==5 ){
            return 2000;
        } else if( han==6 || han==7 ){
            return 3000;
        } else if( han==8 || han==9 || han==10 ){
            return 4000;
        } else if( han==11 || han==12 ){
            return 6000;
        } else if( han>=13 && han<26 ){
            return 8000;
        } else if( han>=26 && han<39 ){
            return 16000;
        } else if( han>=39 && han<52 ){
            return 24000;
        } else if( han>=52 && han<65 ){
            return 32000;
        } else if( han>=65 ){
            return 40000;
        }
        return base;
    }


    ///////////////////////////////////////////////////////////////////////
    /////////////////          Internal methods           /////////////////
    ///////////////////////////////////////////////////////////////////////
    // Convenience methods
    private int countSuits( List<Tile> s ){
        HashSet<String> noDupSet = new HashSet<>();
        for( Tile t : s ){
            noDupSet.add(t.suit.toString());
        }
        noDupSet.remove("HONOR");
        return noDupSet.size();
    }
    private int countTileInSet( Tile t, List<Tile> s ){
        int count = 0;
        for( Tile setTile : s ){
            if( setTile.toString().equals(t.toString()) ){
                count++;
            }
        }
        return count;
    }

    // Unusually structed hands (must be handled seperately)
    private void checkChiitoitsu(Hand h){
        Set<Tile> tz = findDuplicateTiles(h.tiles);
        if(tz.size()==7 && findDuplicateTiles(tz).size()==0 ) {
            Hand chiitoitsuHand = new Hand(h);

            checkTanyao(chiitoitsuHand);
            checkHonroutou(chiitoitsuHand);
            checkHonitsu(chiitoitsuHand);
            checkChinitsu(chiitoitsuHand);

            chiitoitsuHand.unsortedTiles.clear();
            chiitoitsuHand.chiiToitsu=true;
            countHan(chiitoitsuHand);
            chiitoitsuHand.fu = 25;

            sortedHands.add(chiitoitsuHand);
        }
    }
    private void checkDaichisei(Hand h){
        Set<Tile> tz = findDuplicateTiles(h.tiles);
        Set<Tile> doubleDupes = findDuplicateTiles(tz);
        int suitCount = countSuits(h.tiles);
        if( tz.size()==7 && doubleDupes.size()==0 && suitCount==0 ){
            h.unsortedTiles.clear();
            h.daichisei = true;
            clearNormalYaku(h);
            scoredHand = h;
            countHan(h);
        }
    }     // 7 pairs of all honors
    private void checkKokushiMusou(Hand h){
        if( !Utils.containsHonorsOrTerminalsOnly(h.tiles) ){
            return;
        }

        //Different tiles
        HashSet<String> noDupSet = new HashSet<>();
        for( Tile t : h.tiles ){
            noDupSet.add(t.toString());
        }
        if( noDupSet.size()==13 && h.getWinningTile()!=null && countTileInSet(h.getWinningTile(), h.tiles)==2 ){
            h.unsortedTiles.clear();
            h.kokushiMusou13wait = true;
            clearNormalYaku(h);
            scoredHand=h;
            countHan(scoredHand);
        } else if( noDupSet.size()==13 ){
            h.unsortedTiles.clear();
            h.kokushiMusou = true;
            clearNormalYaku(h);
            scoredHand=h;
            countHan(scoredHand);
        }
    }
    private void checkNagashiMangan(Hand h){
        if( h.nagashiMangan ){
            h.unsortedTiles.clear();
            scoredHand=h;
            validatedHand=h;
            countHan(validatedHand);
        }
    }


    // Circumstantial Yaku
    private void checkDoubleRiichi(Hand h){}
    private void checkRiichi(Hand h){}
    private void checkTsumo(Hand h){
        for( Tile t : h.tiles ){
            if( t.winningTile && t.calledFrom==Tile.CalledFrom.NONE ){
                h.selfDrawWinningTile = true;
            }
        }
        if( !h.isOpen() && h.selfDrawWinningTile ){
            h.tsumo = (h.selfDrawWinningTile);
        }
    }
    private void checkIppatsu(Hand h){}
    private void checkHaitei(Hand h){}
    private void checkHoutei(Hand h){}
    private void checkRinshan(Hand h){
        if( h.rinshan ){
            h.tsumo = false;
        }
    }
    private void checkChanKan(Hand h){}

    // Standard Yaku
    private void checkPinfu(Hand h){
        //If no winning tile has been set, it's ok to still to count this as pinfu
        if( h.isOpen() || fuFromMelds(h)!=0 || fuFromPair(h)!=0 || fuFromWait(h)!=0 ){
            h.pinfu = false;
            return;
        }
        h.pinfu = true;
    }
    private void checkIipeikou(Hand h){
        if( !h.isOpen() ){
            if(        h.meld1.toString().equals(h.meld2.toString()) ){
                h.iipeikou = true;
            } else if( h.meld1.toString().equals(h.meld3.toString()) ){
                h.iipeikou = true;
            } else if( h.meld1.toString().equals(h.meld4.toString()) ){
                h.iipeikou = true;
            } else if( h.meld2.toString().equals(h.meld3.toString()) ){
                h.iipeikou = true;
            } else if( h.meld2.toString().equals(h.meld4.toString()) ){
                h.iipeikou = true;
            } else if( h.meld3.toString().equals(h.meld4.toString()) ){
                h.iipeikou = true;
            }
        }
    }
    private void checkSanshokuDoujun(Hand h){
        //Get 3+ chiis
        List<Tile> leadTiles = new ArrayList<>();
        if( h.meld1.isChii() && h.meld1.getSuit()!=Tile.Suit.HONOR ){
            leadTiles.add(h.meld1.firstTile());
        }
        if( h.meld2.isChii() && h.meld2.getSuit()!=Tile.Suit.HONOR ){
            leadTiles.add(h.meld2.firstTile());
        }
        if( h.meld3.isChii() && h.meld3.getSuit()!=Tile.Suit.HONOR ){
            leadTiles.add(h.meld3.firstTile());
        }
        if( h.meld4.isChii() && h.meld4.getSuit()!=Tile.Suit.HONOR ){
            leadTiles.add(h.meld4.firstTile());
        }
        if( leadTiles.size()<3 ){
            return;
        }

        //Same number (remove the ones that don't match number)
        int firstTile = 0;
        int secondTile = 0;
        for( Tile t : leadTiles ){
            if( t.number.equals(leadTiles.get(0).number) ){
                firstTile++;
            } else if( t.number.equals(leadTiles.get(1).number) ){
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
            h.sanshokuDoujun = true;
        }
    }
    private void checkIttsuu(Hand h){
        //find dominant suit first
        final Set<String> duplicateSuits = new HashSet<>();
        final Set<String> tempSet = new HashSet<>();
        final Set<String> tempSet2 = new HashSet<>();
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
        Boolean firstMeld = false;
        Boolean secondMeld = false;
        Boolean thirdMeld = false;
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
            h.ittsuu = true;
        }
    }
    private void checkRyanpeikou(Hand h){
        if(        h.meld1.toString().equals(h.meld2.toString()) && h.meld3.toString().equals(h.meld4.toString()) ){
            h.ryanpeikou = true;
            h.iipeikou = false;
        } else if( h.meld1.toString().equals(h.meld3.toString()) && h.meld2.toString().equals(h.meld4.toString()) ){
            h.ryanpeikou = true;
            h.iipeikou = false;
        } else if( h.meld1.toString().equals(h.meld4.toString()) && h.meld2.toString().equals(h.meld3.toString()) ){
            h.ryanpeikou = true;
            h.iipeikou = false;
        }
    }
    private void checkToitoi(Hand h){
        if( !h.meld1.isChii() && !h.meld2.isChii() && !h.meld3.isChii() && !h.meld4.isChii() ){
            h.toitoi=true;
        }
    }
    private void checkSanAnkou(Hand h){
        Integer closedTriplets = 0;
        if( !h.meld1.isChii() && h.meld1.isClosed() ){
            closedTriplets += 1;
        }
        if( !h.meld2.isChii() && h.meld2.isClosed() ){
            closedTriplets += 1;
        }
        if( !h.meld3.isChii() && h.meld3.isClosed() ){
            closedTriplets += 1;
        }
        if( !h.meld4.isChii() && h.meld4.isClosed() ){
            closedTriplets += 1;
        }

        h.sanAnkou= (closedTriplets>=3);
    }
    private void checkSanshokuDoukou(Hand h){
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
            if( t.number.equals(leadTiles.get(0).number) ){
                firstTile++;
            } else if( t.number.equals(leadTiles.get(1).number) ){
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
            h.sanshokuDoukou = true;
        }
    }
    private void checkSanKantsu(Hand h){
        Integer kanNumber = 0;
        kanNumber = (h.meld1.isKan()) ? kanNumber+1 : kanNumber;
        kanNumber = (h.meld2.isKan()) ? kanNumber+1 : kanNumber;
        kanNumber = (h.meld3.isKan()) ? kanNumber+1 : kanNumber;
        kanNumber = (h.meld4.isKan()) ? kanNumber+1 : kanNumber;
        if( kanNumber==3 ){
            h.sanKantsu = true;
        }
    }
    private void checkTanyao(Hand h){
        if( !Utils.containsHonorsOrTerminals(h.tiles) ){
            h.tanyao=true;
        }
    }
    private void checkYakuhai(Hand h){
        h.whiteDragon = h.hasDragonWhiteSet();
        h.greenDragon = h.hasDragonGreenSet();
        h.redDragon   = h.hasDragonRedSet();
        h.roundWind   = h.hasPrevailingWindSet();
        h.seatWind    = h.hasPlayerWindSet();
    }
    private void checkJunchan(Hand h){
        if( Utils.containsTerminals(h.pair.getTiles())
                && Utils.containsTerminals(h.meld1.getTiles())
                && Utils.containsTerminals(h.meld2.getTiles())
                && Utils.containsTerminals(h.meld3.getTiles())
                && Utils.containsTerminals(h.meld4.getTiles()) ){
            h.junchan = true;
        }
    }
    private void checkHonroutou(Hand h){
        if( Utils.containsHonorsOrTerminalsOnly(h.tiles) ){
            h.honroutou = true;
        }
    }
    private void checkChanta(Hand h){
        if( Utils.containsHonorsOrTerminals(h.pair.getTiles())
                && Utils.containsHonorsOrTerminals(h.meld1.getTiles())
                && Utils.containsHonorsOrTerminals(h.meld2.getTiles())
                && Utils.containsHonorsOrTerminals(h.meld3.getTiles())
                && Utils.containsHonorsOrTerminals(h.meld4.getTiles())
                && !h.honroutou
                && !h.junchan ){
            h.chanta = true;
        }
    }
    private void checkShousangen(Hand h){
        HashSet<String> noDupSet = new HashSet<>();
        if( h.pair.firstTile().dragon!=null ){
            noDupSet.add(h.pair.firstTile().dragon.toString());
        }
        if( h.whiteDragon ){
            noDupSet.add("WHITE");
        }
        if( h.greenDragon ){
            noDupSet.add("GREEN");
        }
        if( h.redDragon ){
            noDupSet.add("RED");
        }
        if( noDupSet.size()==3 && (!h.whiteDragon||!h.greenDragon||!h.redDragon) ){
            h.shousangen = true;
        }
    }
    private void checkHonitsu(Hand h){
        if( Utils.containsHonors(h.tiles) && countSuits(h.tiles)==1 ){
            h.honitsu = true;
        }
    }
    private void checkChinitsu(Hand h){
        if( !Utils.containsHonors(h.tiles) && countSuits(h.tiles)==1 ){
            h.chinitsu = true;
        }
    }

    // Yakuman
    private void checkSuuAnkou(Hand h){
        int selfDrawnTriplets = 0;
        for(Meld m : Arrays.asList(h.meld1, h.meld2, h.meld3, h.meld4)){
            if( !m.isPair() && !m.isChii() && m.isClosed() ){
                selfDrawnTriplets++;
            }
        }
        if( selfDrawnTriplets==4 ){
            if( h.pair.hasWinningTile() ){
                h.suuAnkou = false;
                h.suuAnkouTanki = true;
            } else {
                h.suuAnkou = true;
            }
        }
    }      // Four concealed triplets (double for pair wait)
    private void checkDaisangen(Hand h){
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
            h.daisangen = true;
        }
    }
    private void checkShousuushii(Hand h){
        List<Tile.Wind> winds = new ArrayList<>(Arrays.asList(Tile.Wind.values()));
        for( Tile.Wind w : Tile.Wind.values() ){
            if( countTileInSet(new Tile(w.toString(), "HONOR"),h.tiles)==3
                    || countTileInSet(new Tile(w.toString(), "HONOR"),h.tiles)==4 ){
                winds.remove(w);
            }
        }
        if( winds.size()==0 ){
            return;
        }

        int fourthWindCount = countTileInSet(new Tile(winds.get(0).toString(), "HONOR"), h.tiles);
        if(winds.size()==1 && fourthWindCount==2 ){
            h.shousuushii = true;
        }
    }   // Three triplets and a pair of winds
    private void checkDaisuushii(Hand h){
        List<Tile.Wind> winds = new ArrayList<>(Arrays.asList(Tile.Wind.values()));
        for( Tile.Wind w : Tile.Wind.values() ){
            int tileCount = countTileInSet(new Tile(w.toString(), "HONOR"),h.tiles);
            if( tileCount==3 || tileCount==4 ){
                winds.remove(w);
            }
        }

        if( winds.size()==0 ){
            h.daisuushii = true;
        }
    }    // Four triplets of winds
    private void checkTsuuiisou(Hand h){
        if( countSuits(h.tiles)==0 && !h.chiiToitsu ){
            h.tsuuiisou = true;
        }
    }     // All honors
    private void checkChinroutou(Hand h){
        if( !Utils.containsHonors(h.tiles) && !Utils.containsSimples(h.tiles) ){
            h.chinroutou = true;
        }
    }    // All terminals
    private void checkRyuuiisou(Hand h){
        for(Tile t : h.tiles){
            if( t.suit==Tile.Suit.HONOR && t.dragon!=Tile.Dragon.GREEN ){
                return;
            } else if( t.number!=null && (t.number==1||t.number==5||t.number==7||t.number==9) ){
                return;
            }
        }
        h.ryuuiisou = true;
    }     // All green
    private void checkChuurenPoutou(Hand h){
        if( countSuits(h.tiles)!=1 || Utils.containsHonors(h.tiles) ){
            return;
        }

        Tile.Suit suit = h.tiles.get(0).suit;
        List<Tile> expectedTiles = new ArrayList<>();
        for(int i=1; i<10; i++){
            expectedTiles.add(new Tile(i, suit.toString()));
        }
        expectedTiles.add(new Tile(1, suit.toString()));
        expectedTiles.add(new Tile(1, suit.toString()));
        expectedTiles.add(new Tile(9, suit.toString()));
        expectedTiles.add(new Tile(9, suit.toString()));

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
        h.chuurenPoutou = true;
        //TODO implement 9-sided wait... seems like a pain...
    }
    private void checkSuuKantsu(Hand h){
        if( h.meld1.isKan() && h.meld2.isKan() && h.meld3.isKan() && h.meld4.isKan() ){
            h.suuKantsu = true;
        }
    }     // Four Kans

    // Controversial Yaku
    private void checkSanrenkou(Hand h){
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
                h.sanrenkou = true;
            }
        }
    }     // Three consecutive triplets
    private void checkSuurenkou(Hand h){
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
            h.suurenkou = true;
        }
    }     // Four consecutive triplets
    private void checkDaiSharin(Hand h){
        Set<Tile> tz = findDuplicateTiles(h.tiles);
        if( tz.size()==7
                && findDuplicateTiles(tz).size()==0
                && !Utils.containsHonorsOrTerminals(h.tiles)
                && countSuits(h.tiles)==1 ){
            h.daiSharin = true;
        }
    }     // Single suit, pairs from 2-8
    private void checkShiisanpuuta(Hand h){}     // Thirteen unconnected tiles on first draw
    private void checkShiisuupuuta(Hand h){}     // Fourteen unconnected tiles (on first draw? wut)
    private void checkParenchan(Hand h){}        // Eight consecutive wins as dealer

    private void scrubScore(Hand h){
        clearYakuman(h);
        clearNormalYaku(h);
        h.hanList.clear();
    }
    private void clearYakuman(Hand h){
        h.kokushiMusou = false;
        h.kokushiMusou13wait = false;
        h.suuAnkou = false;
        h.suuAnkouTanki = false;
        h.daisangen = false;
        h.shousuushii = false;
        h.daisuushii = false;
        h.tsuuiisou = false;
        h.daichisei = false;
        h.chinroutou = false;
        h.ryuuiisou = false;
        h.chuurenPoutou = false;
        h.chuurenPoutou9wait = false;
        h.suuKantsu = false;
        h.suurenkou = false;
        h.daiSharin = false;
        h.shiisanpuuta = false;
        h.shiisuupuuta = false;
        h.parenchan = false;
    }
    private void clearNormalYaku(Hand h){
        h.chiiToitsu = false;
        h.pinfu = false;
        h.iipeikou = false;
        h.sanshokuDoujun = false;
        h.ittsuu = false;
        h.ryanpeikou = false;
        h.toitoi = false;
        h.sanAnkou = false;
        h.sanshokuDoukou = false;
        h.sanKantsu = false;
        h.tanyao = false;
        h.whiteDragon = false;
        h.greenDragon = false;
        h.redDragon = false;
        h.roundWind = false;
        h.seatWind = false;
        h.chanta = false;
        h.junchan = false;
        h.honroutou = false;
        h.shousangen = false;
        h.honitsu = false;
        h.chinitsu = false;
        h.sanrenkou = false;
        h.dora = 0;
        h.fu = 0;
        h.fuList.clear();
    }

    private Set<Tile> findDuplicateTiles( Collection<Tile> s ){
        final Set<Tile> duplicateTiles = new HashSet<>();
        final Set<String> tempSet = new HashSet<>();

        for (Tile dupTile : s ){
            if (!tempSet.add(dupTile.toString())) {
                duplicateTiles.add(dupTile);
            }
        }
        return duplicateTiles;
    }
}
