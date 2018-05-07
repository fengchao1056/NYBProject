package com.myzyb.appNYB.ui.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.ion.Ion;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.myzyb.appNYB.R;
import com.myzyb.appNYB.common.CommApplication;
import com.myzyb.appNYB.common.Constant;
import com.myzyb.appNYB.http.AES;
import com.myzyb.appNYB.http.Config;
import com.myzyb.appNYB.http.NetUtils;
import com.myzyb.appNYB.http.UrlConstant;
import com.myzyb.appNYB.javabean.PersonalCenter;
import com.myzyb.appNYB.ui.activity.additive.IntegralExplainActivity;
import com.myzyb.appNYB.ui.activity.bank.BindCardActivity;
import com.myzyb.appNYB.ui.activity.bank.ManagePasswdActivity;
import com.myzyb.appNYB.ui.activity.bank.UnBindCardActivity;
import com.myzyb.appNYB.ui.activity.login.LoginActivity;
import com.myzyb.appNYB.ui.activity.main.MainActivity;
import com.myzyb.appNYB.ui.activity.pay.RechargeAndGetActivity;
import com.myzyb.appNYB.ui.activity.my.ReplaceAdressActivity;
import com.myzyb.appNYB.ui.activity.my.ReplacePwActivity;
import com.myzyb.appNYB.ui.activity.my.SpecificationActivity;
import com.myzyb.appNYB.ui.activity.prepose.RegisterAndLoginActivity;
import com.myzyb.appNYB.ui.view.ActionSheetDialog;
import com.myzyb.appNYB.ui.view.CircleImageView;
import com.myzyb.appNYB.util.CommonDialog;
import com.myzyb.appNYB.util.CommonUtil;
import com.myzyb.appNYB.util.JsonUtil;
import com.myzyb.appNYB.util.LogUtil;
import com.myzyb.appNYB.util.SharedPreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

import static com.myzyb.appNYB.ui.view.ActionSheetDialog.OnSheetItemClickListener;
import static com.myzyb.appNYB.ui.view.ActionSheetDialog.SheetItemColor;

/**
 *个人中心
 */

public class MyFragment extends BaseFragment implements View.OnClickListener {


    @Bind(R.id.rl_replacepw_my)
    protected RelativeLayout rl_replacepw;
    @Bind(R.id.rl_adress)
    protected RelativeLayout rl_adress;
    @Bind(R.id.rl_money)
    protected RelativeLayout rl_money;
    @Bind(R.id.rl_outlogin)
    protected RelativeLayout rl_outlogin;
    @Bind(R.id.rl_bankcrad)
    protected RelativeLayout rl_bankcrad;
    @Bind(R.id.bt_addandget_my)
    protected Button bt_addandget;
    @Bind(R.id.tv_name_my)
    protected TextView tvNameMy;
    @Bind(R.id.tv_phone_my)
    protected TextView tvPhoneMy;
    @Bind(R.id.tv_uidnum_my)
    protected TextView tvUidnumMy;
    @Bind(R.id.tv_weightnum_my)
    protected TextView tvWeightnumMy;

    @Bind(R.id.tv_fen_my)
    protected TextView tvFenMy;
    @Bind(R.id.tv_adress_my)
    protected TextView tvAdressMy;
    @Bind(R.id.tv_bankcrad_my)
    protected TextView tvBankcradMy;
    @Bind(R.id.tv_moneynum_my)
    protected TextView tvMoneynumMy;
    @Bind(R.id.tv_fentext_my)
    TextView tv_fentext_my;
    @Bind(R.id.iv_bankcrad_my)
    ImageView iv_bankcrad_my;
    @Bind(R.id.iv_imagehead)
    CircleImageView iv_imagehead;
    @Bind(R.id.tv_replace_my)
    TextView tvReplaceMy;
    private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    @Bind(R.id.tv_bank_stuts)
    TextView tvBankStuts;
    @Bind(R.id.iv_plus_my)
    ImageView ivPlusMy;
    @Bind(R.id.ll_paypasswd)
    LinearLayout llPaypasswd;

    private Bitmap bitmap;
    /* 头像名称 */
    private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
    private File tempFile;
    private PersonalCenter personalCenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, null);
        ButterKnife.bind(this, view);
        TextView txt_title = (TextView) view.findViewById(R.id.txt_title);
        txt_title.setText("个人中心");
        initDate();
        return view;
    }


    private void initDate() {
        rl_replacepw.setOnClickListener(this);
        rl_adress.setOnClickListener(this);
        rl_money.setOnClickListener(this);
        bt_addandget.setOnClickListener(this);
        rl_outlogin.setOnClickListener(this);
        iv_imagehead.setOnClickListener(this);
        tv_fentext_my.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //积分说明
                startActivity(new Intent(getActivity(), IntegralExplainActivity.class));
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_replacepw_my:
                startActivity(new Intent(context, ReplacePwActivity.class));
                break;
            case R.id.rl_adress:
                startActivity(new Intent(context, ReplaceAdressActivity.class));
                break;
            case R.id.rl_money:
                //明细列表
                startActivity(new Intent(context, SpecificationActivity.class));
                break;
            case R.id.rl_outlogin:
                CommonDialog.showInfoDialog(context, "您确定要退出登录吗?", "退出提示", "取消", "确认退出", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.btn_pos:
                                startActivity(new Intent(context, RegisterAndLoginActivity.class));
                                ((MainActivity) context).finish();
                                break;
                        }

                    }
                });
                break;
            case R.id.bt_addandget_my:
                startActivity(new Intent(context, RechargeAndGetActivity.class));
                break;

            case R.id.iv_imagehead:
//
                new ActionSheetDialog(context)
                        .builder()
                        .setCancelable(false)
                        .setCanceledOnTouchOutside(false)
                        .addSheetItem("拍照上传", SheetItemColor.Blue,
                                new OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        camera();
                                    }
                                })
                        .addSheetItem("从相册上传", SheetItemColor.Blue,
                                new OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        gallery();
                                    }


                                }).show();


                break;


        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        // TODO Auto-generated method stub
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getdataFromrevice();
        }
    }

    private void getdataFromrevice() {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//}, 2000);
        getdata();
    }

    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        getdataFromrevice();

    }

    private void getdata() {
        CommonDialog.showProgressDialog(context);
        String token = SharedPreferenceUtil.getString(context, Constant.TOKEN, "");
        String mobile = SharedPreferenceUtil.getString(context, Constant.MOBLIE, "-1");
        String uid = SharedPreferenceUtil.getString(context, Constant.YHID, "-1");
        HashMap<String, String> dictParam = new HashMap<String, String>();
        dictParam.put("mobile", mobile);
        dictParam.put("uid", uid);
        dictParam.put("access_token", NetUtils.getEncode(token));
        RequestParams params = new RequestParams();
        params.add("mobile", mobile);
        params.add("uid", uid);
        params.add("access_token", token);
        NetUtils.newInstance().putReturnJson(context, NetUtils.POST, UrlConstant.SELLERURL, params, dictParam, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    if(response.has("data")){
                        String s = response.getString("data");
                        response = AES.desEncrypt(s);
                    }else{
                        response = response.getJSONObject("result");
                    }
                    if("40013".equals(response.getString("status"))){
                        //activity跳转
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    } else if ("1001".equals(response.getString("status"))) {
                        LogUtil.e("response", response.toString());
                        String list = response.getString("list");

                        if (!TextUtils.isEmpty(list) && !"null".equals(list)) {
                            personalCenter = JsonUtil.getSingleBean(list, PersonalCenter.class);
                            //开户人姓名
                            SharedPreferenceUtil.saveString(context, Constant.Bank_name, personalCenter.bc_name);
                            //开户人身份证号
                            SharedPreferenceUtil.saveString(context, Constant.identity_cord, personalCenter.identity);
                            if (personalCenter.bc_ids != null) {
                                SharedPreferenceUtil.saveString(context, Constant.Bank_card, personalCenter.bc_ids);
                                SharedPreferenceUtil.saveString(context, Constant.Bank_zd, personalCenter.bc_id);
                            } else {
                                SharedPreferenceUtil.saveString(context, Constant.Bank_card, "");
                                SharedPreferenceUtil.saveString(context, Constant.Bank_zd, "");
                            }
                            setUIData(personalCenter.img_url);
                        }
                        CommonDialog.closeProgressDialog();
                    } else {
                        CommonUtil.StartToast(context, response.getString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void setUIData(String img_url) throws MalformedURLException {
        String mobile = SharedPreferenceUtil.getString(context, Constant.MOBLIE, "");
        if (tvNameMy != null && personalCenter != null) {

            if (bitmap == null) {
                Ion.with(context).load(img_url).withBitmap()
                        .placeholder(R.drawable.my_tou)
                        .error(R.drawable.my_tou).intoImageView(iv_imagehead);
            } else {
                this.iv_imagehead.setImageBitmap(bitmap);
            }
            tvNameMy.setText(personalCenter.uname);
            tvPhoneMy.setText(mobile);
            tvUidnumMy.setText(personalCenter.ext_id);
            tvWeightnumMy.setText(String.valueOf(personalCenter.weight_total));
            tvMoneynumMy.setText(personalCenter.money);
            tvFenMy.setText(personalCenter.integral);
            tvAdressMy.setText(new StringBuffer().append(personalCenter.pros).append(personalCenter.citys).append(personalCenter.towns).append(personalCenter.address));

            //银行卡相关
            final String bc_id = personalCenter.bc_id;
            if (bc_id == null || TextUtils.isEmpty(bc_id) || bc_id.length() == 0 || "null".equals(bc_id)) {
                tvBankStuts.setText("未绑定银行卡");
                iv_bankcrad_my.setEnabled(false);
                tvReplaceMy.setVisibility(View.GONE);
                ivPlusMy.setVisibility(View.VISIBLE);
                rl_bankcrad.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getActivity(), BindCardActivity.class));

                    }
                });

            } else {
                //已绑定银行卡
                tvBankStuts.setText("已绑定银行卡");
                tvBankcradMy.setText(bc_id + "");
                iv_bankcrad_my.setEnabled(true);
                tvReplaceMy.setVisibility(View.VISIBLE);
                ivPlusMy.setVisibility(View.GONE);
                llPaypasswd.setVisibility(View.VISIBLE);
                llPaypasswd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getActivity(), ManagePasswdActivity.class));
                    }
                });
                rl_bankcrad.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), UnBindCardActivity.class);
                        intent.putExtra("bc_id", bc_id);
                        startActivity(intent);
                    }
                });


            }
        }

    }
    //上传头像

    public void upload() {
        try {
            String token = SharedPreferenceUtil.getString(context, Constant.TOKEN, "");
            File file = new File(Environment.getExternalStorageDirectory() + "/" + PHOTO_FILE_NAME);
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            RequestParams params = new RequestParams();
            String gid = SharedPreferenceUtil.getString(context, Constant.WDID, "-1");
            String uid = SharedPreferenceUtil.getString(context, Constant.YHID, "-1");
            String loginSalt = SharedPreferenceUtil.getString(CommApplication.getContext(), Constant.LoginSalt, "");
            HashMap<String, String> dictParam = new HashMap<String, String>();
            dictParam.put("login_salt", loginSalt);
            dictParam.put("gid", gid);
            dictParam.put("uid", uid);
            dictParam.put("access_token", NetUtils.getEncode(token));
            params = NetUtils.POST_SIGN(params, dictParam);
            params.put("login_salt",loginSalt);
            params.put("headpic", file);
            params.put("gid", gid);
            params.put("uid", uid);
            params.put("access_token", token);
            LogUtil.e("file", file.getName().toString());
            String url = Config.BASEURL + UrlConstant.UPLOADYYZZ;
            AsyncHttpClient client = new AsyncHttpClient();
            client.post(url, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    try {
                        if(response.has("data")){
                            String s = response.getString("data");
                            response = AES.desEncrypt(s);
                        }else{
                            response = response.getJSONObject("result");
                        }
                        LogUtil.e("code", response.toString());
                        if("40013".equals(response.getString("status"))){
                            //activity跳转
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);
                        }else if("1001".equals(response.getString("status"))){
                            LogUtil.e("code", response.toString());
                            if (statusCode == 200) {
                                Toast.makeText(context, "头像上传成功!", Toast.LENGTH_SHORT)
                                        .show();
                            } else {
                                Toast.makeText(context,
                                        "网络访问异常，错误码：" + statusCode, Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(context,
                                    "网络访问异常，错误码  > " + statusCode, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);

                    Toast.makeText(context,
                            "网络访问异常，错误码  > " + statusCode, Toast.LENGTH_SHORT).show();

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /*
     * 从相册获取
	 */

    public void gallery() {
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    /*
     * 从相机获取
     */
    public void camera() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        // 判断存储卡是否可以用，可用进行存储
        if (hasSdcard()) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(new File(Environment
                            .getExternalStorageDirectory(), PHOTO_FILE_NAME)));
        }
        startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                crop(uri);
            }

        } else if (requestCode == PHOTO_REQUEST_CAMERA) {
            if (hasSdcard()) {
                tempFile = new File(Environment.getExternalStorageDirectory(),
                        PHOTO_FILE_NAME);
                crop(Uri.fromFile(tempFile));
            } else {
                Toast.makeText(context, "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == PHOTO_REQUEST_CUT) {
            try {
                bitmap = data.getParcelableExtra("data");
                this.iv_imagehead.setImageBitmap(bitmap);
                upload();


            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 剪切图片
     *
     * @param uri
     * @function:
     * @author:Jerry
     * @date:2013-12-30
     */
    private void crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        // 图片格式
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);// true:不返回uri，false：返回uri
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    private boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
