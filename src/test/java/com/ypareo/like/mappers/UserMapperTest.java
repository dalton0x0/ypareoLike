package com.ypareo.like.mappers;

import com.ypareo.like.dtos.RoleResponseDto;
import com.ypareo.like.dtos.UserRequestDto;
import com.ypareo.like.dtos.UserResponseDto;
import com.ypareo.like.enums.RoleType;
import com.ypareo.like.models.sql.Role;
import com.ypareo.like.models.sql.User;
import com.ypareo.like.secutiry.CustomPasswordEncoder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {

    @Mock
    private RoleMapper roleMapper;

    @Mock
    private CustomPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserMapper userMapper;

    @Test
    void testConvertDtoToEntity() {

        UserRequestDto userRequestDto = UserRequestDto.builder()
                .id(1L)
                .firstName("firstName")
                .lastName("lastName")
                .email("email")
                .username("username")
                .password(passwordEncoder.encode("password"))
                .build();

        User user = userMapper.convertDtoToEntity(userRequestDto);

        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("firstName", user.getFirstName());
        assertEquals("lastName", user.getLastName());
        assertEquals("email", user.getEmail());
        assertEquals("username", user.getUsername());
        assertEquals(passwordEncoder.encode("password"), user.getPassword());
    }

    @Test
    void testConvertEntityToDto() {

        Role role1 = Role.builder().id(1L).name(RoleType.STUDENT).build();
        Role role2 = Role.builder().id(2L).name(RoleType.ADMIN).build();

        User user = User.builder()
                .id(1L)
                .firstName("firstName")
                .lastName("lastName")
                .email("email")
                .username("username")
                .password("password")
                .roles(new ArrayList<>(Arrays.asList(role1, role2)))
                .build();

        RoleResponseDto roleDto1 = RoleResponseDto.builder().id(1L).name(RoleType.STUDENT).build();
        RoleResponseDto roleDto2 = RoleResponseDto.builder().id(2L).name(RoleType.ADMIN).build();

        when(roleMapper.convertEntityToDto(role1)).thenReturn(roleDto1);
        when(roleMapper.convertEntityToDto(role2)).thenReturn(roleDto2);

        UserResponseDto userResponseDto = userMapper.convertEntityToDto(user);

        assertNotNull(userResponseDto);
        assertEquals(1L, userResponseDto.getId());
        assertEquals("firstName", userResponseDto.getFirstName());
        assertEquals("lastName", userResponseDto.getLastName());
        assertEquals("email", userResponseDto.getEmail());
        assertNotNull(userResponseDto.getRoles());
        assertEquals(2, userResponseDto.getRoles().size());

        verify(roleMapper, times(2)).convertEntityToDto(any(Role.class));
    }

    @Test
    void testConvertEntityToDto_WithNoRoles() {

        User user = User.builder()
                .id(1L)
                .firstName("firstName")
                .lastName("lastName")
                .email("email")
                .username("username")
                .password("password")
                .roles(new ArrayList<>())
                .build();

        UserResponseDto userResponseDto = userMapper.convertEntityToDto(user);

        assertNotNull(userResponseDto);
        assertEquals(1L, userResponseDto.getId());
        assertNotNull(userResponseDto.getRoles());
        assertTrue(userResponseDto.getRoles().isEmpty());
    }

    @Test
    void testUpdateEntityFromDto() {

        User user = User.builder()
                .id(1L)
                .firstName("firstName")
                .lastName("lastName")
                .email("email")
                .username("username")
                .password("password")
                .build();

        UserRequestDto userRequestDto = UserRequestDto.builder()
                .id(1L)
                .firstName("NewFirstName")
                .lastName("NewLastName")
                .email("emailNew")
                .username("NewUsername")
                .password("NewPassword")
                .build();

        userMapper.updateEntityFromDto(user, userRequestDto);

        assertNotNull(user);
        assertNotNull(userRequestDto);
        assertEquals("NewFirstName", user.getFirstName());
        assertEquals("NewLastName", user.getLastName());
        assertEquals("emailNew", user.getEmail());
        assertEquals("NewUsername", user.getUsername());
    }
}
