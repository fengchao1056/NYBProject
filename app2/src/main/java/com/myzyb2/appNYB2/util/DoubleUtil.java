package com.myzyb2.appNYB2.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by cuiz on 2015/12/29.
 */
public class DoubleUtil {
    public static String doubleXintToString(double d, int i) {

        BigDecimal a = new BigDecimal(i);
        BigDecimal b = BigDecimal.valueOf(d);
        String s = String.valueOf(a.multiply(b).doubleValue());
        return s;
    }

    public static double doubleXintToDouble(double d, int i) {
        BigDecimal a = new BigDecimal(i);
        BigDecimal b = BigDecimal.valueOf(d);
        return a.multiply(b).doubleValue();
    }

    public static double doubleFormat(double d) {
        DecimalFormat df = new DecimalFormat("#.##");
        double v = Double.parseDouble(df.format(d));
        return v;
    }

   
}
