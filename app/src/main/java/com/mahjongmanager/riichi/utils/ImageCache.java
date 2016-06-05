package com.mahjongmanager.riichi.utils;

import android.graphics.drawable.BitmapDrawable;
import android.support.v4.util.LruCache;

import com.mahjongmanager.riichi.MainActivity;

public class ImageCache {
    private MainActivity activity;

    private static final int DEFAULT_MEM_CACHE_SIZE = 1024 * 5; // 5MB
    private LruCache<String, BitmapDrawable> mMemoryCache;

    public static String HAND_DISPLAY_KEY = "HandDisplay ";
    public static String KEYBOARD_KEY_LARGE = "KeyboardLarge ";
    public static String KEYBOARD_KEY_SMALL = "KeyboardSmall ";

    public ImageCache(MainActivity ma){
        activity = ma;
        init();
    }

    private void init(){
        mMemoryCache = new LruCache<String, BitmapDrawable>(DEFAULT_MEM_CACHE_SIZE) {
            /**
             * Measure item size in kilobytes rather than units which is more practical
             * for a bitmap cache
             */
            // NOTE for some reason, this makes the Large button version of the HandCalculator
            //      fail to load its button images correctly on tablets with
            //      larger than standard resolutions ... wth?
//            @Override
//            protected int sizeOf(String key, BitmapDrawable value) {
//                final int bitmapSize = getBitmapSize(value) / 1024;
//                return bitmapSize == 0 ? 1 : bitmapSize;
//            }
        };
    }

    /**
     * Adds a bitmap to both memory and disk cache.
     * @param keyString Unique identifier for the bitmap to store
     * @param value The bitmap drawable to store
     */
    public void addBitmapToCache(String keyString, BitmapDrawable value) {
        if (keyString == null || value == null) {
            return;
        }

        if (mMemoryCache != null) {
//            Log.wtf("wtfIsCacheDoing", "keyString: "+keyString);
//            Log.wtf("wtfIsCacheDoing", "wtf cache, size? "+mMemoryCache.size());
            mMemoryCache.put(keyString, value);
        }
    }

    /**
     * Get from memory cache.
     *
     * @param keyString Unique identifier for which item to get
     * @return The bitmap drawable if found in cache, null otherwise
     */
    public BitmapDrawable getBitmapFromCache(String keyString) {
        BitmapDrawable memValue = null;
//        Log.wtf("wtfIsCacheDoing", "keyString: "+keyString);
//        Log.wtf("wtfIsCacheDoing", "wtf cache, size? "+mMemoryCache.size());
//        Log.wtf("wtfIsCacheDoing", "wtf cache, max size? "+mMemoryCache.maxSize());
//        Log.wtf("wtfIsCacheDoing", "wtf cache, miss count? "+mMemoryCache.missCount());
//        Log.wtf("wtfIsCacheDoing", "wtf cache, put count? "+mMemoryCache.putCount());

        if (mMemoryCache != null) {
            memValue = mMemoryCache.get(keyString);
        }

        if( memValue==null && keyString.contains(HAND_DISPLAY_KEY) ){
            activity.getUtils().populateImageCacheForHandDisplay();
            memValue = mMemoryCache.get(keyString);
        } else if( memValue==null && keyString.contains(KEYBOARD_KEY_LARGE) ){
            activity.getUtils().populateImageCacheForKeyboard(KEYBOARD_KEY_LARGE);
            memValue = mMemoryCache.get(keyString);
        } else if( memValue==null && keyString.contains(KEYBOARD_KEY_SMALL) ){
            activity.getUtils().populateImageCacheForKeyboard(KEYBOARD_KEY_SMALL);
            memValue = mMemoryCache.get(keyString);
        }

        return memValue;
    }

    public void clearCache() {
        if (mMemoryCache != null) {
            mMemoryCache.evictAll();
        }
    }

    /**
     * Get the size in bytes of a bitmap in a BitmapDrawable. This returns the allocated
     * memory size of the bitmap which can be larger than the actual bitmap data
     * byte count (in the case it was re-used).
     *
     * @param value
     * @return size in bytes
     */
    public static int getBitmapSize(BitmapDrawable value) {
        return value.getBitmap().getAllocationByteCount();
    }
}
