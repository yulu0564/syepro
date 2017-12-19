package com.syepro.app.utils;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 字符串处理工具类
 */
public class DataUtils {

    /**
     * 匹配是否为手机号
     */
    public static boolean isPhoneNUmber(String str) {
        String mobileNumber = str;
        // 去掉86，+86开头
        if (mobileNumber.startsWith("86")) {
            mobileNumber = mobileNumber.substring(2);
        }
        if (mobileNumber.startsWith("+86")) {
            mobileNumber = mobileNumber.substring(3);
        }
        // 清除空格
        mobileNumber.replace(" ", "");
        if (mobileNumber.length() == 11) {
            Pattern pattern = Pattern
                    .compile("^((13[0-9])|14[5,7]|(15[^4,\\D])|(17[0-9])|(18[0-9]))\\d{8}$");
            return pattern.matcher(str).matches();
        } else {
            return false;
        }
    }

    /**
     * 字符串转utf-8
     */
    public static String EncodeParams(String str) {
        if (android.text.TextUtils.isEmpty(str)) {
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

    public static String substring(String value, int beginIndex, int endIndex) {
        if (value.length() < beginIndex) {
            return "";
        }
        return value.substring(beginIndex, value.length() < endIndex ? value.length() : endIndex);
    }


}