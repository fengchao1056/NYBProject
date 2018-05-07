package com.myzyb.appNYB.ui.activity.bank;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.myzyb.appNYB.R;
import com.myzyb.appNYB.common.Constant;
import com.myzyb.appNYB.ui.activity.main.BaseActivity;
import com.myzyb.appNYB.util.LogUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChooseBankActivity extends BaseActivity {

    @Bind(R.id.lv_dottype)
    ListView lvDottype;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    private String[] strs = new String[]{"中国工商银行", "中国农业银行", "中国银行", "中国建设银行", "中信银行", "中国光大银行", "中国邮政储蓄银行", "招商银行", "兴业银行"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dot_type);
        ButterKnife.bind(this);

        tvTitle.setText("请选择银行");
        lvDottype.setAdapter(new ArrayAdapter<String>(this,

                R.layout.list_checktextview, strs));

        lvDottype.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lvDottype.setDivider(null);
        lvDottype.setItemChecked(getIntent().getIntExtra("mPosition", 0), true);
        LogUtil.e("getExtra", getIntent().getIntExtra("mPosition", 0) + "");
        lvDottype.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                Intent intent = new Intent();
                String dotText = strs[arg2];
                LogUtil.e("list", arg2 + "");
                intent.putExtra("dotText", dotText);
                intent.putExtra("position", arg2);
                setResult(Constant.RESULT_OK, intent);
                finish();

            }
        });
    }
}
