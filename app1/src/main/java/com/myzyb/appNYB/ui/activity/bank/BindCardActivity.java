package com.myzyb.appNYB.ui.activity.bank;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.myzyb.appNYB.R;
import com.myzyb.appNYB.common.Constant;
import com.myzyb.appNYB.http.AES;
import com.myzyb.appNYB.http.NetUtils;
import com.myzyb.appNYB.http.UrlConstant;
import com.myzyb.appNYB.ui.activity.login.LoginActivity;
import com.myzyb.appNYB.ui.activity.main.BaseActivity;
import com.myzyb.appNYB.ui.activity.main.MainActivity;
import com.myzyb.appNYB.ui.view.ClearEditText;
import com.myzyb.appNYB.util.CommonDialog;
import com.myzyb.appNYB.util.CommonUtil;
import com.myzyb.appNYB.util.LogUtil;
import com.myzyb.appNYB.util.SharedPreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class BindCardActivity extends BaseActivity {
    @Bind(R.id.et_name)
    EditText etName;
    @Bind(R.id.tv_choose_bank)
    TextView tvChooseBank;
    @Bind(R.id.et_bank_num)
    ClearEditText etBankNum;
    @Bind(R.id.et_phone)
    ClearEditText etPhone;
    @Bind(R.id.bt_submit)
    Button btSubmit;
    boolean isName = false;
    boolean isChooseBank = false;
    boolean isBankNum = false;
    boolean isPhone = false;
    boolean ispeoplenum = false;
    @Bind(R.id.et_peoplenum)
    EditText etPeoplenum;
    private int mPosition;
    private Integer[] strs = new Integer[]{102, 103, 104, 105, 302, 303, 403, 308, 309};
    private String bank_name;
    private String bank_id;
    private Integer bc_area;
    private String phone;
    private String identity;
    private String dotText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bind_card);
        ButterKnife.bind(this);
        context = this;
        initTitleBar();
        setTitleBar_back();
        setTitleBar_titletext("绑定银行卡");
        initView();
        initData();

    }

    private void initData() {


        String identity_cord = SharedPreferenceUtil.getString(context, Constant.identity_cord, "");
        String Bank_name = SharedPreferenceUtil.getString(context, Constant.Bank_name, "");
        if (Bank_name != null && !"".equals(Bank_name)) {
            etName.setText(Bank_name);
            etName.setEnabled(false);
            etName.setTextColor(Color.parseColor("#646464"));
        }
        if (identity_cord != null && !"".equals(identity_cord)) {
            etPeoplenum.setText(identity_cord);
            etPeoplenum.setEnabled(false);
            etPeoplenum.setTextColor(Color.parseColor("#646464"));
        }
    }


    private void initView() {

        //银行卡号4位分割
        etBankNum.showType = true;
        //手机号分割
        etPhone.showType = true;
        etPhone.showMobileType = true;

        etName.addTextChangedListener(new MyTextWatcher(etName));
        tvChooseBank.addTextChangedListener(new MyTextWatcher(tvChooseBank));
        etBankNum.addTextChangedListener(new MyTextWatcher(etBankNum));
        etPhone.addTextChangedListener(new MyTextWatcher(etPhone));
        etPeoplenum.addTextChangedListener(new MyTextWatcher(etPeoplenum));
        tvChooseBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BindCardActivity.this, ChooseBankActivity.class);
                intent.putExtra("mPosition", mPosition);
                LogUtil.e("click", mPosition + "");
                startActivityForResult(intent, 2);

            }
        });

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonDialog.showProgressDialog(context);
                sendDataToServe();

            }
        });
    }

    private void sendDataToServe() {
//        bank_name	必须		开卡人名称
//        bank_id	必须		卡号
//        identity	必须		身份证号
//        bank_code	必须		所属银行
//        phone	必须		手机号
        String token = SharedPreferenceUtil.getString(context, Constant.TOKEN, "");
        String uid = SharedPreferenceUtil.getString(context, Constant.YHID, "");
        bank_name = etName.getText().toString();
        bank_id = etBankNum.getText().toString().trim().replace(" ", ""); //去空格
        bc_area = strs[mPosition];
        phone = etPhone.getText().toString().trim().replace(" ", "");
        identity = etPeoplenum.getText().toString().trim().replace(" ", "").replace("x", "X");
        HashMap<String, String> dictParam = new HashMap<String, String>();
        dictParam.put("uid", uid);
        dictParam.put("bank_name", NetUtils.getEncode(bank_name));
        dictParam.put("bank_id", bank_id);
        dictParam.put("bank_code", String.valueOf(bc_area));
        dictParam.put("bc_area", NetUtils.getEncode(dotText));
        dictParam.put("phone", phone);
        dictParam.put("identity", identity);
        dictParam.put("access_token", NetUtils.getEncode(token));
        RequestParams params = new RequestParams();
        params.add("uid", uid);
        params.add("bank_name", bank_name);
        params.add("bank_id", bank_id);
        params.add("bank_code", String.valueOf(bc_area));//所属银行代号
        params.add("bc_area", dotText);//所属银行名称
        params.add("phone", phone);
        params.add("identity", identity);//身份证号
        params.add("access_token", token);
        LogUtil.e("bacord", params + "");

        netUtils.putReturnJson(context, NetUtils.POST, UrlConstant.Bank_card, params, dictParam, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    CommonDialog.closeProgressDialog();
                    LogUtil.e("response", response.toString());
                    if(response.has("data")){
                        String s = response.getString("data");
                        response = AES.desEncrypt(s);
                    }else{
                        response = response.getJSONObject("result");
                    }
                    LogUtil.e("response", response.toString());
                    String status = response.getString("status");
                    if("40013".equals(status)){
                        //activity跳转
                        Intent intent = new Intent(BindCardActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }else if ("0000".equals(status)) {
                        String list = response.getString("list");
                        switch (list) {
                            //1有支付密码，0无支付密码
                            case "0":
                                //设置密码
                                CommonUtil.StartToast(context, response.getString("message"));
                                Intent intent = new Intent(BindCardActivity.this, SetBankPaswd.class);
                                intent.putExtra("bank_name", bank_name);
                                intent.putExtra("bank_id", bank_id);
                                intent.putExtra("dotText", dotText);
                                intent.putExtra("phone", phone);
                                intent.putExtra("identity", identity);
                                intent.putExtra("bc_area", String.valueOf(bc_area));
                                startActivity(intent);
                                break;
                            case "1":
                                //到个人中心
                                CommonUtil.StartToast(context, response.getString("message"));
                                Intent intent1 = new Intent(BindCardActivity.this, MainActivity.class);
                                intent1.putExtra("id", 2);
                                startActivity(intent1);
                                break;
                            default:
                                CommonUtil.StartToast(context, response.getString("message"));
                                break;

                        }

                    } else {
                        CommonUtil.StartToast(context, response.getString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                CommonUtil.StartToast(context, "连接失败" + statusCode);
                CommonDialog.closeProgressDialog();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 2:
                // TODO
                if (resultCode == Constant.RESULT_OK) {
                    dotText = data.getStringExtra("dotText");
                    mPosition = data.getIntExtra("position", 0);
                    tvChooseBank.setText(dotText);
                    tvChooseBank.setTextColor(getResources().getColor(R.color.text_323232));
                    LogUtil.e("mes", mPosition + "");
                }
                break;
        }
    }


    class MyTextWatcher implements TextWatcher {
        View view;

        public MyTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String s1 = s.toString();
            ((TextView) view).setTextColor(Color.parseColor("#000000"));
            switch (view.getId()) {
                case R.id.et_name:
                    if (!TextUtils.isEmpty(s1)) {
                        isName = true;

                    } else {
                        isName = false;
                    }
                    break;
                case R.id.tv_choose_bank:
                    if (!TextUtils.isEmpty(s1)) {
                        isChooseBank = true;

                    } else {
                        isChooseBank = false;
                    }

                    break;
                case R.id.et_bank_num:
                    if (!TextUtils.isEmpty(s1)) {
                        isBankNum = true;

                    } else {
                        isBankNum = false;
                    }

                    break;
                case R.id.et_phone:
                    if (!TextUtils.isEmpty(s1)) {
                        isPhone = true;

                    } else {
                        isPhone = false;
                    }

                    break;
                case R.id.et_peoplenum:
                    if (!TextUtils.isEmpty(s1)) {
                        ispeoplenum = true;

                    } else {
                        ispeoplenum = false;
                    }

                    break;
            }

            if (isName && isChooseBank && isBankNum && isPhone && ispeoplenum) {

                btSubmit.setEnabled(true);

            } else {
                btSubmit.setEnabled(false);
            }
        }
    }

}
