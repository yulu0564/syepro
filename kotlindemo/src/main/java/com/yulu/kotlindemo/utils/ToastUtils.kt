package com.yulu.kotlindemo.utils

import android.content.Context
import android.text.TextUtils
import android.widget.Toast
import com.yulu.kotlindemo.R

import com.yulu.kotlindemo.base.BaseApplication

/**
 * 吐司工具类
 */
object ToastUtils {

    private var URL_REG: String? = null

    init {
        URL_REG = "(http(s)?://)([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&#=]*)?"
    }

    fun showToast(msg: String) {
        showToast(BaseApplication.instance, msg)
    }

    fun showToast(ctx: Context, message: String) {
        var message = message
        if (TextUtils.isEmpty(message)) {
            message = ctx.getString(R.string.net_state_error)
        }
        Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show()
    }

    fun showToast(content: String, gravity: Int) {
        val toast = Toast.makeText(BaseApplication.instance, content, Toast.LENGTH_SHORT)
        toast.setGravity(gravity, 0, 0)
        toast.show()
        showToast(content)
    }

    fun showToast(resId: Int) {
        Toast.makeText(BaseApplication.instance, resId, Toast.LENGTH_SHORT).show()
    }
}
