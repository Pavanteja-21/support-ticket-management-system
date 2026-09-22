package com.example.supportTicketManagement.controller;

import com.example.supportTicketManagement.dto.*;
import com.example.supportTicketManagement.security.JwtService;
import com.example.supportTicketManagement.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final Logger log = LoggerFactory.getLogger(AuthController.class);

    // Only Admin can able to register a new user
    @Operation(summary = "Registration form", description = "Only Admin can able to register a new user like employee or agent")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody @Valid UserRegisterDto requestDto) {
        log.info("Request entered '/api/auth/register', register() method is called");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.register(requestDto));
    }

    // This is used for login
    @Operation(summary = "Login form")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto requestDto) {
        log.info("Request entered '/api/auth/login', login() method is called");
        Authentication authRequest =
                new UsernamePasswordAuthenticationToken(requestDto.getEmail(), requestDto.getPassword());

        Authentication authentication = authenticationManager.authenticate(authRequest);

        String token = jwtService.generateToken(authentication);

        return ResponseEntity.ok(new LoginResponseDto(token));
    }

    // Reset Password
    @Operation(summary = "Reset Password")
    @PostMapping("/reset/password")
    public ResponseEntity<String> resetPassword(@RequestBody @Valid ResetPasswordRequestDto requestDto) {
        log.info("Request entered '/api/auth/resetPassword', resetPassword() method is called");
        authService.resetPassword(requestDto);
        return ResponseEntity.ok("Reset Password Successfully");
    }
}
