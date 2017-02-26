package com.mahjongmanager.riichi.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.mahjongmanager.riichi.MainActivity;

public class AppSettings {
    private static SharedPreferences sharedPref;

    public static void init(MainActivity activity){
        sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
    }

    public enum Terminology {
        ENGLISH,
        ROMAJI,
        KANJI
    }

    ///////////////////////////////////////////////////////
    //////////////           Keys            //////////////
    ///////////////////////////////////////////////////////
    // High Scores
    private static final String SQ_HIGH_SCORE = "SpeedQuizHighScore";
    private static final String HB_HAN_RECORD = "HandBuilderHanRecord";
    private static final String HB_FU_RECORD = "HandBuilderFuRecord";

    // App Settings
    private static final String TERMINOLOGY = "Terminology";
    private static final String KEYBOARD_TILE_SIZE = "KeyboardSmallTiles";
    private static final String SQ_RANDOM_WINDS = "SQRandomWinds";
    private static final String SQ_SEPARATE_CLOSED_MELDS = "SQSeparateClosedMelds";
    private static final String SQ_SITUATIONAL_YAKU = "SQSituationalYaku";
    private static final String SQ_NUMBER_OF_SUITS = "SQNumberOfSuits";
    private static final String SQ_ALLOW_HONORS = "SQAllowHonors";
    private static final String SQ_MAX_HANDS = "SQMaxHands";
    private static final String DD_TOOLTIPS = "DDTooltips";
    private static final String BANNER_ADS = "BannerAds";

    // Game Rules
    private static final String KAN_DORA       = "KanDora";
    private static final String LIMIT_URA_DORA = "LimitUraDora";
    private static final String GOLDEN_DORA    = "GoldenDora";
    private static final String WEST_ROUND     = "WestRound";
    private static final String KAN_DORA_IMMEDIATELY      = "KanDoraImmediately";
    private static final String AGARI_YAME                = "AgariYame";
    private static final String ALLOW_BOXING              = "AllowBoxing";
    private static final String RIICHI_WHEN_LOW_ON_POINTS = "RiichiWhenLowOnPoints";
    private static final String ATAMA_HANE_RON        = "AtamaHaneRon";
    private static final String KAN_CAN_CHANGE_WAIT   = "KanCanChangeWait";
    private static final String DOUBLE_YAKUMAN        = "DoubleYakuman";

    private static final String OPEN_TANYAO    = "OpenTanyao";
    private static final String OPEN_PINFU     = "OpenPinfu";
    private static final String OPEN_RIICHI    = "OpenRiichi";
    private static final String NAGASHI_MANGAN = "NagashiMangan";
    private static final String SANRENKOU      = "Sanrenkou";
    private static final String SUURENKOU      = "Suurenkou";
    private static final String DAISHARIN      = "Daisharin";
    private static final String SHIISANPUUTA   = "Shiisanpuuta";
    private static final String SHIISUUPUUTA   = "Shiisuupuuta";
    private static final String PARENCHAN      = "Parenchan";

    private static final String ABORTIVE_4_WINDS      = "Abortive4Winds";
    private static final String ABORTIVE_4_RIICHI     = "Abortive4Riichi";
    private static final String ABORTIVE_KYUSHUKYUHAI = "AbortiveKyushukyuhai";
    private static final String ABORTIVE_4_KANS       = "Abortive4Kans";

    private static final String RED_DORA_COUNT                 = "RedDoraCount";
    private static final String THREE_PLAYER_STARTING_POINTS   = "ThreePlayerStartingPoints";
    private static final String THREE_PLAYER_PLACEMENT_BONUS_1 = "ThreePlayerPlacementBonus1";
    private static final String THREE_PLAYER_CHOMBO_SIZE       = "ThreePlayerChomboSize";
    private static final String FOUR_PLAYER_STARTING_POINTS    = "FourPlayerStartingPoints";
    private static final String FOUR_PLAYER_PLACEMENT_BONUS_1  = "FourPlayerPlacementBonus1";
    private static final String FOUR_PLAYER_PLACEMENT_BONUS_2  = "FourPlayerPlacementBonus2";
    private static final String FOUR_PLAYER_CHOMBO_SIZE        = "FourPlayerChomboSize";
    private static final String FIVE_PLAYER_STARTING_POINTS    = "FivePlayerStartingPoints";
    private static final String FIVE_PLAYER_PLACEMENT_BONUS_1  = "FivePlayerPlacementBonus1";
    private static final String FIVE_PLAYER_PLACEMENT_BONUS_2  = "FivePlayerPlacementBonus2";
    private static final String FIVE_PLAYER_CHOMBO_SIZE        = "FivePlayerChomboSize";


    ///////////////////////////////////////////////////////
    //////////////      Getters/Setters      //////////////
    ///////////////////////////////////////////////////////
    // High Scores
    public static int getSpeedQuizHighScore(){
        return sharedPref.getInt(SQ_HIGH_SCORE, 0);
    }
    public static void setSpeedQuizHighScore(int value){
        savePreference(SQ_HIGH_SCORE, value);
    }

    public static int getHandBuilderHanRecord(){
        return sharedPref.getInt(HB_HAN_RECORD, 0);
    }
    public static void setHandBuilderHanRecord(int value){
        savePreference(HB_HAN_RECORD, value);
    }

    public static int getHandBuilderFuRecord(){
        return sharedPref.getInt(HB_FU_RECORD, 0);
    }
    public static void setHandBuilderFuRecord(int value){
        int roundedFu = (value==25) ? 25 : (int) Math.ceil(value/10.0)*10;
        savePreference(HB_FU_RECORD, roundedFu);
    }

    // App Settings
    public static Terminology getTerminology(){
        return Terminology.valueOf(sharedPref.getString(TERMINOLOGY, "ROMAJI").toUpperCase());
    }
    public static void setTerminology(Terminology term){
        savePreference(TERMINOLOGY, term.toString());
    }

    public static boolean getKeyboardTileSize(){
        return sharedPref.getBoolean(KEYBOARD_TILE_SIZE, false);
    }
    public static void setKeyboardTileSize(boolean value){
        savePreference(KEYBOARD_TILE_SIZE, value);
    }

    public static boolean getSpeedQuizRandomWinds(){
        return sharedPref.getBoolean(SQ_RANDOM_WINDS, false);
    }
    public static void setSpeedQuizRandomWinds(boolean value){
        savePreference(SQ_RANDOM_WINDS, value);
    }

    public static boolean getSpeedQuizSeparateClosedMelds(){
        return sharedPref.getBoolean(SQ_SEPARATE_CLOSED_MELDS, false);
    }
    public static void setSpeedQuizSeparateClosedMelds(boolean value){
        savePreference(SQ_SEPARATE_CLOSED_MELDS, value);
    }

    public static boolean getSpeedQuizSituationalYaku(){
        return sharedPref.getBoolean(SQ_SITUATIONAL_YAKU, false);
    }
    public static void setSpeedQuizSituationalYaku(boolean value){
        savePreference(SQ_SITUATIONAL_YAKU, value);
    }

    public static int getSpeedQuizNumberOfSuits(){
        return sharedPref.getInt(SQ_NUMBER_OF_SUITS, 3);
    }
    public static void setSpeedQuizNumberOfSuits(int value){
        savePreference(SQ_NUMBER_OF_SUITS, value);
    }

    public static boolean getSpeedQuizAllowHonors(){
        return sharedPref.getBoolean(SQ_ALLOW_HONORS, true);
    }
    public static void setSpeedQuizAllowHonors(boolean value){
        savePreference(SQ_ALLOW_HONORS, value);
    }

    public static int getSpeedQuizMaxHands(){
        return sharedPref.getInt(SQ_MAX_HANDS, 10);
    }
    public static void setSpeedQuizMaxHands(int value){
        savePreference(SQ_MAX_HANDS, value);
    }

    public static boolean getDrawDiscardTooltips(){
        return sharedPref.getBoolean(DD_TOOLTIPS, true);
    }
    public static void setDrawDiscardTooltips(boolean value){
        savePreference(DD_TOOLTIPS, value);
    }

    public static boolean getBannerAdsEnabled(){
        return sharedPref.getBoolean(BANNER_ADS, true);
    }
    public static void setBannerAdsEnabled(boolean value){
        savePreference(BANNER_ADS, value);
    }


    // Game Rules
    public static boolean getRulesKanDora(){
        return sharedPref.getBoolean(KAN_DORA, true);
    }
    public static void setRulesKanDora(boolean value){
        savePreference(KAN_DORA, value);
    }

    public static boolean getRulesLimitUraDora(){
        return sharedPref.getBoolean(LIMIT_URA_DORA, false);
    }
    public static void setRulesLimitUraDora(boolean value){
        savePreference(LIMIT_URA_DORA, value);
    }

    public static boolean getRulesGoldenDora(){
        return sharedPref.getBoolean(GOLDEN_DORA, false);
    }
    public static void setRulesGoldenDora(boolean value){
        savePreference(GOLDEN_DORA, value);
    }

    public static boolean getRulesWestRound(){
        return sharedPref.getBoolean(WEST_ROUND, true);
    }
    public static void setRulesWestRound(boolean value){
        savePreference(WEST_ROUND, value);
    }

    public static boolean getRulesKanDoraImmediately(){
        return sharedPref.getBoolean(KAN_DORA_IMMEDIATELY, false);
    }
    public static void setRulesKanDoraImmediately(boolean value){
        savePreference(KAN_DORA_IMMEDIATELY, value);
    }

    public static boolean getRulesAgariYame(){
        return sharedPref.getBoolean(AGARI_YAME, true);
    }
    public static void setRulesAgariYame(boolean value){
        savePreference(AGARI_YAME, value);
    }

    public static boolean getRulesAllowBoxing(){
        return sharedPref.getBoolean(ALLOW_BOXING, true);
    }
    public static void setRulesAllowBoxing(boolean value){
        savePreference(ALLOW_BOXING, value);
    }

    public static boolean getRulesRiichiWhenLowOnPoints(){
        return sharedPref.getBoolean(RIICHI_WHEN_LOW_ON_POINTS, false);
    }
    public static void setRulesRiichiWhenLowOnPoints(boolean value){
        savePreference(RIICHI_WHEN_LOW_ON_POINTS, value);
    }

    // TODO refactor to "AllowMultipleRon"?
    public static boolean getRulesAtamaHaneRon(){
        return sharedPref.getBoolean(ATAMA_HANE_RON, false);
    }
    public static void setRulesAtamaHaneRon(boolean value){
        savePreference(ATAMA_HANE_RON, value);
    }

    public static boolean getRulesKanCanChangeWait(){
        return sharedPref.getBoolean(KAN_CAN_CHANGE_WAIT, false);
    }
    public static void setRulesKanCanChangeWait(boolean value){
        savePreference(KAN_CAN_CHANGE_WAIT, value);
    }

    public static boolean getRulesDoubleYakumanAllowed(){
        return sharedPref.getBoolean(DOUBLE_YAKUMAN, true);
    }
    public static void setRulesDoubleYakumanAllowed(boolean value){
        savePreference(DOUBLE_YAKUMAN, value);
    }

    public static boolean getRulesOpenTanyao(){
        return sharedPref.getBoolean(OPEN_TANYAO, true);
    }
    public static void setRulesOpenTanyao(boolean value){
        savePreference(OPEN_TANYAO, value);
    }

    public static boolean getRulesOpenPinfu(){
        return sharedPref.getBoolean(OPEN_PINFU, false);
    }
    public static void setRulesOpenPinfu(boolean value){
        savePreference(OPEN_PINFU, value);
    }

    public static boolean getRulesOpenRiichi(){
        return sharedPref.getBoolean(OPEN_RIICHI, false);
    }
    public static void setRulesOpenRiichi(boolean value){
        savePreference(OPEN_RIICHI, value);
    }

    public static boolean getRulesNagashiMangan(){
        return sharedPref.getBoolean(NAGASHI_MANGAN, true);
    }
    public static void setRulesNagashiMangan(boolean value){
        savePreference(NAGASHI_MANGAN, value);
    }

    public static boolean getRulesSanrenkou(){
        return sharedPref.getBoolean(SANRENKOU, true);
    }
    public static void setRulesSanrenkou(boolean value){
        savePreference(SANRENKOU, value);
    }

    public static boolean getRulesSuurenkou(){
        return sharedPref.getBoolean(SUURENKOU, false);
    }
    public static void setRulesSuurenkou(boolean value){
        savePreference(SUURENKOU, value);
    }

    public static boolean getRulesDaisharin(){
        return sharedPref.getBoolean(DAISHARIN, false);
    }
    public static void setRulesDaisharin(boolean value){
        savePreference(DAISHARIN, value);
    }

    public static boolean getRulesShiisanpuuta(){
        return sharedPref.getBoolean(SHIISANPUUTA, false);
    }
    public static void setRulesShiisanpuuta(boolean value){
        savePreference(SHIISANPUUTA, value);
    }

    public static boolean getRulesShiisuupuuta(){
        return sharedPref.getBoolean(SHIISUUPUUTA, false);
    }
    public static void setRulesShiisuupuuta(boolean value){
        savePreference(SHIISUUPUUTA, value);
    }

    public static boolean getRulesParenchan(){
        return sharedPref.getBoolean(PARENCHAN, true);
    }
    public static void setRulesParenchan(boolean value){
        savePreference(PARENCHAN, value);
    }

    public static boolean getRulesAbortiveFourWinds(){
        return sharedPref.getBoolean(ABORTIVE_4_WINDS, true);
    }
    public static void setRulesAbortiveFourWinds(boolean value){
        savePreference(ABORTIVE_4_WINDS, value);
    }

    public static boolean getRulesAbortiveFourRiichi(){
        return sharedPref.getBoolean(ABORTIVE_4_RIICHI, true);
    }
    public static void setRulesAbortiveFourRiichi(boolean value){
        savePreference(ABORTIVE_4_RIICHI, value);
    }

    public static boolean getRulesAbortiveKyuushikyuhai(){
        return sharedPref.getBoolean(ABORTIVE_KYUSHUKYUHAI, true);
    }
    public static void setRulesAbortiveKyuushukyuhai(boolean value){
        savePreference(ABORTIVE_KYUSHUKYUHAI, value);
    }

    public static boolean getRulesAbortiveFourKans(){
        return sharedPref.getBoolean(ABORTIVE_4_KANS, true);
    }
    public static void setRulesAbortiveFourKans(boolean value){
        savePreference(ABORTIVE_4_KANS, value);
    }

    public static int getRulesRedDoraCount(){
        return sharedPref.getInt(RED_DORA_COUNT, 3);
    }
    public static void setRulesRedDoraCount(int value){
        savePreference(RED_DORA_COUNT, value);
    }

    public static int getRulesThreePlayerStartingPoints(){
        return sharedPref.getInt(THREE_PLAYER_STARTING_POINTS, 35000);
    }
    public static void setRulesThreePlayerStartingPoints(int value){
        savePreference(THREE_PLAYER_STARTING_POINTS, value);
    }

    public static int getRulesThreePlayerPlacementBonus1(){
        return sharedPref.getInt(THREE_PLAYER_PLACEMENT_BONUS_1, 15);
    }
    public static void aetRulesThreePlayerPlacementBonus1(int value){
        savePreference(THREE_PLAYER_PLACEMENT_BONUS_1, value);
    }

    public static int getRulesThreePlayerChomboSize(){
        return sharedPref.getInt(THREE_PLAYER_CHOMBO_SIZE, 6000);
    }
    public static void setRulesThreePlayerChomboSize(int value){
        savePreference(THREE_PLAYER_CHOMBO_SIZE, value);
    }

    public static int getRulesFourPlayerStartingPoints(){
        return sharedPref.getInt(FOUR_PLAYER_STARTING_POINTS, 25000);
    }
    public static void setRulesFourPlayerStartingPoints(int value){
        savePreference(FOUR_PLAYER_STARTING_POINTS, value);
    }

    public static int getRulesFourPlayerPlacementBonus1(){
        return sharedPref.getInt(FOUR_PLAYER_PLACEMENT_BONUS_1, 15);
    }
    public static void setRulesFourPlayerPlacementBonus1(int value){
        savePreference(FOUR_PLAYER_PLACEMENT_BONUS_1, value);
    }

    public static int getRulesFourPlayerPlacementBonus2(){
        return sharedPref.getInt(FOUR_PLAYER_PLACEMENT_BONUS_2, 5);
    }
    public static void setRulesFourPlayerPlacementBonus2(int value){
        savePreference(FOUR_PLAYER_PLACEMENT_BONUS_2, value);
    }

    public static int getRulesFourPlayerChomboSize(){
        return sharedPref.getInt(FOUR_PLAYER_CHOMBO_SIZE, 3000);
    }
    public static void setRulesFourPlayerChomboSize(int value){
        savePreference(FOUR_PLAYER_CHOMBO_SIZE, value);
    }

    public static int getRulesFivePlayerStartingPoints(){
        return sharedPref.getInt(FIVE_PLAYER_STARTING_POINTS, 25000);
    }
    public static void setRulesFivePlayerStartingPoints(int value){
        savePreference(FIVE_PLAYER_STARTING_POINTS, value);
    }

    public static int getRulesFivePlayerPlacementBonus1(){
        return sharedPref.getInt(FIVE_PLAYER_PLACEMENT_BONUS_1, 20);
    }
    public static void setRulesFivePlayerPlacementBonus1(int value){
        savePreference(FIVE_PLAYER_PLACEMENT_BONUS_1, value);
    }

    public static int getRulesFivePlayerPlacementBonus2(){
        return sharedPref.getInt(FIVE_PLAYER_PLACEMENT_BONUS_2, 10);
    }
    public static void setRulesFivePlayerPlacementBonus2(int value){
        savePreference(FIVE_PLAYER_PLACEMENT_BONUS_2, value);
    }

    public static int getRulesFivePlayerChomboSize(){
        return sharedPref.getInt(FIVE_PLAYER_CHOMBO_SIZE, 2000);
    }
    public static void setRulesFivePlayerChomboSize(int value){
        savePreference(FIVE_PLAYER_CHOMBO_SIZE, value);
    }


    ///////////////////////////////////////////////////////
    //////////////         Generics          //////////////
    ///////////////////////////////////////////////////////
    private static void savePreference(String keyString, String value){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(keyString, value);
        editor.apply();
    }
    private static void savePreference(String keyString, int value){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(keyString, value);
        editor.apply();
    }
    private static void savePreference(String keyString, boolean value){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(keyString, value);
        editor.apply();
    }
}