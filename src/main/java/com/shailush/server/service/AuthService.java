package com.shailush.server.service;

import com.shailush.server.config.JwtConfig;
import com.shailush.server.dto.AuthRequest;
import com.shailush.server.dto.AuthResponse;
import com.shailush.server.dto.RegisterRequest;
import com.shailush.server.model.User;
import com.shailush.server.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecretKey secretKey;
    private final long expiration;
    private final JwtConfig jwtConfig;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       SecretKey secretKey,
                       JwtConfig jwtConfig) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.secretKey = secretKey;
        this.jwtConfig = jwtConfig;
        this.expiration = jwtConfig.getExpiration();
    }

    public String authenticate(AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return Jwts.builder()
                .subject(user.getEmail())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey)
                .compact();
    }

    public AuthResponse register(RegisterRequest registerRequest) {
        User user = new User(
                registerRequest.getEmail(),
                passwordEncoder.encode(registerRequest.getPassword())
        );

        User savedUser = userRepository.save(user);
        String jwt = generateToken(savedUser.getEmail());

        return new AuthResponse(
                jwt,
                savedUser.getId(),
                savedUser.getEmail()
        );
    }

    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtConfig.getExpiration()))
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }
}
