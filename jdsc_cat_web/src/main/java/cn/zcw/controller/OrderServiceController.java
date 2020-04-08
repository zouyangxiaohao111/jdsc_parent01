package cn.zcw.controller;

import cn.zcw.domain.TbOrder;
import cn.zcw.entity.Result;
import cn.zcw.service.OrderService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderServiceController {

    @Reference
    private OrderService orderService;


    /**
     * 保存订单方法
     * @param tbOrder
     * @return
     */
    @RequestMapping("/saveOrder")
    public Result saveOrder(@RequestBody TbOrder tbOrder){

        try {
//            获取登录名
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
//            设置订单表中用户id
            tbOrder.setUserId(username);
            orderService.saveOrder(tbOrder);
            return new Result(true,"保存成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"保存失败");
        }
    }

}
