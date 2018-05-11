package com.yulu.kotlindemo.api.network

interface ProgressCancelListener {
    fun onCancelProgress()

    fun onDismissProgress()
}