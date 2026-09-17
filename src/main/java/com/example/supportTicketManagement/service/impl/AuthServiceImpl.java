package com.example.supportTicketManagement.service.impl;

import com.example.supportTicketManagement.dto.UserRegisterDto;
import com.example.supportTicketManagement.dto.UserResponseDto;
import com.example.supportTicketManagement.dto.RoleRequestDto;
import com.example.supportTicketManagement.dto.RoleResponseDto;
import com.example.supportTicketManagement.entity.Role;
import com.example.supportTicketManagement.entity.User;
import com.example.supportTicketManagement.exception.EmailAlreadyExistsException;
import com.example.supportTicketManagement.exception.RoleAlreadyExistsException;
import com.example.supportTicketManagement.exception.RoleNotFoundException;
import com.example.supportTicketManagement.repository.RoleRepository;
import com.example.supportTicketManagement.repository.UserRepository;
import com.example.supportTicketManagement.service.AuthService;
import com.example.supportTicketManagement.utils.Mapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

}
