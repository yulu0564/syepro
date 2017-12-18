package com.syepro.app.base.skip;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;

import java.io.Serializable;

/**
 * 跳转控制
 */
public class Skip {
    public static void skip(Activity mActivity, Class<? extends Activity> cls, ActivityOptionsCompat options, Serializable... serializ) {
        Intent intent = new Intent(mActivity, cls);
        Bundle extras = new Bundle();
        for (int i = 0; i < serializ.length; i++) {
            Serializable s = serializ[i];
            // 放对象的规则，以顺序为键
            extras.putSerializable(i + "", s);
        }
        intent.putExtras(extras);
        ActivityCompat.startActivity(mActivity, intent, options.toBundle());
    }

    public static void skipForResult(Activity mActivity, Class clasz, ActivityOptionsCompat options, int requestCode, Serializable... serializ) {
        Intent intent = new Intent(mActivity, clasz);
        Bundle extras = new Bundle();
        for (int i = 0; i < serializ.length; i++) {
            Serializable s = serializ[i];
            // 放对象的规则，以顺序为键
            extras.putSerializable(i + "", s);
        }
        intent.putExtras(extras);
        ActivityCompat.startActivityForResult(mActivity, intent, requestCode, options.toBundle());
    }

    public static void skipForResult(Activity mActivity, Class clasz, int requestCode, Serializable... serializ) {
        Intent intent = new Intent(mActivity, clasz);
        Bundle extras = new Bundle();
        for (int i = 0; i < serializ.length; i++) {
            Serializable s = serializ[i];
            // 放对象的规则，以顺序为键
            extras.putSerializable(i + "", s);
        }
        intent.putExtras(extras);
        mActivity.startActivityForResult(intent, requestCode);
    }

    public static void skip(Context mContext, Class<? extends Activity> cls) {
        mContext.startActivity(new Intent(mContext, cls));
    }

    public static void skip(Context mContext, String action) {
        mContext.startActivity(new Intent(action));
    }

    public static void skip(Context mContext, String action, Serializable... serializ) {
        Intent intent = new Intent(action);
        Bundle extras = new Bundle();
        for (int i = 0; i < serializ.length; i++) {
            Serializable s = serializ[i];
            // 放对象的规则，以顺序为键
            extras.putSerializable(i + "", s);
        }
        intent.putExtras(extras);
        mContext.startActivity(intent);
    }

    public static void skip(Context mContext, Class<? extends Activity> cls, Serializable... serializ) {
        Intent intent = new Intent(mContext, cls);
        Bundle extras = new Bundle();
        for (int i = 0; i < serializ.length; i++) {
            Serializable s = serializ[i];
            // 放对象的规则，以顺序为键
            extras.putSerializable(i + "", s);
        }
        intent.putExtras(extras);
        mContext.startActivity(intent);
    }
}
