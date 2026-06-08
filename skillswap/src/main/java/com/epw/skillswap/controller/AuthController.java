package com.epw.skillswap.controller;

import com.epw.skillswap.dto.AuthResponse;
import com.epw.skillswap.dto.LoginRequest;
import com.epw.skillswap.dto.RegisterRequest;
import com.epw.skillswap.entity.User;
import com.epw.skillswap.repository.UserRepository;
import com.epw.skillswap.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepo,
                          PasswordEncoder passwordEncoder,
                          JwtService jwtService) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {

        if (userRepo.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepo.save(user);

        String token = jwtService.generateToken(
                user.getEmail(),
                user.getFirstName(),
                user.getLastName()
        );

        String avatar = (user.getFirstName().charAt(0) + "" + user.getLastName().charAt(0)).toUpperCase();

        return new AuthResponse(
                token,
                user.getEmail(),
                user.getFirstName() + " " + user.getLastName(),
                avatar
        );
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {

        User user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtService.generateToken(
                user.getEmail(),
                user.getFirstName(),
                user.getLastName()
        );

        String avatar = (user.getFirstName().charAt(0) + "" + user.getLastName().charAt(0)).toUpperCase();

        return new AuthResponse(
                token,
                user.getEmail(),
                user.getFirstName() + " " + user.getLastName(),
                avatar
        );
    }
}
