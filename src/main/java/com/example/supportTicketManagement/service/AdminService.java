package com.example.supportTicketManagement.service;

import com.example.supportTicketManagement.dto.UserResponseDto;

import java.util.List;

public interface AdminService {

    List<UserResponseDto> getAllEmployees(int page, int size);

    List<UserResponseDto> getAllAgents(int page, int size);
}
