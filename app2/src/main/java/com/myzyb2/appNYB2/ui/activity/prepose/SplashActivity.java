package com.myzyb2.appNYB2.ui.activity.prepose;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.myzyb2.appNYB2.R;
import com.myzyb2.appNYB2.common.Constant;
import com.myzyb2.appNYB2.http.Config;
import com.myzyb2.appNYB2.http.NetUtils;
import com.myzyb2.appNYB2.http.UrlConstant;
import com.myzyb2.appNYB2.ui.activity.login.LoginActivity;
import com.myzyb2.appNYB2.ui.activity.main.BaseActivity;
import com.myzyb2.appNYB2.ui.view.AlertDialog;
import com.myzyb2.appNYB2.util.CommonUtil;
import com.myzyb2.appNYB2.util.LogUtil;
import com.myzyb2.appNYB2.util.SharedPreferenceUtil;
import com.myzyb2.appNYB2.util.StreamUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class SplashActivity extends BaseActivity {

    protected static final String tag = "SplashActivity";

    /**
     * 需要更新apk,状态码
     */
    protected static final int UPDATE_VERSION = 100;
    protected static final int Force_UPDATE_VERSION = 105;

    /**
     * 进入应用程序主界面状态码
     */
    protected static final int ENTER_HOME = 101;
    protected static final int URL_ERROR = 102;
    protected static final int IO_ERROR = 103;
    protected static final int JSON_ERROR = 104;
    private String versionName;
    private String state;
    private ProgressDialog pBar;
    private String versioncode;
    private String configVersionurl = "http://www.myzyb.com/app_v";
    private Handler mHandler = new Handler() {//membar	成员变量
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_VERSION:
                    //提示用户更新APK
                    if (SharedPreferenceUtil.getBoolean(context, Constant.IS_UPDATA, true)) {
                        showUpdateDialog();
                    } else {
                        enterHome();
                    }
                    break;
                case Force_UPDATE_VERSION:
                    //强制更新APK
                    showForceUpdateDialog();
                    break;
                case ENTER_HOME:
                    enterHome();
                    break;
                case URL_ERROR:
                    isForceUpdate();
                    break;
                case IO_ERROR:
                    isForceUpdate();
                    break;
                case JSON_ERROR:
                    isForceUpdate();
                    break;
                default:
                    isForceUpdate();
                    break;
            }
        }

        ;
    };
    private String downloadUrl;//服务器下载地址


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = this;
        initData();
    }


    //判断是否为强制
    private void isForceUpdate() {
        if ("0".equals(state)) {
            enterHome();
            return;
        }
        if ("1".equals(state)) {
            CommonUtil.StartToast(context, "连接失败，请检查网络");
        }

    }

    //非强制更新
    private void showUpdateDialog() {
        String verName = getVersionName();
        StringBuffer sb = new StringBuffer();
        sb.append("当前版本: V");
        sb.append(verName);
        sb.append(",发现新版本：V");
        sb.append(versionName);
        sb.append(", 是否更新?");

        new AlertDialog(context).builder()
                .setTitle("版本更新")
                .setMsg(sb.toString())
                .setPositiveButton("更新",// 设置确定按钮
                        new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
//                                LogUtil.e("downloadUrl", configVersionurl + versionName + "/" + Constant.UPDATE_SAVENAME);
                                downFile();
//                                enterHome();
                            }
                        })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferenceUtil.saveBoolean(context, Constant.IS_UPDATA, false);
                        enterHome();
                    }
                }).show();
    }

    //强制更新
    private void showForceUpdateDialog() {
        String verName = getVersionName();
        StringBuffer sb = new StringBuffer();
        sb.append("当前版本：V");
        sb.append(verName + "已停用，请更新版本：V");
        sb.append(versionName);
        new AlertDialog(context).builder()
                .setTitle("版本更新")
                .setMsg(sb.toString())
                .setPositiveButton("更新",// 设置确定按钮
                        new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                //服务器下载地址
//                                downloadUrl = configVersionurl + versionName + "/" + Constant.UPDATE_SAVENAME;
                                downFile();
                            }
                        }).show();
    }

    /**
     * 下载服务端apk文件
     */
    void downFile() {
        Intent intent= new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(configVersionurl);
        intent.setData(content_url);
        startActivity(intent);
        //开启服务
//        startService(new Intent(context, DownloadService.class).putExtra("url", configVersionurl));
    }


    /**
     * 进入应用程序主界面
     */
    protected void enterHome() {
        //activity跳转
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();

    }
    /**
     * 初始化数据
     */
    private void initData() {
        checkVersion();
    }

    /**
     * 通过版本号,检测版本更新
     */
    private void checkVersion() {
        new Thread() {
            public void run() {
                Message msg = Message.obtain();
                long startTime = System.currentTimeMillis();
                try {
                    //1,获取访问网络Url地址
                    URL url = new URL(Config.BASEURL + UrlConstant.UPDATE_VERSION);
                    //2,在此url地址上去开启一个连接请求
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    //3,请求的配置信息

                    connection.setRequestMethod("POST");// 提交模式
                    connection.setConnectTimeout(2000);
                    connection.setReadTimeout(2000);
                    // conn.setConnectTimeout(10000);//连接超时 单位毫秒
                    // conn.setReadTimeout(2000);//读取超时 单位毫秒
                    connection.setDoOutput(true);// 是否输入参数

                    StringBuffer params = new StringBuffer();
                    //修改后
                    String sorts = NetUtils.getSorts();
                    HashMap<String, String> dictParam = new HashMap<String, String>();
                    dictParam.put("type", "1");
                    dictParam.put("name", Constant.type);
                    dictParam.put("sorts", sorts);
                    String sign = NetUtils.getSign(dictParam);
                    // 表单参数与get形式一样
                    params.append("type").append("=").append("1").append("&")
                            .append("name").append("=").append(Constant.type).append("&")
                            .append("sign").append("=").append(sign).append("&")
                            .append("sorts").append("=").append(sorts);
                    byte[] bypes = params.toString().getBytes();
                    connection.getOutputStream().write(bypes);// 输入参数
                    LogUtil.e(context,url+params.toString());
                    //4,通过服务端返回的响应码,判断是否请求成功
                    if (connection.getResponseCode() == 200) {
                        //5,读取服务端返回的数据,流
                        InputStream inputStream = connection.getInputStream();
                        //6,流转换成字符串,常见,工具类
                        String jsonStr = StreamUtil.stream2String(inputStream);
                        LogUtil.e("jsonStr", jsonStr);
                        //7,json解析,看见什么就解析什么,参数就是要去解析的json字符串
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        if(jsonObject.has("data")){
                            jsonObject = jsonObject.getJSONObject("data");
                        }else{
                            jsonObject = jsonObject.getJSONObject("result");
                        }
                        //8,逐个解析字段
                        JSONArray jsonList = jsonObject.getJSONArray("list");
                        JSONObject jsonObject1 = jsonList.getJSONObject(0);

                        state = jsonObject1.getString("state");
                        versionName = jsonObject1.getString("version");
                        versioncode = jsonObject1.getString("version_admin");
                        configVersionurl = jsonObject1.getString("api_url");
                        String localVersionName = getVersionName();
                        if (localVersionName != null && localVersionName.equals(versionName)) {
                            //进入应用的主界面
                            msg.what = ENTER_HOME;
                            return;
                        } else {
                            //提示用户更新
                            if ("0".equals(state)) {
                                //非强制更新
                                msg.what = UPDATE_VERSION;
                                return;
                            }
                            if ("1".equals(state)) {
                                //强制更新
                                msg.what = Force_UPDATE_VERSION;
                                return;
                            }

                        }
                    }
                } catch (
                        MalformedURLException e
                        )

                {
                    e.printStackTrace();

                    msg.what = URL_ERROR;
                } catch (
                        IOException e
                        )

                {

                    e.printStackTrace();
                    msg.what = IO_ERROR;
                } catch (
                        JSONException e
                        ) {
                    e.printStackTrace();
                    msg.what = JSON_ERROR;
                } finally {
                    long endTime = System.currentTimeMillis();
                    //try catch finally此段代码强制执行4秒钟
                    if (endTime - startTime < 4000) {
                        //睡眠4秒
                        try {
                            Thread.sleep(4000 - (endTime - startTime));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    mHandler.sendMessage(msg);
                }
            }

        }.start();

    }

    /**
     * @return 返回版本名称 如果返回null,获取版本名称异常
     */
    private String getVersionName() {
        //获取版本号,PackageMananger---->PackageInfo------->versionName,versionName
        //1,创建PackageMananger对象
        PackageManager pm = getPackageManager();
        //2,获取PackageInfo对象(包名,0获取PackageInfo对象)
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            //3,获取版本名称
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return 返回版本号 如果返回0,获取版本号称异常
     */
    private int getVersionCode() {

        //1,创建PackageMananger对象
        PackageManager pm = getPackageManager();
        //2,获取PackageInfo对象(包名,0获取PackageInfo对象)
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            //3,获取版本名称
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
