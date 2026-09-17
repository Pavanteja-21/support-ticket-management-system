package com.example.supportTicketManagement.service.impl;

import com.example.supportTicketManagement.dto.RoleRequestDto;
import com.example.supportTicketManagement.dto.RoleResponseDto;
import com.example.supportTicketManagement.dto.UserResponseDto;
import com.example.supportTicketManagement.entity.Role;
import com.example.supportTicketManagement.entity.User;
import com.example.supportTicketManagement.exception.RoleAlreadyExistsException;
import com.example.supportTicketManagement.repository.RoleRepository;
import com.example.supportTicketManagement.repository.UserRepository;
import com.example.supportTicketManagement.service.AdminService;
import com.example.supportTicketManagement.utils.Mapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final Mapper mapper;

    private final Logger log = LoggerFactory.getLogger(AdminServiceImpl.class);


    // Returns all the Users of employees
    @Override
    public List<UserResponseDto> getAllEmployees(int page, int size) {
        log.info("Inside the AdminService.getAllEmployee (" + page+ ", " + size + ")");

        Pageable pageable = PageRequest.of(page, size);

        List<User> employees = userRepository.findUsersByRole("EMPLOYEE", pageable);

        log.info("End Of AdminService.getAllEmployee ({}, {})", page, size);
        return employees.stream()
                .map(mapper::mapToUserRegisterDto)
                .toList();
    }

    // Returns all the Users of support agents
    @Override
    public List<UserResponseDto> getAllAgents(int page, int size) {
        log.info("Inside the AdminService.getAllAgents (" + page+ ", " + size + ")");

        Pageable pageable = PageRequest.of(page, size);

        List<User> employees = userRepository.findUsersByRole("SUPPORT_AGENT", pageable);

        log.info("End Of AdminService.getAllAgents ({}, {})", page, size);
        return employees.stream()
                .map(mapper::mapToUserRegisterDto)
                .toList();

    }

    // This method is used to add the role in db
    @Override
    public RoleResponseDto addRole(RoleRequestDto roleRequestDto) {
        log.info("Inside AuthService.addRole() method");

        boolean exists = roleRepository.existsByRoleName(roleRequestDto.getRoleName());

        if(exists){
            log.info("Threw RoleNotFoundException as role is exists in the db, in AuthService.addRole()");
            throw new RoleAlreadyExistsException("Role already exists");
        }

        Role role = new Role();
        role.setRoleName(roleRequestDto.getRoleName());

        Role savedRole = roleRepository.save(role);

        log.info("Successfully saved the role in db, end of the AuthService.addRole() method");

        return mapper.mapToRoleDto(savedRole);
    }

}
