package cn.zcw.controller;

import cn.zcw.domain.TbSeller;
import cn.zcw.entity.Result;
import cn.zcw.service.SellerService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    @Reference
    private SellerService sellerService;



    /**
     * 注册
     * @param seller
     * @return
     */
    @RequestMapping("/save")
    public Result save(@RequestBody TbSeller seller) {
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

    
  

}
