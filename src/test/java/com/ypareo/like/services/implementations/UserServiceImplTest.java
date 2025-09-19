package com.ypareo.like.services.implementations;

import com.ypareo.like.dtos.UserRequestDto;
import com.ypareo.like.dtos.UserResponseDto;
import com.ypareo.like.exceptions.BadRequestException;
import com.ypareo.like.exceptions.ResourceNotFoundException;
import com.ypareo.like.mappers.UserMapper;
import com.ypareo.like.models.sql.User;
import com.ypareo.like.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getAllUsers_ShouldReturnListOfUsers() {

        User user1 = User.builder().id(1L).firstName("John").lastName("Doe").email("john@email.com").build();
        User user2 = User.builder().id(2L).firstName("Jane").lastName("Smith").email("jane@email.com").build();

        UserResponseDto dto1 = UserResponseDto.builder().id(1L).firstName("John").lastName("Doe").email("john@email.com").build();
        UserResponseDto dto2 = UserResponseDto.builder().id(2L).firstName("Jane").lastName("Smith").email("jane@email.com").build();

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));
        when(userMapper.convertEntityToDto(user1)).thenReturn(dto1);
        when(userMapper.convertEntityToDto(user2)).thenReturn(dto2);

        List<UserResponseDto> result = userService.getAllUsers();

        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Jane", result.get(1).getFirstName());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnUser() {

        Long userId = 1L;
        User user = User.builder().id(userId).firstName("John").lastName("Doe").email("john@email.com").build();
        UserResponseDto expectedDto = UserResponseDto.builder().id(userId).firstName("John").lastName("Doe").email("john@email.com").build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.convertEntityToDto(user)).thenReturn(expectedDto);

        UserResponseDto result = userService.getUserById(userId);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void getUserById_WhenUserNotFound_ShouldThrowException() {

        Long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(userId));
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void createUser_WithValidData_ShouldCreateUser() {

        UserRequestDto requestDto = UserRequestDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@email.com")
                .username("johndoe")
                .password("password123")
                .build();

        User user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@email.com")
                .username("johndoe")
                .password("password123")
                .build();

        User savedUser = User.builder().id(1L).firstName("John").lastName("Doe").email("john@email.com").build();
        UserResponseDto expectedResponse = UserResponseDto.builder().id(1L).firstName("John").lastName("Doe").email("john@email.com").build();

        when(userRepository.existsByEmail(requestDto.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(requestDto.getUsername())).thenReturn(false);
        when(userMapper.convertDtoToEntity(requestDto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(savedUser);
        when(userMapper.convertEntityToDto(savedUser)).thenReturn(expectedResponse);

        UserResponseDto result = userService.createUser(requestDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void createUser_WithMissingFirstName_ShouldThrowException() {

        UserRequestDto requestDto = UserRequestDto.builder()
                .lastName("Doe")
                .email("john@email.com")
                .username("johndoe")
                .password("password123")
                .build();

        assertNull(requestDto.getFirstName());
        assertThrows(BadRequestException.class, () -> userService.createUser(requestDto));
    }

    @Test
    void createUser_WithMissingPassword_ShouldThrowException() {

        UserRequestDto requestDto = UserRequestDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@email.com")
                .username("johndoe")
                .build();

        assertNull(requestDto.getPassword());
        assertThrows(BadRequestException.class, () -> userService.createUser(requestDto));
    }

    @Test
    void createUser_WithExistingEmail_ShouldThrowException() {

        UserRequestDto requestDto = UserRequestDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("existing@email.com")
                .username("johndoe")
                .password("password123")
                .build();

        when(userRepository.existsByEmail(requestDto.getEmail())).thenReturn(true);

        assertThrows(BadRequestException.class, () -> userService.createUser(requestDto));
    }

    @Test
    void updateUser_WithValidData_ShouldUpdateUser() {

        Long userId = 1L;
        UserRequestDto requestDto = UserRequestDto.builder()
                .firstName("JohnUpdated")
                .lastName("DoeUpdated")
                .email("john.updated@email.com")
                .username("johndoeupdated")
                .password("newpassword123")
                .build();

        User existingUser = User.builder()
                .id(userId)
                .firstName("John")
                .lastName("Doe")
                .email("john@email.com")
                .username("johndoe")
                .password("oldpassword")
                .build();

        User updatedUser = User.builder()
                .id(userId)
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

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmail(requestDto.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(requestDto.getUsername())).thenReturn(false);
        when(userRepository.save(existingUser)).thenReturn(updatedUser);
        when(userMapper.convertEntityToDto(updatedUser)).thenReturn(expectedResponse);

        UserResponseDto result = userService.updateUser(userId, requestDto);

        assertNotNull(result);
        assertEquals("JohnUpdated", result.getFirstName());
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void updateUser_WhenEmailChangedAndNotTaken_ShouldUpdateEmail() {

        Long userId = 1L;
        UserRequestDto requestDto = UserRequestDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("new.email@email.com")
                .username("johndoe")
                .password("password123")
                .build();

        User existingUser = User.builder()
                .id(userId)
                .firstName("John")
                .lastName("Doe")
                .email("old.email@email.com")
                .username("johndoe")
                .password("password123")
                .build();

        User updatedUser = User.builder()
                .id(userId)
                .firstName("John")
                .lastName("Doe")
                .email("new.email@email.com")
                .username("johndoe")
                .password("password123")
                .build();

        UserResponseDto expectedResponse = UserResponseDto.builder()
                .id(userId)
                .firstName("John")
                .lastName("Doe")
                .email("new.email@email.com")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmail(requestDto.getEmail())).thenReturn(false);
        when(userRepository.save(existingUser)).thenReturn(updatedUser);
        when(userMapper.convertEntityToDto(updatedUser)).thenReturn(expectedResponse);

        UserResponseDto result = userService.updateUser(userId, requestDto);

        assertNotNull(result);
        assertEquals("new.email@email.com", result.getEmail());
        verify(userRepository, times(1)).existsByEmail("new.email@email.com");
    }

    @Test
    void updateUser_WhenEmailChangedButTaken_ShouldThrowException() {

        Long userId = 1L;
        UserRequestDto requestDto = UserRequestDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("taken.email@email.com")
                .username("johndoe")
                .password("password123")
                .build();

        User existingUser = User.builder()
                .id(userId)
                .firstName("John")
                .lastName("Doe")
                .email("old.email@email.com")
                .username("johndoe")
                .password("password123")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmail(requestDto.getEmail())).thenReturn(true);

        assertThrows(BadRequestException.class, () -> userService.updateUser(userId, requestDto));
        verify(userRepository, times(1)).existsByEmail("taken.email@email.com");
    }

    @Test
    void updateUser_WhenUsernameChangedAndNotTaken_ShouldUpdateUsername() {

        Long userId = 1L;
        UserRequestDto requestDto = UserRequestDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@email.com")
                .username("newusername")
                .password("password123")
                .build();

        User existingUser = User.builder()
                .id(userId)
                .firstName("John")
                .lastName("Doe")
                .email("john@email.com")
                .username("oldusername")
                .password("password123")
                .build();

        User updatedUser = User.builder()
                .id(userId)
                .firstName("John")
                .lastName("Doe")
                .email("john@email.com")
                .username("newusername")
                .password("password123")
                .build();

        UserResponseDto expectedResponse = UserResponseDto.builder()
                .id(userId)
                .firstName("John")
                .lastName("Doe")
                .email("john@email.com")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByUsername(requestDto.getUsername())).thenReturn(false);
        when(userRepository.save(existingUser)).thenReturn(updatedUser);
        when(userMapper.convertEntityToDto(updatedUser)).thenReturn(expectedResponse);

        UserResponseDto result = userService.updateUser(userId, requestDto);

        assertNotNull(result);

        verify(userRepository, times(1)).existsByUsername("newusername");
    }

    @Test
    void updateUser_WhenUsernameChangedButTaken_ShouldThrowException() {

        Long userId = 1L;
        UserRequestDto requestDto = UserRequestDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@email.com")
                .username("takenusername")
                .password("password123")
                .build();

        User existingUser = User.builder()
                .id(userId)
                .firstName("John")
                .lastName("Doe")
                .email("john@email.com")
                .username("oldusername")
                .password("password123")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByUsername(requestDto.getUsername())).thenReturn(true);

        assertThrows(BadRequestException.class, () -> userService.updateUser(userId, requestDto));
        verify(userRepository, times(1)).existsByUsername("takenusername");
    }

    @Test
    void updateUser_WhenEmailNotChanged_ShouldKeepSameEmail() {

        Long userId = 1L;
        String sameEmail = "john@email.com";

        UserRequestDto requestDto = UserRequestDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email(sameEmail)
                .username("johndoe")
                .password("password123")
                .build();

        User existingUser = User.builder()
                .id(userId)
                .firstName("John")
                .lastName("Doe")
                .email(sameEmail)
                .username("johndoe")
                .password("password123")
                .build();

        User updatedUser = User.builder()
                .id(userId)
                .firstName("John")
                .lastName("Doe")
                .email(sameEmail)
                .username("johndoe")
                .password("password123")
                .build();

        UserResponseDto expectedResponse = UserResponseDto.builder()
                .id(userId)
                .firstName("John")
                .lastName("Doe")
                .email(sameEmail)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(updatedUser);
        when(userMapper.convertEntityToDto(updatedUser)).thenReturn(expectedResponse);

        UserResponseDto result = userService.updateUser(userId, requestDto);

        assertNotNull(result);
        assertEquals(sameEmail, result.getEmail());

        verify(userRepository, never()).existsByEmail(sameEmail);
    }

    @Test
    void updateUser_WhenUsernameNotChanged_ShouldKeepSameUsername() {

        Long userId = 1L;
        String sameUsername = "johndoe";

        UserRequestDto requestDto = UserRequestDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@email.com")
                .username(sameUsername)
                .password("password123")
                .build();

        User existingUser = User.builder()
                .id(userId)
                .firstName("John")
                .lastName("Doe")
                .email("john@email.com")
                .username(sameUsername)
                .password("password123")
                .build();

        User updatedUser = User.builder()
                .id(userId)
                .firstName("John")
                .lastName("Doe")
                .email("john@email.com")
                .username(sameUsername)
                .password("password123")
                .build();

        UserResponseDto expectedResponse = UserResponseDto.builder()
                .id(userId)
                .firstName("John")
                .lastName("Doe")
                .email("john@email.com")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(updatedUser);
        when(userMapper.convertEntityToDto(updatedUser)).thenReturn(expectedResponse);

        UserResponseDto result = userService.updateUser(userId, requestDto);

        assertNotNull(result);

        verify(userRepository, never()).existsByUsername(sameUsername);
    }

    @Test
    void updateUser_WhenEmailChangedToSameButWithDifferentCase_ShouldHandleCaseSensitivity() {

        Long userId = 1L;
        UserRequestDto requestDto = UserRequestDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("JOHN@email.com")
                .username("johndoe")
                .password("password123")
                .build();

        User existingUser = User.builder()
                .id(userId)
                .firstName("John")
                .lastName("Doe")
                .email("john@email.com")
                .username("johndoe")
                .password("password123")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        assertDoesNotThrow(() -> userService.updateUser(userId, requestDto));
    }

    @Test
    void deleteUser_WhenUserExists_ShouldDeleteUser() {

        Long userId = 1L;
        User existingUser = User.builder().id(userId).build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        doNothing().when(userRepository).delete(existingUser);

        userService.deleteUser(userId);

        verify(userRepository, times(1)).delete(existingUser);
    }
}
