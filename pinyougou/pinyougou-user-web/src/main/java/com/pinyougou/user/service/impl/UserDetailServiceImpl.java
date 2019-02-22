package com.pinyougou.user.service.impl;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class UserDetailServiceImpl implements UserDetailsService {
    /**
     * 动态认证授权方法
     * @param username 用户在页面中输入的用户名
     * @return 用户信息（用户名、密码、角色权限）
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //角色权限；正式的做法应该是根据用户名从数据库中查询
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        //只有审核通过后的用户才能登录;登录已经交由cas进行，所以密码是不需要设置值的，为空字符串即可。
        return new User(username, "",authorities);
    }

}
