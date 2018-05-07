package com.myzyb.appNYB.ui.activity.regiser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.myzyb.appNYB.R;
import com.myzyb.appNYB.ui.activity.main.BaseActivity;
import com.myzyb.appNYB.common.Constant;
import com.myzyb.appNYB.util.LogUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *选择网点类型dialog
 */
public class DotTypeActivity extends BaseActivity {

    @Bind(R.id.lv_dottype)
    ListView lvDottype;
    private String[] strs = new String[]{"(DZM)电动自行车/助力车", "(EV/EVF)电动车", "(FM)小型阀控", "(DM)", "(DG)", "(GMEV)电动自行车"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dot_type);
        ButterKnife.bind(this);


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
