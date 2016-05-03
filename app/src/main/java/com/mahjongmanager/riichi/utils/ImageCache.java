package com.mahjongmanager.riichi.utils;

import android.graphics.drawable.BitmapDrawable;
import android.support.v4.util.LruCache;

import com.mahjongmanager.riichi.MainActivity;

public class ImageCache {
    private MainActivity activity;

    private static final int DEFAULT_MEM_CACHE_SIZE = 1024 * 5; // 5MB
    private LruCache<String, BitmapDrawable> mMemoryCache;

    public static String HAND_DISPLAY_KEY = "HandDisplay ";
    public static int HAND_DISPLAY_TILE_WIDTH = 50;

    public static String KEYBOARD_KEY = "Keyboard ";
    public static int KEYBOARD_TILE_WIDTH = 110;

    public ImageCache(MainActivity ma){
        activity = ma;
        init();
    }

    public void init(){
        mMemoryCache = new LruCache<String, BitmapDrawable>(DEFAULT_MEM_CACHE_SIZE) {
            /**
             * Measure item size in kilobytes rather than units which is more practical
             * for a bitmap cache
             */
            @Override
            protected int sizeOf(String key, BitmapDrawable value) {
                final int bitmapSize = getBitmapSize(value) / 1024;
                return bitmapSize == 0 ? 1 : bitmapSize;
            }
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

        if (mMemoryCache != null) {
            memValue = mMemoryCache.get(keyString);
        }

        if( memValue==null && keyString.contains(KEYBOARD_KEY) ){
            activity.getUtils().populateImageCacheForKeyboard();
            memValue = mMemoryCache.get(keyString);
        } else if( memValue==null && keyString.contains(HAND_DISPLAY_KEY) ){
            activity.getUtils().populateImageCacheForHandDisplay();
            memValue = mMemoryCache.get(keyString);
        }

        return memValue;
    }

    public static int getTileHeight(int width){
        return (int) ((double)width * Utils.TILE_RATIO);
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
