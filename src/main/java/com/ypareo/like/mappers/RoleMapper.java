package com.ypareo.like.mappers;

import com.ypareo.like.dtos.RoleRequestDto;
import com.ypareo.like.dtos.RoleResponseDto;
import com.ypareo.like.models.sql.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    public Role convertDtoToEntity(RoleRequestDto roleRequestDto) {
        return Role.builder()
                .id(roleRequestDto.getId())
                .name(roleRequestDto.getName())
                .build();
    }

    public RoleResponseDto convertEntityToDto(Role role) {
        return RoleResponseDto.builder()
                .id(role.getId())
                .name(role.getName())
                .build();
    }
}
