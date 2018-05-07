package com.myzyb.appNYB.ui.activity.login;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.myzyb.appNYB.R;
import com.myzyb.appNYB.ui.activity.main.BaseActivity;
import com.myzyb.appNYB.ui.activity.main.homeActivity;
import com.myzyb.appNYB.ui.activity.regiser.RegisterActivity;
import com.myzyb.appNYB.util.CheckUtils;
import com.myzyb.appNYB.util.ToastUtil;

public class NewLogin extends BaseActivity {
    private TextView phone;
    private TextView pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_login);
        phone = (TextView) findViewById(R.id.et_phone);
        pwd = (TextView)findViewById(R.id.et_pwd);
        addEvent();
    }


    //注册按钮点击
    private void addEvent(){
      Button regBtn = (Button) findViewById(R.id.register);
      regBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {

          }
      });


      //忘记密码按钮点击
      Button forgetBtn = (Button) findViewById(R.id.forget_pwd);
      forgetBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              RegisterActivity regist = new RegisterActivity();
          }
      });

      //登录按钮点击
      Button loginBtn = (Button) findViewById(R.id.login);
      loginBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              if(CheckUtils.isPhoneNumberValid(phone.getText().toString())){
                  if (CheckUtils.isPasswordValidNew(pwd.getText().toString())){
                      enterHome();
                  }else {
                      ToastUtil.showToast("请检查密码，密码（8-16）位数字和字母组合");
                  }
              }else {
                  ToastUtil.showToast("请检查手机号是否正确");
              }
          }
      });



      //协议按钮点击
      final Button xyBtn = (Button) findViewById(R.id.xieyi);
      xyBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
             xyBtn.setSelected(!xyBtn.isSelected());
             if (xyBtn.isSelected()){
                     xyBtn.setBackgroundResource(R.drawable.select);
             }else {
                    xyBtn.setBackgroundResource(R.drawable.noselect);
             }
          }
      });

      //登录协议html加载
      Button htmlBtn = (Button) findViewById(R.id.toAgree);

      htmlBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
               ;
          }
      });

    }

    private void enterHome() {
        Intent tent = new Intent(this,homeActivity.class);
        startActivity(tent);
        finish();
    }

    private void enterMain(){

    }
}
