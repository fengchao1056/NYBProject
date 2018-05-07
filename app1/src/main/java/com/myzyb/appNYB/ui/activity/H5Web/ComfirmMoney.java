package com.myzyb.appNYB.ui.activity.H5Web;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.myzyb.appNYB.API.APIinfo.OrderPayModule.OrderAlipayAPi;
import com.myzyb.appNYB.API.APIinfo.OrderPayModule.OrderWeichatAPI;
import com.myzyb.appNYB.API.APIinfo.OrderPayModule.OrderBody;
import com.myzyb.appNYB.API.APIinfo.OrderPayModule.OrderResult;
import com.myzyb.appNYB.API.APIinfo.OrderPayModule.OrderResultAPI;
import com.myzyb.appNYB.API.BaseRequest;
import com.myzyb.appNYB.Manager.PayManager;
import com.myzyb.appNYB.R;
import com.myzyb.appNYB.common.CommApplication;
import com.myzyb.appNYB.navigation.NavigationView;
import com.myzyb.appNYB.ui.activity.main.BaseActivity;
import com.myzyb.appNYB.util.CommonUtil;
import com.myzyb.appNYB.util.DeviceUtil;
import com.myzyb.appNYB.util.JsonUtil;
import com.myzyb.appNYB.util.ToastUtil;

import org.json.JSONObject;

import java.util.HashMap;

import cz.msebera.android.httpclient.Header;


/**
 * Created by xialv on 2018/2/2.
 */
public class ComfirmMoney extends BaseActivity {
    private static int resultOk = 0x1000;
    private HashMap map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comfirm_money);
        TextView leftText = (TextView)findViewById(R.id.leftlabel);
        TextView MoneyText = (TextView)findViewById(R.id.confirm_money);
        leftText.setText("支付金额:");
         map  = (HashMap) this.getIntent().getSerializableExtra("urlMap");;
        MoneyText.setText(" ¥"+(CharSequence) map.get("money"));
        initView();

    }
    private NavigationView navigationView;
    private void initView() {
        navigationView = (NavigationView) super.findViewById(R.id.nav_main);
        navigationView.setTitle("确定金额");
        navigationView.hiddeRight();
        navigationView.setClickCallback(new NavigationView.ClickCallback() {

            @Override
            public void onRightClick() {

            }
            @Override
            public void onBackClick() {
                setResult(0);
                finish();
            }
        });
    }
    //订单结果查询
    private void handleResult(String orderNo){
        OrderResult orderResult = new OrderResult();
        orderResult.builderReq()
                .setOrderNo(orderNo)
                .setSendType("json")
                .setCome("APP")
                .setUid((String) map.get("uid"))
                .setVersion("5.3");
        OrderResultAPI resultApi = new OrderResultAPI(orderResult.reqInfo);
        resultApi.startRequest(resultApi, new BaseRequest.RequestResult() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //订单结果查询成功处理
                OrderResult.RepInfo ResultrepInfo = JsonUtil.getSingleBean(response.toString(), OrderResult.RepInfo.class);
                handResult(ResultrepInfo);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, JSONObject errorResponse) {
                //订单结果查询失败处理
            }
        });

    }
    //订单结果处理
    private void handResult(OrderResult.RepInfo repInfo){

        if (repInfo.orderNoStatue.equals("0")){
            Intent intent = new Intent();
            intent.putExtra("url", repInfo.detailUrl);
            setResult(resultOk, intent);
            finish();
        }else{
            //订单交易失败


        }

    }

    //确定付款
    public void cofirmPay(View view){

     if (map.containsKey("paytype")&&map.get("paytype").equals("1")){
         weiChatPay();
     }else if(map.containsKey("paytype")&&map.get("paytype").equals("2")){
         AlipayPay();
     }
    }
    //调用支付宝支付
    public void AlipayPay(){
        OrderBody boder  =new OrderBody();
        boder.builderReqInfo()
                .setUid((String) map.get("uid"))
                .setMoney((String) map.get("money"));


        OrderAlipayAPi alipay = new OrderAlipayAPi(boder.reqInfo);
        alipay.startRequest(alipay, new BaseRequest.RequestResult() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                final OrderBody.AlipayRepInfo alipayinfo  =
                        JsonUtil.getSingleBean(response.toString(),
                                OrderBody.AlipayRepInfo.class);
               PayManager.shareInstance().builderAlipay().openAlipaySdk(ComfirmMoney.this, alipayinfo.sign, new PayManager.PayStatue() {
                   @Override
                   public void PaySucess() {
                       handleResult(alipayinfo.orderNO);
                   }

                   @Override
                   public void PayFail() {
                       //handleResult(alipayinfo.orderNO);
                   }

                   @Override
                   public void PaySucessOnfirm() {
                       handleResult(alipayinfo.orderNO);
                   }
               });
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable,
                                  JSONObject errorResponse) {

            }
        });

    }

    //调用微信支付
    public void  weiChatPay(){
        OrderBody boder  =new OrderBody();
        boder.repInfo= boder.builderRepInfo();
        boder.builderReqInfo()
                .setMoney((String) map.get("money"))
                .setSpbill_create_ip(DeviceUtil.ip(this.context))
                .setTradeType("APP")
                .setUid((String) map.get("uid"));

        OrderWeichatAPI Orderapi = new OrderWeichatAPI(boder.reqInfo);


        Orderapi.startRequest(Orderapi, new BaseRequest.RequestResult() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                final OrderBody.RepInfo repInfo = JsonUtil.getSingleBean(response.toString(), OrderBody.RepInfo.class);
                if (repInfo.return_code.equals("FAIL"))
                {

                    ToastUtil.showToast(repInfo.return_msg);
                    return;
                }

                PayManager.shareInstance().BuildWeiChat().setAppId(repInfo.appid)
                        .setNonceStr(repInfo.nonce_str)
                        .setPackageValue(getPackageName())
                        .setPartnerId(repInfo.mch_id)
                        .setPrepayId(repInfo.prepay_id)
                        .setSign(repInfo.sign).
                        toWXPay(ComfirmMoney.this,"false", new PayManager.PayStatue() {

                            @Override
                            public void PaySucess() {
                                //微信订单支付成功处理
                                handleResult(repInfo.order_no);
                            }

                            @Override
                            public void PayFail() {
                                //微信订单支付失败处理
                                //if (repInfo.order_no.length() > 5) {
                                 //   handleResult(repInfo.order_no);
                                //}
                            }

                            @Override
                            public void  PaySucessOnfirm(){
                                if (repInfo.order_no.length() > 5) {
                                    handleResult(repInfo.order_no);
                                }

                            }

                        });
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //网络请求失败处理

            }
        });
    }
}
