package cms.epay.bean;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.TreeMap;

import cms.epay.function.EpayCoreFunction;
import cms.epay.function.EpayMD5Function;
import cms.web.action.payment.impl.pc.EpayConfig_PC;

/*
* @program: EpaySubmit
 * @description: 易支付请求接口提交类
* @author: Mr.Zou
* @create: 2021-10-22 11:18
*/
public class EpaySubmit {

	private EpayConfig_PC epay_config;
	private String alipay_gateway_new;
	
	public EpaySubmit(EpayConfig_PC epay_config) {
		this.epay_config = epay_config;
		this.alipay_gateway_new = epay_config.getApiurl()+"submit.php?";
	}
	
	public TreeMap<String,String> buildRequestPara(HashMap<String, String> parameter) throws Exception{
		//除去待签名参数数组中的空值和签名参数
		HashMap<String, String> para_filter =EpayCoreFunction.paraFilter(parameter);
		//对签名参数数组排序
		TreeMap<String, String> para_sort = EpayCoreFunction.argSort(para_filter);
		//生成签名结果
		String mysign = this.buildRequestMysign(para_sort);
		//签名结果与签名方式加入请求提交参数数组中
	
		para_sort.put("sign", mysign);
		para_sort.put("sign_type",epay_config.getSign_type().toUpperCase());
		return para_sort;
		
	}
	
	public String buildRequestMysign(TreeMap<String, String> para_sort) throws Exception{
		//把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
		String prestr = EpayCoreFunction.createLinkstring(para_sort);
		String mysign = EpayMD5Function.md5Sign(prestr.trim(),epay_config.getKey());
		return mysign;
	}
	
	/**
	 * 建立请求，以表单Html的形式构造
	 * @param parameter  请求参数数组
	 * @return
	 * @throws NoSuchAlgorithmException 
	 */
	public String buildRequestForm(HashMap<String, String> parameter) throws Exception{
		TreeMap<String,String> para = this.buildRequestPara(parameter);
		String sHtml = "<form id='alipaysubmit' name='alipaysubmit' action='"+this.alipay_gateway_new+"_input_charset="+this.epay_config.getInput_charset().toLowerCase().trim()+"' method='"+epay_config.getMethod()+"'>";
		//遍历处理过后的 Parameter，拼接字符串
		for(String key : para.keySet()){
			 sHtml+= "<input type='hidden' name='"+key+"' value='"+para.get(key)+"'/>";
		}
		sHtml+="<input type='submit' value='页面正在跳转，请稍后！' style='display:none'></form>";
		sHtml+="<script>document.forms['alipaysubmit'].submit();</script>";
		return sHtml;
	}
	
}
