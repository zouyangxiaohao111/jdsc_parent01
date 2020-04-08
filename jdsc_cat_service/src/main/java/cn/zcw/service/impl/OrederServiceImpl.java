package cn.zcw.service.impl;

import cn.zcw.domain.TbOrder;
import cn.zcw.domain.TbOrderItem;
import cn.zcw.domain.TbPayLog;
import cn.zcw.groupentity.Cart;
import cn.zcw.mapper.TbOrderItemMapper;
import cn.zcw.mapper.TbOrderMapper;
import cn.zcw.mapper.TbPayLogMapper;
import cn.zcw.service.OrderService;
import cn.zcw.util.IdWorker;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 发布服务到zookeeper
 *
 */
@Service
@Transactional
public class OrederServiceImpl implements OrderService {
    @Autowired
    private TbOrderMapper tbOrderMapper;
    @Autowired
    private TbOrderItemMapper tbOrderItemMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private TbPayLogMapper tbPayLogMapper;

    @Override
    public void saveOrder(TbOrder tbOrder) {
//        从缓存中获取购物车集合
        String carListStr =(String) redisTemplate.boundValueOps(tbOrder.getUserId()).get();
//        将字符串数组转换为集合
        List<Cart> cartList = JSON.parseArray(carListStr, Cart.class);

//       设置订单号集合
        List<String> orderIdList = new ArrayList<>();
//        定义订单集合总价
        double allPrice =0.0;

        //        遍历购物车集合,每个购物车就是一个订单号
        for (Cart cart : cartList) {
            //        设置订单表数据
            TbOrder order = new TbOrder();

            //        生成订单id
            long orderId = idWorker.nextId();

            //        设置订单id
            order.setOrderId(orderId);

            order.setCreateTime(new Date());

            order.setPaymentTime(new Date());

            order.setUpdateTime(new Date());

            //            设置订单用户名

            order.setUserId(tbOrder.getUserId());

            //            设置商家id
            order.setSellerId(cart.getSellerId());

            //            设置付款类型：1在线支付  2货到付款
            order.setPaymentType(tbOrder.getPaymentType());

            //            设置联系人
            order.setReceiver(tbOrder.getReceiver());

            //            设置收货地址
            order.setReceiverAreaName(tbOrder.getReceiverAreaName());

            //            设置联系人电话
            order.setReceiverMobile(tbOrder.getReceiverMobile());

            //            设置付款状态
            order.setStatus("1");

            //            设置付款来源
            order.setSourceType(tbOrder.getSourceType());
            double money = 0;

//            获取订单项
            List<TbOrderItem> orderItemList = cart.getOrderItemList();

            for (TbOrderItem tbOrderItem : orderItemList) {
//                设置订单id
                tbOrderItem.setOrderId(orderId);

                tbOrderItem.setId(idWorker.nextId());

                money+=tbOrderItem.getTotalFee().doubleValue();
//                保存订单项

                tbOrderItemMapper.insert(tbOrderItem);
            }
//            设置一个订单的支付金额（也就是一个商家）
            order.setPayment(new BigDecimal(money));
            // 所有订单的总金额
            allPrice = order.getPayment().doubleValue();
//            保存订单
            tbOrderMapper.insert(order);
//            添加订单号到订单号集合中,首先将long 类型订单号转换成string
            String orderIdd = String.valueOf(orderId);
            // 添加至集合
            orderIdList.add(orderIdd);
        }
//        设置支付日志
        TbPayLog tbPayLog = new TbPayLog();
//        设置订单创建时间
        tbPayLog.setCreateTime(new Date());
//        设置订单支付状态
        tbPayLog.setTradeState("0");
//        订单表id
        tbPayLog.setOutTradeNo(idWorker.nextId()+"");
//        设置支付方式
        tbPayLog.setPayType(tbOrder.getPaymentType());
//        将订单集合放入订单表
        tbPayLog.setOrderList(vOf(orderIdList));
//        设置订单总额,单位是分
        tbPayLog.setTotalFee((long) (allPrice*100));
//        设置用户id
        tbPayLog.setUserId(tbOrder.getUserId());
//        保存订单表
        tbPayLogMapper.insert(tbPayLog);
        //        清除缓存
        redisTemplate.delete(tbOrder.getUserId());
//        将订单号集合存入redis中
        redisTemplate.boundHashOps("pay_log").put(tbPayLog.getUserId(),tbPayLog);
//        清除缓存
        redisTemplate.delete(tbOrder.getUserId());
    }

    /**
     * list转换String字符串
     * @param orderIdList
     * @return
     */
    private String vOf(List<String> orderIdList){
        return org.apache.commons.lang.StringUtils.join(orderIdList.toArray(),",");
    }

    /**
     * 将数据保存到redis中
     * @param userId
     * @param orderIdList
     */
    private void saveOrderRedis(String userId, List<String> orderIdList) {
        String orderIds = JSON.toJSONString(orderIdList);
        redisTemplate.boundValueOps(userId).set(orderIds,2, TimeUnit.DAYS);
    }
}
