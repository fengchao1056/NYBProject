package com.myzyb.appNYB.ui.activity.regiser;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.myzyb.appNYB.R;
import com.myzyb.appNYB.http.AES;
import com.myzyb.appNYB.http.NetUtils;
import com.myzyb.appNYB.http.UrlConstant;
import com.myzyb.appNYB.ui.activity.additive.AgreementActivity;
import com.myzyb.appNYB.ui.activity.login.LoginActivity;
import com.myzyb.appNYB.ui.activity.main.BaseActivity;
import com.myzyb.appNYB.ui.activity.prepose.RegisterAndLoginActivity;
import com.myzyb.appNYB.ui.map.LocationDemo;
import com.myzyb.appNYB.util.ClickUtil;
import com.myzyb.appNYB.util.CommonDialog;
import com.myzyb.appNYB.util.CommonUtil;
import com.myzyb.appNYB.common.Constant;
import com.myzyb.appNYB.util.ImageUtils;
import com.myzyb.appNYB.util.LogUtil;
import com.myzyb.appNYB.util.SharedPreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class RegisterMesActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.et_wdmc)
    EditText etWdmc;
    @Bind(R.id.et_lxr)
    EditText etLxr;
    @Bind(R.id.ed_xxdz)
    EditText edXxdz;
    @Bind(R.id.ed_tjrid)
    EditText edTjrid;
    @Bind(R.id.cb_agree_mse)
    CheckBox cbAgreeMse;
    @Bind(R.id.tv_xytk_mes)
    TextView tvXytkMes;
    private int mPosition = 0;
    private static final String PHOTOPATH = "/photo/";
    private static String srcPath;
    private static String fileName;
    private Dialog d;
    private StringBuffer buffer;
    private Context context;
    final String state = Environment.getExternalStorageState();
    @Bind(R.id.bt_sum_mse)
    protected Button bt_sum_mse;
    @Bind(R.id.tv_choose_adress)
    protected TextView tv_choose_adress;
    @Bind(R.id.tv_type_choose)
    protected TextView tv_type_choose;
    @Bind(R.id.rl_loadpicture)
    protected RelativeLayout rl_loadpicture;
    @Bind(R.id.iv_photo_yyzz)
    protected ImageView iv_photo_yyzz;
    //    private String proviceId;
//    private String cityId;
//    private String districtId;
    private boolean wdmc = false;
    private boolean wdlx = false;
    private boolean lxr = false;
    private boolean ssx = false;
    private boolean xxdz = false;
    private boolean yyzz = false;
    private boolean iscebox = false;

    private Bitmap bitmap;
    private String mProvince;
    private String mCity;
    private String mDistrict;
    private String height;
    private String coordinate;
    private String name;

    private String targetPath = Environment.getExternalStorageDirectory() + "/compressPic.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_mes);
        context = this;
        ButterKnife.bind(this);
        initTitleBar();
        setTitleBar_titletext("注册能源宝账号");
        initUI();
    }

    private void initUI() {
        tvXytkMes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterMesActivity.this, AgreementActivity.class));
            }
        });
        etWdmc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    wdmc = true;
                    initSubmit();
                } else {
                    wdmc = false;
                    initSubmit();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tv_type_choose.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!"请选择网点类型".equals(s.toString())) {
                    wdlx = true;
                    initSubmit();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etLxr.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    lxr = true;
                    initSubmit();
                } else {
                    lxr = false;
                    initSubmit();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


//        tv_choose_adress.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (!"请选择地址".equals(s.toString())) {
//                    ssx = true;
//                    initSubmit();
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
        edXxdz.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    xxdz = true;
                    initSubmit();
                } else {
                    xxdz = false;
                    initSubmit();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        cbAgreeMse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                iscebox = isChecked;
                initSubmit();
            }
        });
    }

    private void initDialog() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_uploadphoto, null);
        d = new Dialog(this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(view);
        Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
        TextView tv_photograph = (TextView) view.findViewById(R.id.tv_photograph);
        TextView tv_localupload = (TextView) view.findViewById(R.id.tv_localupload);
        bt_cancel.setOnClickListener(this);
        tv_photograph.setOnClickListener(this);
        tv_localupload.setOnClickListener(this);
        d.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_sum_mse:
                if (!ClickUtil.isFastClick()) {
                    upData();
                }
                break;
            case R.id.tv_choose_adress:
                startActivityForResult(new Intent(RegisterMesActivity.this, LocationDemo.class), 1);
                break;
            case R.id.tv_type_choose:
                Intent intent = new Intent(RegisterMesActivity.this, DotTypeActivity.class);
                intent.putExtra("mPosition", mPosition);
                LogUtil.e("click", mPosition + "");
                startActivityForResult(intent, 2);
                break;
            case R.id.rl_loadpicture:
                initDialog();
                break;
            case R.id.bt_cancel:
                d.dismiss();
                break;
            case R.id.tv_photograph:
                if (state.equals(Environment.MEDIA_MOUNTED)) {
                    Intent getpath = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File file = new File(Environment.getExternalStorageDirectory() + PHOTOPATH);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    String tempphotoname = System.currentTimeMillis() + ".jpg";
                    buffer = new StringBuffer();
                    buffer.append(Environment.getExternalStorageDirectory() + PHOTOPATH).append(tempphotoname);
                    getpath.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(buffer.toString())));
                    startActivityForResult(getpath, 3);
                } else {
                    Toast.makeText(getApplicationContext(), "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.tv_localupload:
                if (state.equals(Environment.MEDIA_MOUNTED)) {
                    Intent getImage = new Intent(Intent.ACTION_PICK);
                    getImage.setType("image/*");//相片类型
                    startActivityForResult(getImage, 4);
                } else {
                    Toast.makeText(getApplicationContext(), "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }

    private void upData() {
        CommonDialog.showProgressDialog(context);
        String token = SharedPreferenceUtil.getString(context, Constant.TOKEN, "");
        String swdmc = etWdmc.getText().toString().trim();
        String slxr = etLxr.getText().toString().trim();
        String stzr = edTjrid.getText().toString().trim();
        String swdlx = tv_type_choose.getText().toString().trim();
        String sxxdz = edXxdz.getText().toString().trim();
        String uid = SharedPreferenceUtil.getString(context, Constant.YHID, "");
        String swdmcFilter = stringFilter(swdmc.toString());
        String slxrFilter = stringFilter(slxr.toString());
        if(!swdmc.equals(swdmcFilter)){
            Toast.makeText(RegisterMesActivity.this, "网点名称不能输入非法字符！" , Toast.LENGTH_SHORT).show();
        }else if(!slxr.equals(slxrFilter)){
            Toast.makeText(RegisterMesActivity.this, "联系人不能输入非法字符！" , Toast.LENGTH_SHORT).show();
        }else{
            if (!TextUtils.isEmpty(swdmc) && !TextUtils.isEmpty(slxr) && !TextUtils.isEmpty(sxxdz)) {
                HashMap<String, String> dictParam = new HashMap<String, String>();
                dictParam.put("user_id", NetUtils.getEncode(uid));
                dictParam.put("gname", NetUtils.getEncode(swdmc));
                dictParam.put("uname", NetUtils.getEncode(slxr));
                dictParam.put("province", NetUtils.getEncode(mProvince));
                dictParam.put("city", NetUtils.getEncode(mCity));
                dictParam.put("county", NetUtils.getEncode(mDistrict));
                dictParam.put("area_x", NetUtils.getEncode(height));
                dictParam.put("area_y", NetUtils.getEncode(coordinate));
                dictParam.put("address", NetUtils.getEncode(sxxdz));
                dictParam.put("main_items", NetUtils.getEncode(swdlx));
                dictParam.put("access_token", NetUtils.getEncode(token));
                RequestParams params = new RequestParams();
                params.put("user_id", uid);
                params.put("gname", swdmc);
                params.put("uname", slxr);
//            params.add("province", proviceId);
//            params.add("city", cityId);
//            params.add("county", districtId);
                params.put("province", mProvince);
                params.put("city", mCity);
                params.put("county", mDistrict);
                params.put("area_x", height);
                params.put("area_y", coordinate);
                params.put("address", sxxdz);
                params.put("main_items", swdlx);
                params.put("access_token", token);
                try {
                    File file = new File(targetPath);
                    params.put("headpic", file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    CommonDialog.showInfoDialog(context, "图片未找到");
                }
                if (!TextUtils.isEmpty(stzr)) {
                    dictParam.put("ext_id", NetUtils.getEncode(stzr));
                    params.put("ext_id", stzr);
                }
                LogUtil.e("params", params.toString());
                NetUtils.newInstance().putReturnJson(context, NetUtils.POST, UrlConstant.ADDSELLERURL, params, dictParam, new JsonHttpResponseHandler() {
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
                            String status = response.getString("status");
                            String message = response.getString("message");
                            LogUtil.e("response", response.toString());
                            CommonDialog.closeProgressDialog();
                            switch (status) {
                                case "40013":
                                    //activity跳转
                                    Intent intent = new Intent(RegisterMesActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    break;
                                case "1015":
                                    CommonDialog.showInfoDialog(context, message);
                                    break;
                                case "1001":
                                    startActivity(new Intent(context, AuditInformationActivity.class));
                                    break;
                                default:
                                    CommonUtil.StartToast(context, message);
                                    break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            CommonDialog.closeProgressDialog();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        CommonDialog.closeProgressDialog();
                        CommonDialog.showInfoDialog(context, "连接服务器失败");
                    }
                });
            } else {
                CommonDialog.showInfoDialog(context, "输入内容不能为全空格");
            }
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            CommonDialog.showInfoDialog(context, "确认要退出登录吗？", "提示", "取消", "确认", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.btn_pos:
                            startActivity(new Intent(context, RegisterAndLoginActivity.class));
                            finish();
                            break;

                    }
                }
            });
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                // TODO

//                    String ProviceName = data.getStringExtra("mCurrentProviceName");
//                    String CityName = data.getStringExtra("mCurrentCityName");
//                    String DistrictName = data.getStringExtra("mCurrentDistrictName");
//                    proviceId = data.getStringExtra("mCurrentProviceId");
//                    cityId = data.getStringExtra("mCurrentCityId");
//                    districtId = data.getStringExtra("mCurrentDistrictId");
//                    tv_choose_adress.setText(ProviceName + " " + CityName + " " + DistrictName);
//                    tv_choose_adress.setTextColor(getResources().getColor(R.color.text_323232));
                if (data != null) {
                    mProvince = data.getStringExtra("mProvince");
                    if (mProvince.contains("市")) {
                        mProvince = mProvince.replace("市", "");

                    }
                    if (mProvince.contains("省")) {
                        mProvince = mProvince.replace("省", "");

                    }

                    mCity = data.getStringExtra("mCity");
                    mDistrict = data.getStringExtra("mDistrict");
                    height = String.valueOf(data.getDoubleExtra("x-height", 0.1));
                    coordinate = String.valueOf(data.getDoubleExtra("Y-coordinate", 0.1));
                    name = data.getStringExtra("name");
                    tv_choose_adress.setText(mProvince + mCity + mDistrict + name);
                    tv_choose_adress.setTextColor(getResources().getColor(R.color.text_323232));
                    ssx = true;
                    initSubmit();
                }
                break;
            case 2:
                // TODO
                if (resultCode == Constant.RESULT_OK) {
                    String dotText = data.getStringExtra("dotText");
                    mPosition = data.getIntExtra("position", 0);
                    tv_type_choose.setText(dotText);
                    tv_type_choose.setTextColor(getResources().getColor(R.color.text_323232));
                    LogUtil.e("mes", mPosition + "");
                }
                break;

            default:
                if (resultCode == Activity.RESULT_OK) {
                    switch (requestCode) {
                        case 3:
                            srcPath = buffer.toString();
                            setImage();
                            break;
                        case 4:
                            if (resultCode == Activity.RESULT_OK) {
                                Uri uri2 = data.getData();
                                srcPath = ImageUtils.getPathByUri(context, uri2);
                                setImage();
                            }
                            break;
                    }
                    break;
                }
        }
    }

    private void setImage() {
        //调用压缩图片的方法，返回压缩后的图片path
        compressImage(srcPath, targetPath, 30);
        bitmap = ImageUtils.getimage(srcPath);
        Drawable drawable = new BitmapDrawable(bitmap);
        iv_photo_yyzz.setImageDrawable(drawable);
        yyzz = true;
        initSubmit();
        iv_photo_yyzz.setVisibility(View.VISIBLE);
        d.dismiss();
    }


    private void initSubmit() {
        if (wdmc && wdlx && lxr && ssx && xxdz && yyzz && iscebox) {
            bt_sum_mse.setEnabled(true);
        } else {
            bt_sum_mse.setEnabled(false);
        }
    }

    public static String stringFilter(String str)throws PatternSyntaxException {
        // 只允许字母、数字和汉字
        String   regEx  =  "[^a-zA-Z0-9\u4E00-\u9FA5]";
        Pattern   p   =   Pattern.compile(regEx);
        Matcher   m   =   p.matcher(str);
        return   m.replaceAll("").trim();
    }

    public static String compressImage(String filePath, String targetPath, int quality)  {
        Bitmap bm = getSmallBitmap(filePath);//获取一定尺寸的图片
        int degree = readPictureDegree(filePath);//获取相片拍摄角度
        if(degree!=0){//旋转照片角度，防止头像横着显示
            bm=rotateBitmap(bm,degree);
        }
        File outputFile=new File(targetPath);
        try {
            if (!outputFile.exists()) {
                outputFile.getParentFile().mkdirs();
            }else{
                outputFile.delete();
            }
            FileOutputStream out = new FileOutputStream(outputFile);
            bm.compress(Bitmap.CompressFormat.JPEG, quality, out);
        }catch (Exception e){}
        return outputFile.getPath();
    }

    /**
     * 根据路径获得图片信息并按比例压缩，返回bitmap
     */
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//只解析图片边沿，获取宽高
        BitmapFactory.decodeFile(filePath, options);
        // 计算缩放比
        options.inSampleSize = calculateInSampleSize(options, 480, 800);
        // 完整解析图片返回bitmap
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }


    /**
     * 获取照片角度
     * @param path
     * @return
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转照片
     * @param bitmap
     * @param degress
     * @return
     */
    public static Bitmap rotateBitmap(Bitmap bitmap,int degress) {
        if (bitmap != null) {
            Matrix m = new Matrix();
            m.postRotate(degress);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), m, true);
            return bitmap;
        }
        return bitmap;
    }
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }
}
