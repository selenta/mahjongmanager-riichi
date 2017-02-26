package com.mahjongmanager.riichi.utils;

import com.mahjongmanager.riichi.MainActivity;
import com.mahjongmanager.riichi.common.Hand;
import com.mahjongmanager.riichi.common.Tile;
import com.mahjongmanager.riichi.common.TileSet;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Contains all details for the HandBuilder Practice (tutorial). Users will practice drawing
 * and discarding in a scripted scenario, and afterwards will be judged. If they do not "pass"
 * they will see a detailed description of the logic behind each number.
 */
public class TutorialLibrary {
    public static List<DiscardDetails> allDiscardDetails = new ArrayList<>();

    // Starting hand
    private static Tile s1  = new Tile(3, Tile.Suit.MANZU);
    private static Tile s2  = new Tile(5, Tile.Suit.MANZU);
    private static Tile s3  = new Tile(8, Tile.Suit.MANZU);
    private static Tile s4  = new Tile(4, Tile.Suit.PINZU);
    private static Tile s5  = new Tile(5, Tile.Suit.PINZU);
    private static Tile s6  = new Tile(7, Tile.Suit.PINZU);
    private static Tile s7  = new Tile(9, Tile.Suit.PINZU);
    private static Tile s8  = new Tile(2, Tile.Suit.SOUZU);
    private static Tile s9  = new Tile(4, Tile.Suit.SOUZU);
    private static Tile s10 = new Tile(9, Tile.Suit.SOUZU);
    private static Tile s11 = new Tile(Tile.Wind.WEST);
    private static Tile s12 = new Tile(Tile.Wind.NORTH);
    private static Tile s13 = new Tile(Tile.Dragon.RED);

    // Draws
    private static Tile d1  = new Tile(Tile.Wind.WEST);
    private static Tile d2  = new Tile(3, Tile.Suit.SOUZU);    // Advances
    private static Tile d3  = new Tile(4, Tile.Suit.PINZU);    // Advances
    private static Tile d4  = new Tile(2, Tile.Suit.MANZU);    // Advances
    private static Tile d5  = new Tile(7, Tile.Suit.SOUZU);    // Temporary
    private static Tile d6  = new Tile(Tile.Wind.SOUTH);
    private static Tile d7  = new Tile(3, Tile.Suit.PINZU);    // Advances
    private static Tile d8  = new Tile(2, Tile.Suit.SOUZU);    // Advances
    private static Tile d9  = new Tile(9, Tile.Suit.MANZU);
    private static Tile d10 = new Tile(2, Tile.Suit.PINZU);    // Advances
    private static Tile d11 = new Tile(2, Tile.Suit.SOUZU);    // Advances
    private static Tile d12 = new Tile(8, Tile.Suit.PINZU);
    private static Tile d13 = new Tile(Tile.Dragon.WHITE);
    private static Tile d14 = new Tile(6, Tile.Suit.PINZU);    // Advances
    private static Tile d15 = new Tile(8, Tile.Suit.SOUZU);
    private static Tile d16 = new Tile(Tile.Dragon.RED);
    private static Tile d17 = new Tile(Tile.Dragon.RED);
    private static Tile d18 = new Tile(7, Tile.Suit.MANZU);

    // Hand-building Tutorial
    public static List<Tile> getTutorialTiles(){
        return Arrays.asList(s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12, s13, d1, d2, d3, d4, d5, d6, d7, d8, d9, d10, d11, d12, d13, d14, d15, d16, d17, d18);
    }

    /**
     * Expected Discards:
     * <ol>
     *     <li>North</li>
     *     <li>9 Sou</li>
     *     <li>Red</li>
     *     <li>West</li>
     *     <li>West</li>
     *     <li>South</li>
     *     <li>8 Man</li>
     *     <li>9 Pin</li>
     *     <li>9 Man</li>
     *     <li>7 Pin</li>
     *     <li>7 Sou</li>
     *     <li>8 Pin</li>
     *     <li>White</li>
     *     <li>5 Man</li>
     *     <li>8 Sou</li>
     *     <li>Red</li>
     *     <li>Red</li>
     *     <li>7 Man</li>
     * </ol>
     */
    public static List<Tile> getTutorialExpectedDiscards(){
        return Arrays.asList(s12, s10, s13, s11, d1, d6, s3, s7, d9, s6, d5, d12, d13, s2, d15, d16, d17, d18 );
    }
    /**
     * Expected Hand (can substitute a 7 Pin for a 4 Pin):
     * <ol>
     *     <li>2 Man</li>
     *     <li>3 Man</li>
     *     <li>2 Pin</li>
     *     <li>3 Pin</li>
     *     <li>4 Pin</li>
     *     <li>4 Pin</li>
     *     <li>5 Pin</li>
     *     <li>6 Pin</li>
     *     <li>2 Sou</li>
     *     <li>2 Sou</li>
     *     <li>2 Sou</li>
     *     <li>3 Sou</li>
     *     <li>4 Sou</li>
     * </ol>
     */
    public static Hand getTutorialExpectedHand(){
        return new Hand(Arrays.asList(d4, s1, d10, d7, s4, d3, s5, d14, s8, d8, d11, d2, s9));
    }


    public static void populate(MainActivity activity){
        XmlPullParserFactory factory = null;
        try {
            factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();

            InputStream is = activity.getAssets().open("tutorialDrawDiscard.xml");
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
        DiscardDetails discardDetails = null;
        List<Tile> tileSet = null;
        String text = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            String tagname = parser.getName();
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    if (tagname.equalsIgnoreCase("discard")) {
                        discardDetails = new DiscardDetails();
                    } else if (tagname.equalsIgnoreCase("tiles")) {
                        tileSet = new ArrayList<>();
                    }
                    break;

                case XmlPullParser.TEXT:
                    text = parser.getText();
                    break;

                case XmlPullParser.END_TAG:
                    if (tagname.equalsIgnoreCase("discard")) {
                        allDiscardDetails.add(discardDetails);
                    } else if (tagname.equalsIgnoreCase("tiles")) {
                        discardDetails.tiles.add(tileSet);
                    } else if (tagname.equalsIgnoreCase("number")) {
                        discardDetails.number = Integer.valueOf(text);
                    } else if (tagname.equalsIgnoreCase("tile")) {
                        Tile tile = getTileFromString(text);
                        tileSet.add(tile);
                    } else if (tagname.equalsIgnoreCase("description")) {
                        discardDetails.descriptions.add(text);
                    }
                    break;
            }
            eventType = parser.next();
        }
    }

    public static class DiscardDetails {
        public int number = -1;
        public List<List<Tile>> tiles = new ArrayList<>();
        public List<String> descriptions = new ArrayList<>();

        public String toString(){
            String s = "DiscardDetails: "+number+"\n";
            for(int i=0; i<tiles.size(); i++){
                s += tiles.get(i).toString()+" - "+descriptions.get(i)+"\n";
            }
            return s;
        }
    }

    private static Tile getTileFromString(String string){
        String[] strings = string.split(" ");
        if( strings.length==1 ){
            switch (string){
                case "East":
                    return new Tile(Tile.Wind.EAST);
                case "South":
                    return new Tile(Tile.Wind.SOUTH);
                case "West":
                    return new Tile(Tile.Wind.WEST);
                case "North":
                    return new Tile(Tile.Wind.NORTH);
                case "White":
                    return new Tile(Tile.Dragon.WHITE);
                case "Green":
                    return new Tile(Tile.Dragon.GREEN);
                case "Red":
                    return new Tile(Tile.Dragon.RED);
            }
        }

        int n = Integer.parseInt(strings[0]);
        switch (strings[1]){
            case "Man":
                return new Tile(n, Tile.Suit.MANZU);
            case "Pin":
                return new Tile(n, Tile.Suit.PINZU);
            case "Sou":
                return new Tile(n, Tile.Suit.SOUZU);
        }
        return null;
    }
}
