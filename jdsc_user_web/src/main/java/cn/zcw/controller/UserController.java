package cn.zcw.controller;

import cn.zcw.domain.TbUser;
import cn.zcw.entity.Result;
import cn.zcw.service.impl.UserService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Reference
    private UserService userService;

    /**
     * 验证码的发送
     * @param phone
     * @return
     */
    @RequestMapping("/sendCode/{phone}")
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

    @RequestMapping("/register/{code}")
    public Result register(@PathVariable("code") String code,@RequestBody TbUser tbUser){
        try {
//            发送验证码到phone上
            Result result = userService.register(code, tbUser);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"注册失败");
        }

    }




}
