package com.myzyb2.appNYB2.util;

/**
 * Created by cuiz on 2015/12/29.
 */
public class ClickUtil {

    private static long lastClickTime;

    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 2000) {
            return true;
        }
        lastClickTime = time;
        return false;

    }
}
