package com.syepro.app.commonjar.utils;

import android.util.Log;

/**
 * 日志工具类
 */
public class LogUtils {
    private static String TAG = "TAG";

    private static boolean DEBUG = true;

    public static void debug(String msg) {
        if (DEBUG) {
            Log.d(TAG, msg);
        }
    }

    public static void info(String msg) {
        if (DEBUG) {
            Log.i(TAG, msg);
        }
    }

    public static void error(String msg) {
        if (DEBUG) {
            Log.e(TAG, msg);
        }
    }

    public static void warn(String msg) {
        if (DEBUG) {
            Log.w(TAG, msg);
        }
    }

    public static void debug(String tag, String msg) {
        if (DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void info(String tag, String msg) {
        if (DEBUG) {
            Log.i(tag, msg);
        }
    }

    public static void error(String tag, String msg) {
        if (DEBUG) {
            Log.e(tag, msg);
        }
    }

    public static void warn(String tag, String msg) {
        if (DEBUG) {
            Log.w(tag, msg);
        }
    }
}
