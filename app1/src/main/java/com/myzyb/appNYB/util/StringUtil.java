package com.myzyb.appNYB.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xialv on 2018/2/1.
 */

public class StringUtil{
    //获取get url中的参数
    public static HashMap<String, String> getParamsMap(String url) {

        HashMap<String, String> map = new HashMap<String, String>();
        int start = url.indexOf("?");
        if (start >= 0) {
            String str = url.substring(start + 1);
            System.out.println(str);
            String[] paramsArr = str.split("&");
            for (String param : paramsArr) {
                String[] temp = param.split("=");
                map.put(temp[0], temp[1]);
            }
        }
        return map;
    }
}
