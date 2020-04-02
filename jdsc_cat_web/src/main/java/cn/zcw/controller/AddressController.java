package cn.zcw.controller;

import cn.zcw.domain.TbAddress;
import cn.zcw.entity.Result;
import cn.zcw.service.AddressService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressController {

    @Reference
    private AddressService addressService;

    @RequestMapping("/findAddressList")
    public Result findAddressList(){
        try {
//            从springsecurity框架中获取登录名
            String username = SecurityContextHolder.getContext().getAuthentication().getName();

//            调用业务层获取该用户下所有的地址
             List<TbAddress> addressList = addressService.findAddressList(username);



            return new Result(true,"查询成功",addressList);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"查询失败");
        }
    }
}
