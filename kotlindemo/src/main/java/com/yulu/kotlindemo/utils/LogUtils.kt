package com.yulu.kotlindemo.utils

import android.util.Log

/**
 * 日志工具类
 */
object LogUtils {
    private val TAG = "TAG"

    private val DEBUG = true

    fun debug(msg: String) {
        if (DEBUG) {
            Log.d(TAG, msg)
        }
    }

    fun info(msg: String) {
        if (DEBUG) {
            Log.i(TAG, msg)
        }
    }

    fun error(msg: String) {
        if (DEBUG) {
            Log.e(TAG, msg)
        }
    }

    fun warn(msg: String) {
        if (DEBUG) {
            Log.w(TAG, msg)
        }
    }

    fun debug(tag: String, msg: String) {
        if (DEBUG) {
            Log.d(tag, msg)
        }
    }

    fun info(tag: String, msg: String) {
        if (DEBUG) {
            Log.i(tag, msg)
        }
    }

    fun error(tag: String, msg: String) {
        if (DEBUG) {
            Log.e(tag, msg)
        }
    }

    fun warn(tag: String, msg: String) {
        if (DEBUG) {
            Log.w(tag, msg)
        }
    }
}
