package com.myzyb.appNYB.ui.activity.regiser;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import com.myzyb.appNYB.R;
import com.myzyb.appNYB.ui.adapter.widgetadapter.ArrayWheelAdapter;
import com.myzyb.appNYB.ui.view.widgetview.OnWheelChangedListener;
import com.myzyb.appNYB.ui.view.widgetview.WheelView;
import com.myzyb.appNYB.common.Constant;

/**
 *省市县三级联动
 */

public class WidgetActivity extends WidgetBaseActivity implements OnClickListener, OnWheelChangedListener {
    private WheelView mViewProvince;
    private WheelView mViewCity;
    private WheelView mViewDistrict;
    private Button mBtnConfirm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget);
        application.addActvity(this);
        getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        setUpViews();
        setUpListener();
        setUpData();

    }

    private void setUpViews() {
        mViewProvince = (WheelView) findViewById(R.id.id_province);
        mViewCity = (WheelView) findViewById(R.id.id_city);
        mViewDistrict = (WheelView) findViewById(R.id.id_district);
        mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
    }

    private void setUpListener() {

        mViewProvince.addChangingListener(this);

        mViewCity.addChangingListener(this);

        mViewDistrict.addChangingListener(this);

        mBtnConfirm.setOnClickListener(this);
    }

    private void setUpData() {
        initProvinceDatas();
        mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(WidgetActivity.this, mProvinceDatas));
        mViewProvince.setVisibleItems(7);
        mViewCity.setVisibleItems(7);
        mViewDistrict.setVisibleItems(7);
        updateCities();
        updateAreas();
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        // TODO Auto-generated method stub
        if (wheel == mViewProvince) {
            updateCities();
        } else if (wheel == mViewCity) {
            updateAreas();
        } else if (wheel == mViewDistrict) {
            if (newValue != 0) {
                mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
                mCurrentDistrictId = mDistrictIdDatasMap.get(mCurrentCityName + mCurrentDistrictName);
            } else {
                mCurrentDistrictName = "";
                mCurrentDistrictId = "";
            }

        }
    }


    private void updateAreas() {
        int pCurrent = mViewCity.getCurrentItem();
        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
        String[] areas = mDistrictDatasMap.get(mCurrentCityName);
        if (areas.length == 0) {
            areas = new String[]{""};
            mCurrentDistrictName = "";
            mCurrentDistrictId = "";
        } else {
            mCurrentDistrictName = areas[0];
            mCurrentProviceId = mProvinceIdDatasMap.get(mCurrentProviceName);
            mCurrentCityId = mCityIdDatasMap.get(mCurrentCityName);
            mCurrentDistrictId = mDistrictIdDatasMap.get(mCurrentCityName + mCurrentDistrictName);
        }
        mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
        mViewDistrict.setCurrentItem(0);


    }


    private void updateCities() {
        int pCurrent = mViewProvince.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        mCurrentCityName = cities[mViewCity.getCurrentItem()];
        mCurrentCityId = mCityIdDatasMap.get(mCurrentCityName);
        mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
        mViewCity.setCurrentItem(0);
        updateAreas();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                Intent intent = new Intent();
                intent.putExtra("mCurrentProviceName", mCurrentProviceName);
                intent.putExtra("mCurrentCityName", mCurrentCityName);
                intent.putExtra("mCurrentDistrictName", mCurrentDistrictName);
                intent.putExtra("mCurrentProviceId", mCurrentProviceId);
                intent.putExtra("mCurrentCityId", mCurrentCityId);
                intent.putExtra("mCurrentDistrictId", mCurrentDistrictId);
                setResult(Constant.RESULT_OK, intent);
                finish();
                break;
            default:
                break;
        }
    }
}
