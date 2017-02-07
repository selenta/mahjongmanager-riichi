package com.mahjongmanager.riichi.common;

import com.mahjongmanager.riichi.utils.HandGenerator;
import com.mahjongmanager.riichi.utils.Log;

import java.util.ArrayList;
import java.util.List;

import static com.mahjongmanager.riichi.utils.Utils.randomTile;

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

    private List<Tile> wall = new ArrayList<>();
    private List<Tile> deadWall = new ArrayList<>();

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

    /////////////////////////////////////////////////////
    /////////////          Init           ///////////////
    /////////////////////////////////////////////////////
    public Round (Tile.Wind rWind){
        roundWind = rWind;

        buildWalls();
        buildHands();

        draw();
    }
    private void buildWalls(){
        List<Tile> allTiles = HandGenerator.allTiles();
        allTiles.addAll(HandGenerator.allTiles());
        allTiles.addAll(HandGenerator.allTiles());
        allTiles.addAll(HandGenerator.allTiles());

        while(deadWall.size()<14){
            Tile t = randomTile(allTiles);
            deadWall.add(t);
            allTiles.remove(t);
        }
        while(allTiles.size()>0){
            Tile t = randomTile(allTiles);
            wall.add(t);
            allTiles.remove(t);
        }
    }
    private void buildHands(){
        eastHand  = new Hand( drawTiles(13) );
        eastHand.playerWind = Tile.Wind.EAST;
        eastHand.prevailingWind = roundWind;

        southHand = new Hand( drawTiles(13) );
        southHand.playerWind = Tile.Wind.SOUTH;
        southHand.prevailingWind = roundWind;

        westHand  = new Hand( drawTiles(13) );
        westHand.playerWind = Tile.Wind.WEST;
        westHand.prevailingWind = roundWind;

        northHand = new Hand( drawTiles(13) );
        northHand.playerWind = Tile.Wind.NORTH;
        northHand.prevailingWind = roundWind;
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
                eastHand.addTile(drawTile());
                break;
            case SOUTH:
                southHand.addTile(drawTile());
                break;
            case WEST:
                westHand.addTile(drawTile());
                break;
            case NORTH:
                northHand.addTile(drawTile());
                break;
        }
        currentState = State.ACTIVE;
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

        Log.d("endTurn", "Tiles remaining in wall at end of seat's turn: "+activePlayer+" - "+wall.size());
        if( wall.size() > 0 ){
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
    private List<Tile> drawTiles(int n){
        List<Tile> drawnTiles = new ArrayList<>();
        for(int i=0; i<n;i++){
            drawnTiles.add( drawTile() );
        }
        return drawnTiles;
    }
    private Tile drawTile(){
        if( wall.isEmpty() ){
            Log.e("drawTile", "Wall is empty! Cannot draw a tile");
            return null;
        }
        Tile t = wall.get(0);
        wall.remove(0);
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
        return wall.size();
    }
}
