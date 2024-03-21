package com.ethereal.kernel.service;

import com.ethereal.kernel.dto.information.UserDto;

/**
 * @author Jie Jie
 * date 2024-03-21
 */
public interface IUserService {

    UserDto findUserByUserName(String username);
}
