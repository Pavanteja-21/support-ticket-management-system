package com.example.supportTicketManagement.service;

import com.example.supportTicketManagement.dto.RoleResponseDto;
import com.example.supportTicketManagement.dto.UserRegisterDto;
import com.example.supportTicketManagement.dto.UserResponseDto;
import com.example.supportTicketManagement.entity.Role;
import com.example.supportTicketManagement.entity.User;
import com.example.supportTicketManagement.exception.EmailAlreadyExistsException;
import com.example.supportTicketManagement.exception.RoleNotFoundException;
import com.example.supportTicketManagement.repository.RoleRepository;
import com.example.supportTicketManagement.repository.UserRepository;
import com.example.supportTicketManagement.service.impl.AuthServiceImpl;
import com.example.supportTicketManagement.utils.Mapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private Mapper mapper;

    @InjectMocks
    private AuthServiceImpl authService;

    private UserRegisterDto registerDto;
    private Role role;
    private User user;
    private User savedUser;
    private UserResponseDto responseDto;
    private Set<Role> roles = new HashSet<>();
    private Set<RoleResponseDto> roleResponseDtos = new HashSet<>();

    // This is the predefined setup required to execute in all test cases
    @BeforeEach
    void setup() {
        registerDto = new UserRegisterDto();
        registerDto.setFirstName("Deva");
        registerDto.setLastName("Kondapakula");
        registerDto.setEmail("deva@gmail.com");
        registerDto.setPassword("dev123");
        registerDto.setRoleName("ADMIN");

        role = new Role();
        role.setId(1L);
        role.setRoleName("ADMIN");

        roles.add(role);

        user = new User();
        user.setFirstName("Deva");
        user.setLastName("Kondapakula");
        user.setEmail("deva@gmail.com");
        user.setPassword("encodedPassword");
        user.setRoles(roles);

        savedUser = new User();
        savedUser.setEmail("deva@gmail.com");

        roleResponseDtos.add(new RoleResponseDto(1L, "ADMIN"));

        responseDto = new UserResponseDto();
        responseDto.setId(1L);
        responseDto.setFirstName("Deva");
        responseDto.setLastName("Kondapakula");
        responseDto.setActive(true);
        responseDto.setEmail("deva@gmail.com");
        responseDto.setRoles(roleResponseDtos);
    }

    // Test case passes if all requirements satisfies to get register
    @Test
    void shouldAbleToRegister(){
       when(userRepository.existsByEmail(registerDto.getEmail())).thenReturn(false);
       when(roleRepository.findByRoleName(registerDto.getRoleName())).thenReturn(Optional.of(role));
       when(passwordEncoder.encode(registerDto.getPassword())).thenReturn("encodedPassword");
       when(userRepository.save(any(User.class))).thenReturn(savedUser);
       when(mapper.mapToUserRegisterDto(savedUser)).thenReturn(responseDto);

       UserResponseDto result = authService.register(registerDto);

       assertNotNull(result);

       assertEquals(responseDto.getId(), result.getId());
       assertEquals(responseDto.getFirstName(), result.getFirstName());
       assertEquals(responseDto.getLastName(), result.getLastName());
       assertEquals(responseDto.getEmail(), result.getEmail());
       assertEquals(responseDto.getRoles(), result.getRoles());

       verify(userRepository).existsByEmail(registerDto.getEmail());
       verify(roleRepository).findByRoleName(registerDto.getRoleName());
       verify(passwordEncoder).encode(registerDto.getPassword());
       verify(userRepository).save(any(User.class));
       verify(mapper).mapToUserRegisterDto(savedUser);
    }

    // Test case passes if it throws EmailAlreadyExistsException
    @Test
    void shouldThrowEmailAlreadyExistsException(){
        when(userRepository.existsByEmail(registerDto.getEmail())).thenReturn(true);

        EmailAlreadyExistsException ex = assertThrows(
                EmailAlreadyExistsException.class,
                () -> authService.register(registerDto)
        );

        assertEquals("Email already exists", ex.getMessage());
        verify(userRepository).existsByEmail(registerDto.getEmail());
        verify(roleRepository, never()).findByRoleName(registerDto.getRoleName());
        verify(userRepository, never()).save(any(User.class));
    }

    // Test case passes if it throws RoleNotFoundException
    @Test
    void shouldThrowRoleNotFoundException(){
        when(userRepository.existsByEmail(registerDto.getEmail())).thenReturn(false);
        when(roleRepository.findByRoleName(registerDto.getRoleName())).thenReturn(Optional.empty());

        RoleNotFoundException ex = assertThrows(
                RoleNotFoundException.class,
                () -> authService.register(registerDto)
        );

        assertEquals("Role is not found", ex.getMessage());

        verify(userRepository).existsByEmail(registerDto.getEmail());
        verify(roleRepository).findByRoleName(registerDto.getRoleName());
        verify(userRepository, never()).save(any(User.class));
    }

}
