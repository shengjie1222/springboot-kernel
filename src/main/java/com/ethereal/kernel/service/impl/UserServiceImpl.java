package com.ethereal.kernel.service.impl;

import com.ethereal.kernel.mapper.PermissionMapper;
import com.ethereal.kernel.mapper.UserMapper;
import com.ethereal.kernel.service.IUserService;
import com.ethereal.kernel.dto.information.UserDto;
import com.ethereal.kernel.entity.Permission;
import com.ethereal.kernel.entity.Role;
import com.ethereal.kernel.entity.User;
import com.ethereal.kernel.mapper.RoleMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Jie Jie
 * date 2024-03-21
 */
@Service
public class UserServiceImpl implements IUserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private PermissionMapper permissionMapper;

    @Override
    public UserDto findUserByUserName(String username) {

        User user = userMapper.selectByUserName(username);
        if(user == null) return null;

        Integer roleId = user.getRoleId();
        Role role = roleMapper.selectByPrimaryKey(roleId);

        List<Permission> permissions = permissionMapper.selectByRoleId(roleId);

        UserDto userDto = UserDto.convert(user, role, permissions);

        return userDto;
    }
}
