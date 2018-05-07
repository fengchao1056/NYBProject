package com.myzyb.appNYB.util;

import java.util.regex.Pattern;

/**
 * 检查工具类
 *
 * Created by lawson on 15/4/28.
 */
public final class CheckUtils {
  public static final int CAPTCHA_TIME_LIMITED = 60;

  /** 中文英文数字 */
  private static final Pattern NON_SPECIAL_CHARACTERS_PATTERN =
      Pattern.compile("^[a-zA-Z0-9\\u4E00-\\u9FA5]+$");
  /** 汉字开始与结尾，中间可为"·" */
  private static final Pattern USERNAME_PATTERN =
      Pattern.compile("^[\\u4E00-\\u9FA5]([\\u4E00-\\u9FA5]|·){0,18}[\\u4E00-\\u9FA5]$");
  /** 身份证 */
  private static final Pattern IDENTITY_NUMBER_PATTERN = Pattern.compile("^[0-9]{17}([0-9]|x|X)$");
  /** 手机号码 */
  //private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^1[0-9]{10}$");
  private static final Pattern PHONE_NUMBER_PATTERN =
      Pattern.compile("((13[0-9])|(14[579])|(15[0-35-9])|(17[013678])|(18[0-9]))\\d{8}");
  /** 固定电话 */
  private static final Pattern FIXEDLINE_NUMBER_PATTERN = Pattern.compile("^0[0-9]{10,11}$");
  /** 数字 */
  private static final Pattern NUMBER_PATTERN = Pattern.compile("^[0-9]*$");
  /** 字母 */
  private static final Pattern ALPHABET_PATTERN = Pattern.compile("^[a-zA-Z]*$");
  /** 非数字雨字母 */
  private static final Pattern NON_NUMBER_AND_ALPHABET_PATTERN = Pattern.compile("^[^0-9a-zA-Z]*$");
  /** 中英文 */
  private static final Pattern CHINESE_AND_ALPHABET_PATTERN =
      Pattern.compile("^[a-zA-Z\\u4E00-\\u9FA5]+$");
  /** 密码（8-16位数字或字母,不包括特殊字符） */
  private static final Pattern PASSWORD_NUMBER_AND_ALPHABET_PATTERN =
      Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$");
  /** 地址-（汉字开始，可包含数字、英文、字母、字符，字符仅可以为 - . ( ) # 空格） */
  private static final Pattern ADDRESS_PATTERN =
      Pattern.compile("^[\\u4E00-\\u9FA5][0-9a-zA-Z\\u4E00-\\u9FA5-\\.\\\\()（）＃# ]{2,59}$");

  /**
   * 验证是否是6位相同的数字(用于交易密码的判断)
   */
  private static final Pattern PAY_PWD_PATTERN = Pattern.compile("^(?:([0-9])\\1{5})$");

  private CheckUtils() {
    // No instances.
  }

  public static boolean isUsernameValid(String username) {
    if (Strings.isBlank(username)) {
      return false;
    }
    int length = username.length();
    return !(length < 2 || length > 20) && USERNAME_PATTERN.matcher(username).matches();
  }

  public static boolean isIdentityNumberValid(String identityNumber) {
    if (Strings.isBlank(identityNumber)) {
      return false;
    }
    int length = identityNumber.length();
    return length == 18 && IDENTITY_NUMBER_PATTERN.matcher(identityNumber).matches();
  }

  public static boolean isFixedLineMobilePhoneNumberValid(String phoneNumber) {
    if (phoneNumber.startsWith("1")) {
      return isPhoneNumberValid(phoneNumber);
    } else if (phoneNumber.startsWith("0")) {
      return isFixedLinePhoneNumberValid(phoneNumber);
    }
    return false;
  }

  public static boolean isPhoneNumberValid(String phoneNumber) {
    if (Strings.isBlank(phoneNumber)) {
      return false;
    }
    int length = phoneNumber.length();
    return length == 11 && PHONE_NUMBER_PATTERN.matcher(phoneNumber).matches();
  }

  /**
   * 校验固定电话
   */
  public static boolean isFixedLinePhoneNumberValid(String phoneNumber) {
    if (Strings.isBlank(phoneNumber)) {
      return false;
    }
    int length = phoneNumber.length();
    return (length == 11 || length == 12) && FIXEDLINE_NUMBER_PATTERN.matcher(phoneNumber)
        .matches();
  }

  public static boolean isPayPasswordValid(String payPassword) {
    if (Strings.isBlank(payPassword)) {
      return false;
    }
    int length = payPassword.length();
    return length == 6 && NUMBER_PATTERN.matcher(payPassword).matches();
  }

  public static boolean isPayPasswordEasyValid(String payPassword) {
    int length = payPassword.length();
    int firstChar = payPassword.charAt(0);
    for (int i = 0; i < length; i++) {
      if (firstChar != payPassword.charAt(i)) {
        return false;
      }
    }
    return true;
  }

  public static boolean isPasswordValidNew(String password) {
    if (Strings.isBlank(password)) {
      return false;
    }
    return PASSWORD_NUMBER_AND_ALPHABET_PATTERN.matcher(password).matches();
  }

  public static boolean isCaptchaValid(String captcha) {
    if (Strings.isBlank(captcha)) {
      return false;
    }
    int length = captcha.length();
    return (length == 4 || length == 6) && NUMBER_PATTERN.matcher(captcha).matches();
  }

  public static boolean isNumberValid(String captcha) {
    if (Strings.isBlank(captcha)) {
      return false;
    }
    return NUMBER_PATTERN.matcher(captcha).matches();
  }

  public static boolean isBankcardNumberValid(String bankcardNumber) {
    if (Strings.isBlank(bankcardNumber)) {
      return false;
    }
    int length = bankcardNumber.length();
    return length > 13 && length < 20 && NUMBER_PATTERN.matcher(bankcardNumber).matches();
  }

  /**
   * 验证是否为大于0的数字
   *
   * @param arg 需要验证的字段
   */
  public static boolean isPositiveNumberValid(String arg) {
    if (Strings.isBlank(arg)) {
      return false;
    }
    double number = -1;
    try {
      number = Double.parseDouble(arg);
    } catch (NumberFormatException e) {
      e.printStackTrace();
    }
    return number > 0;
  }

  public static boolean isChineseAlphabetValid(String string) {
    return !Strings.isBlank(string) && CHINESE_AND_ALPHABET_PATTERN.matcher(string).matches();
  }

  /**
   * 验证中文字母数字
   *
   * @param arg 需要验证的字段
   */
  public static boolean isNonSpecialCharValid(String arg) {
    return NON_SPECIAL_CHARACTERS_PATTERN.matcher(arg).matches();
  }

  public static boolean isAddressVaild(String arg) {
    return ADDRESS_PATTERN.matcher(arg).matches() ;
  }

  public static boolean nameIsFirstChinese(String arg){
    return Pattern.compile("^[\\u4E00-\\u9FA5].{2,59}$").matcher(arg).matches();
  }

  public static boolean paypwd_to_simple(String arg){
    return PAY_PWD_PATTERN.matcher(arg).matches();
  }
}
