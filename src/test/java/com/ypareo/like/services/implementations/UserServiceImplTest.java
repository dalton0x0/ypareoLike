package com.ypareo.like.services.implementations;

import com.ypareo.like.dtos.UserRequestDto;
import com.ypareo.like.dtos.UserResponseDto;
import com.ypareo.like.enums.RoleType;
import com.ypareo.like.exceptions.BadRequestException;
import com.ypareo.like.exceptions.ResourceNotFoundException;
import com.ypareo.like.mappers.UserMapper;
import com.ypareo.like.models.sql.Role;
import com.ypareo.like.models.sql.User;
import com.ypareo.like.repositories.RoleRepository;
import com.ypareo.like.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
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

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getAllUsers_ShouldReturnAllUsers() {

        User user1 = User.builder().id(1L).firstName("John").lastName("Doe").build();
        User user2 = User.builder().id(2L).firstName("Jane").lastName("Smith").build();
        List<User> users = List.of(user1, user2);

        UserResponseDto dto1 = UserResponseDto.builder().id(1L).firstName("John").lastName("Doe").build();
        UserResponseDto dto2 = UserResponseDto.builder().id(2L).firstName("Jane").lastName("Smith").build();

        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.convertEntityToDto(user1)).thenReturn(dto1);
        when(userMapper.convertEntityToDto(user2)).thenReturn(dto2);

        List<UserResponseDto> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(dto1, result.get(0));
        assertEquals(dto2, result.get(1));
        verify(userRepository).findAll();
    }

    @Test
    void getAllUsers_WhenNoUsers_ShouldReturnEmptyList() {

        when(userRepository.findAll()).thenReturn(List.of());

        List<UserResponseDto> result = userService.getAllUsers();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRepository).findAll();
    }

    @Test
    void getUserById_WithValidId_ShouldReturnUser() {

        Long userId = 1L;
        User user = User.builder().id(userId).firstName("John").lastName("Doe").build();
        UserResponseDto dto = UserResponseDto.builder().id(userId).firstName("John").lastName("Doe").build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.convertEntityToDto(user)).thenReturn(dto);

        UserResponseDto result = userService.getUserById(userId);

        assertNotNull(result);
        assertEquals(dto, result);
        verify(userRepository).findById(userId);
    }

    @Test
    void getUserById_WithInvalidId_ShouldThrowException() {

        Long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(userId));
        verify(userRepository).findById(userId);
    }

    @Test
    void createUser_WithValidData_ShouldCreateUser() {

        UserRequestDto requestDto = UserRequestDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .username("johndoe")
                .password("password")
                .roleIds(List.of(1L))
                .build();

        Role role = Role.builder().id(1L).name(RoleType.STUDENT).build();
        User user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .username("johndoe")
                .password("password")
                .build();
        User savedUser = User.builder().id(1L).build();
        UserResponseDto responseDto = UserResponseDto.builder().id(1L).build();

        when(userRepository.existsByEmail(requestDto.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(requestDto.getUsername())).thenReturn(false);
        when(roleRepository.findAllById(requestDto.getRoleIds())).thenReturn(List.of(role));
        when(userMapper.convertDtoToEntity(requestDto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(savedUser);
        when(userMapper.convertEntityToDto(savedUser)).thenReturn(responseDto);

        UserResponseDto result = userService.createUser(requestDto);

        assertNotNull(result);
        assertEquals(responseDto, result);
        verify(userRepository).existsByEmail(requestDto.getEmail());
        verify(userRepository).existsByUsername(requestDto.getUsername());
        verify(roleRepository).findAllById(requestDto.getRoleIds());
        verify(userRepository).save(user);
    }

    @Test
    void createUser_WithMissingFirstName_ShouldThrowException() {

        UserRequestDto requestDto = UserRequestDto.builder()
                .firstName(null)
                .lastName("Doe")
                .email("john.doe@example.com")
                .username("johndoe")
                .password("password")
                .build();

        assertThrows(BadRequestException.class, () -> userService.createUser(requestDto));
        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_WithMissingLastName_ShouldThrowException() {

        UserRequestDto requestDto = UserRequestDto.builder()
                .firstName("John")
                .lastName(null)
                .email("john.doe@example.com")
                .username("johndoe")
                .password("password")
                .build();

        assertThrows(BadRequestException.class, () -> userService.createUser(requestDto));
        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_WithMissingEmail_ShouldThrowException() {

        UserRequestDto requestDto = UserRequestDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email(null)
                .username("johndoe")
                .password("password")
                .build();

        assertThrows(BadRequestException.class, () -> userService.createUser(requestDto));
        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_WithMissingUsername_ShouldThrowException() {

        UserRequestDto requestDto = UserRequestDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .username(null)
                .password("password")
                .build();

        assertThrows(BadRequestException.class, () -> userService.createUser(requestDto));
        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_WithMissingPassword_ShouldThrowException() {

        UserRequestDto requestDto = UserRequestDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .username("johndoe")
                .password(null)
                .build();

        assertThrows(BadRequestException.class, () -> userService.createUser(requestDto));
        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_WithExistingEmail_ShouldThrowException() {

        UserRequestDto requestDto = UserRequestDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("existing@example.com")
                .username("johndoe")
                .password("password")
                .build();

        when(userRepository.existsByEmail(requestDto.getEmail())).thenReturn(true);

        assertThrows(BadRequestException.class, () -> userService.createUser(requestDto));
        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_WithExistingUsername_ShouldThrowException() {

        UserRequestDto requestDto = UserRequestDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .username("existingUsername")
                .password("password")
                .build();

        when(userRepository.existsByUsername(requestDto.getUsername())).thenReturn(true);

        assertThrows(BadRequestException.class, () -> userService.createUser(requestDto));
        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_WithNoRoles_ShouldThrowException() {

        UserRequestDto requestDto = UserRequestDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .username("johndoe")
                .password("password")
                .roleIds(List.of())
                .build();

        when(userRepository.existsByEmail(requestDto.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(requestDto.getUsername())).thenReturn(false);

        assertThrows(BadRequestException.class, () -> userService.createUser(requestDto));
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUser_WithValidData_ShouldUpdateUser() {

        Role role1 = Role.builder().id(1L).name(RoleType.STUDENT).build();
        Role role2 = Role.builder().id(2L).name(RoleType.TEACHER).build();

        Long userId = 1L;

        UserRequestDto requestDto = UserRequestDto.builder()
                .firstName("John Updated")
                .lastName("Doe Updated")
                .email("john.doe.updated@example.com")
                .username("johndoeupdated")
                .password("newpassword")
                .roleIds(List.of(1L, 2L))
                .build();

        User existingUser = User.builder()
                .id(userId)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .username("johndoe")
                .password("oldpassword")
                .roles(List.of())
                .build();

        User updatedUser = User.builder()
                .firstName("John Updated")
                .lastName("Doe Updated")
                .email("john.doe.updated@example.com")
                .username("johndoeupdated")
                .password("newpassword")
                .roles(List.of(role1, role2))
                .build();

        UserResponseDto expectedResponseDto = UserResponseDto.builder()
                .id(userId)
                .firstName("John Updated")
                .lastName("Doe Updated")
                .email("john.doe.updated@example.com")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmail(requestDto.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(requestDto.getUsername())).thenReturn(false);
        when(roleRepository.findAllById(requestDto.getRoleIds())).thenReturn(List.of(role1, role2));
        when(userRepository.save(existingUser)).thenReturn(updatedUser);
        when(userMapper.convertEntityToDto(updatedUser)).thenReturn(expectedResponseDto);

        UserResponseDto result = userService.updateUser(userId, requestDto);

        assertNotNull(result);
        assertEquals(expectedResponseDto, result);
        assertEquals("John Updated", result.getFirstName());
        assertEquals(2, existingUser.getRoles().size());
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void updateUser_WithEmailChange_ShouldCheckEmailAvailability() {

        Long userId = 1L;
        UserRequestDto requestDto = UserRequestDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("new.email@example.com")
                .username("johndoe")
                .password("password")
                .build();

        User existingUser = User.builder()
                .id(userId)
                .firstName("John")
                .lastName("Doe")
                .email("old.email@example.com")
                .username("johndoe")
                .password("password")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmail("new.email@example.com")).thenReturn(false);
        when(userRepository.save(existingUser)).thenReturn(existingUser);
        when(userMapper.convertEntityToDto(existingUser)).thenReturn(UserResponseDto.builder().build());

        userService.updateUser(userId, requestDto);

        assertEquals("new.email@example.com", existingUser.getEmail());
        verify(userRepository).existsByEmail("new.email@example.com");
    }

    @Test
    void deleteUser_WithValidId_ShouldDeleteUser() {

        Long userId = 1L;
        User user = User.builder().id(userId).firstName("John").lastName("Doe").build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.deleteUser(userId);

        verify(userRepository).findById(userId);
        verify(userRepository).delete(user);
    }

    @Test
    void addRoleToUser_ShouldAddRole() {

        Long userId = 1L;
        Long roleId = 2L;
        User user = User.builder().id(userId).roles(new ArrayList<>()).build();
        Role role = Role.builder().id(roleId).name(RoleType.TEACHER).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.convertEntityToDto(user)).thenReturn(UserResponseDto.builder().build());

        UserResponseDto result = userService.addRoleToUser(userId, roleId);

        assertNotNull(result);
        assertTrue(user.getRoles().contains(role));
        verify(userRepository).save(user);
    }

    @Test
    void addRoleToUser_WhenRoleAlreadyExists_ShouldNotAddDuplicate() {

        Long userId = 1L;
        Long roleId = 2L;
        Role role = Role.builder().id(roleId).name(RoleType.TEACHER).build();
        User user = User.builder().id(userId).roles(new ArrayList<>(List.of(role))).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
        when(userMapper.convertEntityToDto(user)).thenReturn(UserResponseDto.builder().build());

        UserResponseDto result = userService.addRoleToUser(userId, roleId);

        assertNotNull(result);
        assertEquals(1, user.getRoles().size()); // Should not add duplicate
        verify(userRepository, never()).save(user);
    }

    @Test
    void removeRoleFromUser_ShouldRemoveRole() {

        Long userId = 1L;
        Long roleId = 2L;
        Role role = Role.builder().id(roleId).name(RoleType.TEACHER).build();
        User user = User.builder().id(userId).roles(new ArrayList<>(List.of(role))).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.convertEntityToDto(user)).thenReturn(UserResponseDto.builder().build());

        UserResponseDto result = userService.removeRoleFromUser(userId, roleId);

        assertNotNull(result);
        assertFalse(user.getRoles().contains(role));
        verify(userRepository).save(user);
    }

    @Test
    void removeRoleFromUser_WhenRoleNotExists_ShouldNotRemove() {

        Long userId = 1L;
        Long roleId = 2L;
        Role role = Role.builder().id(roleId).name(RoleType.TEACHER).build();
        User user = User.builder().id(userId).roles(new ArrayList<>()).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
        when(userMapper.convertEntityToDto(user)).thenReturn(UserResponseDto.builder().build());

        UserResponseDto result = userService.removeRoleFromUser(userId, roleId);

        assertNotNull(result);
        assertTrue(user.getRoles().isEmpty());
        verify(userRepository, never()).save(user);
    }
}
