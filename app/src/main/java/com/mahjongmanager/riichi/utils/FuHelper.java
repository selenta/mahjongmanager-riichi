package com.mahjongmanager.riichi.utils;

import com.mahjongmanager.riichi.Hand;
import com.mahjongmanager.riichi.Meld;
import com.mahjongmanager.riichi.Tile;

public class FuHelper {
    private static String FUUTEI            = "Fuutei";
    private static String CHIITOITSU        = "Chiitoitsu";
    private static String MENZEN_KAFU       = "Menzen-Kafu";        //TODO this is appearing as "Menzen-kafu" in prod and is thus not matching
    private static String PINFU             = "Pinfu";
    private static String PINFU_OPEN        = "Open Pinfu";
    private static String SELF_DRAW         = "Self Draw Winning Tile";
    private static String DRAGON_PAIR       = "Dragon Pair";
    private static String PREVAILING_WIND   = "Prevailing Wind";
    private static String SEAT_WIND         = "Seat Wind";
    private static String PAIR_WAIT         = "Pair Wait";
    private static String SINGLE_SIDED_WAIT = "Single-sided Wait";
    private static String INSIDE_WAIT       = "Inside Wait";

    /**
     * Examines the hand in depth, including its overall structure, meld structure, yaku, pair,
     * and its winning tile. Adding an entry to the hand's fuList for each type of Fu present.
     * @param h The fuList of this Hand will be populated
     */
    public static void populateFuList(Hand h){
        if( h.hasYakuman() || h.nagashiMangan ){
            return;
        }

        // (0) Fu starts at 20
        h.fuList.put(FUUTEI, 20);

        // (1) Set fu for chiitoitsu
        if( h.chiiToitsu ){
            h.fuList.clear();
            h.fuList.put(CHIITOITSU, 25);
            h.fu = 25;
            return;
        }

        // (2,3,4) Add fu for closed hand, then melds/pair, then wait
        fuFromClosedHand(h);
        fuFromPair(h);
        fuFromMelds(h);
        fuFromWait(h);

        // (5) Add fu for tsumo (pinfu exception)
        if( !hasFu(h) ){
            boolean isClosed = h.fuList.containsKey(MENZEN_KAFU);
            h.fuList.clear();
            h.fuList.put( PINFU, 20);
            if( isClosed ){
                h.fuList.put( MENZEN_KAFU, 10);
            }
        } else if( h.selfDrawWinningTile ) {
            h.fuList.put( SELF_DRAW, 2);
        }

        // (6) Set fu for open pinfu
        if( h.fuList.size()==1 && !h.pinfu ){
            h.fuList.put( PINFU_OPEN, 10);
        }
    }
    private static void fuFromClosedHand(Hand h){
        Boolean isOpen = false;
        for( Tile t : h.tiles ){
            if( !t.winningTile ){
                if( t.revealedState!= Tile.RevealedState.NONE && t.revealedState!= Tile.RevealedState.CLOSEDKAN ){
                    isOpen = true;
                }
            }
        }
        if( !isOpen && h.getWinningTile()!=null && h.getWinningTile().calledFrom!=Tile.CalledFrom.NONE ){
            h.fuList.put(MENZEN_KAFU, 10);
        }
    }
    private static void fuFromPair(Hand h){
        if( h.pair.firstTile().dragon!=null ){
            h.fuList.put( DRAGON_PAIR, 2);
        } else if( h.pair.firstTile().wind==h.prevailingWind ){
            h.fuList.put( PREVAILING_WIND, 2);
        }
        if( h.pair.firstTile().wind==h.playerWind ){
            // Double counting here for pairs of winds not allowed in all rulesets
            h.fuList.put( SEAT_WIND, 2);
        }
    }
    private static void fuFromMelds(Hand h){
        fuFromMeld( h, h.meld1, "Meld 1" );
        fuFromMeld( h, h.meld2, "Meld 2" );
        fuFromMeld( h, h.meld3, "Meld 3" );
        fuFromMeld( h, h.meld4, "Meld 4" );
    }
    private static void fuFromMeld(Hand h, Meld meld, String label){
        if( !meld.isChii() ){
            int meldFu = 2;
            meldFu = (meld.isClosed())                                       ? meldFu*2 : meldFu;
            meldFu = (Utils.containsHonorsOrTerminalsOnly(meld.getTiles()))  ? meldFu*2 : meldFu;
            meldFu = (meld.isKan())                                          ? meldFu*4 : meldFu;
            h.fuList.put( label, meldFu);
        }
    }
    private static void fuFromWait(Hand h){
        Meld winningMeld = h.getWinningMeld();
        if( winningMeld!=null && winningMeld.isPair() ){
            h.fuList.put( PAIR_WAIT, 2);
        } else if( winningMeld!=null && winningMeld.isChii() ){
            if( winningMeld.thirdTile().winningTile && winningMeld.firstTile().number==1 && winningMeld.secondTile().number==2 && winningMeld.thirdTile().number==3 ){
                // Wait on a 3 for a 1-2-3 meld
                h.fuList.put( SINGLE_SIDED_WAIT, 2);
            } else if( winningMeld.firstTile().winningTile && winningMeld.firstTile().number==7 && winningMeld.secondTile().number==8 && winningMeld.thirdTile().number==9 ){
                // Wait on a 7 for a 7-8-9 meld
                h.fuList.put( SINGLE_SIDED_WAIT, 2);
            } else if( winningMeld.secondTile().winningTile ){
                // Wait on an inside tile for a chii
                h.fuList.put( INSIDE_WAIT, 2);
            }
        }
    }

    /**
     * Get the name of the Fu that describes the meld. Meld is expected to be a non-sequence meld
     * or else will return an ugly value.
     *
     * Example: Closed Simples Triplet
     */
    public static String getFuName(Meld m){
        if( m.isChii() ){
            return "ERROR - Meld is sequence";
        }
        boolean isClosed = m.isClosed();
        boolean isSimples = !Utils.containsHonorsOrTerminalsOnly(m.getTiles());
        boolean isKan = m.isKan();

        String s = (isClosed) ? "Closed " : "Open ";
        if( isSimples ){
            s += "Simples ";
        } else if(m.firstTile().suit== Tile.Suit.HONOR) {
            s += "Honors ";
        } else {
            s += "Terminals ";
        }
        s += (isKan) ? "Quad " : "Triplet";
        return s;
    }

    /**
     * Determines whether or not the hand qualifies for Pinfu or not. The hand should have no
     * triplets/quads, no valuable pair, and has a two-sided wait on a sequence.
     * @param h Hand to be examined
     * @return True if the hand qualifies for Pinfu
     */
    public static boolean hasFu(Hand h){
        if(h.fuList.size()==0){
            populateFuList(h);
        }

        return h.fuList.containsKey(CHIITOITSU)
                || h.fuList.containsKey(DRAGON_PAIR)
                || h.fuList.containsKey(PREVAILING_WIND)
                || h.fuList.containsKey(SEAT_WIND)
                || h.fuList.containsKey(PAIR_WAIT)
                || h.fuList.containsKey(SINGLE_SIDED_WAIT)
                || h.fuList.containsKey(INSIDE_WAIT)
                || h.fuList.containsKey("Meld 1")
                || h.fuList.containsKey("Meld 2")
                || h.fuList.containsKey("Meld 3")
                || h.fuList.containsKey("Meld 4");
    }

    /**
     * When provided a Fu name, will return a more detailed description of the Fu. Valid options
     * include this class's public static strings.
     * @param s The name of the Fu
     * @return Description of the Fu
     */
    public static String getDescription(String s){
        if( s.equals(FUUTEI) ){
            return "A standard winning hand consisting of four melds and a pair";
        } else if( s.equals(CHIITOITSU) ){
            return "A winning hand made up of 7 pairs";
        } else if( s.equals(MENZEN_KAFU) ){
            return "Won from another player's discarded tile with a closed hand";
        } else if( s.equals(PINFU) ){
            return "A special winning hand that contains no other Fu. This hand consists of: a closed hand, a valueless pair, all four melds are runs, and the winning tile was a two-sided wait";
        } else if( s.equals(PINFU_OPEN) ){
            return "A special winning hand that contains no other Fu. This hand consists of: an open hand, a valueless pair, all four melds are runs, and the winning tile was a two-sided wait";
        } else if( s.equals(SELF_DRAW) ){
            return "Drew the winning tile";
        } else if( s.equals(DRAGON_PAIR) ){
            return "The pair consists of dragons";
        } else if( s.equals(PREVAILING_WIND) ){
            return "The pair consists of the prevailing wind (aka round wind)";
        } else if( s.equals(SEAT_WIND) ){
            return "The pair consists of the player's seat wind";
        } else if( s.equals(PAIR_WAIT) ){
            return "The winning tile completes the pair";
        } else if( s.equals(SINGLE_SIDED_WAIT) ){
            return "The winning tile completes a run. Either the hand contains a 1-2 and is waiting on a 3, or the hand contains a 9-8 and is waiting on a 7";
        } else if( s.equals(INSIDE_WAIT) ){
            return "The winning tile completes a run and is the middle tile of the run. Example: the hand contains a 3-5 and is waiting on a 4";
        }
        return "Error - Could not find description for "+s;
    }
}
