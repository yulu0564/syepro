package com.syepro.app.api.network;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.syepro.app.R;
import com.syepro.app.dialog.CustomProgressDialog;

public class ProgressDialogHandler extends Handler {

    public static final int SHOW_PROGRESS_DIALOG = 1;
    public static final int DISMISS_PROGRESS_DIALOG = 2;
    public static final int DISMISS_PROGRESS_DIALOG_IMMIDIATLY = 3;
    private static final long TIME_DELAY = 500;

    private CustomProgressDialog pd;

    private Context context;
    private boolean cancelable;
    private ProgressCancelListener mProgressCancelListener;
    private String mProgressDialogMessage;

    public ProgressDialogHandler(Context context, ProgressCancelListener mProgressCancelListener,
                                 boolean cancelable) {
        super();
        this.context = context;
        this.mProgressCancelListener = mProgressCancelListener;
        this.cancelable = cancelable;
        mProgressDialogMessage = context.getString(R.string.load_ing);
    }

    private long showProgressTime = 0;
    private Runnable dismissRunnable = new Runnable() {
        @Override
        public void run() {
            if (pd != null && pd.isShowing()) {
                try {
                    pd.dismiss();
                } catch (Exception ignored) {
                }
            }
            pd = null;
        }
    };

    public void setProgressDialogMessage(String mProgressDialogMessage) {
        if (!TextUtils.isEmpty(mProgressDialogMessage)) {
            this.mProgressDialogMessage = mProgressDialogMessage;
        } else {
            this.mProgressDialogMessage = context.getString(R.string.load_ing);
        }
    }

    private void initProgressDialog() {
        if (pd == null) {
            pd = new CustomProgressDialog(context, mProgressDialogMessage);
            pd.setCancelable(cancelable);
            pd.setCanceledOnTouchOutside(false);

            if (cancelable) {
                pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        if (mProgressCancelListener != null) {
                            mProgressCancelListener.onCancelProgress();
                        }
                        dismissProgressDialog();
                    }
                });
            }

            if (!pd.isShowing()) {
                try {
                    pd.show();
                } catch (Exception e) {

                }
            }
            pd.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (mProgressCancelListener != null) {
                        mProgressCancelListener.onDismissProgress();
                    }
                }
            });
        }
        showProgressTime = System.currentTimeMillis();
        removeCallbacks(dismissRunnable);
    }

    private void dismissProgressDialog(boolean immidiatly) {
        if (pd != null) {
            if (immidiatly) {
                if ((context instanceof Activity && ((Activity) context).isFinishing()) || System.currentTimeMillis() - showProgressTime > TIME_DELAY) {
                    removeCallbacks(dismissRunnable);
                    try {
                        pd.dismiss();
                    } catch (Exception ignored) {
                    }
                    pd = null;
                }
            } else if ((context instanceof Activity && ((Activity) context).isFinishing()) || System.currentTimeMillis() - showProgressTime > TIME_DELAY) {
                removeCallbacks(dismissRunnable);
                try {
                    pd.dismiss();
                } catch (Exception ignored) {
                }
                pd = null;
            } else {
                postDelayed(dismissRunnable, TIME_DELAY - (System.currentTimeMillis() - showProgressTime));
            }
        }
    }

    private void dismissProgressDialog() {
        dismissProgressDialog(false);
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case SHOW_PROGRESS_DIALOG:
                initProgressDialog();
                break;
            case DISMISS_PROGRESS_DIALOG:
                dismissProgressDialog();
                break;
            case DISMISS_PROGRESS_DIALOG_IMMIDIATLY:
                dismissProgressDialog();
                break;
        }
    }
}