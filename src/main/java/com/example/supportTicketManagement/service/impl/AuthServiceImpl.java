package com.example.supportTicketManagement.service.impl;

import com.example.supportTicketManagement.dto.*;
import com.example.supportTicketManagement.entity.Role;
import com.example.supportTicketManagement.entity.User;
import com.example.supportTicketManagement.exception.*;
import com.example.supportTicketManagement.repository.RoleRepository;
import com.example.supportTicketManagement.repository.UserRepository;
import com.example.supportTicketManagement.service.AuthService;
import com.example.supportTicketManagement.utils.Mapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final Mapper mapper;

    private final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    // This method is used to register the user in db
    @Override
    public UserResponseDto register(UserRegisterDto registerRequestDto) {
        log.info("Inside AuthService.register() method");

        User user = new User();
        user.setFirstName(registerRequestDto.getFirstName());
        user.setLastName(registerRequestDto.getLastName());
        user.setEmail(registerRequestDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));

        boolean exists = userRepository.existsByEmail(registerRequestDto.getEmail());

        if(exists){
            log.info("Threw EmailAlreadyExistsException as email already exists in AuthService.register()");
            throw new EmailAlreadyExistsException("Email already exists");
        }

        Role role = roleRepository.findByRoleName(registerRequestDto.getRoleName())
                .orElseThrow(() -> {
                    log.info("Threw RoleNotFoundException as role not found in AuthService.register()");
                    return new RoleNotFoundException("Role is not found");
                });

        user.getRoles().add(role);

        User savedUser = userRepository.save(user);

        log.info("Successfully saved the user in db, end of the AuthService.register() method");

        return mapper.mapToUserRegisterDto(savedUser);
    }

    // Used to Reset the Password for Users
    @Override
    public void resetPassword(ResetPasswordRequestDto requestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BadCredentialsException("User is not authenticated");
        }

        String username = authentication.getName();

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> {
                    log.info("Threw UsernameNotFoundException as user is not found in AuthService.resetPassword()");
                    return new UsernameNotFoundException("User is not found");
                });

        if(!passwordEncoder.matches(requestDto.getCurrentPassword(), user.getPassword())){
            log.info("Threw BadCredentialsException as user is not found in AuthService.resetPassword()");
            throw new BadCredentialsException("Bad credentials");
        }

        if(!requestDto.getNewPassword().equals(requestDto.getConfirmNewPassword())){
            log.info("Threw BadCredentialsException as user is not found in AuthService.resetPassword()");
            throw new PasswordMismatchException("Entered Passwords are not match");
        }

        if(passwordEncoder.matches(requestDto.getNewPassword(), user.getPassword())){
            log.info("Threw BadCredentialsException as user is not found in AuthService.resetPassword()");
            throw new BadCredentialsException("New password must be different from the current password");
        }

        user.setPassword(passwordEncoder.encode(requestDto.getNewPassword()));

        userRepository.save(user);
    }

}
