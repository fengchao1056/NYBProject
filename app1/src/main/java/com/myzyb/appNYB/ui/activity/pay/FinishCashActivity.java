package com.myzyb.appNYB.ui.activity.pay;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.myzyb.appNYB.R;
import com.myzyb.appNYB.ui.activity.main.BaseActivity;
import com.myzyb.appNYB.ui.activity.main.MainActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *提现完成界面
 */
public class FinishCashActivity extends BaseActivity {

    @Bind(R.id.imb_back)
    ImageButton imbBack;
    @Bind(R.id.tv_bank_zd)
    TextView tvBankZd;
    @Bind(R.id.tv_price)
    TextView tvPrice;
    @Bind(R.id.bt_next)
    Button btNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_cash);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String bank_zd = intent.getStringExtra("bank_zd");
        String imput_money = intent.getStringExtra("imput_money");
        tvBankZd.setText(bank_zd);
        tvPrice.setText(imput_money);
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(FinishCashActivity.this, MainActivity.class);
                intent1.putExtra("id", 2);
                startActivity(intent1);
            }
        });
        imbBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
