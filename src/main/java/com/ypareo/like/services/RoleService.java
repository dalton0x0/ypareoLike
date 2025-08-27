package com.ypareo.like.services;

import com.ypareo.like.dtos.RoleRequestDto;
import com.ypareo.like.dtos.RoleResponseDto;

import java.util.List;

public interface RoleService {
    List<RoleResponseDto> getAllRoles();
    RoleResponseDto getRoleById(Long id);
    RoleResponseDto createRole(RoleRequestDto roleRequestDto);
    void deleteRole(Long id);
}
