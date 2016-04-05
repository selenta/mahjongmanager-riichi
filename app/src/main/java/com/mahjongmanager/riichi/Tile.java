package com.mahjongmanager.riichi;

import android.util.Log;

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

    public Integer number;
    public Dragon dragon;
    public Wind wind;

    public RevealedState revealedState;
    public CalledFrom calledFrom;

    public Boolean winningTile;

    public Tile(String valueString, String suitString ){
        suit = Suit.valueOf(suitString.toUpperCase());

        //check for dragon
        for( Dragon d : Dragon.values() ){
            if(d.name().equalsIgnoreCase(valueString)){
                dragon = Dragon.valueOf(valueString.toUpperCase());
            }
        }
        //check for wind
        for( Wind w : Wind.values() ){
            if(w.name().equalsIgnoreCase(valueString)){
                wind = Wind.valueOf(valueString.toUpperCase());
            }
        }
        //check for number
        if( dragon==null && wind==null ){
            number = Integer.valueOf(valueString);
        }

        red = false;
        winningTile = false;
        revealedState = RevealedState.NONE;
        calledFrom = CalledFrom.NONE;
        value = getValue();
        determineSortId();

        validateConstructor(suitString, valueString);
    }
    public Tile( Integer valueInt, String suitString ){
        suit = Suit.valueOf(suitString.toUpperCase());
        number = valueInt;

        red = false;
        winningTile = false;
        revealedState = RevealedState.NONE;
        calledFrom = CalledFrom.NONE;
        value = getValue();
        determineSortId();

        validateConstructor(suitString, valueInt.toString());
    }
    public Tile( Tile oldTile ){
        suit          = oldTile.suit;
        value         = oldTile.value;
        sortId        = oldTile.sortId;
        red           = oldTile.red;
        number        = oldTile.number;
        dragon        = oldTile.dragon;
        wind          = oldTile.wind;
        revealedState = oldTile.revealedState;
        calledFrom    = oldTile.calledFrom;
        winningTile   = oldTile.winningTile;
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

    private void validateConstructor(String specifiedSuit, String specifiedVal){
        if( number==null && dragon==null && wind==null ){
            Log.e("constructorValidation", "Tile has no value. Value given was: " + specifiedVal);
        } else if( suit==null ){
            Log.e("constructorValidation", "Tile has no suit. Suit given was: " + specifiedSuit);
        }
    }
    public boolean validateTile(){
        if( suit==null ){
            Log.e("validateTile", "Tile is naked! It has no suit!");
            return false;
        }

        //Has exactly one non-null number/dragon/wind value
        boolean n = number!=null;
        boolean d = dragon!=null;
        boolean w = wind!=null;
        if(!( (n&&!d&&!w)||(!n&&d&&!w)||(!n&&!d&&w) )){
            Log.e("validateTile", "Tile does not have exactly one non-null number/wind/dragon: " + number + " " + wind + " " + dragon );
            return false;
        }

        //Value matches suit
        if( suit==Suit.HONOR && n ){
            Log.e("validateTile", "Honor tiles should not have a number: " + number );
            return false;
        } else if( suit!=Suit.HONOR && (d || w) ){
            Log.e("validateTile", "Suited tiles should not have a wind or dragon value: " + wind + " " + dragon);
            return false;
        }

        if (red && (number==null || number!=5)) {
            Log.e("validateTile", "Only 5s can be red: " + toString());
            return false;
        }
        determineSortId();
        value = getValue();
        return true;
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
                        id=1;
                        break;
                    case 2:
                        id=2;
                        break;
                    case 3:
                        id=3;
                        break;
                    case 4:
                        id=4;
                        break;
                    case 5:
                        id=5;
                        if(red){
                            id=6;
                        }
                        break;
                    case 6:
                        id=11;
                        break;
                    case 7:
                        id=12;
                        break;
                    case 8:
                        id=13;
                        break;
                    case 9:
                        id=14;
                        break;
                }
                break;
            case PINZU:
                switch (number){
                    case 1:
                        id=21;
                        break;
                    case 2:
                        id=22;
                        break;
                    case 3:
                        id=23;
                        break;
                    case 4:
                        id=24;
                        break;
                    case 5:
                        id=25;
                        if(red){
                            id=26;
                        }
                        break;
                    case 6:
                        id=31;
                        break;
                    case 7:
                        id=32;
                        break;
                    case 8:
                        id=33;
                        break;
                    case 9:
                        id=34;
                        break;
                }
                break;
            case SOUZU:
                switch (number){
                    case 1:
                        id=41;
                        break;
                    case 2:
                        id=42;
                        break;
                    case 3:
                        id=43;
                        break;
                    case 4:
                        id=44;
                        break;
                    case 5:
                        id=45;
                        if(red){
                            id=46;
                        }
                        break;
                    case 6:
                        id=51;
                        break;
                    case 7:
                        id=52;
                        break;
                    case 8:
                        id=53;
                        break;
                    case 9:
                        id=54;
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

    public Integer getImageInt(){
        switch (suit){
            case MANZU:
                switch (number){
                    case 1:
                        return R.drawable.man1;
                    case 2:
                        return R.drawable.man2;
                    case 3:
                        return R.drawable.man3;
                    case 4:
                        return R.drawable.man4;
                    case 5:
                        if( !red ){
                            return R.drawable.man5;
                        } else {
                            return R.drawable.man5_dora;
                        }
                    case 6:
                        return R.drawable.man6;
                    case 7:
                        return R.drawable.man7;
                    case 8:
                        return R.drawable.man8;
                    case 9:
                        return R.drawable.man9;
                }
                break;
            case PINZU:
                switch (number){
                    case 1:
                        return R.drawable.pin1;
                    case 2:
                        return R.drawable.pin2;
                    case 3:
                        return R.drawable.pin3;
                    case 4:
                        return R.drawable.pin4;
                    case 5:
                        if( !red ){
                            return R.drawable.pin5;
                        } else {
                            return R.drawable.pin5_dora;
                        }
                    case 6:
                        return R.drawable.pin6;
                    case 7:
                        return R.drawable.pin7;
                    case 8:
                        return R.drawable.pin8;
                    case 9:
                        return R.drawable.pin9;
                }
                break;
            case SOUZU:
                switch (number){
                    case 1:
                        return R.drawable.sou1;
                    case 2:
                        return R.drawable.sou2;
                    case 3:
                        return R.drawable.sou3;
                    case 4:
                        return R.drawable.sou4;
                    case 5:
                        if( !red ){
                            return R.drawable.sou5;
                        } else {
                            return R.drawable.sou5_dora;
                        }
                    case 6:
                        return R.drawable.sou6;
                    case 7:
                        return R.drawable.sou7;
                    case 8:
                        return R.drawable.sou8;
                    case 9:
                        return R.drawable.sou9;
                }
                break;
            case HONOR:
                switch (value){
                    case "East":
                        return R.drawable.ton;
                    case "South":
                        return R.drawable.nan;
                    case "West":
                        return R.drawable.shaa;
                    case "North":
                        return R.drawable.pei;
                    case "White":
                        return R.drawable.haku;
                    case "Green":
                        return R.drawable.hatsu;
                    case "Red":
                        return R.drawable.chun;
                }
                break;
        }
        return R.drawable.blank;
    }
}
