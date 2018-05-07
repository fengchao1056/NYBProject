package com.myzyb.appNYB.util;


import android.widget.Toast;

import com.myzyb.appNYB.R;
import com.myzyb.appNYB.common.CommApplication;

public class ToastUtil {
    private static Toast toast;

    /**
     * 强大的吐司，可以连续弹，不会等上一个消失
     *
     * 服务器返回数据失败
     */
    public static void toastOnFailure() {
        showToast(CommApplication.getContext().getResources().getString(R.string.onfailure));

    }
    /**
     * 强大的吐司，可以连续弹，不会等上一个消失
     *
     * @param text
     */
    public static void showToast(final String text) {
        CommonUtil.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                //保证在主线程弹吐司
                if (toast == null) {
                    toast = Toast.makeText(CommApplication.getContext(), text, Toast.LENGTH_LONG);
                }
                toast.setText(text);
                toast.show();
            }
        });


    }
}
