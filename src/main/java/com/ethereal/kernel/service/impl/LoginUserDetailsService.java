package com.ethereal.kernel.service.impl;
 
import com.ethereal.kernel.dto.information.UserDto;
import com.ethereal.kernel.entity.Permission;
import com.ethereal.kernel.service.IUserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
 
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
 
 
@Service
public class LoginUserDetailsService implements UserDetailsService {

    @Resource
    private IUserService userService;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDto user = userService.findUserByUserName(username);
        if (user == null){
            throw new UsernameNotFoundException("not found");
        }
        //定义权限列表.
        List<GrantedAuthority> authorities = new ArrayList<>();
        // 用户可以访问的资源名称（或者说用户所拥有的权限） 注意：必须"ROLE_"开头
        if (user.getRole()!=null){
            if (user.getRole().getPermissions() !=null && user.getRole().getPermissions().size()>0){
                for (Permission permission : user.getRole().getPermissions()) {
                    authorities.add(new SimpleGrantedAuthority(permission.getCode()));
                }
            }
        }
 
        User user1 = new User(user.getUsername(), user.getPassword(), authorities);
        return user1;
    }
}
