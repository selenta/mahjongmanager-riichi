package com.mahjongmanager.riichi.common;

import com.mahjongmanager.riichi.utils.AppSettings;

public class Yaku {
    public enum Name {
        RIICHI, CHIITOITSU, NAGASHI, TSUMO, IPPATSU, HAITEI, HOUTEI, RINSHAN, CHANKAN, DOUBLERIICHI,
        PINFU, IIPEIKOU, SANSHOKUDOUJUN, ITTSUU, RYANPEIKOU, TOITOI, SANANKOU, SANSHOKUDOUKOU,
        SANKANTSU, TANYAO, YAKUHAI, CHANTA, JUNCHAN, HONROUTOU, SHOUSANGEN, HONITSU, CHINITSU,
        KOKUSHIMUSOU, KOKUSHIMUSOU13SIDED, SUUANKOU, SUUANKOUTANKI, DAISANGEN, SHOUSUUSHII,
        DAISUUSHII, TSUUIISOU, DAICHISEI, CHINROUTOU, RYUUIISOU, CHUURENPOUTOU, CHUURENPOUTOU9SIDED,
        SUUKANTSU, TENHOU, CHIIHOU, RENHOU, SANRENKOU, SUURENKOU, DAISHARIN, SHIISANPUUTA,
        SHIISUUPUUTA, PARENCHAN, DORA
    }

    public Name name;

    public String english;
    public String romaji;
    public String kanji;
    public String hanClosed;
    public String hanOpen;
    public String description;

    public Hand exampleHand;

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
        s = s + " hanClosed: " + hanClosed + "\n";
        s = s + " hanOpen: " + hanOpen + "\n";
        s = s + " description: " + description + "\n";
        s = s + " exampleHand: " + exampleHand + "\n";
        return s;
    }
}