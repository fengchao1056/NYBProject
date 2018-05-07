package com.myzyb.appNYB.ui.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by xialv on 2018/2/8.
 */

public class HomeViewPager extends ViewPager {
    private boolean isSlipping = true;//可滑动标志位

    public HomeViewPager(Context context) {
        super(context);
    }

    public HomeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        if (isSlipping){
            return false;
        }
        return super.onInterceptHoverEvent(event);
    }

    @Override
    public boolean onFilterTouchEventForSecurity(MotionEvent event) {
        if (isSlipping){
            return false;
        }
        return super.onFilterTouchEventForSecurity(event);
    }
    public void setisSlipping (boolean flag ){
        isSlipping = flag;//可滑动标志位
    }
}
