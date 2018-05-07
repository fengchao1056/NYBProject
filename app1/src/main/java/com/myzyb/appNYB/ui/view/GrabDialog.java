package com.myzyb.appNYB.ui.view;




import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.myzyb.appNYB.R;

/**
 * 自定义对话框
 */
public class GrabDialog {
	private Context context;
	private Dialog dialog;
	private LinearLayout lLayout_bg;
	private Button btn_call;
	private Display display;

	public GrabDialog(Context context) {
		this.context = context;
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		display = windowManager.getDefaultDisplay();
	}

	public GrabDialog builder() {
		// 获取Dialog布局
		View view = LayoutInflater.from(context).inflate(
				R.layout.view_grabdialog, null);

		// 获取自定义Dialog布局中的控件
		lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
		btn_call = (Button) view.findViewById(R.id.btn_call);
		// 定义Dialog布局和参数
		dialog = new Dialog(context, R.style.AlertDialogStyle);
		dialog.setContentView(view);
		view.findViewById(R.id.ib_cancle).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		// 调整dialog背景大小
		lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
				.getWidth() * 0.75), LayoutParams.WRAP_CONTENT));
		return this;
	}

	public GrabDialog setCancelable(boolean cancel) {
		dialog.setCancelable(cancel);
		return this;
	}
	public void setTelCall(OnClickListener mOnClickListener){
		btn_call.setOnClickListener(mOnClickListener);
	}

	public void show() {
		try {
			dialog.show();
		}catch (Exception e){
			e.getStackTrace();
		}
	}
}
