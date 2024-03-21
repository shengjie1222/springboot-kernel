package com.ethereal.kernel.dto.information;

import com.ethereal.kernel.entity.Permission;
import com.ethereal.kernel.entity.Role;
import lombok.Data;

import java.util.List;

/**
 * @author Jie Jie
 * date 2024-03-21
 */
@Data
public class RoleDto extends Role {

    private List<Permission> permissions;

    public static RoleDto convert(Role role,  List<Permission> permissions) {
        if (role == null) {
            return null;
        }
        RoleDto roleDto = new RoleDto();
        roleDto.setId(role.getId());
        roleDto.setName(role.getName());
        roleDto.setParentId(role.getParentId());
        roleDto.setRemark(role.getRemark());
        roleDto.setPermissions(permissions);
        return roleDto;
    }
}
