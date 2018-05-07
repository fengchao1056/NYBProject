package com.myzyb2.appNYB2.ui.fragment;

import android.app.AlertDialog;
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
import android.view.Window;
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
import com.myzyb2.appNYB2.R;
import com.myzyb2.appNYB2.common.Constant;
import com.myzyb2.appNYB2.http.AES;
import com.myzyb2.appNYB2.http.Config;
import com.myzyb2.appNYB2.http.NetUtils;
import com.myzyb2.appNYB2.http.UrlConstant;
import com.myzyb2.appNYB2.javabean.PersonalCenter;
import com.myzyb2.appNYB2.ui.activity.bank.BindCardActivity;
import com.myzyb2.appNYB2.ui.activity.bank.ManagePasswdActivity;
import com.myzyb2.appNYB2.ui.activity.bank.UnBindCardActivity;
import com.myzyb2.appNYB2.ui.activity.login.LoginActivity;
import com.myzyb2.appNYB2.ui.activity.main.MainActivity;
import com.myzyb2.appNYB2.ui.activity.my.CommonProblemActivity;
import com.myzyb2.appNYB2.ui.activity.my.ShowDealerActivity;
import com.myzyb2.appNYB2.ui.activity.my.TakeOrdersActivity;
import com.myzyb2.appNYB2.ui.activity.pay.RechargeAndGetActivity;
import com.myzyb2.appNYB2.ui.activity.my.ReplacePwActivity;
import com.myzyb2.appNYB2.ui.activity.my.SpecificationActivity;
import com.myzyb2.appNYB2.ui.view.ActionSheetDialog;
import com.myzyb2.appNYB2.ui.view.CircleImageView;
import com.myzyb2.appNYB2.util.CommonDialog;
import com.myzyb2.appNYB2.util.JsonUtil;
import com.myzyb2.appNYB2.util.LogUtil;
import com.myzyb2.appNYB2.util.SharedPreferenceUtil;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

import static com.myzyb2.appNYB2.ui.view.ActionSheetDialog.OnSheetItemClickListener;
import static com.myzyb2.appNYB2.ui.view.ActionSheetDialog.SheetItemColor;


public class MyFragment extends BaseFragment implements View.OnClickListener {


    @Bind(R.id.rl_money)
    RelativeLayout rlMoney;
    @Bind(R.id.rl_carmes_my)
    RelativeLayout rlCarmesMy;
    @Bind(R.id.rl_replacepw_my)
    RelativeLayout rlReplacepwMy;
    @Bind(R.id.rl_outlogin)
    RelativeLayout rlOutlogin;
    @Bind(R.id.tv_moneynum_my)
    TextView tv_moneynum_my;
    @Bind(R.id.tv_adress_name)
    TextView tv_adress_name;
    @Bind(R.id.tv_phone)
    TextView tv_phone;
    @Bind(R.id.tv_xxadress)
    TextView tv_xxadress;
    @Bind(R.id.iv_imagehead)
    CircleImageView imageview;
    @Bind(R.id.bt_addandget_my)
    Button btAddandgetMy;
    @Bind(R.id.iv_bankcrad_my)
    ImageView ivBankcradMy;
    @Bind(R.id.tv_bank_stuts)
    TextView tvBankStuts;
    @Bind(R.id.tv_bankcrad_my)
    TextView tvBankcradMy;
    @Bind(R.id.iv_plus_my)
    ImageView ivPlusMy;
    @Bind(R.id.tv_replace_my)
    TextView tvReplaceMy;
    @Bind(R.id.rl_bankcrad)
    RelativeLayout rlBankcrad;
    @Bind(R.id.rl_wdlist_my)
    RelativeLayout rlWdlist;
    @Bind(R.id.rl_alreadyget_my)
    RelativeLayout rlAlreadyget;
    @Bind(R.id.rl_questions_my)
    RelativeLayout rlQuestions_my;
    @Bind(R.id.ll_paypasswd)
    LinearLayout llPaypasswd;


    private PersonalCenter personalCenter;
    private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    private Bitmap bitmap;
    /* 头像名称 */
    private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
    private File tempFile;

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
        rlCarmesMy.setOnClickListener(this);
        rlMoney.setOnClickListener(this);
        rlReplacepwMy.setOnClickListener(this);
        btAddandgetMy.setOnClickListener(this);
        rlOutlogin.setOnClickListener(this);
        imageview.setOnClickListener(this);
        rlCarmesMy.setOnClickListener(this);
        rlWdlist.setOnClickListener(this);
        rlAlreadyget.setOnClickListener(this);
        rlQuestions_my.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.rl_replacepw_my:
                /**
                 * 修改密码
                 */
                startActivity(new Intent(context, ReplacePwActivity.class));
                break;
            // TODO
            case R.id.rl_money:
                /**
                 * 明细列表
                 */
                startActivity(new Intent(context, SpecificationActivity.class));
                break;

            case R.id.rl_carmes_my:
                /**
                 * 车辆信息对话框
                 */
                final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.show();
                Window window = alertDialog.getWindow();
                window.setContentView(R.layout.dialog_carmes);
                TextView tv_carmodel = (TextView) window.findViewById(R.id.tv_carmodel);
                TextView tv_carnum = (TextView) window.findViewById(R.id.tv_carnum);
                TextView tv_carweight = (TextView) window.findViewById(R.id.tv_carweight);
                Button bt_close = (Button) window.findViewById(R.id.bt_close);
                if (tv_carmodel != null && personalCenter != null && personalCenter.auto_info != null
                        && personalCenter.auto_info.size() > 0) {
                    tv_carmodel.setText(personalCenter.auto_info.get(0).auto_model);
                    tv_carnum.setText(personalCenter.auto_info.get(0).auto_id);
                    tv_carweight.setText(personalCenter.auto_info.get(0).auto_load);
                }
                bt_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                break;
            case R.id.rl_outlogin:
                /**
                 * 退出登录
                 */
                CommonDialog.showInfoDialog(context, "您确定要退出登录吗?", "退出提示", "取消", "确认退出", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.btn_pos:
                                startActivity(new Intent(context, LoginActivity.class));
                                ((MainActivity) context).finish();
                                break;
                        }

                    }
                });
                break;

            case R.id.iv_imagehead:
                /**
                 * 头像上传
                 */
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

            case R.id.bt_addandget_my:
                /**
                 * 充值提现
                 */
                startActivity(new Intent(getActivity(), RechargeAndGetActivity.class));
                break;
            case R.id.rl_wdlist_my:
                /**
                 * 网点列表
                 */
                startActivity(new Intent(getActivity(), ShowDealerActivity.class));
                break;
            case R.id.rl_alreadyget_my:
                /**
                 * 已取订单
                 */
                startActivity(new Intent(getActivity(), TakeOrdersActivity.class));
                break;
            case R.id.rl_questions_my:
                /**
                 * 常见问题
                 */
                startActivity(new Intent(getActivity(), CommonProblemActivity.class));
                break;
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        // TODO Auto-generated method stub
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getdata();
        }
    }


    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (getUserVisibleHint()) {
            getdata();
        }
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
        netUtils.putReturnJson(context, NetUtils.POST, UrlConstant.SELLERURL, params, dictParam, new JsonHttpResponseHandler() {
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
                    LogUtil.e("response", response.toString());
//                    String list = response.getString("list");
                    if("40013".equals(response.getString("status"))){
                        //activity跳转
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    } else {
                        if (response != null && !TextUtils.isEmpty(response.toString()) && !"null".equals(response)) {
                            String str = response.getString("list");
                            personalCenter = JsonUtil.getSingleBean(str.toString(), PersonalCenter.class);
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
                            setUIData();

                        }
                    }
                    LogUtil.e("list", response.toString());
                    CommonDialog.closeProgressDialog();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private void setUIData() {
        String MOBLIE = SharedPreferenceUtil.getString(context, Constant.MOBLIE, "-1");
        if (tv_adress_name != null) {
            //省市县
            tv_adress_name.setText(personalCenter.uname);
            tv_phone.setText(MOBLIE);//手机
            tv_moneynum_my.setText(personalCenter.money + "元");//余额
            tv_xxadress.setText(personalCenter.pros + personalCenter.citys + personalCenter.towns+"\n"+personalCenter.address);//详细地址
            //头像
            if (bitmap == null) {
                Ion.with(context).load(personalCenter.img_url).withBitmap()
                        .placeholder(R.drawable.my_tou)
                        .error(R.drawable.my_tou).intoImageView(imageview);
            } else {
                this.imageview.setImageBitmap(bitmap);
            }
            //银行卡相关
            final String bc_id = personalCenter.bc_id;

            if (bc_id == null || TextUtils.isEmpty(bc_id) || bc_id.length() == 0 || "null".equals(bc_id)) {
                tvBankStuts.setText("未绑定银行卡");
                ivBankcradMy.setEnabled(false);
                tvReplaceMy.setVisibility(View.GONE);
                ivPlusMy.setVisibility(View.VISIBLE);
                rlBankcrad.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferenceUtil.saveBoolean(context, Constant.GO_MAIN, true);
                        Intent intent = new Intent(getActivity(), BindCardActivity.class);
                        intent.putExtra("bc_id", bc_id);
                        startActivity(intent);

                    }
                });

            } else {
                //已绑定银行卡
                tvBankStuts.setText("已绑定银行卡");
                tvBankcradMy.setText(bc_id + "");
                ivBankcradMy.setEnabled(true);
                tvReplaceMy.setVisibility(View.VISIBLE);
                ivPlusMy.setVisibility(View.GONE);
                llPaypasswd.setVisibility(View.VISIBLE);
                llPaypasswd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getActivity(), ManagePasswdActivity.class));
                    }
                });
                rlBankcrad.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferenceUtil.saveBoolean(context, Constant.GO_MAIN, true);
                        Intent intent = new Intent(getActivity(), UnBindCardActivity.class);
                        intent.putExtra("bc_id", bc_id);
                        startActivity(intent);


                    }
                });
            }
        }

    }


    public void upload() {
        try {
            String token = SharedPreferenceUtil.getString(context, Constant.TOKEN, "");
            File file = new File(Environment.getExternalStorageDirectory() + "/" + PHOTO_FILE_NAME);
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            RequestParams params = new RequestParams();
            String gid = SharedPreferenceUtil.getString(context, Constant.WL_ID, "-1");
            String uid = SharedPreferenceUtil.getString(context, Constant.YHID, "-1");
            String loginSalt = SharedPreferenceUtil.getString(context, Constant.LoginSalt, "");
            HashMap<String, String> dictParam = new HashMap<String, String>();
            dictParam.put("login_salt", loginSalt);
            dictParam.put("gid", gid);
            dictParam.put("uid", uid);
            dictParam.put("access_token", NetUtils.getEncode(token));
            params = NetUtils.SIGN(params, dictParam);
            params.add("login_salt", loginSalt);
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
                upload();
                this.imageview.setImageBitmap(bitmap);
                boolean delete = tempFile.delete();


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
