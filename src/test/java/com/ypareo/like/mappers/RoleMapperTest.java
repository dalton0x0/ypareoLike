package com.ypareo.like.mappers;

import com.ypareo.like.dtos.RoleRequestDto;
import com.ypareo.like.dtos.RoleResponseDto;
import com.ypareo.like.enums.RoleType;
import com.ypareo.like.models.sql.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RoleMapperTest {

    @InjectMocks
    private RoleMapper roleMapper;

    @Test
    void testConvertDtoToEntity() {

        RoleRequestDto requestDto = RoleRequestDto.builder()
                .id(1L)
                .name(RoleType.ADMIN)
                .build();

        Role role = roleMapper.convertDtoToEntity(requestDto);

        assertNotNull(role);
        assertEquals(1L, role.getId());
        assertEquals(RoleType.ADMIN, role.getName());
    }

    @Test
    void testConvertEntityToDto() {

        Role role = Role.builder()
                .id(1L)
                .name(RoleType.TEACHER)
                .build();

        RoleResponseDto responseDto = roleMapper.convertEntityToDto(role);

        assertNotNull(responseDto);
        assertEquals(1L, responseDto.getId());
        assertEquals(RoleType.TEACHER, responseDto.getName());
    }

    @Test
    void testConvertDtoToEntityWithNullValues() {

        RoleRequestDto requestDto = RoleRequestDto.builder()
                .id(null)
                .name(null)
                .build();

        Role role = roleMapper.convertDtoToEntity(requestDto);

        assertNotNull(role);
        assertNull(role.getId());
        assertNull(role.getName());
    }
}
