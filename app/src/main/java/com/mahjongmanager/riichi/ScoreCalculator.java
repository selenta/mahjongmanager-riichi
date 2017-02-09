package com.mahjongmanager.riichi;

import com.mahjongmanager.riichi.common.Hand;
import com.mahjongmanager.riichi.common.Meld;
import com.mahjongmanager.riichi.common.Tile;
import com.mahjongmanager.riichi.utils.FuHelper;
import com.mahjongmanager.riichi.utils.HanHelper;
import com.mahjongmanager.riichi.utils.HandGenerator;
import com.mahjongmanager.riichi.utils.Log;
import com.mahjongmanager.riichi.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ScoreCalculator {
    /*
     * These are the four primary stages of the process for a valid/complete winning hand:
     *
     * unsortedHand  - Raw hand, completely untouched (original hand remains untouched)
     * sortedHands   - Every possible valid interpretation of how the hand could be
     *                 sorted/scored are placed in here (e.g. both a chiitoitsu interpretation of
     *                 the hand AND a ryanpeikou could appear in the list).
     * scoredHand    - All of the hands in sortedHands are examined, this hand is the one
     *                 with the highest value.
     * validatedHand - The scoredHand is thoroughly examined for anything that could be
     *                 nonsensical or contradictory. If ANY part of the hand/melds/tiles/yaku
     *                 looks wrong, this will remain null.
     */
    private Hand       unsortedHand;
    private List<Hand> sortedHands;
    public Hand        scoredHand;
    public Hand        validatedHand;

    /*
     * Incomplete hands are those that do not meed the 4-sets+1-pair model. Choosing to examine
     * these hands will reveal calculate its shanten, as well as its waits (if shanten=0).
     */
    private boolean processIncompleteHands = false;
    public int shanten = -1;
    public List<Wait> waits = new ArrayList<>();
    private List<Tile> candidates = new ArrayList<>();


    public ScoreCalculator(Hand h){
        processHand(h);
    }
    public ScoreCalculator(Hand h, boolean checkIncomplete){
        processIncompleteHands = checkIncomplete;
        processHand(h);
    }
    private void processHand(Hand h){
        unsortedHand = new Hand(h);
        sortedHands = new ArrayList<>();
        processUnsortedHand();

        // TODO validate that hands were processed correctly (have exception yaku or have sets set) as "processed, but not complete"
        //      There is a method to validate a "complete" hand, but not a "processed" hand

        determineScoredHand();
        if( scoredHand!=null && Validator.validate(scoredHand) ){
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
        if( unsortedHand==null || unsortedHand.tiles.size()<13 || unsortedHand.tiles.size()>18 ){
            return;
        } else if( unsortedHand.tiles.size()==13 ){
            processIncompleteHand();        // Skip straight to this, it can't be a complete hand
            return;
        }
        checkForUnusualStructure();

        //The hand should already be "processed" if it's an unusual hand
        if( scoredHand!=null ){
            return;
        }

        if( unsortedHand.unsortedTiles.size()==0 ){
            if( !unsortedHand.hasAbnormalStructure() ){
                scrubScore(unsortedHand);
            }
            scoredHand = unsortedHand;
        }

        // Start the sorting process, now that unusual hands are accounted for
        // Pull out pairs first
        List<Hand> sortingHands = new ArrayList<>();
        Set<Tile> duplicateTiles = Utils.findDuplicateTiles(unsortedHand.tiles);

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

        // We tried to process it as a complete hand, and it failed to pass as a valid hand
        if( sortedHands.isEmpty() && scoredHand==null ){
            processIncompleteHand();
        }
    }
    private void checkForUnusualStructure(){
        Hand hand = new Hand(unsortedHand);
        scrubScore(hand);

        if( HanHelper.isKokushiVariant(unsortedHand) ){
            HanHelper.checkKokushiMusou(hand);
            scoredHand = hand;
            return;
        }

        if( HanHelper.isDaichisei(unsortedHand) ){
            HanHelper.checkDaichisei(hand);
            scoredHand = hand;
            return;
        }

        if( HanHelper.isChiitoitsu(unsortedHand) ){
            HanHelper.checkChiitoitsu(hand);
            sortedHands.add(hand);
        }

        if( unsortedHand.nagashiMangan ){
            HanHelper.checkNagashiMangan(hand);
            scoredHand = hand;
            validatedHand = hand;
        }
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
            countHan(scoredHand);
            countFu(scoredHand);
        } else if( sortedHands.size()>0 ){
            boolean anyHandValidated = false;
            for( Hand unscoredHand : sortedHands ){
                boolean validated = Validator.validate(unscoredHand);
                anyHandValidated = anyHandValidated || validated;

                if( anyHandValidated && !validated ){
                    continue;
                }

                countHan(unscoredHand);
                countFu(unscoredHand);
                if( scoredHand==null || handTwoIsLarger(scoredHand, unscoredHand) ){
                    scoredHand = unscoredHand;
                }
            }
        }
    }
    private boolean handTwoIsLarger(Hand handOne, Hand handTwo ){
        int handOneBasePoints = scoreBasePoints(handOne.han, handOne.fu);
        int handTwoBasePoints = scoreBasePoints(handTwo.han, handTwo.fu);

        return handTwoBasePoints > handOneBasePoints
                || (handOneBasePoints==handTwoBasePoints && handTwo.han > handOne.han) ;
    }

    private void countHan(Hand h){
        HanHelper.populateHanList(h);

        int total = 0;
        for(int i : h.hanList.values() ){
            total += i;
        }
        h.han = total;
    }
    private void countFu(Hand h){
        FuHelper.populateFuList(h);

        int total = 0;
        for(int i : h.fuList.values() ){
            total += i;
        }
        h.fu = total;
    }

    private void scrubScore(Hand h){
        h.hanList.clear();
        h.fuList.clear();
        h.han = 0;
        h.fu = 0;
    }

    public static Integer scoreBasePoints( int han, int fu ){
        int roundedFu = (fu==25) ? 25 : (int) Math.ceil(fu/10.0)*10;
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
    /////////////////          Incomplete Hands           /////////////////
    ///////////////////////////////////////////////////////////////////////
    /*
     * This whole section is incredibly inefficient and ugly
     *      (though this is definitely an objectively non-trivial problem)
     * Ideads to make this more efficient (...maybe?):
     * - Pre-cache meld combinations, then build MeldSolvers from combinations
     * - Guarantee all saved MeldSolvers have unique combination of melds (makes saving them
     *      slower, but would make later processing/analysis faster, such as for an AI)
     */
    /**
     * These are used in the processing of incomplete hands. Hands being used in this way
     * MUST not need pre-processing. This means:
     * <ul>
     *     <li>Tiles from revealed melds will have the correct called/states</li>
     *     <li>Tiles from revealed melds will not be preset in the unsorted tiles list</li>
     * </ul>
     */
    private class MeldSolver {
        List<Tile> tiles = new ArrayList<>();
        List<Tile> uniqueTiles = new ArrayList<>();
        List<Meld> melds = new ArrayList<>();

        MeldSolver(Hand h) {
            tiles.addAll(h.unsortedTiles);
            uniquify();
        }
        MeldSolver(MeldSolver oldMs) {
            this.tiles.addAll(oldMs.tiles);
            this.melds.addAll(oldMs.melds);
            uniquify();
        }

        List<Tile> getTilesOf(Tile t){
            List<Tile> temp = new ArrayList<>();
            for(Tile tile : tiles){
                if(tile.isSame(t)){
                    temp.add(tile);
                }
            }
            return temp;
        }
        void createMeld(List<Tile> ts){
            melds.add(new Meld(ts));
            for(Tile t : ts){
                if( !tiles.remove(t) ){
                    Log.w("createMeld", "Created a meld with a tile that was not in the MeldSolver's tiles list: "+t+" - "+tiles);
                }
            }
            uniquify();
            removeOutliers();
            Utils.sortMelds(melds);
        }

        private void uniquify(){
            uniqueTiles.clear();
            for(Tile t : tiles){
                if( !Utils.listContainsTile(uniqueTiles, t) ){
                    uniqueTiles.add(t);
                }
            }
        }
        private void removeOutliers(){
            List<Tile> outliers = new ArrayList<>();
            for( Tile tile : tiles ){
                if( !hasAnyNearbyTile(tiles, tile) ){
                    outliers.add(tile);
                }
            }
            tiles.removeAll(outliers);
            uniqueTiles.removeAll(outliers);
        }

        public int getShanten(int emptyMeldsInHand){
            int countedMelds = 0;
            int count = 0;
            boolean hasPair = false;

            for(Meld m : melds){
                if( !hasPair && m.isPair() ){
                    hasPair = true;
                } else if (countedMelds < emptyMeldsInHand){
                    if( m.size()==2 || m.size()==4 ){
                        count++;
                    }
                    countedMelds++;
                }
            }
            if( hasPair && countedMelds==emptyMeldsInHand){
                count--;
            }
            for(int i=countedMelds; i<emptyMeldsInHand; i++){
                // all remaining tiles are singles, but still need more melds
                count += 2;
            }
            return count;
        }

        public String toString(){
            return "MeldSolver-"+melds.toString();
        }
        public String toStringVerbose(){
            String s = "MeldSolver["+tiles.size()+"]\n";
            s += "tiles: " + tiles.toString() + "\n";
            s += "uniqueTiles: " + uniqueTiles.toString() + "\n";
            s += "melds: " + melds.toString() + "\n";
            return s;
        }
    }

    // TODO properly account for pairs
    //  The following hand is scored as shanten=1 (should be shanten=2)
    //  Manzu: 22244, Pinzu: 45, Souzu: 235678
    private void processIncompleteHand(){
        if( !processIncompleteHands){
            return;
        }

        Hand incompleteHand = new Hand(unsortedHand);
        cleanHandBeforeMessyPart(incompleteHand);

        List<MeldSolver> possibilities = solveRemainingMelds(incompleteHand);
        shanten = getShanten(incompleteHand, possibilities);

        Log.i("processIncompleteHand", "shanten: "+shanten);
        if( shanten==0 ){
            processTenpaiHand(possibilities);
        }
    }

    private void cleanHandBeforeMessyPart(Hand hand){
        // Removes tiles from unsortedTiles that can't actually be used in melds
        //      Technically unneeded, but makes later steps cleaner
        removeOutliers(hand);

        // These will actually remove tiles from unsortedTiles and place them in melds
        //      Technically unneeded, but makes later steps cleaner
        removeObviousTriplets(hand);
        removeObviousLoneRuns(hand);
    }
    private void removeOutliers(Hand hand){
        List<Tile> outliers = new ArrayList<>();
        for( Tile tile : hand.tiles ){
            if( !hasAnyNearbyTile(hand.tiles, tile) ){
                outliers.add(tile);
            }
        }
        hand.unsortedTiles.removeAll(outliers);
    }
    private void removeObviousTriplets(Hand hand){
        List<Tile> triplets = new ArrayList<>();
        for(Tile tile : hand.unsortedTiles){
            int count = hand.countTile(tile);
            boolean hasNeighbors = hasNearbyChiiCandidate(hand.unsortedTiles, tile);

            if( !hasNeighbors ){
                switch (count){
                    case 4:
                        Log.w("removeObviousTriplets", "Hand has an undeclared Closed Kan, not sure what to do lol: "+tile+" - "+hand);
                        break;
                    case 3:
                        if( !Utils.listContainsTile(triplets, tile) ){
                            triplets.add(tile);
                        }
                        break;
                    default:
                        Log.v("removeObviousTriplets", "Hand has a pair of these tiles: "+tile+" - "+hand);
                        break;
                }
            }
        }
        for(Tile trip : triplets){
            Tile firstPonTile  = hand.popUnsortedTile(trip.value, trip.suit, null);
            Tile secondPonTile = hand.popUnsortedTile(trip.value, trip.suit, firstPonTile.revealedState);
            Tile thirdPonTile  = hand.popUnsortedTile(trip.value, trip.suit, firstPonTile.revealedState);
            hand.setMeld(Arrays.asList(firstPonTile, secondPonTile, thirdPonTile));
        }
    }
    private void removeObviousLoneRuns(Hand hand){
        List<Tile> runFounders = new ArrayList<>();     //Always start with lowest tile in the run
        for(Tile tile : hand.unsortedTiles){
            if( tile.suit==Tile.Suit.HONOR || tile.number>7 ){
                continue;
            }

            int count = hand.countTile(tile);
            int nextCount = hand.countTile(tile.getNextTile());
            int nextNextCount = hand.countTile(tile.getNextTile().getNextTile());
            if( count!=1 || nextCount!=1 || nextNextCount!=1 ){
                continue;
            }

            boolean hasLower = hasChiiCandidateLower(hand.unsortedTiles, tile);
            boolean nextNextHasAbove = hasChiiCandidateAbove(hand.unsortedTiles, tile.getNextTile().getNextTile());
            if( hasLower || nextNextHasAbove ) {
                continue;
            }

            runFounders.add(tile);
        }
        for(Tile founder : runFounders){
            Tile firstChiiTile = hand.popUnsortedTile(String.valueOf(founder.number), founder.suit, null);
            Tile secondChiiTile = hand.popUnsortedTile(String.valueOf(founder.number + 1), founder.suit, firstChiiTile.revealedState);
            Tile thirdChiiTile = hand.popUnsortedTile(String.valueOf(founder.number + 2), founder.suit, firstChiiTile.revealedState);
            hand.setMeld(Arrays.asList(firstChiiTile, secondChiiTile, thirdChiiTile));
        }
    }

    private List<MeldSolver> solveRemainingMelds(Hand hand){
        List<MeldSolver> incomplete = new ArrayList<>();
        incomplete.add(new MeldSolver(hand));

        List<MeldSolver> solved = new ArrayList<>();
        int lowestShanten = 14;
        int emptyMeldsInHand = hand.emptyMeldCount();

        // Iterate over the tiles, reading it for each possible combination of chii/pon/kan until all tiles have been examined
        while( incomplete.size() > 0 ){
            MeldSolver ms = incomplete.get(0);

            // All tiles have been slotted, remove it from the list
            if( ms.tiles.size()<2 ){
                incomplete.remove(ms);
                int msShanten = ms.getShanten(emptyMeldsInHand);
                if( msShanten < lowestShanten ){
                    lowestShanten = msShanten;
                    solved.clear();
                }
                if( msShanten <= lowestShanten ){
                    solved.add(ms);
                }
                continue;
            }

            // TODO consider trying to build melds from every tile, to be thorough
            //      Likely necessary to get all valid Waits
            incomplete.addAll(slotIncompletePons(ms));
            incomplete.addAll(slotIncompleteChiis(ms));

            // Remove the original version of the hand. If it didn't have children, its line ends here
            incomplete.remove(ms);
        }

        for(MeldSolver ms : solved){
            Log.i("solveRemainingMelds", "MeldSolver possibility: "+ms.toString());
        }
        Log.i("solveRemainingMelds", "MeldSolvers generated: "+solved.size());

        return solved;
    }
    private List<MeldSolver> slotIncompletePons(MeldSolver ms){
        List<MeldSolver> slottedPons = new ArrayList<>();

        List<Tile> tiles = ms.getTilesOf(ms.uniqueTiles.get(0));
        switch (tiles.size()){
            // Intentionally ignoring break statements here
            //      It is correct behavior that 4 of a kind creates one of each MeldSolver
            case 4:
                MeldSolver msQuad = new MeldSolver(ms);
                msQuad.createMeld(tiles);
                slottedPons.add(msQuad);
            case 3:
                MeldSolver msTriplet = new MeldSolver(ms);
                msTriplet.createMeld(tiles.subList(0,3));
                slottedPons.add(msTriplet);
            case 2:
                MeldSolver msPair = new MeldSolver(ms);
                msPair.createMeld(tiles.subList(0,2));
                slottedPons.add(msPair);
        }

        return slottedPons;
    }
    private List<MeldSolver> slotIncompleteChiis(MeldSolver ms){
        List<MeldSolver> slottedChiis = new ArrayList<>();

        Tile founder = ms.uniqueTiles.get(0);
        if( founder.suit== Tile.Suit.HONOR || founder.number>8 ){
            return slottedChiis;
        }

        Tile founderNext = null;
        List<Tile> nextTiles = ms.getTilesOf(founder.getNextTile());
        if( !nextTiles.isEmpty() ){
            founderNext = nextTiles.get(0);
        }
        Tile founderNextNext = null;
        List<Tile> nextNextTiles = ms.getTilesOf(founder.getNextTile().getNextTile());
        if( !nextNextTiles.isEmpty() && founder.number!=8){
            founderNextNext = nextNextTiles.get(0);
        }

        // Found: 1-2-3
        if( founderNext!=null && founderNextNext!=null ){
            MeldSolver msFull = new MeldSolver(ms);
            msFull.createMeld(Arrays.asList(founder, founderNext, founderNextNext));
            slottedChiis.add(msFull);
        }

        // Found: 1-3
        if( founderNext!=null ){
            MeldSolver msShort = new MeldSolver(ms);
            msShort.createMeld(Arrays.asList(founder, founderNext));
            slottedChiis.add(msShort);
        }

        // Found: 1-2
        if( founderNextNext!=null ){
            MeldSolver msHollow = new MeldSolver(ms);
            msHollow.createMeld(Arrays.asList(founder, founderNextNext));
            slottedChiis.add(msHollow);
        }

        return slottedChiis;
    }

    private static boolean hasAnyNearbyTile(List<Tile> list, Tile tile){
        return hasBrotherTile(list, tile) || hasNearbyChiiCandidate(list, tile);
    }
    private static boolean hasBrotherTile(List<Tile> list, Tile tile){
        for(Tile t : list) {
            if (t.isSame(tile) && t != tile) {
                return true;
            }
        }
        return false;
    }
    private static boolean hasNearbyChiiCandidate(List<Tile> list, Tile tile){
        return hasChiiCandidateLower(list, tile) || hasChiiCandidateAbove(list, tile);
    }
    private static boolean hasChiiCandidateLower(List<Tile> list, Tile tile){
        Tile prevPrevTile = tile.getPreviousTile().getPreviousTile();
        Tile prevTile = tile.getPreviousTile();

        for(Tile t : list) {
            if(tile.suit != Tile.Suit.HONOR){
                switch (tile.number){
                    case 1:
                        return false;
                    case 2:
                        if( t.isSame(prevTile) ){
                            return true;
                        }
                        break;
                    default:
                        if( t.isSame(prevPrevTile) || t.isSame(prevTile) ){
                            return true;
                        }
                        break;
                }
            }
        }
        return false;
    }
    private static boolean hasChiiCandidateAbove(List<Tile> list, Tile tile){
        Tile nextTile = tile.getNextTile();
        Tile nextNextTile = tile.getNextTile().getNextTile();

        for(Tile t : list) {
            if(tile.suit != Tile.Suit.HONOR){
                switch (tile.number){
                    case 8:
                        if( t.isSame(nextTile) ){
                            return true;
                        }
                        break;
                    case 9:
                        return false;
                    default:
                        if( t.isSame(nextTile) || t.isSame(nextNextTile)){
                            return true;
                        }
                        break;
                }
            }
        }
        return false;
    }

    private int getShanten(Hand h, List<MeldSolver> meldSolvers){
        int shanten = 14;
        int kokushi = countKokushiShanten(h);
        int chiitoitsu = countChiitoitsuShanten(h);
        shanten = (kokushi<shanten) ? kokushi : shanten;
        shanten = (chiitoitsu<shanten) ? chiitoitsu : shanten;

        int handEmptyMelds = h.emptyMeldCount();

        for(MeldSolver ms : meldSolvers){
            int msShanten = ms.getShanten(handEmptyMelds);
            shanten = (msShanten<shanten) ? msShanten : shanten;
        }

        return shanten;
    }
    private int countKokushiShanten(Hand h){
        int kokushiCount = 0;
        List<Tile> uniqueTiles = Utils.findUniqueTiles(h.tiles);
        for( Tile t : uniqueTiles ){
            if( t.isTerminal() || t.isHonor() ){
                kokushiCount++;
            }
        }
        return 12 - kokushiCount;
    }
    private int countChiitoitsuShanten(Hand h){
        Set<Tile> tz = Utils.findDuplicateTiles(h.tiles);
        return 6 - tz.size();
    }

    ///////////////////////////////////////////////////////////////////////
    /////////////////         Calculating Waits           /////////////////
    ///////////////////////////////////////////////////////////////////////

    public static class Wait {
        public int han;
        public int fu;
        public boolean isTsumo;
        public List<Tile> tiles = new ArrayList<>();
        public Wait( Tile tile, int h, int f, boolean tsumo ){
            han = h;
            fu = f;
            isTsumo = tsumo;
            tiles.add(tile);
        }

        public String toString(){
            return "["+han+","+fu+","+isTsumo+"-"+tiles+"]";
        }
    }

    /*
     * Look at each possibility,
     *       find the missing tile(s) options,
     *       add them to list of candidates.
     *  Once all unique tiles have been found,
     *      create a copy of the hand with each candidate.
     *  If found to be a valid hand,
     *      add the tile as a wait.
     *  If that tile is already listed as a wait (for that isTsumo option),
     *       use whichever scores higher.
     */
    private void processTenpaiHand(List<MeldSolver> possibilities){
        for(MeldSolver ms : possibilities){
            for(Meld m : ms.melds){
                findCandidates(m);
            }
        }

        for(Tile candidate : candidates){
            testCandidate(candidate, false);
            testCandidate(candidate, true);
        }
    }

    private void findCandidates(Meld m){
        if(m.size()!=2){
            return;
        }

        if( m.firstTile().isSame(m.secondTile()) ){                             // Pair

            // looking for a third
            addCandidate(m.firstTile());
        } else if( m.firstTile().getNextTile().isSame(m.secondTile()) ){        // Sequence (touching)

            // looking for a tile on either end (unless it's on the end)
            if( m.firstTile().number==1 ){
                addCandidate(m.secondTile().getNextTile());
            } else if( m.secondTile().number==9 ){
                addCandidate(m.firstTile().getPreviousTile());
            } else {
                addCandidate(m.firstTile().getPreviousTile());
                addCandidate(m.secondTile().getNextTile());
            }
        } else {                                                                // Sequence (seperated)

            // looking for a tile in the middle
            addCandidate(m.firstTile().getNextTile());
        }
    }
    private void addCandidate(Tile tile){
        boolean isNew = true;
        for(Tile t : candidates){
            if(t.isSame(tile)){
                isNew = false;
            }
        }
        if(isNew){
            candidates.add(new Tile(tile));
        }
    }

    private void testCandidate(Tile candidate, boolean isTsumo){
        Hand hand = new Hand(unsortedHand);
        Tile tile = new Tile(candidate);
        if( !isTsumo ){
            tile.calledFrom = Tile.CalledFrom.LEFT;
        } else {
            hand.selfDrawWinningTile = true;
            tile.calledFrom = Tile.CalledFrom.NONE;
        }
        tile.winningTile = true;
        hand.addTile(candidate);

        ScoreCalculator sc = new ScoreCalculator(hand);
        if( sc.validatedHand!=null ){
            addWait(candidate, sc.validatedHand.han, sc.validatedHand.fu, isTsumo);
        }
        Log.i("testCandidate", "Tested candidate: "+candidate+"   -   "+sc.validatedHand);
    }
    private void addWait(Tile tile, int han, int fu, boolean tsumo){
        for( Wait wait : waits ){
            if( wait.tiles.contains(tile) && wait.isTsumo==tsumo ){
                int estPoints = ScoreCalculator.scoreBasePoints(han,fu);
                int waitPoints = ScoreCalculator.scoreBasePoints(wait.han,wait.fu);

                if( estPoints>waitPoints ){
                    wait.tiles.remove(tile);
                }
            }
            if( han==wait.han && fu==wait.fu && tsumo==wait.isTsumo ){
                wait.tiles.add(tile);
                return;
            }
        }
        waits.add(new Wait(tile, han, fu, tsumo));
    }

}
