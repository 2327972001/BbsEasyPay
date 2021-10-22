package cms.web.action.payment.impl.pc;

import cms.bean.payment.Epay;
import cms.bean.payment.OnlinePaymentInterface;
import cms.epay.bean.EpaySubmit;
import cms.service.payment.PaymentService;
import cms.utils.JsonUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/*
* @program: EpayConfig_PC
* @description: 易支付配置
* @author: Mr.Zou
* @create: 2021-10-21 21:42
*/
@Component("epayConfig_PC")
public class EpayConfig_PC {
    @Resource PaymentService paymentService;

    //商户ID
    private String partner = "";
    //商户Key
    private String key = "";
    //签名方式不用更改
    private String sign_type = "MD5";
    //字符编码格式，目前支持GBK或 utf-8
    private String input_charset = "utf-8";
    //访问模式,根据自己的服务器是否支持ssl访问，若支持请选择https；若不支持请选择http
    private String transport = "http";
    //表单请求方式 post或get
    private String method = "post";
    //支付API地址
    private String apiurl = "http://pay.mhy12.com/";

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }
    public String getSign_type() { return sign_type; }
    public void setSign_type(String sign_type) { this.sign_type = sign_type; }
    public String getInput_charset() { return input_charset; }
    public void setInput_charset(String input_charset) { this.input_charset = input_charset; }
    public String getTransport() { return transport; }
    public void setTransport(String transport) { this.transport = transport; }
    public String getApiurl() { return apiurl; }
    public void setApiurl(String apiurl) { this.apiurl = apiurl; }
    public String getPartner() { return partner; }
    public void setPartner(String partner) { this.partner = partner; }
    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    //获取pid和key
    //off=1 返回pid
    //off!=1 返回key
    private synchronized String update(Integer interfaceProduct,int off){
        String pid = null;
        String key = null;
        List<OnlinePaymentInterface> onlinePaymentInterfaceList = paymentService.findAllEffectiveOnlinePaymentInterface_cache();
        for(OnlinePaymentInterface onlinePaymentInterface : onlinePaymentInterfaceList){
            //接口产品
            if(onlinePaymentInterface.getInterfaceProduct().equals(interfaceProduct)){
                if(onlinePaymentInterface.getDynamicParameter() != null && !"".equals(onlinePaymentInterface.getDynamicParameter().trim())){
                    Epay alipay = JsonUtils.toObject(onlinePaymentInterface.getDynamicParameter(), Epay.class);
                    pid = alipay.getApp_id();
                    key = alipay.getEpay_public_key();
                }
            }
        }
        if(off==1){
            return pid;
        }else{
            return key;
        }
    }

    //拿到pid和key
    public void GetPidOrKey(Integer interfaceProduct){
        setPartner(update(interfaceProduct,1));
        setKey(update(interfaceProduct,0));
    }
}
