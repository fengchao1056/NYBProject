package com.myzyb.appNYB.http;

/**
 * Created by cuiz on 2015/12/22.
 */
public class UrlConstant {
    /**
     * 版本更新
     */
    public static final String UPDATE_VERSION = "?m=User&a=sysVersion";
    /**
     * 注册
     */
    public static final String REGISTERURL = "?m=User&a=userRegister";
    /**
     * 登录
     */
    public static final String LOGINURL = "?m=User&a=userLogin";
    /**
     * 添加网点
     */
    public static final String ADDSELLERURL = "?m=User&a=registerDealers";
    /**
     * 网点信息
     */
    public static final String SELLERURL = "?m=Dealer&a=userDealer";

    /**
     * 网点修改提交界面
     */
    public static final String ADDREPLACESELLERURL = "?m=Dealer&a=dealerUpDos";
    /**
     * 修改密码
     */
    public static final String REPLACEPAW = "?m=User&a=modifyPasswd";
    /**
     * 忘记密码修改密码
     */
    public static final String FORGETREPLACEPAW = "?m=User&a=passWordUploadCode";

    /**
     * 获取验证码
     */
    public static final String GETCODE = "?m=User&a=registerCode";
    /**
     * 忘记密码获取验证码
     */
    public static final String FORGETCODE = "?m=User&a=passWordUpload";
    /**
     * 分类
     */
    public static final String CHOOSEMODLE = "?m=Info&a=getCatList";
    /**
     * 容量
     */
    public static final String BATTERYVOLUME = "?m=Info&a=getCapList";
    /**
     * 电池
     */
    public static final String BATTERY = "?m=Info&a=getInfoList";
    /**
     * 回收价
     */
    public static final String RECOVERYPRICE = "?m=User&a=getRecoveryPrice";
    /**
     * 待取货列表
     */
    public static final String WAITCLAIMGOODS = "?m=Cart&a=cartList";
    /**
     * 添加购物车
     */
    public static final String AddBUYCAR = "?m=Cart&a=cartAdd";
    /**
     * 删除电池
     */
    public static final String DELBATTERY = "?m=Cart&a=cartDel";
    /**
     * 修改电池数量
     */
    public static final String UpdateBATTERY = "?m=Cart&a=cartUpdate";
    /**
     * 待确认列表
     */
    public static final String WAITAGREE = "?m=Cartbargain&a=cartBarList";
    /*** 已取货
     *
     */
    public static final String AREADYCLAIMGOODS = "?m=Order&a=orderList";
    /**
     * 确认驳回
     */
    public static final String AGREEORNOT = "?m=Cartbargain&a=bargainStatus";
    /**
     * 上传头像
     */
    public static final String UPLOADYYZZ = "?m=Dealer&a=userHead";
    /**
     * 上传营业执照
     */
    public static final String UPLOADHEADPHOTO = "upload/img/papers";

    /**
     * 修改地址
     */
    public static final String Replace_Adress = "?m=Dealer&a=addressUps";
    /**
     * 明细列表
     */
    public static final String pay_list = "?m=Pay&a=getHistory";
    /**
     * 充值提现
     */
    public static final String pay_and_get = "?m=Order&a=payOrder";
    /**
     * 积分说明，协议条款
     */
    public static final String JF_TK = "?m=User&a=getExplain";
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
     * h5入口url
     *
     */
    public static final String h5Index ="index.php?s=/Index/purchase.html";

    /**
     * 微信下单接口
     *
     */
    public static final String WeChat ="index.php?s=/Shop/Pay/unifiedorder";
    /**
     * 支付宝生成authSing接口
     *
     */
    public static final String alipay ="/index.php?s=/Shop/Pay/Alipay";

    /**
     * 获取支付结果
     *
     */
    public static final String PayResult ="/index.php?s=/Home/User/WeiChatPayResult";

}
