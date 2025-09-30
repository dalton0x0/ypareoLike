package com.ypareo.like.services.implementations;

import com.ypareo.like.dtos.UserRequestDto;
import com.ypareo.like.dtos.UserResponseDto;
import com.ypareo.like.exceptions.BadRequestException;
import com.ypareo.like.exceptions.ResourceNotFoundException;
import com.ypareo.like.mappers.UserMapper;
import com.ypareo.like.models.sql.Role;
import com.ypareo.like.models.sql.User;
import com.ypareo.like.repositories.RoleRepository;
import com.ypareo.like.repositories.UserRepository;
import com.ypareo.like.services.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;

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
        checkRole(userRequestDto);
        User newUser = userMapper.convertDtoToEntity(userRequestDto);
        setRoles(userRequestDto, newUser);
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
        updateRoles(userRequestDto, existingUser);
        User savedUser = userRepository.save(existingUser);
        return userMapper.convertEntityToDto(savedUser);
    }

    @Override
    public void deleteUser(Long id) {
        User existingUser = getExistingUser(id);
        userRepository.delete(existingUser);
    }

    @Override
    public UserResponseDto addRoleToUser(Long userId, Long roleId) {
        User user = getExistingUser(userId);
        Role role = getExistingRole(roleId);
        if (!user.getRoles().contains(role)) {
            user.getRoles().add(role);
            userRepository.save(user);
        }
        return userMapper.convertEntityToDto(user);
    }

    @Override
    public UserResponseDto removeRoleFromUser(Long userId, Long roleId) {
        User user = getExistingUser(userId);
        Role role = getExistingRole(roleId);
        if (user.getRoles().contains(role)) {
            user.getRoles().remove(role);
            userRepository.save(user);
        }
        return userMapper.convertEntityToDto(user);
    }

    private User getExistingUser(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User " + id + " not found")
        );
    }

    private Role getExistingRole(Long roleId) {
        return roleRepository.findById(roleId).orElseThrow(
                () -> new ResourceNotFoundException("Role " + roleId + " not found")
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

    private void checkRole(UserRequestDto userRequestDto) {
        if (userRequestDto.getRoleIds() == null || userRequestDto.getRoleIds().isEmpty()) {
            throw new BadRequestException("At least one role is required");
        }
    }

    private void setRoles(UserRequestDto userRequestDto, User newUser) {
        if (userRequestDto.getRoleIds() != null && !userRequestDto.getRoleIds().isEmpty()) {
            List<Role> roles = roleRepository.findAllById(userRequestDto.getRoleIds());
            newUser.setRoles(roles);
        }
    }

    private void updateRoles(UserRequestDto userRequestDto, User existingUser) {
        if (userRequestDto.getRoleIds() != null) {
            List<Role> roles = roleRepository.findAllById(userRequestDto.getRoleIds());
            existingUser.setRoles(roles);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("This user does not exists")
        );
    }
}
