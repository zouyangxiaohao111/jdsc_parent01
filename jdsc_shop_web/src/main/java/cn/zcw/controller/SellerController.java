package cn.zcw.controller;

import cn.zcw.domain.TbSeller;
import cn.zcw.entity.Result;
import cn.zcw.service.SellerService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * controller
 *
 * @author Administrator
 */
@RestController
@RequestMapping("/seller")
public class SellerController {

//    从注册中心获取接口
    @Reference
    private SellerService sellerService;



    /**
     * 注册
     * @param seller
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody TbSeller seller) {
        try {
            // 获取密码
            String password = seller.getPassword();
            // 创建对象
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            // 加密
            String pwd = encoder.encode(password);

            // 设置进去
            seller.setPassword(pwd);
            // 保存
            sellerService.add(seller);
            return new Result(true, "成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "增加失败");
        }
    }

    /**
     * 更新
     * @param seller
     * @return
     */
    @RequestMapping("/update/{name}")
    public Result update(@PathVariable("name") String name ,@RequestBody TbSeller seller) {
        try {
            // 获取原密码
            String password1 = seller.getName();
            // 创建对象
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            // 加密

//                获取前端的新密码
            String newPassword = seller.getPassword();
            String encode = encoder.encode(newPassword);
            seller.setPassword(encode);
            // 保存
            sellerService.update(seller, name);
            return new Result(true, "成功,2");


        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "用户名或密码错误");
        }
    }

    /**
     * 存储
     * @param seller
     * @return
     */
    @RequestMapping("/save")
    public Result save(@RequestBody TbSeller seller) {
        try {

            sellerService.update(seller);
            return new Result(true, "成功,1");


        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "用户名或密码错误");
        }
    }

    /**
     * 回显
     * @param
     * @return seller
     */
    @RequestMapping("/findOne/{loginname}")
    public Result findOne( @PathVariable("loginname") String loginname) {
        try {

            TbSeller seller = sellerService.findOne(loginname);
            return new Result(true, "成功,1",seller);


        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "失败");
        }
    }
}
