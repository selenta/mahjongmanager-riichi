package com.mahjongmanager.riichi;

import android.util.Log;

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
        if( unsortedHand==null || unsortedHand.tiles.size()<14 || unsortedHand.tiles.size()>18 ){
            return;
        }

        //Check for unusually structured hands
        checkKokushiMusou(unsortedHand);
        checkChiitoitsu(unsortedHand);
        checkNagashiMangan(unsortedHand);

        //The hand should already be "processed" if it's an unusual hand
        if( scoredHand!=null && (scoredHand.kokushiMusou||scoredHand.nagashiMangan) ){
            return;
        }

        if( unsortedHand.unsortedTiles.size()==0 ){
            //TODO scrub hand, so it can be rescored? or directly assign it to validated? (scrub is definitely cleaner)
            scoredHand = unsortedHand;
            validatedHand = unsortedHand;
        } else if( unsortedHand.set1.size()+unsortedHand.set2.size()
                  +unsortedHand.set3.size()+unsortedHand.set4.size()
                  +unsortedHand.unsortedTiles.size() != unsortedHand.tiles.size() ){
            Log.wtf("InvalidHandState", "tiles list size does not match the number of tiles used in sets/unsortedTiles: \n" + unsortedHand.toStringVerbose());
            return;
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
                    sortingHand.pair.addAll(Arrays.asList(pairTile1,pairTile2));
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

            Hand chiiHand = checkForChii(firstHand);
            if( chiiHand!=null ){
                sortingHands.add(chiiHand);
            }
            Hand ponHand = checkForPon(firstHand);
            if( ponHand!=null ){
                sortingHands.add(ponHand);
            }
            Hand kanHand = checkForKan(firstHand);
            if( kanHand!=null ){
                sortingHands.add(kanHand);
            }

            //TODO Check if tiles marked as revealed are appropriately grouped
            // Remove the original version of the hand. If it didn't have children, its line ends here
            sortingHands.remove(firstHand);
        }
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
                chiiHand.setSet(Arrays.asList(firstChiiTile, secondChiiTile, thirdChiiTile));
                return chiiHand;
            }
        }
        return null;
    }
    private Hand checkForPon(Hand h){
        Tile firstTile = h.unsortedTiles.get(0);
        Hand ponHand = new Hand(h);

        Tile firstPonTile  = ponHand.popUnsortedTile(firstTile.value, firstTile.suit, null);
        Tile secondPonTile = ponHand.popUnsortedTile(firstTile.value, firstTile.suit, firstPonTile.revealedState);
        Tile thirdPonTile  = ponHand.popUnsortedTile(firstTile.value, firstTile.suit, firstPonTile.revealedState);

        if( secondPonTile!=null && thirdPonTile!=null ){
            //Yay, we have a pon!
            ponHand.setSet(Arrays.asList(firstPonTile, secondPonTile, thirdPonTile));
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
                kanHand.setSet(Arrays.asList(firstKanTile, secondKanTile, thirdKanTile, fourthKanTile));
                return kanHand;
            }
        }
        return null;
    }

    /*
     * Only keep the version of the hand that gives the largest score. Set that hand as scoredHand.
     */
    private void determineScoredHand(){
        if( scoredHand!=null ){
            scoreHand(scoredHand);
            countHan(scoredHand);
            countFu(scoredHand);
        } else if( sortedHands.size()>0 ){
            scoredHand = sortedHands.get(0);
            for( Hand unscoredHand : sortedHands ){
                scoreHand(unscoredHand);
                countHan(unscoredHand);
                countFu(unscoredHand);
                if( scoreBasePoints(unscoredHand.han,unscoredHand.fu) > scoreBasePoints(scoredHand.han, scoredHand.fu) ){
                    scoredHand = unscoredHand;
                }
            }
        } else {
            Log.w("incompleteHand", "sortedHands list is empty, and scoredHand is null");
        }
    }

    // Checks hand for all yaku that consist of normal melds (e.g. does NOT check for Chiitoitsu or Kokushi)
    private void scoreHand(Hand h){
        if( h.kokushiMusou || h.chiiToitsu || h.nagashiMangan ){
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
            clearNonYakuman(h);
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
            h.fuList.clear();
            h.fuList.put( "Pinfu", 20);
            fu = 20;
        } else if( h.selfDrawWinningTile ) {
            h.fuList.put( "Self Draw Winning Tile", 2);
            fu += 2;
        }

        // (6) Set fu for open pinfu
        //      TODO Handle open pinfu here?

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
        if( h.pair.get(0).dragon!=null ){
            switch (h.pair.get(0).dragon){
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
        } else if( h.pair.get(0).wind==h.prevailingWind ){
            h.fuList.put( "Prevailing Wind", 2);
            pairFu += 2;
        }
        if( h.pair.get(0).wind==h.playerWind ){
            h.fuList.put( "Seat Wind", 2);
            pairFu += 2;        // Double counting here for pairs of winds not allowed in all rulesets
        }
        return pairFu;
    }
    private int fuFromMelds(Hand h){
        int allMeldsFu = 0;
        allMeldsFu += fuFromMeld( h, h.set1, "Meld 1" );
        allMeldsFu += fuFromMeld( h, h.set2, "Meld 2" );
        allMeldsFu += fuFromMeld( h, h.set3, "Meld 3" );
        allMeldsFu += fuFromMeld( h, h.set4, "Meld 4" );
        return allMeldsFu;
    }
    private int fuFromMeld( Hand h, List<Tile> meld, String label ){
        Integer meldFu = 0;
        if( !isChii(meld) ){
            meldFu += 2;
            meldFu = (!isSetOpenOrWinningTile(meld))        ? meldFu*2 : meldFu;
            meldFu = (containsHonorsOrTerminalsOnly(meld))  ? meldFu*2 : meldFu;
            meldFu = (isKan(meld))                          ? meldFu*4 : meldFu;
            h.fuList.put( label, meldFu);
        }
        return meldFu;
    }
    private int fuFromWait(Hand h){
        int waitFu = 0;
        List<Tile> winningMeld = new ArrayList<>();
        winningMeld = (containsWinningTile(h.pair)) ? h.pair : winningMeld;
        winningMeld = (containsWinningTile(h.set1)) ? h.set1 : winningMeld;
        winningMeld = (containsWinningTile(h.set2)) ? h.set2 : winningMeld;
        winningMeld = (containsWinningTile(h.set3)) ? h.set3 : winningMeld;
        winningMeld = (containsWinningTile(h.set4)) ? h.set4 : winningMeld;
        if( isPair(winningMeld) ){
            h.fuList.put( "Pair Wait", 2);
            waitFu += 2;
        } else if( isChii(winningMeld) ){
            if( winningMeld.get(2).winningTile && winningMeld.get(0).number==1 && winningMeld.get(1).number==2 && winningMeld.get(2).number==3 ){
                // Wait on a 3 for a 1-2-3 meld
                h.fuList.put( "Single-sided Wait", 2);
                waitFu += 2;
            } else if( winningMeld.get(0).winningTile && winningMeld.get(0).number==7 && winningMeld.get(1).number==8 && winningMeld.get(2).number==9 ){
                // Wait on a 7 for a 7-8-9 meld
                h.fuList.put( "Single-sided Wait", 2);
                waitFu += 2;
            } else if( winningMeld.get(1).winningTile ){
                // Wait on an inside tile for a chii
                h.fuList.put( "Inside Wait", 2);
                waitFu += 2;
            }
        }
        return waitFu;
    }

    public Integer scoreBasePoints( Integer han, Integer fu ){
        Integer roundedFu = (fu==25) ? 25 : (int) Math.ceil(fu/10.0)*10;
        Double value = roundedFu * Math.pow(2,2+han);
        if( han>4 ){
            return getBasePointsForLargeHand(han, value.intValue());
        }
        return value.intValue();
    }
    public String scoreHanFu( Integer han, Integer fu, Boolean dealer, Boolean tsumo ){
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
    private int getBasePointsForLargeHand(int han, int base){
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
    private boolean isPair( List<Tile> set ){
        return set.size()==2 && set.get(0).value.equals(set.get(1).value);
    }
    private boolean isChii( List<Tile> set ){
        return set.size()==3 && !set.get(0).value.equals(set.get(1).value);
    }
    private boolean isPon( List<Tile> set ){
        return set.size()==3 && set.get(0).value.equals(set.get(1).value);
    }
    private boolean isKan( List<Tile> set ){
        return set.size()==4 && set.get(0).value.equals(set.get(1).value);
    }
    private boolean isSetOpen( List<Tile> s ){
        for( Tile t : s ){
            if( t.revealedState!=Tile.RevealedState.NONE
                    && t.revealedState!=Tile.RevealedState.CLOSEDKAN ){
                return true;
            }
        }
        return false;
    }
    private boolean isSetOpenOrWinningTile( List<Tile> s ){
        for( Tile t : s ){
            if( (t.revealedState!=Tile.RevealedState.NONE && t.revealedState!=Tile.RevealedState.CLOSEDKAN)
                    || t.calledFrom!=Tile.CalledFrom.NONE ){
                return true;
            }
        }
        return false;
    }

    private boolean containsHonorsOrTerminals( List<Tile> s ){
        for( Tile t : s ){
            if( t.dragon!=null || t.wind!=null || t.number==1 || t.number==9 ){
                return true;
            }
        }
        return false;
    }
    private boolean containsHonorsOrTerminalsOnly( List<Tile> s ){
        for( Tile t : s ){
            if( t.number!=null && t.number>1 && t.number<9 ){
                return false;
            }
        }
        return true;
    }
    private boolean containsHonors( List<Tile> s ){
        for( Tile t : s ){
            if( t.dragon!=null || t.wind!=null ){
                return true;
            }
        }
        return false;
    }
    private boolean containsTerminals( List<Tile> s ){
        for( Tile t : s ){
            if( !t.suit.toString().equals("HONOR") && (t.number==1||t.number==9) ){
                return true;
            }
        }
        return false;
    }
    private boolean containsSimples( List<Tile> s ){
        for( Tile t : s ){
            if( t.number!=null && t.number>1 && t.number<9 ){
                return true;
            }
        }
        return false;
    }
    private boolean containsWinningTile( List<Tile> s ){
        for( Tile t : s ){
            if( t.winningTile ){
                return true;
            }
        }
        return false;
    }
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
    private void checkKokushiMusou(Hand h){
        if( !containsHonorsOrTerminalsOnly(h.tiles) ){
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
            clearNonYakuman(h);
            scoredHand=h;
            countHan(scoredHand);
        } else if( noDupSet.size()==13 ){
            h.unsortedTiles.clear();
            h.kokushiMusou = true;
            clearNonYakuman(h);
            scoredHand=h;
            countHan(scoredHand);
        }
    }
    private void checkNagashiMangan(Hand h){
        if( h.nagashiMangan ){
            h.unsortedTiles.clear();
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
        for( Tile t : h.tiles ){
            if( !t.winningTile ){
                if( t.revealedState!= Tile.RevealedState.NONE && t.revealedState!= Tile.RevealedState.CLOSEDKAN ){
                    return;
                }
            }
        }
        h.tsumo = (h.selfDrawWinningTile);
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
        if( fuFromMelds(h)!=0 || fuFromPair(h)!=0 || fuFromWait(h)!=0 ){
            h.pinfu = false;
            return;
        }
        h.pinfu = true;
    }
    private void checkIipeikou(Hand h){
        if(        h.set1.toString().equals(h.set2.toString()) ){
            h.iipeikou = true;
        } else if( h.set1.toString().equals(h.set3.toString()) ){
            h.iipeikou = true;
        } else if( h.set1.toString().equals(h.set4.toString()) ){
            h.iipeikou = true;
        } else if( h.set2.toString().equals(h.set3.toString()) ){
            h.iipeikou = true;
        } else if( h.set2.toString().equals(h.set4.toString()) ){
            h.iipeikou = true;
        } else if( h.set3.toString().equals(h.set4.toString()) ){
            h.iipeikou = true;
        }
    }
    private void checkSanshokuDoujun(Hand h){
        //Get 3+ chiis
        List<Tile> leadTiles = new ArrayList<>();
        if( isChii(h.set1) && h.set1.get(0).suit!=Tile.Suit.HONOR ){
            leadTiles.add(h.set1.get(0));
        }
        if( isChii(h.set2) && h.set2.get(0).suit!=Tile.Suit.HONOR ){
            leadTiles.add(h.set2.get(0));
        }
        if( isChii(h.set3) && h.set3.get(0).suit!=Tile.Suit.HONOR ){
            leadTiles.add(h.set3.get(0));
        }
        if( isChii(h.set4) && h.set4.get(0).suit!=Tile.Suit.HONOR ){
            leadTiles.add(h.set4.get(0));
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
            } else if( t.number.equals(leadTiles.get(0).number) ){
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
        for (Tile dupTile : Arrays.asList(h.set1.get(0), h.set2.get(0), h.set3.get(0), h.set4.get(0)) ){
            if (!tempSet.add(dupTile.suit.toString())) {
                if (!tempSet2.add(dupTile.suit.toString())) {
                    duplicateSuits.add(dupTile.suit.toString());
                }
            }
        }

        //check for a 1-2-3, 4-5-6, and 7-8-9 chii in the dominant suit
        Boolean firstSet = false;
        Boolean secondSet = false;
        Boolean thirdSet = false;
        for( List<Tile> set : Arrays.asList(h.set1, h.set2, h.set3, h.set4) ){
            if( isChii(set) && duplicateSuits.contains(set.get(0).suit.toString()) && set.get(0).number==1 ){
                firstSet = true;
            } else if( isChii(set) && duplicateSuits.contains(set.get(0).suit.toString()) && set.get(0).number==4 ){
                secondSet = true;
            } else if( isChii(set) && duplicateSuits.contains(set.get(0).suit.toString()) && set.get(0).number==7 ){
                thirdSet = true;
            }
        }

        //if all three sets exist, we have an ittsuu
        if( firstSet && secondSet && thirdSet ){
            h.ittsuu = true;
        }
    }
    private void checkRyanpeikou(Hand h){
        if(        h.set1.toString().equals(h.set2.toString()) && h.set3.toString().equals(h.set4.toString()) ){
            h.ryanpeikou = true;
            h.iipeikou = false;
        } else if( h.set1.toString().equals(h.set3.toString()) && h.set2.toString().equals(h.set4.toString()) ){
            h.ryanpeikou = true;
            h.iipeikou = false;
        } else if( h.set1.toString().equals(h.set4.toString()) && h.set2.toString().equals(h.set3.toString()) ){
            h.ryanpeikou = true;
            h.iipeikou = false;
        }
    }
    private void checkToitoi(Hand h){
        if( !isChii(h.set1) && !isChii(h.set2) && !isChii(h.set3) && !isChii(h.set4) ){
            h.toitoi=true;
        }
    }
    private void checkSanAnkou(Hand h){
        Integer closedTriplets = 0;
        if( !isChii(h.set1) && !isSetOpenOrWinningTile(h.set1) ){
            closedTriplets += 1;
        }
        if( !isChii(h.set2) && !isSetOpenOrWinningTile(h.set2) ){
            closedTriplets += 1;
        }
        if( !isChii(h.set3) && !isSetOpenOrWinningTile(h.set3) ){
            closedTriplets += 1;
        }
        if( !isChii(h.set4) && !isSetOpenOrWinningTile(h.set4) ){
            closedTriplets += 1;
        }

        h.sanAnkou= (closedTriplets>=3);
    }
    private void checkSanshokuDoukou(Hand h){
        //Get 3+ triplets
        List<Tile> leadTiles = new ArrayList<>();
        if( !isChii(h.set1) && h.set1.get(0).suit!=Tile.Suit.HONOR ){
            leadTiles.add(h.set1.get(0));
        }
        if( !isChii(h.set2) && h.set2.get(0).suit!=Tile.Suit.HONOR ){
            leadTiles.add(h.set2.get(0));
        }
        if( !isChii(h.set3) && h.set3.get(0).suit!=Tile.Suit.HONOR ){
            leadTiles.add(h.set3.get(0));
        }
        if( !isChii(h.set4) && h.set4.get(0).suit!=Tile.Suit.HONOR ){
            leadTiles.add(h.set4.get(0));
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
        kanNumber = (isKan(h.set1)) ? kanNumber+1 : kanNumber;
        kanNumber = (isKan(h.set2)) ? kanNumber+1 : kanNumber;
        kanNumber = (isKan(h.set3)) ? kanNumber+1 : kanNumber;
        kanNumber = (isKan(h.set4)) ? kanNumber+1 : kanNumber;
        if( kanNumber==3 ){
            h.sanKantsu = true;
        }
    }
    private void checkTanyao(Hand h){
        if( !containsHonorsOrTerminals(h.tiles) ){
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
        if( containsTerminals(h.pair)
                && containsTerminals(h.set1)
                && containsTerminals(h.set2)
                && containsTerminals(h.set3)
                && containsTerminals(h.set4) ){
            h.junchan = true;
        }
    }
    private void checkHonroutou(Hand h){
        if( containsHonorsOrTerminalsOnly(h.tiles) ){
            h.honroutou = true;
        }
    }
    private void checkChanta(Hand h){
        if( containsHonorsOrTerminals(h.pair)
                && containsHonorsOrTerminals(h.set1)
                && containsHonorsOrTerminals(h.set2)
                && containsHonorsOrTerminals(h.set3)
                && containsHonorsOrTerminals(h.set4)
                && !h.honroutou
                && !h.junchan ){
            h.chanta = true;
        }
    }
    private void checkShousangen(Hand h){
        HashSet<String> noDupSet = new HashSet<>();
        if( h.pair.get(0).dragon!=null ){
            noDupSet.add(h.pair.get(0).dragon.toString());
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
        if( containsHonors(h.tiles) && countSuits(h.tiles)==1 ){
            h.honitsu = true;
        }
    }
    private void checkChinitsu(Hand h){
        if( !containsHonors(h.tiles) && countSuits(h.tiles)==1 ){
            h.chinitsu = true;
        }
    }

    // Yakuman
    private void checkSuuAnkou(Hand h){
        HashSet<String> uniqueTiles = new HashSet<>();
        for( Tile t : h.tiles ){
            uniqueTiles.add(t.toString());
        }
        if( uniqueTiles.size()==5 ){
            int tripletCount = 0;
            for( String s : uniqueTiles ){
                if( countTileInSet(h.findTile(s), h.tiles)==3 || countTileInSet(h.findTile(s), h.tiles)==4){
                    tripletCount++;
                }
            }
            if( tripletCount==4 && h.getWinningTile()!=null && countTileInSet(h.getWinningTile(),h.tiles)==2 ){
                h.suuAnkou = false;
                h.suuAnkouTanki = true;
            } else if( tripletCount==4 && !h.isOpen() && h.selfDrawWinningTile ){
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
            if( countTileInSet(new Tile(w.toString(), "HONOR"),h.tiles)==3
                    || countTileInSet(new Tile(w.toString(), "HONOR"),h.tiles)==4 ){
                winds.remove(w);
            }
        }

        if( winds.size()==0 ){
            h.shousuushii = true;
        }
    }    // Four triplets of winds
    private void checkTsuuiisou(Hand h){
        if( countSuits(h.tiles)==0 && !h.chiiToitsu ){
            h.tsuuiisou = true;
        }
    }     // All honors
    private void checkDaichisei(Hand h){
        Set<Tile> tz = findDuplicateTiles(h.tiles);
        if( tz.size()==7
                && findDuplicateTiles(tz).size()==0
                && countSuits(h.tiles)==0 ){
            h.daichisei = true;
        }
    }     // 7 pairs of all honors
    private void checkChinroutou(Hand h){
        if( !containsHonors(h.tiles) && !containsSimples(h.tiles) ){
            h.chinroutou = true;
        }
    }    // All terminals
    private void checkRyuuiisou(Hand h){
        for(Tile t : h.tiles){
            if( t.suit==Tile.Suit.HONOR && t.dragon!=Tile.Dragon.GREEN ){
                return;
            } else if( t.number!=null && (t.number==1||t.number==5||t.number==7||t.number==8) ){
                return;
            }
        }
        h.ryuuiisou = true;
    }     // All green
    private void checkChuurenPoutou(Hand h){
        if( countSuits(h.tiles)!=1 || containsHonors(h.tiles) ){
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
        Log.v("checkChuurenPoutou", "usedTiles is not empty: "+usedTiles.toString());
        Log.v("checkChuurenPoutou", "usedExpectedTiles is not empty: " + usedExpectedTiles.toString());
        expectedTiles.removeAll(usedExpectedTiles);
        if( expectedTiles.size()!=0 ){
            Log.v("checkChuurenPoutou", "expectedTiles is not empty: "+expectedTiles.toString());
            return;
        }
        List<Tile> tempList = new ArrayList<>();
        tempList.addAll(h.tiles);
        tempList.removeAll(usedTiles);
        if( tempList.size()>1 ){
            Log.v("checkChuurenPoutou", "tempList still too big: "+tempList.toString());
            return;
        }
        h.chuurenPoutou = true;
        //TODO implement 9-sided wait... seems like a pain...
    }
    private void checkSuuKantsu(Hand h){
        if( isKan(h.set1) && isKan(h.set2) && isKan(h.set3) && isKan(h.set4) ){
            h.suuKantsu = true;
        }
    }     // Four Kans

    // Controversial Yaku
    private void checkSanrenkou(Hand h){
        //Get 3+ triplets
        List<Tile> leadTiles = new ArrayList<>();
        if( !isChii(h.set1) && h.set1.get(0).suit!=Tile.Suit.HONOR ){
            leadTiles.add(h.set1.get(0));
        }
        if( !isChii(h.set2) && h.set2.get(0).suit!=Tile.Suit.HONOR ){
            leadTiles.add(h.set2.get(0));
        }
        if( !isChii(h.set3) && h.set3.get(0).suit!=Tile.Suit.HONOR ){
            leadTiles.add(h.set3.get(0));
        }
        if( !isChii(h.set4) && h.set4.get(0).suit!=Tile.Suit.HONOR ){
            leadTiles.add(h.set4.get(0));
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
        if( !isChii(h.set1) && h.set1.get(0).suit!=Tile.Suit.HONOR ){
            leadTiles.add(h.set1.get(0));
        }
        if( !isChii(h.set2) && h.set2.get(0).suit!=Tile.Suit.HONOR ){
            leadTiles.add(h.set2.get(0));
        }
        if( !isChii(h.set3) && h.set3.get(0).suit!=Tile.Suit.HONOR ){
            leadTiles.add(h.set3.get(0));
        }
        if( !isChii(h.set4) && h.set4.get(0).suit!=Tile.Suit.HONOR ){
            leadTiles.add(h.set4.get(0));
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
                && !containsHonorsOrTerminals(h.tiles)
                && countSuits(h.tiles)==1 ){
            h.daiSharin = true;
        }
    }     // Single suit, pairs from 2-8
    private void checkShiisanpuuta(Hand h){}  // Thirteen unconnected tiles on first draw
    private void checkShiisuupuuta(Hand h){}  // Fourteen unconnected tiles (on first draw? wut)
    private void checkParenchan(Hand h){}     // Eight consecutive wins as dealer

    private void clearNonYakuman(Hand h){
        h.doubleRiichi = false;
        h.riichi = false;
        h.ippatsu = false;
        h.tsumo = false;
        h.rinshan = false;
        h.chanKan = false;
        h.haitei = false;
        h.houtei = false;
        h.chiiToitsu = false;
        h.nagashiMangan = false;
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
