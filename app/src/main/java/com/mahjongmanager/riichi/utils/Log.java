package com.mahjongmanager.riichi.utils;

import com.mahjongmanager.riichi.BuildConfig;

public class Log {
    private static int LOG_LEVEL = 3;

    public static void v(String tag, String msg){
        if(BuildConfig.DEBUG && LOG_LEVEL>=5) {
            android.util.Log.v( tag, msg );
        }
    }

    public static void d(String tag, String msg){
        if(BuildConfig.DEBUG && LOG_LEVEL>=4) {
            android.util.Log.d( tag, msg );
        }
    }

    public static void i(String tag, String msg){
        if(BuildConfig.DEBUG && LOG_LEVEL>=3) {
            android.util.Log.i( tag, msg );
        }
    }

    public static void w(String tag, String msg){
        if(BuildConfig.DEBUG && LOG_LEVEL>=2) {
            android.util.Log.w( tag, msg );
        }
    }

    public static void e(String tag, String msg){
        if(BuildConfig.DEBUG && LOG_LEVEL>=1) {
            android.util.Log.e( tag, msg );
        }
    }

    public static void wtf(String tag, String msg){
        if(BuildConfig.DEBUG && LOG_LEVEL>=0) {
            android.util.Log.wtf( tag, msg );
        }
    }
}
