package com.example.user.service;

import com.example.user.request.UserRequest;
import com.example.user.entity.Role;
import com.example.user.entity.UserEntity;
import com.example.user.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
@Transactional
    public UserEntity register(UserRequest userRequest) {
        if (findByUserName(userRequest.getUsername()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        UserEntity user = new UserEntity();
        user.setUsername(userRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        Role userRole = roleService.getUserRole();
        user.setRoles(Collections.singletonList(userRole));

        return userRepository.save(user);
    }
@Transactional(readOnly = true)
    public Optional<UserEntity> findByUserName(String username) {
        return userRepository.findByUsername(username);
    }
}
