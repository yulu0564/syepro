package com.syepro.app.commonjar.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

/**
 * 权限管理
 */
public class PermissionsUtils {

    public static final int REQUEST_MUST = 1, REQUEST_WRITE_EXTERNAL_STORAGE = 2, REQUEST_READ_EXTERNAL_STORAGE = 3,
            REQUEST_CAMERA = 4, REQUEST_CALL_PHONE = 5, REQUEST_ACCESS_FINE_LOCATION = 6, REQUEST_ACCESS_WIFI_STATE = 7;
    private static final String READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;  //读SDCard
    private static final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE; // 往SDCard写入数据权限
    private static final String CAMERA = Manifest.permission.CAMERA;  //相机权限
    private static final String CALL_PHONE = Manifest.permission.CALL_PHONE;  //拨打电话
    private static final String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;  //允许一个程序访问精良位置(如GPS)
    private static final String ACCESS_WIFI_STATE = Manifest.permission.ACCESS_WIFI_STATE;  //允许程序访问Wi-Fi网络状态信息
    private static String[] PERMISSIONS_MUST = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };  //基础必须权限

    public enum PermissionsType {
        TYPE_WRITE_STORAGE, TYPE_READ_STORAGE, TYPE_CAMERA, TYPE_CALL_PHONE, TYPE_ACCESS_FINE_LOCATION, TYPE_ACCESS_WIFI_STATE
    }

    /**
     * 请求权限
     *
     * @param type 权限类型
     */
    public static void verifyPermissions(Activity activity, PermissionsType type) {
        if (!isPermissions(activity, type)) {
            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{getPermissionName(type)},
                    getQuest(type)
            );
        }
    }

    /**
     * 判断权限是否通过
     *
     * @param type 权限类型
     * @return
     */
    public static boolean isPermissions(Activity activity, PermissionsType type) {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        return lacksPermission(getPermissionName(type), activity);
    }


    /**
     * 返回权限名
     *
     * @param type 权限类型
     */
    public static String getPermissionName(PermissionsType type) {
        switch (type) {
            case TYPE_READ_STORAGE:
                return READ_EXTERNAL_STORAGE;
            case TYPE_WRITE_STORAGE:
                return WRITE_EXTERNAL_STORAGE;
            case TYPE_CAMERA:
                return CAMERA;
            case TYPE_CALL_PHONE:
                return CALL_PHONE;
            case TYPE_ACCESS_FINE_LOCATION:
                return ACCESS_FINE_LOCATION;
            case TYPE_ACCESS_WIFI_STATE:
                return ACCESS_WIFI_STATE;

        }
        return "";
    }

    /**
     * 返回权限标识
     *
     * @param type 权限类型
     * @return
     */
    public static int getQuest(PermissionsType type) {
        switch (type) {
            case TYPE_READ_STORAGE:
                return REQUEST_READ_EXTERNAL_STORAGE;
            case TYPE_WRITE_STORAGE:
                return REQUEST_WRITE_EXTERNAL_STORAGE;
            case TYPE_CAMERA:
                return REQUEST_CAMERA;
            case TYPE_CALL_PHONE:
                return REQUEST_CALL_PHONE;
            case TYPE_ACCESS_FINE_LOCATION:
                return REQUEST_ACCESS_FINE_LOCATION;
            case TYPE_ACCESS_WIFI_STATE:
                return REQUEST_ACCESS_WIFI_STATE;
        }
        return 0;
    }

    /**
     * 判断权限是否被静止请求
     *
     * @param mContext
     * @return
     */
    public static boolean isPermissionRationale(Activity mContext, PermissionsType type) {
        return ActivityCompat.shouldShowRequestPermissionRationale(mContext, getPermissionName(type));
    }


    /**
     * 判断是否缺少权限
     */
    private static boolean lacksPermission(String permission, Context mContext) {
        return ActivityCompat.checkSelfPermission(mContext, permission) ==
                PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 权限请求是否都通过
     */
    public static boolean isPermissionsResultSuccess(int[] grantResults) {
        for (int permission : grantResults) {
            if (permission != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 启动应用必须权限请求
     *
     * @param activity
     */
    public static void verifyMustPermissions(Activity activity) {
        if (!lacksPermissions(activity)) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_MUST,
                    REQUEST_MUST
            );
        }
    }

    /**
     * 判断所有必须权限
     *
     * @param mContext
     * @return
     */
    public static boolean lacksPermissions(Context mContext) {
        if (Build.VERSION.SDK_INT >= 23) {
            for (String permission : PERMISSIONS_MUST) {
                if (!lacksPermission(permission, mContext)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 判断权限是否被静止请求
     *
     * @param mContext
     * @return
     */
    public static boolean lacksPermissionRationale(Activity mContext) {
        for (String permission : PERMISSIONS_MUST) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(mContext, permission)) {
                return true;
            }
        }
        return false;
    }
}
