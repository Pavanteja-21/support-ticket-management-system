package com.example.supportTicketManagement.service;

import com.example.supportTicketManagement.dto.RoleRequestDto;
import com.example.supportTicketManagement.dto.RoleResponseDto;
import com.example.supportTicketManagement.dto.UserResponseDto;
import com.example.supportTicketManagement.entity.Role;
import com.example.supportTicketManagement.entity.User;
import com.example.supportTicketManagement.repository.RoleRepository;
import com.example.supportTicketManagement.repository.UserRepository;
import com.example.supportTicketManagement.service.impl.AdminServiceImpl;
import com.example.supportTicketManagement.utils.Mapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private Mapper mapper;

    @InjectMocks
    private AdminServiceImpl adminService;

    // Test can passes if all employees are returned from db
    @Test
    void shouldReturnAllEmployees() {
       int page = 0, size = 10;
       Pageable pageable = PageRequest.of(page, size);

       User user = new User();
       user.setFirstName("Kasi");
       user.setActive(true);

       UserResponseDto userResponseDto = new UserResponseDto();
       userResponseDto.setActive(true);
       userResponseDto.setFirstName("Kasi");

       List<User> usersList = List.of(user);

       when(userRepository.findUsersByRole("EMPLOYEE", pageable)).thenReturn(usersList);
       when(mapper.mapToUserRegisterDto(user)).thenReturn(userResponseDto);

       List<UserResponseDto> result = adminService.getAllEmployees(page, size);

       assertNotNull(result);
       assertEquals(1, result.size());
       assertEquals(userResponseDto.getFirstName(),result.getFirst().getFirstName());
       assertEquals(userResponseDto.isActive(),result.getFirst().isActive());
       assertEquals(userResponseDto,result.getFirst());

       verify(userRepository).findUsersByRole("EMPLOYEE", pageable);
       verify(mapper).mapToUserRegisterDto(user);
    }

    // Test can passes if all agents are returned from db
    @Test
    void shouldReturnAllAgents() {
        int page = 0, size = 10;
        Pageable pageable = PageRequest.of(page, size);

        User user = new User();
        user.setFirstName("Kasi");
        user.setActive(true);

        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setActive(true);
        userResponseDto.setFirstName("Kasi");

        List<User> usersList = List.of(user);

        when(userRepository.findUsersByRole("SUPPORT_AGENT", pageable)).thenReturn(usersList);
        when(mapper.mapToUserRegisterDto(user)).thenReturn(userResponseDto);

        List<UserResponseDto> result = adminService.getAllAgents(page, size);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userResponseDto.getFirstName(),result.getFirst().getFirstName());
        assertEquals(userResponseDto.isActive(),result.getFirst().isActive());
        assertEquals(userResponseDto,result.getFirst());

        verify(userRepository).findUsersByRole("SUPPORT_AGENT", pageable);
        verify(mapper).mapToUserRegisterDto(user);
    }

    // Test can passes if role is added in db
    @Test
    void shouldAddRole() {
        Role role = new Role(1L, "ADMIN");
        RoleRequestDto request = new RoleRequestDto("ADMIN");
        RoleResponseDto roleDto = new RoleResponseDto(1L, "ADMIN");

        when(roleRepository.save(any(Role.class))).thenReturn(role);
        when(mapper.mapToRoleDto(role)).thenReturn(roleDto);

        RoleResponseDto result = adminService.addRole(request);

        assertEquals(roleDto.getRoleId(), result.getRoleId());
        assertEquals(roleDto.getRoleName(), result.getRoleName());

        verify(roleRepository).save(any(Role.class));
        verify(mapper).mapToRoleDto(role);
    }
}
