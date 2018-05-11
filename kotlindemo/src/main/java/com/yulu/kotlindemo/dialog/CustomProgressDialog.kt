package com.yulu.kotlindemo.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.AnimationDrawable
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView

import com.yulu.kotlindemo.R

class CustomProgressDialog : Dialog {

    private var isShow = true
    private var message: String? = null

    constructor(context: Context, message: String) : super(context, R.style.Custom_Progress) {
        this.message = message
    }

    constructor(context: Context, theme: Int) : super(context, theme) {}

    /**
     * 当窗口焦点改变时调用
     */
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        val imageView = findViewById<View>(R.id.loadingImageView) as ImageView
        // 获取ImageView上的动画背景
        val spinner = imageView.background as AnimationDrawable
        // 开始动画
        spinner.start()
    }

    /**
     * 给Dialog设置提示信息
     *
     * @param message
     */
    fun setMessage(message: CharSequence?) {
        if (message != null && message.length > 0) {
            val txt = findViewById<View>(R.id.id_tv_loadingmsg) as TextView
            if (txt != null) {
                txt.text = message
                txt.invalidate()
                findViewById<View>(R.id.id_tv_loadingmsg).visibility = View.VISIBLE
            } else {
                this.message = message.toString()
            }

        }
    }

    fun setProgressMsg(progressMsg: String) {
        val txt = findViewById<View>(R.id.id_tv_loadingmsg) as TextView
        if (txt != null) {
            txt.text = progressMsg
        }
    }

    override fun show() {
        if (isShow) {
            setTitle("")
            setContentView(R.layout.dialog_custom_progress)
            val txt = findViewById<TextView>(R.id.id_tv_loadingmsg)
            txt.text = message
            // 按返回键是否取消
            setCancelable(true)
            // 设置居中
            window!!.attributes.gravity = Gravity.CENTER
            val lp = window!!.attributes
            // 设置背景层透明度
            lp.dimAmount = 0.0f
            window!!.attributes = lp
            isShow = false
        }
        super.show()
    }

    companion object {

        /**
         * 弹出自定义ProgressDialog
         *
         * @param context        上下文
         * @param message        提示
         * @param cancelable     是否按返回键取消
         * @param cancelListener 按下返回键监听
         * @return
         */
        fun show(context: Context, message: CharSequence?, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener): CustomProgressDialog {
            val dialog = CustomProgressDialog(context, R.style.Custom_Progress)
            dialog.setTitle("")
            dialog.setContentView(R.layout.dialog_custom_progress)
            if (message == null) {
                dialog.findViewById<View>(R.id.id_tv_loadingmsg).visibility = View.GONE
            } else if (message.length == 0) {
                val txt = dialog.findViewById<TextView>(R.id.id_tv_loadingmsg)
                txt.text = "加载中"
            } else {
                val txt = dialog.findViewById<TextView>(R.id.id_tv_loadingmsg)
                txt.text = message
            }
            // 按返回键是否取消
            dialog.setCancelable(cancelable)
            // 监听返回键处理
            dialog.setOnCancelListener(cancelListener)
            // 设置居中
            dialog.window!!.attributes.gravity = Gravity.CENTER
            val lp = dialog.window!!.attributes
            // 设置背景层透明度
            lp.dimAmount = 0.2f
            dialog.window!!.attributes = lp
            // dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
            dialog.isShow = false
            dialog.show()
            return dialog
        }
    }
}

