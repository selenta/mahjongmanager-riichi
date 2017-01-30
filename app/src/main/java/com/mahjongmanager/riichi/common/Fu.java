package com.mahjongmanager.riichi.common;

import com.mahjongmanager.riichi.utils.AppSettings;

public class Fu {
    public enum Name {
        FUUTEI,
        CHIITOITSU,
        MENZEN_KAFU,
        PINFU,
        PINFU_OPEN,
        SELF_DRAW,
        DRAGON_PAIR,
        PREVAILING_WIND,
        SEAT_WIND,
        PAIR_WAIT,
        SINGLE_SIDED_WAIT,
        INSIDE_WAIT,
        MELD_1,
        MELD_2,
        MELD_3,
        MELD_4
    }

    public Name name;

    public String english;
    public String romaji;
    public String kanji;
    public int value;
    public String description;

    public String getLocalizedString(){
        AppSettings.Terminology t = AppSettings.getTerminology();

        switch (t) {
            case ENGLISH:
                return english;
            case KANJI:
                return kanji;
            default:
                return romaji;
        }
    }

    public String toString(){
        return "romaji: "+romaji;
    }
    public String toStringVerbose(){
        String s = " name: " + name + "\n";
        s = s + " english: " + english + "\n";
        s = s + " romaji: " + romaji + "\n";
        s = s + " kanji: " + kanji + "\n";
        s = s + " value: " + value + "\n";
        s = s + " description: " + description + "\n";
        return s;
    }
}
