package cn.zcw.controller;

import cn.zcw.entity.Result;
import cn.zcw.service.PayService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/pay")
public class PayServiceController {

    @Reference
    private PayService payService;




        @RequestMapping("/createNative")
        public Result createNative() {
            try {
                // 获取登录的id
                String userId = SecurityContextHolder.getContext().getAuthentication().getName();
                // 调用service，统一下单接口
                Map<String, String> resultMap = payService.createNative(userId);
                // 返回结果
                return new Result(true, "下单成功", resultMap);
            } catch (Exception e) {
                e.printStackTrace();
                return new Result(false, "下单失败");
            }
        }

        /**
         * 查询订单
         *
         * @return
         */
        @RequestMapping("/queryPayStatus/{orderCode}")
        public Result queryPayStatus(@PathVariable String orderCode) {
            try {
                // 定义次数
                int times = 1;
                while (times <= 30) {
                    // 睡眠 5 秒
                    Thread.sleep(5000);
                    // 查询订单支付结果
                    Map<String, String> resultMap = payService.queryPayStatus(orderCode);
                    // 次数累加
                    times++;
                    // 根据返回的结果进行判断
                    if (resultMap.get("trade_state").equals("SUCCESS")) {

                        // 修改支付日志表的状态
                        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
                        // 获取transaction_id
                        String transaction_id = resultMap.get("transaction_id");
                        // 修改支付日志状态
                        payService.updatePayLogStatus(userId,transaction_id);

                        // 说明交易成功
                        return new Result(true, "支付成功");
                    }
                }

                // 返回结果
                return new Result(false, "支付超时");
            } catch (Exception e) {
                e.printStackTrace();
                return new Result(false, "操作失败");
            }
        }




}
