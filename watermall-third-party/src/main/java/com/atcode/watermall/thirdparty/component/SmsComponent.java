package com.atcode.watermall.thirdparty.component;


import com.atcode.watermall.thirdparty.utils.HttpUtils;
import org.apache.http.HttpResponse;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 短信发送验证组件
 */
@ConfigurationProperties(prefix = "spring.cloud.sms")
@Component
public class SmsComponent {
    private String host;  //API 主机
    private String path;  //API 访问路径
    private String method;  //访问方法
//    private String param; //内容模板
    private String appcode;  // 购买产品时生成的appcode
    private String smsSignId; //标志模板
    private String templateId;  //内容模板
    //TODO 可以抽取成配置服务，在配置文件中配置
    public void sendSmsCode(String phone , String code){
//        String host = "https://gyytz.market.alicloudapi.com";
//        String path = "/sms/smsSend";
//        String method = "POST";
//        String appcode = "b5ee94a931614741b7c69a77f879e348";


        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", phone);
        String param = "**code**:"+code+",**minute**:5";
//        querys.put("param", "**code**:12345,**minute**:5");
        querys.put("param", param);

//smsSignId（短信前缀）和templateId（短信模板），可登录国阳云控制台自助申请。参考文档：http://help.guoyangyun.com/Problem/Qm.html

        querys.put("smsSignId", smsSignId);
        querys.put("templateId", templateId);
        Map<String, String> bodys = new HashMap<String, String>();


        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getAppcode() {
        return appcode;
    }

    public void setAppcode(String appcode) {
        this.appcode = appcode;
    }

    public String getSmsSignId() {
        return smsSignId;
    }

    public void setSmsSignId(String smsSignId) {
        this.smsSignId = smsSignId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

//    public String getParam() {
//        return param;
//    }
//
//    public void setParam(String param) {
//        this.param = param;
//    }
}
