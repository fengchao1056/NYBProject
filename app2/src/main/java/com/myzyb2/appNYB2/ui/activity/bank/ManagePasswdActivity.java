package com.myzyb2.appNYB2.ui.activity.bank;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;


import com.myzyb2.appNYB2.R;
import com.myzyb2.appNYB2.ui.activity.main.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ManagePasswdActivity extends BaseActivity {

    @Bind(R.id.ll_replace)
    LinearLayout llReplace;
    @Bind(R.id.ll_forget)
    LinearLayout llForget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_passwd);
        ButterKnife.bind(this);
        initTitleBar();
        setTitleBar_back();
        setTitleBar_titletext("管理支付密码");
        llReplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManagePasswdActivity.this, ReplacePayActivity.class));
            }
        });
        llForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManagePasswdActivity.this, ForgetPayActivity.class));
            }
        });
    }


}
