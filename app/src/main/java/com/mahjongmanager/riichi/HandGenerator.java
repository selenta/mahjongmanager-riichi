package com.mahjongmanager.riichi;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/*
 * This class is a mess... can't seem to get it all in my head at once
 *      The problem is creating interesting hands with realish yaku
 *      For now, stick to using h=completelyRandomHand() followed by addOtherYaku(h)
 */
public class HandGenerator {
    private Context context;

    HashMap<String, Double> yakuOdds = new HashMap<>();

    List<Tile> pair = new ArrayList<>();
    List<Tile> meld1 = new ArrayList<>();
    List<Tile> meld2 = new ArrayList<>();
    List<Tile> meld3 = new ArrayList<>();
    List<Tile> meld4 = new ArrayList<>();

    List<String> yakuList = new ArrayList<>();


    public HandGenerator(){
        yakuOdds.put("Chiitoitsu", 10D);
        yakuOdds.put("Honroutou", 20D);
        yakuOdds.put("Tanyao", 40D);
        yakuOdds.put("Chinitsu", 15D);
        yakuOdds.put("Honitsu", 30D);
    }
    public HandGenerator(Context current){
        this.context = current;
        populateYakuOdds();
    }

    public Hand generateHand(){
        cleanup();
        Hand h = addStandardYaku();
        addOtherYaku(h);
        return h;
    }

    private Hand addStandardYaku(){
        Hand h = new Hand(new ArrayList<Tile>());

        //Decide if special hand structure (chiitoitsu) first
        if( Math.random()<yakuOdds.get("Chiitoitsu") ){
            addChiitoitsu(h);

            if( Math.random()<yakuOdds.get("Honroutou") ){
                //honroutou
                addHonroutouToChiitoitsu(h);
            } else if( Math.random()<yakuOdds.get("Tanyao") ){
                //tanyao
                addTanyaoToChiitoitsu(h);
            }
            if( Math.random()<yakuOdds.get("Chinitsu") ){
                //chinitsu
                addChinitsuToChiitoitsu(h);
            } else if( Math.random()<yakuOdds.get("Honitsu") ){
                //honitsu
                addHonitsuToChiitoitsu(h);
            }
            //daichisei
            // end early (retry if get a ryanpeikou hand?)
            return h;
        }

        //Sequence Yaku
        if( meldsRemaining()==4 ){
            if( Math.random()<yakuOdds.get("Ryanpeikou") ){
                addRyanpeikou(h);
            }
            if( Math.random()<yakuOdds.get("Ryanpeikou") ){
                addRyanpeikou(h);
            }
        }

        //Triplet Yaku

        //Honor/Terminal Yaku

        //Suit Yaku


        return h;
    }

    public void addOtherYaku(Hand h){
        // Riichi, Ippatsu, Tsumo, Rinshan Haitei, Houtei
        if( Math.random()<0.5 && !h.isOpen() ){
            addRiichi(h);
        }
        if( Math.random()<0.1 && h.riichi ){
            addIppatsu(h);
        }
        if( Math.random()<0.2 && !h.isOpen() ){
            addTsumo(h);
        }
        boolean hasKan = (h.set1.size()==4);
        hasKan = (h.set2.size() == 4) || hasKan;
        hasKan = (h.set3.size() == 4) || hasKan;
        hasKan = (h.set4.size() == 4) || hasKan;
        if( Math.random()<0.003 && hasKan ){
            addRinshan(h);
        }
        if( Math.random()<0.0035 && h.getWinningTile().calledFrom==Tile.CalledFrom.NONE ){
            addHaitei(h);
        }
        if( Math.random()<0.007 && h.getWinningTile().calledFrom!=Tile.CalledFrom.NONE ){
            addHoutei(h);
        }

        // Dora indicators
        Integer doraIndicators = 1;
        doraIndicators = (h.set1.size()==4) ? doraIndicators+1 : doraIndicators;
        doraIndicators = (h.set2.size()==4) ? doraIndicators+1 : doraIndicators;
        doraIndicators = (h.set3.size()==4) ? doraIndicators+1 : doraIndicators;
        doraIndicators = (Math.random()<0.2) ? doraIndicators+1 : doraIndicators;
        for(int i=0; i<doraIndicators ; i++){
            addDoraIndicator(h);
        }
    }

    public Hand completelyRandomHand(){
        cleanup();
        List<Tile> foundingTiles = new ArrayList<>();
        foundingTiles.add(randomTile());

        pair.add(foundingTiles.get(0));
        foundingTiles.add(randomTile(foundingTiles));
        meld1.add(foundingTiles.get(foundingTiles.size() - 1));
        foundingTiles.add(randomTile(foundingTiles));
        meld2.add(foundingTiles.get(foundingTiles.size() - 1));
        foundingTiles.add(randomTile(foundingTiles));
        meld3.add(foundingTiles.get(foundingTiles.size() - 1));
        foundingTiles.add(randomTile(foundingTiles));
        meld4.add(foundingTiles.get(foundingTiles.size() - 1));

        pair.add(new Tile(pair.get(0)));

        expandMeld(meld1);
        expandMeld(meld2);
        expandMeld(meld3);
        expandMeld(meld4);

        List<Tile> allTiles = new ArrayList<>();
        allTiles.addAll(pair);
        allTiles.addAll(meld1);
        allTiles.addAll(meld2);
        allTiles.addAll(meld3);
        allTiles.addAll(meld4);

        randomWinningTile();

        return new Hand(allTiles);
    }
    private void expandMeld(List<Tile> meld){
        Double roll = Math.random();
        if(roll>0.4 && meld.get(0).suit!=Tile.Suit.HONOR ){
            expandChii(meld);
        } else if(roll<0.1) {
            expandKan(meld);
        } else {
            expandPon(meld);
        }
    }
    private void expandPon(List<Tile> meld){
        Tile founder = meld.get(0);
        meld.add(new Tile(founder));
        meld.add(new Tile(founder));

        // TODO add more variety for called sets
    }
    private void expandKan(List<Tile> meld){
        Tile founder = meld.get(0);
        meld.add(new Tile(founder));
        meld.add(new Tile(founder));
        meld.add(new Tile(founder));

        // TODO add more variety for called sets
        for( Tile t : meld ){
            t.revealedState=Tile.RevealedState.CLOSEDKAN;
        }
    }
    private void expandChii(List<Tile> meld){
        Tile founder = meld.get(0);

        if(founder.number==1){
            meld.add(new Tile(2, founder.suit.toString()));
            meld.add(new Tile(3, founder.suit.toString()));
        } else if(founder.number==9){
            meld.add(new Tile(8, founder.suit.toString()));
            meld.add(new Tile(7, founder.suit.toString()));
        } else {
            meld.add(new Tile(founder.number-1, founder.suit.toString()));
            meld.add(new Tile(founder.number+1, founder.suit.toString()));
        }

        // TODO add more variety for called sets
    }


    /////////////////////////////////////////////////////////////////////////
    ///////////////       Convenience Methods        ////////////////////////
    /////////////////////////////////////////////////////////////////////////
    private Integer meldsRemaining(){
        Integer openMelds = 0;
        if(meld1.size()==0){
            openMelds++;
        }
        if(meld2.size()==0){
            openMelds++;
        }
        if(meld3.size()==0){
            openMelds++;
        }
        if(meld4.size()==0){
            openMelds++;
        }
        return openMelds;
    }
    private Tile randomNewTile(Hand h){
        return randomTile(h.tiles);
    }
    private Tile randomTile(){
        return randomTile(new ArrayList<Tile>());
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
        int randIdx = new Random().nextInt(tiles.size());
        return tiles.get(randIdx);
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
        candidates.addAll(pair);
        if( meld1.size()==3 ){
            candidates.addAll(meld1);
        }
        if( meld2.size()==3 ){
            candidates.addAll(meld2);
        }
        if( meld3.size()==3 ){
            candidates.addAll(meld3);
        }
        if( meld4.size()==3 ){
            candidates.addAll(meld4);
        }

        Tile wTile = candidates.get(new Random().nextInt(candidates.size()));

        wTile.winningTile = true;

        List<Tile.CalledFrom> calledFroms = new ArrayList<>();
        calledFroms.add(Tile.CalledFrom.NONE);
        calledFroms.add(Tile.CalledFrom.NONE);
        calledFroms.add(Tile.CalledFrom.NONE);
        calledFroms.add(Tile.CalledFrom.LEFT);
        calledFroms.add(Tile.CalledFrom.CENTER);
        calledFroms.add(Tile.CalledFrom.RIGHT);
        wTile.calledFrom = calledFroms.get(new Random().nextInt(calledFroms.size()));
    }
    private void addDoraIndicator(Hand h){
        Tile dora = randomTile(h.getAllUsedTiles());
        if( h.doraIndicator1==null ){
            h.doraIndicator1 = dora;
        } else if( h.doraIndicator2==null ){
            h.doraIndicator2 = dora;
        } else if( h.doraIndicator3==null ){
            h.doraIndicator3 = dora;
        } else if( h.doraIndicator4==null ){
            h.doraIndicator4 = dora;
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

    private List<Tile> allTiles(){
        List<Tile> tiles = new ArrayList<>();
        for(int i=1; i<10; i++){
            tiles.add(new Tile(i, "MANZU"));
            tiles.add(new Tile(i, "PINZU"));
            tiles.add(new Tile(i, "SOUZU"));
        }
        tiles.add(new Tile("East", "HONOR"));
        tiles.add(new Tile("South", "HONOR"));
        tiles.add(new Tile("West", "HONOR"));
        tiles.add(new Tile("North", "HONOR"));
        tiles.add(new Tile("White", "HONOR"));
        tiles.add(new Tile("Green", "HONOR"));
        tiles.add(new Tile("Red", "HONOR"));
        return tiles;
    }
    private List<Tile> allOfSuit(Tile.Suit s){
        if( s==Tile.Suit.MANZU ){
            return allManzu();
        } else if( s==Tile.Suit.PINZU ){
            return allPinzu();
        } else if( s==Tile.Suit.SOUZU ){
            return allSouzu();
        }
        return allHonors();
    }
    private List<Tile> allManzu(){
        List<Tile> tiles = new ArrayList<>();
        for(int i=1; i<10; i++){
            tiles.add(new Tile(i, "MANZU"));
        }
        return tiles;
    }
    private List<Tile> allPinzu(){
        List<Tile> tiles = new ArrayList<>();
        for(int i=1; i<10; i++){
            tiles.add(new Tile(i, "PINZU"));
        }
        return tiles;
    }
    private List<Tile> allSouzu(){
        List<Tile> tiles = new ArrayList<>();
        for(int i=1; i<10; i++){
            tiles.add(new Tile(i, "SOUZU"));
        }
        return tiles;
    }
    private List<Tile> allHonors(){
        List<Tile> tiles = new ArrayList<>();
        tiles.add(new Tile("East", "HONOR"));
        tiles.add(new Tile("South", "HONOR"));
        tiles.add(new Tile("West", "HONOR"));
        tiles.add(new Tile("North", "HONOR"));
        tiles.add(new Tile("White", "HONOR"));
        tiles.add(new Tile("Green", "HONOR"));
        tiles.add(new Tile("Red", "HONOR"));
        return tiles;
    }
    private List<Tile> allTerminals(){
        List<Tile> tiles = new ArrayList<>();
        tiles.add(new Tile(1, "MANZU"));
        tiles.add(new Tile(9, "MANZU"));
        tiles.add(new Tile(1, "PINZU"));
        tiles.add(new Tile(9, "PINZU"));
        tiles.add(new Tile(1, "SOUZU"));
        tiles.add(new Tile(9, "SOUZU"));
        return tiles;
    }
    private List<Tile> allSimples(){
        List<Tile> tiles = new ArrayList<>();
        for(int i=2; i<9; i++){
            tiles.add(new Tile(i, "MANZU"));
            tiles.add(new Tile(i, "PINZU"));
            tiles.add(new Tile(i, "SOUZU"));
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
    public void addChiitoitsu(Hand h){
        for(int i=0; i<7; i++ ){
            Tile t1 = randomNewTile(h);
            h.addTile(t1);
            h.addTile(new Tile(t1));
        }
        h.sort();
    }

    private void addRiichi(Hand h){}
    private void addIppatsu(Hand h){}
    private void addTsumo(Hand h){}
    private void addHaitei(Hand h){}
    private void addHoutei(Hand h){}
    private void addRinshan(Hand h){}
    private void addChanKan(Hand h){}
    private void addDoubleRiichi(Hand h){}

    private void addPinfu(Hand h){}
    private void addIipeikou(Hand h){}
    private void addSanshokuDoujun(Hand h){}
    private void addIttsuu(Hand h){}
    private void addRyanpeikou(Hand h){}

    private void addToitoi(Hand h){}
    private void addSanAnkou(Hand h){}
    private void addSanshokuDoukou(Hand h){}
    private void addSanKantsu(Hand h){}

    public void addTanyaoToChiitoitsu(Hand h){
        for(Tile t : h.tiles){
            if( !t.isSimple() ){
                String str = t.toString();

                List<Tile> exclusions = combineTileList(h.tiles, allHonors());
                exclusions.addAll(allTerminals());
                transformTileInto(t, randomTile(exclusions));

                Tile otherTile = h.findTile(str);
                transformTileInto(otherTile, t);
            }
        }
        h.sort();
    }
    private void addYakuhai(Hand h){}
    private void addJunchan(Hand h){}
    private void addHonroutou(Hand h){}
    public void addHonroutouToChiitoitsu(Hand h){
        for(Tile t : h.tiles){
            if( t.isSimple() ){
                String str = t.toString();

                List<Tile> exclusions = combineTileList(h.tiles, allSimples());
                transformTileInto(t, randomTile(exclusions));

                Tile otherTile = h.findTile(str);
                transformTileInto(otherTile, t);
            }
        }
        h.sort();
    }
    private void addChanta(Hand h){}
    private void addShousangen(Hand h){}

    private void addHonitsu(Hand h){}
    public void addHonitsuToChiitoitsu(Hand h){
        Tile.Suit honitsuSuit = randomSuit();

        for(Tile t : h.tiles){
            if( t.suit!=Tile.Suit.HONOR && t.suit!=honitsuSuit ){
                String str = t.toString();

                List<Tile> exclusions = allTiles();
                List<Tile> options = combineTileList(allHonors(), allOfSuit(honitsuSuit) );
                removeTilesFromList(options, h.tiles);
                removeTilesFromList(exclusions, options);

                //Goal=exclusions has all honor/suited EXCEPT for tiles already in hand
                transformTileInto(t, randomTile(exclusions));

                Tile otherTile = h.findTile(str);
                transformTileInto(otherTile, t);
            }
        }
        h.sort();
    }
    private void addChinitsu(Hand h){}
    public void addChinitsuToChiitoitsu(Hand h){
        Tile.Suit chinitsuSuit = randomSuit();

        for(Tile t : h.tiles){
            if( t.suit!=chinitsuSuit ){
                String str = t.toString();

                List<Tile> exclusions = allTiles();
                List<Tile> options = allOfSuit(chinitsuSuit);
                removeTilesFromList(options, h.tiles);
                removeTilesFromList(exclusions, options);

                transformTileInto(t, randomTile(exclusions));

                Tile otherTile = h.findTile(str);
                transformTileInto(otherTile, t);
            }
        }
        h.sort();
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
        pair  = new ArrayList<>();
        meld1 = new ArrayList<>();
        meld2 = new ArrayList<>();
        meld3 = new ArrayList<>();
        meld4 = new ArrayList<>();
        yakuList = new ArrayList<>();
    }
}
