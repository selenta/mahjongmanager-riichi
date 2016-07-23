package com.mahjongmanager.riichi.utils;

import android.content.Context;

import com.mahjongmanager.riichi.Hand;
import com.mahjongmanager.riichi.Meld;
import com.mahjongmanager.riichi.Tile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Used to generate completely random hands. There is some massaging to make them slightly
 * more interesting, but the only thing can currently be determined before generating the hand are:
 * <ul>
 *     <li>Number of Suits (default - 3)</li>
 *     <li>Allow Honors (default - true)</li>
 * </ul>
 * For now, stick to using h=completelyRandomHand() followed by addOtherYaku(h) (optional).
 * Generating yaku first and then a hands from that is probably more effort than it's worth, tbh
 */
public class HandGenerator {
    private Context context;

    /**
     * When generating melds for a hand, each starts with a founding (aka seed) tile from which
     * the rest of the meld is generated. The founding tile does not influence what kind of
     * meld (pon, open kan, chi, etc) it will become when the rest of the tiles are added.
     */
    List<Tile> foundingTileOptions = new ArrayList<>();
    private int numberOfSuits = 3;
    private boolean allowHonors = true;

    HashMap<String, Double> yakuOdds = new HashMap<>();

    Meld pair  = new Meld();
    Meld meld1 = new Meld();
    Meld meld2 = new Meld();
    Meld meld3 = new Meld();
    Meld meld4 = new Meld();

    List<String> yakuList = new ArrayList<>();


    public HandGenerator(){
        yakuOdds.put("Chiitoitsu", 0.1);
        yakuOdds.put("Honroutou", 0.2);
        yakuOdds.put("Tanyao", 0.4);
        yakuOdds.put("Honitsu", 0.25);
        yakuOdds.put("Chinitsu", 0.15);
    }
    public HandGenerator(Context current){
        this.context = current;
        populateYakuOdds();
    }

    public void setNumberOfSuits(int i){
        if( i<1 || i>3 ){
            Log.e("setNumberOfSuits", "Number of suits is invalid: "+i);
            return;
        }

        numberOfSuits = i;
    }
    public void setAllowHonors(boolean bool){
        allowHonors = bool;
    }

    public Hand completelyRandomHand(){
        cleanup();
        setFoundingTileOptions();

        Log.d("completelyRandomHand", "Number of suits: " + numberOfSuits);
        Log.d("completelyRandomHand", "Allow honors: " + allowHonors);

        //Decide if special hand structure (chiitoitsu) first
        if( Math.random()<yakuOdds.get("Chiitoitsu") ){
            return generateChiitoitsu();
        }

        createMelds();
        randomWinningTile();
        return new Hand(usedTiles());
    }
    private void createMelds(){
        createFounderTiles();

        pair.addTile(new Tile(pair.firstTile()));

        expandMeld(meld1);
        expandMeld(meld2);
        expandMeld(meld3);
        expandMeld(meld4);
    }
    //Never use the same founder tile twice... is this unnecessary?
    private void createFounderTiles(){
        List<Tile> usedFoundingTiles = new ArrayList<>();

        pair.addTile(randomFoundingTile());
        usedFoundingTiles.add(pair.firstTile());

        meld1.addTile(randomFoundingTile(usedFoundingTiles));
        usedFoundingTiles.add(meld1.firstTile());
        meld2.addTile(randomFoundingTile(usedFoundingTiles));
        usedFoundingTiles.add(meld2.firstTile());
        meld3.addTile(randomFoundingTile(usedFoundingTiles));
        usedFoundingTiles.add(meld3.firstTile());
        meld4.addTile(randomFoundingTile(usedFoundingTiles));
        usedFoundingTiles.add(meld4.firstTile());
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
    public void addOtherYaku(Hand h){
        // Riichi, Ippatsu, Tsumo, Rinshan Haitei, Houtei
        if( Math.random()<0.8 && !h.isOpen() ){
            addRiichi(h);
        }
        if( Math.random()<0.4 && h.riichi ){
            addIppatsu(h);
        }
        if( Math.random()<0.2 && !h.isOpen() ){
            addTsumo(h);
        }
        if( Math.random()<0.3
                && h.hasKan()
                && h.countTile(h.getWinningTile())==1
                && h.getWinningTile().calledFrom==Tile.CalledFrom.NONE){
            addRinshan(h);
        }
        if( Math.random()<0.1 && h.getWinningTile().calledFrom==Tile.CalledFrom.NONE ){
            addHaitei(h);
        }
        if( Math.random()<0.1 && h.getWinningTile().calledFrom!=Tile.CalledFrom.NONE ){
            addHoutei(h);
        }

        // Dora indicators
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
        return randomFoundingTile(new ArrayList<Tile>());
    }
    private Tile randomFoundingTile(List<Tile> exclusions){
        List<Tile> nonFoundingTileOptions = allTiles();
        removeTilesFromList(nonFoundingTileOptions, foundingTileOptions);

        nonFoundingTileOptions.addAll(exclusions);
        Log.v("randomFoundingTile", "Non-Options: " + nonFoundingTileOptions.toString());
        return randomTile(nonFoundingTileOptions);
    }
    private Tile randomTile(List<Tile> exclusions){
        List<Tile> tiles = allTiles();
        List<Tile> markedForRemoval = new ArrayList<>();
        for( Tile exc : exclusions ){
            for( Tile tile : tiles ){
                if( exc.toString().equals(tile.toString()) ){
                    markedForRemoval.add(tile);
                }
            }
        }
        tiles.removeAll(markedForRemoval);
        Log.v("randomTile", "Options: " + tiles.toString());
        Tile randTile = tiles.get(new Random().nextInt(tiles.size()));
        Log.v("randomTile", "Tile: " + randTile.toString());
        return randTile;
    }
    private Tile.Suit randomSuit(){
        List<Tile.Suit> suits = new ArrayList<>();
        suits.add(Tile.Suit.MANZU);
        suits.add(Tile.Suit.PINZU);
        suits.add(Tile.Suit.SOUZU);

        int pick = new Random().nextInt(suits.size());
        return suits.get(pick);
    }

    //modify hand
    private void randomWinningTile(){
        List<Tile> candidates = new ArrayList<>();
        candidates.addAll(pair.getTiles());
        if( meld1.size()==3 ){
            Tile calledTile = meld1.getCalledTile();
            if( calledTile==null ){
                candidates.addAll(meld1.getTiles());
            }
        }
        if( meld2.size()==3 ){
            Tile calledTile = meld2.getCalledTile();
            if( calledTile==null ){
                candidates.addAll(meld2.getTiles());
            }
        }
        if( meld3.size()==3 ){
            Tile calledTile = meld3.getCalledTile();
            if( calledTile==null ){
                candidates.addAll(meld3.getTiles());
            }
        }
        if( meld4.size()==3 ){
            Tile calledTile = meld4.getCalledTile();
            if( calledTile==null ){
                candidates.addAll(meld4.getTiles());
            }
        }

        Tile wTile = candidates.get(new Random().nextInt(candidates.size()));

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
        Tile dora = randomTile(h.getAllUsedTiles());
        Tile uraDora = randomTile(h.getAllUsedTiles());
        if( h.doraIndicator1==null ){
            h.doraIndicator1    = dora;
            h.uraDoraIndicator1 = uraDora;
        } else if( h.doraIndicator2==null ){
            h.doraIndicator2    = dora;
            h.uraDoraIndicator2 = uraDora;
        } else if( h.doraIndicator3==null ){
            h.doraIndicator3    = dora;
            h.uraDoraIndicator3 = uraDora;
        } else if( h.doraIndicator4==null ){
            h.doraIndicator4    = dora;
            h.uraDoraIndicator4 = uraDora;
        }
    }


    //Don't want to accidentally modify the hand.tiles list, but I need to reference it constantly...
    private List<Tile> combineTileList(List<Tile> listOne, Tile tile) {
        List<Tile> newList = new ArrayList<Tile>();
        newList.addAll(listOne);
        newList.add(tile);
        return newList;
    }
    private List<Tile> combineTileList(List<Tile> listOne, List<Tile> listTwo) {
        List<Tile> newList = new ArrayList<Tile>();
        newList.addAll(listOne);
        newList.addAll(listTwo);
        return newList;
    }
    private void removeTilesFromList( List<Tile> primaryList, List<Tile> removeList ){
        List<Tile> markedForRemoval = new ArrayList<>();
        for( Tile exc : removeList ){
            for( Tile tile : primaryList ){
                if( exc.toString().equals(tile.toString()) ){
                    markedForRemoval.add(tile);
                }
            }
        }
        primaryList.removeAll(markedForRemoval);
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
    private static List<Tile> allManzu(){
        List<Tile> tiles = new ArrayList<>();
        for(int i=1; i<10; i++){
            tiles.add(new Tile(i, Tile.Suit.MANZU));
        }
        return tiles;
    }
    private static List<Tile> allPinzu(){
        List<Tile> tiles = new ArrayList<>();
        for(int i=1; i<10; i++){
            tiles.add(new Tile(i, Tile.Suit.PINZU));
        }
        return tiles;
    }
    private static List<Tile> allSouzu(){
        List<Tile> tiles = new ArrayList<>();
        for(int i=1; i<10; i++){
            tiles.add(new Tile(i, Tile.Suit.SOUZU));
        }
        return tiles;
    }
    private static List<Tile> allHonors(){
        List<Tile> tiles = new ArrayList<>();
        tiles.add(new Tile(Tile.Wind.EAST));
        tiles.add(new Tile(Tile.Wind.SOUTH));
        tiles.add(new Tile(Tile.Wind.WEST));
        tiles.add(new Tile(Tile.Wind.NORTH));
        tiles.add(new Tile(Tile.Dragon.WHITE));
        tiles.add(new Tile(Tile.Dragon.GREEN));
        tiles.add(new Tile(Tile.Dragon.RED));
        return tiles;
    }
    private static List<Tile> allTerminals(){
        List<Tile> tiles = new ArrayList<>();
        tiles.add(new Tile(1, Tile.Suit.MANZU));
        tiles.add(new Tile(9, Tile.Suit.MANZU));
        tiles.add(new Tile(1, Tile.Suit.PINZU));
        tiles.add(new Tile(9, Tile.Suit.PINZU));
        tiles.add(new Tile(1, Tile.Suit.SOUZU));
        tiles.add(new Tile(9, Tile.Suit.SOUZU));
        return tiles;
    }
    private static List<Tile> allSimples(){
        List<Tile> tiles = new ArrayList<>();
        for(int i=2; i<9; i++){
            tiles.add(new Tile(i, Tile.Suit.MANZU));
            tiles.add(new Tile(i, Tile.Suit.PINZU));
            tiles.add(new Tile(i, Tile.Suit.SOUZU));
        }
        return tiles;
    }

    private void transformTileInto( Tile originalTile, Tile newTile ){
        originalTile.suit   = newTile.suit;
        originalTile.red    = newTile.red;
        originalTile.number = newTile.number;
        originalTile.dragon = newTile.dragon;
        originalTile.wind   = newTile.wind;

        originalTile.value = originalTile.getValue();
        originalTile.determineSortId();
    }


    ////////////////////////////////////////////////////////////////////////
    ///////////////          Yaku Methods           ////////////////////////
    ////////////////////////////////////////////////////////////////////////
    public Hand generateChiitoitsu(){
        Hand h = new Hand(new ArrayList<Tile>());

        addChiitoitsu(h);

        if( Math.random()<yakuOdds.get("Honroutou") && allowHonors ){
            //honroutou
            addHonroutouToChiitoitsu(h);
        } else if( Math.random()<yakuOdds.get("Tanyao") ){
            //tanyao
            addTanyaoToChiitoitsu(h);
        }
        if( Math.random()<yakuOdds.get("Chinitsu") ){
            //chinitsu
            addChinitsuToChiitoitsu(h);
        } else if( Math.random()<yakuOdds.get("Honitsu") && allowHonors ){
            //honitsu
            addHonitsuToChiitoitsu(h);
        }
        //daichisei
        // end early (retry if get a ryanpeikou hand?)

        Tile wTile = h.tiles.get(new Random().nextInt(h.tiles.size()));
        setWinningTileState(wTile);

        return h;
    }

    public void addChiitoitsu(Hand h){
        for(int i=0; i<7; i++ ){
            Tile t1 = randomFoundingTile(h.tiles);
            h.addTile(t1);
            h.addTile(new Tile(t1));
        }
        h.sort();
    }
    public void addTanyaoToChiitoitsu(Hand h){
        removeTilesFromList(foundingTileOptions, allHonors());
        removeTilesFromList(foundingTileOptions, allTerminals());
        Log.d("addTanyaoToChiitoitsu", "foundingTileOptions: " + foundingTileOptions);

        for(Tile t : h.tiles){
            if( !t.isSimple() ){
                String str = t.toString();

                transformTileInto(t, randomFoundingTile(h.tiles));

                Tile otherTile = h.findTile(str);
                transformTileInto(otherTile, t);
            }
        }
        h.sort();
    }
    public void addHonroutouToChiitoitsu(Hand h){
        removeTilesFromList(foundingTileOptions, allSimples());
        Log.d("addHonroutouToChiitoitsu", "foundingTileOptions: " + foundingTileOptions);

        for(Tile t : h.tiles){
            if( t.isSimple() ){
                String str = t.toString();

                transformTileInto(t, randomFoundingTile(h.tiles));

                Tile otherTile = h.findTile(str);
                transformTileInto(otherTile, t);
            }
        }
        h.sort();
    }
    public void addHonitsuToChiitoitsu(Hand h){
        Log.d("addHonitsuToChiitoitsu", "foundingTileOptions(1/2): " + foundingTileOptions );
        //get a random non-honor suit from founding tiles
        Tile.Suit honitsuSuit = null;
        while( honitsuSuit==null ){
            Tile randTile = foundingTileOptions.get(new Random().nextInt(foundingTileOptions.size()));
            if( randTile.suit!= Tile.Suit.HONOR ){
                honitsuSuit = randTile.suit;
            }
        }

        //all tiles that are not of the chosen suit are no longer options for founding tiles
        List<Tile> nonHonitsuTiles = allTiles();
        removeTilesFromList(nonHonitsuTiles, allOfSuit(honitsuSuit));
        removeTilesFromList(nonHonitsuTiles, allHonors());
        removeTilesFromList(foundingTileOptions, nonHonitsuTiles);
        //in case hand was set to honroutou first
        if(foundingTileOptions.size()<7){
            foundingTileOptions = allOfSuit(honitsuSuit);
        }
        Log.d("addHonitsuToChiitoitsu", "foundingTileOptions(2/2): " + foundingTileOptions );

        //replace tiles that are not of the chosen suit
        for(Tile t : h.tiles){
            if( t.suit!=Tile.Suit.HONOR && t.suit!=honitsuSuit ){
                String str = t.toString();

                //Goal=exclusions has all honor/suited EXCEPT for tiles already in hand
                transformTileInto(t, randomFoundingTile(h.tiles));

                Tile otherTile = h.findTile(str);
                transformTileInto(otherTile, t);
            }
        }
        h.sort();
    }
    public void addChinitsuToChiitoitsu(Hand h){
        Log.d("addChinitsuToChiitoitsu", "foundingTileOptions(1/2): " + foundingTileOptions);
        //get a random non-honor suit from founding tiles
        Tile.Suit chinitsuSuit = null;
        while( chinitsuSuit==null ){
            Tile randTile = foundingTileOptions.get(new Random().nextInt(foundingTileOptions.size()));
            if( randTile.suit!= Tile.Suit.HONOR ){
                chinitsuSuit = randTile.suit;
            }
        }

        //all tiles that are not of the chosen suit are no longer options for founding tiles
        List<Tile> nonHonitsuTiles = allTiles();
        removeTilesFromList(nonHonitsuTiles, allOfSuit(chinitsuSuit));
        removeTilesFromList(foundingTileOptions, nonHonitsuTiles);
        //in case hand was set to honroutou first
        if(foundingTileOptions.size()<7){
            foundingTileOptions = allOfSuit(chinitsuSuit);
        }
        Log.d("addChinitsuToChiitoitsu", "foundingTileOptions(2/2): " + foundingTileOptions);

        //replace tiles that are not of the chosen suit
        for(Tile t : h.tiles){
            if( t.suit!=chinitsuSuit ){
                String str = t.toString();

                transformTileInto(t, randomFoundingTile(h.tiles));

                Tile otherTile = h.findTile(str);
                transformTileInto(otherTile, t);
            }
        }
        h.sort();
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

    private void setFoundingTileOptions(){
        foundingTileOptions.clear();

        List<String> usedSuits = new ArrayList<>();
        while( usedSuits.size() < numberOfSuits ){
            Tile.Suit suit = randomSuit();
            if( !usedSuits.contains(suit.toString()) ){
                foundingTileOptions.addAll(allOfSuit(suit));
                usedSuits.add(suit.toString());
            }
        }
        if( allowHonors ){
            foundingTileOptions.addAll(allHonors());
        }
    }

    private void populateYakuOdds(){
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
        foundingTileOptions.clear();
        pair  = new Meld();
        meld1 = new Meld();
        meld2 = new Meld();
        meld3 = new Meld();
        meld4 = new Meld();
        yakuList = new ArrayList<>();
    }
}
