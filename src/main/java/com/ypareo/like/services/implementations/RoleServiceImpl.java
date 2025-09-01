package com.ypareo.like.services.implementations;

import com.ypareo.like.dtos.RoleRequestDto;
import com.ypareo.like.dtos.RoleResponseDto;
import com.ypareo.like.exceptions.BadRequestException;
import com.ypareo.like.exceptions.ResourceNotFoundException;
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
        Role existingRole = getExistingRole(id);
        return roleMapper.convertEntityToDto(existingRole);
    }

    public RoleResponseDto createRole(RoleRequestDto roleRequestDto) {
        validateRoleRequestDto(roleRequestDto);
        checkUniqueConstraints(roleRequestDto);
        Role newRole = roleMapper.convertDtoToEntity(roleRequestDto);
        Role savedRole = roleRepository.save(newRole);
        return roleMapper.convertEntityToDto(savedRole);
    }

    public void deleteRole(Long id) {
        Role existingRole = getExistingRole(id);
        roleRepository.deleteById(existingRole.getId());
    }

    private Role getExistingRole(Long id) {
        return roleRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Role with id " + id + " not found")
        );
    }

    private void validateRoleRequestDto(RoleRequestDto roleRequestDto) {
        if (roleRequestDto.getName() == null) {
            throw new BadRequestException("Role name cannot be null");
        }
    }

    private void checkUniqueConstraints(RoleRequestDto roleRequestDto) {
        roleRepository.findByName(roleRequestDto.getName()).ifPresent(existingRole -> {
            throw new BadRequestException("Role " + roleRequestDto.getName() + " already exists");
        });
    }
}
