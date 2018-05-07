package com.myzyb.appNYB.API;



import android.app.Application;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.myzyb.appNYB.common.CommApplication;
import com.myzyb.appNYB.common.Constant;
import com.myzyb.appNYB.http.Config;
import com.myzyb.appNYB.http.NetUtils;
import com.myzyb.appNYB.http.UrlConstant;
import com.myzyb.appNYB.util.CommonDialog;
import com.myzyb.appNYB.util.CommonUtil;
import com.myzyb.appNYB.util.JsonUtil;
import com.myzyb.appNYB.util.LogUtil;
import com.myzyb.appNYB.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

/**
 * Created by xialv on 2018/2/2.
 */

public  class BaseRequest<T>{
    public T requestObject;
    public int httpstatusCode;
    public Header[] headers;
    public JSONObject response;
    public Throwable throwable;
    private  AsyncHttpClient client;
    public   BaseRequest(T t){
        this.requestObject = t;
    }
    public  RequestMethod RequestMethod(){
        return RequestMethod.POST;
    }
    public void RequestWillStart (){
        CommonDialog.showProgressDialog(CommApplication.context);

    };
    public void RequestDidend(){
        CommonDialog.closeProgressDialog();
    };
    public  interface RequestResult{
        public void onSuccess(int statusCode, Header[] headers, JSONObject response);
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse);

    }
    public enum RequestMethod{
        POST,GET,PUT,DELETE,UPDATE
    }
    public HashMap requestArgs() {return null;}
    public Object reponseObject(){return null;};

    public String requestURL(){return null;}

    public String RequestBaseURL(){return Config.appH5Url;}




    public void startRequest(final BaseRequest request,final RequestResult result){
        this.RequestWillStart();
        BaseRequestManager.shareInstance().BuilderBaseRequest(this);
        String baseURL = this.RequestBaseURL();
        String url = this.requestURL()!=null?(baseURL + this.requestURL()) : baseURL;
        if (this.RequestMethod()==RequestMethod.POST){
            client = new AsyncHttpClient();
            client.post(CommApplication.getContext(), url, this.jsonData(this.requestArgs()), "application/json", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    request.RequestDidend();
                    if (statusCode == 200) {

                        Integer teg = null;
                        try {
                             teg= (Integer) response.get("code");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if ( teg.intValue()== 2000) {
                            try {
                                response = (JSONObject) response.get("data");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            result.onSuccess(statusCode, headers, response);
                        } else {
                            try {
                                String message = (String) response.get("message");
                                ToastUtil.showToast(message+response.get("code").toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    } else {
                        try {
                            if (response.get("message") instanceof String) {
                                ToastUtil.showToast((String) response.get("message"));
                            } else {
                                ToastUtil.showToast(response.get("code").toString());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    BaseRequest.printLog(statusCode, headers, response);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    request.RequestDidend();
                    if (statusCode != 200) {
                        String str = "网络错误" + statusCode;
                        ToastUtil.showToast(str);
                        result.onFailure(statusCode, headers, throwable, errorResponse);
                    }

                    BaseRequest.printLog(statusCode, headers, response);
                }
            });

        }else if(this.RequestMethod() ==RequestMethod.GET) {
            client = new AsyncHttpClient();
            client.get(CommApplication.getContext(),url,this.jsonData(this.requestArgs()),"application/json",new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    if (statusCode == 200) {
                        result.onSuccess(statusCode, headers, response);
                    }else {

                    }
                    request.RequestDidend();
                    BaseRequest.printLog(statusCode,headers,response);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    if (statusCode !=200) {
                        result.onFailure(statusCode, headers, throwable, errorResponse);
                    }
                    request.RequestDidend();
                    BaseRequest.printLog(statusCode,headers,response);
                }
            });
        }else if(this.RequestMethod() == RequestMethod.PUT){


        }else if(this.RequestMethod()==RequestMethod.DELETE){

        }else {

        }
    }
    public Map requestHeads(){
        return null;
    }


    public ByteArrayEntity jsonData(Map<String,String> mapData){
        this.printLog();
        JSONObject jsonObject = new JSONObject();
        try {
            if (mapData != null) {
                for (Map.Entry<String, String> entry : mapData.entrySet()) {
                    jsonObject.put(entry.getKey(), entry.getValue());
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        ByteArrayEntity entity = null;
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return  entity;
    }

    public  void printLog(){
        LogUtil.d(this.toString(),"{url:"+this.RequestBaseURL()+this.requestURL()+"}");
        LogUtil.d(this.toString(),"{args:"+this.requestArgs().toString()+"}");
        LogUtil.d("baseRequest","{RequestMethod:"+this.RequestMethod()+"}");
    }

    public static void printLog(int statuscode ,Header[] headers, JSONObject response){
        LogUtil.d("baseRequest","{ResponseCode:"+statuscode+"}");
        LogUtil.d("baseRequest","{ResponseArgs:"+response.toString()+"}");
        LogUtil.d("baseRequest","{Responseheaders:"+headers.toString()+"}");

    }

}
