package cms.bean.payment;

import java.io.Serializable;

/*
* @program: Epay
* @description: 易支付参数
* @author: Mr.Zou
* @create: 2021-10-21 21:43
*/
public class Epay implements Serializable {
    private static final long serialVersionUID = 1284080713919267981L;

    /** 易支付的ID **/
    private String app_id="";
    /** 易支付的密钥 **/
    private String epay_public_key="";

    public String getApp_id() {
        return app_id;
    }
    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }
    public String getEpay_public_key() {
        return epay_public_key;
    }
    public void setEpay_public_key(String alipay_public_key) {
        this.epay_public_key = alipay_public_key;
    }
}
