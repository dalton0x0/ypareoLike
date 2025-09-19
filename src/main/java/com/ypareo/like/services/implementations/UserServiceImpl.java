package com.ypareo.like.services.implementations;

import com.ypareo.like.dtos.UserRequestDto;
import com.ypareo.like.dtos.UserResponseDto;
import com.ypareo.like.exceptions.BadRequestException;
import com.ypareo.like.exceptions.ResourceNotFoundException;
import com.ypareo.like.mappers.UserMapper;
import com.ypareo.like.models.sql.User;
import com.ypareo.like.repositories.UserRepository;
import com.ypareo.like.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::convertEntityToDto)
                .toList();
    }

    @Override
    public UserResponseDto getUserById(Long id) {
        User existingUser = getExistingUser(id);
        return userMapper.convertEntityToDto(existingUser);
    }

    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        validateUserRequestDto(userRequestDto);
        checkIfEmailExists(userRequestDto);
        checkIfUsernameExists(userRequestDto);
        checkPassword(userRequestDto);
        User newUser = userMapper.convertDtoToEntity(userRequestDto);
        User savedUser = userRepository.save(newUser);
        return userMapper.convertEntityToDto(savedUser);
    }

    @Override
    public UserResponseDto updateUser(Long id, UserRequestDto userRequestDto) {
        User existingUser = getExistingUser(id);
        validateUserRequestDto(userRequestDto);
        checkIfEmailHasChanged(id, userRequestDto);
        checkIfUsernameHasChanged(id, userRequestDto);
        checkIfPasswordHasChanged(id, userRequestDto);
        userMapper.updateEntityFromDto(existingUser, userRequestDto);
        User savedUser = userRepository.save(existingUser);
        return userMapper.convertEntityToDto(savedUser);
    }

    @Override
    public void deleteUser(Long id) {
        User existingUser = getExistingUser(id);
        userRepository.delete(existingUser);
    }

    private User getExistingUser(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User with id " + id + " not found")
        );
    }

    private void validateUserRequestDto(UserRequestDto userRequestDto) {
        if (userRequestDto.getFirstName() == null || userRequestDto.getFirstName().isBlank()) {
            throw new BadRequestException("First name is required");
        }
        if (userRequestDto.getLastName() == null || userRequestDto.getLastName().isBlank()) {
            throw new BadRequestException("Last name is required");
        }
        if (userRequestDto.getEmail() == null || userRequestDto.getEmail().isBlank()) {
            throw new BadRequestException("Email is required");
        }
        if (userRequestDto.getUsername() == null || userRequestDto.getUsername().isBlank()) {
            throw new BadRequestException("Username is required");
        }
    }

    private void checkIfEmailExists(UserRequestDto userRequestDto) {
        if (emailExists(userRequestDto.getEmail())) {
            throw new BadRequestException("Email " + userRequestDto.getEmail() + " is not available");
        }
    }

    private void checkIfUsernameExists(UserRequestDto userRequestDto) {
        if (usernameExists(userRequestDto.getUsername())) {
            throw new BadRequestException("Username " + userRequestDto.getUsername() + " is not available");
        }
    }

    private boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    private boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    private void checkPassword(UserRequestDto userRequestDto) {
        if (userRequestDto.getPassword() == null || userRequestDto.getPassword().isBlank()) {
            throw new BadRequestException("Password is required");
        }
    }

    private void checkIfEmailHasChanged(Long userId, UserRequestDto userRequestDto) {
        User existingUser = getExistingUser(userId);
        if (userRequestDto.getEmail() != null && !existingUser.getEmail().equalsIgnoreCase(userRequestDto.getEmail())) {
            checkIfEmailExists(userRequestDto);
            existingUser.setEmail(userRequestDto.getEmail());
        }
    }

    private void checkIfUsernameHasChanged(Long userId, UserRequestDto userRequestDto) {
        User existingUser = getExistingUser(userId);
        if (!userRequestDto.getUsername().trim().isEmpty() && !existingUser.getUsername().equalsIgnoreCase(userRequestDto.getUsername())) {
            checkIfUsernameExists(userRequestDto);
            existingUser.setUsername(userRequestDto.getUsername());
        }
    }

    private void checkIfPasswordHasChanged(Long userId, UserRequestDto userRequestDto) {
        User existingUser = getExistingUser(userId);
        String oldPassword = existingUser.getPassword();

        if (userRequestDto.getPassword() != null && !oldPassword.equals(userRequestDto.getPassword()) && !userRequestDto.getPassword().trim().isEmpty()) {
            existingUser.setPassword(userRequestDto.getPassword());
        }
        if (userRequestDto.getPassword() == null || oldPassword.equals(userRequestDto.getPassword()) || userRequestDto.getPassword().trim().isEmpty()) {
            existingUser.setPassword(oldPassword);
        }
    }
}
