package com.ypareo.like.web;

import com.ypareo.like.dtos.RoleRequestDto;
import com.ypareo.like.dtos.RoleResponseDto;
import com.ypareo.like.enums.RoleType;
import com.ypareo.like.services.RoleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleControllerTest {

    @Mock
    private RoleService roleService;

    @InjectMocks
    private RoleController roleController;

    @Test
    void testGetRoles_WithContent() {

        List<RoleResponseDto> roles = Arrays.asList(
                RoleResponseDto.builder().id(1L).name(RoleType.ADMIN).build(),
                RoleResponseDto.builder().id(2L).name(RoleType.TEACHER).build()
        );

        when(roleService.getAllRoles()).thenReturn(roles);

        ResponseEntity<List<RoleResponseDto>> response = roleController.getRoles();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());

        verify(roleService, times(1)).getAllRoles();
    }

    @Test
    void testGetRoles_NoContent() {

        when(roleService.getAllRoles()).thenReturn(Collections.emptyList());

        ResponseEntity<List<RoleResponseDto>> response = roleController.getRoles();

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(roleService, times(1)).getAllRoles();
    }

    @Test
    void testGetRole() {

        Long roleId = 1L;
        RoleResponseDto responseDto = RoleResponseDto.builder()
                .id(roleId)
                .name(RoleType.STUDENT)
                .build();

        when(roleService.getRoleById(roleId)).thenReturn(responseDto);

        ResponseEntity<RoleResponseDto> response = roleController.getRole(roleId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(roleId, response.getBody().getId());
        assertEquals(RoleType.STUDENT, response.getBody().getName());

        verify(roleService, times(1)).getRoleById(roleId);
    }

    @Test
    void testCreateRole() {

        RoleRequestDto requestDto = RoleRequestDto.builder()
                .name(RoleType.ADMIN)
                .build();

        RoleResponseDto responseDto = RoleResponseDto.builder()
                .id(1L)
                .name(RoleType.ADMIN)
                .build();

        when(roleService.createRole(any(RoleRequestDto.class))).thenReturn(responseDto);

        ResponseEntity<RoleResponseDto> response = roleController.createRole(requestDto);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals(RoleType.ADMIN, response.getBody().getName());

        verify(roleService, times(1)).createRole(requestDto);
    }

    @Test
    void testDeleteRole() {

        Long roleId = 1L;
        doNothing().when(roleService).deleteRole(roleId);

        ResponseEntity<Void> response = roleController.deleteRole(roleId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(roleService, times(1)).deleteRole(roleId);
    }
}
