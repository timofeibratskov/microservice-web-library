package com.example.user.service;

import com.example.user.entity.Role;
import com.example.user.dto.UserDto;
import com.example.user.entity.UserEntity;
import com.example.user.exceptions.UserAlreadyExistsException;
import com.example.user.jwt.JwtProvider;
import com.example.user.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final JwtProvider jwtProvider;
    private final ModelMapper modelMapper;

    @Transactional
    public UserEntity register(UserDto userDto) {

        if (findByUserName(userDto.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("User with this name already exists! \n Сhoose another name ");
        }

        UserEntity user = modelMapper.map(userDto, UserEntity.class);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRoles(Collections.singletonList(roleService.getUserRole()));

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Optional<UserEntity> findByUserName(String username) {
        return userRepository.findByUsername(username);
    }

    public String generateToken(UserEntity user) {
        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());
        return jwtProvider.createToken(user.getUsername(), roles);
    }

    @Transactional(readOnly = true)
    public UserEntity getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }
}