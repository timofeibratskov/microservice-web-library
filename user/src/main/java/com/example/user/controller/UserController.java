package com.example.user.controller;

import com.example.user.jwt.JwtProvider;
import com.example.user.request.UserRequest;
import com.example.user.entity.UserEntity;
import com.example.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRequest userRequest) {
        UserEntity user = userService.register(userRequest);


        List<String> roles = user.getRoles().stream()
                .map(role -> role.getName()) // Преобразуем в список строк
                .collect(Collectors.toList());


        String token = jwtProvider.createToken(user.getUsername(), roles);
        return ResponseEntity.ok("Bearer " + token);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserRequest userRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userRequest.getUsername(), userRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);


        UserEntity user = userService.findByUserName(userRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<String> roles = user.getRoles().stream()
                .map(role -> role.getName())
                .collect(Collectors.toList());


        String token = jwtProvider.createToken(user.getUsername(), roles);
        return ResponseEntity.ok("Bearer " + token);
    }
}
