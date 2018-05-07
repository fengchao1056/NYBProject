package com.myzyb.appNYB.util;

import android.util.Log;

import com.myzyb.appNYB.BuildConfig;

/**
 * Log管理类
 *
 * @author Administrator
 */
public class LogUtil {
    private static boolean isDebug = true;//需要开发完毕后，上传市场前，置为false

    /**
     * 打印d级别的log
     *
     * @param tag
     * @param msg
     */
    public static void d(String tag, String msg) {
        if (isDebug) {
            Log.d(tag, msg);
        }
    }

    /**
     * 方便打log
     *
     * @param object
     * @param msg
     */
    public static void d(Object object, String msg) {
        if (isDebug) {
            Log.d(object.getClass().getSimpleName(), msg);
        }
    }

    /**
     * 打印e级别的log
     *
     * @param tag
     * @param msg
     */
    public static void e(String tag, String msg) {
        if (isDebug) {
            Log.e(tag, msg);
        }
    }

    /**
     * 方便打log
     *
     * @param object
     * @param msg
     */
    public static void e(Object object, String msg) {
        if (isDebug) {
            Log.e(object.getClass().getSimpleName(), msg);
        }
    }
}
