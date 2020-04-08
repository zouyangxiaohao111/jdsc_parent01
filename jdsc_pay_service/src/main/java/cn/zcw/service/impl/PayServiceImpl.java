package cn.zcw.service.impl;

import cn.zcw.domain.TbPayLog;
import cn.zcw.mapper.TbPayLogMapper;
import cn.zcw.service.PayService;
import cn.zcw.util.HttpClient;
import cn.zcw.util.IdWorker;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class PayServiceImpl implements PayService {
    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private TbPayLogMapper tbPayLogMapper;

    /**
     * 调用统一下单接口
     *
     * @return
     */
    @Override
    public Map<String, String> createNative(String userId) throws Exception {

        // 从redis中获取数据
        TbPayLog log = (TbPayLog) redisTemplate.boundHashOps("pay_log").get(userId);

        // 订单号 生成订单号
        // String orderNo = idWorker.nextId() + "";
        // 支付的金额
//        String money = "1";

        // 获取订单编号
        String orderNo = log.getOutTradeNo();
        // 获取总金额
         Long totalFee = log.getTotalFee();

        // 1.创建参数
        Map<String, String> param = new HashMap();
        // 公众号
        param.put("appid", "wx0609f8351dca9750");
        // 商户号
        param.put("mch_id", "1536725911");
        // 随机字符串
        param.put("nonce_str", WXPayUtil.generateNonceStr());
        // 商品描述
        param.put("body", "交大商城支付");
        // 商户订单号
        param.put("out_trade_no", orderNo);
        // 总金额（分）
        param.put("total_fee", String.valueOf(totalFee));
        // IP
        param.put("spbill_create_ip", "127.0.0.1");
        // 回调地址
        param.put("notify_url", "http://www.txjava.cn");
        // 交易类型
        param.put("trade_type", "NATIVE");

        //2.生成要发送的xml
        String xmlParam = WXPayUtil.generateSignedXml(param, "jiaodashangchengzcw0123456789123");
        // 输出要发送的xml数据
        System.out.println(xmlParam);
        // 远程发送
        HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
        client.setHttps(true);
        client.setXmlParam(xmlParam);
        client.post();
        //3.获得结果
        String result = client.getContent();
        System.out.println(result);

        // 返回的结果
        Map<String, String> resultMap = WXPayUtil.xmlToMap(result);

        // 存储商户订单号
        resultMap.put("out_trade_no", orderNo);
        //需要支付的金额
        resultMap.put("total_fee", String.valueOf(totalFee));

        return resultMap;
    }

    /**
     * 查询交易状态
     *
     * @param orderCode
     * @return
     */
    @Override
    public Map<String, String> queryPayStatus(String orderCode) throws Exception {
        // 查询支付状态的请求地址
        HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
        // 调用微信的查询订单支付状态API
        Map<String, String> paramMap = new HashMap<String, String>();
        // 公众账号ID
        paramMap.put("appid", "wx0609f8351dca9750");
        // 商户号
        paramMap.put("mch_id", "1536725911");
        // 商户的订单号
        paramMap.put("out_trade_no", orderCode);
        // 随机字符串
        paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
        // 把paramMap转成xml  转xml时带有签名
        String paramXml = WXPayUtil.generateSignedXml(paramMap, "txjavayingmulaoshi01234567891234");
        // 设置参数
        httpClient.setXmlParam(paramXml);
        // 发送post请求
        httpClient.post();
        // 获取返回结果
        String content = httpClient.getContent();
        // 根据返回结果生成map集合
        Map<String, String> map = WXPayUtil.xmlToMap(content);
        // 返回结果
        return map;
    }

    /**
     * 修改支付状态
     * @param userId
     * @param transaction_id
     */
    @Override
    public void updatePayLogStatus(String userId, String transaction_id) {
        // 从redis中获取
        TbPayLog log = (TbPayLog) redisTemplate.boundHashOps("pay_log").get(userId);
        // 设置数据
        log.setPayTime(new Date());
        // 已经支付
        log.setTradeState("1");
        // 支付订单号
        log.setTransactionId(transaction_id);
        // 修改数据
        tbPayLogMapper.updateByPrimaryKey(log);
        // 从redis删除
        redisTemplate.boundHashOps("pay_log").delete(userId);
    }

}
