package com.myzyb.appNYB.ui.activity.additive;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myzyb.appNYB.R;
import com.myzyb.appNYB.ui.activity.main.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 等待物流付款对话框
 */

public class WaitEmsActivity extends BaseActivity {

    @Bind(R.id.tv_know)
    TextView tvKnow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_ems);
        ButterKnife.bind(this);
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        setFinishOnTouchOutside(false);
        tvKnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

            return false;
        }
        return super.onKeyDown(keyCode, event);
    }


}
