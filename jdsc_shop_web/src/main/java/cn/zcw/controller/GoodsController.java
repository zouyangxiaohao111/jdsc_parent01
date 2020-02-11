package cn.zcw.controller;

import cn.zcw.entity.Result;
import cn.zcw.groupentity.Goods;
import cn.zcw.service.GoodService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/goods")
public class GoodsController {
//    获取注册中心的接口
    @Reference
    private GoodService goodService;
    @RequestMapping("/save")
    public Result add(@RequestBody Goods goods){
        try {

            goodService.add(goods);
            return new Result(true,"保存成功,正在跳转页面");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"失败");
        }
    }


}
