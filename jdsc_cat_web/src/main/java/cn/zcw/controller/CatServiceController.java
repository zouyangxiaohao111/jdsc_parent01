package cn.zcw.controller;

import cn.zcw.entity.Result;
import cn.zcw.groupentity.Cart;
import cn.zcw.service.CatService;
import cn.zcw.util.CookieUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CatServiceController {
    @Reference
    private CatService catService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpSession session;

    @Autowired
    private HttpServletResponse response;
    /**
     * 从cookie中获取sessionId的值
     * @return
     */
    public String getSessionId(){
        // 从cookie中获取sessionId的值
        String value = CookieUtil.getCookieValue(request, "jdsc_sessionid", "UTF-8");
        // 判断
        if(value == null){
            // 第一次访问，cookie中没有存储sessionId的值
            value = session.getId();
            // 存储到cookie中
            CookieUtil.setCookie(request,response,"jdsc_sessionid",value,48*60*60,"UTF-8");
        }
        return value;
    }

    /**
     * 查询购物车
     * @return
     */
    @RequestMapping("/findCartList")
    public Result findCartList(){
        try {
//            获取sessionId
            String sessionId = getSessionId();
//            调用service层
            List<Cart> cartList_sessionid = catService.findCartList(sessionId);
//            获取登录名
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
//            判断
            if ("anonymousUser".equals(username)){
//                说明未登录状态
                return new Result(true,"查询成功",cartList_sessionid);
            }
//            说明是已经登录状态
            List<Cart> cartList_username = catService.findCartList(username);
//            如果登录，就将之前加入购物车的数据与用户数据合并
            if (cartList_sessionid != null && cartList_sessionid.size()>0){
//                数据合并
                cartList_username = catService.merge(cartList_sessionid,cartList_username);

//                清除redis缓存数据
                catService.delete(sessionId);

//                将信息存储到redis
                catService.saveRedis(username,cartList_username);

            }


            return new Result(true,"查询成功",cartList_username);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"查询失败");
        }
    }

    /**
     * 添加购物车
     * @return
     */
    @RequestMapping("/addCart/{itemId}/{num}")
    public Result addCart(@PathVariable("itemId") Long itemId,@PathVariable("num") int num){
        try {
//            获取sessionId
            String sessionId = getSessionId();
//            获取登录名
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
//            定义key
            String key = sessionId;
            if (!"anonymousUser".equals(username)){
//                如果已经登录（交换key）
                key = username;
            }
//            查询缓存中的购物车信息
            List<Cart> cartList = catService.findCartList(key);
//            保存商品
           cartList = catService.addCart(cartList,itemId,num);
            // 把购物车的集合数据存入到redis中一份，覆盖掉
            catService.saveRedis(key,cartList);

            return new Result(true,"添加成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"添加失败");
        }
    }




}
