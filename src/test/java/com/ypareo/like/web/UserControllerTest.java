package com.ypareo.like.web;

import com.ypareo.like.dtos.UserRequestDto;
import com.ypareo.like.dtos.UserResponseDto;
import com.ypareo.like.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void getAllUsers_WhenUsersExist_ShouldReturnUsers() {

        UserResponseDto user1 = UserResponseDto.builder().id(1L).firstName("John").lastName("Doe").email("john@email.com").build();
        UserResponseDto user2 = UserResponseDto.builder().id(2L).firstName("Jane").lastName("Smith").email("jane@email.com").build();

        when(userService.getAllUsers()).thenReturn(List.of(user1, user2));

        ResponseEntity<List<UserResponseDto>> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void getAllUsers_WhenNoUsers_ShouldReturnNoContent() {

        when(userService.getAllUsers()).thenReturn(List.of());

        ResponseEntity<List<UserResponseDto>> response = userController.getAllUsers();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void getUserById_ShouldReturnUser() {

        Long userId = 1L;
        UserResponseDto expectedUser = UserResponseDto.builder().id(userId).firstName("John").lastName("Doe").email("john@email.com").build();
        when(userService.getUserById(userId)).thenReturn(expectedUser);

        ResponseEntity<UserResponseDto> response = userController.getUserById(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("John", response.getBody().getFirstName());
        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    void createUser_ShouldCreateAndReturnUser() {

        UserRequestDto requestDto = UserRequestDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@email.com")
                .username("johndoe")
                .password("password123")
                .build();

        UserResponseDto expectedResponse = UserResponseDto.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john@email.com")
                .build();

        when(userService.createUser(requestDto)).thenReturn(expectedResponse);

        ResponseEntity<UserResponseDto> response = userController.createUser(requestDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        verify(userService, times(1)).createUser(requestDto);
    }

    @Test
    void updateUser_ShouldUpdateAndReturnUser() {

        Long userId = 1L;
        UserRequestDto requestDto = UserRequestDto.builder()
                .firstName("JohnUpdated")
                .lastName("DoeUpdated")
                .email("john.updated@email.com")
                .username("johndoeupdated")
                .password("newpassword123")
                .build();

        UserResponseDto expectedResponse = UserResponseDto.builder()
                .id(userId)
                .firstName("JohnUpdated")
                .lastName("DoeUpdated")
                .email("john.updated@email.com")
                .build();

        when(userService.updateUser(userId, requestDto)).thenReturn(expectedResponse);

        ResponseEntity<UserResponseDto> response = userController.updateUser(userId, requestDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("JohnUpdated", response.getBody().getFirstName());
        verify(userService, times(1)).updateUser(userId, requestDto);
    }

    @Test
    void deleteUser_ShouldDeleteUser() {

        Long userId = 1L;
        doNothing().when(userService).deleteUser(userId);

        ResponseEntity<Void> response = userController.deleteUser(userId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService, times(1)).deleteUser(userId);
    }
}
