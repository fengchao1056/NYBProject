package com.myzyb.appNYB.javabean;

import java.util.List;

/**
 * Created by cuiz on 2016/1/20.
 */
public class PersonalCenter {


    /**
     * id : 329
     * eid :
     * gname : 木图测试网点
     * address : 宣武区
     * img_url : http://www.zokay.cn/api/upload/img/20160130/CR-VuY3631454157487.jpg
     * province : 1
     * city : 2
     * county : 6
     * type : dealer
     * main_items : 0
     * examine : 1
     * license : http://www.zokay.cn/api/upload/img/20151231/CR-zLC3631451546119.png
     * weight_total : 0
     * integral : 482704
     * funds : 0
     * ems_id : 327
     * ctime : 1451546119
     * is_show : yes
     * bc_id : 6225474188017275
     * bc_name : 吉日木图
     * bc_area : 中国招商银行
     * auto_info : [{"auto_model":"高尔夫7","auto_id":"冀AH5N3","auto_load":"7吨"}]
     * pay_type : online
     * share_id : 0
     * ext_id : 8153642
     * uid : 363
     * dealer_num : 0
     * remark :
     * mtime : 1453817468
     * uname : 木图
     * money : 4703.38
     * area_id : 6
     * area_name : 华北
     * pros : 北京
     * citys : 北京市
     * towns : 宣武区
     */

    public String id;
    public String eid;
    public String gname;
    public String address;
    public String img_url;
    public String province;
    public String city;
    public String county;
    public String type;
    public String main_items;
    public String examine;
    public String license;
    public String weight_total;
    public String integral;
    public String funds;
    public String ems_id;
    public String ctime;
    public String is_show;
    public String bc_id;
    public String bc_ids;
    public String bc_name;
    public String identity;
    public String bc_area;
    public String pay_type;
    public String share_id;
    public String ext_id;
    public String uid;
    public String dealer_num;
    public String remark;
    public String mtime;
    public String uname;
    public String money;
    public String area_id;
    public String area_name;
    public String pros;
    public String citys;
    public String towns;
    /**
     * auto_model : 高尔夫7
     * auto_id : 冀AH5N3
     * auto_load : 7吨
     */

    private List<AutoInfoEntity> auto_info;


    public static class AutoInfoEntity {
        private String auto_model;
        private String auto_id;
        private String auto_load;


    }
}
