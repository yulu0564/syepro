package com.syepro.app.commonjar.utils;

import android.text.TextUtils;

import java.text.DecimalFormat;
import java.util.regex.Pattern;

/**
 * 数字处理工具类
 */
public class NumberUtils {
    public static final String DEFAULT_STRING = "- -";
    /**
     * 用正则表达式判断字符串是否为数字
     */
    public static boolean isNumeric(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 保留两位有效数字
     */
    public static String getTwoStep(double vaule) {
        try {
            DecimalFormat df = new DecimalFormat("###########0.00");
            return df.format(Math.round(vaule * 100) / 100.0);
        } catch (Exception e) {
        }
        return Math.round(vaule * 100) / 100.00f + "";
    }

    /**
     * 保留两位有效数字,如果后面为0，则保留整数
     */
    public static String getTwoStepAndInt(double vaule) {
        try {
            if (vaule == (int) vaule) {
                return ((int) vaule) + "";
            } else {
                DecimalFormat df = new DecimalFormat("###########0.00");
                return df.format(Math.round(vaule * 100) / 100.0);
            }
        } catch (Exception e) {
        }
        return Math.round(vaule * 100) / 100.00f + "";
    }


    public static String getTwoStepStr(String vaule) {
        try {
            float vauleF = Float.parseFloat(vaule);
            String unit = "";
            if (Math.abs(vauleF) > 100000000) {
                unit = "亿";
                vauleF /= 100000000;
            } else if (Math.abs(vauleF) > 10000) {
                unit = "万";
                vauleF /= 10000;
            }
            DecimalFormat df = new DecimalFormat("###########0.00");
            return df.format(Math.round(vauleF * 100) / 100.0) + unit;
        } catch (Exception e) {
        }
        return DEFAULT_STRING;
    }

    public static String getTwoStep(String vaule, String exStr) {
        try {
            float vauleF = Float.parseFloat(vaule);
            DecimalFormat df = new DecimalFormat("###########0.00");
            return df.format(Math.round(vauleF * 100) / 100.0) + exStr;
        } catch (Exception e) {
        }
        return DEFAULT_STRING;
    }

    public static String getTwoStepStrAnd(String vaule) {
        return getTwoStepStrAnd(vaule,"%s");
    }

    public static String getTwoStepStrAnd(String vaule, String unitWhole) {
        try {
            float vauleF = Float.parseFloat(vaule);
            String unit = "";
            if (Math.abs(vauleF) > 100000000) {
                unit = "亿";
                vauleF /= 100000000;
            } else if (Math.abs(vauleF) > 10000) {
                unit = "万";
                vauleF /= 10000;
            }
            StringBuffer vauleR = new StringBuffer();
            if (vauleF == (int) vauleF) {
                vauleR.append(vauleF);
            } else {
                DecimalFormat df = new DecimalFormat("###########0.00");
                vauleR.append(df.format(Math.round(vauleF * 100) / 100.0));
            }
            vauleR.append(String.format(unitWhole, unit));
            return vauleR.toString();
        } catch (Exception e) {
        }
        return DEFAULT_STRING;
    }

    /**
     * 取整
     *
     * @param vaule
     * @return
     */
    public static String getIntStep(double vaule) {
        try {
            DecimalFormat df = new DecimalFormat("###########0");
            return df.format(Math.round(vaule * 100) / 100.0);
        } catch (Exception e) {
        }
        return Math.round(vaule) + "";
    }

    /**
     * 字符串转int类型,
     *
     * @param vaule
     * @return
     */
    public static int parseInt(String vaule,int defaultValue) {
        try {
            if (TextUtils.isEmpty(vaule)) {
                return defaultValue;
            }
            return Integer.parseInt(vaule);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static long parseLong(String vaule,long defaultValue) {
        try {
            if (TextUtils.isEmpty(vaule)) {
                return defaultValue;
            }
            return Long.parseLong(vaule);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 字符串转float类型
     *
     * @param vaule
     * @return
     */
    public static float parseFloat(String vaule,float defaultValue) {
        try {
            if (TextUtils.isEmpty(vaule)) {
                return defaultValue;
            }
            return Float.parseFloat(vaule);
        } catch (Exception e) {
            return defaultValue;
        }
    }


    /**
     * 字符串转double类型
     *
     * @param vaule
     * @return
     */
    public static double parseDouble(String vaule,double defaultValue) {
        try {
            if (TextUtils.isEmpty(vaule)) {
                return defaultValue;
            }
            return Double.parseDouble(vaule);
        } catch (Exception e) {
            return defaultValue;
        }
    }

}
