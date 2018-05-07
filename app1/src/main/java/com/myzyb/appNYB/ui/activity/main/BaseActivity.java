package com.myzyb.appNYB.ui.activity.main;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.myzyb.appNYB.R;
import com.myzyb.appNYB.common.CommApplication;
import com.myzyb.appNYB.http.NetUtils;
import com.myzyb.appNYB.ui.view.CustomProgressDialog;
import com.myzyb.appNYB.util.ValidateUtil;


public class BaseActivity extends Activity {
    protected Context context;
    protected CommApplication application;
    protected NetUtils netUtils;
    public TextView txt_title;
    public ImageButton imgbtn_left;
    public ImageButton imgbtn_left2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        application = CommApplication.getInstance();
        application.addActvity(this);
        context = getApplicationContext();
        netUtils = NetUtils.newInstance();
    }

    //初始化头的方法
    public void initTitleBar() {
        imgbtn_left = (ImageButton) findViewById(R.id.imgbtn_left);
        imgbtn_left2 = (ImageButton) findViewById(R.id.imgbtn_left2);
        txt_title = (TextView) findViewById(R.id.txt_title);

    }
    //设置头文字

    public void setTitleBar_titletext(String str) {
        if (!TextUtils.isEmpty(str)) {
            txt_title.setText(str);
        }
    }

    //设置返回

    public void setTitleBar_back() {
        imgbtn_left.setVisibility(View.VISIBLE);
        imgbtn_left.setImageResource(R.drawable.back_button);
        imgbtn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public void setTitleBar_left_bn(int i, View.OnClickListener onClickListener) {


        if (!ValidateUtil.isEmpty(i)) {

            imgbtn_left.setVisibility(View.VISIBLE);
            imgbtn_left.setImageResource(i);
        }
        if (onClickListener != null) {
            imgbtn_left.setOnClickListener(onClickListener);
        }
    }

    public void setTitleBar_left_bn(int i) {


        if (!ValidateUtil.isEmpty(i)) {

            imgbtn_left.setVisibility(View.VISIBLE);
            imgbtn_left.setImageResource(i);
        }

    }

    public void setTitleBar_left_bn2(int i) {


        if (!ValidateUtil.isEmpty(i)) {

            imgbtn_left2.setVisibility(View.VISIBLE);
            imgbtn_left2.setImageResource(i);
        }

    }

    public static CustomProgressDialog progressDialog;
    private static boolean mShowingDialog = false;

    protected void showProgressDialog() {
        progressDialog = CustomProgressDialog.createDialog(BaseActivity.this);
        progressDialog.setCanceledOnTouchOutside(false);
        // this.progressDialog.setTitle(getString(R.string.loadTitle));
        progressDialog.setMessage("加载中");
        // this.progressDialog.setMessage(getString(R.string.LoadContent));
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    protected void showProgressDialog(String s) {
        progressDialog = CustomProgressDialog.createDialog(BaseActivity.this);
        progressDialog.setCanceledOnTouchOutside(false);
        // this.progressDialog.setTitle(getString(R.string.loadTitle));
        progressDialog.setMessage(s);
        // this.progressDialog.setMessage(getString(R.string.LoadContent));
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    /**
     * 关闭dialog
     */
    protected void closeProgressDialog() {
        if (this.progressDialog != null)
            this.progressDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        application.removeActvity(this);
        context = null;
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
