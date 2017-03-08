package com.mahjongmanager.riichi.common;

import com.mahjongmanager.riichi.Validator;
import com.mahjongmanager.riichi.utils.Log;

public class Tile {
    public enum Suit {
        MANZU, PINZU, SOUZU, HONOR
    }
    public enum Dragon {
        WHITE, GREEN, RED;

        private static Dragon[] vals = values();
        public Dragon next(){
            return vals[(this.ordinal()+1) % vals.length ];
        }
        public Dragon prev(){
            return vals[(this.ordinal()+vals.length-1) % vals.length ];
        }
    }
    public enum Wind {
        EAST, SOUTH, WEST, NORTH;

        private static Wind[] vals = values();
        public Wind next(){
            return vals[(this.ordinal()+1) % vals.length ];
        }
        public Wind prev(){
            return vals[(this.ordinal()+vals.length-1) % vals.length ];
        }
    }
    public enum RevealedState {
        NONE, PON, CHI, OPENKAN, CLOSEDKAN, ADDEDKAN
    }
    public enum CalledFrom {
        NONE, LEFT, CENTER, RIGHT
    }

    public Suit suit;
    public String value;
    public int sortId;

    public boolean red = false;
    public boolean faceDown = false;

    public int number;
    public Dragon dragon;
    public Wind wind;

    public RevealedState revealedState;
    public CalledFrom calledFrom;

    public boolean winningTile;

    public Tile( int valueInt, Suit suitVal ){
        suit = suitVal;
        number = valueInt;
        initTile();
    }
    public Tile( Dragon dragonVal ){
        suit = Suit.HONOR;
        dragon = dragonVal;
        initTile();
    }
    public Tile( Wind windVal ){
        suit = Suit.HONOR;
        wind = windVal;
        initTile();
    }
    public Tile( Tile oldTile ){
        suit          = oldTile.suit;
        number        = oldTile.number;
        dragon        = oldTile.dragon;
        wind          = oldTile.wind;

        red           = oldTile.red;
        winningTile   = oldTile.winningTile;
        revealedState = oldTile.revealedState;
        calledFrom    = oldTile.calledFrom;

        value = getValue();
        determineSortId();
    }
    private void initTile(){
        red = false;
        winningTile = false;
        revealedState = RevealedState.NONE;
        calledFrom = CalledFrom.NONE;
        value = getValue();
        determineSortId();

        if( number==0 && dragon==null && wind==null ){
            Log.e("constructorValidation", "Tile has no value. Value given was: " + getValue());
        } else if( suit==null ){
            Log.e("constructorValidation", "Tile has no suit.");
        }
    }

    public boolean isSame( Tile t ){
        return t != null
            && this.suit   == t.suit
            && this.number == t.number
            && this.wind   == t.wind
            && this.dragon == t.dragon;
    }
    public boolean isTerminal(){
        return (number==1 || number==9);
    }
    public boolean isSimple(){
        return number>1 && number<9;
    }
    public boolean isHonor(){
        return suit==Suit.HONOR;
    }

    public String getValue(){
        if( number==10 ){
            return "Placeholder";
        } else if( number>0 && number<10 ){
            return String.valueOf(number);
        } else if( dragon!=null ){
            switch(dragon){
                case WHITE:
                    return "White";
                case GREEN:
                    return "Green";
                case RED:
                    return "Red";
            }
        } else if( wind!=null ){
            switch(wind){
                case EAST:
                    return "East";
                case SOUTH:
                    return "South";
                case WEST:
                    return "West";
                case NORTH:
                    return "North";
            }
        }
        return "???";
    }
    public void determineSortId(){
        Integer id = 999;

        switch (suit){
            case MANZU:
                switch (number){
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                        id = number;
                        if(red){
                            id++;
                        }
                        break;
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                        id = 5+number;
                        break;
                }
                break;
            case PINZU:
                switch (number){
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                        id = 20+number;
                        if(red){
                            id++;
                        }
                        break;
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                        id = 25+number;
                        break;
                }
                break;
            case SOUZU:
                switch (number){
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                        id = 40+number;
                        if(red){
                            id++;
                        }
                        break;
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                        id = 45+number;
                        break;
                }
                break;
            case HONOR:
                if(wind!=null){
                    switch (wind){
                        case EAST:
                            id=61;
                            break;
                        case SOUTH:
                            id=62;
                            break;
                        case WEST:
                            id=63;
                            break;
                        case NORTH:
                            id=64;
                            break;
                    }
                }
                if(dragon!=null){
                    switch (dragon){
                        case WHITE:
                            id=71;
                            break;
                        case GREEN:
                            id=72;
                            break;
                        case RED:
                            id=73;
                            break;
                    }
                }
                break;
        }

        sortId = id;
    }
    public String toString(){
        switch (suit){
            case MANZU:
                return value + " Man";
            case SOUZU:
                return value + " Sou";
            case PINZU:
                return value + " Pin";
            default:
                return value;
        }
    }

    public Tile getNextTile(){
        if( suit==Suit.HONOR && wind!=null ) {
            return new Tile(wind.next());
        } else if( suit==Suit.HONOR ){
            return new Tile(dragon.next());
        } else if(number==9) {
            return new Tile(1, suit);
        }
        return new Tile(number+1, suit);
    }
    public Tile getPreviousTile(){
        if( suit==Suit.HONOR && wind!=null ) {
            return new Tile(wind.prev());
        } else if( suit==Suit.HONOR ){
            return new Tile(dragon.prev());
        } else if(number==1) {
            return new Tile(9, suit);
        }
        return new Tile(number-1, suit);
    }


    public String getImageCacheKey(String usage, boolean rotated){
        String s = getImageCacheKey(usage);
        if( rotated ){
            s += " Rotated";
        }
        return s;
    }
    public String getImageCacheKey(String usage){
        String s = usage;
        if( faceDown ){
            s += "Facedown";
        } else if( red ){
            s += toString() + " Red";
        } else if( !Validator.validate(this) ){
            s += "Blank";
        } else {
            s += toString();
        }
        return s;
    }
}
