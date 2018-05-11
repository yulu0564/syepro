package com.yulu.kotlindemo.api.network

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import com.yulu.kotlindemo.R

class ProgressDialogHandler(private val context: Context, private val mProgressCancelListener: ProgressCancelListener?,
                            private val cancelable: Boolean) : Handler() {

    private var pd: CustomProgressDialog? = null
    private var mProgressDialogMessage: String? = null

    private var showProgressTime: Long = 0
    private val dismissRunnable = Runnable {
        pd.let {
            if (it != null) {
                if (it.isShowing) {
                    try {
                        it.dismiss()
                    } catch (ignored: Exception) {
                    }
                }
            }
        }
        pd = null
    }

    init {
        mProgressDialogMessage = context.getString(R.string.load_ing)
    }

    fun setProgressDialogMessage(mProgressDialogMessage: String) {
        if (!TextUtils.isEmpty(mProgressDialogMessage)) {
            this.mProgressDialogMessage = mProgressDialogMessage
        } else {
            this.mProgressDialogMessage = context.getString(R.string.load_ing)
        }
    }

    private fun initProgressDialog() {
        if (pd == null) {
            pd = CustomProgressDialog(context, mProgressDialogMessage)
            pd.let {
                if (it != null) {
                    it.setCancelable(cancelable)
                    it.setCanceledOnTouchOutside(false)
                    if (cancelable) {
                        it.setOnCancelListener {
                            mProgressCancelListener?.onCancelProgress()
                            dismissProgressDialog()
                        }
                    }

                    if (!it.isShowing) {
                        try {
                            it.show()
                        } catch (e: Exception) {

                        }

                    }
                    it.setOnDismissListener {
                        mProgressCancelListener?.onDismissProgress()
                    }
                }
            }
        }
        showProgressTime = System.currentTimeMillis()
        removeCallbacks(dismissRunnable)
    }

    private fun dismissProgressDialog(immidiatly: Boolean = false) {
        pd.let {
            if (it != null) {
                if (immidiatly) {
                    if (context is Activity && context.isFinishing || System.currentTimeMillis() - showProgressTime > TIME_DELAY) {
                        removeCallbacks(dismissRunnable)
                        try {
                            it.dismiss()
                        } catch (ignored: Exception) {
                        }
                        pd = null
                    }
                } else if (context is Activity && context.isFinishing || System.currentTimeMillis() - showProgressTime > TIME_DELAY) {
                    removeCallbacks(dismissRunnable)
                    try {
                        it.dismiss()
                    } catch (ignored: Exception) {
                    }

                    pd = null
                } else {
                    postDelayed(dismissRunnable, TIME_DELAY - (System.currentTimeMillis() - showProgressTime))
                }
            }
        }
    }

    override fun handleMessage(msg: Message) {
        when (msg.what) {
            SHOW_PROGRESS_DIALOG -> initProgressDialog()
            DISMISS_PROGRESS_DIALOG -> dismissProgressDialog()
            DISMISS_PROGRESS_DIALOG_IMMIDIATLY -> dismissProgressDialog()
        }
    }

    companion object {

        val SHOW_PROGRESS_DIALOG = 1
        val DISMISS_PROGRESS_DIALOG = 2
        val DISMISS_PROGRESS_DIALOG_IMMIDIATLY = 3
        private val TIME_DELAY: Long = 500
    }
}