package com.rs.wxmgr.wechat.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

public class WXFileUtils {
	
	public static String GetSuffix(String filename) {
		int position = filename.lastIndexOf(".");
		if(position == -1) {
			return null;
		}
		return filename.substring(position + 1);
	}
	
	
	   //产品名称:云通信短信API产品,开发者无需替换
    static final String product = "Dysmsapi";
    //产品域名,开发者无需替换
    static final String domain = "dysmsapi.aliyuncs.com";

    // TODO 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
    static final String accessKeyId = "LTAIwXTvKEUC6zIx";
    static final String accessKeySecret = "XVHSiIL7uLtJYbd5bUX53o49LEyS6p";

    public static  SendSmsResponse  sendSms(final String name,final String telephone,final String id_card,final String appeal_time) throws ClientException {

        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(telephone);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName("桃娃智能助手");
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode("SMS_126462030");
        
        String value = new StringBuilder("{\"appeal_name\":\"").append(name).append("\",\"appeal_time\":\"").append(appeal_time).append("\"}").toString();
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        //request.setTemplateParam("{\"appeal_name\":\"  小李子  +\",\"appeal_time\":\"2018-03-03 22:03:00\"}");
        request.setTemplateParam(value);
        
        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");

        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        //request.setOutId("yourOutId");

        //hint 此处可能会抛出异常，注意catch
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        
        System.out.println(sendSmsResponse.getMessage());
        return sendSmsResponse;
    }
	
}
