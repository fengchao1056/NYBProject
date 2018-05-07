package com.myzyb.appNYB.util;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cuiz on 2015/11/16.
 */

    public class ValidateUtil {
        public ValidateUtil() {
        }

        public static boolean isEmpty(String str) {
            return str == null || str.length() == 0;
        }

        public static boolean isEmpty(List list) {
            return list == null || list.size() == 0;
        }

        public static boolean isEmpty(Map map) {
            return map == null || map.size() == 0;
        }

        public static boolean isEmpty(Object object) {
            return object == null;
        }

        public static boolean isEmpty(Object[] object) {
            return object == null || object.length == 0;
        }

    /**
     * 手机号验证
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }/**
     * 密码验证
     *
     * @param
     * @return 验证通过返回true
     */
    public static boolean isPasswd(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[0-9a-zA-Z]{6,8}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

}

