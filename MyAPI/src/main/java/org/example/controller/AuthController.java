package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dtos.UserAuthDto;
import org.example.dtos.UserGoogleAuthDto;
import org.example.dtos.UserRegisterDto;
import org.example.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    // Реєстрація нового користувача
    @PostMapping(path = "/register", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> register(@ModelAttribute UserRegisterDto userDto) {
        try {
            userService.registerUser(userDto);
            return ResponseEntity.ok(Map.of("message", "User '" + userDto.getUsername() + "' registered successfully!"));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Registration error: " + e.getMessage()));
        }
    }

    // Логін користувача та отримання JWT токену
    @PostMapping("/login")
    public ResponseEntity<?> login(@ModelAttribute UserAuthDto userDto) {
        try {
            var accessToken = userService.authenticateUser(userDto);
            return ResponseEntity.ok(Map.of("token", "Bearer " + accessToken));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Login error " + e.getMessage()));
        }
    }

    @PostMapping("/google")
    public ResponseEntity<?> googleLogin(@RequestBody UserGoogleAuthDto googleAuthDto) {
        try {
            // Перевірка, чи існує користувач і чи правильні дані
            String token = userService.signInGoogle(googleAuthDto.getToken());
            return ResponseEntity.ok(Map.of("token", "Bearer " + token));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Google login error: " + e.getMessage()));
        }
    }
}