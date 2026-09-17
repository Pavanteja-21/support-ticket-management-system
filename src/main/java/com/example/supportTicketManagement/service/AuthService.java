package com.example.supportTicketManagement.service;

import com.example.supportTicketManagement.dto.UserRegisterDto;
import com.example.supportTicketManagement.dto.UserResponseDto;
import com.example.supportTicketManagement.dto.RoleRequestDto;
import com.example.supportTicketManagement.dto.RoleResponseDto;

public interface AuthService {
    UserResponseDto register(UserRegisterDto registerRequestDto);
}
