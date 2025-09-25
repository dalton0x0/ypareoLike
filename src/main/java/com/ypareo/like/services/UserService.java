package com.ypareo.like.services;

import com.ypareo.like.dtos.UserRequestDto;
import com.ypareo.like.dtos.UserResponseDto;

import java.util.List;

public interface UserService {

    List<UserResponseDto> getAllUsers();
    UserResponseDto getUserById(Long id);
    UserResponseDto createUser(UserRequestDto userRequestDto);
    UserResponseDto updateUser(Long id, UserRequestDto userRequestDto);
    void deleteUser(Long id);

    UserResponseDto addRoleToUser(Long userId, Long roleId);
    UserResponseDto removeRoleFromUser(Long userId, Long roleId);
}
