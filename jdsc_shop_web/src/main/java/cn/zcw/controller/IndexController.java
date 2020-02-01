package cn.zcw.controller;

import cn.zcw.entity.Result;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/index")
public class IndexController {
    /**
     * 获取登录名
     * @return
     */
    @RequestMapping("/findLoginname")
    public Result findLoginname() {
        String loginName = null;
        try {
            // 从SpringSecurity框架中获取登录名称
            // ServletContext 域对象
            // 上下文对象
            SecurityContext context = SecurityContextHolder.getContext();
            // 获取用户名
            loginName = context.getAuthentication().getName();
            return new Result(true, "成功", loginName);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, "查询失败");
        }

    }
}


