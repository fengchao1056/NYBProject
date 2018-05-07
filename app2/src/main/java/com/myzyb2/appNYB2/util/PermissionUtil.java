package com.myzyb2.appNYB2.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by alioth on 2016/10/23.
 * API Level>=23时权限检查与申请工具
 */

public class PermissionUtil {
    /**
     * 检查权限
     *
     * @param context
     * @param permissType
     * @return
     */
    public static boolean checkPermission(Context context, String permissType) {
        int permissionResult = ContextCompat.checkSelfPermission(context, permissType);
        if (permissionResult != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }


    /**
     * 请求权限
     *
     * @param thisActivity
     * @param requestCode
     */
    public static void requestPermission(Activity thisActivity, int requestCode, String[] permissionType) {
        ActivityCompat.requestPermissions(thisActivity, permissionType,requestCode);

    }

    /**
     * 检查写入权限
     *
     * @param context
     * @return
     */
    public static boolean checkWritePermission(Context context) {
        return checkPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    /**
     * 请求写入权限
     *
     * @param thisActivity
     * @param requestCode
     */
    public static void requestWritePermission(Activity thisActivity, int requestCode) {
        requestPermission(thisActivity, requestCode, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE});
    }


    /**
     * 检查拍照权限
     *
     * @param context
     * @return
     */
    public static boolean checkCameraPermission(Context context) {
        return checkPermission(context, Manifest.permission.CAMERA);
    }

    /**
     * 请求拍照权限
     *
     * @param thisActivity
     * @param requestCode
     */
    public static void requestCameraPermission(Activity thisActivity, int requestCode) {
        requestPermission(thisActivity, requestCode, new String[]{Manifest.permission.CAMERA});
    }

    /**
     * 检查文档权限
     *
     * @param context
     * @return
     */
    public static boolean checkDocPermission(Context context) {
        return checkPermission(context, Manifest.permission.MANAGE_DOCUMENTS);
    }

    /**
     * 请求文档权限
     *
     * @param thisActivity
     * @param requestCode
     */
    public static void requestDocPermission(Activity thisActivity, int requestCode) {
        requestPermission(thisActivity, requestCode, new String[]{Manifest.permission.MANAGE_DOCUMENTS});
    }

    /**
     * 检查文档权限
     *
     * @param context
     * @return
     */
    public static boolean checkGpsPermission(Context context) {
        return checkPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
    }

    /**
     * 请求文档权限
     *
     * @param thisActivity
     * @param requestCode
     */
    public static void requestGpsPermission(Activity thisActivity, int requestCode) {
        requestPermission(thisActivity, requestCode, new String[]{Manifest.permission.ACCESS_FINE_LOCATION});
    }


    public static boolean checkPhoneStatusPermission(Context context) {
        return checkPermission(context, Manifest.permission.READ_PHONE_STATE);
    }

    public static void requestPhoneStatusPermission(Activity thisActivity, int requestCode) {
        requestPermission(thisActivity, requestCode, new String[]{Manifest.permission.READ_PHONE_STATE});

    }
}
