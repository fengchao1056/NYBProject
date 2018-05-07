package com.myzyb.appNYB.util;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

/**
 * 字符串工具类
 *
 * Created by RobX on 15/4/12.
 */
public final class Strings {

  private Strings() {
    // No instances.
  }

  public static boolean isBlank(CharSequence string) {
    return (string == null || string.toString().trim().length() == 0);
  }

  /**
   * true anyone is blank
   */
  public static boolean isBlank(CharSequence... strings) {
    boolean isBlank = false;
    if (strings != null) {
      for (CharSequence string : strings) {
        if (isBlank(string)) {
          return true;
        }
      }
      return false;
    } else {
      return false;
    }
  }

  public static String replaceBlank(String string) {
    if (string != null) {
      string = string.replaceAll("\\s", "");
    }
    return string;
  }

  public static double parseDouble(String string) {
    double result = 0;
    if (!Strings.isBlank(string)) {
      try {
        result = Double.parseDouble(string);
      } catch (NumberFormatException e) {
        e.printStackTrace();
        //Timber.e(e, "String parser double error");
      }
    }
    return result;
  }

  public static float parseFloat(String string) {
    float result = 0;
    if (!Strings.isBlank(string)) {
      try {
        result = Float.parseFloat(string);
      } catch (NumberFormatException e) {
        e.printStackTrace();
        //Timber.e(e, "String parser float error");
      }
    }
    return result;
  }

  public static int parseInt(String string) {
    int result = 0;
    if (!Strings.isBlank(string)) {
      try {
        result = Integer.parseInt(string);
      } catch (NumberFormatException e) {
        e.printStackTrace();
        //Timber.e(e, "String parser integer error");
      }
    }
    return result;
  }

  public static long parseLong(String string) {
    long result = 0;
    if (!Strings.isBlank(string)) {
      try {
        result = Long.parseLong(string);
      } catch (NumberFormatException e) {
        e.printStackTrace();
        //Timber.e(e, "String parser long error");
      }
    }
    return result;
  }

  /**
   * 精确计算两数之差
   *
   * @param arg1 总金额
   * @param arg2 首付金额
   * @return 贷款金额
   */
  public static double subtract(String arg1, String arg2) {
    double result = 0;
    if (isBlank(arg2)) {
      arg2 = "0";
    }
    if (!Strings.isBlank(arg1)) {
      BigDecimal decimal1 = new BigDecimal(arg1);
      BigDecimal decimal2 = new BigDecimal(arg2);
      result = decimal1.subtract(decimal2).doubleValue();
    }
    return result;
  }

  public static String inputStreamToString(InputStream is) throws IOException {
    ByteArrayOutputStream b = new ByteArrayOutputStream();
    int i;
    while ((i = is.read()) != -1) {
      b.write(i);
    }
    return b.toString("utf-8");
  }

  /**
   * 空数据格式化
   *
   * @param args 需要格式化对象
   * @return 格式化后对象
   */
  public static String removeNull(@NonNull String args) {
    String notNull = "0";
    if (TextUtils.isEmpty(args) || "null".equalsIgnoreCase(args)) {
      return notNull;
    }
    return args;
  }

  /**
   * 移除非数字字符，主要用来格式化手机号
   */
  public static String trimNonDigits(String args) {
    if (args == null || args.length() == 0) {
      return args;
    } else {

      StringBuilder sb = new StringBuilder();
      for (int i = 0, j = args.length(); i < j; i++) {
        char ch = args.charAt(i);
        if (Character.isDigit(args.charAt(i))) {
          sb.append(ch);
        }
      }
      return sb.toString();
    }
  }



  /**
   * 对姓名进行脱敏处理
   * @param name
   * @return
   */
  public static String sensitName(String name) {
    if (name == null || name.length() < 2) {
      return name;
    }
    return "*" + name.substring(1, name.length());
  }

  /**
   * 对手机号进行脱敏处理
   * @param phone
   * @return
   */
  public static String sensitPhoneNumb(String phone) {
    if (phone == null || phone.length() < 8) {
      return phone;
    }
    return phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4, phone.length());
  }

  /**
   * 对身份证号进行脱敏处理
   * @param
   * @return
   */
  public static String sensitIdentity(String id) {
    if (id == null || id.length() < 8) {
      return id;
    }
    return id.substring(0, 3) + "***********" + id.substring(id.length() - 4, id.length());
  }

  public static String trimNull(String s) {
    if (s == null) {
      return "";
    }
    return s;
  }
}
