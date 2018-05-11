package com.yulu.kotlindemo.api.network

import android.content.Context
import com.yulu.kotlindemo.R
import com.yulu.kotlindemo.base.BaseActivity
import com.yulu.kotlindemo.utils.ToastUtils
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.net.UnknownHostException


/**
 * Created by wang.xiaolong5 on 2017-3-1.
 *
 *
 * Rx使用的回调器，附带一个加载中的dialog
 *
 *
 *
 *
 * 传入BaseActivity实例可以将当前请求绑定到Activity上，当退出生命周期时
 * 可以一同结束请求，方式内存泄漏
 *
 */
abstract class ProgressSubscriber<T> : ProgressCancelListener, Observer<ResultBean<T>> {

    private var cancelable = true
    private var mProgressDialogHandler: ProgressDialogHandler? = null
    private var disposable: Disposable? = null
    /**
     * 当Code不是200时，如果需要处理对应的body体力的内容时，可以重写此方法
     *
     * @param errorResponseBean 错误的body体
     * @return 如果处理了这个错误信息，返回true， 如果不处理这个错误，而是直接走统一处理（弹Toast），那么就返回false
     */
    //    public boolean onError(ErrorResponseBean errorResponseBean) {
    //
    //        return false;
    //    }


    /**
     * 获取整个返回的结果，而不仅仅是data
     *
     * @return
     */
    var response: ResultBean<T>? = null
        private set
    private var context: Context? = null
    private var showDialog = true

    /**
     * 重写此方法可以修改dialog的文字
     *
     * @return
     */
    protected val progressDialogMessage: String?
        get() = null

    /**
     * 传入context会自动在开始时打开一个progressdialog
     *
     * @param context 不为空的时候会附带一个progressdialog
     */
    constructor(context: Context) {
        init(context, true, false)
    }

    /**
     * 传入context会自动在开始时打开一个progressdialog
     *
     * @param context    不为空的时候会附带一个progressdialog
     * @param showDialog 是否展示Dialog 默认展示
     */
    constructor(context: Context, showDialog: Boolean) {
        init(context, showDialog, false)
    }

    /**
     * 传入context会自动在开始时打开一个progressdialog
     *
     * @param context 不为空的时候会附带一个progressdialog
     */
    constructor(context: Context, showDialog: Boolean, cancelable: Boolean) {
        init(context, showDialog, cancelable)
    }


    /**
     * 无progressdialog
     */
    constructor() {

    }

    private fun init(context: Context?, showDialog: Boolean, cancelable: Boolean) {
        this.context = context
        this.showDialog = showDialog
        this.cancelable = cancelable
        if (context != null && context is BaseActivity) {
            bind(context)
        }
    }

    private fun showProgressDialog() {
        if (!showDialog || context == null)
            return
        if (mProgressDialogHandler == null) {
            mProgressDialogHandler = ProgressDialogHandler(context!!, this, cancelable)
            context = null
        }
        mProgressDialogHandler!!.setProgressDialogMessage(progressDialogMessage!!)
        mProgressDialogHandler!!.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget()
    }

    /**
     * 会延迟一小段时间再让dialog消失，防止当网络访问过快时，页面一闪而过
     */
    protected fun dismissDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler!!.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget()
            mProgressDialogHandler = null
        }
    }

    protected fun dismissDialogIMM() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler!!.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG_IMMIDIATLY).sendToTarget()
            mProgressDialogHandler = null
        }
    }

    fun setContext(context: Context): ProgressSubscriber<T> {
        this.context = context
        return this
    }

    override fun onDismissProgress() {}

    /**
     * 不要重写此方法，这个是给Rx调用，如果需要处理http不是200的情况，
     * 请重写onErpublic void onError(ErrorResponseBean errorResponseBean) 这个方法
     *
     * @param e
     */
    @Deprecated("")
    override fun onError(e: Throwable) {
        dismissDialog()
        e.printStackTrace()
        if (e is UnknownHostException) {
            ToastUtils.showToast(R.string.net_state_error)
        } else if (e is ResponseCodeNotDefined) {
            ToastUtils.showToast(e.message ?: "")
        } else {
            ToastUtils.showToast(R.string.common_otherError)
        }
    }

    override fun onComplete() {
        dismissDialog()
        if (context != null && context is BaseActivity) {
            disposable.let {
                if (it != null) {
                    (context as BaseActivity).removeDisposable(it)
                }
            }
        }
    }


    override fun onSubscribe(d: Disposable) {
        this.disposable = d
        showProgressDialog()
    }


    override fun onNext(response: ResultBean<T>) {
        this.response = response

        if (response.code == 200) {
            response.data.let {
                if (it != null) {
                    onResponse(it)
                }
            }
        }
    }

    /**
     * 服务器返回的Code没有定义时，走这个Error，可以在onError里获取到message
     */
    class ResponseCodeNotDefined(message: String) : Throwable(message)

    abstract fun onResponse(result: T)

    override fun onCancelProgress() {
        dismissDialogIMM()
        if (context != null && context is BaseActivity) {
            disposable.let {
                if (it != null) {
                    (context as BaseActivity).removeDisposable(it)
                }
            }
        }
        disposable.let {
            if (it != null) {
                if (!it.isDisposed) {
                    it.dispose()
                }
            }
        }

    }

    /**
     * 绑定到Activity
     *
     * @param activity
     * @return
     */
    fun bind(activity: BaseActivity): ProgressSubscriber<T> {
        context = activity
        disposable.let {
            if (it != null) {
                activity.addDisposable(it)
            }
        }
        return this
    }
}
