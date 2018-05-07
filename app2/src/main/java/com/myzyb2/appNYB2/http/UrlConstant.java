package com.myzyb2.appNYB2.http;

/**
 * Created by cuiz on 2015/12/22.
 */
public class UrlConstant {


    /**
     * 版本更新
     */
    public static final String UPDATE_VERSION = "?m=User&a=sysVersion";
    /**
     * 登录
     */
    public static final String LOGINURL = "?m=User&a=userLogin";

    /**
     * 网点信息
     */
    public static final String SELLERURL = "?m=Dealer&a=userDealer";

    /**
     * 修改密码
     */
    public static final String REPLACEPAW = "?m=User&a=modifyPasswd";
    /**
     * 忘记密码修改密码
     */
    public static final String FORGETREPLACEPAW = "?m=User&a=passWordUploadCode";

    /**
     * 忘记密码获取验证码
     */
    public static final String FORGETCODE = "?m=User&a=passWordUpload";

    /**
     * 待确认列表
     */
    public static final String WAITAGREE = "?m=Cartbargain&a=cartBarList";

    /**
     * 已取货
     */
    public static final String AREADYCLAIMGOODS = "?m=Order&a=orderList";
    /**
     * 上传头像
     */
    public static final String UPLOADYYZZ = "?m=Dealer&a=userHead";

    /**
     * 抢单提交
     */
    public static final String click_grab="?m=Cartbargain&a=robGrab";

    /**
     * 取消抢单
     */
    public static final String cancle_grab="?m=Cartbargain&a=robOut";

    /**
     * 抢单列表
     */
    public static final String show_grablist = "?m=Cartbargain&a=cartBarCountRob";
    /**
     * 物流展示网点
     */
    public static final String show_catshop = "?m=Cartbargain&a=cartBarCount";
    /**
     * 物流端点击网点到议价
     */
    public static final String click_catshop = "?m=Cartbargain&a=CartbargainAdd";
    /**
     * 物流端确认接口
     */
    public static final String replace_agree = "?m=Cartbargain&a=bargainStatus";
    /**
     * 物流端付款接口
     */
    public static final String pay_agree = "?m=Order&a=orderAdd";
    /**
     * 物流端返回
     */
    public static final String back_wl = "?m=Cart&a=cartStatus";
    /**
     * 明细列表
     */
    public static final String pay_list = "?m=Pay&a=getHistory";
    /**
     * 银行卡验证
     */
    public static final String Bank_card = "?m=Dealer&a=bankCards";
    /**
     * 银行卡支付密码
     */
    public static final String Bank_passwd = "?m=Dealer&a=stpasswd_vars";
    /**
     * 解绑
     */
    public static final String Un_bank = "?m=Dealer&a=bankUnbundlings";
    /**
     * 修改支付密码
     */
    public static final String replace_passwd = "?m=Dealer&a=stpasswdUp";
    /**
     * 重置支付密码
     */
    public static final String resetting_passwd = "?m=Dealer&a=forgetStpasswd";
    /**
     * 忘记支付密码获取验证码
     */
    public static final String get_code = "?m=Dealer&a=stpasswdCode";
    /**
     * 忘记支付密码验证
     */
    public static final String validate_code = "?m=Dealer&a=forgetStpasswdUP";
    /**
     * 充值提现，理财余额
     */
    public static final String balance_lc = "?m=Info&a=manage";
    /**
     * 获取银行卡号
     */
    public static final String bank_number = "?m=Dealer&a=groupBank";
    /**
     * 充值提现
     */
    public static final String pay_and_get = "?m=Order&a=payOrder";
    /**
     * 个人中心展示网点列表
     */
    public static final String Dealer_show = "?m=Dealer&a=emsDealer";

    /**
     * 已取货列表选择时间
     */
    public static final String Time_Already_get = "?m=Order&a=selectOrder";
    /**
     * 积分说明，协议条款
     */
    public static final String JF_TK = "?m=User&a=getExplain";

    /**
     * 修正价范围系数
     */

    public static final String FIXED_PRICE = "?m=Info&a=section";
    /**
     * 是否有密码
     */

    public static final String HAS_PAWD = "?m=Dealer&a=stpasswzCard";


    /**
     *
     * 添加报价
     */

    public static final  String  RecoveryPrice ="?m=User&a=getRecoveryPrice";
}
