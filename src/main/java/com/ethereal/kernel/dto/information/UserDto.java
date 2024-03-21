package com.ethereal.kernel.dto.information;

import com.ethereal.kernel.entity.Permission;
import com.ethereal.kernel.entity.Role;
import com.ethereal.kernel.entity.User;
import lombok.Data;

import java.util.List;

/**
 * @author Jie Jie
 * date 2024-03-21
 */
@Data
public class UserDto extends User {

    private RoleDto role;

    public static UserDto convert(User user, Role role, List<Permission> permissions) {
        if (user == null) {
            return null;
        }
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setRoleId(user.getRoleId());
        userDto.setName(user.getName());
        userDto.setUsername(user.getUsername());
        userDto.setPassword(user.getPassword());
        userDto.setRemark(user.getRemark());
        userDto.setCreateTime(user.getCreateTime());
        userDto.setUpdateTime(user.getUpdateTime());

        RoleDto roleDto = RoleDto.convert(role, permissions);
        userDto.setRole(roleDto);
        return userDto;
    }


}
