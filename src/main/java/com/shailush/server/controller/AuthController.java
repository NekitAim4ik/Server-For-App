package com.shailush.server.controller;

import com.shailush.server.dto.AuthRequest;
import com.shailush.server.dto.AuthResponse;
import com.shailush.server.dto.MessageResponse;
import com.shailush.server.dto.RegisterRequest;
import com.shailush.server.model.Profile;
import com.shailush.server.model.User;
import com.shailush.server.repository.ProfileRepository;
import com.shailush.server.repository.UserRepository;
import com.shailush.server.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProfileRepository profileRepository;

    public AuthController(AuthService authService, UserRepository userRepository, PasswordEncoder passwordEncoder,
                          ProfileRepository profileRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.profileRepository = profileRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        // Проверка на существующего пользователя
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already taken!"));
        }

        // Создание нового пользователя
        User user = new User(
                registerRequest.getEmail(),
                passwordEncoder.encode(registerRequest.getPassword())
        );

        Profile profile = new Profile(
                "Аноним",
                "Аноним",
                Date.valueOf(LocalDate.now())
        );

        userRepository.save(user);

        profileRepository.save(profile);

        // Генерация JWT токена
        String jwt = authService.generateToken(user.getEmail());

        return ResponseEntity.ok(new AuthResponse(
                jwt,
                user.getId(),
                user.getEmail()
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            AuthResponse response = authService.authenticate(request);
            return ResponseEntity.ok(Map.of("id", response.getId(), "token", response.getToken()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        }
    }
}