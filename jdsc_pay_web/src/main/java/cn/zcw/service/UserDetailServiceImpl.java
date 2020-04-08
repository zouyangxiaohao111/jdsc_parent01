package cn.zcw.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取角色登录名
 */
public class UserDetailServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 存储角色集合
        List<GrantedAuthority> authorities=new ArrayList();
        // 添加角色
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        // 返回用户信息
        return new User(username, ""  , authorities);
    }

}
