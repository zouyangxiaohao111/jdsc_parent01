package cn.zcw.controller;

import cn.zcw.entity.Result;
import cn.zcw.service.impl.UserService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Reference
    private UserService userService;


    @RequestMapping("/sendCode/{phone]")
    public Result sendCode(@PathVariable String phone){
        try {
//            发送验证码到phone上
            userService.sendCode(phone);
            return new Result(true,"发送成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"发送失败");
        }

    }




}
