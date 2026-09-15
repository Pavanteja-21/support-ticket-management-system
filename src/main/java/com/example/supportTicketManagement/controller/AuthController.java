package com.example.supportTicketManagement.controller;

import com.example.supportTicketManagement.dto.*;
import com.example.supportTicketManagement.security.JwtService;
import com.example.supportTicketManagement.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    // Only Admin can able to register a new user
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody @Valid UserRegisterDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.register(requestDto));
    }

    // This is used for login
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto requestDto) {
        Authentication authRequest =
                new UsernamePasswordAuthenticationToken(requestDto.getEmail(), requestDto.getPassword());

        Authentication authentication = authenticationManager.authenticate(authRequest);

        String token = jwtService.generateToken(authentication);

        return ResponseEntity.ok(new LoginResponseDto(token));
    }

    // Only Admin can add roles
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/role")
    public ResponseEntity<RoleResponseDto> addRole(@RequestBody @Valid RoleRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.addRole(requestDto));
    }


}
