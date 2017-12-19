package com.syepro.app.api.network;

import android.content.Context;

import com.syepro.app.R;
import com.syepro.app.base.activity.BaseActivity;
import com.syepro.app.utils.ToastUtils;


import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * Created by wang.xiaolong5 on 2017-3-1.
 * <p>
 * Rx使用的回调器，附带一个加载中的dialog</p>
 * <p>
 * <p>
 * 传入BaseActivity实例可以将当前请求绑定到Activity上，当退出生命周期时
 * 可以一同结束请求，方式内存泄漏
 * </p>
 */
public abstract class ProgressSubscriber<T> implements ProgressCancelListener, Observer<ResultBean<T>> {

    private boolean cancelable = true;
    private ProgressDialogHandler mProgressDialogHandler;
    private Disposable disposable;
    private ResultBean<T> response;
    private Context context;
    private boolean showDialog = true;

    /**
     * 传入context会自动在开始时打开一个progressdialog
     *
     * @param context 不为空的时候会附带一个progressdialog
     */
    public ProgressSubscriber(Context context) {
        init(context, true, false);
    }

    /**
     * 传入context会自动在开始时打开一个progressdialog
     *
     * @param context    不为空的时候会附带一个progressdialog
     * @param showDialog 是否展示Dialog 默认展示
     */
    public ProgressSubscriber(Context context, boolean showDialog) {
        init(context, showDialog, false);
    }

    /**
     * 传入context会自动在开始时打开一个progressdialog
     *
     * @param context 不为空的时候会附带一个progressdialog
     */
    public ProgressSubscriber(Context context, boolean showDialog, boolean cancelable) {
        init(context, showDialog, cancelable);
    }

    private void init(Context context, boolean showDialog, boolean cancelable) {
        this.context = context;
        this.showDialog = showDialog;
        this.cancelable = cancelable;
        if (context != null && context instanceof BaseActivity) {
            bind((BaseActivity) this.context);
        }
    }

    /**
     * 无progressdialog
     */
    public ProgressSubscriber() {

    }

    /**
     * 重写此方法可以修改dialog的文字
     *
     * @return
     */
    protected String getProgressDialogMessage() {
        return null;
    }

    private void showProgressDialog() {
        if (!showDialog || context == null)
            return;
        if (mProgressDialogHandler == null) {
            mProgressDialogHandler = new ProgressDialogHandler(context, this, cancelable);
            context = null;
        }
        mProgressDialogHandler.setProgressDialogMessage(getProgressDialogMessage());
        mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
    }

    /**
     * 会延迟一小段时间再让dialog消失，防止当网络访问过快时，页面一闪而过
     */
    protected void dismissDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget();
            mProgressDialogHandler = null;
        }
    }

    protected void dismissDialogIMM() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG_IMMIDIATLY).sendToTarget();
            mProgressDialogHandler = null;
        }
    }

    public ProgressSubscriber<T> setContext(Context context) {
        this.context = context;
        return this;
    }

    @Override
    public void onDismissProgress() {
    }

    /**
     * 不要重写此方法，这个是给Rx调用，如果需要处理http不是200的情况，
     * 请重写onErpublic void onError(ErrorResponseBean errorResponseBean) 这个方法
     *
     * @param e
     */
    @Override
    @Deprecated
    public void onError(Throwable e) {
        dismissDialog();
        e.printStackTrace();
        if (e instanceof UnknownHostException) {
            ToastUtils.showToast(R.string.net_state_error);
        } else if (e instanceof ResponseCodeNotDefined) {
            ToastUtils.showToast(e.getMessage());
        } else {
            ToastUtils.showToast(R.string.common_otherError);
        }
    }

    @Override
    public void onComplete() {
        dismissDialog();
        if (context != null && context instanceof BaseActivity) {
            ((BaseActivity) context).removeDisposable(disposable);
        }
    }


    @Override
    public void onSubscribe(Disposable d) {
        this.disposable = d;
        showProgressDialog();
    }


    @Override
    public void onNext(ResultBean<T> response) {
        this.response = response;
        if (response.getCode() == 200) {
            onResponse(response.getData());
        }
    }

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
    public ResultBean<T> getResponse() {
        return response;
    }

    /**
     * 服务器返回的Code没有定义时，走这个Error，可以在onError里获取到message
     */
    public static class ResponseCodeNotDefined extends Throwable {

        public ResponseCodeNotDefined(String message) {
            super(message);
        }
    }

    public abstract void onResponse(T result);

    @Override
    public void onCancelProgress() {
        dismissDialogIMM();
        if (context != null && context instanceof BaseActivity) {
            ((BaseActivity) context).removeDisposable(disposable);
        }
        if (!disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    /**
     * 绑定到Activity
     *
     * @param activity
     * @return
     */
    public ProgressSubscriber<T> bind(BaseActivity activity) {
        context = activity;
        activity.addDisposable(disposable);
        return this;
    }
}
