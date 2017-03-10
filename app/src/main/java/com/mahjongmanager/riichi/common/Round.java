package com.mahjongmanager.riichi.common;

import com.mahjongmanager.riichi.utils.HandGenerator;
import com.mahjongmanager.riichi.utils.Log;
import com.mahjongmanager.riichi.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Round {
    private enum State{
        DRAW,
        ACTIVE,
        DISCARD,
        ENDTURN;

        private static State[] vals = values();
        public State next(){
            return vals[(this.ordinal()+1) & vals.length ];
        }
    }

    private Deck deck = new Deck();

    private Tile.Wind roundWind    = Tile.Wind.EAST;
    private Tile.Wind activePlayer = Tile.Wind.EAST;
    private int continuances = 0;               // TODO implement

    private State currentState = State.DRAW;
    private boolean roundIsOver = false;

    private Tile lastDraw;

    private Hand eastHand;
    private Hand southHand;
    private Hand westHand;
    private Hand northHand;
    private List<Tile> eastDiscards  = new ArrayList<>();
    private List<Tile> southDiscards = new ArrayList<>();
    private List<Tile> westDiscards  = new ArrayList<>();
    private List<Tile> northDiscards = new ArrayList<>();

    private List<Tile.Wind> winningPlayers = new ArrayList<>();

    private boolean justDeclaredKan = false;

    /////////////////////////////////////////////////////
    /////////////          Init           ///////////////
    /////////////////////////////////////////////////////
    public Round (Tile.Wind rWind){
        roundWind = rWind;

        buildHands();
    }
    private void buildHands(){
        eastHand  = new Hand( drawTiles(Tile.Wind.EAST, 13) );
        eastHand.playerWind = Tile.Wind.EAST;
        eastHand.prevailingWind = roundWind;

        southHand = new Hand( drawTiles(Tile.Wind.SOUTH, 13) );
        southHand.playerWind = Tile.Wind.SOUTH;
        southHand.prevailingWind = roundWind;

        westHand  = new Hand( drawTiles(Tile.Wind.WEST, 13) );
        westHand.playerWind = Tile.Wind.WEST;
        westHand.prevailingWind = roundWind;

        northHand = new Hand( drawTiles(Tile.Wind.NORTH, 13) );
        northHand.playerWind = Tile.Wind.NORTH;
        northHand.prevailingWind = roundWind;
    }

    public void stackDeck(Tile.Wind player, List<Tile> tiles){
        deck = new Deck(deck);
        deck.stackDeck(player, tiles);
        buildHands();
    }

    /////////////////////////////////////////////////////
    /////////////         Actions         ///////////////
    /////////////////////////////////////////////////////
    /**
     * (DRAW > ACTIVE) Draw first tile of the turn. Do not call this for any other reason.
     * TODO Call this automatically after previous turn ends
     */
    public void draw(){
        if(roundIsOver){
            Log.w("draw", "Round is over! Do nothing.");
            return;
        }

        switch (activePlayer){
            case EAST:
                eastHand.addTile(drawTile(activePlayer));
                break;
            case SOUTH:
                southHand.addTile(drawTile(activePlayer));
                break;
            case WEST:
                westHand.addTile(drawTile(activePlayer));
                break;
            case NORTH:
                northHand.addTile(drawTile(activePlayer));
                break;
        }
        currentState = State.ACTIVE;
    }


    /**
     * The currently active hand declares a closed kan of the specified tile
     * @param t Tile of the type to be Kan'd
     */
    public void declareClosedKan(Tile t){
        Hand hand = getActiveHand();
        if( currentState!=State.ACTIVE ){
            Log.w("declareKan", "Declaring kan when state is not ACTIVE: "+currentState);
        } else if( Utils.findTiles(hand.unsortedTiles, t).size()!=4 ){
            Log.w("declareKan", "Declaring kan when hand does not contain 4 unsorted of the tile: "+t+" - "+hand);
        }

        //Slot the tiles in the hand
        List<Tile> kanTiles = new ArrayList<>();
        for( int i=0; i<4; i++ ){
            Tile tile = hand.popUnsortedTile(t.value, t.suit, null);
            tile.revealedState = Tile.RevealedState.CLOSEDKAN;
            kanTiles.add( tile );
        }
        hand.setMeld(kanTiles);

        // Deal with dead wall draw     TODO implement?
        //      Technically, this is just flavor, it doesn't really change anything
        //      Effectively, just restart the turn

        // Dora indicator               TODO implement
        //      Depends on mode, Draw/Discard mode doesn't allow dora
        //      (Perhaps this should be always implemented and Draw/Discard just removes the Dora from HanList right before the end)

        justDeclaredKan = true;
    }

    /**
     * (ACTIVE > DISCARD)   Declare tile as discard.
     * The discard will ultimately be successful and the turn will end if no other player
     * calls the discard.
     * @param discard Tile to be discarded
     */
    public void discard(Tile discard){
        if(roundIsOver){
            Log.w("discard", "Round is over! Do nothing to tile - "+discard);
            return;
        } else if( currentState!=State.ACTIVE ){
            Log.w("discard", "Declaring a discard when state is not ACTIVE: "+currentState);
        }

        discard(activePlayer, discard);
        currentState = State.DISCARD;
        justDeclaredKan = false;
        checkForCalling();
    }

    /**
     * (ACTIVE > END_TURN) Declare victory for the active player via Tsumo.
     * Game is over, the player has either won the hand or committed a Chombo.
     */
    public void declareTsumo(){
        if( currentState!=State.ACTIVE ){
            Log.w("declareTsumo", "Declaring tsumo when state is not ACTIVE: "+currentState);
        }
        roundIsOver = true;
        lastDraw.winningTile = true;
        getActiveHand().selfDrawWinningTile = true;
        if( tilesInWall()==0 ){
            getActiveHand().haitei = true;
        }
        if( justDeclaredKan ){
            getActiveHand().rinshan = true;
        }
        winningPlayers.add(activePlayer);
        currentState = State.ENDTURN;
    }

    // Check whether to pause on DISCARD step
    private void checkForCalling(){
        // TODO Check if any other player can Ron
        boolean playerCanRon = false;

        // TODO Check if other 3 players can Pon
        boolean playerCanPon = false;

        // TODO Check if next player can Chii
        boolean playerCanChii = false;

        if( playerCanRon || playerCanPon || playerCanChii ){
            Log.i("checkForCalling", "Pausing to give players a chance to call");
        } else {
            currentState = State.ENDTURN;
        }
    }

    /**
     * (END_TURN > DRAW) Declares the end of the turn.
     * TODO call this automatically once discard is completed successfully
     */
    public void endTurn(){
        if( currentState!=State.ENDTURN ){
            Log.w("declareTsumo", "Ending turn when state is not END_TURN: "+currentState);
        }

        Log.d("endTurn", "Tiles remaining in wall at end of seat's turn: "+activePlayer+" - "+deck.tiles());
        if( deck.tiles() > 0 ){
            currentState = State.DRAW;
            activePlayer = activePlayer.next();
            Log.v("endTurn", "Next player's wind: "+activePlayer);
        } else {
            roundIsOver = true;
            Log.i("endTurn", "Round is now over!");
        }
    }

    ///////////////////////////////////////////////////
    /////////////       Internal        ///////////////
    ///////////////////////////////////////////////////
    // Draw tiles from the wall. These are the correct methods for additional draws during a turn
    private List<Tile> drawTiles(Tile.Wind player, int n){
        List<Tile> drawnTiles = new ArrayList<>();
        for(int i=0; i<n;i++){
            drawnTiles.add( drawTile(player) );
        }
        return drawnTiles;
    }
    private Tile drawTile(Tile.Wind player){
        Tile t = deck.draw(player);
        lastDraw = t;
        return t;
    }

    private void discard(Tile.Wind seatWind, Tile t){
        switch (seatWind){
            case EAST:
                eastHand.discardTile(t);
                eastDiscards.add(t);
                break;
            case SOUTH:
                southHand.discardTile(t);
                southDiscards.add(t);
                break;
            case WEST:
                westHand.discardTile(t);
                westDiscards.add(t);
                break;
            case NORTH:
                northHand.discardTile(t);
                northDiscards.add(t);
                break;
        }
    }

    private class Deck {
        private List<Tile> wall = new ArrayList<>();
        private List<Tile> deadWall = new ArrayList<>();

        // If we want to stack the deck for a player
        //    When building walls we will ensure that tiles in these lists are put in the correct
        //    position so that (assuming no calls are made) they will be drawn by the player
        private Map<Tile.Wind, List<Tile>> presetDraws = new HashMap<Tile.Wind, List<Tile>>();

        Deck(){
            presetDraws.put(Tile.Wind.EAST,  new ArrayList<Tile>());
            presetDraws.put(Tile.Wind.SOUTH, new ArrayList<Tile>());
            presetDraws.put(Tile.Wind.WEST,  new ArrayList<Tile>());
            presetDraws.put(Tile.Wind.NORTH, new ArrayList<Tile>());
        }
        Deck(Deck oldDeck){
            presetDraws.put(Tile.Wind.EAST,  oldDeck.presetDraws.get(Tile.Wind.EAST));
            presetDraws.put(Tile.Wind.SOUTH, oldDeck.presetDraws.get(Tile.Wind.SOUTH));
            presetDraws.put(Tile.Wind.WEST,  oldDeck.presetDraws.get(Tile.Wind.WEST));
            presetDraws.put(Tile.Wind.NORTH, oldDeck.presetDraws.get(Tile.Wind.NORTH));
        }

        void stackDeck(Tile.Wind playerWind, List<Tile> tiles){
            int max = (playerWind==Tile.Wind.EAST || playerWind==Tile.Wind.SOUTH) ? 31 : 30;
            if( tiles.size() > max ){
                Log.w("stackDeck", "Trying to stack the deck with an abnormally large number of tiles: "+playerWind+" - "+tiles.size()+" - "+tiles);
            }
            presetDraws.get(playerWind).addAll(tiles);
            Log.i("stackDeck", "StackedDeck: "+playerWind+" - "+presetDraws.get(playerWind));
        }

        public Tile draw(Tile.Wind playerWind){
            if( deadWall.size()==0){
                buildDeck();
            }

            if( !presetDraws.get(playerWind).isEmpty() ){
                return presetDraws.get(playerWind).remove(0);
            } else if( wall.size()==0 ){
                for(Tile.Wind wind : presetDraws.keySet()){
                    if( !presetDraws.get(wind).isEmpty() ){
                        Log.wtf("draw", "Woah shit boy! Wall is empty but preset draws are not(drawing a tile from here then): "+wind+" - "+presetDraws.get(wind));
                        return presetDraws.get(wind).remove(0);
                    }
                }
                Log.w("draw", "Wall is empty! Cannot draw tile from an empty wall");
                return null;
            }
            return wall.remove(0);
        }

        // Remove tiles in presetDraws from allTiles first
        private void buildDeck(){
            List<Tile> allTiles = HandGenerator.allTiles();
            allTiles.addAll(HandGenerator.allTiles());
            allTiles.addAll(HandGenerator.allTiles());
            allTiles.addAll(HandGenerator.allTiles());

            Log.i("buildDeck", "AllTiles before removing: "+allTiles.size());
            for(Tile.Wind wind : presetDraws.keySet()){
                Log.i("buildDeck", "PresetDraws: "+wind+" - "+presetDraws.get(wind));
                for(Tile tile : presetDraws.get(wind) ){
                    removeTileFromList(allTiles, tile);
                }
            }
            Log.i("buildDeck", "AllTiles after removing: "+allTiles.size());


            while(deadWall.size()<14){
                Tile t = Utils.randomTile(allTiles);
                deadWall.add(t);
                allTiles.remove(t);
            }
            while(allTiles.size()>0){
                Tile t = Utils.randomTile(allTiles);
                wall.add(t);
                allTiles.remove(t);
            }
        }

        private void removeTileFromList(List<Tile> list, Tile tile){
            Tile doomed = null;
            for(Tile t : list){
                if( t.isSame(tile) ){
                    doomed = t;
                    break;
                }
            }
            Log.v("removeTileFromList", "Before removing tile: "+list.size()+" - "+doomed);
            list.remove(doomed);
            Log.v("removeTileFromList", "After removing tile: "+list.size());
        }

        public int tiles(){
            int count = wall.size();
            for(Tile.Wind wind : presetDraws.keySet()){
                count += presetDraws.get(wind).size();
            }
            return count;
        }
    }

    ///////////////////////////////////////////////////
    /////////////        Getters        ///////////////
    ///////////////////////////////////////////////////
    public Tile.Wind getRoundWind(){
        return roundWind;
    }
    public int getContinuanceCounter(){
        return continuances;
    }
    public Tile.Wind getActivePlayerWind(){
        return activePlayer;
    }
    public Tile getLastDraw(){
        return lastDraw;
    }
    public State getState(){
        return currentState;
    }
    public boolean isOver(){
        return roundIsOver;
    }

    public List<Hand> getWinningHands(){
        List<Hand> hands = new ArrayList<>();
        for(Tile.Wind wind : winningPlayers){
            hands.add(getHand(wind));
        }
        return hands;
    }
    public Hand getActiveHand(){
        return getHand(activePlayer);
    }
    public Hand getHand(Tile.Wind seatWind){
        switch (seatWind){
            case EAST:
                return eastHand;
            case SOUTH:
                return southHand;
            case WEST:
                return westHand;
            case NORTH:
                return northHand;
        }
        return null;
    }
    public List<Tile> getDiscards(Tile.Wind seatWind){
        switch (seatWind){
            case EAST:
                return eastDiscards;
            case SOUTH:
                return southDiscards;
            case WEST:
                return westDiscards;
            case NORTH:
                return northDiscards;
        }
        return null;
    }

    public int tilesInWall(){
        return deck.tiles();
    }
}
