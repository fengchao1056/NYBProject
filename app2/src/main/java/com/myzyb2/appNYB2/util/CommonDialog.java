package com.myzyb2.appNYB2.util;


import android.content.Context;
import android.view.View;

import com.myzyb2.appNYB2.ui.view.AlertDialog;
import com.myzyb2.appNYB2.ui.view.CustomProgressDialog;


/***
 * @ClassName: CommonDialog
 * @Description: 对话框工具类
 */
public class CommonDialog {
    private final static String TAG = "CommonDialog";


    /**
     * @param @param context
     * @return void
     * @throws
     * @Title: showInfoDialog
     * @Description: 信息提示框
     */
    public static void showInfoDialog(Context context, String message) {
        new AlertDialog(context).builder().setTitle("提示")
                .setMsg(message)
                .show();
    }


    public static void showInfoDialogFailure(Context context) {
        showInfoDialog(context, "网络连接失败");
    }

    public static void showInfoDialog(Context context, String message, String titleStr) {

        new AlertDialog(context).builder().setTitle(titleStr)
                .setMsg(message)
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .show();
    }

    public static void showInfoDialog(Context context, String message, String titleStr, String negativeStr, String positiveStr, View.OnClickListener onClickListener) {

        new AlertDialog(context).builder().setTitle(titleStr)
                .setMsg(message)
                .setPositiveButton(positiveStr, onClickListener)
                .setNegativeButton(negativeStr, onClickListener)
                .show();
    }


    public static CustomProgressDialog progressDialog;
    private static boolean mShowingDialog = false;

    /**
     * 启动一个Progressdialog
     */
    public static void showProgressDialog(Context context) {
        try {
            if (!mShowingDialog) {
                progressDialog = CustomProgressDialog.createDialog(context);
                progressDialog.setCanceledOnTouchOutside(false);
                // this.progressDialog.setTitle(getString(R.string.loadTitle));
                progressDialog.setMessage("加载中");
                // this.progressDialog.setMessage(getString(R.string.LoadContent));
                if (!progressDialog.isShowing())
                    progressDialog.show();

                mShowingDialog = true;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }


    /**
     * 关闭dialog
     */
    public static void closeProgressDialog() {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
                mShowingDialog = false;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
