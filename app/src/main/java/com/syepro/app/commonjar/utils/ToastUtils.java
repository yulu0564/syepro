package com.syepro.app.commonjar.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.syepro.app.base.BaseApplication;

/**
 * 吐司工具类
 */
public class ToastUtils {

    private static String URL_REG;

    static {
        URL_REG = "(http(s)?://)([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&#=]*)?";
    }

    public static void showToast(String msg) {
        showToast(BaseApplication.getInstance(), msg);
    }

    public static void showToast(Context ctx, String message) {
        if (TextUtils.isEmpty(message)) {
            message = "请求网络失败";
        }
        Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(String content, int gravity) {
        Toast toast = Toast.makeText(BaseApplication.getInstance(), content, Toast.LENGTH_SHORT);
        toast.setGravity(gravity, 0, 0);
        toast.show();
        showToast(content);
    }

    public static void showToast(int resId) {
        Toast.makeText(BaseApplication.getInstance(), resId, Toast.LENGTH_SHORT).show();

    }
}
