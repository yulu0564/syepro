package com.syepro.app.commonjar.utils;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 字符串处理工具类
 * 2017/1/17.
 */
public class StringUtils {
    /**
     * 判断字符串是否为空
     *
     * @param checkStr 被验证的字符串
     * @return boolean 如果为空,返回true,否则,返回false
     */
    public static boolean isNull(String checkStr) {
        return TextUtils.isEmpty(checkStr);
    }

    /**
     * 字符串转Encode utf-8
     */
    public static String EncodeParams(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        try {
            return java.net.URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 字符串以逗号隔开
     *
     * @param stringList
     * @param <T>
     * @return
     */
    public static <T> String listToString(List<T> stringList) {
        if (stringList == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        boolean flag = false;
        for (T string : stringList) {
            if (flag) {
                result.append(",");
            } else {
                flag = true;
            }
            result.append(string);
        }
        return result.toString();
    }

    /**
     * @param value      内容
     * @param beginIndex 起始位置
     * @param endIndex   结束位置
     * @return 指定位置字符串
     */
    public static String substring(String value, int beginIndex, int endIndex) {
        if (value.length() < beginIndex) {
            return "";
        }
        return value.substring(beginIndex, value.length() < endIndex ? value.length() : endIndex);
    }
}
