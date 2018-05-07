package com.myzyb.appNYB;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.myzyb.appNYB.common.Constant;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class AppRegister extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		final IWXAPI api = WXAPIFactory.createWXAPI(context, null);

//		 ����appע�ᵽ΢��
		api.registerApp(Constant.WeiChat_AppID);
	}
}
