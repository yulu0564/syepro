package com.syepro.app.base.skip;

import android.app.Activity;
import android.support.v4.app.ActivityOptionsCompat;

import java.io.Serializable;

/**
 * 跳转方法
 */
public interface SkipView {
    void skip(Class<? extends Activity> cls, ActivityOptionsCompat options, Serializable... serializ);

    void skipForResult(Class clasz, ActivityOptionsCompat options, int requestCode, Serializable... serializ);

    void skipForResult(Class clasz, int requestCode, Serializable... serializ);

    void skip(Class<? extends Activity> cls);

    void skip(String action);

    void skip(String action, Serializable... serializ);

    void skip(Class<? extends Activity> cls, Serializable... serializ);
}
