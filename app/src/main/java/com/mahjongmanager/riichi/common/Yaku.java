package com.mahjongmanager.riichi.common;

import com.mahjongmanager.riichi.utils.AppSettings;

import java.util.Arrays;
import java.util.List;

public class Yaku {
    public enum Name {
        RIICHI,     CHIITOITSU,     NAGASHI,        TSUMO,          IPPATSU,        HAITEI,
        HOUTEI,     RINSHAN,        CHANKAN,        DOUBLERIICHI,   PINFU,          IIPEIKOU,
        SANSHOKUDOUJUN,             ITTSUU,         RYANPEIKOU,     TOITOI,         SANANKOU,
        SANSHOKUDOUKOU,             SANKANTSU,      TANYAO,         YAKUHAI,        CHANTA,
        JUNCHAN,    HONROUTOU,      SHOUSANGEN,     HONITSU,        CHINITSU,       KOKUSHIMUSOU,
        KOKUSHIMUSOU13SIDED,        SUUANKOU,       SUUANKOUTANKI,  DAISANGEN,      SHOUSUUSHII,
        DAISUUSHII, TSUUIISOU,      DAICHISEI,      CHINROUTOU,     RYUUIISOU,      CHUURENPOUTOU,
        CHUURENPOUTOU9SIDED,        SUUKANTSU,      TENHOU,         CHIIHOU,        RENHOU,
        SANRENKOU,   SUURENKOU,     DAISHARIN,      SHIISANPUUTA,   SHIISUUPUUTA,   PARENCHAN,
        DORA
    }

    public Name name;
    public int rarity;      // Determines when it is displayed in the YakuDescriptions page

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

    public static List<Name> getAbnormalStructureNames(){
        return Arrays.asList(
                Name.KOKUSHIMUSOU,
                Name.KOKUSHIMUSOU13SIDED,
                Name.CHIITOITSU,
                Name.DAICHISEI,
                Name.NAGASHI
        );
    }
    public static List<Name> getYakumanNames(){
        return Arrays.asList(
                Name.KOKUSHIMUSOU,
                Name.KOKUSHIMUSOU13SIDED,
                Name.SUUANKOU,
                Name.SUUANKOUTANKI,
                Name.DAISANGEN,
                Name.SHOUSUUSHII,
                Name.DAISUUSHII,
                Name.TSUUIISOU,
                Name.DAICHISEI,
                Name.CHINROUTOU,
                Name.RYUUIISOU,
                Name.CHUURENPOUTOU,
                Name.CHUURENPOUTOU9SIDED,
                Name.SUUKANTSU,
                Name.SUURENKOU,
                Name.DAISHARIN,
                Name.SHIISANPUUTA,
                Name.SHIISUUPUUTA,
                Name.PARENCHAN
        );
    }

    public String toString(){
        return "romaji: "+romaji;
    }
    public String toStringVerbose(){
        String s = " name: " + name + "\n";
        s = s + " rarity: " + rarity+ "\n";
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
