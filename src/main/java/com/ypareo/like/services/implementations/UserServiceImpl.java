package com.ypareo.like.services.implementations;

import com.ypareo.like.dtos.UserRequestDto;
import com.ypareo.like.dtos.UserResponseDto;
import com.ypareo.like.exceptions.BadRequestException;
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
        User newUser = userMapper.convertDtoToEntity(userRequestDto);
        User savedUser = userRepository.save(newUser);
        return userMapper.convertEntityToDto(savedUser);
    }

    @Override
    public UserResponseDto updateUser(Long id, UserRequestDto userRequestDto) {
        User existingUser = getExistingUser(id);
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
                () -> new BadRequestException("User with id " + id + " not found")
        );
    }
}
