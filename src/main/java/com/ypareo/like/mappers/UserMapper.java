package com.ypareo.like.mappers;

import com.ypareo.like.dtos.UserRequestDto;
import com.ypareo.like.dtos.UserResponseDto;
import com.ypareo.like.models.sql.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final RoleMapper roleMapper;

    public User convertDtoToEntity(UserRequestDto userRequestDto) {
        return User.builder()
                .id(userRequestDto.getId())
                .firstName(userRequestDto.getFirstName())
                .lastName(userRequestDto.getLastName())
                .email(userRequestDto.getEmail())
                .username(userRequestDto.getUsername())
                .password(userRequestDto.getPassword())
                .build();
    }

    public UserResponseDto convertEntityToDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .roles(user.getRoles().stream()
                        .map(roleMapper::convertEntityToDto)
                        .toList())
                .build();
    }

    public void updateEntityFromDto(User user, UserRequestDto userRequestDto) {
        user.setFirstName(userRequestDto.getFirstName());
        user.setLastName(userRequestDto.getLastName());
        user.setEmail(userRequestDto.getEmail());
        user.setUsername(userRequestDto.getUsername());
    }
}
