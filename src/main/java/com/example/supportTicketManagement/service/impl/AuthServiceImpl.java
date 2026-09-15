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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final Mapper mapper;

    // This method is used to register the user in db
    @Override
    public UserResponseDto register(UserRegisterDto registerRequestDto) {
        User user = new User();
        user.setFirstName(registerRequestDto.getFirstName());
        user.setLastName(registerRequestDto.getLastName());
        user.setEmail(registerRequestDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));

        boolean exists = userRepository.existsByEmail(registerRequestDto.getEmail());

        if(exists){
            throw new EmailAlreadyExistsException("Email already exists");
        }

        Role role = roleRepository.findByRoleName(registerRequestDto.getRoleName())
                .orElseThrow(() -> new RoleNotFoundException("Role is not found"));

        user.getRoles().add(role);

        User savedUser = userRepository.save(user);

        return mapper.mapToUserRegisterDto(savedUser);
    }

    // This method is used to add the role in db
    @Override
    public RoleResponseDto addRole(RoleRequestDto roleRequestDto) {
        boolean exists = roleRepository.existsByRoleName(roleRequestDto.getRoleName());

        if(exists){
            throw new RoleAlreadyExistsException("Role already exists");
        }

        Role role = new Role();
        role.setRoleName(roleRequestDto.getRoleName());

        Role savedRole = roleRepository.save(role);

        return mapper.mapToRoleDto(savedRole);
    }



}
