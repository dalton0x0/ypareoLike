package com.ypareo.like.mappers;

import com.ypareo.like.dtos.UserRequestDto;
import com.ypareo.like.dtos.UserResponseDto;
import com.ypareo.like.models.sql.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserMapperTest {

    @InjectMocks
    private UserMapper userMapper;

    @Test
    void testConvertDtoToEntity() {

        UserRequestDto userResponseDto = UserRequestDto.builder()
                .id(1L)
                .firstName("firstName")
                .lastName("lastName")
                .email("email")
                .username("username")
                .password("password")
                .build();

        User user = userMapper.convertDtoToEntity(userResponseDto);

        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("firstName", user.getFirstName());
    }

    @Test
    void testConvertEntityToDto() {

        User user = User.builder()
                .id(1L)
                .firstName("firstName")
                .lastName("lastName")
                .email("email")
                .username("username")
                .password("password")
                .build();

        UserResponseDto userResponseDto = userMapper.convertEntityToDto(user);

        assertNotNull(userResponseDto);
        assertEquals(1L, userResponseDto.getId());
        assertEquals("firstName", userResponseDto.getFirstName());
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
    }
}
