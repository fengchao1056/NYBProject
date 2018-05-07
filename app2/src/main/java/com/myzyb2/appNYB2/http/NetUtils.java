package com.myzyb2.appNYB2.http;

import android.content.Context;
import android.text.TextUtils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.myzyb2.appNYB2.R;
import com.myzyb2.appNYB2.common.CommApplication;
import com.myzyb2.appNYB2.common.Constant;
import com.myzyb2.appNYB2.util.CommonDialog;
import com.myzyb2.appNYB2.util.CommonUtil;
import com.myzyb2.appNYB2.util.LogUtil;
import com.myzyb2.appNYB2.util.SharedPreferenceUtil;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;


/**
 * 网络工具类
 *
 * @author zhuxc
 * @ClassName: NetUtils
 * @Description: TODO
 * @date modify by 2015-7-3 下午2:58:31
 */
public class NetUtils {
    private static NetUtils netUtils;
    public static String GET = "GET";
    public static String POST = "POST";
    private static AsyncHttpClient Client;

    public static NetUtils newInstance() {
        if (null == netUtils) {
            netUtils = new NetUtils();
            Client = new AsyncHttpClient();
            //请求超时时间
            Client.setTimeout(5000);
        }
        return netUtils;
    }

    /**
     * 网络请求数据，返回的是json格式数据     *
     *
     * @param context
     * @param method
     * @param relativeUrl
     * @param params
     */

    public void putReturnJson(final Context context, String method, final String relativeUrl, RequestParams params,
                              HashMap<String, String> dictParam, JsonHttpResponseHandler jhrhandler) {
        if (CommonUtil.hasNetwork(context)) {
            if(!relativeUrl.contains("m=User")){
                String loginSalt = SharedPreferenceUtil.getString(CommApplication.getContext(), Constant.LoginSalt, "");
                String uid = SharedPreferenceUtil.getString(CommApplication.getContext(), Constant.YHID, "-1");
                dictParam.put("login_salt", loginSalt);
                dictParam.put("uid", uid);
                params.add("login_salt", loginSalt);
                params.add("uid", uid);
            }
            params = SIGN(params, dictParam);
            StringBuffer buffer = new StringBuffer();
            buffer.append(Config.BASEURL);
            buffer.append(relativeUrl);
            String absoluteUrl = buffer.toString();
            LogUtil.e("net", absoluteUrl + "&" + params.toString());
            if (GET.equals(method)) {
                Client.get(context, absoluteUrl, params, jhrhandler);
            } else if (POST.equals(method)) {
                Client.post(context, absoluteUrl, params, jhrhandler);
            }
        } else {
            CommonDialog.closeProgressDialog();
            CommonDialog.showInfoDialog(context, context.getResources().getString(R.string.net_error));

        }

    }

    //get不带参数
    public void getReturnJson(final Context context, final String relativeUrl, JsonHttpResponseHandler jhrhandler) {

        if (CommonUtil.hasNetwork(context)) {
            StringBuffer buffer = new StringBuffer();
            buffer.append(Config.BASEURL);
            buffer.append(relativeUrl);
            String absoluteUrl = buffer.toString();
            Client.get(context, absoluteUrl, jhrhandler);
        } else {
            CommonDialog.showInfoDialog(context, context.getResources().getString(R.string.net_error));
        }

    }

    public static RequestParams SIGN(RequestParams params,
                               HashMap<String, String> dictParam){
        String loginSalt = SharedPreferenceUtil.getString(CommApplication.getContext(), Constant.LoginSalt, "");
        String sorts = getSorts();
        if(!TextUtils.isEmpty(loginSalt)){
            dictParam.put("login_salt", loginSalt);
        }
        dictParam.put("sorts", sorts);
        String sign = getSign(dictParam);
        params.add("sorts", sorts);
        params.add("sign", sign);
        params.add("login_salt", loginSalt);
        return params;
    }

    public static String getSign(HashMap<String, String> dictParam){
        int index = 0;
        StringBuilder sb = new StringBuilder();
        SortedSet<String> keys = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
        keys.addAll(dictParam.keySet());
        for (String key : keys) {
            index = index + 1;
            sb.append(key);
            sb.append("=");
            sb.append(dictParam.get(key));
            if(index < keys.size()){
                sb.append("&");
            }
        }
        return getMd5(sb.toString());
    }

    public static String getMd5(String sign){
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update((sign).getBytes("UTF-8"));
            byte b[] = md5.digest();

            int i;
            StringBuffer buf = new StringBuffer("");

            for(int offset=0; offset<b.length; offset++){
                i = b[offset];
                if(i<0){
                    i+=256;
                }
                if(i<16){
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            return buf.toString().toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String Md5(String sign){
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update((sign).getBytes("UTF-8"));
            byte b[] = md5.digest();

            int i;
            StringBuffer buf = new StringBuffer("");

            for(int offset=0; offset<b.length; offset++){
                i = b[offset];
                if(i<0){
                    i+=256;
                }
                if(i<16){
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getSorts(){
        StringBuilder sb = new StringBuilder();
        int math = (int)((Math.random()*9+1)*100000);
        long time = System.currentTimeMillis() / 1000;
        sb.append(math);
        sb.append(time);
        return sb.toString();
    }

    public static String getEncode(String token){
        token = java.net.URLEncoder.encode(token);
        return token;
    }
}