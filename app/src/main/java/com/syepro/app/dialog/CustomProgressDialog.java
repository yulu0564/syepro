package com.syepro.app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.syepro.app.R;

public class CustomProgressDialog extends Dialog {

    private boolean isShow = true;
    private String message;

    public CustomProgressDialog(Context context, String message) {
        super(context, R.style.Custom_Progress);
        // TODO Auto-generated constructor stub
        this.message = message;
    }

    public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    /**
     * 弹出自定义ProgressDialog
     *
     * @param context        上下文
     * @param message        提示
     * @param cancelable     是否按返回键取消
     * @param cancelListener 按下返回键监听
     * @return
     */
    public static CustomProgressDialog show(Context context, CharSequence message, boolean cancelable, OnCancelListener cancelListener) {
        CustomProgressDialog dialog = new CustomProgressDialog(context, R.style.Custom_Progress);
        dialog.setTitle("");
        dialog.setContentView(R.layout.dialog_custom_progress);
        if (message == null) {
            dialog.findViewById(R.id.id_tv_loadingmsg).setVisibility(View.GONE);
        } else if (message.length() == 0) {
            TextView txt = (TextView) dialog.findViewById(R.id.id_tv_loadingmsg);
            txt.setText("加载中");
        } else {
            TextView txt = (TextView) dialog.findViewById(R.id.id_tv_loadingmsg);
            txt.setText(message);
        }
        // 按返回键是否取消
        dialog.setCancelable(cancelable);
        // 监听返回键处理
        dialog.setOnCancelListener(cancelListener);
        // 设置居中
        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        // 设置背景层透明度
        lp.dimAmount = 0.2f;
        dialog.getWindow().setAttributes(lp);
        // dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialog.isShow = false;
        dialog.show();
        return dialog;
    }

    /**
     * 当窗口焦点改变时调用
     */
    public void onWindowFocusChanged(boolean hasFocus) {
        ImageView imageView = (ImageView) findViewById(R.id.loadingImageView);
        // 获取ImageView上的动画背景
        AnimationDrawable spinner = (AnimationDrawable) imageView.getBackground();
        // 开始动画
        spinner.start();
    }

    /**
     * 给Dialog设置提示信息
     *
     * @param message
     */
    public void setMessage(CharSequence message) {
        if (message != null && message.length() > 0) {
            TextView txt = (TextView) findViewById(R.id.id_tv_loadingmsg);
            if (txt != null) {
                txt.setText(message);
                txt.invalidate();
                findViewById(R.id.id_tv_loadingmsg).setVisibility(View.VISIBLE);
            } else {
                this.message = message.toString();
            }

        }
    }

    public void setProgressMsg(String progressMsg) {
        TextView txt = (TextView) findViewById(R.id.id_tv_loadingmsg);
        if (txt != null) {
            txt.setText(progressMsg);
        }
    }

    public void show() {
        if (isShow) {
            setTitle("");
            setContentView(R.layout.dialog_custom_progress);
            TextView txt = findViewById(R.id.id_tv_loadingmsg);
            txt.setText(message);
            // 按返回键是否取消
            setCancelable(true);
            // 设置居中
            getWindow().getAttributes().gravity = Gravity.CENTER;
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            // 设置背景层透明度
            lp.dimAmount = 0.0f;
            getWindow().setAttributes(lp);
            isShow = false;
        }
        super.show();
    }
}

