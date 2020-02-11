package cn.zcw.controller;

import cn.zcw.domain.TbSeller;
import cn.zcw.service.SellerService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
    @Reference
    private SellerService sellerService;
    /**
     * loadUserByUsername  通过用户名查询用户的信息
     * @param username  当前登录的用户名
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
// 通过用户名查询数据
        TbSeller tbseller = sellerService.findOne(username);
        // 判断
        if(tbseller == null){
            // 把null数据返回给SpringSecurity框架，并且后台会出现异常
            return null;
        }
        // 必须是审核通过
        if(!tbseller.getStatus().equals("1")){
            return null;
        }

        // 存储的用户的角色
        List<SimpleGrantedAuthority> list = new ArrayList<>();
        list.add(new SimpleGrantedAuthority("ROLE_USER"));
        list.add(new SimpleGrantedAuthority("ROLE_SELLER"));
        list.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        // 给SpringSecurity框架返回数据

        User user = new User(username,tbseller.getPassword(),list);
        return user;
    }
}
