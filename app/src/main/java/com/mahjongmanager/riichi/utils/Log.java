package com.mahjongmanager.riichi.utils;

import com.mahjongmanager.riichi.BuildConfig;

public class Log {
    public static void v(String tag, String msg){
        if(BuildConfig.DEBUG) {
            android.util.Log.v( tag, msg );
        }
    }

    public static void d(String tag, String msg){
        if(BuildConfig.DEBUG) {
            android.util.Log.d( tag, msg );
        }
    }

    public static void i(String tag, String msg){
        if(BuildConfig.DEBUG) {
            android.util.Log.i( tag, msg );
        }
    }

    public static void w(String tag, String msg){
        if(BuildConfig.DEBUG) {
            android.util.Log.w( tag, msg );
        }
    }

    public static void e(String tag, String msg){
        if(BuildConfig.DEBUG) {
            android.util.Log.e( tag, msg );
        }
    }

    public static void wtf(String tag, String msg){
        if(BuildConfig.DEBUG) {
            android.util.Log.wtf( tag, msg );
        }
    }
}
