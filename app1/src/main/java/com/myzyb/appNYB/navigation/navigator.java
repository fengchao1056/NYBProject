package com.myzyb.appNYB.navigation;

import android.content.Context;

import java.util.HashMap;

/**
 * Created by xialv on 2018/2/2.
 */

 public final class navigator {
    static navigator shareInstance;
    public navigator shareInstance(){
        if (shareInstance == null){
            shareInstance = new navigator();
        }
        return shareInstance;
    }
    public void navigationToConfirmMoney(Context ctx, HashMap Map){

    }

}
