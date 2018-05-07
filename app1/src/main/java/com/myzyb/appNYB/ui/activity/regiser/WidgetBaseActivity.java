package com.myzyb.appNYB.ui.activity.regiser;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import android.content.res.AssetManager;

import com.myzyb.appNYB.javabean.CityModel;
import com.myzyb.appNYB.javabean.DistrictModel;
import com.myzyb.appNYB.javabean.ProvinceModel;
import com.myzyb.appNYB.service.XmlParserHandler;
import com.myzyb.appNYB.ui.activity.main.BaseActivity;

public class WidgetBaseActivity extends BaseActivity {

    protected String[] mProvinceDatas;

    protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();

    protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

    protected Map<String, String> mDistrictIdDatasMap = new HashMap<String, String>();
    protected Map<String, String> mProvinceIdDatasMap = new HashMap<String, String>();
    protected Map<String, String> mCityIdDatasMap = new HashMap<String, String>();


    protected String mCurrentProviceName = "";

    protected String mCurrentCityName = "";

    protected String mCurrentDistrictName = "";


    protected String mCurrentDistrictId = "";
    protected String mCurrentProviceId = "";
    protected String mCurrentCityId = "";


    protected void initProvinceDatas() {
        List<ProvinceModel> provinceList = null;
        AssetManager asset = getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            provinceList = handler.getDataList();
            //*/ 初始化默认选中的省、市、区
            if (provinceList != null && !provinceList.isEmpty()) {
                mCurrentProviceName = provinceList.get(0).getName();
                mCurrentProviceId = provinceList.get(0).getId();

                List<CityModel> cityList = provinceList.get(0).getCityList();
                if (cityList != null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).getName();
                    mCurrentCityId = cityList.get(0).getId();
                    List<DistrictModel> districtList = cityList.get(0).getDistrictList();
                    mCurrentDistrictName = districtList.get(0).getName();
                    mCurrentDistrictId = districtList.get(0).getId();
                }
            }
            mProvinceDatas = new String[provinceList.size()];
            for (int i = 0; i < provinceList.size(); i++) {
                mProvinceDatas[i] = provinceList.get(i).getName();
                mProvinceIdDatasMap.put(provinceList.get(i).getName(), provinceList.get(i).getId());
                List<CityModel> cityList = provinceList.get(i).getCityList();
                String[] cityNames = new String[cityList.size()];
                for (int j = 0; j < cityList.size(); j++) {
                    mCityIdDatasMap.put(cityList.get(j).getName(), cityList.get(j).getId());
                    cityNames[j] = cityList.get(j).getName();
                    List<DistrictModel> districtList = cityList.get(j).getDistrictList();
                    String[] distrinctNameArray = new String[districtList.size()];
                    DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
                    for (int k = 0; k < districtList.size(); k++) {

                        DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(), districtList.get(k).getId());

                        mDistrictIdDatasMap.put(cityList.get(j).getName() + districtList.get(k).getName(), districtList.get(k).getId());
                        distrinctArray[k] = districtModel;
                        distrinctNameArray[k] = districtModel.getName();
                    }

                    mDistrictDatasMap.put(cityNames[j], distrinctNameArray);


                }

                mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }
    }

}
