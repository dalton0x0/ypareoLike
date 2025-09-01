package com.ypareo.like.services.implementations;

import com.ypareo.like.dtos.RoleRequestDto;
import com.ypareo.like.dtos.RoleResponseDto;
import com.ypareo.like.enums.RoleType;
import com.ypareo.like.exceptions.BadRequestException;
import com.ypareo.like.exceptions.ResourceNotFoundException;
import com.ypareo.like.mappers.RoleMapper;
import com.ypareo.like.models.sql.Role;
import com.ypareo.like.repositories.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RoleMapper roleMapper;

    @InjectMocks
    private RoleServiceImpl roleService;

    @Test
    void testGetAllRoles() {

        Role role1 = Role.builder().id(1L).name(RoleType.ADMIN).build();
        Role role2 = Role.builder().id(2L).name(RoleType.TEACHER).build();
        List<Role> roles = Arrays.asList(role1, role2);

        RoleResponseDto dto1 = RoleResponseDto.builder().id(1L).name(RoleType.ADMIN).build();
        RoleResponseDto dto2 = RoleResponseDto.builder().id(2L).name(RoleType.TEACHER).build();

        when(roleRepository.findAll()).thenReturn(roles);
        when(roleMapper.convertEntityToDto(role1)).thenReturn(dto1);
        when(roleMapper.convertEntityToDto(role2)).thenReturn(dto2);

        List<RoleResponseDto> result = roleService.getAllRoles();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(RoleType.ADMIN, result.get(0).getName());
        assertEquals(RoleType.TEACHER, result.get(1).getName());

        verify(roleRepository, times(1)).findAll();
        verify(roleMapper, times(2)).convertEntityToDto(any(Role.class));
    }

    @Test
    void testGetRoleById_Success() {

        Long roleId = 1L;
        Role role = Role.builder().id(roleId).name(RoleType.STUDENT).build();
        RoleResponseDto responseDto = RoleResponseDto.builder().id(roleId).name(RoleType.STUDENT).build();

        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
        when(roleMapper.convertEntityToDto(role)).thenReturn(responseDto);

        RoleResponseDto result = roleService.getRoleById(roleId);

        assertNotNull(result);
        assertEquals(roleId, result.getId());
        assertEquals(RoleType.STUDENT, result.getName());

        verify(roleRepository, times(1)).findById(roleId);
        verify(roleMapper, times(1)).convertEntityToDto(role);
    }

    @Test
    void testGetRoleById_NotFound() {

        Long roleId = 999L;
        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> roleService.getRoleById(roleId));

        assertEquals("Role with id 999 not found", exception.getMessage());
        verify(roleRepository, times(1)).findById(roleId);
    }

    @Test
    void testCreateRole() {

        RoleRequestDto requestDto = RoleRequestDto.builder()
                .name(RoleType.ADMIN)
                .build();

        Role roleToSave = Role.builder().name(RoleType.ADMIN).build();
        Role savedRole = Role.builder().id(1L).name(RoleType.ADMIN).build();
        RoleResponseDto responseDto = RoleResponseDto.builder().id(1L).name(RoleType.ADMIN).build();

        when(roleMapper.convertDtoToEntity(requestDto)).thenReturn(roleToSave);
        when(roleRepository.save(roleToSave)).thenReturn(savedRole);
        when(roleMapper.convertEntityToDto(savedRole)).thenReturn(responseDto);

        RoleResponseDto result = roleService.createRole(requestDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(RoleType.ADMIN, result.getName());

        verify(roleMapper, times(1)).convertDtoToEntity(requestDto);
        verify(roleRepository, times(1)).save(roleToSave);
        verify(roleMapper, times(1)).convertEntityToDto(savedRole);
    }

    @Test
    void testDeleteRole_Success() {

        Long roleId = 1L;
        Role existingRole = Role.builder().id(roleId).name(RoleType.TEACHER).build();

        when(roleRepository.findById(roleId)).thenReturn(Optional.of(existingRole));
        doNothing().when(roleRepository).deleteById(roleId);

        roleService.deleteRole(roleId);

        verify(roleRepository, times(1)).findById(roleId);
        verify(roleRepository, times(1)).deleteById(roleId);
    }

    @Test
    void testDeleteRole_NotFound() {

        Long roleId = 999L;
        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> roleService.deleteRole(roleId));

        assertEquals("Role with id 999 not found", exception.getMessage());
        verify(roleRepository, times(1)).findById(roleId);
        verify(roleRepository, never()).delete(any());
    }

    @Test
    void testValidateRoleRequestDto () {

        RoleRequestDto requestDto = RoleRequestDto.builder().build();

        assertNull(requestDto.getName());

        BadRequestException exception = assertThrows(BadRequestException.class, () -> roleService.createRole(requestDto));

        assertEquals("Role name cannot be null", exception.getMessage());
        verify(roleMapper, never()).convertDtoToEntity(any(RoleRequestDto.class));
        verify(roleRepository, never()).save(any(Role.class));
        verify(roleMapper, never()).convertEntityToDto(any(Role.class));
    }

    @Test
    void testCheckUniqueConstraints () {

        RoleRequestDto requestDto = RoleRequestDto.builder()
                .name(RoleType.ADMIN)
                .build();

        Role existingRole = Role.builder().name(RoleType.ADMIN).build();

        when(roleRepository.findByName(RoleType.ADMIN)).thenReturn(Optional.of(existingRole));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> roleService.createRole(requestDto));

        assertEquals("Role " + RoleType.ADMIN + " already exists", exception.getMessage());
        verify(roleRepository, times(1)).findByName(RoleType.ADMIN);
        verify(roleMapper, never()).convertDtoToEntity(any(RoleRequestDto.class));
        verify(roleRepository, never()).save(any(Role.class));
        verify(roleMapper, never()).convertEntityToDto(any(Role.class));
    }
}
