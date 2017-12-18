package com.syepro.app.commonjar.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;
import java.util.Set;

/**
 * sharedPreferences 工具类
 */
public class SPUtil {

    private SPUtil() {
    }

    private SharedPreferences sp = null;

    private String name = null;
    private Context user = null;

    public SharedPreferences getShared(Context context, String spName) {
        if (name != null && user != null && (user != context || !(name.equals(spName)))) {
            sp = null;
        }
        if (sp == null) {
            sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        }
        return sp;
    }

    private static SPUtil instance = null;

    public static SPUtil getInstance() {
        if (instance == null) {
            instance = new SPUtil();
        }
        return instance;
    }

    public void save(Map<String, String> params) {
        Set<Map.Entry<String, String>> set = params.entrySet();
        SharedPreferences.Editor editor = sp.edit();
        for (Map.Entry entry : set) {
            editor.putString(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
        }
        editor.commit();
    }

    public void save(String key, String value) {
        sp.edit().putString(key, value).commit();
    }

    public String get(String key) {
        return sp.getString(key, null);
    }

}
