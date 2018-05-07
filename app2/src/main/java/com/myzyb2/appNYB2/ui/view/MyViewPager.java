package com.myzyb2.appNYB2.ui.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @ClassName: MyiewPager
 * @Description: TODO自定义ViewPager
 * @author Panyy
 * @date 2013 2013年11月7日 下午3:19:01
 *
 */
public class MyViewPager extends ViewPager {
	private boolean isSlipping = true;//可滑动标志位

	public MyViewPager(Context context) {
		super(context);
	}

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		if (!isSlipping) {
			return false;
		}
		return super.onInterceptTouchEvent(arg0);
	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		if (!isSlipping) {
			return false;
		}
		return super.onTouchEvent(arg0);
	}

	/**
	 *@Title: setSlipping
	 *@Description: TODO设置ViewPager是否可滑动
	 *@param isSlipping
	 */
	public void setSlipping(boolean isSlipping) {
		this.isSlipping = isSlipping;
	}
}
