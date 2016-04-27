package com.mahjongmanager.riichi.utils;

import android.util.Log;

import com.mahjongmanager.riichi.Hand;
import com.mahjongmanager.riichi.MainActivity;
import com.mahjongmanager.riichi.ScoreCalculator;
import com.mahjongmanager.riichi.Tile;
import com.mahjongmanager.riichi.Yaku;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Copying the example hands at: https://en.wikipedia.org/wiki/Japanese_Mahjong_yaku
public class ExampleHands {
    private MainActivity activity;

    public ExampleHands(MainActivity ma){
        activity = ma;
        populateYakuDescriptions();
    }

    public List<Yaku> yakuDescriptions = new ArrayList<>();
    private void populateYakuDescriptions(){
        XmlPullParserFactory factory = null;
        try {
            factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();

            InputStream is = activity.getAssets().open("yaku.xml");
            xpp.setInput(is, null);

            parseXml( xpp );
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void parseXml(XmlPullParser parser) throws XmlPullParserException, IOException {
        int eventType = parser.getEventType();
        Yaku yaku = null;
        String text = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            String tagname = parser.getName();
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    if (tagname.equalsIgnoreCase("yaku")) {
                        yaku = new Yaku();
                    }
                    break;

                case XmlPullParser.TEXT:
                    text = parser.getText();
                    break;

                case XmlPullParser.END_TAG:
                    if (tagname.equalsIgnoreCase("yaku")) {
                        // add the example hand and then add yaku object to list
                        attachHand(yaku);
                        //Log.i("yakuDescription", yaku.toString());
                        yakuDescriptions.add(yaku);
                    } else if (tagname.equalsIgnoreCase("name")) {
                        yaku.name = Yaku.YakuName.valueOf(text);
                    } else if (tagname.equalsIgnoreCase("english")) {
                        yaku.english = text;
                    } else if (tagname.equalsIgnoreCase("romaji")) {
                        yaku.romaji = text;
                    } else if (tagname.equalsIgnoreCase("kanji")) {
                        yaku.kanji = text;
                    } else if (tagname.equalsIgnoreCase("closedValue")) {
                        yaku.hanClosed = text;
                    } else if (tagname.equalsIgnoreCase("openValue")) {
                        yaku.hanOpen = text;
                    } else if (tagname.equalsIgnoreCase("description")) {
                        yaku.description = text;
                    }
                    break;

                default:
                    break;
            }
            eventType = parser.next();
        }
    }

    private void attachHand(Yaku y){
        switch (y.name){
            case RIICHI:
                break;
            case CHIITOITSU:
                y.exampleHand = getChiitoitsuHand();
                break;
            case NAGASHI:
            case TSUMO:
            case IPPATSU:
            case HAITEI:
            case HOUTEI:
            case RINSHAN:
            case CHANKAN:
            case DOUBLERIICHI:
                break;
            case PINFU:
                y.exampleHand = getPinfuHand();
                break;
            case IIPEIKOU:
                y.exampleHand = getIipeikouHand();
                break;
            case SANSHOKUDOUJUN:
                y.exampleHand = getSanshokuDoujunHand();
                break;
            case ITTSUU:
                y.exampleHand = getIttsuuHand();
                break;
            case RYANPEIKOU:
                y.exampleHand = getRyanpeikouHand();
                break;
            case TOITOI:
                y.exampleHand = getToitoiHand();
                break;
            case SANANKOU:
                y.exampleHand = getSanAnkouHand();
                break;
            case SANSHOKUDOUKOU:
                y.exampleHand = getSanshokuDoukouHand();
                break;
            case SANKANTSU:
                y.exampleHand = getSanKantsuHand();
                break;
            case TANYAO:
                y.exampleHand = getTanyaoHand();
                break;
            case YAKUHAI:
                y.exampleHand = getYakuhaiHand();
                break;
            case CHANTA:
                y.exampleHand = getChantaHand();
                break;
            case JUNCHAN:
                y.exampleHand = getJunchanHand();
                break;
            case HONROUTOU:
                y.exampleHand = getHonroutouHand();
                break;
            case SHOUSANGEN:
                y.exampleHand = getShousangenHand();
                break;
            case HONITSU:
                y.exampleHand = getHonitsuHand();
                break;
            case CHINITSU:
                y.exampleHand = getChinitsuHand();
                break;
            case KOKUSHIMUSOU:
                y.exampleHand = getKokushiMusouHand();
                break;
            case KOKUSHIMUSOU13SIDED:
                y.exampleHand = getKokushiMusou13SidedHand();
                break;
            case SUUANKOU:
                y.exampleHand = getSuuAnkouHand();
                break;
            case SUUANKOUTANKI:
                y.exampleHand = getSuuAnkouTankiHand();
                break;
            case DAISANGEN:
                y.exampleHand = getDaisangenHand();
                break;
            case SHOUSUUSHII:
                y.exampleHand = getShousuushiiHand();
                break;
            case DAISUUSHII:
                y.exampleHand = getDaisuushiiHand();
                break;
            case TSUUIISOU:
                y.exampleHand = getTsuuiisouHand();
                break;
            case DAICHISEI:
                y.exampleHand = getDaichiseiHand();
                break;
            case CHINROUTOU:
                y.exampleHand = getChinroutouHand();
                break;
            case RYUUIISOU:
                y.exampleHand = getRyuuiisouHand();
                break;
            case CHUURENPOUTOU:
                y.exampleHand = getChuurenPoutouHand();
                break;
            case CHUURENPOUTOU9SIDED:
                y.exampleHand = getChuurenPoutou9SidedHand();
                break;
            case SUUKANTSU:
                y.exampleHand = getSuuKantsuHand();
                break;
            case TENHOU:
            case CHIIHOU:
            case RENHOU:
            case SANRENKOU:
            case SUURENKOU:
            case DAISHARIN:
            case SHIISANPUUTA:
            case SHIISUUPUUTA:
            case PARENCHAN:
                break;
        }
    }

    // Exception hands
    public static Hand getChiitoitsuHand(){
        Tile t1  = new Tile(3, "MANZU");
        Tile t2  = new Tile(3, "MANZU");
        Tile t3  = new Tile(1, "PINZU");
        Tile t4  = new Tile(1, "PINZU");
        Tile t5  = new Tile(5, "PINZU");
        Tile t6  = new Tile(5, "PINZU");
        Tile t7  = new Tile(1, "SOUZU");
        Tile t8  = new Tile(1, "SOUZU");
        Tile t9  = new Tile(8, "SOUZU");
        Tile t10 = new Tile(8, "SOUZU");
        Tile t11 = new Tile("EAST", "HONOR");
        Tile t12 = new Tile("EAST", "HONOR");
        Tile t13 = new Tile("RED", "HONOR");
        Tile t14 = new Tile("RED", "HONOR");

        t8.winningTile = true;
        t8.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }

    // Run-based hands
    public static Hand getPinfuHand(){
        Tile t1  = new Tile(1, "MANZU");
        Tile t2  = new Tile(2, "MANZU");
        Tile t3  = new Tile(3, "MANZU");
        Tile t4  = new Tile(5, "MANZU");
        Tile t5  = new Tile(5, "MANZU");
        Tile t6  = new Tile(2, "PINZU");
        Tile t7  = new Tile(3, "PINZU");
        Tile t8  = new Tile(4, "PINZU");
        Tile t9  = new Tile(7, "PINZU");
        Tile t10 = new Tile(8, "PINZU");
        Tile t11 = new Tile(9, "PINZU");
        Tile t12 = new Tile(4, "SOUZU");
        Tile t13 = new Tile(5, "SOUZU");
        Tile t14 = new Tile(6, "SOUZU");

        t6.winningTile = true;
        t6.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }
    public static Hand getIipeikouHand(){
        Tile t1  = new Tile(2, "MANZU");
        Tile t2  = new Tile(3, "MANZU");
        Tile t3  = new Tile(4, "MANZU");
        Tile t4  = new Tile(5, "PINZU");
        Tile t5  = new Tile(5, "PINZU");
        Tile t6  = new Tile(6, "PINZU");
        Tile t7  = new Tile(6, "PINZU");
        Tile t8  = new Tile(7, "PINZU");
        Tile t9  = new Tile(7, "PINZU");
        Tile t10 = new Tile(9, "SOUZU");
        Tile t11 = new Tile(9, "SOUZU");
        Tile t12 = new Tile(9, "SOUZU");
        Tile t13 = new Tile("GREEN", "HONOR");
        Tile t14 = new Tile("GREEN", "HONOR");

        t2.winningTile = true;
        t2.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }
    public static Hand getSanshokuDoujunHand(){
        Tile t1  = new Tile(2, "MANZU");
        Tile t2  = new Tile(3, "MANZU");
        Tile t3  = new Tile(4, "MANZU");
        Tile t4  = new Tile(1, "PINZU");
        Tile t5  = new Tile(1, "PINZU");
        Tile t6  = new Tile(1, "PINZU");
        Tile t7  = new Tile(2, "PINZU");
        Tile t8  = new Tile(3, "PINZU");
        Tile t9  = new Tile(4, "PINZU");
        Tile t10 = new Tile(2, "SOUZU");
        Tile t11 = new Tile(3, "SOUZU");
        Tile t12 = new Tile(4, "SOUZU");
        Tile t13 = new Tile("SOUTH", "HONOR");
        Tile t14 = new Tile("SOUTH", "HONOR");

        t1.revealedState = t2.revealedState = t3.revealedState = Tile.RevealedState.CHI;
        t1.calledFrom = Tile.CalledFrom.LEFT;
        t10.revealedState = t11.revealedState = t12.revealedState = Tile.RevealedState.CHI;
        t11.calledFrom = Tile.CalledFrom.LEFT;

        t14.winningTile = true;
        t14.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }
    public static Hand getIttsuuHand(){
        Tile t1  = new Tile(9, "MANZU");
        Tile t2  = new Tile(9, "MANZU");
        Tile t3  = new Tile(9, "MANZU");
        Tile t4  = new Tile(1, "SOUZU");
        Tile t5  = new Tile(2, "SOUZU");
        Tile t6  = new Tile(3, "SOUZU");
        Tile t7  = new Tile(4, "SOUZU");
        Tile t8  = new Tile(5, "SOUZU");
        Tile t9  = new Tile(6, "SOUZU");
        Tile t10 = new Tile(7, "SOUZU");
        Tile t11 = new Tile(8, "SOUZU");
        Tile t12 = new Tile(9, "SOUZU");
        Tile t13 = new Tile("WHITE", "HONOR");
        Tile t14 = new Tile("WHITE", "HONOR");

        t4.winningTile = true;
        t4.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }
    public static Hand getRyanpeikouHand(){
        Tile t1  = new Tile(5, "MANZU");
        Tile t2  = new Tile(5, "MANZU");
        Tile t3  = new Tile(7, "PINZU");
        Tile t4  = new Tile(7, "PINZU");
        Tile t5  = new Tile(8, "PINZU");
        Tile t6  = new Tile(8, "PINZU");
        Tile t7  = new Tile(9, "PINZU");
        Tile t8  = new Tile(9, "PINZU");
        Tile t9  = new Tile(7, "SOUZU");
        Tile t10 = new Tile(7, "SOUZU");
        Tile t11 = new Tile(8, "SOUZU");
        Tile t12 = new Tile(8, "SOUZU");
        Tile t13 = new Tile(9, "SOUZU");
        Tile t14 = new Tile(9, "SOUZU");

        t5.winningTile = true;
        t5.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }

    // Triplet/Quad-based hands
    public static Hand getToitoiHand(){
        Tile t1  = new Tile(8, "MANZU");
        Tile t2  = new Tile(8, "MANZU");
        Tile t3  = new Tile(8, "MANZU");
        Tile t4  = new Tile(3, "PINZU");
        Tile t5  = new Tile(3, "PINZU");
        Tile t6  = new Tile(3, "PINZU");
        Tile t7  = new Tile(1, "SOUZU");
        Tile t8  = new Tile(1, "SOUZU");
        Tile t9  = new Tile(1, "SOUZU");
        Tile t10 = new Tile(7, "SOUZU");
        Tile t11 = new Tile(7, "SOUZU");
        Tile t12 = new Tile(7, "SOUZU");
        Tile t13 = new Tile("NORTH", "HONOR");
        Tile t14 = new Tile("NORTH", "HONOR");

        t1.revealedState = t2.revealedState = t3.revealedState = Tile.RevealedState.PON;
        t2.calledFrom = Tile.CalledFrom.CENTER;
        t4.revealedState = t5.revealedState = t6.revealedState = Tile.RevealedState.PON;
        t4.calledFrom = Tile.CalledFrom.LEFT;
        t10.winningTile = true;
        t10.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }
    public static Hand getSanAnkouHand(){
        Tile t1  = new Tile(1, "MANZU");
        Tile t2  = new Tile(1, "MANZU");
        Tile t3  = new Tile(4, "MANZU");
        Tile t4  = new Tile(4, "MANZU");
        Tile t5  = new Tile(4, "MANZU");
        Tile t6  = new Tile(1, "PINZU");
        Tile t7  = new Tile(2, "PINZU");
        Tile t8  = new Tile(3, "PINZU");
        Tile t9  = new Tile(9, "PINZU");
        Tile t10 = new Tile(9, "PINZU");
        Tile t11 = new Tile(9, "PINZU");
        Tile t12 = new Tile(2, "SOUZU");
        Tile t13 = new Tile(2, "SOUZU");
        Tile t14 = new Tile(2, "SOUZU");

        t1.winningTile = true;
        t1.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }
    public static Hand getSanshokuDoukouHand(){
        Tile t1  = new Tile(3, "MANZU");
        Tile t2  = new Tile(3, "MANZU");
        Tile t3  = new Tile(3, "MANZU");
        Tile t4  = new Tile(3, "PINZU");
        Tile t5  = new Tile(3, "PINZU");
        Tile t6  = new Tile(3, "PINZU");
        Tile t7  = new Tile(6, "PINZU");
        Tile t8  = new Tile(7, "PINZU");
        Tile t9  = new Tile(8, "PINZU");
        Tile t10 = new Tile(3, "SOUZU");
        Tile t11 = new Tile(3, "SOUZU");
        Tile t12 = new Tile(3, "SOUZU");
        Tile t13 = new Tile("WEST", "HONOR");
        Tile t14 = new Tile("WEST", "HONOR");

        t4.revealedState = t5.revealedState = t6.revealedState = Tile.RevealedState.PON;
        t5.calledFrom = Tile.CalledFrom.CENTER;
        t3.winningTile = true;
        t3.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }
    public static Hand getSanKantsuHand(){
        Tile t1  = new Tile(9, "MANZU");
        Tile t2  = new Tile(9, "MANZU");
        Tile t3  = new Tile(9, "MANZU");
        Tile t4  = new Tile(9, "MANZU");
        Tile t5  = new Tile(2, "SOUZU");
        Tile t6  = new Tile(3, "SOUZU");
        Tile t7  = new Tile(4, "SOUZU");
        Tile t8  = new Tile(5, "SOUZU");
        Tile t9  = new Tile(5, "SOUZU");
        Tile t10 = new Tile(8, "PINZU");
        Tile t11 = new Tile(8, "PINZU");
        Tile t12 = new Tile(8, "PINZU");
        Tile t13 = new Tile(8, "PINZU");
        Tile t14 = new Tile("NORTH", "HONOR");
        Tile t15 = new Tile("NORTH", "HONOR");
        Tile t16 = new Tile("NORTH", "HONOR");
        Tile t17 = new Tile("NORTH", "HONOR");

        t1.revealedState = t2.revealedState = t3.revealedState = t4.revealedState = Tile.RevealedState.CLOSEDKAN;
        t10.revealedState = t11.revealedState = t12.revealedState = Tile.RevealedState.PON;
        t13.revealedState = Tile.RevealedState.ADDEDKAN;
        t11.calledFrom = Tile.CalledFrom.RIGHT;
        t14.revealedState = t15.revealedState = t16.revealedState = Tile.RevealedState.PON;
        t17.revealedState = Tile.RevealedState.ADDEDKAN;
        t14.calledFrom = Tile.CalledFrom.CENTER;

        t5.winningTile = true;
        t5.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }

    // Honor/Terminal-based hands
    public static Hand getTanyaoHand(){
        Tile t1  = new Tile(2, "MANZU");
        Tile t2  = new Tile(3, "MANZU");
        Tile t3  = new Tile(4, "MANZU");
        Tile t4  = new Tile(2, "PINZU");
        Tile t5  = new Tile(2, "PINZU");
        Tile t6  = new Tile(2, "PINZU");
        Tile t7  = new Tile(6, "PINZU");
        Tile t8  = new Tile(7, "PINZU");
        Tile t9  = new Tile(8, "PINZU");
        Tile t10 = new Tile(5, "SOUZU");
        Tile t11 = new Tile(5, "SOUZU");
        Tile t12 = new Tile(6, "SOUZU");
        Tile t13 = new Tile(7, "SOUZU");
        Tile t14 = new Tile(8, "SOUZU");

        t10.winningTile = true;
        t10.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }
    public static Hand getYakuhaiHand(){
        Tile t1  = new Tile(5, "MANZU");
        Tile t2  = new Tile(5, "MANZU");
        Tile t3  = new Tile(1, "PINZU");
        Tile t4  = new Tile(2, "PINZU");
        Tile t5  = new Tile(3, "PINZU");
        Tile t6  = new Tile(5, "PINZU");
        Tile t7  = new Tile(6, "PINZU");
        Tile t8  = new Tile(7, "PINZU");
        Tile t9  = new Tile(8, "SOUZU");
        Tile t10 = new Tile(8, "SOUZU");
        Tile t11 = new Tile(8, "SOUZU");
        Tile t12 = new Tile("RED", "HONOR");
        Tile t13 = new Tile("RED", "HONOR");
        Tile t14 = new Tile("RED", "HONOR");

        t14.winningTile = true;
        t14.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }
    public static Hand getChantaHand(){
        Tile t1  = new Tile(7, "MANZU");
        Tile t2  = new Tile(8, "MANZU");
        Tile t3  = new Tile(9, "MANZU");
        Tile t4  = new Tile(1, "PINZU");
        Tile t5  = new Tile(1, "PINZU");
        Tile t6  = new Tile(1, "PINZU");
        Tile t7  = new Tile(7, "PINZU");
        Tile t8  = new Tile(8, "PINZU");
        Tile t9  = new Tile(9, "PINZU");
        Tile t10 = new Tile(9, "SOUZU");
        Tile t11 = new Tile(9, "SOUZU");
        Tile t12 = new Tile(9, "SOUZU");
        Tile t13 = new Tile("WHITE", "HONOR");
        Tile t14 = new Tile("WHITE", "HONOR");

        t9.winningTile = true;
        t9.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }
    public static Hand getJunchanHand(){
        Tile t1  = new Tile(1, "MANZU");
        Tile t2  = new Tile(2, "MANZU");
        Tile t3  = new Tile(3, "MANZU");
        Tile t4  = new Tile(9, "MANZU");
        Tile t5  = new Tile(9, "MANZU");
        Tile t6  = new Tile(9, "MANZU");
        Tile t7  = new Tile(9, "PINZU");
        Tile t8  = new Tile(9, "PINZU");
        Tile t9  = new Tile(1, "SOUZU");
        Tile t10 = new Tile(2, "SOUZU");
        Tile t11 = new Tile(3, "SOUZU");
        Tile t12 = new Tile(7, "SOUZU");
        Tile t13 = new Tile(8, "SOUZU");
        Tile t14 = new Tile(9, "SOUZU");

        t11.winningTile = true;
        t11.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }
    public static Hand getHonroutouHand(){
        Tile t1  = new Tile(1, "MANZU");
        Tile t2  = new Tile(1, "MANZU");
        Tile t3  = new Tile(1, "MANZU");
        Tile t4  = new Tile(9, "PINZU");
        Tile t5  = new Tile(9, "PINZU");
        Tile t6  = new Tile(9, "PINZU");
        Tile t7  = new Tile(1, "SOUZU");
        Tile t8  = new Tile(1, "SOUZU");
        Tile t9  = new Tile(1, "SOUZU");
        Tile t10 = new Tile(9, "SOUZU");
        Tile t11 = new Tile(9, "SOUZU");
        Tile t12 = new Tile(9, "SOUZU");
        Tile t13 = new Tile("GREEN", "HONOR");
        Tile t14 = new Tile("GREEN", "HONOR");

        t1.revealedState = t2.revealedState = t3.revealedState = Tile.RevealedState.PON;
        t2.calledFrom = Tile.CalledFrom.RIGHT;
        t4.revealedState = t5.revealedState = t6.revealedState = Tile.RevealedState.PON;
        t5.calledFrom = Tile.CalledFrom.RIGHT;

        t9.winningTile = true;
        t9.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }
    public static Hand getHonroutouChiitoitsuHand(){
        Tile t1  = new Tile(1, "MANZU");
        Tile t2  = new Tile(1, "MANZU");
        Tile t3  = new Tile(1, "PINZU");
        Tile t4  = new Tile(1, "PINZU");
        Tile t5  = new Tile(9, "SOUZU");
        Tile t6  = new Tile(9, "SOUZU");
        Tile t7  = new Tile("EAST", "HONOR");
        Tile t8  = new Tile("EAST", "HONOR");
        Tile t9  = new Tile("SOUTH", "HONOR");
        Tile t10 = new Tile("SOUTH", "HONOR");
        Tile t11 = new Tile("WHITE", "HONOR");
        Tile t12 = new Tile("WHITE", "HONOR");
        Tile t13 = new Tile("NORTH", "HONOR");
        Tile t14 = new Tile("NORTH", "HONOR");

        t8.winningTile = true;
        t8.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }
    public static Hand getShousangenHand(){
        Tile t1  = new Tile(2, "MANZU");
        Tile t2  = new Tile(3, "MANZU");
        Tile t3  = new Tile(4, "MANZU");
        Tile t4  = new Tile(6, "SOUZU");
        Tile t5  = new Tile(7, "SOUZU");
        Tile t6  = new Tile(8, "SOUZU");
        Tile t7  = new Tile("WHITE", "HONOR");
        Tile t8  = new Tile("WHITE", "HONOR");
        Tile t9  = new Tile("WHITE", "HONOR");
        Tile t10 = new Tile("GREEN", "HONOR");
        Tile t11 = new Tile("GREEN", "HONOR");
        Tile t12 = new Tile("GREEN", "HONOR");
        Tile t13 = new Tile("RED", "HONOR");
        Tile t14 = new Tile("RED", "HONOR");

        t1.revealedState = t2.revealedState = t3.revealedState = Tile.RevealedState.CHI;
        t2.calledFrom = Tile.CalledFrom.LEFT;
        t10.revealedState = t11.revealedState = t12.revealedState = Tile.RevealedState.PON;
        t11.calledFrom = Tile.CalledFrom.CENTER;

        t9.winningTile = true;
        t9.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }

    // Suit-based
    public static Hand getHonitsuHand(){
        Tile t1  = new Tile(1, "PINZU");
        Tile t2  = new Tile(1, "PINZU");
        Tile t3  = new Tile(1, "PINZU");
        Tile t4  = new Tile(3, "PINZU");
        Tile t5  = new Tile(4, "PINZU");
        Tile t6  = new Tile(5, "PINZU");
        Tile t7  = new Tile(7, "PINZU");
        Tile t8  = new Tile(8, "PINZU");
        Tile t9  = new Tile(9, "PINZU");
        Tile t10 = new Tile("SOUTH", "HONOR");
        Tile t11 = new Tile("SOUTH", "HONOR");
        Tile t12 = new Tile("SOUTH", "HONOR");
        Tile t13 = new Tile("WHITE", "HONOR");
        Tile t14 = new Tile("WHITE", "HONOR");

        t6.winningTile = true;
        t6.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }
    public static Hand getChinitsuHand(){
        Tile t1  = new Tile(1, "SOUZU");
        Tile t2  = new Tile(2, "SOUZU");
        Tile t3  = new Tile(3, "SOUZU");
        Tile t4  = new Tile(4, "SOUZU");
        Tile t5  = new Tile(4, "SOUZU");
        Tile t6  = new Tile(4, "SOUZU");
        Tile t7  = new Tile(5, "SOUZU");
        Tile t8  = new Tile(5, "SOUZU");
        Tile t9  = new Tile(6, "SOUZU");
        Tile t10 = new Tile(6, "SOUZU");
        Tile t11 = new Tile(6, "SOUZU");
        Tile t12 = new Tile(7, "SOUZU");
        Tile t13 = new Tile(8, "SOUZU");
        Tile t14 = new Tile(9, "SOUZU");

        t4.revealedState = t5.revealedState = t6.revealedState = Tile.RevealedState.PON;
        t4.calledFrom = Tile.CalledFrom.CENTER;
        t9.revealedState = t10.revealedState = t11.revealedState = Tile.RevealedState.PON;
        t9.calledFrom = Tile.CalledFrom.LEFT;

        t2.winningTile = true;
        t2.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }

    // Yakuman
    public static Hand getKokushiMusouHand(){
        Tile t1  = new Tile(1, "MANZU");
        Tile t2  = new Tile(9, "MANZU");
        Tile t3  = new Tile(1, "PINZU");
        Tile t4  = new Tile(9, "PINZU");
        Tile t5  = new Tile(1, "SOUZU");
        Tile t6  = new Tile(9, "SOUZU");
        Tile t7  = new Tile("EAST", "HONOR");
        Tile t8  = new Tile("SOUTH", "HONOR");
        Tile t9  = new Tile("WEST", "HONOR");
        Tile t10 = new Tile("NORTH", "HONOR");
        Tile t11 = new Tile("WHITE", "HONOR");
        Tile t12 = new Tile("WHITE", "HONOR");
        Tile t13 = new Tile("GREEN", "HONOR");
        Tile t14 = new Tile("RED", "HONOR");

        t2.winningTile = true;
        t2.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }
    public static Hand getKokushiMusou13SidedHand(){
        Tile t1  = new Tile(1, "MANZU");
        Tile t2  = new Tile(9, "MANZU");
        Tile t3  = new Tile(1, "PINZU");
        Tile t4  = new Tile(9, "PINZU");
        Tile t5  = new Tile(1, "SOUZU");
        Tile t6  = new Tile(9, "SOUZU");
        Tile t7  = new Tile("EAST", "HONOR");
        Tile t8  = new Tile("SOUTH", "HONOR");
        Tile t9  = new Tile("WEST", "HONOR");
        Tile t10 = new Tile("NORTH", "HONOR");
        Tile t11 = new Tile("WHITE", "HONOR");
        Tile t12 = new Tile("WHITE", "HONOR");
        Tile t13 = new Tile("GREEN", "HONOR");
        Tile t14 = new Tile("RED", "HONOR");

        t12.winningTile = true;
        t12.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }
    public static Hand getSuuAnkouHand(){
        Tile t1  = new Tile(4, "MANZU");
        Tile t2  = new Tile(4, "MANZU");
        Tile t3  = new Tile(4, "MANZU");
        Tile t4  = new Tile(8, "MANZU");
        Tile t5  = new Tile(8, "MANZU");
        Tile t6  = new Tile(8, "MANZU");
        Tile t7  = new Tile(9, "PINZU");
        Tile t8  = new Tile(9, "PINZU");
        Tile t9  = new Tile(9, "PINZU");
        Tile t10 = new Tile(2, "SOUZU");
        Tile t11 = new Tile(2, "SOUZU");
        Tile t12 = new Tile(2, "SOUZU");
        Tile t13 = new Tile("EAST", "HONOR");
        Tile t14 = new Tile("EAST", "HONOR");

        t6.winningTile = true;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }
    public static Hand getSuuAnkouTankiHand(){
        Tile t1  = new Tile(5, "MANZU");
        Tile t2  = new Tile(5, "MANZU");
        Tile t3  = new Tile(5, "MANZU");
        Tile t4  = new Tile(2, "PINZU");
        Tile t5  = new Tile(2, "PINZU");
        Tile t6  = new Tile(2, "PINZU");
        Tile t7  = new Tile(9, "PINZU");
        Tile t8  = new Tile(9, "PINZU");
        Tile t9  = new Tile(9, "PINZU");
        Tile t10 = new Tile("NORTH", "HONOR");
        Tile t11 = new Tile("NORTH", "HONOR");
        Tile t12 = new Tile("NORTH", "HONOR");
        Tile t13 = new Tile("GREEN", "HONOR");
        Tile t14 = new Tile("GREEN", "HONOR");

        t14.winningTile = true;
        t14.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }
    public static Hand getDaisangenHand(){
        Tile t1  = new Tile(5, "MANZU");
        Tile t2  = new Tile(6, "MANZU");
        Tile t3  = new Tile(7, "MANZU");
        Tile t4  = new Tile(4, "PINZU");
        Tile t5  = new Tile(4, "PINZU");
        Tile t6  = new Tile("WHITE", "HONOR");
        Tile t7  = new Tile("WHITE", "HONOR");
        Tile t8  = new Tile("WHITE", "HONOR");
        Tile t9  = new Tile("GREEN", "HONOR");
        Tile t10 = new Tile("GREEN", "HONOR");
        Tile t11 = new Tile("GREEN", "HONOR");
        Tile t12 = new Tile("RED", "HONOR");
        Tile t13 = new Tile("RED", "HONOR");
        Tile t14 = new Tile("RED", "HONOR");

        t10.winningTile = true;
        t10.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }
    public static Hand getShousuushiiHand(){
        Tile t1  = new Tile(6, "SOUZU");
        Tile t2  = new Tile(7, "SOUZU");
        Tile t3  = new Tile(8, "SOUZU");
        Tile t4  = new Tile("EAST", "HONOR");
        Tile t5  = new Tile("EAST", "HONOR");
        Tile t6  = new Tile("EAST", "HONOR");
        Tile t7  = new Tile("SOUTH", "HONOR");
        Tile t8  = new Tile("SOUTH", "HONOR");
        Tile t9  = new Tile("SOUTH", "HONOR");
        Tile t10 = new Tile("WEST", "HONOR");
        Tile t11 = new Tile("WEST", "HONOR");
        Tile t12 = new Tile("WEST", "HONOR");
        Tile t13 = new Tile("NORTH", "HONOR");
        Tile t14 = new Tile("NORTH", "HONOR");

        t11.winningTile = true;
        t11.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }
    public static Hand getDaisuushiiHand(){
        Tile t1  = new Tile(3, "PINZU");
        Tile t2  = new Tile(3, "PINZU");
        Tile t3  = new Tile("EAST", "HONOR");
        Tile t4  = new Tile("EAST", "HONOR");
        Tile t5  = new Tile("EAST", "HONOR");
        Tile t6  = new Tile("SOUTH", "HONOR");
        Tile t7  = new Tile("SOUTH", "HONOR");
        Tile t8  = new Tile("SOUTH", "HONOR");
        Tile t9  = new Tile("WEST", "HONOR");
        Tile t10 = new Tile("WEST", "HONOR");
        Tile t11 = new Tile("WEST", "HONOR");
        Tile t12 = new Tile("NORTH", "HONOR");
        Tile t13 = new Tile("NORTH", "HONOR");
        Tile t14 = new Tile("NORTH", "HONOR");

        t13.winningTile = true;
        t13.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }
    public static Hand getTsuuiisouHand(){
        Tile t1  = new Tile("EAST", "HONOR");
        Tile t2  = new Tile("EAST", "HONOR");
        Tile t3  = new Tile("EAST", "HONOR");
        Tile t4  = new Tile("SOUTH", "HONOR");
        Tile t5  = new Tile("SOUTH", "HONOR");
        Tile t6  = new Tile("SOUTH", "HONOR");
        Tile t7  = new Tile("WEST", "HONOR");
        Tile t8  = new Tile("WEST", "HONOR");
        Tile t9  = new Tile("WHITE", "HONOR");
        Tile t10 = new Tile("WHITE", "HONOR");
        Tile t11 = new Tile("WHITE", "HONOR");
        Tile t12 = new Tile("GREEN", "HONOR");
        Tile t13 = new Tile("GREEN", "HONOR");
        Tile t14 = new Tile("GREEN", "HONOR");

        t3.winningTile = true;
        t3.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }
    public static Hand getDaichiseiHand(){
        Tile t1  = new Tile("EAST", "HONOR");
        Tile t2  = new Tile("EAST", "HONOR");
        Tile t3  = new Tile("SOUTH", "HONOR");
        Tile t4  = new Tile("SOUTH", "HONOR");
        Tile t5  = new Tile("WEST", "HONOR");
        Tile t6  = new Tile("WEST", "HONOR");
        Tile t7  = new Tile("NORTH", "HONOR");
        Tile t8  = new Tile("NORTH", "HONOR");
        Tile t9  = new Tile("WHITE", "HONOR");
        Tile t10 = new Tile("WHITE", "HONOR");
        Tile t11 = new Tile("GREEN", "HONOR");
        Tile t12 = new Tile("GREEN", "HONOR");
        Tile t13 = new Tile("RED", "HONOR");
        Tile t14 = new Tile("RED", "HONOR");

        t9.winningTile = true;
        t9.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }
    public static Hand getChinroutouHand(){
        Tile t1  = new Tile(1, "MANZU");
        Tile t2  = new Tile(1, "MANZU");
        Tile t3  = new Tile(1, "MANZU");
        Tile t4  = new Tile(1, "PINZU");
        Tile t5  = new Tile(1, "PINZU");
        Tile t6  = new Tile(1, "PINZU");
        Tile t7  = new Tile(9, "PINZU");
        Tile t8  = new Tile(9, "PINZU");
        Tile t9  = new Tile(9, "PINZU");
        Tile t10 = new Tile(1, "SOUZU");
        Tile t11 = new Tile(1, "SOUZU");
        Tile t12 = new Tile(9, "SOUZU");
        Tile t13 = new Tile(9, "SOUZU");
        Tile t14 = new Tile(9, "SOUZU");

        t7.revealedState = t8.revealedState = t9.revealedState = Tile.RevealedState.PON;
        t7.calledFrom = Tile.CalledFrom.RIGHT;

        t2.winningTile = true;
        t2.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }
    public static Hand getRyuuiisouHand(){
        Tile t1  = new Tile(2, "SOUZU");
        Tile t2  = new Tile(2, "SOUZU");
        Tile t3  = new Tile(3, "SOUZU");
        Tile t4  = new Tile(3, "SOUZU");
        Tile t5  = new Tile(4, "SOUZU");
        Tile t6  = new Tile(4, "SOUZU");
        Tile t7  = new Tile(6, "SOUZU");
        Tile t8  = new Tile(6, "SOUZU");
        Tile t9  = new Tile(6, "SOUZU");
        Tile t10 = new Tile(8, "SOUZU");
        Tile t11 = new Tile(8, "SOUZU");
        Tile t12 = new Tile("GREEN", "HONOR");
        Tile t13 = new Tile("GREEN", "HONOR");
        Tile t14 = new Tile("GREEN", "HONOR");

        t8.winningTile = true;
        t8.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }
    public static Hand getChuurenPoutouHand(){
        Tile t1  = new Tile(1, "MANZU");
        Tile t2  = new Tile(1, "MANZU");
        Tile t3  = new Tile(1, "MANZU");
        Tile t4  = new Tile(2, "MANZU");
        Tile t5  = new Tile(3, "MANZU");
        Tile t6  = new Tile(4, "MANZU");
        Tile t7  = new Tile(4, "MANZU");
        Tile t8  = new Tile(5, "MANZU");
        Tile t9  = new Tile(6, "MANZU");
        Tile t10 = new Tile(7, "MANZU");
        Tile t11 = new Tile(8, "MANZU");
        Tile t12 = new Tile(9, "MANZU");
        Tile t13 = new Tile(9, "MANZU");
        Tile t14 = new Tile(9, "MANZU");

        t11.winningTile = true;
        t11.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }
    public static Hand getChuurenPoutou9SidedHand(){
        Tile t1  = new Tile(1, "MANZU");
        Tile t2  = new Tile(1, "MANZU");
        Tile t3  = new Tile(1, "MANZU");
        Tile t4  = new Tile(2, "MANZU");
        Tile t5  = new Tile(3, "MANZU");
        Tile t6  = new Tile(4, "MANZU");
        Tile t7  = new Tile(5, "MANZU");
        Tile t8  = new Tile(6, "MANZU");
        Tile t9  = new Tile(7, "MANZU");
        Tile t10 = new Tile(8, "MANZU");
        Tile t11 = new Tile(8, "MANZU");
        Tile t12 = new Tile(9, "MANZU");
        Tile t13 = new Tile(9, "MANZU");
        Tile t14 = new Tile(9, "MANZU");

        t10.winningTile = true;
        t10.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }
    public static Hand getSuuKantsuHand(){
        Tile t1  = new Tile(1, "MANZU");
        Tile t2  = new Tile(1, "MANZU");
        Tile t3  = new Tile(1, "MANZU");
        Tile t4  = new Tile(1, "MANZU");
        Tile t5  = new Tile(4, "PINZU");
        Tile t6  = new Tile(4, "PINZU");
        Tile t7  = new Tile(4, "PINZU");
        Tile t8  = new Tile(4, "PINZU");
        Tile t9  = new Tile(9, "PINZU");
        Tile t10 = new Tile(9, "PINZU");
        Tile t11 = new Tile("WHITE", "HONOR");
        Tile t12 = new Tile("WHITE", "HONOR");
        Tile t13 = new Tile("WHITE", "HONOR");
        Tile t14 = new Tile("WHITE", "HONOR");
        Tile t15 = new Tile("NORTH", "HONOR");
        Tile t16 = new Tile("NORTH", "HONOR");
        Tile t17 = new Tile("NORTH", "HONOR");
        Tile t18 = new Tile("NORTH", "HONOR");

        t1.revealedState = t2.revealedState = t3.revealedState = t4.revealedState = Tile.RevealedState.CLOSEDKAN;
        t5.revealedState = t6.revealedState = t7.revealedState = Tile.RevealedState.PON;
        t8.revealedState = Tile.RevealedState.ADDEDKAN;
        t7.calledFrom = Tile.CalledFrom.RIGHT;
        t11.revealedState = t12.revealedState = t13.revealedState = t14.revealedState = Tile.RevealedState.OPENKAN;
        t12.calledFrom = Tile.CalledFrom.CENTER;
        t15.revealedState = t16.revealedState = t17.revealedState = Tile.RevealedState.PON;
        t18.revealedState = Tile.RevealedState.ADDEDKAN;
        t17.calledFrom = Tile.CalledFrom.CENTER;


        t9.winningTile = true;
        t9.calledFrom = Tile.CalledFrom.CENTER;

        Hand h = new Hand(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18));

        ScoreCalculator sc = new ScoreCalculator(h);
        return sc.validatedHand;
    }
}
