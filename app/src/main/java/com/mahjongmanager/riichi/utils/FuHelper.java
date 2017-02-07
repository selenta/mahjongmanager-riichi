package com.mahjongmanager.riichi.utils;

import com.mahjongmanager.riichi.MainActivity;
import com.mahjongmanager.riichi.common.Fu;
import com.mahjongmanager.riichi.common.Hand;
import com.mahjongmanager.riichi.common.Meld;
import com.mahjongmanager.riichi.common.Tile;
import com.mahjongmanager.riichi.common.Yaku;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FuHelper {
    public static List<Fu> allFu = new ArrayList<>();

    public static void populate(MainActivity activity){
        XmlPullParserFactory factory = null;
        try {
            factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();

            InputStream is = activity.getAssets().open("fu.xml");
            xpp.setInput(is, null);

            parseXml( xpp );
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void parseXml(XmlPullParser parser) throws XmlPullParserException, IOException {
        int eventType = parser.getEventType();
        Fu fu = null;
        String text = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            String tagname = parser.getName();
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    if (tagname.equalsIgnoreCase("fu")) {
                        fu = new Fu();
                    }
                    break;

                case XmlPullParser.TEXT:
                    text = parser.getText();
                    break;

                case XmlPullParser.END_TAG:
                    if (tagname.equalsIgnoreCase("fu")) {
                        allFu.add(fu);
                    } else if (tagname.equalsIgnoreCase("name")) {
                        fu.name = Fu.Name.valueOf(text);
                    } else if (tagname.equalsIgnoreCase("english")) {
                        fu.english = text;
                    } else if (tagname.equalsIgnoreCase("romaji")) {
                        fu.romaji = text;
                    } else if (tagname.equalsIgnoreCase("kanji")) {
                        fu.kanji = text;
                    } else if (tagname.equalsIgnoreCase("value")) {
                        fu.value = Integer.parseInt(text);
                    } else if (tagname.equalsIgnoreCase("description")) {
                        fu.description = text;
                    }
                    break;

                default:
                    break;
            }
            eventType = parser.next();
        }
    }

    /////////////////////////////////////////////////////////////////////////
    ////////////////////        General Methods        //////////////////////
    /////////////////////////////////////////////////////////////////////////
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
        h.fuList.put(Fu.Name.FUUTEI, 20);

        // (1) Set fu for chiitoitsu
        if( h.hasYaku(Yaku.Name.CHIITOITSU) ){
            h.fuList.clear();
            h.fuList.put(Fu.Name.CHIITOITSU, 25);
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
            boolean isClosed = h.fuList.containsKey(Fu.Name.MENZEN_KAFU);
            h.fuList.clear();
            h.fuList.put(Fu.Name.PINFU, 20);
            if( isClosed ){
                h.fuList.put(Fu.Name.MENZEN_KAFU, 10);
            }
        } else if( h.selfDrawWinningTile ) {
            h.fuList.put(Fu.Name.SELF_DRAW, 2);
        }

        // (6) Set fu for open pinfu
        if( h.fuList.size()==1 && !h.hasYaku(Yaku.Name.PINFU) ){
            h.fuList.put(Fu.Name.PINFU_OPEN, 10);
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
            h.fuList.put(Fu.Name.MENZEN_KAFU, 10);
        }
    }
    private static void fuFromPair(Hand h){
        if( h.pair.firstTile().dragon!=null ){
            h.fuList.put(Fu.Name.DRAGON_PAIR, 2);
        } else if( h.pair.firstTile().wind==h.prevailingWind ){
            h.fuList.put(Fu.Name.PREVAILING_WIND, 2);
        }
        if( h.pair.firstTile().wind==h.playerWind ){
            // Double counting here for pairs of winds not allowed in all rulesets
            h.fuList.put(Fu.Name.SEAT_WIND, 2);
        }
    }
    private static void fuFromMelds(Hand h){
        fuFromMeld( h, h.meld1, Fu.Name.MELD_1 );
        fuFromMeld( h, h.meld2, Fu.Name.MELD_2 );
        fuFromMeld( h, h.meld3, Fu.Name.MELD_3 );
        fuFromMeld( h, h.meld4, Fu.Name.MELD_4 );
    }
    private static void fuFromMeld(Hand h, Meld meld, Fu.Name name){
        if( !meld.isChii() ){
            int meldFu = 2;
            meldFu = (meld.isClosed())                                       ? meldFu*2 : meldFu;
            meldFu = (Utils.containsHonorsOrTerminalsOnly(meld.getTiles()))  ? meldFu*2 : meldFu;
            meldFu = (meld.isKan())                                          ? meldFu*4 : meldFu;
            h.fuList.put( name, meldFu);
        }
    }
    private static void fuFromWait(Hand h){
        Meld winningMeld = h.getWinningMeld();
        if( winningMeld!=null && winningMeld.isPair() ){
            h.fuList.put(Fu.Name.PAIR_WAIT, 2);
        } else if( winningMeld!=null && winningMeld.isChii() ){
            if( winningMeld.thirdTile().winningTile && winningMeld.firstTile().number==1 && winningMeld.secondTile().number==2 && winningMeld.thirdTile().number==3 ){
                // Wait on a 3 for a 1-2-3 meld
                h.fuList.put(Fu.Name.SINGLE_SIDED_WAIT, 2);
            } else if( winningMeld.firstTile().winningTile && winningMeld.firstTile().number==7 && winningMeld.secondTile().number==8 && winningMeld.thirdTile().number==9 ){
                // Wait on a 7 for a 7-8-9 meld
                h.fuList.put(Fu.Name.SINGLE_SIDED_WAIT, 2);
            } else if( winningMeld.secondTile().winningTile ){
                // Wait on an inside tile for a chii
                h.fuList.put(Fu.Name.INSIDE_WAIT, 2);
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

        return h.fuList.containsKey(Fu.Name.CHIITOITSU)
                || h.fuList.containsKey(Fu.Name.DRAGON_PAIR)
                || h.fuList.containsKey(Fu.Name.PREVAILING_WIND)
                || h.fuList.containsKey(Fu.Name.SEAT_WIND)
                || h.fuList.containsKey(Fu.Name.PAIR_WAIT)
                || h.fuList.containsKey(Fu.Name.SINGLE_SIDED_WAIT)
                || h.fuList.containsKey(Fu.Name.INSIDE_WAIT)
                || h.fuList.containsKey(Fu.Name.MELD_1)
                || h.fuList.containsKey(Fu.Name.MELD_2)
                || h.fuList.containsKey(Fu.Name.MELD_3)
                || h.fuList.containsKey(Fu.Name.MELD_4);
    }
}
