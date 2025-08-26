package com.ypareo.like.services.implementations;

import com.ypareo.like.dtos.RoleRequestDto;
import com.ypareo.like.dtos.RoleResponseDto;
import com.ypareo.like.mappers.RoleMapper;
import com.ypareo.like.models.sql.Role;
import com.ypareo.like.repositories.RoleRepository;
import com.ypareo.like.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public List<RoleResponseDto> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(roleMapper::convertEntityToDto)
                .toList();
    }

    public RoleResponseDto getRoleById(Long id) {
        Role existingRole = roleRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Role with id " + id + " not found")
        );
        return roleMapper.convertEntityToDto(existingRole);
    }

    public RoleResponseDto createRole(RoleRequestDto roleRequestDto) {
        Role newRole = roleMapper.convertDtoToEntity(roleRequestDto);
        Role savedRole = roleRepository.save(newRole);
        return roleMapper.convertEntityToDto(savedRole);
    }

    public void deleteRole(Long id) {
        Role existingRole = roleRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Role with id " + id + " not found")
        );
        roleRepository.delete(existingRole);
    }
}
