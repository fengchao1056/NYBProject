package com.myzyb.appNYB.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.myzyb.appNYB.Manager.PayManager;
import com.myzyb.appNYB.common.Constant;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


//public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
public class WXEntryActivity extends Activity  implements IWXAPIEventHandler{
	private  IWXAPI api;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		api = WXAPIFactory.createWXAPI(this, Constant.WeiChat_AppID);
		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}
	@Override
	public void onReq(BaseReq baseReq) {

	}

	@Override
	public void onResp(BaseResp resp) {
		if(resp.getType()== ConstantsAPI.COMMAND_PAY_BY_WX){
			if(resp.errCode==0){
				Toast.makeText(this, "支付成功", Toast.LENGTH_LONG).show();
				PayManager.shareInstance().PayBuilder.PaySucess();
			}
			else {
				Toast.makeText(this, "支付失败", Toast.LENGTH_LONG).show();
				PayManager.shareInstance().PayBuilder.PayFail();
			}
			finish();
		}
	}



}