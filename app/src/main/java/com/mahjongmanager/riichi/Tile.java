package com.mahjongmanager.riichi;

import com.mahjongmanager.riichi.utils.Log;

public class Tile {
    public enum Suit {
        MANZU, PINZU, SOUZU, HONOR
    }
    public enum Dragon {
        WHITE, GREEN, RED
    }
    public enum Wind {
        EAST, SOUTH, WEST, NORTH
    }
    public enum RevealedState {
        NONE, PON, CHI, OPENKAN, CLOSEDKAN, ADDEDKAN
    }
    public enum CalledFrom {
        NONE, LEFT, CENTER, RIGHT
    }

    public Suit suit;
    public String value;
    public Integer sortId;

    public Boolean red = false;
    public Boolean faceDown = false;

    public Integer number;
    public Dragon dragon;
    public Wind wind;

    public RevealedState revealedState;
    public CalledFrom calledFrom;

    public Boolean winningTile;

    public Tile( Integer valueInt, Suit suitVal ){
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

        if( number==null && dragon==null && wind==null ){
            Log.e("constructorValidation", "Tile has no value. Value given was: " + getValue());
        } else if( suit==null ){
            Log.e("constructorValidation", "Tile has no suit.");
        }
    }

    public boolean isSame( Tile t ){
        return isSame(t.value, t.suit.toString());
    }
    public boolean isSame( String val, String s ){
        String realSuit = (s.equalsIgnoreCase("DRAGON")||s.equalsIgnoreCase("WIND")) ? "HONOR" : s;
        return value.equalsIgnoreCase(val) && suit.toString().equalsIgnoreCase(realSuit);
    }
    public boolean isTerminal(){
        return number!=null && (number==1 || number==9);
    }
    public boolean isSimple(){
        return number!=null && number>1 && number<9;
    }
    public boolean isHonor(){
        return suit==Suit.HONOR;
    }

    public String getValue(){
        if( number!=null ){
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
        } else {
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
            switch (wind){
                case EAST:
                    return new Tile(Wind.SOUTH);
                case SOUTH:
                    return new Tile(Wind.WEST);
                case WEST:
                    return new Tile(Wind.NORTH);
                case NORTH:
                    return new Tile(Wind.EAST);
            }
        } else if( suit==Suit.HONOR ){
            switch (dragon){
                case WHITE:
                    return new Tile(Dragon.GREEN);
                case GREEN:
                    return new Tile(Dragon.RED);
                case RED:
                    return new Tile(Dragon.WHITE);
            }
        } else if(number==9) {
            return new Tile(1, suit);
        }
        return new Tile(number+1, suit);
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
            s = s + "Facedown";
        } else if( red ){
            s = s + toString() + " Red";
        } else if( !Validator.validate(this) ){
            s = s + "Blank";
        } else {
            s = s + toString();
        }
        return s;
    }
}
