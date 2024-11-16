package com.example.user.controller;

import com.example.user.dto.UserDto;
import com.example.user.entity.UserEntity;
import com.example.user.jwt.JwtProvider;
import com.example.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtProvider jwtProvider;

    @Operation(summary = "Регистрация пользователя", description = "Создание пользователя в системе")
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDto userDto) {
        UserEntity user = userService.register(userDto);
        String token = userService.generateToken(user);
        return ResponseEntity.ok("Bearer " + token);
    }

    @Operation(summary = "Аутентификация пользователя", description = "Пользователя ищет в системе и возвращает jwt токен")
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDto userDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserEntity user = userService.getUserByUsername(userDto.getUsername());

            List<String> roles = user.getRoles().stream()
                    .map(role -> role.getName())
                    .collect(Collectors.toList());

            String token = jwtProvider.createToken(user.getUsername(), roles);
            return ResponseEntity.ok("Bearer " + token);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }
}