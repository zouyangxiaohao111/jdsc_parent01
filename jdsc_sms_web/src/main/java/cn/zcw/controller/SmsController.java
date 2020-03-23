package cn.zcw.controller;

import cn.zcw.entity.Result;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/sms")
public class SmsController {


    /**
     * 发送短信验证码的方法
     * @param phone    手机号
     * @param code     短信验证码
     * @return
     */
    @RequestMapping("/send/{phone}/{code}")
    public Result send(@PathVariable("phone") String phone, @PathVariable("code") String code){
        try {
            //产品名称:云通信短信API产品,开发者无需替换
            String product = "Dysmsapi";
            //产品域名,开发者无需替换
            String domain = "dysmsapi.aliyuncs.com";

            // 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)，非常重要。不要泄露给其他人
            String accessKeyId = "LTAI4FiA6YVrBpZUiqnSExNZ";
            String accessKeySecret = "d2kouupeOmZGlrOFMqB7DoiGFI6WJP";

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
            request.setPhoneNumbers(phone);

            //必填:短信签名-可在短信控制台中找到
            request.setSignName("交大商城");
            //必填:短信模板-可在短信控制台中找到
            request.setTemplateCode("SMS_185212147");

            //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
            request.setTemplateParam("{\"code\":\""+code+"\"}");
            //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
            request.setOutId("yourOutId");

            // ========================需要大家填写的内容结束======================================

            //hint 此处可能会抛出异常，注意catch
            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

            // 创建Map集合，封装数据
            Map<String,Object> map = new HashMap();

            map.put("code", sendSmsResponse.getCode());
            map.put("message", sendSmsResponse.getMessage());
            map.put("requestId", sendSmsResponse.getRequestId());
            map.put("bizId", sendSmsResponse.getBizId());

            // 返回result
            return new Result(true,"发送短信成功",map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,"发送短信失败");
        }
    }
}
