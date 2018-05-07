package com.myzyb.appNYB.ui.activity.H5Web;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.ClientCertRequest;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.myzyb.appNYB.Manager.PayManager;
import com.myzyb.appNYB.R;
import com.myzyb.appNYB.common.Constant;
import com.myzyb.appNYB.http.Config;
import com.myzyb.appNYB.http.UrlConstant;
import com.myzyb.appNYB.ui.activity.main.BaseActivity;
import com.myzyb.appNYB.util.LogUtil;
import com.myzyb.appNYB.util.StringUtil;

import java.util.HashMap;
import java.util.Map;
/**
 * Created by xialv on 2018/2/2.
 */
public class H5AppWeb extends BaseActivity {
    private WebView _myWebView;
    private static int resultOk = 0x1000;

            //viewdidload
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_h5_web);
        _myWebView = (WebView) findViewById(R.id.app_mWebView);
        _myWebView.loadUrl(Config.appH5Url+UrlConstant.h5Index);
        // 可以使用javascript
        WebSettings settings = _myWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        _myWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                LogUtil.e('1',url);
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                if (url.indexOf(Constant.H5InterceptUrlWx)!=-1){
                    //微信可以打开就拦截url处理不能打开就加载网页，如果有类型就判断支付类型
                    HashMap<String, String> paramsMap = StringUtil.getParamsMap(url);
                        if(PayManager.isWeixinAvilible(getApplicationContext())==true) {
                            Intent confirmIntent = new Intent(H5AppWeb.this, ComfirmMoney.class);
                            confirmIntent.putExtra("urlMap", paramsMap);
                            startActivityForResult(confirmIntent, resultOk);
                            return true;
                        }else {
                            view.loadUrl(url);
                            return  false;
                        }
                    }else if(url.indexOf(Constant.H5InterceptUrlAlipay)!=-1){
                      HashMap<String, String> paramsMap = StringUtil.getParamsMap(url);
                        Intent confirmIntent = new Intent(H5AppWeb.this, ComfirmMoney.class);
                        confirmIntent.putExtra("urlMap", paramsMap);
                        startActivityForResult(confirmIntent, resultOk);
                        return true;
                    }else {
                    view.loadUrl(url);
                    return false;
                }

            }


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //页面开始加载时
                super.onPageStarted(view, url, favicon);
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                //页面加载结束时
                super.onPageFinished(view, url);
            }
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                // 这里进行无网络或错误处理，具体可以根据errorCode的值进行判断，
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                handler.proceed();
                //android 空白页处理 webView
            }
        });

        _myWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // 获得网页的加载进度 newProgress为当前加载百分比
                super.onProgressChanged(view, newProgress);
            }
            @Override
            public void onReceivedTitle(WebView view, String title) {
                // 获取网页的title，客户端可以在这里动态修改页面的title
                // 另外，当加载错误时title为“找不到该网页”
                super.onReceivedTitle(view, title);
            }
        });
    }
    private void loadWebUrl(String url){
        if (url.indexOf("http:")!=-1){

            _myWebView.loadUrl(url);
        }else {
            _myWebView.loadUrl(Config.BASEURL+url);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (resultOk ==resultCode){
                _myWebView.goBack();
               String url =  data.getStringExtra("url");
                loadWebUrl(url);
            }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //view will appear
    @Override
    protected void onStart() {
        super.onStart();
    }

    //view did appear
    @Override
    protected void onResume() {
        super.onResume();
    }

    //view will dispear
    @Override
    protected void onPause() {
        super.onPause();
    }

    //view did dispear
    @Override
    protected void onStop() {
        super.onStop();
    }


    //从 on stop =》onRestart =》onStart
    @Override
    protected void onRestart() {
        super.onRestart();
    }

    //deallow 资源回收
    @Override
    protected void onDestroy() {
        if (_myWebView!= null) {
            _myWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            _myWebView.clearHistory();
            ((ViewGroup) _myWebView.getParent()).removeView(_myWebView);
            _myWebView.destroy();
            _myWebView= null;
        }

        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            if(_myWebView.canGoBack())
            {
                _myWebView.goBack();//返回上一页面
                return true;
            }
            else
            {
                System.exit(0);//退出程序
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
