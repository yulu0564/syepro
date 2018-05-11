package com.yulu.kotlindemo.base

import android.app.Application
import android.content.Context
import kotlin.properties.Delegates


class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        init()
    }

    /**
     * 初始化
     */
    fun init() {
        instance = this
    }
    companion object {
        var instance: Context by Delegates.notNull()
    }
}
