package com.myzyb.appNYB.Manager;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.myzyb.appNYB.API.BaseRequest;
import com.myzyb.appNYB.common.Constant;
import com.myzyb.appNYB.http.Config;
import com.myzyb.appNYB.util.LogUtil;
import com.myzyb.appNYB.util.SecurityUtils;
import com.myzyb.appNYB.wxapi.PayResult;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.logging.Handler;

/**
 * Created by xialv on 2018/2/2.
 *
 */

public final class PayManager  {
    public WXPayBuilder builder; // 微信
    public PayStatue PayBuilder; // 支付状态回调处理
    public AliPayBuilder AliBuilder; // 支付宝支付处理
    public static PayManager PayManager; // 支付宝支付管理者
//    private   IWXAPI api;
//    private  Thread payThread;
    public WXPayBuilder BuildWeiChat(){
        builder = new  WXPayBuilder();
        return builder;
    };

    public static PayManager shareInstance(){
        if (PayManager == null){
            PayManager = new PayManager();
        }
        return  PayManager;
    }

    public AliPayBuilder builderAlipay(){
        AliBuilder = new AliPayBuilder();
        return  AliBuilder;
    }


    // 微信builder
    public final  class WXPayBuilder {
        private String AppId;
        private String PartnerId;
        private String PrepayId;
        private String PackageValue;
        private String NonceStr;
        private String TimeStamp;
        private String Sign;
        private PayReq request;
        public  Context ctx ;
        public WXPayBuilder setAppId(String appId) {
            AppId = appId;
            return this;
        }


        public WXPayBuilder setPartnerId(String partnerId) {
            PartnerId = partnerId;
            return this;
        }

        public WXPayBuilder setPrepayId(String prepayId) {
            PrepayId = prepayId;
            return this;
        }

        public WXPayBuilder setPackageValue(String packageValue) {
            PackageValue = packageValue;
            return this;
        }

        public WXPayBuilder setNonceStr(String nonceStr) {
            NonceStr = nonceStr;
            return this;
        }

        public WXPayBuilder setTimeStamp(String timeStamp) {
            TimeStamp = timeStamp;
            return this;
        }

        public WXPayBuilder setSign(String sign) {
            Sign = sign;
            return this;
        }
        public PayReq build(){
            request = new PayReq();
            request.appId = AppId;
            request.partnerId = PartnerId;
            request.prepayId = PrepayId;
            request.packageValue = "Sign=WXPay";
            request.nonceStr = NonceStr;
            request.timeStamp =System.currentTimeMillis()/1000+"" ;

            request.sign = signNum(
                    request.partnerId,
                    request.prepayId,
                    request.packageValue,
                    request.nonceStr,
                    request.timeStamp,
                    Constant.WEIXIN_PARTERKEY) ;
            return request;
        }
        public void toWXPay(Context context, String str, PayStatue statue){
            ctx = (Context) context;
            PayBuilder = statue;
            request = this.build();
            final IWXAPI api =  WXAPIFactory.createWXAPI(ctx, Constant.WeiChat_AppID,true);

            api.registerApp(Constant.WeiChat_AppID);

            Runnable payRunnable = new Runnable() {
                public void run() {
                    api.sendReq(request);
                }
            };
//
            Thread payThread = new Thread(payRunnable);
             payThread.start();
        }
    }

    //微信支付成功回调
//    @Override
//    public void onResp(BaseResp baseResp) {
//        if(baseResp.getType()==ConstantsAPI.COMMAND_PAY_BY_WX){
//            if(baseResp.errCode==0){
//                Toast.makeText(this.builder.ctx, "支付成功", Toast.LENGTH_LONG).show();
//                this.PayBuilder.PaySucess();
//            }
//            else {
//                Toast.makeText(this.builder.ctx, "支付失败", Toast.LENGTH_LONG).show();
//                this.PayBuilder.PayFail();
//            }
//        }
//
//    }
//    @Override
//    public void onReq(BaseReq baseReq) {
//
//    }







    //支付宝支付成功回调
   public class AliPayBuilder{
       public Context act;
       private  int SDK_PAY_FLAG = 100;

       private android.os.Handler sHandler = new android.os.Handler() {
           public void handleMessage(Message msg) {
               switch (msg.what) {
                   case 100: {
                       PayResult payResult = new PayResult((Map)msg.obj);
                       /**
                        * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                        * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                        * docType=1) 建议商户依赖异步通知
                        */
                       String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                       String resultStatus = payResult.getResultStatus();
                       // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                       if (TextUtils.equals(resultStatus, "9000")) {
                           if (PayBuilder !=null){
                               if (PayBuilder != null) {
                                   PayBuilder.PaySucess();
                               }
                           }

                       } else {
                           // 判断resultStatus 为非"9000"则代表可能支付失败
                           // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                           if (TextUtils.equals(resultStatus, "8000")) {
                               // Toast.makeText(MainActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();
                               if (PayBuilder != null) {
                                   PayBuilder.PaySucessOnfirm();
                               }

                           } else {
                               // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                               //Toast.makeText(MainActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                               if (PayBuilder != null){
                                   PayBuilder.PayFail();
                               }
                           }
                       }
                       break;
                   }
                   default:
                       break;
               }

           };

       };


       public void openAlipaySdk(Context ctx, final String orderInfo, PayStatue statue ){
           act =ctx;
           PayBuilder = statue;
           Runnable payRunnable = new Runnable() {

               @Override
               public void run() {
                   PayTask alipay = new PayTask((Activity) act);
                   Map<String, String> result = alipay.payV2(orderInfo,true);

                   Message msg = new Message();
                   msg.what = SDK_PAY_FLAG;
                   msg.obj = result;
                   sHandler.sendMessage(msg);
               }
           };
           // 必须异步调用
           Thread payThread = new Thread(payRunnable);
           payThread.start();
       }
   }

    //支付结果
    public  interface PayStatue {
        public void PaySucess();
        public void PayFail();
        public void PaySucessOnfirm();
    }

    //判断微信是不是可以打开
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static   String createSign(SortedMap<Object, Object> parameters) {
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();//所有参与传参的参数按照accsii排序（升序）
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        //这个partnerkey是需要自己进行设置的,要登陆你的微信的商户帐号(注意是商户,不是开放平台帐号),然后到api什么接口安全之类的那去设置,然后获取到
        sb.append("key=" + Constant.WEIXIN_PARTERKEY);
        Log.e("TAG", sb.toString());
        String sign = SecurityUtils.md5(sb.toString()).toUpperCase();
        Log.e("TAG", "sign的值为" + sign);
        return sign;
    }

    public static String signNum(String partnerId,String prepayId,String packageValue,String nonceStr,String timeStamp,String key){
        String stringA=
                "appid="+Constant.WeiChat_AppID
                        +"&noncestr="+nonceStr
                        +"&package="+packageValue
                        +"&partnerid="+partnerId
                        +"&prepayid="+prepayId
                        +"&timestamp="+timeStamp;


        String stringSignTemp = stringA+"&key="+key;
        String sign = SecurityUtils.stringMD5(stringSignTemp);
        return  sign;
    }
}
