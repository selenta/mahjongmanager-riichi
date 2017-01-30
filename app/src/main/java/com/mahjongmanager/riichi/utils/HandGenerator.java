package com.mahjongmanager.riichi.utils;

import android.content.Context;

import com.mahjongmanager.riichi.common.Hand;
import com.mahjongmanager.riichi.common.Meld;
import com.mahjongmanager.riichi.common.Tile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

/**
 * Used to generate completely random hands. There is some massaging to make them slightly
 * more interesting, but the only thing can currently be determined before generating the hand are:
 * <ul>
 *     <li>Number of Suits (default - 3)</li>
 *     <li>Allow Honors (default - true)</li>
 * </ul>
 * For now, stick to using h=completelyRandomHand() followed by addSituationalYaku(h) (optional).
 * Generating yaku first and then a hands from that is probably more effort than it's worth, tbh
 */
public class HandGenerator {
    private HashMap<String, Double> yakuOdds = new HashMap<>();

    /**
     * When generating melds for a hand, each starts with a founding (aka seed) tile from which
     * the rest of the meld is generated. The founding tile does not influence what kind of
     * meld (pon, open kan, chi, etc) it will become when the rest of the tiles are added.
     */
    private List<Tile> foundingTileOptions = new ArrayList<>();
    private int numberOfSuits = 3;
    private boolean allowHonors = true;

    private Meld pair;
    private Meld meld1;
    private Meld meld2;
    private Meld meld3;
    private Meld meld4;

    public HandGenerator(){
        yakuOdds.put("Chiitoitsu", 0.1);
        yakuOdds.put("Honroutou",  0.2);
        yakuOdds.put("Tanyao",     0.4);
        yakuOdds.put("Honitsu",    0.25);
        yakuOdds.put("Chinitsu",   0.15);
        yakuOdds.put("Riichi",  0.8);
        yakuOdds.put("Ippatsu", 0.4);
        yakuOdds.put("Tsumo",   0.2);
        yakuOdds.put("Rinshan", 0.3);
        yakuOdds.put("Haitei",  0.1);
        yakuOdds.put("Houtei",  0.1);
        cleanup();
    }
    public HandGenerator(Context current){
        populateYakuOdds(current);
        cleanup();
    }

    public Hand generateSpeedQuizHand(){
        numberOfSuits = AppSettings.getSpeedQuizNumberOfSuits();
        allowHonors = AppSettings.getSpeedQuizAllowHonors();

        Hand hand = completelyRandomHand();

        if( AppSettings.getSpeedQuizSituationalYaku() ){
            addSituationalYaku(hand);
        }

        if( AppSettings.getSpeedQuizRandomWinds() ){
            hand.prevailingWind = Utils.getRandomWind();
            hand.playerWind = Utils.getRandomWind();
        }

        return hand;
    }

    public Hand completelyRandomHand(){
        Log.d("completelyRandomHand", "Number of suits: " + numberOfSuits);
        Log.d("completelyRandomHand", "Allow honors: " + allowHonors);
        initFoundingTileOptions();

        //Decide if special hand structure (chiitoitsu) first
        if( Math.random()<yakuOdds.get("Chiitoitsu") ){
            return generateChiitoitsu();
        }

        createMelds();
        randomWinningTile();

        Hand hand = new Hand( usedTiles() );
        cleanup();
        return hand;
    }
    private void createMelds(){
        createFounderTiles();

        pair.addTile(new Tile(pair.firstTile()));

        expandMeld(meld1);
        expandMeld(meld2);
        expandMeld(meld3);
        expandMeld(meld4);
    }
    private void createFounderTiles(){
        pair.addTile(randomFoundingTile());
        meld1.addTile(randomFoundingTile());
        meld2.addTile(randomFoundingTile());
        meld3.addTile(randomFoundingTile());
        meld4.addTile(randomFoundingTile());
    }
    private void expandMeld(Meld meld){
        Double roll = Math.random();
        if(roll<0.7 && meld.getSuit()!=Tile.Suit.HONOR ){
            expandChii(meld);
        } else if(roll<0.1) {
            expandKan(meld);
        } else {
            expandPon(meld);
        }
    }
    private void expandPon(Meld meld){
        Tile founder = meld.firstTile();
        meld.addTile(new Tile(founder));
        meld.addTile(new Tile(founder));

        if( Math.random()<0.4 ){
            Tile calledTile = meld.getRandomTile();
            calledTile.calledFrom = Tile.CalledFrom.CENTER;
            for(Tile t : meld.getTiles()){
                t.revealedState = Tile.RevealedState.PON;
            }
        }
    }
    private void expandKan(Meld meld){
        Tile founder = meld.firstTile();
        meld.addTile(new Tile(founder));
        meld.addTile(new Tile(founder));
        meld.addTile(new Tile(founder));

        Double roll = Math.random();
        if( roll<0.4 ){
            for( Tile t : meld.getTiles() ){
                t.revealedState=Tile.RevealedState.CLOSEDKAN;
            }
        } else if( roll<0.7 ){
            List<Tile> remainders = new ArrayList<>();
            remainders.addAll(meld.getTiles());

            Tile calledTile = meld.getRandomTile();
            calledTile.calledFrom = Tile.CalledFrom.CENTER;
            calledTile.revealedState = Tile.RevealedState.PON;
            remainders.remove(calledTile);

            Tile addedTile = remainders.get(new Random().nextInt(remainders.size()));
            addedTile.revealedState = Tile.RevealedState.ADDEDKAN;
            remainders.remove(addedTile);

            for(Tile t : remainders){
                t.revealedState = Tile.RevealedState.PON;
            }
        } else {
            Tile calledTile = meld.getRandomTile();
            calledTile.calledFrom = Tile.CalledFrom.CENTER;

            for(Tile t : meld.getTiles()){
                t.revealedState = Tile.RevealedState.OPENKAN;
            }
        }
    }
    private void expandChii(Meld meld){
        Tile founder = meld.firstTile();

        if(founder.number==1){
            meld.addTile(new Tile(2, founder.suit));
            meld.addTile(new Tile(3, founder.suit));
        } else if(founder.number==9){
            meld.addTile(new Tile(8, founder.suit));
            meld.addTile(new Tile(7, founder.suit));
        } else {
            meld.addTile(new Tile(founder.number-1, founder.suit));
            meld.addTile(new Tile(founder.number+1, founder.suit));
        }

        if( Math.random()<0.3 ){
            Tile calledTile = meld.getRandomTile();
            calledTile.calledFrom = Tile.CalledFrom.LEFT;
            for(Tile t : meld.getTiles()){
                t.revealedState = Tile.RevealedState.CHI;
            }
        }
    }
    private List<Tile> usedTiles(){
        List<Tile> tilesInMelds = new ArrayList<>();
        tilesInMelds.addAll( pair.getTiles());
        tilesInMelds.addAll(meld1.getTiles());
        tilesInMelds.addAll(meld2.getTiles());
        tilesInMelds.addAll(meld3.getTiles());
        tilesInMelds.addAll(meld4.getTiles());

        return tilesInMelds;
    }

    /**
     * This method will randomly add situational yaku and dora that are possible
     * based on the state of the hand. This must be called separately, as this is a
     * configurable option for the Speed Quiz.
     * @param h Hand to be modified
     */
    public void addSituationalYaku(Hand h){
        // Riichi, Ippatsu, Tsumo, Rinshan, Haitei, Houtei
        if( Math.random()<yakuOdds.get("Riichi") && !h.isOpen() ){
            addRiichi(h);
        }
        if( Math.random()<yakuOdds.get("Ippatsu") && h.riichi ){
            addIppatsu(h);
        }
        if( Math.random()<yakuOdds.get("Tsumo") && !h.isOpen() ){
            addTsumo(h);
        }
        if( Math.random()<yakuOdds.get("Rinshan")
                && h.hasKan()
                && h.countTile(h.getWinningTile())==1
                && h.getWinningTile().calledFrom==Tile.CalledFrom.NONE){
            addRinshan(h);
        }
        if( Math.random()<yakuOdds.get("Haitei")
                && h.getWinningTile().calledFrom==Tile.CalledFrom.NONE ){
            addHaitei(h);
        }
        if( Math.random()<yakuOdds.get("Houtei")
                && h.getWinningTile().calledFrom!=Tile.CalledFrom.NONE ){
            addHoutei(h);
        }

        // Dora indicators (20% chance someone else has called a Kan)
        Integer doraIndicators = 1;
        doraIndicators = (h.meld1.isKan()) ? doraIndicators+1 : doraIndicators;
        doraIndicators = (h.meld2.isKan()) ? doraIndicators+1 : doraIndicators;
        doraIndicators = (h.meld3.isKan()) ? doraIndicators+1 : doraIndicators;
        doraIndicators = (h.meld4.isKan()) ? doraIndicators+1 : doraIndicators;
        doraIndicators = (Math.random()<0.2) ? doraIndicators+1 : doraIndicators;
        for(int i=0; i<doraIndicators ; i++){
            addDoraIndicator(h);
        }
    }


    /////////////////////////////////////////////////////////////////////////
    ///////////////       Convenience Methods        ////////////////////////
    /////////////////////////////////////////////////////////////////////////
    private Tile randomFoundingTile(){
        return randomTile(foundingTileOptions);
    }
    private Tile randomTile(List<Tile> options){
        return options.get(new Random().nextInt(options.size()));
    }

    private void removeTilesFromFounders( List<Tile> removeList ){
        List<Tile> markedForRemoval = new ArrayList<>();
        for( Tile exc : removeList ){
            for( Tile tile : foundingTileOptions ){
                if( exc.isSame(tile) ){
                    markedForRemoval.add(tile);
                }
            }
        }
        foundingTileOptions.removeAll(markedForRemoval);
    }

    //modify hand
    private void randomWinningTile(){
        List<Tile> candidates = new ArrayList<>();
        candidates.addAll(pair.getTiles());

        if( meld1.firstTile().revealedState == Tile.RevealedState.NONE ){
            candidates.addAll(meld1.getTiles());
        }
        if( meld2.firstTile().revealedState == Tile.RevealedState.NONE ){
            candidates.addAll(meld2.getTiles());
        }
        if( meld3.firstTile().revealedState == Tile.RevealedState.NONE ){
            candidates.addAll(meld3.getTiles());
        }
        if( meld4.firstTile().revealedState == Tile.RevealedState.NONE ){
            candidates.addAll(meld4.getTiles());
        }

        Tile wTile = randomTile(candidates);

        setWinningTileState(wTile);
    }
    private void setWinningTileState(Tile t){
        t.winningTile = true;

        List<Tile.CalledFrom> calledFroms = new ArrayList<>();
        calledFroms.add(Tile.CalledFrom.NONE);
        calledFroms.add(Tile.CalledFrom.NONE);
        calledFroms.add(Tile.CalledFrom.NONE);
        calledFroms.add(Tile.CalledFrom.LEFT);
        calledFroms.add(Tile.CalledFrom.CENTER);
        calledFroms.add(Tile.CalledFrom.RIGHT);
        t.calledFrom = calledFroms.get(new Random().nextInt(calledFroms.size()));
    }
    private void addDoraIndicator(Hand h){
        if( h.doraIndicator1==null ){
            h.doraIndicator1    = randomTile(allTiles());
            h.uraDoraIndicator1 = randomTile(allTiles());
        } else if( h.doraIndicator2==null ){
            h.doraIndicator2    = randomTile(allTiles());
            h.uraDoraIndicator2 = randomTile(allTiles());
        } else if( h.doraIndicator3==null ){
            h.doraIndicator3    = randomTile(allTiles());
            h.uraDoraIndicator3 = randomTile(allTiles());
        } else if( h.doraIndicator4==null ){
            h.doraIndicator4    = randomTile(allTiles());
            h.uraDoraIndicator4 = randomTile(allTiles());
        }
    }

    public static List<Tile> allTiles(){
        List<Tile> tiles = new ArrayList<>();
        tiles.addAll(allManzu());
        tiles.addAll(allPinzu());
        tiles.addAll(allSouzu());
        tiles.addAll(allHonors());
        return tiles;
    }
    private static List<Tile> allOfSuit(Tile.Suit s){
        if( s==Tile.Suit.MANZU ){
            return allManzu();
        } else if( s==Tile.Suit.PINZU ){
            return allPinzu();
        } else if( s==Tile.Suit.SOUZU ){
            return allSouzu();
        }
        return allHonors();
    }
    public static List<Tile> allManzu(){
        List<Tile> tiles = new ArrayList<>();
        for(int i=1; i<10; i++){
            tiles.add(new Tile(i, Tile.Suit.MANZU));
        }
        return tiles;
    }
    public static List<Tile> allPinzu(){
        List<Tile> tiles = new ArrayList<>();
        for(int i=1; i<10; i++){
            tiles.add(new Tile(i, Tile.Suit.PINZU));
        }
        return tiles;
    }
    public static List<Tile> allSouzu(){
        List<Tile> tiles = new ArrayList<>();
        for(int i=1; i<10; i++){
            tiles.add(new Tile(i, Tile.Suit.SOUZU));
        }
        return tiles;
    }
    public static List<Tile> allHonors(){
        List<Tile> tiles = new ArrayList<>();
        tiles.addAll(allWinds());
        tiles.addAll(allDragons());
        return tiles;
    }
    public static List<Tile> allWinds(){
        List<Tile> tiles = new ArrayList<>();
        tiles.add(new Tile(Tile.Wind.EAST));
        tiles.add(new Tile(Tile.Wind.SOUTH));
        tiles.add(new Tile(Tile.Wind.WEST));
        tiles.add(new Tile(Tile.Wind.NORTH));
        return tiles;
    }
    public static List<Tile> allDragons(){
        List<Tile> tiles = new ArrayList<>();
        tiles.add(new Tile(Tile.Dragon.WHITE));
        tiles.add(new Tile(Tile.Dragon.GREEN));
        tiles.add(new Tile(Tile.Dragon.RED));
        return tiles;
    }
    public static List<Tile> allTerminals(){
        List<Tile> tiles = new ArrayList<>();
        tiles.add(new Tile(1, Tile.Suit.MANZU));
        tiles.add(new Tile(9, Tile.Suit.MANZU));
        tiles.add(new Tile(1, Tile.Suit.PINZU));
        tiles.add(new Tile(9, Tile.Suit.PINZU));
        tiles.add(new Tile(1, Tile.Suit.SOUZU));
        tiles.add(new Tile(9, Tile.Suit.SOUZU));
        return tiles;
    }
    public static List<Tile> allSimples(){
        List<Tile> tiles = new ArrayList<>();
        for(int i=2; i<9; i++){
            tiles.add(new Tile(i, Tile.Suit.MANZU));
            tiles.add(new Tile(i, Tile.Suit.PINZU));
            tiles.add(new Tile(i, Tile.Suit.SOUZU));
        }
        return tiles;
    }


    ////////////////////////////////////////////////////////////////////////
    ///////////////          Yaku Methods           ////////////////////////
    ////////////////////////////////////////////////////////////////////////
    private Hand generateChiitoitsu(){
        if( Math.random()<yakuOdds.get("Honroutou") && allowHonors ){
            restrictFoundersToHonroutou();
        } else if( Math.random()<yakuOdds.get("Tanyao") ){
            restrictFoundersToTanyao();
        } else if( Math.random()<yakuOdds.get("Chinitsu") ){
            restrictFoundersToChinitsu();
        } else if( Math.random()<yakuOdds.get("Honitsu") && allowHonors ){
            restrictFoundersToHonitsu();
        }
        //daichisei
        // end early (retry if get a ryanpeikou hand?)

        Hand hand = createChiitoitsu();
        setWinningTileState(randomTile(hand.tiles));

        hand.sort();
        cleanup();
        return hand;
    }
    private Hand createChiitoitsu(){
        Hand h = new Hand(new ArrayList<Tile>());
        for(int i=0; i<7; i++ ){
            Tile newF;
            while(true){
                newF = randomFoundingTile();
                boolean unique = true;
                for( Tile used : h.tiles ){
                    if( newF.isSame(used) ){
                        unique = false;
                        break;
                    }
                }
                if(unique){
                    break;
                }
            }

            h.addTile(newF);
            h.addTile(new Tile(newF));
        }
        return h;
    }

    private void restrictFoundersToTanyao(){
        removeTilesFromFounders( allHonors() );
        removeTilesFromFounders( allTerminals() );
        Log.d("restrictFoundersToTanyao", "foundingTileOptions: " + foundingTileOptions);
    }
    private void restrictFoundersToHonroutou(){
        removeTilesFromFounders( allSimples() );
        Log.d("restrictFoundersToHonroutou", "foundingTileOptions: " + foundingTileOptions);
    }
    private void restrictFoundersToHonitsu(){
        //get a random non-honor suit from founding tiles
        Tile.Suit honitsuSuit = null;
        while( honitsuSuit==null ){
            Tile randTile = randomTile(foundingTileOptions);
            if( randTile.suit != Tile.Suit.HONOR ){
                honitsuSuit = randTile.suit;
            }
        }

        //all tiles that are not of the chosen suit are no longer options for founding tiles
        for( Tile.Suit suit : Tile.Suit.values() ){
            if( suit != honitsuSuit ){
                removeTilesFromFounders( allOfSuit(honitsuSuit) );
            }
        }
        Log.d("restrictFoundersToHonitsu", "foundingTileOptions: " + foundingTileOptions );
    }
    private void restrictFoundersToChinitsu(){
        //get a random non-honor suit from founding tiles
        Tile.Suit chinitsuSuit = null;
        while( chinitsuSuit==null ){
            Tile randTile = randomTile(foundingTileOptions);
            if( randTile.suit!= Tile.Suit.HONOR ){
                chinitsuSuit = randTile.suit;
            }
        }

        //all tiles that are not of the chosen suit are no longer options for founding tiles
        //  honors are also removed
        removeTilesFromFounders( allHonors() );
        for( Tile.Suit suit : Tile.Suit.values() ){
            if( suit != chinitsuSuit ){
                removeTilesFromFounders( allOfSuit(chinitsuSuit) );
            }
        }
        Log.d("restrictFoundersToChinitsu", "foundingTileOptions: " + foundingTileOptions);
    }

    private void addRiichi(Hand h){
        h.riichi = true;
    }
    private void addIppatsu(Hand h){
        h.ippatsu = true;
    }
    private void addTsumo(Hand h){
        h.getWinningTile().calledFrom = Tile.CalledFrom.NONE;
    }
    private void addHaitei(Hand h){
        h.haitei = true;
    }
    private void addHoutei(Hand h){
        h.houtei = true;
    }
    private void addRinshan(Hand h){
        h.rinshan = true;
    }


    /////////////////////////////////////////////////
    /////////////    Init/Cleanup     ///////////////
    /////////////////////////////////////////////////
    private void populateYakuOdds(Context context){
        InputStream inputStream = null;
        try {
            inputStream =  context.getResources().getAssets().open("RiichiStats.csv");
            CSVFile csvFile = new CSVFile(inputStream);
            List<String[]> scoreList = csvFile.read();

            for( String[] yakuStat : scoreList ){
                Log.i("readCsv", "yakuStat: " + Arrays.toString(yakuStat));
                Double yakuOdd = Double.valueOf(yakuStat[2])/100.0;
                yakuOdds.put(yakuStat[1], yakuOdd);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("readCsv", "yakuOdds: " + yakuOdds.toString());
    }
    private void cleanup(){
        numberOfSuits = 3;
        allowHonors = true;
        pair  = new Meld();
        meld1 = new Meld();
        meld2 = new Meld();
        meld3 = new Meld();
        meld4 = new Meld();
        initFoundingTileOptions();
    }
    private void initFoundingTileOptions(){
        foundingTileOptions.clear();

        HashSet<Tile.Suit> usedSuits = new HashSet<>();
        while( usedSuits.size() < numberOfSuits ){
            Tile.Suit suit = Utils.getRandomSuit();
            if( suit != Tile.Suit.HONOR && usedSuits.add(suit) ){
                foundingTileOptions.addAll(allOfSuit(suit));
            }
        }
        if( allowHonors ){
            foundingTileOptions.addAll(allHonors());
        }
    }
}
